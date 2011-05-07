package paintchat;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class MgText
{
  public byte head = 100;
  public int ID = 0;
  public byte[] bName = null;
  private byte[] data = null;
  private int seekMax = 0;
  private String strData = null;
  public static final String ENCODE = "UTF8";
  private static final String EMPTY = "";
  public static final byte M_TEXT = 0;
  public static final byte M_IN = 1;
  public static final byte M_OUT = 2;
  public static final byte M_UPDATE = 3;
  public static final byte M_VERSION = 4;
  public static final byte M_EXIT = 5;
  public static final byte M_INFOMATION = 6;
  public static final byte M_SCRIPT = 8;
  public static final byte M_EMPTY = 100;
  public static final byte M_SERVER = 102;

  public MgText()
  {
  }

  public MgText(int paramInt, byte paramByte, byte[] paramArrayOfByte)
  {
    setData(paramInt, paramByte, paramArrayOfByte);
  }

  public MgText(int paramInt, byte paramByte, String paramString)
  {
    setData(paramInt, paramByte, paramString);
  }

  public MgText(MgText paramMgText)
  {
    setData(paramMgText);
  }

  public void getData(OutputStream paramOutputStream, boolean paramBoolean)
    throws IOException
  {
    if (this.head == -1)
      return;
    toBin(false);
    int i = (this.bName == null) || (!paramBoolean) ? 0 : this.bName.length;
    int j = this.seekMax + 4 + i;
    if (i >= 255)
      throw new IOException("longer name");
    w2(paramOutputStream, j);
    paramOutputStream.write(this.head);
    w2(paramOutputStream, this.ID);
    if (paramBoolean)
    {
      paramOutputStream.write(i);
      if (i > 0)
        paramOutputStream.write(this.bName);
    }
    else
    {
      paramOutputStream.write(0);
    }
    if (this.seekMax > 0)
      paramOutputStream.write(this.data, 0, this.seekMax);
  }

  public String getUserName()
  {
    try
    {
      if (this.bName != null)
        return new String(this.bName, "UTF8");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    return "";
  }

  public void setUserName(Object paramObject)
  {
    try
    {
      if (paramObject == null)
      {
        this.bName = null;
      }
      else if ((paramObject instanceof String))
      {
        String str = (String)paramObject;
        this.bName = (str.length() <= 0 ? null : str.getBytes("UTF8"));
      }
      else
      {
        this.bName = ((byte[])paramObject);
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      this.bName = null;
    }
  }

  public int getValueSize()
  {
    toBin(false);
    return this.seekMax;
  }

  private final int r(InputStream paramInputStream)
    throws IOException
  {
    int i = paramInputStream.read();
    if (i == -1)
      throw new EOFException();
    return i;
  }

  private final int r2(InputStream paramInputStream)
    throws IOException
  {
    return r(paramInputStream) << 8 | r(paramInputStream);
  }

  private final void r(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    int i = 0;
    while (i < paramInt)
    {
      int j = paramInputStream.read(paramArrayOfByte, i, paramInt - i);
      if (j == -1)
        throw new EOFException();
      i += j;
    }
  }

  private final void w2(OutputStream paramOutputStream, int paramInt)
    throws IOException
  {
    paramOutputStream.write(paramInt >>> 8 & 0xFF);
    paramOutputStream.write(paramInt & 0xFF);
  }

  public void setData(int paramInt, byte paramByte, byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    this.head = paramByte;
    this.ID = paramInt;
    this.bName = null;
    if ((this.data == null) || (this.data.length < i))
      this.data = paramArrayOfByte;
    else
      System.arraycopy(paramArrayOfByte, 0, this.data, 0, i);
    this.seekMax = i;
    this.strData = null;
  }

  public void setData(int paramInt, byte paramByte, String paramString)
  {
    this.head = paramByte;
    this.ID = paramInt;
    this.bName = null;
    this.strData = paramString;
    this.seekMax = 0;
  }

  public int setData(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      this.strData = null;
      int i = r2(paramInputStream);
      this.head = (byte)r(paramInputStream);
      this.ID = r2(paramInputStream);
      int j = r(paramInputStream);
      if (j <= 0)
      {
        this.bName = null;
      }
      else
      {
        if (j >= 255)
          throw new IOException("abnormal");
        this.bName = new byte[j];
        r(paramInputStream, this.bName, j);
      }
      int k = i - 1 - 2 - 1 - j;
      if (k > 0)
      {
        if (k >= 1024)
          throw new IOException("abnormal");
        if ((this.data == null) || (this.data.length < k))
          this.data = new byte[k];
        r(paramInputStream, this.data, k);
        this.seekMax = k;
      }
      else
      {
        this.seekMax = 0;
      }
      return i + 2;
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
      this.head = 100;
      this.ID = 0;
      this.bName = null;
      this.seekMax = 0;
    }
    return 2;
  }

  public void setData(MgText paramMgText)
  {
    this.head = paramMgText.head;
    this.ID = paramMgText.ID;
    this.bName = paramMgText.bName;
    this.seekMax = paramMgText.seekMax;
    if (this.seekMax > 0)
    {
      if ((this.data == null) || (this.data.length < this.seekMax))
        this.data = new byte[this.seekMax];
      System.arraycopy(paramMgText.data, 0, this.data, 0, this.seekMax);
    }
    this.strData = paramMgText.strData;
  }

  public final void toBin(boolean paramBoolean)
  {
    if ((this.seekMax == 0) && (this.strData != null) && (this.strData.length() > 0))
      try
      {
        byte[] arrayOfByte = this.strData.getBytes("UTF8");
        int i = arrayOfByte.length;
        if ((this.data == null) || (this.data.length < i))
          this.data = arrayOfByte;
        else
          System.arraycopy(arrayOfByte, 0, this.data, 0, i);
        this.seekMax = i;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        this.strData = null;
        this.seekMax = 0;
      }
    if (paramBoolean)
      this.strData = null;
  }

  public String toString()
  {
    try
    {
      if ((this.seekMax > 0) && (this.strData == null))
        this.strData = new String(this.data, 0, this.seekMax, "UTF8");
      if (this.strData != null)
        return this.strData;
    }
    catch (RuntimeException localRuntimeException)
    {
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    this.strData = null;
    this.seekMax = 0;
    return "";
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.MgText
 * JD-Core Version:    0.6.0
 */