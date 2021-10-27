package aop.logging;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LoggingInvocationHandler implements InvocationHandler{
    private static final String LOG_ANNOTATION = "aop.annotation.Log";
    private final Object myClass;

    public LoggingInvocationHandler(Object myClass) {
        this.myClass = myClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Annotation[] annotations = method.getDeclaredAnnotations();
        Arrays.stream(annotations).forEach(annotation -> {
            String annotationName = annotation.annotationType().getName();
            if (annotationName != null && annotationName == LOG_ANNOTATION) {
                System.out.println("invoking method: " + method.getName() + ", params: " + Arrays.toString(args));
            }
        });
        return method.invoke(myClass, args);
    }
}
