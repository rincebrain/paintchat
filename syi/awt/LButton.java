package syi.awt;

import java.awt.*;
import java.awt.event.*;

// Referenced classes of package syi.awt:
//            Awt

public class LButton extends Canvas
{

    private Dimension size;
    private Image BackImage;
    private String Text;
    private ActionListener listener;
    int Gap;
    int textWidth;
    boolean isPress;
    Color dkBackColor;

    public LButton()
    {
        this(null);
    }

    public LButton(String s)
    {
        size = null;
        BackImage = null;
        Text = null;
        listener = null;
        Gap = 3;
        textWidth = -1;
        isPress = false;
        dkBackColor = Color.darkGray;
        enableEvents(17L);
        setBackground(new Color(0xcfcfcf));
        setText(s);
    }

    public void addActionListener(ActionListener actionlistener)
    {
        listener = actionlistener;
    }

    private void doAction()
    {
        if(listener != null)
        {
            listener.actionPerformed(new ActionEvent(this, 1001, getText()));
        }
    }

    public Image getBackImage()
    {
        return BackImage;
    }

    public int getGap()
    {
        return Gap;
    }

    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }

    public Dimension getPreferredSize()
    {
        int i = 50;
        int j = 10;
        if(BackImage != null)
        {
            i = BackImage.getWidth(null);
            j = BackImage.getHeight(null);
        } else
        {
            java.awt.Font font = getFont();
            int k = Gap * 2;
            if(font != null)
            {
                FontMetrics fontmetrics = getFontMetrics(getFont());
                if(fontmetrics != null)
                {
                    j = fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent();
                }
                if(Text != null)
                {
                    i = fontmetrics.stringWidth(Text) + k;
                }
            }
            j += k;
        }
        return new Dimension(i, j);
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

    public void paint(Graphics g)
    {
        try
        {
            Dimension dimension = getSize();
            if(BackImage != null)
            {
                Awt.drawFrame(g, isPress, 0, 0, dimension.width, dimension.height);
                g.drawImage(BackImage, 1, 1, null);
            } else
            {
                Awt.fillFrame(g, isPress, 0, 0, dimension.width, dimension.height);
            }
            if(Text == null)
            {
                return;
            }
            FontMetrics fontmetrics = g.getFontMetrics();
            if(textWidth == -1)
            {
                textWidth = fontmetrics.stringWidth(Text);
            }
            g.setColor(getForeground());
            g.drawString(Text, (size.width - textWidth) / 2, fontmetrics.getMaxAscent() + Gap + 1);
        }
        catch(Throwable _ex) { }
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
                if(i == 501)
                {
                    isPress = true;
                    repaint();
                }
                if(i == 502)
                {
                    if(contains(((MouseEvent)awtevent).getPoint()))
                    {
                        doAction();
                    }
                    isPress = false;
                    repaint();
                }
                return;
            }
            if(awtevent instanceof ComponentEvent)
            {
                if(i == 101 || i == 102)
                {
                    size = null;
                    repaint();
                }
                return;
            }
            super.processEvent(awtevent);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void resetSize()
    {
        super.setSize(getPreferredSize());
    }

    public void setBackground(Color color)
    {
        if(color == null)
        {
            return;
        } else
        {
            dkBackColor = color.darker();
            super.setBackground(color);
            return;
        }
    }

    public void setBackImage(Image image)
    {
        BackImage = image;
        if(isShowing())
        {
            repaint();
        }
    }

    public void setGap(int i)
    {
        Gap = i;
        resetSize();
    }

    public void setText(String s)
    {
        Text = s;
        textWidth = -1;
        invalidate();
        Component component = Awt.getParent(this);
        if(component != null)
        {
            component.validate();
        }
        if(isShowing())
        {
            repaint();
        }
    }

    public void update(Graphics g)
    {
        paint(g);
    }
}
