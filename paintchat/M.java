package paintchat;

import java.applet.Applet;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Hashtable;
import syi.awt.Awt;
import syi.util.ByteStream;

// Referenced classes of package paintchat:
//            LO, Res, SRaster

public class M
{
    public class User
    {

        private Image image;
        private SRaster raster;
        private int buffer[];
        private int argb[];
        public int points[];
        private int ps2[];
        private int p[];
        private int pW;
        private int pM;
        private int pA;
        private int pS;
        private float pV;
        private float pTT[];
        private int pTTW;
        private boolean isDirect;
        public int wait;
        public boolean isPre;
        private int pX[];
        private int pY[];
        private int oX;
        private int oY;
        private float fX;
        private float fY;
        private int iDCount;
        private int X;
        private int Y;
        private int X2;
        private int Y2;
        private int count;
        private int countMax;

        private void setup(M m)
        {
            pV = M.b255[m.info.bPen[m.iPenM].length - 1];
            m.getPM();
            count = 0;
            iDCount = 0;
            oX = -1000;
            oY = -1000;
            isDirect = m.iPen == 3 || m.iHint == 9 || m.isOver;
            if(info.L <= m.iLayer)
            {
                info.setL(m.iLayer + 1);
            }
            info.layers[m.iLayer].isDraw = true;
            if(m.iTT >= 12)
            {
                pTT = info.getTT(m.iTT);
                pTTW = (int)Math.sqrt(pTT.length);
            }
        }

        public void setIm(M m)
        {
            if(m.isText())
            {
                return;
            }
            if(pM != m.iPenM || pA != m.iAlpha || pS != m.iSize)
            {
                int ai[] = m.info.bPen[m.iPenM][m.iSize];
                int i = ai.length;
                if(p == null || p.length < i)
                {
                    p = new int[i];
                }
                float f = M.b255[m.iAlpha];
                for(int j = 0; j < i; j++)
                {
                    float f1 = (float)ai[j] * f;
                    p[j] = f1 > 1.0F || f1 <= 0.0F ? (int)f1 : 1;
                }

                pW = m.iPen = (int)Math.sqrt(i);
                pM = m.iPenM;
                pA = m.iAlpha;
                pS = m.iSize;
            }
        }

        public int getPixel(int i, int j)
        {
            int k = info.imW;
            if(i < 0 || j < 0 || i >= k || j >= info.imH)
            {
                return 0;
            }
            int l = info.Q;
            mkLPic(buffer, i, j, 1, 1, l);
            int _tmp = info.L;
            int _tmp1 = info.m.iLayer;
            LO alo[] = info.layers;
            i *= l;
            j *= l;
            float f = 0.0F;
            for(int i1 = info.m.iLayer; i1 >= 0; i1--)
            {
                f += (1.0F - f) * M.b255[alo[i1].getPixel(i, j) >>> 24] * alo[i1].iAlpha;
                if(f >= 1.0F)
                {
                    break;
                }
            }

            return ((int)(f * 255F) << 24) + (buffer[0] & 0xffffff);
        }

        public int[] getBuffer()
        {
            return buffer;
        }

        public long getRect()
        {
            return (long)(X > 0 ? X : 0) << 48 | (long)(Y > 0 ? Y : 0) << 32 | (long)(X2 << 16) | (long)Y2;
        }

        public void setRect(int i, int j, int k, int l)
        {
            X = i;
            Y = j;
            X2 = k;
            Y2 = l;
        }

        public final void addRect(int i, int j, int k, int l)
        {
            setRect(Math.min(i, X), Math.min(j, Y), Math.max(k, X2), Math.max(l, Y2));
        }

        public Image mkImage(int i, int j)
        {
            raster.newPixels(image, buffer, i, j);
            return image;
        }















































        public User()
        {
            image = null;
            raster = null;
            buffer = new int[0x10000];
            argb = new int[4];
            points = new int[6];
            ps2 = null;
            p = null;
            pM = -1;
            pA = -1;
            pS = -1;
            pV = 1.0F;
            pTT = null;
            wait = 0;
            isPre = false;
            pX = new int[4];
            pY = new int[4];
            count = 0;
        }
    }

    public class Info
    {

        private ByteStream workOut;
        public boolean isLEdit;
        public boolean isFill;
        public boolean isClean;
        public long permission;
        public long unpermission;
        private Res cnf;
        private String dirTT;
        public Graphics g;
        private int vWidth;
        private int vHeight;
        private Dimension vD;
        private Component component;
        public int Q;
        public int L;
        public LO layers[];
        public int scale;
        public int scaleX;
        public int scaleY;
        private byte iMOffs[];
        public int imH;
        public int imW;
        public int W;
        public int H;
        private int bPen[][][];
        private float bTT[][];
        public M m;

        public void setSize(int i, int j, int k)
        {
            int l = i * k;
            int i1 = j * k;
            if(l != W || i1 != H)
            {
                for(int j1 = 0; j1 < L; j1++)
                {
                    layers[j1].setSize(l, i1);
                }

            }
            imW = i;
            imH = j;
            W = l;
            H = i1;
            Q = k;
            int k1 = W * H;
            if(iMOffs == null || iMOffs.length < k1)
            {
                iMOffs = new byte[k1];
            }
        }

        public void setLayers(LO alo[])
        {
            L = alo.length;
            layers = alo;
        }

        public void setComponent(Component component1, Graphics g1, int i, int j)
        {
            component = component1;
            vWidth = i;
            vHeight = j;
            g = g1;
        }

        public void setL(int i)
        {
            int j = layers != null ? layers.length : 0;
            int k = Math.min(j, i);
            if(j != i)
            {
                LO alo[] = new LO[i];
                if(layers != null)
                {
                    System.arraycopy(layers, 0, alo, 0, k);
                }
                for(int l = 0; l < i; l++)
                {
                    if(alo[l] == null)
                    {
                        alo[l] = LO.getLO(W, H);
                    }
                }

                layers = alo;
            }
            L = i;
        }

        public void delL(int i)
        {
            int j = layers.length;
            if(i >= j)
            {
                return;
            }
            LO alo[] = new LO[j - 1];
            int k = 0;
            for(int l = 0; l < j; l++)
            {
                if(l != i)
                {
                    alo[k++] = layers[l];
                }
            }

            layers = alo;
            L = j - 1;
        }

        public void swapL(int i, int j)
        {
            int k = Math.max(i, j);
            if(k >= L)
            {
                setL(k);
            }
            layers[i].isDraw = true;
            layers[j].isDraw = true;
            layers[i].swap(layers[j]);
        }

        public boolean addScale(int i, boolean flag)
        {
            if(flag)
            {
                if(i <= 0)
                {
                    scale = 1;
                    setQuality(1 - i);
                } else
                {
                    setQuality(1);
                    scale = i;
                }
                return true;
            }
            int j = scale + i;
            if(j > 32)
            {
                return false;
            }
            if(j <= 0)
            {
                scale = 1;
                setQuality((Q + 1) - j);
            } else
            if(Q >= 2)
            {
                setQuality(Q - 1);
            } else
            {
                setQuality(1);
                scale = j;
            }
            return true;
        }

        public void setQuality(int i)
        {
            Q = i;
            imW = W / Q;
            imH = H / Q;
        }

        public Dimension getSize()
        {
            vD.setSize(vWidth, vHeight);
            return vD;
        }

        private void center(Point point)
        {
            point.x = point.x / scale + scaleX;
            point.y = point.y / scale + scaleY;
        }

        public int[][][] getPenMask()
        {
            return bPen;
        }

        public int getPenSize(M m1)
        {
            return (int)Math.sqrt(bPen[m1.iPenM][m1.iSize].length);
        }

        public int getPMMax()
        {
            return !m.isText() && (m.iHint < 3 || m.iHint > 6) ? bPen[m.iPenM].length : 255;
        }

        public float[] getTT(int i)
        {
            i -= 12;
            if(bTT[i] == null)
            {
                if(dirTT != null)
                {
                    String s1 = dirTT;
                    dirTT = null;
                    try
                    {
                        cnf.loadZip(s1);
                    }
                    catch(IOException ioexception)
                    {
                        ioexception.printStackTrace();
                    }
                }
                int ai[] = loadIm("tt/" + i + ".gif", false);
                if(ai == null)
                {
                    return null;
                }
                int j = ai.length;
                float af[] = new float[j];
                for(int k = 0; k < j; k++)
                {
                    af[k] = M.b255[ai[k]];
                }

                bTT[i] = af;
            }
            return bTT[i];
        }












        public Info()
        {
            workOut = new ByteStream();
            isLEdit = false;
            isFill = false;
            isClean = false;
            permission = -1L;
            unpermission = 0L;
            dirTT = null;
            g = null;
            vD = new Dimension();
            component = null;
            Q = 1;
            layers = null;
            scale = 1;
            scaleX = 0;
            scaleY = 0;
            bPen = new int[16][][];
            bTT = new float[14][];
            m = new M();
        }
    }


    private Info info;
    private User user;
    public int iHint;
    public int iPen;
    public int iPenM;
    public int iTT;
    public int iColor;
    public int iColorMask;
    public int iAlpha;
    public int iAlpha2;
    public int iSA;
    public int iLayer;
    public int iLayerSrc;
    public int iMask;
    public int iSize;
    public int iSS;
    public int iCount;
    public int iSOB;
    public boolean isAFix;
    public boolean isOver;
    public boolean isCount;
    public boolean isAnti;
    public boolean isAllL;
    public byte strHint[];
    private int iSeek;
    private int iOffset;
    private byte offset[];
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
    private static float b255[] = new float[256];
    static float b255d[] = new float[256];
    private static ColorModel color_model = null;
    private static final M mgDef = new M();

    public M()
    {
        iHint = 0;
        iPen = 0;
        iPenM = 0;
        iTT = 0;
        iColor = 0;
        iColorMask = 0;
        iAlpha = 255;
        iSA = 65280;
        iLayer = 0;
        iLayerSrc = 1;
        iMask = 0;
        iSize = 0;
        iSS = 65280;
        iCount = -8;
        isCount = true;
    }

    public M(Info info1, User user1)
    {
        iHint = 0;
        iPen = 0;
        iPenM = 0;
        iTT = 0;
        iColor = 0;
        iColorMask = 0;
        iAlpha = 255;
        iSA = 65280;
        iLayer = 0;
        iLayerSrc = 1;
        iMask = 0;
        iSize = 0;
        iSS = 65280;
        iCount = -8;
        isCount = true;
        info = info1;
        user = user1;
    }

    private final void copy(int ai[][], int ai1[][])
    {
        for(int i = 0; i < ai1.length; i++)
        {
            System.arraycopy(ai[i], 0, ai1[i], 0, ai1[i].length);
        }

    }

    public final void dBuffer()
    {
        dBuffer(!user.isDirect, user.X, user.Y, user.X2, user.Y2);
    }

    private final void dBuffer(boolean flag, int i, int j, int k, int l)
    {
        try
        {
            int i1 = info.scale;
            int j1 = info.Q;
            int k1 = info.W;
            int l1 = info.H;
            int j2 = info.scaleX;
            int k2 = info.scaleY;
            boolean flag1 = i1 == 1;
            int ai[] = user.buffer;
            Color color = Color.white;
            Graphics g = info.g;
            if(g == null)
            {
                return;
            }
            i /= j1;
            j /= j1;
            k /= j1;
            l /= j1;
            i = i > j2 ? i : j2;
            j = j > k2 ? j : k2;
            int l2 = info.vWidth / i1 + j2;
            k = k <= l2 ? k : l2;
            k = k <= k1 ? k : k1;
            l2 = info.vHeight / i1 + k2;
            l = l <= l2 ? l : l2;
            l = l <= l1 ? l : l1;
            if(k <= i || l <= j)
            {
                return;
            }
            k1 = k - i;
            int i3 = k1 * i1;
            int j3 = (i - j2) * i1;
            int _tmp = i;
            int k3 = j;
            l2 = ai.length / (k1 * j1 * j1);
            do
            {
                int i2 = Math.min(l2, l - k3);
                if(i2 <= 0)
                {
                    break;
                }
                Image image = flag ? mkMPic(i, k3, k1, i2, j1) : mkLPic(null, i, k3, k1, i2, j1);
                if(flag1)
                {
                    g.drawImage(image, j3, k3 - k2, color, null);
                } else
                {
                    g.drawImage(image, j3, (k3 - k2) * i1, i3, i2 * i1, color, null);
                }
                k3 += i2;
            } while(true);
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
        }
    }

    private final void dBz(int ai[])
        throws InterruptedException
    {
        try
        {
            int i = ai[0];
            int k = 0;
            for(int l = 1; l < 4; l++)
            {
                float f2 = ai[l] >> 16;
                float f4 = (short)ai[l];
                float _tmp = (float)(i >> 16);
                float _tmp1 = (float)(short)i;
                k = (int)((double)k + Math.sqrt(f2 * f2 + f4 * f4));
                i = ai[l];
            }

            if(k <= 0)
            {
                return;
            }
            byte byte0 = -100;
            byte byte1 = -100;
            int k1 = -1000;
            int l1 = -1000;
            int i2 = 0;
            boolean flag = isAnti;
            int j2 = user.pW / 2;
            for(int j = k; j > 0; j--)
            {
                float f1 = (float)j / (float)k;
                float f = (float)Math.pow(1.0F - f1, 3D);
                float f3 = f * (float)(ai[3] >> 16);
                float f5 = f * (float)(short)ai[3];
                f = 3F * (1.0F - f1) * (1.0F - f1) * f1;
                f3 += f * (float)(ai[2] >> 16);
                f5 += f * (float)(short)ai[2];
                f = 3F * f1 * f1 * (1.0F - f1);
                f3 += f * (float)(ai[1] >> 16);
                f5 += f * (float)(short)ai[1];
                f = f1 * f1 * f1;
                f3 += f * (float)(ai[0] >> 16);
                f5 += f * (float)(short)ai[0];
                int i1 = (int)f3 + j2;
                int j1 = (int)f5 + j2;
                if(i1 != k1 || j1 != l1)
                {
                    if(flag)
                    {
                        shift(i1, j1);
                        if(++i2 >= 4)
                        {
                            dFLine2(iSize);
                        }
                    } else
                    {
                        dFLine(i1, j1, iSize);
                    }
                    k1 = i1;
                    l1 = j1;
                }
            }

            user.X--;
            user.Y--;
            user.X2 += 2;
            user.Y2 += 2;
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
        }
    }

    public void dClear()
    {
        if(iPen == 14)
        {
            return;
        }
        for(int i = 0; i < info.L; i++)
        {
            if(i >= 64 || (info.unpermission & (long)(1 << i)) == 0L)
            {
                info.layers[i].clear();
            }
        }

        user.isDirect = true;
        setD(0, 0, info.W, info.H);
        if(user.wait >= 0)
        {
            dBuffer();
        }
    }

    private void dFusion(byte abyte0[])
    {
        LO alo[] = info.layers;
        LO lo2 = new LO();
        LO lo4 = new LO();
        int i = info.W;
        int j = abyte0.length / 4;
        int ai[] = user.buffer;
        int k = ai.length / i;
        for(int i1 = 0; i1 < info.H; i1 += k)
        {
            int l = Math.min(info.H - i1, k);
            int _tmp = i * l;
            int j1 = 0;
            LO lo3 = null;
            for(int l1 = 0; l1 < j; l1++)
            {
                LO lo = alo[abyte0[j1++]];
                lo2.setField(lo);
                lo.iAlpha = b255[abyte0[j1++] & 0xff];
                lo.iCopy = abyte0[j1++];
                j1++;
                lo.normalize(lo.iAlpha, 0, i1, i, i1 + l);
                if(lo3 == null)
                {
                    lo3 = lo;
                    lo4.setField(lo2);
                    lo.reserve();
                } else
                {
                    if(lo.iCopy == 1)
                    {
                        memset(ai, 0xffffff);
                        for(int i2 = 0; i2 < l1 - 2; i2++)
                        {
                            alo[i2].draw(ai, 0, i1, i, i1 + l, i);
                        }

                    }
                    lo.dAdd(lo3.offset, 0, i1, i, i1 + l, ai);
                    lo.clear(0, i1, i, i1 + l);
                    lo.setField(lo2);
                }
            }

            if(lo3 != alo[iLayer])
            {
                lo3.copyTo(0, i1, i, i1 + l, alo[iLayer], 0, i1, null);
                lo3.clear(0, i1, i, i1 + l);
            }
        }

        lo2.iAlpha = 1.0F;
        lo2.iCopy = 0;
        lo2.isDraw = true;
        for(int k1 = 0; k1 < j; k1++)
        {
            LO lo1 = alo[abyte0[k1 * 4]];
            lo2.name = lo1.name;
            lo1.setField(lo2);
        }

    }

    private void dCopy(int ai[])
    {
        int _tmp = info.W;
        int _tmp1 = info.H;
        int i = ai[0];
        int j = i >> 16;
        short word0 = (short)i;
        i = ai[1];
        int k = i >> 16;
        short word1 = (short)i;
        i = ai[2];
        int l = i >> 16;
        short word2 = (short)i;
        info.layers[iLayerSrc].copyTo(j, word0, k, word1, info.layers[iLayer], l, word2, user.buffer);
        setD(l, word2, l + (k - j), word2 + (word1 - word0));
    }

    public final void dEnd()
        throws InterruptedException
    {
        if(!user.isDirect)
        {
            dFlush();
        }
        ByteStream bytestream = info.workOut;
        if(bytestream.size() > 0)
        {
            offset = bytestream.writeTo(offset, 0);
            iOffset = bytestream.size();
        }
        if(user.wait == -1)
        {
            dBuffer();
        }
    }

    private void dFill(byte abyte0[], int i, int j, int k, int l)
    {
        byte byte0 = (byte)iAlpha;
        int i1 = info.W;
        try
        {
            int j2 = k - i;
            for(; j < l; j++)
            {
                int j1;
                int k1 = j1 = j * i1 + i;
                int i2;
                for(i2 = 0; i2 < j2; i2++)
                {
                    if(abyte0[j1] == byte0)
                    {
                        break;
                    }
                    j1++;
                }

                for(; i2 < j2; i2++)
                {
                    if(abyte0[j1] != byte0)
                    {
                        break;
                    }
                    j1++;
                }

                k1 = j1;
                if(i2 < j2)
                {
                    for(; i2 < j2; i2++)
                    {
                        if(abyte0[j1] == byte0)
                        {
                            break;
                        }
                        j1++;
                    }

                    int l1 = j1;
                    if(i2 < j2)
                    {
                        for(; k1 < l1; k1++)
                        {
                            abyte0[k1] = byte0;
                        }

                    }
                }
            }

        }
        catch(RuntimeException runtimeexception)
        {
            System.out.println(runtimeexception);
        }
    }

    private void dFill(int ai[], int i, int j, int k, int l)
    {
        int i1 = iAlpha;
        int j1 = info.W;
        try
        {
            int j2 = k - i;
            for(; j < l; j++)
            {
                int k1 = j * j1 + i;
                int k2;
                for(k2 = k1 + j2; k1 < k2; k1++)
                {
                    if(ai[k1] == i1)
                    {
                        break;
                    }
                }

                if(k1 < k2 - 1)
                {
                    for(k1++; k1 < k2; k1++)
                    {
                        if(ai[k1] != i1)
                        {
                            break;
                        }
                    }

                    if(k1 < k2 - 1)
                    {
                        int l1 = k1;
                        for(k1++; k1 < k2; k1++)
                        {
                            if(ai[k1] == i1)
                            {
                                break;
                            }
                        }

                        if(k1 < k2)
                        {
                            for(int i2 = k1; l1 < i2; l1++)
                            {
                                ai[l1] = i1;
                            }

                        }
                    }
                }
            }

        }
        catch(RuntimeException runtimeexception)
        {
            System.out.println(runtimeexception);
        }
    }

    private void dFill(int i, int j)
    {
        int k = info.W;
        int l = info.H;
        byte byte0 = (byte)iAlpha;
        byte abyte0[] = info.iMOffs;
        try
        {
            int ai[] = user.buffer;
            int i1 = 0;
            if(i < 0 || i >= k || j < 0 || j >= l)
            {
                return;
            }
            int l1 = pix(i, j);
            int i2 = iAlpha << 24 | iColor;
            if(l1 == i2)
            {
                return;
            }
            ai[i1++] = s(l1, i, j) << 16 | j;
            while(i1 > 0) 
            {
                int j1 = ai[--i1];
                i = j1 >>> 16;
                j = j1 & 0xffff;
                int k1 = k * j;
                boolean flag = false;
                boolean flag1 = false;
                do
                {
                    abyte0[k1 + i] = byte0;
                    if(j > 0 && pix(i, j - 1) == l1 && abyte0[(k1 - k) + i] == 0)
                    {
                        if(!flag)
                        {
                            flag = true;
                            ai[i1++] = s(l1, i, j - 1) << 16 | j - 1;
                        }
                    } else
                    {
                        flag = false;
                    }
                    if(j < l - 1 && pix(i, j + 1) == l1 && abyte0[k1 + k + i] == 0)
                    {
                        if(!flag1)
                        {
                            flag1 = true;
                            ai[i1++] = s(l1, i, j + 1) << 16 | j + 1;
                        }
                    } else
                    {
                        flag1 = false;
                    }
                    if(i <= 0 || pix(i - 1, j) != l1 || abyte0[(k1 + i) - 1] != 0)
                    {
                        break;
                    }
                    i--;
                } while(true);
            }
        }
        catch(RuntimeException runtimeexception)
        {
            System.out.println(runtimeexception);
        }
        setD(0, 0, k, l);
        t();
    }

    private final void dFLine(float f, float f1, int i)
        throws InterruptedException
    {
        int j = user.wait;
        float f2 = user.fX;
        float f3 = user.fY;
        float f4 = f - f2;
        float f5 = f1 - f3;
        float f6 = Math.max(Math.abs(f4), Math.abs(f5));
        int k = (int)f2;
        int l = (int)f3;
        int i1 = user.oX;
        int j1 = user.oY;
        float f9 = 0.25F;
        if(!isCount)
        {
            user.count = 0;
        }
        int i2 = ss(i);
        int j2 = sa(i);
        int k2 = Math.max(i2, iSize);
        float f10 = iSize;
        float f11 = iAlpha;
        float f12 = f6 != 0.0F ? ((float)i2 - f10) / f6 : 0.0F;
        f12 = f12 < 1.0F ? f12 > -1F ? f12 : -1F : 1.0F;
        float f13 = f6 != 0.0F ? ((float)j2 - f11) / f6 : 0.0F;
        f13 = f13 < 1.0F ? f13 > -1F ? f13 : -1F : 1.0F;
        float f14 = f4 != 0.0F ? f4 / f6 : 0.0F;
        float f15 = f5 != 0.0F ? f5 / f6 : 0.0F;
        float f16 = f2;
        float f17 = f3;
        if(f6 <= 0.0F)
        {
            f6++;
        }
        f14 *= f9;
        f15 *= f9;
        f12 *= f9;
        f13 *= f9;
        int l2 = (int)(f6 / f9);
        for(int i3 = 0; i3 < l2; i3++)
        {
            if(i1 != k || j1 != l)
            {
                user.count--;
                i1 = k;
                j1 = l;
            }
            if(user.count <= 0)
            {
                user.count = user.countMax;
                iSize = (int)f10;
                iAlpha = (int)f11;
                getPM();
                int k1 = k - (user.pW >>> 1);
                int l1 = l - (user.pW >>> 1);
                float f7 = f16 - (float)(int)f16;
                float f8 = f17 - (float)(int)f17;
                if(f7 < 0.0F)
                {
                    k1--;
                    f7++;
                }
                if(f8 < 0.0F)
                {
                    l1--;
                    f8++;
                }
                if(f7 != 1.0F && f8 != 1.0F)
                {
                    dPen(k1, l1, (1.0F - f7) * (1.0F - f8));
                }
                if(f7 != 0.0F)
                {
                    dPen(k1 + 1, l1, f7 * (1.0F - f8));
                }
                if(f8 != 0.0F)
                {
                    dPen(k1, l1 + 1, (1.0F - f7) * f8);
                }
                if(f7 != 0.0F && f8 != 0.0F)
                {
                    dPen(k1 + 1, l1 + 1, f7 * f8);
                }
                if(j > 0)
                {
                    dBuffer(!user.isDirect, k1, l1, k1 + user.pW, l1 + user.pW);
                    if(j > 1)
                    {
                        Thread.currentThread();
                        Thread.sleep(j);
                    }
                }
            }
            k = (int)(f16 += f14);
            l = (int)(f17 += f15);
            f10 += f12;
            f11 += f13;
        }

        user.fX = f16;
        user.fY = f17;
        user.oX = i1;
        user.oY = j1;
        int j3 = (int)Math.sqrt(info.bPen[iPenM][k2].length) / 2;
        int k3 = (int)Math.min(f2, f) - j3;
        int l3 = (int)Math.min(f3, f1) - j3;
        int i4 = (int)Math.max(f2, f) + j3 + info.Q + 1;
        int j4 = (int)Math.max(f3, f1) + j3 + info.Q + 1;
        if(j == 0)
        {
            dBuffer(!user.isDirect, k3, l3, i4, j4);
        }
        addD(k3, l3, i4, j4);
    }

    private final void dFLine(int i, int j, int k)
        throws InterruptedException
    {
        int l = user.wait;
        int i1 = (int)user.fX;
        int j1 = (int)user.fY;
        int k1 = i - i1;
        int l1 = j - j1;
        int i2 = Math.max(Math.abs(k1), Math.abs(l1));
        int j2 = i1;
        int k2 = j1;
        int l2 = user.oX;
        int i3 = user.oY;
        if(!isCount)
        {
            user.count = 0;
        }
        int l3 = ss(k);
        int i4 = sa(k);
        int j4 = Math.max(l3, iSize);
        float f = iSize;
        float f1 = iAlpha;
        float f2 = i2 != 0 ? ((float)l3 - f) / (float)i2 : 0.0F;
        f2 = f2 < 1.0F ? f2 > -1F ? f2 : -1F : 1.0F;
        float f3 = i2 != 0 ? ((float)i4 - f1) / (float)i2 : 0.0F;
        f3 = f3 < 5F ? f3 > -10F ? f3 : -10F : 5F;
        float f4 = k1 != 0 ? (float)k1 / (float)i2 : 0.0F;
        float f5 = l1 != 0 ? (float)l1 / (float)i2 : 0.0F;
        float f6 = i1;
        float f7 = j1;
        if(i2 <= 0)
        {
            i2++;
        }
        for(int k4 = 0; k4 < i2; k4++)
        {
            if(l2 != j2 || i3 != k2)
            {
                user.count--;
                l2 = j2;
                i3 = k2;
                if(user.count <= 0)
                {
                    user.count = user.countMax;
                    iSize = (int)f;
                    iAlpha = (int)f1;
                    getPM();
                    int j3 = j2 - (user.pW >>> 1);
                    int k3 = k2 - (user.pW >>> 1);
                    dPen(j3, k3, 1.0F);
                    if(l > 0)
                    {
                        dBuffer(!user.isDirect, j3, k3, j3 + user.pW, k3 + user.pW);
                        if(l > 1)
                        {
                            Thread.currentThread();
                            Thread.sleep(l);
                        }
                    }
                }
            }
            j2 = (int)(f6 += f4);
            k2 = (int)(f7 += f5);
            f += f2;
            f1 += f3;
        }

        user.fX = f6 - f4;
        user.fY = f7 - f5;
        user.oX = l2;
        user.oY = i3;
        int l4 = (int)Math.sqrt(info.bPen[iPenM][j4].length) / 2;
        int i5 = Math.min(i1, j2) - l4;
        int j5 = Math.min(j1, k2) - l4;
        int k5 = Math.max(i1, j2) + l4 + info.Q;
        int l5 = Math.max(j1, k2) + l4 + info.Q;
        if(l == 0)
        {
            dBuffer(!user.isDirect, i5, j5, k5, l5);
        }
        addD(i5, j5, k5, l5);
    }

    private final void dFLine2(int i)
        throws InterruptedException
    {
        try
        {
            int j = user.pX[0];
            int k = user.pY[0];
            int l = user.pX[1];
            int i1 = user.pY[1];
            int j1 = user.pX[2];
            int k1 = user.pY[2];
            int l1 = user.pX[3];
            int i2 = user.pY[3];
            boolean flag = isAnti;
            float f = user.fX;
            float f1 = user.fY;
            int j3 = (int)f;
            int k3 = (int)f1;
            int l3 = j3;
            int i4 = k3;
            int j4 = user.oX;
            int k4 = user.oY;
            int l4 = user.wait;
            if(!isCount)
            {
                user.count = 0;
            }
            int k5 = 2 * l;
            int l5 = 2 * i1;
            int i6 = ((2 * j - 5 * l) + 4 * j1) - l1;
            int j6 = ((2 * k - 5 * i1) + 4 * k1) - i2;
            int k6 = ((-j + 3 * l) - 3 * j1) + l1;
            int l6 = ((-k + 3 * i1) - 3 * k1) + i2;
            float f11 = iSize;
            float f12 = iAlpha;
            int i7 = ss(i);
            int j7 = sa(i);
            float f13 = (float)(i7 - iSize) * 0.25F;
            f13 = f13 > -1.5F ? f13 < 1.5F ? f13 : 1.5F : -1.5F;
            float f14 = (float)(j7 - iAlpha) * 0.25F;
            int k7 = (int)Math.sqrt(Math.max(info.getPenMask()[iPenM][iSize].length, info.getPenMask()[iPenM][i7].length));
            int l7 = info.Q;
            for(float f17 = 0.0F; f17 < 1.0F; f17 += 0.25F)
            {
                float f7 = f17 * f17;
                float f8 = f7 * f17;
                float f2 = 0.5F * ((float)k5 + (float)(-j + j1) * f17 + (float)i6 * f7 + (float)k6 * f8);
                float f3 = 0.5F * ((float)l5 + (float)(-k + k1) * f17 + (float)j6 * f7 + (float)l6 * f8);
                float f4 = Math.max(Math.abs(f2 - f), Math.abs(f3 - f1));
                if(f4 >= 1.0F)
                {
                    float f5 = ((f2 - f) / f4) * 0.25F;
                    f5 = f5 > -1F ? f5 < 1.0F ? f5 : 1.0F : -1F;
                    float f6 = ((f3 - f1) / f4) * 0.25F;
                    f6 = f6 > -1F ? f6 < 1.0F ? f6 : 1.0F : -1F;
                    int k2 = (int)(f4 / 0.25F);
                    if(k2 < 16)
                    {
                        k2 = 1;
                    }
                    float f15 = f13 / (float)k2;
                    float f16 = f14 / (float)k2;
                    j3 = Math.min(Math.min((int)f, (int)f2), j3);
                    k3 = Math.min(Math.min((int)f1, (int)f3), k3);
                    l3 = Math.max(Math.max((int)f, (int)f2), l3);
                    i4 = Math.max(Math.max((int)f1, (int)f3), i4);
                    for(int j2 = 0; j2 < k2; j2++)
                    {
                        int i5 = (int)f;
                        int j5 = (int)f1;
                        if(j4 != i5 || k4 != j5)
                        {
                            j4 = i5;
                            k4 = j5;
                            user.count--;
                        } else
                        {
                            f11 += f15;
                            f12 += f16;
                        }
                        if(user.count > 0)
                        {
                            f += f5;
                            f1 += f6;
                        } else
                        {
                            iSize = (int)f11;
                            iAlpha = (int)f12;
                            getPM();
                            int l2 = user.pW / 2;
                            i5 -= l2;
                            j5 -= l2;
                            user.count = user.countMax;
                            if(flag)
                            {
                                float f9 = f - (float)(int)f;
                                float f10 = f1 - (float)(int)f1;
                                if(f9 < 0.0F)
                                {
                                    i5--;
                                    f9++;
                                }
                                if(f10 < 0.0F)
                                {
                                    j5--;
                                    f10++;
                                }
                                if(f9 != 1.0F && f10 != 1.0F)
                                {
                                    dPen(i5, j5, (1.0F - f9) * (1.0F - f10));
                                }
                                if(f9 != 0.0F)
                                {
                                    dPen(i5 + 1, j5, f9 * (1.0F - f10));
                                }
                                if(f10 != 0.0F)
                                {
                                    dPen(i5, j5 + 1, (1.0F - f9) * f10);
                                }
                                if(f9 != 0.0F && f10 != 0.0F)
                                {
                                    dPen(i5 + 1, j5 + 1, f9 * f10);
                                }
                            } else
                            {
                                dPen(i5, j5, 1.0F);
                            }
                            if(l4 > 0)
                            {
                                dBuffer(!user.isDirect, i5, j5, i5 + l2 * 2, j5 + l2 * 2);
                                if(l4 > 1)
                                {
                                    Thread.currentThread();
                                    Thread.sleep(l4);
                                }
                            }
                            f += f5;
                            f1 += f6;
                        }
                    }

                }
            }

            user.oX = j4;
            user.oY = k4;
            user.fX = f;
            user.fY = f1;
            int i3 = k7 / 2;
            j3 -= i3;
            k3 -= i3;
            l3 += i3 + 1;
            i4 += i3 + 1;
            addD(j3, k3, l3, i4);
            if(user.wait == 0)
            {
                dBuffer(!user.isDirect, j3, k3, l3 + l7, i4 + l7);
            }
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
        }
    }

    private final void dFlush()
    {
        if(user.isPre)
        {
            return;
        }
        int _tmp = info.Q;
        int j1 = info.W;
        int k1 = info.H;
        int l1 = user.X > 0 ? user.X : 0;
        int i2 = user.Y > 0 ? user.Y : 0;
        int j2 = user.X2 < j1 ? user.X2 : j1;
        int k2 = user.Y2 < k1 ? user.Y2 : k1;
        if(j2 - l1 <= 0 || k2 - i2 <= 0 || iLayer >= info.L)
        {
            return;
        }
        byte abyte0[] = info.iMOffs;
        LO lo = info.layers[iLayer];
        switch(iPen)
        {
        case 17: // '\021'
            lo.dLR(l1, i2, j2, k2);
            dCMask(l1, i2, j2, k2);
            break;

        case 18: // '\022'
            lo.dUD(l1, i2, j2, k2);
            dCMask(l1, i2, j2, k2);
            break;

        case 19: // '\023'
            lo.dR(l1, i2, j2, k2, null);
            dCMask(l1, i2, j2, k2);
            addD(l1, i2, l1 + Math.max(j2 - l1, k2 - i2), i2 + Math.max(j2 - l1, k2 - i2));
            break;

        case 20: // '\024'
            int l2 = iOffset <= 8 ? 0 : ((int) (offset[8]));
            LO lo1 = info.layers[iLayerSrc];
            LO lo2 = lo;
            lo1.normalize(b255[iAlpha2 & 0xff], l1, i2, j2, k2);
            lo2.normalize(b255[iAlpha2 >>> 8], l1, i2, j2, k2);
            if(lo1.offset == null)
            {
                dCMask(l1, i2, j2, k2);
            } else
            {
                lo2.reserve();
                LO lo3 = lo2;
                LO lo4 = lo1;
                if(iLayer < iLayerSrc)
                {
                    lo3 = lo1;
                    lo4 = lo2;
                }
                int _tmp1 = lo3.W;
                LO lo5 = new LO();
                LO lo6 = new LO();
                lo5.setField(lo3);
                lo6.setField(lo4);
                lo3.iCopy = l2;
                lo4.reserve();
                lo3.dAdd(lo4.offset, l1, i2, j2, k2, null);
                if(lo2 != lo4)
                {
                    lo4.copyTo(l1, i2, j2, k2, lo3, l1, i2, null);
                }
                lo1.clear(l1, i2, j2, k2);
                lo1.isDraw = true;
                dCMask(l1, i2, j2, k2);
                lo3.setField(lo5);
                lo4.setField(lo6);
            }
            break;

        case 9: // '\t'
            lo.reserve();
            int ai[] = lo.offset;
            int i3 = iAlpha / 10 + 1;
            l1 = (l1 / i3) * i3;
            i2 = (i2 / i3) * i3;
            int ai2[] = user.argb;
            for(int j3 = i2; j3 < k2; j3 += i3)
            {
                for(int l = l1; l < j2; l += i3)
                {
                    int k4 = Math.min(i3, j1 - l);
                    int l4 = Math.min(i3, k1 - j3);
                    for(int i5 = 0; i5 < 4; i5++)
                    {
                        ai2[i5] = 0;
                    }

                    int k5 = 0;
                    for(int i4 = 0; i4 < l4; i4++)
                    {
                        for(int k3 = 0; k3 < k4; k3++)
                        {
                            int l5 = pix(l + k3, j3 + i4);
                            int i = (j3 + i4) * j1 + l + k3;
                            for(int j5 = 0; j5 < 4; j5++)
                            {
                                ai2[j5] += l5 >>> j5 * 8 & 0xff;
                            }

                            k5++;
                        }

                    }

                    int i6 = ai2[3] << 24 | ai2[2] / k5 << 16 | ai2[1] / k5 << 8 | ai2[0] / k5;
                    for(int j4 = j3; j4 < j3 + l4; j4++)
                    {
                        int j = j1 * j4 + l;
                        for(int l3 = 0; l3 < k4; l3++)
                        {
                            if(abyte0[j] != 0)
                            {
                                abyte0[j] = 0;
                                ai[j] = i6;
                            }
                            j++;
                        }

                    }

                }

            }

            break;

        case 3: // '\003'
            dCMask(l1, i2, j2, k2);
            break;

        default:
            if(iHint == 14 || iHint == 9)
            {
                dCMask(l1, i2, j2, k2);
            } else
            {
                lo.reserve();
                int ai1[] = lo.offset;
                for(; i2 < k2; i2++)
                {
                    int k = i2 * j1 + l1;
                    for(int i1 = l1; i1 < j2; i1++)
                    {
                        ai1[k] = getM(ai1[k], abyte0[k] & 0xff, k);
                        abyte0[k] = 0;
                        k++;
                    }

                }

            }
            break;
        }
        if(user.wait >= 0)
        {
            dBuffer();
        }
    }

    private final void dCMask(int i, int j, int k, int l)
    {
        int i1 = k - i;
        int j1 = info.W;
        int k1 = j * j1 + i;
        byte abyte0[] = info.iMOffs;
        for(int l1 = 0; l1 < i1; l1++)
        {
            abyte0[k1 + l1] = 0;
        }

        j++;
        int i2 = k1;
        k1 += j1;
        for(; j < l; j++)
        {
            System.arraycopy(abyte0, i2, abyte0, k1, i1);
            k1 += j1;
        }

    }

    private final boolean dNext()
        throws InterruptedException
    {
        if(iSeek >= iOffset)
        {
            return false;
        }
        int i = user.pX[3] + rPo();
        int j = user.pY[3] + rPo();
        int k = iSOB == 0 ? 0 : ru();
        shift(i, j);
        user.iDCount++;
        if(iHint != 11)
        {
            if(isAnti)
            {
                dFLine(i, j, k);
            } else
            {
                dFLine(i, j, k);
            }
        } else
        if(user.iDCount >= 2)
        {
            dFLine2(k);
        }
        return true;
    }

    public final void dNext(int i, int j, int k, int l)
        throws InterruptedException, IOException
    {
        int i1 = info.scale;
        int _tmp = user.pW;
        i = (i / i1 + info.scaleX) * info.Q;
        j = (j / i1 + info.scaleY) * info.Q;
        if(Math.abs(i - user.pX[3]) + Math.abs(j - user.pY[3]) < l)
        {
            return;
        }
        wPo(i - user.pX[3]);
        wPo(j - user.pY[3]);
        shift(i, j);
        user.iDCount++;
        if(iSOB != 0)
        {
            info.workOut.write(k);
        }
        if(iHint == 11)
        {
            if(user.iDCount >= 2)
            {
                dFLine2(k);
            }
        } else
        if(isAnti)
        {
            dFLine(i, j, k);
        } else
        {
            dFLine(i, j, k);
        }
    }

    private final void dPen(int i, int j, float f)
    {
        if(iPen == 3)
        {
            if(!user.isPre)
            {
                dPY(i, j);
            }
            return;
        }
        dPenM(i, j, f);
        if(isOver)
        {
            dFlush();
        }
    }

    private final void dPenM(int i, int j, float f)
    {
        boolean flag = false;
        int _tmp = info.Q;
        int ai[] = getPM();
        int i2 = info.W;
        int j2 = user.pW;
        int k2 = j2 * Math.max(-j, 0) + Math.max(-i, 0);
        int l2 = Math.min(i + j2, i2);
        int i3 = Math.min(j + j2, info.H);
        if(l2 <= 0 || i3 <= 0)
        {
            return;
        }
        i = i > 0 ? i : 0;
        j = j > 0 ? j : 0;
        int ai1[] = info.layers[iLayer].offset;
        byte abyte0[] = info.iMOffs;
        for(int l = j; l < i3; l++)
        {
            int i1 = i2 * l + i;
            int j1 = k2;
            k2 += j2;
            for(int k = i; k < l2; k++)
            {
                if(isM(ai1[i1]))
                {
                    i1++;
                    j1++;
                } else
                {
                    int k1 = abyte0[i1] & 0xff;
                    int l1 = ai[j1++];
                    if(l1 == 0)
                    {
                        i1++;
                    } else
                    {
                        switch(iPen)
                        {
                        case 1: // '\001'
                        case 20: // '\024'
                            l1 = Math.max((int)((float)l1 * b255[255 - k1 >>> 1] * f), 1);
                            abyte0[i1++] = (byte)Math.min(k1 + l1, 255);
                            break;

                        case 2: // '\002'
                        case 5: // '\005'
                        case 6: // '\006'
                        case 7: // '\007'
                            if((l1 = (int)((float)l1 * getTT(k, l))) != 0)
                            {
                                abyte0[i1] = (byte)Math.min(k1 + Math.max((int)((float)l1 * b255[255 - k1 >>> 2]), 1), 255);
                            }
                            i1++;
                            break;

                        default:
                            abyte0[i1++] = (byte)Math.max((int)((float)l1 * getTT(k, l)), k1);
                            break;
                        }
                    }
                }
            }

        }

    }

    private final void dPY(int i, int j)
    {
        info.layers[iLayer].reserve();
        boolean flag = false;
        int ai[] = getPM();
        int j2 = info.W;
        int k2 = user.pW;
        int l2 = k2 * Math.max(-j, 0) + Math.max(-i, 0);
        int i3 = l2;
        int j3 = Math.min(i + k2, j2);
        int k3 = Math.min(j + k2, info.H);
        i = i > 0 ? i : 0;
        j = j > 0 ? j : 0;
        if(j3 - i <= 0 || k3 - j <= 0)
        {
            return;
        }
        int ai1[] = info.layers[iLayer].offset;
        int l3 = 0;
        int j5 = 0;
        int k5 = 0;
        int l5 = 0;
        int i6 = 0;
        for(int l6 = j; l6 < k3; l6++)
        {
            int k = j2 * l6 + i;
            int j1 = i3;
            i3 += k2;
            for(int j7 = i; j7 < j3; j7++)
            {
                int l1;
                int j6;
                if((l1 = ai[j1++]) == 0 || isM(j6 = ai1[k++]))
                {
                    k++;
                } else
                {
                    j5 += j6 >>> 24;
                    k5 += j6 >>> 16 & 0xff;
                    l5 += j6 >>> 8 & 0xff;
                    i6 += j6 & 0xff;
                    l3++;
                }
            }

        }

        if(l3 == 0)
        {
            return;
        }
        j5 /= l3;
        k5 /= l3;
        l5 /= l3;
        i6 /= l3;
        if(iAlpha > 0)
        {
            float f1 = b255[iAlpha] / 3F;
            int k7 = iColor >>> 16 & 0xff;
            int i8 = iColor >>> 8 & 0xff;
            int j8 = iColor & 0xff;
            j5 = Math.max((int)((float)j5 + (float)(255 - j5) * f1), 1);
            int l = (int)((float)(k7 - k5) * f1);
            k5 += l == 0 ? ((int) (k7 <= k5 ? ((int) (k7 >= k5 ? 0 : -1)) : 1)) : l;
            l = (int)((float)(i8 - l5) * f1);
            l5 += l == 0 ? ((int) (i8 <= l5 ? ((int) (i8 >= l5 ? 0 : -1)) : 1)) : l;
            l = (int)((float)(j8 - i6) * f1);
            i6 += l == 0 ? ((int) (j8 <= i6 ? ((int) (j8 >= i6 ? 0 : -1)) : 1)) : l;
        }
        i3 = l2;
        for(int i7 = j; i7 < k3; i7++)
        {
            int i1 = j2 * i7 + i;
            int k1 = i3;
            i3 += k2;
            for(int l7 = i; l7 < j3; l7++)
            {
                int i2 = ai[k1++];
                int k6 = ai1[i1];
                float f;
                if(i2 == 0 || isM(k6) || (f = getTT(l7, i7) * b255[i2]) == 0.0F)
                {
                    i1++;
                } else
                {
                    int j4 = k6 >>> 24;
                    int k4 = k6 >>> 16 & 0xff;
                    int i5 = k6 >>> 8 & 0xff;
                    int l4 = k6 & 0xff;
                    int i4 = (int)((float)(j5 - j4) * f);
                    j4 += i4 == 0 ? ((int) (j5 <= j4 ? ((int) (j5 >= j4 ? 0 : -1)) : 1)) : i4;
                    i4 = (int)((float)(k5 - k4) * f);
                    k4 += i4 == 0 ? ((int) (k5 <= k4 ? ((int) (k5 >= k4 ? 0 : -1)) : 1)) : i4;
                    i4 = (int)((float)(l5 - i5) * f);
                    i5 += i4 == 0 ? ((int) (l5 <= i5 ? ((int) (l5 >= i5 ? 0 : -1)) : 1)) : i4;
                    i4 = (int)((float)(i6 - l4) * f);
                    l4 += i4 == 0 ? ((int) (i6 <= l4 ? ((int) (i6 >= l4 ? 0 : -1)) : 1)) : i4;
                    ai1[i1++] = (j4 << 24) + (k4 << 16) + (i5 << 8) + l4;
                }
            }

        }

    }

    public final void draw()
        throws InterruptedException
    {
        try
        {
            if(info == null)
            {
                return;
            }
            iSeek = 0;
            switch(iHint)
            {
            case 0: // '\0'
            case 1: // '\001'
            case 11: // '\013'
                dStart();
                while(dNext()) ;
                break;

            case 10: // '\n'
                dClear();
                break;

            default:
                dRetouch();
                break;
            }
        }
        catch(InterruptedException _ex) { }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        dEnd();
    }

    private void dRect(int i, int j, int k, int l)
    {
        int i1 = info.W;
        int j1 = info.H;
        byte abyte0[] = info.iMOffs;
        int j3 = (byte)iAlpha;
        if(i < 0)
        {
            i = 0;
        }
        if(j < 0)
        {
            j = 0;
        }
        if(k > i1)
        {
            k = i1;
        }
        if(l > j1)
        {
            l = j1;
        }
        if(i >= k || j >= l || j3 == 0)
        {
            return;
        }
        setD(i, j, k, l);
        info.layers[iLayer].reserve();
        int ai[] = info.layers[iLayer].offset;
label0:
        switch(iHint)
        {
        default:
            break;

        case 3: // '\003'
            for(int k3 = j; k3 < l; k3++)
            {
                int k1 = k3 * i1 + i;
                for(int k2 = i; k2 < k; k2++)
                {
                    if(!isM(ai[k1]))
                    {
                        abyte0[k1] = j3;
                    }
                    k1++;
                }

            }

            break;

        case 4: // '\004'
            int l3 = i;
            int j4 = j;
            int l4 = k;
            int j5 = l;
            for(int l5 = 0; l5 < iSize + 1; l5++)
            {
                int l1 = i1 * j4 + l3;
                int j2 = i1 * (j5 - 1) + l3;
                for(int l2 = l3; l2 < l4; l2++)
                {
                    if(!isM(ai[l1]))
                    {
                        abyte0[l1] = j3;
                    }
                    if(!isM(ai[j2]))
                    {
                        abyte0[j2] = j3;
                    }
                    l1++;
                    j2++;
                }

                l1 = i1 * j4 + l3;
                j2 = (i1 * j4 + l4) - 1;
                for(int j6 = j4; j6 < j5; j6++)
                {
                    if(!isM(ai[l1]))
                    {
                        abyte0[l1] = j3;
                    }
                    if(!isM(ai[j2]))
                    {
                        abyte0[j2] = j3;
                    }
                    l1 += i1;
                    j2 += i1;
                }

                l3++;
                l4--;
                j4++;
                j5--;
                if(l4 <= l3 || j5 <= j4)
                {
                    break label0;
                }
            }

            break;

        case 5: // '\005'
        case 6: // '\006'
            int i4 = k - i - 1;
            int k4 = l - j - 1;
            int i6 = i4 / 2;
            int k6 = k4 / 2;
            int l6 = Math.min(Math.min(iSize + 1, i6), k6);
            for(int i7 = 0; i7 < l6; i7++)
            {
                for(float f = 0.0F; f < 7F; f = (float)((double)f + 0.001D))
                {
                    int i5 = i + i6 + (int)Math.round(Math.cos(f) * (double)(i6 - i7));
                    int k5 = j + k6 + (int)Math.round(Math.sin(f) * (double)(k6 - i7));
                    abyte0[i1 * k5 + i5] = j3;
                }

            }

            if(iHint == 5 && i6 > 0 && k6 > 0)
            {
                int j7 = iColor;
                iColor = j3;
                dFill(abyte0, i, j, k, l);
                iColor = j7;
            }
            for(int k7 = j; k7 < l; k7++)
            {
                int i2 = k7 * i1 + i;
                for(int i3 = i; i3 < k; i3++)
                {
                    if(isM(ai[i2]))
                    {
                        abyte0[i2] = 0;
                    }
                    i2++;
                }

            }

            break;
        }
        t();
    }

    public void dRetouch()
        throws InterruptedException
    {
        try
        {
            getPM();
            user.setup(this);
            int i = user.pW / 2;
            int j = info.W;
            int k = info.H;
            LO alo[] = info.layers;
            setD(0, 0, 0, 0);
            int ai[] = user.points;
            byte byte0 = ((byte)(isText() ? 1 : 4));
            for(int l = 0; l < byte0 && iSeek < iOffset; l++)
            {
                ai[l] = (r2() & 0xffff) << 16 | r2() & 0xffff;
            }

            int i1 = ai[0] >> 16;
            short word0 = (short)ai[0];
            switch(iHint)
            {
            case 2: // '\002'
                int j1 = user.wait;
                user.wait = -2;
                dStart(i1 + i, word0 + i, 0, false, false);
                dBz(ai);
                user.wait = j1;
                break;

            case 8: // '\b'
            case 12: // '\f'
                String s1 = new String(offset, iSeek, iOffset - iSeek, "UTF8");
                int k1 = s1.indexOf('\0');
                dText(s1.substring(k1 + 1), i1, word0);
                break;

            case 9: // '\t'
                dCopy(ai);
                break;

            case 7: // '\007'
                dFill(i1, word0);
                break;

            case 14: // '\016'
                LO lo = alo[iLayer];
                switch(word0)
                {
                case 4: // '\004'
                default:
                    break;

                case 0: // '\0'
                    info.swapL(iLayerSrc, iLayer);
                    break;

                case 1: // '\001'
                    info.setL(ai[1]);
                    break;

                case 2: // '\002'
                    info.delL(iLayerSrc);
                    break;

                case 3: // '\003'
                    if(iLayer > iLayerSrc)
                    {
                        for(int l1 = iLayerSrc; l1 < iLayer; l1++)
                        {
                            info.swapL(l1, l1 + 1);
                        }

                    }
                    if(iLayer >= iLayerSrc)
                    {
                        break;
                    }
                    for(int i2 = iLayerSrc; i2 > iLayer; i2--)
                    {
                        info.swapL(i2, i2 - 1);
                    }

                    break;

                case 6: // '\006'
                    try
                    {
                        Toolkit toolkit = info.component.getToolkit();
                        i1 = ai[1] >> 16;
                        word0 = (short)ai[1];
                        Image image;
                        if((ai[2] & 0xff) == 1)
                        {
                            image = toolkit.createImage(offset, iSeek, iOffset - iSeek);
                        } else
                        {
                            image = toolkit.createImage((byte[])info.cnf.getRes(new String(offset, iSeek, iOffset - iSeek, "UTF8")));
                        }
                        if(image == null)
                        {
                            break;
                        }
                        Awt.wait(image);
                        int j2 = image.getWidth(null);
                        int k2 = image.getHeight(null);
                        int ai1[] = Awt.getPix(image);
                        image.flush();
                        image = null;
                        if(j2 > 0 && k2 > 0)
                        {
                            alo[iLayer].toCopy(j2, k2, ai1, i1, word0);
                        }
                    }
                    catch(Throwable throwable1)
                    {
                        throwable1.printStackTrace();
                    }
                    break;

                case 7: // '\007'
                    byte byte1 = offset[4];
                    byte abyte0[] = new byte[byte1 * 4];
                    System.arraycopy(offset, 6, abyte0, 0, byte1 * 4);
                    dFusion(abyte0);
                    break;

                case 5: // '\005'
                case 8: // '\b'
                    lo.iAlpha = b255[offset[4] & 0xff];
                    break;

                case 9: // '\t'
                    lo.iCopy = offset[4];
                    break;

                case 10: // '\n'
                    lo.name = new String(offset, 4, iOffset - 4, "UTF8");
                    break;
                }
                setD(0, 0, j, k);
                break;

            case 3: // '\003'
            case 4: // '\004'
            case 5: // '\005'
            case 6: // '\006'
            case 10: // '\n'
            case 11: // '\013'
            case 13: // '\r'
            default:
                dRect(i1, word0, ai[1] >> 16, (short)ai[1]);
                break;
            }
            if(isOver)
            {
                dFlush();
            }
            if(user.wait >= 0)
            {
                dBuffer();
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void dStart()
    {
        try
        {
            int i = r2();
            int j = r2();
            user.setup(this);
            info.layers[iLayer].reserve();
            int k = iSOB == 0 ? 0 : ru();
            if(iSOB != 0)
            {
                iSize = ss(k);
                iAlpha = sa(k);
            }
            memset(user.pX, i);
            memset(user.pY, j);
            int l = user.pW / 2;
            setD(i - l - 1, j - l - 1, i + l, j + l);
            user.fX = (float)i;
            user.fY = (float)j;
            if(iHint != 11 && !isAnti)
            {
                dFLine(i, j, k);
            }
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
        }
        catch(InterruptedException _ex) { }
    }

    public void dStart(int i, int j, int k, boolean flag, boolean flag1)
    {
        try
        {
            user.setup(this);
            info.layers[iLayer].reserve();
            iSize = ss(k);
            iAlpha = sa(k);
            user.setup(this);
            if(flag1)
            {
                int l = info.scale;
                i = (i / l + info.scaleX) * info.Q;
                j = (j / l + info.scaleY) * info.Q;
            }
            if(flag)
            {
                ByteStream bytestream = getWork();
                bytestream.w(i, 2);
                bytestream.w(j, 2);
                if(iSOB != 0)
                {
                    bytestream.write(k);
                }
            }
            memset(user.pX, i);
            memset(user.pY, j);
            int i1 = user.pW / 2;
            setD(i - i1 - 1, j - i1 - 1, i + i1, j + i1);
            user.fX = (float)i;
            user.fY = (float)j;
            if(iHint != 11 && !isAnti)
            {
                dFLine(i, j, k);
            }
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        catch(InterruptedException interruptedexception)
        {
            interruptedexception.printStackTrace();
        }
    }

    private void dText(String s1, int i, int j)
    {
        try
        {
            int k = info.W;
            int l = info.H;
            int ai[] = info.layers[iLayer].offset;
            byte abyte0[] = info.iMOffs;
            float f = b255[iAlpha];
            if(f == 0.0F)
            {
                return;
            }
            Font font = getFont(iSize);
            FontMetrics fontmetrics = info.component.getFontMetrics(font);
            if(s1 == null || s1.length() <= 0)
            {
                return;
            }
            info.layers[iLayer].reserve();
            boolean flag = iHint == 8;
            int i1 = fontmetrics.getMaxAdvance();
            int k1 = fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent() + fontmetrics.getLeading() + 2;
            int l1 = fontmetrics.getMaxAscent() + fontmetrics.getLeading() / 2 + 1;
            int k2 = s1.length();
            int i2;
            int j2;
            if(flag)
            {
                i2 = i1 * (k2 + 1) + 2;
                j2 = k1;
            } else
            {
                int j1 = fontmetrics.getMaxAdvance();
                i2 = j1 + 2;
                j2 = (k1 + iCount) * (k2 + 1);
            }
            i2 = Math.min(i2, k);
            j2 = Math.min(j2, l);
            setD(i, j, i + i2, j + j2);
            Image image = info.component.createImage(i2, j2);
            Graphics g = image.getGraphics();
            g.setFont(font);
            g.setColor(Color.black);
            g.fillRect(0, 0, i2, j2);
            g.setColor(Color.blue);
            if(flag)
            {
                g.drawString(s1, 1, l1);
            } else
            {
                int l2 = l1;
                for(int i3 = 0; i3 < k2; i3++)
                {
                    g.drawString(String.valueOf(s1.charAt(i3)), 1, l2);
                    l2 += k1 + iCount;
                }

            }
            g.dispose();
            g = null;
            font = null;
            fontmetrics = null;
            int ai1[] = Awt.getPix(image);
            image.flush();
            image = null;
            boolean flag1 = false;
            int l3 = Math.min(k - i, i2);
            int i4 = Math.min(l - j, j2);
            for(int j4 = 0; j4 < i4; j4++)
            {
                int j3 = j4 * i2;
                int k3 = (j4 + j) * k + i;
                for(int k4 = 0; k4 < l3; k4++)
                {
                    if(!isM(ai[k3]))
                    {
                        abyte0[k3] = (byte)(int)((float)(ai1[j3] & 0xff) * f);
                    }
                    j3++;
                    k3++;
                }

            }

            setD(i, j, i + i2, j + j2);
            t();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private final int fu(int i, int j, int k)
    {
        if(k == 0)
        {
            return i;
        } else
        {
            int l = i >>> 24;
            int i1 = l + (int)((float)k * b255[255 - l]);
            float f = b255[Math.min((int)((float)k * b255d[i1]), 255)];
            int j1 = i >>> 16 & 0xff;
            int k1 = i >>> 8 & 0xff;
            int l1 = i & 0xff;
            return i1 << 24 | j1 + (int)((float)((j >>> 16 & 0xff) - j1) * f) << 16 | k1 + (int)((float)((j >>> 8 & 0xff) - k1) * f) << 8 | l1 + (int)((float)((j & 0xff) - l1) * f);
        }
    }

    public final void get(OutputStream outputstream, ByteStream bytestream, M m)
    {
        try
        {
            bytestream.reset();
            int i = 0;
            boolean flag = false;
            int j = getFlag(m);
            int k = j >>> 8 & 0xff;
            int l = j & 0xff;
            bytestream.write(j >>> 16);
            bytestream.write(k);
            bytestream.write(l);
            if((k & 1) != 0)
            {
                i = iHint;
                flag = true;
            }
            if((k & 2) != 0)
            {
                if(flag)
                {
                    bytestream.write(i << 4 | iPenM);
                } else
                {
                    i = iPenM;
                }
                flag = !flag;
            }
            if((k & 4) != 0)
            {
                if(flag)
                {
                    bytestream.write(i << 4 | iMask);
                } else
                {
                    i = iMask;
                }
                flag = !flag;
            }
            if(flag)
            {
                bytestream.write(i << 4);
            }
            if((k & 8) != 0)
            {
                bytestream.write(iPen);
            }
            if((k & 0x10) != 0)
            {
                bytestream.write(iTT);
            }
            if((k & 0x20) != 0)
            {
                bytestream.write(iLayer);
            }
            if((k & 0x40) != 0)
            {
                bytestream.write(iLayerSrc);
            }
            if((l & 1) != 0)
            {
                bytestream.write(iAlpha);
            }
            if((l & 2) != 0)
            {
                bytestream.w(iColor, 3);
            }
            if((l & 4) != 0)
            {
                bytestream.w(iColorMask, 3);
            }
            if((l & 8) != 0)
            {
                bytestream.write(iSize);
            }
            if((l & 0x10) != 0)
            {
                bytestream.write(iCount);
            }
            if((l & 0x20) != 0)
            {
                bytestream.w(iSA, 2);
            }
            if((l & 0x40) != 0)
            {
                bytestream.w(iSS, 2);
            }
            if(iPen == 20)
            {
                bytestream.w2(iAlpha2);
            }
            if(isText())
            {
                if(strHint == null)
                {
                    bytestream.w2(0);
                } else
                {
                    bytestream.w2(strHint.length);
                    bytestream.write(strHint);
                }
            }
            if(offset != null && iOffset > 0)
            {
                bytestream.write(offset, 0, iOffset);
            }
            outputstream.write(bytestream.size() >>> 8);
            outputstream.write(bytestream.size() & 0xff);
            bytestream.writeTo(outputstream);
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
        }
    }

    private final int getFlag(M m)
    {
        int j = 0;
        if(isAllL)
        {
            j |= 1;
        }
        if(isAFix)
        {
            j |= 2;
        }
        if(isAnti)
        {
            j |= 0x10;
        }
        if(isCount)
        {
            j |= 8;
        }
        if(isOver)
        {
            j |= 4;
        }
        j |= iSOB << 6;
        int i = j << 16;
        if(m == null)
        {
            return i | 0xffff;
        }
        j = 0;
        if(iHint != m.iHint)
        {
            j |= 1;
        }
        if(iPenM != m.iPenM)
        {
            j |= 2;
        }
        if(iMask != m.iMask)
        {
            j |= 4;
        }
        if(iPen != m.iPen)
        {
            j |= 8;
        }
        if(iTT != m.iTT)
        {
            j |= 0x10;
        }
        if(iLayer != m.iLayer)
        {
            j |= 0x20;
        }
        if(iLayerSrc != m.iLayerSrc)
        {
            j |= 0x40;
        }
        i |= j << 8;
        j = 0;
        if(iAlpha != m.iAlpha)
        {
            j |= 1;
        }
        if(iColor != m.iColor)
        {
            j |= 2;
        }
        if(iColorMask != m.iColorMask)
        {
            j |= 4;
        }
        if(iSize != m.iSize)
        {
            j |= 8;
        }
        if(iCount != m.iCount)
        {
            j |= 0x10;
        }
        if(iSA != m.iSA)
        {
            j |= 0x20;
        }
        if(iSS != m.iSS)
        {
            j |= 0x40;
        }
        return i | j;
    }

    public Image getImage(int i, int j, int k, int l, int i1)
    {
        j = Math.round(j / info.scale) + info.scaleX;
        k = Math.round(k / info.scale) + info.scaleY;
        l /= info.scale;
        i1 /= info.scale;
        int j1 = info.Q;
        if(j1 <= 1)
        {
            return info.component.createImage(new MemoryImageSource(l, i1, info.layers[i].offset, k * info.W + j, info.W));
        } else
        {
            Image image = info.component.createImage(new MemoryImageSource(l * j1, i1 * j1, info.layers[i].offset, k * j1 * info.W + j * j1, info.W));
            Image image1 = image.getScaledInstance(l, i1, 2);
            image.flush();
            return image1;
        }
    }

    private final int getM(int i, int j, int k)
    {
        if(j == 0)
        {
            return i;
        }
        switch(iPen)
        {
        case 12: // '\f'
        case 13: // '\r'
        case 14: // '\016'
        case 15: // '\017'
        case 16: // '\020'
        case 17: // '\021'
        case 18: // '\022'
        case 19: // '\023'
        default:
            return fu(i, iColor, j);

        case 4: // '\004'
        case 5: // '\005'
            int l = i >>> 24;
            int k1 = l - (int)((float)l * b255[j]);
            return k1 != 0 ? k1 << 24 | i & 0xffffff : 0xffffff;

        case 6: // '\006'
        case 11: // '\013'
            int i1 = i >>> 24;
            int l1 = i >>> 16 & 0xff;
            int j2 = i >>> 8 & 0xff;
            int k3 = i & 0xff;
            int _tmp = iColor;
            float f1 = b255[j];
            return (i1 << 24) + (Math.min(l1 + (int)((float)l1 * f1), 255) << 16) + (Math.min(j2 + (int)((float)j2 * f1), 255) << 8) + Math.min(k3 + (int)((float)k3 * f1), 255);

        case 7: // '\007'
            int j1 = i >>> 24;
            int i2 = i >>> 16 & 0xff;
            int k2 = i >>> 8 & 0xff;
            int l3 = i & 0xff;
            int _tmp1 = iColor;
            float f2 = b255[j];
            return (j1 << 24) + (Math.max(i2 - (int)((float)(255 - i2) * f2), 0) << 16) + (Math.max(k2 - (int)((float)(255 - k2) * f2), 0) << 8) + Math.max(l3 - (int)((float)(255 - l3) * f2), 0);

        case 8: // '\b'
            int _tmp2 = i >>> 24;
            int _tmp3 = i >>> 16 & 0xff;
            int _tmp4 = i >>> 8 & 0xff;
            int _tmp5 = i & 0xff;
            int _tmp6 = iColor;
            float f = b255[j];
            int ai[] = user.argb;
            int ai1[] = info.layers[iLayer].offset;
            int i4 = info.W;
            for(int j4 = 0; j4 < 4; j4++)
            {
                ai[j4] = 0;
            }

            int l2 = k % i4;
            k += l2 != 0 ? l2 != i4 - 1 ? 0 : -1 : 1;
            k += k >= i4 ? k <= i4 * (info.H - 1) ? 0 : -i4 : i4;
            for(int k4 = -1; k4 < 2; k4++)
            {
                for(int i5 = -1; i5 < 2; i5++)
                {
                    int i3 = ai1[k + i5 + k4 * i4];
                    int _tmp7 = i3 >>> 24;
                    for(int j5 = 0; j5 < 4; j5++)
                    {
                        ai[j5] += i3 >>> (j5 << 3) & 0xff;
                    }

                }

            }

            for(int l4 = 0; l4 < 4; l4++)
            {
                int j3 = i >>> (l4 << 3) & 0xff;
                ai[l4] = j3 + (int)((float)(ai[l4] / 9 - j3) * f);
            }

            return ai[3] << 24 | ai[2] << 16 | ai[1] << 8 | ai[0];

        case 9: // '\t'
        case 20: // '\024'
            if(j == 0)
            {
                return i;
            } else
            {
                return j << 24 | 0xff0000;
            }

        case 10: // '\n'
            return j << 24 | iColor;
        }
    }

    public final byte[] getOffset()
    {
        return offset;
    }

    private final int[] getPM()
    {
        if(isText() || iHint >= 3 && iHint <= 6)
        {
            return null;
        }
        int ai[] = user.p;
        if(user.pM != iPenM || user.pA != iAlpha || user.pS != iSize)
        {
            int ai1[][] = info.bPen[iPenM];
            int ai2[] = ai1[iSize];
            int i = ai2.length;
            if(ai == null || ai.length < i)
            {
                ai = new int[i];
            }
            float f = b255[iAlpha];
            for(int j = 0; j < i; j++)
            {
                ai[j] = (int)((float)ai2[j] * f);
            }

            user.pW = (int)Math.sqrt(i);
            user.pM = iPenM;
            user.pA = iAlpha;
            user.pS = iSize;
            user.p = ai;
            user.countMax = iCount < 0 ? (int)(((float)user.pW / (float)Math.sqrt(ai1[ai1.length - 1].length)) * (float)(-iCount)) : iCount;
            user.count = Math.min(user.countMax, user.count);
        }
        return ai;
    }

    private final float getTT(int i, int j)
    {
        if(iTT == 0)
        {
            return 1.0F;
        }
        if(iTT < 12)
        {
            return (float)(isTone(iTT - 1, i, j) ? 0 : 1);
        } else
        {
            int k = user.pTTW;
            return user.pTT[(j % k) * k + i % k];
        }
    }

    private final ByteStream getWork()
    {
        info.workOut.reset();
        return info.workOut;
    }

    private final boolean isM(int i)
    {
        if(iMask == 0)
        {
            return false;
        } else
        {
            i &= 0xffffff;
            return iMask != 1 ? iMask != 2 ? false : iColorMask != i : iColorMask == i;
        }
    }

    public static final boolean isTone(int i, int j, int k)
    {
        switch(i)
        {
        default:
            break;

        case 10: // '\n'
            if((j + 3) % 4 == 0 && (k + 2) % 4 == 0)
            {
                return true;
            }
            break;

        case 9: // '\t'
            if((j + 1) % 4 == 0 && (k + 2) % 4 == 0)
            {
                break;
            }
            // fall through

        case 8: // '\b'
            if(j % 2 != 0 && (k + 1) % 2 != 0)
            {
                return true;
            }
            break;

        case 7: // '\007'
            if((j + 2) % 4 == 0 && (k + 3) % 4 == 0)
            {
                break;
            }
            // fall through

        case 6: // '\006'
            if(j % 4 == 0 && (k + 1) % 4 == 0)
            {
                break;
            }
            // fall through

        case 5: // '\005'
            if((j + 1) % 2 != (k + 1) % 2)
            {
                return true;
            }
            break;

        case 4: // '\004'
            if((j + 1) % 4 == 0 && (k + 3) % 4 == 0)
            {
                break;
            }
            // fall through

        case 3: // '\003'
            if(j % 2 != 0 || k % 2 != 0)
            {
                return true;
            }
            break;

        case 2: // '\002'
            if((j + 2) % 4 == 0 && (k + 4) % 4 == 0)
            {
                break;
            }
            // fall through

        case 1: // '\001'
            if((j + 2) % 4 == 0 && (k + 2) % 4 == 0)
            {
                break;
            }
            // fall through

        case 0: // '\0'
            if(j % 4 != 0 || k % 4 != 0)
            {
                return true;
            }
            break;
        }
        return false;
    }

    private int[] loadIm(Object obj, boolean flag)
    {
        try
        {
            Component component = info.component;
            Image image = component.getToolkit().createImage((byte[])info.cnf.getRes(obj));
            info.cnf.remove(obj);
            Awt.wait(image);
            int ai[] = Awt.getPix(image);
            int i = ai.length;
            image.flush();
            image = null;
            if(flag)
            {
                for(int j = 0; j < i; j++)
                {
                    ai[j] = ai[j] & 0xff ^ 0xff;
                }

            } else
            {
                for(int k = 0; k < i; k++)
                {
                    ai[k] &= 0xff;
                }

            }
            return ai;
        }
        catch(RuntimeException _ex)
        {
            return null;
        }
    }

    public final void m_paint(int i, int j, int k, int l)
    {
        int i1 = info.scale;
        int j1 = info.Q;
        i = (i / i1 + info.scaleX) * j1;
        j = (j / i1 + info.scaleY) * j1;
        k = (k / i1) * j1;
        l = (l / i1) * j1;
        dBuffer(false, i, j, i + k, j + l);
    }

    public final void memset(float af[], float f)
    {
        int i = af.length >>> 1;
        for(int j = 0; j < i; j++)
        {
            af[j] = f;
        }

        System.arraycopy(af, 0, af, i - 1, i);
        af[(i + i) - 1] = f;
    }

    public final void memset(int ai[], int i)
    {
        int j = ai.length >>> 1;
        for(int k = 0; k < j; k++)
        {
            ai[k] = i;
        }

        System.arraycopy(ai, 0, ai, j - 1, j);
        ai[(j + j) - 1] = i;
    }

    public final void memset(byte abyte0[], byte byte0)
    {
        int i = abyte0.length >>> 1;
        for(int j = 0; j < i; j++)
        {
            abyte0[j] = byte0;
        }

        System.arraycopy(abyte0, 0, abyte0, i - 1, i);
        abyte0[(i + i) - 1] = byte0;
    }

    public final Image mkLPic(int ai[], int i, int j, int k, int l, int i1)
    {
        i *= i1;
        j *= i1;
        k *= i1;
        l *= i1;
        boolean flag = ai == null;
        int j1 = info.L;
        LO alo[] = info.layers;
        if(flag)
        {
            ai = user.buffer;
        }
        memset(ai, 0xffffff);
        for(int k1 = 0; k1 < j1; k1++)
        {
            alo[k1].draw(ai, i, j, i + k, j + l, k);
        }

        if(flag)
        {
            user.raster.newPixels(user.image, k, l, i1);
        } else
        {
            user.raster.scale(ai, k, l, i1);
        }
        return user.image;
    }

    private final Image mkMPic(int i, int j, int k, int l, int i1)
    {
        i *= i1;
        j *= i1;
        k *= i1;
        l *= i1;
        int ai[] = user.buffer;
        int j1 = info.L;
        LO alo[] = info.layers;
        memset(ai, 0xffffff);
        for(int k1 = 0; k1 < j1; k1++)
        {
            if(k1 == iLayer)
            {
                byte abyte0[] = info.iMOffs;
                int ai1[] = alo[k1].offset;
                int j6 = info.W;
                float f = alo[k1].iAlpha;
                if(ai1 != null)
                {
                    switch(alo[k1].iCopy)
                    {
                    case 1: // '\001'
                        for(int k5 = 0; k5 < l; k5++)
                        {
                            int j3 = j6 * (k5 + j) + i;
                            int i4 = k * k5;
                            for(int l4 = 0; l4 < k; l4++)
                            {
                                int j7 = ai[i4];
                                int k6 = getM(ai1[j3], abyte0[j3] & 0xff, j3);
                                float f1 = b255[k6 >>> 24] * f;
                                if(f1 > 0.0F)
                                {
                                    ai[i4] = ((j7 >>> 16 & 0xff) - (int)(b255[j7 >>> 16 & 0xff] * ((float)(k6 >>> 16 & 0xff ^ 0xff) * f1)) << 16) + ((j7 >>> 8 & 0xff) - (int)(b255[j7 >>> 8 & 0xff] * ((float)(k6 >>> 8 & 0xff ^ 0xff) * f1)) << 8) + ((j7 & 0xff) - (int)(b255[j7 & 0xff] * ((float)(k6 & 0xff ^ 0xff) * f1)));
                                }
                                i4++;
                                j3++;
                            }

                        }

                        break;

                    case 2: // '\002'
                        for(int l5 = 0; l5 < l; l5++)
                        {
                            int k3 = j6 * (l5 + j) + i;
                            int j4 = k * l5;
                            for(int i5 = 0; i5 < k; i5++)
                            {
                                int k7 = ai[j4];
                                int l6 = getM(ai1[k3], abyte0[k3] & 0xff, k3);
                                float f2 = b255[l6 >>> 24] * f;
                                l6 ^= 0xffffff;
                                int l1 = k7 >>> 16 & 0xff;
                                int j2 = k7 >>> 8 & 0xff;
                                int l2 = k7 & 0xff;
                                ai[j4++] = f2 != 1.0F ? l1 + (int)((float)((l6 >>> 16 & 0xff) - l1) * f2) << 16 | j2 + (int)((float)((l6 >>> 8 & 0xff) - j2) * f2) << 8 | l2 + (int)((float)((l6 & 0xff) - l2) * f2) : l6;
                                k3++;
                            }

                        }

                        break;

                    default:
                        for(int i6 = 0; i6 < l; i6++)
                        {
                            int l3 = j6 * (i6 + j) + i;
                            int k4 = k * i6;
                            for(int j5 = 0; j5 < k; j5++)
                            {
                                int l7 = ai[k4];
                                int i7 = getM(ai1[l3], abyte0[l3] & 0xff, l3);
                                float f3 = b255[i7 >>> 24] * f;
                                if(f3 == 1.0F)
                                {
                                    ai[k4++] = i7;
                                } else
                                {
                                    int i2 = l7 >>> 16 & 0xff;
                                    int k2 = l7 >>> 8 & 0xff;
                                    int i3 = l7 & 0xff;
                                    ai[k4++] = i2 + (int)((float)((i7 >>> 16 & 0xff) - i2) * f3) << 16 | k2 + (int)((float)((i7 >>> 8 & 0xff) - k2) * f3) << 8 | i3 + (int)((float)((i7 & 0xff) - i3) * f3);
                                }
                                l3++;
                            }

                        }

                        break;
                    }
                }
            } else
            {
                alo[k1].draw(ai, i, j, i + k, j + l, k);
            }
        }

        user.raster.newPixels(user.image, k, l, i1);
        return user.image;
    }

    public Info newInfo(Applet applet, Component component, Res res)
    {
        if(info != null)
        {
            return info;
        }
        info = new Info();
        info.cnf = res;
        info.component = component;
        Info info1 = info;
        M m = info.m;
        float f = 3.141593F;
        for(int i = 1; i < 256; i++)
        {
            b255[i] = (float)i / 255F;
            b255d[i] = 255F / (float)i;
        }

        b255[0] = 0.0F;
        b255d[0] = 0.0F;
        int ai[][][] = info.bPen;
        boolean flag = false;
        int j4 = 1;
        char c = '\377';
        m.iAlpha = 255;
        set(m);
        int ai1[][] = new int[23][];
        for(int j = 0; j < 23; j++)
        {
            int j2 = j4 * j4;
            if(j4 <= 6)
            {
                int ai2[];
                ai1[j] = ai2 = new int[j2];
                int k4 = j4;
                for(int i1 = 0; i1 < j2; i1++)
                {
                    ai2[i1] = i1 >= j4 && j2 - i1 >= j4 && i1 % j4 != 0 && i1 % j4 != j4 - 1 ? m.iAlpha : ((int) (c));
                }

                if(j4 >= 3)
                {
                    ai2[0] = ai2[j4 - 1] = ai2[j4 * (j4 - 1)] = ai2[j2 - 1] = 0;
                }
            } else
            {
                int l4 = j4 + 1;
                int ai3[];
                ai1[j] = ai3 = new int[l4 * l4];
                int i4 = (j4 - 1) / 2;
                int l2 = (int)((float)Math.round(2.0F * f * (float)i4) * 3F);
                for(int j1 = 0; j1 < l2; j1++)
                {
                    int k3 = Math.min(i4 + (int)Math.round((double)i4 * Math.cos(j1)), j4);
                    int l3 = Math.min(i4 + (int)Math.round((double)i4 * Math.sin(j1)), j4);
                    ai3[l3 * l4 + k3] = c;
                }

                info1.W = info1.H = l4;
                dFill(ai3, 0, 0, l4, l4);
            }
            j4 += j > 7 ? j >= 18 ? 4 : 2 : 1;
        }

        ai[0] = ai1;
        m.iAlpha = 255;
        ai1 = new int[32][];
        ai1[0] = (new int[] {
            128
        });
        ai1[1] = (new int[] {
            255
        });
        ai1[2] = (new int[] {
            0, 128, 0, 128, 255, 128, 0, 128, 0
        });
        ai1[3] = (new int[] {
            128, 174, 128, 174, 255, 174, 128, 174, 128
        });
        ai1[4] = (new int[] {
            174, 255, 174, 255, 255, 255, 174, 255, 174
        });
        ai1[5] = new int[9];
        memset(ai1[5], 255);
        ai1[6] = (new int[] {
            0, 128, 128, 0, 128, 255, 255, 128, 128, 255, 
            255, 128, 0, 128, 128, 0
        });
        int ai4[] = ai1[7] = new int[16];
        memset(ai4, 255);
        ai4[0] = ai4[3] = ai4[15] = ai4[12] = 128;
        memset(ai1[8] = new int[16], 255);
        j4 = 3;
        for(int k = 9; k < 32; k++)
        {
            int i5 = j4 + 3;
            float f5 = (float)j4 / 2.0F;
            int ai5[];
            ai1[k] = ai5 = new int[i5 * i5];
            int i3 = (int)((float)Math.round(2.0F * f * f5) * (float)(2 + k / 16)) + k * 2;
            for(int k1 = 0; k1 < i3; k1++)
            {
                float f1;
                int j5 = (int)(f1 = f5 + 1.5F + f5 * (float)Math.cos(k1));
                float f2;
                int k5 = (int)(f2 = f5 + 1.5F + f5 * (float)Math.sin(k1));
                float f3 = f1 - (float)j5;
                float f4 = f2 - (float)k5;
                int l5 = k5 * i5 + j5;
                ai5[l5] += (int)((1.0F - f3) * 255F);
                ai5[l5 + 1] += (int)(f3 * 255F);
                ai5[l5 + i5] += (int)((1.0F - f4) * 255F);
                ai5[l5 + i5 + 1] += (int)(f4 * 255F);
            }

            int k2 = i5 * i5;
            for(int l1 = 0; l1 < k2; l1++)
            {
                ai5[l1] = Math.min(ai5[l1], 255);
            }

            j4 += 2;
            info1.W = info1.H = i5;
            dFill(ai5, 0, 0, i5, i5);
        }

        ai[1] = ai1;
        set(((M) (null)));
        m.set(((M) (null)));
        if(res != null)
        {
            for(int l = 0; l < 16; l++)
            {
                int j3;
                for(j3 = 0; res.get("pm" + l + '/' + j3 + ".gif") != null; j3++) { }
                if(j3 > 0)
                {
                    ai[l] = new int[j3][];
                    for(int i2 = 0; i2 < j3; i2++)
                    {
                        ai[l][i2] = loadIm("pm" + l + '/' + i2 + ".gif", true);
                    }

                }
            }

            info.bTT = new float[res.getP("tt_size", 31)][];
        }
        String s1 = applet.getParameter("tt.zip");
        if(s1 != null && s1.length() > 0)
        {
            info.dirTT = s1;
        }
        return info;
    }

    public User newUser(Component component)
    {
        if(user == null)
        {
            user = new User();
            if(color_model == null)
            {
                color_model = new DirectColorModel(24, 0xff0000, 65280, 255);
            }
            user.raster = new SRaster(color_model, user.buffer, 128, 128);
            user.image = component.createImage(user.raster);
        }
        return user;
    }

    public final int pix(int i, int j)
    {
        if(!isAllL)
        {
            return info.layers[iLayer].getPixel(i, j);
        }
        int k = info.L;
        int i1 = 0;
        int k1 = 0xffffff;
        int _tmp = info.W * j + i;
        for(int i2 = 0; i2 < k; i2++)
        {
            int l1 = info.layers[i2].getPixel(i, j);
            float f = b255[l1 >>> 24];
            if(f != 0.0F)
            {
                if(f == 1.0F)
                {
                    k1 = l1;
                    i1 = 255;
                }
                i1 = (int)((float)i1 + (float)(255 - i1) * f);
                int l = 0;
                for(int j2 = 16; j2 >= 0; j2 -= 8)
                {
                    int j1 = k1 >>> j2 & 0xff;
                    l |= j1 + (int)((float)((l1 >>> j2 & 0xff) - j1) * f) << j2;
                }

                k1 = l;
            }
        }

        return i1 << 24 | k1;
    }

    private final byte r()
    {
        if(iSeek >= iOffset)
        {
            return 0;
        } else
        {
            return offset[iSeek++];
        }
    }

    private final int r(byte abyte0[], int i, int j)
    {
        int k = 0;
        for(int l = j - 1; l >= 0; l--)
        {
            k |= (abyte0[i++] & 0xff) << l * 8;
        }

        return k;
    }

    private final short r2()
    {
        return (short)((ru() << 8) + ru());
    }

    public void reset(boolean flag)
    {
        byte abyte0[] = info.iMOffs;
        int j = info.W;
        int k = Math.max(user.X, 0);
        int l = Math.max(user.Y, 0);
        int i1 = Math.min(user.X2, j);
        int j1 = Math.min(user.Y2, info.H);
        for(int l1 = l; l1 < j1; l1++)
        {
            int i = k + l1 * j;
            for(int k1 = k; k1 < i1; k1++)
            {
                abyte0[i++] = 0;
            }

        }

        if(flag)
        {
            dBuffer(false, k, l, i1, j1);
        }
        setD(0, 0, 0, 0);
    }

    private final int rPo()
    {
        byte byte0 = r();
        return byte0 == -128 ? r2() : byte0;
    }

    private final int ru()
    {
        return r() & 0xff;
    }

    private final int s(int i, int j, int k)
    {
        byte abyte0[] = info.iMOffs;
        int l = info.W - 1;
        for(int i1 = (l + 1) * k + j; j < l && pix(j + 1, k) == i && abyte0[i1 + 1] == 0; j++)
        {
            i1++;
        }

        return j;
    }

    private final int sa(int i)
    {
        if((iSOB & 1) == 0)
        {
            return iAlpha;
        } else
        {
            int j = iSA & 0xff;
            return j + (int)(b255[(iSA >>> 8) - j] * (float)i);
        }
    }

    public final int set(byte abyte0[], int i)
    {
        int j = (abyte0[i++] & 0xff) << 8 | abyte0[i++] & 0xff;
        int k = i;
        if(j <= 2)
        {
            return j + 2;
        }
        try
        {
            int l = 0;
            boolean flag = false;
            int i1 = abyte0[i++] & 0xff;
            int j1 = abyte0[i++] & 0xff;
            int k1 = abyte0[i++] & 0xff;
            isAllL = (i1 & 1) != 0;
            isAFix = (i1 & 2) != 0;
            isOver = (i1 & 4) != 0;
            isCount = (i1 & 8) != 0;
            isAnti = (i1 & 0x10) != 0;
            iSOB = i1 >>> 6;
            if((j1 & 1) != 0)
            {
                l = abyte0[i++] & 0xff;
                flag = true;
                iHint = l >>> 4;
            }
            if((j1 & 2) != 0)
            {
                if(!flag)
                {
                    l = abyte0[i++] & 0xff;
                    iPenM = l >>> 4;
                } else
                {
                    iPenM = l & 0xf;
                }
                flag = !flag;
            }
            if((j1 & 4) != 0)
            {
                if(!flag)
                {
                    l = abyte0[i++] & 0xff;
                    iMask = l >>> 4;
                } else
                {
                    iMask = l & 0xf;
                }
                flag = !flag;
            }
            if((j1 & 8) != 0)
            {
                iPen = abyte0[i++] & 0xff;
            }
            if((j1 & 0x10) != 0)
            {
                iTT = abyte0[i++] & 0xff;
            }
            if((j1 & 0x20) != 0)
            {
                iLayer = abyte0[i++] & 0xff;
            }
            if((j1 & 0x40) != 0)
            {
                iLayerSrc = abyte0[i++] & 0xff;
            }
            if((k1 & 1) != 0)
            {
                iAlpha = abyte0[i++] & 0xff;
            }
            if((k1 & 2) != 0)
            {
                iColor = r(abyte0, i, 3);
                i += 3;
            }
            if((k1 & 4) != 0)
            {
                iColorMask = r(abyte0, i, 3);
                i += 3;
            }
            if((k1 & 8) != 0)
            {
                iSize = abyte0[i++] & 0xff;
            }
            if((k1 & 0x10) != 0)
            {
                iCount = abyte0[i++];
            }
            if((k1 & 0x20) != 0)
            {
                iSA = r(abyte0, i, 2);
                i += 2;
            }
            if((k1 & 0x40) != 0)
            {
                iSS = r(abyte0, i, 2);
                i += 2;
            }
            if(iPen == 20)
            {
                iAlpha2 = r(abyte0, i, 2);
                i += 2;
            }
            if(isText())
            {
                int l1 = r(abyte0, i, 2);
                i += 2;
                if(l1 == 0)
                {
                    strHint = null;
                } else
                {
                    strHint = new byte[l1];
                    System.arraycopy(abyte0, i, strHint, 0, l1);
                    i += l1;
                }
            }
            k = j - (i - k);
            if(k > 0)
            {
                if(offset == null || offset.length < k)
                {
                    offset = new byte[k];
                }
                iOffset = k;
                System.arraycopy(abyte0, i, offset, 0, k);
            } else
            {
                iOffset = 0;
            }
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
            iOffset = 0;
        }
        return j + 2;
    }

    public final void set(String s1)
    {
        try
        {
            if(s1 == null || s1.length() == 0)
            {
                return;
            }
            Field afield[] = getClass().getDeclaredFields();
            int i = s1.indexOf('@');
            if(i < 0)
            {
                i = s1.length();
            }
            int k;
            for(int j = 0; j < i; j = k + 1)
            {
                k = s1.indexOf('=', j);
                if(k == -1)
                {
                    break;
                }
                String s2 = s1.substring(j, k);
                j = k + 1;
                k = s1.indexOf(';', j);
                if(k < 0)
                {
                    k = i;
                }
                try
                {
                    for(int l = 0; l < afield.length; l++)
                    {
                        Field field = afield[l];
                        if(!field.getName().equals(s2))
                        {
                            continue;
                        }
                        String s3 = s1.substring(j, k);
                        Class class1 = field.getType();
                        if(class1.equals(Integer.TYPE))
                        {
                            field.setInt(this, Integer.parseInt(s3));
                        } else
                        if(class1.equals(Boolean.TYPE))
                        {
                            field.setBoolean(this, Integer.parseInt(s3) != 0);
                        } else
                        {
                            field.set(this, s3);
                        }
                        break;
                    }

                }
                catch(NumberFormatException _ex) { }
                catch(IllegalAccessException _ex) { }
            }

            if(i != s1.length())
            {
                ByteStream bytestream = getWork();
                for(int i1 = i + 1; i1 < s1.length(); i1 += 2)
                {
                    bytestream.write(Character.digit(s1.charAt(i1), 16) << 4 | Character.digit(s1.charAt(i1 + 1), 16));
                }

                offset = bytestream.toByteArray();
                iOffset = offset.length;
            }
        }
        catch(Throwable _ex) { }
    }

    public final void set(M m)
    {
        if(m == null)
        {
            m = mgDef;
        }
        iHint = m.iHint;
        iPen = m.iPen;
        iPenM = m.iPenM;
        iTT = m.iTT;
        iMask = m.iMask;
        iSize = m.iSize;
        iSS = m.iSS;
        iCount = m.iCount;
        isOver = m.isOver;
        isCount = m.isCount;
        isAFix = m.isAFix;
        isAnti = m.isAnti;
        isAllL = m.isAllL;
        iAlpha = m.iAlpha;
        iAlpha2 = m.iAlpha2;
        iSA = m.iSA;
        iColor = m.iColor;
        iColorMask = m.iColorMask;
        iLayer = m.iLayer;
        iLayerSrc = m.iLayerSrc;
        iSOB = m.iSOB;
        strHint = m.strHint;
        iOffset = 0;
    }

    public final int set(ByteStream bytestream)
    {
        return set(bytestream.getBuffer(), 0);
    }

    public void setRetouch(int ai[], byte abyte0[], int i, boolean flag)
    {
        try
        {
            int j = 4;
            int k = info.scale;
            int l = info.Q;
            int i1 = info.scaleX;
            int j1 = info.scaleY;
            getPM();
            int k1 = user.pW / 2;
            int l1 = iHint != 2 ? 0 : k1;
            int ai1[] = user.points;
            switch(iHint)
            {
            case 8: // '\b'
            case 12: // '\f'
                j = 1;
                break;

            case 9: // '\t'
                j = 3;
                break;

            case 10: // '\n'
                j = 0;
                break;

            case 3: // '\003'
            case 4: // '\004'
            case 5: // '\005'
            case 6: // '\006'
            case 11: // '\013'
            case 13: // '\r'
            default:
                j = 2;
                break;

            case 2: // '\002'
            case 7: // '\007'
            case 14: // '\016'
                break;
            }
            if(ai != null)
            {
                j = Math.min(j, ai.length);
            }
            for(int k2 = 0; k2 < j; k2++)
            {
                int i2 = ai[k2] >> 16;
                int j2 = (short)ai[k2];
                if(flag)
                {
                    i2 = (i2 / k + i1) * l - l1;
                    j2 = (j2 / k + j1) * l - l1;
                }
                ai1[k2] = i2 << 16 | j2 & 0xffff;
            }

            ByteStream bytestream = getWork();
            for(int l2 = 0; l2 < j; l2++)
            {
                bytestream.w(ai1[l2], 4);
            }

            if(abyte0 != null && i > 0)
            {
                bytestream.write(abyte0, 0, i);
            }
            offset = bytestream.writeTo(offset, 0);
            iOffset = bytestream.size();
            bytestream.reset();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private final void addD(int i, int j, int k, int l)
    {
        user.addRect(i, j, k, l);
    }

    private final void setD(int i, int j, int k, int l)
    {
        user.setRect(i, j, k, l);
    }

    public void setInfo(Info info1)
    {
        info = info1;
    }

    public void setUser(User user1)
    {
        user = user1;
    }

    private final void shift(int i, int j)
    {
        System.arraycopy(user.pX, 1, user.pX, 0, 3);
        System.arraycopy(user.pY, 1, user.pY, 0, 3);
        user.pX[3] = i;
        user.pY[3] = j;
    }

    private final int ss(int i)
    {
        if((iSOB & 2) == 0)
        {
            return iSize;
        } else
        {
            int j = iSS & 0xff;
            return (int)(((float)j + b255[(iSS >>> 8) - j] * (float)i) * user.pV);
        }
    }

    private final void t()
    {
        if(iTT == 0)
        {
            return;
        }
        byte abyte0[] = info.iMOffs;
        int k = info.W;
        int l = user.X;
        int i1 = user.Y;
        int j1 = user.X2;
        int k1 = user.Y2;
        for(int l1 = i1; l1 < k1; l1++)
        {
            int j = k * l1 + l;
            for(int i = l; i < j1; i++)
            {
                abyte0[j] = (byte)(int)((float)(abyte0[j++] & 0xff) * getTT(i, l1));
            }

        }

    }

    private final void wPo(int i)
        throws IOException
    {
        ByteStream bytestream = info.workOut;
        if(i > 127 || i < -127)
        {
            bytestream.write(-128);
            bytestream.w(i, 2);
        } else
        {
            bytestream.write(i);
        }
    }

    public boolean isText()
    {
        return iHint == 8 || iHint == 12;
    }

    public Font getFont(int i)
    {
        try
        {
            if(strHint != null)
            {
                return Font.decode(new String(strHint, "UTF8") + i);
            }
        }
        catch(IOException _ex) { }
        return new Font("sansserif", 0, iSize);
    }

    public static float[] getb255()
    {
        return b255;
    }





}
