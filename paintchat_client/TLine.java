package paintchat_client;

import java.io.IOException;
import java.util.zip.Inflater;
import paintchat.M;
import paintchat_server.PaintChatTalker;
import syi.util.ByteStream;
import syi.util.ThreadPool;

// Referenced classes of package paintchat_client:
//            Data

public class TLine extends PaintChatTalker
{

    public Data data;
    private ByteStream bSendCash;
    private ByteStream stmIn;
    private ByteStream workSend;
    private M mgOut;
    private M mgDraw;
    private boolean isCompress;
    private boolean isRunDraw;
    Inflater inflater;

    public TLine(Data data1, M m)
    {
        bSendCash = new ByteStream();
        stmIn = new ByteStream();
        workSend = new ByteStream();
        mgOut = null;
        mgDraw = null;
        isCompress = false;
        isRunDraw = false;
        inflater = new Inflater(false);
        data = data1;
        mgDraw = m;
    }

    protected void mDestroy()
    {
        isRunDraw = false;
        super.iSendInterval = 0;
    }

    protected void mRead(ByteStream bytestream)
        throws IOException
    {
        if(bytestream.size() <= 1)
        {
            switch(bytestream.getBuffer()[0])
            {
            case 0: // '\0'
                synchronized(stmIn)
                {
                    stmIn.w2(0);
                }
                break;

            case 1: // '\001'
                isCompress = true;
                break;
            }
            return;
        }
        try
        {
            int i = bytestream.size();
            if(isCompress)
            {
                isCompress = false;
                if(inflater == null)
                {
                    inflater = new Inflater(false);
                }
                inflater.reset();
                inflater.setInput(bytestream.getBuffer(), 0, i);
                synchronized(this)
                {
                    byte abyte0[] = workSend.getBuffer();
                    synchronized(stmIn)
                    {
                        int j;
                        for(; !inflater.needsInput(); stmIn.write(abyte0, 0, j))
                        {
                            j = inflater.inflate(abyte0, 0, abyte0.length);
                        }

                    }
                }
            } else
            {
                if(inflater != null)
                {
                    inflater.end();
                    inflater = null;
                }
                synchronized(stmIn)
                {
                    bytestream.writeTo(stmIn);
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void mInit()
    {
    }

    protected void mIdle(long l)
        throws IOException
    {
    }

    protected void mWrite()
        throws IOException
    {
        if(bSendCash.size() <= 0)
        {
            return;
        }
        synchronized(bSendCash)
        {
            write(bSendCash);
            bSendCash.reset();
        }
    }

    public void send(M m)
    {
label0:
        {
            synchronized(bSendCash)
            {
                if(m != null)
                {
                    break label0;
                }
                try
                {
                    super.canWrite = true;
                    bSendCash.reset();
                    bSendCash.w2(2);
                    write(bSendCash);
                    flush();
                }
                catch(IOException _ex) { }
            }
            return;
        }
        m.get(bSendCash, workSend, mgOut);
        if(mgOut == null)
        {
            mgOut = new M();
        }
        mgOut.set(m);
        bytestream;
        JVM INSTR monitorexit ;
    }

    public void run()
    {
        if(!isRunDraw)
        {
            isRunDraw = true;
            ThreadPool.poolStartThread(this, 'd');
            super.run();
            return;
        }
          goto _L1
_L3:
label0:
        {
            if(stmIn.size() < 2)
            {
                break MISSING_BLOCK_LABEL_191;
            }
            int j;
            synchronized(stmIn)
            {
                byte abyte0[] = stmIn.getBuffer();
                int i = (abyte0[0] & 0xff) << 8 | abyte0[1] & 0xff;
                if(i > 2)
                {
                    break label0;
                }
                mgDraw.newUser(null).wait = 0;
                data.addTextComp();
                stmIn.reset(i + 2);
            }
            continue; /* Loop/switch isn't completed */
        }
        j = mgDraw.set(stmIn.getBuffer(), 0);
        stmIn.reset(j);
        bytestream;
        JVM INSTR monitorexit ;
        if(mgDraw.iLayer >= data.info.L)
        {
            data.info.setL(mgDraw.iLayer + 1);
        }
        mgDraw.draw();
        continue; /* Loop/switch isn't completed */
        Thread.currentThread();
        Thread.sleep(3000L);
_L1:
        if(isRunDraw) goto _L3; else goto _L2
_L2:
        break MISSING_BLOCK_LABEL_212;
        JVM INSTR pop ;
    }

    public synchronized void mRStop()
    {
        send(null);
        mStop();
    }
}
