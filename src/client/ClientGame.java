package client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import utils.Attack;
import utils.Camera;
import utils.Effect;
import utils.Event;
import utils.Hud;
import utils.Map;
import utils.Network.addPlayer;
import utils.Network.movePlayer;
import utils.Network.removeEffect;
import utils.Network.removePlayer;
import utils.Network.removeSpell;
import utils.Network.updatep;
import utils.Player;
import utils.Spells;

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
	 * shared list of event received by the server
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
	
	public Spells allSpell;
	public Hud hud;
	private GameContainer container;
	private Map map;
	public Player player;
	private Camera camera;
	public static NetworkClient client;
	
	public int tcpport;
	public int udpport;

	/*public static void main(String[] args) throws SlickException {
		
		AppGameContainer app = new AppGameContainer(new ClientGame(), 1024, 768, false);
		app.setAlwaysRender(true);
		app.setTargetFrameRate(120);
		app.start();
	}
*/
	public ClientGame(int tport, int uport) {
		super("derpnslash");
		this.tcpport = tport;
		this.udpport= uport;
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		ClientGame.gameEventSend = new LinkedList<>();
		this.allSpell = new Spells();
		this.hud = new Hud();
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
		this.allSpell.render(container, g);
	    this.hud.render(container,g, playerl, player);
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
					this.player.setLife(ptoup.life);
					return;
				}
				for (Player player : this.playerl) {
					if (player.name.equals(ptoup.name)){
						player.x = ptoup.x;
						player.y = ptoup.y;
						player.setDirection(ptoup.direction);
						player.setMoving(ptoup.move);
						player.setLife(ptoup.life);
						break;
					}
				}
			}
			
			if (e.object instanceof removePlayer){
					for (int i = 0; i <this.playerl.size(); i++) 
					{
						if (playerl.get(i).getName().equals(((removePlayer)e.object).name)) 
							this.playerl.remove(i);
					}
				}

			if (e.object instanceof removeSpell){
					for (int i = 0; i < this.allSpell.allSpell.size(); i++) 
					{
						if (this.allSpell.allSpell.get(i).getId() == ((removeSpell)e.object).id) 
							this.allSpell.allSpell.remove(i);
					}
				}
			
			if (e.object instanceof Attack){
				int i = 0;
				boolean check = false;
				for (; i < this.allSpell.allSpell.size(); i++)
				{
					if (this.allSpell.allSpell.get(i).getId() == ((Attack)e.object).getId()) 
					{
						check = true;
						this.allSpell.allSpell.get(i).setXbeg(((Attack)e.object).getXbeg());
						this.allSpell.allSpell.get(i).setYbeg(((Attack)e.object).getYbeg());
					}
				}
				if (!check) {
					this.allSpell.allSpell.add((Attack) e.object);
				}
			}
			if (e.object instanceof Player){
				if (playerl.contains((Player)e.object)) {
					playerl.remove((Player)e.object);
				}
			}
			
			if (e.object instanceof Effect){
				}
		}
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		super.mouseMoved(oldx, oldy, newx, newy);	
		if (newx > this.container.getWidth()/2){
			 player.setOrientation(3);
		}
		else {
			player.setOrientation(1);
		}
		int checkdiv = (newx-this.container.getWidth()/2);
		if (checkdiv == 0)
			checkdiv=1;
		double res =((newy-this.container.getHeight()/2)*this.container.getWidth())/((checkdiv)*this.container.getHeight()); 
		if (Math.abs(res) > 1/1.3){
			if (newy > this.container.getHeight()/2)
				player.setOrientation(2);
			else
				player.setOrientation(0);
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
	 * get all the event received by the server in shared list
	 */
	private void getEventReceive(){
		while (!ElistReceive.isEmpty()) {
			this.gameEventReceive.add(ElistReceive.poll());
		}
	}
}
