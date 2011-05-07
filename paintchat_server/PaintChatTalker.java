package paintchat_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import paintchat.Res;
import syi.util.ByteInputStream;
import syi.util.ByteStream;
import syi.util.Io;
import syi.util.ThreadPool;

public abstract class PaintChatTalker
  implements Runnable
{
  private boolean isLive = true;
  protected boolean canWrite = true;
  private boolean doWrite = false;
  protected int iSendInterval = 2000;
  private OutputStream Out;
  private InputStream In;
  private Socket socket;
  private ByteStream stm_buffer = new ByteStream();
  private Res status = null;
  long lTime;
  private boolean isConnect = false;

  private void mInitInside()
    throws IOException
  {
    if (this.In == null)
      this.In = this.socket.getInputStream();
    if (this.Out == null)
      this.Out = this.socket.getOutputStream();
    updateTimer();
    if (this.isConnect)
    {
      w(98);
      write(getStatus());
      flush();
      ByteStream localByteStream = getWriteBuffer();
      read(localByteStream);
      if (localByteStream.size() > 0)
      {
        ByteInputStream localByteInputStream = new ByteInputStream();
        localByteInputStream.setByteStream(localByteStream);
        getStatus().load(localByteInputStream);
      }
    }
    else
    {
      write(getStatus());
      flush();
    }
    mInit();
  }

  public void updateTimer()
  {
    this.lTime = System.currentTimeMillis();
  }

  public synchronized void mStart(Socket paramSocket, InputStream paramInputStream, OutputStream paramOutputStream, Res paramRes)
  {
    this.socket = paramSocket;
    this.In = paramInputStream;
    this.Out = paramOutputStream;
    this.status = (paramRes == null ? new Res() : paramRes);
    paramSocket.getInetAddress().getHostName();
    ThreadPool.poolStartThread(this, 'l');
  }

  public synchronized void mConnect(Socket paramSocket, Res paramRes)
    throws IOException
  {
    this.isConnect = true;
    mStart(paramSocket, null, null, paramRes);
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

  private void mDestroyInside()
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

  protected abstract void mInit()
    throws IOException;

  protected abstract void mDestroy();

  protected abstract void mRead(ByteStream paramByteStream)
    throws IOException;

  protected abstract void mIdle(long paramLong)
    throws IOException;

  protected abstract void mWrite()
    throws IOException;

  public void run()
  {
    long l1 = 0L;
    long l2 = 0L;
    try
    {
      mInitInside();
      this.canWrite = false;
      while (this.isLive)
        if (canRead(this.In))
        {
          this.stm_buffer.reset();
          read(this.stm_buffer);
          if (this.stm_buffer.size() > 0)
            mRead(this.stm_buffer);
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
              write(this.stm_buffer);
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

  protected void read(ByteStream paramByteStream)
    throws IOException
  {
    int i = Io.readUShort(this.In);
    if (i <= 0)
      return;
    paramByteStream.write(this.In, i);
    updateTimer();
  }

  protected void write(ByteStream paramByteStream)
    throws IOException
  {
    write(paramByteStream.getBuffer(), paramByteStream.size());
  }

  protected void write(Res paramRes)
    throws IOException
  {
    if (paramRes == null)
    {
      write(null, 0);
      return;
    }
    StringBuffer localStringBuffer = new StringBuffer();
    Object localObject = paramRes.keys();
    while (((Enumeration)localObject).hasMoreElements())
    {
      String str = (String)((Enumeration)localObject).nextElement();
      if (localStringBuffer.length() > 0)
        localStringBuffer.append('\n');
      localStringBuffer.append(str);
      localStringBuffer.append('=');
      localStringBuffer.append(paramRes.get(str));
    }
    localObject = localStringBuffer.toString().getBytes("UTF8");
    write(localObject, localObject.length);
  }

  protected void write(byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    if (!this.isLive)
      return;
    if (!this.canWrite)
      throw new IOException("write() bad timing");
    Io.wShort(this.Out, paramInt);
    if (paramInt > 0)
      this.Out.write(paramArrayOfByte, 0, paramInt);
    this.doWrite = true;
    updateTimer();
  }

  protected void w(int paramInt)
    throws IOException
  {
    if (!this.isLive)
      return;
    this.Out.write(paramInt);
    this.doWrite = true;
    updateTimer();
  }

  protected void flush()
    throws IOException
  {
    if ((this.Out != null) && (this.doWrite))
      this.Out.flush();
    this.doWrite = false;
  }

  public boolean canRead(InputStream paramInputStream)
    throws IOException
  {
    return (paramInputStream != null) && (paramInputStream.available() >= 2);
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
    this.stm_buffer.reset();
    return this.stm_buffer;
  }

  public synchronized InetAddress getAddress()
  {
    return (!this.isLive) || (this.socket == null) ? null : this.socket.getInetAddress();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.PaintChatTalker
 * JD-Core Version:    0.6.0
 */