package syi.png;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import syi.util.ByteStream;

public class SPngEncoder
{
  private final String STR_C = "(C)shi-chan 2001-2003";
  private OutputStream OUT;
  private int width;
  private int height;
  private int[] i_off;
  private CRC32 crc = new CRC32();
  private Deflater deflater;
  private byte[] b;
  private byte[] bGet = new byte[1025];
  private int seek = 0;
  private int[] iF = new int[3];
  private int[] iFNow = new int[3];
  private int[] iFLOld = new int[3];
  private int[] iFL;
  private ByteStream work;
  private int image_type = 2;
  private byte image_filter;
  private boolean isProgress = false;

  public SPngEncoder(ByteStream paramByteStream1, ByteStream paramByteStream2, Deflater paramDeflater)
  {
    System.out.println("(C)shi-chan 2001-2003");
    this.work = paramByteStream1;
    this.b = paramByteStream2.getBuffer();
    this.deflater = paramDeflater;
  }

  private void bFilter()
    throws IOException
  {
    bW(this.image_filter);
    zero(this.iF);
  }

  private void bW(byte paramByte)
    throws IOException
  {
    this.b[(this.seek++)] = paramByte;
    if (this.seek >= this.b.length)
      wCompress();
  }

  public void encode(OutputStream paramOutputStream, int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      this.OUT = paramOutputStream;
      this.width = paramInt1;
      this.height = paramInt2;
      this.i_off = paramArrayOfInt;
      this.image_filter = (byte)paramInt3;
      if (this.image_filter > 1)
      {
        if ((this.iFL == null) || (this.iFL.length < this.width))
          this.iFL = new int[this.width];
        zero(this.iFL);
        zero(this.iFLOld);
      }
      paramOutputStream.write(new byte[] { -119, 80, 78, 71, 13, 10, 26, 10 });
      mIHDR();
      mEXt("Title", "Shi-Tools Oekaki Data");
      mEXt("Copyright", "(C)shi-chan 2001-2003");
      mEXt("Software", "Shi-Tools");
      mIDAT();
      mIEND();
      paramOutputStream.flush();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void fencode(ByteStream paramByteStream, int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    paramByteStream.reset();
    int[] arrayOfInt = new int[4];
    for (int i = 0; i < 4; i++)
    {
      paramByteStream.reset();
      encode(paramByteStream, paramArrayOfInt, paramInt1, paramInt2, i);
      arrayOfInt[i] = paramByteStream.size();
    }
    for (i = 0; i < 4; i++)
    {
      for (int j = i + 1; j < 4; j++)
        if (arrayOfInt[i] > arrayOfInt[j])
          break;
      if (j >= 4)
        break;
    }
    if (i != 3)
    {
      paramByteStream.reset();
      encode(paramByteStream, paramArrayOfInt, paramInt1, paramInt2, i);
    }
  }

  private void getFPic(int paramInt1, int paramInt2)
  {
    for (int i = 0; i < 3; i++)
      this.iFNow[i] = (paramInt1 >>> (i << 3) & 0xFF);
    switch (this.image_filter)
    {
    case 0:
      return;
    case 1:
      for (i = 0; i < 3; i++)
        this.iF[i] = (this.iFNow[i] - this.iF[i]);
      break;
    case 2:
      for (i = 0; i < 3; i++)
        this.iF[i] = (this.iFNow[i] - (this.iFL[paramInt2] >>> (i << 3) & 0xFF));
      this.iFL[paramInt2] = paramInt1;
      break;
    case 3:
      for (i = 0; i < 3; i++)
        this.iF[i] = (this.iFNow[i] - (this.iF[i] + (this.iFL[paramInt2] >>> (i << 3) & 0xFF) >>> 1));
      this.iFL[paramInt2] = paramInt1;
      break;
    case 4:
    }
    int[] arrayOfInt = this.iF;
    this.iF = this.iFNow;
    this.iFNow = arrayOfInt;
  }

  private void mEXt(String paramString1, String paramString2)
    throws IOException
  {
    int i = 1950701684;
    wCh(i);
    wArray(paramString1.getBytes());
    w(0);
    wArray(paramString2.getBytes());
    wChA();
  }

  private void mIDAT()
    throws IOException
  {
    int i = 1229209940;
    wCh(i);
    wImage();
    wChA();
  }

  private void mIEND()
    throws IOException
  {
    int i = 1229278788;
    wCh(i);
    wChA();
  }

  private void mIHDR()
    throws IOException
  {
    int i = 1229472850;
    wCh(i);
    wInt(this.width);
    wInt(this.height);
    w(8);
    w(2);
    w(0);
    w(0);
    w(this.isProgress ? 1 : 0);
    wChA();
  }

  public void setInterlace(boolean paramBoolean)
  {
    this.isProgress = paramBoolean;
  }

  private void w(int paramInt)
    throws IOException
  {
    this.work.write(paramInt);
  }

  private void wArray(byte[] paramArrayOfByte)
    throws IOException
  {
    wArray(paramArrayOfByte, paramArrayOfByte.length);
  }

  private void wArray(byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    if (paramInt > 0)
      this.work.write(paramArrayOfByte, 0, paramInt);
  }

  private void wCh(int paramInt)
    throws IOException
  {
    this.work.reset();
    wInt(paramInt);
  }

  private void wChA()
    throws IOException
  {
    int i = this.work.size();
    this.crc.reset();
    this.crc.update(this.work.getBuffer(), 0, i);
    wInt((int)this.crc.getValue());
    i -= 4;
    for (int j = 24; j >= 0; j -= 8)
      this.OUT.write(i >>> j & 0xFF);
    this.work.writeTo(this.OUT);
  }

  private void wCompress()
    throws IOException
  {
    if (this.seek == 0)
      return;
    this.deflater.setInput(this.b, 0, this.seek);
    this.seek = 0;
    while (!this.deflater.needsInput())
    {
      int i = this.deflater.deflate(this.bGet, 0, this.bGet.length - 1);
      if (i == 0)
        continue;
      wArray(this.bGet, i);
    }
  }

  private void wImage()
    throws IOException
  {
    int i = 0;
    this.deflater.reset();
    this.seek = 0;
    int k;
    int j;
    int m;
    if (!this.isProgress)
    {
      for (k = 0; k < this.height; k++)
      {
        bFilter();
        for (j = 0; j < this.width; j++)
        {
          getFPic(this.i_off[(i++)], j);
          for (m = 2; m >= 0; m--)
            bW((byte)this.iFNow[m]);
        }
      }
    }
    else
    {
      int i2 = 0;
      byte[][][] arrayOfByte = { { new byte[1] }, { { 4 } }, { { 32, 36 } }, { { 2, 6 }, { 34, 38 } }, { { 16, 18, 20, 22 }, { 48, 50, 52, 54 } }, { { 1, 3, 5, 7 }, { 17, 19, 21, 23 }, { 33, 35, 37, 39 }, { 49, 51, 53, 55 } }, { { 8, 9, 10, 11, 12, 13, 14, 15 }, { 24, 25, 26, 27, 28, 29, 30, 31 }, { 40, 41, 42, 43, 44, 45, 46, 47 }, { 56, 57, 58, 59, 60, 61, 62, 63 } } };
      for (int i4 = 0; i4 < arrayOfByte.length; i4++)
      {
        zero(this.iFL);
        zero(this.iF);
        for (k = 0; k < this.height; k += 8)
          for (int i5 = 0; i5 < arrayOfByte[i4].length; i5++)
          {
            int i3 = 0;
            for (j = 0; j < this.width; j += 8)
              for (int i6 = 0; i6 < arrayOfByte[i4][i5].length; i6++)
              {
                i2 = arrayOfByte[i4][i5][i6];
                int n = j + i2 % 8;
                int i1 = k + i2 / 8;
                if ((n >= this.width) || (i1 >= this.height))
                  continue;
                if (i3 == 0)
                {
                  bFilter();
                  i3 = 1;
                }
                getFPic(this.i_off[(this.width * i1 + n)], n);
                for (m = 2; m >= 0; m--)
                  bW((byte)this.iFNow[m]);
              }
          }
      }
    }
    wCompress();
    this.deflater.finish();
    while (!this.deflater.finished())
    {
      i = this.deflater.deflate(this.bGet, 0, this.bGet.length - 1);
      wArray(this.bGet, i);
    }
  }

  private void wInt(int paramInt)
    throws IOException
  {
    for (int i = 24; i >= 0; i -= 8)
      w(paramInt >>> i & 0xFF);
  }

  private void zero(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null)
      return;
    for (int i = 0; i < paramArrayOfInt.length; i++)
      paramArrayOfInt[i] = 0;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.png.SPngEncoder
 * JD-Core Version:    0.6.0
 */