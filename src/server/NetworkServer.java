package server;

import java.util.ArrayList;
import com.esotericsoftware.kryonet.Server;
import utils.*;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;


public class NetworkServer {
	public static Server server;
	public static ArrayList<Player> logged = new ArrayList<>();

	public NetworkServer (){
		server = new Server();
		Network.register(server);

		server.addListener(new ThreadedListener(new ServerListener()));
		try {
				server.bind(Network.tcpport, Network.udpport);	
			} catch (Exception e) {
				e.printStackTrace();
			}
		server.start();
	}

	public static void main(String[] args) {
		new NetworkServer();
		new Thread(new ServerGame()).start();
	}
}
