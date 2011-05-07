package syi.awt;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.CharArrayWriter;

public class TextCanvas extends Canvas
{
  String[] strs = null;
  int seek = 0;
  CharArrayWriter buffer = new CharArrayWriter();
  Dimension d = new Dimension();
  private int Gap = 3;
  public boolean isBorder = true;

  public TextCanvas()
  {
  }

  public TextCanvas(String paramString)
  {
    setText(paramString);
  }

  public synchronized void addText(String paramString)
  {
    if (this.strs == null)
    {
      this.strs = new String[6];
    }
    else if (this.seek >= this.strs.length)
    {
      String[] arrayOfString = new String[this.strs.length * 2];
      System.arraycopy(this.strs, 0, arrayOfString, 0, this.strs.length);
      this.strs = arrayOfString;
    }
    this.strs[(this.seek++)] = paramString;
  }

  public Dimension getPreferredSize()
  {
    if ((this.strs == null) || (this.seek == 0))
      return new Dimension(50, 10);
    try
    {
      FontMetrics localFontMetrics = getFontMetrics(getFont());
      int i = 0;
      int j = this.Gap * 2;
      for (int k = 0; k < this.seek; k++)
        i = Math.max(i, localFontMetrics.stringWidth(this.strs[k]));
      return new Dimension(i + j + 4, (localFontMetrics.getMaxDescent() + localFontMetrics.getMaxAscent() + this.Gap) * this.seek + 4);
    }
    catch (RuntimeException localRuntimeException)
    {
    }
    return new Dimension(50, 10);
  }

  public void paint(Graphics paramGraphics)
  {
    try
    {
      Dimension localDimension = getSize();
      paramGraphics.clearRect(1, 1, localDimension.width - 2, localDimension.height - 2);
      if (this.isBorder)
        paramGraphics.drawRect(0, 0, localDimension.width - 1, localDimension.height - 1);
      FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
      int i = localFontMetrics.getMaxAscent() + localFontMetrics.getMaxDescent() + this.Gap;
      int j = localFontMetrics.getMaxAscent();
      for (int k = 0; k < this.seek; k++)
        paramGraphics.drawString(this.strs[k], this.Gap + 2, i * k + j + this.Gap + 2);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void reset()
  {
    if (this.strs == null)
      return;
    for (int i = 0; i < this.seek; i++)
      this.strs[i] = null;
    this.seek = 0;
  }

  public final void setText(String paramString)
  {
    if (paramString == null)
      return;
    CharArrayWriter localCharArrayWriter = this.buffer;
    synchronized (localCharArrayWriter)
    {
      localCharArrayWriter.reset();
      reset();
      int i = paramString.length();
      for (int k = 0; k < i; k++)
      {
        int j = paramString.charAt(k);
        if ((j == 13) || (j == 10))
        {
          if (localCharArrayWriter.size() <= 0)
            continue;
          addText(localCharArrayWriter.toString());
          localCharArrayWriter.reset();
        }
        else
        {
          localCharArrayWriter.write(j);
        }
      }
      if (localCharArrayWriter.size() > 0)
        addText(localCharArrayWriter.toString());
    }
    setSize(getPreferredSize());
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.TextCanvas
 * JD-Core Version:    0.6.0
 */