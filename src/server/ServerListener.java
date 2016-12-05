package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import utils.Event;
/*import utils.Player;
import utils.Network.addPlayer;
import utils.Network.movePlayer;*/

public class ServerListener extends Listener{
	
	private NetworkServer server;
	
	public ServerListener(NetworkServer server) {
		super();
		this.server = server;
	}

	@Override
	public void connected(Connection c) {
		System.out.println(c.getID()+": Connected");
	}
	
	@Override
	public void disconnected(Connection c) {
		System.out.println(c.getID()+": Disconnected");
	}
	
	@Override
	public void received (Connection c, Object object) {
		this.server.ElistReceive.add(new Event(c, object));
	}

}
