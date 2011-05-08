package syi.png;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import syi.util.ByteStream;

public class SPngEncoder
{

    private final String STR_C = "(C)shi-chan 2001-2003";
    private OutputStream OUT;
    private int width;
    private int height;
    private int i_off[];
    private CRC32 crc;
    private Deflater deflater;
    private byte b[];
    private byte bGet[];
    private int seek;
    private int iF[];
    private int iFNow[];
    private int iFLOld[];
    private int iFL[];
    private ByteStream work;
    private int image_type;
    private byte image_filter;
    private boolean isProgress;

    public SPngEncoder(ByteStream bytestream, ByteStream bytestream1, Deflater deflater1)
    {
        crc = new CRC32();
        bGet = new byte[1025];
        seek = 0;
        iF = new int[3];
        iFNow = new int[3];
        iFLOld = new int[3];
        image_type = 2;
        isProgress = false;
        System.out.println("(C)shi-chan 2001-2003");
        work = bytestream;
        b = bytestream1.getBuffer();
        deflater = deflater1;
    }

    private void bFilter()
        throws IOException
    {
        bW(image_filter);
        zero(iF);
    }

    private void bW(byte byte0)
        throws IOException
    {
        b[seek++] = byte0;
        if(seek >= b.length)
        {
            wCompress();
        }
    }

    public void encode(OutputStream outputstream, int ai[], int i, int j, int k)
    {
        try
        {
            OUT = outputstream;
            width = i;
            height = j;
            i_off = ai;
            image_filter = (byte)k;
            if(image_filter > 1)
            {
                if(iFL == null || iFL.length < width)
                {
                    iFL = new int[width];
                }
                zero(iFL);
                zero(iFLOld);
            }
            outputstream.write(new byte[] {
                -119, 80, 78, 71, 13, 10, 26, 10
            });
            mIHDR();
            mEXt("Title", "Shi-Tools Oekaki Data");
            mEXt("Copyright", "(C)shi-chan 2001-2003");
            mEXt("Software", "Shi-Tools");
            mIDAT();
            mIEND();
            outputstream.flush();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void fencode(ByteStream bytestream, int ai[], int i, int j)
    {
        bytestream.reset();
        int ai1[] = new int[4];
        for(int k = 0; k < 4; k++)
        {
            bytestream.reset();
            encode(bytestream, ai, i, j, k);
            ai1[k] = bytestream.size();
        }

        int l;
        for(l = 0; l < 4; l++)
        {
            int i1;
            for(i1 = l + 1; i1 < 4; i1++)
            {
                if(ai1[l] > ai1[i1])
                {
                    break;
                }
            }

            if(i1 >= 4)
            {
                break;
            }
        }

        if(l != 3)
        {
            bytestream.reset();
            encode(bytestream, ai, i, j, l);
        }
    }

    private void getFPic(int i, int j)
    {
        for(int k = 0; k < 3; k++)
        {
            iFNow[k] = i >>> (k << 3) & 0xff;
        }

        switch(image_filter)
        {
        case 4: // '\004'
        default:
            break;

        case 0: // '\0'
            return;

        case 1: // '\001'
            for(int l = 0; l < 3; l++)
            {
                iF[l] = iFNow[l] - iF[l];
            }

            break;

        case 2: // '\002'
            for(int i1 = 0; i1 < 3; i1++)
            {
                iF[i1] = iFNow[i1] - (iFL[j] >>> (i1 << 3) & 0xff);
            }

            iFL[j] = i;
            break;

        case 3: // '\003'
            for(int j1 = 0; j1 < 3; j1++)
            {
                iF[j1] = iFNow[j1] - (iF[j1] + (iFL[j] >>> (j1 << 3) & 0xff) >>> 1);
            }

            iFL[j] = i;
            break;
        }
        int ai[] = iF;
        iF = iFNow;
        iFNow = ai;
    }

    private void mEXt(String s, String s1)
        throws IOException
    {
        int i = 0x74455874;
        wCh(i);
        wArray(s.getBytes());
        w(0);
        wArray(s1.getBytes());
        wChA();
    }

    private void mIDAT()
        throws IOException
    {
        int i = 0x49444154;
        wCh(i);
        wImage();
        wChA();
    }

    private void mIEND()
        throws IOException
    {
        int i = 0x49454e44;
        wCh(i);
        wChA();
    }

    private void mIHDR()
        throws IOException
    {
        int i = 0x49484452;
        wCh(i);
        wInt(width);
        wInt(height);
        w(8);
        w(2);
        w(0);
        w(0);
        w(isProgress ? 1 : 0);
        wChA();
    }

    public void setInterlace(boolean flag)
    {
        isProgress = flag;
    }

    private void w(int i)
        throws IOException
    {
        work.write(i);
    }

    private void wArray(byte abyte0[])
        throws IOException
    {
        wArray(abyte0, abyte0.length);
    }

    private void wArray(byte abyte0[], int i)
        throws IOException
    {
        if(i > 0)
        {
            work.write(abyte0, 0, i);
        }
    }

    private void wCh(int i)
        throws IOException
    {
        work.reset();
        wInt(i);
    }

    private void wChA()
        throws IOException
    {
        int i = work.size();
        crc.reset();
        crc.update(work.getBuffer(), 0, i);
        wInt((int)crc.getValue());
        i -= 4;
        for(int j = 24; j >= 0; j -= 8)
        {
            OUT.write(i >>> j & 0xff);
        }

        work.writeTo(OUT);
    }

    private void wCompress()
        throws IOException
    {
        if(seek == 0)
        {
            return;
        }
        deflater.setInput(b, 0, seek);
        seek = 0;
        while(!deflater.needsInput()) 
        {
            int i = deflater.deflate(bGet, 0, bGet.length - 1);
            if(i != 0)
            {
                wArray(bGet, i);
            }
        }
    }

    private void wImage()
        throws IOException
    {
        int i = 0;
        deflater.reset();
        seek = 0;
        if(!isProgress)
        {
            for(int i1 = 0; i1 < height; i1++)
            {
                bFilter();
                for(int k = 0; k < width; k++)
                {
                    getFPic(i_off[i++], k);
                    for(int k1 = 2; k1 >= 0; k1--)
                    {
                        bW((byte)iFNow[k1]);
                    }

                }

            }

        } else
        {
            boolean flag = false;
            byte abyte0[][][] = {
                {
                    new byte[1]
                }, {
                    {
                        4
                    }
                }, {
                    {
                        32, 36
                    }
                }, {
                    {
                        2, 6
                    }, {
                        34, 38
                    }
                }, {
                    {
                        16, 18, 20, 22
                    }, {
                        48, 50, 52, 54
                    }
                }, {
                    {
                        1, 3, 5, 7
                    }, {
                        17, 19, 21, 23
                    }, {
                        33, 35, 37, 39
                    }, {
                        49, 51, 53, 55
                    }
                }, {
                    {
                        8, 9, 10, 11, 12, 13, 14, 15
                    }, {
                        24, 25, 26, 27, 28, 29, 30, 31
                    }, {
                        40, 41, 42, 43, 44, 45, 46, 47
                    }, {
                        56, 57, 58, 59, 60, 61, 62, 63
                    }
                }
            };
            for(int k2 = 0; k2 < abyte0.length; k2++)
            {
                zero(iFL);
                zero(iF);
                for(int j1 = 0; j1 < height; j1 += 8)
                {
                    for(int l2 = 0; l2 < abyte0[k2].length; l2++)
                    {
                        boolean flag1 = false;
                        for(int l = 0; l < width; l += 8)
                        {
                            for(int i3 = 0; i3 < abyte0[k2][l2].length; i3++)
                            {
                                byte byte0 = abyte0[k2][l2][i3];
                                int i2 = l + byte0 % 8;
                                int j2 = j1 + byte0 / 8;
                                if(i2 < width && j2 < height)
                                {
                                    if(!flag1)
                                    {
                                        bFilter();
                                        flag1 = true;
                                    }
                                    getFPic(i_off[width * j2 + i2], i2);
                                    for(int l1 = 2; l1 >= 0; l1--)
                                    {
                                        bW((byte)iFNow[l1]);
                                    }

                                }
                            }

                        }

                    }

                }

            }

        }
        wCompress();
        deflater.finish();
        int j;
        for(; !deflater.finished(); wArray(bGet, j))
        {
            j = deflater.deflate(bGet, 0, bGet.length - 1);
        }

    }

    private void wInt(int i)
        throws IOException
    {
        for(int j = 24; j >= 0; j -= 8)
        {
            w(i >>> j & 0xff);
        }

    }

    private void zero(int ai[])
    {
        if(ai == null)
        {
            return;
        }
        for(int i = 0; i < ai.length; i++)
        {
            ai[i] = 0;
        }

    }
}
