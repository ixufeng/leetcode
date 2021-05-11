package com.yx.leecode.threadpool;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author xufeng
 * Create Date: 2020-03-15 20:06
 * һ�������ṩ������ֹһ�����߶�����񡢲��ṩ����׷�ٷ���������
 **/
public interface ExecutorService extends Executor{

    /**
     * ִ������رգ��ر�ǰִ���Ѿ��ύ�����񣬵����µ����񲻻ᱻ���ܡ�
     * ����Ѿ��رգ�����ò�������κ�Ӱ�죨�����ظ�ִ�ж���������쳣��
     */
    void shutdown();

    /**
     * ����ֹͣ���л�Ծ�����񣬷������ڵȴ��������б�
     * �˷������ܱ�ֹ֤ͣ����������ִ�е��������粻��Ӧ�ն˵���������޷�ֹͣ��
     *
     * @return
     */
    List<Runnable> shutdownNow();

    /**
     * �Ƿ�ֹͣ��
     *
     * @return
     */
    boolean isShutdown();

    /**
     * �Ƿ����������Ѿ���ֹͣ��ɡ�
     * ֻ����ִ�С�shutdown/shutdownNow�� �󣬲��п��ܷ���true
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
     * �ύһ���з���ֵ������
     *
     * @param task
     * @param <T>
     * @return
     */
    <T> Future<T> submit(Callable<T> task);

    /**
     * �ύһ��runnable���񣬲�����һ��future��ִ��get�������ظ�����result
     *
     * @param task
     * @param result
     * @param <T>
     * @return
     */
    <T> Future<T> submit(Runnable task, T result);

    /**
     * �ύһ��runnable���񣬲�����һ��future��ִ��get��������null
     *
     * @param task
     * @return
     */
    Future<?> submit(Runnable task);

    /**
     * ִ�������и���������
     *
     * @param tasks
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
            throws InterruptedException;


    /**
     * ���г�ʱʱ���ִ��
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
     * ִ��һЩ����һ����һ�����񷵻أ���ȡ������δ��ɵ�����
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
     * ���г�ʱʱ���any
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
