package syi.awt;

import java.awt.*;
import java.io.CharArrayWriter;

public class TextCanvas extends Canvas
{

    String strs[];
    int seek;
    CharArrayWriter buffer;
    Dimension d;
    private int Gap;
    public boolean isBorder;

    public TextCanvas()
    {
        strs = null;
        seek = 0;
        buffer = new CharArrayWriter();
        d = new Dimension();
        Gap = 3;
        isBorder = true;
    }

    public TextCanvas(String s)
    {
        strs = null;
        seek = 0;
        buffer = new CharArrayWriter();
        d = new Dimension();
        Gap = 3;
        isBorder = true;
        setText(s);
    }

    public synchronized void addText(String s)
    {
        if(strs == null)
        {
            strs = new String[6];
        } else
        if(seek >= strs.length)
        {
            String as[] = new String[strs.length * 2];
            System.arraycopy(strs, 0, as, 0, strs.length);
            strs = as;
        }
        strs[seek++] = s;
    }

    public Dimension getPreferredSize()
    {
        if(strs == null || seek == 0)
        {
            return new Dimension(50, 10);
        }
        try
        {
            FontMetrics fontmetrics = getFontMetrics(getFont());
            int i = 0;
            int j = Gap * 2;
            for(int k = 0; k < seek; k++)
            {
                i = Math.max(i, fontmetrics.stringWidth(strs[k]));
            }

            return new Dimension(i + j + 4, (fontmetrics.getMaxDescent() + fontmetrics.getMaxAscent() + Gap) * seek + 4);
        }
        catch(RuntimeException _ex)
        {
            return new Dimension(50, 10);
        }
    }

    public void paint(Graphics g)
    {
        try
        {
            Dimension dimension = getSize();
            g.clearRect(1, 1, dimension.width - 2, dimension.height - 2);
            if(isBorder)
            {
                g.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
            }
            FontMetrics fontmetrics = g.getFontMetrics();
            int i = fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent() + Gap;
            int j = fontmetrics.getMaxAscent();
            for(int k = 0; k < seek; k++)
            {
                g.drawString(strs[k], Gap + 2, i * k + j + Gap + 2);
            }

        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void reset()
    {
        if(strs == null)
        {
            return;
        }
        for(int i = 0; i < seek; i++)
        {
            strs[i] = null;
        }

        seek = 0;
    }

    public final void setText(String s)
    {
        if(s == null)
        {
            return;
        }
        CharArrayWriter chararraywriter = buffer;
        synchronized(chararraywriter)
        {
            chararraywriter.reset();
            reset();
            int i = s.length();
            for(int j = 0; j < i; j++)
            {
                char c = s.charAt(j);
                if(c == '\r' || c == '\n')
                {
                    if(chararraywriter.size() > 0)
                    {
                        addText(chararraywriter.toString());
                        chararraywriter.reset();
                    }
                } else
                {
                    chararraywriter.write(c);
                }
            }

            if(chararraywriter.size() > 0)
            {
                addText(chararraywriter.toString());
            }
        }
        setSize(getPreferredSize());
    }

    public void update(Graphics g)
    {
        paint(g);
    }
}
