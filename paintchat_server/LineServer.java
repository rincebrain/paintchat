package paintchat_server;

import java.net.InetAddress;
import paintchat.Config;
import paintchat.M;
import paintchat.debug.Debug;
import syi.util.ByteStream;
import syi.util.PProperties;

// Referenced classes of package paintchat_server:
//            LineTalker, PchOutputStreamForServer, PaintChatTalker, Server

public class LineServer
{

    private boolean isLive;
    private LineTalker talkers[];
    private int countTalker;
    private boolean isCash;
    private ByteStream bCash;
    private ByteStream bWork;
    private M mgCash;
    private M mgWork;
    private PchOutputStreamForServer bLog;
    private Debug debug;
    private Server server;
    public Config config;

    public LineServer()
    {
        isLive = true;
        talkers = new LineTalker[15];
        countTalker = 0;
        isCash = true;
        bCash = new ByteStream(0x186a0);
        bWork = new ByteStream();
        mgCash = null;
        mgWork = new M();
    }

    public synchronized void mInit(Config config1, Debug debug1, Server server1)
    {
        debug = debug1;
        config = config1;
        server = server1;
        bLog = new PchOutputStreamForServer(config);
        isCash = config1.getBool("Server_Cash_Line", true);
    }

    public synchronized void mStop()
    {
        if(!isLive)
        {
            return;
        }
        isLive = false;
        for(int i = 0; i < countTalker; i++)
        {
            LineTalker linetalker = talkers[i];
            if(linetalker != null)
            {
                linetalker.mStop();
            }
        }

        if(bLog != null)
        {
            writeLog();
            bLog.close();
        }
    }

    protected void finalize()
        throws Throwable
    {
        isLive = false;
    }

    public synchronized void addTalker(LineTalker linetalker)
    {
        LineTalker alinetalker[] = talkers;
        if(countTalker >= talkers.length)
        {
            LineTalker alinetalker1[] = new LineTalker[countTalker + 1];
            System.arraycopy(alinetalker, 0, alinetalker1, 0, countTalker);
            alinetalker = alinetalker1;
        }
        alinetalker[countTalker] = linetalker;
        countTalker++;
        if(talkers != alinetalker)
        {
            talkers = alinetalker;
        }
        linetalker.send(bCash, mgWork, bWork);
        bLog.getLog(linetalker.getLogArray());
    }

    public synchronized void removeTalker(LineTalker linetalker)
    {
        int i = countTalker;
        for(int j = 0; j < i; j++)
        {
            if(linetalker != talkers[j])
            {
                continue;
            }
            removeTalker(j);
            break;
        }

    }

    private void removeTalker(int i)
    {
        LineTalker linetalker = talkers[i];
        talkers[i] = null;
        if(linetalker != null)
        {
            linetalker.mStop();
        }
        if(i + 1 < countTalker)
        {
            System.arraycopy(talkers, i + 1, talkers, i, countTalker - (i + 1));
            talkers[countTalker - 1] = null;
        }
        countTalker--;
    }

    public synchronized void addLine(LineTalker linetalker, ByteStream bytestream)
    {
        try
        {
            for(int i = 0; i < countTalker;)
            {
                LineTalker linetalker1 = talkers[i];
                if(linetalker1 == null || !linetalker1.isValidate())
                {
                    removeTalker(i);
                    i = 0;
                } else
                {
                    if(linetalker1 != linetalker)
                    {
                        linetalker1.send(bytestream, mgWork, bWork);
                    }
                    i++;
                }
            }

            addCash(bytestream, false);
        }
        catch(Throwable throwable)
        {
            debug.log(throwable.getMessage());
        }
    }

    public void addClear(LineTalker linetalker, ByteStream bytestream)
    {
        addLine(linetalker, bytestream);
        server.sendClearMessage(linetalker.getAddress());
        newLog();
    }

    private void addCash(ByteStream bytestream, boolean flag)
    {
        if(!isCash || bytestream == null)
        {
            return;
        }
        int i = bytestream.size();
        if(i <= 0)
        {
            return;
        }
        if(flag || i + bCash.size() > 60000)
        {
            writeLog();
        }
        try
        {
            int j = 0;
            byte abyte0[] = bytestream.getBuffer();
            while(j < i) 
            {
                j += mgWork.set(abyte0, j);
                mgWork.get(bCash, bWork, mgCash);
                if(mgCash == null)
                {
                    mgCash = new M();
                }
                mgCash.set(mgWork);
            }
        }
        catch(RuntimeException runtimeexception)
        {
            debug.log(runtimeexception);
        }
    }

    private void writeLog()
    {
        if(bCash.size() <= 0)
        {
            return;
        } else
        {
            bLog.write(bCash.getBuffer(), 0, bCash.size());
            bCash.reset();
            mgCash = null;
            return;
        }
    }

    public int getUserCount()
    {
        return countTalker;
    }

    public synchronized LineTalker getTalker(InetAddress inetaddress)
    {
        for(int i = 0; i < countTalker; i++)
        {
            LineTalker linetalker = talkers[i];
            if(linetalker != null && inetaddress.equals(linetalker.getAddress()))
            {
                return linetalker;
            }
        }

        return null;
    }

    public synchronized LineTalker getTalkerAt(int i)
    {
        if(i < 0 || i >= countTalker)
        {
            return null;
        } else
        {
            return talkers[i];
        }
    }

    public synchronized void newLog()
    {
        writeLog();
        bLog.newLog();
    }
}
