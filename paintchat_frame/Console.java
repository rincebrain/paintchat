package paintchat_frame;

import java.applet.Applet;
import java.awt.Color;
import java.awt.TextField;
import java.io.CharArrayWriter;
import paintchat.debug.Debug;
import paintchat.debug.DebugListener;
import syi.awt.TextPanel;
import syi.util.ThreadPool;

public class Console extends TextPanel
  implements Runnable, DebugListener
{
  private Debug debug = null;
  private CharArrayWriter cOut = null;
  private Thread thread = null;
  private boolean isRun;

  public Console()
  {
    this(null, 400, Color.white, Color.black, null);
  }

  public Console(Applet paramApplet, int paramInt, Color paramColor1, Color paramColor2, TextField paramTextField)
  {
    super(paramApplet, paramInt, paramColor1, paramColor2, paramTextField);
  }

  public void run()
  {
    try
    {
      Thread localThread = Thread.currentThread();
      while (this.isRun)
      {
        if (this.cOut.size() > 0)
          synchronized (this.cOut)
          {
            setText(this.cOut.toString());
            this.cOut.reset();
          }
        Thread.sleep(2500L);
      }
    }
    catch (Throwable localThrowable)
    {
    }
    stop();
  }

  public synchronized void start(Debug paramDebug)
  {
    this.debug = paramDebug;
    if (this.cOut == null)
      this.cOut = new CharArrayWriter();
    this.debug.setListener(this);
    this.isRun = true;
    this.thread = ThreadPool.poolStartThread(this, "c");
  }

  public synchronized void stop()
  {
    if (!this.isRun)
      return;
    this.isRun = false;
    this.debug.setListener(null);
    if ((this.thread != null) && (Thread.currentThread() != this.thread))
    {
      this.thread.interrupt();
      this.thread = null;
    }
  }

  public void log(Object paramObject)
  {
    addText(paramObject.toString());
  }

  public void logDebug(Object paramObject)
  {
    addText(paramObject.toString());
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_frame.Console
 * JD-Core Version:    0.6.0
 */