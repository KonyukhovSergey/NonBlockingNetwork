package ru.serjik.nionet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Future;

public class ClientAcceptor
{
	private ServerSocketChannel serverSocket = ServerSocketChannel.open();

	public ClientAcceptor(int port) throws IOException
	{
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false);
	}

	public SocketChannel accept() throws IOException
	{
		SocketChannel socketChannel = serverSocket.accept();

		if (socketChannel != null)
		{
			socketChannel.configureBlocking(false);
			socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			socketChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
			return socketChannel;
		}

		return null;
	}

	public void close() throws IOException
	{
		serverSocket.close();
	}
}
