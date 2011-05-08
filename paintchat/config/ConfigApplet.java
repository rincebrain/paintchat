package paintchat.config;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import paintchat.Res;
import paintchat.Resource;
import syi.applet.ServerStub;
import syi.awt.*;

public class ConfigApplet extends Applet
    implements MouseListener, FocusListener
{

    Res res;
    private HelpWindow helpWindow;

    public ConfigApplet()
    {
        res = Resource.loadResource("Config");
        helpWindow = null;
    }

    public void focusGained(FocusEvent focusevent)
    {
    }

    public void focusLost(FocusEvent focusevent)
    {
        getHelp().reset();
    }

    protected boolean getBool(String s)
    {
        if(s == null || s.length() <= 0)
        {
            return false;
        }
        char c = Character.toLowerCase(s.charAt(0));
        switch(c)
        {
        case 49: // '1'
        case 111: // 'o'
        case 116: // 't'
        case 121: // 'y'
            return true;
        }
        return false;
    }

    protected boolean getBool(String s, boolean flag)
    {
        if(s == null || s.length() <= 0)
        {
            return flag;
        }
        char c = Character.toLowerCase(s.charAt(0));
        switch(c)
        {
        case 49: // '1'
        case 111: // 'o'
        case 116: // 't'
        case 121: // 'y'
            return true;

        case 48: // '0'
        case 99: // 'c'
        case 102: // 'f'
        case 110: // 'n'
            return false;
        }
        return flag;
    }

    protected HelpWindow getHelp()
    {
        if(helpWindow == null)
        {
            helpWindow = new HelpWindow((Frame)getParent().getParent());
            helpWindow.setIsShow(getBool(getParameter("App_IsConsole"), true));
        }
        return helpWindow;
    }

    public void getParameter(Component component)
    {
        if(component instanceof Container)
        {
            Component acomponent[] = ((Container)component).getComponents();
            for(int i = 0; i < acomponent.length; i++)
            {
                getParameter(acomponent[i]);
            }

        } else
        {
            if(!(component instanceof LTextField) && !(component instanceof Checkbox))
            {
                return;
            }
            String s = getParameter(component.getName());
            if(s == null)
            {
                return;
            }
            if(component instanceof LTextField)
            {
                ((LTextField)component).setText(s);
            }
            if(component instanceof Checkbox)
            {
                ((Checkbox)component).setState(getBool(s));
            }
        }
    }

    public void getResource(Res res1, Component component)
    {
        if(component instanceof Container)
        {
            Container container = (Container)component;
            int i = container.getComponentCount();
            for(int j = 0; j < i; j++)
            {
                getResource(res1, container.getComponent(j));
            }

        } else
        {
            String s = component.getName();
            s = res1.get(s, s);
            if(component instanceof LTextField)
            {
                ((LTextField)component).setTitle(s);
                return;
            }
            if(component instanceof LButton)
            {
                ((LButton)component).setText(s);
                return;
            }
            if(component instanceof Checkbox)
            {
                ((Checkbox)component).setLabel(s);
                return;
            }
            if(component instanceof Label)
            {
                Label label = (Label)component;
                label.setText(res1.get(label.getText(), label.getText()));
                return;
            }
        }
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
        Component component = mouseevent.getComponent();
        if(component instanceof Container)
        {
            return;
        }
        String s = component.getName();
        if(s != null && s.length() > 0)
        {
            Point point = component.getLocationOnScreen();
            Point point1 = mouseevent.getPoint();
            point.translate(point1.x + 10, point1.y);
            getHelp().startHelp(new HelpWindowContent(s, true, point, res));
        }
    }

    public void mouseExited(MouseEvent mouseevent)
    {
        getHelp().reset();
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void setMouseListener(Component component, MouseListener mouselistener)
    {
        if(component instanceof Container)
        {
            Component acomponent[] = ((Container)component).getComponents();
            int i = acomponent.length;
            for(int j = 0; j < i; j++)
            {
                setMouseListener(acomponent[j], mouselistener);
            }

        } else
        {
            component.addMouseListener(mouselistener);
        }
    }

    protected void setParam(Component component, Hashtable hashtable)
    {
        if(component instanceof Container)
        {
            Component acomponent[] = ((Container)component).getComponents();
            int i = acomponent.length;
            for(int j = 0; j < i; j++)
            {
                setParam(acomponent[j], hashtable);
            }

        } else
        {
            if(component instanceof LTextField)
            {
                hashtable.put(component.getName(), ((LTextField)component).getText());
                return;
            }
            if(component instanceof Checkbox)
            {
                hashtable.put(component.getName(), String.valueOf(((Checkbox)component).getState()));
                return;
            }
            if(component instanceof TextField)
            {
                hashtable.put(component.getName(), ((TextField)component).getText());
                return;
            }
        }
    }

    public void setParameter(Component component)
    {
        syi.util.PProperties pproperties = ((ServerStub)getAppletContext()).getHashTable();
        setParam(this, pproperties);
    }
}
