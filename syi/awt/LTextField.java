package syi.awt;

import java.awt.*;
import java.awt.event.*;

// Referenced classes of package syi.awt:
//            Awt, MessageBox

public class LTextField extends Component
{

    private String Title;
    private String Text;
    private int Gap;
    private boolean edit;
    private Color Bk;
    private Color BkD;
    private Color Fr;
    private boolean isPress;
    private Dimension size;
    private ActionListener actionListener;

    public LTextField()
    {
        this("", "");
    }

    public LTextField(String s)
    {
        this(s, "");
    }

    public LTextField(String s, String s1)
    {
        Gap = 3;
        edit = true;
        isPress = false;
        size = null;
        actionListener = null;
        enableEvents(17L);
        setBk(Color.white);
        setFr(new Color(0x505078));
        setText(s);
        setTitle(s1);
    }

    public void addActionListener(ActionListener actionlistener)
    {
        actionListener = actionlistener;
    }

    public boolean getEdit()
    {
        return edit;
    }

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    public Dimension getPreferredSize()
    {
        java.awt.Font font = getFont();
        if(font == null)
        {
            return size;
        }
        FontMetrics fontmetrics = getFontMetrics(font);
        if(fontmetrics == null)
        {
            return size;
        }
        int i = Gap * 4;
        if(Text != null)
        {
            i += fontmetrics.stringWidth(Text);
        } else
        {
            i += 10;
        }
        if(Title != null)
        {
            i += fontmetrics.stringWidth(Title);
        } else
        {
            i += 10;
        }
        return new Dimension(i, fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent() + Gap * 2);
    }

    public Dimension getSize()
    {
        if(size == null)
        {
            size = super.getSize();
        }
        return size;
    }

    public String getText()
    {
        return Text != null ? Text : "";
    }

    public String getTitle()
    {
        return Title != null ? Title : "";
    }

    public void paint(Graphics g)
    {
        try
        {
            FontMetrics fontmetrics = g.getFontMetrics();
            int i = Gap;
            int j = fontmetrics.getMaxAscent() + 1;
            if(Title.length() > 0)
            {
                int k = fontmetrics.stringWidth(Title);
                g.drawString(Title, Gap, Gap + j);
                i += Gap + k;
            }
            Dimension dimension = getSize();
            Awt.fillFrame(g, isPress, i, 0, dimension.width - i, dimension.height);
            if(Text.length() > 0)
            {
                g.setColor(Fr);
                g.drawString(Text, i + Gap, Gap + j);
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    protected void processEvent(AWTEvent awtevent)
    {
        try
        {
            int i = awtevent.getID();
            if(awtevent instanceof MouseEvent)
            {
                MouseEvent mouseevent = (MouseEvent)awtevent;
                mouseevent.consume();
                if(edit && i == 501)
                {
                    isPress = true;
                    repaint();
                }
                if(edit && i == 502)
                {
                    mouseevent.consume();
                    repaint();
                    isPress = false;
                    Point point = mouseevent.getPoint();
                    if(contains(point))
                    {
                        String s = getText();
                        Point point1 = getLocationOnScreen();
                        point.translate(point1.x, point1.y);
                        setText(MessageBox.getString(getText(), getTitle()));
                        if(!s.equals(getText()) && actionListener != null)
                        {
                            actionListener.actionPerformed(new ActionEvent(this, 1001, getText()));
                        }
                    }
                }
            }
            if((awtevent instanceof ComponentEvent) && (i == 101 || i == 102))
            {
                size = null;
            }
            super.processEvent(awtevent);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void pMouse(MouseEvent mouseevent)
    {
    }

    public void setBk(Color color)
    {
        Bk = color;
        BkD = color.darker();
    }

    public void setEdit(boolean flag)
    {
        edit = flag;
    }

    public void setFr(Color color)
    {
        Fr = color;
    }

    public void setGap(int i)
    {
        Gap = i;
    }

    public void setText(String s)
    {
        Text = s;
        invalidate();
        java.awt.Container container = getParent();
        if(container != null)
        {
            container.validate();
        }
        if(isShowing())
        {
            repaint();
        }
    }

    public void setTitle(String s)
    {
        Title = s;
        if(isShowing())
        {
            repaint();
        }
    }

    public void update(Graphics g)
    {
        try
        {
            paint(g);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }
}
