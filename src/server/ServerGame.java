package server;

import utils.Player;
import utils.Network.updatep;

public class ServerGame implements Runnable {
	
	private boolean run = true;
	private final int TICK = 120;
	private final long OPTIMAL_TIME = 1000000000 / TICK;
	
	public void run() {
		double update = System.nanoTime();
		while (run){ 
			double lastUpdateTime = System.nanoTime();
			for (Player player : NetworkServer.logged) {
				player.update(null, (int)(System.nanoTime() - update)/1000000);
				//make all logique code here
				updatep ptoup= new updatep();
				ptoup.name = player.name;
				ptoup.x = (int)player.x;
				ptoup.y = (int)player.y;
				ptoup.direction = player.getDirection();
				ptoup.move = player.isMoving();
				NetworkServer.server.sendToAllUDP(ptoup);
			}
			update = System.nanoTime();
			try {
				while (System.nanoTime() - lastUpdateTime < OPTIMAL_TIME)
					Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
