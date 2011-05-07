/* TalkerInstance - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import paintchat.Config;
import paintchat.Res;
import paintchat.debug.DebugListener;

import syi.util.ByteInputStream;
import syi.util.Io;
import syi.util.ThreadPool;
import syi.util.Vector2;

public class TalkerInstance implements Runnable
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
    boolean isAscii = false;
    
    public TalkerInstance(Config config, Server server, TextServer textserver,
			  LineServer lineserver, DebugListener debuglistener) {
	this.server = server;
	textServer = textserver;
	lineServer = lineserver;
	serverStatus = config;
	debug = debuglistener;
	strPassword = config.getString("Admin_Password", "");
    }
    
    public void run() {
	try {
	    switchConnection(socket);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	    closeSocket();
	}
    }
    
    public void newTalker(Socket socket) {
	TalkerInstance talkerinstance_0_
	    = new TalkerInstance(serverStatus, server, textServer, lineServer,
				 debug);
	talkerinstance_0_.socket = socket;
	ThreadPool.poolStartThread(talkerinstance_0_, "sock_switch");
    }
    
    private boolean isKillAddress(Socket socket) {
	try {
	    InetAddress inetaddress = socket.getInetAddress();
	    Vector2 vector2 = textServer.vKillIP;
	    for (int i = 0; i < vector2.size(); i++) {
		InetAddress inetaddress_1_
		    = ((PaintChatTalker) vector2.get(i)).getAddress();
		if (inetaddress_1_.equals(inetaddress))
		    return true;
	    }
	} catch (RuntimeException runtimeexception) {
	    debug.log(runtimeexception);
	}
	return false;
    }
    
    private void switchConnection(Socket socket) throws IOException {
	ByteInputStream byteinputstream = new ByteInputStream();
	InputStream inputstream = socket.getInputStream();
	In = inputstream;
	isAscii = Io.r(inputstream) != 98;
	if (isAscii)
	    switchAsciiConnection();
	else {
	    int i = Io.readUShort(inputstream);
	    if (i <= 0)
		throw new IOException("protocol unknown");
	    byte[] is = new byte[i];
	    Io.rFull(inputstream, is, 0, i);
	    Res res = new Res();
	    byteinputstream.setBuffer(is, 0, i);
	    res.load(byteinputstream);
	    if (res.getBool("local_admin"))
		switchLocalAdmin(socket, res);
	    else {
		PaintChatTalker paintchattalker = null;
		String string = res.get("protocol", "");
		if (string.equals("paintchat.text"))
		    paintchattalker = new TextTalker(textServer, debug);
		if (string.equals("paintchat.line"))
		    paintchattalker = new LineTalker(lineServer, debug);
		string.equals("paintchat.infomation");
		if (paintchattalker != null) {
		    if (strPassword.length() > 0
			&& strPassword.equals(res.get("password", "")))
			res.put
			    ("permission",
			     "layer:all;canvas:true;talk:true;layer_edit:true;fill:true;clean:true;");
		    else
			res.put("permission",
				serverStatus.get("Client_Permission"));
		    paintchattalker.mStart(this.socket, inputstream, null,
					   res);
		}
	    }
	}
    }
    
    private void switchAsciiConnection() throws IOException {
	BufferedInputStream bufferedinputstream = new BufferedInputStream(In);
	StringBuffer stringbuffer = new StringBuffer();
	int i = 0;
	int i_2_;
	while ((i_2_ = Io.r(bufferedinputstream)) != -1) {
	    stringbuffer.append((char) i_2_);
	    if (i_2_ == 0)
		break;
	    if (++i >= 255)
		throw new IOException("abnormal");
	}
	String string = stringbuffer.toString();
	Object object = null;
	int i_3_ = string.indexOf("type=");
	String string_4_;
	if (i_3_ <= 0)
	    string_4_ = "paintchat.text";
	else {
	    i_3_ += 6;
	    int i_5_ = string.indexOf('\"', i_3_);
	    if (i_5_ == -1)
		i_5_ = string.indexOf('\'', i_3_);
	    string_4_
		= i_5_ == -1 ? "paintchat.text" : string.substring(i_3_, i_5_);
	}
	if (string_4_.equals("paintchat.infomation"))
	    switchLocalAdminXML(string);
	else {
	    XMLTextTalker xmltexttalker = new XMLTextTalker(textServer, debug);
	    if (xmltexttalker != null)
		xmltexttalker.mStart(socket, bufferedinputstream, null, null);
	}
    }
    
    private void switchLocalAdmin(Socket socket, Res res) {
	try {
	    String string = serverStatus.getString("Admin_Password");
	    if (socket.getInetAddress().equals(InetAddress.getLocalHost())
		|| (string != null && string.length() > 0
		    && string.equals(res.get("password", "")))) {
		String string_6_ = res.get("request", "ping");
		StringBuffer stringbuffer = new StringBuffer();
		byte[] is = null;
		if (string_6_.equals("ping")) {
		    stringbuffer.append("response=ping ok\n");
		    stringbuffer.append("version=");
		    stringbuffer.append
			("(C)\u3057\u3043\u3061\u3083\u3093 PaintChatServer v3.57b");
		    stringbuffer.append("\n");
		    is = stringbuffer.toString().getBytes("UTF8");
		}
		if (string_6_.equals("exit")) {
		    stringbuffer.append("response=exit ok\n");
		    byte[] is_7_ = stringbuffer.toString().getBytes("UTF8");
		    Out = this.socket.getOutputStream();
		    Io.wShort(Out, is_7_.length);
		    Out.write(is_7_);
		    Out.flush();
		    closeSocket();
		    server.exitServer();
		    return;
		}
		if (is != null) {
		    Out = this.socket.getOutputStream();
		    Io.wShort(Out, is.length);
		    Out.write(is);
		    Out.flush();
		}
	    }
	} catch (IOException ioexception) {
	    debug.log(ioexception);
	}
	closeSocket();
    }
    
    private void switchLocalAdminXML(String string) throws IOException {
	int i = string.indexOf("request=");
	if (i < 0)
	    closeSocket();
	else {
	    i += 9;
	    int i_8_ = string.indexOf('\"', i);
	    if (i_8_ < 0) {
		i_8_ = string.indexOf('\'', i);
		if (i_8_ < 0) {
		    closeSocket();
		    return;
		}
	    }
	    if (i != i_8_) {
		String string_9_ = string.substring(i, i_8_);
		if (string_9_.equals("userlist")) {
		    Out = socket.getOutputStream();
		    StringBuffer stringbuffer = new StringBuffer();
		    textServer.getUserListXML(stringbuffer);
		    stringbuffer.append('\0');
		    Out.write(stringbuffer.toString().getBytes("UTF8"));
		    closeSocket();
		} else if (string_9_.equals("infomation")) {
		    Out = socket.getOutputStream();
		    closeSocket();
		} else
		    closeSocket();
	    }
	}
    }
    
    private void closeSocket() {
	try {
	    if (In != null) {
		In.close();
		In = null;
	    }
	    if (Out != null) {
		Out.close();
		Out = null;
	    }
	    if (socket != null) {
		socket.close();
		socket = null;
	    }
	} catch (IOException ioexception) {
	    debug.log(ioexception);
	}
    }
}
