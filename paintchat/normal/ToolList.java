/* ToolList - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat.normal;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import paintchat.M;
import paintchat.Res;

public class ToolList
{
    private Tools tools;
    private Res res;
    private Res cnf;
    boolean isField;
    boolean isClass;
    boolean isDirect;
    boolean isMask;
    boolean isEraser;
    boolean isSelect;
    boolean isDrawList;
    boolean isIm;
    String strField;
    private int quality = 1;
    private boolean isDrag = false;
    public int iSelect;
    public int iSelectList;
    public boolean isList;
    private M info = null;
    private M[] mgs = null;
    private int[] items = null;
    private String[] strs = null;
    private String[] strings;
    private int length;
    private ToolList[] lists;
    private Font font;
    private int base = 0;
    private Image image;
    private int imW;
    private int imH;
    private int imIndex;
    public Rectangle r = new Rectangle();
    /*synthetic*/ static Class class$0;
    
    private void dImage(Graphics graphics, Color color, int i, int i_0_) {
	int i_1_ = r.height;
	int i_2_ = r.width;
	graphics.setColor(color);
	graphics.fillRect(2, i + 2, r.width - 4, i_1_ - 4);
	if (isMask) {
	    graphics.setColor(new Color(info.iColorMask));
	    graphics.fillRect(i_2_ - imW - 3, i + 3, imW, (i_1_ - 4) / 2);
	}
	if (isIm && image != null && i_0_ < image.getHeight(null) / imH) {
	    int i_3_ = imIndex * imW;
	    int i_4_ = i_0_ * imH;
	    int i_5_ = r.x + 2;
	    int i_6_ = i + 2;
	    graphics.drawImage(image, i_5_, i_6_, i_5_ + i_2_ - 4,
			       i_6_ + i_1_ - 4, i_3_, i_4_, i_3_ + imW,
			       i_4_ + imH, color, null);
	}
    }
    
    private void drag(int i, int i_7_) {
	if (isDrag) {
	    int i_8_ = len();
	    int i_9_ = r.width;
	    int i_10_ = r.height;
	    int i_11_ = i_7_ / (i_10_ - 2) - 1;
	    isList = true;
	    int i_12_ = iSelectList;
	    if (i < 0 || i >= i_9_ || i_11_ < 0 || i_11_ >= i_8_) {
		iSelectList = -1;
		i_11_ = -1;
	    } else
		iSelectList = i_11_;
	    if (isList && !isDrawList) {
		isDrawList = true;
		repaint();
	    }
	    if (i_12_ != i_11_ && isList) {
		Graphics graphics = tools.primary();
		if (i_12_ >= 0) {
		    graphics.setColor(tools.clFrame);
		    graphics.drawRect(r.x + 1,
				      r.y + (i_10_ - 3) * (i_12_ + 1) + 2,
				      i_9_ - 3, i_10_ - 3);
		}
		if (i_11_ >= 0) {
		    graphics.setColor(tools.clSel);
		    graphics.drawRect(r.x + 1,
				      r.y + (i_10_ - 3) * (i_11_ + 1) + 2,
				      i_9_ - 3, i_10_ - 3);
		}
	    }
	}
    }
    
    private int getValue() {
	try {
	    int i;
	    if (isField) {
		Class var_class = class$0;
		if (var_class == null) {
		    Class var_class_13_;
		    try {
			var_class_13_ = Class.forName("paintchat.M");
		    } catch (ClassNotFoundException classnotfoundexception) {
			NoClassDefFoundError noclassdeffounderror
			    = new NoClassDefFoundError();
			noclassdeffounderror.NoClassDefFoundError
			    (classnotfoundexception.getMessage());
			throw noclassdeffounderror;
		    }
		    var_class = class$0 = var_class_13_;
		}
		i = var_class.getField(strField).getInt(info);
	    } else
		i = iSelect;
	    return i;
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	    return 0;
	}
    }
    
    public void init(Tools tools, Res res, Res res_14_, M m,
		     ToolList[] toollists, int i) {
	try {
	    this.tools = tools;
	    this.res = res;
	    cnf = res_14_;
	    lists = toollists;
	    info = m;
	    String string = "t0" + i + "_";
	    isDirect = res_14_.getP(string + "direct", false);
	    isClass = res_14_.getP(string + "class", false);
	    isEraser = res_14_.getP(string + "iseraser", false);
	    isIm = res_14_.getP(string + "image", true);
	    strField = res_14_.getP(string + "field", (String) null);
	    isField = strField != null;
	    if (isField && strField.equals("iMask"))
		isMask = true;
	    string = "t0" + i;
	    int i_15_;
	    for (i_15_ = 0; res_14_.getP(string + i_15_) != null; i_15_++) {
		/* empty */
	    }
	    strings = new String[i_15_];
	    for (int i_16_ = 0; i_16_ < i_15_; i_16_++) {
		String string_17_ = string + i_16_;
		if (isField) {
		    if (items == null)
			items = new int[i_15_];
		    items[i_16_] = res_14_.getP(string_17_, 0);
		} else if (isClass) {
		    if (strs == null)
			strs = new String[i_15_];
		    strs[i_16_] = res_14_.getP(string_17_);
		} else {
		    if (mgs == null)
			mgs = new M[i_15_];
		    (mgs[i_16_] = new M()).set(res_14_.getP(string_17_));
		}
		strings[i_16_] = res.res(string_17_);
		res_14_.remove(string_17_);
		res.remove(string_17_);
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private int len() {
	return (mgs == null
		? items == null ? strs == null ? 0 : strs.length : items.length
		: mgs.length);
    }
    
    public void paint(Graphics graphics, Graphics graphics_18_) {
	try {
	    if (graphics != null && graphics_18_ != null) {
		int i = r.width;
		int i_19_ = r.height;
		int i_20_ = r.x;
		int i_21_ = r.y;
		int i_22_ = len();
		int i_23_ = i_19_ - 2;
		if (isList) {
		    int i_24_ = i_21_ + i_19_ - 2;
		    Color color = isDirect ? tools.clB2 : tools.clB;
		    for (int i_25_ = 0; i_25_ < i_22_; i_25_++) {
			dImage(graphics, color, i_24_, i_25_);
			graphics.setColor(tools.clText);
			if (i_25_ < strings.length)
			    graphics.drawString(strings[i_25_], i_20_ + 4,
						i_24_ + base);
			i_24_ += i_23_ - 1;
		    }
		    i_24_ = i_21_ + i_23_;
		    graphics.setColor(tools.clFrame);
		    graphics.drawRect(i_20_, i_24_, i - 1,
				      (i_23_ - 1) * i_22_ + 2);
		    for (int i_26_ = 0; i_26_ < i_22_; i_26_++) {
			graphics.drawRect(i_20_ + 1, i_24_ + 1, i - 3,
					  i_19_ - 3);
			i_24_ += i_23_ - 1;
		    }
		}
		int i_27_ = getValue();
		if (isField) {
		    int i_28_ = items.length;
		    for (int i_29_ = 0; i_29_ < i_28_; i_29_++) {
			if (items[i_29_] == i_27_) {
			    i_27_ = i_29_;
			    break;
			}
		    }
		}
		dImage(graphics_18_, isDirect ? tools.clB2 : tools.clB, 0,
		       i_27_);
		graphics_18_.setColor(tools.clFrame);
		graphics_18_.drawRect(0, 0, i - 1, i_19_ - 1);
		if (isSelect) {
		    graphics_18_.setColor(tools.clSel);
		    graphics_18_.drawRect(1, 1, i - 3, i_19_ - 3);
		} else {
		    graphics_18_.setColor(tools.clBL);
		    graphics_18_.fillRect(1, 1, i - 2, 1);
		    graphics_18_.fillRect(1, 1, 1, i_19_ - 2);
		    graphics_18_.setColor(tools.clBD);
		    graphics_18_.fillRect(i - 2, 2, 1, i_19_ - 4);
		    graphics_18_.fillRect(2, i_19_ - 2, i - 3, 1);
		}
		if (i_27_ >= 0 && i_27_ < strings.length) {
		    graphics_18_.setColor(tools.clText);
		    graphics_18_.drawString(strings[i_27_], 3, base);
		}
		graphics.drawImage(tools.imBack, r.x, r.y, r.x + i,
				   r.y + i_19_, 0, 0, i, i_19_, tools.clB,
				   null);
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void pMouse(MouseEvent mouseevent) {
	int i = mouseevent.getX();
	int i_30_ = mouseevent.getY();
	int i_31_ = i - r.x;
	int i_32_ = i_30_ - r.y;
	switch (mouseevent.getID()) {
	case 501:
	    if (r.contains(i, i_30_))
		press();
	    break;
	case 506:
	    drag(i_31_, i_32_);
	    break;
	case 502:
	    release(i_31_, i_32_, r.contains(i, i_30_));
	    break;
	}
    }
    
    private void press() {
	if (!isDrag) {
	    isDrag = true;
	    iSelectList = -1;
	    if (isDirect) {
		isList = true;
		isDrawList = true;
		repaint();
	    }
	}
    }
    
    private void release(int i, int i_33_, boolean bool) {
	if (isDrag) {
	    int i_34_ = r.height;
	    int i_35_ = r.width;
	    int i_36_ = i_33_ / (i_34_ - 2) - 1;
	    boolean bool_37_ = isDrawList;
	    boolean bool_38_ = false;
	    isDrag = false;
	    isList = false;
	    if (i_36_ < 0 || i_36_ >= len() || i < 0 || i >= i_35_)
		i_36_ = -1;
	    if (i_36_ == -1) {
		if (bool) {
		    if (isSelect) {
			int i_39_ = len();
			unSelect();
			if (++iSelect >= i_39_)
			    iSelect = 0;
			select();
		    } else
			select();
		    bool_38_ = true;
		}
	    } else {
		if (isSelect)
		    unSelect();
		iSelect = i_36_;
		select();
	    }
	    iSelectList = -1;
	    isDrawList = false;
	    if (bool_37_) {
		Graphics graphics = tools.primary();
		graphics.setColor(tools.getBackground());
		graphics.fillRect(r.x - 1, r.y - 1, i_35_ + 2,
				  (i_34_ - 2) * (len() + 1) + 2);
	    }
	    if (bool_38_ || bool_37_)
		tools.mPaint(-1);
	}
    }
    
    public void repaint() {
	paint(tools.primary(), tools.getBack());
    }
    
    public void select() {
	try {
	    if (isField) {
		Class var_class = class$0;
		if (var_class == null) {
		    Class var_class_40_;
		    try {
			var_class_40_ = Class.forName("paintchat.M");
		    } catch (ClassNotFoundException classnotfoundexception) {
			NoClassDefFoundError noclassdeffounderror
			    = new NoClassDefFoundError();
			noclassdeffounderror
			    .NoClassDefFoundError
			    (classnotfoundexception.getMessage());
			throw noclassdeffounderror;
		    }
		    var_class = class$0 = var_class_40_;
		}
		var_class.getField(strField).setInt(info, items[iSelect]);
		tools.upCS();
	    } else if (isClass)
		tools.showW(strs[iSelect]);
	    else {
		if (!isDirect)
		    tools.unSelect();
		int i = info.iColor;
		int i_41_ = info.iMask;
		int i_42_ = info.iColorMask;
		int i_43_ = info.iLayer;
		int i_44_ = info.iLayerSrc;
		int i_45_ = info.iTT;
		int i_46_ = info.iSA;
		int i_47_ = info.iSS;
		info.set(mgs[iSelect]);
		info.iColor = i;
		info.iMask = i_41_;
		info.iColorMask = i_42_;
		info.iLayer = i_43_;
		info.iLayerSrc = i_44_;
		info.iTT = i_45_;
		if (i_46_ != info.iSA || i_47_ != info.iSS)
		    tools.mi.up();
		if (!isDirect)
		    isSelect = true;
	    }
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    public void setImage(Image image, int i, int i_48_, int i_49_) {
	this.image = image;
	imW = i;
	imH = i_48_;
	imIndex = i_49_;
    }
    
    public void setSize(int i, int i_50_, int i_51_) {
	r.setSize(i, i_50_);
	base = i_51_;
    }
    
    public void unSelect() {
	if (isSelect && !isDirect)
	    mgs[iSelect].set(info);
	isSelect = false;
    }
}
