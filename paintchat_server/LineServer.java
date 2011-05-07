/* LineServer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.net.InetAddress;

import paintchat.Config;
import paintchat.M;
import paintchat.debug.Debug;

import syi.util.ByteStream;

public class LineServer
{
    private boolean isLive = true;
    private LineTalker[] talkers = new LineTalker[15];
    private int countTalker = 0;
    private boolean isCash = true;
    private ByteStream bCash = new ByteStream(100000);
    private ByteStream bWork = new ByteStream();
    private M mgCash = null;
    private M mgWork = new M();
    private PchOutputStreamForServer bLog;
    private Debug debug;
    private Server server;
    public Config config;
    
    public synchronized void mInit(Config config, Debug debug, Server server) {
	this.debug = debug;
	this.config = config;
	this.server = server;
	bLog = new PchOutputStreamForServer(this.config);
	isCash = config.getBool("Server_Cash_Line", true);
    }
    
    public synchronized void mStop() {
	if (isLive) {
	    isLive = false;
	    for (int i = 0; i < countTalker; i++) {
		LineTalker linetalker = talkers[i];
		if (linetalker != null)
		    linetalker.mStop();
	    }
	    if (bLog != null) {
		writeLog();
		bLog.close();
	    }
	}
    }
    
    protected void finalize() throws Throwable {
	isLive = false;
    }
    
    public synchronized void addTalker(LineTalker linetalker) {
	LineTalker[] linetalkers = talkers;
	if (countTalker >= talkers.length) {
	    LineTalker[] linetalkers_0_ = new LineTalker[countTalker + 1];
	    System.arraycopy(linetalkers, 0, linetalkers_0_, 0, countTalker);
	    linetalkers = linetalkers_0_;
	}
	linetalkers[countTalker] = linetalker;
	countTalker++;
	if (talkers != linetalkers)
	    talkers = linetalkers;
	linetalker.send(bCash, mgWork, bWork);
	bLog.getLog(linetalker.getLogArray());
    }
    
    public synchronized void removeTalker(LineTalker linetalker) {
	int i = countTalker;
	for (int i_1_ = 0; i_1_ < i; i_1_++) {
	    if (linetalker == talkers[i_1_]) {
		removeTalker(i_1_);
		break;
	    }
	}
    }
    
    private void removeTalker(int i) {
	LineTalker linetalker = talkers[i];
	talkers[i] = null;
	if (linetalker != null)
	    linetalker.mStop();
	if (i + 1 < countTalker) {
	    System.arraycopy(talkers, i + 1, talkers, i,
			     countTalker - (i + 1));
	    talkers[countTalker - 1] = null;
	}
	countTalker--;
    }
    
    public synchronized void addLine(LineTalker linetalker,
				     ByteStream bytestream) {
	try {
	    int i = 0;
	    while (i < countTalker) {
		LineTalker linetalker_2_ = talkers[i];
		if (linetalker_2_ == null || !linetalker_2_.isValidate()) {
		    removeTalker(i);
		    i = 0;
		} else {
		    if (linetalker_2_ != linetalker)
			linetalker_2_.send(bytestream, mgWork, bWork);
		    i++;
		}
	    }
	    addCash(bytestream, false);
	} catch (Throwable throwable) {
	    debug.log(throwable.getMessage());
	}
    }
    
    public void addClear(LineTalker linetalker, ByteStream bytestream) {
	addLine(linetalker, bytestream);
	server.sendClearMessage(linetalker.getAddress());
	newLog();
    }
    
    private void addCash(ByteStream bytestream, boolean bool) {
	if (isCash && bytestream != null) {
	    int i = bytestream.size();
	    if (i > 0) {
		if (bool || i + bCash.size() > 60000)
		    writeLog();
		try {
		    int i_3_ = 0;
		    byte[] is = bytestream.getBuffer();
		    while (i_3_ < i) {
			i_3_ += mgWork.set(is, i_3_);
			mgWork.get(bCash, bWork, mgCash);
			if (mgCash == null)
			    mgCash = new M();
			mgCash.set(mgWork);
		    }
		} catch (RuntimeException runtimeexception) {
		    debug.log(runtimeexception);
		}
	    }
	}
    }
    
    private void writeLog() {
	if (bCash.size() > 0) {
	    bLog.write(bCash.getBuffer(), 0, bCash.size());
	    bCash.reset();
	    mgCash = null;
	}
    }
    
    public int getUserCount() {
	return countTalker;
    }
    
    public synchronized LineTalker getTalker(InetAddress inetaddress) {
	for (int i = 0; i < countTalker; i++) {
	    LineTalker linetalker = talkers[i];
	    if (linetalker != null
		&& inetaddress.equals(linetalker.getAddress()))
		return linetalker;
	}
	return null;
    }
    
    public synchronized LineTalker getTalkerAt(int i) {
	if (i < 0 || i >= countTalker)
	    return null;
	return talkers[i];
    }
    
    public synchronized void newLog() {
	writeLog();
	bLog.newLog();
    }
}
