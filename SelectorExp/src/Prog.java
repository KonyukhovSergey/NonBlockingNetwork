import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.serjik.nionet.ClientAcceptor;
import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.NioNetServer;

public class Prog
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			NioNetServer server = new NioNetServer(11001);

			while (true)
			{
				server.tick();

				Thread.sleep(333);

				String cmd = read(System.in);

				if (cmd.startsWith("stop"))
				{
					break;
				}
				else if (cmd.length() > 0)
				{
					server.broadcast("from server: " + cmd);
				}
			}

			server.stop();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("programm stoped");
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