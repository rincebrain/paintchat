package paintchat_client;

import java.io.IOException;
import java.util.Hashtable;
import paintchat.MgText;
import paintchat.Res;
import paintchat_server.PaintChatTalker;
import syi.util.ByteInputStream;
import syi.util.ByteStream;

// Referenced classes of package paintchat_client:
//            Pl, Data

public class TText extends PaintChatTalker
{

    private Pl pl;
    private Data data;
    private MgText mg;
    private ByteInputStream bin;
    private ByteStream stm;
    private Res names;

    public TText(Pl pl1, Data data1)
    {
        mg = new MgText();
        bin = new ByteInputStream();
        stm = new ByteStream();
        names = new Res();
        pl = pl1;
        data = data1;
        super.iSendInterval = 1000;
    }

    public void mInit()
    {
    }

    protected synchronized void mDestroy()
    {
        try
        {
            MgText mgtext = new MgText(0, (byte)2, (String) null);
            stm.reset();
            mgtext.getData(stm, false);
            write(stm);
            stm.reset();
            flush();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    protected void mRead(ByteStream bytestream)
        throws IOException
    {
        int i = 0;
        int j = bytestream.size();
        bin.setByteStream(bytestream);
        while(i < j) 
        {
            i += mg.setData(bin);
            String s = mg.toString();
            Integer integer = new Integer(mg.ID);
            switch(mg.head)
            {
            case 1: // '\001'
                names.put(integer, s);
                pl.addInOut(s, true);
                break;

            case 2: // '\002'
                names.remove(integer);
                pl.addInOut(s, false);
                break;

            case 0: // '\0'
                pl.addText(mg.bName == null ? (String)names.get(integer) : mg.getUserName(), s, true);
                pl.dSound(1);
                break;

            case 102: // 'f'
                data.mPermission(s);
                break;

            default:
                pl.addText(null, s, true);
                pl.dSound(1);
                break;
            }
        }
    }

    protected void mIdle(long l)
        throws IOException
    {
    }

    protected synchronized void mWrite()
        throws IOException
    {
        if(stm.size() <= 0)
        {
            return;
        } else
        {
            write(stm);
            stm.reset();
            return;
        }
    }

    public synchronized void send(MgText mgtext)
    {
        try
        {
            mgtext.getData(stm, false);
        }
        catch(IOException _ex) { }
    }

    public synchronized void mRStop()
    {
        try
        {
            super.canWrite = true;
            stm.reset();
            (new MgText(0, (byte)2, (String) null)).getData(stm, false);
            write(stm);
            flush();
            mStop();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }
}
