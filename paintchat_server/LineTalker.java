/* LineTalker - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.IOException;

import paintchat.M;
import paintchat.debug.DebugListener;

import syi.util.ByteStream;
import syi.util.VectorBin;

public class LineTalker extends PaintChatTalker
{
    ByteStream lines_send = new ByteStream();
    VectorBin lines_log = new VectorBin();
    LineServer server;
    DebugListener debug;
    private M mgRead = new M();
    private ByteStream workReceive = new ByteStream();
    private ByteStream workReceive2 = new ByteStream();
    private M mgSend = null;
    private int countDraw = 0;
    
    public LineTalker(LineServer lineserver, DebugListener debuglistener) {
	server = lineserver;
	debug = debuglistener;
    }
    
    public void send(ByteStream bytestream, M m, ByteStream bytestream_0_) {
	if (this.isValidate()) {
	    try {
		if (lines_send.size() >= 65535) {
		    lines_send = null;
		    throw new IOException("client error");
		}
		int i = 0;
		int i_1_ = bytestream.size();
		byte[] is = bytestream.getBuffer();
		ByteStream bytestream_2_;
		MONITORENTER (bytestream_2_ = lines_send);
		MISSING MONITORENTER
		synchronized (bytestream_2_) {
		    while (i + 1 < i_1_) {
			i += m.set(is, i);
			m.get(lines_send, bytestream_0_, mgSend);
			if (mgSend == null)
			    mgSend = new M();
			mgSend.set(m);
		    }
		}
	    } catch (IOException ioexception) {
		debug.log(ioexception);
		this.mStop();
	    }
	}
    }
    
    protected void mInit() throws IOException {
	iSendInterval = 4000;
	server.addTalker(this);
	ByteStream bytestream;
	MONITORENTER (bytestream = lines_send);
	MISSING MONITORENTER
	synchronized (bytestream) {
	    lines_send.w2(1);
	    lines_send.write(0);
	}
    }
    
    protected void mRead(ByteStream bytestream) throws IOException {
	int i = bytestream.size();
	if (i > 0) {
	    if (i <= 2 && bytestream.getBuffer()[0] == 0)
		server.removeTalker(this);
	    else {
		workReceive.reset();
		while (bytestream.size() >= 2) {
		    i = mgRead.set(bytestream);
		    if (i < 0)
			return;
		    countDraw++;
		    bytestream.reset(i);
		    mgRead.get(workReceive, workReceive2, null);
		    if (mgRead.iHint == 10) {
			server.addClear(this, workReceive);
			workReceive.reset();
		    }
		}
		if (workReceive.size() > 0)
		    server.addLine(this, workReceive);
	    }
	}
    }
    
    protected void mIdle(long l) throws IOException {
	/* empty */
    }
    
    protected void mWrite() throws IOException {
	if (lines_log != null) {
	    if (lines_log.size() <= 0)
		lines_log = null;
	    else {
		mSendFlag(1);
		byte[] is = lines_log.get(0);
		this.write(is, is.length);
		lines_log.remove(1);
	    }
	} else if (lines_send.size() > 2) {
	    ByteStream bytestream = this.getWriteBuffer();
	    ByteStream bytestream_3_;
	    MONITORENTER (bytestream_3_ = lines_send);
	    MISSING MONITORENTER
	    synchronized (bytestream_3_) {
		lines_send.writeTo(bytestream);
		lines_send.reset();
	    }
	    this.write(bytestream);
	    bytestream.reset();
	}
    }
    
    protected void mDestroy() {
	lines_send = null;
	lines_log = null;
	server = null;
	debug = null;
    }
    
    public VectorBin getLogArray() {
	return lines_log;
    }
    
    private void mSendFlag(int i) throws IOException {
	ByteStream bytestream = this.getWriteBuffer();
	bytestream.write(i);
	this.write(bytestream);
    }
    
    public int getDrawCount() {
	return countDraw;
    }
}
