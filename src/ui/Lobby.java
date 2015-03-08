package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.ConnectException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
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
	private JTextArea status;
	private JList<String> clients;
	
	private boolean name;
	private boolean ip;
	
	private Font textstyle;
	
	public Lobby(UI ui) {
		this.ui = ui;
		this.textstyle = new Font("Segoe Print", Font.PLAIN, 12);
	}
	
	public void gameStarted() {
		dispose();
		ui.game.initWindow();
	}
	
	public void newPlayerConnected(String name) {
		//clients.append(name + newline);
	}
	
	public void showConnected() {
		status.setText("Verbindung aufgebaut." + newline +  "Warte auf Spielstart...");
		connect.setEnabled(false);
	}
	
	public void showConnectionFailed() {
		status.setText("Verbindung fehlgeschlagen.");
	}
	
	public void showDisconnected() {
		status.setText("Verbindung unterbrochen.");
		connect.setEnabled(true);
	}
	
	public void initWindow() {
		
		setTitle("Draw My Thing!");
		setBounds(0, 0, 275, 275);
		
		container = new JPanel();
		container.setLayout(null);
		
		nameheader = new JLabel();
		nameheader.setBounds(10, 10, 250, 20);
		nameheader.setFont(textstyle);
		nameheader.setText("Gib deinen Namen ein:");
		container.add(nameheader);
		
		nameenter = new JTextField();
		nameenter.setBounds(10, 30, 250, 30);
		nameenter.setFont(textstyle);
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
		ipheader.setFont(textstyle);
		ipheader.setText("Gib die IP des Servers ein:");
		container.add(ipheader);
		
		ipenter = new JTextField();
		ipenter.setBounds(10, 90, 250, 30);
		ipenter.setFont(textstyle);
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
		connect.setFont(textstyle);
		connect.setText("Connect");
		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ui.client.name = nameenter.getText();
				ui.client.ip = ipenter.getText();
				ui.client.start();
			}
		});
		connect.setEnabled(false);
		container.add(connect);
		
		status = new JTextArea();
		status.setBounds(10, 160, 250, 50);
		status.setEditable(false);
		status.setBackground(null);
		status.setFont(textstyle);
		status.setText("");
		container.add(status);
		
		/*
		clients = new JTextArea();
		clients.setBounds(10, 230, 250, 200);
		clients.setEditable(false);
		clients.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		clients.setMargin(new Insets(4, 4, 4, 4));
		clients.setBackground(Color.WHITE);
		container.add(clients);
		*/
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if(connect.isEnabled()) {
					ui.client.name = nameenter.getText();
					ui.client.ip = ipenter.getText();
					ui.client.start();
				}
			}
		});
		
		setContentPane(container);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
