package paintchat_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import syi.util.Io;

public class PchOutputStream extends OutputStream
{
  private OutputStream out;
  private boolean isWriteHeader = false;
  public static String OPTION_MGCOUNT = "mg_count";
  public static String OPTION_VERSION = "version";
  private int write_size = 0;

  public PchOutputStream(OutputStream paramOutputStream, boolean paramBoolean)
  {
    this.out = paramOutputStream;
    this.isWriteHeader = paramBoolean;
  }

  public void close()
    throws IOException
  {
    if (!this.isWriteHeader)
      writeHeader();
    this.out.close();
  }

  public void flush()
    throws IOException
  {
    if (!this.isWriteHeader)
      writeHeader();
    this.out.flush();
  }

  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (!this.isWriteHeader)
      writeHeader();
    Io.wShort(this.out, paramInt2);
    this.out.write(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void write(int paramInt)
    throws IOException
  {
    if (!this.isWriteHeader)
      writeHeader();
    this.out.write(paramInt);
  }

  public void write(File paramFile)
    throws IOException
  {
    if (!this.isWriteHeader)
      writeHeader();
    int i = 0;
    int j = (int)paramFile.length();
    byte[] arrayOfByte = (byte[])null;
    if (j <= 2)
      return;
    FileInputStream localFileInputStream = null;
    try
    {
      localFileInputStream = new FileInputStream(paramFile);
      while (i < j)
      {
        int k = Io.readUShort(localFileInputStream);
        if ((arrayOfByte == null) || (arrayOfByte.length < k))
          arrayOfByte = new byte[k];
        Io.rFull(localFileInputStream, arrayOfByte, 0, k);
        write(arrayOfByte, 0, k);
      }
    }
    catch (IOException localIOException1)
    {
    }
    if (localFileInputStream != null)
      try
      {
        localFileInputStream.close();
      }
      catch (IOException localIOException2)
      {
      }
  }

  private void writeHeader()
    throws IOException
  {
    if (this.isWriteHeader)
      return;
    this.out.write(13);
    this.out.write(10);
    this.isWriteHeader = true;
  }

  public void writeHeader(String paramString1, String paramString2)
    throws IOException
  {
    if (this.isWriteHeader)
      return;
    this.out.write((paramString1 + "=" + paramString2 + "\r\n").getBytes("UTF8"));
  }

  public int size()
  {
    return this.write_size;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.PchOutputStream
 * JD-Core Version:    0.6.0
 */