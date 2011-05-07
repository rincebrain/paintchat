package syi.jpeg;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class SJpegEncoder
{
  OutputStream OUT;
  private int[] i_off;
  private int width;
  private int height;
  private int HV;
  private double[][] mCosT = new double[8][8];
  private int[] mOldDC;
  private double kSqrt2 = 1.41421356D;
  private double kDisSqrt2 = 1.0D / this.kSqrt2;
  private double kPaiDiv16 = 0.1963495408493621D;
  private int bitSeek = 7;
  private int bitValue = 0;
  private int[] kYQuantumT = new int[64];
  private int[] kCQuantumT = new int[64];
  private final byte[] kYDcSizeT = { 2, 3, 3, 3, 3, 3, 4, 5, 6, 7, 8, 9 };
  private final short[] kYDcCodeT = { 0, 2, 3, 4, 5, 6, 14, 30, 62, 126, 254, 510 };
  private final byte[] kCDcSizeT = { 2, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
  private final short[] kCDcCodeT = { 0, 1, 2, 6, 14, 30, 62, 126, 254, 510, 1022, 2046 };
  private final byte[] kYAcSizeT = { 4, 2, 2, 3, 4, 5, 7, 8, 10, 16, 16, 4, 5, 7, 9, 11, 16, 16, 16, 16, 16, 5, 8, 10, 12, 16, 16, 16, 16, 16, 16, 6, 9, 12, 16, 16, 16, 16, 16, 16, 16, 6, 10, 16, 16, 16, 16, 16, 16, 16, 16, 7, 11, 16, 16, 16, 16, 16, 16, 16, 16, 7, 12, 16, 16, 16, 16, 16, 16, 16, 16, 8, 12, 16, 16, 16, 16, 16, 16, 16, 16, 9, 15, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 10, 16, 16, 16, 16, 16, 16, 16, 16, 16, 10, 16, 16, 16, 16, 16, 16, 16, 16, 16, 11, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 11, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16 };
  private final short[] kYAcCodeT = { 10, 0, 1, 4, 11, 26, 120, 248, 1014, -126, -125, 12, 27, 121, 502, 2038, -124, -123, -122, -121, -120, 28, 249, 1015, 4084, -119, -118, -117, -116, -115, -114, 58, 503, 4085, -113, -112, -111, -110, -109, -108, -107, 59, 1016, -106, -105, -104, -103, -102, -101, -100, -99, 122, 2039, -98, -97, -96, -95, -94, -93, -92, -91, 123, 4086, -90, -89, -88, -87, -86, -85, -84, -83, 250, 4087, -82, -81, -80, -79, -78, -77, -76, -75, 504, 32704, -74, -73, -72, -71, -70, -69, -68, -67, 505, -66, -65, -64, -63, -62, -61, -60, -59, -58, 506, -57, -56, -55, -54, -53, -52, -51, -50, -49, 1017, -48, -47, -46, -45, -44, -43, -42, -41, -40, 1018, -39, -38, -37, -36, -35, -34, -33, -32, -31, 2040, -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, 2041, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2 };
  private int kYEOBidx = 0;
  private int kYZRLidx = 151;
  private final byte[] kCAcSizeT = { 2, 2, 3, 4, 5, 5, 6, 7, 9, 10, 12, 4, 6, 8, 9, 11, 12, 16, 16, 16, 16, 5, 8, 10, 12, 15, 16, 16, 16, 16, 16, 5, 8, 10, 12, 16, 16, 16, 16, 16, 16, 6, 9, 16, 16, 16, 16, 16, 16, 16, 16, 6, 10, 16, 16, 16, 16, 16, 16, 16, 16, 7, 11, 16, 16, 16, 16, 16, 16, 16, 16, 7, 11, 16, 16, 16, 16, 16, 16, 16, 16, 8, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 11, 16, 16, 16, 16, 16, 16, 16, 16, 16, 14, 16, 16, 16, 16, 16, 16, 16, 16, 16, 10, 15, 16, 16, 16, 16, 16, 16, 16, 16, 16 };
  private final short[] kCAcCodeT = { 0, 1, 4, 10, 24, 25, 56, 120, 500, 1014, 4084, 11, 57, 246, 501, 2038, 4085, -120, -119, -118, -117, 26, 247, 1015, 4086, 32706, -116, -115, -114, -113, -112, 27, 248, 1016, 4087, -111, -110, -109, -108, -107, -106, 58, 502, -105, -104, -103, -102, -101, -100, -99, -98, 59, 1017, -97, -96, -95, -94, -93, -92, -91, -90, 121, 2039, -89, -88, -87, -86, -85, -84, -83, -82, 122, 2040, -81, -80, -79, -78, -77, -76, -75, -74, 249, -73, -72, -71, -70, -69, -68, -67, -66, -65, 503, -64, -63, -62, -61, -60, -59, -58, -57, -56, 504, -55, -54, -53, -52, -51, -50, -49, -48, -47, 505, -46, -45, -44, -43, -42, -41, -40, -39, -38, 506, -37, -36, -35, -34, -33, -32, -31, -30, -29, 2041, -28, -27, -26, -25, -24, -23, -22, -21, -20, 16352, -19, -18, -17, -16, -15, -14, -13, -12, -11, 1018, 32707, -10, -9, -8, -7, -6, -5, -4, -3, -2 };
  private int kCEOBidx = 0;
  private int kCZRLidx = 151;
  private final byte[] kZigzag = { 0, 1, 8, 16, 9, 2, 3, 10, 17, 24, 32, 25, 18, 11, 4, 5, 12, 19, 26, 33, 40, 48, 41, 34, 27, 20, 13, 6, 7, 14, 21, 28, 35, 42, 49, 56, 57, 50, 43, 36, 29, 22, 15, 23, 30, 37, 44, 51, 58, 59, 52, 45, 38, 31, 39, 46, 53, 60, 61, 54, 47, 55, 62, 63 };

  public SJpegEncoder()
  {
    for (int i = 0; i < 8; i++)
      for (int j = 0; j < 8; j++)
        this.mCosT[i][j] = Math.cos((2 * j + 1) * i * this.kPaiDiv16);
  }

  public void encode(OutputStream paramOutputStream, int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      int i = 65496;
      int j = 65497;
      this.OUT = paramOutputStream;
      this.mOldDC = new int[3];
      q(paramInt3);
      this.i_off = paramArrayOfInt;
      this.width = paramInt1;
      this.height = paramInt2;
      wShort(i);
      mAPP0();
      mComment();
      mDQT();
      mDHT();
      mSOF();
      mSOS();
      mMCU();
      wShort(j);
      this.OUT.flush();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void getYCC(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int k = 0;
    int m = 0;
    int n = paramInt3 * 8;
    int i1 = paramInt4 * 8;
    int i2 = paramInt1 + n;
    int i3 = paramInt2 + i1;
    int[] arrayOfInt1 = new int[3];
    int i;
    for (int i4 = paramInt2; i4 < i3; i4++)
    {
      if (i4 >= this.height)
        try
        {
          for (i5 = k; i5 < paramArrayOfInt2.length; i5++)
            paramArrayOfInt2[i5] = paramArrayOfInt2[(i5 - n)];
        }
        catch (RuntimeException localRuntimeException)
        {
        }
      int j = i4 * this.width + paramInt1;
      for (int i5 = paramInt1; i5 < i2; i5++)
      {
        i = i5 >= this.width ? m : this.i_off[j];
        m = i;
        paramArrayOfInt2[(k++)] = i;
        j++;
      }
    }
    int[] arrayOfInt2 = new int[3];
    k = 0;
    int i6 = 0;
    while (i6 < i1)
    {
      int i7 = 0;
      while (i7 < n)
      {
        i4 = 0;
        arrayOfInt2[0] = 0;
        arrayOfInt2[1] = 0;
        arrayOfInt2[2] = 0;
        for (int i8 = 0; i8 < paramInt4; i8++)
          for (int i9 = 0; i9 < paramInt3; i9++)
          {
            i = paramArrayOfInt2[((i6 + i8) * n + (i7 + i9))];
            for (int i10 = 0; i10 < 3; i10++)
              arrayOfInt2[i10] += (i >> i10 * 8 & 0xFF);
            i4++;
          }
        for (i8 = 0; i8 < 3; i8++)
          arrayOfInt2[i8] = Math.min(Math.max(arrayOfInt2[i8] / i4, 0), 255);
        paramArrayOfInt1[(k++)] = (arrayOfInt2[2] << 16 | arrayOfInt2[1] << 8 | arrayOfInt2[0]);
        i7 += paramInt3;
      }
      i6 += paramInt4;
    }
    for (i6 = 0; i6 < 64; i6++)
    {
      ycc(arrayOfInt1, paramArrayOfInt1[i6]);
      paramArrayOfInt1[i6] = arrayOfInt1[paramInt5];
    }
  }

  private void mAPP0()
    throws IOException
  {
    int i = 65504;
    byte[] arrayOfByte = "".getBytes();
    wShort(i);
    wShort(arrayOfByte.length + 11);
    wArray(arrayOfByte);
    w(1);
    w(2);
    w(1);
    wShort(72);
    wShort(72);
    w(0);
    w(0);
  }

  private final void mComment()
    throws IOException
  {
    int i = 65534;
    String str = "(C)shi-chan 2001";
    byte[] arrayOfByte = (str + '\000').getBytes();
    wShort(i);
    wShort(arrayOfByte.length + 2);
    wArray(arrayOfByte);
    System.out.println(str);
  }

  private void mDHT()
    throws IOException
  {
    int i = 65476;
    byte[] arrayOfByte1 = { 0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
    byte[] arrayOfByte2 = { 1, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
    byte[] arrayOfByte3 = { 16, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125, 1, 2, 3, 0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 81, 97, 7, 34, 113, 20, 50, -127, -111, -95, 8, 35, 66, -79, -63, 21, 82, -47, -16, 36, 51, 98, 114, -126, 9, 10, 22, 23, 24, 25, 26, 37, 38, 39, 40, 41, 42, 52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6 };
    byte[] arrayOfByte4 = { 17, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119, 0, 1, 2, 3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 7, 97, 113, 19, 34, 50, -127, 8, 20, 66, -111, -95, -79, -63, 9, 35, 51, 82, -16, 21, 98, 114, -47, 10, 22, 36, 52, -31, 37, -15, 23, 24, 25, 26, 38, 39, 40, 41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -126, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -30, -29, -28, -27, -26, -25, -24, -23, -22, -14, -13, -12, -11, -10, -9, -8, -7, -6 };
    wShort(i);
    wShort(arrayOfByte1.length + arrayOfByte3.length + arrayOfByte2.length + arrayOfByte4.length + 2);
    wArray(arrayOfByte1);
    wArray(arrayOfByte3);
    wArray(arrayOfByte2);
    wArray(arrayOfByte4);
  }

  private void mDQT()
    throws IOException
  {
    int i = 65499;
    wShort(i);
    wShort(132);
    w(0);
    for (int j = 0; j < 64; j++)
      w(this.kYQuantumT[this.kZigzag[j]] & 0xFF);
    w(1);
    for (j = 0; j < 64; j++)
      w(this.kCQuantumT[this.kZigzag[j]] & 0xFF);
  }

  private void mMCU()
    throws IOException
  {
    try
    {
      int i = this.HV;
      int j = 8 * i;
      int[] arrayOfInt1 = new int[64];
      int[] arrayOfInt2 = new int[64];
      int[] arrayOfInt3 = new int[j * j];
      int k = 0;
      while (k < this.height)
      {
        int m = 0;
        while (m < this.width)
        {
          for (int n = 0; n < i; n++)
            for (int i1 = 0; i1 < i; i1++)
            {
              getYCC(arrayOfInt1, arrayOfInt3, m + 8 * i1, k + 8 * n, 1, 1, 0);
              tDCT(arrayOfInt1, arrayOfInt2);
              tQuantization(arrayOfInt2, 0);
              tHuffman(arrayOfInt2, 0);
            }
          getYCC(arrayOfInt1, arrayOfInt3, m, k, this.HV, this.HV, 1);
          tDCT(arrayOfInt1, arrayOfInt2);
          tQuantization(arrayOfInt2, 1);
          tHuffman(arrayOfInt2, 1);
          getYCC(arrayOfInt1, arrayOfInt3, m, k, this.HV, this.HV, 2);
          tDCT(arrayOfInt1, arrayOfInt2);
          tQuantization(arrayOfInt2, 2);
          tHuffman(arrayOfInt2, 2);
          m += j;
        }
        k += j;
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void mSOF()
    throws IOException
  {
    int i = 65472;
    wShort(i);
    wShort(17);
    w(8);
    wShort(this.height);
    wShort(this.width);
    w(3);
    for (int k = 0; k < 3; k++)
    {
      w(k);
      int j = k == 0 ? this.HV : 1;
      w(j << 4 | j);
      w(k == 0 ? 0 : 1);
    }
  }

  private void mSOS()
    throws IOException
  {
    int i = 65498;
    wShort(i);
    wShort(12);
    w(3);
    for (int j = 0; j < 3; j++)
    {
      w(j);
      w(j == 0 ? 0 : 17);
    }
    w(0);
    w(63);
    w(0);
  }

  private void q(int paramInt)
  {
    byte[] arrayOfByte1 = { 16, 11, 10, 16, 24, 40, 51, 61, 12, 12, 14, 19, 26, 58, 60, 55, 14, 13, 16, 24, 40, 57, 69, 56, 14, 17, 22, 29, 51, 87, 80, 62, 18, 22, 37, 56, 68, 109, 103, 77, 24, 35, 55, 64, 81, 104, 113, 92, 49, 64, 78, 87, 103, 121, 120, 101, 72, 92, 95, 98, 112, 100, 103, 99 };
    byte[] arrayOfByte2 = { 17, 18, 24, 47, 99, 99, 99, 99, 18, 21, 26, 66, 99, 99, 99, 99, 24, 26, 56, 99, 99, 99, 99, 99, 47, 66, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99 };
    paramInt = paramInt > 100 ? 100 : paramInt < 1 ? 1 : paramInt;
    this.HV = 1;
    if (paramInt >= 25)
      this.HV = 2;
    float f1 = paramInt / 50.0F;
    float f2 = paramInt / 50.0F;
    for (int i = 0; i < 64; i++)
    {
      this.kYQuantumT[i] = Math.min(Math.max((int)(arrayOfByte1[i] * f1), 1), 127);
      this.kCQuantumT[i] = Math.min(Math.max((int)(arrayOfByte2[i] * f2), 1), 127);
    }
  }

  private void tDCT(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    try
    {
      int i = 0;
      int j = 0;
      for (int k = 0; k < 8; k++)
      {
        double d1 = k > 0 ? 1.0D : this.kDisSqrt2;
        for (int m = 0; m < 8; m++)
        {
          double d2 = m > 0 ? 1.0D : this.kDisSqrt2;
          double d3 = 0.0D;
          j = 0;
          for (int n = 0; n < 8; n++)
            for (int i1 = 0; i1 < 8; i1++)
              d3 += paramArrayOfInt1[(j++)] * this.mCosT[m][i1] * this.mCosT[k][n];
          paramArrayOfInt2[(i++)] = (int)(d3 * d2 * d1 / 4.0D);
        }
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
  }

  private void tHuffman(int[] paramArrayOfInt, int paramInt)
    throws IOException
  {
    try
    {
      byte[] arrayOfByte2;
      short[] arrayOfShort2;
      byte[] arrayOfByte1;
      short[] arrayOfShort1;
      int i;
      int j;
      if (paramInt == 0)
      {
        arrayOfByte2 = this.kYAcSizeT;
        arrayOfShort2 = this.kYAcCodeT;
        arrayOfByte1 = this.kYDcSizeT;
        arrayOfShort1 = this.kYDcCodeT;
        i = this.kYEOBidx;
        j = this.kYZRLidx;
      }
      else
      {
        arrayOfByte2 = this.kCAcSizeT;
        arrayOfShort2 = this.kCAcCodeT;
        arrayOfByte1 = this.kCDcSizeT;
        arrayOfShort1 = this.kCDcCodeT;
        i = this.kCEOBidx;
        j = this.kCZRLidx;
      }
      int k = paramArrayOfInt[0] - this.mOldDC[paramInt];
      this.mOldDC[paramInt] = paramArrayOfInt[0];
      int m = Math.abs(k);
      for (int n = 0; m > 0; n = (byte)(n + 1))
        m >>= 1;
      wBit(arrayOfShort1[n], arrayOfByte1[n]);
      if (n != 0)
      {
        if (k < 0)
          k--;
        wBit(k, n);
      }
      int i1 = 0;
      for (int i2 = 1; i2 < 64; i2++)
      {
        m = Math.abs(paramArrayOfInt[this.kZigzag[i2]]);
        if (m == 0)
        {
          i1++;
        }
        else
        {
          while (i1 > 15)
          {
            wBit(arrayOfShort2[j], arrayOfByte2[j]);
            i1 -= 16;
          }
          for (n = 0; m > 0; n = (byte)(n + 1))
            m >>= 1;
          int i3 = i1 * 10 + n + (i1 == 15 ? 1 : 0);
          wBit(arrayOfShort2[i3], arrayOfByte2[i3]);
          k = paramArrayOfInt[this.kZigzag[i2]];
          if (k < 0)
            k--;
          wBit(k, n);
          i1 = 0;
        }
      }
      if (i1 > 0)
        wBit(arrayOfShort2[i], arrayOfByte2[i]);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void tQuantization(int[] paramArrayOfInt, int paramInt)
  {
    int[] arrayOfInt = paramInt == 0 ? this.kYQuantumT : this.kCQuantumT;
    for (int i = 0; i < paramArrayOfInt.length; i++)
      paramArrayOfInt[i] /= arrayOfInt[i];
  }

  private void w(int paramInt)
    throws IOException
  {
    wFullBit();
    this.OUT.write(paramInt);
  }

  private void wArray(byte[] paramArrayOfByte)
    throws IOException
  {
    wFullBit();
    for (int i = 0; i < paramArrayOfByte.length; i++)
      w(paramArrayOfByte[i] & 0xFF);
  }

  private void wArray(int[] paramArrayOfInt)
    throws IOException
  {
    wFullBit();
    for (int i = 0; i < paramArrayOfInt.length; i++)
      w(paramArrayOfInt[i] & 0xFF);
  }

  private void wBit(int paramInt, byte paramByte)
    throws IOException
  {
    paramByte = (byte)(paramByte - 1);
    for (int j = paramByte; j >= 0; j = (byte)(j - 1))
    {
      int i = paramInt >>> j & 0x1;
      this.bitValue |= i << this.bitSeek;
      if (--this.bitSeek > -1)
        continue;
      this.OUT.write(this.bitValue);
      if (this.bitValue == 255)
        this.OUT.write(0);
      this.bitValue = 0;
      this.bitSeek = 7;
    }
  }

  private void wFullBit()
    throws IOException
  {
    if (this.bitSeek != 7)
    {
      wBit(255, (byte)(this.bitSeek + 1));
      this.bitValue = 0;
      this.bitSeek = 7;
    }
  }

  private void wShort(int paramInt)
    throws IOException
  {
    wFullBit();
    this.OUT.write(paramInt >>> 8 & 0xFF);
    this.OUT.write(paramInt & 0xFF);
  }

  private void ycc(int[] paramArrayOfInt, int paramInt)
  {
    int i = paramInt >>> 16 & 0xFF;
    int j = paramInt >>> 8 & 0xFF;
    int k = paramInt & 0xFF;
    paramArrayOfInt[0] = (int)(0.299F * i + 0.587F * j + 0.114F * k - 128.0F);
    paramArrayOfInt[1] = (int)(-(0.1687F * i) - 0.3313F * j + 0.5F * k);
    paramArrayOfInt[2] = (int)(0.5F * i - 0.4187F * j - 0.0813F * k);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.jpeg.SJpegEncoder
 * JD-Core Version:    0.6.0
 */