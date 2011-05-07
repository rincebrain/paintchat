/* Client - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_client;
import java.applet.Applet;
import java.awt.BorderLayout;

import syi.util.ThreadPool;

public class Client extends Applet
{
    private Pl pl;
    
    public void destroy() {
	if (pl != null)
	    pl.destroy();
    }
    
    public void init() {
	try {
	    this.setLayout(new BorderLayout());
	    pl = new Pl(this);
	    this.add(pl, "Center");
	    this.validate();
	    ThreadPool.poolStartThread(pl, 'i');
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
}
