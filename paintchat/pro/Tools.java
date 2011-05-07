package paintchat.pro;

import java.applet.Applet;
import java.awt.CheckboxMenuItem;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuComponent;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ColorModel;
import java.lang.reflect.Field;
import java.util.EventObject;
import java.util.Hashtable;
import paintchat.M;
import paintchat.M.Info;
import paintchat.M.User;
import paintchat.Res;
import paintchat.SRaster;
import paintchat.ToolBox;
import paintchat_client.L;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.awt.LComponent;

public class Tools
  implements ToolBox, ActionListener
{
  private Applet applet;
  private Component parent;
  private Res res;
  protected Mi mi;
  M.Info info;
  M mg;
  private LComponent[] components;
  private TPic tPic;
  private TPalette tPalette;
  protected int[] iBuffer;
  private Image image = null;
  private SRaster raster;

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    try
    {
      PopupMenu localPopupMenu = (PopupMenu)paramActionEvent.getSource();
      int i = localPopupMenu.getItemCount();
      String str = paramActionEvent.getActionCommand();
      for (int j = 0; j < i; j++)
      {
        if (!localPopupMenu.getItem(j).getLabel().equals(str))
          continue;
        this.mg.set(localPopupMenu.getName() + '=' + String.valueOf(j));
        repaint();
        break;
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public String getC()
  {
    return this.tPalette.getC();
  }

  public LComponent[] getCs()
  {
    return this.components;
  }

  public Dimension getCSize()
  {
    return null;
  }

  public void init(Container paramContainer, Applet paramApplet, Res paramRes1, Res paramRes2, Mi paramMi)
  {
    this.applet = paramApplet;
    this.info = paramMi.info;
    this.mg = this.info.m;
    this.res = paramRes2;
    this.iBuffer = paramMi.user.getBuffer();
    this.mi = paramMi;
    this.parent = paramContainer;
    Dimension localDimension = paramContainer.getSize();
    LComponent[] arrayOfLComponent = new LComponent[9];
    TPen localTPen1 = new TPen(this, this.info, paramRes1, null, arrayOfLComponent);
    localTPen1.init(0);
    arrayOfLComponent[0] = localTPen1;
    TPen localTPen2 = new TPen(this, this.info, paramRes1, localTPen1, arrayOfLComponent);
    localTPen2.init(1);
    arrayOfLComponent[1] = localTPen2;
    TPalette localTPalette = new TPalette();
    localTPalette.setLocation((int)(localTPen1.getSizeW().width * Awt.q()) + 10, 0);
    localTPalette.init(this, this.info, paramRes1, paramRes2);
    arrayOfLComponent[2] = localTPalette;
    this.tPalette = localTPalette;
    TPen localTPen3 = new TPen(this, this.info, paramRes1, null, arrayOfLComponent);
    localTPen3.initTT();
    arrayOfLComponent[3] = localTPen3;
    TPic localTPic = new TPic(this);
    arrayOfLComponent[4] = localTPic;
    this.tPic = localTPic;
    TPen localTPen4 = new TPen(this, this.info, paramRes1, null, arrayOfLComponent);
    localTPen4.setLocation(localTPalette.getLocation().x + localTPalette.getSizeW().width, 0);
    localTPen4.initHint();
    arrayOfLComponent[5] = localTPen4;
    L localL = new L(paramMi, this, paramRes2, paramRes1);
    arrayOfLComponent[6] = localL;
    TBar localTBar1 = new TBar(paramRes1, paramRes2, arrayOfLComponent);
    arrayOfLComponent[7] = localTBar1;
    TBar localTBar2 = new TBar(paramRes1, paramRes2, arrayOfLComponent);
    arrayOfLComponent[8] = localTBar2;
    localTBar1.initOption(paramApplet.getCodeBase(), paramMi);
    localTBar2.init();
    localTPen3.setLocation(localTPen4.getLocation().x + localTPen4.getSizeW().width, 0);
    localTPen2.setLocation(0, localTPen1.getSizeW().height);
    localTPic.setLocation(0, localTPen2.getLocation().y + localTPen2.getSizeW().height);
    localTBar2.setLocation(localDimension.width - localTBar2.getSizeW().width, 0);
    for (int i = 0; i < arrayOfLComponent.length; i++)
    {
      arrayOfLComponent[i].setVisible(false);
      paramContainer.add(arrayOfLComponent[i], 0);
    }
    this.components = arrayOfLComponent;
    localTBar2.setVisible(true);
    localTPen1.setItem(0, null);
  }

  public void lift()
  {
    ((TPen)this.components[0]).setItem(-1, null);
  }

  protected Image mkImage(int paramInt1, int paramInt2)
  {
    if (this.raster == null)
    {
      this.raster = new SRaster(ColorModel.getRGBdefault(), this.iBuffer, paramInt1, paramInt2);
      this.image = this.applet.createImage(this.raster);
    }
    else
    {
      this.raster.newPixels(this.image, this.iBuffer, paramInt1, paramInt2);
    }
    return this.image;
  }

  public void pack()
  {
    if (this.components != null)
    {
      for (int i = 0; i < this.components.length; i++)
      {
        if (this.components[i] == null)
          continue;
        this.components[i].inParent();
      }
      this.mi.setVisible(false);
      Dimension localDimension1 = this.parent.getSize();
      this.mi.setDimension(null, new Dimension(localDimension1), new Dimension(localDimension1));
      Dimension localDimension2 = this.mi.getSize();
      this.mi.setLocation((localDimension1.width - localDimension2.width) / 2, (localDimension1.height - localDimension2.height) / 2);
      this.mi.setVisible(true);
    }
  }

  void repaint()
  {
    for (int i = 0; i < this.components.length; i++)
      this.components[i].repaint();
  }

  public void selPix(boolean paramBoolean)
  {
    ((TPen)this.components[0]).undo(paramBoolean);
  }

  public void setARGB(int paramInt)
  {
    int i = this.mg.iAlpha << 24 | this.mg.iColor;
    this.mg.iAlpha = (paramInt >>> 24);
    this.mg.iColor = (paramInt & 0xFFFFFF);
    if (i != paramInt)
    {
      this.tPic.setColor(paramInt);
      this.tPalette.setColor(paramInt);
    }
  }

  public void setC(String paramString)
  {
    this.tPalette.setC(paramString);
  }

  void setField(Component paramComponent, String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    try
    {
      PopupMenu localPopupMenu = new PopupMenu();
      localPopupMenu.setName(paramString1);
      localPopupMenu.addActionListener(this);
      int i = M.class.getField(paramString1).getInt(this.mg);
      for (int j = 0; j < 16; j++)
      {
        Object localObject = this.res.get(paramString2 + j);
        if (localObject == null)
          continue;
        if (i == j)
          localPopupMenu.add(new CheckboxMenuItem(localObject.toString(), true));
        else
          localPopupMenu.add(localObject.toString());
      }
      paramComponent.add(localPopupMenu);
      localPopupMenu.show(paramComponent, paramInt1, paramInt2);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void setLineSize(int paramInt)
  {
    this.tPalette.setLineSize(paramInt);
  }

  public void setMask(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      setField(paramComponent, "iMask", "mask_", paramInt2, paramInt3);
    }
    else
    {
      this.mg.iColorMask = (paramInt1 & 0xFFFFFF);
      this.components[4].repaint();
    }
  }

  public void setRGB(int paramInt)
  {
    setARGB(this.mg.iAlpha << 24 | paramInt & 0xFFFFFF);
  }

  public void up()
  {
    this.tPic.repaint();
    this.tPalette.repaint();
    if ((this.components != null) && (this.components[6] != null))
      this.components[6].repaint();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.pro.Tools
 * JD-Core Version:    0.6.0
 */