package paintchat_server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import paintchat.Config;
import syi.util.Io;
import syi.util.PProperties;
import syi.util.VectorBin;

public class PchOutputStreamForServer extends OutputStream
{
  private Config config;
  private PchOutputStream out = null;
  private long lTimer = 0L;
  private boolean isLogEnable = false;
  private boolean isLogLoad = false;
  private int iCashMax;
  private int iLogMax;
  private Deflater deflater;
  private byte[] bDeflate = new byte[70000];
  private VectorBin vAnime;

  public PchOutputStreamForServer(Config paramConfig)
  {
    this.config = paramConfig;
    mInit();
  }

  private void mInit()
  {
    this.iCashMax = this.config.getInt("Server_Cash_Line_Size", 500000);
    this.iLogMax = 100000000;
    this.isLogEnable = this.config.getBool("Server_Log_Line", false);
    this.isLogLoad = this.config.getBool("Server_Load_Line", false);
    boolean bool = this.config.getBool("Server_Cash_Line", true);
    if (((this.isLogLoad) || (this.isLogEnable) || (bool)) && (this.deflater == null))
      this.deflater = new Deflater(9, false);
    if (this.isLogLoad)
    {
      File localFile = getTmpFile();
      if ((localFile.isFile()) && (localFile.length() > 0L))
        try
        {
          PchInputStream localPchInputStream = new PchInputStream(new BufferedInputStream(new FileInputStream(localFile)), this.iCashMax);
          this.vAnime = localPchInputStream.getLines();
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
        }
    }
    if (this.vAnime == null)
      this.vAnime = new VectorBin();
  }

  public void mFinalize()
  {
    close();
    this.deflater.end();
  }

  public void write(int paramInt)
  {
    write(new byte[] { (byte)paramInt });
  }

  public void write(byte[] paramArrayOfByte)
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    try
    {
      if (checkTimeout())
        newLog();
      setupStream();
      this.deflater.reset();
      this.deflater.setInput(paramArrayOfByte, paramInt1, paramInt2);
      this.deflater.finish();
      int i = 0;
      while (!this.deflater.finished())
        i += this.deflater.deflate(this.bDeflate, i, paramInt2 - i);
      byte[] arrayOfByte = new byte[i];
      System.arraycopy(this.bDeflate, 0, arrayOfByte, 0, i);
      this.vAnime.add(arrayOfByte);
      if (this.vAnime.getSizeBytes() >= this.iCashMax)
        this.vAnime.remove(1);
      if (this.out != null)
      {
        this.out.write(this.bDeflate, 0, i);
        if (this.out.size() >= this.iLogMax)
        {
          close();
          setupStream();
        }
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      close();
      this.isLogEnable = false;
    }
  }

  public void flush()
  {
    try
    {
      if (this.out != null)
        this.out.flush();
    }
    catch (IOException localIOException)
    {
      close();
    }
  }

  public void close()
  {
    try
    {
      if (this.out != null)
      {
        this.out.flush();
        this.out.close();
      }
    }
    catch (IOException localIOException1)
    {
      localIOException1.printStackTrace();
    }
    this.out = null;
    if (this.isLogEnable)
      try
      {
        File localFile1 = getTmpFile();
        if ((localFile1.isFile()) && (localFile1.length() > 0L))
        {
          File localFile2 = new File(Io.getDateString("pch", "spch", Io.getDirectory(localFile1.getCanonicalPath())));
          Io.copyFile(localFile1, localFile2);
        }
      }
      catch (IOException localIOException2)
      {
        localIOException2.printStackTrace();
      }
  }

  public boolean checkTimeout()
  {
    return (this.lTimer != 0L) && (System.currentTimeMillis() - this.lTimer >= 86400000L);
  }

  public void setupStream()
    throws IOException
  {
    if ((this.out != null) || ((!this.isLogEnable) && (!this.isLogLoad)))
      return;
    File localFile = getTmpFile();
    boolean bool = (localFile.isFile()) && (localFile.length() > 0L);
    this.out = new PchOutputStream(new BufferedOutputStream(new FileOutputStream(localFile, bool)), bool);
    if (!bool)
    {
      this.out.writeHeader("Client_Image_Width", this.config.getString("Client_Image_Width"));
      this.out.writeHeader("Client_Image_Height", this.config.getString("Client_Image_Height"));
      this.out.writeHeader("version", "2");
    }
    this.lTimer = System.currentTimeMillis();
  }

  public void getLog(VectorBin paramVectorBin)
  {
    this.vAnime.copy(paramVectorBin);
  }

  public void newLog()
  {
    this.vAnime.removeAll();
    flush();
    close();
    File localFile = getTmpFile();
    if (localFile.isFile())
      localFile.delete();
  }

  private File getTmpFile()
  {
    File localFile = new File(this.config.getString("Server_Log_Line_Dir", "./save_server/"));
    if (!localFile.isDirectory())
      localFile.mkdirs();
    return new File(localFile, "line_cash.tmp");
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_server.PchOutputStreamForServer
 * JD-Core Version:    0.6.0
 */