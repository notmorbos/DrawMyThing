package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

public class GamePanel extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final String newline = "\n";
	
	//übergreifender Container
	private JPanel container;
	
	//Grundelemente des Panels
	private JScrollPane chatscroll;
	private JTextArea chatwindow;
	private JTextField chatinput;
	private JButton chatsend;
	private PaintPanel paintarea;
	private ButtonGroup color;
	
	GamePanel() {
		
	}
	
	public void initWindow() {
		
		setTitle("Draw My Thing!");
		setBounds(0,0,900,600);
		
		container = new JPanel();
		container.setLayout(null);
		
		paintarea = new PaintPanel();
		paintarea.setBounds(60, 5, 590, 525);
		paintarea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		paintarea.setBackground(Color.WHITE);
		container.add(paintarea);

		chatwindow = new JTextArea();
		chatwindow.setEditable(false);
		chatwindow.setLineWrap(true);
	    chatscroll = new JScrollPane(chatwindow);
		chatscroll.setBounds(655, 5, 235, 525);
		chatscroll.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    DefaultCaret caret = (DefaultCaret)chatwindow.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
	    chatscroll.setViewportView(chatwindow);
		chatwindow.setText(" Willkommen im Chat!" + newline);
		container.add(chatscroll);
		
		chatinput = new JTextField();
		chatinput.setBounds(655, 535, 175, 30);
		chatinput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		chatinput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.VK_ENTER) {
					if(!(chatinput.getText().equals(""))) {
						chatwindow.append(" User 1: " + chatinput.getText() + newline);
						chatinput.setText("");
					}
				}
			}
		});
		container.add(chatinput);
		
		chatsend = new JButton();
		chatsend.setBounds(830, 535, 60, 30);
		chatsend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!(chatinput.getText().equals(""))) {
					chatwindow.append(" User 1: " + chatinput.getText() + newline);
					chatinput.setText("");
					chatinput.grabFocus();
				}
			}
		});
		container.add(chatsend);
		

		List<Color> colors = new ArrayList<Color>() {{
			add(Color.BLACK); 
			add(Color.DARK_GRAY); 
			add(Color.GRAY); 
			add(Color.LIGHT_GRAY); 
			add(Color.WHITE); 
			add(Color.BLUE); 
			add(Color.CYAN); 
			add(Color.GREEN); 
			add(Color.MAGENTA); 
			add(Color.YELLOW);
			add(Color.ORANGE); 
			add(Color.RED); 
			add(Color.PINK); 
		}};
		color = new ButtonGroup();
		Point buttontopleft = new Point(5, 5);
		
		for(Color tempcolor : colors) {
			JRadioButton temp = new JRadioButton();
			temp.setBounds(buttontopleft.x, buttontopleft.y, 50, 20);
			temp.setBackground(tempcolor);
			temp.addActionListener(new ColorButtonListener(tempcolor));
			buttontopleft.y += 20;
			color.add(temp);
			container.add(temp);
		}
		
		int[] widths = new int[]{3, 6, 12, 24};
		color = new ButtonGroup();
		buttontopleft.y += 10;
		
		for(int width : widths) {
			JRadioButton temp = new JRadioButton();
			temp.setBounds(buttontopleft.x, buttontopleft.y, 50, 20);
			temp.setText("" + width);
			temp.addActionListener(new WidthButtonListener(width));
			buttontopleft.y += 20;
			color.add(temp);
			container.add(temp);
		}

		setContentPane(container);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	class ColorButtonListener implements ActionListener {
		
		private Color drawColor;
		
		public ColorButtonListener(Color color) {
			this.drawColor = color;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			paintarea.setDrawColor(drawColor);
		}
	}
	
	class WidthButtonListener implements ActionListener {
		private int drawWidth;
		
		public WidthButtonListener(int width) {
			this.drawWidth = width;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			paintarea.setDrawWidth(drawWidth);
		}
	}
}
