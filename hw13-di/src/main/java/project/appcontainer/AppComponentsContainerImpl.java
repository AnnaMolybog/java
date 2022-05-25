package project.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import project.appcontainer.api.AppComponent;
import project.appcontainer.api.AppComponentsContainer;
import project.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws Exception {
        this.init(Collections.singletonList(initialConfigClass));
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) throws Exception {
        this.init(Arrays.asList(initialConfigClasses));
    }

    public AppComponentsContainerImpl(String packageName) throws Exception {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> annotated = reflections.get(Scanners.TypesAnnotated.get(AppComponentsContainerConfig.class).asClass());
        this.init(new ArrayList<>(annotated));
    }
    
    private void init(List<Class<?>> initialConfigClasses) throws Exception {
        for (Class<?> configClass : this.sortConfigClasses(initialConfigClasses)) {
            processConfig(configClass);
        }
    }

    private void processConfig(Class<?> configClass) throws Exception {
        checkConfigClass(configClass);
        var configClassInstance = configClass.getDeclaredConstructor().newInstance();

        this.sortAppComponents(configClass.getDeclaredMethods()).forEach(orderedAppComponent -> {
            Object appComponentInstance = null;
            try {
                appComponentInstance = this.createAppComponentInstance(
                    configClassInstance,
                    orderedAppComponent
                );
            } catch (InvocationTargetException|IllegalAccessException e) {
                e.printStackTrace();
            }

            appComponentsByName.put(
                orderedAppComponent.getAnnotation(AppComponent.class).name(),
                appComponentInstance
            );
            
            var existentAppComponent = this.getAppComponent(appComponentInstance.getClass());
            if (existentAppComponent != null ) {
                appComponents.remove(existentAppComponent);
            }
            
            appComponents.add(appComponentInstance);
        });
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
    
    private List<Class<?>> sortConfigClasses(List<Class<?>> initialConfigClasses) {
        List<Class<?>> orderedConfigs = new ArrayList<>();
        initialConfigClasses.stream()
            .filter(initialConfigClass -> initialConfigClass.isAnnotationPresent(AppComponentsContainerConfig.class))
            .sorted(Comparator.comparing(initialConfigClass -> initialConfigClass.getAnnotation(AppComponentsContainerConfig.class).order()))
            .forEach(initialConfigClass -> {
                if (initialConfigClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
                    orderedConfigs.add(initialConfigClass);
                }
            });
        return orderedConfigs;
    }

    private List<Method> sortAppComponents(Method... methods) {
        List<Method> orderedAppComponents = new ArrayList<>();

        Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(AppComponent.class))
            .sorted(Comparator.comparing(method -> method.getAnnotation(AppComponent.class).order()))
            .forEach(method -> {
                if (method.isAnnotationPresent(AppComponent.class)) {
                    orderedAppComponents.add(method);
                }
            });
        
        return orderedAppComponents;
    }

    private Object createAppComponentInstance(Object rootClassInstance, Method method) throws InvocationTargetException, IllegalAccessException {
        if (method.getParameterCount() == 0) {
            return method.invoke(rootClassInstance);
        }

        List<Object> params = new ArrayList<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Arrays.stream(parameterTypes).forEach(parameterType -> {
            try {
                if (this.getAppComponent(parameterType) != null) {
                    params.add(this.getAppComponent(parameterType));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
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
    public <C> C getAppComponent(String componentName) throws Exception {
        if (!appComponentsByName.containsKey(componentName)) {
            throw new Exception("Component with name: " + componentName + " was not found");
        }
        
        return (C) appComponentsByName.get(componentName);
    }
}
