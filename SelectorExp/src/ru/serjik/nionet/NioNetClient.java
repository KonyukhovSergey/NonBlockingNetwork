package ru.serjik.nionet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class NioNetClient implements ClientListener
{
	private ClientData clientData;
	private SocketChannel socket;
	public int state = 0;

	public NioNetClient(String host, int port) throws IOException
	{
		socket = SocketChannel.open();
		socket.configureBlocking(false);
		socket.connect(new InetSocketAddress(host, port));
	}

	public void tick() throws IOException
	{
		switch (state)
		{
		case 0:
			if (socket.finishConnect())
			{
				clientData = new ClientData(socket, this);
				state = 1;
			}
			break;

		case 1:
			if (clientData.recv() == false)
			{
				//clientData.close();
				state = 2;
				break;
			}
			clientData.send(null);

			break;

		case 2:
			break;
		}
	}

	public void send(String message) throws IOException
	{
		if (state == 1)
		{
			clientData.send(message);
		}
	}

	@Override
	public void onMessage(ClientData client, String message) throws IOException
	{
		System.out.println(message);
	}

}
