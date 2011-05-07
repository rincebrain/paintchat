/* LocalAdmin - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat.admin;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;

import paintchat.Res;

import syi.util.Io;

public class LocalAdmin
{
    private Res status;
    private InetAddress addr;
    private int iPort;
    private byte[] bRes;
    
    public LocalAdmin(Res res, InetAddress inetaddress, int i) {
	status = res;
	if (res != null)
	    res.put("local_admin", "t");
	addr = inetaddress;
	iPort = i <= 0 ? 41411 : i;
    }
    
    private void doConnect(String string) {
	Object object = null;
	Object object_0_ = null;
	Object object_1_ = null;
	bRes = null;
	try {
	    StringBuffer stringbuffer = new StringBuffer();
	    Enumeration enumeration = status.keys();
	    while (enumeration.hasMoreElements()) {
		String string_2_ = enumeration.nextElement().toString();
		if (string_2_.length() > 0) {
		    stringbuffer.append(string_2_);
		    stringbuffer.append('=');
		    stringbuffer.append(status.get(string_2_));
		    stringbuffer.append('\n');
		}
	    }
	    stringbuffer.append("request=");
	    stringbuffer.append(string);
	    byte[] is = stringbuffer.toString().getBytes("UTF8");
	    Object object_3_ = null;
	    Socket socket = new Socket(addr, iPort);
	    InputStream inputstream = socket.getInputStream();
	    OutputStream outputstream = socket.getOutputStream();
	    outputstream.write(98);
	    Io.wShort(outputstream, is.length);
	    outputstream.write(is);
	    outputstream.flush();
	    is = null;
	    int i = Io.readUShort(inputstream);
	    if (i > 0) {
		is = new byte[i];
		Io.rFull(inputstream, is, 0, i);
	    }
	    try {
		inputstream.close();
		outputstream.close();
		socket.close();
	    } catch (java.io.IOException ioexception) {
		/* empty */
	    }
	    bRes = is;
	} catch (java.io.IOException ioexception) {
	    /* empty */
	}
    }
    
    public String getString(String string) {
	try {
	    doConnect(string);
	    return (bRes != null && bRes.length > 0 ? new String(bRes, "UTF8")
		    : "");
	} catch (Exception exception) {
	    exception.printStackTrace();
	    return "";
	}
    }
    
    public byte[] getBytes(String string) {
	try {
	    doConnect(string);
	    return bRes;
	} catch (Exception exception) {
	    exception.printStackTrace();
	    return null;
	}
    }
}
