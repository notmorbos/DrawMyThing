package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
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
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

public class GamePanel extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final String newline = "\n";
	
	//Das UI, das dieses GamePanel aufruft
	private UI ui;
	
	//übergreifender Container
	private JPanel container;
	
	//Grundelemente des Panels
	private JTextArea whatisgoingon;
	private JScrollPane chatscroll;
	private JTextArea chatwindow;
	private JTextField chatinput;
	private JButton chatsend;
	private PaintPanel paintarea;
	private ButtonGroup color;
	private List<JRadioButton> colorsaves;
	private JButton paintareaclear;
	
	//Standardfarbe und Breite
	private Color standardcolor = Color.BLACK;
	private int standardwidth = 6;
	
	//Schriftgröße und Stil im Chat
	private Font textstyle;
	
	public GamePanel(UI ui) {
		this.ui = ui;
		this.standardcolor = Color.BLACK;
		this.standardwidth = 6;
		this.textstyle = new Font("Arial Black", Font.PLAIN, 12);
	}
	/**
	 * Soll irgendwann Chatnachrichten vom Server empfangen
	 * @param message Die übergebene Nachricht
	 */
	public void receiveMessage(String msg) {
		chatwindow.append(msg + newline);
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
	
	public void setTurn(String player) {
		paintarea.setMyTurn(player.equals(ui.client.name));
		paintarea.clearPanel();
		whatisgoingon.setText(player + " ist am Zug!");
		for(JRadioButton temp : colorsaves) {
			temp.setEnabled(player.equals(ui.client.name));
		}
	}
	
	public void chooseWord (String choice1, String choice2, String choice3) {
		String[] choices = {choice1, choice2, choice3};
	}
	
	public void initWindow() {
		
		setTitle("Draw My Thing!");
		setBounds(0, 0, 950, 620);
		
		container = new JPanel();
		container.setLayout(null);
		
		
		whatisgoingon = new JTextArea();
		whatisgoingon.setBounds(10, 0, 600, 50);
		whatisgoingon.setFont(new Font("Arial Black", Font.PLAIN, 28));
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
		chatwindow = new JTextArea();
		chatwindow.setEditable(false);
		chatwindow.setLineWrap(true);
		chatwindow.setWrapStyleWord(true);
		chatwindow.setFont(textstyle);
		chatwindow.setMargin(new Insets(4, 4, 4, 4));
	    chatscroll = new JScrollPane(chatwindow);
		chatscroll.setBounds(680, 60, 250, 480);
		chatscroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
		
		//Buttons für jede Farbe
		for(Color tempcolor : colors) {
			JRadioButton temp = new JRadioButton();
			temp.setBounds(buttontopleft.x, buttontopleft.y, 50, 20);
			temp.setBackground(tempcolor);
			temp.addActionListener(new ColorButtonListener(tempcolor));
			buttontopleft.y += 20;
			if(tempcolor == standardcolor) {
				temp.setSelected(true);
			}
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
			color.add(temp);
			colorsaves.add(temp);
			container.add(temp);
		}
		
		/*
		//Clear-Button für die Zeichenfläche
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
		setVisible(true);
	}
	
	/**
	 * Custom Listener für die Colorbuttons mit Übergabe an das PaintPanel
	 * @author Markus
	 */
	class ColorButtonListener implements ActionListener {
		
		private Color drawColor;
		
		/**
		 * @param color Die Farbe des zugehörigen Colorbuttons
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
	 * Custom Listener für die Stiftbreitebuttons mit Übergabe an das PaintPanel
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
}
