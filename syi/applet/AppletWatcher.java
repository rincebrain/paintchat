package syi.applet;

import java.applet.Applet;
import java.awt.*;
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

// Referenced classes of package syi.applet:
//            ServerStub

public class AppletWatcher extends Frame
{

    private Applet applet;
    private boolean bool_exit;

    public AppletWatcher(String s, String s1, Config config, Hashtable hashtable, boolean flag)
        throws ClassNotFoundException, Exception, IOException
    {
        super(s1);
        enableEvents(64L);
        bool_exit = flag;
        setLayout(new BorderLayout());
        applet = (Applet)Class.forName(s).newInstance();
        applet.setStub(ServerStub.getDefaultStub(config, hashtable));
        add(applet, "Center");
        applet.init();
        Gui.getDefSize(this);
        Awt.moveCenter(this);
    }

    private PProperties getProperties(URL url)
    {
        try
        {
            boolean flag = false;
            URLConnection urlconnection = url.openConnection();
            urlconnection.connect();
            BufferedInputStream bufferedinputstream = new BufferedInputStream(urlconnection.getInputStream());
            int i = urlconnection.getContentLength();
            int j = 0;
            char ac[] = new char[i];
            for(; j < i; j++)
            {
                int l = bufferedinputstream.read();
                switch(l)
                {
                case -1: 
                    ac[j] = '\0';
                    i = 0;
                    break;

                case 9: // '\t'
                case 10: // '\n'
                case 13: // '\r'
                case 32: // ' '
                    if(!flag)
                    {
                        flag = true;
                        ac[j] = (char)l;
                    }
                    break;

                default:
                    flag = false;
                    ac[j] = (char)l;
                    break;
                }
            }

            bufferedinputstream.close();
            bufferedinputstream = null;
            i = j;
            flag = false;
            int i1 = 0;
            int j1 = 0;
            char ac1[] = {
                'p', 'a', 'r', 'a', 'm'
            };
            char ac2[] = {
                'a', 'p', 'p', 'l', 'e', 't'
            };
            for(int k = 0; k < i; k++)
            {
                if(ac[k] == '<')
                {
                    for(k++; k < i; k++)
                    {
                        if(ac[k] == ' ')
                        {
                            if(flag)
                            {
                                break;
                            }
                        } else
                        {
                            flag = true;
                            char c = Character.toLowerCase(ac[k]);
                            i1 = ac1[i1] != c ? 0 : i1 + 1;
                            j1 = ac2[i1] != c ? 0 : j1 + 1;
                        }
                    }

                }
            }

        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return null;
    }

    public static void main(String args[])
    {
    }

    protected void processWindowEvent(WindowEvent windowevent)
    {
        try
        {
            int i = windowevent.getID();
            Window window = windowevent.getWindow();
            switch(i)
            {
            default:
                break;

            case 201: 
                window.dispose();
                applet.stop();
                break;

            case 202: 
                applet.destroy();
                applet = null;
                if(bool_exit)
                {
                    System.exit(0);
                }
                break;

            case 200: 
                applet.start();
                break;
            }
        }
        catch(Throwable _ex) { }
    }
}
