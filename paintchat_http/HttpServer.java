package paintchat_http;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import paintchat.Config;
import paintchat.Res;
import paintchat.Resource;
import paintchat.debug.Debug;
import syi.util.PProperties;

public class HttpServer
  implements Runnable
{
  private boolean live = true;
  private boolean isOnlyServer = false;
  public Debug debug = null;
  private ServerSocket ssocket;
  private Thread thread;
  public File dirCurrent = null;
  public File dirResource = null;
  public String dirLog;
  public static PProperties Mime = null;
  public static Config config = null;
  public static Res res = null;
  public static SimpleDateFormat fmt = null;
  public String strHost = null;
  public EOFException EOF = new EOFException();
  private HttpFiles httpFiles;
  static final String STR_FILE_MIME = "./cnf/mime.cf";
  public static final String STR_VERSION = "PaintChatHTTP/3.1";
  private static final String STR_TIME_FORMAT = "EEEE',' dd MMM yyyy HH:mm:ss 'GMT'";

  public HttpServer(Config paramConfig, Debug paramDebug, boolean paramBoolean)
  {
    res = Resource.loadResource("Http");
    this.debug = new Debug(paramDebug, res);
    config = paramConfig;
    this.isOnlyServer = paramBoolean;
  }

  public synchronized void exitServer()
  {
    if (!this.live)
      return;
    try
    {
      this.live = false;
      ServerSocket localServerSocket = this.ssocket;
      this.ssocket = null;
      localServerSocket.close();
      localServerSocket = null;
      if ((this.thread != null) && (Thread.currentThread() != this.thread))
      {
        this.thread.interrupt();
        this.thread.join();
        this.thread = null;
      }
      this.debug.log(res.get("Exit"));
    }
    catch (Throwable localThrowable)
    {
      this.debug.log(res.get("Error") + localThrowable.getMessage());
    }
    if (this.isOnlyServer)
      System.exit(0);
  }

  protected void finalize()
    throws Throwable
  {
    super.finalize();
    exitServer();
  }

  public String getLogName(String paramString1, String paramString2, String paramString3)
  {
    String str1 = null;
    File localFile1 = new File(paramString3);
    if (!localFile1.exists())
      localFile1.mkdirs();
    try
    {
      GregorianCalendar localGregorianCalendar = new GregorianCalendar(TimeZone.getDefault());
      String str2 = paramString1 + localGregorianCalendar.get(2) + '-' + localGregorianCalendar.get(5) + '_';
      for (int i = 0; i < 32767; i++)
      {
        File localFile2 = new File(paramString3, str2 + i + '.' + paramString2);
        if (localFile2.exists())
          continue;
        str1 = paramString3 + "/" + str2 + i + paramString2;
        break;
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      this.debug.log("getDate" + localRuntimeException);
    }
    return str1;
  }

  public void init(int paramInt)
  {
    try
    {
      Config localConfig = config;
      this.debug.setDebug(localConfig.getBool("Server_Debug"));
      if (localConfig.getBool("Http_Log"))
        this.debug.setFileWriter(getLogName("phttpd", "log", "save_log"));
      if (Mime == null)
      {
        Mime = new PProperties();
        Mime.load(new FileInputStream("./cnf/mime.cf"));
      }
      if (this.debug.bool_debug)
        this.debug.logDebug("Mimeロード");
      fmt = new SimpleDateFormat("EEEE',' dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
      this.dirLog = config.getString("Http_Log.Dir");
      this.dirCurrent = new File(config.getString("Http_Dir", "./www/"));
      if (!this.dirCurrent.exists())
        this.dirCurrent.mkdirs();
      this.dirResource = new File("./cnf/template/");
      this.strHost = InetAddress.getLocalHost().getHostName();
      this.httpFiles = new HttpFiles(config, res);
      this.ssocket = new ServerSocket(paramInt, 50);
      this.thread = new Thread(this);
      this.thread.setDaemon(false);
      this.thread.setPriority(1);
      this.thread.start();
      this.debug.log("Run HTTPD PORT:" + paramInt);
    }
    catch (Exception localException)
    {
      this.debug.log(res.get("Error") + localException.getMessage());
      return;
    }
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      int i = 80;
      String str = "./cnf/paintchat.cf";
      if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
      {
        try
        {
          i = Integer.parseInt(paramArrayOfString[0]);
        }
        catch (Exception localException2)
        {
          i = 80;
        }
        if (paramArrayOfString.length >= 2)
          str = paramArrayOfString[1];
      }
      Config localConfig = new Config(str);
      HttpServer localHttpServer = new HttpServer(localConfig, null, true);
      localHttpServer.init(i);
    }
    catch (Exception localException1)
    {
      System.out.println("http_main:" + localException1.toString());
    }
  }

  public void run()
  {
    long l = System.currentTimeMillis();
    label90: 
    while (this.live)
    {
      try
      {
        Socket localSocket = null;
        localSocket = this.ssocket.accept();
        localSocket.setSoTimeout(30000);
        new TalkerHttp(localSocket, this, this.httpFiles);
      }
      catch (InterruptedIOException localInterruptedIOException)
      {
        if (System.currentTimeMillis() - l <= 86400000L)
          continue;
        l = System.currentTimeMillis();
        this.debug.newLogFile(getLogName("phttpd", "log", this.dirLog));
      }
      catch (Throwable localThrowable)
      {
        if (this.live)
          break label90;
      }
      break;
      this.debug.log(res.get("Error") + localThrowable.getMessage());
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_http.HttpServer
 * JD-Core Version:    0.6.0
 */