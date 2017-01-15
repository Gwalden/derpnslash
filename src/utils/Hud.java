package utils;

import java.util.ArrayList;

import org.newdawn.slick.AppletGameContainer.Container;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Class that will display the interface that the player will see when he will play
 * @author Léo
 *
 */
public class Hud {
	private Image playerBars;
	  private static final int BAR_WIDTH = 50;
	  private static final int BAR_HEIGHT = 10;

	  private static final Color LIFE_COLOR = new Color(208, 46, 73);
	  private static final Color STRING_COLOR = new Color(255, 255, 255);
	  private Image lightSpell;
	  private Image spellFire;
	  private Image frostArrow;
	  private Image circleArrow;
	  private Image pinkArrow;
	  private Image shadowSpell;
	  
	  public Hud() throws SlickException 
	  {
		  this.init();
	  }
	  
	  /**
	   * Method that will initialise all the images in the class.
	   * @throws SlickException
	   */
	  public void init() throws SlickException {
	    this.playerBars = new Image("ressources/HUD/rock_bar.png");
	    this.spellFire = new Image("ressources/HUD/spellFire.png");
	    this.frostArrow = new Image("ressources/HUD/frostArrow.png");
	    this.lightSpell = new Image("ressources/HUD/lightSpell.png");
	    this.shadowSpell = new Image("ressources/HUD/ShadowSpell.png");
	    this.pinkArrow = new Image("ressources/HUD/pinkArrowSpell.png");
	    this.circleArrow = new Image("ressources/HUD/circleArrowSpell.png");
	  }
	   
	  public void render(GameContainer container, Graphics g, ArrayList<Player> player, Player p) {
		 g.setColor(new Color(255,255,255));
		 g.drawString(p.getName(), p.x -23, p.y-70);

		 g.setColor(LIFE_COLOR);
		 g.drawImage(playerBars, p.x -23, p.y-55);

		 g.fillRect(p.x - 23, p.y - 53, (float) ((p.getLife()*0.01) * BAR_WIDTH), BAR_HEIGHT);
		 g.setColor(STRING_COLOR);
		 g.drawString(""+p.getLife(),p.x - 15, p.y - 60);
		 for (int i = 0; i < player.size(); i++)
		 {
			 g.setColor(new Color(255,255,255));
			 g.drawString(player.get(i).getName(),player.get(i).x -23,player.get(i).y-70);
			 g.setColor(LIFE_COLOR);
			 g.drawImage(playerBars, player.get(i).x -23, player.get(i).y-55);
			 g.fillRect(player.get(i).x - 23, player.get(i).y - 55, (float) ((player.get(i).getLife()*0.01) * BAR_WIDTH), BAR_HEIGHT);
			 g.setColor(STRING_COLOR);
			 g.drawString(""+player.get(i).getLife(),player.get(i).x - 15, player.get(i).y - 60);
		 }
		 if (p.getType().equals("hunter"))
			 this.intHunter(g,p);
		 else if (p.getType().equals("wizard"))
			 this.intWizard(g,p);
	  }
		 public void intHunter(Graphics g, Player p) {
			 g.drawImage(frostArrow, p.getX()  - 50, p.getY() + 326);
			 g.drawImage(pinkArrow, p.getX() + 56 - 50, p.getY() + 326);
			 g.drawImage(circleArrow, p.getX() + 56*2 - 50, p.getY() + 326); 
		 }
		 public void intWizard(Graphics g, Player p) {
			 g.drawImage(lightSpell, p.getX() - 50, p.getY() + 326);
			 g.drawImage(spellFire, p.getX() + 56 - 50, p.getY() + 326);
			 g.drawImage(shadowSpell, p.getX() + 56*2 - 50, p.getY() + 326);
		 }
}
