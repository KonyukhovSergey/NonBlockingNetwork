package com.example.nionetandr;

import ru.serjik.nionet.ClientData;
import ru.serjik.nionet.NioNetClient;
import ru.serjik.nionet.NioNetClientListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.annotation.TargetApi;
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
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		permitNetwork();

		textMessages = (TextView) findViewById(R.id.text_messages);
		editMessage = (EditText) findViewById(R.id.edit_message);
		buttonSend = (Button) findViewById(R.id.button_send);
		buttonSend.setOnClickListener(onButtonSendClick);
		client = new NioNetClient("serjik.noip.me", 11001, this);

		handler = new Handler();
		handler.postDelayed(runnableNet, 20);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void permitNetwork()
	{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
		StrictMode.setThreadPolicy(policy);
	}

	private OnClickListener onButtonSendClick = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			String message = editMessage.getText().toString();
			editMessage.setText("");
			addMessage(message);
			client.send(message);
		}
	};

	private void addMessage(String text)
	{
		textMessages.append(text + "\r\n");
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
			if (client.state != NioNetClient.STATE_DISCONNECTED)
			{
				client.tick();
				SystemClock.sleep(33);
				handler.postDelayed(this, 20);
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onMessage(ClientData client, final String message)
	{
		Log.v("nionet", "onMessage " + message);
		addMessage(message);
	}

	@Override
	public void onConnect()
	{
		Log.v("nionet", "onConnect");
		addMessage("connected");
	}

	@Override
	public void onDisconnect()
	{
		Log.v("nionet", "onDisconnect");
		addMessage("disconnected");
	}

}
