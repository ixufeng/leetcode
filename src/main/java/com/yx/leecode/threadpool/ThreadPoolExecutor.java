package com.yx.leecode.threadpool;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xufeng
 * Create Date: 2020-03-15 23:55
 **/
public class ThreadPoolExecutor extends AbstractExecutorService {
    private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();

    /**
     * �̳߳�ȫ����
     */
    private final ReentrantLock mainLock = new ReentrantLock();

    private final Condition termination = mainLock.newCondition();

    private static final boolean ONLY_ONE = true;

    private long completedTaskCount;

    /**
     * ������Ա����
     **/
    private volatile int corePoolSize;
    private volatile int maximumPoolSize;
    private final BlockingQueue<Runnable> workQueue;
    private volatile long keepAliveTime;
    private volatile ThreadFactory threadFactory;
    private volatile RejectedExecutionHandler handler;
    private final AccessControlContext acc;


    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING = -1 << COUNT_BITS;
    private static final int SHUTDOWN = 0 << COUNT_BITS;
    private static final int STOP = 1 << COUNT_BITS;
    private static final int TIDYING = 2 << COUNT_BITS;
    private static final int TERMINATED = 3 << COUNT_BITS;

    private volatile boolean allowCoreThreadTimeOut;

    private final HashSet<Worker> workers = new HashSet<>();
    /**
     * Tracks largest attained pool size. Accessed only under
     * mainLock.
     */
    private int largestPoolSize;


    private static final RuntimePermission shutdownPerm =
            new RuntimePermission("modifyThread");

    private boolean compareAndIncrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect + 1);
    }


    // Packing and unpacking ctl
    private static int runStateOf(int c) {
        return c & ~CAPACITY;
    }

    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }

    private static boolean isRunning(int c) {
        return c < SHUTDOWN;
    }

    private static boolean runStateLessThan(int c, int s) {
        return c < s;
    }

    private static boolean runStateAtLeast(int c, int s) {
        return c >= s;
    }

    /**
     * �������캯��
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactory
     * @param handler
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {

        if (corePoolSize < 0 || maximumPoolSize <= 0 || maximumPoolSize < corePoolSize || keepAliveTime < 0) {
            throw new IllegalArgumentException();
        }

        if (workQueue == null || threadFactory == null || handler == null) {
            throw new NullPointerException();
        }
        //todo ��ȫ����
        this.acc = System.getSecurityManager() == null ? null : AccessController.getContext();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }

    private void checkShutdownAccess() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPermission(shutdownPerm);
            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                for (Worker w : workers) {
                    //todo check security
                    security.checkAccess(w.thread);
                }
            } finally {
                mainLock.unlock();
            }
        }
    }

    /**
     * cas �޸ĵ�ǰ������״̬
     *
     * @param targetState
     */
    private void advanceRunState(int targetState) {
        for (; ; ) {
            int c = ctl.get();
            if (runStateAtLeast(c, targetState) ||
                    ctl.compareAndSet(c, ctlOf(targetState, workerCountOf(c))))
                break;
        }
    }

    @Override
    public void shutdown() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            checkShutdownAccess();
            //�޸�״̬
            advanceRunState(SHUTDOWN);
            //�����ж������߳�
            interruptIdleWorkers(false);
            // hook for ScheduledThreadPoolExecutor
            onShutdown();
        } finally {
            mainLock.unlock();
        }
        tryTerminate();

    }

    /**
     * Interrupts all threads, even if active. Ignores SecurityExceptions
     * (in which case some threads may remain uninterrupted).
     */
    private void interruptWorkers() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            for (Worker w : workers) {
                w.interruptIfStarted();
            }

        } finally {
            mainLock.unlock();
        }
    }

    /**
     * Drains the task queue into a new list, normally using
     * drainTo. But if the queue is a DelayQueue or any other kind of
     * queue for which poll or drainTo may fail to remove some
     * elements, it deletes them one by one.
     */
    private List<Runnable> drainQueue() {
        BlockingQueue<Runnable> q = workQueue;
        ArrayList<Runnable> taskList = new ArrayList<>();
        q.drainTo(taskList);
        if (!q.isEmpty()) {
            for (Runnable r : q.toArray(new Runnable[0])) {
                if (q.remove(r))
                    taskList.add(r);
            }
        }
        return taskList;
    }

    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> tasks;
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            checkShutdownAccess();
            advanceRunState(STOP);
            interruptWorkers();
            tasks = drainQueue();
        } finally {
            mainLock.unlock();
        }
        tryTerminate();
        return tasks;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            for (; ; ) {
                if (runStateAtLeast(ctl.get(), TERMINATED))
                    return true;
                if (nanos <= 0)
                    return false;
                nanos = termination.awaitNanos(nanos);
            }
        } finally {
            mainLock.unlock();
        }
    }


    void onShutdown() {
    }

    @Override
    public void execute(Runnable command) {
        if (command == null)
            throw new NullPointerException();
        int c = ctl.get();
        //1. �����߳�����С�ں����߳�����,������������worker����������
        if (workerCountOf(c) < corePoolSize) {
            //���������̳߳ɹ������
            if (addWorker(command, true)) {
                return;
            }
            c = ctl.get();
        }
        //2. ����worker�����������ޣ�ѡ������ж�
        if (isRunning(c) && workQueue.offer(command)) {
            int recheck = ctl.get();
            //�ٴμ�鴦�ڲ�����״̬����ɾ������ɹ�
            if (!isRunning(recheck) && remove(command)) {
                reject(command);
            } else if (workerCountOf(recheck) == 0) {
                //�������worker�Ƿ��Ѿ�����
                //���û����һ����ļ�⣬���������ռ�����к�����worker���������´˸ռ��������û��workerִ��
                addWorker(null, false);
            } else {
                //�����else��˵����task�ᱻ����workerִ��
                //ignore
            }

            //3. ����������Ǻ���worker
        } else if (!addWorker(command, false)) {
            reject(command);
        }

    }

    /**
     * CoreWorker
     * NonCoreWorker
     * SupplyWorker
     * ���ǰ� firstTask==null, core == false ��worker��Ϊ {SupplyWorker}(����worker)
     * ����������ᴴ��SupplyWorker
     * 1. ����ռ�����У���������worker�Ѿ�����
     * 2. һ��worker���жϽ���ʱ
     *
     * @param firstTask
     * @param core
     * @return
     */
    private boolean addWorker(Runnable firstTask, boolean core) {
        retry:
        for (; ; ) {
            int c = ctl.get();
            int rs = runStateOf(c);

            // Check if queue empty only if necessary.

            //�ر�״̬����������worker��������shutdown�£�����һ������worker���������������������
            if (rs >= SHUTDOWN &&
                    !(rs == SHUTDOWN && firstTask == null && !workQueue.isEmpty())) {
                return false;
            }
            //case
            for (; ; ) {
                int wc = workerCountOf(c);
                //����workerSize
                if (wc >= CAPACITY || wc >= (core ? corePoolSize : maximumPoolSize)) {
                    return false;
                }

                //wc+1�ɹ�
                if (compareAndIncrementWorkerCount(c)) {
                    break retry;
                }
                //wc+1ʧ�ܣ������»�ȡ�̳߳�״̬�����״̬�仯������check
                c = ctl.get();  // Re-read ctl
                if (runStateOf(c) != rs) {
                    continue retry;
                }

            }
        }
        //��ʼ����worker
        boolean workerStarted = false;
        boolean workerAdded = false;
        Worker w = null;
        try {
            w = new Worker(firstTask);
            final Thread t = w.thread;
            if (t != null) {
                //����ջ�����á���߷���Ч��
                final ReentrantLock mainLock = this.mainLock;
                //�̳߳�ȫ����
                mainLock.lock();
                try {
                    //ȫ������check�̳߳ص�״̬
                    int rs = runStateOf(ctl.get());
                    //��������״̬
                    //����shutdown״̬-> 1. ���������к�û�д���worker
                    if (rs < SHUTDOWN || (rs == SHUTDOWN && firstTask == null)) {
                        // precheck that t is startable
                        if (t.isAlive()) {
                            throw new IllegalThreadStateException();
                        }
                        workers.add(w);
                        int s = workers.size();
                        if (s > largestPoolSize) {
                            largestPoolSize = s;
                        }
                        workerAdded = true;
                    }
                } finally {
                    mainLock.unlock();
                }
                if (workerAdded) {
                    t.start();
                    workerStarted = true;
                }
            }
        } finally {
            //����workerʧ��
            if (!workerStarted) {
                addWorkerFailed(w);
            }

        }
        return workerStarted;
    }

    /**
     * ����workerʧ��
     * 1. ����ɾ��worker
     * 2. ����worker��¼������
     * 3. ���Թر�
     *
     * @param w
     */
    private void addWorkerFailed(Worker w) {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            if (w != null) {
                workers.remove(w);
            }
            decrementWorkerCount();
            tryTerminate();
        } finally {
            mainLock.unlock();
        }
    }

    /**
     * ������ֹ������ state is shutdown and queue is empty else do nothing
     * or state is stop (this state queue is empty inevitably)
     */
    final void tryTerminate() {
        for (; ; ) {
            int c = ctl.get();
            if (isRunning(c) ||
                    runStateAtLeast(c, TIDYING) ||
                    (runStateOf(c) == SHUTDOWN && !workQueue.isEmpty()))
                return;

            //��������ܵ�״̬ �� (shutdown && queue is empty) || stop
            //todo  why do this
            if (workerCountOf(c) != 0) {
                // Eligible to terminate
                interruptIdleWorkers(ONLY_ONE);
                return;
            }

            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
                    try {
                        terminated();
                    } finally {
                        ctl.set(ctlOf(TERMINATED, 0));
                        termination.signalAll();
                    }
                    return;
                }
            } finally {
                mainLock.unlock();
            }
            // else retry on failed CAS
        }
    }

    private final class Worker
            extends AbstractQueuedSynchronizer
            implements Runnable {

        final Thread thread;
        /**
         * ��ʼ���񣬿���Ϊnull
         */
        Runnable firstTask;
        /**
         * ÿ���̵߳���������
         */
        volatile long completedTasks;

        /**
         * Creates with given first task and thread from ThreadFactory.
         *
         * @param firstTask the first task (null if none)
         */
        Worker(Runnable firstTask) {
            setState(-1); // inhibit interrupts until runWorker
            this.firstTask = firstTask;
            this.thread = getThreadFactory().newThread(this);
        }

        /**
         * Delegates main run loop to outer runWorker
         */
        public void run() {
            runWorker(this);
        }

        // Lock methods
        //
        // The value 0 represents the unlocked state.
        // The value 1 represents the locked state.
        protected boolean isHeldExclusively() {
            return getState() != 0;
        }

        protected boolean tryAcquire(int unused) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        protected boolean tryRelease(int unused) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        public void lock() {
            acquire(1);
        }

        public boolean tryLock() {
            return tryAcquire(1);
        }

        public void unlock() {
            release(1);
        }

        public boolean isLocked() {
            return isHeldExclusively();
        }

        void interruptIfStarted() {
            Thread t;
            if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
                try {
                    t.interrupt();
                } catch (SecurityException ignore) {
                }
            }
        }
    }

    private boolean compareAndDecrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect - 1);
    }

    private void decrementWorkerCount() {
        do {
        } while (!compareAndDecrementWorkerCount(ctl.get()));
    }

    /**
     * ��������л�ȡ����
     * �����ȡ����Ϊnull����ǰworker������
     *
     * @return
     */
    private Runnable getTask() {
        // Did the last poll() time out?
        boolean timedOut = false;

        for (; ; ) {
            int c = ctl.get();
            int rs = runStateOf(c);

            //�˳�worker
            //stop
            //shutdown ״̬�����������Ϊ��
            if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
                decrementWorkerCount();
                return null;
            }

            int wc = workerCountOf(c);

            // Are workers subject to culling?
            boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;

            //todo ʲôʱ�� wc > maximum����
            //��ʱ������worker��������ʱ�򣬿���ֱ�ӷ���null���˳�worker��
            if ((wc > maximumPoolSize || (timed && timedOut))
                    && (wc > 1 || workQueue.isEmpty())) {
                if (compareAndDecrementWorkerCount(c))
                    return null;
                continue;
            }

            try {
                Runnable r = timed ?
                        workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                        workQueue.take();
                if (r != null)
                    return r;
                timedOut = true;
            } catch (InterruptedException retry) {
                timedOut = false;
            }
        }
    }

    final void runWorker(Worker w) {
        Thread wt = Thread.currentThread();
        Runnable task = w.firstTask;
        w.firstTask = null;
        //�ͷ�����Ϊ�˿��Ա��ж�
        w.unlock();
        boolean completedAbruptly = true;
        try {
            while (task != null || (task = getTask()) != null) {
                //ִ������Ĺ����У��̲߳������ж�
                w.lock();

                if ((runStateAtLeast(ctl.get(), STOP) ||
                        (Thread.interrupted() &&
                                runStateAtLeast(ctl.get(), STOP))) &&
                        !wt.isInterrupted())
                    wt.interrupt();
                try {
                    beforeExecute(wt, task);
                    Throwable thrown = null;
                    try {
                        task.run();
                    } catch (RuntimeException x) {
                        thrown = x;
                        throw x;
                    } catch (Error x) {
                        thrown = x;
                        throw x;
                    } catch (Throwable x) {
                        thrown = x;
                        throw new Error(x);
                    } finally {
                        afterExecute(task, thrown);
                    }
                } finally {
                    task = null;
                    w.completedTasks++;
                    w.unlock();
                }
            }
            //�߳�������ֹ��timeout��
            completedAbruptly = false;
        } finally {
            //ΪʲôdecrementWorkerCount��ͳһ��processWorkerExit��ִ�У�������getTask()��ִ�У���
            //�²�1�������ܿ���޸�wc
            processWorkerExit(w, completedAbruptly);
        }
    }

    private void interruptIdleWorkers(boolean onlyOne) {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            for (Worker w : workers) {
                Thread t = w.thread;
                if (!t.isInterrupted() && w.tryLock()) {
                    try {
                        t.interrupt();
                    } catch (SecurityException ignore) {
                    } finally {
                        w.unlock();
                    }
                }
                if (onlyOne)
                    break;
            }
        } finally {
            mainLock.unlock();
        }
    }

    /**
     * ����worker���˳���worker������
     *
     * @param w
     * @param completedAbruptly
     */
    private void processWorkerExit(Worker w, boolean completedAbruptly) {
        if (completedAbruptly) // If abrupt, then workerCount wasn't adjusted
            decrementWorkerCount();

        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            this.completedTaskCount += w.completedTasks;
            workers.remove(w);
        } finally {
            mainLock.unlock();
        }

        tryTerminate();

        int c = ctl.get();
        //ÿ��worker����������Ҫȷ����������Ȼ�����񣬵���worker�Ѿ�ȫ��������������ᷢ��
        if (runStateLessThan(c, STOP)) {
            if (!completedAbruptly) {
                int min = allowCoreThreadTimeOut ? 0 : corePoolSize;
                if (min == 0 && !workQueue.isEmpty()) {
                    min = 1;
                }
                if (workerCountOf(c) >= min)
                    return; // replacement not needed
            }
            addWorker(null, false);
        }
    }


    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    /**
     * Invokes the rejected execution handler for the given command.
     * Package-protected for use by ScheduledThreadPoolExecutor.
     */
    final void reject(Runnable command) {
        handler.rejectedExecution(command, this);
    }

    public boolean remove(Runnable task) {
        boolean removed = workQueue.remove(task);
        tryTerminate(); // In case SHUTDOWN and now empty
        return removed;
    }

    //ִ��ǰ����
    protected void beforeExecute(Thread t, Runnable r) {
    }

    //ִ�к���
    protected void afterExecute(Runnable r, Throwable t) {
    }

    //��ֹ����
    protected void terminated() {
    }

    public void allowCoreThreadTimeOut(boolean value) {
        if (value && keepAliveTime <= 0) {
            throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
        }
        if (value != allowCoreThreadTimeOut) {
            allowCoreThreadTimeOut = value;
            if (value)
                interruptIdleWorkers(false);
        }
    }

    /**
     * Returns the approximate total number of tasks that have ever been
     * scheduled for execution. Because the states of tasks and
     * threads may change dynamically during computation, the returned
     * value is only an approximation.
     *
     * @return the number of tasks
     */
    public long getTaskCount() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            long n = completedTaskCount;
            for (Worker w : workers) {
                n += w.completedTasks;
                if (w.isLocked())
                    ++n;
            }
            return n + workQueue.size();
        } finally {
            mainLock.unlock();
        }
    }

    public long getCompletedTaskCount() {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            long n = completedTaskCount;
            for (Worker w : workers)
                n += w.completedTasks;
            return n;
        } finally {
            mainLock.unlock();
        }
    }

    public static class AbortPolicy implements RejectedExecutionHandler {
        /**
         * Creates an {@code AbortPolicy}.
         */
        public AbortPolicy() {
        }

        /**
         * Always throws RejectedExecutionException.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         * @throws RejectedExecutionException always
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    e.toString());
        }
    }
}
