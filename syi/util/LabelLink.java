/* LabelLink - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.net.URL;

public class LabelLink extends Label
{
    private String strLink = null;
    private boolean isMouse = false;
    private Color clLink;
    private Color clBack;
    private Applet applet;
    
    public LabelLink() {
	/* empty */
    }
    
    public LabelLink(String string) {
	super(string);
    }
    
    public LabelLink(String string, int i) {
	super(string, i);
    }
    
    private void init() {
	if (!isMouse) {
	    isMouse = true;
	    this.enableEvents(48L);
	    clBack = this.getForeground();
	}
    }
    
    protected void processMouseEvent(MouseEvent mouseevent) {
	processMouseMotionEvent(mouseevent);
    }
    
    protected void processMouseMotionEvent(MouseEvent mouseevent) {
	if (isMouse) {
	    try {
		switch (mouseevent.getID()) {
		case 504:
		    setFont(true);
		    break;
		case 505:
		    setFont(false);
		    break;
		case 501:
		    applet.getAppletContext().showDocument(new URL(strLink),
							   "top");
		    break;
		}
	    } catch (Throwable throwable) {
		throwable.printStackTrace();
	    }
	}
    }
    
    public void setFont(boolean bool) {
	Font font = this.getFont();
	font = new Font(font.getName(), bool ? 2 : 0, font.getSize());
	this.setFont(font);
	this.setForeground(bool ? clLink : clBack);
    }
    
    public void setLink(Applet applet, String string, Color color) {
	if (string != null && string.length() > 0) {
	    init();
	    strLink = string;
	    clLink = color;
	    this.applet = applet;
	}
    }
}
