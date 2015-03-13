package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

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
    
    private int drawWidthSave;
    private Color drawColorSave;
    
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
        this.drawWidthSave = drawWidth;
        this.drawColorSave = drawColor;
    }
    
    public void setDrawWidth(int width) {
    	if(myturn) {
    		ui.sendDrawWidth(width);
    	}
		this.drawWidth = width;
		g2d.setStroke(new BasicStroke(drawWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
    }
    
    public void setDrawColor(Color color) {
    	if(myturn) {
    		ui.sendDrawColor(color.getRGB());
    	}
		this.drawColor = color;
		g2d.setColor(drawColor);
    }
    
    /**
     * Leert das Zeichenfeld
     */
    public void clearPanel () {
    	now = null;
    	last = null;
    	g2d.setColor(Color.WHITE);
    	g2d.fillRect(0, 0, getWidth(), getHeight());
    	g2d.setColor(drawColor);
    	repaint();
    }

    /**
     * Setzt die Linie zum eingegeben Punkt fort
     * @param p Der zuletzt augenommene Punkt
     */
    public void draw (Point p, boolean isDragging) {
    	last = now;
    	now = p;
    	if(p.x == -1) {
    		last = null;
    	}
    	else if (g2d != null) {
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
    	if(p.x == -1) {
    		now = null;
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
    		drawColor = drawColorSave;
    		drawWidth = drawWidthSave;
    		ui.sendDrawColor(drawColor.getRGB());
    		ui.sendDrawWidth(drawWidth);
    		g2d.setColor(drawColor);
    		g2d.setStroke(new BasicStroke(drawWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
    	}
    	else {
    		drawColorSave = drawColor;
    		drawWidthSave = drawWidth;
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
        	draw(new Point(-1, -1), false);
        	drawing = false;
        }
    }
    
    //UNFERTIG
    public void saveImage(String currentplayer) {
        BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D bg2d = bimage.createGraphics();
        bg2d.drawImage(image, 0, 0, null);
        bg2d.dispose();

    	File output = new File("C:/Users/Markus/Pictures/" + currentplayer + Math.random() + ".png");
    	try {
        	output.createNewFile();
			ImageIO.write(bimage, "png", output);
		} catch (IOException e) {
			
		}
    }
}
