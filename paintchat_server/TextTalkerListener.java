/* TextTalkerListener - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import paintchat.MgText;
import paintchat.Res;

import syi.util.Vector2;

public interface TextTalkerListener
{
    public void mStart(Socket socket, InputStream inputstream,
		       OutputStream outputstream, Res res);
    
    public void mStop();
    
    public void send(MgText mgtext);
    
    public MgText getHandleName();
    
    public void sendUpdate(Vector2 vector2);
    
    public boolean isValidate();
    
    public InetAddress getAddress();
    
    public boolean isGuest();
    
    public void kill();
    
    public int getSpeakCount();
}
