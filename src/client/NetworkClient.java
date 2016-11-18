package client;

import java.io.IOException;


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;
import utils.Network;
import utils.Network.*;
import view.WindowGame;

public class NetworkClient {
	private Client client;
	public static WindowGame game;
	String name = Integer.toString((int)(Math.random()*100));

	public NetworkClient (WindowGame gamez) {
		NetworkClient.game = gamez;
		client = new Client();
		client.start();

		Network.register(client);

		client.addListener(new ThreadedListener(new ClientListener()));
		try {
			client.connect(5000, Network.host, Network.tcpport, Network.udpport);
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
		ptomove.move = game.player.isMoving();
		client.sendUDP(ptomove);
		System.out.println("send");
	}
}


