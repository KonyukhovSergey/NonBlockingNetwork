import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.ConsoleLineReader;
import ru.serjik.nionet.NioNetServer;
import ru.serjik.nionet.NioNetServerListener;

public class Prog
{
	/**
	 * @param args
	 */
	private static NioNetServer server;

	public static void main(String[] args)
	{
		try
		{
			server = new NioNetServer(11001, new NioNetServerListener()
			{
				@Override
				public void onMessage(ClientData client, String message)
				{
					if (message.equals("quit"))
					{
						server.broadcast(client.toString() + " want to quit");
						client.close();
					}
					else if (message.equals("info"))
					{
						client.send("cliens count = " + server.clients().size());
					}
					else
					{
						server.broadcast(client.toString() + ": " + message);
					}
				}

				@Override
				public void onDisconnect(ClientData client)
				{
					System.out.println("disconnected: " + client.toString());
					server.broadcast(client.toString() + " has removed");
				}

				@Override
				public void onAccept(ClientData client)
				{
					System.out.println("connected: " + client.toString());
					server.broadcast(client.toString() + " has joined");
				}
			});

			while (true)
			{
				server.tick();

				Thread.sleep(1);

				String cmd = ConsoleLineReader.read(System.in);

				if (cmd.equals("stop"))
				{
					break;
				}
				else if (cmd.length() > 0)
				{
					System.out.println("broadcast: " + cmd);
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
}
