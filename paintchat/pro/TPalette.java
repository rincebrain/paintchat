/* TPalette - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat.pro;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.StringReader;

import paintchat.M;
import paintchat.Res;

import syi.awt.Awt;
import syi.awt.LComponent;

public class TPalette extends LComponent
{
    private int lenColor = 255;
    private int iDrag = -1;
    private M.Info info;
    private M mg;
    private Tools tools;
    private Res res;
    private Res config;
    private int sizePalette = 20;
    private int selPalette = 0;
    private int oldColor = 0;
    private Color[] cls;
    private int isRGB = 1;
    private float[] fhsb = new float[3];
    private int iColor;
    private static Font clFont = null;
    private static final char[][] clValue
	= { { 'H', 'S', 'B', 'A' }, { 'R', 'G', 'B', 'A' } };
    private static Color[][] clRGB = null;
    private static int[] clsDef
	= { 0, 16777215, 11826549, 8947848, 16422550, 12621504, 16758527,
	    8421631, 2475977, 15197581, 15177261, 10079099, 16575714,
	    16375247 };
    
    public TPalette() {
	if (clRGB == null)
	    clRGB = new Color[][] { { Color.magenta, Color.cyan, Color.white,
				      Color.lightGray },
				    { new Color(16422550), new Color(8581688),
				      new Color(8421631), Color.lightGray } };
    }
    
    public void changeRH() {
	int i = getRGB();
	isRGB = isRGB == 0 ? 1 : 0;
	setColor(i);
    }
    
    public String getC() {
	try {
	    StringBuffer stringbuffer = new StringBuffer();
	    for (int i = 0; i < cls.length; i++) {
		if (i != 0)
		    stringbuffer.append('\n');
		int i_0_ = cls[i].getRGB();
		stringbuffer.append("#" + Integer.toHexString
					      (~0xffffff | i_0_ & 0xffffff)
					      .substring
					      (2).toUpperCase());
	    }
	    return stringbuffer.toString();
	} catch (Throwable throwable) {
	    return null;
	}
    }
    
    private int getRGB() {
	return (isRGB == 1 ? iColor
		: Color.HSBtoRGB((float) (iColor >>> 16 & 0xff) / 255.0F,
				 (float) (iColor >>> 8 & 0xff) / 255.0F,
				 (float) (iColor & 0xff) / 255.0F) & 0xffffff);
    }
    
    public void init(Tools tools, M.Info info, Res res, Res res_1_) {
	this.info = info;
	mg = info.m;
	this.res = res_1_;
	config = res;
	this.tools = tools;
	this.setDimension(new Dimension((int) (42.0F * LComponent.Q),
					(int) (42.0F * LComponent.Q)),
			  new Dimension((int) (112.0F * LComponent.Q),
					(int) (202.0F * LComponent.Q)),
			  new Dimension((int) (300.0F * LComponent.Q),
					(int) (300.0F * LComponent.Q)));
    }
    
    public void paint2(Graphics graphics) {
	try {
	    if (cls == null) {
		cls = new Color[sizePalette];
		for (int i = 0; i < sizePalette; i += 2) {
		    cls[i] = new Color(config.getP("color_" + (i + 2),
						   (i >= clsDef.length
						    ? 16777215 : clsDef[i])));
		    cls[i + 1]
			= new Color(config.getP("color_" + (i + 1),
						(i >= clsDef.length ? 16777215
						 : clsDef[i])));
		}
	    }
	    Dimension dimension = this.getSize();
	    int i = Math.min((dimension.height - 1) / 10, 64);
	    int i_2_ = (int) ((float) i * 1.5F);
	    int i_3_ = i <= 12 ? 0 : 2;
	    int i_4_ = 0;
	    if (cls.length != 0) {
		/* empty */
	    }
	    int i_5_ = 0;
	    int i_6_ = 0;
	    for (int i_7_ = 0; i_7_ < cls.length; i_7_++) {
		graphics.setColor(cls[i_4_++]);
		graphics.fillRect(i_5_ + 1, i_6_ + 1, i_2_ - 1 - i_3_,
				  i - 1 - i_3_);
		graphics.setColor(Awt.cF);
		graphics.drawRect(i_5_, i_6_, i_2_ - i_3_, i - i_3_);
		if (selPalette == i_7_) {
		    graphics.setColor(Awt.cFSel);
		    graphics.drawRect(i_5_ + 1, i_6_ + 1, i_2_ - i_3_ - 2,
				      i - i_3_ - 2);
		}
		if (i_5_ == 0)
		    i_5_ += i_2_;
		else {
		    i_5_ = 0;
		    i_6_ += i;
		}
	    }
	    int i_8_ = i_2_ * 2;
	    i_6_ = pBar(graphics, i_8_, 0, i);
	    graphics.setColor(this.getBackground());
	    graphics.fillRect(i_5_ + i_8_, i_6_, dimension.width - i_8_,
			      dimension.height - i_6_);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private int pBar(Graphics graphics, int i, int i_9_, int i_10_) {
	Dimension dimension = this.getSize();
	int i_11_ = dimension.width - i - 1;
	if (dimension.height - i_9_ != 0) {
	    /* empty */
	}
	Color color = this.getBackground();
	Color color_12_ = Awt.cFore;
	boolean bool = mg.isText();
	int i_13_ = bool ? 255 : info.getPenMask()[mg.iPenM].length;
	int i_14_ = Math.min(i_10_ * 6, i_13_ * 8 + 1);
	int i_15_ = mg.iSize;
	i_15_ = i_15_ <= 0 ? 0 : i_15_ >= i_13_ ? i_13_ - 1 : i_15_;
	mg.iSize = i_15_;
	String string
	    = (bool ? String.valueOf(mg.iSize) + "pt"
	       : (String.valueOf((int) Math.sqrt((double) info.getPenMask()
							  [mg.iPenM]
							  [mg.iSize].length))
		  + "px"));
	graphics.setColor(Awt.cF);
	graphics.drawRect(i, i_9_, i_11_, i_14_);
	int i_16_
	    = (int) ((float) i_14_ * ((float) (i_15_ + 1) / (float) i_13_));
	graphics.setColor(cls[selPalette]);
	graphics.fillRect(i + 1, i_9_ + 1, i_11_ - 1, i_16_ - 1);
	graphics.setColor(color);
	graphics.fillRect(i + 1, i_9_ + 1 + i_16_, i_11_ - 1,
			  i_14_ - i_16_ - 1);
	graphics.setColor(color_12_);
	graphics.setFont(Awt.getDefFont());
	graphics.setXORMode(color);
	graphics.drawString(string, i + 2, i_9_ + i_14_ - 2);
	graphics.setPaintMode();
	if (clFont == null || clFont.getSize() != Math.max(i_10_ - 2, 1))
	    clFont = new Font("sansserif", 0, Math.max(i_10_ - 2, 1));
	graphics.setFont(clFont);
	int i_17_ = iColor << 8 | mg.iAlpha;
	int i_18_ = 24;
	i_9_ += i_14_;
	for (int i_19_ = 0; i_19_ < 4; i_19_++) {
	    graphics.setColor(Awt.cF);
	    graphics.drawRect(i, i_9_ + 1, i_11_, i_10_ - 2);
	    graphics.setColor(Color.white);
	    graphics.fillRect(i + 1, i_9_ + 2, i_11_ - 1, 1);
	    graphics.fillRect(i + 1, i_9_ + 3, 1, i_10_ - 4);
	    i_16_ = (int) ((float) (i_11_ - 2)
			   * ((float) (i_17_ >>> i_18_ & 0xff) / 255.0F));
	    graphics.setColor(clRGB[isRGB][i_19_]);
	    graphics.fillRect(i + 2, i_9_ + 3, i_16_, i_10_ - 4);
	    graphics.setColor(Color.gray);
	    graphics.fillRect(i + 1 + i_16_, i_9_ + 3, 1, i_10_ - 4);
	    graphics.setColor(color);
	    graphics.fillRect(i + 2 + i_16_, i_9_ + 3, i_11_ - i_16_ - 2,
			      i_10_ - 4);
	    graphics.setColor(color_12_);
	    graphics.drawString((String.valueOf(clValue[isRGB][i_19_])
				 + (i_17_ >>> i_18_ & 0xff)),
				i + 2, i_9_ + i_10_ - 2);
	    i_9_ += i_10_;
	    i_18_ -= 8;
	}
	return i_9_;
    }
    
    public void pMouse(MouseEvent mouseevent) {
	int i = mouseevent.getID();
	int i_20_ = mouseevent.getX();
	int i_21_ = mouseevent.getY();
	Dimension dimension = this.getSize();
	int i_22_ = (dimension.height - 1) / 10;
	int i_23_ = (int) ((float) i_22_ * 1.5F);
	int i_24_ = i_23_ * 2;
	boolean bool = Awt.isR(mouseevent);
	boolean bool_25_ = mg.isText();
	int i_26_ = bool_25_ ? 255 : info.getPenMask()[mg.iPenM].length;
	int i_27_ = Math.min(i_22_ * 6, i_26_ * 8 + 1);
	if (i_20_ <= i_24_ && i == 501) {
	    iDrag = -1;
	    int i_28_ = Math.min(i_21_ / i_22_ * 2 + i_20_ / i_23_, 19);
	    selPalette = i_28_;
	    int i_29_ = bool ? getRGB() : cls[i_28_].getRGB();
	    if (mouseevent.isShiftDown() && i_28_ < clsDef.length)
		i_29_ = clsDef[i_28_];
	    tools.setRGB(i_29_);
	} else {
	    boolean bool_30_ = false;
	    switch (i) {
	    case 501:
		if (i_21_ < i_27_) {
		    if (bool) {
			tools.setField(this, "iPenM", "penm_", i_20_, i_21_);
			return;
		    }
		    iDrag = 0;
		} else {
		    if (bool) {
			changeRH();
			return;
		    }
		    iDrag = (i_21_ - i_27_) / i_22_;
		    iDrag = (iDrag <= 0 ? 0 : iDrag >= 3 ? 3 : iDrag) + 1;
		}
		bool_30_ = true;
		break;
	    case 502:
		iDrag = -1;
		break;
	    case 506:
		if (iDrag >= 0)
		    bool_30_ = true;
		break;
	    }
	    if (bool_30_) {
		if (iDrag == 0)
		    setLineSize((int) ((float) i_21_ / (float) i_27_
				       * (float) i_26_));
		else {
		    int i_31_
			= (int) ((float) (i_20_ - i_24_)
				 / (float) (dimension.width - i_24_) * 255.0F);
		    int i_32_ = 24 - 8 * (iDrag - 1);
		    int i_33_ = iColor << 8 | mg.iAlpha;
		    i_33_ = (i_33_ & (0xffffffff ^ 255 << i_32_)
			     | Math.max(Math.min(i_31_, 255), 0) << i_32_);
		    iColor = i_33_ >>> 8;
		    mg.iAlpha = Math.max(i_33_ & 0xff, 1);
		    i_33_ = iColor;
		    tools.setRGB(getRGB());
		    boolean bool_34_ = iColor == i_33_;
		    iColor = i_33_;
		    if (bool_34_)
			this.repaint();
		}
	    }
	}
    }
    
    public void setC(String string) {
	try {
	    BufferedReader bufferedreader
		= new BufferedReader(new StringReader(string));
	    int i = 0;
	    while ((string = bufferedreader.readLine()) != null) {
		if (i < cls.length)
		    cls[i] = Color.decode(string);
		if (i < clsDef.length)
		    clsDef[i++] = cls[i].getRGB() & 0xffffff;
	    }
	    this.repaint();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void setColor(int i) {
	i &= 0xffffff;
	boolean bool = getRGB() != i;
	if (isRGB == 1)
	    iColor = i;
	else {
	    Color.RGBtoHSB(i >>> 16, i >>> 8 & 0xff, i & 0xff, fhsb);
	    iColor
		= ((int) (fhsb[0] * 255.0F) << 16
		   | (int) (fhsb[1] * 255.0F) << 8 | (int) (fhsb[2] * 255.0F));
	}
	if ((cls[selPalette].getRGB() & 0xffffff) != i) {
	    cls[selPalette] = new Color(mg.iColor);
	    bool = true;
	}
	if (bool)
	    this.repaint();
    }
    
    public void setLineSize(int i) {
	int i_35_ = mg.isText() ? 255 : info.getPenMask()[mg.iPenM].length;
	int i_36_ = mg.iSize;
	mg.iSize = Math.min(Math.max(0, i), i_35_);
	if (i_36_ != mg.iSize)
	    this.repaint();
    }
}
