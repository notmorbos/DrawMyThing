package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PaintPanel extends JPanel {
	
	private MouseHandler mouseHandler = new MouseHandler();
    private Point now;
    private Point last;
    private int drawWidth;
    private Color drawColor;
    private boolean drawing;

    public PaintPanel() {
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
        drawWidth = 6;
        drawColor = Color.BLACK;
    }
    
    public void setDrawWidth(int width) {
    	this.drawWidth = width;
    }
    
    public void setDrawColor(Color color) {
    	this.drawColor = color;
    }

    private void draw (Point p) {
		last = now;
    	now = p;
    	repaint(now.x - drawWidth/2, now.y - drawWidth/2, drawWidth, drawWidth);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(drawColor);
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(8,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        if(now != null && last != null) {
        	g.fillRect(now.x - drawWidth/2, now.y - drawWidth/2, now.x + drawWidth/2, now.y + drawWidth/2);
        }
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
        	if(drawing) {
        		draw(e.getPoint());
        	}
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        	if(drawing) {
        		draw(e.getPoint());
        	}
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        	if(drawing) {
        		draw(e.getPoint());
        	}
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
        	drawing = true;
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
        	drawing = false;
        }
    }

}
