package syi.util;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;

public class PProperties extends Hashtable
{
  private static final String str_empty = "";

  public final boolean getBool(String paramString)
  {
    return getBool(paramString, false);
  }

  public final boolean getBool(String paramString, boolean paramBoolean)
  {
    try
    {
      paramString = (String)get(paramString);
      if ((paramString == null) || (paramString.length() <= 0))
        return paramBoolean;
      int i = Character.toLowerCase(paramString.charAt(0));
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
    catch (Exception localException)
    {
    }
    return paramBoolean;
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
      String str = (String)get(paramString);
      if ((str != null) && (str.length() > 0))
        return Integer.decode(str).intValue();
    }
    catch (Throwable localThrowable)
    {
    }
    return paramInt;
  }

  public final String getString(String paramString)
  {
    return getString(paramString, "");
  }

  public final String getString(String paramString1, String paramString2)
  {
    if (paramString1 == null)
      return paramString2;
    Object localObject = get(paramString1);
    if (localObject == null)
      return paramString2;
    return localObject.toString();
  }

  public synchronized boolean load(InputStream paramInputStream)
  {
    clear();
    return loadPut(paramInputStream);
  }

  public synchronized boolean load(Reader paramReader)
  {
    clear();
    return loadPut(paramReader);
  }

  public synchronized void load(String paramString)
  {
    if ((paramString == null) || (paramString.length() <= 0))
      return;
    load(new StringReader(paramString));
  }

  public synchronized boolean loadPut(InputStream paramInputStream)
  {
    return loadPut(new InputStreamReader(paramInputStream));
  }

  public synchronized boolean loadPut(Reader paramReader)
  {
    try
    {
      BufferedReader localBufferedReader = (paramReader instanceof StringReader) ? paramReader : new BufferedReader(paramReader, 1024);
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
              localObject = str.substring(0, i);
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
      System.out.println("load" + localIOException.getMessage());
      return false;
    }
    return true;
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

  public void save(OutputStream paramOutputStream)
    throws Exception
  {
    save(paramOutputStream, null);
  }

  public synchronized void save(OutputStream paramOutputStream, Hashtable paramHashtable)
    throws IOException
  {
    PrintWriter localPrintWriter = new PrintWriter(new OutputStreamWriter(paramOutputStream), false);
    int i = paramHashtable != null ? 1 : 0;
    Enumeration localEnumeration = keys();
    while (localEnumeration.hasMoreElements())
    {
      String str2 = (String)localEnumeration.nextElement();
      if ((str2 == null) || (str2.length() <= 0) || (str2.charAt(0) == '#'))
        continue;
      if (i != 0)
      {
        String str3 = (String)paramHashtable.get(str2);
        if ((str3 != null) && (str3.length() > 0))
        {
          StringReader localStringReader = new StringReader(str3);
          try
          {
            while (true)
            {
              String str4 = readLine(localStringReader);
              localPrintWriter.print('#');
              localPrintWriter.println(str4);
            }
          }
          catch (EOFException localEOFException)
          {
          }
        }
      }
      String str1 = getString(str2);
      localPrintWriter.print(str2);
      localPrintWriter.print('=');
      if ((str1 != null) && (str1.length() > 0))
        localPrintWriter.print(str1);
      localPrintWriter.println();
      localPrintWriter.println();
    }
    localPrintWriter.flush();
    localPrintWriter.close();
    paramOutputStream.close();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.PProperties
 * JD-Core Version:    0.6.0
 */