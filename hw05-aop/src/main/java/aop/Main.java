package aop;

import aop.service.Container;
import aop.service.TestLoggingInterface;

public class Main {
    public static void main(String[] args) throws Exception {
        TestLoggingInterface testLogging = (TestLoggingInterface) Container.getService(TestLoggingInterface.class);
        testLogging.calculation(6);
    }
}
