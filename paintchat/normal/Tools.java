package paintchat.normal;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.EventObject;
import java.util.Hashtable;
import paintchat.LO;
import paintchat.M;
import paintchat.M.Info;
import paintchat.Res;
import paintchat.SW;
import paintchat.ToolBox;
import paintchat_client.L;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.awt.LComponent;

public class Tools extends LComponent
  implements ToolBox, ActionListener
{
  Applet applet;
  Container parent;
  Mi mi;
  L L;
  M.Info info;
  M mg;
  Res res;
  Res config;
  private Graphics primary = null;
  private Font fDef;
  private Font fIg;
  ToolList[] list;
  private Rectangle rPaint = new Rectangle();
  private Rectangle[] rects = null;
  private int fit_w = -1;
  private int fit_h = -1;
  private int nowButton = -1;
  private int nowColor = -1;
  private int oldPen;
  Color clFrame;
  Color clB;
  Color clBD;
  Color clBL;
  Color clText;
  Color clBar;
  Color clB2;
  Color clSel;
  private boolean isWest = false;
  private boolean isLarge;
  private PopupMenu popup;
  private LComponent[] cs;
  private Window[] ws;
  private static int[] DEFC = { 0, 16777215, 11826549, 8947848, 16422550, 12621504, 16758527, 8421631, 2475977, 15197581, 15177261, 10079099, 16575714, 16375247 };
  private static int[] COLORS = new int[14];
  private static Color[][] clRGB;
  private static Color[][] clERGB;
  private char[][] clValue = { { 'H', 'S', 'B', 'A' }, { 'R', 'G', 'B', 'A' } };
  private boolean isRGB = true;
  private float[] fhsb = new float[3];
  private int iColor;
  protected Image imBack = null;
  private Graphics back;
  protected int W;
  protected int H;
  protected int IMW;
  protected int IMH;

  static
  {
    System.arraycopy(DEFC, 0, COLORS, 0, 14);
    clRGB = new Color[][] { { Color.magenta, Color.cyan, Color.white, Color.lightGray }, { new Color(16422550), new Color(8581688), new Color(8421631), Color.lightGray } };
    clERGB = new Color[2][4];
    for (int i = 0; i < 2; i++)
      for (int j = 0; j < 4; j++)
        clERGB[i][j] = clRGB[i][j].darker();
  }

  public Tools()
  {
    setTitle("tools");
    this.isHide = false;
    this.iGap = 2;
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    String str = paramActionEvent.getActionCommand();
    Menu localMenu = (Menu)paramActionEvent.getSource();
    int i = 0;
    int j = localMenu.getItemCount();
    for (int k = 0; k < j; k++)
    {
      if (!localMenu.getItem(k).getLabel().equals(str))
        continue;
      i = k;
      break;
    }
    switch (Integer.parseInt(localMenu.getLabel()))
    {
    case 2:
      this.mg.iPenM = i;
      setLineSize(0, this.mg.iSize);
      repaint();
    }
  }

  public void addC(Object paramObject)
  {
    int i;
    Object localObject;
    if ((paramObject instanceof LComponent))
    {
      if (this.cs != null)
      {
        for (i = 0; i < this.cs.length; i++)
          if (this.cs[i] == paramObject)
            return;
      }
      else
      {
        this.cs = new LComponent[] { (LComponent)paramObject };
        return;
      }
      i = this.cs.length;
      localObject = new LComponent[i + 1];
      System.arraycopy(this.cs, 0, localObject, 0, i);
      localObject[i] = ((LComponent)paramObject);
      this.cs = ((LComponent)localObject);
    }
    else
    {
      if (this.ws != null)
      {
        for (i = 0; i < this.ws.length; i++)
          if (this.ws[i] == paramObject)
            return;
      }
      else
      {
        this.ws = new Window[] { (Window)paramObject };
        return;
      }
      i = this.ws.length;
      localObject = new Window[i + 1];
      System.arraycopy(this.cs, 0, localObject, 0, i);
      localObject[i] = ((Window)paramObject);
      this.ws = ((Window)localObject);
    }
  }

  public Graphics getBack()
  {
    if (this.imBack == null)
      synchronized (this)
      {
        if (this.imBack == null)
          try
          {
            int i = 0;
            for (int j = 0; j < this.list.length; j++)
              i = Math.max(i, this.list[j].r.height);
            i = Math.max(i, 32) * 2;
            this.imBack = createImage(this.W + 1, i + 1);
            this.back = this.imBack.getGraphics();
          }
          catch (RuntimeException localRuntimeException)
          {
            this.imBack = null;
            this.back = null;
          }
      }
    if (this.back != null)
      this.back.setFont(this.fDef);
    return this.back;
  }

  public String getC()
  {
    try
    {
      int[] arrayOfInt = COLORS == null ? DEFC : COLORS;
      StringBuffer localStringBuffer = new StringBuffer();
      for (int i = 0; i < arrayOfInt.length; i++)
      {
        if (i != 0)
          localStringBuffer.append('\n');
        localStringBuffer.append("#" + Integer.toHexString(0xFF000000 | arrayOfInt[i] & 0xFFFFFF).substring(2).toUpperCase());
      }
      return localStringBuffer.toString();
    }
    catch (Throwable localThrowable)
    {
    }
    return null;
  }

  public LComponent[] getCs()
  {
    return this.cs;
  }

  public Dimension getCSize()
  {
    Dimension localDimension = this.parent.getSize();
    return new Dimension(localDimension.width - getSizeW().width - this.mi.getGapW(), localDimension.height);
  }

  private int getRGB()
  {
    if (!this.isRGB)
      return Color.HSBtoRGB((this.iColor >>> 16 & 0xFF) / 255.0F, (this.iColor >>> 8 & 0xFF) / 255.0F, (this.iColor & 0xFF) / 255.0F) & 0xFFFFFF;
    return this.iColor & 0xFFFFFF;
  }

  private int i(String paramString, int paramInt)
  {
    return this.config.getP(paramString, paramInt);
  }

  public boolean i(String paramString, boolean paramBoolean)
  {
    return this.config.getP(paramString, paramBoolean);
  }

  public void init(Container paramContainer, Applet paramApplet, Res paramRes1, Res paramRes2, Mi paramMi)
  {
    this.applet = paramApplet;
    this.parent = paramContainer;
    this.res = paramRes2;
    this.config = paramRes1;
    this.info = paramMi.info;
    this.mi = paramMi;
    this.mg = this.info.m;
    this.W = (i("tool_width", 48) + 4);
    this.H = i("tool_height", 470);
    for (int i = 0; i < DEFC.length; i += 2)
    {
      DEFC[i] = paramRes1.getP("color_" + (i + 2), DEFC[i]);
      DEFC[(i + 1)] = paramRes1.getP("color_" + (i + 1), DEFC[(i + 1)]);
    }
    System.arraycopy(DEFC, 0, COLORS, 0, 14);
    sCMode(false);
    String str = "tool_color_";
    setBackground(new Color(i(str + "bk", i(str + "back", 10066363))));
    this.clB = new Color(i(str + "button", -1515602));
    this.clB2 = new Color(i(str + "button" + '2', 16308906));
    this.clFrame = new Color(i(str + "frame", 0));
    this.clText = new Color(i(str + "text", 7811891));
    this.clBar = new Color(i(str + "bar", 14540287));
    this.clSel = new Color(i(str + "iconselect", i("color_iconselect", 15610675)));
    this.clBL = new Color(i(str + "button" + "_hl", this.clB.brighter().getRGB()));
    this.clBD = new Color(i(str + "button" + "_dk", this.clB.darker().getRGB()));
    this.isWest = "left".equals(paramRes1.getP("tool_align"));
    this.isLarge = paramRes1.getP("icon_enlarge", true);
    paramContainer.getSize();
    setDimension(new Dimension(this.W, 42), new Dimension(this.W, this.H), new Dimension(this.W, (int)(this.H * 1.25F)));
    this.list[0].select();
    paramContainer.add(this, 0);
    addC(this);
    if (paramRes1.getP("tool_layer", true))
    {
      this.L = new L(paramMi, this, this.res, paramRes1);
      this.L.setVisible(false);
      addC(this.L);
    }
  }

  private int isClick(int paramInt1, int paramInt2)
  {
    if (this.rects == null)
      return -1;
    int i = this.rects.length;
    int j = this.list.length;
    for (int k = 0; k < j; k++)
      if (this.list[k].r.contains(paramInt1, paramInt2))
        return k;
    for (k = 0; k < i; k++)
    {
      Rectangle localRectangle = this.rects[k];
      if ((localRectangle != null) && (localRectangle.contains(paramInt1, paramInt2)))
        return k + j;
    }
    return -1;
  }

  public void lift()
  {
    unSelect();
    this.nowButton = -1;
    repaint();
  }

  private void makeList()
  {
    Object localObject = null;
    int i = 0;
    int j = 0;
    this.clB.brighter();
    this.clB.darker();
    try
    {
      String str = "res/s.gif";
      Image localImage = getToolkit().createImage((byte[])this.config.getRes(str));
      Awt.wait(localImage);
      localObject = localImage;
      this.config.remove(str);
      int m = i("tool_icon_count", 7);
      this.list = new ToolList[m];
      i = localImage.getWidth(null) / m;
      j = i("tool_icon_height", localImage.getHeight(null) / 9);
      this.IMW = i;
      this.IMH = j;
    }
    catch (RuntimeException localRuntimeException)
    {
    }
    for (int k = 0; k < this.list.length; k++)
    {
       tmp149_146 = new ToolList();
      ToolList localToolList = tmp149_146;
      this.list[k] = tmp149_146;
      localToolList.init(this, this.res, this.config, this.mg, this.list, k);
      localToolList.setImage(localObject, i, j, k);
    }
  }

  private void menu(int paramInt1, int paramInt2, int paramInt3)
  {
    if (this.popup == null)
    {
      this.popup = new PopupMenu(String.valueOf(paramInt3));
      this.popup.addActionListener(this);
    }
    else
    {
      remove(this.popup);
      this.popup.removeAll();
      this.popup.setLabel(String.valueOf(paramInt3));
    }
    int i;
    String str;
    int j;
    switch (paramInt3)
    {
    case 0:
      for (i = 0; i < 11; i++)
      {
        str = String.valueOf(i == 0 ? 5 : i * 10) + '%';
        if (this.mg.iTT - 1 == i)
          this.popup.add(new CheckboxMenuItem(str, true));
        else
          this.popup.add(str);
      }
      break;
    case 1:
      i = this.config.getInt("tt_size");
      for (j = 0; j < i; j++)
      {
        str = this.res.res("t042" + j);
        if (this.mg.iTT - 12 == j)
          this.popup.add(new CheckboxMenuItem(str, true));
        else
          this.popup.add(str);
      }
      break;
    case 2:
      for (j = 0; j < 16; j++)
      {
        str = (String)this.res.get("penm_" + j);
        if (str == null)
          break;
        if (this.mg.iPenM == j)
          this.popup.add(new CheckboxMenuItem(str, true));
        else
          this.popup.add(str);
      }
    }
    add(this.popup);
    this.popup.show(this, paramInt1, paramInt2);
  }

  public void mPaint(int paramInt)
  {
    Rectangle localRectangle;
    if (paramInt == -1)
    {
      localRectangle = this.rPaint;
      localRectangle.setSize(getSize());
      localRectangle.setLocation(0, 0);
    }
    else if (paramInt < this.list.length)
    {
      localRectangle = this.rPaint;
      localRectangle.setBounds(this.list[paramInt].r);
    }
    else
    {
      localRectangle = this.rects[(paramInt - this.list.length)];
    }
    mPaint(primary(), localRectangle);
  }

  public void mPaint(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Rectangle localRectangle = this.rPaint;
    localRectangle.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    mPaint(primary(), localRectangle);
  }

  private void mPaint(Graphics paramGraphics, Rectangle paramRectangle)
  {
    if ((this.rects == null) || (paramGraphics == null) || (this.list == null))
      return;
    Graphics localGraphics = getBack();
    if (paramRectangle == null)
    {
      paramRectangle = paramGraphics.getClipBounds();
      if ((paramRectangle == null) || (paramRectangle.isEmpty()))
        paramRectangle = new Rectangle(getSize());
    }
    if (paramRectangle.isEmpty())
      return;
    int k = this.list.length;
    Dimension localDimension = getSize();
    localGraphics.setFont(this.fDef);
    for (int i = 0; i < k; i++)
    {
      if (!this.list[i].r.intersects(paramRectangle))
        continue;
      this.list[i].paint(paramGraphics, localGraphics);
    }
    localGraphics.setFont(this.fIg);
    int m = this.isRGB ? 1 : 0;
    for (i = 0; i < this.rects.length; i++)
    {
      Rectangle localRectangle = this.rects[i];
      int j = i + k;
      if (!localRectangle.intersects(paramRectangle))
        continue;
      if (i < 14)
      {
        Color localColor1 = new Color(COLORS[i]);
        localGraphics.setColor(i == this.nowColor ? localColor1.darker() : localColor1.brighter());
        localGraphics.drawRect(1, 1, localRectangle.width - 2, localRectangle.height - 2);
        localGraphics.setColor(localColor1);
        localGraphics.fillRect(2, 2, localRectangle.width - 3, localRectangle.height - 3);
        localGraphics.setColor(this.nowColor == i ? this.clSel : this.clFrame);
      }
      else
      {
        int i2;
        int i3;
        int i4;
        switch (i)
        {
        case 14:
        case 15:
        case 16:
        case 17:
          int n = i - 14;
          int i1 = localRectangle.height;
          i2 = i == 17 ? this.mg.iAlpha : this.iColor >>> (2 - n) * 8 & 0xFF;
          i3 = (int)((localDimension.width - 10 - 2) / 255.0F * i2);
          localGraphics.setColor(this.clB2);
          localGraphics.fillRect(0, 0, 5, i1 - 1);
          localGraphics.fillRect(localRectangle.width - 5, 1, 5, i1 - 1);
          localGraphics.setColor(this.clFrame);
          localGraphics.fillRect(5, 1, 1, i1 - 1);
          localGraphics.fillRect(localRectangle.width - 5 - 1, 1, 1, i1 - 1);
          if (i3 > 0)
          {
            localGraphics.setColor(clRGB[m][n]);
            localGraphics.fillRect(6, 1, i3, localRectangle.height - 2);
          }
          i4 = localRectangle.width - 10 - i3 - 2;
          if (i4 > 0)
          {
            localGraphics.setColor(this.clBar);
            localGraphics.fillRect(i3 + 5 + 1, 1, i4, localRectangle.height - 2);
            localGraphics.setColor(clERGB[m][n]);
            localGraphics.fillRect(i3 + 5 + 1, 1, 1, localRectangle.height - 2);
          }
          localGraphics.setColor(this.clText);
          localGraphics.drawString(String.valueOf(this.clValue[m][n]) + i2, 8, localRectangle.height - 2);
          break;
        case 18:
          boolean bool = this.mg.isText();
          Color localColor2 = new Color(getRGB());
          i2 = bool ? 255 : this.info.getPMMax();
          i3 = localRectangle.width - 10;
          i4 = localRectangle.height - 2;
          localGraphics.setColor(this.clB2);
          localGraphics.fillRect(1, 1, localRectangle.width - 2, i4);
          if (this.mg.iSize >= i2)
            this.mg.iSize = (i2 - 1);
          localGraphics.setColor(localColor2);
          localGraphics.fillRect(1, 1, i3, (int)((this.mg.iSize + 1) / i2 * i4));
          if (this.info.getPenMask() == null)
            return;
          localGraphics.setColor(this.clText);
          localGraphics.drawString(String.valueOf(this.mg.iSize), 6, i4 - 1);
          localGraphics.setColor(this.clFrame);
          localGraphics.fillRect(i3, 1, 1, i4);
          localGraphics.fillRect(i3 + 1, i4 / 2, 8, 1);
          localGraphics.setColor(localColor2);
          for (int i5 = 3; i5 >= 1; i5--)
          {
            localGraphics.fillRect(localRectangle.width - 5 - i5, i5 + 2, i5 << 1, 1);
            localGraphics.fillRect(localRectangle.width - 5 - i5, i4 - 2 - i5, i5 << 1, 1);
          }
          localGraphics.fillRect(localRectangle.width - 6, 5, 2, 8);
          localGraphics.fillRect(localRectangle.width - 6, i4 - 11, 2, 8);
          break;
        case 19:
          localGraphics.setColor(this.clBar);
          localGraphics.fillRect(1, 1, localRectangle.width - 1, localRectangle.height - 2);
          if ((this.info.layers == null) || (this.info.layers.length <= this.mg.iLayer))
            break;
          LO localLO = this.info.layers[this.mg.iLayer];
          localGraphics.setColor(this.clText);
          if (localLO.name != null)
            localGraphics.drawString(localLO.name, 2, localRectangle.height - localGraphics.getFontMetrics().getMaxDescent() - 1);
          if (localLO.iAlpha != 0.0F)
            break;
          localGraphics.setColor(Color.red);
          localGraphics.drawLine(1, 1, localRectangle.width - 3, localRectangle.height - 3);
        }
        localGraphics.setColor(this.nowButton == j ? this.clSel : this.clFrame);
      }
      localGraphics.drawRect(0, 0, localRectangle.width - 1, localRectangle.height - 1);
      paramGraphics.drawImage(this.imBack, localRectangle.x, localRectangle.y, localRectangle.x + localRectangle.width, localRectangle.y + localRectangle.height, 0, 0, localRectangle.width, localRectangle.height, Color.white, null);
    }
  }

  private void mPress(MouseEvent paramMouseEvent)
  {
    int i = paramMouseEvent.getX();
    int j = paramMouseEvent.getY();
    int k = isClick(i, j);
    int m = k;
    boolean bool = Awt.isR(paramMouseEvent);
    this.nowButton = k;
    if (k < 0)
      return;
    if (k - this.list.length < 0)
    {
      if (bool)
      {
        if ((this.list[k].isField) && (this.list[k].isMask))
        {
          this.mg.iColorMask = this.mg.iColor;
          mPaint(k);
        }
        this.nowButton = -1;
      }
      return;
    }
    k -= this.list.length;
    Rectangle localRectangle = this.rects[k];
    if (k - 14 < 0)
    {
      if (bool)
      {
        COLORS[k] = this.mg.iColor;
      }
      else if (paramMouseEvent.isShiftDown())
      {
        COLORS[k] = DEFC[k];
      }
      else
      {
        this.nowColor = k;
        this.mg.iColor = COLORS[this.nowColor];
        selPix(false);
        toColor(this.mg.iColor);
      }
      up();
      return;
    }
    k -= 14;
    int n;
    if (k - 4 < 0)
    {
      n = i >= localRectangle.width - 5 ? 1 : i <= 5 ? -1 : 0;
      if (n != 0)
      {
        this.nowButton = -1;
        if (bool)
          n *= 5;
      }
      else if (bool)
      {
        sCMode(this.isRGB);
        this.nowButton = -1;
        mPaint(-1);
        return;
      }
      setRGB(k, n, i);
      return;
    }
    k -= 4;
    if (k - 1 < 0)
    {
      if (bool)
      {
        this.nowButton = -1;
        menu(i, j, 2);
        return;
      }
      if (i >= localRectangle.x + localRectangle.width - 10)
      {
        setLineSize(0, Math.max(this.mg.iSize + ((localRectangle.y + localRectangle.height - j) / 2 >= 10 ? -1 : 1), 0));
        this.nowButton = -1;
      }
      else
      {
        setLineSize(j, -1);
      }
      mPaint(m);
      return;
    }
    k--;
    if (k - 1 < 0)
    {
      n = this.mg.iLayer;
      if (bool)
      {
        LO localLO = this.info.layers[n];
        localLO.iAlpha = (localLO.iAlpha == 0.0F ? 1 : 0);
        this.mi.repaint();
      }
      else if ((this.L != null) && (!this.L.isVisible()))
      {
        if (this.L.getParent() == null)
          this.parent.add(this.L, 0);
        this.L.setVisible(true);
      }
      else
      {
        if (++this.mg.iLayer >= this.info.L)
          this.mg.iLayer = 0;
        this.mg.iLayerSrc = this.mg.iLayer;
      }
      if (this.L != null)
        this.L.repaint();
      mPaint(m);
      return;
    }
  }

  public void pack()
  {
    try
    {
      Container localContainer = this.parent;
      Dimension localDimension1 = localContainer.getSize();
      setSize(Math.min(this.W, localDimension1.width), Math.min(this.H, localDimension1.height));
      if (this.L != null)
        this.L.inParent();
      Dimension localDimension2 = getCSize();
      Dimension localDimension3 = getSizeW();
      if (!this.mi.isGUI)
      {
        this.mi.setDimension(null, localDimension2, localDimension2);
        localDimension2 = this.mi.getSizeW();
        this.mi.setLocation((localDimension1.width - localDimension3.width - localDimension2.width) / 2 + (this.isWest ? localDimension3.width : 0), (localDimension1.height - localDimension2.height) / 2);
      }
      localDimension2 = this.mi.getSizeW();
      Point localPoint = this.mi.getLocation();
      setLocation(this.isWest ? Math.max(0, localPoint.x - localDimension3.width - 10) : Math.min(localPoint.x + localDimension2.width + 10, localDimension1.width - localDimension3.width), (localDimension1.height - localDimension3.height) / 2);
      if (this.cs != null)
        for (int i = 2; i < this.cs.length; i++)
          ((SW)this.cs[i]).mPack();
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void paint2(Graphics paramGraphics)
  {
    try
    {
      paramGraphics.setFont(this.fDef);
      mPaint(paramGraphics, paramGraphics.getClipBounds());
      if (this.primary != null)
      {
        this.primary.dispose();
        this.primary = null;
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void pMouse(MouseEvent paramMouseEvent)
  {
    try
    {
      if (this.rects == null)
        return;
      int i = paramMouseEvent.getX();
      int j = paramMouseEvent.getY();
      boolean bool = Awt.isR(paramMouseEvent);
      getSize();
      if (this.list != null)
        for (k = 0; k < this.list.length; k++)
        {
          if ((this.list[k].isMask) && (bool))
            continue;
          this.list[k].pMouse(paramMouseEvent);
        }
      int k = paramMouseEvent.getID();
      switch (k)
      {
      case 501:
        mPress(paramMouseEvent);
        break;
      case 506:
        int m = this.nowButton;
        if (m < this.list.length)
          return;
        m -= this.list.length;
        if (m - 14 < 0)
          return;
        m -= 14;
        if (m - 4 < 0)
        {
          setRGB(m, 0, i);
          mPaint(-1);
          upCS();
          return;
        }
        m -= 4;
        if (m - 1 >= 0)
          break;
        setLineSize(j, -1);
        mPaint(this.nowButton);
        return;
        break;
      case 502:
        this.nowButton = -1;
      case 503:
      case 504:
      case 505:
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public Graphics primary()
  {
    if (this.primary == null)
      try
      {
        this.primary = getGraphics();
        if (this.primary != null)
        {
          this.primary.setFont(this.fDef);
          this.primary.translate(getGapX(), getGapY());
          Dimension localDimension = getSize();
          this.primary.setClip(0, 0, localDimension.width, localDimension.height);
        }
      }
      catch (RuntimeException localRuntimeException)
      {
        this.primary = null;
      }
    return this.primary;
  }

  protected void processEvent(AWTEvent paramAWTEvent)
  {
    paramAWTEvent.getID();
    switch (paramAWTEvent.getID())
    {
    case 100:
      int i = getLocation().x;
      int j = getParent().getSize().width / 2 - getSize().width / 2;
      if ((i < j) && (!this.isWest))
      {
        this.isWest = true;
        pack();
      }
      else
      {
        if ((i < j) || (!this.isWest))
          break;
        this.isWest = false;
        pack();
      }
      break;
    case 101:
    case 102:
      if (this.primary == null)
        break;
      this.primary.dispose();
      this.primary = null;
    }
    super.processEvent(paramAWTEvent);
  }

  private void sCMode(boolean paramBoolean)
  {
    this.isRGB = (!paramBoolean);
    toColor(this.mg.iColor);
  }

  public void selPix(boolean paramBoolean)
  {
    if (this.list == null)
      return;
    int i = this.list.length;
    Object localObject1 = null;
    Object localObject2 = null;
    for (int j = 0; j < i; j++)
    {
      ToolList localToolList = this.list[j];
      if (localToolList.isEraser)
        localObject2 = localToolList;
      if (!localToolList.isSelect)
        continue;
      localObject1 = localToolList;
    }
    if (paramBoolean)
    {
      if (localObject1 != localObject2)
      {
        unSelect();
        localObject2.select();
        mPaint(-1);
      }
    }
    else if (localObject1 == localObject2)
    {
      unSelect();
      this.list[this.oldPen].select();
      mPaint(-1);
    }
  }

  public void setARGB(int paramInt)
  {
    this.mg.iAlpha = (paramInt >>> 24);
    paramInt &= 16777215;
    this.mg.iColor = paramInt;
    toColor(paramInt);
    mPaint(-1);
    upCS();
  }

  public void setC(String paramString)
  {
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new StringReader(paramString));
      int i = 0;
      while (((paramString = localBufferedReader.readLine()) != null) && (paramString.length() > 0))
        DEFC[(i++)] = Integer.decode(paramString).intValue();
      System.arraycopy(DEFC, 0, COLORS, 0, COLORS.length);
      repaint();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void setLineSize(int paramInt)
  {
    setLineSize(0, Math.max(paramInt, 0));
    mPaint(this.list.length + 5);
  }

  public void setLineSize(int paramInt1, int paramInt2)
  {
    int i = this.info.getPMMax();
    Rectangle localRectangle = this.rects[18];
    if (paramInt2 == -1)
      paramInt2 = (int)((paramInt1 - localRectangle.y) / localRectangle.height * i);
    paramInt2 = paramInt2 >= i ? i - 1 : paramInt2 <= 0 ? 0 : paramInt2;
    this.mg.iSize = paramInt2;
  }

  private void setRGB(int paramInt1, int paramInt2, int paramInt3)
  {
    int j = paramInt1 == 3 ? 24 : (2 - paramInt1) * 8;
    int k = this.mg.iAlpha << 24 | this.iColor;
    if (paramInt2 != 0)
    {
      i = k >>> j & 0xFF;
      i += paramInt2;
    }
    else
    {
      Rectangle localRectangle = this.rects[(14 + paramInt1)];
      paramInt3 = paramInt3 - localRectangle.x - 4;
      i = paramInt3 > 0 ? (int)(paramInt3 / (this.W - 8) * 255.0F) : 0;
    }
    int i = i >= 255 ? 255 : i <= 0 ? 0 : i;
    int m = 255 << j;
    m ^= -1;
    k = k & m | i << j;
    this.iColor = (k & 0xFFFFFF);
    this.mg.iColor = getRGB();
    this.mg.iAlpha = Math.max(k >>> 24, 1);
    mPaint(paramInt1);
    if (this.nowColor >= 0)
      COLORS[this.nowColor] = this.mg.iColor;
    mPaint(-1);
    upCS();
  }

  public void setSize(int paramInt1, int paramInt2)
  {
    if (this.applet == null)
    {
      super.setSize(paramInt1, paramInt2);
      return;
    }
    if ((paramInt1 == this.fit_w) && (paramInt2 == this.fit_h))
      return;
    synchronized (this)
    {
      this.fit_w = paramInt1;
      this.fit_h = paramInt2;
      if (this.list == null)
        makeList();
      if (this.rects == null)
      {
        this.rects = new Rectangle[20];
        for (i = 0; i < this.rects.length; i++)
          this.rects[i] = new Rectangle();
      }
      Rectangle[] arrayOfRectangle = this.rects;
      getSize();
      (paramInt1 / this.W);
      float f = paramInt2 / this.H;
      int j = (int)((this.IMH + 4) * f);
      if (!this.isLarge)
        j = Math.min(this.IMH + 4, j);
      int k = Math.min((paramInt2 - (j + 1) * this.list.length - (int)(16.0F * f * 4.0F) - (int)(33.0F * f) - 3) / 8, (paramInt1 - 1) / 2);
      this.fIg = new Font("sansserif", 0, (int)(k * 0.475F));
      this.fDef = new Font("sansserif", 0, (int)(j * 0.43F));
      FontMetrics localFontMetrics = getFontMetrics(this.fDef);
      int m = j - localFontMetrics.getMaxDescent() - 2;
      int n = 0;
      int i1 = 0;
      for (int i = 0; i < this.list.length; i++)
      {
        this.list[i].r.setLocation(0, i1);
        this.list[i].setSize(this.W, j, m);
        i1 += j + 1;
      }
      n = (paramInt1 - 1) / 2;
      for (i = 0; i < 14; i++)
      {
        localRectangle = arrayOfRectangle[i];
        localRectangle.setBounds(i % 2 == 1 ? n + 1 : 0, i1, i % 2 == 1 ? paramInt1 - n - 1 : n, k);
        if (i % 2 != 1)
          continue;
        i1 += k + 1;
      }
      n = (int)(15.0F * f);
      while (i < 18)
      {
        localRectangle = arrayOfRectangle[i];
        localRectangle.setBounds(0, i1, paramInt1, n);
        i1 += n + 1;
        i++;
      }
      n = (int)(32.0F * f);
      Rectangle localRectangle = arrayOfRectangle[(i++)];
      localRectangle.setBounds(0, i1, paramInt1, n);
      i1 += n + 1;
      n = Math.min(paramInt2 - i1, k);
      localRectangle = arrayOfRectangle[(i++)];
      localRectangle.setBounds(0, paramInt2 - n, paramInt1, n);
      i1 += n + 1;
      super.setSize(paramInt1, paramInt2);
    }
  }

  public void showW(String paramString)
  {
    int i;
    if (this.cs != null)
      for (i = 0; i < this.cs.length; i++)
      {
        if (!this.cs[i].getClass().getName().equals(paramString))
          continue;
        if (!this.cs[i].isVisible())
          this.cs[i].setVisible(true);
        return;
      }
    if (this.ws != null)
      for (i = 0; i < this.ws.length; i++)
      {
        if (!this.ws[i].getClass().getName().equals(paramString))
          continue;
        if (!this.ws[i].isVisible())
          this.ws[i].setVisible(true);
        return;
      }
    try
    {
      SW localSW = (SW)Class.forName(paramString).newInstance();
      if ((localSW instanceof Window))
      {
        addC(localSW);
        localSW.mSetup(this, this.info, this.mi.user, this.mg, this.res, this.config);
      }
      else
      {
        LComponent localLComponent = (LComponent)localSW;
        addC(localLComponent);
        localLComponent.setVisible(false);
        localSW.mSetup(this, this.info, this.mi.user, this.mg, this.res, this.config);
        this.parent.add(localLComponent, 0);
        localSW.mPack();
        localLComponent.setVisible(true);
      }
    }
    catch (Throwable localThrowable)
    {
      this.mi.alert(localThrowable.getLocalizedMessage(), false);
    }
  }

  private void toColor(int paramInt)
  {
    if (!this.isRGB)
    {
      Color.RGBtoHSB(paramInt >>> 16 & 0xFF, paramInt >>> 8 & 0xFF, paramInt & 0xFF, this.fhsb);
      this.iColor = (this.mg.iAlpha << 24 | (int)(this.fhsb[0] * 255.0F) << 16 | (int)(this.fhsb[1] * 255.0F) << 8 | (int)(this.fhsb[2] * 255.0F));
    }
    else
    {
      this.iColor = (this.mg.iAlpha << 24 | paramInt);
    }
  }

  public void unSelect()
  {
    for (int i = 0; i < this.list.length; i++)
    {
      ToolList localToolList = this.list[i];
      if (!localToolList.isSelect)
        continue;
      if (!localToolList.isEraser)
        this.oldPen = i;
      localToolList.unSelect();
    }
  }

  public void up()
  {
    try
    {
      mPaint(-1);
      this.mi.up();
      upCS();
      if (this.L != null)
        this.L.repaint();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void upCS()
  {
    for (int i = 2; i < this.cs.length; i++)
      ((SW)this.cs[i]).up();
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.normal.Tools
 * JD-Core Version:    0.6.0
 */