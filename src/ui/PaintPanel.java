package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PaintPanel extends JPanel {
	
	private MouseHandler mouseHandler = new MouseHandler();
	
	private UI ui;
	
	private Image image;
	private Graphics2D g2d;
	
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
    		ui.sendDrawWidth(width);
    	}
		this.drawWidth = width;
    }
    
    public void setDrawColor(Color color) {
    	if(myturn) {
    		ui.sendDrawColor(color.getRGB());
    	}
		this.drawColor = color;
    }
    
    /**
     * Leert das Zeichenfeld
     */
    public void clearPanel () {
    	now = null;
    	last = null;
    	g2d.setColor(Color.WHITE);
    	g2d.fillRect(0, 0, getWidth(), getHeight());
    	repaint();
    }

    /**
     * Setzt die Linie zum eingegeben Punkt fort
     * @param p Der zuletzt augenommene Punkt
     */
    public void draw (Point p, boolean isDragging) {
    	last = now;
    	now = p;
    	if (g2d != null) {
    		g2d.setStroke(new BasicStroke(drawWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
    		g2d.setColor(drawColor);
    		if(isDragging && last != null) {
    			g2d.draw(new Line2D.Float(now.x, now.y, last.x, last.y));
    		}
    		else {
    			g2d.draw(new Line2D.Float(now.x, now.y, now.x, now.y));
    		}
    	}
    	if (myturn) {
    		ui.sendPoint(now.x, now.y, isDragging);
    	}
    	repaint();
    }
    
    /**
     * Übergibt, ob dieser Spieler am Zug ist.
     * @param myturn
     */
    public void setMyTurn(boolean myturn) {
    	this.myturn = myturn;
    	if(myturn) {
    		ui.sendDrawColor(drawColor.getRGB());
    		ui.sendDrawWidth(drawWidth);
    	}
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    	if(image == null){
			image = createImage(getSize().width, getSize().height);
			g2d = (Graphics2D)image.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clearPanel();

		}
    	g.drawImage(image, 0, 0, null);
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
        	if(drawing && myturn) {
        		draw(e.getPoint(), false);
        	}
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        	if(drawing && myturn) {
        		draw(e.getPoint(), false);
        	}
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        	if(drawing && myturn) {
        		draw(e.getPoint(), true);
        	}
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
        	drawing = true;
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
        	drawing = false;
        	now = null;
        	last = null;
        }
    }

}
