package paintchat_server;

import java.net.InetAddress;
import paintchat.Config;
import paintchat.M;
import paintchat.debug.Debug;
import syi.util.ByteStream;
import syi.util.PProperties;

public class LineServer
{
  private boolean isLive = true;
  private LineTalker[] talkers = new LineTalker[15];
  private int countTalker = 0;
  private boolean isCash = true;
  private ByteStream bCash = new ByteStream(100000);
  private ByteStream bWork = new ByteStream();
  private M mgCash = null;
  private M mgWork = new M();
  private PchOutputStreamForServer bLog;
  private Debug debug;
  private Server server;
  public Config config;

  public synchronized void mInit(Config paramConfig, Debug paramDebug, Server paramServer)
  {
    this.debug = paramDebug;
    this.config = paramConfig;
    this.server = paramServer;
    this.bLog = new PchOutputStreamForServer(this.config);
    this.isCash = paramConfig.getBool("Server_Cash_Line", true);
  }

  public synchronized void mStop()
  {
    if (!this.isLive)
      return;
    this.isLive = false;
    for (int i = 0; i < this.countTalker; i++)
    {
      LineTalker localLineTalker = this.talkers[i];
      if (localLineTalker == null)
        continue;
      localLineTalker.mStop();
    }
    if (this.bLog != null)
    {
      writeLog();
      this.bLog.close();
    }
  }

  protected void finalize()
    throws Throwable
  {
    this.isLive = false;
  }

  public synchronized void addTalker(LineTalker paramLineTalker)
  {
    Object localObject = this.talkers;
    if (this.countTalker >= this.talkers.length)
    {
      LineTalker[] arrayOfLineTalker = new LineTalker[this.countTalker + 1];
      System.arraycopy(localObject, 0, arrayOfLineTalker, 0, this.countTalker);
      localObject = arrayOfLineTalker;
    }
    localObject[this.countTalker] = paramLineTalker;
    this.countTalker += 1;
    if (this.talkers != localObject)
      this.talkers = ((LineTalker)localObject);
    paramLineTalker.send(this.bCash, this.mgWork, this.bWork);
    this.bLog.getLog(paramLineTalker.getLogArray());
  }

  public synchronized void removeTalker(LineTalker paramLineTalker)
  {
    int i = this.countTalker;
    for (int j = 0; j < i; j++)
    {
      if (paramLineTalker != this.talkers[j])
        continue;
      removeTalker(j);
      break;
    }
  }

  private void removeTalker(int paramInt)
  {
    LineTalker localLineTalker = this.talkers[paramInt];
    this.talkers[paramInt] = null;
    if (localLineTalker != null)
      localLineTalker.mStop();
    if (paramInt + 1 < this.countTalker)
    {
      System.arraycopy(this.talkers, paramInt + 1, this.talkers, paramInt, this.countTalker - (paramInt + 1));
      this.talkers[(this.countTalker - 1)] = null;
    }
    this.countTalker -= 1;
  }

  public synchronized void addLine(LineTalker paramLineTalker, ByteStream paramByteStream)
  {
    try
    {
      int i = 0;
      while (i < this.countTalker)
      {
        LineTalker localLineTalker = this.talkers[i];
        if ((localLineTalker == null) || (!localLineTalker.isValidate()))
        {
          removeTalker(i);
          i = 0;
        }
        else
        {
          if (localLineTalker != paramLineTalker)
            localLineTalker.send(paramByteStream, this.mgWork, this.bWork);
          i++;
        }
      }
      addCash(paramByteStream, false);
    }
    catch (Throwable localThrowable)
    {
      this.debug.log(localThrowable.getMessage());
    }
  }

  public void addClear(LineTalker paramLineTalker, ByteStream paramByteStream)
  {
    addLine(paramLineTalker, paramByteStream);
    this.server.sendClearMessage(paramLineTalker.getAddress());
    newLog();
  }

  private void addCash(ByteStream paramByteStream, boolean paramBoolean)
  {
    if ((!this.isCash) || (paramByteStream == null))
      return;
    int i = paramByteStream.size();
    if (i <= 0)
      return;
    if ((paramBoolean) || (i + this.bCash.size() > 60000))
      writeLog();
    try
    {
      int j = 0;
      byte[] arrayOfByte = paramByteStream.getBuffer();
      while (j < i)
      {
        j += this.mgWork.set(arrayOfByte, j);
        this.mgWork.get(this.bCash, this.bWork, this.mgCash);
        if (this.mgCash == null)
          this.mgCash = new M();
        this.mgCash.set(this.mgWork);
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      this.debug.log(localRuntimeException);
    }
  }

  private void writeLog()
  {
    if (this.bCash.size() <= 0)
      return;
    this.bLog.write(this.bCash.getBuffer(), 0, this.bCash.size());
    this.bCash.reset();
    this.mgCash = null;
  }

  public int getUserCount()
  {
    return this.countTalker;
  }

  public synchronized LineTalker getTalker(InetAddress paramInetAddress)
  {
    for (int i = 0; i < this.countTalker; i++)
    {
      LineTalker localLineTalker = this.talkers[i];
      if ((localLineTalker != null) && (paramInetAddress.equals(localLineTalker.getAddress())))
        return localLineTalker;
    }
    return null;
  }

  public synchronized LineTalker getTalkerAt(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.countTalker))
      return null;
    return this.talkers[paramInt];
  }

  public synchronized void newLog()
  {
    writeLog();
    this.bLog.newLog();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.LineServer
 * JD-Core Version:    0.6.0
 */