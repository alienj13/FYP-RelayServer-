package RelayServer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import RelayServer.Threads;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CustomWebSocketServer extends WebSocketServer {

	private final Set<WebSocket> conns;

	private Threads t;
	private ConcurrentHashMap<String, Threads> threads = new ConcurrentHashMap<String, Threads>();

	public CustomWebSocketServer(int port) throws SocketException {
		super(new InetSocketAddress(port));
		conns = Collections.synchronizedSet(new HashSet<>());
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		conns.add(conn);
		System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
		t = new Threads(conn);
		t.start();
		threads.put(conn.getRemoteSocketAddress().getAddress().getHostAddress(), t);
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress());
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		conns.remove(conn);
		System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("Message from client: " + message);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		if (conn != null) {
			conns.remove(conn);
		}
		assert conn != null;
		System.out.println(
				"ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + ": " + ex.getMessage());
	}

	// Broadcast a message to all connected WebSocket clients
	public void sendMessage(DatagramPacket d) {
		String receivedData = new String(d.getData(), 0, d.getLength());
		try {
			Threads temp = threads.get(d.getAddress().getHostAddress());
			System.out.println(receivedData);
			if(receivedData != null) {
			temp.setMessage(receivedData);
			}
		} catch (NullPointerException e) {

		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

}
