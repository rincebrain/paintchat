package paintchat_client;

import java.io.IOException;
import java.util.zip.Inflater;
import paintchat.M;
import paintchat.M.Info;
import paintchat_server.PaintChatTalker;
import syi.util.ByteStream;
import syi.util.ThreadPool;

public class TLine extends PaintChatTalker
{
  public Data data;
  private ByteStream bSendCash = new ByteStream();
  private ByteStream stmIn = new ByteStream();
  private ByteStream workSend = new ByteStream();
  private M mgOut = null;
  private M mgDraw = null;
  private boolean isCompress = false;
  private boolean isRunDraw = false;
  Inflater inflater = new Inflater(false);

  public TLine(Data paramData, M paramM)
  {
    this.data = paramData;
    this.mgDraw = paramM;
  }

  protected void mDestroy()
  {
    this.isRunDraw = false;
    this.iSendInterval = 0;
  }

  protected void mRead(ByteStream paramByteStream)
    throws IOException
  {
    if (paramByteStream.size() <= 1)
    {
      switch (paramByteStream.getBuffer()[0])
      {
      case 0:
        synchronized (this.stmIn)
        {
          this.stmIn.w2(0);
        }
        break;
      case 1:
        this.isCompress = true;
      }
      return;
    }
    try
    {
      int i = paramByteStream.size();
      if (this.isCompress)
      {
        this.isCompress = false;
        if (this.inflater == null)
          this.inflater = new Inflater(false);
        this.inflater.reset();
        this.inflater.setInput(paramByteStream.getBuffer(), 0, i);
        synchronized (this)
        {
          byte[] arrayOfByte = this.workSend.getBuffer();
          synchronized (this.stmIn)
          {
            while (!this.inflater.needsInput())
            {
              int j = this.inflater.inflate(arrayOfByte, 0, arrayOfByte.length);
              this.stmIn.write(arrayOfByte, 0, j);
            }
          }
        }
      }
      else
      {
        if (this.inflater != null)
        {
          this.inflater.end();
          this.inflater = null;
        }
        synchronized (this.stmIn)
        {
          paramByteStream.writeTo(this.stmIn);
        }
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public void mInit()
  {
  }

  protected void mIdle(long paramLong)
    throws IOException
  {
  }

  protected void mWrite()
    throws IOException
  {
    if (this.bSendCash.size() <= 0)
      return;
    synchronized (this.bSendCash)
    {
      write(this.bSendCash);
      this.bSendCash.reset();
    }
  }

  public void send(M paramM)
  {
    synchronized (this.bSendCash)
    {
      if (paramM == null)
      {
        try
        {
          this.canWrite = true;
          this.bSendCash.reset();
          this.bSendCash.w2(2);
          write(this.bSendCash);
          flush();
        }
        catch (IOException localIOException)
        {
        }
        return;
      }
      paramM.get(this.bSendCash, this.workSend, this.mgOut);
      if (this.mgOut == null)
        this.mgOut = new M();
      this.mgOut.set(paramM);
    }
  }

  public void run()
  {
    if (!this.isRunDraw)
    {
      this.isRunDraw = true;
      ThreadPool.poolStartThread(this, 'd');
      super.run();
      return;
    }
    try
    {
      while (this.isRunDraw)
        if (this.stmIn.size() >= 2)
        {
          synchronized (this.stmIn)
          {
            byte[] arrayOfByte = this.stmIn.getBuffer();
            int i = (arrayOfByte[0] & 0xFF) << 8 | arrayOfByte[1] & 0xFF;
            if (i <= 2)
            {
              this.mgDraw.newUser(null).wait = 0;
              this.data.addTextComp();
              this.stmIn.reset(i + 2);
              continue;
            }
            i = this.mgDraw.set(this.stmIn.getBuffer(), 0);
            this.stmIn.reset(i);
          }
          if (this.mgDraw.iLayer >= this.data.info.L)
            this.data.info.setL(this.mgDraw.iLayer + 1);
          this.mgDraw.draw();
        }
        else
        {
          Thread.currentThread();
          Thread.sleep(3000L);
        }
    }
    catch (InterruptedException localInterruptedException)
    {
    }
  }

  public synchronized void mRStop()
  {
    send(null);
    mStop();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_client.TLine
 * JD-Core Version:    0.6.0
 */