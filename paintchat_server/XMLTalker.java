/* XMLTalker - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import paintchat.Res;

import syi.util.ByteInputStream;
import syi.util.ByteStream;
import syi.util.Io;
import syi.util.ThreadPool;

public abstract class XMLTalker extends DefaultHandler implements Runnable
{
    private boolean isLive = true;
    private boolean isInit = false;
    private boolean canWrite = true;
    private boolean doWrite = false;
    protected int iSendInterval = 2000;
    private BufferedWriter Out;
    private BufferedInputStream In;
    private Socket socket;
    private ByteStream stm_buffer = new ByteStream();
    private Res status;
    long lTime;
    private SAXParser saxParser = null;
    private String strTag;
    private Res res_att = new Res();
    private final String[] strTags
	= { "talk", "in", "leave", "infomation", "script", "admin" };
    private final String[] strStartTags
	= { "<talk", "<in", "<leave", "<infomation", "<script", "<admin" };
    private final int[] strHints = { 0, 1, 2, 6, 8, 102 };
    
    private synchronized void mInitInside() throws Throwable {
	if (!isInit) {
	    isInit = true;
	    if (In == null)
		In = new BufferedInputStream(socket.getInputStream());
	    if (Out == null)
		Out = new BufferedWriter(new OutputStreamWriter
					 (socket.getOutputStream(), "UTF8"));
	    updateTimer();
	}
    }
    
    public void updateTimer() {
	lTime = System.currentTimeMillis();
    }
    
    public synchronized void mStart(Socket socket, InputStream inputstream,
				    OutputStream outputstream, Res res) {
	try {
	    this.socket = socket;
	    if (inputstream != null)
		In = (inputstream instanceof BufferedInputStream
		      ? (BufferedInputStream) inputstream
		      : new BufferedInputStream(inputstream));
	    if (outputstream != null)
		Out = new BufferedWriter(new OutputStreamWriter(outputstream));
	    status = res == null ? new Res() : res;
	    ThreadPool.poolStartThread(this, 'l');
	} catch (Throwable throwable) {
	    mStop();
	}
    }
    
    public synchronized void mStop() {
	if (isLive) {
	    isLive = false;
	    canWrite = true;
	    mDestroy();
	    canWrite = false;
	    mDestroyInside();
	}
    }
    
    private synchronized void mDestroyInside() {
	try {
	    if (Out != null)
		Out.close();
	} catch (IOException ioexception) {
	    /* empty */
	}
	Out = null;
	try {
	    if (In != null)
		In.close();
	} catch (IOException ioexception) {
	    /* empty */
	}
	In = null;
	try {
	    if (socket != null)
		socket.close();
	} catch (IOException ioexception) {
	    /* empty */
	}
	socket = null;
    }
    
    private void mParseXML(InputStream inputstream) {
	try {
	    if (saxParser == null)
		saxParser = SAXParserFactory.newInstance().newSAXParser();
	    saxParser.parse(inputstream, this);
	} catch (Exception exception) {
	    mStop();
	}
    }
    
    protected abstract void mInit() throws IOException;
    
    protected abstract void mDestroy();
    
    protected abstract void mRead(String string, String string_0_, Res res)
	throws IOException;
    
    protected abstract void mWrite() throws IOException;
    
    public void run() {
	try {
	    mInitInside();
	    ByteInputStream byteinputstream = new ByteInputStream();
	    long l = 0L;
	    long l_1_ = 0L;
	    canWrite = false;
	    while (isLive) {
		if (canRead(In)) {
		    stm_buffer.reset();
		    read(stm_buffer);
		    byteinputstream.setByteStream(stm_buffer);
		    mParseXML(byteinputstream);
		    stm_buffer.reset();
		} else {
		    long l_2_ = System.currentTimeMillis();
		    l = l_2_ - lTime;
		    if (l_2_ - l_1_ >= (long) iSendInterval) {
			l_1_ = l_2_;
			canWrite = true;
			mWrite();
			if (l >= 60000L) {
			    stm_buffer.reset();
			    write("ping", null);
			}
			canWrite = false;
			flush();
		    }
		    Thread.currentThread();
		    Thread.sleep((long) (l < 1000L ? 200 : l < 5000L ? 400
					 : l < 10000L ? 600 : l < 20000L ? 1200
					 : 2400));
		}
	    }
	} catch (InterruptedException interruptedexception) {
	    /* empty */
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
	mStop();
    }
    
    private void setAtt(Attributes attributes) {
	int i = attributes.getLength();
	res_att.clear();
	try {
	    for (int i_3_ = 0; i_3_ < i; i_3_++)
		res_att.put(attributes.getQName(i_3_),
			    attributes.getValue(i_3_));
	} catch (RuntimeException runtimeexception) {
	    res_att.clear();
	}
    }
    
    protected void write(Res res) throws IOException {
	StringBuffer stringbuffer = new StringBuffer();
	Enumeration enumeration = res.keys();
	while (enumeration.hasMoreElements()) {
	    String string = (String) enumeration.nextElement();
	    if (stringbuffer.length() > 0)
		stringbuffer.append('\n');
	    write(string, res.get(string));
	}
    }
    
    protected void write(String string, String string_4_) throws IOException {
	write(string, string_4_, null);
    }
    
    protected void write(String string, String string_5_, String string_6_)
	throws IOException {
	if (isLive && string != null && string.length() > 0) {
	    if (!canWrite)
		throw new IOException("write() bad timing");
	    if (string_5_ == null || string_5_.length() <= 0) {
		if (string_6_ != null && string_6_.length() > 0)
		    Out.write(String.valueOf('<') + string + ' ' + string_6_
			      + "/>");
		else
		    Out.write(String.valueOf('<') + string + "/>");
	    } else {
		if (string_6_ != null && string_6_.length() > 0) {
		    System.out.println(String.valueOf('<') + string + ' '
				       + string_6_ + '>');
		    Out.write(String.valueOf('<') + string + ' ' + string_6_
			      + '>');
		} else
		    Out.write(String.valueOf('<') + string + '>');
		Out.write(string_5_);
		Out.write("</" + string + '>');
	    }
	    doWrite = true;
	    updateTimer();
	}
    }
    
    protected void read(ByteStream bytestream) throws IOException {
	while (isLive) {
	    int i = Io.r(In);
	    if (i == 0)
		break;
	    bytestream.write(i);
	}
    }
    
    protected void flush() throws IOException {
	if (Out != null && doWrite) {
	    Out.write('\0');
	    Out.flush();
	}
	doWrite = false;
    }
    
    public boolean canRead(InputStream inputstream) throws IOException {
	if (inputstream != null && inputstream.available() >= 1)
	    return true;
	return false;
    }
    
    public Res getStatus() {
	return status;
    }
    
    public boolean isValidate() {
	return isLive;
    }
    
    public ByteStream getWriteBuffer() throws IOException {
	if (!canWrite)
	    throw new IOException("getWriteBuffer() bad timing");
	return stm_buffer;
    }
    
    public synchronized InetAddress getAddress() {
	return !isLive || socket == null ? null : socket.getInetAddress();
    }
    
    public void characters(char[] cs, int i, int i_7_) throws SAXException {
	if (strTag != null) {
	    try {
		mRead(strTag, new String(cs, i, i_7_), res_att);
	    } catch (IOException ioexception) {
		mStop();
	    }
	}
    }
    
    public void endElement
	(String string, String string_8_, String string_9_)
	throws SAXException {
	strTag = null;
    }
    
    public void startElement(String string, String string_10_,
			     String string_11_,
			     Attributes attributes) throws SAXException {
	try {
	    strTag = string_11_;
	    if (string_11_ != null && string_11_.length() > 0) {
		if (string_11_.equals("ping"))
		    strTag = null;
		else if (!string_11_.equals("talk")) {
		    setAtt(attributes);
		    if (string_11_.equals("initialize")) {
			setStatus(attributes);
			mInit();
		    } else if (string_11_.equals("leave")) {
			mRead(string_11_, null, null);
			mStop();
		    }
		}
	    }
	} catch (Throwable throwable) {
	    mStop();
	}
    }
    
    public void setStatus(Attributes attributes) {
	if (attributes != null) {
	    Res res = getStatus();
	    int i = attributes.getLength();
	    for (int i_12_ = 0; i_12_ < i; i_12_++) {
		String string = attributes.getQName(i_12_);
		String string_13_ = attributes.getValue(i_12_);
		if (string != null && string.length() > 0
		    && string_13_ != null)
		    res.put(string, string_13_);
	    }
	}
    }
    
    protected String hintToString(int i) {
	for (int i_14_ = 0; i_14_ < strHints.length; i_14_++) {
	    if (strHints[i_14_] == i)
		return strTags[i_14_];
	}
	return strTags[0];
    }
    
    protected int strToHint(String string) {
	if (string == null || string.length() <= 0 || string.equals("talk"))
	    return 0;
	int i = strTags.length;
	for (int i_15_ = 1; i_15_ < i; i_15_++) {
	    if (strTags[i_15_].equals(string))
		return strHints[i_15_];
	}
	return 0;
    }
}
