package otus.framework.service;

import otus.framework.helper.ReflectionHelper;
import otus.framework.wrapper.MethodsWrapper;
import otus.framework.wrapper.TestStatistic;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TestRunner {
    private TestStatistic testStatistic;

    public TestRunner() {
        testStatistic = new TestStatistic();
    }

    public void run(Class<?> clazz) {
        Object testClazz = ReflectionHelper.instantiate(clazz);
        MethodsWrapper methodsWrapper = new MethodsWrapper();

        Method[] methodsPublic = testClazz.getClass().getDeclaredMethods();

        Arrays.stream(methodsPublic).forEach(method -> {
            Annotation[] annotations = method.getDeclaredAnnotations();
            Arrays.stream(annotations).forEach(annotation -> {
                methodsWrapper.addMethod(annotation.annotationType().getName(), method.getName());
            });
        });

        testStatistic.setTotalAmountOfTests(methodsWrapper.getTestMethods().size());
        for (String testMethod: methodsWrapper.getTestMethods()) {
            try {
                for (String beforeMethod: methodsWrapper.getBeforeMethods()) {
                    ReflectionHelper.callMethod(testClazz, beforeMethod);
                }

                try {
                    ReflectionHelper.callMethod(testClazz, testMethod);
                    testStatistic.addTestStatus(testMethod, "PASSED");
                } catch (Exception exception) {
                    testStatistic.addTestStatus(testMethod, "FAILED", exception.getMessage());
                }
            } catch (Exception exception) {
                throw exception;
            } finally {
                for (String afterMethod: methodsWrapper.getAfterMethods()) {
                    ReflectionHelper.callMethod(testClazz, afterMethod);
                }
            }

        }
    }

    public TestStatistic getTestStatistic() {
        return testStatistic;
    }
}
