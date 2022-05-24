package project.appcontainer;

import project.appcontainer.api.AppComponent;
import project.appcontainer.api.AppComponentsContainer;
import project.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        checkConfigClass(configClass);
        Method[] methods = configClass.getDeclaredMethods();
        List<Method> orderedAppComponents = new ArrayList<>();

        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparing(method -> method.getAnnotation(AppComponent.class).order()))
                .forEach(method -> {
                    if (method.isAnnotationPresent(AppComponent.class)) {
                        orderedAppComponents.add(method);
                    }
                });

        var configClassInstance = configClass.getDeclaredConstructor().newInstance();

        orderedAppComponents.forEach(orderedAppComponent -> {
            try {
                Object appComponentInstance = this.createAppComponentInstance(
                    configClassInstance,
                    orderedAppComponent
                );
                appComponentsByName.put(
                    orderedAppComponent.getAnnotation(AppComponent.class).name(),
                    appComponentInstance
                );
                appComponents.add(appComponentInstance);
            } catch (InvocationTargetException|IllegalAccessException e) {
                e.printStackTrace(); // TODO: create custom exception
            }
        });
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object createAppComponentInstance(Object rootClassInstance, Method method) throws InvocationTargetException, IllegalAccessException {
        if (method.getParameterCount() == 0) {
            return method.invoke(rootClassInstance);
        }

        List<Object> params = new ArrayList<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Arrays.stream(parameterTypes).forEach(parameterType -> {
            if (this.getAppComponent(parameterType) != null) {
                params.add(this.getAppComponent(parameterType));
            }
        });

        if (params.size() == method.getParameterCount()) {
            return method.invoke(rootClassInstance, params.toArray());
        }
        return null;
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var appComponentOptional = appComponents
            .stream()
            .filter(appComponent -> appComponent.getClass() == componentClass)
            .findFirst();

        if (appComponentOptional.isPresent()) {
            return (C) appComponentOptional.get();
        }
        
        // search among interfaces
        for (Object appComponent : appComponents) {
            var appComponentInterfaces = appComponent.getClass().getInterfaces();
            var appComponentInterfaceOptional = Arrays.stream(appComponentInterfaces)
                .filter(appComponentInterface -> appComponentInterface == componentClass)
                .findFirst();

            if (appComponentInterfaceOptional.isPresent()) {
                return (C) appComponent;
            }
        }

        return null;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
