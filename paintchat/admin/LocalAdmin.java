package paintchat.admin;

import B;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import paintchat.Res;
import syi.util.Io;

public class LocalAdmin
{
  private Res status;
  private InetAddress addr;
  private int iPort;
  private byte[] bRes;

  public LocalAdmin(Res paramRes, InetAddress paramInetAddress, int paramInt)
  {
    this.status = paramRes;
    if (paramRes != null)
      paramRes.put("local_admin", "t");
    this.addr = paramInetAddress;
    this.iPort = (paramInt <= 0 ? 41411 : paramInt);
  }

  private void doConnect(String paramString)
  {
    InputStream localInputStream = null;
    OutputStream localOutputStream = null;
    Socket localSocket = null;
    this.bRes = null;
    try
    {
      StringBuffer localStringBuffer = new StringBuffer();
      Object localObject = this.status.keys();
      while (((Enumeration)localObject).hasMoreElements())
      {
        String str = ((Enumeration)localObject).nextElement().toString();
        if (str.length() <= 0)
          continue;
        localStringBuffer.append(str);
        localStringBuffer.append('=');
        localStringBuffer.append(this.status.get(str));
        localStringBuffer.append('\n');
      }
      localStringBuffer.append("request=");
      localStringBuffer.append(paramString);
      localObject = localStringBuffer.toString().getBytes("UTF8");
      localStringBuffer = null;
      localSocket = new Socket(this.addr, this.iPort);
      localInputStream = localSocket.getInputStream();
      localOutputStream = localSocket.getOutputStream();
      localOutputStream.write(98);
      Io.wShort(localOutputStream, localObject.length);
      localOutputStream.write(localObject);
      localOutputStream.flush();
      localObject = (byte[])null;
      int i = Io.readUShort(localInputStream);
      if (i > 0)
      {
        localObject = new byte[i];
        Io.rFull(localInputStream, localObject, 0, i);
      }
      try
      {
        localInputStream.close();
        localOutputStream.close();
        localSocket.close();
      }
      catch (IOException localIOException1)
      {
      }
      this.bRes = ((B)localObject);
    }
    catch (IOException localIOException2)
    {
    }
  }

  public String getString(String paramString)
  {
    try
    {
      doConnect(paramString);
      return (this.bRes != null) && (this.bRes.length > 0) ? new String(this.bRes, "UTF8") : "";
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return "";
  }

  public byte[] getBytes(String paramString)
  {
    try
    {
      doConnect(paramString);
      return this.bRes;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.admin.LocalAdmin
 * JD-Core Version:    0.6.0
 */