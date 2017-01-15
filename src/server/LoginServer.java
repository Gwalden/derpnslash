package server;

import java.util.LinkedList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import utils.Network;
import utils.Sqlquery;
import utils.User;
import utils.Network.*;
import utils.Network.errorlogin;

public class LoginServer {

	private static Sqlquery sqlquery;
	private Server logServ;
	private LinkedList<User> usersl;
	private LinkedList<User> matchmaking;
	
	public LoginServer() {
		sqlquery = new Sqlquery();
		this.logServ = new Server();
		this.usersl =new LinkedList<>();
		this.matchmaking = new LinkedList<>();
		Network.logregister(this.logServ);
		try {
			this.addlitener();
			logServ.bind(Network.tcpport, Network.udpport);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.logServ.start();
	}
	
	public void addlitener(){
		this.logServ.addListener(new Listener(){
			@Override
			public void connected(Connection c) {
			}
			@Override
			public void received(Connection c, Object o) {
				if (o instanceof login){
					login recvlog = (login)o;
					if (sqlquery.isUser(recvlog.login, recvlog.password)){
						usersl.add(new User(c, recvlog.login));
						logServ.sendToTCP(c.getID(), new loginok());
					}
					else{
						errorlogin err = new errorlogin();
						err.error = "wrong login or password";
						logServ.sendToTCP(c.getID(), err);
					}
				}
				if (o instanceof selectclass){
					for (int i = 0; i < usersl.size(); i++) {
						if (usersl.get(i).c.equals(c) && !matchmaking.contains(usersl.get(i))){
							usersl.get(i).selected = ((selectclass)o).selected;
							matchmaking.add(new User(usersl.get(i)));
							System.out.println(matchmaking);
							matchmaking();
						}
					}
				}
			}
			@Override
			public void disconnected(Connection c) {
				usersl.remove(new User(c, null));
				matchmaking.remove(new User(c, null));
			}
		});
	}
	
	public void matchmaking(){
		int nb = 2;
		System.out.println("matchmaking");
		if (matchmaking.size()>=nb){
			System.out.println("new game");
			int port = -1;
			ServerGame game = new ServerGame();
			port = game.init();
			new Thread(game).start();
			if (port > 0){
				gamestart packet = new gamestart();
				packet.tcpport = port;
				packet.udpport = port+1;
				while (nb > 0){
					User u = matchmaking.pop();
					this.logServ.sendToTCP(u.c.getID(), packet);
					nb--;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new LoginServer();
	}
}
