package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.text.DefaultCaret;

import server.GameStateHandler;

public class ServerUI extends JFrame {

	private static final String newline = "\n";
	
	private GameStateHandler gg;
	
	private JPanel container;
	
	private JLabel logheader;
	private JScrollPane log;
	private JTextArea logtext;
	private JLabel ipheader;
	private JTextField yourip;
	private JLabel tableheader;
	private JList<String> clients;
	private JButton gamestart;
	private JLabel gamerunning;
	private JButton nextturn;
	
	int i = 0;
	
	private DefaultListModel<String> listclients;
	
	public ServerUI(GameStateHandler gg) {
		this.gg = gg;
	}
	
	public void newPlayerConnected(String name, String ip) {
		listclients.addElement(ip + "   " + name);
	}
	
	public void playerDisconnected(String name, String ip) {
		listclients.removeElement(ip + "   " + name);
		if(listclients.isEmpty()) {
			gamestart.setEnabled(true);
			gamerunning.setText("");
		}
	}
	
	public void toLog(String input) {
		logtext.append(input + newline);
	}
	
	public void setGameStartable(boolean startable) {
		if(gamerunning.getText().equals("")) {
			gamestart.setEnabled(startable);
		}
	}

	public void initWindow() {
		InetAddress ip;
		String myIP = "Could not detect IP";
		try 
		{
			ip = InetAddress.getLocalHost();
			myIP = ip.getHostAddress();
		} 
		catch (UnknownHostException e) 
		{
			
		}
		setTitle("DrawMyThing Server");
		setBounds(0, 0, 525, 500);
		
		container = new JPanel();
		container.setLayout(null);
		
		ipheader = new JLabel();
		ipheader.setBounds(10, 10, 250, 20);
		ipheader.setText("Deine IP-Adresse:");
		container.add(ipheader);
		
		yourip = new JTextField();
		yourip.setBounds(10, 30, 150, 30);
		yourip.setFont(new Font("Courier", Font.PLAIN, 12));
		yourip.setText(myIP);
		yourip.setEditable(false);
		yourip.setHorizontalAlignment(JTextField.CENTER);
		yourip.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		container.add(yourip);
		
		tableheader = new JLabel();
		tableheader.setBounds(10, 80, 250, 20);
		tableheader.setText("Verbundene Clients:");
		container.add(tableheader);
		
		listclients = new DefaultListModel<String>();
		clients = new JList<String>(listclients);
		clients.setBounds(10, 100, 250, 300);
		clients.setFont(new Font("Courier", Font.PLAIN, 12));
		clients.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		//clients.setBackground(Color.WHITE);
		container.add(clients);
		
		gamestart = new JButton();
		gamestart.setBounds(160, 410, 100, 30);
		gamestart.setText("Start");
		gamestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gg.sendGameStart();
				gamerunning.setText("Spiel läuft!");
				toLog("SYST: Game started!");
				gamestart.setEnabled(false);
				nextturn.setEnabled(true);
			}
		});
		gamestart.setEnabled(false);
		container.add(gamestart);
		
		gamerunning = new JLabel();
		gamerunning.setBounds(60, 415, 90, 20);
		gamerunning.setText("");
		container.add(gamerunning);
		
		logheader = new JLabel();
		logheader.setBounds(310, 10, 200, 20);
		logheader.setText("Log:");
		container.add(logheader);
		
		logtext = new JTextArea();
		logtext.setEditable(false);
		logtext.setLineWrap(true);
		logtext.setMargin(new Insets(4, 4, 4, 4));
		logtext.setFont(new Font("Courier", Font.PLAIN, 12));
	    log = new JScrollPane(logtext);
		log.setBounds(310, 30, 200, 410);
		log.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    DefaultCaret caret = (DefaultCaret)logtext.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
	    log.setViewportView(logtext);
		logtext.setText("");
		container.add(log);
		
		nextturn = new JButton();
		nextturn.setBounds(180, 30, 80, 30);
		nextturn.setText("Next");
		nextturn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gg.handleTurn(i);
				i++;
				if(i > gg.IDList.size()) {
					i = 0;
				}
			}
		});
		nextturn.setEnabled(false);
		container.add(nextturn);
		
		setContentPane(container);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
