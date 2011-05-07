/* ToolBox - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat;
import java.applet.Applet;
import java.awt.Container;
import java.awt.Dimension;

import paintchat_client.Mi;

import syi.awt.LComponent;

public interface ToolBox
{
    public String getC();
    
    public LComponent[] getCs();
    
    public Dimension getCSize();
    
    public void init(Container container, Applet applet, Res res, Res res_0_,
		     Mi mi);
    
    public void lift();
    
    public void pack();
    
    public void selPix(boolean bool);
    
    public void setARGB(int i);
    
    public void setC(String string);
    
    public void setLineSize(int i);
    
    public void up();
}
