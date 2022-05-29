package project.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import project.appcontainer.api.AppComponent;
import project.appcontainer.api.AppComponentsContainer;
import project.appcontainer.api.AppComponentsContainerConfig;
import project.appcontainer.api.Qualifier;
import project.appcontainer.helpers.AppComponentOrderComparator;
import project.appcontainer.wrapper.AppComponentWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final List<AppComponentWrapper<?>> appComponents = new ArrayList<>();
    private final Map<String, AppComponentWrapper<?>> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> configClass) throws RuntimeException {
        this.processConfig(Collections.singletonList(configClass));
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> annotated = reflections.get(Scanners.TypesAnnotated.get(AppComponentsContainerConfig.class).asClass());
        this.processConfig(new ArrayList<>(annotated));
    }

    public AppComponentsContainerImpl(Class<?>... configClasses) {
        this.processConfig(Arrays.asList(configClasses));
    }
    
    private void processConfig(List<Class<?>> initialConfigClasses) {
        List<AppComponentWrapper<?>> scannedAppComponents = this.scanConfigClasses(
                sortConfigClasses(initialConfigClasses)
        );
        scannedAppComponents.sort(new AppComponentOrderComparator());

        List<AppComponentWrapper<?>> appComponents = this.instantiateAppComponents(scannedAppComponents);

        appComponents.forEach(appComponent -> {
            this.appComponents.add(appComponent);
            this.appComponentsByName.put(appComponent.getComponentName(), appComponent);
        });
    }

    private List<AppComponentWrapper<?>> scanConfigClasses(List<Class<?>> initialConfigClasses) {
        Map<String, AppComponentWrapper<?>> scannedAppComponents = new HashMap<>();

        initialConfigClasses.forEach(configClass -> {
            checkConfigClass(configClass);
            Method[] configClassDeclaredMethods = configClass.getDeclaredMethods();
            Object configClassInstance = createConfigClassInstance(configClass);

            Arrays.stream(configClassDeclaredMethods).forEach(configClassDeclaredMethod -> {
                if (configClassDeclaredMethod.isAnnotationPresent(AppComponent.class)) {
                    String appComponentName = configClassDeclaredMethod.getAnnotation(AppComponent.class).name();

                    if (scannedAppComponents.containsKey(appComponentName)) {
                        throw new RuntimeException(
                            String.format(
                                "Component name should be unique. Component with name %s already exists. Config class: %s",
                                    appComponentName,
                                configClass.getName()
                            )
                        );
                    }

                    AppComponentWrapper<?> appComponentWrapper = new AppComponentWrapper<>(
                        configClassDeclaredMethod.getReturnType(),
                        configClassDeclaredMethod,
                        configClassInstance,
                        configClassDeclaredMethod.getAnnotation(AppComponent.class).order(),
                        appComponentName
                    );

                    scannedAppComponents.put(appComponentWrapper.getComponentName(), appComponentWrapper);
                };
            });
        });
        
        return new ArrayList<>(scannedAppComponents.values());
    }

    private List<AppComponentWrapper<?>> instantiateAppComponents(List<AppComponentWrapper<?>> scannedAppComponentsWrappers) {
        List<AppComponentWrapper<?>> appComponents = new ArrayList<>();
        scannedAppComponentsWrappers.forEach(scannedAppComponentWrapper -> {
            if (scannedAppComponentWrapper.getInstance() == null) {
                scannedAppComponentWrapper.setInstance(createInstance(scannedAppComponentWrapper, appComponents));
            }
            appComponents.add(scannedAppComponentWrapper);
        });

        return appComponents;
    }

    private Object createInstance(AppComponentWrapper<?> appComponentWrapper, List<AppComponentWrapper<?>> appComponents) {
        List<Object> parametersInstances = this.getParametersInstances(appComponentWrapper, appComponents);
        Object instance;
        
        if (parametersInstances.size() != appComponentWrapper.getMethod().getParameterCount()) {
            throw new RuntimeException(
                String.format(
                    "Object %s cannot be instantiated. expected amount: %d, actual amount: %d. Parameters needs to be qualified",
                    appComponentWrapper.getAppComponentType().getName(),
                    appComponentWrapper.getMethod().getParameterCount(), 
                    parametersInstances.size()
                )
            );
        }

        try {
            if (appComponentWrapper.getMethod().getParameterCount() == 0) {
                instance = appComponentWrapper.getMethod().invoke(appComponentWrapper.getConfigClassInstance());
            } else {
                instance = appComponentWrapper.getMethod().invoke(
                    appComponentWrapper.getConfigClassInstance(),
                    parametersInstances.toArray()
                );
            }

            if (instance == null) {
                throw new RuntimeException(
                    String.format(
                        "Component %s was not created",
                        appComponentWrapper.getComponentName()
                    )
                );
            }
            return instance;
        } catch (InvocationTargetException | IllegalAccessException exception) {
            throw new RuntimeException(
                String.format(
                    "Component %s was not created, error: %s",
                    appComponentWrapper.getComponentName(),
                    exception.getMessage()
                )
            );
        }
    }
    
    private List<Object> getParametersInstances(
        AppComponentWrapper<?> appComponentWrapper,
        List<AppComponentWrapper<?>> appComponents
    ) {
        Parameter[] parameters = appComponentWrapper.getMethod().getParameters();
        List<Object> parametersInstances = new ArrayList<>();

        if (appComponentWrapper.getMethod().getParameterCount() > 0) {
            for (Parameter parameter : parameters) {
                for (AppComponentWrapper<?> appComponent : appComponents) {
                    if (isAppComponentCompatible(appComponent, parameter.getType()) && appComponent.getInstance() != null) {
                        if (parameter.isAnnotationPresent(Qualifier.class)) {
                            String qualifiedClass = parameter.getAnnotation(Qualifier.class).name();
                            if (qualifiedClass.equals(appComponent.getComponentName())) {
                                parametersInstances.add(appComponent.getInstance());
                            }
                        } else {
                            parametersInstances.add(appComponent.getInstance());
                        }
                    }
                }
            }
        }
        
        return parametersInstances;
    }

    private Object createConfigClassInstance(Class<?> configClass) {
        try {
            Object configClassInstance = configClass.getDeclaredConstructors()[0].newInstance();
            if (configClassInstance == null) {
                throw new RuntimeException(
                    String.format(
                        "Config class %s instance was not created",
                        configClass.getName()
                    )
                );
            }
            return configClassInstance;
        } catch (IllegalAccessException|InstantiationException|InvocationTargetException exception) {
            throw new RuntimeException(
                String.format(
                    "Config class %s instance was not created: %s",
                    configClass.getName(),
                    exception.getMessage()
                )
            );
        }
    }

    private Optional<AppComponentWrapper<?>> findAppComponent(Class<?> componentClass) {
        List<AppComponentWrapper<?>> components = new ArrayList<>();
        for (AppComponentWrapper<?> appComponentWrapper : appComponents) {
            if (isAppComponentCompatible(appComponentWrapper, componentClass)) {
                components.add(appComponentWrapper);
            }
        }

        if (components.size() > 1) {
            throw new RuntimeException(
                    String.format(
                            "There several app components with type %s. App component needs to be qualified",
                            componentClass.getName()
                    )
            );
        }

        return Optional.of(components.get(0));
    }

    private List<Class<?>> sortConfigClasses(List<Class<?>> initialConfigClasses) {
        List<Class<?>> orderedConfigs = new ArrayList<>();
        initialConfigClasses.stream()
            .filter(initialConfigClass -> initialConfigClass.isAnnotationPresent(AppComponentsContainerConfig.class))
            .sorted(Comparator.comparing(initialConfigClass -> initialConfigClass.getAnnotation(AppComponentsContainerConfig.class).order()))
            .forEach(orderedConfigs::add);
        return orderedConfigs;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private boolean isAppComponentCompatible(AppComponentWrapper<?> appComponentWrapper, Class<?> searchedClass) {
        return searchedClass.isAssignableFrom(appComponentWrapper.getAppComponentType()) ||
                (appComponentWrapper.getInstance() != null &&
                        searchedClass.isAssignableFrom(appComponentWrapper.getInstance().getClass()));
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        Optional<AppComponentWrapper<?>> appComponent = findAppComponent(componentClass);
        if (appComponent.isPresent() && appComponent.get().getInstance() != null) {
            return (C) appComponent.get().getInstance();
        }

        throw new RuntimeException(String.format("AppComponent %s was not found", componentClass.getName()));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName).getInstance();
        }

        throw new RuntimeException(String.format("AppComponent %s was not found", componentName));
    }
}
