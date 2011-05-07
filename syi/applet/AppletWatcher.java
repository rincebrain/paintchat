package syi.applet;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import paintchat.Config;
import syi.awt.Awt;
import syi.awt.Gui;
import syi.util.PProperties;

public class AppletWatcher extends Frame
{
  private Applet applet;
  private boolean bool_exit;

  public AppletWatcher(String paramString1, String paramString2, Config paramConfig, Hashtable paramHashtable, boolean paramBoolean)
    throws ClassNotFoundException, Exception, IOException
  {
    super(paramString2);
    enableEvents(64L);
    this.bool_exit = paramBoolean;
    setLayout(new BorderLayout());
    this.applet = ((Applet)Class.forName(paramString1).newInstance());
    this.applet.setStub(ServerStub.getDefaultStub(paramConfig, paramHashtable));
    add(this.applet, "Center");
    this.applet.init();
    Gui.getDefSize(this);
    Awt.moveCenter(this);
  }

  private PProperties getProperties(URL paramURL)
  {
    try
    {
      int i = 0;
      URLConnection localURLConnection = paramURL.openConnection();
      localURLConnection.connect();
      BufferedInputStream localBufferedInputStream = new BufferedInputStream(localURLConnection.getInputStream());
      int j = localURLConnection.getContentLength();
      int k = 0;
      char[] arrayOfChar1 = new char[j];
      while (k < j)
      {
        m = localBufferedInputStream.read();
        switch (m)
        {
        case -1:
          arrayOfChar1[k] = '\000';
          j = 0;
          break;
        case 9:
        case 10:
        case 13:
        case 32:
          if (i != 0)
            break;
          i = 1;
          arrayOfChar1[k] = (char)m;
          break;
        default:
          i = 0;
          arrayOfChar1[k] = (char)m;
        }
        k++;
      }
      localBufferedInputStream.close();
      localBufferedInputStream = null;
      j = k;
      i = 0;
      int m = 0;
      int n = 0;
      char[] arrayOfChar2 = { 'p', 'a', 'r', 'a', 'm' };
      char[] arrayOfChar3 = { 'a', 'p', 'p', 'l', 'e', 't' };
      for (k = 0; k < j; k++)
      {
        if (arrayOfChar1[k] != '<')
          continue;
        k++;
        while (k < j)
        {
          if (arrayOfChar1[k] == ' ')
          {
            if (i != 0)
              break;
          }
          else
          {
            i = 1;
            int i1 = Character.toLowerCase(arrayOfChar1[k]);
            m = arrayOfChar2[m] == i1 ? m + 1 : 0;
            n = arrayOfChar3[m] == i1 ? n + 1 : 0;
          }
          k++;
        }
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return null;
  }

  public static void main(String[] paramArrayOfString)
  {
  }

  protected void processWindowEvent(WindowEvent paramWindowEvent)
  {
    try
    {
      int i = paramWindowEvent.getID();
      Window localWindow = paramWindowEvent.getWindow();
      switch (i)
      {
      case 201:
        localWindow.dispose();
        this.applet.stop();
        break;
      case 202:
        this.applet.destroy();
        this.applet = null;
        if (!this.bool_exit)
          break;
        System.exit(0);
        break;
      case 200:
        this.applet.start();
      }
    }
    catch (Throwable localThrowable)
    {
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.applet.AppletWatcher
 * JD-Core Version:    0.6.0
 */