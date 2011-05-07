package syi.util;

import java.io.InputStream;

public class BitInputStream
{
  private byte[] data;
  private int seekMax;
  private int seekByte = 0;
  private int seekBit = 7;
  private byte nowByte;
  private InputStream in;

  public BitInputStream()
  {
  }

  public BitInputStream(byte[] paramArrayOfByte)
  {
    setArray(paramArrayOfByte);
  }

  public BitInputStream(InputStream paramInputStream)
  {
  }

  public final int r()
  {
    if (this.seekByte >= this.seekMax)
      return -1;
    int i = this.nowByte >> this.seekBit & 0x1;
    if (--this.seekBit < 0)
    {
      this.seekBit = 7;
      this.seekByte += 1;
      if (this.seekByte < this.seekMax)
        this.nowByte = this.data[this.seekByte];
    }
    return i;
  }

  public final int rByte()
  {
    int i = 0;
    for (int j = 7; j >= 0; j--)
    {
      if (r() == -1)
        return j == 7 ? -1 : i;
      i |= r() << j;
    }
    return i;
  }

  public void setArray(byte[] paramArrayOfByte)
  {
    setArray(paramArrayOfByte, paramArrayOfByte.length);
  }

  public void setArray(byte[] paramArrayOfByte, int paramInt)
  {
    setArray(paramArrayOfByte, 0, paramInt);
  }

  public void setArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.data = paramArrayOfByte;
    this.seekByte = paramInt1;
    this.seekBit = 7;
    this.seekMax = paramInt2;
    this.nowByte = this.data[paramInt1];
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.BitInputStream
 * JD-Core Version:    0.6.0
 */