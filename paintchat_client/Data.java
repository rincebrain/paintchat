package paintchat_client;

import java.applet.Applet;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Locale;
import paintchat.M;
import paintchat.M.Info;
import paintchat.MgText;
import paintchat.Res;
import paintchat_server.PaintChatTalker;
import syi.awt.Awt;
import syi.awt.TextPanel;
import syi.util.ByteStream;

public class Data
{
  public Pl pl;
  public Res res;
  public Res config;
  public M.Info info;
  private boolean isDestroy = false;
  private int Port;
  private InetAddress address;
  private int ID = 0;
  private M mgDraw = new M();
  public Mi mi;
  private TextPanel text;
  public int imW;
  public int imH;
  public int MAX_KAIWA;
  public int MAX_KAIWA_BORDER;
  private EOFException EOF = new EOFException();
  private TLine tLine;
  private TText tText;
  public String strName = null;

  public Data(Pl paramPl)
  {
    this.pl = paramPl;
  }

  public synchronized void destroy()
  {
    if (this.isDestroy)
      return;
    this.isDestroy = true;
    try
    {
      if (this.tLine != null)
      {
        this.tLine.mRStop();
        this.tLine = null;
      }
      if (this.tText != null)
      {
        this.tText.mRStop();
        this.tText = null;
      }
    }
    catch (Throwable localThrowable)
    {
    }
  }

  private Socket getSocket()
  {
    if (this.address == null)
    {
      InetAddress localInetAddress = null;
      String str1 = this.config.getP("Connection_Host", null);
      try
      {
        if (str1 != null)
          localInetAddress = InetAddress.getByName(str1);
      }
      catch (UnknownHostException localUnknownHostException1)
      {
        localInetAddress = null;
      }
      try
      {
        str1 = this.pl.applet.getCodeBase().getHost();
        if ((str1 == null) || (str1.length() <= 0))
          str1 = "localhost";
        localInetAddress = InetAddress.getByName(str1);
      }
      catch (UnknownHostException localUnknownHostException2)
      {
        localInetAddress = null;
      }
      if (localInetAddress == null)
      {
        destroy();
        return null;
      }
      this.address = localInetAddress;
      String str2 = "Connection_Port_PaintChat";
      this.Port = this.config.getP(str2, 41411);
    }
    try
    {
      while (!this.isDestroy)
      {
        int i = 0;
        while (i < 2)
          try
          {
            return new Socket(this.address, this.Port);
          }
          catch (IOException localIOException)
          {
            Thread.currentThread();
            Thread.sleep(3000L);
            i++;
          }
        if (!this.mi.alert("reconnect", true))
          break;
      }
    }
    catch (InterruptedException localInterruptedException)
    {
    }
    destroy();
    return null;
  }

  public void init()
  {
    try
    {
      ByteStream localByteStream = new ByteStream();
      Applet localApplet = this.pl.applet;
      URL localURL1 = localApplet.getCodeBase();
      String str1 = p("dir_resource", "./res");
      if (!str1.endsWith("/"))
        str1 = str1 + '/';
      URL localURL2 = new URL(localURL1, str1);
      this.res = new Res(localApplet, localURL2, localByteStream);
      this.config = new Res(localApplet, localURL2, localByteStream);
      this.config.loadZip(p("res.zip", "res.zip"));
      try
      {
        String str2 = "param_utf8.txt";
        this.config.load(new String((byte[])this.config.getRes(str2), "UTF8"));
        this.config.remove(str2);
      }
      catch (IOException localIOException2)
      {
        localIOException2.printStackTrace();
      }
      Me.res = this.res;
      Me.conf = this.config;
      this.pl.iPG(true);
      try
      {
        this.config.load(Awt.openStream(new URL(localURL1, this.config.getP("File_PaintChat_Infomation", this.config.getP("server", ".paintchat")))));
      }
      catch (IOException localIOException3)
      {
        System.out.println(localIOException3);
      }
      this.pl.iPG(true);
      this.res.loadResource(this.config, "res", Locale.getDefault().getLanguage());
      this.pl.iPG(true);
      this.MAX_KAIWA_BORDER = this.config.getP("Cash_Text_Max", 120);
      this.imW = this.config.getP("Client_Image_Width", this.config.getP("image_width", 1200));
      this.imH = this.config.getP("Client_Image_Height", this.config.getP("image_height", 1200));
    }
    catch (IOException localIOException1)
    {
      localIOException1.printStackTrace();
    }
  }

  public void send(M paramM)
  {
    this.tLine.send(paramM);
  }

  public void send(MgText paramMgText)
  {
    this.tText.send(paramMgText);
  }

  public void start()
    throws IOException
  {
    Mi localMi = this.mi;
    this.info = localMi.info;
    this.mgDraw.setInfo(localMi.info);
    this.mgDraw.newUser(localMi).wait = -1;
    Res localRes = new Res();
    localRes.put("name", this.strName);
    localRes.put("password", this.config.get("chat_password"));
    this.config.put(localRes);
    mRunText(localRes);
    mRunLine(localRes);
  }

  private void mRunLine(Res paramRes)
    throws IOException
  {
    Res localRes = new Res();
    localRes.put(paramRes);
    localRes.put("protocol", "paintchat.line");
    this.tLine = new TLine(this, this.mgDraw);
    Socket localSocket = getSocket();
    this.tLine.mConnect(localSocket, localRes);
  }

  private void mRunText(Res paramRes)
    throws IOException
  {
    Res localRes = new Res();
    localRes.put(paramRes);
    localRes.put("protocol", "paintchat.text");
    this.tText = new TText(this.pl, this);
    Socket localSocket = getSocket();
    this.tText.mConnect(localSocket, localRes);
  }

  public String p(String paramString1, String paramString2)
  {
    try
    {
      String str = this.pl.applet.getParameter(paramString1);
      if ((str == null) || (str.length() <= 0))
        return paramString2;
      return str;
    }
    catch (Throwable localThrowable)
    {
    }
    return paramString2;
  }

  public void addTextComp()
  {
    this.pl.addTextInfo(this.res.get("log_complete"), true);
    mPermission(this.tLine.getStatus().get("permission"));
  }

  public void mPermission(String paramString)
  {
    int i = 0;
    int k = paramString.length();
    int j;
    do
    {
      j = paramString.indexOf(';', i);
      if (j < 0)
        j = k;
      if (j - i > 0)
        mP(paramString.substring(i, j));
      i = j + 1;
    }
    while (j < k);
  }

  private void mP(String paramString)
  {
    try
    {
      int i = paramString.indexOf(':');
      if (i <= 0)
        return;
      String str1 = paramString.substring(0, i).trim();
      String str2 = paramString.substring(i + 1).trim();
      boolean bool = false;
      if (str2.length() > 0)
        bool = str2.charAt(0) == 't';
      if (str1.equals("layer"))
        this.info.permission = (str2.equals("all") ? -1 : Integer.parseInt(str2));
      if (str1.equals("layer_edit"))
        this.info.isLEdit = bool;
      if (str1.equals("canvas"))
        this.mi.isEnable = bool;
      if (str1.equals("fill"))
        this.info.isFill = bool;
      if (str1.equals("clean"))
        this.info.isClean = bool;
      if (str1.equals("unlayer"))
        this.info.unpermission = Integer.parseInt(str2);
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_client.Data
 * JD-Core Version:    0.6.0
 */