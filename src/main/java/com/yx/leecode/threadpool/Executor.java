package com.yx.leecode.threadpool;

/**
 * @author xufeng
 * Create Date: 2020-03-15 19:54
 * ִ����
 **/
public interface Executor {

    /**
     * �ڽ�����ĳ��ʱ��ִ�и��������
     * ������������һ���µ��̻߳�һ���̳߳��ֻ��ڵ����߳���ִ�У�
     * ������ʵ���߾���
     *
     * @param command
     */
    void execute(Runnable command);
}
