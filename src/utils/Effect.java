package utils;

public class Effect {
	
	private String name;
	private int time;
	private String nameP;
	private int id;
	
	public Effect() {
		
	}
	
	public Effect(String name, int time) {
		this.name = name;
		this.time = time;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Player changePlayer(Player oldPlayer) {
		Player newP = new Player();
		if (this.getName().equals("Frost"))
		{
			newP.setSpeed(0.07f);
		}
		
		if (this.getName().equals("stun"))
		{
			
		}
		
		if (this.getName().equals("poison"))
		{
			
		}
		return newP;
	}
	@Override
	public String toString() {
		return "Effect [name=" + name + ", time=" + time + "]";
	}
	public String getNameP() {
		return nameP;
	}
	public void setNameP(String nameP) {
		this.nameP = nameP;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
