package aop.service;

import aop.annotation.Log;
import aop.logging.LoggingInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class Container {
    private static Map<Class<?>, Object> applicationScope = new HashMap<>();
    private static Map<Class<?>, List<String>> loggingScope = new HashMap<>();
    private Container() {
    }

    public static void init() {
        applicationScope.put(TestLoggingInterface.class, new TestLogging());

        for(Map.Entry<Class<?>, Object> entry : applicationScope.entrySet()) {
            Object object = entry.getValue();
            Method[] methodsPublic = object.getClass().getDeclaredMethods();
            List<String> methodsToLog = new ArrayList<>();
            Arrays.stream(methodsPublic).forEach(method -> {
                Annotation[] annotations = method.getDeclaredAnnotations();
                Arrays.stream(annotations).forEach(annotation -> {
                    if (annotation.annotationType().equals(Log.class)) {
                        methodsToLog.add(method.getName());
                    }
                });
            });

            loggingScope.put(entry.getKey(), methodsToLog);
        }
    }

    public static Object getService(Class<?> interfaceClass) throws Exception {
        if (!applicationScope.containsKey(interfaceClass)) {
            throw new Exception("Service was not found: " + interfaceClass);
        }

        if (loggingScope.containsKey(interfaceClass)) {
            InvocationHandler handler = new LoggingInvocationHandler(
                applicationScope.get(interfaceClass),
                loggingScope.get(interfaceClass)
            );
            return Proxy.newProxyInstance(Container.class.getClassLoader(),
                    new Class<?>[]{interfaceClass}, handler);
        }

        return applicationScope.get(interfaceClass);
    }
}
