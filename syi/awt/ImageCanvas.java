package syi.awt;

import java.awt.*;
import syi.util.Io;

public class ImageCanvas extends Canvas
{

    private Image image;
    private String string;
    private int imW;
    private int imH;

    public ImageCanvas(Color color, Color color1)
    {
        image = null;
        string = null;
        imW = 0;
        imH = 0;
        setBackground(color);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(imW + 2, imH + 2);
    }

    public void paint(Graphics g)
    {
        try
        {
            Dimension dimension = getSize();
            g.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
            if(image != null)
            {
                g.drawImage(image, 1, 1, null);
            } else
            {
                g.clearRect(1, 1, dimension.width - 2, dimension.height - 2);
            }
            if(string != null)
            {
                FontMetrics fontmetrics = g.getFontMetrics();
                g.drawString(string, 10, fontmetrics.getMaxAscent() + 10);
            }
        }
        catch(RuntimeException _ex) { }
    }

    public synchronized void reset()
    {
        if(image != null)
        {
            image.flush();
            image = null;
            imW = 0;
            imH = 0;
        }
        string = null;
    }

    public void setImage(Image image1)
    {
        image = image1;
        imW = image1.getWidth(null);
        imH = image1.getHeight(null);
        setSize(imW + 2, imH + 2);
    }

    public void setImage(String s)
    {
        if(s == null || s.length() <= 0)
        {
            return;
        } else
        {
            setImage(Io.loadImageNow(this, s));
            return;
        }
    }

    public void setText(String s)
    {
        string = s;
    }

    public void update(Graphics g)
    {
        paint(g);
    }
}
