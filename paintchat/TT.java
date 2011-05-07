package paintchat;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Hashtable;
import syi.awt.Awt;
import syi.awt.LComponent;

public class TT extends LComponent
  implements SW, Runnable
{
  private ToolBox ts;
  private M.Info info;
  private M.User user;
  private M mg;
  private boolean isRun = false;
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
  private int sizeTT = 0;
  private int iLast = -1;

  private int getIndex(int paramInt1, int paramInt2, int paramInt3)
  {
    Dimension localDimension = getSize();
    int i = this.imW;
    int j = this.imH;
    paramInt1 -= paramInt3;
    int k = (localDimension.width - paramInt3) / i;
    return paramInt2 / j * k + Math.min(paramInt1 / i, k);
  }

  public void lift()
  {
  }

  public void mPack()
  {
    inParent();
    Container localContainer = getParent();
    Dimension localDimension = getMaximumSize();
    localDimension.height = (localContainer.getSize().height - getGapH());
  }

  public void mSetup(ToolBox paramToolBox, M.Info paramInfo, M.User paramUser, M paramM, Res paramRes1, Res paramRes2)
  {
    this.ts = paramToolBox;
    this.info = paramInfo;
    this.user = paramUser;
    this.mg = paramM;
    setTitle(paramRes2.getP("window_3"));
    getToolkit();
    this.imW = (this.imH = (int)(34.0F * LComponent.Q));
    try
    {
      String str = "tt_size";
      this.images = new Image[Integer.parseInt(paramRes2.get(str))];
      paramRes2.remove(str);
      int i = this.imW * 5 + 1;
      int j = ((this.images.length + 12) / 5 + 1) * this.imW + 1;
      setDimension(new Dimension(this.imW + 1, this.imW + 1), new Dimension(i, j), new Dimension(i, j));
    }
    catch (RuntimeException localRuntimeException)
    {
    }
  }

  public void paint2(Graphics paramGraphics)
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
    M localM = this.mg;
    int[] arrayOfInt = this.user.getBuffer();
    Color localColor = getBackground();
    int i4 = 255;
    localColor.getRGB();
    Dimension localDimension = getSize();
    getToolkit();
    this.iLast = localM.iTT;
    for (int i5 = -1; i5 < i; i5++)
    {
      paramGraphics.setColor(i5 + 1 == localM.iTT ? Awt.cFSel : Awt.cF);
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
              arrayOfInt[(i6++)] = (M.isTone(i7, i2, i3) ? -1 : i4);
          paramGraphics.drawImage(this.user.mkImage(i1, i1), j + 2, k + 2, localColor, null);
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
          paramGraphics.drawImage(localImage, j + 2, k + 2, localColor, null);
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

  public void run()
  {
    try
    {
      DirectColorModel localDirectColorModel = new DirectColorModel(24, 65280, 65280, 255);
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
        this.images[k] = Awt.toMin(createImage(new MemoryImageSource(m, m, localDirectColorModel, arrayOfInt, 0, m)), i - 3, j - 3);
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

  public void up()
  {
    if (this.iLast != this.mg.iTT)
      repaint();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.TT
 * JD-Core Version:    0.6.0
 */