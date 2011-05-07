/* ConfigDialog - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_frame;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.WindowEvent;

import paintchat.Config;
import paintchat.Res;

import syi.applet.ServerStub;
import syi.awt.Awt;

public class ConfigDialog extends Dialog
{
    private Applet applet;
    
    public ConfigDialog(String string, String string_0_, Config config,
			Res res, String string_1_) throws Exception {
	super(Awt.getPFrame());
	this.setModal(true);
	applet = (Applet) Class.forName(string).newInstance();
	applet.setStub(ServerStub.getDefaultStub(config, res));
	this.enableEvents(64L);
	this.setLayout(new BorderLayout());
	this.add(applet, "Center");
	applet.init();
	this.pack();
	Awt.moveCenter(this);
	this.setVisible(true);
	applet.start();
    }
    
    protected void processWindowEvent(WindowEvent windowevent) {
	switch (windowevent.getID()) {
	case 201: {
	    applet.stop();
	    Window window = windowevent.getWindow();
	    window.dispose();
	    window.removeAll();
	    break;
	}
	case 202:
	    applet.destroy();
	    break;
	}
	if (windowevent.getID() == 201) {
	    Window window = windowevent.getWindow();
	    window.dispose();
	    window.removeAll();
	}
    }
}
