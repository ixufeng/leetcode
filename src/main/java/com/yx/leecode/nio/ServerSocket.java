package com.yx.leecode.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author xufeng
 * Create Date: 2019-10-31 15:05
 **/
public class ServerSocket {


    public static void main(String[] args) throws Exception {
        //监听tcp连接通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        //绑定端口
        serverChannel.socket().bind(new InetSocketAddress(9999));

        while (true) {
            SocketChannel socketChannel = serverChannel.accept();
            new Thread(() -> {
                ByteBuffer buffer = ByteBuffer.allocateDirect(1500);
                try {
                    byte[] bytes = new byte[1500];
                    while (socketChannel.read(buffer) != -1) {
                        buffer.get(bytes);
                        String result = new String(bytes);
                        System.out.println(result);
                    }
                } catch (Exception e) {

                }

            }).start();
        }


    }
}
