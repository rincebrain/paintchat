package paintchat;

import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.Hashtable;
import syi.awt.Awt;

public class SRaster
  implements ImageProducer
{
  private int width;
  private int height;
  private ColorModel model;
  private int[] pixels;
  private ImageConsumer consumer = null;
  private static Hashtable properties = null;
  private boolean isWin;

  public SRaster(ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    this.model = paramColorModel;
    this.pixels = paramArrayOfInt;
    this.width = paramInt1;
    this.height = paramInt2;
    if (properties == null)
    {
      properties = new Hashtable();
      this.isWin = Awt.isWin();
    }
  }

  public synchronized void addConsumer(ImageConsumer paramImageConsumer)
  {
    try
    {
      this.consumer = paramImageConsumer;
      paramImageConsumer.setDimensions(this.width, this.height);
      paramImageConsumer.setColorModel(this.model);
      paramImageConsumer.setProperties(properties);
      paramImageConsumer.setHints(30);
      sendPix();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public boolean isConsumer(ImageConsumer paramImageConsumer)
  {
    return paramImageConsumer == this.consumer;
  }

  public void newPixels(Image paramImage, int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    this.pixels = paramArrayOfInt;
    newPixels(paramImage, paramInt1, paramInt2);
  }

  public void newPixels(Image paramImage, int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 != 1)
      scale(paramArrayOfInt, paramInt1, paramInt2, paramInt3);
    this.pixels = paramArrayOfInt;
    newPixels(paramImage, paramInt1 / paramInt3, paramInt2 / paramInt3);
  }

  public void newPixels(Image paramImage, int paramInt1, int paramInt2)
  {
    if ((this.width != paramInt1) || (this.height != paramInt2) || (this.consumer == null) || (!this.isWin))
    {
      this.width = paramInt1;
      this.height = paramInt2;
      paramImage.flush();
    }
    else
    {
      sendPix();
    }
  }

  public void newPixels(Image paramImage, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 != 1)
    {
      scale(this.pixels, paramInt1, paramInt2, paramInt3);
      paramInt1 /= paramInt3;
      paramInt2 /= paramInt3;
    }
    newPixels(paramImage, paramInt1, paramInt2);
  }

  public void removeConsumer(ImageConsumer paramImageConsumer)
  {
  }

  public void requestTopDownLeftRightResend(ImageConsumer paramImageConsumer)
  {
  }

  public final void scale(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    int k = 0;
    (paramInt1 * paramInt2);
    int i6 = paramInt3 * paramInt3;
    int i7 = 0;
    while (i7 < paramInt2)
    {
      i = paramInt1 * i7;
      int m = 0;
      while (m < paramInt1)
      {
        int i4;
        int i3;
        int i2 = i3 = i4 = 0;
        int j = i;
        for (int i1 = 0; i1 < paramInt3; i1++)
        {
          for (int n = 0; n < paramInt3; n++)
          {
            int i5 = paramArrayOfInt[(j++)];
            i2 += (i5 >>> 16 & 0xFF);
            i3 += (i5 >>> 8 & 0xFF);
            i4 += (i5 & 0xFF);
          }
          j += paramInt1 - paramInt3;
        }
        paramArrayOfInt[(k++)] = (i2 / i6 << 16 | i3 / i6 << 8 | i4 / i6);
        i += paramInt3;
        m += paramInt3;
      }
      i7 += paramInt3;
    }
  }

  private void sendPix()
  {
    if (this.consumer != null)
    {
      this.consumer.setPixels(0, 0, this.width, this.height, this.model, this.pixels, 0, this.width);
      this.consumer.imageComplete(3);
    }
  }

  public void startProduction(ImageConsumer paramImageConsumer)
  {
    addConsumer(paramImageConsumer);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.SRaster
 * JD-Core Version:    0.6.0
 */