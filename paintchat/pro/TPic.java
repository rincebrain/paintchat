package paintchat.pro;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import paintchat.M;
import paintchat.M.Info;
import syi.awt.Awt;
import syi.awt.LComponent;

public class TPic extends LComponent
{
  private Tools tools;
  public Component tColor;
  private int iDrag = -1;
  private int sizePalette = 20;
  private int selPalette = 0;
  private int oldColor = 0;
  private Color[] cls;
  private int isRGB = 1;
  private int iColor;
  private static float[] fhsb = new float[3];

  public TPic(Tools paramTools)
  {
    setDimension(new Dimension((int)(66.0F * LComponent.Q), (int)(66.0F * LComponent.Q)), new Dimension((int)(128.0F * LComponent.Q), (int)(128.0F * LComponent.Q)), new Dimension((int)(284.0F * LComponent.Q), (int)(284.0F * LComponent.Q)));
    this.tools = paramTools;
  }

  private Image cMk()
  {
    int[] arrayOfInt = this.tools.iBuffer;
    int i = 64;
    int m = 0;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 1.0F / i;
    float f1 = fhsb[0];
    for (int k = 0; k < i; k++)
    {
      f2 = 1.0F;
      for (int j = 0; j < i; j++)
      {
        long tmp60_59 = (f2 - f4);
        f2 = tmp60_59;
        arrayOfInt[(m++)] = Color.HSBtoRGB(f1, tmp60_59, f3);
      }
      f3 += f4;
    }
    return this.tools.mkImage(i, i);
  }

  private Image cMkB()
  {
    int[] arrayOfInt1 = this.tools.iBuffer;
    int i = (int)(64.0F * LComponent.Q);
    int j = (int)(22.0F * LComponent.Q);
    int[] arrayOfInt2 = arrayOfInt1;
    int k = 0;
    float f1 = 0.0F;
    float f2 = 1.0F / i;
    for (int i1 = 0; i1 < i; i1++)
    {
      int m = Color.HSBtoRGB(f1, 1.0F, 1.0F);
      for (int n = 0; n < j; n++)
        arrayOfInt2[(k++)] = m;
      f1 += f2;
    }
    return this.tools.mkImage(j, i);
  }

  private int getRGB()
  {
    return Color.HSBtoRGB(fhsb[0], fhsb[1], fhsb[2]);
  }

  public void mPaint()
  {
    try
    {
      Graphics localGraphics = getG();
      mPaint(localGraphics);
      localGraphics.dispose();
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
  }

  public void mPaint(Graphics paramGraphics)
  {
    Dimension localDimension = getSize();
    int i = (int)(22.0F * LComponent.Q);
    int j = i;
    Object localObject1 = 0;
    int k = 0;
    int m = localDimension.width - j - 1;
    int n = localDimension.height - i - 1;
    synchronized (this.tools.iBuffer)
    {
      Image localImage = cMk();
      paramGraphics.drawImage(localImage, localObject1, k, m, n, Color.white, null);
      localImage = cMkB();
      paramGraphics.drawImage(localImage, m + 1, k, j, n, Color.white, null);
      k += n;
    }
    Awt.drawFrame(paramGraphics, false, localObject1, k + 1, m, i);
    ??? = (int)((m - 8) * 0.7F);
    paramGraphics.setColor(new Color(this.tools.info.m.iColorMask));
    paramGraphics.fillRect(localObject1 + ??? + 6, k + 4, (int)((m - 8) * 0.3F), i - 6);
    paramGraphics.setColor(Color.getHSBColor(fhsb[0], fhsb[1], fhsb[2]));
    paramGraphics.fillRect(localObject1 + 3, k + 4, ???, i - 6);
    paramGraphics.setColor(Color.blue);
    paramGraphics.setXORMode(Color.white);
    int i1 = Math.max((int)(10.0F * LComponent.Q), 2);
    int i2 = i1 >>> 1;
    paramGraphics.setClip(m + 1, 0, j, n);
    paramGraphics.drawOval(m + 1 + j / 2 - i2, (int)(n * fhsb[0]) - i2, i1, i1);
    paramGraphics.setClip(0, 0, m, n);
    paramGraphics.drawOval((int)(m * (1.0F - fhsb[1])) - i2, (int)(n * fhsb[2]) - i2, i1, i1);
    paramGraphics.setPaintMode();
    paramGraphics.setClip(0, 0, localDimension.width, localDimension.height);
  }

  public void paint2(Graphics paramGraphics)
  {
    mPaint(paramGraphics);
  }

  public void pMouse(MouseEvent paramMouseEvent)
  {
    int i = paramMouseEvent.getX();
    int j = paramMouseEvent.getY();
    int k = (int)(22.0F * LComponent.Q);
    int m = (int)(25.0F * LComponent.Q);
    int n = 0;
    Dimension localDimension = getSize();
    int i1 = localDimension.width - m - 1;
    int i2 = localDimension.height - k - 1;
    boolean bool = ((paramMouseEvent.getModifiers() & 0x4) != 0) || (paramMouseEvent.isShiftDown()) || (paramMouseEvent.isControlDown());
    if ((paramMouseEvent.getID() == 501) && (j > i2))
    {
      int i3 = (int)((i1 - 8) * 0.7F);
      if (i > i3)
        this.tools.setMask(this, getRGB(), i, j, bool);
      return;
    }
    i = i >= i1 ? i1 : i <= 0 ? 0 : i;
    j = j >= i2 ? i2 : j <= 0 ? 0 : j;
    switch (paramMouseEvent.getID())
    {
    case 501:
      this.iDrag = (i < i1 ? 0 : 1);
      n = 1;
      break;
    case 506:
      n = this.iDrag >= 0 ? 1 : 0;
      break;
    case 502:
      if (this.iDrag < 0)
        break;
      n = 1;
      this.iDrag = -1;
      this.tools.setRGB(Color.HSBtoRGB(fhsb[0], fhsb[1], fhsb[2]));
    case 503:
    case 504:
    case 505:
    }
    if ((n != 0) && (this.iDrag >= 0))
    {
      if (this.iDrag == 0)
      {
        fhsb[1] = (1.0F - i / i1);
        fhsb[2] = (j / i2);
      }
      else
      {
        fhsb[0] = (j / i2);
      }
      mPaint();
    }
  }

  public void setColor(int paramInt)
  {
    Color.RGBtoHSB(paramInt >>> 16 & 0xFF, paramInt >>> 8 & 0xFF, paramInt & 0xFF, fhsb);
    mPaint();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.pro.TPic
 * JD-Core Version:    0.6.0
 */