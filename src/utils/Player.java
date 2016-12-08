package utils;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import com.esotericsoftware.kryonet.Connection;
import client.ClientGame;
import utils.Network.movePlayer;

public class Player {

	public int id;
	public Connection c;
	public String name = Integer.toString((int)(Math.random()*10000));
	public float x = (int)(Math.random()*500), y = (int)(Math.random()*500);
	private int direction = 0;
	private int orientation = 0;

	private boolean moving = false;
	private float speed = 0.17f;
	private Animation[] animations = new Animation[8];

	public void init() {
		
		SpriteSheet spriteSheet;
		try {
			spriteSheet = new SpriteSheet("ressources/character.png", 64, 64);
			this.animations[0] = loadAnimation(spriteSheet, 0, 1, 0);
			this.animations[1] = loadAnimation(spriteSheet, 0, 1, 1);
			this.animations[2] = loadAnimation(spriteSheet, 0, 1, 2);
			this.animations[3] = loadAnimation(spriteSheet, 0, 1, 3);
			this.animations[4] = loadAnimation(spriteSheet, 1, 9, 0);
			this.animations[5] = loadAnimation(spriteSheet, 1, 9, 1);
			this.animations[6] = loadAnimation(spriteSheet, 1, 9, 2);
			this.animations[7] = loadAnimation(spriteSheet, 1, 9, 3);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y) {
		Animation animation = new Animation();
		for (int x = startX; x < endX; x++) {
			animation.addFrame(spriteSheet.getSprite(x, y), 100);
		}
		return animation;
	}

	public void render(GameContainer container, Graphics g) {
		g.setColor(new Color(0, 0, 0, .5f));
		g.fillOval(x - 16, y - 8, 32, 16);
		g.drawAnimation(animations[orientation + (moving ? 4 : 0)], x-32, y-60);
	}

	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_W:
			this.direction = 0;
			this.moving = true;
			break;
		case Input.KEY_A:
			this.direction = 1;
			this.moving = true;
			break;
		case Input.KEY_S:
			this.direction = 2;
			this.moving = true;
			break;
		case Input.KEY_D:
			this.direction = 3;
			this.moving = true;
			break;
		}
		if (this.moving == true){
			movePlayer ptomove = new movePlayer();
			ptomove.name = this.name;
			ptomove.direction = this.getDirection();
			ptomove.move = this.isMoving();
			ClientGame.gameEventSend.add(new Event(null,ptomove));
		}
	}
	
	public void keyReleased(int key, char c) {
		switch (key) {
		case Input.KEY_W:
			this.direction = 0;
			this.moving = false;
			break;
		case Input.KEY_A:
			this.direction = 1;
			this.moving = false;
			break;
		case Input.KEY_S:
			this.direction = 2;
			this.moving = false;
			break;
		case Input.KEY_D:
			this.direction = 3;
			this.moving = false;
			break;
		}
		if (this.moving == false){
			movePlayer ptomove = new movePlayer();
			ptomove.name = this.name;
			ptomove.direction = this.getDirection();
			ptomove.move = this.isMoving();
			ClientGame.gameEventSend.add(new Event(null,ptomove));
		}
	}
	
	public void update(GameContainer container, int delta) {
		if (this.moving) {
			switch (this.direction) {
			case 0:
				this.y -= speed * delta;
				break;
			case 1:
				this.x -= speed * delta;
				break;
			case 2:
				this.y += speed * delta;
				break;
			case 3:
				this.x += speed * delta;
				break;
			}
		}
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	
	@Override
	public String toString() {
		return "Player [name=" + name + ", x=" + x + ", y=" + y + "]";
	}
}
