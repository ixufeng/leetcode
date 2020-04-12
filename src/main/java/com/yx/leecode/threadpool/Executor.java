package com.yx.leecode.threadpool;

/**
 * @author xufeng
 * Create Date: 2020-03-15 19:54
 * 执行器
 **/
public interface Executor {

    /**
     * 在将来的某个时间执行给定的命令。
     * 这个命令可能在一个新的线程或一个线程池又或在调用线程中执行，
     * 具体由实现者决定
     *
     * @param command
     */
    void execute(Runnable command);
}
