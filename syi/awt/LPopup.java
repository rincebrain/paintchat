package syi.awt;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class LPopup extends Component
{
  private String[] strs = null;
  private int seek = 0;
  private ActionListener listener = null;
  private int minWidth = 10;

  public LPopup()
  {
    enableEvents(48L);
    setVisible(false);
  }

  public void add(String paramString)
  {
    Object localObject;
    if ((this.strs == null) || (this.seek >= this.strs.length))
    {
      localObject = new String[(int)((this.seek + 1) * 1.5D)];
      if (this.strs != null)
        for (int i = 0; i < this.strs.length; i++)
          localObject[i] = this.strs[i];
      this.strs = ((String)localObject);
    }
    this.strs[(this.seek++)] = paramString;
    try
    {
      localObject = getFontMetrics(getFont());
      this.minWidth = Math.max(((FontMetrics)localObject).stringWidth(paramString), this.minWidth);
    }
    catch (RuntimeException localRuntimeException)
    {
    }
  }

  public void add(String paramString, int paramInt)
  {
  }

  public void addActionListener(ActionListener paramActionListener)
  {
  }

  public Dimension getPreferredSize()
  {
    try
    {
      FontMetrics localFontMetrics = getFontMetrics(getFont());
      return new Dimension(this.minWidth, localFontMetrics.getMaxDescent() + localFontMetrics.getMaxAscent());
    }
    catch (RuntimeException localRuntimeException)
    {
    }
    return new Dimension(10, 10);
  }

  public void paint(Graphics paramGraphics)
  {
    try
    {
      Dimension localDimension = getSize();
      paramGraphics.setColor(getForeground());
      paramGraphics.drawRect(0, 0, localDimension.width - 1, localDimension.height - 1);
      paramGraphics.setColor(Color.white);
      paramGraphics.fillRect(1, 1, localDimension.width - 2, localDimension.height - 2);
    }
    catch (Throwable localThrowable)
    {
    }
  }

  protected void processMouseEvent(MouseEvent paramMouseEvent)
  {
    try
    {
      int i = paramMouseEvent.getID();
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void removeAt(int paramInt)
  {
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.LPopup
 * JD-Core Version:    0.6.0
 */