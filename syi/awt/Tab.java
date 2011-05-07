package syi.awt;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import paintchat.M;
import paintchat.M.Info;

public class Tab extends LComponent
{
  private M mg;
  private int iDrag = -1;
  private int sizeBar;
  private int max = 0;
  private int strange;
  private Object tab;
  private Method mGet;
  private Method mPoll;
  private Method mEx;
  private byte iSOB;
  private final String[] STR = { "alpha", "size" };

  public Tab(Container paramContainer, M.Info paramInfo)
    throws Throwable
  {
    try
    {
      this.mg = paramInfo.m;
      int i = this.sizeBar = (int)(16.0F * LComponent.Q);
      Dimension localDimension = getSize();
      localDimension.setSize(i * 4, 8 + i * 6);
      setDimension(localDimension, localDimension, localDimension);
      Class localClass = Class.forName("cello.tablet.JTablet");
      this.tab = localClass.newInstance();
      this.mGet = localClass.getMethod("getPressure", null);
      this.mPoll = localClass.getMethod("poll", null);
      this.mEx = localClass.getMethod("getPressureExtent", null);
      setTitle("tablet");
      paramContainer.add(this, 0);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private int at(int paramInt)
  {
    if (paramInt > getSize().height / 2)
      paramInt -= 5;
    return paramInt / this.sizeBar;
  }

  private void dBar(Graphics paramGraphics, int paramInt)
  {
    Dimension localDimension = getSize();
    int i = this.sizeBar;
    int j = paramInt * (localDimension.height / 2) + i;
    float f = (localDimension.width - 6) / 255.0F;
    for (int m = 0; m < 2; m++)
    {
      int n = paramInt == 0 ? this.mg.iSA : this.mg.iSS;
      paramGraphics.setColor(this.clFrame);
      paramGraphics.drawRect(2, j, (int)(f * 255.0F), i);
      int k = (int)((n >>> m * 8 & 0xFF) * f);
      paramGraphics.setColor(getForeground());
      paramGraphics.fillRect(3, j + 1, k, i - 1);
      paramGraphics.setColor(getBackground());
      paramGraphics.fillRect(k + 3, j + 1, localDimension.width - k - 7, i - 1);
      j += i;
    }
  }

  private void drag(int paramInt)
  {
    int i = this.iDrag;
    if ((i <= 0) || (i == 3))
      return;
    int j = i >= 3 ? 1 : 0;
    if ((this.iSOB & 1 << (j == 0 ? 0 : 1)) == 0)
      return;
    paramInt = (int)(255.0F / getSize().width * paramInt);
    paramInt = paramInt >= 255 ? 255 : paramInt <= 0 ? 0 : paramInt;
    int k;
    if (j != 0)
    {
      k = (i - 4) * 8;
      this.mg.iSS = (this.mg.iSS & 255 << 8 - k | paramInt << k);
    }
    else
    {
      k = (i - 1) * 8;
      this.mg.iSA = (this.mg.iSA & 255 << 8 - k | paramInt << k);
    }
    Graphics localGraphics = getG();
    dBar(localGraphics, i < 3 ? 0 : 1);
    localGraphics.dispose();
  }

  public void paint2(Graphics paramGraphics)
  {
    try
    {
      int i = this.sizeBar;
      Dimension localDimension = getSize();
      int k = localDimension.width - 1;
      int m = k - 6;
      int n = i * 3 + 4;
      (m / 255.0F);
      for (int j = 0; j < 2; j++)
      {
        boolean bool = (this.iSOB & j + 1) != 0;
        int i1 = n * j;
        Awt.fillFrame(paramGraphics, !bool, 0, i1, k, n);
        Awt.fillFrame(paramGraphics, bool, 0, i1, i, i);
        paramGraphics.setColor(getForeground());
        paramGraphics.drawString(this.STR[j] + '.' + (bool ? "On" : "Off"), i + 2, i1 + i - 2);
        dBar(paramGraphics, j);
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void pMouse(MouseEvent paramMouseEvent)
  {
    int i = paramMouseEvent.getX();
    int j = paramMouseEvent.getY();
    int k = this.sizeBar;
    switch (paramMouseEvent.getID())
    {
    case 501:
      if (this.iDrag >= 0)
        break;
      int m = at(j);
      this.iDrag = m;
      if (((m == 0) || (m == 3)) && (i <= k))
      {
        this.iSOB = (byte)(this.iSOB ^ 1 << (m == 0 ? 0 : 1));
        repaint();
      }
      else
      {
        drag(i);
      }
      break;
    case 502:
      this.iDrag = -1;
      break;
    case 506:
      drag(i);
    case 503:
    case 504:
    case 505:
    }
  }

  public final boolean poll()
  {
    if (this.iSOB == 0)
      return false;
    try
    {
      if (((Boolean)this.mPoll.invoke(this.tab, null)).booleanValue())
      {
        this.mg.iSOB = this.iSOB;
        if (this.max <= 0)
        {
          this.max = ((Integer)this.mEx.invoke(this.tab, null)).intValue();
          if (this.max != 0)
            this.mEx = null;
        }
        return true;
      }
    }
    catch (Throwable localThrowable)
    {
    }
    return false;
  }

  public final int strange()
  {
    try
    {
      if (poll())
        this.strange = (int)(((Integer)this.mGet.invoke(this.tab, null)).intValue() / this.max * 255.0F);
      else
        this.strange = 0;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return this.strange;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.Tab
 * JD-Core Version:    0.6.0
 */