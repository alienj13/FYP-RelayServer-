package RelayServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CustomWebSocketServer extends WebSocketServer {

    private final Set<WebSocket> conns;

    public CustomWebSocketServer(int port) {
        super(new InetSocketAddress(port));
        conns = Collections.synchronizedSet(new HashSet<>());
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conns.add(conn);
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
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
        System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + ": " + ex.getMessage());
    }

    // Broadcast a message to all connected WebSocket clients
    public void broadcast(String text) {
        for (WebSocket sock : conns) {
            sock.send(text);
        }
    }

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}
}
