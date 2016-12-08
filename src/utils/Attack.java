package utils;

public class Attack {
	
	private int id;
	private String name;
	private String effect;
	private int damage;
	private int cd;
	private int traverse;
	private String path;
	private int x;
	private int y;
	private int xend;
	private int yend;
	private int speed;
	private int direction;
	
	public int getXbeg() {
		return x;
	}


	public void setXbeg(int xbeg) {
		this.x = xbeg;
	}


	public int getYbeg() {
		return y;
	}


	public void setYbeg(int ybeg) {
		this.y = ybeg;
	}


	public int getXend() {
		return xend;
	}


	public void setXend(int xend) {
		this.xend = xend;
	}


	public int getYend() {
		return yend;
	}


	public void setYend(int yend) {
		this.yend = yend;
	}


	public Attack() {
		
	}


	@Override
	public String toString() {
		return "Attack [id=" + id + ", name=" + name + ", effect=" + effect + ", damage=" + damage + ", cd=" + cd
				+ ", traverse=" + traverse + ", path=" + path + ", xbeg=" + x + ", ybeg=" + y + ", xend=" + xend
				+ ", yend=" + yend + ", speed=" + speed + "]";
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getEffect() {
		return effect;
	}


	public void setEffect(String effect) {
		this.effect = effect;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getDamage() {
		return damage;
	}


	public void setDamage(int damage) {
		this.damage = damage;
	}


	public int getTraverse() {
		return traverse;
	}


	public void setTraverse(int traverse) {
		this.traverse = traverse;
	}


	public int getCd() {
		return cd;
	}


	public void setCd(int cd) {
		this.cd = cd;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public int getSpeed() {
		return speed;
	}


	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public boolean checkHit(Player p) {
		if ((this.x <= p.getX() + 32  && this.x >= p.getX() - 32) && (this.y <= p.getY() + 32 && this.y >= p.getY() - 32))
			return true;
		return false;
	}


	public int getDirection() {
		return direction;
	}


	public void setDirection(int direction) {
		this.direction = direction;
	}
}
