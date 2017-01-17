package utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;


public class Network {

	static public final int tcpport = 54555;
	static public final int udpport = 54556;
	static public final String host = "localhost";
	
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(addPlayer.class);
		kryo.register(movePlayer.class);
		kryo.register(updatep.class);
		kryo.register(attackPlayer.class);
		kryo.register(Attack.class);
		kryo.register(removePlayer.class);
		kryo.register(Effect.class);
		kryo.register(removeSpell.class);
		kryo.register(removeEffect.class);
	}
	
	static public void logregister(EndPoint endPoint){
		Kryo kryo = endPoint.getKryo();
		kryo.register(login.class);
		kryo.register(loginok.class);
		kryo.register(selectclass.class);
		kryo.register(gamestart.class);
		kryo.register(errorlogin.class);
	}
	
	static public class login{
		public String login;
		public String password;
	}
	
	static public class errorlogin{
		public String error;
	}
	
	static public class loginok{
	}
	
	static public class selectclass{
		public int selected;
	}
	
	static public class gamestart{
		public int tcpport;
		public int udpport;
	}
	
	static public class createplayer{
		public int classe;
	}
	
	static public class playercreated{
		public int classe;
		public int x,y;
	}
	
	static public class stopmove{
	}
	
	static public class updatep{
		public String name;
		public int x, y;
		public int direction;
		public boolean move;
		public int life;
	}
	

	static public class removeSpell {
		public int id;
	}
	
	static public class removeEffect {
		public int id;

	}
	static public class removePlayer {
		public String name;
	}
	
	static public class attackPlayer{
		public String name;
		public int direction, x, y, id;
		public int pushed;
		public boolean attacking;
		public String type;
		public int team;
	}
	
	static public class movePlayer{
		public String name;
		public int direction;
		public boolean move;
	}
	
	static public class addPlayer{
		public String name;
		public int x, y;
		public int team;
		public String type;
	}
}
