package syi.applet;

import java.applet.*;
import java.awt.Image;
import java.io.*;
import java.net.URL;
import java.util.*;
import paintchat.Config;
//import sun.applet.AppletAudioClip;
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

    public ServerStub(Config config, Hashtable hashtable)
    {
        params = config;
        res = hashtable;
        try
        {
            String s = System.getProperty("user.dir");
            s = Awt.replaceText(s, "/", "\\");
            if(!s.endsWith("/"))
            {
                s = s + '/';
            }
            url_base = new URL("file:/" + s);
        }
        catch(RuntimeException _ex) { }
        catch(IOException _ex) { }
    }

    public void appletResize(int i, int j)
    {
    }

    public Applet getApplet(String s)
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

    public AudioClip getAudioClip(URL url)
    {
        try
        {
            //return new AppletAudioClip(url);
        	return null;
        }
        catch(Throwable throwable)
        {
            System.out.println(throwable);
        }
        try
        {
            return (AudioClip)url.getContent();
        }
        catch(Exception _ex)
        {
            return null;
        }
    }

    public URL getCodeBase()
    {
        return url_base;
    }

    public static ServerStub getDefaultStub(Config config, Hashtable hashtable)
    {
        if(default_stub == null)
        {
            default_stub = new ServerStub(config, hashtable);
        }
        return default_stub;
    }

    public URL getDocumentBase()
    {
        return url_base;
    }

    public PProperties getHashTable()
    {
        return params;
    }

    public Image getImage(URL url)
    {
        try
        {
            return (Image)url.getContent();
        }
        catch(Exception _ex)
        {
            return null;
        }
    }

    public String getParameter(String s)
    {
        return (String)params.get(s);
    }

    public boolean isActive()
    {
        return false;
    }

    public void showDocument(URL url)
    {
        showDocument(url, "");
    }

    public void showDocument(URL url, String s)
    {
        Gui.showDocument(url.toExternalForm(), params, res);
    }

    public void showStatus(String s)
    {
    }

    public InputStream getStream(String s)
    {
        return null;
    }

    public void setStream(String s, InputStream inputstream)
    {
    }

}
