/* HttpServer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_http;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import paintchat.Config;
import paintchat.Res;
import paintchat.Resource;
import paintchat.debug.Debug;

import syi.util.PProperties;

public class HttpServer implements Runnable
{
    private boolean live = true;
    private boolean isOnlyServer = false;
    public Debug debug = null;
    private ServerSocket ssocket;
    private Thread thread;
    public File dirCurrent = null;
    public File dirResource = null;
    public String dirLog;
    public static PProperties Mime = null;
    public static Config config = null;
    public static Res res = null;
    public static SimpleDateFormat fmt = null;
    public String strHost = null;
    public EOFException EOF = new EOFException();
    private HttpFiles httpFiles;
    static final String STR_FILE_MIME = "./cnf/mime.cf";
    public static final String STR_VERSION = "PaintChatHTTP/3.1";
    private static final String STR_TIME_FORMAT
	= "EEEE',' dd MMM yyyy HH:mm:ss 'GMT'";
    
    public HttpServer(Config config, Debug debug, boolean bool) {
	res = Resource.loadResource("Http");
	this.debug = new Debug(debug, res);
	HttpServer.config = config;
	isOnlyServer = bool;
    }
    
    public synchronized void exitServer() {
	if (live) {
	    try {
		live = false;
		ServerSocket serversocket = ssocket;
		ssocket = null;
		serversocket.close();
		Object object = null;
		if (thread != null && Thread.currentThread() != thread) {
		    thread.interrupt();
		    thread.join();
		    thread = null;
		}
		debug.log(res.get("Exit"));
	    } catch (Throwable throwable) {
		debug.log(res.get("Error") + throwable.getMessage());
	    }
	    if (isOnlyServer)
		System.exit(0);
	}
    }
    
    protected void finalize() throws Throwable {
	super.finalize();
	exitServer();
    }
    
    public String getLogName(String string, String string_0_,
			     String string_1_) {
	String string_2_ = null;
	File file = new File(string_1_);
	if (!file.exists())
	    file.mkdirs();
	try {
	    GregorianCalendar gregoriancalendar
		= new GregorianCalendar(TimeZone.getDefault());
	    String string_3_ = (string + gregoriancalendar.get(2) + '-'
				+ gregoriancalendar.get(5) + '_');
	    for (int i = 0; i < 32767; i++) {
		File file_4_
		    = new File(string_1_, string_3_ + i + '.' + string_0_);
		if (!file_4_.exists()) {
		    string_2_ = string_1_ + "/" + string_3_ + i + string_0_;
		    break;
		}
	    }
	} catch (RuntimeException runtimeexception) {
	    debug.log("getDate" + runtimeexception);
	}
	return string_2_;
    }
    
    public void init(int i) {
	try {
	    Config config = HttpServer.config;
	    debug.setDebug(config.getBool("Server_Debug"));
	    if (config.getBool("Http_Log"))
		debug.setFileWriter(getLogName("phttpd", "log", "save_log"));
	    if (Mime == null) {
		Mime = new PProperties();
		Mime.load(new FileInputStream("./cnf/mime.cf"));
	    }
	    if (debug.bool_debug)
		debug.logDebug("Mime\u30ed\u30fc\u30c9");
	    fmt = new SimpleDateFormat("EEEE',' dd MMM yyyy HH:mm:ss 'GMT'",
				       Locale.ENGLISH);
	    dirLog = HttpServer.config.getString("Http_Log.Dir");
	    dirCurrent
		= new File(HttpServer.config.getString("Http_Dir", "./www/"));
	    if (!dirCurrent.exists())
		dirCurrent.mkdirs();
	    dirResource = new File("./cnf/template/");
	    strHost = InetAddress.getLocalHost().getHostName();
	    httpFiles = new HttpFiles(HttpServer.config, res);
	    ssocket = new ServerSocket(i, 50);
	    thread = new Thread(this);
	    thread.setDaemon(false);
	    thread.setPriority(1);
	    thread.start();
	    debug.log("Run HTTPD PORT:" + i);
	} catch (Exception exception) {
	    debug.log(res.get("Error") + exception.getMessage());
	}
    }
    
    public static void main(String[] strings) {
	try {
	    int i = 80;
	    String string = "./cnf/paintchat.cf";
	    if (strings != null && strings.length > 0) {
		try {
		    i = Integer.parseInt(strings[0]);
		} catch (Exception exception) {
		    i = 80;
		}
		if (strings.length >= 2)
		    string = strings[1];
	    }
	    Config config = new Config(string);
	    HttpServer httpserver = new HttpServer(config, null, true);
	    httpserver.init(i);
	} catch (Exception exception) {
	    System.out.println("http_main:" + exception.toString());
	}
    }
    
    public void run() {
	long l = System.currentTimeMillis();
	while (live) {
	    try {
		Object object = null;
		Socket socket = ssocket.accept();
		socket.setSoTimeout(30000);
		new TalkerHttp(socket, this, httpFiles);
	    } catch (java.io.InterruptedIOException interruptedioexception) {
		if (System.currentTimeMillis() - l > 86400000L) {
		    l = System.currentTimeMillis();
		    debug.newLogFile(getLogName("phttpd", "log", dirLog));
		}
	    } catch (Throwable throwable) {
		if (!live)
		    break;
		debug.log(res.get("Error") + throwable.getMessage());
	    }
	}
    }
}
