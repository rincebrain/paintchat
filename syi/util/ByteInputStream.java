package syi.util;

import java.io.IOException;
import java.io.InputStream;

public class ByteInputStream extends InputStream
{
  private byte[] buffer;
  private int iSeekStart = 0;
  private int iSeek = 0;
  private int iLen = 0;

  public int available()
  {
    return this.iLen - this.iSeek;
  }

  public int read()
  {
    return this.iSeek >= this.iLen ? -1 : this.buffer[(this.iSeek++)] & 0xFF;
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (this.iSeek >= this.iLen)
      return -1;
    paramInt2 = Math.min(this.iLen - this.iSeek, paramInt2);
    System.arraycopy(this.buffer, this.iSeek, paramArrayOfByte, paramInt1, paramInt2);
    this.iSeek += paramInt2;
    return paramInt2;
  }

  public void reset()
  {
    this.iSeek = this.iSeekStart;
  }

  public void setBuffer(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.buffer = paramArrayOfByte;
    this.iLen = (paramInt1 + paramInt2);
    this.iSeekStart = (this.iSeek = paramInt1);
  }

  public void setByteStream(ByteStream paramByteStream)
  {
    setBuffer(paramByteStream.getBuffer(), 0, paramByteStream.size());
  }

  public void close()
    throws IOException
  {
  }

  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public long skip(long paramLong)
    throws IOException
  {
    paramLong = Math.min(paramLong, this.iLen - this.iSeek);
    this.iSeek = (int)(this.iSeek + paramLong);
    return paramLong;
  }

  public boolean markSupported()
  {
    return true;
  }

  public void mark(int paramInt)
  {
    this.iSeekStart = this.iSeek;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.ByteInputStream
 * JD-Core Version:    0.6.0
 */