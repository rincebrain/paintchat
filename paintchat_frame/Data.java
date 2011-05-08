package paintchat_frame;

import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.PrintStream;
import java.util.EventObject;
import java.util.Hashtable;
import paintchat.Config;
import paintchat.Res;
import paintchat.debug.Debug;
import paintchat_http.HttpServer;
import paintchat_server.Server;
import syi.applet.ServerStub;
import syi.awt.*;
import syi.util.*;

// Referenced classes of package paintchat_frame:
//            ConfigDialog

public class Data
    implements Runnable, ActionListener
{

    protected transient PropertyChangeSupport propertyChange;
    private Res res;
    private Config config;
    private Debug debug;
    private boolean fieldIsNativeWindows;
    private Server server;
    private HttpServer http;
    private Applet lobby;
    private LButton bChat;
    private LButton bHttp;
    private LButton bLobby;

    public Data()
    {
        res = null;
        config = null;
        debug = null;
        fieldIsNativeWindows = false;
        server = null;
        http = null;
        lobby = null;
        File file = new File("cnf/temp.tmp");
        setIsNativeWindows(file.isFile());
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        try
        {
            Object obj = actionevent.getSource();
            ThreadPool.poolStartThread(this, obj != bChat ? ((char) (obj != bHttp ? 'l' : 'h')) : 'c');
        }
        catch(Throwable throwable)
        {
            debug.log(throwable.getMessage());
        }
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertychangelistener)
    {
        getPropertyChange().addPropertyChangeListener(propertychangelistener);
    }

    public boolean exitWin()
    {
        try
        {
            File file = new File(System.getProperty("user.dir"));
            File file1 = new File(file, "cnf\\temp.tmp");
            if(!file1.isFile())
            {
                return false;
            }
            file1 = new File(file, "PaintChat.exe");
            if(!file1.isFile())
            {
                return false;
            }
            Process process = Runtime.getRuntime().exec(file1.getCanonicalPath() + " 1");
            process.waitFor();
        }
        catch(Throwable throwable)
        {
            System.out.println("win" + throwable);
        }
        return true;
    }

    public void firePropertyChange(String s, Object obj, Object obj1)
    {
        getPropertyChange().firePropertyChange(s, obj, obj1);
    }

    public boolean getIsNativeWindows()
    {
        return fieldIsNativeWindows;
    }

    protected PropertyChangeSupport getPropertyChange()
    {
        if(propertyChange == null)
        {
            propertyChange = new PropertyChangeSupport(this);
        }
        return propertyChange;
    }

    public void init(Config config1, Res res1, Debug debug1, LButton lbutton, LButton lbutton1, LButton lbutton2)
    {
        config = config1;
        res = res1;
        debug = debug1;
        bChat = lbutton;
        bHttp = lbutton1;
        bLobby = lbutton2;
        lbutton.addActionListener(this);
        lbutton1.addActionListener(this);
        lbutton2.addActionListener(this);
        if(config1.getBool("App_Auto_Http"))
        {
            ThreadPool.poolStartThread(this, 'h');
        } else
        {
            lbutton1.setText(res1.get("Http_Button_Start"));
        }
        if(config1.getBool("App_Auto_Paintchat"))
        {
            ThreadPool.poolStartThread(this, 'c');
        } else
        {
            lbutton.setText(res1.get("Paintchat_Button_Start"));
        }
    }

    public boolean isRunPaintChatServer()
    {
        return server != null;
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertychangelistener)
    {
        getPropertyChange().removePropertyChangeListener(propertychangelistener);
    }

    public void run()
    {
        switch(Thread.currentThread().getName().charAt(0))
        {
        case 99: // 'c'
            runPaintChat();
            break;

        case 104: // 'h'
            runHttp();
            break;

        case 108: // 'l'
            runLobby();
            break;
        }
    }

    private synchronized void runHttp()
    {
        if(http == null)
        {
            http = new HttpServer(config, debug, false);
            http.init(config.getInt("Connection_Port_Http", 80));
            bHttp.setText(res.get("Http_Button_Stop"));
        } else
        {
            http.exitServer();
            http = null;
            bHttp.setText(res.get("Http_Button_Start"));
        }
    }

    private synchronized void runLobby()
    {
        String s = "chatIndex";
        if(lobby != null)
        {
            if(!MessageBox.confirm("LobbyDisconnect", "(C)And Mino, PaintChatApp v3.66"))
            {
                return;
            } else
            {
                Applet applet = lobby;
                lobby = null;
                applet.stop();
                debug.log(res.get("LobbyOut"));
                return;
            }
        }
        if(config.getInt(s) == 0)
        {
            try
            {
                new ConfigDialog("paintchat.config.Ao", "cnf/dialogs.jar", config, res, "(C)And Mino, PaintChatApp v3.66");
            }
            catch(Exception exception)
            {
                debug.log(exception.getMessage());
            }
            if(config.getInt(s) == 0)
            {
                debug.log("LobbyCancel");
                return;
            }
        }
        try
        {
            int i = "(C)And Mino, PaintChatApp v3.66".lastIndexOf('v') + 1;
            config.put("pchatVersion", "(C)And Mino, PaintChatApp v3.66".substring(i, i + 4));
            lobby = (Applet)(new ClassLoaderCustom()).loadClass("lobbyusers.Users", "cnf", true).newInstance();
            lobby.setStub(ServerStub.getDefaultStub(config, res));
            lobby.init();
            lobby.start();
            debug.log(res.get("LobbyIn"));
            if(config.getBool("ao_show_html"))
            {
                Gui.showDocument("http://ax.sakura.ne.jp/~aotama/pchat/LobbyRoom.html", config, res);
            }
        }
        catch(Exception exception1)
        {
            debug.log(exception1.getMessage());
        }
    }

    private synchronized void runPaintChat()
    {
        if(server == null)
        {
            server = new Server(config.getString("Admin_Password"), config, debug, false);
            server.init();
            bChat.setText(res.get("Paintchat_Button_Stop"));
            if(config.getBool("App_Auto_Lobby", true))
            {
                startLobby(false);
            }
        } else
        {
            server.exitServer();
            server = null;
            bChat.setText(res.get("Paintchat_Button_Start"));
            if(lobby != null)
            {
                runLobby();
            }
        }
    }

    public void setIsNativeWindows(boolean flag)
    {
        fieldIsNativeWindows = flag;
    }

    public void startHttp(boolean flag)
    {
        if(flag)
        {
            ThreadPool.poolStartThread(this, 'h');
        } else
        {
            runHttp();
        }
    }

    public void startLobby(boolean flag)
    {
        if(flag)
        {
            ThreadPool.poolStartThread(this, 'l');
        } else
        {
            runLobby();
        }
    }

    public void startPaintChat(boolean flag)
    {
        if(flag)
        {
            ThreadPool.poolStartThread(this, 'c');
        } else
        {
            runPaintChat();
        }
    }
}
