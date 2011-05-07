/* Server - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import paintchat.Config;
import paintchat.MgText;
import paintchat.Res;
import paintchat.admin.LocalAdmin;
import paintchat.debug.Debug;

import syi.util.Io;
import syi.util.ThreadPool;

public class Server implements Runnable
{
    public static final String STR_VERSION
	= "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatServer v3.57b";
    public static final String FILE_CONFIG = "cnf/paintchat.cf";
    private TalkerInstance talkerInstance;
    private boolean live = true;
    private boolean isLiveThread = true;
    private boolean isOnlyServer;
    private Config config = null;
    public Debug debug = null;
    private ServerSocket sSocket;
    private Thread tConnect = null;
    private Thread tLive = null;
    TextServer textServer;
    LineServer lineServer;
    
    public Server(String string, Config config, Debug debug, boolean bool) {
	this.config = config;
	isOnlyServer = bool;
	if (string != null && string.length() > 0)
	    config.put("Admin_Password", string);
	this.debug = new Debug(debug, null);
    }
    
    public synchronized void exitServer() {
	if (live) {
	    live = false;
	    try {
		if (sSocket != null) {
		    try {
			sSocket.close();
			sSocket = null;
		    } catch (Exception exception) {
			/* empty */
		    }
		}
		Thread thread = Thread.currentThread();
		if (tConnect != null && tConnect != thread) {
		    try {
			tConnect.interrupt();
			tConnect = null;
		    } catch (Exception exception) {
			/* empty */
		    }
		}
		if (tLive != null && tLive != thread) {
		    try {
			tLive.interrupt();
			tLive = null;
		    } catch (Exception exception) {
			/* empty */
		    }
		}
		lineServer.mStop();
		textServer.mStop();
		debug.log
		    ("PaintChat\u30b5\u30fc\u30d0\u30fc\u3092\u7d42\u4e86\u3055\u305b\u307e\u3059");
	    } catch (Throwable throwable) {
		/* empty */
	    }
	    if (isOnlyServer)
		System.exit(0);
	}
    }
    
    public void init() {
	try {
	    Thread thread = new Thread(this, "init");
	    thread.setDaemon(false);
	    thread.start();
	} catch (Exception exception) {
	    debug.log("init_thread" + exception);
	}
    }
    
    public void initMakeInfomation() {
	try {
	    File file
		= new File(config.getString("File_PaintChat_Infomation",
					    "./cnf/template/.paintchat"));
	    File file_0_ = Io.getDirectory(file);
	    if (!file_0_.isDirectory())
		file_0_.mkdirs();
	    int i = sSocket.getLocalPort();
	    int i_1_ = config.getInt("Client_Image_Width", 1200);
	    int i_2_ = config.getInt("Client_Image_Height", 1200);
	    String string = config.getString("Client_Sound", "false");
	    int i_3_ = config.getInt("layer_count", 3);
	    PrintWriter printwriter
		= new PrintWriter(new FileWriter(file), false);
	    printwriter.println("Connection_Port_PaintChat=" + i);
	    printwriter.println("Client_Image_Width=" + i_1_);
	    printwriter.println("Client_Image_Height=" + i_2_);
	    printwriter.println("Client_Sound=" + string);
	    printwriter.println("layer_count=" + i_3_);
	    printwriter.flush();
	    printwriter.close();
	    File file_4_ = new File("./cnf/template/.paintchat");
	    if (!Io.getDirectory(file_4_).isDirectory())
		Io.getDirectory(file_4_).mkdirs();
	    if (!file_4_.equals(file))
		Io.copyFile(file, file_4_);
	    StringWriter stringwriter = new StringWriter();
	    stringwriter
		.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
	    stringwriter.write("<pchat_user_list port=\"" + i
			       + "\" host=\"\" refresh=\"" + 15000
			       + "\" />\r\n");
	    stringwriter.write("<pchat_admin port=\"" + i + "\" host=\"\" />");
	    stringwriter.flush();
	    stringwriter.close();
	    File file_5_ = new File(Io.getDirectory(file), "pconfig.xml");
	    printwriter = new PrintWriter(new FileWriter(file_5_), false);
	    printwriter.write(stringwriter.toString());
	    printwriter.flush();
	    printwriter.close();
	    File file_6_ = new File("./cnf/template/pconfig.xml");
	    if (!file_5_.equals(file_6_)) {
		printwriter = new PrintWriter(new FileWriter(file_6_));
		printwriter.write(stringwriter.toString());
		printwriter.flush();
		printwriter.close();
	    }
	} catch (Throwable throwable) {
	    debug.log(throwable);
	}
    }
    
    public boolean isExecute(boolean bool) {
	if (null != null) {
	    /* empty */
	}
	if (null != null) {
	    /* empty */
	}
	if (null != null) {
	    /* empty */
	}
	boolean bool_7_ = false;
	try {
	    Res res = new Res();
	    res.put("password", config.getString("Admin_Password", ""));
	    LocalAdmin localadmin
		= new LocalAdmin(res, InetAddress.getLocalHost(),
				 config.getInt("Connection_Port_PaintChat",
					       Config.DEF_PORT));
	    String string = localadmin.getString("ping");
	    bool_7_ = string.indexOf("version=") >= 0;
	    if (bool_7_ && bool) {
		string = localadmin.getString("terminate");
		debug.log("server got terminated");
		System.exit(0);
	    }
	    return bool_7_;
	} catch (Throwable throwable) {
	    if (bool_7_)
		debug.log("server have work now.");
	    return bool_7_;
	}
    }
    
    public static void main(String[] strings) {
	try {
	    String string = "cnf/paintchat.cf";
	    String string_8_ = null;
	    boolean bool = false;
	    if (strings != null) {
		if (strings.length >= 1) {
		    string = strings[0];
		    if (string.trim().length() <= 0)
			string = "cnf/paintchat.cf";
		}
		if (strings.length >= 2) {
		    if (strings[1].equalsIgnoreCase("exit"))
			bool = true;
		    else
			string_8_ = new String(strings[1]);
		}
	    }
	    System.out.println(new File(string).getCanonicalPath());
	    Config config = new Config(string);
	    Server server = new Server(string_8_, config, null, true);
	    if (server.isExecute(bool))
		throw new Exception
			  ("\u65e2\u306b\u8d77\u52d5\u3057\u3066\u3044\u307e\u3059\u3002");
	    if (bool)
		throw new Exception
			  ("\u65e2\u306b\u30b5\u30fc\u30d0\u30fc\u306f\u7d42\u4e86\u3057\u3066\u3044\u308b\u304b\u3001\u691c\u7d22\u3092\u5931\u6557\u3057\u307e\u3057\u305f\u3002");
	    server.init();
	} catch (Throwable throwable) {
	    System.out.println("server_main" + throwable.toString());
	    System.exit(0);
	}
    }
    
    public synchronized void rInit() throws Throwable {
	if (live) {
	    Config config = this.config;
	    int i
		= config.getInt("Connection_Port_PaintChat", Config.DEF_PORT);
	    int i_9_ = config.getInt("Connection_Max", 255);
	    InetAddress.getLocalHost().getHostAddress();
	    try {
		sSocket = new ServerSocket(i, i_9_);
	    } catch (IOException ioexception) {
		debug.log("Server error:" + ioexception.getMessage());
		throw ioexception;
	    }
	    i = sSocket.getLocalPort();
	    textServer = new TextServer();
	    textServer.mInit(this, this.config, debug);
	    lineServer = new LineServer();
	    lineServer.mInit(this.config, debug, this);
	    talkerInstance = new TalkerInstance(this.config, this, textServer,
						lineServer, debug);
	    initMakeInfomation();
	    runServer();
	    debug.log("Run ChatServer port=" + i);
	}
    }
    
    public void run() {
	try {
	    switch (Thread.currentThread().getName().charAt(0)) {
	    case 'c':
		runConnect();
		break;
	    case 'g':
		runLive();
		break;
	    case 'i':
		rInit();
		break;
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void runConnect() throws InterruptedException {
	while (live) {
	    try {
		Socket socket = sSocket.accept();
		socket.setSoTimeout(180000);
		if (!isLiveThread) {
		    Thread thread;
		    MONITORENTER (thread = tLive);
		    MISSING MONITORENTER
		    synchronized (thread) {
			tLive.notify();
		    }
		}
		talkerInstance.newTalker(socket);
	    } catch (Throwable throwable) {
		try {
		    Thread.currentThread();
		    Thread.sleep(5000L);
		} catch (Throwable throwable_10_) {
		    /* empty */
		}
	    }
	}
	exitServer();
    }
    
    private void runLive() {
	long l = System.currentTimeMillis();
	long l_11_ = l + 86400000L;
	long l_12_ = l + 3600000L;
	while (live) {
	    try {
		if (tLive == null)
		    break;
		Thread.currentThread();
		Thread.sleep(60000L);
		if (!live)
		    break;
		if (debug.bool_debug) {
		    int i = textServer.getUserCount();
		    int i_13_ = lineServer.getUserCount();
		    debug.logSys("pchats_gc:connect t=" + i + " l=" + i_13_);
		    debug.logSys("ThreadSleep="
				 + ThreadPool.getCountOfSleeping()
				 + " ThreadWork="
				 + ThreadPool.getCountOfWorking());
		}
		textServer.clearDeadTalker();
		if (textServer.getUserCount() <= 0
		    && lineServer.getUserCount() <= 0) {
		    Thread thread;
		    MONITORENTER (thread = tLive);
		    MISSING MONITORENTER
		    synchronized (thread) {
			debug.log("pchat_gc:suspend");
			isLiveThread = false;
			tLive.wait();
			isLiveThread = true;
		    }
		    debug.log("pchat_gc:resume");
		}
		l = System.currentTimeMillis();
		if (l >= l_12_) {
		    l_12_ = l + 3600000L;
		    debug.log(new Date().toString());
		}
		if (l >= l_11_) {
		    l_11_ += l + 86400000L;
		    debug.newLogFile(Io.getDateString
				     ("pserv_", "log",
				      config.getString("Server_Log_Server_Dir",
						       "save_server")));
		    textServer.clearKillIP();
		}
	    } catch (Throwable throwable) {
		try {
		    debug.log(throwable);
		    Thread.currentThread();
		    Thread.sleep(1000L);
		} catch (Throwable throwable_14_) {
		    break;
		}
	    }
	}
	exitServer();
    }
    
    private void runServer() {
	tConnect = new Thread(this);
	tConnect.setDaemon(true);
	tConnect.setPriority(1);
	tConnect.setName("connection");
	tLive = new Thread(this);
	tLive.setDaemon(false);
	tLive.setPriority(1);
	tLive.setName("gc");
	tConnect.start();
	tLive.start();
    }
    
    public MgText getInfomation() {
	StringWriter stringwriter = new StringWriter();
	int i = 0;
	boolean bool = true;
	stringwriter.write("---------------<br><b>");
	TextTalkerListener texttalkerlistener;
	while ((texttalkerlistener = textServer.getTalkerAt(i)) != null) {
	    i++;
	    MgText mgtext = texttalkerlistener.getHandleName();
	    InetAddress inetaddress = texttalkerlistener.getAddress();
	    LineTalker linetalker = lineServer.getTalker(inetaddress);
	    if (texttalkerlistener.isValidate()) {
		if (!bool)
		    stringwriter.write("<br>");
		bool = true;
		if (mgtext != null)
		    stringwriter.write(mgtext.toString() + "<br>");
		if (inetaddress != null)
		    stringwriter
			.write("host=" + inetaddress.toString() + "<br>");
		stringwriter.write("speak="
				   + texttalkerlistener.getSpeakCount()
				   + "<br>");
		if (linetalker != null && linetalker.isValidate())
		    stringwriter
			.write("draw=" + linetalker.getDrawCount() + "<br>");
		stringwriter.write("-----<br>");
	    }
	}
	stringwriter.write("</b>---------------");
	stringwriter.toString();
	return (i <= 0 ? null
		: new MgText(0, (byte) 8, stringwriter.toString()));
    }
    
    public void sendClearMessage(InetAddress inetaddress) {
	TextTalkerListener texttalkerlistener
	    = textServer.getTalker(inetaddress);
	if (texttalkerlistener != null) {
	    MgText mgtext
		= new MgText(0, (byte) 0,
			     (texttalkerlistener.getHandleName().getUserName()
			      + " erased canvas."));
	    mgtext.setUserName("PaintChat");
	    textServer.addText(texttalkerlistener, mgtext);
	}
    }
}
