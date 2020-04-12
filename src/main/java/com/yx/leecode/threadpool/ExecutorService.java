package com.yx.leecode.threadpool;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author xufeng
 * Create Date: 2020-03-15 20:06
 * 一个可以提供管理、终止一个或者多个任务、并提供任务追踪方法的容器
 **/
public interface ExecutorService extends Executor{

    /**
     * 执行有序关闭，关闭前执行已经提交的任务，但是新的任务不会被接受。
     * 如果已经关闭，则调用不会产生任何影响（可以重复执行而不会产生异常）
     */
    void shutdown();

    /**
     * 尝试停止所有活跃的任务，返回正在等待的任务列表
     * 此方法不能保证停止掉所有正在执行的任务，例如不响应终端的任务可能无法停止。
     *
     * @return
     */
    List<Runnable> shutdownNow();

    /**
     * 是否被停止过
     *
     * @return
     */
    boolean isShutdown();

    /**
     * 是否所有任务已经被停止完成。
     * 只有在执行「shutdown/shutdownNow」 后，才有可能返回true
     *
     * @return
     */
    boolean isTerminated();

    /**
     * Blocks until all tasks have completed execution after a shutdown
     * request, or the timeout occurs, or the current thread is
     * interrupted, whichever happens first.
     *
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    boolean awaitTermination(long timeout, TimeUnit unit)
            throws InterruptedException;

    /**
     * 提交一个有返回值的任务
     *
     * @param task
     * @param <T>
     * @return
     */
    <T> Future<T> submit(Callable<T> task);

    /**
     * 提交一个runnable任务，并返回一个future，执行get方法返回给定的result
     *
     * @param task
     * @param result
     * @param <T>
     * @return
     */
    <T> Future<T> submit(Runnable task, T result);

    /**
     * 提交一个runnable任务，并返回一个future，执行get方法返回null
     *
     * @param task
     * @return
     */
    Future<?> submit(Runnable task);

    /**
     * 执行完所有给定的任务
     *
     * @param tasks
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
            throws InterruptedException;


    /**
     * 带有超时时间的执行
     *
     * @param tasks
     * @param timeout
     * @param unit
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                  long timeout, TimeUnit unit)
            throws InterruptedException;

    /**
     * 执行一些任务，一旦有一个任务返回，则取消其他未完成的任务
     *
     * @param tasks
     * @param <T>
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    <T> T invokeAny(Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException;

    /**
     * 带有超时时间的any
     *
     * @param tasks
     * @param timeout
     * @param unit
     * @param <T>
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                    long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException;

}
