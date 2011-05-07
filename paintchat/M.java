package paintchat;

import F;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Hashtable;
import syi.awt.Awt;
import syi.util.ByteStream;

public class M
{
  private Info info;
  private User user;
  public int iHint = 0;
  public int iPen = 0;
  public int iPenM = 0;
  public int iTT = 0;
  public int iColor = 0;
  public int iColorMask = 0;
  public int iAlpha = 255;
  public int iAlpha2;
  public int iSA = 65280;
  public int iLayer = 0;
  public int iLayerSrc = 1;
  public int iMask = 0;
  public int iSize = 0;
  public int iSS = 65280;
  public int iCount = -8;
  public int iSOB;
  public boolean isAFix;
  public boolean isOver;
  public boolean isCount = true;
  public boolean isAnti;
  public boolean isAllL;
  public byte[] strHint;
  private int iSeek;
  private int iOffset;
  private byte[] offset;
  public static final int H_FLINE = 0;
  public static final int H_LINE = 1;
  public static final int H_BEZI = 2;
  public static final int H_RECT = 3;
  public static final int H_FRECT = 4;
  public static final int H_OVAL = 5;
  public static final int H_FOVAL = 6;
  public static final int H_FILL = 7;
  public static final int H_TEXT = 8;
  public static final int H_COPY = 9;
  public static final int H_CLEAR = 10;
  public static final int H_SP = 11;
  public static final int H_VTEXT = 12;
  public static final int H_L = 14;
  public static final int P_SOLID = 0;
  public static final int P_PEN = 1;
  public static final int P_SUISAI = 2;
  public static final int P_SUISAI2 = 3;
  public static final int P_WHITE = 4;
  public static final int P_SWHITE = 5;
  public static final int P_LIGHT = 6;
  public static final int P_DARK = 7;
  public static final int P_BOKASHI = 8;
  public static final int P_MOSAIC = 9;
  public static final int P_FILL = 10;
  public static final int P_LPEN = 11;
  public static final int P_NULL = 14;
  public static final int P_LR = 17;
  public static final int P_UD = 18;
  public static final int P_R = 19;
  public static final int P_FUSION = 20;
  public static final int PM_PEN = 0;
  public static final int PM_SUISAI = 1;
  public static final int PM_MANY = 2;
  public static final int M_N = 0;
  public static final int M_M = 1;
  public static final int M_R = 2;
  public static final int M_ADD = 3;
  public static final int M_SUB = 4;
  private static final int F1O = 4;
  private static final int F1C = 8;
  private static final int F1A = 16;
  private static final int F1S = 32;
  private static final int F2H = 1;
  private static final int F2PM = 2;
  private static final int F2M = 4;
  private static final int F2P = 8;
  private static final int F2T = 16;
  private static final int F2L = 32;
  private static final int F2LS = 64;
  private static final int F3A = 1;
  private static final int F3C = 2;
  private static final int F3CM = 4;
  private static final int F3S = 8;
  private static final int F3E = 16;
  private static final int F3SA = 32;
  private static final int F3SS = 64;
  private static final int DEF_COUNT = -8;
  private static final String ENCODE = "UTF8";
  private static float[] b255 = new float[256];
  static float[] b255d = new float[256];
  private static ColorModel color_model = null;
  private static final M mgDef = new M();

  public M()
  {
  }

  public M(Info paramInfo, User paramUser)
  {
    this.info = paramInfo;
    this.user = paramUser;
  }

  private final void copy(int[][] paramArrayOfInt1, int[][] paramArrayOfInt2)
  {
    for (int i = 0; i < paramArrayOfInt2.length; i++)
      System.arraycopy(paramArrayOfInt1[i], 0, paramArrayOfInt2[i], 0, paramArrayOfInt2[i].length);
  }

  public final void dBuffer()
  {
    dBuffer(!this.user.isDirect, this.user.X, this.user.Y, this.user.X2, this.user.Y2);
  }

  private final void dBuffer(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    try
    {
      int i = this.info.scale;
      int j = this.info.Q;
      int k = this.info.W;
      int m = this.info.H;
      int n = this.info.scaleX;
      int i1 = this.info.scaleY;
      int i3 = i == 1 ? 1 : 0;
      int[] arrayOfInt = this.user.buffer;
      Color localColor = Color.white;
      Graphics localGraphics = this.info.g;
      if (localGraphics == null)
        return;
      paramInt1 /= j;
      paramInt2 /= j;
      paramInt3 /= j;
      paramInt4 /= j;
      paramInt1 = paramInt1 <= n ? n : paramInt1;
      paramInt2 = paramInt2 <= i1 ? i1 : paramInt2;
      int i2 = this.info.vWidth / i + n;
      paramInt3 = paramInt3 > i2 ? i2 : paramInt3;
      paramInt3 = paramInt3 > k ? k : paramInt3;
      i2 = this.info.vHeight / i + i1;
      paramInt4 = paramInt4 > i2 ? i2 : paramInt4;
      paramInt4 = paramInt4 > m ? m : paramInt4;
      if ((paramInt3 <= paramInt1) || (paramInt4 <= paramInt2))
        return;
      k = paramInt3 - paramInt1;
      int i4 = k * i;
      int i5 = (paramInt1 - n) * i;
      int i6 = paramInt2;
      i2 = arrayOfInt.length / (k * j * j);
      while (true)
      {
        m = Math.min(i2, paramInt4 - i6);
        if (m <= 0)
          break;
        Image localImage = paramBoolean ? mkMPic(paramInt1, i6, k, m, j) : mkLPic(null, paramInt1, i6, k, m, j);
        if (i3 != 0)
          localGraphics.drawImage(localImage, i5, i6 - i1, localColor, null);
        else
          localGraphics.drawImage(localImage, i5, (i6 - i1) * i, i4, m * i, localColor, null);
        i6 += m;
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
  }

  private final void dBz(int[] paramArrayOfInt)
    throws InterruptedException
  {
    try
    {
      int i = paramArrayOfInt[0];
      int j = 0;
      float f3;
      float f4;
      for (int k = 1; k < 4; k++)
      {
        f3 = paramArrayOfInt[k] >> 16;
        f4 = (short)paramArrayOfInt[k];
        (i >> 16);
        (short)i;
        j = (int)(j + Math.sqrt(f3 * f3 + f4 * f4));
        i = paramArrayOfInt[k];
      }
      if (j <= 0)
        return;
      k = -100;
      int m = -100;
      int n = -1000;
      int i1 = -1000;
      int i2 = 0;
      boolean bool = this.isAnti;
      int i3 = this.user.pW / 2;
      for (i = j; i > 0; i--)
      {
        float f2 = i / j;
        float f1 = (float)Math.pow(1.0F - f2, 3.0D);
        f3 = f1 * (paramArrayOfInt[3] >> 16);
        f4 = f1 * (short)paramArrayOfInt[3];
        f1 = 3.0F * (1.0F - f2) * (1.0F - f2) * f2;
        f3 += f1 * (paramArrayOfInt[2] >> 16);
        f4 += f1 * (short)paramArrayOfInt[2];
        f1 = 3.0F * f2 * f2 * (1.0F - f2);
        f3 += f1 * (paramArrayOfInt[1] >> 16);
        f4 += f1 * (short)paramArrayOfInt[1];
        f1 = f2 * f2 * f2;
        f3 += f1 * (paramArrayOfInt[0] >> 16);
        f4 += f1 * (short)paramArrayOfInt[0];
        k = (int)f3 + i3;
        m = (int)f4 + i3;
        if ((k == n) && (m == i1))
          continue;
        if (bool)
        {
          shift(k, m);
          i2++;
          if (i2 >= 4)
            dFLine2(this.iSize);
        }
        else
        {
          dFLine(k, m, this.iSize);
        }
        n = k;
        i1 = m;
      }
      this.user.X -= 1;
      this.user.Y -= 1;
      this.user.X2 += 2;
      this.user.Y2 += 2;
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
  }

  public void dClear()
  {
    if (this.iPen == 14)
      return;
    for (int i = 0; i < this.info.L; i++)
    {
      if ((i < 64) && ((this.info.unpermission & 1 << i) != 0L))
        continue;
      this.info.layers[i].clear();
    }
    this.user.isDirect = true;
    setD(0, 0, this.info.W, this.info.H);
    if (this.user.wait >= 0)
      dBuffer();
  }

  private void dFusion(byte[] paramArrayOfByte)
  {
    LO[] arrayOfLO = this.info.layers;
    LO localLO2 = new LO();
    LO localLO3 = new LO();
    int i = this.info.W;
    int j = paramArrayOfByte.length / 4;
    int[] arrayOfInt = this.user.buffer;
    int k = arrayOfInt.length / i;
    int n = 0;
    LO localLO1;
    while (n < this.info.H)
    {
      int m = Math.min(this.info.H - n, k);
      (i * m);
      i1 = 0;
      Object localObject = null;
      for (int i2 = 0; i2 < j; i2++)
      {
        localLO1 = arrayOfLO[paramArrayOfByte[(i1++)]];
        localLO2.setField(localLO1);
        localLO1.iAlpha = b255[(paramArrayOfByte[(i1++)] & 0xFF)];
        localLO1.iCopy = paramArrayOfByte[(i1++)];
        i1++;
        localLO1.normalize(localLO1.iAlpha, 0, n, i, n + m);
        if (localObject == null)
        {
          localObject = localLO1;
          localLO3.setField(localLO2);
          localLO1.reserve();
        }
        else
        {
          if (localLO1.iCopy == 1)
          {
            memset(arrayOfInt, 16777215);
            for (int i3 = 0; i3 < i2 - 2; i3++)
              arrayOfLO[i3].draw(arrayOfInt, 0, n, i, n + m, i);
          }
          localLO1.dAdd(localObject.offset, 0, n, i, n + m, arrayOfInt);
          localLO1.clear(0, n, i, n + m);
          localLO1.setField(localLO2);
        }
      }
      if (localObject != arrayOfLO[this.iLayer])
      {
        localObject.copyTo(0, n, i, n + m, arrayOfLO[this.iLayer], 0, n, null);
        localObject.clear(0, n, i, n + m);
      }
      n += k;
    }
    localLO2.iAlpha = 1.0F;
    localLO2.iCopy = 0;
    localLO2.isDraw = true;
    for (int i1 = 0; i1 < j; i1++)
    {
      localLO1 = arrayOfLO[paramArrayOfByte[(i1 * 4)]];
      localLO2.name = localLO1.name;
      localLO1.setField(localLO2);
    }
  }

  private void dCopy(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt[0];
    int j = i >> 16;
    int k = (short)i;
    i = paramArrayOfInt[1];
    int m = i >> 16;
    int n = (short)i;
    i = paramArrayOfInt[2];
    int i1 = i >> 16;
    int i2 = (short)i;
    this.info.layers[this.iLayerSrc].copyTo(j, k, m, n, this.info.layers[this.iLayer], i1, i2, this.user.buffer);
    setD(i1, i2, i1 + (m - j), i2 + (n - k));
  }

  public final void dEnd()
    throws InterruptedException
  {
    if (!this.user.isDirect)
      dFlush();
    ByteStream localByteStream = this.info.workOut;
    if (localByteStream.size() > 0)
    {
      this.offset = localByteStream.writeTo(this.offset, 0);
      this.iOffset = localByteStream.size();
    }
    if (this.user.wait == -1)
      dBuffer();
  }

  private void dFill(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = (byte)this.iAlpha;
    int j = this.info.W;
    try
    {
      int i2 = paramInt3 - paramInt1;
      label170: 
      while (paramInt2 < paramInt4)
      {
        int k;
        int m = k = paramInt2 * j + paramInt1;
        for (int i1 = 0; i1 < i2; i1++)
        {
          if (paramArrayOfByte[k] == i)
            break;
          k++;
        }
        while (i1 < i2)
        {
          if (paramArrayOfByte[k] != i)
            break;
          k++;
          i1++;
        }
        m = k;
        if (i1 >= i2)
          break label170;
        while (i1 < i2)
        {
          if (paramArrayOfByte[k] == i)
            break;
          k++;
          i1++;
        }
        int n = k;
        if (i1 >= i2)
          break label170;
        while (m < n)
        {
          paramArrayOfByte[m] = i;
          m++;
        }
        paramInt2++;
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      System.out.println(localRuntimeException);
    }
  }

  private void dFill(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = this.iAlpha;
    int j = this.info.W;
    try
    {
      int i1 = paramInt3 - paramInt1;
      while (paramInt2 < paramInt4)
      {
        int k = paramInt2 * j + paramInt1;
        int i2 = k + i1;
        while (k < i2)
        {
          if (paramArrayOfInt[k] == i)
            break;
          k++;
        }
        if (k < i2 - 1)
        {
          k++;
          while (k < i2)
          {
            if (paramArrayOfInt[k] != i)
              break;
            k++;
          }
          if (k < i2 - 1)
          {
            int m = k;
            k++;
            while (k < i2)
            {
              if (paramArrayOfInt[k] == i)
                break;
              k++;
            }
            if (k < i2)
            {
              int n = k;
              while (m < n)
              {
                paramArrayOfInt[m] = i;
                m++;
              }
            }
          }
        }
        paramInt2++;
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      System.out.println(localRuntimeException);
    }
  }

  private void dFill(int paramInt1, int paramInt2)
  {
    int i = this.info.W;
    int j = this.info.H;
    int k = (byte)this.iAlpha;
    byte[] arrayOfByte = this.info.iMOffs;
    try
    {
      int[] arrayOfInt = this.user.buffer;
      int m = 0;
      if ((paramInt1 < 0) || (paramInt1 >= i) || (paramInt2 < 0) || (paramInt2 >= j))
        return;
      int i4 = pix(paramInt1, paramInt2);
      int i5 = this.iAlpha << 24 | this.iColor;
      if (i4 == i5)
        return;
      arrayOfInt[(m++)] = (s(i4, paramInt1, paramInt2) << 16 | paramInt2);
      while (m > 0)
      {
        m--;
        int n = arrayOfInt[m];
        paramInt1 = n >>> 16;
        paramInt2 = n & 0xFFFF;
        int i1 = i * paramInt2;
        int i2 = 0;
        int i3 = 0;
        while (true)
        {
          arrayOfByte[(i1 + paramInt1)] = k;
          if ((paramInt2 > 0) && (pix(paramInt1, paramInt2 - 1) == i4) && (arrayOfByte[(i1 - i + paramInt1)] == 0))
          {
            if (i2 == 0)
            {
              i2 = 1;
              arrayOfInt[(m++)] = (s(i4, paramInt1, paramInt2 - 1) << 16 | paramInt2 - 1);
            }
          }
          else
            i2 = 0;
          if ((paramInt2 < j - 1) && (pix(paramInt1, paramInt2 + 1) == i4) && (arrayOfByte[(i1 + i + paramInt1)] == 0))
          {
            if (i3 == 0)
            {
              i3 = 1;
              arrayOfInt[(m++)] = (s(i4, paramInt1, paramInt2 + 1) << 16 | paramInt2 + 1);
            }
          }
          else
            i3 = 0;
          if ((paramInt1 <= 0) || (pix(paramInt1 - 1, paramInt2) != i4) || (arrayOfByte[(i1 + paramInt1 - 1)] != 0))
            break;
          paramInt1--;
        }
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      System.out.println(localRuntimeException);
    }
    setD(0, 0, i, j);
    t();
  }

  private final void dFLine(float paramFloat1, float paramFloat2, int paramInt)
    throws InterruptedException
  {
    int i = this.user.wait;
    float f1 = this.user.fX;
    float f2 = this.user.fY;
    float f3 = paramFloat1 - f1;
    float f4 = paramFloat2 - f2;
    float f5 = Math.max(Math.abs(f3), Math.abs(f4));
    int j = (int)f1;
    int k = (int)f2;
    int m = this.user.oX;
    int n = this.user.oY;
    float f8 = 0.25F;
    if (!this.isCount)
      this.user.count = 0;
    int i3 = ss(paramInt);
    int i4 = sa(paramInt);
    int i5 = Math.max(i3, this.iSize);
    float f9 = this.iSize;
    float f10 = this.iAlpha;
    float f11 = f5 == 0.0F ? 0.0F : (i3 - f9) / f5;
    f11 = f11 <= -1.0F ? -1.0F : f11 >= 1.0F ? 1.0F : f11;
    float f12 = f5 == 0.0F ? 0.0F : (i4 - f10) / f5;
    f12 = f12 <= -1.0F ? -1.0F : f12 >= 1.0F ? 1.0F : f12;
    float f13 = f3 == 0.0F ? 0.0F : f3 / f5;
    float f14 = f4 == 0.0F ? 0.0F : f4 / f5;
    float f15 = f1;
    float f16 = f2;
    if (f5 <= 0.0F)
      f5 += 1.0F;
    f13 *= f8;
    f14 *= f8;
    f11 *= f8;
    f12 *= f8;
    int i6 = (int)(f5 / f8);
    for (int i7 = 0; i7 < i6; i7++)
    {
      if ((m != j) || (n != k))
      {
        this.user.count -= 1;
        m = j;
        n = k;
      }
      if (this.user.count <= 0)
      {
        this.user.count = this.user.countMax;
        this.iSize = (int)f9;
        this.iAlpha = (int)f10;
        getPM();
        int i1 = j - (this.user.pW >>> 1);
        int i2 = k - (this.user.pW >>> 1);
        float f6 = f15 - (int)f15;
        float f7 = f16 - (int)f16;
        if (f6 < 0.0F)
        {
          i1--;
          f6 += 1.0F;
        }
        if (f7 < 0.0F)
        {
          i2--;
          f7 += 1.0F;
        }
        if ((f6 != 1.0F) && (f7 != 1.0F))
          dPen(i1, i2, (1.0F - f6) * (1.0F - f7));
        if (f6 != 0.0F)
          dPen(i1 + 1, i2, f6 * (1.0F - f7));
        if (f7 != 0.0F)
          dPen(i1, i2 + 1, (1.0F - f6) * f7);
        if ((f6 != 0.0F) && (f7 != 0.0F))
          dPen(i1 + 1, i2 + 1, f6 * f7);
        if (i > 0)
        {
          dBuffer(!this.user.isDirect, i1, i2, i1 + this.user.pW, i2 + this.user.pW);
          if (i > 1)
          {
            Thread.currentThread();
            Thread.sleep(i);
          }
        }
      }
      j = (int)(f15 += f13);
      k = (int)(f16 += f14);
      f9 += f11;
      f10 += f12;
    }
    this.user.fX = f15;
    this.user.fY = f16;
    this.user.oX = m;
    this.user.oY = n;
    i7 = (int)Math.sqrt(this.info.bPen[this.iPenM][i5].length) / 2;
    int i8 = (int)Math.min(f1, paramFloat1) - i7;
    int i9 = (int)Math.min(f2, paramFloat2) - i7;
    int i10 = (int)Math.max(f1, paramFloat1) + i7 + this.info.Q + 1;
    int i11 = (int)Math.max(f2, paramFloat2) + i7 + this.info.Q + 1;
    if (i == 0)
      dBuffer(!this.user.isDirect, i8, i9, i10, i11);
    addD(i8, i9, i10, i11);
  }

  private final void dFLine(int paramInt1, int paramInt2, int paramInt3)
    throws InterruptedException
  {
    int i = this.user.wait;
    int j = (int)this.user.fX;
    int k = (int)this.user.fY;
    int m = paramInt1 - j;
    int n = paramInt2 - k;
    int i1 = Math.max(Math.abs(m), Math.abs(n));
    int i2 = j;
    int i3 = k;
    int i4 = this.user.oX;
    int i5 = this.user.oY;
    if (!this.isCount)
      this.user.count = 0;
    int i8 = ss(paramInt3);
    int i9 = sa(paramInt3);
    int i10 = Math.max(i8, this.iSize);
    float f1 = this.iSize;
    float f2 = this.iAlpha;
    float f3 = i1 == 0 ? 0.0F : (i8 - f1) / i1;
    f3 = f3 <= -1.0F ? -1.0F : f3 >= 1.0F ? 1.0F : f3;
    float f4 = i1 == 0 ? 0.0F : (i9 - f2) / i1;
    f4 = f4 <= -10.0F ? -10.0F : f4 >= 5.0F ? 5.0F : f4;
    float f5 = m == 0 ? 0.0F : m / i1;
    float f6 = n == 0 ? 0.0F : n / i1;
    float f7 = j;
    float f8 = k;
    if (i1 <= 0)
      i1++;
    for (int i11 = 0; i11 < i1; i11++)
    {
      if ((i4 != i2) || (i5 != i3))
      {
        this.user.count -= 1;
        i4 = i2;
        i5 = i3;
        if (this.user.count <= 0)
        {
          this.user.count = this.user.countMax;
          this.iSize = (int)f1;
          this.iAlpha = (int)f2;
          getPM();
          int i6 = i2 - (this.user.pW >>> 1);
          int i7 = i3 - (this.user.pW >>> 1);
          dPen(i6, i7, 1.0F);
          if (i > 0)
          {
            dBuffer(!this.user.isDirect, i6, i7, i6 + this.user.pW, i7 + this.user.pW);
            if (i > 1)
            {
              Thread.currentThread();
              Thread.sleep(i);
            }
          }
        }
      }
      i2 = (int)(f7 += f5);
      i3 = (int)(f8 += f6);
      f1 += f3;
      f2 += f4;
    }
    this.user.fX = (f7 - f5);
    this.user.fY = (f8 - f6);
    this.user.oX = i4;
    this.user.oY = i5;
    i11 = (int)Math.sqrt(this.info.bPen[this.iPenM][i10].length) / 2;
    int i12 = Math.min(j, i2) - i11;
    int i13 = Math.min(k, i3) - i11;
    int i14 = Math.max(j, i2) + i11 + this.info.Q;
    int i15 = Math.max(k, i3) + i11 + this.info.Q;
    if (i == 0)
      dBuffer(!this.user.isDirect, i12, i13, i14, i15);
    addD(i12, i13, i14, i15);
  }

  private final void dFLine2(int paramInt)
    throws InterruptedException
  {
    try
    {
      int i = this.user.pX[0];
      int j = this.user.pY[0];
      int k = this.user.pX[1];
      int m = this.user.pY[1];
      int n = this.user.pX[2];
      int i1 = this.user.pY[2];
      int i2 = this.user.pX[3];
      int i3 = this.user.pY[3];
      boolean bool = this.isAnti;
      float f1 = this.user.fX;
      float f2 = this.user.fY;
      int i7 = (int)f1;
      int i8 = (int)f2;
      int i9 = i7;
      int i10 = i8;
      int i11 = this.user.oX;
      int i12 = this.user.oY;
      int i13 = this.user.wait;
      if (!this.isCount)
        this.user.count = 0;
      int i16 = 2 * k;
      int i17 = 2 * m;
      int i18 = 2 * i - 5 * k + 4 * n - i2;
      int i19 = 2 * j - 5 * m + 4 * i1 - i3;
      int i20 = -i + 3 * k - 3 * n + i2;
      int i21 = -j + 3 * m - 3 * i1 + i3;
      float f12 = this.iSize;
      float f13 = this.iAlpha;
      int i22 = ss(paramInt);
      int i23 = sa(paramInt);
      float f14 = (i22 - this.iSize) * 0.25F;
      f14 = f14 >= 1.5F ? 1.5F : f14 <= -1.5F ? -1.5F : f14;
      float f15 = (i23 - this.iAlpha) * 0.25F;
      int i24 = (int)Math.sqrt(Math.max(this.info.getPenMask()[this.iPenM][this.iSize].length, this.info.getPenMask()[this.iPenM][i22].length));
      int i25 = this.info.Q;
      float f18 = 0.0F;
      while (f18 < 1.0F)
      {
        float f8 = f18 * f18;
        float f9 = f8 * f18;
        float f3 = 0.5F * (i16 + (-i + n) * f18 + i18 * f8 + i20 * f9);
        float f4 = 0.5F * (i17 + (-j + i1) * f18 + i19 * f8 + i21 * f9);
        float f5 = Math.max(Math.abs(f3 - f1), Math.abs(f4 - f2));
        if (f5 >= 1.0F)
        {
          float f6 = (f3 - f1) / f5 * 0.25F;
          f6 = f6 >= 1.0F ? 1.0F : f6 <= -1.0F ? -1.0F : f6;
          float f7 = (f4 - f2) / f5 * 0.25F;
          f7 = f7 >= 1.0F ? 1.0F : f7 <= -1.0F ? -1.0F : f7;
          int i5 = (int)(f5 / 0.25F);
          if (i5 < 16)
            i5 = 1;
          float f16 = f14 / i5;
          float f17 = f15 / i5;
          i7 = Math.min(Math.min((int)f1, (int)f3), i7);
          i8 = Math.min(Math.min((int)f2, (int)f4), i8);
          i9 = Math.max(Math.max((int)f1, (int)f3), i9);
          i10 = Math.max(Math.max((int)f2, (int)f4), i10);
          for (int i4 = 0; i4 < i5; i4++)
          {
            int i14 = (int)f1;
            int i15 = (int)f2;
            if ((i11 != i14) || (i12 != i15))
            {
              i11 = i14;
              i12 = i15;
              this.user.count -= 1;
            }
            else
            {
              f12 += f16;
              f13 += f17;
            }
            if (this.user.count > 0)
            {
              f1 += f6;
              f2 += f7;
            }
            else
            {
              this.iSize = (int)f12;
              this.iAlpha = (int)f13;
              getPM();
              i6 = this.user.pW / 2;
              i14 -= i6;
              i15 -= i6;
              this.user.count = this.user.countMax;
              if (bool)
              {
                float f10 = f1 - (int)f1;
                float f11 = f2 - (int)f2;
                if (f10 < 0.0F)
                {
                  i14--;
                  f10 += 1.0F;
                }
                if (f11 < 0.0F)
                {
                  i15--;
                  f11 += 1.0F;
                }
                if ((f10 != 1.0F) && (f11 != 1.0F))
                  dPen(i14, i15, (1.0F - f10) * (1.0F - f11));
                if (f10 != 0.0F)
                  dPen(i14 + 1, i15, f10 * (1.0F - f11));
                if (f11 != 0.0F)
                  dPen(i14, i15 + 1, (1.0F - f10) * f11);
                if ((f10 != 0.0F) && (f11 != 0.0F))
                  dPen(i14 + 1, i15 + 1, f10 * f11);
              }
              else
              {
                dPen(i14, i15, 1.0F);
              }
              if (i13 > 0)
              {
                dBuffer(!this.user.isDirect, i14, i15, i14 + i6 * 2, i15 + i6 * 2);
                if (i13 > 1)
                {
                  Thread.currentThread();
                  Thread.sleep(i13);
                }
              }
              f1 += f6;
              f2 += f7;
            }
          }
        }
        f18 += 0.25F;
      }
      this.user.oX = i11;
      this.user.oY = i12;
      this.user.fX = f1;
      this.user.fY = f2;
      int i6 = i24 / 2;
      i7 -= i6;
      i8 -= i6;
      i9 += i6 + 1;
      i10 += i6 + 1;
      addD(i7, i8, i9, i10);
      if (this.user.wait == 0)
        dBuffer(!this.user.isDirect, i7, i8, i9 + i25, i10 + i25);
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
  }

  private final void dFlush()
  {
    if (this.user.isPre)
      return;
    int k = this.info.W;
    LO localLO2 = this.info.H;
    LO localLO3 = this.user.X <= 0 ? 0 : this.user.X;
    LO localLO4 = this.user.Y <= 0 ? 0 : this.user.Y;
    int m = this.user.X2 >= k ? k : this.user.X2;
    int n = this.user.Y2 >= localLO2 ? localLO2 : this.user.Y2;
    if ((m - localLO3 <= 0) || (n - localLO4 <= 0) || (this.iLayer >= this.info.L))
      return;
    byte[] arrayOfByte = this.info.iMOffs;
    LO localLO5 = this.info.layers[this.iLayer];
    LO localLO8;
    int[] arrayOfInt1;
    int i;
    int j;
    switch (this.iPen)
    {
    case 17:
      localLO5.dLR(localLO3, localLO4, m, n);
      dCMask(localLO3, localLO4, m, n);
      break;
    case 18:
      localLO5.dUD(localLO3, localLO4, m, n);
      dCMask(localLO3, localLO4, m, n);
      break;
    case 19:
      localLO5.dR(localLO3, localLO4, m, n, null);
      dCMask(localLO3, localLO4, m, n);
      addD(localLO3, localLO4, localLO3 + Math.max(m - localLO3, n - localLO4), localLO4 + Math.max(m - localLO3, n - localLO4));
      break;
    case 20:
      int i1 = this.iOffset > 8 ? this.offset[8] : 0;
      LO localLO6 = this.info.layers[this.iLayerSrc];
      localLO8 = localLO5;
      localLO6.normalize(b255[(this.iAlpha2 & 0xFF)], localLO3, localLO4, m, n);
      localLO8.normalize(b255[(this.iAlpha2 >>> 8)], localLO3, localLO4, m, n);
      if (localLO6.offset == null)
      {
        dCMask(localLO3, localLO4, m, n);
      }
      else
      {
        localLO8.reserve();
        LO localLO9 = localLO8;
        LO localLO10 = localLO6;
        if (this.iLayer < this.iLayerSrc)
        {
          localLO9 = localLO6;
          localLO10 = localLO8;
        }
        LO localLO12 = new LO();
        LO localLO13 = new LO();
        localLO12.setField(localLO9);
        localLO13.setField(localLO10);
        localLO9.iCopy = i1;
        localLO10.reserve();
        localLO9.dAdd(localLO10.offset, localLO3, localLO4, m, n, null);
        if (localLO8 != localLO10)
          localLO10.copyTo(localLO3, localLO4, m, n, localLO9, localLO3, localLO4, null);
        localLO6.clear(localLO3, localLO4, m, n);
        localLO6.isDraw = true;
        dCMask(localLO3, localLO4, m, n);
        localLO9.setField(localLO12);
        localLO10.setField(localLO13);
      }
      break;
    case 9:
      localLO5.reserve();
      arrayOfInt1 = localLO5.offset;
      LO localLO7 = this.iAlpha / 10 + 1;
      localLO3 = localLO3 / localLO7 * localLO7;
      localLO4 = localLO4 / localLO7 * localLO7;
      int[] arrayOfInt2 = this.user.argb;
      localLO8 = localLO4;
      int i2;
      while (i2 < n)
      {
        LO localLO1 = localLO3;
        while (j < m)
        {
          int i5 = Math.min(localLO7, k - localLO1);
          LO localLO14 = Math.min(localLO7, localLO2 - localLO8);
          for (int i6 = 0; i6 < 4; i6++)
            arrayOfInt2[i6] = 0;
          int i7 = 0;
          int i3;
          for (LO localLO11 = 0; localLO11 < localLO14; localLO11++)
            for (i3 = 0; i3 < i5; i3++)
            {
              i8 = pix(localLO1 + i3, localLO8 + localLO11);
              i = (localLO8 + localLO11) * k + localLO1 + i3;
              for (i6 = 0; i6 < 4; i6++)
                arrayOfInt2[i6] += (i8 >>> i6 * 8 & 0xFF);
              i7++;
            }
          int i8 = arrayOfInt2[3] << 24 | arrayOfInt2[2] / i7 << 16 | arrayOfInt2[1] / i7 << 8 | arrayOfInt2[0] / i7;
          for (int i4 = localLO8; i4 < localLO8 + localLO14; i4++)
          {
            i = k * i4 + localLO1;
            for (i3 = 0; i3 < i5; i3++)
            {
              if (arrayOfByte[i] != 0)
              {
                arrayOfByte[i] = 0;
                arrayOfInt1[i] = i8;
              }
              i++;
            }
          }
          localLO1 += localLO7;
        }
        localLO8 += localLO7;
      }
      break;
    case 3:
      dCMask(localLO3, localLO4, m, n);
      break;
    default:
      if ((this.iHint == 14) || (this.iHint == 9))
      {
        dCMask(localLO3, localLO4, m, n);
      }
      else
      {
        localLO5.reserve();
        arrayOfInt1 = localLO5.offset;
        while (localLO4 < n)
        {
          i = localLO4 * k + localLO3;
          for (j = localLO3; j < m; j++)
          {
            arrayOfInt1[i] = getM(arrayOfInt1[i], arrayOfByte[i] & 0xFF, i);
            arrayOfByte[i] = 0;
            i++;
          }
          localLO4++;
        }
      }
    }
    if (this.user.wait >= 0)
      dBuffer();
  }

  private final void dCMask(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt3 - paramInt1;
    int j = this.info.W;
    int k = paramInt2 * j + paramInt1;
    byte[] arrayOfByte = this.info.iMOffs;
    for (int m = 0; m < i; m++)
      arrayOfByte[(k + m)] = 0;
    paramInt2++;
    m = k;
    k += j;
    while (paramInt2 < paramInt4)
    {
      System.arraycopy(arrayOfByte, m, arrayOfByte, k, i);
      k += j;
      paramInt2++;
    }
  }

  private final boolean dNext()
    throws InterruptedException
  {
    if (this.iSeek >= this.iOffset)
      return false;
    int i = this.user.pX[3] + rPo();
    int j = this.user.pY[3] + rPo();
    int k = this.iSOB != 0 ? ru() : 0;
    shift(i, j);
    this.user.iDCount += 1;
    if (this.iHint != 11)
    {
      if (this.isAnti)
        dFLine(i, j, k);
      else
        dFLine(i, j, k);
    }
    else if (this.user.iDCount >= 2)
      dFLine2(k);
    return true;
  }

  public final void dNext(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws InterruptedException, IOException
  {
    int i = this.info.scale;
    paramInt1 = (paramInt1 / i + this.info.scaleX) * this.info.Q;
    paramInt2 = (paramInt2 / i + this.info.scaleY) * this.info.Q;
    if (Math.abs(paramInt1 - this.user.pX[3]) + Math.abs(paramInt2 - this.user.pY[3]) < paramInt4)
      return;
    wPo(paramInt1 - this.user.pX[3]);
    wPo(paramInt2 - this.user.pY[3]);
    shift(paramInt1, paramInt2);
    this.user.iDCount += 1;
    if (this.iSOB != 0)
      this.info.workOut.write(paramInt3);
    if (this.iHint == 11)
    {
      if (this.user.iDCount >= 2)
        dFLine2(paramInt3);
    }
    else if (this.isAnti)
      dFLine(paramInt1, paramInt2, paramInt3);
    else
      dFLine(paramInt1, paramInt2, paramInt3);
  }

  private final void dPen(int paramInt1, int paramInt2, float paramFloat)
  {
    if (this.iPen == 3)
    {
      if (!this.user.isPre)
        dPY(paramInt1, paramInt2);
      return;
    }
    dPenM(paramInt1, paramInt2, paramFloat);
    if (this.isOver)
      dFlush();
  }

  private final void dPenM(int paramInt1, int paramInt2, float paramFloat)
  {
    int m = 0;
    int[] arrayOfInt1 = getPM();
    int i2 = this.info.W;
    int i3 = this.user.pW;
    int i4 = i3 * Math.max(-paramInt2, 0) + Math.max(-paramInt1, 0);
    int i5 = Math.min(paramInt1 + i3, i2);
    int i6 = Math.min(paramInt2 + i3, this.info.H);
    if ((i5 <= 0) || (i6 <= 0))
      return;
    paramInt1 = paramInt1 <= 0 ? 0 : paramInt1;
    paramInt2 = paramInt2 <= 0 ? 0 : paramInt2;
    int[] arrayOfInt2 = this.info.layers[this.iLayer].offset;
    byte[] arrayOfByte = this.info.iMOffs;
    for (int j = paramInt2; j < i6; j++)
    {
      int k = i2 * j + paramInt1;
      m = i4;
      i4 += i3;
      for (int i = paramInt1; i < i5; i++)
        if (isM(arrayOfInt2[k]))
        {
          k++;
          m++;
        }
        else
        {
          int n = arrayOfByte[k] & 0xFF;
          int i1 = arrayOfInt1[(m++)];
          if (i1 == 0)
            k++;
          else
            switch (this.iPen)
            {
            case 1:
            case 20:
              i1 = Math.max((int)(i1 * b255[(255 - n >>> 1)] * paramFloat), 1);
              arrayOfByte[(k++)] = (byte)Math.min(n + i1, 255);
              break;
            case 2:
            case 5:
            case 6:
            case 7:
              if ((i1 = (int)(i1 * getTT(i, j))) != 0)
                arrayOfByte[k] = (byte)Math.min(n + Math.max((int)(i1 * b255[(255 - n >>> 2)]), 1), 255);
              k++;
              break;
            default:
              arrayOfByte[(k++)] = (byte)Math.max((int)(i1 * getTT(i, j)), n);
            }
        }
    }
  }

  private final void dPY(int paramInt1, int paramInt2)
  {
    this.info.layers[this.iLayer].reserve();
    int j = 0;
    int[] arrayOfInt1 = getPM();
    int m = this.info.W;
    int n = this.user.pW;
    int i1 = n * Math.max(-paramInt2, 0) + Math.max(-paramInt1, 0);
    int i2 = i1;
    int i3 = Math.min(paramInt1 + n, m);
    int i4 = Math.min(paramInt2 + n, this.info.H);
    paramInt1 = paramInt1 <= 0 ? 0 : paramInt1;
    paramInt2 = paramInt2 <= 0 ? 0 : paramInt2;
    if ((i3 - paramInt1 <= 0) || (i4 - paramInt2 <= 0))
      return;
    int[] arrayOfInt2 = this.info.layers[this.iLayer].offset;
    int i5 = 0;
    int i10 = 0;
    int i11 = 0;
    int i12 = 0;
    int i13 = 0;
    int i;
    int i17;
    int k;
    int i14;
    for (int i15 = paramInt2; i15 < i4; i15++)
    {
      i = m * i15 + paramInt1;
      j = i2;
      i2 += n;
      for (i17 = paramInt1; i17 < i3; i17++)
        if (((k = arrayOfInt1[(j++)]) == 0) || (isM(i14 = arrayOfInt2[(i++)])))
        {
          i++;
        }
        else
        {
          i10 += (i14 >>> 24);
          i11 += (i14 >>> 16 & 0xFF);
          i12 += (i14 >>> 8 & 0xFF);
          i13 += (i14 & 0xFF);
          i5++;
        }
    }
    if (i5 == 0)
      return;
    i10 /= i5;
    i11 /= i5;
    i12 /= i5;
    i13 /= i5;
    if (this.iAlpha > 0)
    {
      float f2 = b255[this.iAlpha] / 3.0F;
      i17 = this.iColor >>> 16 & 0xFF;
      int i18 = this.iColor >>> 8 & 0xFF;
      int i19 = this.iColor & 0xFF;
      i10 = Math.max((int)(i10 + (255 - i10) * f2), 1);
      i = (int)((i17 - i11) * f2);
      i11 += (i17 < i11 ? -1 : i17 > i11 ? 1 : i != 0 ? i : 0);
      i = (int)((i18 - i12) * f2);
      i12 += (i18 < i12 ? -1 : i18 > i12 ? 1 : i != 0 ? i : 0);
      i = (int)((i19 - i13) * f2);
      i13 += (i19 < i13 ? -1 : i19 > i13 ? 1 : i != 0 ? i : 0);
    }
    i2 = i1;
    for (int i16 = paramInt2; i16 < i4; i16++)
    {
      i = m * i16 + paramInt1;
      j = i2;
      i2 += n;
      for (i17 = paramInt1; i17 < i3; i17++)
      {
        k = arrayOfInt1[(j++)];
        i14 = arrayOfInt2[i];
        float f1;
        if ((k == 0) || (isM(i14)) || ((f1 = getTT(i17, i16) * b255[k]) == 0.0F))
        {
          i++;
        }
        else
        {
          int i6 = i14 >>> 24;
          int i7 = i14 >>> 16 & 0xFF;
          int i9 = i14 >>> 8 & 0xFF;
          int i8 = i14 & 0xFF;
          i5 = (int)((i10 - i6) * f1);
          i6 += (i10 < i6 ? -1 : i10 > i6 ? 1 : i5 != 0 ? i5 : 0);
          i5 = (int)((i11 - i7) * f1);
          i7 += (i11 < i7 ? -1 : i11 > i7 ? 1 : i5 != 0 ? i5 : 0);
          i5 = (int)((i12 - i9) * f1);
          i9 += (i12 < i9 ? -1 : i12 > i9 ? 1 : i5 != 0 ? i5 : 0);
          i5 = (int)((i13 - i8) * f1);
          i8 += (i13 < i8 ? -1 : i13 > i8 ? 1 : i5 != 0 ? i5 : 0);
          arrayOfInt2[(i++)] = ((i6 << 24) + (i7 << 16) + (i9 << 8) + i8);
        }
      }
    }
  }

  public final void draw()
    throws InterruptedException
  {
    try
    {
      if (this.info == null)
        return;
      this.iSeek = 0;
      switch (this.iHint)
      {
      case 0:
      case 1:
      case 11:
        dStart();
        while (dNext());
        break;
      case 10:
        dClear();
        break;
      default:
        dRetouch();
      }
    }
    catch (InterruptedException localInterruptedException)
    {
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    dEnd();
  }

  private void dRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = this.info.W;
    int j = this.info.H;
    byte[] arrayOfByte = this.info.iMOffs;
    int i1 = (byte)this.iAlpha;
    if (paramInt1 < 0)
      paramInt1 = 0;
    if (paramInt2 < 0)
      paramInt2 = 0;
    if (paramInt3 > i)
      paramInt3 = i;
    if (paramInt4 > j)
      paramInt4 = j;
    if ((paramInt1 >= paramInt3) || (paramInt2 >= paramInt4) || (i1 == 0))
      return;
    setD(paramInt1, paramInt2, paramInt3, paramInt4);
    this.info.layers[this.iLayer].reserve();
    int[] arrayOfInt = this.info.layers[this.iLayer].offset;
    int i2;
    int k;
    int n;
    int i3;
    int i4;
    int i5;
    int i6;
    switch (this.iHint)
    {
    case 3:
      for (i2 = paramInt2; i2 < paramInt4; i2++)
      {
        k = i2 * i + paramInt1;
        for (n = paramInt1; n < paramInt3; n++)
        {
          if (!isM(arrayOfInt[k]))
            arrayOfByte[k] = i1;
          k++;
        }
      }
      break;
    case 4:
      i2 = paramInt1;
      i3 = paramInt2;
      i4 = paramInt3;
      i5 = paramInt4;
      i6 = 0;
      break;
    case 5:
    case 6:
      while (true)
      {
        k = i * i3 + i2;
        int m = i * (i5 - 1) + i2;
        for (n = i2; n < i4; n++)
        {
          if (!isM(arrayOfInt[k]))
            arrayOfByte[k] = i1;
          if (!isM(arrayOfInt[m]))
            arrayOfByte[m] = i1;
          k++;
          m++;
        }
        k = i * i3 + i2;
        m = i * i3 + i4 - 1;
        for (int i7 = i3; i7 < i5; i7++)
        {
          if (!isM(arrayOfInt[k]))
            arrayOfByte[k] = i1;
          if (!isM(arrayOfInt[m]))
            arrayOfByte[m] = i1;
          k += i;
          m += i;
        }
        i2++;
        i4--;
        i3++;
        i5--;
        if ((i4 <= i2) || (i5 <= i3))
          break;
        i6++;
        if (i6 < this.iSize + 1)
          continue;
        break;
        i2 = paramInt3 - paramInt1 - 1;
        i3 = paramInt4 - paramInt2 - 1;
        i6 = i2 / 2;
        i7 = i3 / 2;
        int i8 = Math.min(Math.min(this.iSize + 1, i6), i7);
        for (int i9 = 0; i9 < i8; i9++)
        {
          float f = 0.0F;
          while (f < 7.0F)
          {
            i4 = paramInt1 + i6 + (int)Math.round(Math.cos(f) * (i6 - i9));
            i5 = paramInt2 + i7 + (int)Math.round(Math.sin(f) * (i7 - i9));
            arrayOfByte[(i * i5 + i4)] = i1;
            f = (float)(f + 0.001D);
          }
        }
        if ((this.iHint == 5) && (i6 > 0) && (i7 > 0))
        {
          i9 = this.iColor;
          this.iColor = i1;
          dFill(arrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4);
          this.iColor = i9;
        }
        for (i9 = paramInt2; i9 < paramInt4; i9++)
        {
          k = i9 * i + paramInt1;
          for (n = paramInt1; n < paramInt3; n++)
          {
            if (isM(arrayOfInt[k]))
              arrayOfByte[k] = 0;
            k++;
          }
        }
      }
    }
    t();
  }

  public void dRetouch()
    throws InterruptedException
  {
    try
    {
      getPM();
      this.user.setup(this);
      int i = this.user.pW / 2;
      int j = this.info.W;
      int k = this.info.H;
      LO[] arrayOfLO = this.info.layers;
      setD(0, 0, 0, 0);
      int[] arrayOfInt1 = this.user.points;
      int m = isText() ? 1 : 4;
      for (int n = 0; (n < m) && (this.iSeek < this.iOffset); n++)
        arrayOfInt1[n] = ((r2() & 0xFFFF) << 16 | r2() & 0xFFFF);
      n = arrayOfInt1[0] >> 16;
      int i1 = (short)arrayOfInt1[0];
      Object localObject1;
      int i3;
      switch (this.iHint)
      {
      case 2:
        int i2 = this.user.wait;
        this.user.wait = -2;
        dStart(n + i, i1 + i, 0, false, false);
        dBz(arrayOfInt1);
        this.user.wait = i2;
        break;
      case 8:
      case 12:
        localObject1 = new String(this.offset, this.iSeek, this.iOffset - this.iSeek, "UTF8");
        i3 = ((String)localObject1).indexOf(0);
        dText(((String)localObject1).substring(i3 + 1), n, i1);
        break;
      case 9:
        dCopy(arrayOfInt1);
        break;
      case 7:
        dFill(n, i1);
        break;
      case 14:
        localObject1 = arrayOfLO[this.iLayer];
        Object localObject2;
        switch (i1)
        {
        case 0:
          this.info.swapL(this.iLayerSrc, this.iLayer);
          break;
        case 1:
          this.info.setL(arrayOfInt1[1]);
          break;
        case 2:
          this.info.delL(this.iLayerSrc);
          break;
        case 3:
          if (this.iLayer > this.iLayerSrc)
            for (i3 = this.iLayerSrc; i3 < this.iLayer; i3++)
              this.info.swapL(i3, i3 + 1);
          if (this.iLayer >= this.iLayerSrc)
            break;
          for (i3 = this.iLayerSrc; i3 > this.iLayer; i3--)
            this.info.swapL(i3, i3 - 1);
          break;
        case 6:
          try
          {
            localObject2 = this.info.component.getToolkit();
            n = arrayOfInt1[1] >> 16;
            i1 = (short)arrayOfInt1[1];
            if ((arrayOfInt1[2] & 0xFF) == 1)
              localImage = ((Toolkit)localObject2).createImage(this.offset, this.iSeek, this.iOffset - this.iSeek);
            else
              localImage = ((Toolkit)localObject2).createImage((byte[])this.info.cnf.getRes(new String(this.offset, this.iSeek, this.iOffset - this.iSeek, "UTF8")));
            if (localImage == null)
              break;
            Awt.wait(localImage);
            int i5 = localImage.getWidth(null);
            int i6 = localImage.getHeight(null);
            int[] arrayOfInt2 = Awt.getPix(localImage);
            localImage.flush();
            Image localImage = null;
            if ((i5 <= 0) || (i6 <= 0))
              break;
            arrayOfLO[this.iLayer].toCopy(i5, i6, arrayOfInt2, n, i1);
          }
          catch (Throwable localThrowable2)
          {
            localThrowable2.printStackTrace();
          }
        case 7:
          int i4 = this.offset[4];
          localObject2 = new byte[i4 * 4];
          System.arraycopy(this.offset, 6, localObject2, 0, i4 * 4);
          dFusion(localObject2);
          break;
        case 5:
        case 8:
          ((LO)localObject1).iAlpha = b255[(this.offset[4] & 0xFF)];
          break;
        case 9:
          ((LO)localObject1).iCopy = this.offset[4];
          break;
        case 10:
          ((LO)localObject1).name = new String(this.offset, 4, this.iOffset - 4, "UTF8");
        case 4:
        }
        setD(0, 0, j, k);
        break;
      case 3:
      case 4:
      case 5:
      case 6:
      case 10:
      case 11:
      case 13:
      default:
        dRect(n, i1, arrayOfInt1[1] >> 16, (short)arrayOfInt1[1]);
      }
      if (this.isOver)
        dFlush();
      if (this.user.wait >= 0)
        dBuffer();
    }
    catch (Throwable localThrowable1)
    {
      localThrowable1.printStackTrace();
    }
  }

  private void dStart()
  {
    try
    {
      int i = r2();
      int j = r2();
      this.user.setup(this);
      this.info.layers[this.iLayer].reserve();
      int k = this.iSOB != 0 ? ru() : 0;
      if (this.iSOB != 0)
      {
        this.iSize = ss(k);
        this.iAlpha = sa(k);
      }
      memset(this.user.pX, i);
      memset(this.user.pY, j);
      int m = this.user.pW / 2;
      setD(i - m - 1, j - m - 1, i + m, j + m);
      this.user.fX = i;
      this.user.fY = j;
      if ((this.iHint != 11) && (!this.isAnti))
        dFLine(i, j, k);
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
    catch (InterruptedException localInterruptedException)
    {
    }
  }

  public void dStart(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      this.user.setup(this);
      this.info.layers[this.iLayer].reserve();
      this.iSize = ss(paramInt3);
      this.iAlpha = sa(paramInt3);
      this.user.setup(this);
      if (paramBoolean2)
      {
        int i = this.info.scale;
        paramInt1 = (paramInt1 / i + this.info.scaleX) * this.info.Q;
        paramInt2 = (paramInt2 / i + this.info.scaleY) * this.info.Q;
      }
      if (paramBoolean1)
      {
        ByteStream localByteStream = getWork();
        localByteStream.w(paramInt1, 2);
        localByteStream.w(paramInt2, 2);
        if (this.iSOB != 0)
          localByteStream.write(paramInt3);
      }
      memset(this.user.pX, paramInt1);
      memset(this.user.pY, paramInt2);
      int j = this.user.pW / 2;
      setD(paramInt1 - j - 1, paramInt2 - j - 1, paramInt1 + j, paramInt2 + j);
      this.user.fX = paramInt1;
      this.user.fY = paramInt2;
      if ((this.iHint != 11) && (!this.isAnti))
        dFLine(paramInt1, paramInt2, paramInt3);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    catch (InterruptedException localInterruptedException)
    {
      localInterruptedException.printStackTrace();
    }
  }

  private void dText(String paramString, int paramInt1, int paramInt2)
  {
    try
    {
      int i = this.info.W;
      int j = this.info.H;
      int[] arrayOfInt1 = this.info.layers[this.iLayer].offset;
      byte[] arrayOfByte = this.info.iMOffs;
      float f = b255[this.iAlpha];
      if (f == 0.0F)
        return;
      Font localFont = getFont(this.iSize);
      FontMetrics localFontMetrics = this.info.component.getFontMetrics(localFont);
      if ((paramString == null) || (paramString.length() <= 0))
        return;
      this.info.layers[this.iLayer].reserve();
      int k = this.iHint == 8 ? 1 : 0;
      int m = localFontMetrics.getMaxAdvance();
      int n = localFontMetrics.getMaxAscent() + localFontMetrics.getMaxDescent() + localFontMetrics.getLeading() + 2;
      int i1 = localFontMetrics.getMaxAscent() + localFontMetrics.getLeading() / 2 + 1;
      int i4 = paramString.length();
      if (k != 0)
      {
        i2 = m * (i4 + 1) + 2;
        i3 = n;
      }
      else
      {
        m = localFontMetrics.getMaxAdvance();
        i2 = m + 2;
        i3 = (n + this.iCount) * (i4 + 1);
      }
      int i2 = Math.min(i2, i);
      int i3 = Math.min(i3, j);
      setD(paramInt1, paramInt2, paramInt1 + i2, paramInt2 + i3);
      Image localImage = this.info.component.createImage(i2, i3);
      Graphics localGraphics = localImage.getGraphics();
      localGraphics.setFont(localFont);
      localGraphics.setColor(Color.black);
      localGraphics.fillRect(0, 0, i2, i3);
      localGraphics.setColor(Color.blue);
      if (k != 0)
      {
        localGraphics.drawString(paramString, 1, i1);
      }
      else
      {
        int i5 = i1;
        for (i6 = 0; i6 < i4; i6++)
        {
          localGraphics.drawString(String.valueOf(paramString.charAt(i6)), 1, i5);
          i5 += n + this.iCount;
        }
      }
      localGraphics.dispose();
      localGraphics = null;
      localFont = null;
      localFontMetrics = null;
      int[] arrayOfInt2 = Awt.getPix(localImage);
      localImage.flush();
      localImage = null;
      int i6 = 0;
      int i8 = Math.min(i - paramInt1, i2);
      int i9 = Math.min(j - paramInt2, i3);
      for (int i10 = 0; i10 < i9; i10++)
      {
        i6 = i10 * i2;
        int i7 = (i10 + paramInt2) * i + paramInt1;
        for (int i11 = 0; i11 < i8; i11++)
        {
          if (!isM(arrayOfInt1[i7]))
            arrayOfByte[i7] = (byte)(int)((arrayOfInt2[i6] & 0xFF) * f);
          i6++;
          i7++;
        }
      }
      setD(paramInt1, paramInt2, paramInt1 + i2, paramInt2 + i3);
      t();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  private final int fu(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 == 0)
      return paramInt1;
    int i = paramInt1 >>> 24;
    int j = i + (int)(paramInt3 * b255[(255 - i)]);
    float f = b255[Math.min((int)(paramInt3 * b255d[j]), 255)];
    int k = paramInt1 >>> 16 & 0xFF;
    int m = paramInt1 >>> 8 & 0xFF;
    int n = paramInt1 & 0xFF;
    return j << 24 | k + (int)(((paramInt2 >>> 16 & 0xFF) - k) * f) << 16 | m + (int)(((paramInt2 >>> 8 & 0xFF) - m) * f) << 8 | n + (int)(((paramInt2 & 0xFF) - n) * f);
  }

  public final void get(OutputStream paramOutputStream, ByteStream paramByteStream, M paramM)
  {
    try
    {
      paramByteStream.reset();
      int i = 0;
      int j = 0;
      int k = getFlag(paramM);
      int m = k >>> 8 & 0xFF;
      int n = k & 0xFF;
      paramByteStream.write(k >>> 16);
      paramByteStream.write(m);
      paramByteStream.write(n);
      if ((m & 0x1) != 0)
      {
        i = this.iHint;
        j = 1;
      }
      if ((m & 0x2) != 0)
      {
        if (j != 0)
          paramByteStream.write(i << 4 | this.iPenM);
        else
          i = this.iPenM;
        j = j != 0 ? 0 : 1;
      }
      if ((m & 0x4) != 0)
      {
        if (j != 0)
          paramByteStream.write(i << 4 | this.iMask);
        else
          i = this.iMask;
        j = j != 0 ? 0 : 1;
      }
      if (j != 0)
        paramByteStream.write(i << 4);
      if ((m & 0x8) != 0)
        paramByteStream.write(this.iPen);
      if ((m & 0x10) != 0)
        paramByteStream.write(this.iTT);
      if ((m & 0x20) != 0)
        paramByteStream.write(this.iLayer);
      if ((m & 0x40) != 0)
        paramByteStream.write(this.iLayerSrc);
      if ((n & 0x1) != 0)
        paramByteStream.write(this.iAlpha);
      if ((n & 0x2) != 0)
        paramByteStream.w(this.iColor, 3);
      if ((n & 0x4) != 0)
        paramByteStream.w(this.iColorMask, 3);
      if ((n & 0x8) != 0)
        paramByteStream.write(this.iSize);
      if ((n & 0x10) != 0)
        paramByteStream.write(this.iCount);
      if ((n & 0x20) != 0)
        paramByteStream.w(this.iSA, 2);
      if ((n & 0x40) != 0)
        paramByteStream.w(this.iSS, 2);
      if (this.iPen == 20)
        paramByteStream.w2(this.iAlpha2);
      if (isText())
        if (this.strHint == null)
        {
          paramByteStream.w2(0);
        }
        else
        {
          paramByteStream.w2(this.strHint.length);
          paramByteStream.write(this.strHint);
        }
      if ((this.offset != null) && (this.iOffset > 0))
        paramByteStream.write(this.offset, 0, this.iOffset);
      paramOutputStream.write(paramByteStream.size() >>> 8);
      paramOutputStream.write(paramByteStream.size() & 0xFF);
      paramByteStream.writeTo(paramOutputStream);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
  }

  private final int getFlag(M paramM)
  {
    int j = 0;
    if (this.isAllL)
      j |= 1;
    if (this.isAFix)
      j |= 2;
    if (this.isAnti)
      j |= 16;
    if (this.isCount)
      j |= 8;
    if (this.isOver)
      j |= 4;
    j |= this.iSOB << 6;
    int i = j << 16;
    if (paramM == null)
      return i | 0xFFFF;
    j = 0;
    if (this.iHint != paramM.iHint)
      j |= 1;
    if (this.iPenM != paramM.iPenM)
      j |= 2;
    if (this.iMask != paramM.iMask)
      j |= 4;
    if (this.iPen != paramM.iPen)
      j |= 8;
    if (this.iTT != paramM.iTT)
      j |= 16;
    if (this.iLayer != paramM.iLayer)
      j |= 32;
    if (this.iLayerSrc != paramM.iLayerSrc)
      j |= 64;
    i |= j << 8;
    j = 0;
    if (this.iAlpha != paramM.iAlpha)
      j |= 1;
    if (this.iColor != paramM.iColor)
      j |= 2;
    if (this.iColorMask != paramM.iColorMask)
      j |= 4;
    if (this.iSize != paramM.iSize)
      j |= 8;
    if (this.iCount != paramM.iCount)
      j |= 16;
    if (this.iSA != paramM.iSA)
      j |= 32;
    if (this.iSS != paramM.iSS)
      j |= 64;
    return i | j;
  }

  public Image getImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    paramInt2 = Math.round(paramInt2 / this.info.scale) + this.info.scaleX;
    paramInt3 = Math.round(paramInt3 / this.info.scale) + this.info.scaleY;
    paramInt4 /= this.info.scale;
    paramInt5 /= this.info.scale;
    int i = this.info.Q;
    if (i <= 1)
      return this.info.component.createImage(new MemoryImageSource(paramInt4, paramInt5, this.info.layers[paramInt1].offset, paramInt3 * this.info.W + paramInt2, this.info.W));
    Image localImage1 = this.info.component.createImage(new MemoryImageSource(paramInt4 * i, paramInt5 * i, this.info.layers[paramInt1].offset, paramInt3 * i * this.info.W + paramInt2 * i, this.info.W));
    Image localImage2 = localImage1.getScaledInstance(paramInt4, paramInt5, 2);
    localImage1.flush();
    return localImage2;
  }

  private final int getM(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt2 == 0)
      return paramInt1;
    int i;
    int j;
    int k;
    int m;
    float f2;
    switch (this.iPen)
    {
    case 12:
    case 13:
    case 14:
    case 15:
    case 16:
    case 17:
    case 18:
    case 19:
    default:
      return fu(paramInt1, this.iColor, paramInt2);
    case 4:
    case 5:
      i = paramInt1 >>> 24;
      j = i - (int)(i * b255[paramInt2]);
      return j == 0 ? 16777215 : j << 24 | paramInt1 & 0xFFFFFF;
    case 6:
    case 11:
      i = paramInt1 >>> 24;
      j = paramInt1 >>> 16 & 0xFF;
      k = paramInt1 >>> 8 & 0xFF;
      m = paramInt1 & 0xFF;
      f2 = b255[paramInt2];
      return (i << 24) + (Math.min(j + (int)(j * f2), 255) << 16) + (Math.min(k + (int)(k * f2), 255) << 8) + Math.min(m + (int)(m * f2), 255);
    case 7:
      i = paramInt1 >>> 24;
      j = paramInt1 >>> 16 & 0xFF;
      k = paramInt1 >>> 8 & 0xFF;
      m = paramInt1 & 0xFF;
      f2 = b255[paramInt2];
      return (i << 24) + (Math.max(j - (int)((255 - j) * f2), 0) << 16) + (Math.max(k - (int)((255 - k) * f2), 0) << 8) + Math.max(m - (int)((255 - m) * f2), 0);
    case 8:
      (paramInt1 >>> 24);
      (paramInt1 >>> 16 & 0xFF);
      (paramInt1 >>> 8 & 0xFF);
      (paramInt1 & 0xFF);
      float f1 = b255[paramInt2];
      int[] arrayOfInt1 = this.user.argb;
      int[] arrayOfInt2 = this.info.layers[this.iLayer].offset;
      int n = this.info.W;
      for (int i1 = 0; i1 < 4; i1++)
        arrayOfInt1[i1] = 0;
      k = paramInt3 % n;
      paramInt3 += (k == n - 1 ? -1 : k == 0 ? 1 : 0);
      paramInt3 += (paramInt3 > n * (this.info.H - 1) ? -n : paramInt3 < n ? n : 0);
      for (i1 = -1; i1 < 2; i1++)
        for (int i2 = -1; i2 < 2; i2++)
        {
          k = arrayOfInt2[(paramInt3 + i2 + i1 * n)];
          (k >>> 24);
          for (int i3 = 0; i3 < 4; i3++)
            arrayOfInt1[i3] += (k >>> (i3 << 3) & 0xFF);
        }
      for (i1 = 0; i1 < 4; i1++)
      {
        k = paramInt1 >>> (i1 << 3) & 0xFF;
        arrayOfInt1[i1] = (k + (int)((arrayOfInt1[i1] / 9 - k) * f1));
      }
      return arrayOfInt1[3] << 24 | arrayOfInt1[2] << 16 | arrayOfInt1[1] << 8 | arrayOfInt1[0];
    case 9:
    case 20:
      if (paramInt2 == 0)
        return paramInt1;
      return paramInt2 << 24 | 0xFF0000;
    case 10:
    }
    return paramInt2 << 24 | this.iColor;
  }

  public final byte[] getOffset()
  {
    return this.offset;
  }

  private final int[] getPM()
  {
    if ((isText()) || ((this.iHint >= 3) && (this.iHint <= 6)))
      return null;
    int[] arrayOfInt1 = this.user.p;
    if ((this.user.pM != this.iPenM) || (this.user.pA != this.iAlpha) || (this.user.pS != this.iSize))
    {
      int[][] arrayOfInt = this.info.bPen[this.iPenM];
      int[] arrayOfInt2 = arrayOfInt[this.iSize];
      int i = arrayOfInt2.length;
      if ((arrayOfInt1 == null) || (arrayOfInt1.length < i))
        arrayOfInt1 = new int[i];
      float f = b255[this.iAlpha];
      for (int j = 0; j < i; j++)
        arrayOfInt1[j] = (int)(arrayOfInt2[j] * f);
      this.user.pW = (int)Math.sqrt(i);
      this.user.pM = this.iPenM;
      this.user.pA = this.iAlpha;
      this.user.pS = this.iSize;
      this.user.p = arrayOfInt1;
      this.user.countMax = (this.iCount >= 0 ? this.iCount : (int)(this.user.pW / (float)Math.sqrt(arrayOfInt[(arrayOfInt.length - 1)].length) * -this.iCount));
      this.user.count = Math.min(this.user.countMax, this.user.count);
    }
    return arrayOfInt1;
  }

  private final float getTT(int paramInt1, int paramInt2)
  {
    if (this.iTT == 0)
      return 1.0F;
    if (this.iTT < 12)
      return isTone(this.iTT - 1, paramInt1, paramInt2) ? 0 : 1;
    int i = this.user.pTTW;
    return this.user.pTT[(paramInt2 % i * i + paramInt1 % i)];
  }

  private final ByteStream getWork()
  {
    this.info.workOut.reset();
    return this.info.workOut;
  }

  private final boolean isM(int paramInt)
  {
    if (this.iMask == 0)
      return false;
    paramInt &= 16777215;
    return this.iColorMask == paramInt;
  }

  public static final boolean isTone(int paramInt1, int paramInt2, int paramInt3)
  {
    switch (paramInt1)
    {
    default:
      break;
    case 10:
      if (((paramInt2 + 3) % 4 != 0) || ((paramInt3 + 2) % 4 != 0))
        break;
      return true;
    case 9:
    case 8:
      if ((((paramInt2 + 1) % 4 == 0) && ((paramInt3 + 2) % 4 == 0)) || (paramInt2 % 2 == 0) || ((paramInt3 + 1) % 2 == 0))
        break;
      return true;
      break;
    case 7:
    case 6:
    case 5:
      if ((((paramInt2 + 2) % 4 == 0) && ((paramInt3 + 3) % 4 == 0)) || ((paramInt2 % 4 == 0) && ((paramInt3 + 1) % 4 == 0)) || ((paramInt2 + 1) % 2 == (paramInt3 + 1) % 2))
        break;
      return true;
    case 4:
    case 3:
      if ((((paramInt2 + 1) % 4 == 0) && ((paramInt3 + 3) % 4 == 0)) || ((paramInt2 % 2 == 0) && (paramInt3 % 2 == 0)))
        break;
      return true;
    case 2:
    case 1:
    case 0:
      if ((((paramInt2 + 2) % 4 == 0) && ((paramInt3 + 4) % 4 == 0)) || (((paramInt2 + 2) % 4 == 0) && ((paramInt3 + 2) % 4 == 0)) || ((paramInt2 % 4 == 0) && (paramInt3 % 4 == 0)))
        break;
      return true;
    }
    return false;
  }

  private int[] loadIm(Object paramObject, boolean paramBoolean)
  {
    try
    {
      Component localComponent = this.info.component;
      Image localImage = localComponent.getToolkit().createImage((byte[])this.info.cnf.getRes(paramObject));
      this.info.cnf.remove(paramObject);
      Awt.wait(localImage);
      int[] arrayOfInt = Awt.getPix(localImage);
      int i = arrayOfInt.length;
      localImage.flush();
      localImage = null;
      int j;
      if (paramBoolean)
        for (j = 0; j < i; j++)
          arrayOfInt[j] = (arrayOfInt[j] & 0xFF ^ 0xFF);
      else
        for (j = 0; j < i; j++)
          arrayOfInt[j] &= 255;
      return arrayOfInt;
    }
    catch (RuntimeException localRuntimeException)
    {
    }
    return null;
  }

  public final void m_paint(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = this.info.scale;
    int j = this.info.Q;
    paramInt1 = (paramInt1 / i + this.info.scaleX) * j;
    paramInt2 = (paramInt2 / i + this.info.scaleY) * j;
    paramInt3 = paramInt3 / i * j;
    paramInt4 = paramInt4 / i * j;
    dBuffer(false, paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }

  public final void memset(float[] paramArrayOfFloat, float paramFloat)
  {
    int i = paramArrayOfFloat.length >>> 1;
    for (int j = 0; j < i; j++)
      paramArrayOfFloat[j] = paramFloat;
    System.arraycopy(paramArrayOfFloat, 0, paramArrayOfFloat, i - 1, i);
    paramArrayOfFloat[(i + i - 1)] = paramFloat;
  }

  public final void memset(int[] paramArrayOfInt, int paramInt)
  {
    int i = paramArrayOfInt.length >>> 1;
    for (int j = 0; j < i; j++)
      paramArrayOfInt[j] = paramInt;
    System.arraycopy(paramArrayOfInt, 0, paramArrayOfInt, i - 1, i);
    paramArrayOfInt[(i + i - 1)] = paramInt;
  }

  public final void memset(byte[] paramArrayOfByte, byte paramByte)
  {
    int i = paramArrayOfByte.length >>> 1;
    for (int j = 0; j < i; j++)
      paramArrayOfByte[j] = paramByte;
    System.arraycopy(paramArrayOfByte, 0, paramArrayOfByte, i - 1, i);
    paramArrayOfByte[(i + i - 1)] = paramByte;
  }

  public final Image mkLPic(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    paramInt1 *= paramInt5;
    paramInt2 *= paramInt5;
    paramInt3 *= paramInt5;
    paramInt4 *= paramInt5;
    int i = paramArrayOfInt == null ? 1 : 0;
    int j = this.info.L;
    LO[] arrayOfLO = this.info.layers;
    if (i != 0)
      paramArrayOfInt = this.user.buffer;
    memset(paramArrayOfInt, 16777215);
    for (int k = 0; k < j; k++)
      arrayOfLO[k].draw(paramArrayOfInt, paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4, paramInt3);
    if (i != 0)
      this.user.raster.newPixels(this.user.image, paramInt3, paramInt4, paramInt5);
    else
      this.user.raster.scale(paramArrayOfInt, paramInt3, paramInt4, paramInt5);
    return this.user.image;
  }

  private final Image mkMPic(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    paramInt1 *= paramInt5;
    paramInt2 *= paramInt5;
    paramInt3 *= paramInt5;
    paramInt4 *= paramInt5;
    int[] arrayOfInt1 = this.user.buffer;
    int i = this.info.L;
    LO[] arrayOfLO = this.info.layers;
    memset(arrayOfInt1, 16777215);
    for (int j = 0; j < i; j++)
      if (j == this.iLayer)
      {
        byte[] arrayOfByte = this.info.iMOffs;
        int[] arrayOfInt2 = arrayOfLO[j].offset;
        int i5 = this.info.W;
        float f1 = arrayOfLO[j].iAlpha;
        if (arrayOfInt2 == null)
          continue;
        int i4;
        int i1;
        int i2;
        int i3;
        int i7;
        int i6;
        float f2;
        int k;
        int m;
        int n;
        switch (arrayOfLO[j].iCopy)
        {
        case 1:
          for (i4 = 0; i4 < paramInt4; i4++)
          {
            i1 = i5 * (i4 + paramInt2) + paramInt1;
            i2 = paramInt3 * i4;
            for (i3 = 0; i3 < paramInt3; i3++)
            {
              i7 = arrayOfInt1[i2];
              i6 = getM(arrayOfInt2[i1], arrayOfByte[i1] & 0xFF, i1);
              f2 = b255[(i6 >>> 24)] * f1;
              if (f2 > 0.0F)
                arrayOfInt1[i2] = (((i7 >>> 16 & 0xFF) - (int)(b255[(i7 >>> 16 & 0xFF)] * ((i6 >>> 16 & 0xFF ^ 0xFF) * f2)) << 16) + ((i7 >>> 8 & 0xFF) - (int)(b255[(i7 >>> 8 & 0xFF)] * ((i6 >>> 8 & 0xFF ^ 0xFF) * f2)) << 8) + ((i7 & 0xFF) - (int)(b255[(i7 & 0xFF)] * ((i6 & 0xFF ^ 0xFF) * f2))));
              i2++;
              i1++;
            }
          }
          break;
        case 2:
          for (i4 = 0; i4 < paramInt4; i4++)
          {
            i1 = i5 * (i4 + paramInt2) + paramInt1;
            i2 = paramInt3 * i4;
            for (i3 = 0; i3 < paramInt3; i3++)
            {
              i7 = arrayOfInt1[i2];
              i6 = getM(arrayOfInt2[i1], arrayOfByte[i1] & 0xFF, i1);
              f2 = b255[(i6 >>> 24)] * f1;
              i6 ^= 16777215;
              k = i7 >>> 16 & 0xFF;
              m = i7 >>> 8 & 0xFF;
              n = i7 & 0xFF;
              arrayOfInt1[(i2++)] = (f2 == 1.0F ? i6 : k + (int)(((i6 >>> 16 & 0xFF) - k) * f2) << 16 | m + (int)(((i6 >>> 8 & 0xFF) - m) * f2) << 8 | n + (int)(((i6 & 0xFF) - n) * f2));
              i1++;
            }
          }
          break;
        default:
          for (i4 = 0; i4 < paramInt4; i4++)
          {
            i1 = i5 * (i4 + paramInt2) + paramInt1;
            i2 = paramInt3 * i4;
            for (i3 = 0; i3 < paramInt3; i3++)
            {
              i7 = arrayOfInt1[i2];
              i6 = getM(arrayOfInt2[i1], arrayOfByte[i1] & 0xFF, i1);
              f2 = b255[(i6 >>> 24)] * f1;
              if (f2 == 1.0F)
              {
                arrayOfInt1[(i2++)] = i6;
              }
              else
              {
                k = i7 >>> 16 & 0xFF;
                m = i7 >>> 8 & 0xFF;
                n = i7 & 0xFF;
                arrayOfInt1[(i2++)] = (k + (int)(((i6 >>> 16 & 0xFF) - k) * f2) << 16 | m + (int)(((i6 >>> 8 & 0xFF) - m) * f2) << 8 | n + (int)(((i6 & 0xFF) - n) * f2));
              }
              i1++;
            }
          }
          break;
        }
      }
      else
      {
        arrayOfLO[j].draw(arrayOfInt1, paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4, paramInt3);
      }
    this.user.raster.newPixels(this.user.image, paramInt3, paramInt4, paramInt5);
    return this.user.image;
  }

  public Info newInfo(Applet paramApplet, Component paramComponent, Res paramRes)
  {
    if (this.info != null)
      return this.info;
    this.info = new Info();
    this.info.cnf = paramRes;
    this.info.component = paramComponent;
    Info localInfo = this.info;
    M localM = this.info.m;
    float f1 = 3.141593F;
    for (int i = 1; i < 256; i++)
    {
      b255[i] = (i / 255.0F);
      b255d[i] = (255.0F / i);
    }
    b255[0] = 0.0F;
    b255d[0] = 0.0F;
    int[][][] arrayOfInt = this.info.bPen;
    int i5 = 0;
    int i7 = 1;
    int i9 = 255;
    localM.iAlpha = 255;
    set(localM);
    int[][] arrayOfInt1 = new int[23];
    int n;
    int i8;
    for (i = 0; i < 23; i++)
    {
      n = i7 * i7;
      int j;
      if (i7 <= 6)
      {
        int[] tmp185_183 = new int[n];
        arrayOfInt2 = tmp185_183;
        arrayOfInt1[i] = tmp185_183;
        i8 = i7;
        for (j = 0; j < n; j++)
          arrayOfInt2[j] = ((j < i7) || (n - j < i7) || (j % i7 == 0) || (j % i7 == i7 - 1) ? i9 : localM.iAlpha);
        if (i7 >= 3)
        {
          int tmp296_295 = (arrayOfInt2[(i7 * (i7 - 1))] = arrayOfInt2[(n - 1)] = 0);
          arrayOfInt2[(i7 - 1)] = tmp296_295;
          arrayOfInt2[0] = tmp296_295;
        }
      }
      else
      {
        i8 = i7 + 1;
        int[] tmp319_317 = new int[i8 * i8];
        arrayOfInt2 = tmp319_317;
        arrayOfInt1[i] = tmp319_317;
        int i6 = (i7 - 1) / 2;
        int i1 = (int)(Math.round(2.0F * f1 * i6) * 3.0F);
        for (j = 0; j < i1; j++)
        {
          int i4 = Math.min(i6 + (int)Math.round(i6 * Math.cos(j)), i7);
          i5 = Math.min(i6 + (int)Math.round(i6 * Math.sin(j)), i7);
          arrayOfInt2[(i5 * i8 + i4)] = i9;
        }
        localInfo.W = (localInfo.H = i8);
        dFill(arrayOfInt2, 0, 0, i8, i8);
      }
      i7 += (i < 18 ? 2 : i <= 7 ? 1 : 4);
    }
    arrayOfInt[0] = arrayOfInt1;
    localM.iAlpha = 255;
    arrayOfInt1 = new int[32];
    arrayOfInt1[0] = { 128 };
    arrayOfInt1[1] = { 255 };
    arrayOfInt1[2] = { 0, 128, 0, 128, 255, 128, 0, 128 };
    arrayOfInt1[3] = { 128, 174, 128, 174, 255, 174, 128, 174, 128 };
    arrayOfInt1[4] = { 174, 255, 174, 255, 255, 255, 174, 255, 174 };
    arrayOfInt1[5] = new int[9];
    memset(arrayOfInt1[5], 255);
    arrayOfInt1[6] = { 0, 128, 128, 0, 128, 255, 255, 128, 128, 255, 255, 128, 0, 128, 128 };
    int[] arrayOfInt2 = arrayOfInt1[7] =  = new int[16];
    memset(arrayOfInt2, 255);
    char tmp857_856 = (arrayOfInt2[15] = arrayOfInt2[12] = '');
    arrayOfInt2[3] = tmp857_856;
    arrayOfInt2[0] = tmp857_856;
    memset(arrayOfInt1[8] =  = new int[16], 255);
    i7 = 3;
    int m;
    for (i = 9; i < 32; i++)
    {
      i8 = i7 + 3;
      float f6 = i7 / 2.0F;
      int[] tmp911_909 = new int[i8 * i8];
      arrayOfInt2 = tmp911_909;
      arrayOfInt1[i] = tmp911_909;
      int i2 = (int)(Math.round(2.0F * f1 * f6) * (2 + i / 16)) + i * 2;
      for (int k = 0; k < i2; k++)
      {
        float f2;
        int i10 = (int)(f2 = f6 + 1.5F + f6 * (float)Math.cos(k));
        float f3;
        int i11 = (int)(f3 = f6 + 1.5F + f6 * (float)Math.sin(k));
        float f4 = f2 - i10;
        float f5 = f3 - i11;
        int i12 = i11 * i8 + i10;
        arrayOfInt2[i12] += (int)((1.0F - f4) * 255.0F);
        arrayOfInt2[(i12 + 1)] += (int)(f4 * 255.0F);
        arrayOfInt2[(i12 + i8)] += (int)((1.0F - f5) * 255.0F);
        arrayOfInt2[(i12 + i8 + 1)] += (int)(f5 * 255.0F);
      }
      n = i8 * i8;
      for (m = 0; m < n; m++)
        arrayOfInt2[m] = Math.min(arrayOfInt2[m], 255);
      i7 += 2;
      localInfo.W = (localInfo.H = i8);
      dFill(arrayOfInt2, 0, 0, i8, i8);
    }
    arrayOfInt[1] = arrayOfInt1;
    set(null);
    localM.set(null);
    if (paramRes != null)
    {
      for (i = 0; i < 16; i++)
      {
        for (int i3 = 0; paramRes.get("pm" + i + '/' + i3 + ".gif") != null; i3++);
        if (i3 <= 0)
          continue;
        arrayOfInt[i] = new int[i3];
        for (m = 0; m < i3; m++)
          arrayOfInt[i][m] = loadIm("pm" + i + '/' + m + ".gif", true);
      }
      this.info.bTT = new float[paramRes.getP("tt_size", 31)];
    }
    String str = paramApplet.getParameter("tt.zip");
    if ((str != null) && (str.length() > 0))
      this.info.dirTT = str;
    return this.info;
  }

  public User newUser(Component paramComponent)
  {
    if (this.user == null)
    {
      this.user = new User();
      if (color_model == null)
        color_model = new DirectColorModel(24, 16711680, 65280, 255);
      this.user.raster = new SRaster(color_model, this.user.buffer, 128, 128);
      this.user.image = paramComponent.createImage(this.user.raster);
    }
    return this.user;
  }

  public final int pix(int paramInt1, int paramInt2)
  {
    if (!this.isAllL)
      return this.info.layers[this.iLayer].getPixel(paramInt1, paramInt2);
    int i = this.info.L;
    int k = 0;
    int n = 16777215;
    (this.info.W * paramInt2 + paramInt1);
    for (int i2 = 0; i2 < i; i2++)
    {
      int i1 = this.info.layers[i2].getPixel(paramInt1, paramInt2);
      float f = b255[(i1 >>> 24)];
      if (f == 0.0F)
        continue;
      if (f == 1.0F)
      {
        n = i1;
        k = 255;
      }
      k = (int)(k + (255 - k) * f);
      int j = 0;
      for (int i3 = 16; i3 >= 0; i3 -= 8)
      {
        int m = n >>> i3 & 0xFF;
        j |= m + (int)(((i1 >>> i3 & 0xFF) - m) * f) << i3;
      }
      n = j;
    }
    return k << 24 | n;
  }

  private final byte r()
  {
    if (this.iSeek >= this.iOffset)
      return 0;
    return this.offset[(this.iSeek++)];
  }

  private final int r(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = paramInt2 - 1; j >= 0; j--)
      i |= (paramArrayOfByte[(paramInt1++)] & 0xFF) << j * 8;
    return i;
  }

  private final short r2()
  {
    return (short)((ru() << 8) + ru());
  }

  public void reset(boolean paramBoolean)
  {
    byte[] arrayOfByte = this.info.iMOffs;
    int j = this.info.W;
    int k = Math.max(this.user.X, 0);
    int m = Math.max(this.user.Y, 0);
    int n = Math.min(this.user.X2, j);
    int i1 = Math.min(this.user.Y2, this.info.H);
    for (int i3 = m; i3 < i1; i3++)
    {
      int i = k + i3 * j;
      for (int i2 = k; i2 < n; i2++)
        arrayOfByte[(i++)] = 0;
    }
    if (paramBoolean)
      dBuffer(false, k, m, n, i1);
    setD(0, 0, 0, 0);
  }

  private final int rPo()
  {
    int i = r();
    return i != -128 ? i : r2();
  }

  private final int ru()
  {
    return r() & 0xFF;
  }

  private final int s(int paramInt1, int paramInt2, int paramInt3)
  {
    byte[] arrayOfByte = this.info.iMOffs;
    int i = this.info.W - 1;
    int j = (i + 1) * paramInt3 + paramInt2;
    while ((paramInt2 < i) && (pix(paramInt2 + 1, paramInt3) == paramInt1) && (arrayOfByte[(j + 1)] == 0))
    {
      j++;
      paramInt2++;
    }
    return paramInt2;
  }

  private final int sa(int paramInt)
  {
    if ((this.iSOB & 0x1) == 0)
      return this.iAlpha;
    int i = this.iSA & 0xFF;
    return i + (int)(b255[((this.iSA >>> 8) - i)] * paramInt);
  }

  public final int set(byte[] paramArrayOfByte, int paramInt)
  {
    int i = (paramArrayOfByte[(paramInt++)] & 0xFF) << 8 | paramArrayOfByte[(paramInt++)] & 0xFF;
    int j = paramInt;
    if (i <= 2)
      return i + 2;
    try
    {
      int k = 0;
      int m = 0;
      int n = paramArrayOfByte[(paramInt++)] & 0xFF;
      int i1 = paramArrayOfByte[(paramInt++)] & 0xFF;
      int i2 = paramArrayOfByte[(paramInt++)] & 0xFF;
      this.isAllL = ((n & 0x1) != 0);
      this.isAFix = ((n & 0x2) != 0);
      this.isOver = ((n & 0x4) != 0);
      this.isCount = ((n & 0x8) != 0);
      this.isAnti = ((n & 0x10) != 0);
      this.iSOB = (n >>> 6);
      if ((i1 & 0x1) != 0)
      {
        k = paramArrayOfByte[(paramInt++)] & 0xFF;
        m = 1;
        this.iHint = (k >>> 4);
      }
      if ((i1 & 0x2) != 0)
      {
        if (m == 0)
        {
          k = paramArrayOfByte[(paramInt++)] & 0xFF;
          this.iPenM = (k >>> 4);
        }
        else
        {
          this.iPenM = (k & 0xF);
        }
        m = m != 0 ? 0 : 1;
      }
      if ((i1 & 0x4) != 0)
      {
        if (m == 0)
        {
          k = paramArrayOfByte[(paramInt++)] & 0xFF;
          this.iMask = (k >>> 4);
        }
        else
        {
          this.iMask = (k & 0xF);
        }
        m = m != 0 ? 0 : 1;
      }
      if ((i1 & 0x8) != 0)
        this.iPen = (paramArrayOfByte[(paramInt++)] & 0xFF);
      if ((i1 & 0x10) != 0)
        this.iTT = (paramArrayOfByte[(paramInt++)] & 0xFF);
      if ((i1 & 0x20) != 0)
        this.iLayer = (paramArrayOfByte[(paramInt++)] & 0xFF);
      if ((i1 & 0x40) != 0)
        this.iLayerSrc = (paramArrayOfByte[(paramInt++)] & 0xFF);
      if ((i2 & 0x1) != 0)
        this.iAlpha = (paramArrayOfByte[(paramInt++)] & 0xFF);
      if ((i2 & 0x2) != 0)
      {
        this.iColor = r(paramArrayOfByte, paramInt, 3);
        paramInt += 3;
      }
      if ((i2 & 0x4) != 0)
      {
        this.iColorMask = r(paramArrayOfByte, paramInt, 3);
        paramInt += 3;
      }
      if ((i2 & 0x8) != 0)
        this.iSize = (paramArrayOfByte[(paramInt++)] & 0xFF);
      if ((i2 & 0x10) != 0)
        this.iCount = paramArrayOfByte[(paramInt++)];
      if ((i2 & 0x20) != 0)
      {
        this.iSA = r(paramArrayOfByte, paramInt, 2);
        paramInt += 2;
      }
      if ((i2 & 0x40) != 0)
      {
        this.iSS = r(paramArrayOfByte, paramInt, 2);
        paramInt += 2;
      }
      if (this.iPen == 20)
      {
        this.iAlpha2 = r(paramArrayOfByte, paramInt, 2);
        paramInt += 2;
      }
      if (isText())
      {
        int i3 = r(paramArrayOfByte, paramInt, 2);
        paramInt += 2;
        if (i3 == 0)
        {
          this.strHint = null;
        }
        else
        {
          this.strHint = new byte[i3];
          System.arraycopy(paramArrayOfByte, paramInt, this.strHint, 0, i3);
          paramInt += i3;
        }
      }
      j = i - (paramInt - j);
      if (j > 0)
      {
        if ((this.offset == null) || (this.offset.length < j))
          this.offset = new byte[j];
        this.iOffset = j;
        System.arraycopy(paramArrayOfByte, paramInt, this.offset, 0, j);
      }
      else
      {
        this.iOffset = 0;
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
      this.iOffset = 0;
    }
    return i + 2;
  }

  public final void set(String paramString)
  {
    try
    {
      if ((paramString == null) || (paramString.length() == 0))
        return;
      Field[] arrayOfField = getClass().getDeclaredFields();
      int i = paramString.indexOf('@');
      if (i < 0)
        i = paramString.length();
      int k;
      int m;
      Object localObject;
      for (int j = 0; j < i; j = k + 1)
      {
        k = paramString.indexOf('=', j);
        if (k == -1)
          break;
        String str1 = paramString.substring(j, k);
        j = k + 1;
        k = paramString.indexOf(';', j);
        if (k < 0)
          k = i;
        try
        {
          for (m = 0; m < arrayOfField.length; m++)
          {
            localObject = arrayOfField[m];
            if (!((Field)localObject).getName().equals(str1))
              continue;
            String str2 = paramString.substring(j, k);
            Class localClass = ((Field)localObject).getType();
            if (localClass.equals(Integer.TYPE))
            {
              ((Field)localObject).setInt(this, Integer.parseInt(str2));
              break;
            }
            if (localClass.equals(Boolean.TYPE))
            {
              ((Field)localObject).setBoolean(this, Integer.parseInt(str2) != 0);
              break;
            }
            ((Field)localObject).set(this, str2);
            break;
          }
        }
        catch (NumberFormatException localNumberFormatException)
        {
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
        }
      }
      if (i != paramString.length())
      {
        localObject = getWork();
        for (m = i + 1; m < paramString.length(); m += 2)
          ((ByteStream)localObject).write(Character.digit(paramString.charAt(m), 16) << 4 | Character.digit(paramString.charAt(m + 1), 16));
        this.offset = ((ByteStream)localObject).toByteArray();
        this.iOffset = this.offset.length;
      }
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public final void set(M paramM)
  {
    if (paramM == null)
      paramM = mgDef;
    this.iHint = paramM.iHint;
    this.iPen = paramM.iPen;
    this.iPenM = paramM.iPenM;
    this.iTT = paramM.iTT;
    this.iMask = paramM.iMask;
    this.iSize = paramM.iSize;
    this.iSS = paramM.iSS;
    this.iCount = paramM.iCount;
    this.isOver = paramM.isOver;
    this.isCount = paramM.isCount;
    this.isAFix = paramM.isAFix;
    this.isAnti = paramM.isAnti;
    this.isAllL = paramM.isAllL;
    this.iAlpha = paramM.iAlpha;
    this.iAlpha2 = paramM.iAlpha2;
    this.iSA = paramM.iSA;
    this.iColor = paramM.iColor;
    this.iColorMask = paramM.iColorMask;
    this.iLayer = paramM.iLayer;
    this.iLayerSrc = paramM.iLayerSrc;
    this.iSOB = paramM.iSOB;
    this.strHint = paramM.strHint;
    this.iOffset = 0;
  }

  public final int set(ByteStream paramByteStream)
  {
    return set(paramByteStream.getBuffer(), 0);
  }

  public void setRetouch(int[] paramArrayOfInt, byte[] paramArrayOfByte, int paramInt, boolean paramBoolean)
  {
    try
    {
      int i = 4;
      int j = this.info.scale;
      int k = this.info.Q;
      int m = this.info.scaleX;
      int n = this.info.scaleY;
      getPM();
      int i1 = this.user.pW / 2;
      int i2 = this.iHint == 2 ? i1 : 0;
      int[] arrayOfInt = this.user.points;
      switch (this.iHint)
      {
      case 8:
      case 12:
        i = 1;
        break;
      case 2:
      case 7:
        break;
      case 9:
        i = 3;
        break;
      case 14:
        break;
      case 10:
        i = 0;
        break;
      case 3:
      case 4:
      case 5:
      case 6:
      case 11:
      case 13:
      default:
        i = 2;
      }
      if (paramArrayOfInt != null)
        i = Math.min(i, paramArrayOfInt.length);
      for (int i5 = 0; i5 < i; i5++)
      {
        int i3 = paramArrayOfInt[i5] >> 16;
        int i4 = (short)paramArrayOfInt[i5];
        if (paramBoolean)
        {
          i3 = (i3 / j + m) * k - i2;
          i4 = (i4 / j + n) * k - i2;
        }
        arrayOfInt[i5] = (i3 << 16 | i4 & 0xFFFF);
      }
      ByteStream localByteStream = getWork();
      for (int i6 = 0; i6 < i; i6++)
        localByteStream.w(arrayOfInt[i6], 4);
      if ((paramArrayOfByte != null) && (paramInt > 0))
        localByteStream.write(paramArrayOfByte, 0, paramInt);
      this.offset = localByteStream.writeTo(this.offset, 0);
      this.iOffset = localByteStream.size();
      localByteStream.reset();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private final void addD(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.user.addRect(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  private final void setD(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.user.setRect(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void setInfo(Info paramInfo)
  {
    this.info = paramInfo;
  }

  public void setUser(User paramUser)
  {
    this.user = paramUser;
  }

  private final void shift(int paramInt1, int paramInt2)
  {
    System.arraycopy(this.user.pX, 1, this.user.pX, 0, 3);
    System.arraycopy(this.user.pY, 1, this.user.pY, 0, 3);
    this.user.pX[3] = paramInt1;
    this.user.pY[3] = paramInt2;
  }

  private final int ss(int paramInt)
  {
    if ((this.iSOB & 0x2) == 0)
      return this.iSize;
    int i = this.iSS & 0xFF;
    return (int)((i + b255[((this.iSS >>> 8) - i)] * paramInt) * this.user.pV);
  }

  private final void t()
  {
    if (this.iTT == 0)
      return;
    byte[] arrayOfByte = this.info.iMOffs;
    int k = this.info.W;
    int m = this.user.X;
    int n = this.user.Y;
    int i1 = this.user.X2;
    int i2 = this.user.Y2;
    for (int i3 = n; i3 < i2; i3++)
    {
      int j = k * i3 + m;
      for (int i = m; i < i1; i++)
        arrayOfByte[j] = (byte)(int)((arrayOfByte[(j++)] & 0xFF) * getTT(i, i3));
    }
  }

  private final void wPo(int paramInt)
    throws IOException
  {
    ByteStream localByteStream = this.info.workOut;
    if ((paramInt > 127) || (paramInt < -127))
    {
      localByteStream.write(-128);
      localByteStream.w(paramInt, 2);
    }
    else
    {
      localByteStream.write(paramInt);
    }
  }

  public boolean isText()
  {
    return (this.iHint == 8) || (this.iHint == 12);
  }

  public Font getFont(int paramInt)
  {
    try
    {
      if (this.strHint != null)
        return Font.decode(new String(this.strHint, "UTF8") + paramInt);
    }
    catch (IOException localIOException)
    {
    }
    return new Font("sansserif", 0, this.iSize);
  }

  public static float[] getb255()
  {
    return b255;
  }

  public class User
  {
    private Image image = null;
    private SRaster raster = null;
    private int[] buffer = new int[65536];
    private int[] argb = new int[4];
    public int[] points = new int[6];
    private int[] ps2 = null;
    private int[] p = null;
    private int pW;
    private int pM = -1;
    private int pA = -1;
    private int pS = -1;
    private float pV = 1.0F;
    private float[] pTT = null;
    private int pTTW;
    private boolean isDirect;
    public int wait = 0;
    public boolean isPre = false;
    private int[] pX = new int[4];
    private int[] pY = new int[4];
    private int oX;
    private int oY;
    private float fX;
    private float fY;
    private int iDCount;
    private int X;
    private int Y;
    private int X2;
    private int Y2;
    private int count = 0;
    private int countMax;

    public User()
    {
    }

    private void setup(M paramM)
    {
      this.pV = M.b255[(M.Info.access$0(paramM.info)[paramM.iPenM].length - 1)];
      paramM.getPM();
      this.count = 0;
      this.iDCount = 0;
      this.oX = -1000;
      this.oY = -1000;
      this.isDirect = ((paramM.iPen == 3) || (paramM.iHint == 9) || (paramM.isOver));
      if (M.this.info.L <= paramM.iLayer)
        M.this.info.setL(paramM.iLayer + 1);
      M.this.info.layers[paramM.iLayer].isDraw = true;
      if (paramM.iTT >= 12)
      {
        this.pTT = M.this.info.getTT(paramM.iTT);
        this.pTTW = (int)Math.sqrt(this.pTT.length);
      }
    }

    public void setIm(M paramM)
    {
      if (paramM.isText())
        return;
      if ((this.pM != paramM.iPenM) || (this.pA != paramM.iAlpha) || (this.pS != paramM.iSize))
      {
        int[] arrayOfInt = M.Info.access$0(paramM.info)[paramM.iPenM][paramM.iSize];
        int i = arrayOfInt.length;
        if ((this.p == null) || (this.p.length < i))
          this.p = new int[i];
        float f1 = M.b255[paramM.iAlpha];
        for (int j = 0; j < i; j++)
        {
          float f2 = arrayOfInt[j] * f1;
          this.p[j] = ((f2 <= 1.0F) && (f2 > 0.0F) ? 1 : (int)f2);
        }
        this.pW = (paramM.iPen = (int)Math.sqrt(i));
        this.pM = paramM.iPenM;
        this.pA = paramM.iAlpha;
        this.pS = paramM.iSize;
      }
    }

    public int getPixel(int paramInt1, int paramInt2)
    {
      int i = M.this.info.imW;
      if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= i) || (paramInt2 >= M.this.info.imH))
        return 0;
      int j = M.this.info.Q;
      M.this.mkLPic(this.buffer, paramInt1, paramInt2, 1, 1, j);
      LO[] arrayOfLO = M.this.info.layers;
      paramInt1 *= j;
      paramInt2 *= j;
      float f = 0.0F;
      for (int k = M.this.info.m.iLayer; k >= 0; k--)
      {
        f += (1.0F - f) * M.b255[(arrayOfLO[k].getPixel(paramInt1, paramInt2) >>> 24)] * arrayOfLO[k].iAlpha;
        if (f >= 1.0F)
          break;
      }
      return ((int)(f * 255.0F) << 24) + (this.buffer[0] & 0xFFFFFF);
    }

    public int[] getBuffer()
    {
      return this.buffer;
    }

    public long getRect()
    {
      return (this.X <= 0 ? 0 : this.X) << 48 | (this.Y <= 0 ? 0 : this.Y) << 32 | this.X2 << 16 | this.Y2;
    }

    public void setRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      this.X = paramInt1;
      this.Y = paramInt2;
      this.X2 = paramInt3;
      this.Y2 = paramInt4;
    }

    public final void addRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      setRect(Math.min(paramInt1, this.X), Math.min(paramInt2, this.Y), Math.max(paramInt3, this.X2), Math.max(paramInt4, this.Y2));
    }

    public Image mkImage(int paramInt1, int paramInt2)
    {
      this.raster.newPixels(this.image, this.buffer, paramInt1, paramInt2);
      return this.image;
    }
  }

  public class Info
  {
    private ByteStream workOut = new ByteStream();
    public boolean isLEdit = false;
    public boolean isFill = false;
    public boolean isClean = false;
    public long permission = -1L;
    public long unpermission = 0L;
    private Res cnf;
    private String dirTT = null;
    public Graphics g = null;
    private int vWidth;
    private int vHeight;
    private Dimension vD = new Dimension();
    private Component component = null;
    public int Q = 1;
    public int L;
    public LO[] layers = null;
    public int scale = 1;
    public int scaleX = 0;
    public int scaleY = 0;
    private byte[] iMOffs;
    public int imH;
    public int imW;
    public int W;
    public int H;
    private int[][][] bPen = new int[16];
    private float[][] bTT = new float[14];
    public M m = new M();

    public Info()
    {
    }

    public void setSize(int paramInt1, int paramInt2, int paramInt3)
    {
      int i = paramInt1 * paramInt3;
      int j = paramInt2 * paramInt3;
      if ((i != this.W) || (j != this.H))
        for (k = 0; k < this.L; k++)
          this.layers[k].setSize(i, j);
      this.imW = paramInt1;
      this.imH = paramInt2;
      this.W = i;
      this.H = j;
      this.Q = paramInt3;
      int k = this.W * this.H;
      if ((this.iMOffs == null) || (this.iMOffs.length < k))
        this.iMOffs = new byte[k];
    }

    public void setLayers(LO[] paramArrayOfLO)
    {
      this.L = paramArrayOfLO.length;
      this.layers = paramArrayOfLO;
    }

    public void setComponent(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
    {
      this.component = paramComponent;
      this.vWidth = paramInt1;
      this.vHeight = paramInt2;
      this.g = paramGraphics;
    }

    public void setL(int paramInt)
    {
      int i = this.layers == null ? 0 : this.layers.length;
      int j = Math.min(i, paramInt);
      if (i != paramInt)
      {
        LO[] arrayOfLO = new LO[paramInt];
        if (this.layers != null)
          System.arraycopy(this.layers, 0, arrayOfLO, 0, j);
        for (int k = 0; k < paramInt; k++)
        {
          if (arrayOfLO[k] != null)
            continue;
          arrayOfLO[k] = LO.getLO(this.W, this.H);
        }
        this.layers = arrayOfLO;
      }
      this.L = paramInt;
    }

    public void delL(int paramInt)
    {
      int i = this.layers.length;
      if (paramInt >= i)
        return;
      LO[] arrayOfLO = new LO[i - 1];
      int j = 0;
      for (int k = 0; k < i; k++)
      {
        if (k == paramInt)
          continue;
        arrayOfLO[(j++)] = this.layers[k];
      }
      this.layers = arrayOfLO;
      this.L = (i - 1);
    }

    public void swapL(int paramInt1, int paramInt2)
    {
      int i = Math.max(paramInt1, paramInt2);
      if (i >= this.L)
        setL(i);
      this.layers[paramInt1].isDraw = true;
      this.layers[paramInt2].isDraw = true;
      this.layers[paramInt1].swap(this.layers[paramInt2]);
    }

    public boolean addScale(int paramInt, boolean paramBoolean)
    {
      if (paramBoolean)
      {
        if (paramInt <= 0)
        {
          this.scale = 1;
          setQuality(1 - paramInt);
        }
        else
        {
          setQuality(1);
          this.scale = paramInt;
        }
        return true;
      }
      int i = this.scale + paramInt;
      if (i > 32)
        return false;
      if (i <= 0)
      {
        this.scale = 1;
        setQuality(this.Q + 1 - i);
      }
      else if (this.Q >= 2)
      {
        setQuality(this.Q - 1);
      }
      else
      {
        setQuality(1);
        this.scale = i;
      }
      return true;
    }

    public void setQuality(int paramInt)
    {
      this.Q = paramInt;
      this.imW = (this.W / this.Q);
      this.imH = (this.H / this.Q);
    }

    public Dimension getSize()
    {
      this.vD.setSize(this.vWidth, this.vHeight);
      return this.vD;
    }

    private void center(Point paramPoint)
    {
      paramPoint.x = (paramPoint.x / this.scale + this.scaleX);
      paramPoint.y = (paramPoint.y / this.scale + this.scaleY);
    }

    public int[][][] getPenMask()
    {
      return this.bPen;
    }

    public int getPenSize(M paramM)
    {
      return (int)Math.sqrt(this.bPen[paramM.iPenM][paramM.iSize].length);
    }

    public int getPMMax()
    {
      return (this.m.isText()) || ((this.m.iHint >= 3) && (this.m.iHint <= 6)) ? 255 : this.bPen[this.m.iPenM].length;
    }

    public float[] getTT(int paramInt)
    {
      paramInt -= 12;
      if (this.bTT[paramInt] == null)
      {
        if (this.dirTT != null)
        {
          localObject = this.dirTT;
          this.dirTT = null;
          try
          {
            this.cnf.loadZip((String)localObject);
          }
          catch (IOException localIOException)
          {
            localIOException.printStackTrace();
          }
        }
        Object localObject = M.this.loadIm("tt/" + paramInt + ".gif", false);
        if (localObject == null)
          return null;
        int i = localObject.length;
        float[] arrayOfFloat = new float[i];
        for (int j = 0; j < i; j++)
          arrayOfFloat[j] = M.access$0()[localObject[j]];
        this.bTT[paramInt] = arrayOfFloat;
      }
      return (F)this.bTT[paramInt];
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.M
 * JD-Core Version:    0.6.0
 */