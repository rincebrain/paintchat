/* ConfigApplet - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat.config;
import java.applet.Applet;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import paintchat.Res;
import paintchat.Resource;

import syi.applet.ServerStub;
import syi.awt.HelpWindow;
import syi.awt.HelpWindowContent;
import syi.awt.LButton;
import syi.awt.LTextField;

public class ConfigApplet extends Applet
    implements MouseListener, FocusListener
{
    Res res = Resource.loadResource("Config");
    private HelpWindow helpWindow = null;
    
    public void focusGained(FocusEvent focusevent) {
	/* empty */
    }
    
    public void focusLost(FocusEvent focusevent) {
	getHelp().reset();
    }
    
    protected boolean getBool(String string) {
	if (string == null || string.length() <= 0)
	    return false;
	char c = Character.toLowerCase(string.charAt(0));
	switch (c) {
	case '1':
	case 'o':
	case 't':
	case 'y':
	    return true;
	default:
	    return false;
	}
    }
    
    protected boolean getBool(String string, boolean bool) {
	if (string == null || string.length() <= 0)
	    return bool;
	char c = Character.toLowerCase(string.charAt(0));
	switch (c) {
	case '1':
	case 'o':
	case 't':
	case 'y':
	    return true;
	case '0':
	case 'c':
	case 'f':
	case 'n':
	    return false;
	default:
	    return bool;
	}
    }
    
    protected HelpWindow getHelp() {
	if (helpWindow == null) {
	    helpWindow = new HelpWindow((Frame) this.getParent().getParent());
	    helpWindow.setIsShow(getBool(this.getParameter("App_IsConsole"),
					 true));
	}
	return helpWindow;
    }
    
    public void getParameter(Component component) {
	if (component instanceof Container) {
	    Component[] components = ((Container) component).getComponents();
	    for (int i = 0; i < components.length; i++)
		getParameter(components[i]);
	} else if (component instanceof LTextField
		   || component instanceof Checkbox) {
	    String string = this.getParameter(component.getName());
	    if (string != null) {
		if (component instanceof LTextField)
		    ((LTextField) component).setText(string);
		if (component instanceof Checkbox)
		    ((Checkbox) component).setState(getBool(string));
	    }
	}
    }
    
    public void getResource(Res res, Component component) {
	if (component instanceof Container) {
	    Container container = (Container) component;
	    int i = container.getComponentCount();
	    for (int i_0_ = 0; i_0_ < i; i_0_++)
		getResource(res, container.getComponent(i_0_));
	} else {
	    String string = component.getName();
	    string = res.get(string, string);
	    if (component instanceof LTextField)
		((LTextField) component).setTitle(string);
	    else if (component instanceof LButton)
		((LButton) component).setText(string);
	    else if (component instanceof Checkbox)
		((Checkbox) component).setLabel(string);
	    else if (component instanceof Label) {
		Label label = (Label) component;
		label.setText(res.get(label.getText(), label.getText()));
	    }
	}
    }
    
    public void mouseClicked(MouseEvent mouseevent) {
	/* empty */
    }
    
    public void mouseEntered(MouseEvent mouseevent) {
	Component component = mouseevent.getComponent();
	if (!(component instanceof Container)) {
	    String string = component.getName();
	    if (string != null && string.length() > 0) {
		Point point = component.getLocationOnScreen();
		Point point_1_ = mouseevent.getPoint();
		point.translate(point_1_.x + 10, point_1_.y);
		getHelp().startHelp(new HelpWindowContent(string, true, point,
							  res));
	    }
	}
    }
    
    public void mouseExited(MouseEvent mouseevent) {
	getHelp().reset();
    }
    
    public void mousePressed(MouseEvent mouseevent) {
	/* empty */
    }
    
    public void mouseReleased(MouseEvent mouseevent) {
	/* empty */
    }
    
    public void setMouseListener(Component component,
				 MouseListener mouselistener) {
	if (component instanceof Container) {
	    Component[] components = ((Container) component).getComponents();
	    int i = components.length;
	    for (int i_2_ = 0; i_2_ < i; i_2_++)
		setMouseListener(components[i_2_], mouselistener);
	} else
	    component.addMouseListener(mouselistener);
    }
    
    protected void setParam(Component component, Hashtable hashtable) {
	if (component instanceof Container) {
	    Component[] components = ((Container) component).getComponents();
	    int i = components.length;
	    for (int i_3_ = 0; i_3_ < i; i_3_++)
		setParam(components[i_3_], hashtable);
	} else if (component instanceof LTextField)
	    hashtable.put(component.getName(),
			  ((LTextField) component).getText());
	else if (component instanceof Checkbox)
	    hashtable.put(component.getName(),
			  String.valueOf(((Checkbox) component).getState()));
	else if (component instanceof TextField)
	    hashtable.put(component.getName(),
			  ((TextField) component).getText());
    }
    
    public void setParameter(Component component) {
	syi.util.PProperties pproperties
	    = ((ServerStub) this.getAppletContext()).getHashTable();
	setParam(this, pproperties);
    }
}
