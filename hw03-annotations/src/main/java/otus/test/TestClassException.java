package otus.test;

import otus.framework.annotation.After;
import otus.framework.annotation.Before;
import otus.framework.annotation.Test;

public class TestClassException {
    @Before
    public void Before() {
        if (true) {
            throw new RuntimeException("Before method throws exception");
        }
    }

    @Test
    public void Test() {
        if (true) {
            System.out.println("Never called");
        }
    }


    @After
    public void After() {
        if (true) {
            System.out.println("After method is called anyway");
        }
    }
}
