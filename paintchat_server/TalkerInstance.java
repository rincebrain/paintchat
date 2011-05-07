package paintchat_server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Hashtable;
import paintchat.Config;
import paintchat.Res;
import paintchat.debug.DebugListener;
import syi.util.ByteInputStream;
import syi.util.Io;
import syi.util.PProperties;
import syi.util.ThreadPool;
import syi.util.Vector2;

public class TalkerInstance
  implements Runnable
{
  private Socket socket;
  private InputStream In;
  private OutputStream Out;
  private TextServer textServer;
  private LineServer lineServer;
  private DebugListener debug;
  private Config serverStatus;
  private Server server;
  private String strPassword;
  boolean isAscii = false;

  public TalkerInstance(Config paramConfig, Server paramServer, TextServer paramTextServer, LineServer paramLineServer, DebugListener paramDebugListener)
  {
    this.server = paramServer;
    this.textServer = paramTextServer;
    this.lineServer = paramLineServer;
    this.serverStatus = paramConfig;
    this.debug = paramDebugListener;
    this.strPassword = paramConfig.getString("Admin_Password", "");
  }

  public void run()
  {
    try
    {
      switchConnection(this.socket);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
      closeSocket();
    }
  }

  public void newTalker(Socket paramSocket)
  {
    TalkerInstance localTalkerInstance = new TalkerInstance(this.serverStatus, this.server, this.textServer, this.lineServer, this.debug);
    localTalkerInstance.socket = paramSocket;
    ThreadPool.poolStartThread(localTalkerInstance, "sock_switch");
  }

  private boolean isKillAddress(Socket paramSocket)
  {
    try
    {
      InetAddress localInetAddress1 = paramSocket.getInetAddress();
      Vector2 localVector2 = this.textServer.vKillIP;
      for (int i = 0; i < localVector2.size(); i++)
      {
        InetAddress localInetAddress2 = ((PaintChatTalker)localVector2.get(i)).getAddress();
        if (localInetAddress2.equals(localInetAddress1))
          return true;
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      this.debug.log(localRuntimeException);
    }
    return false;
  }

  private void switchConnection(Socket paramSocket)
    throws IOException
  {
    ByteInputStream localByteInputStream = new ByteInputStream();
    InputStream localInputStream = paramSocket.getInputStream();
    this.In = localInputStream;
    this.isAscii = (Io.r(localInputStream) != 98);
    if (this.isAscii)
    {
      switchAsciiConnection();
      return;
    }
    int i = Io.readUShort(localInputStream);
    if (i <= 0)
      throw new IOException("protocol unknown");
    byte[] arrayOfByte = new byte[i];
    Io.rFull(localInputStream, arrayOfByte, 0, i);
    Res localRes = new Res();
    localByteInputStream.setBuffer(arrayOfByte, 0, i);
    localRes.load(localByteInputStream);
    if (localRes.getBool("local_admin"))
    {
      switchLocalAdmin(paramSocket, localRes);
      return;
    }
    Object localObject = null;
    String str = localRes.get("protocol", "");
    if (str.equals("paintchat.text"))
      localObject = new TextTalker(this.textServer, this.debug);
    if (str.equals("paintchat.line"))
      localObject = new LineTalker(this.lineServer, this.debug);
    str.equals("paintchat.infomation");
    if (localObject != null)
    {
      if ((this.strPassword.length() > 0) && (this.strPassword.equals(localRes.get("password", ""))))
        localRes.put("permission", "layer:all;canvas:true;talk:true;layer_edit:true;fill:true;clean:true;");
      else
        localRes.put("permission", this.serverStatus.get("Client_Permission"));
      ((PaintChatTalker)localObject).mStart(this.socket, localInputStream, null, localRes);
    }
  }

  private void switchAsciiConnection()
    throws IOException
  {
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(this.In);
    StringBuffer localStringBuffer = new StringBuffer();
    int j = 0;
    int i;
    while ((i = Io.r(localBufferedInputStream)) != -1)
    {
      localStringBuffer.append((char)i);
      if (i == 0)
        break;
      j++;
      if (j < 255)
        continue;
      throw new IOException("abnormal");
    }
    String str1 = localStringBuffer.toString();
    String str2 = null;
    int k = str1.indexOf("type=");
    if (k <= 0)
    {
      str2 = "paintchat.text";
    }
    else
    {
      k += 6;
      int m = str1.indexOf('"', k);
      if (m == -1)
        m = str1.indexOf('\'', k);
      str2 = m == -1 ? "paintchat.text" : str1.substring(k, m);
    }
    if (str2.equals("paintchat.infomation"))
    {
      switchLocalAdminXML(str1);
      return;
    }
    XMLTextTalker localXMLTextTalker = new XMLTextTalker(this.textServer, this.debug);
    if (localXMLTextTalker != null)
      localXMLTextTalker.mStart(this.socket, localBufferedInputStream, null, null);
  }

  private void switchLocalAdmin(Socket paramSocket, Res paramRes)
  {
    try
    {
      String str1 = this.serverStatus.getString("Admin_Password");
      if ((paramSocket.getInetAddress().equals(InetAddress.getLocalHost())) || ((str1 != null) && (str1.length() > 0) && (str1.equals(paramRes.get("password", "")))))
      {
        String str2 = paramRes.get("request", "ping");
        StringBuffer localStringBuffer = new StringBuffer();
        byte[] arrayOfByte1 = (byte[])null;
        if (str2.equals("ping"))
        {
          localStringBuffer.append("response=ping ok\n");
          localStringBuffer.append("version=");
          localStringBuffer.append("(C)しぃちゃん PaintChatServer v3.57b");
          localStringBuffer.append("\n");
          arrayOfByte1 = localStringBuffer.toString().getBytes("UTF8");
        }
        if (str2.equals("exit"))
        {
          localStringBuffer.append("response=exit ok\n");
          byte[] arrayOfByte2 = localStringBuffer.toString().getBytes("UTF8");
          this.Out = this.socket.getOutputStream();
          Io.wShort(this.Out, arrayOfByte2.length);
          this.Out.write(arrayOfByte2);
          this.Out.flush();
          closeSocket();
          this.server.exitServer();
          return;
        }
        if (arrayOfByte1 != null)
        {
          this.Out = this.socket.getOutputStream();
          Io.wShort(this.Out, arrayOfByte1.length);
          this.Out.write(arrayOfByte1);
          this.Out.flush();
        }
      }
    }
    catch (IOException localIOException)
    {
      this.debug.log(localIOException);
    }
    closeSocket();
  }

  private void switchLocalAdminXML(String paramString)
    throws IOException
  {
    int i = paramString.indexOf("request=");
    if (i < 0)
    {
      closeSocket();
      return;
    }
    i += 9;
    int j = paramString.indexOf('"', i);
    if (j < 0)
    {
      j = paramString.indexOf('\'', i);
      if (j < 0)
      {
        closeSocket();
        return;
      }
    }
    if (i == j)
      return;
    String str = paramString.substring(i, j);
    if (str.equals("userlist"))
    {
      this.Out = this.socket.getOutputStream();
      StringBuffer localStringBuffer = new StringBuffer();
      this.textServer.getUserListXML(localStringBuffer);
      localStringBuffer.append('\000');
      this.Out.write(localStringBuffer.toString().getBytes("UTF8"));
      closeSocket();
      return;
    }
    if (str.equals("infomation"))
    {
      this.Out = this.socket.getOutputStream();
      closeSocket();
      return;
    }
    closeSocket();
  }

  private void closeSocket()
  {
    try
    {
      if (this.In != null)
      {
        this.In.close();
        this.In = null;
      }
      if (this.Out != null)
      {
        this.Out.close();
        this.Out = null;
      }
      if (this.socket != null)
      {
        this.socket.close();
        this.socket = null;
      }
    }
    catch (IOException localIOException)
    {
      this.debug.log(localIOException);
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.TalkerInstance
 * JD-Core Version:    0.6.0
 */