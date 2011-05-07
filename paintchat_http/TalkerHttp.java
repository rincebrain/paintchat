/* TalkerHttp - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_http;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import syi.util.ByteStream;
import syi.util.ThreadPool;

public class TalkerHttp implements Runnable
{
    private HttpServer server;
    private HttpFiles files;
    private Socket socket;
    private InputStream In;
    private OutputStream Out;
    private Date date = new Date();
    private boolean isLineOut = false;
    private char[] bC = new char[350];
    private byte[] bB = new byte[350];
    private ByteStream bWork = new ByteStream();
    private String strMethod;
    private String strRequest;
    private static final byte[] strHttp = "HTTP/1.0".getBytes();
    private long sizeRequest = 0L;
    private String strIfMod = null;
    private int sizeIfMod = -1;
    private boolean isCash = true;
    private boolean isBody = true;
    private File fileRequest;
    private static final String STR_PRAGMA = "pragma";
    private static final String STR_LENGTH = "length=";
    private static final String STR_IF_MODIFIED_SINCE = "if-modified-since";
    private static final String STR_CONTENT_LENGTH = "content-length";
    private static final String STR_DEF_MIME = "application/octet-stream";
    private static final String[] strDef
	= { "GET", "HEAD", "/", "HTTP/1.0", "HTTP/1.1" };
    private static final String[] strDef2
	= { "pragma", "if-modified-since", "content-length" };
    private static final EOFException EOF = new EOFException();
    private static final int I_OK = 0;
    private static final int I_NOT_MODIFIED = 1;
    private static final int I_MOVED_PERMANENTLY = 2;
    private static final int I_NOT_FOUND = 3;
    private static final int I_SERVER_ERROR = 4;
    private static final int I_SERVICE_UNAVALIABLE = 5;
    private static final byte[][] BYTE_RESPONCE
	= { " 200 OK".getBytes(), " 304 Not Modified".getBytes(),
	    " 301 Moved Permanently".getBytes(), " 404 Not Found".getBytes(),
	    " 500 Internal Server Error".getBytes(),
	    " 503 Service Unavailable".getBytes() };
    private static final byte[] BYTE_DEF_HEADER
	= "Server: PaintChatHTTP/3.1".getBytes();
    private static final byte[] BYTE_CRLF = { 13, 10 };
    private static final byte[] BYTE_LOCATION = "Location: ".getBytes();
    private static final byte[] BYTE_LAST_MODIFIED
	= "Last-Modified: ".getBytes();
    private static final byte[] BYTE_DATE = "Date: ".getBytes();
    private static final byte[] BYTE_CONTENT_LENGTH
	= "Content-Length: ".getBytes();
    private static final byte[] BYTE_CONTENT_TYPE
	= "Content-Type: ".getBytes();
    private static final byte[] BYTE_CONNECTION_CLOSE
	= "Connection: close".getBytes();
    
    public TalkerHttp(Socket socket, HttpServer httpserver,
		      HttpFiles httpfiles) {
	server = httpserver;
	this.socket = socket;
	files = httpfiles;
	ThreadPool.poolStartThread(this, (String) null);
    }
    
    private void close() {
	if (socket != null) {
	    try {
		Out.close();
		Out = null;
	    } catch (IOException ioexception) {
		/* empty */
	    }
	    try {
		In.close();
		In = null;
	    } catch (IOException ioexception) {
		/* empty */
	    }
	    try {
		socket.close();
		socket = null;
	    } catch (IOException ioexception) {
		/* empty */
	    }
	}
    }
    
    private int getResponce() {
	try {
	    boolean bool
		= (sizeIfMod != -1 && (long) sizeIfMod != fileRequest.length()
		   || (strIfMod != null
		       && (HttpServer.fmt.parse(strIfMod).getTime()
			   == fileRequest.lastModified())));
	    return bool && isCash ? 1 : 0;
	} catch (RuntimeException runtimeexception) {
	    /* empty */
	} catch (java.text.ParseException parseexception) {
	    /* empty */
	}
	return 0;
    }
    
    public byte[] getSince(long l) {
	date.setTime(l);
	return HttpServer.fmt.format(date).getBytes();
    }
    
    private String getString(char[] cs, int i) {
	if (i == 0)
	    return null;
	for (int i_0_ = 0; i_0_ < strDef.length; i_0_++) {
	    String string = strDef[i_0_];
	    int i_1_ = string.length();
	    if (i == i_1_) {
		int i_2_;
		for (i_2_ = 0; i_2_ < i_1_; i_2_++) {
		    if (cs[i_2_] != string.charAt(i_2_))
			break;
		}
		if (i_2_ == i_1_)
		    return string;
	    }
	}
	return new String(cs, 0, i);
    }
    
    private char r() throws IOException {
	int i = In.read();
	if (i == -1)
	    throw EOF;
	return (char) i;
    }
    
    private void readMethod() throws IOException {
	try {
	    for (;;) {
		String string;
		if ((string = readSpace()) != null) {
		    strMethod = string;
		    break;
		}
		if (isLineOut)
		    throw EOF;
	    }
	    for (;;) {
		String string;
		if ((string = readSpace()) != null) {
		    strRequest = string;
		    break;
		}
		if (isLineOut)
		    throw EOF;
	    }
	    String string;
	    while ((string = readSpace()) == null) {
		if (isLineOut)
		    throw EOF;
	    }
	    if (!isLineOut) {
		for (int i = 0; i < 4096; i++) {
		    if (r() == '\n')
			break;
		}
	    }
	} catch (IOException ioexception) {
	    close();
	    throw ioexception;
	}
    }
    
    private boolean readOption() throws IOException {
	boolean bool = true;
	char[] cs = bC;
	int i = 0;
	int i_3_ = cs.length;
	for (int i_4_ = 0; i_4_ < i_3_; i_4_++) {
	    char c = r();
	    if (c == '\n') {
		bool = false;
		break;
	    }
	    if (!Character.isWhitespace(c)) {
		if (c == ':') {
		    bool = false;
		    break;
		}
		if (c == '+')
		    c = ' ';
		if (c == '%')
		    c = (char) (Character.digit(r(), 16) << 4
				| Character.digit(r(), 16));
		cs[i++] = Character.toLowerCase(c);
	    }
	}
	if (bool) {
	    int i_5_;
	    for (i_5_ = 0; i_5_ < 4096; i_5_++) {
		if (r() == '\n')
		    break;
	    }
	    if (i_5_ >= 4096)
		throw EOF;
	    return true;
	}
	if (i == 0)
	    return false;
	String string = getString(cs, i);
	i = 0;
	bool = true;
	for (int i_6_ = 0; i_6_ < i_3_; i_6_++) {
	    char c = r();
	    if (c == '\n') {
		bool = false;
		break;
	    }
	    if ((i != 0 || !Character.isWhitespace(c)) && c != '\r') {
		if (c == '+')
		    c = ' ';
		if (c == '%')
		    c = (char) (Character.digit(r(), 16) << 4
				| Character.digit(r(), 16));
		cs[i++] = c;
	    }
	}
	if (bool) {
	    int i_7_;
	    for (i_7_ = 0; i_7_ < 4096; i_7_++) {
		if (r() == '\n')
		    break;
	    }
	    if (i_7_ >= 4096)
		throw EOF;
	}
	switchOption(string, i <= 0 ? null : new String(cs, 0, i));
	return true;
    }
    
    private String readSpace() throws IOException {
	boolean bool = false;
	int i = -1;
	int i_8_ = 0;
	int i_9_ = 1024;
	bWork.reset();
	for (/**/; i_8_ < i_9_; i_8_++) {
	    i = In.read();
	    if (i != 13) {
		if (i == -1 || Character.isWhitespace((char) i))
		    break;
		switch (i) {
		case 37: {
		    int i_10_ = In.read();
		    int i_11_ = In.read();
		    if (i_10_ == -1 || i_11_ == -1)
			throw EOF;
		    bWork.write(Character.digit((char) i_10_, 16) << 4
				| Character.digit((char) i_11_, 16));
		    break;
		}
		default:
		    bWork.write(i);
		}
	    }
	}
	isLineOut = i == -1 || i == 10;
	if (!bool && i == -1 || i_8_ >= 1024)
	    throw EOF;
	return new String(bWork.getBuffer(), 0, bWork.size(), "UTF8");
    }
    
    private void rRun() throws Throwable {
	In = socket.getInputStream();
	Out = socket.getOutputStream();
	try {
	    readMethod();
	    while (readOption()) {
		/* empty */
	    }
	    server.debug.log(socket.getLocalAddress().getHostName() + ' '
			     + strRequest);
	    String string = files.uriToPath(strRequest);
	    if (files.needMove(string))
		sendMessage(2, files.addIndex(string));
	    else {
		fileRequest = files.getFile(string);
		sendMessage(getResponce(), string);
	    }
	} catch (EOFException eofexception) {
	    /* empty */
	} catch (java.io.FileNotFoundException filenotfoundexception) {
	    sendMessage(3, strRequest);
	} catch (Throwable throwable) {
	    server.debug.log(throwable.getMessage());
	}
	close();
    }
    
    public void run() {
	try {
	    rRun();
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    private void sendMessage(int i, String string) throws IOException {
	if (server.debug.bool_debug)
	    server.debug.log(string + ' ' + i);
	Out.write(strHttp);
	Out.write(BYTE_RESPONCE[i]);
	Out.write(BYTE_CRLF);
	Out.write(BYTE_DEF_HEADER);
	Out.write(BYTE_CRLF);
	byte[] is = getSince(System.currentTimeMillis());
	Out.write(BYTE_DATE);
	Out.write(is);
	Out.write(BYTE_CRLF);
	switch (i) {
	case 2:
	    w(BYTE_LOCATION);
	    w(string);
	    w(BYTE_CONNECTION_CLOSE);
	    w(BYTE_CRLF);
	    w(BYTE_CRLF);
	    break;
	case 1:
	    w(BYTE_CONNECTION_CLOSE);
	    w(BYTE_CRLF);
	    w(BYTE_CRLF);
	    break;
	case 0:
	    is = getSince(fileRequest.lastModified());
	    w(BYTE_LAST_MODIFIED);
	    w(is);
	    w(BYTE_CRLF);
	    w(BYTE_CONTENT_TYPE);
	    writeMime(string);
	    w(BYTE_CRLF);
	    w(BYTE_CONTENT_LENGTH);
	    w(String.valueOf(fileRequest.length()));
	    w(BYTE_CRLF);
	    w(BYTE_CONNECTION_CLOSE);
	    w(BYTE_CRLF);
	    w(BYTE_CRLF);
	    if (isBody)
		wFile(fileRequest);
	    break;
	default: {
	    byte[] is_12_ = strRequest.getBytes();
	    w(BYTE_CONTENT_TYPE);
	    w("text/html");
	    w(BYTE_CRLF);
	    w(BYTE_CONTENT_LENGTH);
	    w(String.valueOf(files.getErrorMessageSize(i) + is_12_.length));
	    w(BYTE_CRLF);
	    w(BYTE_CONNECTION_CLOSE);
	    w(BYTE_CRLF);
	    w(BYTE_CRLF);
	    if (isBody)
		files.getErrorMessage(Out, i, is_12_, 0, is_12_.length);
	}
	}
	Out.flush();
    }
    
    private void switchOption(String string, String string_13_)
	throws RuntimeException {
	if (server.debug.bool_debug)
	    server.debug.log(string + ": " + string_13_);
	if (string != null && string_13_ != null && string.length() > 0
	    && string_13_.length() > 0) {
	    if (string.equals("pragma"))
		isCash = string_13_.indexOf("no-cache") < 0;
	    else if (string.equals("content-length")) {
		try {
		    int i = string_13_.length();
		    int i_14_;
		    for (i_14_ = 0; i_14_ < i; i_14_++) {
			if (!Character.isDigit(string_13_.charAt(i_14_)))
			    break;
		    }
		    if (i != i_14_)
			string_13_ = string_13_.substring(0, i_14_);
		    sizeRequest = (long) Integer.parseInt(string_13_);
		} catch (NumberFormatException numberformatexception) {
		    sizeRequest = 0L;
		}
	    } else if (string.equals("if-modified-since")) {
		try {
		    int i = string_13_.indexOf(';');
		    strIfMod
			= i != -1 ? string_13_.substring(0, i) : string_13_;
		    if (i >= 0) {
			i = string_13_.indexOf("length=", i + 1);
			if (i != -1) {
			    i += "length=".length();
			    if (i < string_13_.length())
				sizeIfMod
				    = Integer.parseInt(string_13_.substring(i),
						       10);
			}
		    }
		} catch (Exception exception) {
		    exception.printStackTrace();
		    strIfMod = null;
		    sizeIfMod = -1;
		}
	    }
	}
    }
    
    private void w(byte[] is) throws IOException {
	Out.write(is);
    }
    
    private void w(String string) throws IOException {
	int i = string.length();
	string.getChars(0, string.length(), bC, 0);
	for (int i_15_ = 0; i_15_ < i; i_15_++)
	    bB[i_15_] = (byte) bC[i_15_];
	Out.write(bB, 0, i);
    }
    
    private void wFile(File file) throws IOException {
	if (file.length() > 0L) {
	    FileInputStream fileinputstream = new FileInputStream(file);
	    int i;
	    while ((i = fileinputstream.read(bB)) != -1)
		Out.write(bB, 0, i);
	    fileinputstream.close();
	}
    }
    
    private void wln(byte[] is) throws IOException {
	w(is);
	w(BYTE_CRLF);
    }
    
    private void writeMime(String string) throws IOException {
	int i = string.lastIndexOf('.');
	String string_16_;
	if (i < 0)
	    string_16_ = "application/octet-stream";
	else
	    string_16_ = HttpServer.Mime.getString(string.substring(i + 1),
						   "application/octet-stream");
	w(string_16_);
    }
}
