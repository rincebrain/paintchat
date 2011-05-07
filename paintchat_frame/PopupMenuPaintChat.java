package paintchat_frame;

import java.awt.Component;
import java.awt.Container;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import paintchat.Config;
import paintchat.Res;
import paintchat.debug.Debug;
import syi.awt.LTextField;
import syi.awt.MessageBox;
import syi.util.Io;
import syi.util.PProperties;

public class PopupMenuPaintChat
  implements ActionListener
{
  private Debug debug;
  private Res res;
  private Config config;
  private LTextField textField;
  public String strSelected;
  private String strAuto;
  private String strCgi;

  public PopupMenuPaintChat(Debug paramDebug, Config paramConfig, Res paramRes)
  {
    this.debug = paramDebug;
    this.config = paramConfig;
    this.res = paramRes;
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    try
    {
      doAction(paramActionEvent.getActionCommand());
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public static String addressToString(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < 4; i++)
    {
      if (i > 0)
        localStringBuffer.append('.');
      localStringBuffer.append(paramArrayOfByte[i] & 0xFF);
    }
    return localStringBuffer.toString();
  }

  public void copyURL(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("http://");
    localStringBuffer.append(paramString);
    int i = this.config.getInt("Connection_Port_Http", 80);
    if (i != 80)
    {
      localStringBuffer.append(':');
      localStringBuffer.append(i);
    }
    localStringBuffer.append('/');
    StringSelection localStringSelection = new StringSelection(localStringBuffer.toString());
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(localStringSelection, localStringSelection);
  }

  private void doAction(String paramString)
  {
    try
    {
      if (paramString.equals(this.strAuto))
      {
        paramString = addressToString(getGlobalAddress().getAddress());
      }
      else if (paramString.equals(this.strCgi))
      {
        paramString = getAddressFromCGI();
        if ((paramString == null) || (paramString.length() <= 0))
        {
          MessageBox.alert("NotfoundCGI", "TitleOfError");
          return;
        }
      }
      this.textField.setText(paramString);
      copyURL(paramString);
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public static String getAddress(Config paramConfig, Debug paramDebug)
  {
    InetAddress localInetAddress = null;
    try
    {
      boolean bool = paramConfig.getBool("Connection_GrobalAddress");
      if (bool)
        localInetAddress = selectGlobalAddress(getGlobalAddress());
      else
        localInetAddress = InetAddress.getLocalHost();
    }
    catch (IOException localIOException1)
    {
      localInetAddress = null;
    }
    if (localInetAddress == null)
      try
      {
        localInetAddress = InetAddress.getLocalHost();
      }
      catch (IOException localIOException2)
      {
        localInetAddress = null;
      }
    if (localInetAddress == null)
    {
      paramDebug.logRes("BadAddress");
      return "127.0.0.1";
    }
    byte[] arrayOfByte = localInetAddress.getAddress();
    if (!isGlobalIP(arrayOfByte))
      paramDebug.logRes("LocalAddress");
    return addressToString(arrayOfByte);
  }

  public String getAddressFromCGI()
    throws IOException
  {
    String str = this.config.getString("App_Cgi");
    if ((str == null) || (str.length() <= 0))
      return null;
    URL localURL = new URL(str);
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(localURL.openStream());
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      while (true)
      {
        int i = Io.r(localBufferedInputStream);
        if (i == 37)
          localStringBuffer.append((char)(Character.digit((char)Io.r(localBufferedInputStream), 16) << 4 | Character.digit((char)Io.r(localBufferedInputStream), 16)));
        else
          localStringBuffer.append((char)i);
      }
    }
    catch (EOFException localEOFException)
    {
      localBufferedInputStream.close();
    }
    return localStringBuffer.toString();
  }

  private static InetAddress getGlobalAddress()
    throws IOException
  {
    InetAddress localInetAddress1 = InetAddress.getLocalHost();
    InetAddress localInetAddress2;
    try
    {
      Socket localSocket = new Socket(new URL(System.getProperty("java.vendor.url")).getHost(), 80);
      localInetAddress2 = localSocket.getLocalAddress();
      localSocket.close();
      localSocket = null;
    }
    catch (IOException localIOException)
    {
      localInetAddress2 = localInetAddress1;
    }
    return selectGlobalAddress(localInetAddress2);
  }

  public static boolean isGlobalIP(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte[0] == 127) && (paramArrayOfByte[1] == 0) && (paramArrayOfByte[2] == 0) && (paramArrayOfByte[3] == 1))
      return false;
    if (paramArrayOfByte[0] == 10)
      return false;
    if ((paramArrayOfByte[0] == 172) && (paramArrayOfByte[1] >= 16) && (paramArrayOfByte[1] < 31))
      return false;
    return (paramArrayOfByte[0] != 192) || (paramArrayOfByte[1] != 168);
  }

  private static InetAddress selectGlobalAddress(InetAddress paramInetAddress)
  {
    try
    {
      byte[] arrayOfByte = paramInetAddress.getAddress();
      if (!isGlobalIP(arrayOfByte))
      {
        InetAddress[] arrayOfInetAddress = InetAddress.getAllByName(paramInetAddress.getHostAddress());
        for (int i = 0; i < arrayOfInetAddress.length; i++)
        {
          arrayOfByte = arrayOfInetAddress[i].getAddress();
          if (isGlobalIP(arrayOfByte))
            return arrayOfInetAddress[i];
        }
      }
    }
    catch (IOException localIOException)
    {
    }
    catch (RuntimeException localRuntimeException)
    {
    }
    return paramInetAddress;
  }

  public void show(Container paramContainer, LTextField paramLTextField, int paramInt1, int paramInt2)
  {
    PopupMenu localPopupMenu = new PopupMenu();
    localPopupMenu.addActionListener(this);
    this.strAuto = this.res.get("ConnectAuto");
    this.strCgi = this.res.get("ConnectCGI");
    this.textField = paramLTextField;
    localPopupMenu.add(getAddress(this.config, this.debug));
    localPopupMenu.addSeparator();
    localPopupMenu.add(this.strAuto);
    localPopupMenu.add(this.strCgi);
    paramContainer.add(localPopupMenu);
    localPopupMenu.show(paramContainer, paramInt1, paramInt2);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_frame.PopupMenuPaintChat
 * JD-Core Version:    0.6.0
 */