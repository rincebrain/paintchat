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

    public abstract void mStart(Socket socket, InputStream inputstream, OutputStream outputstream, Res res);

    public abstract void mStop();

    public abstract void send(MgText mgtext);

    public abstract MgText getHandleName();

    public abstract void sendUpdate(Vector2 vector2);

    public abstract boolean isValidate();

    public abstract InetAddress getAddress();

    public abstract boolean isGuest();

    public abstract void kill();

    public abstract int getSpeakCount();
}
