package syi.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClassLoaderCustom extends ClassLoader
{
  private Hashtable cash = new Hashtable();
  private ClassLoader cl = getClass().getClassLoader();

  public boolean loadArchive(String paramString)
  {
    try
    {
      Object localObject;
      if (paramString.indexOf("://") != -1)
        localObject = new URL(paramString).openStream();
      else
        localObject = new FileInputStream(new File(Io.getCurrent(), paramString));
      ZipInputStream localZipInputStream = new ZipInputStream((InputStream)localObject);
      byte[] arrayOfByte1 = new byte[4000];
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(4000);
      ZipEntry localZipEntry;
      while ((localZipEntry = localZipInputStream.getNextEntry()) != null)
      {
        String str2 = replace(localZipEntry.getName(), '/', '.', false);
        if (!str2.endsWith(".class"))
          continue;
        localByteArrayOutputStream.reset();
        int i;
        while ((i = localZipInputStream.read(arrayOfByte1)) != -1)
          localByteArrayOutputStream.write(arrayOfByte1, 0, i);
        localByteArrayOutputStream.flush();
        localZipInputStream.closeEntry();
        byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
        String str1 = str2.substring(0, str2.length() - 6);
        Class localClass = defineClass(str1, arrayOfByte2, 0, arrayOfByte2.length);
        arrayOfByte2 = (byte[])null;
        if (localClass != null)
          this.cash.put(str1, localClass);
        localClass = null;
      }
      localZipInputStream.close();
      return true;
    }
    catch (Exception localException)
    {
      System.out.println(localException.getMessage());
    }
    return false;
  }

  public Class loadClass(String paramString1, String paramString2, boolean paramBoolean)
  {
    Class localClass = (Class)this.cash.get(paramString1);
    if (localClass == null)
    {
      if ((paramString2 == null) || (!paramString2.startsWith("http://")))
        localClass = loadLocal(paramString1, paramString2);
      else
        localClass = loadURL(paramString1, paramString2);
      if (localClass == null)
        localClass = Object.class;
      this.cash.put(paramString1, localClass);
    }
    if (paramBoolean)
      resolveClass(localClass);
    return localClass;
  }

  protected Class loadClass(String paramString, boolean paramBoolean)
    throws ClassNotFoundException
  {
    Class localClass = (Class)this.cash.get(paramString);
    if (localClass == null)
      return findSystemClass(paramString);
    if (paramBoolean)
      resolveClass(localClass);
    return localClass;
  }

  private Class loadLocal(String paramString1, String paramString2)
  {
    Class localClass;
    try
    {
      if ((paramString2 == null) || (paramString2.length() <= 0))
      {
        localClass = findSystemClass(paramString1);
        if (localClass != null)
          return localClass;
      }
      File localFile = new File(paramString2, replace(paramString1, '.', File.separatorChar, true));
      if (!localFile.isFile())
        return null;
      byte[] arrayOfByte = new byte[(int)localFile.length()];
      DataInputStream localDataInputStream = new DataInputStream(new FileInputStream(localFile));
      localDataInputStream.readFully(arrayOfByte);
      localDataInputStream.close();
      localClass = defineClass(paramString1, arrayOfByte, 0, arrayOfByte.length);
    }
    catch (IOException localIOException)
    {
      localClass = null;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClass = null;
    }
    return localClass;
  }

  private Class loadURL(String paramString1, String paramString2)
  {
    Class localClass;
    try
    {
      String str = replace(paramString1, '\\', '/', true);
      URL localURL;
      if (paramString2.charAt(paramString2.length() - 1) != '/')
        localURL = new URL(paramString2 + '/' + str);
      else
        localURL = new URL(paramString2 + str);
      str = null;
      URLConnection localURLConnection = localURL.openConnection();
      localURLConnection.connect();
      int i = localURLConnection.getContentLength();
      byte[] arrayOfByte = new byte[i];
      int j = 0;
      InputStream localInputStream = localURLConnection.getInputStream();
      while (j += localInputStream.read(arrayOfByte, j, i - j) < i);
      localInputStream.close();
      localClass = defineClass(paramString1, arrayOfByte, 0, i);
    }
    catch (Exception localException)
    {
      localClass = null;
    }
    return localClass;
  }

  public void putClass(Class paramClass)
  {
    this.cash.put(paramClass.getName(), paramClass);
  }

  private String replace(String paramString, char paramChar1, char paramChar2, boolean paramBoolean)
  {
    StringBuffer localStringBuffer = new StringBuffer(paramString);
    int i = localStringBuffer.length();
    for (int j = 0; j < i; j++)
    {
      if (localStringBuffer.charAt(j) != paramChar1)
        continue;
      localStringBuffer.setCharAt(j, paramChar2);
    }
    if (paramBoolean)
      localStringBuffer.append(".class");
    return localStringBuffer.toString();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.ClassLoaderCustom
 * JD-Core Version:    0.6.0
 */