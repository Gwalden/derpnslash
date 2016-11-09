package utils;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Map {

	private TiledMap map;

	public void init() throws SlickException {
		this.map = new TiledMap("ressources/map.tmx");
	}

	public void render(GameContainer container, Graphics g) {
		this.map.render(0, 0);
	}
}
