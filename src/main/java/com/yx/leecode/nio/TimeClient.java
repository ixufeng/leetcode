package com.yx.leecode.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xufeng
 * Create Date: 2019-11-03 17:08
 **/
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        String host = "127.0.0.1";

        new Thread(new TimeClientHandle(host, port)).start();

    }

    static class TimeClientHandle implements Runnable {
        private String host;
        private int port;
        private Selector selector;
        private SocketChannel socketChannel;
        private volatile boolean stop;

        public TimeClientHandle(String host, int port) {
            this.host = host;
            this.port = port;
            try {
                this.selector = Selector.open();
                this.socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
            } catch (Exception e) {
                //
                System.exit(1);
            }
        }

        @Override
        public void run() {
            try {
                doConnect();
            } catch (Exception e) {
                //
            }
            while (!stop) {
                try {
                    selector.select(1000);
                    Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    SelectionKey key;
                    while (it.hasNext()) {
                        key = it.next();
                        it.remove();
                        try {
                            handleInput(key);
                        } catch (Exception e) {
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null) {
                                    key.channel().close();
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (selector != null) {
                try {
                    selector.close();
                } catch (Exception e) {

                }
            }

        }

        private void handleInput(SelectionKey key) throws IOException {
            if (!key.isValid()) {
                return;
            }
            SocketChannel sc = (SocketChannel) key.channel();

            if (key.isConnectable() && sc.finishConnect()) {
                sc.register(selector, SelectionKey.OP_READ);
                doWrite(sc);
            }

            if (key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes);
                    System.out.println("Now is : " + body);
                    this.stop = true;
                } else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                }
            }
        }

        private void doConnect() throws IOException {
            if (socketChannel.connect(new InetSocketAddress(host, port))) {
                socketChannel.register(this.selector, SelectionKey.OP_READ);
                System.out.println("do first write!");
                doWrite(this.socketChannel);
            } else {
                //todo
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
                doWrite(this.socketChannel);
            }
        }


        private void doWrite(SocketChannel socketChannel) throws IOException {
            byte[] req = "QUERY TIME ORDER".getBytes();
            ByteBuffer writeBuffer = ByteBuffer.wrap(req);
//            writeBuffer.put(req);
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
            if (!writeBuffer.hasRemaining()) {
                System.out.println("Send order server succeed.");
            }
        }
    }
}
