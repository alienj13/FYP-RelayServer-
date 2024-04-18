package RelayServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Set;

import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.server.WebSocketServer;

public class Threads extends Thread {

	private WebSocket connection;
	private volatile String message;

	public Threads(WebSocket connection) {

		this.connection = connection;
	}

	public void run() {

		while (true) {
			try {
				while (message != null) {
						connection.send(message);
						message = null;
				}
			
			} catch (WebsocketNotConnectedException i) {

			}
		}
	
	}
	public void setMessage(String message) {

		this.message = message;
		System.out.println(this.message);
	}
}
