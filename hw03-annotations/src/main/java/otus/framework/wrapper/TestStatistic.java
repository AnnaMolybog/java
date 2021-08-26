package otus.framework.wrapper;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestStatistic {
    private int totalAmountOfTests;
    private Map<String, String> testsStatus = new LinkedHashMap<>();

    public int getTotalAmountOfTests() {
        return totalAmountOfTests;
    }

    public void setTotalAmountOfTests(int totalAmountOfTests) {
        this.totalAmountOfTests = totalAmountOfTests;
    }

    public void addTestStatus(String testName, String status) {
        testsStatus.put(testName, status);
    }

    public void addTestStatus(String testName, String status, String message) {
        testsStatus.put(testName, status + ", message: " + message);
    }

    public Map<String, String> getTestsStatus() {
        return testsStatus;
    }
}
