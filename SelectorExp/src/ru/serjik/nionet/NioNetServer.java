package ru.serjik.nionet;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NioNetServer implements ClientListener
{
	private List<ClientData> clients = new ArrayList<ClientData>();

	private ClientAcceptor clientAcceptor;

	public NioNetServer(int port) throws IOException
	{
		clientAcceptor = new ClientAcceptor(port);
	}

	public void stop() throws IOException
	{
		clientAcceptor.close();
	}

	public void tick() throws IOException
	{
		SocketChannel socketChannel = clientAcceptor.accept();

		if (socketChannel != null)
		{
			clients.add(new ClientData(socketChannel, this));
		}

		for (Iterator<ClientData> iterator = clients.iterator(); iterator.hasNext();)
		{
			ClientData clientData = iterator.next();

			if (clientData.recv() == false)
			{
				iterator.remove();
			}

			clientData.send(null);
		}
	}

	public void broadcast(String message) throws IOException
	{
		for (ClientData client : clients)
		{
			client.send(message);
		}
	}

	@Override
	public void onMessage(ClientData client, String message) throws IOException
	{
		if (message.equals("quit"))
		{
			client.close();
			clients.remove(client);
			broadcast("client " + client.toString() + " has qiut");
		}
		else if (message.equals("info"))
		{
			client.send("cliens count = " + clients.size());
		}
		else
		{
			broadcast("client " + client.toString() + ": " + message);
		}
	}
}
