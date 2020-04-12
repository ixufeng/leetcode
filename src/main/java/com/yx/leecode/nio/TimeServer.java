package com.yx.leecode.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xufeng
 * Create Date: 2019-11-03 16:08
 **/
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        new Thread(new MultiplexerTimeServer(port)).start();

    }


    /**
     * 时间服务器
     */
    static class MultiplexerTimeServer implements Runnable {

        private Selector selector;
        private ServerSocketChannel serverSocketChannel;
        private volatile boolean stop;

        /**
         * 初始化多路复用器，绑定监听端口
         *
         * @param port
         */
        public MultiplexerTimeServer(int port) {
            try {
                this.selector = Selector.open();
                this.serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.socket().bind(new InetSocketAddress(port));
                //注册到多路复用器上
                serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
                System.out.println("the time server start in port:" + port);
            } catch (Exception e) {
                System.exit(1);
            }
        }

        public void stop() {
            this.stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                try {
                    //阻塞获取
                    this.selector.select(1000);
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    SelectionKey key;
                    while (it.hasNext()) {
                        key = it.next();
                        it.remove();
                        try {
                            handleInput(key);
                        } catch (Exception e) {
                            //处理异常逻辑
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null) {
                                    key.channel().close();
                                }
                            }
                        }
                    }

                } catch (Throwable t) {

                }
            }
            //释放复用器
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 处理接入的请求
         *
         * @param key
         */
        private void handleInput(SelectionKey key) throws IOException {
            if (key.isValid()) {
                //处理新接入的请求
                if (key.isAcceptable()) {
                    //接入一个新的连接
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    //把新接入的连接注册到多路复用器上
                    sc.register(this.selector, SelectionKey.OP_READ);
                }
                //可读事件
                if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    //分配读缓冲区
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);

                    //将channel的数据读取到缓冲区
                    int readBytes = sc.read(readBuffer);
                    if (readBytes > 0) {
                        readBuffer.flip();
                        //从缓冲区读取到数组
                        byte[] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);
                        String body = new String(bytes);
                        System.out.println("the time server receive order : " + body);
                        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
                                ? new Date().toString() : "BAD ORDER";
                        //应答客户端
                        doWrite(sc, currentTime);

                    }
                }
            }
        }

        /**
         * 写会socket
         *
         * @param sc
         * @param response
         */
        private void doWrite(SocketChannel sc, String response) throws IOException {
            if (response != null && response.trim().length() > 0) {
                byte[] bytes = response.getBytes();
                //写缓冲区
                ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                writeBuffer.put(bytes);
                writeBuffer.flip();
                sc.write(writeBuffer);
            }
        }
    }

}
