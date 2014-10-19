import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import ru.serjik.nionet.BufferWriter;
import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.NioNetClient;

public class Prog
{
	public static void main(String[] args)
	{
		try
		{
			System.out.println("dd");
			NioNetClient client = new NioNetClient("localhost", 11001);

			while (true)
			{
				client.tick();
				
				String cmd = read(System.in);

				if (cmd.startsWith("stop"))
				{
					break;
				}
				else if (cmd.length() > 0)
				{
					client.send(cmd);
				}
				
				Thread.sleep(333);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static String read(InputStream stream) throws IOException
	{
		if (stream.available() > 0)
		{
			byte[] data = new byte[stream.available()];
			stream.read(data);
			return new String(data);
		}
		return "";
	}
}
