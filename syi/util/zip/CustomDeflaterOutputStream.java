package syi.util.zip;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import syi.util.ByteStream;
import syi.util.Io;

public class CustomDeflaterOutputStream
{
  private ByteStream stm = new ByteStream();
  private ByteStream work = new ByteStream();
  private byte[] buffer = new byte[32767];
  private OutputStream Out;
  private boolean isOriginal = false;
  private ZipOutputStream OutZip;
  private CRC32 crc = new CRC32();

  public CustomDeflaterOutputStream(OutputStream paramOutputStream)
  {
    this(paramOutputStream, false);
  }

  public CustomDeflaterOutputStream(OutputStream paramOutputStream, boolean paramBoolean)
  {
    this.Out = paramOutputStream;
    this.isOriginal = paramBoolean;
    if (!paramBoolean)
    {
      this.OutZip = new ZipOutputStream(this.stm);
      this.OutZip.setMethod(0);
      this.OutZip.setLevel(0);
    }
  }

  public void close()
    throws IOException
  {
    close(null);
  }

  public void close(String paramString)
    throws IOException
  {
    if (!this.isOriginal)
    {
      this.OutZip.close();
      ZipOutputStream localZipOutputStream = new ZipOutputStream(this.Out);
      localZipOutputStream.setLevel(9);
      localZipOutputStream.putNextEntry(new ZipEntry((paramString == null) || (paramString.length() == 0) ? "0.zip" : paramString));
      this.stm.writeTo(localZipOutputStream);
      localZipOutputStream.flush();
      localZipOutputStream.closeEntry();
      localZipOutputStream.close();
    }
    else
    {
      Deflater localDeflater = new Deflater(9, true);
      localDeflater.setInput(this.stm.getBuffer(), 0, this.stm.size());
      localDeflater.finish();
      while (!localDeflater.finished())
      {
        int i = localDeflater.deflate(this.buffer, 0, 1);
        if (i <= 0)
          continue;
        this.Out.write(this.buffer, 0, i);
      }
    }
    try
    {
      this.Out.flush();
      this.Out.close();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }

  public static void compress(File paramFile1, File paramFile2)
  {
    try
    {
      CustomDeflaterOutputStream localCustomDeflaterOutputStream = new CustomDeflaterOutputStream(new FileOutputStream(paramFile2), false);
      localCustomDeflaterOutputStream.write(paramFile1);
      localCustomDeflaterOutputStream.close(paramFile1.getName());
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      File localFile1 = new File(paramArrayOfString[0] + ".tmp");
      File localFile2 = new File(paramArrayOfString[0]);
      compress(localFile2, localFile1);
      localFile2.delete();
      localFile1.renameTo(localFile2);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    System.exit(0);
  }

  public void write(File paramFile)
    throws IOException
  {
    ZipInputStream localZipInputStream = new ZipInputStream(new FileInputStream(paramFile));
    ZipEntry localZipEntry1;
    int i;
    if (this.isOriginal)
      while ((localZipEntry1 = localZipInputStream.getNextEntry()) != null)
      {
        this.work.reset();
        this.work.write(localZipEntry1.getName().getBytes());
        this.work.write(0);
        while ((i = localZipInputStream.read(this.buffer)) != -1)
          this.work.write(this.buffer, 0, i);
        if (this.work.size() <= 0)
          continue;
        Io.wShort(this.stm, this.work.size() & 0xFFFF);
        this.work.writeTo(this.stm);
      }
    else
      while ((localZipEntry1 = localZipInputStream.getNextEntry()) != null)
      {
        this.work.reset();
        while ((i = localZipInputStream.read(this.buffer)) != -1)
          this.work.write(this.buffer, 0, i);
        if (this.work.size() <= 0)
          continue;
        this.crc.reset();
        this.crc.update(this.work.getBuffer(), 0, this.work.size());
        ZipEntry localZipEntry2 = new ZipEntry(localZipEntry1.getName());
        localZipEntry2.setSize(this.work.size());
        localZipEntry2.setCrc(this.crc.getValue());
        this.OutZip.putNextEntry(localZipEntry2);
        this.work.writeTo(this.OutZip);
        this.OutZip.flush();
        this.OutZip.closeEntry();
      }
    localZipInputStream.close();
  }

  public void write(ByteStream paramByteStream)
    throws IOException
  {
    ZipInputStream localZipInputStream = new ZipInputStream(new ByteArrayInputStream(paramByteStream.getBuffer(), 0, paramByteStream.size()));
    ZipEntry localZipEntry1;
    int i;
    if (this.isOriginal)
      while ((localZipEntry1 = localZipInputStream.getNextEntry()) != null)
      {
        this.work.reset();
        this.work.write(localZipEntry1.getName().getBytes());
        this.work.write(0);
        while ((i = localZipInputStream.read(this.buffer)) != -1)
          this.work.write(this.buffer, 0, i);
        if (this.work.size() <= 0)
          continue;
        Io.wShort(paramByteStream, this.work.size() & 0xFFFF);
        this.work.writeTo(paramByteStream);
      }
    else
      while ((localZipEntry1 = localZipInputStream.getNextEntry()) != null)
      {
        this.work.reset();
        while ((i = localZipInputStream.read(this.buffer)) != -1)
          this.work.write(this.buffer, 0, i);
        if (this.work.size() <= 0)
          continue;
        this.crc.reset();
        this.crc.update(this.work.getBuffer(), 0, this.work.size());
        ZipEntry localZipEntry2 = new ZipEntry(localZipEntry1.getName());
        localZipEntry2.setSize(this.work.size());
        localZipEntry2.setCrc(this.crc.getValue());
        this.OutZip.putNextEntry(localZipEntry2);
        this.work.writeTo(this.OutZip);
        this.OutZip.flush();
        this.OutZip.closeEntry();
      }
    localZipInputStream.close();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.zip.CustomDeflaterOutputStream
 * JD-Core Version:    0.6.0
 */