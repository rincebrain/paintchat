package paintchat_server;

import java.io.IOException;
import java.io.OutputStream;
import paintchat.M;
import paintchat.debug.DebugListener;
import syi.util.ByteStream;
import syi.util.VectorBin;

public class LineTalker extends PaintChatTalker
{
  ByteStream lines_send = new ByteStream();
  VectorBin lines_log = new VectorBin();
  LineServer server;
  DebugListener debug;
  private M mgRead = new M();
  private ByteStream workReceive = new ByteStream();
  private ByteStream workReceive2 = new ByteStream();
  private M mgSend = null;
  private int countDraw = 0;

  public LineTalker(LineServer paramLineServer, DebugListener paramDebugListener)
  {
    this.server = paramLineServer;
    this.debug = paramDebugListener;
  }

  public void send(ByteStream paramByteStream1, M paramM, ByteStream paramByteStream2)
  {
    if (!isValidate())
      return;
    try
    {
      if (this.lines_send.size() >= 65535)
      {
        this.lines_send = null;
        throw new IOException("client error");
      }
      int i = 0;
      int j = paramByteStream1.size();
      byte[] arrayOfByte = paramByteStream1.getBuffer();
      synchronized (this.lines_send)
      {
        while (i + 1 < j)
        {
          i += paramM.set(arrayOfByte, i);
          paramM.get(this.lines_send, paramByteStream2, this.mgSend);
          if (this.mgSend == null)
            this.mgSend = new M();
          this.mgSend.set(paramM);
        }
      }
    }
    catch (IOException localIOException)
    {
      this.debug.log(localIOException);
      mStop();
    }
  }

  protected void mInit()
    throws IOException
  {
    this.iSendInterval = 4000;
    this.server.addTalker(this);
    synchronized (this.lines_send)
    {
      this.lines_send.w2(1);
      this.lines_send.write(0);
    }
  }

  protected void mRead(ByteStream paramByteStream)
    throws IOException
  {
    int i = paramByteStream.size();
    if (i <= 0)
      return;
    if ((i <= 2) && (paramByteStream.getBuffer()[0] == 0))
    {
      this.server.removeTalker(this);
      return;
    }
    this.workReceive.reset();
    while (paramByteStream.size() >= 2)
    {
      i = this.mgRead.set(paramByteStream);
      if (i < 0)
        return;
      this.countDraw += 1;
      paramByteStream.reset(i);
      this.mgRead.get(this.workReceive, this.workReceive2, null);
      if (this.mgRead.iHint != 10)
        continue;
      this.server.addClear(this, this.workReceive);
      this.workReceive.reset();
    }
    if (this.workReceive.size() > 0)
      this.server.addLine(this, this.workReceive);
  }

  protected void mIdle(long paramLong)
    throws IOException
  {
  }

  protected void mWrite()
    throws IOException
  {
    if (this.lines_log != null)
    {
      if (this.lines_log.size() <= 0)
      {
        this.lines_log = null;
        return;
      }
      mSendFlag(1);
      localObject1 = this.lines_log.get(0);
      write(localObject1, localObject1.length);
      this.lines_log.remove(1);
      return;
    }
    if (this.lines_send.size() <= 2)
      return;
    Object localObject1 = getWriteBuffer();
    synchronized (this.lines_send)
    {
      this.lines_send.writeTo((OutputStream)localObject1);
      this.lines_send.reset();
    }
    write((ByteStream)localObject1);
    ((ByteStream)localObject1).reset();
  }

  protected void mDestroy()
  {
    this.lines_send = null;
    this.lines_log = null;
    this.server = null;
    this.debug = null;
  }

  public VectorBin getLogArray()
  {
    return this.lines_log;
  }

  private void mSendFlag(int paramInt)
    throws IOException
  {
    ByteStream localByteStream = getWriteBuffer();
    localByteStream.write(paramInt);
    write(localByteStream);
  }

  public int getDrawCount()
  {
    return this.countDraw;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.LineTalker
 * JD-Core Version:    0.6.0
 */