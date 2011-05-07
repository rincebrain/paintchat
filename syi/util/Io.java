package syi.util;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

public class Io
{
  public static void close(Object paramObject)
  {
    if (paramObject == null)
      return;
    try
    {
      if ((paramObject instanceof InputStream))
      {
        ((InputStream)paramObject).close();
        return;
      }
      if ((paramObject instanceof OutputStream))
      {
        ((OutputStream)paramObject).close();
        return;
      }
      if ((paramObject instanceof Reader))
      {
        ((Reader)paramObject).close();
        return;
      }
      if ((paramObject instanceof Writer))
      {
        ((Writer)paramObject).close();
        return;
      }
      if ((paramObject instanceof RandomAccessFile))
      {
        ((RandomAccessFile)paramObject).close();
        return;
      }
    }
    catch (IOException localIOException)
    {
    }
    try
    {
      paramObject.getClass().getMethod("close", new Class[0]).invoke(paramObject, new Object[0]);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
    }
  }

  public static void copyDirectory(File paramFile1, File paramFile2)
  {
    copyDirectory(paramFile1, paramFile2, null);
  }

  public static void copyDirectory(File paramFile1, File paramFile2, Vector paramVector)
  {
    if (!paramFile2.isDirectory())
      paramFile2.mkdirs();
    String[] arrayOfString = paramFile1.list();
    for (int i = 0; i < arrayOfString.length; i++)
    {
      File localFile = new File(paramFile1, arrayOfString[i]);
      if (localFile.isFile())
        copyFile(new File(paramFile1, arrayOfString[i]), paramFile2, paramVector);
      else
        copyDirectory(new File(paramFile1, arrayOfString[i]), new File(paramFile2, arrayOfString[i]), paramVector);
    }
  }

  public static void copy(File paramFile1, File paramFile2, Vector paramVector)
  {
    if (paramFile1.isFile())
      copyFile(paramFile1, paramFile2, paramVector);
    else if (paramFile1.isDirectory())
      copyDirectory(paramFile1, paramFile2, paramVector);
  }

  public static boolean copyFile(File paramFile1, File paramFile2)
  {
    try
    {
      File localFile = getDirectory(paramFile2);
      if (!localFile.isDirectory())
        localFile.mkdirs();
      if (paramFile2.isDirectory())
        paramFile2 = new File(paramFile2, getFileName(paramFile1.toString()));
      byte[] arrayOfByte = new byte[512];
      FileInputStream localFileInputStream = new FileInputStream(paramFile1);
      FileOutputStream localFileOutputStream = new FileOutputStream(paramFile2);
      int i;
      while ((i = localFileInputStream.read(arrayOfByte)) != -1)
        localFileOutputStream.write(arrayOfByte, 0, i);
      localFileOutputStream.flush();
      localFileOutputStream.close();
      localFileInputStream.close();
      return true;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return false;
  }

  public static boolean copyFile(File paramFile1, File paramFile2, Vector paramVector)
  {
    try
    {
      if (paramFile1.isDirectory())
        throw new IOException("src is directory");
      if (paramVector != null)
      {
        int i = 0;
        String str1 = getFileName(paramFile1.getCanonicalPath()).toLowerCase();
        Enumeration localEnumeration = paramVector.elements();
        while (localEnumeration.hasMoreElements())
        {
          String str2 = localEnumeration.nextElement().toString();
          if ((!str2.endsWith("*")) && (!str1.endsWith(str2)))
            continue;
          i = 1;
          break;
        }
        if (i == 0)
          return false;
      }
      copyFile(paramFile1, paramFile2);
      return true;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return false;
  }

  public static void copyFiles(File[] paramArrayOfFile, File paramFile)
  {
    for (int i = 0; i < paramArrayOfFile.length; i++)
      copyFile(paramArrayOfFile[i], paramFile);
  }

  public static void copyFiles(String[] paramArrayOfString, String paramString)
  {
    for (int i = 0; i < paramArrayOfString.length; i++)
      copyFile(new File(paramArrayOfString[i]), new File(paramString));
  }

  public static String getCurrent()
  {
    return System.getProperty("user.dir", "/");
  }

  public static final String getDateString(String paramString1, String paramString2, String paramString3)
  {
    String str1 = '.' + paramString2;
    String str2 = null;
    File localFile1 = new File(paramString3);
    if (!localFile1.isDirectory())
      localFile1.mkdirs();
    try
    {
      GregorianCalendar localGregorianCalendar = new GregorianCalendar();
      String str3 = paramString1 + localGregorianCalendar.get(2) + '-' + localGregorianCalendar.get(5) + '_';
      for (int i = 0; i < 256; i++)
      {
        File localFile2 = new File(paramString3, str3 + i + str1);
        if (localFile2.isFile())
          continue;
        str2 = paramString3 + "/" + str3 + i + str1;
        break;
      }
      if (i >= 32767)
        str2 = paramString3 + "/" + paramString1 + "over_file255" + str1;
    }
    catch (RuntimeException localRuntimeException)
    {
      str2 = paramString1 + "." + paramString2;
    }
    return str2;
  }

  public static File getDirectory(File paramFile)
  {
    try
    {
      return new File(getDirectory(paramFile.getCanonicalPath()));
    }
    catch (IOException localIOException)
    {
    }
    return null;
  }

  public static String getDirectory(String paramString)
  {
    if ((paramString == null) || (paramString.length() <= 0))
      return "./";
    if (paramString.indexOf('\\') >= 0)
      paramString = paramString.replace('\\', '/');
    int i = paramString.lastIndexOf('/');
    if (i < 0)
      return "./";
    int j = paramString.indexOf('.', i);
    if (j < i)
    {
      if (paramString.charAt(paramString.length() - 1) != '/')
        paramString = paramString + '/';
    }
    else
      paramString = paramString.substring(0, i + 1);
    return paramString;
  }

  public static String getFileName(String paramString)
  {
    if (paramString.lastIndexOf('.') < 0)
      return "";
    int i = paramString.lastIndexOf('/');
    if (i < 0)
      i = paramString.lastIndexOf('\\');
    if (i < 0)
      return paramString;
    return paramString.substring(i + 1);
  }

  public static void initFile(File paramFile)
  {
    try
    {
      File localFile = getDirectory(paramFile);
      if (!localFile.isDirectory())
        localFile.mkdirs();
    }
    catch (RuntimeException localRuntimeException)
    {
    }
  }

  public static Image loadImageNow(Component paramComponent, String paramString)
  {
    Image localImage = null;
    try
    {
      localImage = paramComponent.getToolkit().getImage(paramString);
      MediaTracker localMediaTracker = new MediaTracker(paramComponent);
      localMediaTracker.addImage(localImage, 0);
      localMediaTracker.waitForID(0, 10000L);
      localMediaTracker.removeImage(localImage);
      localMediaTracker = null;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return localImage;
  }

  public static String loadString(File paramFile)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramFile));
    int i;
    while ((i = localBufferedReader.read()) != -1)
      localStringBuffer.append((char)i);
    localBufferedReader.close();
    return localStringBuffer.toString();
  }

  public static File makeFile(String paramString1, String paramString2)
  {
    File localFile = toDir(paramString1);
    if (!localFile.exists())
      localFile.mkdirs();
    return new File(localFile, paramString2);
  }

  public static final void moveFile(File paramFile1, File paramFile2)
    throws Throwable
  {
    if (!paramFile1.renameTo(paramFile2))
    {
      copyFile(paramFile1, paramFile2);
      paramFile1.delete();
    }
  }

  public static final int r(InputStream paramInputStream)
    throws IOException
  {
    int i = paramInputStream.read();
    if (i == -1)
      throw new EOFException();
    return i;
  }

  public static final int readInt(InputStream paramInputStream)
    throws IOException
  {
    int i = paramInputStream.read();
    int j = paramInputStream.read();
    int k = paramInputStream.read();
    int m = paramInputStream.read();
    if ((i | j | k | m) < 0)
      throw new EOFException();
    return (i << 24) + (j << 16) + (k << 8) + m;
  }

  public static final short readShort(InputStream paramInputStream)
    throws IOException
  {
    int i = paramInputStream.read();
    int j = paramInputStream.read();
    if ((i | j) < 0)
      throw new EOFException();
    return (short)((i << 8) + j);
  }

  public static final int readUShort(InputStream paramInputStream)
    throws IOException
  {
    int i = paramInputStream.read();
    int j = paramInputStream.read();
    if ((i | j) < 0)
      throw new EOFException();
    return (i << 8) + j;
  }

  public static final void rFull(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws EOFException, IOException
  {
    paramInt2 += paramInt1;
    while (paramInt1 < paramInt2)
    {
      int i = paramInputStream.read(paramArrayOfByte, paramInt1, paramInt2 - paramInt1);
      if (i == -1)
        throw new EOFException();
      paramInt1 += i;
    }
  }

  public static File toDir(String paramString)
  {
    if ((paramString == null) || (paramString.length() <= 0))
      return new File("./");
    if (paramString.indexOf('\\') >= 0)
      paramString = paramString.replace('\\', '/');
    if (paramString.charAt(paramString.length() - 1) != '/')
      paramString = paramString + '/';
    return new File(paramString);
  }

  public static final void wInt(OutputStream paramOutputStream, int paramInt)
    throws IOException
  {
    paramOutputStream.write(paramInt >>> 24);
    paramOutputStream.write(paramInt >>> 16 & 0xFF);
    paramOutputStream.write(paramInt >>> 8 & 0xFF);
    paramOutputStream.write(paramInt & 0xFF);
  }

  public static final void wShort(OutputStream paramOutputStream, int paramInt)
    throws IOException
  {
    paramOutputStream.write(paramInt >>> 8 & 0xFF);
    paramOutputStream.write(paramInt & 0xFF);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.Io
 * JD-Core Version:    0.6.0
 */