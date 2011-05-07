package paintchat_client;

import java.io.IOException;
import java.util.Hashtable;
import paintchat.MgText;
import paintchat.Res;
import paintchat_server.PaintChatTalker;
import syi.util.ByteInputStream;
import syi.util.ByteStream;

public class TText extends PaintChatTalker
{
  private Pl pl;
  private Data data;
  private MgText mg = new MgText();
  private ByteInputStream bin = new ByteInputStream();
  private ByteStream stm = new ByteStream();
  private Res names = new Res();

  public TText(Pl paramPl, Data paramData)
  {
    this.pl = paramPl;
    this.data = paramData;
    this.iSendInterval = 1000;
  }

  public void mInit()
  {
  }

  protected synchronized void mDestroy()
  {
    try
    {
      MgText localMgText = new MgText(0, 2, null);
      this.stm.reset();
      localMgText.getData(this.stm, false);
      write(this.stm);
      this.stm.reset();
      flush();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  protected void mRead(ByteStream paramByteStream)
    throws IOException
  {
    int i = 0;
    int j = paramByteStream.size();
    this.bin.setByteStream(paramByteStream);
    while (i < j)
    {
      i += this.mg.setData(this.bin);
      String str = this.mg.toString();
      Integer localInteger = new Integer(this.mg.ID);
      switch (this.mg.head)
      {
      case 1:
        this.names.put(localInteger, str);
        this.pl.addInOut(str, true);
        break;
      case 2:
        this.names.remove(localInteger);
        this.pl.addInOut(str, false);
        break;
      case 0:
        this.pl.addText(this.mg.bName != null ? this.mg.getUserName() : (String)this.names.get(localInteger), str, true);
        this.pl.dSound(1);
        break;
      case 102:
        this.data.mPermission(str);
        break;
      default:
        this.pl.addText(null, str, true);
        this.pl.dSound(1);
      }
    }
  }

  protected void mIdle(long paramLong)
    throws IOException
  {
  }

  protected synchronized void mWrite()
    throws IOException
  {
    if (this.stm.size() <= 0)
      return;
    write(this.stm);
    this.stm.reset();
  }

  public synchronized void send(MgText paramMgText)
  {
    try
    {
      paramMgText.getData(this.stm, false);
    }
    catch (IOException localIOException)
    {
    }
  }

  public synchronized void mRStop()
  {
    try
    {
      this.canWrite = true;
      this.stm.reset();
      new MgText(0, 2, null).getData(this.stm, false);
      write(this.stm);
      flush();
      mStop();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_client.TText
 * JD-Core Version:    0.6.0
 */