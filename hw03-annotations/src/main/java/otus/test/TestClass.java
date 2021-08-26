package otus.test;

import otus.framework.annotation.After;
import otus.framework.annotation.Before;
import otus.framework.annotation.Test;

public class TestClass {

    @Before
    public void Before1() {
        if (true) {
            System.out.println("Before1");
        }
    }

    @Before
    public void Before2() {
        if (true) {
            System.out.println("Before2");
        }
    }

    @Test
    public void Test1() {
        if (true) {
            System.out.println("Test1 passed");
        }
    }

    @Test
    public void Test2() {
        if (true) {
            System.out.println("Test2 failed");
            throw new RuntimeException("Test2 failed");
        }
    }

    @Test
    public void Test3() {
        if (true) {
            System.out.println("Test3 passed");
        }
    }

    @After
    public void After() {
        if (true) {
            System.out.println("After");
        }
    }
}
