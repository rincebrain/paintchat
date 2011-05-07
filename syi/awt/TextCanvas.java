/* TextCanvas - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.CharArrayWriter;

public class TextCanvas extends Canvas
{
    String[] strs = null;
    int seek = 0;
    CharArrayWriter buffer;
    Dimension d;
    private int Gap;
    public boolean isBorder;
    
    public TextCanvas() {
	buffer = new CharArrayWriter();
	d = new Dimension();
	Gap = 3;
	isBorder = true;
    }
    
    public TextCanvas(String string) {
	buffer = new CharArrayWriter();
	d = new Dimension();
	Gap = 3;
	isBorder = true;
	setText(string);
    }
    
    public synchronized void addText(String string) {
	if (strs == null)
	    strs = new String[6];
	else if (seek >= strs.length) {
	    String[] strings = new String[strs.length * 2];
	    System.arraycopy(strs, 0, strings, 0, strs.length);
	    strs = strings;
	}
	strs[seek++] = string;
    }
    
    public Dimension getPreferredSize() {
	if (strs == null || seek == 0)
	    return new Dimension(50, 10);
	try {
	    FontMetrics fontmetrics = this.getFontMetrics(this.getFont());
	    int i = 0;
	    int i_0_ = Gap * 2;
	    for (int i_1_ = 0; i_1_ < seek; i_1_++)
		i = Math.max(i, fontmetrics.stringWidth(strs[i_1_]));
	    return new Dimension(i + i_0_ + 4, (fontmetrics.getMaxDescent()
						+ fontmetrics.getMaxAscent()
						+ Gap) * seek + 4);
	} catch (RuntimeException runtimeexception) {
	    return new Dimension(50, 10);
	}
    }
    
    public void paint(Graphics graphics) {
	try {
	    Dimension dimension = this.getSize();
	    graphics.clearRect(1, 1, dimension.width - 2,
			       dimension.height - 2);
	    if (isBorder)
		graphics.drawRect(0, 0, dimension.width - 1,
				  dimension.height - 1);
	    FontMetrics fontmetrics = graphics.getFontMetrics();
	    int i = (fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent()
		     + Gap);
	    int i_2_ = fontmetrics.getMaxAscent();
	    for (int i_3_ = 0; i_3_ < seek; i_3_++)
		graphics.drawString(strs[i_3_], Gap + 2,
				    i * i_3_ + i_2_ + Gap + 2);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void reset() {
	if (strs != null) {
	    for (int i = 0; i < seek; i++)
		strs[i] = null;
	    seek = 0;
	}
    }
    
    public final void setText(String string) {
	if (string != null) {
	    CharArrayWriter chararraywriter = buffer;
	    CharArrayWriter chararraywriter_4_;
/*	    MONITORENTER (chararraywriter_4_ = chararraywriter);
	    MISSING MONITORENTER*/
	    synchronized (chararraywriter_4_) {
		chararraywriter.reset();
		reset();
		int i = string.length();
		for (int i_5_ = 0; i_5_ < i; i_5_++) {
		    char c = string.charAt(i_5_);
		    if (c == '\r' || c == '\n') {
			if (chararraywriter.size() > 0) {
			    addText(chararraywriter.toString());
			    chararraywriter.reset();
			}
		    } else
			chararraywriter.write(c);
		}
		if (chararraywriter.size() > 0)
		    addText(chararraywriter.toString());
	    }
	    this.setSize(getPreferredSize());
	}
    }
    
    public void update(Graphics graphics) {
	paint(graphics);
    }
}
