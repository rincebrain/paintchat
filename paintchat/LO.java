package paintchat;


// Referenced classes of package paintchat:
//            M

public class LO
{

    public int W;
    public int H;
    int offX;
    int offY;
    public int offset[];
    public String name;
    public int iCopy;
    public float iAlpha;
    public boolean isDraw;
    public static int iL = 0;

    public static LO getLO(int i, int j)
    {
        LO lo = new LO(i, j);
        return lo;
    }

    public LO()
    {
        this(0, 0);
    }

    public LO(int i, int j)
    {
        offX = 0;
        offY = 0;
        offset = null;
        iCopy = 0;
        iAlpha = 1.0F;
        isDraw = false;
        W = i;
        H = j;
    }

    public void setSize(int i, int j)
    {
        if((i != W || j != H) && offset != null)
        {
            int k = Math.min(i, W);
            int l = Math.min(j, H);
            int ai[] = new int[i * j];
            for(int i1 = 0; i1 < ai.length; i1++)
            {
                ai[i1] = 0xffffff;
            }

            for(int j1 = 0; j1 < l; j1++)
            {
                System.arraycopy(offset, j1 * W, ai, j1 * i, k);
            }

            offset = ai;
        }
        W = i;
        H = j;
    }

    public void reserve()
    {
        if(offset == null)
        {
            int i = W * H;
            offset = new int[i];
            clear();
        }
    }

    public final void draw(int ai[], int i, int j, int k, int l, int i1)
    {
        if(offset == null || iAlpha <= 0.0F)
        {
            return;
        }
        float af[] = M.getb255();
        float f3 = iAlpha;
        int j4 = W;
        i = Math.max(i, 0);
        j = Math.max(j, 0);
        k = Math.min(W, k);
        l = Math.min(H, l);
        int k4 = k - i;
        int l4 = l - j;
        int i5 = 0;
        int j5 = j * j4 + i;
        switch(iCopy)
        {
        case 1: // '\001'
            for(int j1 = 0; j1 < l4; j1++)
            {
                for(int i2 = 0; i2 < k4; i2++)
                {
                    int l2 = offset[j5 + i2];
                    int k3 = ai[i5 + i2];
                    float f = af[l2 >>> 24] * f3;
                    int k5 = k3 >>> 16 & 0xff;
                    int j6 = k3 >>> 8 & 0xff;
                    int i7 = k3 & 0xff;
                    if(f > 0.0F)
                    {
                        ai[i5 + i2] = ((k3 >>> 16 & 0xff) - (int)(af[k3 >>> 16 & 0xff] * ((float)(l2 >>> 16 & 0xff ^ 0xff) * f)) << 16) + ((k3 >>> 8 & 0xff) - (int)(af[k3 >>> 8 & 0xff] * ((float)(l2 >>> 8 & 0xff ^ 0xff) * f)) << 8) + ((k3 & 0xff) - (int)(af[k3 & 0xff] * ((float)(l2 & 0xff ^ 0xff) * f)));
                    }
                }

                i5 += i1;
                j5 += j4;
            }

            break;

        case 2: // '\002'
            for(int k1 = 0; k1 < l4; k1++)
            {
                for(int j2 = 0; j2 < k4; j2++)
                {
                    int i3 = offset[j5 + j2] ^ 0xffffff;
                    int l3 = ai[i5 + j2];
                    float f1 = af[i3 >>> 24] * f3;
                    if(f1 > 0.0F)
                    {
                        int l5 = l3 >>> 16 & 0xff;
                        int k6 = l3 >>> 8 & 0xff;
                        int j7 = l3 & 0xff;
                        ai[i5 + j2] = f1 != 1.0F ? l5 + (int)((float)((i3 >>> 16 & 0xff) - l5) * f1) << 16 | k6 + (int)((float)((i3 >>> 8 & 0xff) - k6) * f1) << 8 | j7 + (int)((float)((i3 & 0xff) - j7) * f1) : i3;
                    }
                }

                i5 += i1;
                j5 += j4;
            }

            break;

        default:
            for(int l1 = 0; l1 < l4; l1++)
            {
                for(int k2 = 0; k2 < k4; k2++)
                {
                    int j3 = offset[j5 + k2];
                    int i4 = ai[i5 + k2];
                    float f2 = af[j3 >>> 24] * f3;
                    if(f2 > 0.0F)
                    {
                        int i6 = i4 >>> 16 & 0xff;
                        int l6 = i4 >>> 8 & 0xff;
                        int k7 = i4 & 0xff;
                        ai[i5 + k2] = f2 != 1.0F ? i6 + (int)((float)((j3 >>> 16 & 0xff) - i6) * f2) << 16 | l6 + (int)((float)((j3 >>> 8 & 0xff) - l6) * f2) << 8 | k7 + (int)((float)((j3 & 0xff) - k7) * f2) : j3;
                    }
                }

                i5 += i1;
                j5 += j4;
            }

            break;
        }
    }

    public void drawAlpha(int ai[], int i, int j, int k, int l, int i1)
    {
        if(offset == null || iAlpha <= 0.0F)
        {
            return;
        }
        float af[] = M.getb255();
        float f = iAlpha;
        int j2 = W;
        i = Math.max(i, 0);
        j = Math.max(j, 0);
        k = Math.min(W, k);
        l = Math.min(H, l);
        int k2 = k - i;
        int l2 = l - j;
        int i3 = 0;
        int j3 = j * j2 + i;
        int ai1[] = offset;
        for(int j1 = 0; j1 < l2; j1++)
        {
            for(int k1 = 0; k1 < k2; k1++)
            {
                int l1 = (int)((float)(ai1[j3 + k1] >>> 24) * f);
                int i2 = (int)((float)(ai[i3 + k1] >>> 24) * af[255 - l1]);
                ai[i3 + k1] = l1 + i2 << 24 | ai[i3 + k1] & 0xffffff;
            }

            i3 += i1;
            j3 += j2;
        }

    }

    public void dAdd(int ai[], int i, int j, int k, int l, int ai1[])
    {
        if(offset == null)
        {
            return;
        }
        float af[] = M.getb255();
        int ai2[] = offset;
        int j6 = W;
        int k6 = j6;
        i = Math.max(i, 0);
        j = Math.max(j, 0);
        k = Math.min(W, k);
        l = Math.min(H, l);
        int l6 = k - i;
        int i7 = l - j;
        int j7 = j * k6 + i;
        int k7 = j * j6 + i;
        int l7 = 0;
        switch(iCopy)
        {
        case 1: // '\001'
            for(int i1 = 0; i1 < i7; i1++)
            {
                for(int l1 = 0; l1 < l6; l1++)
                {
                    int k2 = ai2[k7 + l1];
                    int j3 = ai[j7 + l1];
                    int i4 = k2 >>> 24;
                    int l4 = j3 >>> 24;
                    int k5 = i4 + (int)((float)l4 * af[255 - i4]);
                    int j10 = ai1[l7 + l1] & 0xffffff;
                    int i8 = j3 >>> 16 & 0xff;
                    int l8 = j3 >>> 8 & 0xff;
                    int k9 = j3 & 0xff;
                    float f = 1.0F - (float)l4 / (float)k5;
                    i8 += (int)((float)((j10 >>> 16 & 0xff) - i8) * f);
                    l8 += (int)((float)((j10 >>> 8 & 0xff) - l8) * f);
                    k9 += (int)((float)((j10 & 0xff) - k9) * f);
                    j3 = i8 << 16 | l8 << 8 | k9;
                    if(l4 <= 0)
                    {
                        ai[j7 + l1] = k2;
                    } else
                    {
                        float f1 = af[i4] + af[255 - i4] * af[255 - l4];
                        ai[j7 + l1] = k5 << 24 | (j3 >>> 16 & 0xff) - (int)(af[j3 >>> 16 & 0xff] * ((float)(k2 >>> 16 & 0xff ^ 0xff) * f1)) << 16 | (j3 >>> 8 & 0xff) - (int)(af[j3 >>> 8 & 0xff] * ((float)(k2 >>> 8 & 0xff ^ 0xff) * f1)) << 8 | (j3 & 0xff) - (int)(af[j3 & 0xff] * ((float)(k2 & 0xff ^ 0xff) * f1));
                    }
                }

                j7 += k6;
                k7 += j6;
                l7 += k6;
            }

            break;

        case 2: // '\002'
            for(int j1 = 0; j1 < i7; j1++)
            {
                for(int i2 = 0; i2 < l6; i2++)
                {
                    int l2 = ai2[k7 + i2];
                    int k3 = ai[j7 + i2];
                    int j4 = l2 >>> 24;
                    int i5 = (int)((float)(k3 >>> 24) * af[255 - j4]);
                    int l5 = j4 + i5;
                    l2 ^= 0xffffff;
                    if(l5 == 0)
                    {
                        ai[j7 + i2] = 0xffffff;
                    } else
                    {
                        float f2 = (float)j4 / (float)l5;
                        int j8 = k3 >>> 16 & 0xff;
                        int i9 = k3 >>> 8 & 0xff;
                        int l9 = k3 & 0xff;
                        ai[j7 + i2] = f2 != 1.0F ? l5 << 24 | j8 + (int)((float)((l2 >>> 16 & 0xff) - j8) * f2) << 16 | i9 + (int)((float)((l2 >>> 8 & 0xff) - i9) * f2) << 8 | l9 + (int)((float)((l2 & 0xff) - l9) * f2) : l2;
                    }
                }

                j7 += k6;
                k7 += j6;
            }

            break;

        default:
            for(int k1 = 0; k1 < i7; k1++)
            {
                for(int j2 = 0; j2 < l6; j2++)
                {
                    int i3 = ai2[k7 + j2];
                    int l3 = ai[j7 + j2];
                    int k4 = i3 >>> 24;
                    int j5 = (int)((float)(l3 >>> 24) * af[255 - k4]);
                    int i6 = k4 + j5;
                    if(i6 == 0)
                    {
                        ai[j7 + j2] = 0xffffff;
                    } else
                    {
                        float f3 = (float)k4 / (float)i6;
                        int k8 = l3 >>> 16 & 0xff;
                        int j9 = l3 >>> 8 & 0xff;
                        int i10 = l3 & 0xff;
                        ai[j7 + j2] = f3 != 1.0F ? i6 << 24 | k8 + (int)((float)((i3 >>> 16 & 0xff) - k8) * f3) << 16 | j9 + (int)((float)((i3 >>> 8 & 0xff) - j9) * f3) << 8 | i10 + (int)((float)((i3 & 0xff) - i10) * f3) : i3;
                    }
                }

                j7 += k6;
                k7 += j6;
            }

            break;
        }
    }

    public void normalize(float f)
    {
        normalize(f, 0, 0, W, H);
    }

    public void normalize(float f, int i, int j, int k, int l)
    {
        if(offset == null)
        {
            return;
        }
        int i1 = k - i;
        for(; j < l; j++)
        {
            int l1 = j * W + i;
            for(int k1 = 0; k1 < i1; k1++)
            {
                int j1 = offset[l1];
                offset[l1] = (int)((float)(j1 >>> 24) * f) << 24 | j1 & 0xffffff;
                l1++;
            }

        }

    }

    public final int getPixel(int i, int j)
    {
        if(offset == null || i < 0 || j < 0 || i >= W || j >= H)
        {
            return 0xffffff;
        } else
        {
            return offset[j * W + i];
        }
    }

    public final void setPixel(int i, int j, int k)
    {
        if(j < 0 || k < 0 || j >= W || k >= H || offset == null)
        {
            return;
        } else
        {
            offset[k * W + j] = i;
            return;
        }
    }

    public final void clear()
    {
        clear(0, 0, W, H);
    }

    public void clear(int i, int j, int k, int l)
    {
        if(offset == null)
        {
            return;
        }
        int i1 = j * W + i;
        int j1 = k - i;
        for(int k1 = 0; k1 < j1; k1++)
        {
            offset[i1 + k1] = 0xffffff;
        }

        i1 += W;
        for(j++; j < l; j++)
        {
            System.arraycopy(offset, i1 - W, offset, i1, j1);
            i1 += W;
        }

    }

    public void copyTo(int i, int j, int k, int l, LO lo, int i1, int j1, 
            int ai[])
    {
        int ai1[] = lo.offset;
        if(offset == null && ai1 == null)
        {
            return;
        }
        if(ai1 == null)
        {
            lo.reserve();
            ai1 = lo.offset;
        }
        copyTo(i, j, k, l, ai1, i1, j1, lo.W, lo.H, ai);
    }

    public final void copyTo(int i, int j, int k, int l, int ai[], int i1, int j1, 
            int k1, int l1, int ai1[])
    {
        i = Math.max(i, 0);
        j = Math.max(j, 0);
        k = Math.min(k, W);
        l = Math.min(l, H);
        int i2 = Math.min(k - i, k1);
        int j2 = Math.min(l - j, l1);
        if(i1 < 0)
        {
            i2 += i1;
            i -= i1;
            i1 = 0;
        }
        if(j1 < 0)
        {
            j2 += j1;
            j -= j1;
            j1 = 0;
        }
        if(i1 + i2 >= k1)
        {
            i2 = k1 - i1;
        }
        if(j1 + j2 >= l1)
        {
            j2 = l1 - j1;
        }
        if(i2 <= 0 || j2 <= 0)
        {
            return;
        }
        if(offset == null)
        {
            for(int i3 = j1; i3 < j1 + j2; i3++)
            {
                int k2 = i3 * k1 + i1;
                for(int l3 = 0; l3 < i2; l3++)
                {
                    ai[k2++] = 0xffffff;
                }

            }

        } else
        {
            int l2 = j * W + i;
            if(offset != ai)
            {
                int j3 = j1 * k1 + i1;
                for(int i4 = 0; i4 < j2; i4++)
                {
                    System.arraycopy(offset, l2, ai, j3, i2);
                    l2 += W;
                    j3 += k1;
                }

            } else
            {
                int k3 = i2 * j2;
                if(ai1 == null || ai1.length < k3)
                {
                    ai1 = new int[k3];
                }
                for(int j4 = 0; j4 < j2; j4++)
                {
                    System.arraycopy(offset, l2, ai1, j4 * i2, i2);
                    l2 += W;
                }

                l2 = j1 * k1 + i1;
                for(int k4 = 0; k4 < j2; k4++)
                {
                    System.arraycopy(ai1, k4 * i2, ai, l2, i2);
                    l2 += k1;
                }

            }
        }
    }

    public void setLayer(LO lo)
    {
        setField(lo);
        setImage(lo);
    }

    public void setImage(LO lo)
    {
        int i = lo.W;
        int j = lo.H;
        int k = i * j;
        if(offset == null && lo.offset == null)
        {
            return;
        }
        reserve();
        if(lo.offset == null)
        {
            for(int l = 0; l < k; l++)
            {
                offset[l] = 0xffffff;
            }

        } else
        {
            System.arraycopy(lo.offset, 0, offset, 0, k);
        }
        W = i;
        H = j;
    }

    public void setField(LO lo)
    {
        name = lo.name;
        iAlpha = lo.iAlpha;
        iCopy = lo.iCopy;
        offX = lo.offX;
        offY = lo.offY;
        isDraw = lo.isDraw;
    }

    public void makeName(String s)
    {
        name = s + iL++;
    }

    public void toCopy(int i, int j, int ai[], int k, int l)
    {
        if(offset == null)
        {
            reserve();
        }
        int i1 = 0;
        int j1 = 0;
        int k1 = i;
        int _tmp = j;
        if(k < 0)
        {
            i1 = -k;
            i += k;
            k = 0;
        }
        if(k + i > W)
        {
            i = W - k;
        }
        if(l < 0)
        {
            j1 = -l;
            j += l;
            l = 0;
        }
        if(l + j > H)
        {
            j = H - l;
        }
        if(i <= 0 || j <= 0)
        {
            return;
        }
        int l1 = j1 * k1 + i1;
        int i2 = l * W + k;
        for(int j2 = 0; j2 < j; j2++)
        {
            System.arraycopy(ai, l1, offset, i2, i);
            l1 += i;
            i2 += W;
        }

    }

    public void dLR(int i, int j, int k, int l)
    {
        if(offset == null)
        {
            return;
        }
        for(; j < l; j++)
        {
            int k1 = k - 1;
            for(int j1 = i; j1 < k1; j1++)
            {
                int i1 = getPixel(j1, j);
                setPixel(getPixel(k1, j), j1, j);
                setPixel(i1, k1, j);
                k1--;
                if(j1 + 1 >= k1)
                {
                    break;
                }
            }

        }

    }

    public void dUD(int i, int j, int k, int l)
    {
        if(offset == null)
        {
            return;
        }
        for(; i < k; i++)
        {
            int k1 = l - 1;
            for(int j1 = j; j1 < k1; j1++)
            {
                int i1 = getPixel(i, j1);
                setPixel(getPixel(i, k1), i, j1);
                setPixel(i1, i, k1);
                k1--;
                if(j1 + 1 >= k1)
                {
                    break;
                }
            }

        }

    }

    public void dR(int i, int j, int k, int l, int ai[])
    {
        if(offset == null)
        {
            return;
        }
        int i1 = W;
        int j1 = H;
        int k1 = k - i;
        int l1 = l - j;
        Math.max(k1, l1);
        int k2 = j * i1 + i;
        int l2 = k1 * l1;
        if(ai == null || ai.length < l2)
        {
            ai = new int[l2];
        }
        for(int i3 = 0; i3 < l1; i3++)
        {
            System.arraycopy(offset, k2 + i1 * i3, ai, k1 * i3, k1);
        }

        for(int j3 = 0; j3 < k1; j3++)
        {
            offset[k2 + j3] = 0xffffff;
        }

        for(int k3 = 1; k3 < l1; k3++)
        {
            System.arraycopy(offset, k2, offset, k2 + k3 * i1, k1);
        }

        l2 = i1 * j1;
        for(int l3 = 0; l3 < l1; l3++)
        {
            int i2 = k1 * l3;
            int j2 = (k2 + l1) - l3;
            for(int i4 = 0; i4 < k1; i4++)
            {
                int j4 = i4 + i;
                if(j4 <= i1 && j4 >= 0 && j2 < l2)
                {
                    offset[j2] = ai[i2];
                }
                j2 += i1;
                i2++;
            }

        }

    }

    public void swap(LO lo)
    {
        LO lo1 = new LO(W, H);
        lo1.setField(this);
        setField(lo);
        lo.setField(lo1);
        int ai[] = offset;
        int ai1[] = lo.offset;
        if(ai != null && ai1 != null)
        {
            int i = W * H;
            for(int k = 0; k < i; k++)
            {
                int j = ai[k];
                ai[k] = ai1[k];
                ai1[k] = j;
            }

        } else
        {
            offset = ai1;
            lo.offset = ai;
        }
    }

}
