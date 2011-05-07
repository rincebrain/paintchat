/* TLine - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_client;
import java.io.IOException;
import java.util.zip.Inflater;

import paintchat.M;

import paintchat_server.PaintChatTalker;

import syi.util.ByteStream;
import syi.util.ThreadPool;

public class TLine extends PaintChatTalker
{
    public Data data;
    private ByteStream bSendCash = new ByteStream();
    private ByteStream stmIn = new ByteStream();
    private ByteStream workSend = new ByteStream();
    private M mgOut = null;
    private M mgDraw = null;
    private boolean isCompress = false;
    private boolean isRunDraw = false;
    Inflater inflater = new Inflater(false);
    
    public TLine(Data data, M m) {
	this.data = data;
	mgDraw = m;
    }
    
    protected void mDestroy() {
	isRunDraw = false;
	iSendInterval = 0;
    }
    
    protected void mRead(ByteStream bytestream) throws IOException {
	if (bytestream.size() <= 1) {
	    switch (bytestream.getBuffer()[0]) {
	    case 0: {
		ByteStream bytestream_0_;
/*		MONITORENTER (bytestream_0_ = stmIn);
		MISSING MONITORENTER*/
		synchronized (bytestream_0_) {
		    stmIn.w2(0);
		    break;
		}
	    }
	    case 1:
		isCompress = true;
		break;
	    }
	} else {
	    do {
		try {
		    int i = bytestream.size();
		    if (isCompress) {
			isCompress = false;
			if (inflater == null)
			    inflater = new Inflater(false);
			inflater.reset();
			inflater.setInput(bytestream.getBuffer(), 0, i);
			TLine tline_1_;
/*			MONITORENTER (tline_1_ = this);
			MISSING MONITORENTER*/
			synchronized (tline_1_) {
			    byte[] is = workSend.getBuffer();
			    ByteStream bytestream_2_;
/*			    MONITORENTER (bytestream_2_ = stmIn);
			    MISSING MONITORENTER*/
			    synchronized (bytestream_2_) {
				while (!inflater.needsInput()) {
				    int i_3_
					= inflater.inflate(is, 0, is.length);
				    stmIn.write(is, 0, i_3_);
				}
			    }
			    break;
			}
		    }
		    if (inflater != null) {
			inflater.end();
			inflater = null;
		    }
		    ByteStream bytestream_4_;
/*		    MONITORENTER (bytestream_4_ = stmIn);
		    MISSING MONITORENTER*/
		    synchronized (bytestream_4_) {
			bytestream.writeTo(stmIn);
		    }
		} catch (Exception exception) {
		    exception.printStackTrace();
		}
	    } while (false);
	}
    }
    
    public void mInit() {
	/* empty */
    }
    
    protected void mIdle(long l) throws IOException {
	/* empty */
    }
    
    protected void mWrite() throws IOException {
	if (bSendCash.size() > 0) {
	    ByteStream bytestream;
/*	    MONITORENTER (bytestream = bSendCash);
	    MISSING MONITORENTER*/
	    synchronized (bytestream) {
		this.write(bSendCash);
		bSendCash.reset();
	    }
	}
    }
    
    public void send(M m) {
	object = object_5_;
	break while_0_;
    }
    
    public void run() {
	object = object_7_;
	break while_2_;
    }
    
    public synchronized void mRStop() {
	send(null);
	this.mStop();
    }
}
