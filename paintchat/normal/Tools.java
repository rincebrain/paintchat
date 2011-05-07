/* Tools - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat.normal;
import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.StringReader;

import paintchat.LO;
import paintchat.M;
import paintchat.Res;
import paintchat.SW;
import paintchat.ToolBox;

import paintchat_client.L;
import paintchat_client.Mi;

import syi.awt.Awt;
import syi.awt.LComponent;

public class Tools extends LComponent implements ToolBox, ActionListener
{
    Applet applet;
    Container parent;
    Mi mi;
    L L;
    M.Info info;
    M mg;
    Res res;
    Res config;
    private Graphics primary = null;
    private Font fDef;
    private Font fIg;
    ToolList[] list;
    private Rectangle rPaint = new Rectangle();
    private Rectangle[] rects = null;
    private int fit_w = -1;
    private int fit_h = -1;
    private int nowButton = -1;
    private int nowColor = -1;
    private int oldPen;
    Color clFrame;
    Color clB;
    Color clBD;
    Color clBL;
    Color clText;
    Color clBar;
    Color clB2;
    Color clSel;
    private boolean isWest = false;
    private boolean isLarge;
    private PopupMenu popup;
    private LComponent[] cs;
    private Window[] ws;
    private static int[] DEFC
	= { 0, 16777215, 11826549, 8947848, 16422550, 12621504, 16758527,
	    8421631, 2475977, 15197581, 15177261, 10079099, 16575714,
	    16375247 };
    private static int[] COLORS = new int[14];
    private static Color[][] clRGB;
    private static Color[][] clERGB;
    private char[][] clValue
	= { { 'H', 'S', 'B', 'A' }, { 'R', 'G', 'B', 'A' } };
    private boolean isRGB = true;
    private float[] fhsb = new float[3];
    private int iColor;
    protected Image imBack = null;
    private Graphics back;
    protected int W;
    protected int H;
    protected int IMW;
    protected int IMH;
    
    static {
	System.arraycopy(DEFC, 0, COLORS, 0, 14);
	clRGB = new Color[][] { { Color.magenta, Color.cyan, Color.white,
				  Color.lightGray },
				{ new Color(16422550), new Color(8581688),
				  new Color(8421631), Color.lightGray } };
	clERGB = new Color[2][4];
	for (int i = 0; i < 2; i++) {
	    for (int i_0_ = 0; i_0_ < 4; i_0_++)
		clERGB[i][i_0_] = clRGB[i][i_0_].darker();
	}
    }
    
    public Tools() {
	this.setTitle("tools");
	isHide = false;
	iGap = 2;
    }
    
    public void actionPerformed(ActionEvent actionevent) {
	String string = actionevent.getActionCommand();
	Menu menu = (Menu) actionevent.getSource();
	int i = 0;
	int i_1_ = menu.getItemCount();
	for (int i_2_ = 0; i_2_ < i_1_; i_2_++) {
	    if (menu.getItem(i_2_).getLabel().equals(string)) {
		i = i_2_;
		break;
	    }
	}
	switch (Integer.parseInt(menu.getLabel())) {
	case 2:
	    mg.iPenM = i;
	    setLineSize(0, mg.iSize);
	    this.repaint();
	}
    }
    
    public void addC(Object object) {
	if (object instanceof LComponent) {
	    if (cs != null) {
		for (int i = 0; i < cs.length; i++) {
		    if (cs[i] == object)
			return;
		}
	    } else {
		cs = new LComponent[] { (LComponent) object };
		return;
	    }
	    int i = cs.length;
	    LComponent[] lcomponents = new LComponent[i + 1];
	    System.arraycopy(cs, 0, lcomponents, 0, i);
	    lcomponents[i] = (LComponent) object;
	    cs = lcomponents;
	} else {
	    if (ws != null) {
		for (int i = 0; i < ws.length; i++) {
		    if (ws[i] == object)
			return;
		}
	    } else {
		ws = new Window[] { (Window) object };
		return;
	    }
	    int i = ws.length;
	    Window[] windows = new Window[i + 1];
	    System.arraycopy(cs, 0, windows, 0, i);
	    windows[i] = (Window) object;
	    ws = windows;
	}
    }
    
    public Graphics getBack() {
	if (imBack == null) {
	    Tools tools_3_;
	    //MONITORENTER (tools_3_ = this);
//	    MISSING MONITORENTER
	    synchronized (tools_3_) {
		if (imBack == null) {
		    try {
			int i = 0;
			for (int i_4_ = 0; i_4_ < list.length; i_4_++)
			    i = Math.max(i, list[i_4_].r.height);
			i = Math.max(i, 32) * 2;
			imBack = this.createImage(W + 1, i + 1);
			back = imBack.getGraphics();
		    } catch (RuntimeException runtimeexception) {
			imBack = null;
			back = null;
		    }
		}
	    }
	}
	if (back != null)
	    back.setFont(fDef);
	return back;
    }
    
    public String getC() {
	try {
	    int[] is = COLORS == null ? DEFC : COLORS;
	    StringBuffer stringbuffer = new StringBuffer();
	    for (int i = 0; i < is.length; i++) {
		if (i != 0)
		    stringbuffer.append('\n');
		stringbuffer.append("#" + Integer.toHexString
					      (~0xffffff | is[i] & 0xffffff)
					      .substring
					      (2).toUpperCase());
	    }
	    return stringbuffer.toString();
	} catch (Throwable throwable) {
	    return null;
	}
    }
    
    public LComponent[] getCs() {
	return cs;
    }
    
    public Dimension getCSize() {
	Dimension dimension = parent.getSize();
	return new Dimension((dimension.width - this.getSizeW().width
			      - mi.getGapW()),
			     dimension.height);
    }
    
    private int getRGB() {
	if (!isRGB)
	    return (Color.HSBtoRGB((float) (iColor >>> 16 & 0xff) / 255.0F,
				   (float) (iColor >>> 8 & 0xff) / 255.0F,
				   (float) (iColor & 0xff) / 255.0F)
		    & 0xffffff);
	return iColor & 0xffffff;
    }
    
    private int i(String string, int i) {
	return config.getP(string, i);
    }
    
    public boolean i(String string, boolean bool) {
	return config.getP(string, bool);
    }
    
    public void init(Container container, Applet applet, Res res, Res res_5_,
		     Mi mi) {
	this.applet = applet;
	parent = container;
	this.res = res_5_;
	config = res;
	info = mi.info;
	this.mi = mi;
	mg = info.m;
	W = i("tool_width", 48) + 4;
	H = i("tool_height", 470);
	for (int i = 0; i < DEFC.length; i += 2) {
	    DEFC[i] = res.getP("color_" + (i + 2), DEFC[i]);
	    DEFC[i + 1] = res.getP("color_" + (i + 1), DEFC[i + 1]);
	}
	System.arraycopy(DEFC, 0, COLORS, 0, 14);
	sCMode(false);
	String string = "tool_color_";
	this.setBackground(new Color(i(string + "bk",
				       i(string + "back", 10066363))));
	clB = new Color(i(string + "button", -1515602));
	clB2 = new Color(i(string + "button" + '2', 16308906));
	clFrame = new Color(i(string + "frame", 0));
	clText = new Color(i(string + "text", 7811891));
	clBar = new Color(i(string + "bar", 14540287));
	clSel = new Color(i(string + "iconselect",
			    i("color_iconselect", 15610675)));
	clBL
	    = new Color(i(string + "button" + "_hl", clB.brighter().getRGB()));
	clBD = new Color(i(string + "button" + "_dk", clB.darker().getRGB()));
	isWest = "left".equals(res.getP("tool_align"));
	isLarge = res.getP("icon_enlarge", true);
	container.getSize();
	this.setDimension(new Dimension(W, 42), new Dimension(W, H),
			  new Dimension(W, (int) ((float) H * 1.25F)));
	list[0].select();
	container.add(this, 0);
	addC(this);
	if (res.getP("tool_layer", true)) {
	    L = new L(mi, this, this.res, res);
	    L.setVisible(false);
	    addC(L);
	}
    }
    
    private int isClick(int i, int i_6_) {
	if (rects == null)
	    return -1;
	int i_7_ = rects.length;
	int i_8_ = list.length;
	for (int i_9_ = 0; i_9_ < i_8_; i_9_++) {
	    if (list[i_9_].r.contains(i, i_6_))
		return i_9_;
	}
	for (int i_10_ = 0; i_10_ < i_7_; i_10_++) {
	    Rectangle rectangle = rects[i_10_];
	    if (rectangle != null && rectangle.contains(i, i_6_))
		return i_10_ + i_8_;
	}
	return -1;
    }
    
    public void lift() {
	if (nowButton != 0) {
	    /* empty */
	}
	unSelect();
	nowButton = -1;
	this.repaint();
    }
    
    private void makeList() {
	Image image = null;
	int i = 0;
	int i_11_ = 0;
	clB.brighter();
	clB.darker();
	try {
	    String string = "res/s.gif";
	    Image image_12_ = this.getToolkit()
				  .createImage((byte[]) config.getRes(string));
	    Awt.wait(image_12_);
	    image = image_12_;
	    config.remove(string);
	    int i_13_ = i("tool_icon_count", 7);
	    list = new ToolList[i_13_];
	    i = image_12_.getWidth(null) / i_13_;
	    i_11_ = i("tool_icon_height", image_12_.getHeight(null) / 9);
	    IMW = i;
	    IMH = i_11_;
	} catch (RuntimeException runtimeexception) {
	    /* empty */
	}
	for (int i_14_ = 0; i_14_ < list.length; i_14_++) {
	    ToolList toollist;
	    list[i_14_] = toollist = new ToolList();
	    toollist.init(this, res, config, mg, list, i_14_);
	    toollist.setImage(image, i, i_11_, i_14_);
	}
    }
    
    private void menu(int i, int i_15_, int i_16_) {
	if (popup == null) {
	    popup = new PopupMenu(String.valueOf(i_16_));
	    popup.addActionListener(this);
	} else {
	    this.remove(popup);
	    popup.removeAll();
	    popup.setLabel(String.valueOf(i_16_));
	}
	switch (i_16_) {
	case 0:
	    for (int i_17_ = 0; i_17_ < 11; i_17_++) {
		String string
		    = String.valueOf(i_17_ == 0 ? 5 : i_17_ * 10) + '%';
		if (mg.iTT - 1 == i_17_)
		    popup.add(new CheckboxMenuItem(string, true));
		else
		    popup.add(string);
	    }
	    break;
	case 1: {
	    int i_18_ = config.getInt("tt_size");
	    for (int i_19_ = 0; i_19_ < i_18_; i_19_++) {
		String string = res.res("t042" + i_19_);
		if (mg.iTT - 12 == i_19_)
		    popup.add(new CheckboxMenuItem(string, true));
		else
		    popup.add(string);
	    }
	    break;
	}
	case 2:
	    for (int i_20_ = 0; i_20_ < 16; i_20_++) {
		String string = (String) res.get((Object) ("penm_" + i_20_));
		if (string == null)
		    break;
		if (mg.iPenM == i_20_)
		    popup.add(new CheckboxMenuItem(string, true));
		else
		    popup.add(string);
	    }
	    break;
	}
	this.add(popup);
	popup.show(this, i, i_15_);
    }
    
    public void mPaint(int i) {
	Rectangle rectangle;
	if (i == -1) {
	    rectangle = rPaint;
	    rectangle.setSize(this.getSize());
	    rectangle.setLocation(0, 0);
	} else if (i < list.length) {
	    rectangle = rPaint;
	    rectangle.setBounds(list[i].r);
	} else
	    rectangle = rects[i - list.length];
	mPaint(primary(), rectangle);
    }
    
    public void mPaint(int i, int i_21_, int i_22_, int i_23_) {
	Rectangle rectangle = rPaint;
	rectangle.setBounds(i, i_21_, i_22_, i_23_);
	mPaint(primary(), rectangle);
    }
    
    private void mPaint(Graphics graphics, Rectangle rectangle) {
	if (rects != null && graphics != null && list != null) {
	    Graphics graphics_24_ = getBack();
	    if (rectangle == null) {
		rectangle = graphics.getClipBounds();
		if (rectangle == null || rectangle.isEmpty())
		    rectangle = new Rectangle(this.getSize());
	    }
	    if (!rectangle.isEmpty()) {
		int i = list.length;
		Dimension dimension = this.getSize();
		graphics_24_.setFont(fDef);
		for (int i_25_ = 0; i_25_ < i; i_25_++) {
		    if (list[i_25_].r.intersects(rectangle))
			list[i_25_].paint(graphics, graphics_24_);
		}
		graphics_24_.setFont(fIg);
		int i_26_ = isRGB ? 1 : 0;
		for (int i_27_ = 0; i_27_ < rects.length; i_27_++) {
		    Rectangle rectangle_28_ = rects[i_27_];
		    int i_29_ = i_27_ + i;
		    if (rectangle_28_.intersects(rectangle)) {
			if (i_27_ < 14) {
			    Color color = new Color(COLORS[i_27_]);
			    graphics_24_.setColor(i_27_ == nowColor
						  ? color.darker()
						  : color.brighter());
			    graphics_24_.drawRect(1, 1,
						  rectangle_28_.width - 2,
						  rectangle_28_.height - 2);
			    graphics_24_.setColor(color);
			    graphics_24_.fillRect(2, 2,
						  rectangle_28_.width - 3,
						  rectangle_28_.height - 3);
			    graphics_24_
				.setColor(nowColor == i_27_ ? clSel : clFrame);
			} else {
			    switch (i_27_) {
			    case 14:
			    case 15:
			    case 16:
			    case 17: {
				int i_30_ = i_27_ - 14;
				int i_31_ = rectangle_28_.height;
				int i_32_
				    = (i_27_ == 17 ? mg.iAlpha
				       : iColor >>> (2 - i_30_) * 8 & 0xff);
				int i_33_
				    = (int) ((float) (dimension.width - 10 - 2)
					     / 255.0F * (float) i_32_);
				graphics_24_.setColor(clB2);
				graphics_24_.fillRect(0, 0, 5, i_31_ - 1);
				graphics_24_.fillRect(rectangle_28_.width - 5,
						      1, 5, i_31_ - 1);
				graphics_24_.setColor(clFrame);
				graphics_24_.fillRect(5, 1, 1, i_31_ - 1);
				graphics_24_.fillRect((rectangle_28_.width - 5
						       - 1),
						      1, 1, i_31_ - 1);
				if (i_33_ > 0) {
				    graphics_24_.setColor(clRGB[i_26_][i_30_]);
				    graphics_24_.fillRect(6, 1, i_33_,
							  (rectangle_28_.height
							   - 2));
				}
				int i_34_
				    = rectangle_28_.width - 10 - i_33_ - 2;
				if (i_34_ > 0) {
				    graphics_24_.setColor(clBar);
				    graphics_24_.fillRect(i_33_ + 5 + 1, 1,
							  i_34_,
							  (rectangle_28_.height
							   - 2));
				    graphics_24_
					.setColor(clERGB[i_26_][i_30_]);
				    graphics_24_.fillRect(i_33_ + 5 + 1, 1, 1,
							  (rectangle_28_.height
							   - 2));
				}
				graphics_24_.setColor(clText);
				graphics_24_.drawString
				    ((String.valueOf(clValue[i_26_][i_30_])
				      + i_32_),
				     8, rectangle_28_.height - 2);
				break;
			    }
			    case 18: {
				boolean bool = mg.isText();
				Color color = new Color(getRGB());
				int i_35_ = bool ? 255 : info.getPMMax();
				int i_36_ = rectangle_28_.width - 10;
				int i_37_ = rectangle_28_.height - 2;
				graphics_24_.setColor(clB2);
				graphics_24_.fillRect(1, 1,
						      rectangle_28_.width - 2,
						      i_37_);
				if (mg.iSize >= i_35_)
				    mg.iSize = i_35_ - 1;
				graphics_24_.setColor(color);
				graphics_24_.fillRect(1, 1, i_36_,
						      (int) ((float) (mg.iSize
								      + 1)
							     / (float) i_35_
							     * (float) i_37_));
				if (info.getPenMask() != null) {
				    graphics_24_.setColor(clText);
				    graphics_24_.drawString
					(String.valueOf(mg.iSize), 6,
					 i_37_ - 1);
				    graphics_24_.setColor(clFrame);
				    graphics_24_.fillRect(i_36_, 1, 1, i_37_);
				    graphics_24_.fillRect(i_36_ + 1, i_37_ / 2,
							  8, 1);
				    graphics_24_.setColor(color);
				    for (int i_38_ = 3; i_38_ >= 1; i_38_--) {
					graphics_24_.fillRect(((rectangle_28_
								.width)
							       - 5 - i_38_),
							      i_38_ + 2,
							      i_38_ << 1, 1);
					graphics_24_.fillRect
					    (rectangle_28_.width - 5 - i_38_,
					     i_37_ - 2 - i_38_, i_38_ << 1, 1);
				    }
				    graphics_24_.fillRect((rectangle_28_.width
							   - 6),
							  5, 2, 8);
				    graphics_24_.fillRect((rectangle_28_.width
							   - 6),
							  i_37_ - 11, 2, 8);
				    break;
				}
				return;
			    }
			    case 19:
				graphics_24_.setColor(clBar);
				graphics_24_.fillRect(1, 1,
						      rectangle_28_.width - 1,
						      (rectangle_28_.height
						       - 2));
				if (info.layers != null
				    && info.layers.length > mg.iLayer) {
				    LO lo = info.layers[mg.iLayer];
				    graphics_24_.setColor(clText);
				    if (lo.name != null)
					graphics_24_.drawString
					    (lo.name, 2,
					     (rectangle_28_.height
					      - graphics_24_.getFontMetrics
						    ().getMaxDescent()
					      - 1));
				    if (lo.iAlpha == 0.0F) {
					graphics_24_.setColor(Color.red);
					graphics_24_.drawLine
					    (1, 1, rectangle_28_.width - 3,
					     rectangle_28_.height - 3);
				    }
				}
				break;
			    }
			    graphics_24_.setColor(nowButton == i_29_ ? clSel
						  : clFrame);
			}
			graphics_24_.drawRect(0, 0, rectangle_28_.width - 1,
					      rectangle_28_.height - 1);
			graphics.drawImage
			    (imBack, rectangle_28_.x, rectangle_28_.y,
			     rectangle_28_.x + rectangle_28_.width,
			     rectangle_28_.y + rectangle_28_.height, 0, 0,
			     rectangle_28_.width, rectangle_28_.height,
			     Color.white, null);
		    }
		}
	    }
	}
    }
    
    private void mPress(MouseEvent mouseevent) {
	int i = mouseevent.getX();
	int i_39_ = mouseevent.getY();
	int i_40_ = isClick(i, i_39_);
	int i_41_ = i_40_;
	boolean bool = Awt.isR(mouseevent);
	nowButton = i_40_;
	if (i_40_ >= 0) {
	    if (i_40_ - list.length < 0) {
		if (bool) {
		    if (list[i_40_].isField && list[i_40_].isMask) {
			mg.iColorMask = mg.iColor;
			mPaint(i_40_);
		    }
		    nowButton = -1;
		}
	    } else {
		i_40_ -= list.length;
		Rectangle rectangle = rects[i_40_];
		if (i_40_ - 14 < 0) {
		    if (bool)
			COLORS[i_40_] = mg.iColor;
		    else if (mouseevent.isShiftDown())
			COLORS[i_40_] = DEFC[i_40_];
		    else {
			if (nowColor != 0) {
			    /* empty */
			}
			nowColor = i_40_;
			mg.iColor = COLORS[nowColor];
			selPix(false);
			toColor(mg.iColor);
		    }
		    up();
		} else {
		    i_40_ -= 14;
		    if (i_40_ - 4 < 0) {
			int i_42_
			    = i <= 5 ? -1 : i >= rectangle.width - 5 ? 1 : 0;
			if (i_42_ != 0) {
			    nowButton = -1;
			    if (bool)
				i_42_ *= 5;
			} else if (bool) {
			    sCMode(isRGB);
			    nowButton = -1;
			    mPaint(-1);
			    return;
			}
			setRGB(i_40_, i_42_, i);
		    } else {
			i_40_ -= 4;
			if (i_40_ - 1 < 0) {
			    if (bool) {
				nowButton = -1;
				menu(i, i_39_, 2);
			    } else {
				if (i >= rectangle.x + rectangle.width - 10) {
				    setLineSize(0, Math.max((mg.iSize
							     + (((rectangle.y
								  + (rectangle
								     .height)
								  - i_39_) / 2
								 >= 10)
								? -1 : 1)),
							    0));
				    nowButton = -1;
				} else
				    setLineSize(i_39_, -1);
				mPaint(i_41_);
			    }
			} else if (--i_40_ - 1 < 0) {
			    int i_43_ = mg.iLayer;
			    if (bool) {
				LO lo = info.layers[i_43_];
				lo.iAlpha
				    = (float) (lo.iAlpha == 0.0F ? 1 : 0);
				mi.repaint();
			    } else if (L != null && !L.isVisible()) {
				if (L.getParent() == null)
				    parent.add(L, 0);
				L.setVisible(true);
			    } else {
				if (++mg.iLayer >= info.L)
				    mg.iLayer = 0;
				mg.iLayerSrc = mg.iLayer;
			    }
			    if (L != null)
				L.repaint();
			    mPaint(i_41_);
			}
		    }
		}
	    }
	}
    }
    
    public void pack() {
	try {
	    Container container = parent;
	    Dimension dimension = container.getSize();
	    setSize(Math.min(W, dimension.width),
		    Math.min(H, dimension.height));
	    if (L != null)
		L.inParent();
	    Dimension dimension_44_ = getCSize();
	    Dimension dimension_45_ = this.getSizeW();
	    if (!mi.isGUI) {
		mi.setDimension(null, dimension_44_, dimension_44_);
		dimension_44_ = mi.getSizeW();
		mi.setLocation(((dimension.width - dimension_45_.width
				 - dimension_44_.width) / 2
				+ (isWest ? dimension_45_.width : 0)),
			       (dimension.height - dimension_44_.height) / 2);
	    }
	    dimension_44_ = mi.getSizeW();
	    Point point = mi.getLocation();
	    this.setLocation((isWest
			      ? Math.max(0, point.x - dimension_45_.width - 10)
			      : Math.min(point.x + dimension_44_.width + 10,
					 (dimension.width
					  - dimension_45_.width))),
			     (dimension.height - dimension_45_.height) / 2);
	    if (cs != null) {
		for (int i = 2; i < cs.length; i++)
		    ((SW) cs[i]).mPack();
	    }
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    public void paint2(Graphics graphics) {
	try {
	    graphics.setFont(fDef);
	    mPaint(graphics, graphics.getClipBounds());
	    if (primary != null) {
		primary.dispose();
		primary = null;
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void pMouse(MouseEvent mouseevent) {
	try {
	    if (rects != null) {
		int i = mouseevent.getX();
		int i_46_ = mouseevent.getY();
		boolean bool = Awt.isR(mouseevent);
		this.getSize();
		if (list != null) {
		    for (int i_47_ = 0; i_47_ < list.length; i_47_++) {
			if (!list[i_47_].isMask || !bool)
			    list[i_47_].pMouse(mouseevent);
		    }
		}
		int i_48_ = mouseevent.getID();
		switch (i_48_) {
		case 501:
		    mPress(mouseevent);
		    break;
		case 506: {
		    int i_49_ = nowButton;
		    if (i_49_ >= list.length) {
			i_49_ -= list.length;
			if (i_49_ - 14 >= 0) {
			    i_49_ -= 14;
			    if (i_49_ - 4 < 0) {
				setRGB(i_49_, 0, i);
				mPaint(-1);
				upCS();
			    } else {
				i_49_ -= 4;
				if (i_49_ - 1 < 0) {
				    setLineSize(i_46_, -1);
				    mPaint(nowButton);
				}
				break;
			    }
			    break;
			}
			break;
		    }
		    break;
		}
		case 502:
		    nowButton = -1;
		    break;
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public Graphics primary() {
	if (primary == null) {
	    try {
		primary = this.getGraphics();
		if (primary != null) {
		    primary.setFont(fDef);
		    primary.translate(this.getGapX(), this.getGapY());
		    Dimension dimension = this.getSize();
		    primary.setClip(0, 0, dimension.width, dimension.height);
		}
	    } catch (RuntimeException runtimeexception) {
		primary = null;
	    }
	}
	return primary;
    }
    
    protected void processEvent(AWTEvent awtevent) {
	awtevent.getID();
	switch (awtevent.getID()) {
	case 100: {
	    int i = this.getLocation().x;
	    int i_50_ = (this.getParent().getSize().width / 2
			 - this.getSize().width / 2);
	    if (i < i_50_ && !isWest) {
		isWest = true;
		pack();
	    } else if (i >= i_50_ && isWest) {
		isWest = false;
		pack();
	    }
	    break;
	}
	case 101:
	case 102:
	    if (primary != null) {
		primary.dispose();
		primary = null;
	    }
	    break;
	}
	super.processEvent(awtevent);
    }
    
    private void sCMode(boolean bool) {
	isRGB = !bool;
	toColor(mg.iColor);
    }
    
    public void selPix(boolean bool) {
	if (list != null) {
	    int i = list.length;
	    ToolList toollist = null;
	    ToolList toollist_51_ = null;
	    for (int i_52_ = 0; i_52_ < i; i_52_++) {
		ToolList toollist_53_ = list[i_52_];
		if (toollist_53_.isEraser)
		    toollist_51_ = toollist_53_;
		if (toollist_53_.isSelect)
		    toollist = toollist_53_;
	    }
	    if (bool) {
		if (toollist != toollist_51_) {
		    unSelect();
		    toollist_51_.select();
		    mPaint(-1);
		}
	    } else if (toollist == toollist_51_) {
		unSelect();
		list[oldPen].select();
		mPaint(-1);
	    }
	}
    }
    
    public void setARGB(int i) {
	mg.iAlpha = i >>> 24;
	i &= 0xffffff;
	mg.iColor = i;
	toColor(i);
	mPaint(-1);
	upCS();
    }
    
    public void setC(String string) {
	try {
	    BufferedReader bufferedreader
		= new BufferedReader(new StringReader(string));
	    int i = 0;
	    while ((string = bufferedreader.readLine()) != null
		   && string.length() > 0)
		DEFC[i++] = Integer.decode(string).intValue();
	    System.arraycopy(DEFC, 0, COLORS, 0, COLORS.length);
	    this.repaint();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void setLineSize(int i) {
	setLineSize(0, Math.max(i, 0));
	mPaint(list.length + 5);
    }
    
    public void setLineSize(int i, int i_54_) {
	int i_55_ = info.getPMMax();
	Rectangle rectangle = rects[18];
	if (rectangle.height != 0) {
	    /* empty */
	}
	if (i_54_ == -1)
	    i_54_ = (int) ((float) (i - rectangle.y) / (float) rectangle.height
			   * (float) i_55_);
	i_54_ = i_54_ <= 0 ? 0 : i_54_ >= i_55_ ? i_55_ - 1 : i_54_;
	mg.iSize = i_54_;
    }
    
    private void setRGB(int i, int i_56_, int i_57_) {
	int i_58_ = i == 3 ? 24 : (2 - i) * 8;
	int i_59_ = mg.iAlpha << 24 | iColor;
	int i_60_;
	if (i_56_ != 0) {
	    i_60_ = i_59_ >>> i_58_ & 0xff;
	    i_60_ += i_56_;
	} else {
	    Rectangle rectangle = rects[14 + i];
	    i_57_ = i_57_ - rectangle.x - 4;
	    i_60_ = (i_57_ > 0
		     ? (int) ((float) i_57_ / (float) (W - 8) * 255.0F) : 0);
	}
	i_60_ = i_60_ <= 0 ? 0 : i_60_ >= 255 ? 255 : i_60_;
	int i_61_ = 255 << i_58_;
	i_61_ ^= 0xffffffff;
	i_59_ = i_59_ & i_61_ | i_60_ << i_58_;
	iColor = i_59_ & 0xffffff;
	mg.iColor = getRGB();
	mg.iAlpha = Math.max(i_59_ >>> 24, 1);
	mPaint(i);
	if (nowColor >= 0)
	    COLORS[nowColor] = mg.iColor;
	mPaint(-1);
	upCS();
    }
    
    public void setSize(int i, int i_62_) {
	if (applet == null)
	    super.setSize(i, i_62_);
	else if (i != fit_w || i_62_ != fit_h) {
	    Tools tools_63_;
	    //MONITORENTER (tools_63_ = this);
//	    MISSING MONITORENTER
	    synchronized (tools_63_) {
		fit_w = i;
		fit_h = i_62_;
		if (list == null)
		    makeList();
		if (rects == null) {
		    rects = new Rectangle[20];
		    for (int i_64_ = 0; i_64_ < rects.length; i_64_++)
			rects[i_64_] = new Rectangle();
		}
		Rectangle[] rectangles = rects;
		this.getSize();
		if ((float) i / (float) W != 0) {
		    /* empty */
		}
		float f = (float) i_62_ / (float) H;
		int i_65_ = (int) ((float) (IMH + 4) * f);
		if (!isLarge)
		    i_65_ = Math.min(IMH + 4, i_65_);
		int i_66_ = Math.min((i_62_ - (i_65_ + 1) * list.length
				      - (int) (16.0F * f * 4.0F)
				      - (int) (33.0F * f) - 3) / 8,
				     (i - 1) / 2);
		fIg = new Font("sansserif", 0, (int) ((float) i_66_ * 0.475F));
		fDef = new Font("sansserif", 0, (int) ((float) i_65_ * 0.43F));
		FontMetrics fontmetrics = this.getFontMetrics(fDef);
		int i_67_ = i_65_ - fontmetrics.getMaxDescent() - 2;
		boolean bool = false;
		int i_68_ = 0;
		for (int i_69_ = 0; i_69_ < list.length; i_69_++) {
		    list[i_69_].r.setLocation(0, i_68_);
		    list[i_69_].setSize(W, i_65_, i_67_);
		    i_68_ += i_65_ + 1;
		}
		int i_70_ = (i - 1) / 2;
		int i_71_;
		for (i_71_ = 0; i_71_ < 14; i_71_++) {
		    Rectangle rectangle = rectangles[i_71_];
		    rectangle.setBounds(i_71_ % 2 == 1 ? i_70_ + 1 : 0, i_68_,
					i_71_ % 2 == 1 ? i - i_70_ - 1 : i_70_,
					i_66_);
		    if (i_71_ % 2 == 1)
			i_68_ += i_66_ + 1;
		}
		i_70_ = (int) (15.0F * f);
		for (/**/; i_71_ < 18; i_71_++) {
		    Rectangle rectangle = rectangles[i_71_];
		    rectangle.setBounds(0, i_68_, i, i_70_);
		    i_68_ += i_70_ + 1;
		}
		i_70_ = (int) (32.0F * f);
		Rectangle rectangle = rectangles[i_71_++];
		rectangle.setBounds(0, i_68_, i, i_70_);
		i_68_ += i_70_ + 1;
		i_70_ = Math.min(i_62_ - i_68_, i_66_);
		rectangle = rectangles[i_71_++];
		rectangle.setBounds(0, i_62_ - i_70_, i, i_70_);
		i_68_ += i_70_ + 1;
		super.setSize(i, i_62_);
	    }
	}
    }
    
    public void showW(String string) {
	if (cs != null) {
	    for (int i = 0; i < cs.length; i++) {
		if (cs[i].getClass().getName().equals(string)) {
		    if (!cs[i].isVisible())
			cs[i].setVisible(true);
		    return;
		}
	    }
	}
	if (ws != null) {
	    for (int i = 0; i < ws.length; i++) {
		if (ws[i].getClass().getName().equals(string)) {
		    if (!ws[i].isVisible())
			ws[i].setVisible(true);
		    return;
		}
	    }
	}
	try {
	    SW sw = (SW) Class.forName(string).newInstance();
	    if (sw instanceof Window) {
		addC(sw);
		sw.mSetup(this, info, mi.user, mg, res, config);
	    } else {
		LComponent lcomponent = (LComponent) sw;
		addC(lcomponent);
		lcomponent.setVisible(false);
		sw.mSetup(this, info, mi.user, mg, res, config);
		parent.add(lcomponent, 0);
		sw.mPack();
		lcomponent.setVisible(true);
	    }
	} catch (Throwable throwable) {
	    mi.alert(throwable.getLocalizedMessage(), false);
	}
    }
    
    private void toColor(int i) {
	if (!isRGB) {
	    Color.RGBtoHSB(i >>> 16 & 0xff, i >>> 8 & 0xff, i & 0xff, fhsb);
	    iColor
		= (mg.iAlpha << 24 | (int) (fhsb[0] * 255.0F) << 16
		   | (int) (fhsb[1] * 255.0F) << 8 | (int) (fhsb[2] * 255.0F));
	} else
	    iColor = mg.iAlpha << 24 | i;
    }
    
    public void unSelect() {
	for (int i = 0; i < list.length; i++) {
	    ToolList toollist = list[i];
	    if (toollist.isSelect) {
		if (!toollist.isEraser)
		    oldPen = i;
		toollist.unSelect();
	    }
	}
    }
    
    public void up() {
	try {
	    mPaint(-1);
	    mi.up();
	    upCS();
	    if (L != null)
		L.repaint();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void upCS() {
	for (int i = 2; i < cs.length; i++)
	    ((SW) cs[i]).up();
    }
    
    public void update(Graphics graphics) {
	this.paint(graphics);
    }
}
