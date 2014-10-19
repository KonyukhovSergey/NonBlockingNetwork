package ru.serjik.nionet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ClientData
{
	private final static int BUFFER_SIZE = 4096;
	private final static int TIME_OUT = 15000;

	private ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
	private ByteBuffer sendBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	private BufferReader reader = new BufferReader(BUFFER_SIZE);

	private SocketChannel socket;
	private ClientListener clientListener;

	private Queue<String> messages = new LinkedList<String>();

	public ClientData(SocketChannel socket, ClientListener clientListener)
	{
		this.socket = socket;
		this.clientListener = clientListener;
		sendBuffer.limit(0);
	}

	public void close() throws IOException
	{
		messages.clear();
		socket.close();
	}

	public void send(String message) throws IOException
	{
		if (messages.size() == 0 && sendBuffer.hasRemaining() == false && message != null)
		{
			BufferWriter.write(sendBuffer, message);
			socket.write(sendBuffer);
			return;
		}

		if (message != null)
		{
			messages.add(message);
		}

		if (sendBuffer.hasRemaining())
		{
			socket.write(sendBuffer);
		}

		if (sendBuffer.hasRemaining() == false && messages.size() > 0)
		{
			BufferWriter.write(sendBuffer, messages.poll());
			socket.write(sendBuffer);
		}
	}

	public boolean recv() throws IOException
	{
		buffer.clear();

		int count = socket.read(buffer);

		if (count == -1)
		{
			System.out.println("readed -1 bytes");
			socket.close();
			return false;
		}

		buffer.flip();

		if (count > 0)
		{
			String line;

			while ((line = reader.read(buffer, count)) != null)
			{
				if (line.length() > 0)
				{
					clientListener.onMessage(this, line);
				}
			}
		}

		return true;
	}
}
