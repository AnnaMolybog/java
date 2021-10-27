package aop.service;

import aop.annotation.Log;

public interface TestLoggingInterface {
    @Log
    void calculation(int param);
}
