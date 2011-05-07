package syi.util.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import syi.util.ByteStream;
import syi.util.Io;

public class ZipOutputStreamCustom extends ZipOutputStream
{
  private boolean is_deflate = true;
  private CRC32 crc32 = null;

  public ZipOutputStreamCustom(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      File localFile = new File("C:\\IBMVJava\\Ide\\project_resources\\PaintChat\\sub\\bbs\\pbbs\\");
      ZipOutputStreamCustom localZipOutputStreamCustom = new ZipOutputStreamCustom(new FileOutputStream(new File(localFile, "_PaintBBS.jar")));
      localZipOutputStreamCustom.setMethod(8);
      localZipOutputStreamCustom.setLevel(9);
      localZipOutputStreamCustom.putZip(new ZipInputStream(new FileInputStream(new File(localFile, "PaintBBS.jar"))));
      localZipOutputStreamCustom.finish();
      localZipOutputStreamCustom.close();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    System.exit(0);
  }

  public void putBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, String paramString)
    throws IOException
  {
    ZipEntry localZipEntry = new ZipEntry(paramString);
    if (!this.is_deflate)
    {
      if (this.crc32 == null)
        this.crc32 = new CRC32();
      else
        this.crc32.reset();
      this.crc32.update(paramArrayOfByte);
      localZipEntry.setSize(paramArrayOfByte.length);
      localZipEntry.setCrc(this.crc32.getValue());
    }
    putNextEntry(localZipEntry);
    write(paramArrayOfByte, paramInt1, paramInt2);
    closeEntry();
  }

  public void putDirectory(File paramFile, String paramString)
    throws IOException
  {
    File localFile = new File(paramFile, paramString);
    if (localFile.isFile())
    {
      putFile(paramFile, paramString);
      return;
    }
    if (!localFile.exists())
      return;
    String[] arrayOfString = localFile.list();
    for (int i = 0; i < arrayOfString.length; i++)
    {
      new File(localFile, arrayOfString[i]);
      putDirectory(paramFile, paramString + '/' + arrayOfString[i]);
    }
  }

  public void putFile(File paramFile, String paramString)
    throws IOException
  {
    byte[] arrayOfByte = (byte[])null;
    File localFile = new File(paramFile, paramString);
    if (localFile.isDirectory())
    {
      putDirectory(paramFile, paramString);
      return;
    }
    if (!localFile.exists())
      return;
    try
    {
      arrayOfByte = new byte[(int)localFile.length()];
      FileInputStream localFileInputStream = new FileInputStream(localFile);
      Io.rFull(localFileInputStream, arrayOfByte, 0, arrayOfByte.length);
      localFileInputStream.close();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      arrayOfByte = (byte[])null;
    }
    if (arrayOfByte == null)
      return;
    putBytes(arrayOfByte, 0, arrayOfByte.length, paramString);
  }

  public void putFile(String paramString1, String paramString2)
    throws IOException
  {
    putFile(new File(paramString1), paramString2);
  }

  public void putZip(ZipInputStream paramZipInputStream)
    throws IOException
  {
    ByteStream localByteStream = new ByteStream();
    byte[] arrayOfByte = new byte[512];
    ZipEntry localZipEntry;
    while ((localZipEntry = paramZipInputStream.getNextEntry()) != null)
    {
      int i;
      while ((i = paramZipInputStream.read(arrayOfByte)) != -1)
        localByteStream.write(arrayOfByte, 0, i);
      paramZipInputStream.closeEntry();
      putBytes(localByteStream.getBuffer(), 0, localByteStream.size(), localZipEntry.getName());
      localByteStream.reset();
    }
    paramZipInputStream.close();
  }

  public void setMethod(int paramInt)
  {
    this.is_deflate = (paramInt == 8);
    super.setMethod(paramInt);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.zip.ZipOutputStreamCustom
 * JD-Core Version:    0.6.0
 */