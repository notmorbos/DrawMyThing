package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Interface extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final String newline = "\n";
	
	private JPanel container;
	
	private JTextArea chatwindow;
	private JTextField chatinput;
	private PaintPanel paintarea;
	
	private Point point;

	Interface() {
		
	}
	
	public void initWindow() {
		
		setTitle("Draw My Thing!");
		setBounds(0,0,900,600);
		
		container = new JPanel();
		container.setLayout(null);
		
		paintarea = new PaintPanel();
		paintarea.setBounds(5, 5, 645, 525);
		paintarea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		paintarea.setBackground(Color.WHITE);
		container.add(paintarea);
		
		chatwindow = new JTextArea();
		chatwindow.setBounds(655, 5, 235, 525);
		chatwindow.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		chatwindow.setEditable(false);
		chatwindow.setLineWrap(true);
		chatwindow.setText(" Willkommen im Chat!" + newline);
		container.add(chatwindow);
		
		chatinput = new JTextField();
		chatinput.setBounds(655, 535, 235, 30);
		chatinput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		chatinput.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.VK_ENTER) {
					chatwindow.append(" User 1: " + chatinput.getText() + newline);
					chatinput.setText("");
				}
			}
		});
		container.add(chatinput);

		setContentPane(container);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
