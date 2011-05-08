package syi.util;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URL;

public class LabelLink extends Label
{

    private String strLink;
    private boolean isMouse;
    private Color clLink;
    private Color clBack;
    private Applet applet;

    public LabelLink()
    {
        strLink = null;
        isMouse = false;
    }

    public LabelLink(String s)
    {
        super(s);
        strLink = null;
        isMouse = false;
    }

    public LabelLink(String s, int i)
    {
        super(s, i);
        strLink = null;
        isMouse = false;
    }

    private void init()
    {
        if(isMouse)
        {
            return;
        } else
        {
            isMouse = true;
            enableEvents(48L);
            clBack = getForeground();
            return;
        }
    }

    protected void processMouseEvent(MouseEvent mouseevent)
    {
        processMouseMotionEvent(mouseevent);
    }

    protected void processMouseMotionEvent(MouseEvent mouseevent)
    {
        if(!isMouse)
        {
            return;
        }
        try
        {
            switch(mouseevent.getID())
            {
            case 504: 
                setFont(true);
                break;

            case 505: 
                setFont(false);
                break;

            case 501: 
                applet.getAppletContext().showDocument(new URL(strLink), "top");
                break;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void setFont(boolean flag)
    {
        Font font = getFont();
        font = new Font(font.getName(), flag ? 2 : 0, font.getSize());
        setFont(font);
        setForeground(flag ? clLink : clBack);
    }

    public void setLink(Applet applet1, String s, Color color)
    {
        if(s == null || s.length() <= 0)
        {
            return;
        } else
        {
            init();
            strLink = s;
            clLink = color;
            applet = applet1;
            return;
        }
    }
}
