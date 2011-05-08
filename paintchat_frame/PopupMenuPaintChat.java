package paintchat_frame;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import paintchat.Config;
import paintchat.Res;
import paintchat.debug.Debug;
import syi.awt.LTextField;
import syi.awt.MessageBox;
import syi.util.Io;
import syi.util.PProperties;

public class PopupMenuPaintChat
    implements ActionListener
{

    private Debug debug;
    private Res res;
    private Config config;
    private LTextField textField;
    public String strSelected;
    private String strAuto;
    private String strCgi;

    public PopupMenuPaintChat(Debug debug1, Config config1, Res res1)
    {
        debug = debug1;
        config = config1;
        res = res1;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        try
        {
            doAction(actionevent.getActionCommand());
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public static String addressToString(byte abyte0[])
    {
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < 4; i++)
        {
            if(i > 0)
            {
                stringbuffer.append('.');
            }
            stringbuffer.append(abyte0[i] & 0xff);
        }

        return stringbuffer.toString();
    }

    public void copyURL(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("http://");
        stringbuffer.append(s);
        int i = config.getInt("Connection_Port_Http", 80);
        if(i != 80)
        {
            stringbuffer.append(':');
            stringbuffer.append(i);
        }
        stringbuffer.append('/');
        StringSelection stringselection = new StringSelection(stringbuffer.toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, stringselection);
    }

    private void doAction(String s)
    {
        try
        {
            if(s.equals(strAuto))
            {
                s = addressToString(getGlobalAddress().getAddress());
            } else
            if(s.equals(strCgi))
            {
                s = getAddressFromCGI();
                if(s == null || s.length() <= 0)
                {
                    MessageBox.alert("NotfoundCGI", "TitleOfError");
                    return;
                }
            }
            textField.setText(s);
            copyURL(s);
        }
        catch(Throwable _ex) { }
    }

    public static String getAddress(Config config1, Debug debug1)
    {
        InetAddress inetaddress = null;
        try
        {
            boolean flag = config1.getBool("Connection_GrobalAddress");
            if(flag)
            {
                inetaddress = selectGlobalAddress(getGlobalAddress());
            } else
            {
                inetaddress = InetAddress.getLocalHost();
            }
        }
        catch(IOException _ex)
        {
            inetaddress = null;
        }
        if(inetaddress == null)
        {
            try
            {
                inetaddress = InetAddress.getLocalHost();
            }
            catch(IOException _ex)
            {
                inetaddress = null;
            }
        }
        if(inetaddress == null)
        {
            debug1.logRes("BadAddress");
            return "127.0.0.1";
        }
        byte abyte0[] = inetaddress.getAddress();
        if(!isGlobalIP(abyte0))
        {
            debug1.logRes("LocalAddress");
        }
        return addressToString(abyte0);
    }

    public String getAddressFromCGI()
        throws IOException
    {
        String s = config.getString("App_Cgi");
        if(s == null || s.length() <= 0)
        {
            return null;
        }
        URL url = new URL(s);
        BufferedInputStream bufferedinputstream = new BufferedInputStream(url.openStream());
        StringBuffer stringbuffer = new StringBuffer();
        try
        {
            do
            {
                int i = Io.r(bufferedinputstream);
                if(i == 37)
                {
                    stringbuffer.append((char)(Character.digit((char)Io.r(bufferedinputstream), 16) << 4 | Character.digit((char)Io.r(bufferedinputstream), 16)));
                } else
                {
                    stringbuffer.append((char)i);
                }
            } while(true);
        }
        catch(EOFException _ex)
        {
            bufferedinputstream.close();
        }
        return stringbuffer.toString();
    }

    private static InetAddress getGlobalAddress()
        throws IOException
    {
        InetAddress inetaddress = InetAddress.getLocalHost();
        InetAddress inetaddress1;
        try
        {
            Socket socket = new Socket((new URL(System.getProperty("java.vendor.url"))).getHost(), 80);
            inetaddress1 = socket.getLocalAddress();
            socket.close();
            socket = null;
        }
        catch(IOException _ex)
        {
            inetaddress1 = inetaddress;
        }
        return selectGlobalAddress(inetaddress1);
    }

    public static boolean isGlobalIP(byte abyte0[])
    {
        if(abyte0[0] == 127 && abyte0[1] == 0 && abyte0[2] == 0 && abyte0[3] == 1)
        {
            return false;
        }
        if(abyte0[0] == 10)
        {
            return false;
        }
        if(abyte0[0] == 172 && abyte0[1] >= 16 && abyte0[1] < 31)
        {
            return false;
        }
        return abyte0[0] != 192 || abyte0[1] != 168;
    }

    private static InetAddress selectGlobalAddress(InetAddress inetaddress)
    {
        try
        {
            byte abyte0[] = inetaddress.getAddress();
            if(!isGlobalIP(abyte0))
            {
                InetAddress ainetaddress[] = InetAddress.getAllByName(inetaddress.getHostAddress());
                for(int i = 0; i < ainetaddress.length; i++)
                {
                    byte abyte1[] = ainetaddress[i].getAddress();
                    if(isGlobalIP(abyte1))
                    {
                        return ainetaddress[i];
                    }
                }

            }
        }
        catch(IOException _ex) { }
        catch(RuntimeException _ex) { }
        return inetaddress;
    }

    public void show(Container container, LTextField ltextfield, int i, int j)
    {
        PopupMenu popupmenu = new PopupMenu();
        popupmenu.addActionListener(this);
        strAuto = res.get("ConnectAuto");
        strCgi = res.get("ConnectCGI");
        textField = ltextfield;
        popupmenu.add(getAddress(config, debug));
        popupmenu.addSeparator();
        popupmenu.add(strAuto);
        popupmenu.add(strCgi);
        container.add(popupmenu);
        popupmenu.show(container, i, j);
    }
}
