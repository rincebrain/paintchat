package paintchat;

import java.applet.Applet;
import java.awt.Container;
import java.awt.Dimension;
import paintchat_client.Mi;
import syi.awt.LComponent;

// Referenced classes of package paintchat:
//            Res

public interface ToolBox
{

    public abstract String getC();

    public abstract LComponent[] getCs();

    public abstract Dimension getCSize();

    public abstract void init(Container container, Applet applet, Res res, Res res1, Mi mi);

    public abstract void lift();

    public abstract void pack();

    public abstract void selPix(boolean flag);

    public abstract void setARGB(int i);

    public abstract void setC(String s);

    public abstract void setLineSize(int i);

    public abstract void up();
}
