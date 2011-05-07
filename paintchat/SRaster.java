/* SRaster - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.util.Hashtable;

import syi.awt.Awt;

public class SRaster implements ImageProducer
{
    private int width;
    private int height;
    private ColorModel model;
    private int[] pixels;
    private ImageConsumer consumer = null;
    private static Hashtable properties = null;
    private boolean isWin;
    
    public SRaster(ColorModel colormodel, int[] is, int i, int i_0_) {
	model = colormodel;
	pixels = is;
	width = i;
	height = i_0_;
	if (properties == null) {
	    properties = new Hashtable();
	    isWin = Awt.isWin();
	}
    }
    
    public synchronized void addConsumer(ImageConsumer imageconsumer) {
	try {
	    consumer = imageconsumer;
	    imageconsumer.setDimensions(width, height);
	    imageconsumer.setColorModel(model);
	    imageconsumer.setProperties(properties);
	    imageconsumer.setHints(30);
	    sendPix();
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }
    
    public boolean isConsumer(ImageConsumer imageconsumer) {
	if (imageconsumer == consumer)
	    return true;
	return false;
    }
    
    public void newPixels(Image image, int[] is, int i, int i_1_) {
	pixels = is;
	newPixels(image, i, i_1_);
    }
    
    public void newPixels(Image image, int[] is, int i, int i_2_, int i_3_) {
	if (i_3_ != 1)
	    scale(is, i, i_2_, i_3_);
	pixels = is;
	newPixels(image, i / i_3_, i_2_ / i_3_);
    }
    
    public void newPixels(Image image, int i, int i_4_) {
	if (width != i || height != i_4_ || consumer == null || !isWin) {
	    width = i;
	    height = i_4_;
	    image.flush();
	} else
	    sendPix();
    }
    
    public void newPixels(Image image, int i, int i_5_, int i_6_) {
	if (i_6_ != 1) {
	    scale(pixels, i, i_5_, i_6_);
	    i /= i_6_;
	    i_5_ /= i_6_;
	}
	newPixels(image, i, i_5_);
    }
    
    public void removeConsumer(ImageConsumer imageconsumer) {
	/* empty */
    }
    
    public void requestTopDownLeftRightResend(ImageConsumer imageconsumer) {
	/* empty */
    }
    
    public final void scale(int[] is, int i, int i_7_, int i_8_) {
	boolean bool = false;
	int i_9_ = 0;
	if (i * i_7_ != 0) {
	    /* empty */
	}
	int i_10_ = i_8_ * i_8_;
	for (int i_11_ = 0; i_11_ < i_7_; i_11_ += i_8_) {
	    int i_12_ = i * i_11_;
	    for (int i_13_ = 0; i_13_ < i; i_13_ += i_8_) {
		int i_15_;
		int i_16_;
		int i_14_ = i_15_ = i_16_ = 0;
		int i_17_ = i_12_;
		for (int i_18_ = 0; i_18_ < i_8_; i_18_++) {
		    for (int i_19_ = 0; i_19_ < i_8_; i_19_++) {
			int i_20_ = is[i_17_++];
			i_14_ += i_20_ >>> 16 & 0xff;
			i_15_ += i_20_ >>> 8 & 0xff;
			i_16_ += i_20_ & 0xff;
		    }
		    i_17_ += i - i_8_;
		}
		is[i_9_++]
		    = i_14_ / i_10_ << 16 | i_15_ / i_10_ << 8 | i_16_ / i_10_;
		i_12_ += i_8_;
	    }
	}
    }
    
    private void sendPix() {
	if (consumer != null) {
	    consumer.setPixels(0, 0, width, height, model, pixels, 0, width);
	    consumer.imageComplete(3);
	}
    }
    
    public void startProduction(ImageConsumer imageconsumer) {
	addConsumer(imageconsumer);
    }
}
