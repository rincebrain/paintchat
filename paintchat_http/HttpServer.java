package paintchat_http;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import paintchat.*;
import paintchat.debug.Debug;
import syi.util.PProperties;

// Referenced classes of package paintchat_http:
//            HttpFiles, TalkerHttp

public class HttpServer
    implements Runnable
{

    private boolean live;
    private boolean isOnlyServer;
    public Debug debug;
    private ServerSocket ssocket;
    private Thread thread;
    public File dirCurrent;
    public File dirResource;
    public String dirLog;
    public static PProperties Mime = null;
    public static Config config = null;
    public static Res res = null;
    public static SimpleDateFormat fmt = null;
    public String strHost;
    public EOFException EOF;
    private HttpFiles httpFiles;
    static final String STR_FILE_MIME = "./cnf/mime.cf";
    public static final String STR_VERSION = "PaintChatHTTP/3.1";
    private static final String STR_TIME_FORMAT = "EEEE',' dd MMM yyyy HH:mm:ss 'GMT'";

    public HttpServer(Config config1, Debug debug1, boolean flag)
    {
        live = true;
        isOnlyServer = false;
        debug = null;
        dirCurrent = null;
        dirResource = null;
        strHost = null;
        EOF = new EOFException();
        res = Resource.loadResource("Http");
        debug = new Debug(debug1, res);
        config = config1;
        isOnlyServer = flag;
    }

    public synchronized void exitServer()
    {
        if(!live)
        {
            return;
        }
        try
        {
            live = false;
            ServerSocket serversocket = ssocket;
            ssocket = null;
            serversocket.close();
            serversocket = null;
            if(thread != null && Thread.currentThread() != thread)
            {
                thread.interrupt();
                thread.join();
                thread = null;
            }
            debug.log(res.get("Exit"));
        }
        catch(Throwable throwable)
        {
            debug.log(res.get("Error") + throwable.getMessage());
        }
        if(isOnlyServer)
        {
            System.exit(0);
        }
    }

    protected void finalize()
        throws Throwable
    {
        super.finalize();
        exitServer();
    }

    public String getLogName(String s, String s1, String s2)
    {
        String s3 = null;
        File file = new File(s2);
        if(!file.exists())
        {
            file.mkdirs();
        }
        try
        {
            GregorianCalendar gregoriancalendar = new GregorianCalendar(TimeZone.getDefault());
            String s4 = s + gregoriancalendar.get(2) + '-' + gregoriancalendar.get(5) + '_';
            for(int i = 0; i < 32767; i++)
            {
                File file1 = new File(s2, s4 + i + '.' + s1);
                if(file1.exists())
                {
                    continue;
                }
                s3 = s2 + "/" + s4 + i + s1;
                break;
            }

        }
        catch(RuntimeException runtimeexception)
        {
            debug.log("getDate" + runtimeexception);
        }
        return s3;
    }

    public void init(int i)
    {
        try
        {
            Config config1 = config;
            debug.setDebug(config1.getBool("Server_Debug"));
            if(config1.getBool("Http_Log"))
            {
                debug.setFileWriter(getLogName("phttpd", "log", "save_log"));
            }
            if(Mime == null)
            {
                Mime = new PProperties();
                Mime.load(new FileInputStream("./cnf/mime.cf"));
            }
            if(debug.bool_debug)
            {
                debug.logDebug("Mime(Road)");
            }
            fmt = new SimpleDateFormat("EEEE',' dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
            dirLog = config.getString("Http_Log.Dir");
            dirCurrent = new File(config.getString("Http_Dir", "./www/"));
            if(!dirCurrent.exists())
            {
                dirCurrent.mkdirs();
            }
            dirResource = new File("./cnf/template/");
            strHost = InetAddress.getLocalHost().getHostName();
            httpFiles = new HttpFiles(config, res);
            ssocket = new ServerSocket(i, 50);
            thread = new Thread(this);
            thread.setDaemon(false);
            thread.setPriority(1);
            thread.start();
            debug.log("Run HTTPD PORT:" + i);
        }
        catch(Exception exception)
        {
            debug.log(res.get("Error") + exception.getMessage());
            return;
        }
    }

    public static void main(String args[])
    {
        try
        {
            int i = 80;
            String s = "./cnf/paintchat.cf";
            if(args != null && args.length > 0)
            {
                try
                {
                    i = Integer.parseInt(args[0]);
                }
                catch(Exception _ex)
                {
                    i = 80;
                }
                if(args.length >= 2)
                {
                    s = args[1];
                }
            }
            Config config1 = new Config(s);
            HttpServer httpserver = new HttpServer(config1, null, true);
            httpserver.init(i);
        }
        catch(Exception exception)
        {
            System.out.println("http_main:" + exception.toString());
        }
    }

    public void run()
    {
        long l = System.currentTimeMillis();
        while(live) 
        {
            try
            {
                Socket socket = null;
                socket = ssocket.accept();
                socket.setSoTimeout(30000);
                new TalkerHttp(socket, this, httpFiles);
                continue;
            }
            catch(InterruptedIOException _ex)
            {
                if(System.currentTimeMillis() - l > 0x5265c00L)
                {
                    l = System.currentTimeMillis();
                    debug.newLogFile(getLogName("phttpd", "log", dirLog));
                }
                continue;
            }
            catch(Throwable throwable)
            {
                if(!live)
                {
                    break;
                }
                debug.log(res.get("Error") + throwable.getMessage());
            }
        }
    }

}
