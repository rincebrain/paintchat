/* Data - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_client;
import java.applet.Applet;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Locale;

import paintchat.M;
import paintchat.MgText;
import paintchat.Res;

import syi.awt.Awt;
import syi.awt.TextPanel;
import syi.util.ByteStream;

public class Data
{
    public Pl pl;
    public Res res;
    public Res config;
    public M.Info info;
    private boolean isDestroy = false;
    private int Port;
    private InetAddress address;
    private int ID = 0;
    private M mgDraw = new M();
    public Mi mi;
    private TextPanel text;
    public int imW;
    public int imH;
    public int MAX_KAIWA;
    public int MAX_KAIWA_BORDER;
    private EOFException EOF = new EOFException();
    private TLine tLine;
    private TText tText;
    public String strName = null;
    
    public Data(Pl pl) {
	this.pl = pl;
    }
    
    public synchronized void destroy() {
	if (!isDestroy) {
	    isDestroy = true;
	    try {
		if (tLine != null) {
		    tLine.mRStop();
		    tLine = null;
		}
		if (tText != null) {
		    tText.mRStop();
		    tText = null;
		}
	    } catch (Throwable throwable) {
		/* empty */
	    }
	}
    }
    
    private Socket getSocket() {
	do {
	    if (address == null) {
		Object object = null;
		String string = config.getP("Connection_Host", (String) null);
		try {
		    if (string != null) {
			InetAddress inetaddress
			    = InetAddress.getByName(string);
		    }
		} catch (java.net.UnknownHostException unknownhostexception) {
		    object = null;
		}
		InetAddress inetaddress;
		try {
		    string = pl.applet.getCodeBase().getHost();
		    if (string == null || string.length() <= 0)
			string = "localhost";
		    inetaddress = InetAddress.getByName(string);
		} catch (java.net.UnknownHostException unknownhostexception) {
		    inetaddress = null;
		}
		if (inetaddress == null) {
		    destroy();
		    return null;
		}
		address = inetaddress;
		String string_0_ = "Connection_Port_PaintChat";
		Port = config.getP(string_0_, 41411);
		try {
		    /* empty */
		} catch (InterruptedException interruptedexception) {
		    break;
		}
	    }
	    while (!isDestroy) {
		int i = 0;
		while (i < 2) {
		    try {
			return new Socket(address, Port);
		    } catch (IOException ioexception) {
			Thread.currentThread();
			Thread.sleep(3000L);
			i++;
		    }
		}
		if (!mi.alert("reconnect", true))
		    break;
	    }
	} while (false);
	destroy();
	return null;
    }
    
    public void init() {
	try {
	    ByteStream bytestream = new ByteStream();
	    Applet applet = pl.applet;
	    URL url = applet.getCodeBase();
	    String string = p("dir_resource", "./res");
	    if (!string.endsWith("/"))
		string += '/';
	    URL url_1_ = new URL(url, string);
	    res = new Res(applet, url_1_, bytestream);
	    config = new Res(applet, url_1_, bytestream);
	    config.loadZip(p("res.zip", "res.zip"));
	    try {
		String string_2_ = "param_utf8.txt";
		config.load(new String((byte[]) config.getRes(string_2_),
				       "UTF8"));
		config.remove(string_2_);
	    } catch (IOException ioexception) {
		ioexception.printStackTrace();
	    }
	    Me.res = res;
	    Me.conf = config;
	    pl.iPG(true);
	    try {
		config.load(Awt.openStream
			    (new URL(url,
				     config.getP("File_PaintChat_Infomation",
						 config.getP("server",
							     ".paintchat")))));
	    } catch (IOException ioexception) {
		System.out.println(ioexception);
	    }
	    pl.iPG(true);
	    res.loadResource(config, "res", Locale.getDefault().getLanguage());
	    pl.iPG(true);
	    MAX_KAIWA_BORDER = config.getP("Cash_Text_Max", 120);
	    imW = config.getP("Client_Image_Width",
			      config.getP("image_width", 1200));
	    imH = config.getP("Client_Image_Height",
			      config.getP("image_height", 1200));
	} catch (IOException ioexception) {
	    ioexception.printStackTrace();
	}
    }
    
    public void send(M m) {
	tLine.send(m);
    }
    
    public void send(MgText mgtext) {
	tText.send(mgtext);
    }
    
    public void start() throws IOException {
	Mi mi = this.mi;
	info = mi.info;
	mgDraw.setInfo(mi.info);
	mgDraw.newUser(mi).wait = -1;
	Res res = new Res();
	res.put("name", strName);
	res.put("password", config.get("chat_password"));
	config.put(res);
	mRunText(res);
	mRunLine(res);
    }
    
    private void mRunLine(Res res) throws IOException {
	Res res_3_ = new Res();
	res_3_.put(res);
	res_3_.put("protocol", "paintchat.line");
	tLine = new TLine(this, mgDraw);
	Socket socket = getSocket();
	tLine.mConnect(socket, res_3_);
    }
    
    private void mRunText(Res res) throws IOException {
	Res res_4_ = new Res();
	res_4_.put(res);
	res_4_.put("protocol", "paintchat.text");
	tText = new TText(pl, this);
	Socket socket = getSocket();
	tText.mConnect(socket, res_4_);
    }
    
    public String p(String string, String string_5_) {
	try {
	    String string_6_ = pl.applet.getParameter(string);
	    if (string_6_ == null || string_6_.length() <= 0)
		return string_5_;
	    return string_6_;
	} catch (Throwable throwable) {
	    return string_5_;
	}
    }
    
    public void addTextComp() {
	pl.addTextInfo(res.get("log_complete"), true);
	mPermission(tLine.getStatus().get("permission"));
    }
    
    public void mPermission(String string) {
	int i = 0;
	int i_7_ = string.length();
	int i_8_;
	do {
	    i_8_ = string.indexOf(';', i);
	    if (i_8_ < 0)
		i_8_ = i_7_;
	    if (i_8_ - i > 0)
		mP(string.substring(i, i_8_));
	    i = i_8_ + 1;
	} while (i_8_ < i_7_);
    }
    
    private void mP(String string) {
	try {
	    int i = string.indexOf(':');
	    if (i > 0) {
		String string_9_ = string.substring(0, i).trim();
		String string_10_ = string.substring(i + 1).trim();
		boolean bool = false;
		if (string_10_.length() > 0)
		    bool = string_10_.charAt(0) == 't';
		if (string_9_.equals("layer"))
		    info.permission = (long) (string_10_.equals("all") ? -1
					      : Integer.parseInt(string_10_));
		if (string_9_.equals("layer_edit"))
		    info.isLEdit = bool;
		if (string_9_.equals("canvas"))
		    mi.isEnable = bool;
		if (string_9_.equals("fill"))
		    info.isFill = bool;
		if (string_9_.equals("clean"))
		    info.isClean = bool;
		if (string_9_.equals("unlayer"))
		    info.unpermission = (long) Integer.parseInt(string_10_);
	    }
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	}
    }
}
