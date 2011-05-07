package paintchat;

import java.applet.Applet;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import syi.awt.Awt;
import syi.util.ByteStream;

public class Res extends Hashtable
{
  private Object resBase;
  private Applet applet;
  private ByteStream work;
  private static final String EMPTY = "";

  public Res()
  {
    this(null, null, null);
  }

  public Res(Applet paramApplet, Object paramObject, ByteStream paramByteStream)
  {
    this.resBase = paramObject;
    this.applet = paramApplet;
    this.work = paramByteStream;
  }

  public final String get(String paramString)
  {
    return get(paramString, "");
  }

  public final String get(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      return paramString2;
    String str = (String)super.get(paramString1);
    if (str == null)
      return paramString2;
    return str;
  }

  public final boolean getBool(String paramString)
  {
    return getBool(paramString, false);
  }

  public final boolean getBool(String paramString, boolean paramBoolean)
  {
    try
    {
      paramString = get(paramString);
      if ((paramString == null) || (paramString.length() <= 0))
        return paramBoolean;
      int i = paramString.charAt(0);
      switch (i)
      {
      case 49:
      case 111:
      case 116:
      case 121:
        return true;
      case 48:
      case 102:
      case 110:
      case 120:
        return false;
      }
    }
    catch (RuntimeException localRuntimeException)
    {
    }
    return paramBoolean;
  }

  public ByteStream getBuffer()
  {
    if (this.work == null)
      this.work = new ByteStream();
    else
      this.work.reset();
    return this.work;
  }

  public final int getInt(String paramString)
  {
    try
    {
      return getInt(paramString, 0);
    }
    catch (Exception localException)
    {
    }
    return 0;
  }

  public final int getInt(String paramString, int paramInt)
  {
    try
    {
      String str = get(paramString);
      if ((str != null) && (str.length() > 0))
        return parseInt(str);
    }
    catch (Throwable localThrowable)
    {
    }
    return paramInt;
  }

  public String getP(String paramString)
  {
    String str = p(paramString);
    if (str != null)
      return str;
    return get(paramString, null);
  }

  public final int getP(String paramString, int paramInt)
  {
    String str = p(paramString);
    if (str != null)
      put(paramString, str);
    return getInt(paramString, paramInt);
  }

  public String getP(String paramString1, String paramString2)
  {
    String str = p(paramString1);
    return (str == null) || (str.length() <= 0) ? get(paramString1, paramString2) : str;
  }

  public boolean getP(String paramString, boolean paramBoolean)
  {
    String str = p(paramString);
    if (str != null)
      put(paramString, str);
    return getBool(paramString, paramBoolean);
  }

  public final Object getRes(Object paramObject)
  {
    try
    {
      Object localObject = get(paramObject);
      if (localObject == null)
      {
        ByteStream localByteStream = getBuffer();
        localByteStream.write(Awt.openStream((this.resBase instanceof String) ? new URL(this.applet.getCodeBase(), (String)this.resBase + (String)paramObject) : new URL((URL)this.resBase, (String)paramObject)));
        return localByteStream.toByteArray();
      }
      return localObject;
    }
    catch (IOException localIOException)
    {
    }
    return null;
  }

  public boolean load(InputStream paramInputStream)
  {
    try
    {
      return load(new InputStreamReader(paramInputStream, "UTF8"));
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    return false;
  }

  public boolean load(Reader paramReader)
  {
    try
    {
      BufferedReader localBufferedReader = (paramReader instanceof StringReader) ? paramReader : new BufferedReader(paramReader, 512);
      CharArrayWriter localCharArrayWriter = new CharArrayWriter();
      Object localObject = null;
      try
      {
        while (true)
        {
          String str = readLine(localBufferedReader);
          if (str != null)
          {
            int i = str.indexOf('=');
            if (i > 0)
            {
              if (localObject != null)
              {
                put(localObject, localCharArrayWriter.toString());
                localObject = null;
              }
              localObject = str.substring(0, i).trim();
              localCharArrayWriter.reset();
              if (i + 1 < str.length())
                localCharArrayWriter.write(str.substring(i + 1));
            }
            else if (localObject != null)
            {
              localCharArrayWriter.write(10);
              localCharArrayWriter.write(str);
            }
          }
        }
      }
      catch (EOFException localEOFException)
      {
        if ((localObject != null) && (localCharArrayWriter.size() > 0))
          put(localObject, localCharArrayWriter.toString());
        localBufferedReader.close();
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      return false;
    }
    return true;
  }

  public void load(String paramString)
  {
    if ((paramString == null) || (paramString.length() <= 0))
      return;
    load(new StringReader(paramString));
  }

  public void loadResource(Res paramRes, String paramString1, String paramString2)
  {
    ((paramString2 != null) && (paramString2.equals("ja")) ? 1 : 0);
    String str = paramString1 + ((paramString2 != null) && (paramString2.length() != 0) ? '_' + paramString2 : "") + ".txt";
    for (int i = 0; i < 2; i++)
    {
      try
      {
        byte[] arrayOfByte = (byte[])paramRes.getRes(str);
        if (arrayOfByte != null)
        {
          ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
          load(new InputStreamReader(localByteArrayInputStream, "UTF8"));
          break;
        }
      }
      catch (RuntimeException localRuntimeException)
      {
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
      }
      str = paramString1 + ".txt";
    }
  }

  public void loadZip(InputStream paramInputStream)
    throws IOException
  {
    ByteStream localByteStream = getBuffer();
    ZipInputStream localZipInputStream = new ZipInputStream(paramInputStream);
    ZipEntry localZipEntry;
    while ((localZipEntry = localZipInputStream.getNextEntry()) != null)
    {
      localByteStream.reset();
      localByteStream.write(localZipInputStream);
      r(localByteStream, localZipEntry.getName());
    }
    localZipInputStream.close();
  }

  public void loadZip(String paramString)
    throws IOException
  {
    try
    {
      InputStream localInputStream = getClass().getResourceAsStream('/' + paramString);
      if (localInputStream != null)
      {
        loadZip(localInputStream);
        return;
      }
    }
    catch (Throwable localThrowable)
    {
    }
    loadZip(Awt.openStream(new URL(this.applet.getCodeBase(), paramString)));
  }

  private String p(String paramString)
  {
    return this.applet.getParameter(paramString);
  }

  public static final int parseInt(String paramString)
  {
    int i = paramString.length();
    if (i <= 0)
      return 0;
    int j = 0;
    if (paramString.charAt(0) == '0')
      j = 2;
    else if (paramString.charAt(0) == '#')
      j = 1;
    if (j != 0)
    {
      int k = 0;
      i -= j;
      for (int m = 0; m < i; m++)
        k |= Character.digit(paramString.charAt(m + j), 16) << (i - 1 - m) * 4;
      return k;
    }
    return Integer.parseInt(paramString);
  }

  private void r(ByteStream paramByteStream, String paramString)
    throws IOException
  {
    String str = paramString.toLowerCase();
    if (str.endsWith("zip"))
      loadZip(new ByteArrayInputStream(paramByteStream.toByteArray()));
    else
      put(paramString, paramByteStream.toByteArray());
  }

  private final String readLine(Reader paramReader)
    throws EOFException, IOException
  {
    int i = paramReader.read();
    if (i == -1)
      throw new EOFException();
    if ((i == 13) || (i == 10))
      return null;
    if (i == 35)
    {
      while (true)
      {
        i = paramReader.read();
        if ((i == 13) || (i == 10) || (i == -1))
          break;
      }
      return null;
    }
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append((char)i);
    while ((i = paramReader.read()) != -1)
    {
      if ((i == 13) || (i == 10))
        break;
      localStringBuffer.append((char)i);
    }
    return localStringBuffer.toString();
  }

  public final String res(String paramString)
  {
    return getP(paramString, paramString);
  }

  public void put(Res paramRes)
  {
    Enumeration localEnumeration = paramRes.keys();
    while (localEnumeration.hasMoreElements())
    {
      Object localObject = localEnumeration.nextElement();
      put(localObject, paramRes.get(localObject));
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.Res
 * JD-Core Version:    0.6.0
 */