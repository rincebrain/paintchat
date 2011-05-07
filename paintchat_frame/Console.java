/* Console - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_frame;
import java.applet.Applet;
import java.awt.Color;
import java.awt.TextField;
import java.io.CharArrayWriter;

import paintchat.debug.Debug;
import paintchat.debug.DebugListener;

import syi.awt.TextPanel;
import syi.util.ThreadPool;

public class Console extends TextPanel implements Runnable, DebugListener
{
    private Debug debug = null;
    private CharArrayWriter cOut = null;
    private Thread thread = null;
    private boolean isRun;
    
    public Console() {
	this(null, 400, Color.white, Color.black, null);
    }
    
    public Console(Applet applet, int i, Color color, Color color_0_,
		   TextField textfield) {
	super(applet, i, color, color_0_, textfield);
    }
    
    public void run() {
	try {
	    Thread thread = Thread.currentThread();
	    while (isRun) {
		if (cOut.size() > 0) {
		    CharArrayWriter chararraywriter;
/*		    MONITORENTER (chararraywriter = cOut);
		    MISSING MONITORENTER*/
		    synchronized (chararraywriter) {
			this.setText(cOut.toString());
			cOut.reset();
		    }
		}
		Thread.sleep(2500L);
	    }
	} catch (Throwable throwable) {
	    /* empty */
	}
	stop();
    }
    
    public synchronized void start(Debug debug) {
	this.debug = debug;
	if (cOut == null)
	    cOut = new CharArrayWriter();
	this.debug.setListener(this);
	isRun = true;
	thread = ThreadPool.poolStartThread(this, "c");
    }
    
    public synchronized void stop() {
	if (isRun) {
	    isRun = false;
	    debug.setListener(null);
	    if (thread != null && Thread.currentThread() != thread) {
		thread.interrupt();
		thread = null;
	    }
	}
    }
    
    public void log(Object object) {
	this.addText(object.toString());
    }
    
    public void logDebug(Object object) {
	this.addText(object.toString());
    }
}
