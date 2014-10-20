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
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
