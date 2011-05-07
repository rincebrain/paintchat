package paintchat_server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import paintchat.MgText;
import paintchat.Res;
import syi.util.Vector2;

public abstract interface TextTalkerListener
{
  public abstract void mStart(Socket paramSocket, InputStream paramInputStream, OutputStream paramOutputStream, Res paramRes);

  public abstract void mStop();

  public abstract void send(MgText paramMgText);

  public abstract MgText getHandleName();

  public abstract void sendUpdate(Vector2 paramVector2);

  public abstract boolean isValidate();

  public abstract InetAddress getAddress();

  public abstract boolean isGuest();

  public abstract void kill();

  public abstract int getSpeakCount();
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.TextTalkerListener
 * JD-Core Version:    0.6.0
 */