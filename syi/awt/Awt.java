package syi.awt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

public class Awt
{
  public static Frame main_frame = null;
  public static Color cC;
  public static Color cDk;
  public static Color cLt;
  public static Color cBk;
  public static Color cFore;
  public static Color cF;
  public static Color cFSel;
  public static Color clBar;
  public static Color clLBar;
  public static Color clBarT;
  private static Font fontDef = null;
  private static float Q = 0.0F;
  private static MediaTracker mt = null;

  public static final void drawFrame(Graphics paramGraphics, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    setup();
    drawFrame(paramGraphics, paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4, cDk, cLt);
  }

  public static final void drawFrame(Graphics paramGraphics, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor1, Color paramColor2)
  {
    setup();
    int i = paramInt1 + paramInt3;
    int j = paramInt2 + paramInt4;
    paramGraphics.setColor(paramColor1 == null ? cDk : paramColor1);
    paramGraphics.fillRect(paramInt1, paramInt2, paramInt3, 1);
    paramGraphics.fillRect(paramInt1, paramInt2 + 1, 1, paramInt4 - 2);
    paramGraphics.fillRect(paramInt1 + 2, j - 2, paramInt3 - 2, 1);
    paramGraphics.fillRect(i - 1, paramInt2 + 2, 1, paramInt4 - 4);
    paramGraphics.setColor(paramColor2 == null ? cLt : paramColor2);
    if (!paramBoolean)
    {
      paramGraphics.fillRect(paramInt1 + 1, paramInt2 + 1, paramInt3 - 2, 1);
      paramGraphics.fillRect(paramInt1 + 1, paramInt2 + 2, 1, paramInt4 - 4);
    }
    paramGraphics.fillRect(paramInt1 + 1, j - 1, paramInt3 - 1, 1);
    paramGraphics.fillRect(i, paramInt2 + 1, 1, paramInt4 - 2);
  }

  public static final void fillFrame(Graphics paramGraphics, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    fillFrame(paramGraphics, paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4, cC, cDk, cDk, cLt);
  }

  public static final void fillFrame(Graphics paramGraphics, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
  {
    drawFrame(paramGraphics, paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4, paramColor3, paramColor4);
    paramGraphics.setColor(paramColor1 == null ? cC : paramBoolean ? paramColor2 : paramColor2 == null ? cDk : paramColor1);
    paramGraphics.fillRect(paramInt1 + 2, paramInt2 + 2, paramInt3 - 3, paramInt4 - 4);
  }

  public static void getDef(Component paramComponent)
  {
    setup();
    paramComponent.setBackground(cBk);
    paramComponent.setForeground(cFore);
    paramComponent.setFont(getDefFont());
    if ((paramComponent instanceof LComponent))
    {
      LComponent localLComponent = (LComponent)paramComponent;
      localLComponent.clBar = clBar;
      localLComponent.clLBar = clLBar;
      localLComponent.clBarT = clBarT;
      localLComponent.clFrame = cF;
    }
  }

  public static Font getDefFont()
  {
    if (fontDef == null)
      fontDef = new Font("sansserif", 0, (int)(16.0F * q()));
    return fontDef;
  }

  public static Component getParent(Component paramComponent)
  {
    Container localContainer = paramComponent.getParent();
    return (localContainer instanceof Window) ? localContainer : localContainer == null ? paramComponent : getParent(localContainer);
  }

  public static Frame getPFrame()
  {
    if (main_frame == null)
      main_frame = new Frame();
    return main_frame;
  }

  public static boolean isR(MouseEvent paramMouseEvent)
  {
    return (paramMouseEvent.isAltDown()) || (paramMouseEvent.isControlDown()) || ((paramMouseEvent.getModifiers() & 0x4) != 0);
  }

  public static boolean isWin()
  {
    String str = "Win";
    return System.getProperty("os.name", str).startsWith(str);
  }

  public static void moveCenter(Window paramWindow)
  {
    Dimension localDimension1 = paramWindow.getToolkit().getScreenSize();
    Dimension localDimension2 = paramWindow.getSize();
    paramWindow.setLocation(localDimension1.width / 2 - localDimension2.width / 2, localDimension1.height / 2 - localDimension2.height / 2);
  }

  public static InputStream openStream(URL paramURL)
    throws IOException
  {
    URLConnection localURLConnection = paramURL.openConnection();
    localURLConnection.setUseCaches(true);
    return localURLConnection.getInputStream();
  }

  public static float q()
  {
    if (Q == 0.0F)
    {
      Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
      int i = 2264;
      int j = localDimension.width + localDimension.height;
      Q = Math.min(1.0F + (j - i) / i / 2.0F, 2.0F);
    }
    return Q;
  }

  public static String replaceText(String paramString1, String paramString2, String paramString3)
  {
    if (paramString1.indexOf(paramString3) < 0)
      return paramString1;
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      char[] arrayOfChar = paramString3.toCharArray();
      if (arrayOfChar.length <= 0)
        return paramString1;
      int i = 0;
      int j = 0;
      int k = paramString1.length();
      for (int m = 0; m < k; m++)
      {
        char c;
        if ((c = paramString1.charAt(m)) == arrayOfChar[j])
        {
          if (j == 0)
            i = m;
          j++;
          if (j < arrayOfChar.length)
            continue;
          j = 0;
          localStringBuffer.append(paramString2);
        }
        else
        {
          if (j > 0)
          {
            for (int n = 0; n < j; n++)
              localStringBuffer.append(paramString1.charAt(i + n));
            j = 0;
          }
          localStringBuffer.append(c);
        }
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      System.out.println("replace" + localRuntimeException);
    }
    return localStringBuffer.toString();
  }

  public static void setDef(Component paramComponent, boolean paramBoolean)
  {
    try
    {
      Object localObject;
      if (!paramBoolean)
      {
        paramBoolean = true;
      }
      else
      {
        localObject = paramComponent.getParent();
        ((Component)localObject).setFont(((Component)localObject).getFont());
        ((Component)localObject).setForeground(((Component)localObject).getForeground());
        ((Component)localObject).setBackground(((Component)localObject).getBackground());
      }
      if ((paramComponent instanceof Container))
      {
        localObject = ((Container)paramComponent).getComponents();
        if (localObject != null)
          for (int i = 0; i < localObject.length; i++)
          {
            paramComponent = localObject[i];
            setDef(paramComponent, true);
          }
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public static void setPFrame(Frame paramFrame)
  {
    main_frame = paramFrame;
  }

  public static final void setup()
  {
    if (cC == null)
    {
      cC = new Color(13487565);
      cDk = Awt.cLt = null;
    }
    if (cDk == null)
      cDk = cC.darker();
    if (cLt == null)
      cLt = cC.brighter();
    if (cBk == null)
      cBk = new Color(13619199);
    if (cFore == null)
      cFore = new Color(5263480);
    if (cF == null)
      cF = cFore;
    if (cFSel == null)
      cFSel = new Color(15610675);
    if (clBar == null)
      clBar = new Color(6711039);
    if (clLBar == null)
      clLBar = new Color(8947967);
    if (clBarT == null)
      clBarT = Color.white;
  }

  public static Image toMin(Image paramImage, int paramInt1, int paramInt2)
  {
    Image localImage = paramImage.getScaledInstance(paramInt1, paramInt2, 16);
    paramImage.flush();
    wait(localImage);
    return localImage;
  }

  public static String trimString(String paramString1, String paramString2, String paramString3)
  {
    if ((paramString1 == null) || (paramString1.length() <= 0) || (paramString2 == null) || (paramString3 == null))
      return "";
    try
    {
      int i;
      if ((i = paramString1.indexOf(paramString2)) == -1)
        return "";
      int j;
      if ((j = paramString1.indexOf(paramString3, i + paramString2.length())) == -1)
        j = paramString1.length() - 1;
      return paramString1.substring(i + paramString2.length(), j);
    }
    catch (RuntimeException localRuntimeException)
    {
      System.out.println("t_trimString:" + localRuntimeException.toString());
    }
    return "";
  }

  public static int[] getPix(Image paramImage)
  {
    try
    {
      PixelGrabber localPixelGrabber = new PixelGrabber(paramImage, 0, 0, paramImage.getWidth(null), paramImage.getHeight(null), true);
      localPixelGrabber.grabPixels();
      return (int[])localPixelGrabber.getPixels();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return null;
  }

  public static void wait(Image paramImage)
  {
    if (mt == null)
      mt = new MediaTracker(getPFrame());
    try
    {
      mt.addImage(paramImage, 0);
      mt.waitForID(0);
    }
    catch (InterruptedException localInterruptedException)
    {
    }
    mt.removeImage(paramImage, 0);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.Awt
 * JD-Core Version:    0.6.0
 */