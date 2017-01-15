package client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ClassSelector extends JPanel{

	
	private JButton hunter;
	private JButton wizard;
	private JButton launch;
	
	public int selected = 0;
	
	public ClassSelector() {
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		
		this.launch = new JButton("launch");
		this.hunter = new JButton("Hunter");
		this.wizard = new JButton("Wizard");
		
		hunter.setBounds(100, 150, 120, 75);
		hunter.setBorderPainted(false);
		hunter.setOpaque(true);
		hunter.setBackground(Color.red);
		hunter.setForeground(Color.yellow);
		hunter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wizard.setBackground(Color.red);
				hunter.setBackground(Color.blue);
				selected = 1;
			}
		});
		
		wizard.setBounds(300, 150, 120, 75);
		wizard.setBorderPainted(false);
		wizard.setOpaque(true);
		wizard.setBackground(Color.red);
		wizard.setForeground(Color.yellow);
		wizard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wizard.setBackground(Color.blue);
				hunter.setBackground(Color.red);
				selected = 2;
			}
		});
		
		
		launch.setBounds(190, 400, 120, 75);
		launch.setBorderPainted(false);
		launch.setOpaque(true);
		launch.setBackground(Color.red);
		launch.setForeground(Color.yellow);
		launch.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selected == 0){
					JOptionPane.showOptionDialog(launch, "Select a class please",
						    "Client", JOptionPane.CLOSED_OPTION,JOptionPane.ERROR_MESSAGE,
						    null,null,null);
				}
				else{
					MainClient.sendclass(selected);
				}
			}
		});
		
		this.add(hunter);
		this.add(wizard);
		this.add(launch);
	}
}
