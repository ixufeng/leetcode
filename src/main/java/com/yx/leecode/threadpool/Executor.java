package com.yx.leecode.threadpool;

public interface Executor {

    void execute(Runnable command);
}
