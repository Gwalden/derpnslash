package server;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import utils.*;
import utils.Network.*;

public class NetworkServer {
	Server server;
	ArrayList<Player> logged = new ArrayList<>();
	
	public NetworkServer (){
		server = new Server();
		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(server);

		server.addListener(new Listener() {
			public void received (Connection c, Object object) {
				
				if (object instanceof addPlayer){
					addPlayer ptoadd = (addPlayer)object;
					Player p = new Player();
					p.setName(ptoadd.name);
					p.setX(ptoadd.x);
					p.setY(ptoadd.y);
					logged.add(p);
					System.out.println("player add: "+ ptoadd.name);
					for (Player player : logged) {
						addPlayer sendlist = new addPlayer();
						sendlist.name = player.getName();
						sendlist.x = (int)player.getX();
						sendlist.y = (int)player.getY();
						c.sendUDP(sendlist);
					}
					server.sendToAllUDP(ptoadd);
				}
				
				if (object instanceof movePlayer){
					movePlayer ptomove = (movePlayer)object;
					for (Player player : logged) {
						if (player.name.equals(ptomove.name)){
							player.setDirection(ptomove.direction);
							player.setMoving(ptomove.move);
						}
					}
					server.sendToAllUDP(ptomove);
				}
				
				if (object instanceof Hi){
					System.out.println("Client"+((Hi)object).hi);
					Hi re = new Hi();
					re.hi = "hello"+((Hi)object).hi;
					c.sendUDP(re); 
				}
			}
		});
		try {
			server.bind(Network.tcpport, Network.udpport);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		server.start();
	}
	
	public static void main(String[] args) {
		new NetworkServer();
	}
}
