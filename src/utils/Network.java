package utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import view.WindowGame;

public class Network {

	static public final int tcpport = 54555;
	static public final int udpport = 54556;
	static public final String host = "localhost";
	
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(Hi.class);
		kryo.register(addPlayer.class);
		kryo.register(movePlayer.class);
	}

	static public class stopmove{
	}
	
	static public class movePlayer{
		public String name;
		public int direction;
		public boolean move;
	}
	
	static public class addPlayer{
		public String name;
		public int x, y;
	}
	
	static public class Hi{
		public String hi; 
	}
}
