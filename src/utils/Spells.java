package utils;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.svg.InkscapeLoader;
import org.newdawn.slick.svg.SimpleDiagramRenderer;

import utils.Attack;
/**
 * Class that will load all the spell and display them on the players screen.
 * @author Léo
 *
 */
public class Spells {
	public ArrayList<Attack> allSpell = new ArrayList<>();
	private ArrayList<Animation> fireball = new ArrayList<>();
	private ArrayList<Animation> frostArrow = new ArrayList<>();
	private ArrayList<Animation> pinkArrow = new ArrayList<>();
	private ArrayList<Animation> circleArrow = new ArrayList<>();

	private Animation lightBall;
	private Animation shadowBall;
	
	
/**
 * Loading the sprites of all the spells
 * @throws SlickException
 */
	public Spells() throws SlickException {
		
		Animation u = new Animation();
		SpriteSheet sU = new SpriteSheet("ressources/fireBall/fireballUp.png", 31, 69);
		u.addFrame(sU.getSprite(0, 0), 100);
		fireball.add(u);
		
		
		
		Animation l = new Animation();
		SpriteSheet sL = new SpriteSheet("ressources/fireBall/fireBallLeft.png", 69, 31);
		l.addFrame(sL.getSprite(0, 0), 100);
		fireball.add(l);
		
		
		SpriteSheet sD = new SpriteSheet("ressources/fireBall/fireBallDown.png", 31, 69);	
		Animation d = new Animation();
		d.addFrame(sD.getSprite(0, 0), 100);
		fireball.add(d);

		
		SpriteSheet sR = new SpriteSheet("ressources/fireBall/fireBall.png", 69, 31);
		Animation r = new Animation();
		r.addFrame(sR.getSprite(0, 0), 100);
		fireball.add(r);
		
		
		sU = new SpriteSheet("ressources/frostArrow/frostArrowT.png", 33, 70);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		frostArrow.add(u);
		
		
		sU = new SpriteSheet("ressources/frostArrow/frostArrowL.png", 69, 31);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		frostArrow.add(u);

		sU = new SpriteSheet("ressources/frostArrow/frostArrowD.png", 32, 70);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		frostArrow.add(u);
		
		
		sU = new SpriteSheet("ressources/frostArrow/frostArrowR.png", 69, 31);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		frostArrow.add(u);
		
		
		sU = new SpriteSheet("ressources/pinkArrow/pinkArrowT.png", 31, 72);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		pinkArrow.add(u);
		
		sU = new SpriteSheet("ressources/pinkArrow/pinkArrowL.png", 72, 32);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		pinkArrow.add(u);
		
		sU = new SpriteSheet("ressources/pinkArrow/pinkArrowD.png", 31, 72);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		pinkArrow.add(u);
		
		sU = new SpriteSheet("ressources/pinkArrow/pinkArrowR.png", 72, 31);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		pinkArrow.add(u);
		
		sU = new SpriteSheet("ressources/spells/light.png", 30, 31);
		lightBall = new Animation();
		lightBall.addFrame(sU.getSprite(0, 0), 100);

		sU = new SpriteSheet("ressources/spells/shadowBall.png", 31, 34);
		shadowBall = new Animation();
		shadowBall.addFrame(sU.getSprite(0, 0), 100);

		

		sU = new SpriteSheet("ressources/circleArrow/circleArrowT.png", 29, 100);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		circleArrow.add(u);
		
		sU = new SpriteSheet("ressources/circleArrow/circleArrowL.png", 100, 30);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		circleArrow.add(u);
		
		sU = new SpriteSheet("ressources/circleArrow/circleArrowD.png", 27, 101);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		circleArrow.add(u);
		
		sU = new SpriteSheet("ressources/circleArrow/circleArrowR.png", 100, 33);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		circleArrow.add(u);
		
		sU = new SpriteSheet("ressources/circleArrow/circleArrowDiagD.png", 103, 92);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		circleArrow.add(u);
		
		sU = new SpriteSheet("ressources/circleArrow/circleArrowDiagL.png", 85, 84);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		circleArrow.add(u);
		
		sU = new SpriteSheet("ressources/circleArrow/circleArrowDiagDR.png", 100, 85);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		circleArrow.add(u);
		
		sU = new SpriteSheet("ressources/circleArrow/circleArrowDiagDL.png", 88, 81);
		u = new Animation();
		u.addFrame(sU.getSprite(0, 0), 100);
		circleArrow.add(u);
		
	}
	
	public void render(GameContainer container, Graphics g) {
		if (allSpell.size() > 0){
			for (int i = 0 ; i < allSpell.size(); i++) {
				Attack att = this.allSpell.get(i);
				if (att.getName().equals("fireBall"))
					g.drawAnimation(fireball.get(att.getDirection()), att.getXbeg(),  att.getYbeg());
				else if (att.getName().equals("frostArrow"))
					g.drawAnimation(frostArrow.get(att.getDirection()), att.getXbeg(),  att.getYbeg());
				else if (att.getName().equals("pinkArrow"))
					g.drawAnimation(pinkArrow.get(att.getDirection()), att.getXbeg(),  att.getYbeg());
				else if (att.getName().equals("lightBall"))
					g.drawAnimation(lightBall, att.getXbeg(),  att.getYbeg());
				else if (att.getName().equals("shadowBall"))
					g.drawAnimation(shadowBall, att.getXbeg(),  att.getYbeg());
				else if (att.getName().equals("circleArrow"))
					g.drawAnimation(circleArrow.get(att.getDirection()), att.getXbeg(),  att.getYbeg());
			}
		}
	}
}