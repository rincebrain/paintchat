/* TextServer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;

import paintchat.Config;
import paintchat.MgText;
import paintchat.debug.DebugListener;

import syi.util.Vector2;

public class TextServer
{
    private boolean isLive = true;
    private TextTalkerListener[] talkers = new TextTalkerListener[10];
    private int countTalker = 0;
    private XMLTalker[] xmlTalkers = new XMLTalker[10];
    private int countXMLTalker = 0;
    private ChatLogOutputStream outLog;
    private DebugListener debug;
    private Config config;
    private String strPassword;
    private Vector2 mgtexts = new Vector2();
    private boolean isCash = true;
    private int iMaxCash;
    public Vector2 vKillIP = new Vector2();
    private int iUniqueLast = 1;
    private Server server;
    
    public void mInit(Server server, Config config,
		      DebugListener debuglistener) {
	if (isLive) {
	    this.server = server;
	    this.config = config;
	    debug = debuglistener;
	    strPassword = config.getString("Admin_Password", null);
	    boolean bool = config.getBool("Server_Log_Text", false);
	    isCash = config.getBool("Server_Cash_Text", true);
	    File file = new File(config.getString("Server_Log_Text_Dir",
						  "save_server"));
	    outLog = new ChatLogOutputStream(file, debuglistener, bool);
	    iMaxCash
		= Math.max(config.getInt("Server_Cash_Text_Size", 128), 5);
	    if (config.getBool("Server_Load_Text", false))
		outLog.loadLog(mgtexts, iMaxCash, true);
	}
    }
    
    public synchronized void mStop() {
	if (isLive) {
	    isLive = false;
	    for (int i = 0; i < countTalker; i++) {
		TextTalkerListener texttalkerlistener = talkers[i];
		if (texttalkerlistener != null)
		    texttalkerlistener.mStop();
	    }
	    if (outLog != null)
		outLog.close();
	    if (mgtexts.size() > 0) {
		try {
		    BufferedWriter bufferedwriter
			= (new BufferedWriter
			   (new OutputStreamWriter
			    (new FileOutputStream(outLog.getTmpFile()),
			     "UTF8")));
		    int i = mgtexts.size();
		    for (int i_0_ = 0; i_0_ < i; i_0_++) {
			MgText mgtext = (MgText) mgtexts.get(i_0_);
			switch (mgtext.head) {
			case 1:
			    bufferedwriter
				.write("Login name=" + mgtext.toString());
			    break;
			case 2:
			    bufferedwriter
				.write("Logout name=" + mgtext.getUserName());
			    break;
			default:
			    bufferedwriter.write(mgtext.getUserName() + '>'
						 + mgtext.toString());
			}
			bufferedwriter.newLine();
		    }
		    bufferedwriter.flush();
		    bufferedwriter.close();
		} catch (java.io.IOException ioexception) {
		    /* empty */
		}
	    }
	}
    }
    
    public synchronized void addTalker(TextTalkerListener texttalkerlistener) {
	if (isLive) {
	    MgText mgtext = texttalkerlistener.getHandleName();
	    String string = mgtext.toString();
	    if (string.length() <= 0 || !texttalkerlistener.isValidate())
		texttalkerlistener.mStop();
	    else {
		mgtext.ID = getUniqueID();
		String string_1_ = getUniqueName(string);
		if (string != string_1_) {
		    mgtext.setData(mgtext.ID, mgtext.head,
				   getUniqueName(string));
		    string = mgtext.toString();
		    mgtext.setUserName(string);
		}
		mgtext.toBin(false);
		int i = countTalker;
		for (int i_2_ = 0; i_2_ < i; i_2_++) {
		    if (!talkers[i_2_].isGuest())
			texttalkerlistener.send(talkers[i_2_].getHandleName());
		}
		if (isCash)
		    texttalkerlistener.sendUpdate(mgtexts);
		if (!texttalkerlistener.isGuest())
		    addText(texttalkerlistener,
			    new MgText(mgtext.ID, (byte) 1, string));
		if (countTalker + 1 >= talkers.length) {
		    TextTalkerListener[] texttalkerlisteners
			= new TextTalkerListener[talkers.length + 1];
		    System.arraycopy(talkers, 0, texttalkerlisteners, 0,
				     talkers.length);
		    talkers = texttalkerlisteners;
		}
		talkers[countTalker] = texttalkerlistener;
		countTalker++;
	    }
	}
    }
    
    public void doAdmin(String string, int i) {
	if (string.indexOf("kill") >= 0)
	    killTalker(i);
	else {
	    TextTalkerListener texttalkerlistener = getTalker(i);
	    texttalkerlistener.send(new MgText(i, (byte) 102, string));
	}
    }
    
    public synchronized void removeTalker
	(TextTalkerListener texttalkerlistener) {
	if (isLive) {
	    boolean bool = texttalkerlistener.isGuest();
	    texttalkerlistener.mStop();
	    MgText mgtext = new MgText();
	    mgtext.setData(texttalkerlistener.getHandleName());
	    mgtext.head = (byte) 2;
	    mgtext.toBin(false);
	    int i = countTalker;
	    for (int i_3_ = 0; i_3_ < i; i_3_++) {
		if (talkers[i_3_] == texttalkerlistener) {
		    talkers[i_3_] = null;
		    if (i_3_ + 1 < i) {
			System.arraycopy(talkers, i_3_ + 1, talkers, i_3_,
					 i - (i_3_ + 1));
			talkers[i - 1] = null;
		    }
		    countTalker--;
		    i--;
		    i_3_--;
		} else if (!bool)
		    talkers[i_3_].send(mgtext);
	    }
	}
    }
    
    public synchronized void killTalker(int i) {
	TextTalkerListener texttalkerlistener = getTalker(i);
	if (texttalkerlistener != null) {
	    MgText mgtext = texttalkerlistener.getHandleName();
	    vKillIP.add(texttalkerlistener.getAddress());
	    texttalkerlistener.kill();
	    addText(texttalkerlistener,
		    new MgText(0, (byte) 6,
			       ("kill done name=+" + mgtext.toString()
				+ " address="
				+ texttalkerlistener.getAddress())));
	}
    }
    
    public synchronized void addText(TextTalkerListener texttalkerlistener,
				     MgText mgtext) {
	if (isLive) {
	    for (int i = 0; i < countTalker; i++) {
		TextTalkerListener texttalkerlistener_4_ = talkers[i];
		if (texttalkerlistener_4_ != texttalkerlistener
		    && texttalkerlistener_4_ != null) {
		    if (texttalkerlistener_4_.isValidate())
			texttalkerlistener_4_.send(mgtext);
		    else {
			removeTalker(texttalkerlistener_4_);
			i--;
		    }
		}
	    }
	    if (isCash) {
		mgtexts.add(mgtext);
		if (mgtexts.size() >= iMaxCash)
		    mgtexts.remove(5);
	    }
	    if (outLog != null)
		outLog.write(mgtext);
	}
    }
    
    public int getUserCount() {
	return countTalker;
    }
    
    public void clearKillIP() {
	vKillIP.removeAll();
    }
    
    public synchronized void clearDeadTalker() {
	for (int i = 0; i < countTalker; i++) {
	    TextTalkerListener texttalkerlistener = talkers[i];
	    if (texttalkerlistener == null
		|| !texttalkerlistener.isValidate()) {
		if (texttalkerlistener != null)
		    removeTalker(texttalkerlistener);
		else {
		    if (i < countTalker - 1)
			System.arraycopy(talkers, i + 1, talkers, i,
					 countTalker - (i + 1));
		    countTalker--;
		}
		i = -1;
	    }
	}
    }
    
    public synchronized TextTalkerListener getTalker(InetAddress inetaddress) {
	if (inetaddress == null)
	    return null;
	for (int i = 0; i < countTalker; i++) {
	    TextTalkerListener texttalkerlistener = talkers[i];
	    if (texttalkerlistener != null
		&& texttalkerlistener.getAddress().equals(inetaddress))
		return texttalkerlistener;
	}
	return null;
    }
    
    public synchronized TextTalkerListener getTalker(int i) {
	if (i <= 0)
	    return null;
	for (int i_5_ = 0; i_5_ < countTalker; i_5_++) {
	    TextTalkerListener texttalkerlistener = talkers[i_5_];
	    if (texttalkerlistener != null
		&& texttalkerlistener.getHandleName().ID == i)
		return texttalkerlistener;
	}
	return null;
    }
    
    public synchronized TextTalkerListener getTalkerAt(int i) {
	if (i < 0 || i >= countTalker)
	    return null;
	return talkers[i];
    }
    
    private int getUniqueID() {
	int i = iUniqueLast;
	int i_6_ = 0;
	while (i_6_ < countTalker) {
	    TextTalkerListener texttalkerlistener;
	    if ((texttalkerlistener = talkers[i_6_++]) != null
		&& texttalkerlistener.getHandleName().ID == i) {
		if (++i >= 65535)
		    i = 1;
		i_6_ = 0;
	    }
	}
	iUniqueLast = i >= 65535 ? 1 : i + 1;
	return i;
    }
    
    private String getUniqueName(String string) {
	int i = 2;
	String string_7_ = string;
	int i_8_ = 0;
	while (i_8_ < countTalker) {
	    TextTalkerListener texttalkerlistener;
	    if ((texttalkerlistener = talkers[i_8_++]) != null
		&& string_7_.equals(texttalkerlistener.getHandleName()
					.toString())) {
		string_7_ = string + i;
		i++;
		i_8_ = 0;
	    }
	}
	return string_7_;
    }
    
    public String getPassword() {
	return strPassword;
    }
    
    public synchronized void getUserListXML(StringBuffer stringbuffer) {
	int i = countTalker;
	for (int i_9_ = 0; i_9_ < i; i_9_++) {
	    TextTalkerListener texttalkerlistener = talkers[i_9_];
	    if (texttalkerlistener != null && !texttalkerlistener.isGuest()) {
		MgText mgtext = texttalkerlistener.getHandleName();
		String string = mgtext.toString();
		if (string != null && string.length() > 0)
		    stringbuffer.append("<in id=\"" + mgtext.ID + "\">"
					+ string + "</in>");
	    }
	}
    }
    
    public synchronized MgText getInfomation() {
	return server.getInfomation();
    }
    
    public void writeLog(String string) {
	if (outLog != null)
	    outLog.write(string);
    }
}
