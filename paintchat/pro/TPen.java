/* TPen - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat.pro;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;

import paintchat.M;
import paintchat.Res;

import syi.awt.Awt;
import syi.awt.LComponent;

public class TPen extends LComponent implements Runnable
{
    private Tools tools;
    private int iType = 0;
    private boolean isRun = false;
    private LComponent[] cs;
    private TPen tPen;
    private M.Info info;
    private M mg;
    private Res config;
    private Image image = null;
    private Image[] images = null;
    private boolean isDrag = false;
    private int selButton = 0;
    private int selWhite;
    private int selPen = 0;
    private int imW = 0;
    private int imH = 0;
    private int imCount;
    private int selItem = -1;
    private M[] mgs = null;
    private ColorModel cmDef;
    private int sizeTT = 0;
    
    public TPen(Tools tools, M.Info info, Res res, TPen tpen_0_,
		LComponent[] lcomponents) {
	this.tools = tools;
	this.info = info;
	mg = this.info.m;
	config = res;
	tPen = tpen_0_;
	cs = lcomponents;
    }
    
    private int getIndex(int i, int i_1_, int i_2_) {
	Dimension dimension = this.getSize();
	int i_3_ = imW;
	int i_4_ = imH;
	if (iType != 2) {
	    i_3_ += 3;
	    i_4_ += 3;
	}
	i -= i_2_;
	int i_5_ = (dimension.width - i_2_) / i_3_;
	return i_1_ / i_4_ * i_5_ + Math.min(i / i_3_, i_5_);
    }
    
    public void init(int i) {
	iType = i;
	Res res = config;
	i++;
	int i_6_ = 30;
	int i_7_ = 30;
	String string = String.valueOf(i);
	int i_8_;
	for (i_8_ = 0;
	     res.get((Object) (String.valueOf('t') + string + i_8_)) != null;
	     i_8_++) {
	    /* empty */
	}
	if (i_8_ != 0) {
	    mgs = new M[i_8_];
	    for (int i_9_ = 0; i_9_ < i_8_; i_9_++) {
		M m = new M();
		m.set(res.get(String.valueOf('t') + string + i_9_));
		mgs[i_9_] = m;
	    }
	    for (int i_10_ = i_8_ - 1; i_10_ >= 0; i_10_--) {
		if (mgs[i_10_].iPen == 4 || mgs[i_10_].iPen == 5)
		    selWhite = i_10_;
	    }
	    String string_11_ = "res/" + i + ".gif";
	    image = this.getToolkit()
			.createImage((byte[]) res.getRes(string_11_));
	    Awt.wait(image);
	    res.remove(string_11_);
	    i_6_ = (int) ((float) image.getWidth(null) * LComponent.Q);
	    i_7_ = (int) ((float) (image.getHeight(null) / i_8_)
			  * LComponent.Q);
	    if (LComponent.Q != 1.0F)
		image = Awt.toMin(image, i_6_, i_7_ * i_8_);
	    imW = i_6_;
	    imH = i_7_;
	    imCount = i_8_;
	} else {
	    imCount = 0;
	    imW = 20;
	    imH = 20;
	}
	i_6_ += 3;
	i_7_ += 3;
	selItem = -1;
	setItem(0, null);
	this.setDimension(new Dimension(i_6_ + 1, i_7_ + 1),
			  new Dimension(i_6_ + 1, i_7_ * i_8_ + 1),
			  new Dimension(i_6_ * i_8_ + 1, i_7_ * i_8_ + 1));
    }
    
    public void initHint() {
	try {
	    String string = "res/3.gif";
	    iType = 3;
	    imCount = 7;
	    int i = imCount;
	    image = this.getToolkit()
			.createImage((byte[]) config.getRes(string));
	    Awt.wait(image);
	    config.remove(string);
	    int i_12_ = image.getWidth(null);
	    int i_13_ = image.getHeight(null);
	    if (LComponent.Q != 1.0F) {
		i_12_ *= LComponent.Q;
		i_13_ = (int) ((float) i_13_ * LComponent.Q) / i * i;
		image = Awt.toMin(image, i_12_, i_13_);
	    }
	    i_13_ /= i;
	    imW = i_12_;
	    imH = i_13_;
	    i_12_ += 3;
	    i_13_ += 3;
	    this.setDimension(new Dimension(i_12_ + 1, i_13_ + 1),
			      new Dimension(i_12_ + 1, i_13_ * i + 1),
			      new Dimension(i_12_ * i + 1, i_13_ * i + 1));
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	}
    }
    
    public void initTT() {
	iType = 2;
	Res res = config;
	this.getToolkit();
	cmDef = new DirectColorModel(24, 65280, 65280, 255);
	imW = imH = (int) (34.0F * LComponent.Q);
	try {
	    String string = "tt_size";
	    images = new Image[Integer.parseInt(res.get(string))];
	    res.remove(string);
	    int i = imW * 5 + 1;
	    int i_14_ = ((images.length + 12) / 5 + 1) * imW + 1;
	    this.setDimension(new Dimension(imW + 1, imW + 1),
			      new Dimension(i, i_14_),
			      new Dimension(i * 2, i_14_ * 2));
	} catch (RuntimeException runtimeexception) {
	    /* empty */
	}
    }
    
    private void mouseH(MouseEvent mouseevent) {
	if (mouseevent.getID() == 501) {
	    int i = getIndex(mouseevent.getX(), mouseevent.getY(), 0);
	    if (i < 7) {
		mg.iHint = i;
		this.repaint();
	    }
	}
    }
    
    private void mousePen(MouseEvent mouseevent) {
	if (mouseevent.getID() == 501) {
	    int i = getIndex(mouseevent.getX(), mouseevent.getY(), 0);
	    if (i < imCount)
		setItem(i, null);
	}
    }
    
    private void mouseTT(MouseEvent mouseevent) {
	if (mouseevent.getID() == 501) {
	    this.getSize();
	    int i = getIndex(mouseevent.getX(), mouseevent.getY(), 0);
	    if (i < images.length + 12) {
		mg.iTT = i;
		this.repaint();
	    }
	}
    }
    
    public void paint2(Graphics graphics) {
	switch (iType) {
	case 2:
	    paintTT(graphics);
	    break;
	case 3:
	    selItem = mg.iHint;
	    /* fall through */
	default:
	    paintPen(graphics);
	}
    }
    
    private void paintPen(Graphics graphics) {
	if (image != null) {
	    int i = 0;
	    int i_15_ = 0;
	    int i_16_ = imW;
	    int i_17_ = imH;
	    int i_18_ = imW + 3;
	    int i_19_ = imH + 3;
	    Dimension dimension = this.getSize();
	    for (int i_20_ = 0; i_20_ < imCount; i_20_++) {
		graphics.setColor(selItem == i_20_ ? Awt.cFSel : Awt.cF);
		graphics.drawRect(i + 1, i_15_ + 1, i_16_ + 1, i_17_ + 1);
		graphics.drawImage(image, i + 2, i_15_ + 2, i + i_16_ + 2,
				   i_15_ + i_17_ + 2, 0, i_20_ * i_17_, i_16_,
				   (i_20_ + 1) * i_17_, null);
		if (selItem == i_20_) {
		    graphics.setColor(Color.black);
		    graphics.fillRect(i + 2, i_15_ + 2, i_16_, 1);
		    graphics.fillRect(i + 2, i_15_ + 3, 1, i_17_ - 1);
		}
		i = i + i_18_ * 2 >= dimension.width ? 0 : i + i_18_;
		i_15_ = i == 0 ? i_15_ + i_19_ : i_15_;
		if (i_15_ + i_19_ >= dimension.height)
		    break;
	    }
	}
    }
    
    private void paintTT(Graphics graphics) {
	if (images != null) {
	    if (!isRun) {
		Thread thread = new Thread(this);
		thread.setPriority(1);
		thread.setDaemon(true);
		thread.start();
		isRun = true;
	    }
	    int i = images.length + 11;
	    int i_21_ = 0;
	    int i_22_ = 0;
	    int i_23_ = imW;
	    int i_24_ = imH;
	    int i_25_ = i_23_ - 3;
	    int[] is = tools.iBuffer;
	    Dimension dimension = this.getSize();
	    this.getToolkit();
	    int i_26_ = this.getBackground().getRGB();
	    for (int i_27_ = -1; i_27_ < i; i_27_++) {
		graphics.setColor(i_27_ + 1 == mg.iTT ? Awt.cFSel : Awt.cF);
		graphics.drawRect(i_21_ + 1, i_22_ + 1, i_23_ - 2, i_24_ - 2);
		do {
		    if (i_27_ == -1) {
			graphics.setColor(Color.blue);
			graphics.fillRect(i_21_ + 2, i_22_ + 2, i_23_ - 3,
					  i_24_ - 3);
		    } else {
			if (i_27_ < 11) {
			    int[] is_28_;
/*			    MONITORENTER (is_28_ = is);
			    MISSING MONITORENTER*/
			    synchronized (is_28_) {
				int i_29_ = 0;
				int i_30_ = i_27_;
				for (int i_31_ = 0; i_31_ < i_25_; i_31_++) {
				    for (int i_32_ = 0; i_32_ < i_25_; i_32_++)
					is[i_29_++]
					    = (M.isTone(i_30_, i_32_, i_31_)
					       ? i_26_ : -16776961);
				}
				graphics.drawImage(tools.mkImage(i_25_, i_25_),
						   i_21_ + 2, i_22_ + 2,
						   this.getBackground(), null);
				break;
			    }
			}
			Image image = images[i_27_ - 11];
			if (image == null) {
			    graphics.setColor(Color.blue);
			    graphics.fillRect(i_21_ + 2, i_22_ + 2, i_23_ - 3,
					      i_24_ - 3);
			} else
			    graphics.drawImage(image, i_21_ + 2, i_22_ + 2,
					       this.getBackground(), null);
		    }
		} while (false);
		i_21_ += i_23_;
		if (i_21_ + i_23_ >= dimension.width) {
		    i_21_ = 0;
		    i_22_ += i_24_;
		    if (i_22_ + i_24_ >= dimension.height)
			break;
		}
	    }
	}
    }
    
    public void pMouse(MouseEvent mouseevent) {
	switch (iType) {
	default:
	    mousePen(mouseevent);
	    break;
	case 2:
	    mouseTT(mouseevent);
	    break;
	case 3:
	    mouseH(mouseevent);
	}
    }
    
    public void run() {
	try {
	    int i = imW;
	    int i_33_ = imH;
	    for (int i_34_ = 0; i_34_ < images.length; i_34_++) {
		if (images[i_34_] == null) {
		    float[] fs = info.getTT(i_34_ + 12);
		    int[] is = new int[fs.length];
		    for (int i_35_ = 0; i_35_ < is.length; i_35_++)
			is[i_35_]
			    = (int) ((1.0F - fs[i_35_]) * 255.0F) << 8 | 0xff;
		    int i_36_ = (int) Math.sqrt((double) is.length);
		    images[i_34_]
			= Awt.toMin(this.createImage(new MemoryImageSource
						     (i_36_, i_36_, cmDef, is,
						      0, i_36_)),
				    i - 3, i_33_ - 3);
		    if (i_34_ % 5 == 2)
			this.repaint();
		}
	    }
	    this.repaint();
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    public void setItem(int i, M m) {
	if (iType == 1)
	    tPen.setItem(-1, m);
	else {
	    if (selItem >= 0 && selItem < imCount)
		mgs[selItem].set(mg);
	    if (i >= 0) {
		int i_37_ = mgs[i].iPen;
		if (i_37_ == 4 || i_37_ == 5)
		    selWhite = i;
		else
		    selPen = i;
	    }
	}
	selItem = i;
	if (i >= 0 || m != null) {
	    int i_38_ = mg.iColor;
	    int i_39_ = mg.iColorMask;
	    int i_40_ = mg.iMask;
	    int i_41_ = mg.iLayer;
	    mg.set(m != null ? m : mgs[i]);
	    mg.iColor = i_38_;
	    mg.iColorMask = i_39_;
	    mg.iMask = i_40_;
	    mg.iLayer = i_41_;
	    if (tPen != null)
		tPen.repaint();
	    for (int i_42_ = 0; i_42_ < cs.length; i_42_++) {
		if (cs[i_42_] != null)
		    cs[i_42_].repaint();
	    }
	} else
	    this.repaint();
    }
    
    public void undo(boolean bool) {
	if (bool) {
	    if (selWhite != selItem)
		setItem(selWhite, null);
	} else if (selPen != selItem)
	    setItem(selPen, null);
    }
}
