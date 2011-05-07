package paintchat_server;

import java.io.IOException;
import paintchat.MgText;
import paintchat.Res;
import paintchat.debug.DebugListener;
import syi.util.ByteInputStream;
import syi.util.Vector2;

public class XMLTextTalker extends XMLTalker
  implements TextTalkerListener
{
  private Vector2 send_text = new Vector2();
  private Vector2 send_update = new Vector2();
  private ByteInputStream input = new ByteInputStream();
  private TextServer server;
  private DebugListener debug;
  private MgText mgName;
  private boolean isOnlyUserList = false;
  private boolean isGuest = false;
  private boolean isKill = false;
  private int countSpeak = 0;

  public XMLTextTalker(TextServer paramTextServer, DebugListener paramDebugListener)
  {
    this.server = paramTextServer;
    this.debug = paramDebugListener;
  }

  protected void mInit()
    throws IOException
  {
    Res localRes = getStatus();
    this.mgName = new MgText(0, 1, getStatus().get("name"));
    this.isOnlyUserList = localRes.getBool("user_list_only", false);
    this.isGuest = localRes.getBool("guest", false);
    this.server.addTalker(this);
  }

  protected void mDestroy()
  {
  }

  protected void mRead(String paramString1, String paramString2, Res paramRes)
    throws IOException
  {
    if (this.mgName == null)
      return;
    byte b = (byte)strToHint(paramString1);
    MgText localMgText = new MgText();
    localMgText.setData(this.mgName.ID, b, toEscape(paramString2));
    switch (b)
    {
    default:
      if (this.isGuest)
      {
        String str = this.server.getPassword();
        if ((str == null) || (str.length() <= 0) || (!getStatus().get("password").equals(str)))
          break;
      }
      else
      {
        this.server.addText(this, localMgText);
      }
      break;
    case 102:
      try
      {
        if (paramString2.indexOf("get:infomation") >= 0)
        {
          send(this.server.getInfomation());
        }
        else
        {
          int i = paramRes.getInt("id", 0);
          if (i <= 0)
            break;
          this.server.doAdmin(paramString2, i);
        }
      }
      catch (RuntimeException localRuntimeException)
      {
        this.debug.log(localRuntimeException);
      }
    case 2:
      this.server.removeTalker(this);
    }
  }

  protected void mWrite()
    throws IOException
  {
    MgText localMgText;
    if (this.send_update != null)
    {
      i = this.send_update.size();
      if (i > 0)
      {
        StringBuffer localStringBuffer = new StringBuffer();
        for (int k = 0; k < i; k++)
        {
          localMgText = (MgText)this.send_update.get(k);
          String str = hintToString(localMgText.head);
          localStringBuffer.append('<');
          localStringBuffer.append(str);
          localStringBuffer.append(" name=\"");
          localStringBuffer.append(toEscape(localMgText.getUserName()));
          localStringBuffer.append("\">");
          localStringBuffer.append(toEscape(localMgText.toString()));
          localStringBuffer.append("</");
          localStringBuffer.append(str);
          localStringBuffer.append('>');
        }
        write("update", localStringBuffer.toString());
        this.send_update.removeAll();
      }
      this.send_update = null;
    }
    int i = this.send_text.size();
    if (i <= 0)
      return;
    for (int j = 0; j < i; j++)
    {
      localMgText = (MgText)this.send_text.get(j);
      write(hintToString(localMgText.head), toEscape(localMgText.toString()), "id=\"" + localMgText.ID + "\" name=\"" + toEscape(localMgText.getUserName()) + "\"");
    }
    this.send_text.remove(i);
  }

  public void send(MgText paramMgText)
  {
    if (!isValidate())
      return;
    if ((this.isOnlyUserList) && (paramMgText.head != 1) && (paramMgText.head != 2))
      return;
    this.send_text.add(paramMgText);
  }

  public MgText getHandleName()
  {
    return this.mgName;
  }

  public void sendUpdate(Vector2 paramVector2)
  {
    paramVector2.copy(this.send_text);
  }

  public boolean isGuest()
  {
    return this.isGuest;
  }

  private String toEscape(String paramString)
  {
    if ((paramString.indexOf('<') >= 0) || (paramString.indexOf('>') >= 0) || (paramString.indexOf('&') >= 0) || (paramString.indexOf('"') >= 0) || (paramString.indexOf('"') >= 0))
    {
      StringBuffer localStringBuffer = new StringBuffer();
      for (int i = 0; i < paramString.length(); i++)
      {
        char c = paramString.charAt(i);
        switch (c)
        {
        case '<':
          localStringBuffer.append("&lt;");
          break;
        case '>':
          localStringBuffer.append("&gt;");
          break;
        case '&':
          localStringBuffer.append("&amp;");
          break;
        case '"':
          localStringBuffer.append("&qout;");
          break;
        default:
          localStringBuffer.append(c);
        }
      }
      return localStringBuffer.toString();
    }
    return paramString;
  }

  private boolean equalsPassword()
  {
    String str = this.server.getPassword();
    return (str == null) || (str.length() <= 0) || (!getStatus().get("password").equals(str));
  }

  public void kill()
  {
    this.isKill = true;
  }

  public int getSpeakCount()
  {
    return this.countSpeak;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.XMLTextTalker
 * JD-Core Version:    0.6.0
 */