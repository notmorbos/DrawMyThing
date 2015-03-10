package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GamePanel extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final String newline = "\n";
	
	//Das UI, das dieses GamePanel aufruft
	private UI ui;
	
	//�bergreifender Container
	private JPanel container;
	
	//Grundelemente des Panels
	private JTextArea whatisgoingon;
	private JScrollPane chatscroll;
	private JTextPane chatwindow;
	private JPanel nowrappanel;
	private JTextField chatinput;
	private JButton chatsend;
	private PaintPanel paintarea;
	private ButtonGroup color;
	private List<JRadioButton> colorsaves;
	private JButton paintareaclear;
	private Timer timer;
	private JTextArea clock;
	int time = 90;
	private JTextArea hint;
	private Scoreboard scoreboard;
	
	//Standardfarbe und Breite
	private Color standardcolor = Color.BLACK;
	private int standardwidth = 6;
	
	//Schriftgr��e und Stil im Chat
	private Font textstyle;
	
	private String wordtopaint;
	private String currentplayer;
	private List<String> players;
	private List<Color> playercolors;
	
	private boolean firstturn = true;
	
	public GamePanel(UI ui) {
		this.ui = ui;
		this.standardcolor = Color.BLACK;
		this.standardwidth = 6;
		this.textstyle = new Font("Segoe Print", Font.PLAIN, 12);
		
		players = new ArrayList<String>();
		playercolors = new ArrayList<Color>() {{
			add(Color.RED); 
			add(Color.BLUE); 
			add(Color.YELLOW);
			add(Color.GREEN); 
			add(Color.PINK);
			add(new Color(220, 167, 114));
			add(new Color(0, 128, 0));
			add(new Color(0, 0, 139));
			add(new Color(165, 42, 42));
			add(new Color(64, 224, 208));
		}};
	}
	/**
	 * Soll irgendwann Chatnachrichten vom Server empfangen
	 * @param message Die �bergebene Nachricht
	 * @param style Nummer des Stils, 1 f�r Chatnachrichten, 2 f�r Systemnachrichten
	 */
	public void receiveMessage(String msg, int style) {
		StyledDocument doc = chatwindow.getStyledDocument();
		
		if(style == 1) {
			String name = msg.substring(0, msg.indexOf(":")+1);
			String message = msg.substring(msg.indexOf(":")+1);
			if(!players.contains(name)) {
				players.add(name);
			}

			SimpleAttributeSet namestyle = new SimpleAttributeSet();
			StyleConstants.setForeground(namestyle, playercolors.get(players.indexOf(name)%playercolors.size()));
			StyleConstants.setBold(namestyle, true);
			
			try {
				doc.insertString(doc.getLength(), name, namestyle);
			} catch (BadLocationException e) {
				System.out.println("Error while appending message.");
			}
			
			SimpleAttributeSet msgstyle = new SimpleAttributeSet();
			StyleConstants.setForeground(msgstyle, Color.BLACK);
			
			try {
				doc.insertString(doc.getLength(), message + newline, msgstyle);
			} catch (BadLocationException e) {
				System.out.println("Error while appending message.");
			}
		}
		else if(style == 2) {
			SimpleAttributeSet msgstyle = new SimpleAttributeSet();
			StyleConstants.setForeground(msgstyle, Color.BLACK);
			StyleConstants.setBold(msgstyle, true);
		
			try {
				doc.insertString(doc.getLength(), msg + newline, msgstyle);
			} catch (BadLocationException e) {
				System.out.println("Error while appending message.");
			}
		}
		else if(style == 3) {
			String name = msg.substring(0, msg.indexOf(" "));
			String message = msg.substring(msg.indexOf(" "));
			if(!players.contains(name + ":")) {
				players.add(name + ":");
			}

			SimpleAttributeSet namestyle = new SimpleAttributeSet();
			StyleConstants.setForeground(namestyle, playercolors.get(players.indexOf(name + ":")%playercolors.size()));
			StyleConstants.setBold(namestyle, true);
			
			try {
				doc.insertString(doc.getLength(), name, namestyle);
			} catch (BadLocationException e) {
				System.out.println("Error while appending message.");
			}
			
			SimpleAttributeSet msgstyle = new SimpleAttributeSet();
			StyleConstants.setForeground(msgstyle, Color.BLACK);
			StyleConstants.setBold(msgstyle, true);
			
			try {
				doc.insertString(doc.getLength(), message + newline, msgstyle);
			} catch (BadLocationException e) {
				System.out.println("Error while appending message.");
			}
		}
	}
	
	public void setTimer(int seconds) {
		time = seconds;
		clock.setText("" + seconds);
		timer.restart();
	}
	
	public void updateFromServer(Point p, boolean isDragging) {
		paintarea.draw(p, isDragging);
	}
	
	public void drawWidthChanged(int width) {
		paintarea.setDrawWidth(width);
	}
	
	public void drawColorChanged(Color color) {
		paintarea.setDrawColor(color);
	}
	
	public void setTurn(String player, boolean choosing, int playersthatguessedright) {
		
		if(choosing) {
			if (!firstturn) {
				switch (playersthatguessedright) {
				case 0:
					receiveMessage("   - - -   " + newline + "Runde vorbei! Niemand hat das Wort erraten...", 2);
					break;
				case 1:
					receiveMessage("   - - -   " + newline + "Runde vorbei! Nur ein Spieler hat das Wort erraten.", 2);
					break;
				default:
					receiveMessage("   - - -   " + newline + "Runde vorbei! " + playersthatguessedright
							+ " Spieler haben das Wort erraten.", 2);
					break;
				}
				receiveMessage(player + " ist als N�chster am Zug.", 3);
				
				scoreboard.showScoreboard();
			}
			
			timer.stop();
			clock.setText("0");
			if(player.equals(ui.client.name)) {
				whatisgoingon.setText("Du bist am Zug. W�hle ein Wort!");
			}
			else {
				whatisgoingon.setText(player + " w�hlt ein Wort...");
			}
			paintarea.setMyTurn(false);
			for(JRadioButton temp : colorsaves) {
				temp.setEnabled(false);
			}
			currentplayer = player;
			firstturn = false;
		}
		else {
			scoreboard.hideScoreboard();
			setTimer(90);
			if(player.equals(ui.client.name)) {
				whatisgoingon.setText("Dein Wort: " + wordtopaint);
			}
			else {
				whatisgoingon.setText(player + " ist am Zug!");
			}
			paintarea.setMyTurn(player.equals(ui.client.name));
			paintarea.clearPanel();
			for(JRadioButton temp : colorsaves) {
				temp.setEnabled(player.equals(ui.client.name));
			}
			currentplayer = player;
		}
	}	
	
	public void chooseWord (String choice1, String choice2, String choice3) {
		WordChoiceDialog choice = new WordChoiceDialog(choice1, choice2, choice3);
		scoreboard.setVisible(true);
	}
	
	public void refreshScore(String[] namesAndPoints) {
		scoreboard.refreshScore(namesAndPoints);
	}
	
	public void initWindow() {
		
		setTitle("Draw My Thing!");
		setBounds(0, 0, 950, 620);
		
		container = new JPanel();
		container.setLayout(null);
		
		scoreboard = new Scoreboard();
		
		whatisgoingon = new JTextArea();
		whatisgoingon.setBounds(10, 0, 600, 50);
		whatisgoingon.setFont(new Font("Segoe Print", Font.PLAIN, 28));
		whatisgoingon.setEditable(false);
		whatisgoingon.setMargin(new Insets(4, 4, 4, 4));
		whatisgoingon.setText("Willkommen bei Draw My Thing!");
		whatisgoingon.setBackground(null);
		container.add(whatisgoingon);
		
		//Baut das Zeichenfeld auf
		paintarea = new PaintPanel(ui, standardcolor, standardwidth);
		paintarea.setBounds(70, 60, 600, 480);
		paintarea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		paintarea.setBackground(Color.WHITE);
		container.add(paintarea);

		//Der Chat mit Scrollelement
		chatwindow = new JTextPane();
		chatwindow.setEditable(false);
		//chatwindow.setLineWrap(true);
		//chatwindow.setWrapStyleWord(true);
		chatwindow.setFont(textstyle);
		chatwindow.setMargin(new Insets(4, 4, 4, 4));
		nowrappanel = new JPanel(new BorderLayout());
		nowrappanel.add(chatwindow);
	    chatscroll = new JScrollPane(nowrappanel);
		chatscroll.setBounds(680, 60, 250, 480);
		chatscroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		chatscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    DefaultCaret caret = (DefaultCaret)chatwindow.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
	    chatscroll.setViewportView(chatwindow);
		container.add(chatscroll);
		
		//Chateingabe mit Enter-Listener
		chatinput = new JTextField();
		chatinput.setBounds(680, 550, 200, 30);
		chatinput.setFont(textstyle);
		chatinput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		chatinput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!(chatinput.getText().equals(""))) {
						ui.sendMessage(chatinput.getText());
						chatinput.setText("");
					}
				}
			}
		});
		container.add(chatinput);
		
		//Chateingabe - Send-Button
		chatsend = new JButton();
		chatsend.setBounds(890, 550, 40, 30);
		chatsend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!(chatinput.getText().equals(""))) {
					ui.sendMessage(chatinput.getText());
					chatinput.setText("");
					chatinput.grabFocus();
				}
			}
		});
		container.add(chatsend);
		
		clock = new JTextArea();
		clock.setBounds(630, 10, 40, 30);
		clock.setEditable(false);
		clock.setFont(new Font("Segoe Print", Font.PLAIN, 22));
		clock.setBackground(null);
		clock.setText("" + time);
		ActionListener clocksetter = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(time > 0) {
					time--;
					clock.setText("" + time);
				}
			}
		};
		timer = new Timer(1000, clocksetter);
		timer.setInitialDelay(1000);
		container.add(clock);
		
		hint = new JTextArea();
		hint.setBounds(70, 540, 600, 40);
		hint.setEditable(false);
		hint.setFont(new Font("Segoe Print", Font.PLAIN, 22));
		hint.setBackground(null);
		hint.setText("Tipp: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ");
		container.add(hint);
		
		//Liste aller Farben, die zur Auswahl stehen sollen
		List<Color> colors = new ArrayList<Color>() {{
			add(Color.BLACK); 
			add(Color.DARK_GRAY); 
			add(Color.GRAY); 
			add(Color.LIGHT_GRAY); 
			add(Color.WHITE); 
			add(new Color(165, 42, 42));
			add(Color.RED); 
			add(Color.ORANGE); 
			add(Color.YELLOW);
			add(Color.PINK);
			add(new Color(220, 167, 114));
			add(new Color(0, 128, 0));
			add(Color.GREEN); 
			add(new Color(64, 224, 208));
			add(Color.BLUE); 
			add(new Color(0, 0, 139));
		}};
		color = new ButtonGroup();
		colorsaves = new ArrayList<JRadioButton>();
		Point buttontopleft = new Point(10, 60);
		
		//Buttons f�r jede Farbe
		for(Color tempcolor : colors) {
			JRadioButton temp = new JRadioButton();
			temp.setBounds(buttontopleft.x, buttontopleft.y, 50, 20);
			temp.setBackground(tempcolor);
			temp.addActionListener(new ColorButtonListener(tempcolor));
			buttontopleft.y += 20;
			if(tempcolor == standardcolor) {
				temp.setSelected(true);
			}
			temp.setEnabled(false);
			color.add(temp);
			colorsaves.add(temp);
			container.add(temp);
		}
		
		//Array mit allen Stiftbreiten, die zur Auswahl stehen sollen
		int[] widths = new int[]{3, 6, 12, 24};
		color = new ButtonGroup();
		buttontopleft.y += 10;
		
		//Buttons mit Stiftbreiten
		for(int width : widths) {
			JRadioButton temp = new JRadioButton();
			temp.setBounds(buttontopleft.x, buttontopleft.y, 50, 20);
			temp.setText("" + width);
			temp.addActionListener(new WidthButtonListener(width));
			buttontopleft.y += 20;
			if(width == standardwidth) {
				temp.setSelected(true);
			}
			temp.setEnabled(false);
			color.add(temp);
			colorsaves.add(temp);
			container.add(temp);
		}
		
		/*
		//Clear-Button f�r die Zeichenfl�che
		paintareaclear = new JButton();
		paintareaclear.setBounds(5, 500, 50, 30);
		paintareaclear.setText("clear");
		paintareaclear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paintarea.clearPanel();
			}
		});
		container.add(paintareaclear);
		*/
		
		setContentPane(container);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Custom Listener f�r die Colorbuttons mit �bergabe an das PaintPanel
	 * @author Markus
	 */
	class ColorButtonListener implements ActionListener {
		
		private Color drawColor;
		
		/**
		 * @param color Die Farbe des zugeh�rigen Colorbuttons
		 */
		public ColorButtonListener(Color color) {
			this.drawColor = color;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			paintarea.setDrawColor(drawColor);
		}
	}
	
	/**
	 * Custom Listener f�r die Stiftbreitebuttons mit �bergabe an das PaintPanel
	 * @author Markus
	 */
	class WidthButtonListener implements ActionListener {
		private int drawWidth;
		
		/**
		 * @param width Die dem Button zugeordnete Stiftbreite
		 */
		public WidthButtonListener(int width) {
			this.drawWidth = width;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			paintarea.setDrawWidth(drawWidth);
		}
	}
	
	public class WordChoiceDialog extends JFrame {
		
		private JPanel container2;
		
		private JButton choice1;
		private JButton choice2;
		private JButton choice3;
		
		public WordChoiceDialog(String c1, String c2, String c3) {
			
			GridLayout grid = new GridLayout(3, 1);
			container2 = new JPanel(grid);
			
			choice1 = new JButton();
			choice1.setText(c1);
			choice1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ui.sendChosenWord(choice1.getText());
					wordtopaint = choice1.getText();
					dispose();
				}
			});
			container2.add(choice1);
			
			choice2 = new JButton();
			choice2.setText(c2);
			choice2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ui.sendChosenWord(choice2.getText());
					wordtopaint = choice2.getText();
					dispose();
				}
			});
			container2.add(choice2);
			
			choice3 = new JButton();
			choice3.setText(c3);
			choice3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ui.sendChosenWord(choice3.getText());
					wordtopaint = choice3.getText();
					dispose();
				}
			});
			container2.add(choice3);
			
			setContentPane(container2);
			pack();
			setResizable(false);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setLocationRelativeTo(null);
			setVisible(true);
		}
	}
	
	public class Scoreboard extends JFrame {
		
		private JPanel container3;
		
		private JTextArea scores;
		
		private String[] namesAndPoints;
		
		public Scoreboard() {
			container3 = new JPanel();
			
			scores = new JTextArea();
			scores.setFont(new Font("Segoe Print", Font.PLAIN, 18));
			scores.setEditable(false);
			scores.setMargin(new Insets(4, 4, 4, 4));
			scores.setText("");
			scores.setBackground(null);
			container3.add(scores);
			
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					super.windowClosing(e);
				}
			});
			
			setContentPane(container3);
			setResizable(false);
			setVisible(false);
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		}
		
		public void showScoreboard() {
			setVisible(true);
			
			pack();
			setLocationRelativeTo(null);
		}
		
		public void hideScoreboard() {
			setVisible(false);
		}
		
		public void refreshScore(String[] namesAndPoints) {
			scores.setText("Punktestand:" + newline + newline);
			
			for(int i = 1; i < namesAndPoints.length; i += 2) {
				scores.append(namesAndPoints[i] + " - " + namesAndPoints[i-1] + newline);
			}
			
			pack();
		}
	}
}
