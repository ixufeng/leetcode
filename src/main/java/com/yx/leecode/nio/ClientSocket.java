package com.yx.leecode.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author xufeng
 * Create Date: 2019-10-31 15:10
 **/
public class ClientSocket {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.bind(new InetSocketAddress("127.0.0.1", 9999));
        ByteBuffer buffer = ByteBuffer.wrap("hello world!".getBytes());
        socketChannel.write(buffer);
        socketChannel.close();
    }
}
