package paintchat.normal;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.Hashtable;
import paintchat.M;
import paintchat.Res;
import paintchat_client.Mi;

public class ToolList
{
  private Tools tools;
  private Res res;
  private Res cnf;
  boolean isField;
  boolean isClass;
  boolean isDirect;
  boolean isMask;
  boolean isEraser;
  boolean isSelect;
  boolean isDrawList;
  boolean isIm;
  String strField;
  private int quality = 1;
  private boolean isDrag = false;
  public int iSelect;
  public int iSelectList;
  public boolean isList;
  private M info = null;
  private M[] mgs = null;
  private int[] items = null;
  private String[] strs = null;
  private String[] strings;
  private int length;
  private ToolList[] lists;
  private Font font;
  private int base = 0;
  private Image image;
  private int imW;
  private int imH;
  private int imIndex;
  public Rectangle r = new Rectangle();

  private void dImage(Graphics paramGraphics, Color paramColor, int paramInt1, int paramInt2)
  {
    int i = this.r.height;
    int j = this.r.width;
    paramGraphics.setColor(paramColor);
    paramGraphics.fillRect(2, paramInt1 + 2, this.r.width - 4, i - 4);
    if (this.isMask)
    {
      paramGraphics.setColor(new Color(this.info.iColorMask));
      paramGraphics.fillRect(j - this.imW - 3, paramInt1 + 3, this.imW, (i - 4) / 2);
    }
    if ((!this.isIm) || (this.image == null) || (paramInt2 >= this.image.getHeight(null) / this.imH))
      return;
    int k = this.imIndex * this.imW;
    int m = paramInt2 * this.imH;
    int n = this.r.x + 2;
    int i1 = paramInt1 + 2;
    paramGraphics.drawImage(this.image, n, i1, n + j - 4, i1 + i - 4, k, m, k + this.imW, m + this.imH, paramColor, null);
  }

  private void drag(int paramInt1, int paramInt2)
  {
    if (!this.isDrag)
      return;
    int i = len();
    int j = this.r.width;
    int k = this.r.height;
    int m = paramInt2 / (k - 2) - 1;
    this.isList = true;
    int n = this.iSelectList;
    if ((paramInt1 < 0) || (paramInt1 >= j) || (m < 0) || (m >= i))
    {
      this.iSelectList = -1;
      m = -1;
    }
    else
    {
      this.iSelectList = m;
    }
    if ((this.isList) && (!this.isDrawList))
    {
      this.isDrawList = true;
      repaint();
    }
    if ((n == m) || (!this.isList))
      return;
    Graphics localGraphics = this.tools.primary();
    if (n >= 0)
    {
      localGraphics.setColor(this.tools.clFrame);
      localGraphics.drawRect(this.r.x + 1, this.r.y + (k - 3) * (n + 1) + 2, j - 3, k - 3);
    }
    if (m >= 0)
    {
      localGraphics.setColor(this.tools.clSel);
      localGraphics.drawRect(this.r.x + 1, this.r.y + (k - 3) * (m + 1) + 2, j - 3, k - 3);
    }
  }

  private int getValue()
  {
    try
    {
      return this.isField ? M.class.getField(this.strField).getInt(this.info) : this.iSelect;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return 0;
  }

  public void init(Tools paramTools, Res paramRes1, Res paramRes2, M paramM, ToolList[] paramArrayOfToolList, int paramInt)
  {
    try
    {
      this.tools = paramTools;
      this.res = paramRes1;
      this.cnf = paramRes2;
      this.lists = paramArrayOfToolList;
      this.info = paramM;
      String str1 = "t0" + paramInt + "_";
      this.isDirect = paramRes2.getP(str1 + "direct", false);
      this.isClass = paramRes2.getP(str1 + "class", false);
      this.isEraser = paramRes2.getP(str1 + "iseraser", false);
      this.isIm = paramRes2.getP(str1 + "image", true);
      this.strField = paramRes2.getP(str1 + "field", null);
      this.isField = (this.strField != null);
      if ((this.isField) && (this.strField.equals("iMask")))
        this.isMask = true;
      str1 = "t0" + paramInt;
      for (int i = 0; paramRes2.getP(str1 + i) != null; i++);
      this.strings = new String[i];
      for (int j = 0; j < i; j++)
      {
        String str2 = str1 + j;
        if (this.isField)
        {
          if (this.items == null)
            this.items = new int[i];
          this.items[j] = paramRes2.getP(str2, 0);
        }
        else if (this.isClass)
        {
          if (this.strs == null)
            this.strs = new String[i];
          this.strs[j] = paramRes2.getP(str2);
        }
        else
        {
          if (this.mgs == null)
            this.mgs = new M[i];
          (this.mgs[j] =  = new M()).set(paramRes2.getP(str2));
        }
        this.strings[j] = paramRes1.res(str2);
        paramRes2.remove(str2);
        paramRes1.remove(str2);
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private int len()
  {
    return this.mgs == null ? this.items.length : this.items == null ? this.strs.length : this.strs == null ? 0 : this.mgs.length;
  }

  public void paint(Graphics paramGraphics1, Graphics paramGraphics2)
  {
    try
    {
      if ((paramGraphics1 == null) || (paramGraphics2 == null))
        return;
      int j = this.r.width;
      int k = this.r.height;
      int m = this.r.x;
      int n = this.r.y;
      int i1 = len();
      int i2 = k - 2;
      int i3;
      if (this.isList)
      {
        i3 = n + k - 2;
        Color localColor = this.isDirect ? this.tools.clB2 : this.tools.clB;
        for (int i5 = 0; i5 < i1; i5++)
        {
          dImage(paramGraphics1, localColor, i3, i5);
          paramGraphics1.setColor(this.tools.clText);
          if (i5 < this.strings.length)
            paramGraphics1.drawString(this.strings[i5], m + 4, i3 + this.base);
          i3 += i2 - 1;
        }
        i3 = n + i2;
        paramGraphics1.setColor(this.tools.clFrame);
        paramGraphics1.drawRect(m, i3, j - 1, (i2 - 1) * i1 + 2);
        for (i5 = 0; i5 < i1; i5++)
        {
          paramGraphics1.drawRect(m + 1, i3 + 1, j - 3, k - 3);
          i3 += i2 - 1;
        }
      }
      int i = getValue();
      if (this.isField)
      {
        i3 = this.items.length;
        for (int i4 = 0; i4 < i3; i4++)
        {
          if (this.items[i4] != i)
            continue;
          i = i4;
          break;
        }
      }
      dImage(paramGraphics2, this.isDirect ? this.tools.clB2 : this.tools.clB, 0, i);
      paramGraphics2.setColor(this.tools.clFrame);
      paramGraphics2.drawRect(0, 0, j - 1, k - 1);
      if (this.isSelect)
      {
        paramGraphics2.setColor(this.tools.clSel);
        paramGraphics2.drawRect(1, 1, j - 3, k - 3);
      }
      else
      {
        paramGraphics2.setColor(this.tools.clBL);
        paramGraphics2.fillRect(1, 1, j - 2, 1);
        paramGraphics2.fillRect(1, 1, 1, k - 2);
        paramGraphics2.setColor(this.tools.clBD);
        paramGraphics2.fillRect(j - 2, 2, 1, k - 4);
        paramGraphics2.fillRect(2, k - 2, j - 3, 1);
      }
      if ((i >= 0) && (i < this.strings.length))
      {
        paramGraphics2.setColor(this.tools.clText);
        paramGraphics2.drawString(this.strings[i], 3, this.base);
      }
      paramGraphics1.drawImage(this.tools.imBack, this.r.x, this.r.y, this.r.x + j, this.r.y + k, 0, 0, j, k, this.tools.clB, null);
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
    int k = i - this.r.x;
    int m = j - this.r.y;
    switch (paramMouseEvent.getID())
    {
    case 501:
      if (!this.r.contains(i, j))
        break;
      press();
      break;
    case 506:
      drag(k, m);
      break;
    case 502:
      release(k, m, this.r.contains(i, j));
    case 503:
    case 504:
    case 505:
    }
  }

  private void press()
  {
    if (this.isDrag)
      return;
    this.isDrag = true;
    this.iSelectList = -1;
    if (this.isDirect)
    {
      this.isList = true;
      this.isDrawList = true;
      repaint();
    }
  }

  private void release(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (!this.isDrag)
      return;
    int i = this.r.height;
    int j = this.r.width;
    int k = paramInt2 / (i - 2) - 1;
    boolean bool = this.isDrawList;
    int m = 0;
    this.isDrag = false;
    this.isList = false;
    if ((k < 0) || (k >= len()) || (paramInt1 < 0) || (paramInt1 >= j))
      k = -1;
    if (k == -1)
    {
      if (paramBoolean)
      {
        if (this.isSelect)
        {
          int n = len();
          unSelect();
          if (++this.iSelect >= n)
            this.iSelect = 0;
          select();
        }
        else
        {
          select();
        }
        m = 1;
      }
    }
    else
    {
      if (this.isSelect)
        unSelect();
      this.iSelect = k;
      select();
    }
    this.iSelectList = -1;
    this.isDrawList = false;
    if (bool)
    {
      Graphics localGraphics = this.tools.primary();
      localGraphics.setColor(this.tools.getBackground());
      localGraphics.fillRect(this.r.x - 1, this.r.y - 1, j + 2, (i - 2) * (len() + 1) + 2);
    }
    if ((m != 0) || (bool))
      this.tools.mPaint(-1);
  }

  public void repaint()
  {
    paint(this.tools.primary(), this.tools.getBack());
  }

  public void select()
  {
    try
    {
      if (this.isField)
      {
        M.class.getField(this.strField).setInt(this.info, this.items[this.iSelect]);
        this.tools.upCS();
        return;
      }
      if (this.isClass)
      {
        this.tools.showW(this.strs[this.iSelect]);
        return;
      }
      if (!this.isDirect)
        this.tools.unSelect();
      int i = this.info.iColor;
      int j = this.info.iMask;
      int k = this.info.iColorMask;
      int m = this.info.iLayer;
      int n = this.info.iLayerSrc;
      int i1 = this.info.iTT;
      int i2 = this.info.iSA;
      int i3 = this.info.iSS;
      this.info.set(this.mgs[this.iSelect]);
      this.info.iColor = i;
      this.info.iMask = j;
      this.info.iColorMask = k;
      this.info.iLayer = m;
      this.info.iLayerSrc = n;
      this.info.iTT = i1;
      if ((i2 != this.info.iSA) || (i3 != this.info.iSS))
        this.tools.mi.up();
      if (!this.isDirect)
        this.isSelect = true;
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void setImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3)
  {
    this.image = paramImage;
    this.imW = paramInt1;
    this.imH = paramInt2;
    this.imIndex = paramInt3;
  }

  public void setSize(int paramInt1, int paramInt2, int paramInt3)
  {
    this.r.setSize(paramInt1, paramInt2);
    this.base = paramInt3;
  }

  public void unSelect()
  {
    if ((this.isSelect) && (!this.isDirect))
      this.mgs[this.iSelect].set(this.info);
    this.isSelect = false;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.normal.ToolList
 * JD-Core Version:    0.6.0
 */