package paintchat_client;

import java.applet.Applet;
import java.io.*;
import java.net.*;
//import java.util.Hashtable;
import java.util.Locale;

import paintchat.*;
//import paintchat_server.PaintChatTalker;

import syi.awt.Awt;
import syi.awt.TextPanel;
import syi.util.ByteStream;

// Referenced classes of package paintchat_client:
//            TLine, TText, Pl, Mi, 
//            Me

public class Data
{

    public Pl pl;
    public Res res;
    public Res config;
    public paintchat.M.Info info;
    private boolean isDestroy;
    private int Port;
    private InetAddress address;
    private int ID;
    private M mgDraw;
    public Mi mi;
    private TextPanel text;
    public int imW;
    public int imH;
    public int MAX_KAIWA;
    public int MAX_KAIWA_BORDER;
    private EOFException EOF;
    private TLine tLine;
    private TText tText;
    public String strName;

    public Data(Pl pl1)
    {
        isDestroy = false;
        ID = 0;
        mgDraw = new M();
        EOF = new EOFException();
        strName = null;
        pl = pl1;
    }

    public synchronized void destroy()
    {
        if(isDestroy)
        {
            return;
        }
        isDestroy = true;
        try
        {
            if(tLine != null)
            {
                tLine.mRStop();
                tLine = null;
            }
            if(tText != null)
            {
                tText.mRStop();
                tText = null;
            }
        }
        catch(Throwable _ex) { }
    }

    private Socket getSocket()
    {
        if(address == null)
        {
            InetAddress inetaddress = null;
            String s = config.getP("Connection_Host", null);
            try
            {
                if(s != null)
                {
                    inetaddress = InetAddress.getByName(s);
                }
            }
            catch(UnknownHostException _ex)
            {
                inetaddress = null;
            }
            try
            {
                String s1 = pl.applet.getCodeBase().getHost();
                if(s1 == null || s1.length() <= 0)
                {
                    s1 = "localhost";
                }
                inetaddress = InetAddress.getByName(s1);
            }
            catch(UnknownHostException _ex)
            {
                inetaddress = null;
            }
            if(inetaddress == null)
            {
                destroy();
                return null;
            }
            address = inetaddress;
            String s2 = "Connection_Port_PaintChat";
            Port = config.getP(s2, 41411);
        }
        try
        {
            while(!isDestroy) 
            {
                for(int i = 0; i < 2; i++)
                {
                    try
                    {
                        Socket tmp = new Socket(address, Port);
                        tmp.setKeepAlive(true);
                        return tmp;
                    }
                    catch(IOException _ex)
                    {
                        Thread.currentThread();
                    }
                    Thread.sleep(3000L);
                }

                if(!mi.alert("reconnect", true))
                {
                    break;
                }
            }
        }
        catch(InterruptedException _ex) { }
        destroy();
        return null;
    }

    public void init()
    {
        try
        {
            ByteStream bytestream = new ByteStream();
            Applet applet = pl.applet;
            URL url = applet.getCodeBase();
            String s = p("dir_resource", "./res");
            if(!s.endsWith("/"))
            {
                s = s + '/';
            }
            URL url1 = new URL(url, s);
            res = new Res(applet, url1, bytestream);
            config = new Res(applet, url1, bytestream);
            config.loadZip(p("res.zip", "res.zip"));
            try
            {
                String s1 = "param_utf8.txt";
                config.load(new String((byte[])config.getRes(s1), "UTF8"));
                config.remove(s1);
            }
            catch(IOException ioexception1)
            {
                ioexception1.printStackTrace();
            }
            Me.res = res;
            Me.conf = config;
            pl.iPG(true);
            try
            {
                config.load(Awt.openStream(new URL(url, config.getP("File_PaintChat_Infomation", config.getP("server", ".paintchat")))));
            }
            catch(IOException ioexception2)
            {
                System.out.println(ioexception2);
            }
            pl.iPG(true);
            res.loadResource(config, "res", Locale.getDefault().getLanguage());
            pl.iPG(true);
            MAX_KAIWA_BORDER = config.getP("Cash_Text_Max", 120);
            imW = config.getP("Client_Image_Width", config.getP("image_width", 1200));
            imH = config.getP("Client_Image_Height", config.getP("image_height", 1200));
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public void send(M m)
    {
        tLine.send(m);
    }

    public void send(MgText mgtext)
    {
        tText.send(mgtext);
    }

    public void start()
        throws IOException
    {
        Mi mi1 = mi;
        info = mi1.info;
        mgDraw.setInfo(mi1.info);
        mgDraw.newUser(mi1).wait = -1;
        Res res1 = new Res();
        res1.put("name", strName);
        res1.put("password", config.get("chat_password"));
        config.put(res1);
        mRunText(res1);
        mRunLine(res1);
    }

    private void mRunLine(Res res1)
        throws IOException
    {
        Res res2 = new Res();
        res2.put(res1);
        res2.put("protocol", "paintchat.line");
        tLine = new TLine(this, mgDraw);
        Socket socket = getSocket();
        tLine.mConnect(socket, res2);
    }

    private void mRunText(Res res1)
        throws IOException
    {
        Res res2 = new Res();
        res2.put(res1);
        res2.put("protocol", "paintchat.text");
        tText = new TText(pl, this);
        Socket socket = getSocket();
        tText.mConnect(socket, res2);
    }

    public String p(String s, String s1)
    {
        try
        {
            String s2 = pl.applet.getParameter(s);
            if(s2 == null || s2.length() <= 0)
            {
                return s1;
            } else
            {
                return s2;
            }
        }
        catch(Throwable _ex)
        {
            return s1;
        }
    }

    public void addTextComp()
    {
        pl.addTextInfo(res.get("log_complete"), true);
        mPermission(tLine.getStatus().get("permission"));
    }

    public void mPermission(String s)
    {
        int i = 0;
        int k = s.length();
        int j;
        do
        {
            j = s.indexOf(';', i);
            if(j < 0)
            {
                j = k;
            }
            if(j - i > 0)
            {
                mP(s.substring(i, j));
            }
            i = j + 1;
        } while(j < k);
    }

    private void mP(String s)
    {
        try
        {
            int i = s.indexOf(':');
            if(i <= 0)
            {
                return;
            }
            String s1 = s.substring(0, i).trim();
            String s2 = s.substring(i + 1).trim();
            boolean flag = false;
            if(s2.length() > 0)
            {
                flag = s2.charAt(0) == 't';
            }
            if(s1.equals("layer"))
            {
                info.permission = s2.equals("all") ? -1 : Integer.parseInt(s2);
            }
            if(s1.equals("layer_edit"))
            {
                info.isLEdit = flag;
            }
            if(s1.equals("canvas"))
            {
                mi.isEnable = flag;
            }
            if(s1.equals("fill"))
            {
                info.isFill = flag;
            }
            if(s1.equals("clean"))
            {
                info.isClean = flag;
            }
            if(s1.equals("unlayer"))
            {
                info.unpermission = Integer.parseInt(s2);
            }
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
        }
    }
}
