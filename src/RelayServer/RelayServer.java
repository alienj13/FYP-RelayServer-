package RelayServer;
import org.java_websocket.server.WebSocketServer;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.io.IOException;
import java.net.DatagramPacket;



public class RelayServer {
    
    private DatagramSocket udpSocket;
    private WebSocketServer webSocketServer;
    
    public RelayServer(int udpPort, int wsPort) throws SocketException, IOException {
        // Initialise and start UDP server
        udpSocket = new DatagramSocket(udpPort);
        
        // Initialize and start WebSocket server
        webSocketServer = new CustomWebSocketServer(wsPort);
        webSocketServer.start();
        
        // Start listening for UDP packets
        listenForUdpPackets();
    }
    
    private void listenForUdpPackets() throws IOException {
        byte[] buffer = new byte[65507]; // Maximum size of a UDP packet
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        
        while (true) {
            udpSocket.receive(packet);
            String receivedData = new String(packet.getData(), 0, packet.getLength());
            System.out.println(receivedData);
            // Relay this data to all connected WebSocket clients
            webSocketServer.broadcast(receivedData);
        }
    }
    
    public static void main(String[] args) throws SocketException, IOException {
        int udpPort = 61000; // Port that iqr sends data to
        int wsPort = 8080; // Port that Unity will connect to
        new RelayServer(udpPort, wsPort);
    }
}
