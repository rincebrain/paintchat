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

    private Debug debug;
    private CharArrayWriter cOut;
    private Thread thread;
    private boolean isRun;

    public Console()
    {
        this(null, 400, Color.white, Color.black, null);
    }

    public Console(Applet applet, int i, Color color, Color color1, TextField textfield)
    {
        super(applet, i, color, color1, textfield);
        debug = null;
        cOut = null;
        thread = null;
    }

    public void run()
    {
        try
        {
            Thread thread1 = Thread.currentThread();
            while(isRun) 
            {
                if(cOut.size() > 0)
                {
                    synchronized(cOut)
                    {
                        setText(cOut.toString());
                        cOut.reset();
                    }
                }
                Thread.sleep(2500L);
            }
        }
        catch(Throwable _ex) { }
        stop();
    }

    public synchronized void start(Debug debug1)
    {
        debug = debug1;
        if(cOut == null)
        {
            cOut = new CharArrayWriter();
        }
        debug.setListener(this);
        isRun = true;
        thread = ThreadPool.poolStartThread(this, "c");
    }

    public synchronized void stop()
    {
        if(!isRun)
        {
            return;
        }
        isRun = false;
        debug.setListener(null);
        if(thread != null && Thread.currentThread() != thread)
        {
            thread.interrupt();
            thread = null;
        }
    }

    public void log(Object obj)
    {
        addText(obj.toString());
    }

    public void logDebug(Object obj)
    {
        addText(obj.toString());
    }
}
