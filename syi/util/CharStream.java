package syi.util;

import java.io.IOException;
import java.io.Writer;

public class CharStream extends Writer
{
  private char[] buffer;
  private int count = 0;
  private static char[] separator;

  public CharStream()
  {
    this(512);
  }

  public CharStream(int paramInt)
  {
    this.buffer = new char[paramInt <= 0 ? 512 : paramInt];
    if (separator == null)
    {
      String str = System.getProperty("line.separator", "\n");
      separator = str.toCharArray();
    }
  }

  public final void addSize(int paramInt)
  {
    int i = this.count + paramInt;
    if (this.buffer.length < i)
    {
      char[] arrayOfChar = new char[Math.max((int)(this.buffer.length * 1.25F), i) + 1];
      System.arraycopy(this.buffer, 0, arrayOfChar, 0, this.buffer.length);
      this.buffer = arrayOfChar;
    }
  }

  public void close()
  {
  }

  public void flush()
  {
  }

  public void gc()
  {
    if (this.buffer.length == this.count)
      return;
    char[] arrayOfChar = new char[this.count];
    if (this.count != 0)
      System.arraycopy(this.buffer, 0, arrayOfChar, 0, this.count);
    this.buffer = arrayOfChar;
  }

  public char[] getBuffer()
  {
    return this.buffer;
  }

  public final void insert(int paramInt1, int paramInt2)
  {
    this.buffer[paramInt1] = (char)paramInt2;
  }

  public void reset()
  {
    this.count = 0;
  }

  public void seek(int paramInt)
  {
    this.count = paramInt;
  }

  public final int size()
  {
    return this.count;
  }

  public char[] toCharArray()
  {
    char[] arrayOfChar = new char[this.count];
    if (this.count > 0)
      System.arraycopy(this.buffer, 0, arrayOfChar, 0, this.count);
    return arrayOfChar;
  }

  public final void write(char[] paramArrayOfChar)
  {
    write(paramArrayOfChar, 0, paramArrayOfChar.length);
  }

  public final void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    addSize(paramInt2);
    System.arraycopy(paramArrayOfChar, paramInt1, this.buffer, this.count, paramInt2);
    this.count += paramInt2;
  }

  public final void write(int paramInt)
    throws IOException
  {
    addSize(1);
    this.buffer[(this.count++)] = (char)paramInt;
  }

  public final void write(String paramString)
  {
    write(paramString, 0, paramString.length());
  }

  public final void write(String paramString, int paramInt1, int paramInt2)
  {
    addSize(paramInt2);
    paramString.getChars(paramInt1, paramInt1 + paramInt2, this.buffer, this.count);
    this.count += paramInt2;
  }

  public final void writeln(String paramString)
  {
    write(paramString);
    write(separator);
  }

  public void writeTo(char[] paramArrayOfChar, int paramInt)
  {
    int i = paramInt + this.count;
    if (paramInt >= i)
      return;
    if (paramArrayOfChar.length < i)
    {
      char[] arrayOfChar = new char[i];
      System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, paramArrayOfChar.length);
      paramArrayOfChar = arrayOfChar;
    }
    System.arraycopy(this.buffer, 0, paramArrayOfChar, paramInt, this.count);
  }

  public void writeTo(Writer paramWriter)
    throws IOException
  {
    if (this.count == 0)
      return;
    paramWriter.write(this.buffer, 0, this.count);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.CharStream
 * JD-Core Version:    0.6.0
 */