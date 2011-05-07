/* LTextField - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

public class LTextField extends Component
{
    private String Title;
    private String Text;
    private int Gap = 3;
    private boolean edit = true;
    private Color Bk;
    private Color BkD;
    private Color Fr;
    private boolean isPress = false;
    private Dimension size = null;
    private ActionListener actionListener = null;
    
    public LTextField() {
	this("", "");
    }
    
    public LTextField(String string) {
	this(string, "");
    }
    
    public LTextField(String string, String string_0_) {
	this.enableEvents(17L);
	setBk(Color.white);
	setFr(new Color(5263480));
	setText(string);
	setTitle(string_0_);
    }
    
    public void addActionListener(ActionListener actionlistener) {
	actionListener = actionlistener;
    }
    
    public boolean getEdit() {
	return edit;
    }
    
    public Dimension getMinimumSize() {
	return getPreferredSize();
    }
    
    public Dimension getPreferredSize() {
	Font font = this.getFont();
	if (font == null)
	    return size;
	FontMetrics fontmetrics = this.getFontMetrics(font);
	if (fontmetrics == null)
	    return size;
	int i = Gap * 4;
	if (Text != null)
	    i += fontmetrics.stringWidth(Text);
	else
	    i += 10;
	if (Title != null)
	    i += fontmetrics.stringWidth(Title);
	else
	    i += 10;
	return new Dimension(i, (fontmetrics.getMaxAscent()
				 + fontmetrics.getMaxDescent() + Gap * 2));
    }
    
    public Dimension getSize() {
	if (size == null)
	    size = super.getSize();
	return size;
    }
    
    public String getText() {
	return Text == null ? "" : Text;
    }
    
    public String getTitle() {
	return Title == null ? "" : Title;
    }
    
    public void paint(Graphics graphics) {
	try {
	    FontMetrics fontmetrics = graphics.getFontMetrics();
	    int i = Gap;
	    int i_1_ = fontmetrics.getMaxAscent() + 1;
	    if (Title.length() > 0) {
		int i_2_ = fontmetrics.stringWidth(Title);
		graphics.drawString(Title, Gap, Gap + i_1_);
		i += Gap + i_2_;
	    }
	    Dimension dimension = getSize();
	    Awt.fillFrame(graphics, isPress, i, 0, dimension.width - i,
			  dimension.height);
	    if (Text.length() > 0) {
		graphics.setColor(Fr);
		graphics.drawString(Text, i + Gap, Gap + i_1_);
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    protected void processEvent(AWTEvent awtevent) {
	try {
	    int i = awtevent.getID();
	    if (awtevent instanceof MouseEvent) {
		MouseEvent mouseevent = (MouseEvent) awtevent;
		mouseevent.consume();
		if (edit && i == 501) {
		    isPress = true;
		    this.repaint();
		}
		if (edit && i == 502) {
		    mouseevent.consume();
		    this.repaint();
		    isPress = false;
		    Point point = mouseevent.getPoint();
		    if (this.contains(point)) {
			String string = getText();
			Point point_3_ = this.getLocationOnScreen();
			point.translate(point_3_.x, point_3_.y);
			setText(MessageBox.getString(getText(), getTitle()));
			if (!string.equals(getText())
			    && actionListener != null)
			    actionListener.actionPerformed
				(new ActionEvent(this, 1001, getText()));
		    }
		}
	    }
	    if (awtevent instanceof ComponentEvent && (i == 101 || i == 102))
		size = null;
	    super.processEvent(awtevent);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void pMouse(MouseEvent mouseevent) {
	/* empty */
    }
    
    public void setBk(Color color) {
	Bk = color;
	BkD = color.darker();
    }
    
    public void setEdit(boolean bool) {
	edit = bool;
    }
    
    public void setFr(Color color) {
	Fr = color;
    }
    
    public void setGap(int i) {
	Gap = i;
    }
    
    public void setText(String string) {
	Text = string;
	this.invalidate();
	java.awt.Container container = this.getParent();
	if (container != null)
	    container.validate();
	if (this.isShowing())
	    this.repaint();
    }
    
    public void setTitle(String string) {
	Title = string;
	if (this.isShowing())
	    this.repaint();
    }
    
    public void update(Graphics graphics) {
	try {
	    paint(graphics);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
}
