package paintchat.pro;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Hashtable;
import paintchat.M;
import paintchat.M.Info;
import paintchat.Res;
import syi.awt.Awt;
import syi.awt.LComponent;

public class TPen extends LComponent
  implements Runnable
{
  private Tools tools;
  private int iType = 0;
  private boolean isRun = false;
  private LComponent[] cs;
  private TPen tPen;
  private M.Info info;
  private M mg;
  private Res config;
  private Image image = null;
  private Image[] images = null;
  private boolean isDrag = false;
  private int selButton = 0;
  private int selWhite;
  private int selPen = 0;
  private int imW = 0;
  private int imH = 0;
  private int imCount;
  private int selItem = -1;
  private M[] mgs = null;
  private ColorModel cmDef;
  private int sizeTT = 0;

  public TPen(Tools paramTools, M.Info paramInfo, Res paramRes, TPen paramTPen, LComponent[] paramArrayOfLComponent)
  {
    this.tools = paramTools;
    this.info = paramInfo;
    this.mg = this.info.m;
    this.config = paramRes;
    this.tPen = paramTPen;
    this.cs = paramArrayOfLComponent;
  }

  private int getIndex(int paramInt1, int paramInt2, int paramInt3)
  {
    Dimension localDimension = getSize();
    int i = this.imW;
    int j = this.imH;
    if (this.iType != 2)
    {
      i += 3;
      j += 3;
    }
    paramInt1 -= paramInt3;
    int k = (localDimension.width - paramInt3) / i;
    return paramInt2 / j * k + Math.min(paramInt1 / i, k);
  }

  public void init(int paramInt)
  {
    this.iType = paramInt;
    Res localRes = this.config;
    paramInt++;
    int i = 30;
    int j = 30;
    String str1 = String.valueOf(paramInt);
    for (int k = 0; localRes.get(String.valueOf('t') + str1 + k) != null; k++);
    if (k != 0)
    {
      this.mgs = new M[k];
      for (int m = 0; m < k; m++)
      {
        M localM = new M();
        localM.set(localRes.get(String.valueOf('t') + str1 + m));
        this.mgs[m] = localM;
      }
      for (m = k - 1; m >= 0; m--)
      {
        if ((this.mgs[m].iPen != 4) && (this.mgs[m].iPen != 5))
          continue;
        this.selWhite = m;
      }
      String str2 = "res/" + paramInt + ".gif";
      this.image = getToolkit().createImage((byte[])localRes.getRes(str2));
      Awt.wait(this.image);
      localRes.remove(str2);
      i = (int)(this.image.getWidth(null) * LComponent.Q);
      j = (int)(this.image.getHeight(null) / k * LComponent.Q);
      if (LComponent.Q != 1.0F)
        this.image = Awt.toMin(this.image, i, j * k);
      this.imW = i;
      this.imH = j;
      this.imCount = k;
    }
    else
    {
      this.imCount = 0;
      this.imW = 20;
      this.imH = 20;
    }
    i += 3;
    j += 3;
    this.selItem = -1;
    setItem(0, null);
    setDimension(new Dimension(i + 1, j + 1), new Dimension(i + 1, j * k + 1), new Dimension(i * k + 1, j * k + 1));
  }

  public void initHint()
  {
    try
    {
      String str = "res/3.gif";
      this.iType = 3;
      this.imCount = 7;
      int i = this.imCount;
      this.image = getToolkit().createImage((byte[])this.config.getRes(str));
      Awt.wait(this.image);
      this.config.remove(str);
      int j = this.image.getWidth(null);
      int k = this.image.getHeight(null);
      if (LComponent.Q != 1.0F)
      {
        j = (int)(j * LComponent.Q);
        k = (int)(k * LComponent.Q) / i * i;
        this.image = Awt.toMin(this.image, j, k);
      }
      k /= i;
      this.imW = j;
      this.imH = k;
      j += 3;
      k += 3;
      setDimension(new Dimension(j + 1, k + 1), new Dimension(j + 1, k * i + 1), new Dimension(j * i + 1, k * i + 1));
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
  }

  public void initTT()
  {
    this.iType = 2;
    Res localRes = this.config;
    getToolkit();
    this.cmDef = new DirectColorModel(24, 65280, 65280, 255);
    this.imW = (this.imH = (int)(34.0F * LComponent.Q));
    try
    {
      String str = "tt_size";
      this.images = new Image[Integer.parseInt(localRes.get(str))];
      localRes.remove(str);
      int i = this.imW * 5 + 1;
      int j = ((this.images.length + 12) / 5 + 1) * this.imW + 1;
      setDimension(new Dimension(this.imW + 1, this.imW + 1), new Dimension(i, j), new Dimension(i * 2, j * 2));
    }
    catch (RuntimeException localRuntimeException)
    {
    }
  }

  private void mouseH(MouseEvent paramMouseEvent)
  {
    if (paramMouseEvent.getID() != 501)
      return;
    int i = getIndex(paramMouseEvent.getX(), paramMouseEvent.getY(), 0);
    if (i >= 7)
      return;
    this.mg.iHint = i;
    repaint();
  }

  private void mousePen(MouseEvent paramMouseEvent)
  {
    if (paramMouseEvent.getID() == 501)
    {
      int i = getIndex(paramMouseEvent.getX(), paramMouseEvent.getY(), 0);
      if (i >= this.imCount)
        return;
      setItem(i, null);
    }
  }

  private void mouseTT(MouseEvent paramMouseEvent)
  {
    if (paramMouseEvent.getID() == 501)
    {
      getSize();
      int i = getIndex(paramMouseEvent.getX(), paramMouseEvent.getY(), 0);
      if (i >= this.images.length + 12)
        return;
      this.mg.iTT = i;
      repaint();
    }
  }

  public void paint2(Graphics paramGraphics)
  {
    switch (this.iType)
    {
    case 2:
      paintTT(paramGraphics);
      break;
    case 3:
      this.selItem = this.mg.iHint;
    default:
      paintPen(paramGraphics);
    }
  }

  private void paintPen(Graphics paramGraphics)
  {
    if (this.image == null)
      return;
    int i = 0;
    int j = 0;
    int k = this.imW;
    int m = this.imH;
    int n = this.imW + 3;
    int i1 = this.imH + 3;
    Dimension localDimension = getSize();
    for (int i2 = 0; i2 < this.imCount; i2++)
    {
      paramGraphics.setColor(this.selItem == i2 ? Awt.cFSel : Awt.cF);
      paramGraphics.drawRect(i + 1, j + 1, k + 1, m + 1);
      paramGraphics.drawImage(this.image, i + 2, j + 2, i + k + 2, j + m + 2, 0, i2 * m, k, (i2 + 1) * m, null);
      if (this.selItem == i2)
      {
        paramGraphics.setColor(Color.black);
        paramGraphics.fillRect(i + 2, j + 2, k, 1);
        paramGraphics.fillRect(i + 2, j + 3, 1, m - 1);
      }
      i = i + n * 2 >= localDimension.width ? 0 : i + n;
      j = i == 0 ? j + i1 : j;
      if (j + i1 >= localDimension.height)
        break;
    }
  }

  private void paintTT(Graphics paramGraphics)
  {
    if (this.images == null)
      return;
    if (!this.isRun)
    {
      Thread localThread = new Thread(this);
      localThread.setPriority(1);
      localThread.setDaemon(true);
      localThread.start();
      this.isRun = true;
    }
    int i = this.images.length + 11;
    int j = 0;
    int k = 0;
    int m = this.imW;
    int n = this.imH;
    int i1 = m - 3;
    int[] arrayOfInt = this.tools.iBuffer;
    Dimension localDimension = getSize();
    getToolkit();
    int i4 = getBackground().getRGB();
    for (int i5 = -1; i5 < i; i5++)
    {
      paramGraphics.setColor(i5 + 1 == this.mg.iTT ? Awt.cFSel : Awt.cF);
      paramGraphics.drawRect(j + 1, k + 1, m - 2, n - 2);
      if (i5 == -1)
      {
        paramGraphics.setColor(Color.blue);
        paramGraphics.fillRect(j + 2, k + 2, m - 3, n - 3);
      }
      else if (i5 < 11)
      {
        synchronized (arrayOfInt)
        {
          int i6 = 0;
          int i7 = i5;
          for (int i3 = 0; i3 < i1; i3++)
            for (int i2 = 0; i2 < i1; i2++)
              arrayOfInt[(i6++)] = (M.isTone(i7, i2, i3) ? i4 : -16776961);
          paramGraphics.drawImage(this.tools.mkImage(i1, i1), j + 2, k + 2, getBackground(), null);
        }
      }
      else
      {
        Image localImage = this.images[(i5 - 11)];
        if (localImage == null)
        {
          paramGraphics.setColor(Color.blue);
          paramGraphics.fillRect(j + 2, k + 2, m - 3, n - 3);
        }
        else
        {
          paramGraphics.drawImage(localImage, j + 2, k + 2, getBackground(), null);
        }
      }
      j += m;
      if (j + m < localDimension.width)
        continue;
      j = 0;
      k += n;
      if (k + n >= localDimension.height)
        break;
    }
  }

  public void pMouse(MouseEvent paramMouseEvent)
  {
    switch (this.iType)
    {
    default:
      mousePen(paramMouseEvent);
      break;
    case 2:
      mouseTT(paramMouseEvent);
      break;
    case 3:
      mouseH(paramMouseEvent);
    }
  }

  public void run()
  {
    try
    {
      int i = this.imW;
      int j = this.imH;
      for (int k = 0; k < this.images.length; k++)
      {
        if (this.images[k] != null)
          continue;
        float[] arrayOfFloat = this.info.getTT(k + 12);
        int[] arrayOfInt = new int[arrayOfFloat.length];
        for (int m = 0; m < arrayOfInt.length; m++)
          arrayOfInt[m] = ((int)((1.0F - arrayOfFloat[m]) * 255.0F) << 8 | 0xFF);
        m = (int)Math.sqrt(arrayOfInt.length);
        this.images[k] = Awt.toMin(createImage(new MemoryImageSource(m, m, this.cmDef, arrayOfInt, 0, m)), i - 3, j - 3);
        if (k % 5 != 2)
          continue;
        repaint();
      }
      repaint();
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void setItem(int paramInt, M paramM)
  {
    int i;
    if (this.iType == 1)
    {
      this.tPen.setItem(-1, paramM);
    }
    else
    {
      if ((this.selItem >= 0) && (this.selItem < this.imCount))
        this.mgs[this.selItem].set(this.mg);
      if (paramInt >= 0)
      {
        i = this.mgs[paramInt].iPen;
        if ((i == 4) || (i == 5))
          this.selWhite = paramInt;
        else
          this.selPen = paramInt;
      }
    }
    this.selItem = paramInt;
    if ((paramInt >= 0) || (paramM != null))
    {
      i = this.mg.iColor;
      int j = this.mg.iColorMask;
      int k = this.mg.iMask;
      int m = this.mg.iLayer;
      this.mg.set(paramM != null ? paramM : this.mgs[paramInt]);
      this.mg.iColor = i;
      this.mg.iColorMask = j;
      this.mg.iMask = k;
      this.mg.iLayer = m;
      if (this.tPen != null)
        this.tPen.repaint();
      for (int n = 0; n < this.cs.length; n++)
      {
        if (this.cs[n] == null)
          continue;
        this.cs[n].repaint();
      }
    }
    else
    {
      repaint();
    }
  }

  public void undo(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (this.selWhite != this.selItem)
        setItem(this.selWhite, null);
    }
    else if (this.selPen != this.selItem)
      setItem(this.selPen, null);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.pro.TPen
 * JD-Core Version:    0.6.0
 */