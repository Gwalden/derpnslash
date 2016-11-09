package view;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import utils.Player;

public class Camera {

	private Player player;
	private float x = 0, y = 0;

	public Camera(Player player) {
		this.player = player;
		this.x = player.getX();
		this.y = player.getY();
	}

	public void render(GameContainer container, Graphics g) {
		g.translate(container.getWidth() / 2 - (int) this.x, container.getHeight() / 2 - (int) this.y);
	}

	public void update() {
		this.x = player.getX();
		this.y = player.getY();
	}
}
