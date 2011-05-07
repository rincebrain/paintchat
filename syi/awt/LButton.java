/* LButton - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

public class LButton extends Canvas
{
    private Dimension size = null;
    private Image BackImage = null;
    private String Text = null;
    private ActionListener listener = null;
    int Gap = 3;
    int textWidth = -1;
    boolean isPress = false;
    Color dkBackColor = Color.darkGray;
    
    public LButton() {
	this((String) null);
    }
    
    public LButton(String string) {
	this.enableEvents(17L);
	setBackground(new Color(13619151));
	setText(string);
    }
    
    public void addActionListener(ActionListener actionlistener) {
	listener = actionlistener;
    }
    
    private void doAction() {
	if (listener != null)
	    listener.actionPerformed(new ActionEvent(this, 1001, getText()));
    }
    
    public Image getBackImage() {
	return BackImage;
    }
    
    public int getGap() {
	return Gap;
    }
    
    public Dimension getMinimumSize() {
	return getPreferredSize();
    }
    
    public Dimension getPreferredSize() {
	int i = 50;
	int i_0_ = 10;
	if (BackImage != null) {
	    i = BackImage.getWidth(null);
	    i_0_ = BackImage.getHeight(null);
	} else {
	    java.awt.Font font = this.getFont();
	    int i_1_ = Gap * 2;
	    if (font != null) {
		FontMetrics fontmetrics = this.getFontMetrics(this.getFont());
		if (fontmetrics != null)
		    i_0_ = (fontmetrics.getMaxAscent()
			    + fontmetrics.getMaxDescent());
		if (Text != null)
		    i = fontmetrics.stringWidth(Text) + i_1_;
	    }
	    i_0_ += i_1_;
	}
	return new Dimension(i, i_0_);
    }
    
    public Dimension getSize() {
	if (size == null)
	    size = super.getSize();
	return size;
    }
    
    public String getText() {
	return Text == null ? "" : Text;
    }
    
    public void paint(Graphics graphics) {
	try {
	    Dimension dimension = getSize();
	    if (BackImage != null) {
		Awt.drawFrame(graphics, isPress, 0, 0, dimension.width,
			      dimension.height);
		graphics.drawImage(BackImage, 1, 1, null);
	    } else
		Awt.fillFrame(graphics, isPress, 0, 0, dimension.width,
			      dimension.height);
	    if (Text != null) {
		FontMetrics fontmetrics = graphics.getFontMetrics();
		if (textWidth == -1)
		    textWidth = fontmetrics.stringWidth(Text);
		graphics.setColor(this.getForeground());
		graphics.drawString(Text, (size.width - textWidth) / 2,
				    fontmetrics.getMaxAscent() + Gap + 1);
	    }
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    protected void processEvent(AWTEvent awtevent) {
	try {
	    int i = awtevent.getID();
	    if (awtevent instanceof MouseEvent) {
		MouseEvent mouseevent = (MouseEvent) awtevent;
		mouseevent.consume();
		if (i == 501) {
		    isPress = true;
		    this.repaint();
		}
		if (i == 502) {
		    if (this.contains(((MouseEvent) awtevent).getPoint()))
			doAction();
		    isPress = false;
		    this.repaint();
		}
	    } else if (awtevent instanceof ComponentEvent) {
		if (i == 101 || i == 102) {
		    size = null;
		    this.repaint();
		}
	    } else
		super.processEvent(awtevent);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void resetSize() {
	super.setSize(getPreferredSize());
    }
    
    public void setBackground(Color color) {
	if (color != null) {
	    dkBackColor = color.darker();
	    super.setBackground(color);
	}
    }
    
    public void setBackImage(Image image) {
	BackImage = image;
	if (this.isShowing())
	    this.repaint();
    }
    
    public void setGap(int i) {
	Gap = i;
	resetSize();
    }
    
    public void setText(String string) {
	Text = string;
	textWidth = -1;
	this.invalidate();
	Component component = Awt.getParent(this);
	if (component != null)
	    component.validate();
	if (this.isShowing())
	    this.repaint();
    }
    
    public void update(Graphics graphics) {
	paint(graphics);
    }
}
