package view;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import client.NetworkClient;
import utils.Map;
import utils.Player;

public class WindowGame extends BasicGame {

	private GameContainer container;

	private Map map;
	public Player player;
	private Camera camera;
	public static NetworkClient client;
	public ArrayList<Player> listp = new ArrayList<>();
	
	public static void main(String[] args) throws SlickException {
		
		AppGameContainer app = new AppGameContainer(new WindowGame(), 1024, 768, false);
		app.setAlwaysRender(true);
		app.setTargetFrameRate(120);
		app.start();
	}

	public WindowGame() {
		super("derpnslash");
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.container = container;
		this.map = new Map();
		this.player = new Player();
		this.camera = new Camera(player);
		this.map.init();
		this.player.init();
		client = new NetworkClient(this);
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		this.camera.render(container, g);
		this.map.render(container, g);
		this.player.render(container, g);
		for (Player p : listp) {
			p.render(container, g);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		this.player.keyPressed(key, c);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		this.camera.update();
	}

	@Override
	public void keyReleased(int key, char c) {
		if (Input.KEY_ESCAPE == key) {
			container.exit();
		}
		this.player.setMoving(false);
		client.sendmove();
	}
}
