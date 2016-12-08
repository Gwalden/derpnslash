package client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import utils.Camera;
import utils.Event;
import utils.Map;
import utils.Network.addPlayer;
import utils.Network.movePlayer;
import utils.Network.updatep;
import utils.Player;

public class ClientGame extends BasicGame {
	/**
	 * Game state running
	 */
	public boolean run = true;
	/**
	 * list of Event that the game want send  
	 */
	public static	LinkedList<Event> gameEventSend;
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
	/**
	 * list of player
	 */
	public ArrayList<Player> playerl;


	private GameContainer container;
	private Map map;
	public Player player;
	private Camera camera;
	public static NetworkClient client;

	public static void main(String[] args) throws SlickException {

		AppGameContainer app = new AppGameContainer(new ClientGame(), 1024, 768, false);
		app.setAlwaysRender(true);
		app.setTargetFrameRate(120);
		app.start();
	}

	public ClientGame() {
		super("derpnslash");
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ClientGame.gameEventSend = new LinkedList<>();
		this.gameEventReceive = new LinkedList<>();
		this.ElistReceive = new ConcurrentLinkedQueue<>();
		this.ElistSend = new ConcurrentLinkedQueue<>();
		this.playerl = new ArrayList<>();
		this.container = container;
		this.map = new Map();
		this.player = new Player();
		this.camera = new Camera(player);
		this.map.init();
		this.player.init();
		client = new NetworkClient(this);
		new Thread(client).start();
		addPlayer p = new addPlayer();
		p.name = this.player.getName();
		p.x = (int)this.player.getX();
		p.y = (int)this.player.getY();
		ClientGame.gameEventSend.add(new Event(null, p));
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		this.camera.render(container, g);
		this.map.render(container, g);
		this.player.render(container, g);
		for (Player p : playerl) {
			p.render(container, g);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		this.player.keyPressed(key, c);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		this.getEventReceive();
		this.treatEvent();
		this.camera.update();
		this.setEventSend();
	}

	private void treatEvent(){
		while (!gameEventReceive.isEmpty()) {
			Event e = gameEventReceive.poll();
			if (e.object instanceof addPlayer){
				addPlayer ptoadd = (addPlayer)e.object;
				for (Player player : this.playerl) {
					if (ptoadd.name.equals(player.getName()))
						return;
				}
				if (this.player.getName().equals(ptoadd.name))
					return;
				Player p = new Player();
				p.setName(ptoadd.name);
				p.setX(ptoadd.x);
				p.setY(ptoadd.y);
				p.init();
				this.playerl.add(p);
			}
			if (e.object instanceof movePlayer){
				movePlayer ptomove = (movePlayer)e.object;
				for (Player player : this.playerl) {
					if (player.name.equals(ptomove.name)){
						player.setDirection(ptomove.direction);
						player.setMoving(ptomove.move);
					}
				}
				System.out.println("move p");
			}
			if (e.object instanceof updatep){
				updatep ptoup = (updatep)e.object;
				if (this.player.getName().equals(ptoup.name)){
					this.player.x = ptoup.x;
					this.player.y = ptoup.y;
					this.player.setDirection(ptoup.direction);
					this.player.setMoving(ptoup.move);
					return;
				}
				for (Player player : this.playerl) {
					if (player.name.equals(ptoup.name)){
						player.x = ptoup.x;
						player.y = ptoup.y;
						player.setDirection(ptoup.direction);
						player.setMoving(ptoup.move);
						break;
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		if (Input.KEY_ESCAPE == key) {
			container.exit();
		}
		player.keyReleased(key, c);	
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
