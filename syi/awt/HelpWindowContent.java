/* HelpWindowContent - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.awt.Image;
import java.awt.Point;
import java.util.Hashtable;

public class HelpWindowContent
{
    public Point point;
    public String string = null;
    public Image image = null;
    boolean isResource = false;
    public Hashtable res;
    public int timeStart = 2000;
    public int timeEnd = 15000;
    private boolean isEnableVisited = false;
    private boolean isVisit = false;
    
    public HelpWindowContent(Image image, String string, boolean bool,
			     Point point, Hashtable hashtable) {
	this.point = point;
	this.string = string;
	res = hashtable;
	isResource = bool;
	this.image = image;
    }
    
    public HelpWindowContent(String string, boolean bool, Point point,
			     Hashtable hashtable) {
	this.point = point;
	this.string = string;
	res = hashtable;
	isResource = bool;
    }
    
    public String getText() {
	if (this.string == null || this.string.length() == 0)
	    return "";
	if (isResource) {
	    String string = (String) res.get(this.string + "_Com");
	    return string == null ? res.get(this.string).toString() : string;
	}
	return this.string;
    }
    
    public boolean isVisible(boolean bool) {
	return !isEnableVisited ? bool : isVisit;
    }
    
    public void setText(String string, boolean bool) {
	this.string = string;
	isResource = bool;
    }
    
    public void setVisit(boolean bool) {
	isEnableVisited = true;
	isVisit = bool;
    }
}
