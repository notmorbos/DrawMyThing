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
	
	private UI ui;
	
	//aktueller Punkt und letzer Punkt
    private Point now;
    private Point last;
    
    //Stifteigenschaften
    private int drawWidth;
    private Color drawColor;
    
    //Befindet sich die Maus im Zeichenfeld?
    private boolean drawing;
    
    //Ist der Spieler am Zug?
    private boolean myturn;

    public PaintPanel(UI ui, Color standardcolor, int standardwidth) {
    	this.ui = ui;
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
        this.drawWidth = standardwidth;
        this.drawColor = standardcolor;
        this.myturn = true; //TODO: Entfernen, sobald der Server den Zug übergibt.
    }
    
    public void setDrawWidth(int width) {
    	if(myturn) {
    		//ui.sendDrawWidth(width);
    		this.drawWidth = width;
    	}
    }
    
    public void setDrawColor(Color color) {
    	if(myturn) {
    		//ui.sendDrawColor(color.getRGB());
    		this.drawColor = color;
    	}
    }
    
    /**
     * Leert das Zeichenfeld
     */
    public void clearPanel () {
    	now = null;
    	last = null;
    	repaint();
    }

    /**
     * Setzt die Linie zum eingegeben Punkt fort
     * @param p Der zuletzt augenommene Punkt
     */
    public void draw (Point p) {
		last = now;
    	now = p;
    	//ui.sendPoint(now.x, now.y);
    	repaint();
    }
    
    public void updateFromServer(Point p) {
    	if(!myturn) {
    		last = now;
        	now = p;
        	repaint(now.x - drawWidth/2, now.y - drawWidth/2, drawWidth, drawWidth);
    	}
    }
    
    /**
     * Übergibt, ob dieser Spieler am Zug ist.
     * @param myturn
     */
    public void setMyTurn(boolean myturn) {
    	this.myturn = myturn;
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }
    
    @Override
    public void paint(Graphics g) {
    	if(now == null || last == null) {
    		super.paint(g);
    	}
        if(now != null && last != null) {
            g.setColor(drawColor);
        	g.fillOval(now.x - drawWidth/2, now.y - drawWidth/2, drawWidth, drawWidth);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(8,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
        	if(drawing && myturn) {
        		draw(e.getPoint());
        	}
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        	if(drawing && myturn) {
        		draw(e.getPoint());
        	}
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        	if(drawing && myturn) {
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
