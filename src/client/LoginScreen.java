package client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class LoginScreen extends JPanel{

	JButton connect;
	JTextField login;
	JPasswordField password;
	private JTextArea log;
	private JTextArea pass;
	
	public LoginScreen() {
		this.setLayout(null);
		this.setBackground(Color.black);
		
		log = new JTextArea("Login:");
		log.setEditable(false);
		log.setBackground(this.getBackground());
		log.setBounds(20, 20, 100, 20);
		log.setForeground(Color.yellow);
		this.add(log);
		
		login = new JTextField();
		login.setBounds(120, 20, 100, 20);
		login.setBackground(Color.GRAY);
		login.setForeground(Color.yellow);
		this.add(login);
		
		pass = new JTextArea("Password:");
		pass.setEditable(false);
		pass.setBackground(this.getBackground());
		pass.setBounds(20, 70, 100, 20);
		pass.setForeground(Color.yellow);
		this.add(pass);
		
		password = new JPasswordField();
		password.setBounds(120, 70, 100, 20);
		password.setBackground(Color.GRAY);
		password.setForeground(Color.yellow);;
		this.add(password);
		
		
		connect = new JButton("Connection");
		connect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MainClient.sendlogin(login.getText(), password.getPassword());
			}
		});
		connect.setBounds(20, 130, 120, 75);
		connect.setBorderPainted(false);
		connect.setOpaque(true);
		connect.setBackground(Color.red);
		connect.setForeground(Color.yellow);
		this.add(connect);
	}
	
	
}
