package otus;

import otus.framework.wrapper.TestStatistic;
import otus.framework.service.TestRunner;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner();
        if (args.length == 0) {
            System.out.println("Please provide test class name");
            return;
        }

        try {
            Class<?> clazz = Class.forName(args[0]);
            testRunner.run(clazz);
            TestStatistic testStatistic = testRunner.getTestStatistic();
            System.out.println("---- RESULTS ----");
            System.out.println("Total amount of tests: " + testStatistic.getTotalAmountOfTests());
            for (Map.Entry<String, String> entry : testStatistic.getTestsStatus().entrySet()) {
                System.out.println(entry.getKey() + " > " + entry.getValue());
            }
        } catch (ClassNotFoundException exception) {
            System.out.println("Test class does not exists");
            return;
        }
    }
}
