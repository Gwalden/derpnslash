package client;

import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import utils.Network;
import utils.Network.errorlogin;
import utils.Network.login;
import utils.Network.loginok;
import utils.Network.*;


public class MainClient {

	private JFrame window;
	private ImageIcon icon;
	public static LoginScreen loginscreen;
	public static ClassSelector clientpage;
	public ClientGame game;
	public static Client client;
	
	public MainClient() {
		this.window = new JFrame("Derp'N Slash");
		this.window.setSize(500, 500);
		this.icon = new ImageIcon("ressources/derpnslashicon.icon");
		this.window.setIconImage(icon.getImage());
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginscreen = new LoginScreen();
		clientpage = new ClassSelector();
		this.window.add(loginscreen);
		this.window.setVisible(true);
		
		client = new Client();
		Network.logregister(client);
		this.addlistener();
		client.start();
		this.tryconnection();
	}
	
	public void tryconnection(){
		try {
			client.connect(5000, Network.host, Network.tcpport, Network.udpport);
		} catch (IOException e) {
			int n = JOptionPane.showOptionDialog(loginscreen,"Unable to reach server, try again?",
				    "Unable to reach server", JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE,
				    null,null,null);
			System.out.println(n);
			if (n == 0)
				this.tryconnection();
			else{
				this.window.dispatchEvent(new WindowEvent(this.window, WindowEvent.WINDOW_CLOSING));
				System.exit(0);
			}
		}
	}
	
	public void addlistener(){
		client.addListener(new Listener(){
			
			@Override
			public void connected(Connection c) {
			}
			@Override
			public void disconnected(Connection c) {
				int n = JOptionPane.showOptionDialog(loginscreen,"Unable to reach server, try again?",
					    "Unable to reach server", JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE,
					    null,null,null);
				if (n == 0)
					tryconnection();
				else{
					window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
					System.exit(0);
				}
			}
			@Override
			public void received(Connection c, Object o) {
				if (o instanceof errorlogin){
					errorlogin err = (errorlogin)o;
					JOptionPane.showOptionDialog(loginscreen,err.error,
						    "server", JOptionPane.CLOSED_OPTION,JOptionPane.ERROR_MESSAGE,
						    null,null,null);
				}
				if (o instanceof loginok){
					window.add(clientpage);
					loginscreen.setVisible(false);
					clientpage.setVisible(true);
				}
				if (o instanceof gamestart){
					gamestart g = (gamestart)o;
					System.out.println("game start:"+g.tcpport+":"+g.udpport);
					AppGameContainer app;
					try {
						window.setVisible(false);
						app = new AppGameContainer(new ClientGame(g.tcpport, g.udpport,MainClient.clientpage.selected), 1024, 768, false);
						app.setAlwaysRender(true);
						app.setTargetFrameRate(120);
						app.start();
						window.setVisible(true);
					} catch (SlickException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public static void sendlogin(String log, char[] password){
		login sendlog = new login();
		sendlog.login = log;
		sendlog.password = new String(password);
		client.sendTCP(sendlog);
	}
	
	public static void sendclass(int selected){
		selectclass sendselect = new selectclass();
		sendselect.selected = selected;
		client.sendTCP(sendselect);
	}
	
	public static void main(String[] args) {
		new MainClient();
	}
}
