package paintchat_client;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Container;
import syi.util.ThreadPool;

// Referenced classes of package paintchat_client:
//            Pl

public class Client extends Applet
{

    private Pl pl;

    public Client()
    {
    }

    public void destroy()
    {
        if(pl != null)
        {
            pl.destroy();
        }
    }

    public void init()
    {
        try
        {
            setLayout(new BorderLayout());
            pl = new Pl(this);
            add(pl, "Center");
            validate();
            ThreadPool.poolStartThread(pl, 'i');
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }
}
