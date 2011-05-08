package paintchat_server;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Hashtable;
import paintchat.*;
import paintchat.admin.LocalAdmin;
import paintchat.debug.Debug;
import syi.util.*;

// Referenced classes of package paintchat_server:
//            LineServer, TextServer, TalkerInstance, TextTalkerListener, 
//            PaintChatTalker, LineTalker

public class Server
    implements Runnable
{

    public static final String STR_VERSION = "(C)And Mino, PaintChatServer v3.57b";
    public static final String FILE_CONFIG = "cnf/paintchat.cf";
    private TalkerInstance talkerInstance;
    private boolean live;
    private boolean isLiveThread;
    private boolean isOnlyServer;
    private Config config;
    public Debug debug;
    private ServerSocket sSocket;
    private Thread tConnect;
    private Thread tLive;
    TextServer textServer;
    LineServer lineServer;

    public Server(String s, Config config1, Debug debug1, boolean flag)
    {
        live = true;
        isLiveThread = true;
        config = null;
        debug = null;
        tConnect = null;
        tLive = null;
        config = config1;
        isOnlyServer = flag;
        if(s != null && s.length() > 0)
        {
            config1.put("Admin_Password", s);
        }
        debug = new Debug(debug1, null);
    }

    public synchronized void exitServer()
    {
        if(!live)
        {
            return;
        }
        live = false;
        try
        {
            if(sSocket != null)
            {
                try
                {
                    sSocket.close();
                    sSocket = null;
                }
                catch(Exception _ex) { }
            }
            Thread thread = Thread.currentThread();
            if(tConnect != null && tConnect != thread)
            {
                try
                {
                    tConnect.interrupt();
                    tConnect = null;
                }
                catch(Exception _ex) { }
            }
            if(tLive != null && tLive != thread)
            {
                try
                {
                    tLive.interrupt();
                    tLive = null;
                }
                catch(Exception _ex) { }
            }
            lineServer.mStop();
            textServer.mStop();
            debug.log("PaintChat (Terminates the server)");
        }
        catch(Throwable _ex) { }
        if(isOnlyServer)
        {
            System.exit(0);
        }
    }

    public void init()
    {
        try
        {
            Thread thread = new Thread(this, "init");
            thread.setDaemon(false);
            thread.start();
        }
        catch(Exception exception)
        {
            debug.log("init_thread" + exception);
        }
    }

    public void initMakeInfomation()
    {
        try
        {
            File file = new File(config.getString("File_PaintChat_Infomation", "./cnf/template/.paintchat"));
            File file1 = Io.getDirectory(file);
            if(!file1.isDirectory())
            {
                file1.mkdirs();
            }
            int i = sSocket.getLocalPort();
            int j = config.getInt("Client_Image_Width", 1200);
            int k = config.getInt("Client_Image_Height", 1200);
            String s = config.getString("Client_Sound", "false");
            int l = config.getInt("layer_count", 3);
            PrintWriter printwriter = new PrintWriter(new FileWriter(file), false);
            printwriter.println("Connection_Port_PaintChat=" + i);
            printwriter.println("Client_Image_Width=" + j);
            printwriter.println("Client_Image_Height=" + k);
            printwriter.println("Client_Sound=" + s);
            printwriter.println("layer_count=" + l);
            printwriter.flush();
            printwriter.close();
            File file2 = new File("./cnf/template/.paintchat");
            if(!Io.getDirectory(file2).isDirectory())
            {
                Io.getDirectory(file2).mkdirs();
            }
            if(!file2.equals(file))
            {
                Io.copyFile(file, file2);
            }
            StringWriter stringwriter = new StringWriter();
            stringwriter.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
            stringwriter.write("<pchat_user_list port=\"" + i + "\" host=\"\" refresh=\"" + 15000 + "\" />\r\n");
            stringwriter.write("<pchat_admin port=\"" + i + "\" host=\"\" />");
            stringwriter.flush();
            stringwriter.close();
            File file3 = new File(Io.getDirectory(file), "pconfig.xml");
            printwriter = new PrintWriter(new FileWriter(file3), false);
            printwriter.write(stringwriter.toString());
            printwriter.flush();
            printwriter.close();
            File file4 = new File("./cnf/template/pconfig.xml");
            if(!file3.equals(file4))
            {
                PrintWriter printwriter1 = new PrintWriter(new FileWriter(file4));
                printwriter1.write(stringwriter.toString());
                printwriter1.flush();
                printwriter1.close();
            }
        }
        catch(Throwable throwable)
        {
            debug.log(throwable);
        }
    }

    public boolean isExecute(boolean flag)
    {
        Object _tmp = null;
        Object _tmp1 = null;
        Object _tmp2 = null;
        boolean flag1 = false;
        try
        {
            Res res = new Res();
            res.put("password", config.getString("Admin_Password", ""));
            LocalAdmin localadmin = new LocalAdmin(res, InetAddress.getLocalHost(), config.getInt("Connection_Port_PaintChat", Config.DEF_PORT));
            String s = localadmin.getString("ping");
            flag1 = s.indexOf("version=") >= 0;
            if(flag1 && flag)
            {
                String s1 = localadmin.getString("terminate");
                debug.log("server got terminated");
                System.exit(0);
            }
            return flag1;
        }
        catch(Throwable _ex) { }
        if(flag1)
        {
            debug.log("server have work now.");
        }
        return flag1;
    }

    public static void main(String args[])
    {
        try
        {
            String s = "cnf/paintchat.cf";
            String s1 = null;
            boolean flag = false;
            if(args != null)
            {
                if(args.length >= 1)
                {
                    s = args[0];
                    if(s.trim().length() <= 0)
                    {
                        s = "cnf/paintchat.cf";
                    }
                }
                if(args.length >= 2)
                {
                    if(args[1].equalsIgnoreCase("exit"))
                    {
                        flag = true;
                    } else
                    {
                        s1 = new String(args[1]);
                    }
                }
            }
            System.out.println((new File(s)).getCanonicalPath());
            Config config1 = new Config(s);
            Server server = new Server(s1, config1, null, true);
            if(server.isExecute(flag))
            {
                throw new Exception("(Has already started.)");
            }
            if(flag)
            {
                throw new Exception("(That the server is already completed, the search failed.)");
            }
            server.init();
        }
        catch(Throwable throwable)
        {
            System.out.println("server_main" + throwable.toString());
            System.exit(0);
        }
    }

    public synchronized void rInit()
        throws Throwable
    {
        if(!live)
        {
            return;
        }
        Config config1 = config;
        int i = config1.getInt("Connection_Port_PaintChat", Config.DEF_PORT);
        int j = config1.getInt("Connection_Max", 255);
        InetAddress.getLocalHost().getHostAddress();
        try
        {
            sSocket = new ServerSocket(i, j);
        }
        catch(IOException ioexception)
        {
            debug.log("Server error:" + ioexception.getMessage());
            throw ioexception;
        }
        i = sSocket.getLocalPort();
        textServer = new TextServer();
        textServer.mInit(this, config, debug);
        lineServer = new LineServer();
        lineServer.mInit(config, debug, this);
        talkerInstance = new TalkerInstance(config, this, textServer, lineServer, debug);
        initMakeInfomation();
        runServer();
        debug.log("Run ChatServer port=" + i);
    }

    public void run()
    {
        try
        {
            switch(Thread.currentThread().getName().charAt(0))
            {
            case 99: // 'c'
                runConnect();
                break;

            case 103: // 'g'
                runLive();
                break;

            case 105: // 'i'
                rInit();
                break;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void runConnect()
        throws InterruptedException
    {
        while(live) 
        {
            try
            {
                Socket socket = sSocket.accept();
                socket.setSoTimeout(0x2bf20);
                if(!isLiveThread)
                {
                    synchronized(tLive)
                    {
                        tLive.notify();
                    }
                }
                talkerInstance.newTalker(socket);
            }
            catch(Throwable _ex)
            {
                try
                {
                    Thread.currentThread();
                    Thread.sleep(5000L);
                }
                catch(Throwable _ex2) { }
            }
        }
        exitServer();
    }

    private void runLive()
    {
        long l = System.currentTimeMillis();
        long l2 = l + 0x5265c00L;
        long l3 = l + 0x36ee80L;
        while(live) 
        {
            try
            {
                if(tLive == null)
                {
                    break;
                }
                Thread.currentThread();
                Thread.sleep(60000L);
                if(!live)
                {
                    break;
                }
                if(debug.bool_debug)
                {
                    int i = textServer.getUserCount();
                    int j = lineServer.getUserCount();
                    debug.logSys("pchats_gc:connect t=" + i + " l=" + j);
                    debug.logSys("ThreadSleep=" + ThreadPool.getCountOfSleeping() + " ThreadWork=" + ThreadPool.getCountOfWorking());
                }
                textServer.clearDeadTalker();
                if(textServer.getUserCount() <= 0 && lineServer.getUserCount() <= 0)
                {
                    synchronized(tLive)
                    {
                        debug.log("pchat_gc:suspend");
                        isLiveThread = false;
                        tLive.wait();
                        isLiveThread = true;
                    }
                    debug.log("pchat_gc:resume");
                }
                long l1 = System.currentTimeMillis();
                if(l1 >= l3)
                {
                    l3 = l1 + 0x36ee80L;
                    debug.log((new Date()).toString());
                }
                if(l1 >= l2)
                {
                    l2 += l1 + 0x5265c00L;
                    debug.newLogFile(Io.getDateString("pserv_", "log", config.getString("Server_Log_Server_Dir", "save_server")));
                    textServer.clearKillIP();
                }
                continue;
            }
            catch(Throwable throwable)
            {
                try
                {
                    debug.log(throwable);
                    Thread.currentThread();
                    Thread.sleep(1000L);
                    continue;
                }
                catch(Throwable _ex) { }
            }
            break;
        }
        exitServer();
    }

    private void runServer()
    {
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

    public MgText getInfomation()
    {
        StringWriter stringwriter = new StringWriter();
        int i = 0;
        boolean flag = true;
        stringwriter.write("---------------<br><b>");
        TextTalkerListener texttalkerlistener;
        while((texttalkerlistener = textServer.getTalkerAt(i)) != null) 
        {
            i++;
            MgText mgtext = texttalkerlistener.getHandleName();
            InetAddress inetaddress = texttalkerlistener.getAddress();
            LineTalker linetalker = lineServer.getTalker(inetaddress);
            if(texttalkerlistener.isValidate())
            {
                if(!flag)
                {
                    stringwriter.write("<br>");
                }
                flag = true;
                if(mgtext != null)
                {
                    stringwriter.write(mgtext.toString() + "<br>");
                }
                if(inetaddress != null)
                {
                    stringwriter.write("host=" + inetaddress.toString() + "<br>");
                }
                stringwriter.write("speak=" + texttalkerlistener.getSpeakCount() + "<br>");
                if(linetalker != null && linetalker.isValidate())
                {
                    stringwriter.write("draw=" + linetalker.getDrawCount() + "<br>");
                }
                stringwriter.write("-----<br>");
            }
        }
        stringwriter.write("</b>---------------");
        stringwriter.toString();
        return i > 0 ? new MgText(0, (byte)8, stringwriter.toString()) : null;
    }

    public void sendClearMessage(InetAddress inetaddress)
    {
        TextTalkerListener texttalkerlistener = textServer.getTalker(inetaddress);
        if(texttalkerlistener != null)
        {
            MgText mgtext = new MgText(0, (byte)0, texttalkerlistener.getHandleName().getUserName() + " erased canvas.");
            mgtext.setUserName("PaintChat");
            textServer.addText(texttalkerlistener, mgtext);
        }
    }
}
