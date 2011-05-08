package paintchat.debug;

import java.io.*;
import paintchat.Res;

// Referenced classes of package paintchat.debug:
//            DebugListener

public class Debug
    implements DebugListener
{

    private DebugListener listener;
    private Res res;
    public BufferedWriter wFile;
    public boolean bool_debug;

    public Debug()
    {
        listener = null;
        res = null;
        wFile = null;
        bool_debug = false;
    }

    public Debug(DebugListener debuglistener, Res res1)
    {
        listener = null;
        res = null;
        wFile = null;
        bool_debug = false;
        listener = debuglistener;
        res = res1;
    }

    public Debug(Res res1)
    {
        listener = null;
        res = null;
        wFile = null;
        bool_debug = false;
        res = res1;
    }

    protected void finalize()
        throws Throwable
    {
        mDestroy();
    }

    public void log(Object obj)
    {
        try
        {
            if(listener != null)
            {
                listener.log(obj);
            } else
            {
                logSys(obj);
            }
            writeLogFile(obj);
        }
        catch(RuntimeException runtimeexception)
        {
            System.out.println("debug_log:" + runtimeexception.getMessage());
        }
    }

    public void logDebug(Object obj)
    {
        if(!bool_debug)
        {
            return;
        } else
        {
            log(obj);
            return;
        }
    }

    public void logRes(String s)
    {
        log(res.get(s));
    }

    public void logSys(Object obj)
    {
        try
        {
            if(obj == null)
            {
                obj = "null";
            }
            if(obj instanceof Throwable)
            {
                ((Throwable)obj).printStackTrace();
            } else
            {
                System.out.println(obj.toString());
            }
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
        }
    }

    public void newLogFile(String s)
    {
        if(wFile == null)
        {
            return;
        } else
        {
            setFileWriter(s);
            return;
        }
    }

    private void writeLogFile(Object obj)
    {
        String s = obj != null ? obj.toString() : "null";
        synchronized (this)
        {
            BufferedWriter bufferedwriter = wFile;
            try {
                if(bufferedwriter == null)
                {
                    return;
                }
                bufferedwriter.write(s);
                bufferedwriter.newLine();
            }
            catch (IOException _ex1)
            {
                try
                {
                    bufferedwriter.close();
                }
                catch(IOException _ex2) { }
                wFile = null;
            }
        }
    }

    public synchronized void setListener(DebugListener debuglistener)
    {
        listener = debuglistener;
    }

    public synchronized void setResource(Res res1)
    {
        res = res1;
    }

    public void setDebug(boolean flag)
    {
        bool_debug = flag;
    }

    public synchronized void setFileWriter(String s)
    {
        try
        {
            if(wFile != null)
            {
                wFile.flush();
                wFile.close();
            }
            wFile = new BufferedWriter(new FileWriter(s));
        }
        catch(IOException ioexception)
        {
            System.out.println("debug:" + ioexception);
            wFile = null;
        }
    }

    public void mDestroy()
    {
        listener = null;
        BufferedWriter bufferedwriter = wFile;
        wFile = null;
        try
        {
            bufferedwriter.flush();
            bufferedwriter.close();
        }
        catch(IOException _ex) { }
    }
}
