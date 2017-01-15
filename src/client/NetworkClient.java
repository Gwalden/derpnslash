package client;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;

import utils.Event;
import utils.Network;

public class NetworkClient implements Runnable{
	/**
	 * Kryonet client
	 */
	private Client client;
	/**
	 * shared list of event receiv by the client
	 */
	public ConcurrentLinkedQueue<Event> ElistReceive;
	/**
	 * shared list of event to send by the client
	 */
	public ConcurrentLinkedQueue<Event> ElistSend;
	/**
	 * accesor to the game
	 */
	private ClientGame game;
	/**
	 * Game iteration by second wanted
	 */
	public int TICK = 120;
	/**
	 * Optimal time for one iteration of the game
	 */
	public long OPTIMAL_TIME = 1000000000 / TICK;

	public NetworkClient (ClientGame game) {
		client = new Client();
		Network.register(client);
		this.game = game;
		this.ElistReceive = game.ElistReceive;
		this.ElistSend = game.ElistSend;
		client.addListener(new ThreadedListener(new ClientListener(this)));
		client.start();
		try {
			client.connect(5000, Network.host, game.tcpport, game.udpport);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	

	@Override
	public void run() {
		while (this.game.run) {
			double lastUpdateTime = System.nanoTime();
			while (!ElistSend.isEmpty()){
					Event e = ElistSend.poll();
					this.client.sendUDP(e.object);
			}
			try {
				while (System.nanoTime() - lastUpdateTime < this.OPTIMAL_TIME)
					Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.client.close();
	}
}


