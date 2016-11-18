package client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import utils.Player;
import utils.Network.addPlayer;
import utils.Network.movePlayer;
import utils.Network.updatep;

public class ClientListener extends Listener{
	
	@Override
	public void received (Connection connection, Object object) {
		if (object instanceof addPlayer){
			addPlayer ptoadd = (addPlayer)object;
			for (Player player : NetworkClient.game.listp) {
				if (ptoadd.name.equals(player.getName()))
					return;
			}
			if (NetworkClient.game.player.getName().equals(ptoadd.name))
				return;
			Player p = new Player();
			p.setName(ptoadd.name);
			p.setX(ptoadd.x);
			p.setY(ptoadd.y);
			p.init();
			NetworkClient.game.listp.add(p);
			System.out.println(NetworkClient.game.listp);
		}
		
		if (object instanceof movePlayer){
			movePlayer ptomove = (movePlayer)object;
			for (Player player : NetworkClient.game.listp) {
				if (player.name.equals(ptomove.name)){
						player.setDirection(ptomove.direction);
						player.setMoving(ptomove.move);
				}
			}
		}
		if (object instanceof updatep){
			updatep ptoup = (updatep)object;
			if (NetworkClient.game.player.getName().equals(ptoup.name)){
				NetworkClient.game.player.x = ptoup.x;
				NetworkClient.game.player.y = ptoup.y;
				NetworkClient.game.player.setDirection(ptoup.direction);
				NetworkClient.game.player.setMoving(ptoup.move);
				return;
			}
			for (Player player : NetworkClient.game.listp) {
				if (player.name.equals(ptoup.name)){
					player.x = ptoup.x;
					player.y = ptoup.y;
					player.setDirection(ptoup.direction);
					player.setMoving(ptoup.move);
					break;
				}
			}
		}
	}
}
