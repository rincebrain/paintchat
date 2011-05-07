/* LPopup - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class LPopup extends Component
{
    private String[] strs = null;
    private int seek = 0;
    private ActionListener listener = null;
    private int minWidth = 10;
    
    public LPopup() {
	this.enableEvents(48L);
	this.setVisible(false);
    }
    
    public void add(String string) {
	if (strs == null || seek >= strs.length) {
	    String[] strings = new String[(int) ((double) (seek + 1) * 1.5)];
	    if (strs != null) {
		for (int i = 0; i < strs.length; i++)
		    strings[i] = strs[i];
	    }
	    strs = strings;
	}
	strs[seek++] = string;
	try {
	    FontMetrics fontmetrics = this.getFontMetrics(this.getFont());
	    minWidth = Math.max(fontmetrics.stringWidth(string), minWidth);
	} catch (RuntimeException runtimeexception) {
	    /* empty */
	}
    }
    
    public void add(String string, int i) {
	/* empty */
    }
    
    public void addActionListener(ActionListener actionlistener) {
	/* empty */
    }
    
    public Dimension getPreferredSize() {
	try {
	    FontMetrics fontmetrics = this.getFontMetrics(this.getFont());
	    return new Dimension(minWidth, (fontmetrics.getMaxDescent()
					    + fontmetrics.getMaxAscent()));
	} catch (RuntimeException runtimeexception) {
	    return new Dimension(10, 10);
	}
    }
    
    public void paint(Graphics graphics) {
	try {
	    Dimension dimension = this.getSize();
	    graphics.setColor(this.getForeground());
	    graphics.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
	    graphics.setColor(Color.white);
	    graphics.fillRect(1, 1, dimension.width - 2, dimension.height - 2);
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    protected void processMouseEvent(MouseEvent mouseevent) {
	try {
	    int i = mouseevent.getID();
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    public void removeAt(int i) {
	/* empty */
    }
    
    public void update(Graphics graphics) {
	paint(graphics);
    }
}
