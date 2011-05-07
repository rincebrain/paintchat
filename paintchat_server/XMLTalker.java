package paintchat_server;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import paintchat.Res;
import syi.util.ByteInputStream;
import syi.util.ByteStream;
import syi.util.Io;
import syi.util.ThreadPool;

public abstract class XMLTalker extends DefaultHandler
  implements Runnable
{
  private boolean isLive = true;
  private boolean isInit = false;
  private boolean canWrite = true;
  private boolean doWrite = false;
  protected int iSendInterval = 2000;
  private BufferedWriter Out;
  private BufferedInputStream In;
  private Socket socket;
  private ByteStream stm_buffer = new ByteStream();
  private Res status;
  long lTime;
  private SAXParser saxParser = null;
  private String strTag;
  private Res res_att = new Res();
  private final String[] strTags = { "talk", "in", "leave", "infomation", "script", "admin" };
  private final String[] strStartTags = { "<talk", "<in", "<leave", "<infomation", "<script", "<admin" };
  private final int[] strHints = { 0, 1, 2, 6, 8, 102 };

  private synchronized void mInitInside()
    throws Throwable
  {
    if (this.isInit)
      return;
    this.isInit = true;
    if (this.In == null)
      this.In = new BufferedInputStream(this.socket.getInputStream());
    if (this.Out == null)
      this.Out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF8"));
    updateTimer();
  }

  public void updateTimer()
  {
    this.lTime = System.currentTimeMillis();
  }

  public synchronized void mStart(Socket paramSocket, InputStream paramInputStream, OutputStream paramOutputStream, Res paramRes)
  {
    try
    {
      this.socket = paramSocket;
      if (paramInputStream != null)
        this.In = ((paramInputStream instanceof BufferedInputStream) ? (BufferedInputStream)paramInputStream : new BufferedInputStream(paramInputStream));
      if (paramOutputStream != null)
        this.Out = new BufferedWriter(new OutputStreamWriter(paramOutputStream));
      this.status = (paramRes == null ? new Res() : paramRes);
      ThreadPool.poolStartThread(this, 'l');
    }
    catch (Throwable localThrowable)
    {
      mStop();
    }
  }

  public synchronized void mStop()
  {
    if (!this.isLive)
      return;
    this.isLive = false;
    this.canWrite = true;
    mDestroy();
    this.canWrite = false;
    mDestroyInside();
  }

  private synchronized void mDestroyInside()
  {
    try
    {
      if (this.Out != null)
        this.Out.close();
    }
    catch (IOException localIOException1)
    {
    }
    this.Out = null;
    try
    {
      if (this.In != null)
        this.In.close();
    }
    catch (IOException localIOException2)
    {
    }
    this.In = null;
    try
    {
      if (this.socket != null)
        this.socket.close();
    }
    catch (IOException localIOException3)
    {
    }
    this.socket = null;
  }

  private void mParseXML(InputStream paramInputStream)
  {
    try
    {
      if (this.saxParser == null)
        this.saxParser = SAXParserFactory.newInstance().newSAXParser();
      this.saxParser.parse(paramInputStream, this);
    }
    catch (Exception localException)
    {
      mStop();
    }
  }

  protected abstract void mInit()
    throws IOException;

  protected abstract void mDestroy();

  protected abstract void mRead(String paramString1, String paramString2, Res paramRes)
    throws IOException;

  protected abstract void mWrite()
    throws IOException;

  public void run()
  {
    try
    {
      mInitInside();
      ByteInputStream localByteInputStream = new ByteInputStream();
      long l1 = 0L;
      long l2 = 0L;
      this.canWrite = false;
      while (this.isLive)
        if (canRead(this.In))
        {
          this.stm_buffer.reset();
          read(this.stm_buffer);
          localByteInputStream.setByteStream(this.stm_buffer);
          mParseXML(localByteInputStream);
          this.stm_buffer.reset();
        }
        else
        {
          long l3 = System.currentTimeMillis();
          l1 = l3 - this.lTime;
          if (l3 - l2 >= this.iSendInterval)
          {
            l2 = l3;
            this.canWrite = true;
            mWrite();
            if (l1 >= 60000L)
            {
              this.stm_buffer.reset();
              write("ping", null);
            }
            this.canWrite = false;
            flush();
          }
          Thread.currentThread();
          Thread.sleep(l1 < 20000L ? 1200 : l1 < 10000L ? 600 : l1 < 5000L ? 400 : l1 < 1000L ? 200 : 2400);
        }
    }
    catch (InterruptedException localInterruptedException)
    {
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    mStop();
  }

  private void setAtt(Attributes paramAttributes)
  {
    int i = paramAttributes.getLength();
    this.res_att.clear();
    try
    {
      for (int j = 0; j < i; j++)
        this.res_att.put(paramAttributes.getQName(j), paramAttributes.getValue(j));
      return;
    }
    catch (RuntimeException localRuntimeException)
    {
      this.res_att.clear();
    }
  }

  protected void write(Res paramRes)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Enumeration localEnumeration = paramRes.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      if (localStringBuffer.length() > 0)
        localStringBuffer.append('\n');
      write(str, paramRes.get(str));
    }
  }

  protected void write(String paramString1, String paramString2)
    throws IOException
  {
    write(paramString1, paramString2, null);
  }

  protected void write(String paramString1, String paramString2, String paramString3)
    throws IOException
  {
    if ((!this.isLive) || (paramString1 == null) || (paramString1.length() <= 0))
      return;
    if (!this.canWrite)
      throw new IOException("write() bad timing");
    if ((paramString2 == null) || (paramString2.length() <= 0))
    {
      if ((paramString3 != null) && (paramString3.length() > 0))
        this.Out.write('<' + paramString1 + ' ' + paramString3 + "/>");
      else
        this.Out.write('<' + paramString1 + "/>");
    }
    else
    {
      if ((paramString3 != null) && (paramString3.length() > 0))
      {
        System.out.println('<' + paramString1 + ' ' + paramString3 + '>');
        this.Out.write('<' + paramString1 + ' ' + paramString3 + '>');
      }
      else
      {
        this.Out.write('<' + paramString1 + '>');
      }
      this.Out.write(paramString2);
      this.Out.write("</" + paramString1 + '>');
    }
    this.doWrite = true;
    updateTimer();
  }

  protected void read(ByteStream paramByteStream)
    throws IOException
  {
    while (this.isLive)
    {
      int i = Io.r(this.In);
      if (i == 0)
        break;
      paramByteStream.write(i);
    }
  }

  protected void flush()
    throws IOException
  {
    if ((this.Out != null) && (this.doWrite))
    {
      this.Out.write(0);
      this.Out.flush();
    }
    this.doWrite = false;
  }

  public boolean canRead(InputStream paramInputStream)
    throws IOException
  {
    return (paramInputStream != null) && (paramInputStream.available() >= 1);
  }

  public Res getStatus()
  {
    return this.status;
  }

  public boolean isValidate()
  {
    return this.isLive;
  }

  public ByteStream getWriteBuffer()
    throws IOException
  {
    if (!this.canWrite)
      throw new IOException("getWriteBuffer() bad timing");
    return this.stm_buffer;
  }

  public synchronized InetAddress getAddress()
  {
    return (!this.isLive) || (this.socket == null) ? null : this.socket.getInetAddress();
  }

  public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws SAXException
  {
    if (this.strTag == null)
      return;
    try
    {
      mRead(this.strTag, new String(paramArrayOfChar, paramInt1, paramInt2), this.res_att);
    }
    catch (IOException localIOException)
    {
      mStop();
    }
  }

  public void endElement(String paramString1, String paramString2, String paramString3)
    throws SAXException
  {
    this.strTag = null;
  }

  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    try
    {
      this.strTag = paramString3;
      if ((paramString3 == null) || (paramString3.length() <= 0))
        return;
      if (paramString3.equals("ping"))
      {
        this.strTag = null;
        return;
      }
      if (paramString3.equals("talk"))
        return;
      setAtt(paramAttributes);
      if (paramString3.equals("initialize"))
      {
        setStatus(paramAttributes);
        mInit();
        return;
      }
      if (paramString3.equals("leave"))
      {
        mRead(paramString3, null, null);
        mStop();
        return;
      }
    }
    catch (Throwable localThrowable)
    {
      mStop();
    }
  }

  public void setStatus(Attributes paramAttributes)
  {
    if (paramAttributes == null)
      return;
    Res localRes = getStatus();
    int i = paramAttributes.getLength();
    for (int j = 0; j < i; j++)
    {
      String str1 = paramAttributes.getQName(j);
      String str2 = paramAttributes.getValue(j);
      if ((str1 == null) || (str1.length() <= 0) || (str2 == null))
        continue;
      localRes.put(str1, str2);
    }
  }

  protected String hintToString(int paramInt)
  {
    for (int i = 0; i < this.strHints.length; i++)
      if (this.strHints[i] == paramInt)
        return this.strTags[i];
    return this.strTags[0];
  }

  protected int strToHint(String paramString)
  {
    if ((paramString == null) || (paramString.length() <= 0) || (paramString.equals("talk")))
      return 0;
    int i = this.strTags.length;
    for (int j = 1; j < i; j++)
      if (this.strTags[j].equals(paramString))
        return this.strHints[j];
    return 0;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.XMLTalker
 * JD-Core Version:    0.6.0
 */