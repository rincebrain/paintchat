package syi.awt;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class LTextField extends Component
{
  private String Title;
  private String Text;
  private int Gap = 3;
  private boolean edit = true;
  private Color Bk;
  private Color BkD;
  private Color Fr;
  private boolean isPress = false;
  private Dimension size = null;
  private ActionListener actionListener = null;

  public LTextField()
  {
    this("", "");
  }

  public LTextField(String paramString)
  {
    this(paramString, "");
  }

  public LTextField(String paramString1, String paramString2)
  {
    enableEvents(17L);
    setBk(Color.white);
    setFr(new Color(5263480));
    setText(paramString1);
    setTitle(paramString2);
  }

  public void addActionListener(ActionListener paramActionListener)
  {
    this.actionListener = paramActionListener;
  }

  public boolean getEdit()
  {
    return this.edit;
  }

  public Dimension getMinimumSize()
  {
    return getPreferredSize();
  }

  public Dimension getPreferredSize()
  {
    Font localFont = getFont();
    if (localFont == null)
      return this.size;
    FontMetrics localFontMetrics = getFontMetrics(localFont);
    if (localFontMetrics == null)
      return this.size;
    int i = this.Gap * 4;
    if (this.Text != null)
      i += localFontMetrics.stringWidth(this.Text);
    else
      i += 10;
    if (this.Title != null)
      i += localFontMetrics.stringWidth(this.Title);
    else
      i += 10;
    return new Dimension(i, localFontMetrics.getMaxAscent() + localFontMetrics.getMaxDescent() + this.Gap * 2);
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

  public String getTitle()
  {
    return this.Title == null ? "" : this.Title;
  }

  public void paint(Graphics paramGraphics)
  {
    try
    {
      FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
      int i = this.Gap;
      int j = localFontMetrics.getMaxAscent() + 1;
      if (this.Title.length() > 0)
      {
        int k = localFontMetrics.stringWidth(this.Title);
        paramGraphics.drawString(this.Title, this.Gap, this.Gap + j);
        i += this.Gap + k;
      }
      Dimension localDimension = getSize();
      Awt.fillFrame(paramGraphics, this.isPress, i, 0, localDimension.width - i, localDimension.height);
      if (this.Text.length() > 0)
      {
        paramGraphics.setColor(this.Fr);
        paramGraphics.drawString(this.Text, i + this.Gap, this.Gap + j);
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
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
        if ((this.edit) && (i == 501))
        {
          this.isPress = true;
          repaint();
        }
        if ((this.edit) && (i == 502))
        {
          localMouseEvent.consume();
          repaint();
          this.isPress = false;
          Point localPoint1 = localMouseEvent.getPoint();
          if (contains(localPoint1))
          {
            String str = getText();
            Point localPoint2 = getLocationOnScreen();
            localPoint1.translate(localPoint2.x, localPoint2.y);
            setText(MessageBox.getString(getText(), getTitle()));
            if ((!str.equals(getText())) && (this.actionListener != null))
              this.actionListener.actionPerformed(new ActionEvent(this, 1001, getText()));
          }
        }
      }
      if (((paramAWTEvent instanceof ComponentEvent)) && ((i == 101) || (i == 102)))
        this.size = null;
      super.processEvent(paramAWTEvent);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void pMouse(MouseEvent paramMouseEvent)
  {
  }

  public void setBk(Color paramColor)
  {
    this.Bk = paramColor;
    this.BkD = paramColor.darker();
  }

  public void setEdit(boolean paramBoolean)
  {
    this.edit = paramBoolean;
  }

  public void setFr(Color paramColor)
  {
    this.Fr = paramColor;
  }

  public void setGap(int paramInt)
  {
    this.Gap = paramInt;
  }

  public void setText(String paramString)
  {
    this.Text = paramString;
    invalidate();
    Container localContainer = getParent();
    if (localContainer != null)
      localContainer.validate();
    if (isShowing())
      repaint();
  }

  public void setTitle(String paramString)
  {
    this.Title = paramString;
    if (isShowing())
      repaint();
  }

  public void update(Graphics paramGraphics)
  {
    try
    {
      paint(paramGraphics);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.LTextField
 * JD-Core Version:    0.6.0
 */