/* Tab - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;

import paintchat.M;

public class Tab extends LComponent
{
    private M mg;
    private int iDrag = -1;
    private int sizeBar;
    private int max = 0;
    private int strange;
    private Object tab;
    private Method mGet;
    private Method mPoll;
    private Method mEx;
    private byte iSOB;
    private final String[] STR = { "alpha", "size" };
    
    public Tab(Container container, M.Info info) throws Throwable {
	try {
	    mg = info.m;
	    int i = sizeBar = (int) (16.0F * LComponent.Q);
	    Dimension dimension = this.getSize();
	    dimension.setSize(i * 4, 8 + i * 6);
	    this.setDimension(dimension, dimension, dimension);
	    Class var_class = Class.forName("cello.tablet.JTablet");
	    this.tab = var_class.newInstance();
	    mGet = var_class.getMethod("getPressure", null);
	    mPoll = var_class.getMethod("poll", null);
	    mEx = var_class.getMethod("getPressureExtent", null);
	    this.setTitle("tablet");
	    container.add(this, 0);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private int at(int i) {
	if (i > this.getSize().height / 2)
	    i -= 5;
	return i / sizeBar;
    }
    
    private void dBar(Graphics graphics, int i) {
	Dimension dimension = this.getSize();
	int i_0_ = sizeBar;
	int i_1_ = i * (dimension.height / 2) + i_0_;
	float f = (float) (dimension.width - 6) / 255.0F;
	for (int i_2_ = 0; i_2_ < 2; i_2_++) {
	    int i_3_ = i == 0 ? mg.iSA : mg.iSS;
	    graphics.setColor(clFrame);
	    graphics.drawRect(2, i_1_, (int) (f * 255.0F), i_0_);
	    int i_4_ = (int) ((float) (i_3_ >>> i_2_ * 8 & 0xff) * f);
	    graphics.setColor(this.getForeground());
	    graphics.fillRect(3, i_1_ + 1, i_4_, i_0_ - 1);
	    graphics.setColor(this.getBackground());
	    graphics.fillRect(i_4_ + 3, i_1_ + 1, dimension.width - i_4_ - 7,
			      i_0_ - 1);
	    i_1_ += i_0_;
	}
    }
    
    private void drag(int i) {
	int i_5_ = iDrag;
	if (i_5_ > 0 && i_5_ != 3) {
	    boolean bool = i_5_ >= 3;
	    if ((iSOB & 1 << (!bool ? 0 : 1)) != 0) {
		i = (int) (255.0F / (float) this.getSize().width * (float) i);
		i = i <= 0 ? 0 : i >= 255 ? 255 : i;
		if (bool) {
		    int i_6_ = (i_5_ - 4) * 8;
		    mg.iSS = mg.iSS & 255 << 8 - i_6_ | i << i_6_;
		} else {
		    int i_7_ = (i_5_ - 1) * 8;
		    mg.iSA = mg.iSA & 255 << 8 - i_7_ | i << i_7_;
		}
		Graphics graphics = this.getG();
		dBar(graphics, i_5_ < 3 ? 0 : 1);
		graphics.dispose();
	    }
	}
    }
    
    public void paint2(Graphics graphics) {
	try {
	    int i = sizeBar;
	    Dimension dimension = this.getSize();
	    if (mg.iSS != 0) {
		/* empty */
	    }
	    int i_8_ = dimension.width - 1;
	    int i_9_ = i_8_ - 6;
	    int i_10_ = i * 3 + 4;
	    if ((float) i_9_ / 255.0F != 0) {
		/* empty */
	    }
	    for (int i_11_ = 0; i_11_ < 2; i_11_++) {
		boolean bool = (iSOB & i_11_ + 1) != 0;
		int i_12_ = i_10_ * i_11_;
		Awt.fillFrame(graphics, !bool, 0, i_12_, i_8_, i_10_);
		Awt.fillFrame(graphics, bool, 0, i_12_, i, i);
		graphics.setColor(this.getForeground());
		graphics.drawString(STR[i_11_] + '.' + (bool ? "On" : "Off"),
				    i + 2, i_12_ + i - 2);
		dBar(graphics, i_11_);
		if (mg.iSA != 0) {
		    /* empty */
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void pMouse(MouseEvent mouseevent) {
	int i = mouseevent.getX();
	int i_13_ = mouseevent.getY();
	int i_14_ = sizeBar;
	switch (mouseevent.getID()) {
	case 501:
	    if (iDrag < 0) {
		int i_15_ = at(i_13_);
		iDrag = i_15_;
		if ((i_15_ == 0 || i_15_ == 3) && i <= i_14_) {
		    Tab tab_16_ = this;
		    tab_16_.iSOB
			= (byte) (tab_16_.iSOB ^ 1 << (i_15_ == 0 ? 0 : 1));
		    this.repaint();
		} else
		    drag(i);
	    }
	    break;
	case 502:
	    iDrag = -1;
	    break;
	case 506:
	    drag(i);
	    break;
	}
    }
    
    public final boolean poll() {
	if (iSOB == 0)
	    return false;
	try {
	    if (((Boolean) mPoll.invoke(this.tab, null)).booleanValue()) {
		mg.iSOB = iSOB;
		if (max <= 0) {
		    max = ((Integer) mEx.invoke(this.tab, null)).intValue();
		    if (max != 0)
			mEx = null;
		}
		return true;
	    }
	} catch (Throwable throwable) {
	    /* empty */
	}
	return false;
    }
    
    public final int strange() {
	try {
	    if (poll())
		strange
		    = (int) ((float) ((Integer) mGet.invoke(this.tab, null))
					 .intValue()
			     / (float) max * 255.0F);
	    else
		strange = 0;
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
	return strange;
    }
}
