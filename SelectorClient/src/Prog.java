import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.ConsoleLineReader;
import ru.serjik.nionet.NioNetClient;
import ru.serjik.nionet.NioNetClientListener;
import ru.serjik.utils.md5;

public class Prog
{
	private static NioNetClient client;
	private static int state = 0;
	private static String salt;
	private static String passh;

	public static void main(String[] args)
	{
		try
		{
			client = new NioNetClient("localhost", 11001, new NioNetClientListener()
			{

				@Override
				public void onMessage(ClientData client, String message)
				{
					switch (state)
					{
					case 0:
						salt = message;
						state = 1;
						break;
					case 1:
						if (message.startsWith("regme:"))
						{
							client.send(passh + ";captcha");
							state = 2;
						}
						else
						{
							state = 3;
						}
					case 2:
						if (message.equals("welcome"))
						{

						}
						state = 3;
						break;
					}

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
					if (state == 1)
					{
						String[] values = cmd.split(";");
						passh = md5.get(values[1]);
						values[1] = md5.get(passh + salt);
						cmd = values[0] + ";" + values[1];
					}

					client.send(cmd);
				}

				Thread.sleep(1);

				if (client.state == NioNetClient.STATE_DISCONNECTED)
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
