/* Data - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_frame;
import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import paintchat.Config;
import paintchat.Res;
import paintchat.debug.Debug;

import paintchat_http.HttpServer;

import paintchat_server.Server;

import syi.applet.ServerStub;
import syi.awt.Gui;
import syi.awt.LButton;
import syi.awt.MessageBox;
import syi.util.ClassLoaderCustom;
import syi.util.ThreadPool;

public class Data implements Runnable, ActionListener
{
    protected transient PropertyChangeSupport propertyChange;
    private Res res = null;
    private Config config = null;
    private Debug debug = null;
    private boolean fieldIsNativeWindows = false;
    private Server server = null;
    private HttpServer http = null;
    private Applet lobby = null;
    private LButton bChat;
    private LButton bHttp;
    private LButton bLobby;
    
    public Data() {
	File file = new File("cnf/temp.tmp");
	setIsNativeWindows(file.isFile());
    }
    
    public void actionPerformed(ActionEvent actionevent) {
	try {
	    Object object = actionevent.getSource();
	    ThreadPool.poolStartThread(this, (object == bChat ? 'c'
					      : object == bHttp ? 'h' : 'l'));
	} catch (Throwable throwable) {
	    debug.log(throwable.getMessage());
	}
    }
    
    public synchronized void addPropertyChangeListener
	(PropertyChangeListener propertychangelistener) {
	getPropertyChange().addPropertyChangeListener(propertychangelistener);
    }
    
    public boolean exitWin() {
	try {
	    File file = new File(System.getProperty("user.dir"));
	    File file_0_ = new File(file, "cnf\\temp.tmp");
	    if (!file_0_.isFile())
		return false;
	    file_0_ = new File(file, "PaintChat.exe");
	    if (!file_0_.isFile())
		return false;
	    Process process
		= Runtime.getRuntime().exec(file_0_.getCanonicalPath() + " 1");
	    process.waitFor();
	} catch (Throwable throwable) {
	    System.out.println("win" + throwable);
	}
	return true;
    }
    
    public void firePropertyChange(String string, Object object,
				   Object object_1_) {
	getPropertyChange().firePropertyChange(string, object, object_1_);
    }
    
    public boolean getIsNativeWindows() {
	return fieldIsNativeWindows;
    }
    
    protected PropertyChangeSupport getPropertyChange() {
	if (propertyChange == null)
	    propertyChange = new PropertyChangeSupport(this);
	return propertyChange;
    }
    
    public void init(Config config, Res res, Debug debug, LButton lbutton,
		     LButton lbutton_2_, LButton lbutton_3_) {
	this.config = config;
	this.res = res;
	this.debug = debug;
	bChat = lbutton;
	bHttp = lbutton_2_;
	bLobby = lbutton_3_;
	lbutton.addActionListener(this);
	lbutton_2_.addActionListener(this);
	lbutton_3_.addActionListener(this);
	if (config.getBool("App_Auto_Http"))
	    ThreadPool.poolStartThread(this, 'h');
	else
	    lbutton_2_.setText(res.get("Http_Button_Start"));
	if (config.getBool("App_Auto_Paintchat"))
	    ThreadPool.poolStartThread(this, 'c');
	else
	    lbutton.setText(res.get("Paintchat_Button_Start"));
    }
    
    public boolean isRunPaintChatServer() {
	if (server != null)
	    return true;
	return false;
    }
    
    public synchronized void removePropertyChangeListener
	(PropertyChangeListener propertychangelistener) {
	getPropertyChange()
	    .removePropertyChangeListener(propertychangelistener);
    }
    
    public void run() {
	switch (Thread.currentThread().getName().charAt(0)) {
	case 'c':
	    runPaintChat();
	    break;
	case 'h':
	    runHttp();
	    break;
	case 'l':
	    runLobby();
	    break;
	}
    }
    
    private synchronized void runHttp() {
	if (http == null) {
	    http = new HttpServer(config, debug, false);
	    http.init(config.getInt("Connection_Port_Http", 80));
	    bHttp.setText(res.get("Http_Button_Stop"));
	} else {
	    http.exitServer();
	    http = null;
	    bHttp.setText(res.get("Http_Button_Start"));
	}
    }
    
    private synchronized void runLobby() {
	String string = "chatIndex";
	if (lobby != null) {
	    if (MessageBox.confirm
		("LobbyDisconnect",
		 "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66")) {
		Applet applet = lobby;
		lobby = null;
		applet.stop();
		debug.log(res.get("LobbyOut"));
	    }
	} else {
	    if (config.getInt(string) == 0) {
		try {
		    new ConfigDialog
			("paintchat.config.Ao", "cnf/dialogs.jar", config, res,
			 "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66");
		} catch (Exception exception) {
		    debug.log(exception.getMessage());
		}
		if (config.getInt(string) == 0) {
		    debug.log("LobbyCancel");
		    return;
		}
	    }
	    try {
		int i = ("(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66"
			     .lastIndexOf('v')
			 + 1);
		config.put
		    ("pchatVersion",
		     "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66"
			 .substring(i, i + 4));
		lobby = (Applet) new ClassLoaderCustom().loadClass
				     ("lobbyusers.Users", "cnf", true)
				     .newInstance();
		lobby.setStub(ServerStub.getDefaultStub(config, res));
		lobby.init();
		lobby.start();
		debug.log(res.get("LobbyIn"));
		if (config.getBool("ao_show_html"))
		    Gui.showDocument
			("http://ax.sakura.ne.jp/~aotama/pchat/LobbyRoom.html",
			 config, res);
	    } catch (Exception exception) {
		debug.log(exception.getMessage());
	    }
	}
    }
    
    private synchronized void runPaintChat() {
	if (server == null) {
	    server = new Server(config.getString("Admin_Password"), config,
				debug, false);
	    server.init();
	    bChat.setText(res.get("Paintchat_Button_Stop"));
	    if (config.getBool("App_Auto_Lobby", true))
		startLobby(false);
	} else {
	    server.exitServer();
	    server = null;
	    bChat.setText(res.get("Paintchat_Button_Start"));
	    if (lobby != null)
		runLobby();
	}
    }
    
    public void setIsNativeWindows(boolean bool) {
	fieldIsNativeWindows = bool;
    }
    
    public void startHttp(boolean bool) {
	if (bool)
	    ThreadPool.poolStartThread(this, 'h');
	else
	    runHttp();
    }
    
    public void startLobby(boolean bool) {
	if (bool)
	    ThreadPool.poolStartThread(this, 'l');
	else
	    runLobby();
    }
    
    public void startPaintChat(boolean bool) {
	if (bool)
	    ThreadPool.poolStartThread(this, 'c');
	else
	    runPaintChat();
    }
}
