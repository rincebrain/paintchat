package paintchat_server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import paintchat.Config;
import paintchat.MgText;
import paintchat.debug.DebugListener;
import syi.util.PProperties;
import syi.util.Vector2;

public class TextServer
{
  private boolean isLive = true;
  private TextTalkerListener[] talkers = new TextTalkerListener[10];
  private int countTalker = 0;
  private XMLTalker[] xmlTalkers = new XMLTalker[10];
  private int countXMLTalker = 0;
  private ChatLogOutputStream outLog;
  private DebugListener debug;
  private Config config;
  private String strPassword;
  private Vector2 mgtexts = new Vector2();
  private boolean isCash = true;
  private int iMaxCash;
  public Vector2 vKillIP = new Vector2();
  private int iUniqueLast = 1;
  private Server server;

  public void mInit(Server paramServer, Config paramConfig, DebugListener paramDebugListener)
  {
    if (!this.isLive)
      return;
    this.server = paramServer;
    this.config = paramConfig;
    this.debug = paramDebugListener;
    this.strPassword = paramConfig.getString("Admin_Password", null);
    boolean bool = paramConfig.getBool("Server_Log_Text", false);
    this.isCash = paramConfig.getBool("Server_Cash_Text", true);
    File localFile = new File(paramConfig.getString("Server_Log_Text_Dir", "save_server"));
    this.outLog = new ChatLogOutputStream(localFile, paramDebugListener, bool);
    this.iMaxCash = Math.max(paramConfig.getInt("Server_Cash_Text_Size", 128), 5);
    if (paramConfig.getBool("Server_Load_Text", false))
      this.outLog.loadLog(this.mgtexts, this.iMaxCash, true);
  }

  public synchronized void mStop()
  {
    if (!this.isLive)
      return;
    this.isLive = false;
    for (int i = 0; i < this.countTalker; i++)
    {
      TextTalkerListener localTextTalkerListener = this.talkers[i];
      if (localTextTalkerListener == null)
        continue;
      localTextTalkerListener.mStop();
    }
    if (this.outLog != null)
      this.outLog.close();
    if (this.mgtexts.size() > 0)
      try
      {
        BufferedWriter localBufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.outLog.getTmpFile()), "UTF8"));
        int j = this.mgtexts.size();
        for (int k = 0; k < j; k++)
        {
          MgText localMgText = (MgText)this.mgtexts.get(k);
          switch (localMgText.head)
          {
          case 1:
            localBufferedWriter.write("Login name=" + localMgText.toString());
            break;
          case 2:
            localBufferedWriter.write("Logout name=" + localMgText.getUserName());
            break;
          default:
            localBufferedWriter.write(localMgText.getUserName() + '>' + localMgText.toString());
          }
          localBufferedWriter.newLine();
        }
        localBufferedWriter.flush();
        localBufferedWriter.close();
      }
      catch (IOException localIOException)
      {
      }
  }

  public synchronized void addTalker(TextTalkerListener paramTextTalkerListener)
  {
    if (!this.isLive)
      return;
    MgText localMgText = paramTextTalkerListener.getHandleName();
    String str1 = localMgText.toString();
    if ((str1.length() <= 0) || (!paramTextTalkerListener.isValidate()))
    {
      paramTextTalkerListener.mStop();
      return;
    }
    localMgText.ID = getUniqueID();
    String str2 = getUniqueName(str1);
    if (str1 != str2)
    {
      localMgText.setData(localMgText.ID, localMgText.head, getUniqueName(str1));
      str1 = localMgText.toString();
      localMgText.setUserName(str1);
    }
    localMgText.toBin(false);
    int i = this.countTalker;
    for (int j = 0; j < i; j++)
    {
      if (this.talkers[j].isGuest())
        continue;
      paramTextTalkerListener.send(this.talkers[j].getHandleName());
    }
    if (this.isCash)
      paramTextTalkerListener.sendUpdate(this.mgtexts);
    if (!paramTextTalkerListener.isGuest())
      addText(paramTextTalkerListener, new MgText(localMgText.ID, 1, str1));
    if (this.countTalker + 1 >= this.talkers.length)
    {
      TextTalkerListener[] arrayOfTextTalkerListener = new TextTalkerListener[this.talkers.length + 1];
      System.arraycopy(this.talkers, 0, arrayOfTextTalkerListener, 0, this.talkers.length);
      this.talkers = arrayOfTextTalkerListener;
    }
    this.talkers[this.countTalker] = paramTextTalkerListener;
    this.countTalker += 1;
  }

  public void doAdmin(String paramString, int paramInt)
  {
    if (paramString.indexOf("kill") >= 0)
    {
      killTalker(paramInt);
    }
    else
    {
      TextTalkerListener localTextTalkerListener = getTalker(paramInt);
      localTextTalkerListener.send(new MgText(paramInt, 102, paramString));
    }
  }

  public synchronized void removeTalker(TextTalkerListener paramTextTalkerListener)
  {
    if (!this.isLive)
      return;
    boolean bool = paramTextTalkerListener.isGuest();
    paramTextTalkerListener.mStop();
    MgText localMgText = new MgText();
    localMgText.setData(paramTextTalkerListener.getHandleName());
    localMgText.head = 2;
    localMgText.toBin(false);
    int i = this.countTalker;
    for (int j = 0; j < i; j++)
      if (this.talkers[j] == paramTextTalkerListener)
      {
        this.talkers[j] = null;
        if (j + 1 < i)
        {
          System.arraycopy(this.talkers, j + 1, this.talkers, j, i - (j + 1));
          this.talkers[(i - 1)] = null;
        }
        this.countTalker -= 1;
        i--;
        j--;
      }
      else
      {
        if (bool)
          continue;
        this.talkers[j].send(localMgText);
      }
  }

  public synchronized void killTalker(int paramInt)
  {
    TextTalkerListener localTextTalkerListener = getTalker(paramInt);
    if (localTextTalkerListener == null)
      return;
    MgText localMgText = localTextTalkerListener.getHandleName();
    this.vKillIP.add(localTextTalkerListener.getAddress());
    localTextTalkerListener.kill();
    addText(localTextTalkerListener, new MgText(0, 6, "kill done name=+" + localMgText.toString() + " address=" + localTextTalkerListener.getAddress()));
  }

  public synchronized void addText(TextTalkerListener paramTextTalkerListener, MgText paramMgText)
  {
    if (!this.isLive)
      return;
    for (int i = 0; i < this.countTalker; i++)
    {
      TextTalkerListener localTextTalkerListener = this.talkers[i];
      if ((localTextTalkerListener == paramTextTalkerListener) || (localTextTalkerListener == null))
        continue;
      if (localTextTalkerListener.isValidate())
      {
        localTextTalkerListener.send(paramMgText);
      }
      else
      {
        removeTalker(localTextTalkerListener);
        i--;
      }
    }
    if (this.isCash)
    {
      this.mgtexts.add(paramMgText);
      if (this.mgtexts.size() >= this.iMaxCash)
        this.mgtexts.remove(5);
    }
    if (this.outLog != null)
      this.outLog.write(paramMgText);
  }

  public int getUserCount()
  {
    return this.countTalker;
  }

  public void clearKillIP()
  {
    this.vKillIP.removeAll();
  }

  public synchronized void clearDeadTalker()
  {
    for (int i = 0; i < this.countTalker; i++)
    {
      TextTalkerListener localTextTalkerListener = this.talkers[i];
      if ((localTextTalkerListener != null) && (localTextTalkerListener.isValidate()))
        continue;
      if (localTextTalkerListener != null)
      {
        removeTalker(localTextTalkerListener);
      }
      else
      {
        if (i < this.countTalker - 1)
          System.arraycopy(this.talkers, i + 1, this.talkers, i, this.countTalker - (i + 1));
        this.countTalker -= 1;
      }
      i = -1;
    }
  }

  public synchronized TextTalkerListener getTalker(InetAddress paramInetAddress)
  {
    if (paramInetAddress == null)
      return null;
    for (int i = 0; i < this.countTalker; i++)
    {
      TextTalkerListener localTextTalkerListener = this.talkers[i];
      if ((localTextTalkerListener != null) && (localTextTalkerListener.getAddress().equals(paramInetAddress)))
        return localTextTalkerListener;
    }
    return null;
  }

  public synchronized TextTalkerListener getTalker(int paramInt)
  {
    if (paramInt <= 0)
      return null;
    for (int i = 0; i < this.countTalker; i++)
    {
      TextTalkerListener localTextTalkerListener = this.talkers[i];
      if ((localTextTalkerListener != null) && (localTextTalkerListener.getHandleName().ID == paramInt))
        return localTextTalkerListener;
    }
    return null;
  }

  public synchronized TextTalkerListener getTalkerAt(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.countTalker))
      return null;
    return this.talkers[paramInt];
  }

  private int getUniqueID()
  {
    int i = this.iUniqueLast;
    label54: for (int j = 0; j < this.countTalker; j = 0)
    {
      TextTalkerListener localTextTalkerListener;
      if (((localTextTalkerListener = this.talkers[(j++)]) == null) || (localTextTalkerListener.getHandleName().ID != i))
        break label54;
      i++;
      if (i < 65535)
        continue;
      i = 1;
    }
    this.iUniqueLast = (i >= 65535 ? 1 : i + 1);
    return i;
  }

  private String getUniqueName(String paramString)
  {
    int i = 2;
    String str = paramString;
    label71: for (int j = 0; j < this.countTalker; j = 0)
    {
      TextTalkerListener localTextTalkerListener;
      if (((localTextTalkerListener = this.talkers[(j++)]) == null) || (!str.equals(localTextTalkerListener.getHandleName().toString())))
        break label71;
      str = paramString + i;
      i++;
    }
    return str;
  }

  public String getPassword()
  {
    return this.strPassword;
  }

  public synchronized void getUserListXML(StringBuffer paramStringBuffer)
  {
    int i = this.countTalker;
    for (int j = 0; j < i; j++)
    {
      TextTalkerListener localTextTalkerListener = this.talkers[j];
      if ((localTextTalkerListener == null) || (localTextTalkerListener.isGuest()))
        continue;
      MgText localMgText = localTextTalkerListener.getHandleName();
      String str = localMgText.toString();
      if ((str == null) || (str.length() <= 0))
        continue;
      paramStringBuffer.append("<in id=\"" + localMgText.ID + "\">" + str + "</in>");
    }
  }

  public synchronized MgText getInfomation()
  {
    return this.server.getInfomation();
  }

  public void writeLog(String paramString)
  {
    if (this.outLog != null)
      this.outLog.write(paramString);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.TextServer
 * JD-Core Version:    0.6.0
 */