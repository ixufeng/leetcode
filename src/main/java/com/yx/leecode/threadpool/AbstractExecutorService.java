package com.yx.leecode.threadpool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;


public abstract class AbstractExecutorService implements ExecutorService {

    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new FutureTask<T>(runnable, value);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new FutureTask<T>(callable);
    }

    public Future<?> submit(Runnable task) {
        if (task == null) throw new NullPointerException();

        RunnableFuture<Void> ftask = newTaskFor(task, null);
        execute(ftask);
        return ftask;
    }

    public <T> Future<T> submit(Runnable task, T result) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<T> ftask = newTaskFor(task, result);
        execute(ftask);
        return ftask;
    }


    public <T> Future<T> submit(Callable<T> task) {
        if (task == null) throw new NullPointerException();
        RunnableFuture<T> ftask = newTaskFor(task);
        execute(ftask);
        return ftask;
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
            throws InterruptedException {
        if (tasks == null) {
            throw new NullPointerException();
        }
        ArrayList<Future<T>> futures = new ArrayList<>(tasks.size());
        boolean done = false;
        try {
            for (Callable<T> t : tasks) {
                RunnableFuture<T> f = newTaskFor(t);
                futures.add(f);
                execute(f);
            }
            for (Future<T> f : futures) {
                if (!f.isDone()) {
                    try {
                        f.get();
                    } catch (CancellationException | ExecutionException ignore) {
                    }
                }
            }
            done = true;
            return futures;
        } finally {
            if (!done) {
                for (Future<T> future : futures) future.cancel(true);
            }
        }
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
                                         long timeout, TimeUnit unit)
            throws InterruptedException {
        if (tasks == null) {
            throw new NullPointerException();
        }
        long nanos = unit.toNanos(timeout);
        ArrayList<Future<T>> futures = new ArrayList<>(tasks.size());
        boolean done = false;
        try {
            for (Callable<T> t : tasks)
                futures.add(newTaskFor(t));

            final long deadline = System.nanoTime() + nanos;
            final int size = futures.size();

            for (int i = 0; i < size; i++) {
                execute((Runnable) futures.get(i));
                nanos = deadline - System.nanoTime();
                if (nanos <= 0L)
                    return futures;
            }

            for (int i = 0; i < size; i++) {
                Future<T> f = futures.get(i);
                if (!f.isDone()) {
                    if (nanos <= 0L)
                        return futures;
                    try {
                        f.get(nanos, TimeUnit.NANOSECONDS);
                    } catch (CancellationException | ExecutionException ignore) {
                    } catch (TimeoutException toe) {
                        return futures;
                    }
                    nanos = deadline - System.nanoTime();
                }
            }
            done = true;
            return futures;
        } finally {
            if (!done)
                for (Future<T> future : futures) future.cancel(true);
        }
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                           long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
//        return doInvokeAny(tasks, true, unit.toNanos(timeout));
        return null;
    }


    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
//        try {
//            return doInvokeAny(tasks, false, 0);
//        } catch (TimeoutException cannotHappen) {
//            assert false;
//            return null;
//        }
        return null;
    }

//    /**
//     * the main mechanics of invokeAny.
//     */
//    private <T> T doInvokeAny(Collection<? extends Callable<T>> tasks,
//                              boolean timed, long nanos)
//            throws InterruptedException, ExecutionException, TimeoutException {
//        if (tasks == null)
//            throw new NullPointerException();
//        int ntasks = tasks.size();
//        if (ntasks == 0)
//            throw new IllegalArgumentException();
//        ArrayList<Future<T>> futures = new ArrayList<>(ntasks);
//        ExecutorCompletionService<T> ecs =
//                new ExecutorCompletionService<T>(this);
//        try {
//
//            RuntimeException ee = null;
//            final long deadline = timed ? System.nanoTime() + nanos : 0L;
//            Iterator<? extends Callable<T>> it = tasks.iterator();
//
//            // Start one task for sure; the rest incrementally
//            futures.add(ecs.submit(it.next()));
//            --ntasks;
//            int active = 1;
//
//            for (; ; ) {
//                Future<T> f = ecs.poll();
//                if (f == null) {
//                    if (ntasks > 0) {
//                        --ntasks;
//                        futures.add(ecs.submit(it.next()));
//                        ++active;
//                    } else if (active == 0)
//                        break;
//                    else if (timed) {
//                        f = ecs.poll(nanos, TimeUnit.NANOSECONDS);
//                        if (f == null)
//                            throw new TimeoutException();
//                        nanos = deadline - System.nanoTime();
//                    } else
//                        f = ecs.take();
//                }
//                if (f != null) {
//                    --active;
//                    try {
//                        return f.get();
//                    } catch (ExecutionException eex) {
//                        ee = new RuntimeException();
//                    } catch (RuntimeException rex) {
//                        ee = new RuntimeException(rex);
//                    }
//                }
//            }
//
//            if (ee == null)
//                ee = new RuntimeException();
//            throw ee;
//
//        } finally {
//            for (int i = 0, size = futures.size(); i < size; i++)
//                futures.get(i).cancel(true);
//        }
//    }
//

}
