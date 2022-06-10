package project.appcontainer.wrapper;

import java.lang.reflect.Method;

public class AppComponentWrapper<T> {
    private Class<?> appComponentType;
    private Method method;
    private Object configClassInstance;
    private int order;
    private String componentName;
    private Object instance;

    public AppComponentWrapper(
        Class<?> appComponentType,
        Method method,
        Object configClassInstance,
        int order,
        String componentName
    ) {
        this.appComponentType = appComponentType;
        this.method = method;
        this.configClassInstance = configClassInstance;
        this.order = order;
        this.componentName = componentName;
    }

    public Class<?> getAppComponentType() {
        return appComponentType;
    }

    public Method getMethod() {
        return method;
    }

    public int getOrder() {
        return order;
    }

    public String getComponentName() {
        return componentName;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Object getConfigClassInstance() {
        return configClassInstance;
    }
}
