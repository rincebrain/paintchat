package paintchat_server;

import java.io.IOException;
import paintchat.MgText;
import paintchat.Res;
import paintchat.debug.DebugListener;
import syi.util.*;

// Referenced classes of package paintchat_server:
//            PaintChatTalker, TextTalkerListener, TextServer

public class TextTalker extends PaintChatTalker
    implements TextTalkerListener
{

    private Vector2 sendText;
    private MgText sendUpdate[];
    private TextServer server;
    private DebugListener debug;
    private MgText mgName;
    private MgText mgRead;
    private ByteInputStream bin;
    private int countSpeak;
    private boolean isGuest;
    private boolean isKill;

    public TextTalker(TextServer textserver, DebugListener debuglistener)
    {
        sendText = new Vector2();
        sendUpdate = null;
        mgRead = new MgText();
        bin = new ByteInputStream();
        countSpeak = 0;
        isGuest = false;
        isKill = false;
        debug = debuglistener;
        server = textserver;
    }

    protected void mInit()
        throws IOException
    {
        mgName = new MgText(0, (byte)1, getStatus().get("name"));
        mgName.setUserName(mgName.toString());
        isGuest = getStatus().getBool("guest", false);
        server.addTalker(this);
        String s = "User login name=" + mgName.getUserName() + " host=" + getAddress();
        server.writeLog(s);
        debug.log(s);
    }

    protected void mDestroy()
    {
        if(mgName.getUserName().length() > 0)
        {
            String s = "User logout name=" + mgName.getUserName() + " host=" + getAddress();
            server.writeLog(s);
            debug.log(s);
        }
    }

    protected void mRead(ByteStream bytestream)
        throws IOException
    {
        bin.setByteStream(bytestream);
        int i = bytestream.size();
        for(int j = 0; j < i - 1;)
        {
            int k = mgRead.setData(bin);
            mgRead.ID = mgName.ID;
            mgRead.bName = mgName.bName;
            j += k;
            if(k <= 0)
            {
                throw new IOException("broken");
            }
            switchMessage(mgRead);
        }

    }

    protected void mIdle(long l)
        throws IOException
    {
    }

    protected void mWrite()
        throws IOException
    {
        ByteStream bytestream = getWriteBuffer();
        if(sendUpdate != null)
        {
            int i = sendUpdate.length;
            for(int l = 0; l < i; l++)
            {
                byte byte0 = sendUpdate[l].head;
                if(byte0 == 0 || byte0 == 6 || byte0 == 8)
                {
                    sendUpdate[l].getData(bytestream, true);
                }
            }

            sendUpdate = null;
            write(bytestream);
            bytestream.reset();
        }
        int j = sendText.size();
        if(j <= 0)
        {
            return;
        }
        for(int k = 0; k < j; k++)
        {
            ((MgText)sendText.get(k)).getData(bytestream, false);
        }

        sendText.remove(j);
        write(bytestream);
    }

    private void switchMessage(MgText mgtext)
        throws IOException
    {
        switch(mgtext.head)
        {
        case 2: // '\002'
            server.removeTalker(this);
            break;

        case 0: // '\0'
            countSpeak++;
            // fall through

        case 1: // '\001'
        default:
            if(!isKill)
            {
                server.addText(this, new MgText(mgtext));
            }
            break;
        }
    }

    public void send(MgText mgtext)
    {
        try
        {
            if(!isValidate() || isKill)
            {
                return;
            }
            sendText.add(mgtext);
        }
        catch(RuntimeException runtimeexception)
        {
            debug.log(getHandleName() + ":" + runtimeexception.getMessage());
            mStop();
        }
    }

    public synchronized void sendUpdate(Vector2 vector2)
    {
        int i = vector2.size();
        if(i > 0)
        {
            sendUpdate = new MgText[i];
            vector2.copy(sendUpdate, 0, 0, i);
        }
    }

    public MgText getHandleName()
    {
        return mgName;
    }

    public boolean isGuest()
    {
        return isGuest;
    }

    public synchronized void kill()
    {
        send(new MgText(mgName.ID, (byte)102, "canvas:false;chat:false;"));
        isKill = true;
    }

    public int getSpeakCount()
    {
        return countSpeak;
    }
}
