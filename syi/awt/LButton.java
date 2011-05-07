package syi.awt;

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class LButton extends Canvas
{
  private Dimension size = null;
  private Image BackImage = null;
  private String Text = null;
  private ActionListener listener = null;
  int Gap = 3;
  int textWidth = -1;
  boolean isPress = false;
  Color dkBackColor = Color.darkGray;

  public LButton()
  {
    this(null);
  }

  public LButton(String paramString)
  {
    enableEvents(17L);
    setBackground(new Color(13619151));
    setText(paramString);
  }

  public void addActionListener(ActionListener paramActionListener)
  {
    this.listener = paramActionListener;
  }

  private void doAction()
  {
    if (this.listener != null)
      this.listener.actionPerformed(new ActionEvent(this, 1001, getText()));
  }

  public Image getBackImage()
  {
    return this.BackImage;
  }

  public int getGap()
  {
    return this.Gap;
  }

  public Dimension getMinimumSize()
  {
    return getPreferredSize();
  }

  public Dimension getPreferredSize()
  {
    int i = 50;
    int j = 10;
    if (this.BackImage != null)
    {
      i = this.BackImage.getWidth(null);
      j = this.BackImage.getHeight(null);
    }
    else
    {
      Font localFont = getFont();
      int k = this.Gap * 2;
      if (localFont != null)
      {
        FontMetrics localFontMetrics = getFontMetrics(getFont());
        if (localFontMetrics != null)
          j = localFontMetrics.getMaxAscent() + localFontMetrics.getMaxDescent();
        if (this.Text != null)
          i = localFontMetrics.stringWidth(this.Text) + k;
      }
      j += k;
    }
    return new Dimension(i, j);
  }

  public Dimension getSize()
  {
    if (this.size == null)
      this.size = super.getSize();
    return this.size;
  }

  public String getText()
  {
    return this.Text == null ? "" : this.Text;
  }

  public void paint(Graphics paramGraphics)
  {
    try
    {
      Dimension localDimension = getSize();
      if (this.BackImage != null)
      {
        Awt.drawFrame(paramGraphics, this.isPress, 0, 0, localDimension.width, localDimension.height);
        paramGraphics.drawImage(this.BackImage, 1, 1, null);
      }
      else
      {
        Awt.fillFrame(paramGraphics, this.isPress, 0, 0, localDimension.width, localDimension.height);
      }
      if (this.Text == null)
        return;
      FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
      if (this.textWidth == -1)
        this.textWidth = localFontMetrics.stringWidth(this.Text);
      paramGraphics.setColor(getForeground());
      paramGraphics.drawString(this.Text, (this.size.width - this.textWidth) / 2, localFontMetrics.getMaxAscent() + this.Gap + 1);
    }
    catch (Throwable localThrowable)
    {
    }
  }

  protected void processEvent(AWTEvent paramAWTEvent)
  {
    try
    {
      int i = paramAWTEvent.getID();
      if ((paramAWTEvent instanceof MouseEvent))
      {
        MouseEvent localMouseEvent = (MouseEvent)paramAWTEvent;
        localMouseEvent.consume();
        if (i == 501)
        {
          this.isPress = true;
          repaint();
        }
        if (i == 502)
        {
          if (contains(((MouseEvent)paramAWTEvent).getPoint()))
            doAction();
          this.isPress = false;
          repaint();
        }
        return;
      }
      if ((paramAWTEvent instanceof ComponentEvent))
      {
        if ((i == 101) || (i == 102))
        {
          this.size = null;
          repaint();
        }
        return;
      }
      super.processEvent(paramAWTEvent);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void resetSize()
  {
    super.setSize(getPreferredSize());
  }

  public void setBackground(Color paramColor)
  {
    if (paramColor == null)
      return;
    this.dkBackColor = paramColor.darker();
    super.setBackground(paramColor);
  }

  public void setBackImage(Image paramImage)
  {
    this.BackImage = paramImage;
    if (isShowing())
      repaint();
  }

  public void setGap(int paramInt)
  {
    this.Gap = paramInt;
    resetSize();
  }

  public void setText(String paramString)
  {
    this.Text = paramString;
    this.textWidth = -1;
    invalidate();
    Component localComponent = Awt.getParent(this);
    if (localComponent != null)
      localComponent.validate();
    if (isShowing())
      repaint();
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.LButton
 * JD-Core Version:    0.6.0
 */