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

	public void tick()
	{
		try
		{
			SocketChannel socketChannel = clientAcceptor.accept();
			if (socketChannel != null)
			{
				ClientData client = new ClientData(socketChannel, this);
				broadcast(client.toString() + " has joined");
				clients.add(client);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		for (Iterator<ClientData> iterator = clients.iterator(); iterator.hasNext();)
		{
			ClientData clientData = iterator.next();

			try
			{
				clientData.recv();
				clientData.send(null);
			}
			catch (IOException e)
			{
				iterator.remove();
				broadcast(clientData.toString() + " has removed");
				// e.printStackTrace();
			}
		}
	}

	public void broadcast(String message)
	{
		for (Iterator<ClientData> iterator = clients.iterator(); iterator.hasNext();)
		{
			ClientData client = iterator.next();

			try
			{
				client.send(message);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onMessage(ClientData client, String message) throws IOException
	{
		if (message.equals("quit"))
		{
			broadcast("client " + client.toString() + " want to quit");
			client.close();
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
