package client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import utils.Event;

public class ClientListener extends Listener{

	private NetworkClient client;

	public ClientListener(NetworkClient game) {
		super();
		this.client = game;
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
		this.client.ElistReceive.add(new Event(c, object));
	}
}
