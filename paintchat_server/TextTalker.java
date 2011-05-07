package paintchat_server;

import java.io.IOException;
import paintchat.MgText;
import paintchat.Res;
import paintchat.debug.DebugListener;
import syi.util.ByteInputStream;
import syi.util.ByteStream;
import syi.util.Vector2;

public class TextTalker extends PaintChatTalker
  implements TextTalkerListener
{
  private Vector2 sendText = new Vector2();
  private MgText[] sendUpdate = null;
  private TextServer server;
  private DebugListener debug;
  private MgText mgName;
  private MgText mgRead = new MgText();
  private ByteInputStream bin = new ByteInputStream();
  private int countSpeak = 0;
  private boolean isGuest = false;
  private boolean isKill = false;

  public TextTalker(TextServer paramTextServer, DebugListener paramDebugListener)
  {
    this.debug = paramDebugListener;
    this.server = paramTextServer;
  }

  protected void mInit()
    throws IOException
  {
    this.mgName = new MgText(0, 1, getStatus().get("name"));
    this.mgName.setUserName(this.mgName.toString());
    this.isGuest = getStatus().getBool("guest", false);
    this.server.addTalker(this);
    String str = "User login name=" + this.mgName.getUserName() + " host=" + getAddress();
    this.server.writeLog(str);
    this.debug.log(str);
  }

  protected void mDestroy()
  {
    if (this.mgName.getUserName().length() > 0)
    {
      String str = "User logout name=" + this.mgName.getUserName() + " host=" + getAddress();
      this.server.writeLog(str);
      this.debug.log(str);
    }
  }

  protected void mRead(ByteStream paramByteStream)
    throws IOException
  {
    this.bin.setByteStream(paramByteStream);
    int i = paramByteStream.size();
    int j = 0;
    while (j < i - 1)
    {
      int k = this.mgRead.setData(this.bin);
      this.mgRead.ID = this.mgName.ID;
      this.mgRead.bName = this.mgName.bName;
      j += k;
      if (k <= 0)
        throw new IOException("broken");
      switchMessage(this.mgRead);
    }
  }

  protected void mIdle(long paramLong)
    throws IOException
  {
  }

  protected void mWrite()
    throws IOException
  {
    ByteStream localByteStream = getWriteBuffer();
    if (this.sendUpdate != null)
    {
      i = this.sendUpdate.length;
      for (int k = 0; k < i; k++)
      {
        j = this.sendUpdate[k].head;
        if ((j != 0) && (j != 6) && (j != 8))
          continue;
        this.sendUpdate[k].getData(localByteStream, true);
      }
      this.sendUpdate = null;
      write(localByteStream);
      localByteStream.reset();
    }
    int i = this.sendText.size();
    if (i <= 0)
      return;
    for (int j = 0; j < i; j++)
      ((MgText)this.sendText.get(j)).getData(localByteStream, false);
    this.sendText.remove(i);
    write(localByteStream);
  }

  private void switchMessage(MgText paramMgText)
    throws IOException
  {
    switch (paramMgText.head)
    {
    case 2:
      this.server.removeTalker(this);
      break;
    case 0:
      this.countSpeak += 1;
    case 1:
    default:
      if (this.isKill)
        break;
      this.server.addText(this, new MgText(paramMgText));
    }
  }

  public void send(MgText paramMgText)
  {
    try
    {
      if ((!isValidate()) || (this.isKill))
        return;
      this.sendText.add(paramMgText);
    }
    catch (RuntimeException localRuntimeException)
    {
      this.debug.log(getHandleName() + ":" + localRuntimeException.getMessage());
      mStop();
    }
  }

  public synchronized void sendUpdate(Vector2 paramVector2)
  {
    int i = paramVector2.size();
    if (i > 0)
    {
      this.sendUpdate = new MgText[i];
      paramVector2.copy(this.sendUpdate, 0, 0, i);
    }
  }

  public MgText getHandleName()
  {
    return this.mgName;
  }

  public boolean isGuest()
  {
    return this.isGuest;
  }

  public synchronized void kill()
  {
    send(new MgText(this.mgName.ID, 102, "canvas:false;chat:false;"));
    this.isKill = true;
  }

  public int getSpeakCount()
  {
    return this.countSpeak;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.TextTalker
 * JD-Core Version:    0.6.0
 */