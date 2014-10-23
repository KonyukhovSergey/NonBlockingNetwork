package com.example.nionetandr;

import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.NioNetClient;
import ru.serjik.nionet.NioNetClientListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements NioNetClientListener
{
	private TextView textMessages;
	private EditText editMessage;
	private Button buttonSend;

	private NioNetClient client;
	private Thread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textMessages = (TextView) findViewById(R.id.text_messages);
		editMessage = (EditText) findViewById(R.id.edit_message);
		buttonSend = (Button) findViewById(R.id.button_send);
		client = new NioNetClient("serjik.noip.me", 11001, this);

		thread = new Thread(runnableNet);
		thread.start();

	}

	private Runnable runnableNet = new Runnable()
	{
		@Override
		public void run()
		{
			while (client.state != NioNetClient.STATE_DISCONNECTED)
			{
				client.tick();
				SystemClock.sleep(33);
			}
		}
	};

	@Override
	protected void onDestroy()
	{
		client.close();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onMessage(ClientData client, String message)
	{
		Log.v("nionet", "onMessage " + message);
	}

	@Override
	public void onConnect()
	{
		Log.v("nionet", "onConnect");
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnect()
	{
		Log.v("nionet", "onDisconnect");
		// TODO Auto-generated method stub

	}

}
