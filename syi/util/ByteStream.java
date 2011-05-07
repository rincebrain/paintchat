package syi.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteStream extends OutputStream
{
  private byte[] buffer;
  private int last = 0;

  public ByteStream()
  {
    this(512);
  }

  public ByteStream(byte[] paramArrayOfByte)
  {
    this.buffer = paramArrayOfByte;
  }

  public ByteStream(int paramInt)
  {
    this.buffer = new byte[paramInt <= 0 ? 512 : paramInt];
  }

  public final void addSize(int paramInt)
  {
    int i = this.last + paramInt;
    if (this.buffer.length < i)
    {
      byte[] arrayOfByte = new byte[Math.max((int)(this.buffer.length * 1.5F), i) + 1];
      System.arraycopy(this.buffer, 0, arrayOfByte, 0, this.buffer.length);
      this.buffer = arrayOfByte;
    }
  }

  public void gc()
  {
    if (this.buffer.length == this.last)
      return;
    byte[] arrayOfByte = new byte[this.last];
    if (this.last != 0)
      System.arraycopy(this.buffer, 0, arrayOfByte, 0, this.last);
    this.buffer = arrayOfByte;
  }

  public byte[] getBuffer()
  {
    return this.buffer;
  }

  public final void insert(int paramInt1, int paramInt2)
  {
    this.buffer[paramInt1] = (byte)paramInt2;
  }

  public void reset()
  {
    this.last = 0;
  }

  public void reset(int paramInt)
  {
    int i = this.last;
    reset();
    if (paramInt < i)
      write(this.buffer, paramInt, i - paramInt);
  }

  public void seek(int paramInt)
  {
    this.last = paramInt;
  }

  public final int size()
  {
    return this.last;
  }

  public byte[] toByteArray()
  {
    byte[] arrayOfByte = new byte[this.last];
    if (this.last > 0)
      System.arraycopy(this.buffer, 0, arrayOfByte, 0, this.last);
    return arrayOfByte;
  }

  public final void w(long paramLong, int paramInt)
    throws IOException
  {
    for (int i = paramInt - 1; i >= 0; i--)
      write((int)(paramLong >>> (i << 3)) & 0xFF);
  }

  public final void w2(int paramInt)
    throws IOException
  {
    write(paramInt >>> 8 & 0xFF);
    write(paramInt & 0xFF);
  }

  public final void write(byte[] paramArrayOfByte)
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public final void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    addSize(paramInt2);
    System.arraycopy(paramArrayOfByte, paramInt1, this.buffer, this.last, paramInt2);
    this.last += paramInt2;
  }

  public final void write(int paramInt)
    throws IOException
  {
    addSize(1);
    this.buffer[(this.last++)] = (byte)paramInt;
  }

  public void write(InputStream paramInputStream)
    throws IOException
  {
    addSize(128);
    try
    {
      int i;
      while ((i = paramInputStream.read(this.buffer, this.last, this.buffer.length - this.last)) != -1)
      {
        this.last += i;
        if (this.last < this.buffer.length)
          continue;
        addSize(256);
      }
    }
    catch (IOException localIOException)
    {
    }
  }

  public void write(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    if (paramInt == 0)
      return;
    addSize(paramInt);
    int j = 0;
    int i;
    while ((i = paramInputStream.read(this.buffer, this.last, paramInt - j)) != -1)
    {
      this.last += i;
      j += i;
      if (j >= paramInt)
        break;
    }
    if (j < paramInt)
      throw new EOFException();
  }

  public byte[] writeTo(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + this.last;
    if (paramArrayOfByte == null)
      paramArrayOfByte = new byte[i];
    if (paramArrayOfByte.length < i)
    {
      byte[] arrayOfByte = new byte[i];
      System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
      paramArrayOfByte = arrayOfByte;
    }
    System.arraycopy(this.buffer, 0, paramArrayOfByte, paramInt, this.last);
    return paramArrayOfByte;
  }

  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    if (this.last == 0)
      return;
    paramOutputStream.write(this.buffer, 0, this.last);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.ByteStream
 * JD-Core Version:    0.6.0
 */