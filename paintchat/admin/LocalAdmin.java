package paintchat.admin;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import paintchat.Res;
import syi.util.Io;

public class LocalAdmin
{

    private Res status;
    private InetAddress addr;
    private int iPort;
    private byte bRes[];

    public LocalAdmin(Res res, InetAddress inetaddress, int i)
    {
        status = res;
        if(res != null)
        {
            res.put("local_admin", "t");
        }
        addr = inetaddress;
        iPort = i > 0 ? i : 41411;
    }

    private void doConnect(String s)
    {
        Object obj = null;
        Object obj1 = null;
        Object obj2 = null;
        bRes = null;
        try
        {
            StringBuffer stringbuffer = new StringBuffer();
            for(Enumeration enumeration = status.keys(); enumeration.hasMoreElements();)
            {
                String s1 = enumeration.nextElement().toString();
                if(s1.length() > 0)
                {
                    stringbuffer.append(s1);
                    stringbuffer.append('=');
                    stringbuffer.append(status.get(s1));
                    stringbuffer.append('\n');
                }
            }

            stringbuffer.append("request=");
            stringbuffer.append(s);
            byte abyte0[] = stringbuffer.toString().getBytes("UTF8");
            stringbuffer = null;
            Socket socket = new Socket(addr, iPort);
            socket.setKeepAlive(true);
            InputStream inputstream = socket.getInputStream();
            OutputStream outputstream = socket.getOutputStream();
            outputstream.write(98);
            Io.wShort(outputstream, abyte0.length);
            outputstream.write(abyte0);
            outputstream.flush();
            abyte0 = (byte[])null;
            int i = Io.readUShort(inputstream);
            if(i > 0)
            {
                abyte0 = new byte[i];
                Io.rFull(inputstream, abyte0, 0, i);
            }
            try
            {
                inputstream.close();
                outputstream.close();
                socket.close();
            }
            catch(IOException _ex) { }
            bRes = abyte0;
        }
        catch(IOException _ex) { }
    }

    public String getString(String s)
    {
        try
        {
            doConnect(s);
            return bRes == null || bRes.length <= 0 ? "" : new String(bRes, "UTF8");
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return "";
    }

    public byte[] getBytes(String s)
    {
        try
        {
            doConnect(s);
            return bRes;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return null;
    }
}
