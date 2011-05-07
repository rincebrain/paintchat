/* XMLTextTalker - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.IOException;

import paintchat.MgText;
import paintchat.Res;
import paintchat.debug.DebugListener;

import syi.util.ByteInputStream;
import syi.util.Vector2;

public class XMLTextTalker extends XMLTalker implements TextTalkerListener
{
    private Vector2 send_text = new Vector2();
    private Vector2 send_update = new Vector2();
    private ByteInputStream input = new ByteInputStream();
    private TextServer server;
    private DebugListener debug;
    private MgText mgName;
    private boolean isOnlyUserList = false;
    private boolean isGuest = false;
    private boolean isKill = false;
    private int countSpeak = 0;
    
    public XMLTextTalker(TextServer textserver, DebugListener debuglistener) {
	server = textserver;
	debug = debuglistener;
    }
    
    protected void mInit() throws IOException {
	Res res = this.getStatus();
	mgName = new MgText(0, (byte) 1, this.getStatus().get("name"));
	isOnlyUserList = res.getBool("user_list_only", false);
	isGuest = res.getBool("guest", false);
	server.addTalker(this);
    }
    
    protected void mDestroy() {
	/* empty */
    }
    
    protected void mRead(String string, String string_0_, Res res)
	throws IOException {
	if (mgName != null) {
	    byte i = (byte) this.strToHint(string);
	    MgText mgtext = new MgText();
	    mgtext.setData(mgName.ID, i, toEscape(string_0_));
	    switch (i) {
	    default:
		if (isGuest) {
		    String string_1_ = server.getPassword();
		    if (string_1_ == null || string_1_.length() <= 0
			|| !this.getStatus().get("password").equals(string_1_))
			break;
		}
		server.addText(this, mgtext);
		break;
	    case 102:
		try {
		    if (string_0_.indexOf("get:infomation") >= 0)
			send(server.getInfomation());
		    else {
			int i_2_ = res.getInt("id", 0);
			if (i_2_ > 0)
			    server.doAdmin(string_0_, i_2_);
		    }
		    break;
		} catch (RuntimeException runtimeexception) {
		    debug.log(runtimeexception);
		    break;
		}
	    case 2:
		server.removeTalker(this);
	    }
	}
    }
    
    protected void mWrite() throws IOException {
	if (send_update != null) {
	    int i = send_update.size();
	    if (i > 0) {
		StringBuffer stringbuffer = new StringBuffer();
		for (int i_3_ = 0; i_3_ < i; i_3_++) {
		    MgText mgtext = (MgText) send_update.get(i_3_);
		    String string = this.hintToString(mgtext.head);
		    stringbuffer.append('<');
		    stringbuffer.append(string);
		    stringbuffer.append(" name=\"");
		    stringbuffer.append(toEscape(mgtext.getUserName()));
		    stringbuffer.append("\">");
		    stringbuffer.append(toEscape(mgtext.toString()));
		    stringbuffer.append("</");
		    stringbuffer.append(string);
		    stringbuffer.append('>');
		}
		this.write("update", stringbuffer.toString());
		send_update.removeAll();
	    }
	    send_update = null;
	}
	int i = send_text.size();
	if (i > 0) {
	    for (int i_4_ = 0; i_4_ < i; i_4_++) {
		MgText mgtext = (MgText) send_text.get(i_4_);
		this.write(this.hintToString(mgtext.head),
			   toEscape(mgtext.toString()),
			   ("id=\"" + mgtext.ID + "\" name=\""
			    + toEscape(mgtext.getUserName()) + "\""));
	    }
	    send_text.remove(i);
	}
    }
    
    public void send(MgText mgtext) {
	if (this.isValidate()
	    && (!isOnlyUserList || mgtext.head == 1 || mgtext.head == 2))
	    send_text.add(mgtext);
    }
    
    public MgText getHandleName() {
	return mgName;
    }
    
    public void sendUpdate(Vector2 vector2) {
	vector2.copy(send_text);
    }
    
    public boolean isGuest() {
	return isGuest;
    }
    
    private String toEscape(String string) {
	if (string.indexOf('<') >= 0 || string.indexOf('>') >= 0
	    || string.indexOf('&') >= 0 || string.indexOf('\"') >= 0
	    || string.indexOf('\"') >= 0) {
	    StringBuffer stringbuffer = new StringBuffer();
	    for (int i = 0; i < string.length(); i++) {
		char c = string.charAt(i);
		switch (c) {
		case '<':
		    stringbuffer.append("&lt;");
		    break;
		case '>':
		    stringbuffer.append("&gt;");
		    break;
		case '&':
		    stringbuffer.append("&amp;");
		    break;
		case '\"':
		    stringbuffer.append("&qout;");
		    break;
		default:
		    stringbuffer.append(c);
		}
	    }
	    return stringbuffer.toString();
	}
	return string;
    }
    
    private boolean equalsPassword() {
	String string = server.getPassword();
	if (string != null && string.length() > 0
	    && this.getStatus().get("password").equals(string))
	    return false;
	return true;
    }
    
    public void kill() {
	isKill = true;
    }
    
    public int getSpeakCount() {
	return countSpeak;
    }
}
