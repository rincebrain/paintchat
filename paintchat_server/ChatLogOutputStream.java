package paintchat_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import paintchat.MgText;
import paintchat.debug.DebugListener;
import syi.util.Io;
import syi.util.Vector2;

public class ChatLogOutputStream extends Writer
{
  private boolean isValidate = true;
  BufferedWriter outLog = null;
  File fDir;
  DebugListener debug;
  long timeStart = 0L;

  public ChatLogOutputStream(File paramFile, DebugListener paramDebugListener, boolean paramBoolean)
  {
    this.fDir = paramFile;
    this.debug = paramDebugListener;
    this.isValidate = paramBoolean;
  }

  public synchronized void write(char paramChar)
    throws IOException
  {
    try
    {
      if (!this.isValidate)
        return;
      initFile();
      if (this.outLog != null)
        this.outLog.write(paramChar);
    }
    catch (IOException localIOException)
    {
      this.debug.log(localIOException);
    }
  }

  public synchronized void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    try
    {
      if (!this.isValidate)
        return;
      initFile();
      if (this.outLog != null)
      {
        this.outLog.write(paramArrayOfChar, paramInt1, paramInt2);
        this.outLog.newLine();
      }
    }
    catch (IOException localIOException)
    {
      this.debug.log(localIOException);
    }
  }

  public void write(String paramString)
  {
    write(paramString, 0, paramString.length());
  }

  public synchronized void write(String paramString, int paramInt1, int paramInt2)
  {
    try
    {
      if (!this.isValidate)
        return;
      initFile();
      if (this.outLog != null)
      {
        this.outLog.write(paramString, paramInt1, paramInt2);
        this.outLog.newLine();
      }
    }
    catch (IOException localIOException)
    {
      this.debug.log(localIOException);
    }
  }

  public synchronized void write(MgText paramMgText)
  {
    try
    {
      if (!this.isValidate)
        return;
      initFile();
      if (this.outLog == null)
        return;
      if (paramMgText.bName != null)
      {
        this.outLog.write(paramMgText.getUserName());
        this.outLog.write(62);
      }
      switch (paramMgText.head)
      {
      case 0:
      case 6:
      case 8:
        this.outLog.write(paramMgText.toString());
        this.outLog.newLine();
      }
    }
    catch (IOException localIOException)
    {
    }
  }

  private void initFile()
  {
    try
    {
      if (!this.isValidate)
        return;
      long l = System.currentTimeMillis();
      if ((this.outLog == null) || ((this.timeStart != 0L) && (l - this.timeStart >= 86400000L)))
      {
        this.timeStart = l;
        (this.outLog == null ? 1 : 0);
        if (!this.fDir.isDirectory())
          this.fDir.mkdirs();
        if (this.outLog != null)
        {
          try
          {
            this.outLog.flush();
            this.outLog.close();
          }
          catch (IOException localIOException2)
          {
            this.debug.log(localIOException2);
          }
          this.outLog = null;
        }
        File localFile = getLogFile();
        this.outLog = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(localFile, true), "UTF8"), 1024);
      }
    }
    catch (IOException localIOException1)
    {
      this.debug.log(localIOException1);
    }
  }

  public synchronized void close()
  {
    try
    {
      if (this.outLog != null)
      {
        this.outLog.flush();
        this.outLog.close();
        this.outLog = null;
        File localFile = getLogFile();
        if ((localFile.isFile()) && (localFile.length() <= 0L))
          localFile.delete();
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }

  private File getLogFile()
    throws IOException
  {
    return new File(Io.getDateString("text_log", "txt", this.fDir.getCanonicalPath()));
  }

  public File getTmpFile()
  {
    return new File(this.fDir, "text_cash.tmp");
  }

  public void loadLog(Vector2 paramVector2, int paramInt, boolean paramBoolean)
  {
    File localFile = getTmpFile();
    if ((!localFile.isFile()) || (localFile.length() <= 0L))
      return;
    BufferedReader localBufferedReader = null;
    try
    {
      localBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(localFile), "UTF8"));
      String str;
      while ((str = localBufferedReader.readLine()) != null)
      {
        paramVector2.add(new MgText(0, 6, str));
        if (paramVector2.size() < paramInt)
          continue;
        paramVector2.remove(5);
      }
    }
    catch (IOException localIOException1)
    {
    }
    try
    {
      if (localBufferedReader != null)
        localBufferedReader.close();
    }
    catch (IOException localIOException2)
    {
    }
    if (paramBoolean)
      localFile.delete();
  }

  public void flush()
    throws IOException
  {
    if (this.outLog != null)
      this.outLog.flush();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.ChatLogOutputStream
 * JD-Core Version:    0.6.0
 */