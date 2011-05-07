package syi.util;

public class VectorBin
{
  private byte[][] elements = null;
  private int seek = 0;
  private float sizeAdd = 1.5F;
  private int sizeDefault = 25;

  public VectorBin()
  {
    this(10);
  }

  public VectorBin(int paramInt)
  {
    paramInt = paramInt <= 0 ? 50 : paramInt;
    this.sizeDefault = paramInt;
    this.elements = new byte[paramInt];
  }

  public final synchronized void add(byte[] paramArrayOfByte)
  {
    if (this.seek >= this.elements.length)
      moreData(this.seek + 1);
    this.elements[(this.seek++)] = paramArrayOfByte;
  }

  public final synchronized void copy(byte[][] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
  {
    paramInt3 = paramInt1 + paramInt3 > this.seek ? this.seek - paramInt1 : paramInt3;
    if (paramInt3 <= 0)
      return;
    System.arraycopy(this.elements, paramInt1, paramArrayOfByte, paramInt2, paramInt3);
  }

  public final synchronized void copy(VectorBin paramVectorBin)
  {
    for (int i = 0; i < this.seek; i++)
      paramVectorBin.add(this.elements[i]);
  }

  public final synchronized void gc()
  {
    int i = Math.max(this.seek, this.sizeDefault) + 3;
    if (this.elements.length > i)
    {
      byte[][] arrayOfByte = new byte[i];
      System.arraycopy(this.elements, 0, arrayOfByte, 0, this.seek);
      this.elements = arrayOfByte;
    }
  }

  public final synchronized byte[] get(int paramInt)
  {
    return paramInt >= this.seek ? null : this.elements[paramInt];
  }

  public synchronized int getSizeBytes()
  {
    int i = 0;
    byte[][] arrayOfByte = this.elements;
    int j = this.seek;
    for (int k = 0; k < j; k++)
      i += arrayOfByte[k].length;
    return i;
  }

  private final void moreData(int paramInt)
  {
    byte[][] arrayOfByte = new byte[paramInt + Math.max((int)(paramInt * this.sizeAdd), 1)];
    System.arraycopy(this.elements, 0, arrayOfByte, 0, this.elements.length);
    this.elements = arrayOfByte;
  }

  public final synchronized void remove(int paramInt)
  {
    paramInt = Math.min(paramInt, this.seek);
    if (paramInt <= 0)
      return;
    if (this.seek != paramInt)
      System.arraycopy(this.elements, paramInt, this.elements, 0, this.seek - paramInt);
    for (int i = this.seek - paramInt; i < this.seek; i++)
      this.elements[i] = null;
    this.seek -= paramInt;
  }

  public final synchronized void removeAll()
  {
    remove(this.seek);
  }

  public final int size()
  {
    return this.seek;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.VectorBin
 * JD-Core Version:    0.6.0
 */