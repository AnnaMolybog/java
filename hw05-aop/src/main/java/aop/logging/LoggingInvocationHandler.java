package aop.logging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class LoggingInvocationHandler implements InvocationHandler{
    private final Object myClass;
    private List<String> methodsToLog;

    public LoggingInvocationHandler(Object myClass, List<String> methodsToLog) {
        this.myClass = myClass;
        this.methodsToLog = methodsToLog;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (methodsToLog.contains(method.getName())) {
            System.out.println("invoking method: " + method.getName() + ", params: " + Arrays.toString(args));
        }

        return method.invoke(myClass, args);
    }
}
