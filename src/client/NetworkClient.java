package client;

import java.io.IOException;


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import utils.Network;
import utils.Player;
import utils.Network.*;
import view.WindowGame;

public class NetworkClient {
	private Client client;
	private WindowGame game;
	String name = Integer.toString((int)(Math.random()*100));

	public NetworkClient (WindowGame game) {
		this.game = game;
		client = new Client();
		client.start();

		// For consistency, the classes to be sent over the network are
		// registered by the same method for both the client and server.
		Network.register(client);

		// ThreadedListener runs the listener methods on a different thread.
		client.addListener(new ThreadedListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof addPlayer){
					addPlayer ptoadd = (addPlayer)object;
					for (Player player : game.listp) {
						if (ptoadd.name.equals(player.getName()))
							return;
					}
					if (game.player.getName().equals(ptoadd.name))
						return;
					Player p = new Player();
					p.setName(ptoadd.name);
					p.setX(ptoadd.x);
					p.setY(ptoadd.y);
					p.init();
					game.listp.add(p);
					System.out.println(game.listp);
				}
				
				if (object instanceof movePlayer){
					movePlayer ptomove = (movePlayer)object;
					for (Player player : game.listp) {
						if (player.name.equals(ptomove.name)){
								player.setDirection(ptomove.direction);
							player.setX(ptomove.x);
							player.setY(ptomove.y);
						}
					}
				}
			}
		}));
		
		
		try {
			client.connect(5000, Network.host, Network.tcpport, Network.udpport);
			// Server communication after connection can go here, or in Listener#connected().
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		addPlayer ptoadd = new addPlayer();
		ptoadd.name = game.player.getName();
		ptoadd.x = (int)game.player.getX();
		ptoadd.y = (int)game.player.getY();
		client.sendUDP(ptoadd);
	}
	
	public void sendmove(){
		movePlayer ptomove = new movePlayer();
		ptomove.name = game.player.name;
		ptomove.direction = game.player.getDirection();
		client.sendUDP(ptomove);
	}
}


