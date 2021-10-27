package aop.service;

import aop.logging.LoggingInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class Container {
    private Container() {
    }

    public static Object getService(Class<?> interfaceClass) throws Exception {
        if(applicationScope().containsKey(interfaceClass)) {
            InvocationHandler handler = new LoggingInvocationHandler(applicationScope().get(interfaceClass));
            return Proxy.newProxyInstance(Container.class.getClassLoader(),
                    new Class<?>[]{interfaceClass}, handler);
        }

        throw new Exception("Service was not found: " + interfaceClass);
    }

    private static Map<Class<?>, Object> applicationScope() {
        Map<Class<?>, Object> applicationScope = new HashMap<>();
        applicationScope.put(TestLoggingInterface.class, new TestLogging());
        return applicationScope;
    }
}
