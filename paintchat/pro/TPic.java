/* TPic - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat.pro;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;

import syi.awt.Awt;
import syi.awt.LComponent;

public class TPic extends LComponent
{
    private Tools tools;
    public Component tColor;
    private int iDrag = -1;
    private int sizePalette = 20;
    private int selPalette = 0;
    private int oldColor = 0;
    private Color[] cls;
    private int isRGB = 1;
    private int iColor;
    private static float[] fhsb = new float[3];
    
    public TPic(Tools tools) {
	this.setDimension(new Dimension((int) (66.0F * LComponent.Q),
					(int) (66.0F * LComponent.Q)),
			  new Dimension((int) (128.0F * LComponent.Q),
					(int) (128.0F * LComponent.Q)),
			  new Dimension((int) (284.0F * LComponent.Q),
					(int) (284.0F * LComponent.Q)));
	this.tools = tools;
    }
    
    private Image cMk() {
	int[] is = tools.iBuffer;
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
	return tools.mkImage(i, i);
    }
    
    private Image cMkB() {
	int[] is = tools.iBuffer;
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
	return tools.mkImage(i_6_, i);
    }
    
    private int getRGB() {
	return Color.HSBtoRGB(fhsb[0], fhsb[1], fhsb[2]);
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
	int[] is;
/*	MONITORENTER (is = tools.iBuffer);
	MISSING MONITORENTER*/
	synchronized (is) {
	    Image image = cMk();
	    graphics.drawImage(image, i_14_, i_15_, i_16_, i_17_, Color.white,
			       null);
	    image = cMkB();
	    graphics.drawImage(image, i_16_ + 1, i_15_, i_13_, i_17_,
			       Color.white, null);
	    i_15_ += i_17_;
	}
	Awt.drawFrame(graphics, false, i_14_, i_15_ + 1, i_16_, i);
	int i_18_ = (int) ((float) (i_16_ - 8) * 0.7F);
	graphics.setColor(new Color(tools.info.m.iColorMask));
	graphics.fillRect(i_14_ + i_18_ + 6, i_15_ + 4,
			  (int) ((float) (i_16_ - 8) * 0.3F), i - 6);
	graphics.setColor(Color.getHSBColor(fhsb[0], fhsb[1], fhsb[2]));
	graphics.fillRect(i_14_ + 3, i_15_ + 4, i_18_, i - 6);
	graphics.setColor(Color.blue);
	graphics.setXORMode(Color.white);
	int i_19_ = Math.max((int) (10.0F * LComponent.Q), 2);
	int i_20_ = i_19_ >>> 1;
	graphics.setClip(i_16_ + 1, 0, i_13_, i_17_);
	graphics.drawOval(i_16_ + 1 + i_13_ / 2 - i_20_,
			  (int) ((float) i_17_ * fhsb[0]) - i_20_, i_19_,
			  i_19_);
	graphics.setClip(0, 0, i_16_, i_17_);
	graphics.drawOval((int) ((float) i_16_ * (1.0F - fhsb[1])) - i_20_,
			  (int) ((float) i_17_ * fhsb[2]) - i_20_, i_19_,
			  i_19_);
	graphics.setPaintMode();
	graphics.setClip(0, 0, dimension.width, dimension.height);
    }
    
    public void paint2(Graphics graphics) {
	mPaint(graphics);
    }
    
    public void pMouse(MouseEvent mouseevent) {
	int i = mouseevent.getX();
	int i_21_ = mouseevent.getY();
	int i_22_ = (int) (22.0F * LComponent.Q);
	int i_23_ = (int) (25.0F * LComponent.Q);
	boolean bool = false;
	Dimension dimension = this.getSize();
	int i_24_ = dimension.width - i_23_ - 1;
	int i_25_ = dimension.height - i_22_ - 1;
	boolean bool_26_
	    = ((mouseevent.getModifiers() & 0x4) != 0
	       || mouseevent.isShiftDown() || mouseevent.isControlDown());
	if (mouseevent.getID() == 501 && i_21_ > i_25_) {
	    int i_27_ = (int) ((float) (i_24_ - 8) * 0.7F);
	    if (i > i_27_)
		tools.setMask(this, getRGB(), i, i_21_, bool_26_);
	} else {
	    i = i <= 0 ? 0 : i >= i_24_ ? i_24_ : i;
	    i_21_ = i_21_ <= 0 ? 0 : i_21_ >= i_25_ ? i_25_ : i_21_;
	    switch (mouseevent.getID()) {
	    case 501:
		iDrag = i < i_24_ ? 0 : 1;
		bool = true;
		break;
	    case 506:
		bool = iDrag >= 0;
		break;
	    case 502:
		if (iDrag >= 0) {
		    bool = true;
		    iDrag = -1;
		    tools.setRGB(Color.HSBtoRGB(fhsb[0], fhsb[1], fhsb[2]));
		}
		break;
	    }
	    if (bool && iDrag >= 0) {
		if (iDrag == 0) {
		    fhsb[1] = 1.0F - (float) i / (float) i_24_;
		    fhsb[2] = (float) i_21_ / (float) i_25_;
		} else
		    fhsb[0] = (float) i_21_ / (float) i_25_;
		mPaint();
	    }
	}
    }
    
    public void setColor(int i) {
	Color.RGBtoHSB(i >>> 16 & 0xff, i >>> 8 & 0xff, i & 0xff, fhsb);
	mPaint();
    }
}
