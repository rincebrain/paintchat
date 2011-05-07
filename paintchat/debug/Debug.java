package paintchat.debug;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import paintchat.Res;

public class Debug
  implements DebugListener
{
  private DebugListener listener = null;
  private Res res = null;
  public BufferedWriter wFile = null;
  public boolean bool_debug = false;

  public Debug()
  {
  }

  public Debug(DebugListener paramDebugListener, Res paramRes)
  {
    this.listener = paramDebugListener;
    this.res = paramRes;
  }

  public Debug(Res paramRes)
  {
    this.res = paramRes;
  }

  protected void finalize()
    throws Throwable
  {
    mDestroy();
  }

  public void log(Object paramObject)
  {
    try
    {
      if (this.listener != null)
        this.listener.log(paramObject);
      else
        logSys(paramObject);
      writeLogFile(paramObject);
    }
    catch (RuntimeException localRuntimeException)
    {
      System.out.println("debug_log:" + localRuntimeException.getMessage());
    }
  }

  public void logDebug(Object paramObject)
  {
    if (!this.bool_debug)
      return;
    log(paramObject);
  }

  public void logRes(String paramString)
  {
    log(this.res.get(paramString));
  }

  public void logSys(Object paramObject)
  {
    try
    {
      if (paramObject == null)
        paramObject = "null";
      if ((paramObject instanceof Throwable))
        ((Throwable)paramObject).printStackTrace();
      else
        System.out.println(paramObject.toString());
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
  }

  public void newLogFile(String paramString)
  {
    if (this.wFile == null)
      return;
    setFileWriter(paramString);
  }

  private void writeLogFile(Object paramObject)
  {
    String str = paramObject == null ? "null" : paramObject.toString();
    synchronized (this)
    {
      BufferedWriter localBufferedWriter = this.wFile;
      try
      {
        if (localBufferedWriter == null)
          return;
        localBufferedWriter.write(str);
        localBufferedWriter.newLine();
      }
      catch (IOException localIOException1)
      {
        try
        {
          localBufferedWriter.close();
        }
        catch (IOException localIOException2)
        {
        }
        this.wFile = null;
      }
    }
  }

  public synchronized void setListener(DebugListener paramDebugListener)
  {
    this.listener = paramDebugListener;
  }

  public synchronized void setResource(Res paramRes)
  {
    this.res = paramRes;
  }

  public void setDebug(boolean paramBoolean)
  {
    this.bool_debug = paramBoolean;
  }

  public synchronized void setFileWriter(String paramString)
  {
    try
    {
      if (this.wFile != null)
      {
        this.wFile.flush();
        this.wFile.close();
      }
      this.wFile = new BufferedWriter(new FileWriter(paramString));
    }
    catch (IOException localIOException)
    {
      System.out.println("debug:" + localIOException);
      this.wFile = null;
    }
  }

  public void mDestroy()
  {
    this.listener = null;
    BufferedWriter localBufferedWriter = this.wFile;
    this.wFile = null;
    try
    {
      localBufferedWriter.flush();
      localBufferedWriter.close();
    }
    catch (IOException localIOException)
    {
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.debug.Debug
 * JD-Core Version:    0.6.0
 */