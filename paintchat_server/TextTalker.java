/* TextTalker - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.IOException;

import paintchat.MgText;
import paintchat.debug.DebugListener;

import syi.util.ByteInputStream;
import syi.util.ByteStream;
import syi.util.Vector2;

public class TextTalker extends PaintChatTalker implements TextTalkerListener
{
    private Vector2 sendText = new Vector2();
    private MgText[] sendUpdate = null;
    private TextServer server;
    private DebugListener debug;
    private MgText mgName;
    private MgText mgRead = new MgText();
    private ByteInputStream bin = new ByteInputStream();
    private int countSpeak = 0;
    private boolean isGuest = false;
    private boolean isKill = false;
    
    public TextTalker(TextServer textserver, DebugListener debuglistener) {
	debug = debuglistener;
	server = textserver;
    }
    
    protected void mInit() throws IOException {
	mgName = new MgText(0, (byte) 1, this.getStatus().get("name"));
	mgName.setUserName(mgName.toString());
	isGuest = this.getStatus().getBool("guest", false);
	server.addTalker(this);
	String string = ("User login name=" + mgName.getUserName() + " host="
			 + this.getAddress());
	server.writeLog(string);
	debug.log(string);
    }
    
    protected void mDestroy() {
	if (mgName.getUserName().length() > 0) {
	    String string = ("User logout name=" + mgName.getUserName()
			     + " host=" + this.getAddress());
	    server.writeLog(string);
	    debug.log(string);
	}
    }
    
    protected void mRead(ByteStream bytestream) throws IOException {
	bin.setByteStream(bytestream);
	int i = bytestream.size();
	int i_0_ = 0;
	while (i_0_ < i - 1) {
	    int i_1_ = mgRead.setData(bin);
	    mgRead.ID = mgName.ID;
	    mgRead.bName = mgName.bName;
	    i_0_ += i_1_;
	    if (i_1_ <= 0)
		throw new IOException("broken");
	    switchMessage(mgRead);
	}
    }
    
    protected void mIdle(long l) throws IOException {
	/* empty */
    }
    
    protected void mWrite() throws IOException {
	ByteStream bytestream = this.getWriteBuffer();
	if (sendUpdate != null) {
	    int i = sendUpdate.length;
	    for (int i_2_ = 0; i_2_ < i; i_2_++) {
		byte i_3_ = sendUpdate[i_2_].head;
		if (i_3_ == 0 || i_3_ == 6 || i_3_ == 8)
		    sendUpdate[i_2_].getData(bytestream, true);
	    }
	    sendUpdate = null;
	    this.write(bytestream);
	    bytestream.reset();
	}
	int i = sendText.size();
	if (i > 0) {
	    for (int i_4_ = 0; i_4_ < i; i_4_++)
		((MgText) sendText.get(i_4_)).getData(bytestream, false);
	    sendText.remove(i);
	    this.write(bytestream);
	}
    }
    
    private void switchMessage(MgText mgtext) throws IOException {
	switch (mgtext.head) {
	case 2:
	    server.removeTalker(this);
	    break;
	case 0:
	    countSpeak++;
	    /* fall through */
	default:
	    if (!isKill)
		server.addText(this, new MgText(mgtext));
	}
    }
    
    public void send(MgText mgtext) {
	try {
	    if (this.isValidate() && !isKill)
		sendText.add(mgtext);
	} catch (RuntimeException runtimeexception) {
	    debug.log(getHandleName() + ":" + runtimeexception.getMessage());
	    this.mStop();
	}
    }
    
    public synchronized void sendUpdate(Vector2 vector2) {
	int i = vector2.size();
	if (i > 0) {
	    sendUpdate = new MgText[i];
	    vector2.copy(sendUpdate, 0, 0, i);
	}
    }
    
    public MgText getHandleName() {
	return mgName;
    }
    
    public boolean isGuest() {
	return isGuest;
    }
    
    public synchronized void kill() {
	send(new MgText(mgName.ID, (byte) 102, "canvas:false;chat:false;"));
	isKill = true;
    }
    
    public int getSpeakCount() {
	return countSpeak;
    }
}
