package syi.util;

public class Vector2
{
  private Object[] elements = null;
  private int seek = 0;
  private float sizeAdd = 1.5F;
  private int sizeDefault = 25;

  public Vector2()
  {
    this(10);
  }

  public Vector2(int paramInt)
  {
    paramInt = paramInt <= 0 ? 50 : paramInt;
    this.sizeDefault = paramInt;
    this.elements = new Object[paramInt];
  }

  public final synchronized void add(Object[] paramArrayOfObject)
  {
    if (paramArrayOfObject.length + this.seek >= this.elements.length)
      moreData(paramArrayOfObject.length + this.seek);
    System.arraycopy(paramArrayOfObject, 0, this.elements, this.seek, paramArrayOfObject.length);
    this.seek += paramArrayOfObject.length;
  }

  public final synchronized void add(Object paramObject)
  {
    if (this.seek >= this.elements.length)
      moreData(this.seek + 1);
    this.elements[(this.seek++)] = paramObject;
  }

  public final synchronized void copy(Object[] paramArrayOfObject, int paramInt1, int paramInt2, int paramInt3)
  {
    paramInt3 = paramInt1 + paramInt3 > this.seek ? this.seek - paramInt1 : paramInt3;
    if (paramInt3 <= 0)
      return;
    System.arraycopy(this.elements, paramInt1, paramArrayOfObject, paramInt2, paramInt3);
  }

  public final synchronized void copy(Vector2 paramVector2)
  {
    int i = this.seek;
    for (int j = 0; j < i; j++)
      paramVector2.add(this.elements[j]);
  }

  public final synchronized void gc()
  {
    int i = Math.max(this.seek, this.sizeDefault) + 3;
    if (this.elements.length > i)
    {
      Object[] arrayOfObject = new Object[i];
      System.arraycopy(this.elements, 0, arrayOfObject, 0, this.seek);
      this.elements = arrayOfObject;
    }
  }

  public final synchronized Object get(int paramInt)
  {
    return paramInt >= this.seek ? null : this.elements[paramInt];
  }

  private final void moreData(int paramInt)
  {
    Object[] arrayOfObject = new Object[paramInt + Math.max((int)(paramInt * this.sizeAdd), 1)];
    System.arraycopy(this.elements, 0, arrayOfObject, 0, this.elements.length);
    this.elements = arrayOfObject;
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
 * Qualified Name:     syi.util.Vector2
 * JD-Core Version:    0.6.0
 */