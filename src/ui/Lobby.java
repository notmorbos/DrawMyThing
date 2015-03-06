package ui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Lobby extends JFrame {

	private static final String newline = "\n";
	
	private UI ui;
	
	private JPanel container;
	
	private JLabel nameheader;
	private JTextField nameenter;
	private JLabel ipheader;
	private JTextField ipenter;
	private JButton connect;
	private JLabel clientheader;
	private JTextArea clients;
	
	private boolean name;
	private boolean ip;
	
	public Lobby(UI ui) {
		this.ui = ui;
	}
	
	public void gameStarted() {
		dispose();
		ui.game.initWindow();
	}
	
	public void newPlayerConnected(String name) {
		clients.append(name + newline);
	}
	
	public void initWindow() {
		
		setTitle("Draw My Thing!");
		setBounds(0, 0, 275, 470);
		
		container = new JPanel();
		container.setLayout(null);
		
		nameheader = new JLabel();
		nameheader.setBounds(10, 10, 250, 20);
		nameheader.setText("Gib deinen Namen ein:");
		container.add(nameheader);
		
		nameenter = new JTextField();
		nameenter.setBounds(10, 30, 250, 30);
		nameenter.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nameenter.setMargin(new Insets(4, 4, 4, 4));
		nameenter.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(nameenter.getText().length() > 2 && nameenter.getText().length() < 13) {
					if(nameenter.getText().matches("^[a-zA-Z0-9]*$")) {
						name = true;
						if(ip) {
							connect.setEnabled(true);
						}
					}
					else {
						name = false;
						connect.setEnabled(false);
					}
				}
				else {
					name = false;
					connect.setEnabled(false);
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
		});
		container.add(nameenter);
		
		ipheader = new JLabel();
		ipheader.setBounds(10, 70, 250, 20);
		ipheader.setText("Gib die IP des Servers ein:");
		container.add(ipheader);
		
		ipenter = new JTextField();
		ipenter.setBounds(10, 90, 250, 30);
		ipenter.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ipenter.setMargin(new Insets(4, 4, 4, 4));
		ipenter.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(!ipenter.getText().isEmpty()) {
					ip = true;
					if(name) {
						connect.setEnabled(true);
					}
				}
				else {
					ip = false;
					connect.setEnabled(false);
				}	
			}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
		});
		container.add(ipenter);
		
		connect = new JButton();
		connect.setBounds(160, 130, 100, 30);
		connect.setText("Connect");
		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameStarted(); //TODO: Entfernen, sobald Server das Spiel startet
				ui.server.start();
				ui.client.name = nameenter.getText();
				ui.client.ip = ipenter.getText();
				ui.client.start();
				clientheader.setText("Verbundene Spieler:");
			}
		});
		connect.setEnabled(false);
		container.add(connect);
		
		clientheader = new JLabel();
		clientheader.setBounds(10, 210, 250, 20);
		clientheader.setText("");
		container.add(clientheader);
		
		clients = new JTextArea();
		clients.setBounds(10, 230, 250, 200);
		clients.setEditable(false);
		clients.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		clients.setMargin(new Insets(4, 4, 4, 4));
		clients.setBackground(Color.WHITE);
		container.add(clients);
		
		setContentPane(container);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
