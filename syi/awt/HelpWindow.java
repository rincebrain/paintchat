/* HelpWindow - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;

import syi.util.ThreadPool;

public class HelpWindow extends Window implements Runnable
{
    private Thread rAdd = null;
    private HelpWindowContent object = null;
    private ImageCanvas imageCanvas = null;
    private TextCanvas textCanvas = null;
    private boolean isShow = false;
    private boolean isFront = true;
    
    public HelpWindow(Frame frame) {
	super(frame);
    }
    
    public boolean getIsShow() {
	return isShow;
    }
    
    public synchronized void reset() {
	this.setVisible(false);
	object = null;
	if (this.getComponentCount() > 0) {
	    Component component = this.getComponent(0);
	    if (component == textCanvas)
		((TextCanvas) component).reset();
	    else
		((ImageCanvas) component).reset();
	}
	if (rAdd != null && rAdd != Thread.currentThread()) {
	    Thread thread;
	    MONITORENTER (thread = rAdd);
	    MISSING MONITORENTER
	    synchronized (thread) {
		rAdd.interrupt();
		rAdd.notify();
		rAdd = null;
	    }
	}
    }
    
    public void run() {
	try {
	    Thread thread = Thread.currentThread();
	    if (object.timeStart > 0) {
		Thread thread_0_;
		MONITORENTER (thread_0_ = thread);
		MISSING MONITORENTER
		synchronized (thread_0_) {
		    thread.wait((long) object.timeStart);
		}
	    }
	    if (!thread.isInterrupted()) {
		showHelp(object);
		if (object.timeEnd > 0) {
		    Thread thread_1_;
		    MONITORENTER (thread_1_ = thread);
		    MISSING MONITORENTER
		    synchronized (thread_1_) {
			thread.wait((long) object.timeEnd);
		    }
		}
	    }
	    reset();
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    public void setIsFront(boolean bool) {
	isFront = bool;
    }
    
    public void setIsShow(boolean bool) {
	isShow = bool;
    }
    
    private synchronized void showHelp(HelpWindowContent helpwindowcontent) {
	if (helpwindowcontent != null) {
	    if (helpwindowcontent.image != null) {
		if (imageCanvas == null)
		    imageCanvas = new ImageCanvas(this.getBackground(),
						  this.getForeground());
		if (this.getComponentCount() > 0) {
		    Component component = this.getComponent(0);
		    if (component != imageCanvas) {
			this.remove(component);
			this.add(imageCanvas);
		    }
		} else
		    this.add(imageCanvas);
		imageCanvas.setImage(helpwindowcontent.image);
		if (helpwindowcontent.string != null)
		    imageCanvas.setText(helpwindowcontent.getText());
	    } else {
		if (textCanvas == null) {
		    textCanvas = new TextCanvas();
		    textCanvas.setBackground(new Color(13421823));
		    textCanvas.setForeground(Color.black);
		}
		if (this.getComponentCount() > 0) {
		    Component component = this.getComponent(0);
		    if (component != textCanvas) {
			this.remove(component);
			this.add(textCanvas);
		    }
		} else
		    this.add(textCanvas);
		textCanvas.setText(helpwindowcontent.getText());
	    }
	    this.pack();
	    this.getSize();
	    this.setLocation(helpwindowcontent.point.x,
			     helpwindowcontent.point.y);
	    this.setVisible(true);
	    if (isFront)
		this.toFront();
	}
    }
    
    public synchronized void startHelp(HelpWindowContent helpwindowcontent) {
	if (helpwindowcontent.isVisible(isShow)) {
	    reset();
	    object = helpwindowcontent;
	    helpwindowcontent.point.y += 15;
	    rAdd = ThreadPool.poolStartThread(this, "s");
	}
    }
}
