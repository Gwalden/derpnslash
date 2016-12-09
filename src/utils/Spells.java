package utils;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import utils.Attack;

public class Spells {
	public ArrayList<Attack> allSpell = new ArrayList<>();
	private ArrayList<Animation> fire = new ArrayList<>();
	
/**
 * Loading the spells
 * @throws SlickException
 */
	public Spells() throws SlickException {
		
		
		Animation u = new Animation();
		SpriteSheet sU = new SpriteSheet("ressources/fireballUp.png", 31, 69);
		u.addFrame(sU.getSprite(0, 0), 100);
		fire.add(u);
		
		
		
		Animation l = new Animation();
		SpriteSheet sL = new SpriteSheet("ressources/fireballLeft.png", 69, 31);
		l.addFrame(sL.getSprite(0, 0), 100);
		fire.add(l);
		
		
		SpriteSheet sD = new SpriteSheet("ressources/fireballDown.png", 31, 69);	
		Animation d = new Animation();
		d.addFrame(sD.getSprite(0, 0), 100);
		fire.add(d);

		
		SpriteSheet sR = new SpriteSheet("ressources/fireball.png", 69, 31);
		Animation r = new Animation();
		r.addFrame(sR.getSprite(0, 0), 100);
		fire.add(r);
		
	}
	
	public void render(GameContainer container, Graphics g) {
		if (allSpell.size() > 0){
			for (int i = 0 ; i < allSpell.size(); i++) {
				Attack att = allSpell.get(i); 
				g.drawAnimation(fire.get(att.getDirection()), att.getXbeg(),  att.getYbeg());
			}
			allSpell.removeAll(allSpell);
		}
	}
}
