/* PaintChatTalker - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;

import paintchat.Res;

import syi.util.ByteInputStream;
import syi.util.ByteStream;
import syi.util.Io;
import syi.util.ThreadPool;

public abstract class PaintChatTalker implements Runnable
{
    private boolean isLive = true;
    protected boolean canWrite = true;
    private boolean doWrite = false;
    protected int iSendInterval = 2000;
    private OutputStream Out;
    private InputStream In;
    private Socket socket;
    private ByteStream stm_buffer = new ByteStream();
    private Res status = null;
    long lTime;
    private boolean isConnect = false;
    
    private void mInitInside() throws IOException {
	if (In == null)
	    In = socket.getInputStream();
	if (Out == null)
	    Out = socket.getOutputStream();
	updateTimer();
	if (isConnect) {
	    w(98);
	    write(getStatus());
	    flush();
	    ByteStream bytestream = getWriteBuffer();
	    read(bytestream);
	    if (bytestream.size() > 0) {
		ByteInputStream byteinputstream = new ByteInputStream();
		byteinputstream.setByteStream(bytestream);
		getStatus().load(byteinputstream);
	    }
	} else {
	    write(getStatus());
	    flush();
	}
	mInit();
    }
    
    public void updateTimer() {
	lTime = System.currentTimeMillis();
    }
    
    public synchronized void mStart(Socket socket, InputStream inputstream,
				    OutputStream outputstream, Res res) {
	this.socket = socket;
	In = inputstream;
	Out = outputstream;
	status = res == null ? new Res() : res;
	socket.getInetAddress().getHostName();
	ThreadPool.poolStartThread(this, 'l');
    }
    
    public synchronized void mConnect(Socket socket, Res res)
	throws IOException {
	isConnect = true;
	mStart(socket, null, null, res);
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
    
    private void mDestroyInside() {
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
    
    protected abstract void mInit() throws IOException;
    
    protected abstract void mDestroy();
    
    protected abstract void mRead(ByteStream bytestream) throws IOException;
    
    protected abstract void mIdle(long l) throws IOException;
    
    protected abstract void mWrite() throws IOException;
    
    public void run() {
	long l = 0L;
	long l_0_ = 0L;
	try {
	    mInitInside();
	    canWrite = false;
	    while (isLive) {
		if (canRead(In)) {
		    stm_buffer.reset();
		    read(stm_buffer);
		    if (stm_buffer.size() > 0)
			mRead(stm_buffer);
		    stm_buffer.reset();
		} else {
		    long l_1_ = System.currentTimeMillis();
		    l = l_1_ - lTime;
		    if (l_1_ - l_0_ >= (long) iSendInterval) {
			l_0_ = l_1_;
			canWrite = true;
			mWrite();
			if (l >= 60000L) {
			    stm_buffer.reset();
			    write(stm_buffer);
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
    
    protected void read(ByteStream bytestream) throws IOException {
	int i = Io.readUShort(In);
	if (i > 0) {
	    bytestream.write(In, i);
	    updateTimer();
	}
    }
    
    protected void write(ByteStream bytestream) throws IOException {
	write(bytestream.getBuffer(), bytestream.size());
    }
    
    protected void write(Res res) throws IOException {
	if (res == null)
	    write(null, 0);
	else {
	    StringBuffer stringbuffer = new StringBuffer();
	    Enumeration enumeration = res.keys();
	    while (enumeration.hasMoreElements()) {
		String string = (String) enumeration.nextElement();
		if (stringbuffer.length() > 0)
		    stringbuffer.append('\n');
		stringbuffer.append(string);
		stringbuffer.append('=');
		stringbuffer.append(res.get(string));
	    }
	    byte[] is = stringbuffer.toString().getBytes("UTF8");
	    write(is, is.length);
	}
    }
    
    protected void write(byte[] is, int i) throws IOException {
	if (isLive) {
	    if (!canWrite)
		throw new IOException("write() bad timing");
	    Io.wShort(Out, i);
	    if (i > 0)
		Out.write(is, 0, i);
	    doWrite = true;
	    updateTimer();
	}
    }
    
    protected void w(int i) throws IOException {
	if (isLive) {
	    Out.write(i);
	    doWrite = true;
	    updateTimer();
	}
    }
    
    protected void flush() throws IOException {
	if (Out != null && doWrite)
	    Out.flush();
	doWrite = false;
    }
    
    public boolean canRead(InputStream inputstream) throws IOException {
	if (inputstream != null && inputstream.available() >= 2)
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
	stm_buffer.reset();
	return stm_buffer;
    }
    
    public synchronized InetAddress getAddress() {
	return !isLive || socket == null ? null : socket.getInetAddress();
    }
}
