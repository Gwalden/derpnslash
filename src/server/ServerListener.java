package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import utils.Player;
import utils.Network.addPlayer;
import utils.Network.movePlayer;

public class ServerListener extends Listener{
	
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
		if (object instanceof addPlayer){
			addPlayer ptoadd = (addPlayer)object;
			Player p = new Player();
			p.setName(ptoadd.name);
			p.setX(ptoadd.x);
			p.setY(ptoadd.y);
			p.setMoving(false);
			p.setDirection(0);
			NetworkServer.logged.add(p);
			for (Player player : NetworkServer.logged) {
				addPlayer sendlist = new addPlayer();
				sendlist.name = player.getName();
				sendlist.x = (int)player.getX();
				sendlist.y = (int)player.getY();
				c.sendUDP(sendlist);
			}
			NetworkServer.server.sendToAllUDP(ptoadd);
		}

		if (object instanceof movePlayer){

			movePlayer ptomove = (movePlayer)object;
			for (Player player : NetworkServer.logged) {
				if (player.name.equals(ptomove.name)){
					player.setDirection(ptomove.direction);
					player.setMoving(ptomove.move);
					break;
				}
			}
		}
	}

}
