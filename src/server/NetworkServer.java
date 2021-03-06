package server;

import java.util.concurrent.ConcurrentLinkedQueue;
import com.esotericsoftware.kryonet.Server;
import utils.*;
import com.esotericsoftware.kryonet.Listener.ThreadedListener;


public class NetworkServer implements Runnable{
	/**
	 * Kryonet server
	 */
	private Server server;
	/**
	 * shared list of event receiv by the server
	 */
	public ConcurrentLinkedQueue<Event> ElistReceive;
	/**
	 * shared list of event to send by the server
	 */
	public ConcurrentLinkedQueue<Event> ElistSend;
	/**
	 * accesor to the game
	 */
	private ServerGame game;
	
	public NetworkServer (ServerGame game){
		server = new Server();
		Network.register(server);
		this.game = game;
		this.ElistReceive = game.ElistReceive;
		this.ElistSend = game.ElistSend;
		server.addListener(new ThreadedListener(new ServerListener(this)));
	}
	
	public int init(){
		int port = this.bind(Network.tcpport+1, Network.udpport+1);
		server.start();
		return port;
	}

	private int bind(int tcpport, int udpport){
		int port = tcpport;
		int timeout = 5000;
		while (timeout > 0){
			try {
				server.bind(port, port+1);
				return port;
			} catch (Exception e) {
				port++;
				timeout--;
			}
		}
		return -1;
	}
	
	@Override
	public void run() {
		while (this.game.run) {
			double lastUpdateTime = System.nanoTime();
			while (!ElistSend.isEmpty()){
				Event e = ElistSend.poll();
				if (e.c != null)
					this.server.sendToUDP(e.c.getID(), e.object);
				else
					this.server.sendToAllUDP(e.object);
			}
			try {
				while (System.nanoTime() - lastUpdateTime < this.game.OPTIMAL_TIME)
					Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.server.close();
	}
}
