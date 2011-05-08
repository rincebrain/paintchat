package syi.jpeg;

import java.io.*;

public class SJpegEncoder
{

    OutputStream OUT;
    private int i_off[];
    private int width;
    private int height;
    private int HV;
    private double mCosT[][];
    private int mOldDC[];
    private double kSqrt2;
    private double kDisSqrt2;
    private double kPaiDiv16;
    private int bitSeek;
    private int bitValue;
    private int kYQuantumT[];
    private int kCQuantumT[];
    private final byte kYDcSizeT[] = {
        2, 3, 3, 3, 3, 3, 4, 5, 6, 7, 
        8, 9
    };
    private final short kYDcCodeT[] = {
        0, 2, 3, 4, 5, 6, 14, 30, 62, 126, 
        254, 510
    };
    private final byte kCDcSizeT[] = {
        2, 2, 2, 3, 4, 5, 6, 7, 8, 9, 
        10, 11
    };
    private final short kCDcCodeT[] = {
        0, 1, 2, 6, 14, 30, 62, 126, 254, 510, 
        1022, 2046
    };
    private final byte kYAcSizeT[] = {
        4, 2, 2, 3, 4, 5, 7, 8, 10, 16, 
        16, 4, 5, 7, 9, 11, 16, 16, 16, 16, 
        16, 5, 8, 10, 12, 16, 16, 16, 16, 16, 
        16, 6, 9, 12, 16, 16, 16, 16, 16, 16, 
        16, 6, 10, 16, 16, 16, 16, 16, 16, 16, 
        16, 7, 11, 16, 16, 16, 16, 16, 16, 16, 
        16, 7, 12, 16, 16, 16, 16, 16, 16, 16, 
        16, 8, 12, 16, 16, 16, 16, 16, 16, 16, 
        16, 9, 15, 16, 16, 16, 16, 16, 16, 16, 
        16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 10, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 10, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 11, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 11, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 16
    };
    private final short kYAcCodeT[] = {
        10, 0, 1, 4, 11, 26, 120, 248, 1014, -126, 
        -125, 12, 27, 121, 502, 2038, -124, -123, -122, -121, 
        -120, 28, 249, 1015, 4084, -119, -118, -117, -116, -115, 
        -114, 58, 503, 4085, -113, -112, -111, -110, -109, -108, 
        -107, 59, 1016, -106, -105, -104, -103, -102, -101, -100, 
        -99, 122, 2039, -98, -97, -96, -95, -94, -93, -92, 
        -91, 123, 4086, -90, -89, -88, -87, -86, -85, -84, 
        -83, 250, 4087, -82, -81, -80, -79, -78, -77, -76, 
        -75, 504, 32704, -74, -73, -72, -71, -70, -69, -68, 
        -67, 505, -66, -65, -64, -63, -62, -61, -60, -59, 
        -58, 506, -57, -56, -55, -54, -53, -52, -51, -50, 
        -49, 1017, -48, -47, -46, -45, -44, -43, -42, -41, 
        -40, 1018, -39, -38, -37, -36, -35, -34, -33, -32, 
        -31, 2040, -30, -29, -28, -27, -26, -25, -24, -23, 
        -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, 
        -12, 2041, -11, -10, -9, -8, -7, -6, -5, -4, 
        -3, -2
    };
    private int kYEOBidx;
    private int kYZRLidx;
    private final byte kCAcSizeT[] = {
        2, 2, 3, 4, 5, 5, 6, 7, 9, 10, 
        12, 4, 6, 8, 9, 11, 12, 16, 16, 16, 
        16, 5, 8, 10, 12, 15, 16, 16, 16, 16, 
        16, 5, 8, 10, 12, 16, 16, 16, 16, 16, 
        16, 6, 9, 16, 16, 16, 16, 16, 16, 16, 
        16, 6, 10, 16, 16, 16, 16, 16, 16, 16, 
        16, 7, 11, 16, 16, 16, 16, 16, 16, 16, 
        16, 7, 11, 16, 16, 16, 16, 16, 16, 16, 
        16, 8, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 11, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 14, 16, 16, 16, 16, 16, 16, 16, 16, 
        16, 10, 15, 16, 16, 16, 16, 16, 16, 16, 
        16, 16
    };
    private final short kCAcCodeT[] = {
        0, 1, 4, 10, 24, 25, 56, 120, 500, 1014, 
        4084, 11, 57, 246, 501, 2038, 4085, -120, -119, -118, 
        -117, 26, 247, 1015, 4086, 32706, -116, -115, -114, -113, 
        -112, 27, 248, 1016, 4087, -111, -110, -109, -108, -107, 
        -106, 58, 502, -105, -104, -103, -102, -101, -100, -99, 
        -98, 59, 1017, -97, -96, -95, -94, -93, -92, -91, 
        -90, 121, 2039, -89, -88, -87, -86, -85, -84, -83, 
        -82, 122, 2040, -81, -80, -79, -78, -77, -76, -75, 
        -74, 249, -73, -72, -71, -70, -69, -68, -67, -66, 
        -65, 503, -64, -63, -62, -61, -60, -59, -58, -57, 
        -56, 504, -55, -54, -53, -52, -51, -50, -49, -48, 
        -47, 505, -46, -45, -44, -43, -42, -41, -40, -39, 
        -38, 506, -37, -36, -35, -34, -33, -32, -31, -30, 
        -29, 2041, -28, -27, -26, -25, -24, -23, -22, -21, 
        -20, 16352, -19, -18, -17, -16, -15, -14, -13, -12, 
        -11, 1018, 32707, -10, -9, -8, -7, -6, -5, -4, 
        -3, -2
    };
    private int kCEOBidx;
    private int kCZRLidx;
    private final byte kZigzag[] = {
        0, 1, 8, 16, 9, 2, 3, 10, 17, 24, 
        32, 25, 18, 11, 4, 5, 12, 19, 26, 33, 
        40, 48, 41, 34, 27, 20, 13, 6, 7, 14, 
        21, 28, 35, 42, 49, 56, 57, 50, 43, 36, 
        29, 22, 15, 23, 30, 37, 44, 51, 58, 59, 
        52, 45, 38, 31, 39, 46, 53, 60, 61, 54, 
        47, 55, 62, 63
    };

    public SJpegEncoder()
    {
        bitSeek = 7;
        bitValue = 0;
        kYQuantumT = new int[64];
        kCQuantumT = new int[64];
        kYEOBidx = 0;
        kYZRLidx = 151;
        kCEOBidx = 0;
        kCZRLidx = 151;
        kSqrt2 = 1.4142135600000001D;
        kDisSqrt2 = 1.0D / kSqrt2;
        kPaiDiv16 = 0.19634954084936207D;
        mCosT = new double[8][8];
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                mCosT[i][j] = Math.cos((double)((2 * j + 1) * i) * kPaiDiv16);
            }

        }

    }

    public void encode(OutputStream outputstream, int ai[], int i, int j, int k)
    {
        try
        {
            int l = 65496;
            int i1 = 65497;
            OUT = outputstream;
            mOldDC = new int[3];
            q(k);
            i_off = ai;
            width = i;
            height = j;
            wShort(l);
            mAPP0();
            mComment();
            mDQT();
            mDHT();
            mSOF();
            mSOS();
            mMCU();
            wShort(i1);
            OUT.flush();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void getYCC(int ai[], int ai1[], int i, int j, int k, int l, int i1)
    {
        int i2 = 0;
        int j2 = 0;
        int k2 = k * 8;
        int l2 = l * 8;
        int i3 = i + k2;
        int j3 = j + l2;
        int ai2[] = new int[3];
        for(int k3 = j; k3 < j3; k3++)
        {
            if(k3 >= height)
            {
                try
                {
                    for(int i4 = i2; i4 < ai1.length; i4++)
                    {
                        ai1[i4] = ai1[i4 - k2];
                    }

                }
                catch(RuntimeException _ex) { }
                break;
            }
            int l1 = k3 * width + i;
            for(int j4 = i; j4 < i3; j4++)
            {
                int j1 = j4 < width ? i_off[l1] : j2;
                j2 = j1;
                ai1[i2++] = j1;
                l1++;
            }

        }

        int ai3[] = new int[3];
        i2 = 0;
        for(int k4 = 0; k4 < l2; k4 += l)
        {
            for(int i5 = 0; i5 < k2; i5 += k)
            {
                int l3 = 0;
                ai3[0] = 0;
                ai3[1] = 0;
                ai3[2] = 0;
                for(int j5 = 0; j5 < l; j5++)
                {
                    for(int l5 = 0; l5 < k; l5++)
                    {
                        int k1 = ai1[(k4 + j5) * k2 + (i5 + l5)];
                        for(int i6 = 0; i6 < 3; i6++)
                        {
                            ai3[i6] += k1 >> i6 * 8 & 0xff;
                        }

                        l3++;
                    }

                }

                for(int k5 = 0; k5 < 3; k5++)
                {
                    ai3[k5] = Math.min(Math.max(ai3[k5] / l3, 0), 255);
                }

                ai[i2++] = ai3[2] << 16 | ai3[1] << 8 | ai3[0];
            }

        }

        for(int l4 = 0; l4 < 64; l4++)
        {
            ycc(ai2, ai[l4]);
            ai[l4] = ai2[i1];
        }

    }

    private void mAPP0()
        throws IOException
    {
        int i = 65504;
        byte abyte0[] = "JFIF\0".getBytes();
        wShort(i);
        wShort(abyte0.length + 11);
        wArray(abyte0);
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
        String s = "(C)shi-chan 2001";
        byte abyte0[] = (s + '\0').getBytes();
        wShort(i);
        wShort(abyte0.length + 2);
        wArray(abyte0);
        System.out.println(s);
    }

    private void mDHT()
        throws IOException
    {
        int i = 65476;
        byte abyte0[] = {
            0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 
            0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 
            3, 4, 5, 6, 7, 8, 9, 10, 11
        };
        byte abyte1[] = {
            1, 0, 3, 1, 1, 1, 1, 1, 1, 1, 
            1, 1, 0, 0, 0, 0, 0, 0, 1, 2, 
            3, 4, 5, 6, 7, 8, 9, 10, 11
        };
        byte abyte2[] = {
            16, 0, 2, 1, 3, 3, 2, 4, 3, 5, 
            5, 4, 4, 0, 0, 1, 125, 1, 2, 3, 
            0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 
            81, 97, 7, 34, 113, 20, 50, -127, -111, -95, 
            8, 35, 66, -79, -63, 21, 82, -47, -16, 36, 
            51, 98, 114, -126, 9, 10, 22, 23, 24, 25, 
            26, 37, 38, 39, 40, 41, 42, 52, 53, 54, 
            55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 
            73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 
            99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 
            117, 118, 119, 120, 121, 122, -125, -124, -123, -122, 
            -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, 
            -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, 
            -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, 
            -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, 
            -46, -45, -44, -43, -42, -41, -40, -39, -38, -31, 
            -30, -29, -28, -27, -26, -25, -24, -23, -22, -15, 
            -14, -13, -12, -11, -10, -9, -8, -7, -6
        };
        byte abyte3[] = {
            17, 0, 2, 1, 2, 4, 4, 3, 4, 7, 
            5, 4, 4, 0, 1, 2, 119, 0, 1, 2, 
            3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 
            7, 97, 113, 19, 34, 50, -127, 8, 20, 66, 
            -111, -95, -79, -63, 9, 35, 51, 82, -16, 21, 
            98, 114, -47, 10, 22, 36, 52, -31, 37, -15, 
            23, 24, 25, 26, 38, 39, 40, 41, 42, 53, 
            54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 
            72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 
            90, 99, 100, 101, 102, 103, 104, 105, 106, 115, 
            116, 117, 118, 119, 120, 121, 122, -126, -125, -124, 
            -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, 
            -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, 
            -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, 
            -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, 
            -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, 
            -38, -30, -29, -28, -27, -26, -25, -24, -23, -22, 
            -14, -13, -12, -11, -10, -9, -8, -7, -6
        };
        wShort(i);
        wShort(abyte0.length + abyte2.length + abyte1.length + abyte3.length + 2);
        wArray(abyte0);
        wArray(abyte2);
        wArray(abyte1);
        wArray(abyte3);
    }

    private void mDQT()
        throws IOException
    {
        int i = 65499;
        wShort(i);
        wShort(132);
        w(0);
        for(int j = 0; j < 64; j++)
        {
            w(kYQuantumT[kZigzag[j]] & 0xff);
        }

        w(1);
        for(int k = 0; k < 64; k++)
        {
            w(kCQuantumT[kZigzag[k]] & 0xff);
        }

    }

    private void mMCU()
        throws IOException
    {
        try
        {
            int i = HV;
            int j = 8 * i;
            int ai[] = new int[64];
            int ai1[] = new int[64];
            int ai2[] = new int[j * j];
            for(int k = 0; k < height; k += j)
            {
                for(int l = 0; l < width; l += j)
                {
                    for(int i1 = 0; i1 < i; i1++)
                    {
                        for(int j1 = 0; j1 < i; j1++)
                        {
                            getYCC(ai, ai2, l + 8 * j1, k + 8 * i1, 1, 1, 0);
                            tDCT(ai, ai1);
                            tQuantization(ai1, 0);
                            tHuffman(ai1, 0);
                        }

                    }

                    getYCC(ai, ai2, l, k, HV, HV, 1);
                    tDCT(ai, ai1);
                    tQuantization(ai1, 1);
                    tHuffman(ai1, 1);
                    getYCC(ai, ai2, l, k, HV, HV, 2);
                    tDCT(ai, ai1);
                    tQuantization(ai1, 2);
                    tHuffman(ai1, 2);
                }

            }

        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void mSOF()
        throws IOException
    {
        int i = 65472;
        wShort(i);
        wShort(17);
        w(8);
        wShort(height);
        wShort(width);
        w(3);
        for(int k = 0; k < 3; k++)
        {
            w(k);
            int j = k != 0 ? 1 : HV;
            w(j << 4 | j);
            w(k != 0 ? 1 : 0);
        }

    }

    private void mSOS()
        throws IOException
    {
        int i = 65498;
        wShort(i);
        wShort(12);
        w(3);
        for(int j = 0; j < 3; j++)
        {
            w(j);
            w(j != 0 ? 17 : 0);
        }

        w(0);
        w(63);
        w(0);
    }

    private void q(int i)
    {
        byte abyte0[] = {
            16, 11, 10, 16, 24, 40, 51, 61, 12, 12, 
            14, 19, 26, 58, 60, 55, 14, 13, 16, 24, 
            40, 57, 69, 56, 14, 17, 22, 29, 51, 87, 
            80, 62, 18, 22, 37, 56, 68, 109, 103, 77, 
            24, 35, 55, 64, 81, 104, 113, 92, 49, 64, 
            78, 87, 103, 121, 120, 101, 72, 92, 95, 98, 
            112, 100, 103, 99
        };
        byte abyte1[] = {
            17, 18, 24, 47, 99, 99, 99, 99, 18, 21, 
            26, 66, 99, 99, 99, 99, 24, 26, 56, 99, 
            99, 99, 99, 99, 47, 66, 99, 99, 99, 99, 
            99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 
            99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 
            99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 
            99, 99, 99, 99
        };
        i = i >= 1 ? i <= 100 ? i : 100 : 1;
        HV = 1;
        if(i >= 25)
        {
            HV = 2;
        }
        float f = (float)i / 50F;
        float f1 = (float)i / 50F;
        for(int j = 0; j < 64; j++)
        {
            kYQuantumT[j] = Math.min(Math.max((int)((float)abyte0[j] * f), 1), 127);
            kCQuantumT[j] = Math.min(Math.max((int)((float)abyte1[j] * f1), 1), 127);
        }

    }

    private void tDCT(int ai[], int ai1[])
    {
        try
        {
            int i = 0;
            boolean flag = false;
            for(int k = 0; k < 8; k++)
            {
                double d = k <= 0 ? kDisSqrt2 : 1.0D;
                for(int l = 0; l < 8; l++)
                {
                    double d1 = l <= 0 ? kDisSqrt2 : 1.0D;
                    double d2 = 0.0D;
                    int j = 0;
                    for(int i1 = 0; i1 < 8; i1++)
                    {
                        for(int j1 = 0; j1 < 8; j1++)
                        {
                            d2 += (double)ai[j++] * mCosT[l][j1] * mCosT[k][i1];
                        }

                    }

                    ai1[i++] = (int)((d2 * d1 * d) / 4D);
                }

            }

        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
        }
    }

    private void tHuffman(int ai[], int i)
        throws IOException
    {
        try
        {
            byte abyte0[];
            byte abyte1[];
            short aword0[];
            short aword1[];
            int j;
            int k;
            if(i == 0)
            {
                abyte1 = kYAcSizeT;
                aword1 = kYAcCodeT;
                abyte0 = kYDcSizeT;
                aword0 = kYDcCodeT;
                j = kYEOBidx;
                k = kYZRLidx;
            } else
            {
                abyte1 = kCAcSizeT;
                aword1 = kCAcCodeT;
                abyte0 = kCDcSizeT;
                aword0 = kCDcCodeT;
                j = kCEOBidx;
                k = kCZRLidx;
            }
            int l = ai[0] - mOldDC[i];
            mOldDC[i] = ai[0];
            int j1 = Math.abs(l);
            byte byte0;
            for(byte0 = 0; j1 > 0; byte0++)
            {
                j1 >>= 1;
            }

            wBit(aword0[byte0], abyte0[byte0]);
            if(byte0 != 0)
            {
                if(l < 0)
                {
                    l--;
                }
                wBit(l, byte0);
            }
            int l1 = 0;
            for(int i2 = 1; i2 < 64; i2++)
            {
                int k1 = Math.abs(ai[kZigzag[i2]]);
                if(k1 == 0)
                {
                    l1++;
                } else
                {
                    for(; l1 > 15; l1 -= 16)
                    {
                        wBit(aword1[k], abyte1[k]);
                    }

                    byte byte1;
                    for(byte1 = 0; k1 > 0; byte1++)
                    {
                        k1 >>= 1;
                    }

                    int j2 = l1 * 10 + byte1 + (l1 != 15 ? 0 : 1);
                    wBit(aword1[j2], abyte1[j2]);
                    int i1 = ai[kZigzag[i2]];
                    if(i1 < 0)
                    {
                        i1--;
                    }
                    wBit(i1, byte1);
                    l1 = 0;
                }
            }

            if(l1 > 0)
            {
                wBit(aword1[j], abyte1[j]);
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void tQuantization(int ai[], int i)
    {
        int ai1[] = i != 0 ? kCQuantumT : kYQuantumT;
        for(int j = 0; j < ai.length; j++)
        {
            ai[j] /= ai1[j];
        }

    }

    private void w(int i)
        throws IOException
    {
        wFullBit();
        OUT.write(i);
    }

    private void wArray(byte abyte0[])
        throws IOException
    {
        wFullBit();
        for(int i = 0; i < abyte0.length; i++)
        {
            w(abyte0[i] & 0xff);
        }

    }

    private void wArray(int ai[])
        throws IOException
    {
        wFullBit();
        for(int i = 0; i < ai.length; i++)
        {
            w(ai[i] & 0xff);
        }

    }

    private void wBit(int i, byte byte0)
        throws IOException
    {
        byte0--;
        for(byte byte1 = byte0; byte1 >= 0; byte1--)
        {
            int j = i >>> byte1 & 1;
            bitValue |= j << bitSeek;
            if(--bitSeek <= -1)
            {
                OUT.write(bitValue);
                if(bitValue == 255)
                {
                    OUT.write(0);
                }
                bitValue = 0;
                bitSeek = 7;
            }
        }

    }

    private void wFullBit()
        throws IOException
    {
        if(bitSeek != 7)
        {
            wBit(255, (byte)(bitSeek + 1));
            bitValue = 0;
            bitSeek = 7;
        }
    }

    private void wShort(int i)
        throws IOException
    {
        wFullBit();
        OUT.write(i >>> 8 & 0xff);
        OUT.write(i & 0xff);
    }

    private void ycc(int ai[], int i)
    {
        int j = i >>> 16 & 0xff;
        int k = i >>> 8 & 0xff;
        int l = i & 0xff;
        ai[0] = (int)((0.299F * (float)j + 0.587F * (float)k + 0.114F * (float)l) - 128F);
        ai[1] = (int)((-(0.1687F * (float)j) - 0.3313F * (float)k) + 0.5F * (float)l);
        ai[2] = (int)(0.5F * (float)j - 0.4187F * (float)k - 0.0813F * (float)l);
    }
}
