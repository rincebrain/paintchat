/* AppletWatcher - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.applet;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

import paintchat.Config;

import syi.awt.Awt;
import syi.awt.Gui;
import syi.util.PProperties;

public class AppletWatcher extends Frame
{
    private Applet applet;
    private boolean bool_exit;
    
    public AppletWatcher
	(String string, String string_0_, Config config, Hashtable hashtable,
	 boolean bool)
	throws ClassNotFoundException, Exception, IOException {
	super(string_0_);
	this.enableEvents(64L);
	bool_exit = bool;
	this.setLayout(new BorderLayout());
	applet = (Applet) Class.forName(string).newInstance();
	applet.setStub(ServerStub.getDefaultStub(config, hashtable));
	this.add(applet, "Center");
	applet.init();
	Gui.getDefSize(this);
	Awt.moveCenter(this);
    }
    
    private PProperties getProperties(URL url) {
	try {
	    boolean bool = false;
	    URLConnection urlconnection = url.openConnection();
	    urlconnection.connect();
	    BufferedInputStream bufferedinputstream
		= new BufferedInputStream(urlconnection.getInputStream());
	    int i = urlconnection.getContentLength();
	    int i_1_ = 0;
	    char[] cs = new char[i];
	    for (/**/; i_1_ < i; i_1_++) {
		int i_2_ = bufferedinputstream.read();
		switch (i_2_) {
		case -1:
		    cs[i_1_] = '\0';
		    i = 0;
		    break;
		case 9:
		case 10:
		case 13:
		case 32:
		    if (!bool) {
			bool = true;
			cs[i_1_] = (char) i_2_;
		    }
		    break;
		default:
		    bool = false;
		    cs[i_1_] = (char) i_2_;
		}
	    }
	    bufferedinputstream.close();
	    Object object = null;
	    i = i_1_;
	    bool = false;
	    int i_3_ = 0;
	    int i_4_ = 0;
	    char[] cs_5_ = { 'p', 'a', 'r', 'a', 'm' };
	    char[] cs_6_ = { 'a', 'p', 'p', 'l', 'e', 't' };
	    for (i_1_ = 0; i_1_ < i; i_1_++) {
		if (cs[i_1_] == '<') {
		    for (i_1_++; i_1_ < i; i_1_++) {
			if (cs[i_1_] == ' ') {
			    if (bool)
				break;
			} else {
			    bool = true;
			    char c = Character.toLowerCase(cs[i_1_]);
			    i_3_ = cs_5_[i_3_] == c ? i_3_ + 1 : 0;
			    i_4_ = cs_6_[i_3_] == c ? i_4_ + 1 : 0;
			}
		    }
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
	return null;
    }
    
    public static void main(String[] strings) {
	/* empty */
    }
    
    protected void processWindowEvent(WindowEvent windowevent) {
	try {
	    int i = windowevent.getID();
	    Window window = windowevent.getWindow();
	    switch (i) {
	    case 201:
		window.dispose();
		applet.stop();
		break;
	    case 202:
		applet.destroy();
		applet = null;
		if (bool_exit)
		    System.exit(0);
		break;
	    case 200:
		applet.start();
		break;
	    }
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
}
