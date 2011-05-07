package paintchat.pro;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.net.URL;
import paintchat.Res;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.awt.LComponent;

public class TBar extends LComponent
{
  private boolean isOption;
  private Res res;
  private Res config;
  private LComponent[] cs;
  private String[] strs;
  private boolean[] flags;
  private Mi mi;
  private URL codebase;
  private String strAuthor;
  private int W;
  private int H;
  private Image image = null;
  private Color[][] cls = new Color[2][3];
  private Color clT;

  public TBar(Res paramRes1, Res paramRes2, LComponent[] paramArrayOfLComponent)
  {
    this.res = paramRes2;
    this.config = paramRes1;
    this.cs = paramArrayOfLComponent;
    this.H = (int)(19.0F * LComponent.Q);
  }

  public final void drawFrame(Graphics paramGraphics, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Color[] arrayOfColor = this.cls[0];
    Awt.drawFrame(paramGraphics, paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4, arrayOfColor[2], arrayOfColor[1]);
  }

  public final void fillFrame(Graphics paramGraphics, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    drawFrame(paramGraphics, paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    Color[] arrayOfColor = this.cls[0];
    Awt.setup();
    Awt.fillFrame(paramGraphics, paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4, this.cls[0][0], this.cls[1][0], arrayOfColor[2], arrayOfColor[1]);
  }

  public void init()
  {
    this.isHide = false;
    int i = this.H;
    FontMetrics localFontMetrics = getFontMetrics(getFont());
    int j = this.cs.length - 1;
    int k = 0;
    this.strs = new String[j];
    for (int m = 0; m < j; m++)
    {
      String str = this.res.res("window_" + m);
      k = Math.max(localFontMetrics.stringWidth(str) + str.length(), k);
      this.strs[m] = str;
      this.cs[m].setTitle(str);
    }
    s();
    Color[] arrayOfColor = this.cls[0];
    this.cls[0] = this.cls[1];
    this.cls[1] = arrayOfColor;
    this.W = k;
    setDimension(new Dimension(this.W, i), new Dimension(this.W, i * j), new Dimension(this.W * j, i * j));
  }

  public void initOption(URL paramURL, Mi paramMi)
  {
    this.isOption = true;
    this.mi = paramMi;
    this.codebase = paramURL;
    int i = 2;
    this.strs = new String[i];
    this.flags = new boolean[i];
    this.strAuthor = this.res.res("option_author");
    s();
    setDimension(null, new Dimension(10, 10), null);
  }

  public void mouseO(MouseEvent paramMouseEvent)
  {
    if (paramMouseEvent.getID() != 501)
      return;
    int i = paramMouseEvent.getY();
    if (i <= this.H)
    {
      boolean bool = this.flags[0] == 0;
      this.mi.isGUI = bool;
      this.flags[0] = bool;
      repaint();
      this.mi.setVisible(false);
      this.mi.setDimension(null, this.mi.getSize(), getParent().getSize());
      this.mi.setVisible(true);
    }
    else if (i <= this.H * 2)
    {
      z();
      repaint();
    }
    else if (i > this.H * 3)
    {
      try
      {
        String str = this.config.getP("app_url", "http://shichan.jp/");
        if (((str.length() != 1) || (str.charAt(0) != '_')) && (this.mi.alert("kakunin_0", true)))
        {
          Applet localApplet = (Applet)getParent().getParent();
          localApplet.getAppletContext().showDocument(new URL(localApplet.getCodeBase(), str), "_blank");
        }
      }
      catch (Throwable localThrowable)
      {
        localThrowable.printStackTrace();
      }
    }
  }

  public void paint2(Graphics paramGraphics)
  {
    getSize();
    if (this.isOption)
      paintO(paramGraphics);
    else
      paintBar(paramGraphics);
  }

  private void paintBar(Graphics paramGraphics)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    Dimension localDimension = getSize();
    int m = this.W;
    int n = this.H;
    for (int i1 = 0; i1 < this.cs.length; i1++)
    {
      if ((this.cs[i1] == null) || (this.cs[i1] == this))
        continue;
      fillFrame(paramGraphics, !this.cs[i1].isVisible(), i, j, m, n);
      paramGraphics.setColor(this.clT == null ? Awt.cFore : this.clT);
      paramGraphics.drawString(this.strs[(k++)], i + 2, j + n - 3);
      if (i += m + m <= localDimension.width)
        continue;
      i = 0;
      j += n;
    }
  }

  private void paintO(Graphics paramGraphics)
  {
    if (this.image == null)
      try
      {
        FontMetrics localFontMetrics = getFontMetrics(getFont());
        int k = this.strs.length;
        m = 0;
        for (n = 0; n < k; n++)
        {
          String str = this.res.res("option_" + n);
          m = Math.max(localFontMetrics.stringWidth(str + " OFF") + str.length() + 3, m);
          this.strs[n] = str;
        }
        m = Math.max(localFontMetrics.stringWidth(this.strAuthor) + this.strAuthor.length(), m);
        this.image = getToolkit().createImage((byte[])this.config.getRes("bn.gif"));
        Awt.wait(this.image);
        n = this.image.getWidth(null);
        i1 = this.image.getHeight(null);
        if (LComponent.Q > 1.0F)
        {
          n = (int)(n * LComponent.Q);
          i1 = (int)(i1 * LComponent.Q);
        }
        this.W = Math.max(m, n);
        this.H = (localFontMetrics.getHeight() + 2);
        Dimension localDimension2 = new Dimension(this.W, this.H * (k + 2) + i1 + 2);
        setDimension(new Dimension(this.W, this.H), localDimension2, localDimension2);
      }
      catch (RuntimeException localRuntimeException)
      {
      }
    int i = this.image.getWidth(null);
    int j = this.image.getHeight(null);
    if (LComponent.Q > 1.0F)
    {
      i = (int)(i * LComponent.Q);
      j = (int)(j * LComponent.Q);
    }
    Dimension localDimension1 = getSize();
    paramGraphics.setFont(getFont());
    int m = 0;
    int n = 0;
    int i1 = this.W;
    int i2 = this.H;
    int i3 = this.strs.length;
    for (int i4 = 0; i4 < i3; i4++)
    {
      fillFrame(paramGraphics, this.flags[i4], m, n, i1, i2);
      paramGraphics.setColor(this.clT == null ? getForeground() : this.clT);
      paramGraphics.drawString(this.strs[i4] + (this.flags[i4] != 0 ? " ON" : " OFF"), m + 2, n + i2 - 3);
      n += i2;
    }
    n = localDimension1.height - this.H - j - 2;
    paramGraphics.drawRect(m, n, i1 - 1, localDimension1.height - n - 1);
    paramGraphics.drawImage(this.image, (localDimension1.width - i) / 2, n + 2, i, j, getBackground(), null);
    paramGraphics.drawString(this.strAuthor, (localDimension1.width - paramGraphics.getFontMetrics().stringWidth(this.strAuthor)) / 2, localDimension1.height - 2);
  }

  public void pMouse(MouseEvent paramMouseEvent)
  {
    if (this.isOption)
    {
      mouseO(paramMouseEvent);
      return;
    }
    int i = paramMouseEvent.getID();
    int j = this.W;
    int k = this.H;
    Dimension localDimension = getSize();
    if (i == 504)
      repaint();
    if (i == 501)
    {
      int m = localDimension.width / j * (paramMouseEvent.getY() / k) + paramMouseEvent.getX() / j;
      if ((m >= this.cs.length) || (this.cs[m] == this))
        return;
      this.cs[m].setVisible(!this.cs[m].isVisible());
      invalidate();
      repaint();
    }
  }

  private void s()
  {
    String str = "pro_menu_color_off";
    for (int i = 0; i < 2; i++)
    {
      Color[] arrayOfColor = this.cls[i];
      if (this.config.getP(str) != null)
        arrayOfColor[0] = new Color(this.config.getP(str, 0));
      if (this.config.getP(str + "_hl") != null)
        arrayOfColor[1] = new Color(this.config.getP(str + "_hl", 0));
      if (this.config.getP(str + "_dk") != null)
        arrayOfColor[2] = new Color(this.config.getP(str + "_dk", 0));
      str = "pro_menu_color_on";
    }
    if (this.config.getP("pro_menu_color_text") != null)
      this.clT = new Color(this.config.getP("pro_menu_color_text", 0));
  }

  public void z()
  {
    boolean bool = this.flags[1] == 0;
    this.flags[1] = bool;
    for (int i = 0; i < this.cs.length; i++)
    {
      if (this.cs[i] == null)
        continue;
      this.cs[i].isUpDown = bool;
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.pro.TBar
 * JD-Core Version:    0.6.0
 */