package paintchat;

import java.applet.Applet;
import java.awt.Container;
import java.awt.Dimension;
import paintchat_client.Mi;
import syi.awt.LComponent;

public abstract interface ToolBox
{
  public abstract String getC();

  public abstract LComponent[] getCs();

  public abstract Dimension getCSize();

  public abstract void init(Container paramContainer, Applet paramApplet, Res paramRes1, Res paramRes2, Mi paramMi);

  public abstract void lift();

  public abstract void pack();

  public abstract void selPix(boolean paramBoolean);

  public abstract void setARGB(int paramInt);

  public abstract void setC(String paramString);

  public abstract void setLineSize(int paramInt);

  public abstract void up();
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.ToolBox
 * JD-Core Version:    0.6.0
 */