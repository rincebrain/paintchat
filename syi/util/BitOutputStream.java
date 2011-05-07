package syi.util;

import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream extends OutputStream
{
  private int bit_count = 0;
  private int bit;
  private OutputStream out;

  public BitOutputStream(OutputStream paramOutputStream)
  {
    this.out = paramOutputStream;
  }

  public void close()
    throws IOException
  {
    if (this.bit_count > 0)
      wBit(0, 8 - this.bit_count);
    flush();
    this.out.close();
  }

  public void flush()
    throws IOException
  {
    this.out.flush();
  }

  public void wBit(int paramInt1, int paramInt2)
    throws IOException
  {
    while (paramInt2 > 0)
    {
      int i;
      if ((this.bit_count == 0) && (paramInt2 >= 8))
      {
        i = 8;
        this.out.write(paramInt1 & 0xFF);
      }
      else
      {
        i = Math.min(8 - this.bit_count, paramInt2);
        this.bit |= paramInt1 << this.bit_count;
        this.bit_count += i;
        if (this.bit_count >= 8)
        {
          this.out.write(this.bit);
          this.bit = 0;
          this.bit_count = 0;
        }
      }
      paramInt1 >>>= i;
      paramInt2 -= i;
    }
  }

  public void write(int paramInt)
    throws IOException
  {
    wBit(paramInt, 8);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.BitOutputStream
 * JD-Core Version:    0.6.0
 */