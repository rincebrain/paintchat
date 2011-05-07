package paintchat.pro;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.StringReader;
import paintchat.M;
import paintchat.M.Info;
import paintchat.Res;
import syi.awt.Awt;
import syi.awt.LComponent;

public class TPalette extends LComponent
{
  private int lenColor = 255;
  private int iDrag = -1;
  private M.Info info;
  private M mg;
  private Tools tools;
  private Res res;
  private Res config;
  private int sizePalette = 20;
  private int selPalette = 0;
  private int oldColor = 0;
  private Color[] cls;
  private int isRGB = 1;
  private float[] fhsb = new float[3];
  private int iColor;
  private static Font clFont = null;
  private static final char[][] clValue = { { 'H', 'S', 'B', 'A' }, { 'R', 'G', 'B', 'A' } };
  private static Color[][] clRGB = null;
  private static int[] clsDef = { 0, 16777215, 11826549, 8947848, 16422550, 12621504, 16758527, 8421631, 2475977, 15197581, 15177261, 10079099, 16575714, 16375247 };

  public TPalette()
  {
    if (clRGB == null)
      clRGB = new Color[][] { { Color.magenta, Color.cyan, Color.white, Color.lightGray }, { new Color(16422550), new Color(8581688), new Color(8421631), Color.lightGray } };
  }

  public void changeRH()
  {
    int i = getRGB();
    this.isRGB = (this.isRGB == 0 ? 1 : 0);
    setColor(i);
  }

  public String getC()
  {
    try
    {
      StringBuffer localStringBuffer = new StringBuffer();
      for (int j = 0; j < this.cls.length; j++)
      {
        if (j != 0)
          localStringBuffer.append('\n');
        int i = this.cls[j].getRGB();
        localStringBuffer.append("#" + Integer.toHexString(0xFF000000 | i & 0xFFFFFF).substring(2).toUpperCase());
      }
      return localStringBuffer.toString();
    }
    catch (Throwable localThrowable)
    {
    }
    return null;
  }

  private int getRGB()
  {
    return this.isRGB == 1 ? this.iColor : Color.HSBtoRGB((this.iColor >>> 16 & 0xFF) / 255.0F, (this.iColor >>> 8 & 0xFF) / 255.0F, (this.iColor & 0xFF) / 255.0F) & 0xFFFFFF;
  }

  public void init(Tools paramTools, M.Info paramInfo, Res paramRes1, Res paramRes2)
  {
    this.info = paramInfo;
    this.mg = paramInfo.m;
    this.res = paramRes2;
    this.config = paramRes1;
    this.tools = paramTools;
    setDimension(new Dimension((int)(42.0F * LComponent.Q), (int)(42.0F * LComponent.Q)), new Dimension((int)(112.0F * LComponent.Q), (int)(202.0F * LComponent.Q)), new Dimension((int)(300.0F * LComponent.Q), (int)(300.0F * LComponent.Q)));
  }

  public void paint2(Graphics paramGraphics)
  {
    try
    {
      if (this.cls == null)
      {
        this.cls = new Color[this.sizePalette];
        for (int i = 0; i < this.sizePalette; i += 2)
        {
          this.cls[i] = new Color(this.config.getP("color_" + (i + 2), i >= clsDef.length ? 16777215 : clsDef[i]));
          this.cls[(i + 1)] = new Color(this.config.getP("color_" + (i + 1), i >= clsDef.length ? 16777215 : clsDef[i]));
        }
      }
      Dimension localDimension = getSize();
      int j = Math.min((localDimension.height - 1) / 10, 64);
      int k = (int)(j * 1.5F);
      int m = j <= 12 ? 0 : 2;
      int n = 0;
      this.cls.length;
      int i1 = 0;
      int i2 = 0;
      for (int i3 = 0; i3 < this.cls.length; i3++)
      {
        paramGraphics.setColor(this.cls[(n++)]);
        paramGraphics.fillRect(i1 + 1, i2 + 1, k - 1 - m, j - 1 - m);
        paramGraphics.setColor(Awt.cF);
        paramGraphics.drawRect(i1, i2, k - m, j - m);
        if (this.selPalette == i3)
        {
          paramGraphics.setColor(Awt.cFSel);
          paramGraphics.drawRect(i1 + 1, i2 + 1, k - m - 2, j - m - 2);
        }
        if (i1 == 0)
        {
          i1 += k;
        }
        else
        {
          i1 = 0;
          i2 += j;
        }
      }
      i3 = k * 2;
      i2 = pBar(paramGraphics, i3, 0, j);
      paramGraphics.setColor(getBackground());
      paramGraphics.fillRect(i1 + i3, i2, localDimension.width - i3, localDimension.height - i2);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private int pBar(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3)
  {
    Dimension localDimension = getSize();
    int i = localDimension.width - paramInt1 - 1;
    (localDimension.height - paramInt2);
    Color localColor1 = getBackground();
    Color localColor2 = Awt.cFore;
    boolean bool = this.mg.isText();
    int j = bool ? 255 : this.info.getPenMask()[this.mg.iPenM].length;
    int k = Math.min(paramInt3 * 6, j * 8 + 1);
    int m = this.mg.iSize;
    m = m >= j ? j - 1 : m <= 0 ? 0 : m;
    this.mg.iSize = m;
    String str = (int)Math.sqrt(this.info.getPenMask()[this.mg.iPenM][this.mg.iSize].length) + "px";
    paramGraphics.setColor(Awt.cF);
    paramGraphics.drawRect(paramInt1, paramInt2, i, k);
    int n = (int)(k * ((m + 1) / j));
    paramGraphics.setColor(this.cls[this.selPalette]);
    paramGraphics.fillRect(paramInt1 + 1, paramInt2 + 1, i - 1, n - 1);
    paramGraphics.setColor(localColor1);
    paramGraphics.fillRect(paramInt1 + 1, paramInt2 + 1 + n, i - 1, k - n - 1);
    paramGraphics.setColor(localColor2);
    paramGraphics.setFont(Awt.getDefFont());
    paramGraphics.setXORMode(localColor1);
    paramGraphics.drawString(str, paramInt1 + 2, paramInt2 + k - 2);
    paramGraphics.setPaintMode();
    if ((clFont == null) || (clFont.getSize() != Math.max(paramInt3 - 2, 1)))
      clFont = new Font("sansserif", 0, Math.max(paramInt3 - 2, 1));
    paramGraphics.setFont(clFont);
    int i1 = this.iColor << 8 | this.mg.iAlpha;
    int i2 = 24;
    paramInt2 += k;
    for (int i3 = 0; i3 < 4; i3++)
    {
      paramGraphics.setColor(Awt.cF);
      paramGraphics.drawRect(paramInt1, paramInt2 + 1, i, paramInt3 - 2);
      paramGraphics.setColor(Color.white);
      paramGraphics.fillRect(paramInt1 + 1, paramInt2 + 2, i - 1, 1);
      paramGraphics.fillRect(paramInt1 + 1, paramInt2 + 3, 1, paramInt3 - 4);
      n = (int)((i - 2) * ((i1 >>> i2 & 0xFF) / 255.0F));
      paramGraphics.setColor(clRGB[this.isRGB][i3]);
      paramGraphics.fillRect(paramInt1 + 2, paramInt2 + 3, n, paramInt3 - 4);
      paramGraphics.setColor(Color.gray);
      paramGraphics.fillRect(paramInt1 + 1 + n, paramInt2 + 3, 1, paramInt3 - 4);
      paramGraphics.setColor(localColor1);
      paramGraphics.fillRect(paramInt1 + 2 + n, paramInt2 + 3, i - n - 2, paramInt3 - 4);
      paramGraphics.setColor(localColor2);
      paramGraphics.drawString(String.valueOf(clValue[this.isRGB][i3]) + (i1 >>> i2 & 0xFF), paramInt1 + 2, paramInt2 + paramInt3 - 2);
      paramInt2 += paramInt3;
      i2 -= 8;
    }
    return paramInt2;
  }

  public void pMouse(MouseEvent paramMouseEvent)
  {
    int i = paramMouseEvent.getID();
    int j = paramMouseEvent.getX();
    int k = paramMouseEvent.getY();
    Dimension localDimension = getSize();
    int m = (localDimension.height - 1) / 10;
    int n = (int)(m * 1.5F);
    int i1 = n * 2;
    boolean bool1 = Awt.isR(paramMouseEvent);
    boolean bool2 = this.mg.isText();
    int i2 = bool2 ? 255 : this.info.getPenMask()[this.mg.iPenM].length;
    int i3 = Math.min(m * 6, i2 * 8 + 1);
    int i5;
    if ((j <= i1) && (i == 501))
    {
      this.iDrag = -1;
      i4 = Math.min(k / m * 2 + j / n, 19);
      this.selPalette = i4;
      i5 = bool1 ? getRGB() : this.cls[i4].getRGB();
      if ((paramMouseEvent.isShiftDown()) && (i4 < clsDef.length))
        i5 = clsDef[i4];
      this.tools.setRGB(i5);
      return;
    }
    int i4 = 0;
    switch (i)
    {
    case 501:
      if (k < i3)
      {
        if (bool1)
        {
          this.tools.setField(this, "iPenM", "penm_", j, k);
          return;
        }
        this.iDrag = 0;
      }
      else
      {
        if (bool1)
        {
          changeRH();
          return;
        }
        this.iDrag = ((k - i3) / m);
        this.iDrag = ((this.iDrag >= 3 ? 3 : this.iDrag <= 0 ? 0 : this.iDrag) + 1);
      }
      i4 = 1;
      break;
    case 502:
      this.iDrag = -1;
      break;
    case 506:
      if (this.iDrag < 0)
        break;
      i4 = 1;
    case 503:
    case 504:
    case 505:
    }
    if (i4 != 0)
      if (this.iDrag == 0)
      {
        setLineSize((int)(k / i3 * i2));
      }
      else
      {
        i5 = (int)((j - i1) / (localDimension.width - i1) * 255.0F);
        int i6 = 24 - 8 * (this.iDrag - 1);
        int i7 = this.iColor << 8 | this.mg.iAlpha;
        i7 = i7 & (0xFFFFFFFF ^ 255 << i6) | Math.max(Math.min(i5, 255), 0) << i6;
        this.iColor = (i7 >>> 8);
        this.mg.iAlpha = Math.max(i7 & 0xFF, 1);
        i7 = this.iColor;
        this.tools.setRGB(getRGB());
        int i8 = this.iColor == i7 ? 1 : 0;
        this.iColor = i7;
        if (i8 != 0)
          repaint();
      }
  }

  public void setC(String paramString)
  {
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new StringReader(paramString));
      int i = 0;
      while ((paramString = localBufferedReader.readLine()) != null)
      {
        if (i < this.cls.length)
          this.cls[i] = Color.decode(paramString);
        if (i >= clsDef.length)
          continue;
        clsDef[(i++)] = (this.cls[i].getRGB() & 0xFFFFFF);
      }
      repaint();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void setColor(int paramInt)
  {
    paramInt &= 16777215;
    int i = getRGB() != paramInt ? 1 : 0;
    if (this.isRGB == 1)
    {
      this.iColor = paramInt;
    }
    else
    {
      Color.RGBtoHSB(paramInt >>> 16, paramInt >>> 8 & 0xFF, paramInt & 0xFF, this.fhsb);
      this.iColor = ((int)(this.fhsb[0] * 255.0F) << 16 | (int)(this.fhsb[1] * 255.0F) << 8 | (int)(this.fhsb[2] * 255.0F));
    }
    if ((this.cls[this.selPalette].getRGB() & 0xFFFFFF) != paramInt)
    {
      this.cls[this.selPalette] = new Color(this.mg.iColor);
      i = 1;
    }
    if (i != 0)
      repaint();
  }

  public void setLineSize(int paramInt)
  {
    int i = this.mg.isText() ? 255 : this.info.getPenMask()[this.mg.iPenM].length;
    int j = this.mg.iSize;
    this.mg.iSize = Math.min(Math.max(0, paramInt), i);
    if (j != this.mg.iSize)
      repaint();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.pro.TPalette
 * JD-Core Version:    0.6.0
 */