package paintchat_frame;

import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.PrintStream;
import java.util.EventObject;
import java.util.Hashtable;
import paintchat.Config;
import paintchat.Res;
import paintchat.debug.Debug;
import paintchat_http.HttpServer;
import paintchat_server.Server;
import syi.applet.ServerStub;
import syi.awt.Gui;
import syi.awt.LButton;
import syi.awt.MessageBox;
import syi.util.ClassLoaderCustom;
import syi.util.PProperties;
import syi.util.ThreadPool;

public class Data
  implements Runnable, ActionListener
{
  protected transient PropertyChangeSupport propertyChange;
  private Res res = null;
  private Config config = null;
  private Debug debug = null;
  private boolean fieldIsNativeWindows = false;
  private Server server = null;
  private HttpServer http = null;
  private Applet lobby = null;
  private LButton bChat;
  private LButton bHttp;
  private LButton bLobby;

  public Data()
  {
    File localFile = new File("cnf/temp.tmp");
    setIsNativeWindows(localFile.isFile());
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    try
    {
      Object localObject = paramActionEvent.getSource();
      ThreadPool.poolStartThread(this, localObject == this.bHttp ? 'h' : localObject == this.bChat ? 'c' : 'l');
    }
    catch (Throwable localThrowable)
    {
      this.debug.log(localThrowable.getMessage());
    }
  }

  public synchronized void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
  {
    getPropertyChange().addPropertyChangeListener(paramPropertyChangeListener);
  }

  public boolean exitWin()
  {
    try
    {
      File localFile1 = new File(System.getProperty("user.dir"));
      File localFile2 = new File(localFile1, "cnf\\temp.tmp");
      if (!localFile2.isFile())
        return false;
      localFile2 = new File(localFile1, "PaintChat.exe");
      if (!localFile2.isFile())
        return false;
      Process localProcess = Runtime.getRuntime().exec(localFile2.getCanonicalPath() + " 1");
      localProcess.waitFor();
    }
    catch (Throwable localThrowable)
    {
      System.out.println("win" + localThrowable);
    }
    return true;
  }

  public void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
  {
    getPropertyChange().firePropertyChange(paramString, paramObject1, paramObject2);
  }

  public boolean getIsNativeWindows()
  {
    return this.fieldIsNativeWindows;
  }

  protected PropertyChangeSupport getPropertyChange()
  {
    if (this.propertyChange == null)
      this.propertyChange = new PropertyChangeSupport(this);
    return this.propertyChange;
  }

  public void init(Config paramConfig, Res paramRes, Debug paramDebug, LButton paramLButton1, LButton paramLButton2, LButton paramLButton3)
  {
    this.config = paramConfig;
    this.res = paramRes;
    this.debug = paramDebug;
    this.bChat = paramLButton1;
    this.bHttp = paramLButton2;
    this.bLobby = paramLButton3;
    paramLButton1.addActionListener(this);
    paramLButton2.addActionListener(this);
    paramLButton3.addActionListener(this);
    if (paramConfig.getBool("App_Auto_Http"))
      ThreadPool.poolStartThread(this, 'h');
    else
      paramLButton2.setText(paramRes.get("Http_Button_Start"));
    if (paramConfig.getBool("App_Auto_Paintchat"))
      ThreadPool.poolStartThread(this, 'c');
    else
      paramLButton1.setText(paramRes.get("Paintchat_Button_Start"));
  }

  public boolean isRunPaintChatServer()
  {
    return this.server != null;
  }

  public synchronized void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
  {
    getPropertyChange().removePropertyChangeListener(paramPropertyChangeListener);
  }

  public void run()
  {
    switch (Thread.currentThread().getName().charAt(0))
    {
    case 'c':
      runPaintChat();
      break;
    case 'h':
      runHttp();
      break;
    case 'l':
      runLobby();
    }
  }

  private synchronized void runHttp()
  {
    if (this.http == null)
    {
      this.http = new HttpServer(this.config, this.debug, false);
      this.http.init(this.config.getInt("Connection_Port_Http", 80));
      this.bHttp.setText(this.res.get("Http_Button_Stop"));
    }
    else
    {
      this.http.exitServer();
      this.http = null;
      this.bHttp.setText(this.res.get("Http_Button_Start"));
    }
  }

  private synchronized void runLobby()
  {
    String str = "chatIndex";
    if (this.lobby != null)
    {
      if (!MessageBox.confirm("LobbyDisconnect", "(C)しぃちゃん PaintChatApp v3.66"))
        return;
      Applet localApplet = this.lobby;
      this.lobby = null;
      localApplet.stop();
      this.debug.log(this.res.get("LobbyOut"));
      return;
    }
    if (this.config.getInt(str) == 0)
    {
      try
      {
        new ConfigDialog("paintchat.config.Ao", "cnf/dialogs.jar", this.config, this.res, "(C)しぃちゃん PaintChatApp v3.66");
      }
      catch (Exception localException1)
      {
        this.debug.log(localException1.getMessage());
      }
      if (this.config.getInt(str) == 0)
      {
        this.debug.log("LobbyCancel");
        return;
      }
    }
    try
    {
      int i = "(C)しぃちゃん PaintChatApp v3.66".lastIndexOf('v') + 1;
      this.config.put("pchatVersion", "(C)しぃちゃん PaintChatApp v3.66".substring(i, i + 4));
      this.lobby = ((Applet)new ClassLoaderCustom().loadClass("lobbyusers.Users", "cnf", true).newInstance());
      this.lobby.setStub(ServerStub.getDefaultStub(this.config, this.res));
      this.lobby.init();
      this.lobby.start();
      this.debug.log(this.res.get("LobbyIn"));
      if (this.config.getBool("ao_show_html"))
        Gui.showDocument("http://ax.sakura.ne.jp/~aotama/pchat/LobbyRoom.html", this.config, this.res);
    }
    catch (Exception localException2)
    {
      this.debug.log(localException2.getMessage());
    }
  }

  private synchronized void runPaintChat()
  {
    if (this.server == null)
    {
      this.server = new Server(this.config.getString("Admin_Password"), this.config, this.debug, false);
      this.server.init();
      this.bChat.setText(this.res.get("Paintchat_Button_Stop"));
      if (this.config.getBool("App_Auto_Lobby", true))
        startLobby(false);
    }
    else
    {
      this.server.exitServer();
      this.server = null;
      this.bChat.setText(this.res.get("Paintchat_Button_Start"));
      if (this.lobby != null)
        runLobby();
    }
  }

  public void setIsNativeWindows(boolean paramBoolean)
  {
    this.fieldIsNativeWindows = paramBoolean;
  }

  public void startHttp(boolean paramBoolean)
  {
    if (paramBoolean)
      ThreadPool.poolStartThread(this, 'h');
    else
      runHttp();
  }

  public void startLobby(boolean paramBoolean)
  {
    if (paramBoolean)
      ThreadPool.poolStartThread(this, 'l');
    else
      runLobby();
  }

  public void startPaintChat(boolean paramBoolean)
  {
    if (paramBoolean)
      ThreadPool.poolStartThread(this, 'c');
    else
      runPaintChat();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_frame.Data
 * JD-Core Version:    0.6.0
 */