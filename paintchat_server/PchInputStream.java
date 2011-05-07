package paintchat_server;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import paintchat.Res;
import syi.util.ByteInputStream;
import syi.util.ByteStream;
import syi.util.Io;
import syi.util.VectorBin;

public class PchInputStream
{
  private VectorBin lines = new VectorBin();
  private Res res = new Res();
  private InputStream in;
  boolean isRead = false;
  private int iMax = 10000000;

  public PchInputStream(InputStream paramInputStream, int paramInt)
  {
    this.in = paramInputStream;
    this.iMax = Math.max(paramInt, 0);
  }

  public VectorBin getLines()
  {
    getPch();
    return this.lines;
  }

  public Res getStatus()
  {
    getPch();
    return this.res;
  }

  private void getPch()
  {
    if (this.isRead)
      return;
    this.isRead = true;
    ByteStream localByteStream = new ByteStream();
    byte[] arrayOfByte = { 13, 10, 13, 10 };
    int j = 0;
    try
    {
      while (true)
      {
        int i = Io.r(this.in);
        if (i < 0)
          throw new EOFException();
        if (arrayOfByte[j] == i)
        {
          j++;
          if (j >= arrayOfByte.length)
            break;
        }
        else
        {
          j = 0;
        }
        localByteStream.write(i);
      }
      if (localByteStream.size() > 0)
      {
        ByteInputStream localByteInputStream = new ByteInputStream();
        localByteInputStream.setByteStream(localByteStream);
        this.res.load(localByteInputStream);
      }
      while (true)
      {
        j = Io.readUShort(this.in);
        if (j < 0)
          break;
        if (j >= 2)
        {
          arrayOfByte = new byte[j];
          Io.rFull(this.in, arrayOfByte, 0, j);
          this.lines.add(arrayOfByte);
          while ((this.lines.size() > 0) && (this.lines.getSizeBytes() > this.iMax))
            this.lines.remove(1);
        }
      }
    }
    catch (EOFException localEOFException)
    {
    }
    catch (IOException localIOException1)
    {
      localIOException1.printStackTrace();
    }
    try
    {
      this.in.close();
      this.in = null;
    }
    catch (IOException localIOException2)
    {
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.PchInputStream
 * JD-Core Version:    0.6.0
 */