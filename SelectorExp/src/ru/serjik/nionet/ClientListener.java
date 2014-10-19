package ru.serjik.nionet;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ClientListener
{
//	void onAccept(SocketChannel socket);
//	void onConnected(SocketChannel socket);
	void onMessage(ClientData client, String message) throws IOException;
}
