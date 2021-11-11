package aop.service;

import aop.annotation.Log;

public class TestLogging implements TestLoggingInterface {
    @Override
    @Log
    public void calculation(int param) {}
}
