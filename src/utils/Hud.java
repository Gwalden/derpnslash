package utils;

import java.util.ArrayList;

import org.newdawn.slick.AppletGameContainer.Container;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Hud {
	private Image playerBars;
	  private static final int BAR_WIDTH = 50;
	  private static final int BAR_HEIGHT = 10;

	  private static final Color LIFE_COLOR = new Color(255, 0, 0);
	  private static final Color STRING_COLOR = new Color(255, 255, 255);

	  
	  private Image spellFire;
	  private Image spellFrost;
	  private Image contour;

	  public Hud() throws SlickException 
	  {
		  this.init();
	  }
	  public void init() throws SlickException {
	    this.playerBars = new Image("ressources/player-bar.png");
	    this.spellFire = new Image("ressources/spellFire.png");
	    this.spellFrost = new Image("ressources/spellFrost.png");
	    this.contour = new Image("ressources/contour.png");
	  }
	  
	  
	  public void render(GameContainer container, Graphics g, ArrayList<Player> player, Player p) {
		  g.setColor(LIFE_COLOR);
		  g.fillRect(p.x - 23, p.y - 55, (float) ((p.getLife()*0.01) * BAR_WIDTH), BAR_HEIGHT);
		  g.setColor(STRING_COLOR);
		  g.drawString(""+p.getLife(),p.x - 15, p.y - 60);
		  for (int i = 0; i < player.size(); i++)
		  {
			  g.setColor(LIFE_COLOR);
			  g.fillRect(player.get(i).x - 23, player.get(i).y - 55, (float) ((player.get(i).getLife()*0.01) * BAR_WIDTH), BAR_HEIGHT);
			  g.setColor(STRING_COLOR);
			  g.drawString(""+player.get(i).getLife(),player.get(i).x - 15, player.get(i).y - 60);
		  }
		  
		  
		  	g.drawImage(spellFire, p.getX()-50 ,p.getY() + 326);
		  	g.drawImage(spellFrost, p.getX() + 56 - 50, p.getY() + 326);
		  	g.drawImage(contour, p.getX() + 56*2 - 50, p.getY() + 326);
		  	g.drawImage(contour, p.getX() + 56*3 - 50, p.getY() + 326);
		}
}
