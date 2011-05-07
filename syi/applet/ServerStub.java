/* ServerStub - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.applet;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import paintchat.Config;

import sun.applet.AppletAudioClip;

import syi.awt.Awt;
import syi.awt.Gui;
import syi.util.PProperties;

public class ServerStub implements AppletContext, AppletStub
{
    private Config params;
    private Hashtable res;
    URL url_base;
    private static ServerStub default_stub = null;
    
    public Iterator getStreamKeys() {
	return null;
    }
    
    public ServerStub(Config config, Hashtable hashtable) {
	params = config;
	res = hashtable;
	try {
	    String string = System.getProperty("user.dir");
	    string = Awt.replaceText(string, "/", "\\");
	    if (!string.endsWith("/"))
		string += '/';
	    url_base = new URL("file:/" + string);
	} catch (RuntimeException runtimeexception) {
	    /* empty */
	} catch (java.io.IOException ioexception) {
	    /* empty */
	}
    }
    
    public void appletResize(int i, int i_0_) {
	/* empty */
    }
    
    public Applet getApplet(String string) {
	return null;
    }
    
    public AppletContext getAppletContext() {
	return this;
    }
    
    public Enumeration getApplets() {
	return null;
    }
    
    public AudioClip getAudioClip(URL url) {
	try {
	    return new AppletAudioClip(url);
	} catch (Throwable throwable) {
	    System.out.println(throwable);
	    try {
		return (AudioClip) url.getContent();
	    } catch (Exception exception) {
		return null;
	    }
	}
    }
    
    public URL getCodeBase() {
	return url_base;
    }
    
    public static ServerStub getDefaultStub(Config config,
					    Hashtable hashtable) {
	if (default_stub == null)
	    default_stub = new ServerStub(config, hashtable);
	return default_stub;
    }
    
    public URL getDocumentBase() {
	return url_base;
    }
    
    public PProperties getHashTable() {
	return params;
    }
    
    public Image getImage(URL url) {
	try {
	    return (Image) url.getContent();
	} catch (Exception exception) {
	    return null;
	}
    }
    
    public String getParameter(String string) {
	return (String) params.get(string);
    }
    
    public boolean isActive() {
	return false;
    }
    
    public void showDocument(URL url) {
	showDocument(url, "");
    }
    
    public void showDocument(URL url, String string) {
	Gui.showDocument(url.toExternalForm(), params, res);
    }
    
    public void showStatus(String string) {
	/* empty */
    }
    
    public InputStream getStream(String string) {
	return null;
    }
    
    public void setStream(String string, InputStream inputstream) {
	/* empty */
    }
}
