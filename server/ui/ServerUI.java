package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	private JCheckBox hidelog;
	private JTextField numberrounds;
	
	int i = 0;
	int j = 1;
	
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
		ImageIcon image = new ImageIcon("icons/cogwheel.png");
		setIconImage(image.getImage());
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
				int rounds = 3;
				if(Integer.parseInt(numberrounds.getText()) > 0 && Integer.parseInt(numberrounds.getText()) < 30) {
					rounds = Integer.parseInt(numberrounds.getText());
				}
				gg.sendGameStart(rounds);
				gamerunning.setText("Spiel läuft!");
				toLog("SYST: Game started!");
				gamestart.setEnabled(false);
			}
		});
		gamestart.setEnabled(false);
		container.add(gamestart);
		
		numberrounds = new JTextField();
		numberrounds.setBounds(110, 410, 40, 30);
		numberrounds.setAlignmentX(JTextField.CENTER_ALIGNMENT);
		numberrounds.setAlignmentY(JTextField.CENTER_ALIGNMENT);
		numberrounds.setText("2");
		container.add(numberrounds);
		
		gamerunning = new JLabel();
		gamerunning.setBounds(10, 415, 90, 20);
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
		
		hidelog = new JCheckBox();
		hidelog.setBounds(180, 30, 100, 30);
		hidelog.setText("Hide Log");
		hidelog.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				logheader.setVisible(!hidelog.isSelected());
				log.setVisible(!hidelog.isSelected());
				if(hidelog.isSelected()) {
					setSize(275, 500);
				}
				else {
					setSize(525, 500);
				}
			}
		});
		container.add(hidelog);
		
		setContentPane(container);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
