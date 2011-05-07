package syi.awt;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import syi.util.Io;

public class ImageCanvas extends Canvas
{
  private Image image = null;
  private String string = null;
  private int imW = 0;
  private int imH = 0;

  public ImageCanvas(Color paramColor1, Color paramColor2)
  {
    setBackground(paramColor1);
  }

  public Dimension getPreferredSize()
  {
    return new Dimension(this.imW + 2, this.imH + 2);
  }

  public void paint(Graphics paramGraphics)
  {
    try
    {
      Dimension localDimension = getSize();
      paramGraphics.drawRect(0, 0, localDimension.width - 1, localDimension.height - 1);
      if (this.image != null)
        paramGraphics.drawImage(this.image, 1, 1, null);
      else
        paramGraphics.clearRect(1, 1, localDimension.width - 2, localDimension.height - 2);
      if (this.string != null)
      {
        FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
        paramGraphics.drawString(this.string, 10, localFontMetrics.getMaxAscent() + 10);
      }
    }
    catch (RuntimeException localRuntimeException)
    {
    }
  }

  public synchronized void reset()
  {
    if (this.image != null)
    {
      this.image.flush();
      this.image = null;
      this.imW = 0;
      this.imH = 0;
    }
    this.string = null;
  }

  public void setImage(Image paramImage)
  {
    this.image = paramImage;
    this.imW = paramImage.getWidth(null);
    this.imH = paramImage.getHeight(null);
    setSize(this.imW + 2, this.imH + 2);
  }

  public void setImage(String paramString)
  {
    if ((paramString == null) || (paramString.length() <= 0))
      return;
    setImage(Io.loadImageNow(this, paramString));
  }

  public void setText(String paramString)
  {
    this.string = paramString;
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.ImageCanvas
 * JD-Core Version:    0.6.0
 */