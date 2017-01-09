package utils;

public class Attacker {
	
	private int time = 0;
	private String name;
	private String attName;
	
	public Attacker(String name, String attName) {
		this.setName(name);
		this.setAttName(attName);
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getTime() {
		return time;
	}


	public void setTime(int time) {
		this.time = time;
	}


	public String getAttName() {
		return attName;
	}


	public void setAttName(String attName) {
		this.attName = attName;
	}
}
