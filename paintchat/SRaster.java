package paintchat;

import java.awt.Image;
import java.awt.image.*;
import java.util.Hashtable;
import syi.awt.Awt;

public class SRaster
    implements ImageProducer
{

    private int width;
    private int height;
    private ColorModel model;
    private int pixels[];
    private ImageConsumer consumer;
    private static Hashtable properties = null;
    private boolean isWin;

    public SRaster(ColorModel colormodel, int ai[], int i, int j)
    {
        consumer = null;
        model = colormodel;
        pixels = ai;
        width = i;
        height = j;
        if(properties == null)
        {
            properties = new Hashtable();
            isWin = Awt.isWin();
        }
    }

    public synchronized void addConsumer(ImageConsumer imageconsumer)
    {
        try
        {
            consumer = imageconsumer;
            imageconsumer.setDimensions(width, height);
            imageconsumer.setColorModel(model);
            imageconsumer.setProperties(properties);
            imageconsumer.setHints(30);
            sendPix();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public boolean isConsumer(ImageConsumer imageconsumer)
    {
        return imageconsumer == consumer;
    }

    public void newPixels(Image image, int ai[], int i, int j)
    {
        pixels = ai;
        newPixels(image, i, j);
    }

    public void newPixels(Image image, int ai[], int i, int j, int k)
    {
        if(k != 1)
        {
            scale(ai, i, j, k);
        }
        pixels = ai;
        newPixels(image, i / k, j / k);
    }

    public void newPixels(Image image, int i, int j)
    {
        if(width != i || height != j || consumer == null || !isWin)
        {
            width = i;
            height = j;
            image.flush();
        } else
        {
            sendPix();
        }
    }

    public void newPixels(Image image, int i, int j, int k)
    {
        if(k != 1)
        {
            scale(pixels, i, j, k);
            i /= k;
            j /= k;
        }
        newPixels(image, i, j);
    }

    public void removeConsumer(ImageConsumer imageconsumer)
    {
    }

    public void requestTopDownLeftRightResend(ImageConsumer imageconsumer)
    {
    }

    public final void scale(int ai[], int i, int j, int k)
    {
        boolean flag = false;
        int j1 = 0;
        int _tmp = i * j;
        int j3 = k * k;
        for(int k3 = 0; k3 < j; k3 += k)
        {
            int l = i * k3;
            for(int k1 = 0; k1 < i; k1 += k)
            {
                int k2;
                int l2;
                int j2 = k2 = l2 = 0;
                int i1 = l;
                for(int i2 = 0; i2 < k; i2++)
                {
                    for(int l1 = 0; l1 < k; l1++)
                    {
                        int i3 = ai[i1++];
                        j2 += i3 >>> 16 & 0xff;
                        k2 += i3 >>> 8 & 0xff;
                        l2 += i3 & 0xff;
                    }

                    i1 += i - k;
                }

                ai[j1++] = j2 / j3 << 16 | k2 / j3 << 8 | l2 / j3;
                l += k;
            }

        }

    }

    private void sendPix()
    {
        if(consumer != null)
        {
            consumer.setPixels(0, 0, width, height, model, pixels, 0, width);
            consumer.imageComplete(3);
        }
    }

    public void startProduction(ImageConsumer imageconsumer)
    {
        addConsumer(imageconsumer);
    }

}
