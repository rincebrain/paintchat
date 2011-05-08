package paintchat_server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Hashtable;
import paintchat.Config;
import paintchat.Res;
import paintchat.debug.DebugListener;
import syi.util.*;

// Referenced classes of package paintchat_server:
//            TextServer, PaintChatTalker, TextTalker, LineTalker, 
//            XMLTextTalker, XMLTalker, Server, LineServer

public class TalkerInstance
    implements Runnable
{

    private Socket socket;
    private InputStream In;
    private OutputStream Out;
    private TextServer textServer;
    private LineServer lineServer;
    private DebugListener debug;
    private Config serverStatus;
    private Server server;
    private String strPassword;
    boolean isAscii;

    public TalkerInstance(Config config, Server server1, TextServer textserver, LineServer lineserver, DebugListener debuglistener)
    {
        isAscii = false;
        server = server1;
        textServer = textserver;
        lineServer = lineserver;
        serverStatus = config;
        debug = debuglistener;
        strPassword = config.getString("Admin_Password", "");
    }

    public void run()
    {
        try
        {
            switchConnection(socket);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
            closeSocket();
        }
    }

    public void newTalker(Socket socket1)
    {
        TalkerInstance talkerinstance = new TalkerInstance(serverStatus, server, textServer, lineServer, debug);
        talkerinstance.socket = socket1;
        ThreadPool.poolStartThread(talkerinstance, "sock_switch");
    }

    private boolean isKillAddress(Socket socket1)
    {
        try
        {
            InetAddress inetaddress = socket1.getInetAddress();
            Vector2 vector2 = textServer.vKillIP;
            for(int i = 0; i < vector2.size(); i++)
            {
                InetAddress inetaddress1 = ((PaintChatTalker)vector2.get(i)).getAddress();
                if(inetaddress1.equals(inetaddress))
                {
                    return true;
                }
            }

        }
        catch(RuntimeException runtimeexception)
        {
            debug.log(runtimeexception);
        }
        return false;
    }

    private void switchConnection(Socket socket1)
        throws IOException
    {
        ByteInputStream byteinputstream = new ByteInputStream();
        InputStream inputstream = socket1.getInputStream();
        In = inputstream;
        isAscii = Io.r(inputstream) != 98;
        if(isAscii)
        {
            switchAsciiConnection();
            return;
        }
        int i = Io.readUShort(inputstream);
        if(i <= 0)
        {
            throw new IOException("protocol unknown");
        }
        byte abyte0[] = new byte[i];
        Io.rFull(inputstream, abyte0, 0, i);
        Res res = new Res();
        byteinputstream.setBuffer(abyte0, 0, i);
        res.load(byteinputstream);
        if(res.getBool("local_admin"))
        {
            switchLocalAdmin(socket1, res);
            return;
        }
        Object obj = null;
        String s = res.get("protocol", "");
        if(s.equals("paintchat.text"))
        {
            obj = new TextTalker(textServer, debug);
        }
        if(s.equals("paintchat.line"))
        {
            obj = new LineTalker(lineServer, debug);
        }
        s.equals("paintchat.infomation");
        if(obj != null)
        {
            if(strPassword.length() > 0 && strPassword.equals(res.get("password", "")))
            {
                res.put("permission", "layer:all;canvas:true;talk:true;layer_edit:true;fill:true;clean:true;");
            } else
            {
                res.put("permission", serverStatus.get("Client_Permission"));
            }
            ((PaintChatTalker) (obj)).mStart(socket, inputstream, null, res);
        }
    }

    private void switchAsciiConnection()
        throws IOException
    {
        BufferedInputStream bufferedinputstream = new BufferedInputStream(In);
        StringBuffer stringbuffer = new StringBuffer();
        int j = 0;
        int i;
        while((i = Io.r(bufferedinputstream)) != -1) 
        {
            stringbuffer.append((char)i);
            if(i == 0)
            {
                break;
            }
            if(++j >= 255)
            {
                throw new IOException("abnormal");
            }
        }
        String s = stringbuffer.toString();
        String s1 = null;
        int k = s.indexOf("type=");
        if(k <= 0)
        {
            s1 = "paintchat.text";
        } else
        {
            k += 6;
            int l = s.indexOf('"', k);
            if(l == -1)
            {
                l = s.indexOf('\'', k);
            }
            s1 = l != -1 ? s.substring(k, l) : "paintchat.text";
        }
        if(s1.equals("paintchat.infomation"))
        {
            switchLocalAdminXML(s);
            return;
        }
        XMLTextTalker xmltexttalker = new XMLTextTalker(textServer, debug);
        if(xmltexttalker != null)
        {
            xmltexttalker.mStart(socket, bufferedinputstream, null, null);
        }
    }

    private void switchLocalAdmin(Socket socket1, Res res)
    {
        try
        {
            String s = serverStatus.getString("Admin_Password");
            if(socket1.getInetAddress().equals(InetAddress.getLocalHost()) || s != null && s.length() > 0 && s.equals(res.get("password", "")))
            {
                String s1 = res.get("request", "ping");
                StringBuffer stringbuffer = new StringBuffer();
                byte abyte0[] = (byte[])null;
                if(s1.equals("ping"))
                {
                    stringbuffer.append("response=ping ok\n");
                    stringbuffer.append("version=");
                    stringbuffer.append("(C)And Mino, PaintChatServer v3.57b");
                    stringbuffer.append("\n");
                    abyte0 = stringbuffer.toString().getBytes("UTF8");
                }
                if(s1.equals("exit"))
                {
                    stringbuffer.append("response=exit ok\n");
                    byte abyte1[] = stringbuffer.toString().getBytes("UTF8");
                    Out = socket.getOutputStream();
                    Io.wShort(Out, abyte1.length);
                    Out.write(abyte1);
                    Out.flush();
                    closeSocket();
                    server.exitServer();
                    return;
                }
                if(abyte0 != null)
                {
                    Out = socket.getOutputStream();
                    Io.wShort(Out, abyte0.length);
                    Out.write(abyte0);
                    Out.flush();
                }
            }
        }
        catch(IOException ioexception)
        {
            debug.log(ioexception);
        }
        closeSocket();
    }

    private void switchLocalAdminXML(String s)
        throws IOException
    {
        int i = s.indexOf("request=");
        if(i < 0)
        {
            closeSocket();
            return;
        }
        i += 9;
        int j = s.indexOf('"', i);
        if(j < 0)
        {
            j = s.indexOf('\'', i);
            if(j < 0)
            {
                closeSocket();
                return;
            }
        }
        if(i == j)
        {
            return;
        }
        String s1 = s.substring(i, j);
        if(s1.equals("userlist"))
        {
            Out = socket.getOutputStream();
            StringBuffer stringbuffer = new StringBuffer();
            textServer.getUserListXML(stringbuffer);
            stringbuffer.append('\0');
            Out.write(stringbuffer.toString().getBytes("UTF8"));
            closeSocket();
            return;
        }
        if(s1.equals("infomation"))
        {
            Out = socket.getOutputStream();
            closeSocket();
            return;
        } else
        {
            closeSocket();
            return;
        }
    }

    private void closeSocket()
    {
        try
        {
            if(In != null)
            {
                In.close();
                In = null;
            }
            if(Out != null)
            {
                Out.close();
                Out = null;
            }
            if(socket != null)
            {
                socket.close();
                socket = null;
            }
        }
        catch(IOException ioexception)
        {
            debug.log(ioexception);
        }
    }
}
