package utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import com.esotericsoftware.kryonet.Connection;
import client.ClientGame;
import utils.Network.attackPlayer;
import utils.Network.movePlayer;

public class Player {

	private String type = "hunter";	
	public SpellLauncher sl = new SpellLauncher();
	private int team = 2;
	public int id;
	private int life = 100;
	public Connection c;
	public String name = Integer.toString((int)(Math.random()*10000));
	public float x = (int)(Math.random()*500), y = (int)(Math.random()*500);
	private int direction = 0;
	public List<Effect> effectL = new ArrayList<>(); 
	private int orientation = 0;
	private boolean moving = false;
	private boolean attacking = false;
	private float speed = 0.17f;
	private Animation[] animations = new Animation[8];
	private Animation[] attAnimations = new Animation[8];
	public Color rond;
	private int pushed;
	private boolean disable = false;

	
	public LinkedList<Integer> movelist;

	
	
	public void init() {
		movelist= new LinkedList<>();
		rond = new Color(0, 0, 0);
		SpriteSheet spriteSheet;
		try {
			spriteSheet = new SpriteSheet("ressources/ElfeRanger.png", 64, 64);
			this.animations[0] = loadAnimation(spriteSheet, 0, 1, 8);
			this.animations[1] = loadAnimation(spriteSheet, 0, 1, 9);
			this.animations[2] = loadAnimation(spriteSheet, 0, 1, 10);
			this.animations[3] = loadAnimation(spriteSheet, 0, 1, 11);
			this.animations[4] = loadAnimation(spriteSheet, 1, 9, 8);
			this.animations[5] = loadAnimation(spriteSheet, 1, 9, 9);
			this.animations[6] = loadAnimation(spriteSheet, 1, 9, 10);
			this.animations[7] = loadAnimation(spriteSheet, 1, 9, 11);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		try {
			spriteSheet = new SpriteSheet("ressources/ElfeRanger.png", 64, 64);
			this.attAnimations [0] = loadAnimation(spriteSheet, 0, 1, 16);
			this.attAnimations[1] = loadAnimation(spriteSheet, 0, 1, 17);
			this.attAnimations[2] = loadAnimation(spriteSheet, 0, 1, 18);
			this.attAnimations[3] = loadAnimation(spriteSheet, 0, 1, 19);
			this.attAnimations[4] = loadAnimation(spriteSheet, 1, 13, 16);
			this.attAnimations[5] = loadAnimation(spriteSheet, 1, 13, 17);
			this.attAnimations[6] = loadAnimation(spriteSheet, 1, 13, 18);
			this.attAnimations[7] = loadAnimation(spriteSheet, 1, 13, 19);
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
		g.setColor(rond);
		g.fillOval(x - 16, y - 8, 32, 16);
		if (attacking) {
			g.drawAnimation(attAnimations[orientation + (attacking ? 4 : 0)], x-32, y-60);
		}
		else {
		//	g.drawAnimation(animations[direction + (moving ? 4 : 0)], x-32, y-60);
			g.drawAnimation(animations[orientation + (moving ? 4 : 0)], x-32, y-60); 
		}
	}

	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_Z:
			this.movelist.add(0);
			this.sendmove();
			break;
		case Input.KEY_Q:
			this.movelist.add(1);
			this.sendmove();
			break;
		case Input.KEY_S:
			this.movelist.add(2);
			this.sendmove();
			break;
		case Input.KEY_D:
			this.movelist.add(3);
			this.sendmove();
			break;
		case Input.KEY_A:
			if (!disable) {
				this.setPushed(key);
				this.attacking = true;
			}
			break;
		case Input.KEY_E:
			if (!disable) {
				this.setPushed(key);
				this.attacking = true;
			}
		case Input.KEY_R:
			if (!disable) {
				this.setPushed(key);
				this.attacking = true;
			}
			break;

		}
		if (this.attacking == true) {
			attackPlayer pToAttack = new attackPlayer();
			pToAttack.name = this.name;
			pToAttack.team = this.team;
			pToAttack.type = this.type;
			pToAttack.x = (int) this.getX();
			pToAttack.y = (int) this.getY();
			pToAttack.attacking = this.attacking;
			pToAttack.direction = this.getOrientation();
			pToAttack.pushed = this.getPushed();
			ClientGame.gameEventSend.add(new Event(null,pToAttack));
		}
	}
	
	public void keyReleased(int key, char c) {
		switch (key) {
		case Input.KEY_Z:
			movelist.remove(new Integer(0));
			this.sendmove();
			break;
		case Input.KEY_Q:
			movelist.remove(new Integer(1));
			this.sendmove();
			break;
		case Input.KEY_S:
			movelist.remove(new Integer(2));
			this.sendmove();
			break;
		case Input.KEY_D:
			movelist.remove(new Integer(3));
			this.sendmove();
			break;
		case Input.KEY_A:
			this.attacking = false;
			break;
		case Input.KEY_E:
			this.attacking = false;
			break;
		case Input.KEY_R:
			this.attacking = false;
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
	
	private void sendmove(){
		if (movelist.isEmpty()){
			this.moving = false;
		}
		else{
			this.moving = true;
			this.direction = movelist.getLast();
		}
		movePlayer ptomove = new movePlayer();
		ptomove.name = this.name;
		ptomove.direction = this.getDirection();
		ptomove.move = this.isMoving();
		ClientGame.gameEventSend.add(new Event(null,ptomove));
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
	
	public void effChange(Effect eff) {
		if (eff.getName().equals("frost")) {
			setSpeed(0.07f);
			this.rond = new Color(121, 248, 248);
		}
		else if (eff.getName().equals("root")) {
			setSpeed(0.0f);
			this.rond = new Color(255,255,255);
		}
		else if (eff.getName().equals("stun")) {
			setSpeed(0.0f);
			this.disable = true;
			this.rond = new Color(255,255,255);
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
	
	public boolean isAttacking() {
		return this.attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
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
	
	
	public float getSpeed() {
		return this.speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
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

	public void reput() {
		if (this.effectL.size() == 0)
			this.setSpeed(0.17f);
	}
	
	public void addEffect(Effect effect) {
		this.effectL.add(effect);	
	}
	@Override
	public String toString() {
		return "Player [name=" + name + ", x=" + x + ", y=" + y + "]";
	}

	public int getPushed() {
		return pushed;
	}

	public void setPushed(int pushed) {
		this.pushed = pushed;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTeam() {
		return team;
	}
	public void setTeam(int team) {
		this.team = team;
	}
	public SpellLauncher getSl() {
		return sl;
	}
	public void setSl(SpellLauncher sl) {
		this.sl = sl;
	}
}
