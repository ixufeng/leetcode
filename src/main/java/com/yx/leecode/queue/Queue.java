package com.yx.leecode.queue;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * @author xufeng
 * Create Date: 2020-04-12 17:24
 **/
public interface Queue<E> extends Collection<E> {

    /**
     * Inserts the specified element into this queue if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * {@code true} upon success and throwing an {@code IllegalStateException}
     * if no space is currently available.
     *
     * @param e
     * @return
     */
    boolean add(E e);


    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to {@link #add}, which can fail to insert an element only
     * by throwing an exception.
     *
     * @param e
     * @return
     */
    boolean offer(E e);

    /**
     * ??????????????????????????????????
     *
     * @return
     * @throws NoSuchElementException if this queue is empty
     */
    E remove();

    /**
     * ?????????????????????????null
     *
     * @return
     */
    E poll();

    /**
     * ???????????????????????????????????
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    E element();

    /**
     * ???????????????????????????????null
     *
     * @return
     */
    E peek();


}
