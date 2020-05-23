package com.yx.leecode.aqs;

/**
 * @author xufeng
 * Create Date: 2020-04-14 23:29
 *
 **/
public abstract class AbstractOwnableSynchronizer {

    protected AbstractOwnableSynchronizer() {
    }


    private transient Thread exclusiveOwnerThread;


    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }
}
