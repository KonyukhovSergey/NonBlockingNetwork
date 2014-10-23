package com.example.nionetandr;

import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.NioNetClient;
import ru.serjik.nionet.NioNetClientListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
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
		buttonSend.setOnClickListener(onButtonSendClick);
		client = new NioNetClient("serjik.noip.me", 11001, this);

		thread = new Thread(runnableNet);
		thread.start();

	}

	private OnClickListener onButtonSendClick = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			String message = editMessage.getText().toString();
			editMessage.setText("");

			addMessage(message + "\r\n");

			synchronized (v)
			{
				client.send(message);
			}
		}
	};

	private void addMessage(String text)
	{
		textMessages.append(text);
		if (textMessages.getText().length() > 32768)
		{
			CharSequence data = textMessages.getText();

			for (int i = 0; i < data.length(); i++)
			{
				if (data.charAt(i) == '\n')
				{
					textMessages.setText(data.subSequence(i + 1, data.length()));
					break;
				}
			}
		}
		final Layout layout = textMessages.getLayout();

		if (layout != null)
		{
			int scrollDelta = layout.getLineBottom(textMessages.getLineCount() - 1) - textMessages.getScrollY()
					- textMessages.getHeight();
			if (scrollDelta > 0)
			{
				textMessages.scrollBy(0, scrollDelta);
			}
		}
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
	public void onMessage(ClientData client, final String message)
	{
		Log.v("nionet", "onMessage " + message);
		
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				addMessage(message);
			}
		});
		
	}

	@Override
	public void onConnect()
	{
		Log.v("nionet", "onConnect");
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				addMessage("connected");
			}
		});

	}

	@Override
	public void onDisconnect()
	{
		Log.v("nionet", "onDisconnect");
		
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				addMessage("disconnected");
			}
		});
		// TODO Auto-generated method stub
	}

}
