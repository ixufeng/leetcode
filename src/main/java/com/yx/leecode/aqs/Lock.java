package com.yx.leecode.aqs;

import java.util.concurrent.TimeUnit;

/**
 * 1. 结构灵活且支持多个condition
 * 与使用sync方法和语句所获得的锁定相比，
 * Lock实现提供了更广泛的锁定操作。它们允许更灵活的结构，可能具有完全不同的属性，并且可能支持多个关联的Condition对象
 * 2. 支持共享锁
 * 锁是用于控制多个线程对共享资源的访问的工具。
 * 通常，锁提供对共享资源的独占访问：一次只能有一个线程可以获取该锁，并且对共享资源的所有访问都需要首先获取该锁。但是，某些锁可能允许并发访问共享资源，
 * 例如ReadWriteLock的读取锁。
 * 3.  syn 在获取和释放多个锁时，结构死板
 * 使用 Synchronized方法或语句可以访问与每个对象关联的隐式监视器锁，但是强制所有锁的获取和释放以块结构方式进行：
 * 当多个锁时被获取时，它们必须以相反的顺序释放，并且所有锁必须在获取它们的相同词法范围内释放。
 * 4. 举例说明sync虽然简单，但是缺少灵活性
 * 虽然 sync方法的作用域机制和语句使使用监视器锁定的编程变得容易得多，并且并避免了很多常见的涉及锁定的编程错误，
 * 但是在某些情况下您需要使用以更灵活的方式锁定。例如，用于遍历并发访问的数据结构的某些算法需要使用“交接”或“链锁”：
 * 您取得节点A的锁，然后取得节点B，然后释放A并取得C ，然后释放B并获取D，依此类推。
 * Lock接口的实现通过以下方式允许使用此类技术：允许获取和释放不同范围的锁，并且允许以任何顺序获取和释放多个锁。
 * <p>
 * 随着灵活性的提高，额外的责任也随之增加。缺少块结构锁定将消除  sync方法和语句中发生的锁定的自动释放。在大多数情况下，应使用以下习惯用语：
 *
 *
 * <pre> {@code
 *   Lock l = ...;
 *   l.lock();
 *   try {
 *     // access the resource protected by this lock
 *   } finally {
 *     l.unlock();
 *   }}
 *   </pre>
 */
public interface Lock {

    /**
     * 获取锁，如果无法获取锁，则会休眠当前线程
     */
    void lock();

    /**
     * 支持中断的lock
     *
     * @throws InterruptedException
     */
    void lockInterruptibly() throws InterruptedException;

    /**
     * 尝试获取锁
     *
     * @return
     */
    boolean tryLock();

    /**
     * 带有超时时间的lock
     *
     * @param time
     * @param unit
     * @return
     * @throws InterruptedException
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    /**
     * 释放锁
     */
    void unlock();

    Condition newCondition();
}
