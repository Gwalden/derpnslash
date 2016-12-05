package server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import utils.Event;
import utils.Network.addPlayer;
import utils.Network.movePlayer;
import utils.Network.updatep;
import utils.Player;

public class ServerGame implements Runnable {
	
	/**
	 * Game state running
	 */
	public boolean run = true;
	/**
	 * Game iteration by second wanted
	 */
	public int TICK = 120;
	/**
	 * Optimal time for one iteration of the game
	 */
	public long OPTIMAL_TIME = 1000000000 / TICK;
	/**
	 * list of player
	 */
	public ArrayList<Player> playerl;
	/**
	 * list of Event that the game want send  
	 */
	private	LinkedList<Event> gameEventSend;
	/**
	 * list of Event receive to use
	 */
	private	LinkedList<Event> gameEventReceive;
	/**
	 * shared list of event receiv by the server
	 */
	public ConcurrentLinkedQueue<Event> ElistReceive;
	/**
	 * shared list of event to send by the server
	 */
	public ConcurrentLinkedQueue<Event> ElistSend;
	/*
	 * Server
	 */
	public NetworkServer server;
	
	public ServerGame() {
		this.gameEventSend = new LinkedList<>();
		this.gameEventReceive = new LinkedList<>();
		this.ElistReceive = new ConcurrentLinkedQueue<>();
		this.ElistSend = new ConcurrentLinkedQueue<>();
		this.playerl = new ArrayList<>();
		this.server = new NetworkServer(this);
	}
	
	public void run() {
		new Thread(this.server).start();
		double update = System.nanoTime();
		while (run){ 
			double lastUpdateTime = System.nanoTime();
			this.getEventReceive();
			this.treatEvent();
			this.update(update);
			update = System.nanoTime();
			this.setEventSend();
			try {
				while (System.nanoTime() - lastUpdateTime < OPTIMAL_TIME)
					Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void update(double lastupdate){
		for (Player p : playerl) {
			p.update(null, (int)((System.nanoTime() - lastupdate)/1000000));
		}
		for (Player p : playerl){
			updatep pup = new updatep();
			pup.direction = p.getDirection();
			pup.name = p.getName();
			pup.move = p.isMoving();
			pup.direction = p.getDirection();
			pup.x = (int)p.getX();
			pup.y = (int)p.getY();
			for (Player player : playerl) {
				this.gameEventSend.add(new Event(player.c, pup));
			}
		}
	}
	
	
	private void treatEvent(){
		while (!gameEventReceive.isEmpty()) {
			Event e = gameEventReceive.poll();
				if (e.object instanceof addPlayer){
					addPlayer ptoadd = (addPlayer)e.object;
					Player p = new Player();
					p.c = e.c;
					p.setName(ptoadd.name);
					p.setX(ptoadd.x);
					p.setY(ptoadd.y);
					p.setMoving(false);
					p.setDirection(0);
					for (Player pla : playerl) {
						addPlayer add = new addPlayer();
						add.name = pla.getName();
						add.x = (int)pla.getX();
						add.y = (int)pla.getY();
						this.gameEventSend.add(new Event(p.c,add));
					}
					this.playerl.add(p);
					addPlayer pto = new addPlayer();
					pto.name = p.getName();
					pto.x = (int)p.getX();
					pto.y = (int)p.getY();
					Event event = new Event(null, pto);
					this.gameEventSend.add(event);
				}	
			else if (e.object instanceof movePlayer){
				movePlayer ptomove = (movePlayer)e.object;
				for (Player player : this.playerl) {
					if (player.name.equals(ptomove.name)){
						player.setDirection(ptomove.direction);
						player.setMoving(ptomove.move);
						break;
					}	
				}
			}
		}
	}
	
	/**
	 * set all the event to send at the list shared
	 */
	private void setEventSend(){
		while (!gameEventSend.isEmpty()) {
			this.ElistSend.add(gameEventSend.poll());
		}
	}
	
	/**
	 * get all the event receiv by the server in shared list
	 */
	private void getEventReceive(){
		while (!ElistReceive.isEmpty()) {
			this.gameEventReceive.add(ElistReceive.poll());
		}
	}
}
