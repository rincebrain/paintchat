/* TText - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_client;
import java.io.IOException;

import paintchat.MgText;
import paintchat.Res;

import paintchat_server.PaintChatTalker;

import syi.util.ByteInputStream;
import syi.util.ByteStream;

public class TText extends PaintChatTalker
{
    private Pl pl;
    private Data data;
    private MgText mg = new MgText();
    private ByteInputStream bin = new ByteInputStream();
    private ByteStream stm = new ByteStream();
    private Res names = new Res();
    
    public TText(Pl pl, Data data) {
	this.pl = pl;
	this.data = data;
	iSendInterval = 1000;
    }
    
    public void mInit() {
	/* empty */
    }
    
    protected synchronized void mDestroy() {
	try {
	    MgText mgtext = new MgText(0, (byte) 2, (String) null);
	    stm.reset();
	    mgtext.getData(stm, false);
	    this.write(stm);
	    stm.reset();
	    this.flush();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    protected void mRead(ByteStream bytestream) throws IOException {
	int i = 0;
	int i_0_ = bytestream.size();
	bin.setByteStream(bytestream);
	while (i < i_0_) {
	    i += mg.setData(bin);
	    String string = mg.toString();
	    Integer integer = new Integer(mg.ID);
	    switch (mg.head) {
	    case 1:
		names.put(integer, string);
		pl.addInOut(string, true);
		break;
	    case 2:
		names.remove(integer);
		pl.addInOut(string, false);
		break;
	    case 0:
		pl.addText((mg.bName != null ? mg.getUserName()
			    : (String) names.get(integer)),
			   string, true);
		pl.dSound(1);
		break;
	    case 102:
		data.mPermission(string);
		break;
	    default:
		pl.addText(null, string, true);
		pl.dSound(1);
	    }
	}
    }
    
    protected void mIdle(long l) throws IOException {
	/* empty */
    }
    
    protected synchronized void mWrite() throws IOException {
	if (stm.size() > 0) {
	    this.write(stm);
	    stm.reset();
	}
    }
    
    public synchronized void send(MgText mgtext) {
	try {
	    mgtext.getData(stm, false);
	} catch (IOException ioexception) {
	    /* empty */
	}
    }
    
    public synchronized void mRStop() {
	try {
	    canWrite = true;
	    stm.reset();
	    new MgText(0, (byte) 2, (String) null).getData(stm, false);
	    this.write(stm);
	    this.flush();
	    this.mStop();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
}
