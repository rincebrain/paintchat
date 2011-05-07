package paintchat;

public class LO
{
  public int W;
  public int H;
  int offX = 0;
  int offY = 0;
  public int[] offset = null;
  public String name;
  public int iCopy = 0;
  public float iAlpha = 1.0F;
  public boolean isDraw = false;
  public static int iL = 0;

  public static LO getLO(int paramInt1, int paramInt2)
  {
    LO localLO = new LO(paramInt1, paramInt2);
    return localLO;
  }

  public LO()
  {
    this(0, 0);
  }

  public LO(int paramInt1, int paramInt2)
  {
    this.W = paramInt1;
    this.H = paramInt2;
  }

  public void setSize(int paramInt1, int paramInt2)
  {
    if (((paramInt1 != this.W) || (paramInt2 != this.H)) && (this.offset != null))
    {
      int i = Math.min(paramInt1, this.W);
      int j = Math.min(paramInt2, this.H);
      int[] arrayOfInt = new int[paramInt1 * paramInt2];
      for (int k = 0; k < arrayOfInt.length; k++)
        arrayOfInt[k] = 16777215;
      for (k = 0; k < j; k++)
        System.arraycopy(this.offset, k * this.W, arrayOfInt, k * paramInt1, i);
      this.offset = arrayOfInt;
    }
    this.W = paramInt1;
    this.H = paramInt2;
  }

  public void reserve()
  {
    if (this.offset == null)
    {
      int i = this.W * this.H;
      this.offset = new int[i];
      clear();
    }
  }

  public final void draw(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if ((this.offset == null) || (this.iAlpha <= 0.0F))
      return;
    float[] arrayOfFloat = M.getb255();
    float f2 = this.iAlpha;
    int n = this.W;
    paramInt1 = Math.max(paramInt1, 0);
    paramInt2 = Math.max(paramInt2, 0);
    paramInt3 = Math.min(this.W, paramInt3);
    paramInt4 = Math.min(this.H, paramInt4);
    int i1 = paramInt3 - paramInt1;
    int i2 = paramInt4 - paramInt2;
    int i3 = 0;
    int i4 = paramInt2 * n + paramInt1;
    int i;
    int j;
    int k;
    int m;
    float f1;
    int i5;
    int i6;
    int i7;
    switch (this.iCopy)
    {
    case 1:
      for (i = 0; i < i2; i++)
      {
        for (j = 0; j < i1; j++)
        {
          k = this.offset[(i4 + j)];
          m = paramArrayOfInt[(i3 + j)];
          f1 = arrayOfFloat[(k >>> 24)] * f2;
          i5 = m >>> 16 & 0xFF;
          i6 = m >>> 8 & 0xFF;
          i7 = m & 0xFF;
          if (f1 <= 0.0F)
            continue;
          paramArrayOfInt[(i3 + j)] = (((m >>> 16 & 0xFF) - (int)(arrayOfFloat[(m >>> 16 & 0xFF)] * ((k >>> 16 & 0xFF ^ 0xFF) * f1)) << 16) + ((m >>> 8 & 0xFF) - (int)(arrayOfFloat[(m >>> 8 & 0xFF)] * ((k >>> 8 & 0xFF ^ 0xFF) * f1)) << 8) + ((m & 0xFF) - (int)(arrayOfFloat[(m & 0xFF)] * ((k & 0xFF ^ 0xFF) * f1))));
        }
        i3 += paramInt5;
        i4 += n;
      }
      break;
    case 2:
      for (i = 0; i < i2; i++)
      {
        for (j = 0; j < i1; j++)
        {
          k = this.offset[(i4 + j)] ^ 0xFFFFFF;
          m = paramArrayOfInt[(i3 + j)];
          f1 = arrayOfFloat[(k >>> 24)] * f2;
          if (f1 <= 0.0F)
            continue;
          i5 = m >>> 16 & 0xFF;
          i6 = m >>> 8 & 0xFF;
          i7 = m & 0xFF;
          paramArrayOfInt[(i3 + j)] = (f1 == 1.0F ? k : i5 + (int)(((k >>> 16 & 0xFF) - i5) * f1) << 16 | i6 + (int)(((k >>> 8 & 0xFF) - i6) * f1) << 8 | i7 + (int)(((k & 0xFF) - i7) * f1));
        }
        i3 += paramInt5;
        i4 += n;
      }
      break;
    default:
      for (i = 0; i < i2; i++)
      {
        for (j = 0; j < i1; j++)
        {
          k = this.offset[(i4 + j)];
          m = paramArrayOfInt[(i3 + j)];
          f1 = arrayOfFloat[(k >>> 24)] * f2;
          if (f1 <= 0.0F)
            continue;
          i5 = m >>> 16 & 0xFF;
          i6 = m >>> 8 & 0xFF;
          i7 = m & 0xFF;
          paramArrayOfInt[(i3 + j)] = (f1 == 1.0F ? k : i5 + (int)(((k >>> 16 & 0xFF) - i5) * f1) << 16 | i6 + (int)(((k >>> 8 & 0xFF) - i6) * f1) << 8 | i7 + (int)(((k & 0xFF) - i7) * f1));
        }
        i3 += paramInt5;
        i4 += n;
      }
    }
  }

  public void drawAlpha(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if ((this.offset == null) || (this.iAlpha <= 0.0F))
      return;
    float[] arrayOfFloat = M.getb255();
    float f = this.iAlpha;
    int n = this.W;
    paramInt1 = Math.max(paramInt1, 0);
    paramInt2 = Math.max(paramInt2, 0);
    paramInt3 = Math.min(this.W, paramInt3);
    paramInt4 = Math.min(this.H, paramInt4);
    int i1 = paramInt3 - paramInt1;
    int i2 = paramInt4 - paramInt2;
    int i3 = 0;
    int i4 = paramInt2 * n + paramInt1;
    int[] arrayOfInt = this.offset;
    for (int i = 0; i < i2; i++)
    {
      for (int j = 0; j < i1; j++)
      {
        int k = (int)((arrayOfInt[(i4 + j)] >>> 24) * f);
        int m = (int)((paramArrayOfInt[(i3 + j)] >>> 24) * arrayOfFloat[(255 - k)]);
        paramArrayOfInt[(i3 + j)] = (k + m << 24 | paramArrayOfInt[(i3 + j)] & 0xFFFFFF);
      }
      i3 += paramInt5;
      i4 += n;
    }
  }

  public void dAdd(int[] paramArrayOfInt1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt2)
  {
    if (this.offset == null)
      return;
    float[] arrayOfFloat = M.getb255();
    int[] arrayOfInt = this.offset;
    int i3 = this.W;
    int i4 = i3;
    paramInt1 = Math.max(paramInt1, 0);
    paramInt2 = Math.max(paramInt2, 0);
    paramInt3 = Math.min(this.W, paramInt3);
    paramInt4 = Math.min(this.H, paramInt4);
    int i5 = paramInt3 - paramInt1;
    int i6 = paramInt4 - paramInt2;
    int i7 = paramInt2 * i4 + paramInt1;
    int i8 = paramInt2 * i3 + paramInt1;
    int i9 = 0;
    int i;
    int j;
    int k;
    int m;
    int n;
    int i1;
    int i2;
    int i10;
    int i11;
    int i12;
    float f;
    switch (this.iCopy)
    {
    case 1:
      for (i = 0; i < i6; i++)
      {
        for (j = 0; j < i5; j++)
        {
          k = arrayOfInt[(i8 + j)];
          m = paramArrayOfInt1[(i7 + j)];
          n = k >>> 24;
          i1 = m >>> 24;
          i2 = n + (int)(i1 * arrayOfFloat[(255 - n)]);
          int i13 = paramArrayOfInt2[(i9 + j)] & 0xFFFFFF;
          i10 = m >>> 16 & 0xFF;
          i11 = m >>> 8 & 0xFF;
          i12 = m & 0xFF;
          f = 1.0F - i1 / i2;
          i10 += (int)(((i13 >>> 16 & 0xFF) - i10) * f);
          i11 += (int)(((i13 >>> 8 & 0xFF) - i11) * f);
          i12 += (int)(((i13 & 0xFF) - i12) * f);
          m = i10 << 16 | i11 << 8 | i12;
          if (i1 <= 0)
          {
            paramArrayOfInt1[(i7 + j)] = k;
          }
          else
          {
            f = arrayOfFloat[n] + arrayOfFloat[(255 - n)] * arrayOfFloat[(255 - i1)];
            paramArrayOfInt1[(i7 + j)] = (i2 << 24 | (m >>> 16 & 0xFF) - (int)(arrayOfFloat[(m >>> 16 & 0xFF)] * ((k >>> 16 & 0xFF ^ 0xFF) * f)) << 16 | (m >>> 8 & 0xFF) - (int)(arrayOfFloat[(m >>> 8 & 0xFF)] * ((k >>> 8 & 0xFF ^ 0xFF) * f)) << 8 | (m & 0xFF) - (int)(arrayOfFloat[(m & 0xFF)] * ((k & 0xFF ^ 0xFF) * f)));
          }
        }
        i7 += i4;
        i8 += i3;
        i9 += i4;
      }
      break;
    case 2:
      for (i = 0; i < i6; i++)
      {
        for (j = 0; j < i5; j++)
        {
          k = arrayOfInt[(i8 + j)];
          m = paramArrayOfInt1[(i7 + j)];
          n = k >>> 24;
          i1 = (int)((m >>> 24) * arrayOfFloat[(255 - n)]);
          i2 = n + i1;
          k ^= 16777215;
          if (i2 == 0)
          {
            paramArrayOfInt1[(i7 + j)] = 16777215;
          }
          else
          {
            f = n / i2;
            i10 = m >>> 16 & 0xFF;
            i11 = m >>> 8 & 0xFF;
            i12 = m & 0xFF;
            paramArrayOfInt1[(i7 + j)] = (f == 1.0F ? k : i2 << 24 | i10 + (int)(((k >>> 16 & 0xFF) - i10) * f) << 16 | i11 + (int)(((k >>> 8 & 0xFF) - i11) * f) << 8 | i12 + (int)(((k & 0xFF) - i12) * f));
          }
        }
        i7 += i4;
        i8 += i3;
      }
      break;
    default:
      for (i = 0; i < i6; i++)
      {
        for (j = 0; j < i5; j++)
        {
          k = arrayOfInt[(i8 + j)];
          m = paramArrayOfInt1[(i7 + j)];
          n = k >>> 24;
          i1 = (int)((m >>> 24) * arrayOfFloat[(255 - n)]);
          i2 = n + i1;
          if (i2 == 0)
          {
            paramArrayOfInt1[(i7 + j)] = 16777215;
          }
          else
          {
            f = n / i2;
            i10 = m >>> 16 & 0xFF;
            i11 = m >>> 8 & 0xFF;
            i12 = m & 0xFF;
            paramArrayOfInt1[(i7 + j)] = (f == 1.0F ? k : i2 << 24 | i10 + (int)(((k >>> 16 & 0xFF) - i10) * f) << 16 | i11 + (int)(((k >>> 8 & 0xFF) - i11) * f) << 8 | i12 + (int)(((k & 0xFF) - i12) * f));
          }
        }
        i7 += i4;
        i8 += i3;
      }
    }
  }

  public void normalize(float paramFloat)
  {
    normalize(paramFloat, 0, 0, this.W, this.H);
  }

  public void normalize(float paramFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.offset == null)
      return;
    int i = paramInt3 - paramInt1;
    while (paramInt2 < paramInt4)
    {
      int m = paramInt2 * this.W + paramInt1;
      for (int k = 0; k < i; k++)
      {
        int j = this.offset[m];
        this.offset[m] = ((int)((j >>> 24) * paramFloat) << 24 | j & 0xFFFFFF);
        m++;
      }
      paramInt2++;
    }
  }

  public final int getPixel(int paramInt1, int paramInt2)
  {
    if ((this.offset == null) || (paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 >= this.W) || (paramInt2 >= this.H))
      return 16777215;
    return this.offset[(paramInt2 * this.W + paramInt1)];
  }

  public final void setPixel(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt2 < 0) || (paramInt3 < 0) || (paramInt2 >= this.W) || (paramInt3 >= this.H) || (this.offset == null))
      return;
    this.offset[(paramInt3 * this.W + paramInt2)] = paramInt1;
  }

  public final void clear()
  {
    clear(0, 0, this.W, this.H);
  }

  public void clear(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.offset == null)
      return;
    int i = paramInt2 * this.W + paramInt1;
    int j = paramInt3 - paramInt1;
    for (int k = 0; k < j; k++)
      this.offset[(i + k)] = 16777215;
    i += this.W;
    paramInt2++;
    while (paramInt2 < paramInt4)
    {
      System.arraycopy(this.offset, i - this.W, this.offset, i, j);
      i += this.W;
      paramInt2++;
    }
  }

  public void copyTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, LO paramLO, int paramInt5, int paramInt6, int[] paramArrayOfInt)
  {
    int[] arrayOfInt = paramLO.offset;
    if ((this.offset == null) && (arrayOfInt == null))
      return;
    if (arrayOfInt == null)
    {
      paramLO.reserve();
      arrayOfInt = paramLO.offset;
    }
    copyTo(paramInt1, paramInt2, paramInt3, paramInt4, arrayOfInt, paramInt5, paramInt6, paramLO.W, paramLO.H, paramArrayOfInt);
  }

  public final void copyTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt1, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int[] paramArrayOfInt2)
  {
    paramInt1 = Math.max(paramInt1, 0);
    paramInt2 = Math.max(paramInt2, 0);
    paramInt3 = Math.min(paramInt3, this.W);
    paramInt4 = Math.min(paramInt4, this.H);
    int i = Math.min(paramInt3 - paramInt1, paramInt7);
    int j = Math.min(paramInt4 - paramInt2, paramInt8);
    if (paramInt5 < 0)
    {
      i += paramInt5;
      paramInt1 -= paramInt5;
      paramInt5 = 0;
    }
    if (paramInt6 < 0)
    {
      j += paramInt6;
      paramInt2 -= paramInt6;
      paramInt6 = 0;
    }
    if (paramInt5 + i >= paramInt7)
      i = paramInt7 - paramInt5;
    if (paramInt6 + j >= paramInt8)
      j = paramInt8 - paramInt6;
    if ((i <= 0) || (j <= 0))
      return;
    int m;
    int k;
    int n;
    if (this.offset == null)
    {
      for (m = paramInt6; m < paramInt6 + j; m++)
      {
        k = m * paramInt7 + paramInt5;
        for (n = 0; n < i; n++)
          paramArrayOfInt1[(k++)] = 16777215;
      }
    }
    else
    {
      k = paramInt2 * this.W + paramInt1;
      if (this.offset != paramArrayOfInt1)
      {
        m = paramInt6 * paramInt7 + paramInt5;
        for (n = 0; n < j; n++)
        {
          System.arraycopy(this.offset, k, paramArrayOfInt1, m, i);
          k += this.W;
          m += paramInt7;
        }
      }
      else
      {
        m = i * j;
        if ((paramArrayOfInt2 == null) || (paramArrayOfInt2.length < m))
          paramArrayOfInt2 = new int[m];
        for (n = 0; n < j; n++)
        {
          System.arraycopy(this.offset, k, paramArrayOfInt2, n * i, i);
          k += this.W;
        }
        k = paramInt6 * paramInt7 + paramInt5;
        for (n = 0; n < j; n++)
        {
          System.arraycopy(paramArrayOfInt2, n * i, paramArrayOfInt1, k, i);
          k += paramInt7;
        }
      }
    }
  }

  public void setLayer(LO paramLO)
  {
    setField(paramLO);
    setImage(paramLO);
  }

  public void setImage(LO paramLO)
  {
    int i = paramLO.W;
    int j = paramLO.H;
    int k = i * j;
    if ((this.offset == null) && (paramLO.offset == null))
      return;
    reserve();
    if (paramLO.offset == null)
      for (int m = 0; m < k; m++)
        this.offset[m] = 16777215;
    else
      System.arraycopy(paramLO.offset, 0, this.offset, 0, k);
    this.W = i;
    this.H = j;
  }

  public void setField(LO paramLO)
  {
    this.name = paramLO.name;
    this.iAlpha = paramLO.iAlpha;
    this.iCopy = paramLO.iCopy;
    this.offX = paramLO.offX;
    this.offY = paramLO.offY;
    this.isDraw = paramLO.isDraw;
  }

  public void makeName(String paramString)
  {
    this.name = (paramString + iL++);
  }

  public void toCopy(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4)
  {
    if (this.offset == null)
      reserve();
    int i = 0;
    int j = 0;
    int k = paramInt1;
    if (paramInt3 < 0)
    {
      i = -paramInt3;
      paramInt1 += paramInt3;
      paramInt3 = 0;
    }
    if (paramInt3 + paramInt1 > this.W)
      paramInt1 = this.W - paramInt3;
    if (paramInt4 < 0)
    {
      j = -paramInt4;
      paramInt2 += paramInt4;
      paramInt4 = 0;
    }
    if (paramInt4 + paramInt2 > this.H)
      paramInt2 = this.H - paramInt4;
    if ((paramInt1 <= 0) || (paramInt2 <= 0))
      return;
    int m = j * k + i;
    int n = paramInt4 * this.W + paramInt3;
    for (int i1 = 0; i1 < paramInt2; i1++)
    {
      System.arraycopy(paramArrayOfInt, m, this.offset, n, paramInt1);
      m += paramInt1;
      n += this.W;
    }
  }

  public void dLR(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.offset == null)
      return;
    while (paramInt2 < paramInt4)
    {
      int k = paramInt3 - 1;
      for (int j = paramInt1; j < k; j++)
      {
        int i = getPixel(j, paramInt2);
        setPixel(getPixel(k, paramInt2), j, paramInt2);
        setPixel(i, k, paramInt2);
        k--;
        if (j + 1 >= k)
          break;
      }
      paramInt2++;
    }
  }

  public void dUD(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.offset == null)
      return;
    while (paramInt1 < paramInt3)
    {
      int k = paramInt4 - 1;
      for (int j = paramInt2; j < k; j++)
      {
        int i = getPixel(paramInt1, j);
        setPixel(getPixel(paramInt1, k), paramInt1, j);
        setPixel(i, paramInt1, k);
        k--;
        if (j + 1 >= k)
          break;
      }
      paramInt1++;
    }
  }

  public void dR(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    if (this.offset == null)
      return;
    int i = this.W;
    int j = this.H;
    int k = paramInt3 - paramInt1;
    int m = paramInt4 - paramInt2;
    Math.max(k, m);
    int i2 = paramInt2 * i + paramInt1;
    int i3 = k * m;
    if ((paramArrayOfInt == null) || (paramArrayOfInt.length < i3))
      paramArrayOfInt = new int[i3];
    for (int i4 = 0; i4 < m; i4++)
      System.arraycopy(this.offset, i2 + i * i4, paramArrayOfInt, k * i4, k);
    for (i4 = 0; i4 < k; i4++)
      this.offset[(i2 + i4)] = 16777215;
    for (i4 = 1; i4 < m; i4++)
      System.arraycopy(this.offset, i2, this.offset, i2 + i4 * i, k);
    i3 = i * j;
    for (int i5 = 0; i5 < m; i5++)
    {
      int n = k * i5;
      int i1 = i2 + m - i5;
      for (int i6 = 0; i6 < k; i6++)
      {
        int i7 = i6 + paramInt1;
        if ((i7 <= i) && (i7 >= 0) && (i1 < i3))
          this.offset[i1] = paramArrayOfInt[n];
        i1 += i;
        n++;
      }
    }
  }

  public void swap(LO paramLO)
  {
    LO localLO = new LO(this.W, this.H);
    localLO.setField(this);
    setField(paramLO);
    paramLO.setField(localLO);
    int[] arrayOfInt1 = this.offset;
    int[] arrayOfInt2 = paramLO.offset;
    if ((arrayOfInt1 != null) && (arrayOfInt2 != null))
    {
      int i = this.W * this.H;
      for (int k = 0; k < i; k++)
      {
        int j = arrayOfInt1[k];
        arrayOfInt1[k] = arrayOfInt2[k];
        arrayOfInt2[k] = j;
      }
    }
    else
    {
      this.offset = arrayOfInt2;
      paramLO.offset = arrayOfInt1;
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.LO
 * JD-Core Version:    0.6.0
 */