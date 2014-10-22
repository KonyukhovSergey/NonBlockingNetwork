import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.omg.CORBA.Environment;

import ru.serjik.nionet.BufferWriter;
import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.ConsoleLineReader;
import ru.serjik.nionet.NioNetClient;
import ru.serjik.nionet.NioNetClientListener;

public class Prog
{
	private static NioNetClient client;
	public static void main(String[] args)
	{
		try
		{
			client = new NioNetClient("localhost", 11001, new NioNetClientListener()
			{
				@Override
				public void onMessage(ClientData client, String message)
				{
					System.out.println(message);
				}
				
				@Override
				public void onDisconnect()
				{
					System.out.println("disconnected");
				}
				
				@Override
				public void onConnect()
				{
					System.out.println("connected");
				}
			});

			while (true)
			{
				client.tick();

				String cmd = ConsoleLineReader.read(System.in);

				if (cmd.equals("stop"))
				{
					break;
				}
				else if (cmd.length() > 0)
				{
					client.send(cmd);
				}

				Thread.sleep(1);
				
				if(client.state == NioNetClient.STATE_DISCONNECTED)
				{
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
