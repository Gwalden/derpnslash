package utils;

import com.esotericsoftware.kryonet.Connection;

public class User {

	public Connection c;
	public String login;
	public int selected;
	
	public User(Connection co, String log) {
		c = co;
		login = log;
	}
	public User(User u){
		this.c = u.c;
		this.login = u.login;
		this.selected = u.selected;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User){
			User s = (User)obj;
			if (s.c.equals(this.c))
				return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return login;
	}
}
