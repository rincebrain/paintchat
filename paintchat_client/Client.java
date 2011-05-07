package paintchat_client;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Container;
import syi.util.ThreadPool;

public class Client extends Applet
{
  private Pl pl;

  public void destroy()
  {
    if (this.pl != null)
      this.pl.destroy();
  }

  public void init()
  {
    try
    {
      setLayout(new BorderLayout());
      this.pl = new Pl(this);
      add(this.pl, "Center");
      validate();
      ThreadPool.poolStartThread(this.pl, 'i');
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_client.Client
 * JD-Core Version:    0.6.0
 */