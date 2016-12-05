package utils;

import com.esotericsoftware.kryonet.Connection;

public class Event {
	public Connection c;
	public Object object;
	
	public Event(Connection c, Object o) {
		this.c = c;
		this.object= o;
	}
}
