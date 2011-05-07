/* PopupMenuPaintChat - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_frame;
import java.awt.Container;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import paintchat.Config;
import paintchat.Res;
import paintchat.debug.Debug;

import syi.awt.LTextField;
import syi.awt.MessageBox;
import syi.util.Io;

public class PopupMenuPaintChat implements ActionListener
{
    private Debug debug;
    private Res res;
    private Config config;
    private LTextField textField;
    public String strSelected;
    private String strAuto;
    private String strCgi;
    
    public PopupMenuPaintChat(Debug debug, Config config, Res res) {
	this.debug = debug;
	this.config = config;
	this.res = res;
    }
    
    public void actionPerformed(ActionEvent actionevent) {
	try {
	    doAction(actionevent.getActionCommand());
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public static String addressToString(byte[] is) {
	StringBuffer stringbuffer = new StringBuffer();
	for (int i = 0; i < 4; i++) {
	    if (i > 0)
		stringbuffer.append('.');
	    stringbuffer.append(is[i] & 0xff);
	}
	return stringbuffer.toString();
    }
    
    public void copyURL(String string) {
	StringBuffer stringbuffer = new StringBuffer();
	stringbuffer.append("http://");
	stringbuffer.append(string);
	int i = config.getInt("Connection_Port_Http", 80);
	if (i != 80) {
	    stringbuffer.append(':');
	    stringbuffer.append(i);
	}
	stringbuffer.append('/');
	StringSelection stringselection
	    = new StringSelection(stringbuffer.toString());
	Toolkit.getDefaultToolkit().getSystemClipboard()
	    .setContents(stringselection, stringselection);
    }
    
    private void doAction(String string) {
	try {
	    if (string.equals(strAuto))
		string = addressToString(getGlobalAddress().getAddress());
	    else if (string.equals(strCgi)) {
		string = getAddressFromCGI();
		if (string == null || string.length() <= 0) {
		    MessageBox.alert("NotfoundCGI", "TitleOfError");
		    return;
		}
	    }
	    textField.setText(string);
	    copyURL(string);
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    public static String getAddress(Config config, Debug debug) {
	Object object = null;
	InetAddress inetaddress;
	try {
	    boolean bool = config.getBool("Connection_GrobalAddress");
	    if (bool)
		inetaddress = selectGlobalAddress(getGlobalAddress());
	    else
		inetaddress = InetAddress.getLocalHost();
	} catch (IOException ioexception) {
	    inetaddress = null;
	}
	if (inetaddress == null) {
	    try {
		inetaddress = InetAddress.getLocalHost();
	    } catch (IOException ioexception) {
		inetaddress = null;
	    }
	}
	if (inetaddress == null) {
	    debug.logRes("BadAddress");
	    return "127.0.0.1";
	}
	byte[] is = inetaddress.getAddress();
	if (!isGlobalIP(is))
	    debug.logRes("LocalAddress");
	return addressToString(is);
    }
    
    public String getAddressFromCGI() throws IOException {
	String string = config.getString("App_Cgi");
	if (string == null || string.length() <= 0)
	    return null;
	URL url = new URL(string);
	BufferedInputStream bufferedinputstream
	    = new BufferedInputStream(url.openStream());
	StringBuffer stringbuffer = new StringBuffer();
	try {
	    for (;;) {
		int i = Io.r(bufferedinputstream);
		if (i == 37)
		    stringbuffer.append
			((char) ((Character.digit
				  ((char) Io.r(bufferedinputstream), 16)) << 4
				 | (Character.digit
				    ((char) Io.r(bufferedinputstream), 16))));
		else
		    stringbuffer.append((char) i);
	    }
	} catch (java.io.EOFException eofexception) {
	    bufferedinputstream.close();
	    return stringbuffer.toString();
	}
    }
    
    private static InetAddress getGlobalAddress() throws IOException {
	InetAddress inetaddress = InetAddress.getLocalHost();
	InetAddress inetaddress_0_;
	try {
	    Socket socket
		= new Socket(new URL(System.getProperty("java.vendor.url"))
				 .getHost(),
			     80);
	    inetaddress_0_ = socket.getLocalAddress();
	    socket.close();
	    Object object = null;
	} catch (IOException ioexception) {
	    inetaddress_0_ = inetaddress;
	}
	return selectGlobalAddress(inetaddress_0_);
    }
    
    public static boolean isGlobalIP(byte[] is) {
	if (is[0] == 127 && is[1] == 0 && is[2] == 0 && is[3] == 1)
	    return false;
	if (is[0] == 10)
	    return false;
	if (is[0] == 172 && is[1] >= 16 && is[1] < 31)
	    return false;
	if (is[0] == 192 && is[1] == 168)
	    return false;
	return true;
    }
    
    private static InetAddress selectGlobalAddress(InetAddress inetaddress) {
	try {
	    byte[] is = inetaddress.getAddress();
	    if (!isGlobalIP(is)) {
		InetAddress[] inetaddresses
		    = InetAddress.getAllByName(inetaddress.getHostAddress());
		for (int i = 0; i < inetaddresses.length; i++) {
		    is = inetaddresses[i].getAddress();
		    if (isGlobalIP(is))
			return inetaddresses[i];
		}
	    }
	} catch (IOException ioexception) {
	    /* empty */
	} catch (RuntimeException runtimeexception) {
	    /* empty */
	}
	return inetaddress;
    }
    
    public void show(Container container, LTextField ltextfield, int i,
		     int i_1_) {
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
	popupmenu.show(container, i, i_1_);
    }
}
