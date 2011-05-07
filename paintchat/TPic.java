/* TPic - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;

import syi.awt.Awt;
import syi.awt.LComponent;

public class TPic extends LComponent implements SW
{
    private ToolBox ts;
    private M.Info info;
    private M.User user;
    private M mg;
    private Res r_conf;
    private int iDrag;
    private int lastMask = -1;
    private Color[] cls;
    private static float[] fhsb = new float[3];
    
    private Image cMk() {
	int[] is = user.getBuffer();
	int i = 64;
	int i_0_ = 0;
	float f = 0.0F;
	float f_1_ = 0.0F;
	float f_2_ = 1.0F / (float) i;
	float f_3_ = fhsb[0];
	for (int i_4_ = 0; i_4_ < i; i_4_++) {
	    f = 1.0F;
	    for (int i_5_ = 0; i_5_ < i; i_5_++)
		is[i_0_++] = Color.HSBtoRGB(f_3_, f -= f_2_, f_1_);
	    f_1_ += f_2_;
	}
	return user.mkImage(i, i);
    }
    
    private Image cMkB() {
	int[] is = user.getBuffer();
	int i = (int) (64.0F * LComponent.Q);
	int i_6_ = (int) (22.0F * LComponent.Q);
	int[] is_7_ = is;
	int i_8_ = 0;
	float f = 0.0F;
	float f_9_ = 1.0F / (float) i;
	for (int i_10_ = 0; i_10_ < i; i_10_++) {
	    int i_11_ = Color.HSBtoRGB(f, 1.0F, 1.0F);
	    for (int i_12_ = 0; i_12_ < i_6_; i_12_++)
		is_7_[i_8_++] = i_11_;
	    f += f_9_;
	}
	return user.mkImage(i_6_, i);
    }
    
    private int getRGB() {
	return Color.HSBtoRGB(fhsb[0], fhsb[1], fhsb[2]) & 0xffffff;
    }
    
    public void lift() {
	/* empty */
    }
    
    public void mPack() {
	this.inParent();
    }
    
    public void mPaint() {
	try {
	    Graphics graphics = this.getG();
	    mPaint(graphics);
	    graphics.dispose();
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	}
    }
    
    public void mPaint(Graphics graphics) {
	Dimension dimension = this.getSize();
	int i = (int) (22.0F * LComponent.Q);
	int i_13_ = i;
	int i_14_ = 0;
	int i_15_ = 0;
	int i_16_ = dimension.width - i_13_ - 1;
	int i_17_ = dimension.height - i - 1;
	int[] is = user.getBuffer();
	int[] is_18_;
	//MONITORENTER (is_18_ = is);
	//MISSING MONITORENTER
	synchronized (is_18_) {
	    Image image = cMk();
	    graphics.drawImage(image, i_14_, i_15_, i_16_, i_17_, Color.white,
			       null);
	    image = cMkB();
	    graphics.drawImage(image, i_16_ + 1, i_15_, i_13_, i_17_,
			       Color.white, null);
	    i_15_ += i_17_;
	}
	Awt.drawFrame(graphics, false, i_14_, i_15_ + 1, i_16_, i);
	int i_19_ = (int) ((float) (i_16_ - 8) * 0.7F);
	lastMask = mg.iColorMask;
	graphics.setColor(new Color(lastMask));
	graphics.fillRect(i_14_ + i_19_ + 6, i_15_ + 4,
			  (int) ((float) (i_16_ - 8) * 0.3F), i - 6);
	graphics.setColor(Color.getHSBColor(fhsb[0], fhsb[1], fhsb[2]));
	graphics.fillRect(i_14_ + 3, i_15_ + 4, i_19_, i - 6);
	graphics.setColor(Color.blue);
	graphics.setXORMode(Color.white);
	int i_20_ = Math.max((int) (10.0F * LComponent.Q), 2);
	int i_21_ = i_20_ >>> 1;
	graphics.setClip(i_16_ + 1, 0, i_13_, i_17_);
	graphics.drawOval(i_16_ + 1 + i_13_ / 2 - i_21_,
			  (int) ((float) i_17_ * fhsb[0]) - i_21_, i_20_,
			  i_20_);
	graphics.setClip(0, 0, i_16_, i_17_);
	graphics.drawOval((int) ((float) i_16_ * (1.0F - fhsb[1])) - i_21_,
			  (int) ((float) i_17_ * fhsb[2]) - i_21_, i_20_,
			  i_20_);
	graphics.setPaintMode();
	graphics.setClip(0, 0, dimension.width, dimension.height);
    }
    
    public void mSetup(ToolBox toolbox, M.Info info, M.User user, M m, Res res,
		       Res res_22_) {
	ts = toolbox;
	this.info = info;
	this.user = user;
	mg = m;
	r_conf = res_22_;
	this.setTitle(res_22_.getP("window_4"));
	this.setDimension(new Dimension((int) (66.0F * LComponent.Q),
					(int) (66.0F * LComponent.Q)),
			  new Dimension((int) (128.0F * LComponent.Q),
					(int) (128.0F * LComponent.Q)),
			  new Dimension((int) (284.0F * LComponent.Q),
					(int) (284.0F * LComponent.Q)));
    }
    
    public void paint2(Graphics graphics) {
	mPaint(graphics);
    }
    
    public void pMouse(MouseEvent mouseevent) {
	int i = mouseevent.getX();
	int i_23_ = mouseevent.getY();
	int i_24_ = (int) (22.0F * LComponent.Q);
	int i_25_ = (int) (25.0F * LComponent.Q);
	boolean bool = false;
	Dimension dimension = this.getSize();
	int i_26_ = dimension.width - i_25_ - 1;
	int i_27_ = dimension.height - i_24_ - 1;
	if (mouseevent.getID() == 501 && i_23_ > i_27_) {
	    int i_28_ = (int) ((float) (i_26_ - 8) * 0.7F);
	    if (i > i_28_) {
		mg.iColorMask = mg.iColor;
		ts.up();
	    }
	} else {
	    i = i <= 0 ? 0 : i >= i_26_ ? i_26_ : i;
	    i_23_ = i_23_ <= 0 ? 0 : i_23_ >= i_27_ ? i_27_ : i_23_;
	    switch (mouseevent.getID()) {
	    case 501:
		iDrag = i < i_26_ ? 0 : 1;
		bool = true;
		break;
	    case 506:
		bool = iDrag >= 0;
		break;
	    case 502:
		if (iDrag >= 0) {
		    bool = true;
		    iDrag = -1;
		}
		break;
	    }
	    if (bool && iDrag >= 0) {
		if (iDrag == 0) {
		    fhsb[1] = 1.0F - (float) i / (float) i_26_;
		    fhsb[2] = (float) i_23_ / (float) i_27_;
		} else
		    fhsb[0] = (float) i_23_ / (float) i_27_;
		ts.setARGB(mg.iAlpha << 24 | getRGB());
		mPaint();
	    }
	}
    }
    
    public void setColor(int i) {
	Color.RGBtoHSB(i >>> 16 & 0xff, i >>> 8 & 0xff, i & 0xff, fhsb);
	mPaint();
    }
    
    public void up() {
	int i = getRGB();
	int i_29_ = mg.iColor;
	if (i != i_29_ || lastMask != mg.iColorMask)
	    setColor(i_29_);
    }
}
