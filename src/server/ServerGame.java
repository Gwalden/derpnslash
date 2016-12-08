package server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.newdawn.slick.Input;

import utils.Attack;
import utils.Event;
import utils.Network.addPlayer;
import utils.Network.attackPlayer;
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
	public int TICK = 60;
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
	
	public BDD database;

	
	private ArrayList<Attack> spell;

	public ServerGame() {
		this.gameEventSend = new LinkedList<>();
		this.gameEventReceive = new LinkedList<>();
		this.ElistReceive = new ConcurrentLinkedQueue<>();
		this.ElistSend = new ConcurrentLinkedQueue<>();
		this.playerl = new ArrayList<>();
		this.spell = new ArrayList<>();
		this.database = new BDD();
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
	
	private void spellUpdate() {
		if (spell.size() != 0) {
			for (int i = 0 ; i < spell.size(); i++) {
				Attack att = spell.get(i);
				if ((att.getDirection() == 3) && (att.getXbeg() + 1 <= att.getXend()))
				{
					att.setXbeg(att.getXbeg() + 1);
					this.gameEventSend.add(new Event(null,att));
				}
				else if ((att.getDirection() == 2) && (att.getYbeg() <= att.getYend())) {
					att.setYbeg(att.getYbeg() + 1);
					this.gameEventSend.add(new Event(null,att));
				}
				else if (att.getDirection() == 1 && att.getXbeg() >= att.getXend()) {
					att.setXbeg(att.getXbeg() - 1);
					this.gameEventSend.add(new Event(null,att));
				}
				else if (att.getDirection() == 0 && att.getYbeg() > att.getYend()) {
					att.setYbeg(att.getYbeg() - 1);
					this.gameEventSend.add(new Event(null,att));
				}
				else
					spell.remove(att);
			}
		}
	}
	
	private void update(double lastupdate){
		this.spellUpdate();

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
				checkHit(player);
				this.gameEventSend.add(new Event(player.c, pup));
			}
		}
	}
	
	private boolean checkHit(Player player) {
		if (spell.size() != 0) {
			for (int i = 0 ; i < spell.size(); i++) {
				Attack att = spell.get(i); 
				if ((att.getXbeg() >= player.getX() - 20 && att.getXbeg() <= player.getX()+20) && ((att.getYbeg() >= player.getY() - 20 && att.getYbeg() <= player.getY()+20)))
				{
					player.setLife(player.getLife() - att.getDamage());
					spell.remove(att);
					System.out.println("la vie elle descend : " + player.getLife());
					if (player.getLife() <= 0)
					{
						this.playerl.remove(player);
					}
					return true;
				}
			}
		}
		return false;
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
			else if (e.object instanceof attackPlayer) {
				if (((attackPlayer) e.object).pushed == Input.KEY_A) {
					Attack att = database.createAtt("fireball");
					att.setXbeg(((attackPlayer) e.object).x + 10 ); //Le +50 est pour empecher les colisions avec le joueur
					att.setYbeg(((attackPlayer) e .object).y - 23);
					if (((attackPlayer) e.object).direction == 0) 
						att.setYend(att.getYbeg() - 100);
					else if (((attackPlayer) e.object).direction == 1) 
						att.setXend(att.getXbeg() - 100);
					else if (((attackPlayer) e.object).direction == 2) 
						att.setYend(att.getYbeg() + 100);
					else if (((attackPlayer) e.object).direction == 3) 
						att.setXend(att.getXbeg() + 100);
					att.setDirection(((attackPlayer) e.object).direction);
					this.spell.add(att);

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
	
	public static void main(String[] args) {
		ServerGame game = new ServerGame();
		new Thread(game).start();
	}
}
