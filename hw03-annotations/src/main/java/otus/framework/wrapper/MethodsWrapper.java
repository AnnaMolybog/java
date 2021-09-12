package otus.framework.wrapper;

import otus.framework.helper.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MethodsWrapper {
    private HashMap<String, List<String>> methods = new HashMap<>();

    public MethodsWrapper() {
        methods.put(Constants.TEST_ANNOTATION, new ArrayList<>());
        methods.put(Constants.BEFORE_ANNOTATION, new ArrayList<>());
        methods.put(Constants.AFTER_ANNOTATION, new ArrayList<>());
    }

    public void addMethod(String annotationName, String testMethod) {
        if (methods.get(annotationName) != null) {
            methods.get(annotationName).add(testMethod);
        }
    }

    public List<String> getTestMethods() {
        return methods.get(Constants.TEST_ANNOTATION);
    }

    public List<String> getBeforeMethods() {
        return methods.get(Constants.BEFORE_ANNOTATION);
    }

    public List<String> getAfterMethods() {
        return methods.get(Constants.AFTER_ANNOTATION);
    }
}
