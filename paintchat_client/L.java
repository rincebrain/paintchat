package paintchat_client;

import java.awt.AWTEvent;
import java.awt.CheckboxMenuItem;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuComponent;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import paintchat.LO;
import paintchat.M;
import paintchat.M.Info;
import paintchat.M.User;
import paintchat.Res;
import paintchat.ToolBox;
import syi.awt.Awt;
import syi.awt.LComponent;

public class L extends LComponent
  implements ActionListener, ItemListener
{
  private Mi mi;
  private ToolBox tool;
  private Res res;
  private M m;
  private int B = -1;
  private Font bFont;
  private int bH;
  private int bW;
  private int base;
  private int layer_size = -1;
  private int mouse = -1;
  private boolean isASlide = false;
  private int Y;
  private int YOFF;
  private PopupMenu popup = null;
  private String strMenu;
  private boolean is_pre = true;
  private boolean is_DIm = false;
  private Color cM;
  private Color cT;
  private String sL;

  public L(Mi paramMi, ToolBox paramToolBox, Res paramRes1, Res paramRes2)
  {
    this.tool = paramToolBox;
    this.bFont = Awt.getDefFont();
    this.bFont = new Font(this.bFont.getName(), 0, (int)(this.bFont.getSize() * 0.8F));
    FontMetrics localFontMetrics = getFontMetrics(this.bFont);
    this.bH = (localFontMetrics.getHeight() + 6);
    this.base = (this.bH - 2 - localFontMetrics.getMaxDescent());
    int i = (int)(60.0F * LComponent.Q);
    String str = paramRes1.res("Layer");
    this.sL = str;
    this.strMenu = paramRes1.res("MenuLayer");
    this.cM = new Color(paramRes2.getP("l_m_color", 0));
    this.cT = new Color(paramRes2.getP("l_m_color_text", 16777215));
    localFontMetrics = getFontMetrics(this.bFont);
    i = Math.max(localFontMetrics.stringWidth(str + "00") + 4, i);
    i = Math.max(localFontMetrics.stringWidth(this.strMenu) + 4, i);
    this.bW = i;
    i += this.bH + 100;
    this.mi = paramMi;
    this.res = paramRes1;
    setTitle(str);
    this.isGUI = true;
    this.m = paramMi.info.m;
    Dimension localDimension = new Dimension(this.bW, this.bH);
    setDimension(new Dimension(localDimension), localDimension, new Dimension());
    setSize(getMaximumSize());
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    try
    {
      String str = paramActionEvent.getActionCommand();
      int i = this.popup.getItemCount();
      for (int j = 0; j < i; j++)
        if (this.popup.getItem(j).getLabel().equals(str))
          break;
      M.Info localInfo = this.mi.info;
      M localM = mg();
      setA(localM);
      LO[] arrayOfLO = localInfo.layers;
      int k = localInfo.L;
      byte[] arrayOfByte = new byte[4];
      int n = 0;
      int i1 = 0;
      int i2 = this.mi.user.wait;
      this.mi.user.wait = -2;
      if (this.popup.getName().charAt(0) == 'm')
      {
        switch (j)
        {
        case 0:
          localM.setRetouch(new int[] { 1, k + 1 }, null, 0, false);
          n = 1;
          i1 = 1;
          break;
        case 1:
          if ((localInfo.L <= 1) || (!confirm(arrayOfLO[localM.iLayer].name + this.res.res("DelLayerQ"))))
            return;
          localM.iLayerSrc = localM.iLayer;
          localM.setRetouch(new int[] { 2 }, null, 0, false);
          n = 1;
          break;
        case 2:
          dFusion();
          break;
        case 3:
          config(this.m.iLayer);
        default:
          break;
        }
      }
      else if (j == 0)
      {
        localM.iHint = 14;
        localM.setRetouch(new int[] { 3 }, null, 0, false);
        n = 1;
      }
      else
      {
        int i3 = (byte)arrayOfLO[localM.iLayerSrc].iCopy;
        if (i3 == 1)
        {
          dFusion();
        }
        else
        {
          localM.iHint = 3;
          localM.iPen = 20;
          arrayOfByte[0] = i3;
          localM.setRetouch(new int[] { 0, localInfo.W << 16 | localInfo.H }, arrayOfByte, 4, false);
          n = 1;
        }
      }
      if (n != 0)
      {
        localM.draw();
        if (i1 != 0)
          localInfo.layers[(localInfo.L - 1)].makeName(this.sL);
        this.mi.send(localM);
      }
      this.m.iLayerSrc = (this.m.iLayer = Math.min(this.m.iLayer, localInfo.L - 1));
      repaint();
      this.mi.user.wait = i2;
      this.mi.repaint();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private int b(int paramInt)
  {
    if (paramInt < this.bH)
      return 0;
    return Math.max(this.mi.info.L - (paramInt / this.bH - 1), 1);
  }

  private void send(int[] paramArrayOfInt, byte[] paramArrayOfByte)
  {
    M localM = mg();
    setA(localM);
    localM.setRetouch(paramArrayOfInt, paramArrayOfByte, paramArrayOfByte != null ? paramArrayOfByte.length : 0, false);
    int i = this.mi.user.wait;
    try
    {
      localM.draw();
      this.mi.send(localM);
    }
    catch (Throwable localThrowable)
    {
    }
    repaint();
    this.mi.user.wait = i;
    this.mi.repaint();
  }

  private void dFusion()
  {
    if (!confirm(this.res.res("CombineVQ")))
      return;
    try
    {
      int i = this.mi.info.L;
      LO[] arrayOfLO = this.mi.info.layers;
      int j = 0;
      for (int k = 0; k < i; k++)
      {
        if (arrayOfLO[k].iAlpha <= 0.0F)
          continue;
        j++;
      }
      if (j <= 0)
        return;
      k = this.mi.user.wait;
      M localM = mg();
      setA(localM);
      byte[] arrayOfByte = new byte[j * 4 + 2];
      int n = 2;
      arrayOfByte[0] = (byte)j;
      for (int i1 = 0; i1 < i; i1++)
      {
        LO localLO = arrayOfLO[i1];
        if (localLO.iAlpha <= 0.0F)
          continue;
        arrayOfByte[(n++)] = (byte)i1;
        arrayOfByte[(n++)] = (byte)(int)(localLO.iAlpha * 255.0F);
        arrayOfByte[(n++)] = (byte)localLO.iCopy;
        arrayOfByte[(n++)] = 41;
      }
      this.mi.user.wait = -2;
      localM.setRetouch(new int[] { 7 }, arrayOfByte, arrayOfByte.length, false);
      localM.draw();
      this.mi.send(localM);
      this.mi.user.wait = k;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private boolean confirm(String paramString)
  {
    return Me.confirm(paramString, true);
  }

  private void dL(Graphics paramGraphics, int paramInt1, int paramInt2)
  {
    if (this.mi.info.L <= paramInt2)
      return;
    getSize();
    int i = this.bW - 1;
    int j = this.bH - 2;
    Color localColor = this.m.iLayer == paramInt2 ? Awt.cFSel : this.clFrame;
    LO localLO = this.mi.info.layers[paramInt2];
    paramGraphics.setColor(localColor);
    paramGraphics.drawRect(0, paramInt1, i, j);
    paramGraphics.setColor(Awt.cFore);
    paramGraphics.setFont(this.bFont);
    paramGraphics.drawString(localLO.name, 2, paramInt1 + this.base);
    paramGraphics.setColor(localColor);
    paramGraphics.drawRect(this.bW, paramInt1, 100, j);
    paramGraphics.setColor(this.cM);
    int k = (int)(100.0F * localLO.iAlpha);
    paramGraphics.fillRect(this.bW + 1, paramInt1 + 1, k - 1, j - 1);
    paramGraphics.setColor(this.cT);
    paramGraphics.drawString(k + "%", this.bW + 3, paramInt1 + this.base);
    int n = this.bW + 100;
    paramGraphics.setColor(localColor);
    paramGraphics.drawRect(n + 1, paramInt1, j - 2, j);
    paramGraphics.setColor(Awt.cFore);
    if (k == 0)
    {
      paramGraphics.drawLine(n + 2, paramInt1 + 1, n + j - 2, paramInt1 + j - 1);
      paramGraphics.drawLine(1, paramInt1 + 1, i - 1, paramInt1 + this.bH - 3);
    }
    else
    {
      paramGraphics.drawOval(n + 2, paramInt1 + 2, j - 3, j - 3);
    }
  }

  public Dimension getMaximumSize()
  {
    Dimension localDimension = super.getMaximumSize();
    if (this.mi != null)
      localDimension.setSize(this.bW + 100 + this.bH, this.bH * (this.mi.info.L + 1));
    return localDimension;
  }

  public void itemStateChanged(ItemEvent paramItemEvent)
  {
    this.is_pre = (!this.is_pre);
  }

  private M mg()
  {
    M localM = new M(this.mi.info, this.mi.user);
    localM.iAlpha = 255;
    localM.iHint = 14;
    localM.iLayer = this.m.iLayer;
    localM.iLayerSrc = this.m.iLayerSrc;
    return localM;
  }

  private void p()
  {
    repaint();
    this.tool.up();
  }

  public void paint2(Graphics paramGraphics)
  {
    try
    {
      int i = this.mi.info.L;
      for (int j = 0; j < i; j++)
      {
        LO localLO = this.mi.info.layers[j];
        if (localLO.name != null)
          continue;
        localLO.makeName(this.sL);
      }
      if (this.layer_size != i)
      {
        this.layer_size = i;
        setSize(getMaximumSize());
        return;
      }
      Dimension localDimension = getSize();
      int k = i - 1;
      int n = this.bH;
      paramGraphics.setFont(this.bFont);
      paramGraphics.setColor(Awt.cBk);
      paramGraphics.fillRect(0, 0, localDimension.width, localDimension.height);
      while (n < localDimension.height)
      {
        if ((this.isASlide) || (k != this.mouse - 1))
          dL(paramGraphics, n, k);
        k--;
        if (k < 0)
          break;
        n += this.bH;
      }
      if ((!this.isASlide) && (this.mouse > 0))
        dL(paramGraphics, this.Y - this.YOFF, this.mouse - 1);
      Awt.drawFrame(paramGraphics, this.mouse == 0, 0, 0, this.bW, this.bH - 2);
      paramGraphics.setColor(Awt.cFore);
      paramGraphics.drawString(this.strMenu, 3, this.bH - 6);
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void pMouse(MouseEvent paramMouseEvent)
  {
    try
    {
      int i = this.Y = paramMouseEvent.getY();
      int j = paramMouseEvent.getX();
      M.Info localInfo = this.mi.info;
      boolean bool = Awt.isR(paramMouseEvent);
      int i1;
      switch (paramMouseEvent.getID())
      {
      case 501:
        if (this.mouse >= 0)
          break;
        int k = b(i);
        int n = k - 1;
        if (n >= 0)
        {
          if (j > this.bW + 100 + 1)
          {
            i1 = this.mi.user.wait;
            this.mi.user.wait = -2;
            if (bool)
              for (int i2 = 0; i2 < localInfo.L; i2++)
                setAlpha(i2, i2 == n ? 255 : 0, true);
            else
              setAlpha(n, localInfo.layers[n].iAlpha == 0.0F ? 255 : 0, true);
            this.mi.user.wait = i1;
            this.mi.repaint();
            p();
          }
          else if ((paramMouseEvent.getClickCount() >= 2) || (bool))
          {
            config(n);
            this.mi.repaint();
          }
          else
          {
            this.isASlide = (j >= this.bW);
            this.mouse = k;
            this.m.iLayer = (this.m.iLayerSrc = n);
            this.YOFF = (i % this.bH);
            if (this.isASlide)
              setAlpha(n, (int)((j - this.bW) / 100.0F * 255.0F), false);
            else
              p();
          }
        }
        else
        {
          this.m.iLayerSrc = this.m.iLayer;
          if ((j >= this.bW) || (i <= 2))
            break;
          popup(new String[] { "AddLayer", "DelLayer", "CombineV", "PropertyLayer" }, j, i, true);
        }
        break;
      case 506:
        if (this.mouse <= 0)
          break;
        if (this.isASlide)
        {
          setAlpha(this.m.iLayer, (int)((j - this.bW) / 100.0F * 255.0F), false);
        }
        else
        {
          this.m.iLayer = (b(this.Y) - 1);
          repaint();
        }
        break;
      case 503:
        i1 = b(i) - 1;
        if ((!this.is_pre) || (i1 < 0) || (j >= this.bW))
        {
          if (this.is_DIm)
          {
            this.is_DIm = false;
            repaint();
          }
          return;
        }
        this.is_DIm = true;
        Dimension localDimension = getSize();
        int i4 = this.mi.info.W;
        int i5 = this.mi.info.H;
        int[] arrayOfInt = this.mi.info.layers[i1].offset;
        Graphics localGraphics = getG();
        int i6 = Math.min(localDimension.width - this.bW - 1, localDimension.height - 1);
        if (arrayOfInt == null)
        {
          localGraphics.setColor(Color.white);
          localGraphics.fillRect(this.bW + 1, 1, i6 - 1, i6 - 1);
        }
        else
        {
          Image localImage = getToolkit().createImage(new MemoryImageSource(i4, i5, new DirectColorModel(24, 16711680, 65280, 255), arrayOfInt, 0, i4));
          localGraphics.drawImage(localImage, this.bW + 1, 1, i6 - 1, i6 - 1, null);
          localImage.flush();
        }
        localGraphics.setColor(Color.black);
        localGraphics.drawRect(this.bW, 0, i6, i6);
        localGraphics.dispose();
        break;
      case 502:
        if (bool)
          break;
        if (this.isASlide)
        {
          setAlpha(this.m.iLayer, (int)((j - this.bW) / 100.0F * 255.0F), true);
          this.mouse = -1;
          this.isASlide = false;
        }
        else
        {
          i1 = this.mouse - 1;
          int i3 = b(this.Y) - 1;
          if ((i1 >= 0) && (i3 >= 0) && (i1 != i3))
          {
            this.m.iLayer = i3;
            this.m.iLayerSrc = i1;
            popup(new String[] { this.res.res("Shift"), this.res.res("Combine") }, j, i, false);
          }
          this.mouse = -1;
          repaint();
        }
      case 504:
      case 505:
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void popup(String[] paramArrayOfString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (!this.mi.info.isLEdit)
      return;
    if (this.popup == null)
    {
      this.popup = new PopupMenu();
      this.popup.addActionListener(this);
      add(this.popup);
    }
    else
    {
      this.popup.removeAll();
    }
    for (int i = 0; i < paramArrayOfString.length; i++)
      this.popup.add(this.res.res(paramArrayOfString[i]));
    if (paramBoolean)
    {
      this.popup.addSeparator();
      CheckboxMenuItem localCheckboxMenuItem = new CheckboxMenuItem(this.res.res("IsPreview"), this.is_pre);
      localCheckboxMenuItem.addItemListener(this);
      this.popup.add(localCheckboxMenuItem);
      this.popup.setName("m");
    }
    else
    {
      this.popup.setName("l");
    }
    this.popup.show(this, paramInt1, paramInt2);
  }

  private void setA(M paramM)
  {
    paramM.iAlpha2 = ((int)(this.mi.info.layers[paramM.iLayer].iAlpha * 255.0F) << 8 | (int)(this.mi.info.layers[paramM.iLayerSrc].iAlpha * 255.0F));
  }

  public void setAlpha(int paramInt1, int paramInt2, boolean paramBoolean)
    throws Throwable
  {
    paramInt2 = paramInt2 >= 255 ? 255 : paramInt2 <= 0 ? 0 : paramInt2;
    if (paramInt2 == this.mi.info.layers[paramInt1].iAlpha)
      return;
    if (paramBoolean)
    {
      int i = this.m.iLayer;
      this.m.iLayer = paramInt1;
      send(new int[] { 8 }, new byte[] { (byte)paramInt2 });
      this.m.iLayer = i;
    }
    else
    {
      this.mi.info.layers[paramInt1].iAlpha = (paramInt2 / 255.0F);
      this.mi.repaint();
      repaint();
    }
  }

  public void config(int paramInt)
  {
    LO localLO = this.mi.info.layers[paramInt];
    Choice localChoice = new Choice();
    localChoice.add(this.res.res("Normal"));
    localChoice.add(this.res.res("Multiply"));
    localChoice.add(this.res.res("Reverse"));
    localChoice.select(localLO.iCopy);
    TextField localTextField = new TextField(localLO.name);
    Me localMe = Me.getMe();
    Panel localPanel = new Panel(new GridLayout(0, 1));
    localPanel.add(localTextField);
    localPanel.add(localChoice);
    localTextField.addActionListener(localMe);
    localMe.add(localPanel, "Center");
    localMe.pack();
    Awt.moveCenter(localMe);
    localMe.setVisible(true);
    if (localMe.isOk)
    {
      String str = localTextField.getText();
      if (!str.equals(localLO.name))
        try
        {
          send(new int[] { 10 }, str.getBytes("UTF8"));
        }
        catch (Throwable localThrowable)
        {
        }
      int i = localChoice.getSelectedIndex();
      if (localLO.iCopy != i)
        send(new int[] { 9 }, new byte[] { (byte)i });
      repaint();
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_client.L
 * JD-Core Version:    0.6.0
 */