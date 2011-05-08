package syi.awt;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class LPopup extends Component
{

    private String strs[];
    private int seek;
    private ActionListener listener;
    private int minWidth;

    public LPopup()
    {
        strs = null;
        seek = 0;
        listener = null;
        minWidth = 10;
        enableEvents(48L);
        setVisible(false);
    }

    public void add(String s)
    {
        if(strs == null || seek >= strs.length)
        {
            String as[] = new String[(int)((double)(seek + 1) * 1.5D)];
            if(strs != null)
            {
                for(int i = 0; i < strs.length; i++)
                {
                    as[i] = strs[i];
                }

            }
            strs = as;
        }
        strs[seek++] = s;
        try
        {
            FontMetrics fontmetrics = getFontMetrics(getFont());
            minWidth = Math.max(fontmetrics.stringWidth(s), minWidth);
        }
        catch(RuntimeException _ex) { }
    }

    public void add(String s, int i)
    {
    }

    public void addActionListener(ActionListener actionlistener)
    {
    }

    public Dimension getPreferredSize()
    {
        try
        {
            FontMetrics fontmetrics = getFontMetrics(getFont());
            return new Dimension(minWidth, fontmetrics.getMaxDescent() + fontmetrics.getMaxAscent());
        }
        catch(RuntimeException _ex)
        {
            return new Dimension(10, 10);
        }
    }

    public void paint(Graphics g)
    {
        try
        {
            Dimension dimension = getSize();
            g.setColor(getForeground());
            g.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
            g.setColor(Color.white);
            g.fillRect(1, 1, dimension.width - 2, dimension.height - 2);
        }
        catch(Throwable _ex) { }
    }

    protected void processMouseEvent(MouseEvent mouseevent)
    {
        int i;
        try
        {
            i = mouseevent.getID();
        }
        catch(Throwable _ex) { }
    }

    public void removeAt(int i)
    {
    }

    public void update(Graphics g)
    {
        paint(g);
    }
}
