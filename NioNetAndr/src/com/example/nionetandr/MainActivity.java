package com.example.nionetandr;

import java.util.TimerTask;

import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.NioNetClient;
import ru.serjik.nionet.NioNetClientListener;
import android.os.Bundle;
import android.os.Handler;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textMessages = (TextView) findViewById(R.id.text_messages);
		editMessage = (EditText) findViewById(R.id.edit_message);
		buttonSend = (Button) findViewById(R.id.button_send);
		
		client = new NioNetClient("serjik.noip.me", 11001, this);
	}
	
	@Override
	protected void onDestroy()
	{
		netHandler.removeCallbacks(netTick);
		client.close();
		super.onDestroy();
	}	
	
	private Runnable netTick = new Runnable()
	{
		public void run()
		{
			client.tick();
			netHandler.postDelayed(this, 33);
		}
	};
	
	private Thread netThread = new 

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
