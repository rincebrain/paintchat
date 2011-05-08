package paintchat_server;

import java.io.IOException;
import paintchat.M;
import paintchat.debug.DebugListener;
import syi.util.ByteStream;
import syi.util.VectorBin;

// Referenced classes of package paintchat_server:
//            PaintChatTalker, LineServer

public class LineTalker extends PaintChatTalker
{

    ByteStream lines_send;
    VectorBin lines_log;
    LineServer server;
    DebugListener debug;
    private M mgRead;
    private ByteStream workReceive;
    private ByteStream workReceive2;
    private M mgSend;
    private int countDraw;

    public LineTalker(LineServer lineserver, DebugListener debuglistener)
    {
        lines_send = new ByteStream();
        lines_log = new VectorBin();
        mgRead = new M();
        workReceive = new ByteStream();
        workReceive2 = new ByteStream();
        mgSend = null;
        countDraw = 0;
        server = lineserver;
        debug = debuglistener;
    }

    public void send(ByteStream bytestream, M m, ByteStream bytestream1)
    {
        if(!isValidate())
        {
            return;
        }
        try
        {
            if(lines_send.size() >= 65535)
            {
                lines_send = null;
                throw new IOException("client error");
            }
            int i = 0;
            int j = bytestream.size();
            byte abyte0[] = bytestream.getBuffer();
            synchronized(lines_send)
            {
                while(i + 1 < j) 
                {
                    i += m.set(abyte0, i);
                    m.get(lines_send, bytestream1, mgSend);
                    if(mgSend == null)
                    {
                        mgSend = new M();
                    }
                    mgSend.set(m);
                }
            }
        }
        catch(IOException ioexception)
        {
            debug.log(ioexception);
            mStop();
        }
    }

    protected void mInit()
        throws IOException
    {
        super.iSendInterval = 4000;
        server.addTalker(this);
        synchronized(lines_send)
        {
            lines_send.w2(1);
            lines_send.write(0);
        }
    }

    protected void mRead(ByteStream bytestream)
        throws IOException
    {
        int i = bytestream.size();
        if(i <= 0)
        {
            return;
        }
        if(i <= 2 && bytestream.getBuffer()[0] == 0)
        {
            server.removeTalker(this);
            return;
        }
        workReceive.reset();
        while(bytestream.size() >= 2) 
        {
            int j = mgRead.set(bytestream);
            if(j < 0)
            {
                return;
            }
            countDraw++;
            bytestream.reset(j);
            mgRead.get(workReceive, workReceive2, null);
            if(mgRead.iHint == 10)
            {
                server.addClear(this, workReceive);
                workReceive.reset();
            }
        }
        if(workReceive.size() > 0)
        {
            server.addLine(this, workReceive);
        }
    }

    protected void mIdle(long l)
        throws IOException
    {
    }

    protected void mWrite()
        throws IOException
    {
        if(lines_log != null)
        {
            if(lines_log.size() <= 0)
            {
                lines_log = null;
                return;
            } else
            {
                mSendFlag(1);
                byte abyte0[] = lines_log.get(0);
                write(abyte0, abyte0.length);
                lines_log.remove(1);
                return;
            }
        }
        if(lines_send.size() <= 2)
        {
            return;
        }
        ByteStream bytestream = getWriteBuffer();
        synchronized(lines_send)
        {
            lines_send.writeTo(bytestream);
            lines_send.reset();
        }
        write(bytestream);
        bytestream.reset();
    }

    protected void mDestroy()
    {
        lines_send = null;
        lines_log = null;
        server = null;
        debug = null;
    }

    public VectorBin getLogArray()
    {
        return lines_log;
    }

    private void mSendFlag(int i)
        throws IOException
    {
        ByteStream bytestream = getWriteBuffer();
        bytestream.write(i);
        write(bytestream);
    }

    public int getDrawCount()
    {
        return countDraw;
    }
}
