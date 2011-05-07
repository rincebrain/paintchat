/* ImageCanvas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

import syi.util.Io;

public class ImageCanvas extends Canvas
{
    private Image image = null;
    private String string = null;
    private int imW = 0;
    private int imH = 0;
    
    public ImageCanvas(Color color, Color color_0_) {
	this.setBackground(color);
    }
    
    public Dimension getPreferredSize() {
	return new Dimension(imW + 2, imH + 2);
    }
    
    public void paint(Graphics graphics) {
	try {
	    Dimension dimension = this.getSize();
	    graphics.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
	    if (image != null)
		graphics.drawImage(image, 1, 1, null);
	    else
		graphics.clearRect(1, 1, dimension.width - 2,
				   dimension.height - 2);
	    if (string != null) {
		FontMetrics fontmetrics = graphics.getFontMetrics();
		graphics.drawString(string, 10,
				    fontmetrics.getMaxAscent() + 10);
	    }
	} catch (RuntimeException runtimeexception) {
	    /* empty */
	}
    }
    
    public synchronized void reset() {
	if (image != null) {
	    image.flush();
	    image = null;
	    imW = 0;
	    imH = 0;
	}
	string = null;
    }
    
    public void setImage(Image image) {
	this.image = image;
	imW = image.getWidth(null);
	imH = image.getHeight(null);
	this.setSize(imW + 2, imH + 2);
    }
    
    public void setImage(String string) {
	if (string != null && string.length() > 0)
	    setImage(Io.loadImageNow(this, string));
    }
    
    public void setText(String string) {
	this.string = string;
    }
    
    public void update(Graphics graphics) {
	paint(graphics);
    }
}
