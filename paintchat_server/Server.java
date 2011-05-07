package paintchat_server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Hashtable;
import paintchat.Config;
import paintchat.MgText;
import paintchat.Res;
import paintchat.admin.LocalAdmin;
import paintchat.debug.Debug;
import syi.util.Io;
import syi.util.PProperties;
import syi.util.ThreadPool;

public class Server
  implements Runnable
{
  public static final String STR_VERSION = "(C)しぃちゃん PaintChatServer v3.57b";
  public static final String FILE_CONFIG = "cnf/paintchat.cf";
  private TalkerInstance talkerInstance;
  private boolean live = true;
  private boolean isLiveThread = true;
  private boolean isOnlyServer;
  private Config config = null;
  public Debug debug = null;
  private ServerSocket sSocket;
  private Thread tConnect = null;
  private Thread tLive = null;
  TextServer textServer;
  LineServer lineServer;

  public Server(String paramString, Config paramConfig, Debug paramDebug, boolean paramBoolean)
  {
    this.config = paramConfig;
    this.isOnlyServer = paramBoolean;
    if ((paramString != null) && (paramString.length() > 0))
      paramConfig.put("Admin_Password", paramString);
    this.debug = new Debug(paramDebug, null);
  }

  public synchronized void exitServer()
  {
    if (!this.live)
      return;
    this.live = false;
    try
    {
      if (this.sSocket != null)
        try
        {
          this.sSocket.close();
          this.sSocket = null;
        }
        catch (Exception localException1)
        {
        }
      Thread localThread = Thread.currentThread();
      if ((this.tConnect != null) && (this.tConnect != localThread))
        try
        {
          this.tConnect.interrupt();
          this.tConnect = null;
        }
        catch (Exception localException2)
        {
        }
      if ((this.tLive != null) && (this.tLive != localThread))
        try
        {
          this.tLive.interrupt();
          this.tLive = null;
        }
        catch (Exception localException3)
        {
        }
      this.lineServer.mStop();
      this.textServer.mStop();
      this.debug.log("PaintChatサーバーを終了させます");
    }
    catch (Throwable localThrowable)
    {
    }
    if (this.isOnlyServer)
      System.exit(0);
  }

  public void init()
  {
    try
    {
      Thread localThread = new Thread(this, "init");
      localThread.setDaemon(false);
      localThread.start();
    }
    catch (Exception localException)
    {
      this.debug.log("init_thread" + localException);
    }
  }

  public void initMakeInfomation()
  {
    try
    {
      File localFile1 = new File(this.config.getString("File_PaintChat_Infomation", "./cnf/template/.paintchat"));
      File localFile2 = Io.getDirectory(localFile1);
      if (!localFile2.isDirectory())
        localFile2.mkdirs();
      int i = this.sSocket.getLocalPort();
      int j = this.config.getInt("Client_Image_Width", 1200);
      int k = this.config.getInt("Client_Image_Height", 1200);
      String str = this.config.getString("Client_Sound", "false");
      int m = this.config.getInt("layer_count", 3);
      PrintWriter localPrintWriter = new PrintWriter(new FileWriter(localFile1), false);
      localPrintWriter.println("Connection_Port_PaintChat=" + i);
      localPrintWriter.println("Client_Image_Width=" + j);
      localPrintWriter.println("Client_Image_Height=" + k);
      localPrintWriter.println("Client_Sound=" + str);
      localPrintWriter.println("layer_count=" + m);
      localPrintWriter.flush();
      localPrintWriter.close();
      File localFile3 = new File("./cnf/template/.paintchat");
      if (!Io.getDirectory(localFile3).isDirectory())
        Io.getDirectory(localFile3).mkdirs();
      if (!localFile3.equals(localFile1))
        Io.copyFile(localFile1, localFile3);
      StringWriter localStringWriter = new StringWriter();
      localStringWriter.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
      localStringWriter.write("<pchat_user_list port=\"" + i + "\" host=\"\" refresh=\"" + 15000 + "\" />\r\n");
      localStringWriter.write("<pchat_admin port=\"" + i + "\" host=\"\" />");
      localStringWriter.flush();
      localStringWriter.close();
      File localFile4 = new File(Io.getDirectory(localFile1), "pconfig.xml");
      localPrintWriter = new PrintWriter(new FileWriter(localFile4), false);
      localPrintWriter.write(localStringWriter.toString());
      localPrintWriter.flush();
      localPrintWriter.close();
      File localFile5 = new File("./cnf/template/pconfig.xml");
      if (!localFile4.equals(localFile5))
      {
        localPrintWriter = new PrintWriter(new FileWriter(localFile5));
        localPrintWriter.write(localStringWriter.toString());
        localPrintWriter.flush();
        localPrintWriter.close();
      }
    }
    catch (Throwable localThrowable)
    {
      this.debug.log(localThrowable);
    }
  }

  public boolean isExecute(boolean paramBoolean)
  {
    null;
    null;
    null;
    int i = 0;
    try
    {
      Res localRes = new Res();
      localRes.put("password", this.config.getString("Admin_Password", ""));
      LocalAdmin localLocalAdmin = new LocalAdmin(localRes, InetAddress.getLocalHost(), this.config.getInt("Connection_Port_PaintChat", Config.DEF_PORT));
      String str = localLocalAdmin.getString("ping");
      i = str.indexOf("version=") >= 0 ? 1 : 0;
      if ((i != 0) && (paramBoolean))
      {
        str = localLocalAdmin.getString("terminate");
        this.debug.log("server got terminated");
        System.exit(0);
      }
      return i;
    }
    catch (Throwable localThrowable)
    {
      if (i != 0)
        this.debug.log("server have work now.");
    }
    return i;
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      String str1 = "cnf/paintchat.cf";
      String str2 = null;
      boolean bool = false;
      if (paramArrayOfString != null)
      {
        if (paramArrayOfString.length >= 1)
        {
          str1 = paramArrayOfString[0];
          if (str1.trim().length() <= 0)
            str1 = "cnf/paintchat.cf";
        }
        if (paramArrayOfString.length >= 2)
          if (paramArrayOfString[1].equalsIgnoreCase("exit"))
            bool = true;
          else
            str2 = new String(paramArrayOfString[1]);
      }
      System.out.println(new File(str1).getCanonicalPath());
      Config localConfig = new Config(str1);
      Server localServer = new Server(str2, localConfig, null, true);
      if (localServer.isExecute(bool))
        throw new Exception("既に起動しています。");
      if (bool)
        throw new Exception("既にサーバーは終了しているか、検索を失敗しました。");
      localServer.init();
    }
    catch (Throwable localThrowable)
    {
      System.out.println("server_main" + localThrowable.toString());
      System.exit(0);
    }
  }

  public synchronized void rInit()
    throws Throwable
  {
    if (!this.live)
      return;
    Config localConfig = this.config;
    int i = localConfig.getInt("Connection_Port_PaintChat", Config.DEF_PORT);
    int j = localConfig.getInt("Connection_Max", 255);
    InetAddress.getLocalHost().getHostAddress();
    try
    {
      this.sSocket = new ServerSocket(i, j);
    }
    catch (IOException localIOException)
    {
      this.debug.log("Server error:" + localIOException.getMessage());
      throw localIOException;
    }
    i = this.sSocket.getLocalPort();
    this.textServer = new TextServer();
    this.textServer.mInit(this, this.config, this.debug);
    this.lineServer = new LineServer();
    this.lineServer.mInit(this.config, this.debug, this);
    this.talkerInstance = new TalkerInstance(this.config, this, this.textServer, this.lineServer, this.debug);
    initMakeInfomation();
    runServer();
    this.debug.log("Run ChatServer port=" + i);
  }

  public void run()
  {
    try
    {
      switch (Thread.currentThread().getName().charAt(0))
      {
      case 'c':
        runConnect();
        break;
      case 'g':
        runLive();
        break;
      case 'i':
        rInit();
      case 'd':
      case 'e':
      case 'f':
      case 'h':
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void runConnect()
    throws InterruptedException
  {
    while (this.live)
      try
      {
        Socket localSocket = this.sSocket.accept();
        localSocket.setSoTimeout(180000);
        if (!this.isLiveThread)
          synchronized (this.tLive)
          {
            this.tLive.notify();
          }
        this.talkerInstance.newTalker(localSocket);
      }
      catch (Throwable localThrowable1)
      {
        try
        {
          Thread.currentThread();
          Thread.sleep(5000L);
        }
        catch (Throwable localThrowable2)
        {
        }
      }
    exitServer();
  }

  private void runLive()
  {
    long l1 = System.currentTimeMillis();
    long l2 = l1 + 86400000L;
    long l3 = l1 + 3600000L;
    while (this.live)
      try
      {
        if (this.tLive == null)
          break;
        Thread.currentThread();
        Thread.sleep(60000L);
        if (!this.live)
          break;
        if (this.debug.bool_debug)
        {
          int i = this.textServer.getUserCount();
          int j = this.lineServer.getUserCount();
          this.debug.logSys("pchats_gc:connect t=" + i + " l=" + j);
          this.debug.logSys("ThreadSleep=" + ThreadPool.getCountOfSleeping() + " ThreadWork=" + ThreadPool.getCountOfWorking());
        }
        this.textServer.clearDeadTalker();
        if ((this.textServer.getUserCount() <= 0) && (this.lineServer.getUserCount() <= 0))
        {
          synchronized (this.tLive)
          {
            this.debug.log("pchat_gc:suspend");
            this.isLiveThread = false;
            this.tLive.wait();
            this.isLiveThread = true;
          }
          this.debug.log("pchat_gc:resume");
        }
        l1 = System.currentTimeMillis();
        if (l1 >= l3)
        {
          l3 = l1 + 3600000L;
          this.debug.log(new Date().toString());
        }
        if (l1 < l2)
          continue;
        l2 += l1 + 86400000L;
        this.debug.newLogFile(Io.getDateString("pserv_", "log", this.config.getString("Server_Log_Server_Dir", "save_server")));
        this.textServer.clearKillIP();
      }
      catch (Throwable localThrowable1)
      {
        try
        {
          this.debug.log(localThrowable1);
          Thread.currentThread();
          Thread.sleep(1000L);
        }
        catch (Throwable localThrowable2)
        {
          break;
        }
      }
    exitServer();
  }

  private void runServer()
  {
    this.tConnect = new Thread(this);
    this.tConnect.setDaemon(true);
    this.tConnect.setPriority(1);
    this.tConnect.setName("connection");
    this.tLive = new Thread(this);
    this.tLive.setDaemon(false);
    this.tLive.setPriority(1);
    this.tLive.setName("gc");
    this.tConnect.start();
    this.tLive.start();
  }

  public MgText getInfomation()
  {
    StringWriter localStringWriter = new StringWriter();
    int i = 0;
    int j = 1;
    localStringWriter.write("---------------<br><b>");
    TextTalkerListener localTextTalkerListener;
    while ((localTextTalkerListener = this.textServer.getTalkerAt(i)) != null)
    {
      i++;
      MgText localMgText = localTextTalkerListener.getHandleName();
      InetAddress localInetAddress = localTextTalkerListener.getAddress();
      LineTalker localLineTalker = this.lineServer.getTalker(localInetAddress);
      if (!localTextTalkerListener.isValidate())
        continue;
      if (j == 0)
        localStringWriter.write("<br>");
      j = 1;
      if (localMgText != null)
        localStringWriter.write(localMgText.toString() + "<br>");
      if (localInetAddress != null)
        localStringWriter.write("host=" + localInetAddress.toString() + "<br>");
      localStringWriter.write("speak=" + localTextTalkerListener.getSpeakCount() + "<br>");
      if ((localLineTalker != null) && (localLineTalker.isValidate()))
        localStringWriter.write("draw=" + localLineTalker.getDrawCount() + "<br>");
      localStringWriter.write("-----<br>");
    }
    localStringWriter.write("</b>---------------");
    localStringWriter.toString();
    return i <= 0 ? null : new MgText(0, 8, localStringWriter.toString());
  }

  public void sendClearMessage(InetAddress paramInetAddress)
  {
    TextTalkerListener localTextTalkerListener = this.textServer.getTalker(paramInetAddress);
    if (localTextTalkerListener != null)
    {
      MgText localMgText = new MgText(0, 0, localTextTalkerListener.getHandleName().getUserName() + " erased canvas.");
      localMgText.setUserName("PaintChat");
      this.textServer.addText(localTextTalkerListener, localMgText);
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.Server
 * JD-Core Version:    0.6.0
 */