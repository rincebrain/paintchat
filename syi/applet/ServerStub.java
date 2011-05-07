package syi.applet;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import paintchat.Config;
import sun.applet.AppletAudioClip;
import syi.awt.Awt;
import syi.awt.Gui;
import syi.util.PProperties;

public class ServerStub
  implements AppletContext, AppletStub
{
  private Config params;
  private Hashtable res;
  URL url_base;
  private static ServerStub default_stub = null;

  public Iterator getStreamKeys()
  {
    return null;
  }

  public ServerStub(Config paramConfig, Hashtable paramHashtable)
  {
    this.params = paramConfig;
    this.res = paramHashtable;
    try
    {
      String str = System.getProperty("user.dir");
      str = Awt.replaceText(str, "/", "\\");
      if (!str.endsWith("/"))
        str = str + '/';
      this.url_base = new URL("file:/" + str);
    }
    catch (RuntimeException localRuntimeException)
    {
    }
    catch (IOException localIOException)
    {
    }
  }

  public void appletResize(int paramInt1, int paramInt2)
  {
  }

  public Applet getApplet(String paramString)
  {
    return null;
  }

  public AppletContext getAppletContext()
  {
    return this;
  }

  public Enumeration getApplets()
  {
    return null;
  }

  public AudioClip getAudioClip(URL paramURL)
  {
    try
    {
      return new AppletAudioClip(paramURL);
    }
    catch (Throwable localThrowable)
    {
      System.out.println(localThrowable);
      try
      {
        return (AudioClip)paramURL.getContent();
      }
      catch (Exception localException)
      {
      }
    }
    return null;
  }

  public URL getCodeBase()
  {
    return this.url_base;
  }

  public static ServerStub getDefaultStub(Config paramConfig, Hashtable paramHashtable)
  {
    if (default_stub == null)
      default_stub = new ServerStub(paramConfig, paramHashtable);
    return default_stub;
  }

  public URL getDocumentBase()
  {
    return this.url_base;
  }

  public PProperties getHashTable()
  {
    return this.params;
  }

  public Image getImage(URL paramURL)
  {
    try
    {
      return (Image)paramURL.getContent();
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public String getParameter(String paramString)
  {
    return (String)this.params.get(paramString);
  }

  public boolean isActive()
  {
    return false;
  }

  public void showDocument(URL paramURL)
  {
    showDocument(paramURL, "");
  }

  public void showDocument(URL paramURL, String paramString)
  {
    Gui.showDocument(paramURL.toExternalForm(), this.params, this.res);
  }

  public void showStatus(String paramString)
  {
  }

  public InputStream getStream(String paramString)
  {
    return null;
  }

  public void setStream(String paramString, InputStream paramInputStream)
  {
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.applet.ServerStub
 * JD-Core Version:    0.6.0
 */