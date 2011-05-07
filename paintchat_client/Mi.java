/* Mi - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_client;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.Method;

import paintchat.M;
import paintchat.Res;

import syi.awt.Awt;
import syi.awt.LComponent;

public class Mi extends LComponent implements ActionListener
{
    private LComponent tab;
    private Method mGet;
    private Method mPoll;
    private IMi imi;
    private Res res;
    private boolean isRight = false;
    public TextField text;
    private boolean isText;
    private M m;
    public M.Info info;
    public M.User user;
    private M mgInfo;
    public boolean isEnable = false;
    private int[] ps = new int[5];
    private int psCount = -1;
    private Graphics primary;
    private Graphics primary2;
    private int oldX = 0;
    private int oldY = 0;
    private boolean isIn = false;
    private int nowCur = -1;
    private Cursor[] cursors;
    private Image imCopy = null;
    private boolean isSpace = false;
    private boolean isScroll = false;
    private boolean isDrag = false;
    private Point poS = new Point();
    private int[] rS = new int[20];
    private int sizeBar = 20;
    private Color[] cls;
    private Color cPre = Color.black;
    
    public Mi(IMi imi, Res res) throws Exception {
	this.imi = imi;
	this.res = res;
	isRepaint = false;
	isHide = false;
	isGUI = false;
	iGap = 2;
	Me.res = res;
    }
    
    public void actionPerformed(ActionEvent actionevent) {
	if (text != null) {
	    addText(actionevent.getActionCommand());
	    if (isText)
		text.setVisible(false);
	}
    }
    
    public void addText(String string) {
	try {
	    if (mgInfo.isText()) {
		setM();
		byte[] is = (String.valueOf('\0') + string).getBytes("UTF8");
		m.setRetouch(ps, is, is.length, true);
		m.draw();
		send(m);
	    }
	} catch (Exception exception) {
	    /* empty */
	}
    }
    
    public boolean alert(String string, boolean bool) {
	try {
	    return Me.confirm(string, bool);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	    return true;
	}
    }
    
    private boolean b(int i, int i_0_) throws Throwable {
	if (!in(i, i_0_))
	    return false;
	Dimension dimension = info.getSize();
	int i_1_ = dimension.width;
	int i_2_ = dimension.height;
	if (isSpace) {
	    isIn = false;
	    imi.scroll(true, 0, 0);
	    isScroll = true;
	    poS.setLocation(i, i_0_);
	    return true;
	}
	if (i > i_1_ || i_0_ > i_2_) {
	    if (i < sizeBar) {
		scaleChange(isRight ? -1 : 1, false);
		isDrag = false;
	    } else if (i_0_ < sizeBar) {
		do {
		    if (tab == null) {
			try {
			    Class var_class = Class.forName("syi.awt.Tab");
			    tab = ((LComponent)
				   (var_class.getConstructors()[0].newInstance
				    (new Object[] { this.getParent(),
						    info })));
			    mGet = var_class.getMethod("strange", null);
			    mPoll = var_class.getMethod("poll", null);
			    break;
			} catch (Throwable throwable) {
			    tab = this;
			    break;
			}
		    }
		    tab.setVisible(true);
		} while (false);
		isDrag = false;
	    } else if (i > i_1_ && i_0_ > i_2_) {
		scaleChange(isRight ? 1 : -1, false);
		isDrag = false;
	    } else {
		isIn = false;
		imi.scroll(true, 0, 0);
		isScroll = true;
		poS.setLocation(i, i_0_);
	    }
	    return true;
	}
	return false;
    }
    
    private void cursor(int i, int i_3_, int i_4_) {
	if (i == 503) {
	    Dimension dimension = info.getSize();
	    int i_5_ = sizeBar;
	    int i_6_ = dimension.width;
	    int i_7_ = dimension.height;
	    int i_8_;
	    if (i_3_ > i_6_ || i_4_ >= i_7_)
		i_8_ = (i_3_ < i_5_ ? 2 : i_4_ < i_5_ ? 0
			: i_3_ > i_6_ && i_4_ > i_7_ ? 3 : 1);
	    else
		i_8_ = 0;
	    if (nowCur != i_8_) {
		nowCur = i_8_;
		this.setCursor(cursors[i_8_]);
	    }
	}
    }
    
    private void dBz(int i, int i_9_, int i_10_) {
	try {
	    if (psCount <= 0) {
		if (i != 502)
		    dLine(i, i_9_, i_10_);
		else {
		    primary2.drawLine(ps[0] >> 16, (short) ps[0], ps[1] >> 16,
				      (short) ps[1]);
		    user.setRect(0, 0, 0, 0);
		    p(3, i_9_, i_10_);
		    ps[1] = ps[0];
		    ps[2] = ps[0];
		    psCount = 1;
		    dPreB(false);
		}
	    } else {
		ePre();
		dPreB(true);
		switch (i) {
		case 503:
		case 506:
		    p(psCount, i_9_, i_10_);
		    p(2, i_9_, i_10_);
		    break;
		case 502:
		    p(psCount++, i_9_, i_10_);
		    if (psCount >= 3) {
			psCount--;
			psCount = -1;
			m.reset(true);
			m.setRetouch(ps, null, 0, true);
			m.draw();
			dEnd(false);
		    } else {
			p(psCount, i_9_, i_10_);
			break;
		    }
		    return;
		}
		dPreB(false);
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void dClear() throws InterruptedException {
	if (alert("kakunin_1", true)) {
	    setM();
	    m.setRetouch(null, null, 0, true);
	    m.draw();
	    dEnd(false);
	}
    }
    
    private void dCopy(int i, int i_11_, int i_12_)
	throws InterruptedException {
	if (psCount <= 1) {
	    if (i == 502) {
		if (psCount > 0) {
		    psCount = 2;
		    p(1, i_11_, i_12_);
		    if (!transRect())
			psCount = -1;
		    else {
			ps[0] = user.points[0];
			ps[1] = user.points[1];
			ps[2] = ps[0];
		    }
		    ps[4] = mgInfo.iLayer;
		}
	    } else
		dRect(i, i_11_, i_12_);
	} else {
	    int i_13_ = ps[2] >> 16;
	    int i_14_ = (short) ps[2];
	    int i_15_ = (ps[1] >> 16) - (ps[0] >> 16);
	    int i_16_ = (short) ps[1] - (short) ps[0];
	    switch (i) {
	    case 501:
		if (imCopy != null)
		    imCopy.flush();
		imCopy = m.getImage(ps[4], i_13_, i_14_, i_15_, i_16_);
		p(3, i_11_, i_12_);
		break;
	    case 506:
		m_paint(i_13_, i_14_, i_15_, i_16_);
		i_13_ += i_11_ - (ps[3] >> 16);
		i_14_ += i_12_ - (short) ps[3];
		p(2, i_13_, i_14_);
		p(3, i_11_, i_12_);
		primary2.setPaintMode();
		primary2.drawImage(imCopy, i_13_, i_14_, i_15_, i_16_,
				   Color.white, null);
		primary2.setXORMode(Color.white);
		break;
	    case 502:
		i_13_ += i_11_ - (ps[3] >> 16);
		i_14_ += i_12_ - (short) ps[3];
		p(2, i_13_, i_14_);
		m.set(mgInfo);
		m.iLayerSrc = ps[4];
		m.setRetouch(ps, null, 0, true);
		m.draw();
		dEnd(false);
		psCount = -1;
		break;
	    }
	}
    }
    
    private void dEnd(boolean bool) throws InterruptedException {
	M m = this.m;
	if (m.iHint != 10 && m.iPen == 20 && m.iHint != 14) {
	    int i = user.wait;
	    user.wait = -2;
	    if (bool)
		m.dEnd();
	    int i_17_ = m.iLayer;
	    for (int i_18_ = i_17_ + 1; i_18_ < info.L; i_18_++) {
		if (info.layers[i_18_].iAlpha != 0.0F) {
		    m.iLayerSrc = i_18_;
		    setA();
		    m.draw();
		    send(m);
		}
	    }
	    for (int i_19_ = i_17_ - 1; i_19_ >= 0; i_19_--) {
		if (info.layers[i_19_].iAlpha != 0.0F) {
		    m.iLayerSrc = i_19_;
		    setA();
		    m.draw();
		    send(m);
		}
	    }
	    user.wait = i;
	    mPaint(null);
	} else {
	    if (bool)
		m.dEnd();
	    send(m);
	}
    }
    
    private void dFLine(int i, int i_20_, int i_21_) {
	try {
	    switch (i) {
	    case 501:
		poll();
		setM();
		m.dStart(i_20_, i_21_, getS(), true, true);
		oldX = 0;
		oldY = 0;
		psCount = 1;
		p(0, i_20_, i_21_);
		break;
	    case 506:
		if (psCount >= 0 && isOKPo(i_20_, i_21_)) {
		    psCount = 0;
		    m.dNext(i_20_, i_21_, getS(), 0);
		    p(psCount, i_20_, i_21_);
		    psCount++;
		}
		break;
	    case 502:
		if (psCount >= 0) {
		    if (m.iHint == 11)
			m.dNext(i_20_, i_21_, getS(), 0);
		    dEnd(true);
		    psCount = -1;
		}
		break;
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void dLine(int i, int i_22_, int i_23_) {
	try {
	    switch (i) {
	    case 501:
		setM();
		for (int i_24_ = 0; i_24_ < 4; i_24_++)
		    p(i_24_, i_22_, i_23_);
		psCount = 0;
		primary2.setColor(new Color(m.iColor));
		primary2.drawLine(i_22_, i_23_, i_22_, i_23_);
		break;
	    case 506:
		if (psCount >= 0) {
		    primary2.drawLine(ps[0] >> 16, (short) ps[0], ps[1] >> 16,
				      (short) ps[1]);
		    primary2.drawLine(ps[0] >> 16, (short) ps[0], i_22_,
				      i_23_);
		    p(1, i_22_, i_23_);
		}
		break;
	    case 502:
		if (psCount >= 0) {
		    int i_25_ = ps[0] >> 16;
		    int i_26_ = (short) ps[0];
		    int i_27_ = m.iSize;
		    int i_28_ = i_22_ - i_25_;
		    int i_29_ = i_23_ - i_26_;
		    int i_30_ = Math.max(Math.abs(i_28_), Math.abs(i_29_));
		    if (i_30_ > 0) {
			m.dStart(i_25_, i_26_, i_27_, true, true);
			m.dNext(i_22_, i_23_, i_27_, 0);
			dEnd(true);
		    } else
			mPaint(null);
		    psCount = -1;
		}
		break;
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private final void dPre(int i, int i_31_, boolean bool) {
	if (mgInfo != null && !mgInfo.isText() && primary2 != null
	    && psCount < 0) {
	    int i_32_ = mgInfo.iHint;
	    if (i_32_ < 3 || i_32_ > 6) {
		try {
		    int i_33_ = info.getPenSize(mgInfo) * info.scale / info.Q;
		    if (i_33_ > 5) {
			int i_34_ = i_33_ >>> 1;
			Graphics graphics = primary2;
			Color color = cPre;
			color = (((color.getRGB() & 0xffffff)
				  != mgInfo.iColor >>> 1)
				 ? new Color(mgInfo.iColor >>> 1) : color);
			cPre = color;
			graphics.setColor(mgInfo.iPen == 4 || mgInfo.iPen == 5
					  ? Color.cyan
					  : color.getRGB() == 16777215
					  ? Color.red : color);
			if (i_33_ <= info.scale * 2) {
			    if (bool)
				graphics.fillRect(oldX - i_34_, oldY - i_34_,
						  i_33_, i_33_);
			    graphics.fillRect(i - i_34_, i_31_ - i_34_, i_33_,
					      i_33_);
			} else {
			    if (bool)
				graphics.drawOval(oldX - i_34_, oldY - i_34_,
						  i_33_, i_33_);
			    graphics.drawOval(i - i_34_, i_31_ - i_34_, i_33_,
					      i_33_);
			}
			oldX = i;
			oldY = i_31_;
		    }
		} catch (RuntimeException runtimeexception) {
		    /* empty */
		}
	    }
	}
    }
    
    private void dPreB(boolean bool) throws InterruptedException {
	if (!bool) {
	    if (user.wait != 0) {
		/* empty */
	    }
	    long l = user.getRect();
	    m.reset(false);
	    user.isPre = true;
	    m.setRetouch(ps, null, 0, true);
	    m.draw();
	    user.addRect((int) (l >>> 48), (int) (l >>> 32) & 0xffff,
			 (int) (l >>> 16) & 0xffff, (int) (l & 0xffffL));
	    m.dBuffer();
	    user.isPre = false;
	}
	Graphics graphics = primary2;
	int i = psCount + 1;
	for (int i_35_ = 0; i_35_ < (i - 2 + 1) * 2; i_35_ += 2)
	    graphics.drawLine(ps[i_35_] >> 16, (short) ps[i_35_],
			      ps[i_35_ + 1] >> 16, (short) ps[i_35_ + 1]);
	int i_36_ = 7;
	int i_37_ = i_36_ / 2;
	for (int i_38_ = 1; i_38_ < i; i_38_++)
	    graphics.drawOval((ps[i_38_] >> 16) - i_37_,
			      (short) ps[i_38_] - i_37_, i_36_, i_36_);
    }
    
    public void drawScroll(Graphics graphics) {
	try {
	    if (graphics == null)
		graphics = primary;
	    if (graphics != null) {
		float f = (float) info.scale;
		int i = sizeBar;
		int i_39_ = info.scaleX;
		int i_40_ = info.scaleY;
		int i_41_ = info.imW;
		int i_42_ = info.imH;
		Dimension dimension = info.getSize();
		int i_43_ = (int) ((float) dimension.width / f);
		int i_44_ = (int) ((float) dimension.height / f);
		if (i_39_ + i_43_ >= i_41_) {
		    i_39_ = Math.max(0, i_41_ - i_43_);
		    info.scaleX = i_39_;
		}
		if (i_40_ + i_44_ - 1 >= i_42_) {
		    i_40_ = Math.max(0, i_42_ - i_44_);
		    info.scaleY = i_40_;
		}
		int i_45_ = dimension.width - i;
		int i_46_ = dimension.height - i;
		int i_47_ = Math.min((int) ((float) i_43_ / (float) i_41_
					    * (float) i_45_),
				     i_45_);
		int i_48_ = Math.min((int) ((float) i_44_ / (float) i_42_
					    * (float) i_46_),
				     i_46_);
		int i_49_
		    = (int) ((float) i_39_ / (float) i_41_ * (float) i_45_);
		int i_50_
		    = (int) ((float) i_40_ / (float) i_42_ * (float) i_46_);
		int[] is = rS;
		graphics.setColor(cls[0]);
		for (int i_51_ = 0; i_51_ < 20; i_51_ += 4)
		    graphics.drawRect(is[i_51_], is[i_51_ + 1], is[i_51_ + 2],
				      is[i_51_ + 3]);
		if (i_49_ > 0) {
		    graphics.setColor(cls[2]);
		    graphics.drawRect(is[0] + 1, is[1] + 1, i_49_ - 2,
				      is[3] - 2);
		    graphics.setColor(cls[1]);
		    graphics.fillRect(is[0] + 2, is[1] + 2, i_49_ - 2,
				      is[3] - 3);
		}
		graphics.setColor(cls[2]);
		graphics.drawRect(is[0] + i_49_ + i_47_, is[1] + 1,
				  is[2] - i_49_ - i_47_ - 1, is[3] - 2);
		graphics.setColor(cls[1]);
		graphics.fillRect(is[0] + 1 + i_49_ + i_47_, is[1] + 2,
				  is[2] - i_49_ - i_47_ - 2, is[3] - 3);
		graphics.setColor(cls[1]);
		if (i_50_ > 0) {
		    graphics.setColor(cls[2]);
		    graphics.drawRect(is[4] + 1, is[5] + 1, is[6] - 2,
				      i_50_ - 1);
		    graphics.setColor(cls[1]);
		    graphics.fillRect(is[4] + 2, is[5] + 2, is[6] - 3,
				      i_50_ - 1);
		}
		graphics.setColor(cls[2]);
		graphics.drawRect(is[4] + 1, is[5] + i_50_ + i_48_, is[6] - 2,
				  is[7] - i_50_ - i_48_ - 1);
		graphics.setColor(cls[1]);
		graphics.fillRect(is[4] + 2, is[5] + i_50_ + i_48_, is[6] - 3,
				  is[7] - i_50_ - i_48_ - 1);
		for (int i_52_ = 8; i_52_ < 20; i_52_ += 4) {
		    for (int i_53_ = 0; i_53_ < 2; i_53_++) {
			graphics.setColor(cls[2 - i_53_]);
			if (i_53_ == 0)
			    graphics.drawRect(is[i_52_] + 1, is[i_52_ + 1] + 1,
					      is[i_52_ + 2] - 2,
					      is[i_52_ + 3] - 2);
			else
			    graphics.fillRect(is[i_52_] + 2, is[i_52_ + 1] + 2,
					      is[i_52_ + 2] - 3,
					      is[i_52_ + 3] - 3);
		    }
		    graphics.setColor(cls[3]);
		    int i_54_ = is[i_52_ + 2] / 2;
		    int i_55_ = is[i_52_ + 3] / 2;
		    if (i_52_ == 16) {
			int i_56_ = is[i_52_] + i_54_ / 2;
			int i_57_ = is[i_52_ + 1] + i_55_ / 2;
			i_55_ /= 2;
			graphics.drawRect(i_56_, i_57_, i_54_, i_55_);
			graphics.fillRect(i_56_, i_57_ + i_55_, 1, i_55_);
		    } else {
			graphics.fillRect(is[i_52_] + i_54_ / 2,
					  is[i_52_ + 1] + i_55_, i_54_ + 1, 1);
			if (i_52_ == 8)
			    graphics.fillRect(is[i_52_] + i_54_,
					      is[i_52_ + 1] + i_55_ / 2, 1,
					      i_55_);
		    }
		}
		int i_58_ = is[0] + i_49_;
		int i_59_ = is[1] + 1;
		int i_60_ = is[4] + 1;
		int i_61_ = is[5] + i_50_;
		graphics.setColor(cls[0]);
		graphics.drawRect(i_58_, i_59_, i_47_, is[3] - 2);
		graphics.drawRect(i_60_, i_61_, is[6] - 2, i_48_ + 1);
		graphics.setColor(cls[3]);
		graphics.fillRect(i_58_ + 2, i_59_ + 2, i_47_ - 3, is[3] - 5);
		graphics.fillRect(i_60_ + 2, i_61_ + 2, is[6] - 5, i_48_ - 2);
		graphics.setColor(cls[4]);
		graphics.fillRect(i_58_ + 1, i_59_ + 1, i_47_ - 2, 1);
		graphics.fillRect(i_58_ + 1, i_59_ + 2, 1, is[3] - 5);
		graphics.fillRect(i_60_ + 1, i_61_ + 1, is[6] - 4, 1);
		graphics.fillRect(i_60_ + 1, i_61_ + 2, 1, i_48_ - 2);
		graphics.setColor(cls[5]);
		graphics.fillRect(i_58_ + i_47_ - 1, i_59_ + 1, 1, is[3] - 4);
		graphics.fillRect(i_58_ + 1, i_59_ + is[3] - 3, i_47_ - 1, 1);
		graphics.fillRect(i_60_ + is[6] - 3, i_61_ + 1, 1, i_48_ - 1);
		graphics.fillRect(i_60_ + 1, i_61_ + i_48_, is[6] - 3, 1);
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void dRect(int i, int i_62_, int i_63_) {
	try {
	    int[] is = user.points;
	    switch (i) {
	    case 501:
		setM();
		p(0, i_62_, i_63_);
		m.memset(is, 0);
		psCount = 1;
		break;
	    case 506:
		if (psCount == 1) {
		    int i_64_ = is[0];
		    int i_65_ = is[1];
		    int i_66_ = i_64_ >> 16;
		    short i_67_ = (short) i_64_;
		    primary2.drawRect(i_66_, i_67_, (i_65_ >> 16) - i_66_ - 1,
				      (short) i_65_ - i_67_ - 1);
		    p(1, i_62_, i_63_);
		    transRect();
		    i_64_ = is[0];
		    i_65_ = is[1];
		    i_66_ = i_64_ >> 16;
		    i_67_ = (short) i_64_;
		    primary2.drawRect(i_66_, i_67_, (i_65_ >> 16) - i_66_ - 1,
				      (short) i_65_ - i_67_ - 1);
		}
		break;
	    case 502:
		if (psCount > 0) {
		    p(1, i_62_, i_63_);
		    if (transRect()) {
			ps[0] = is[0];
			ps[1] = is[1];
			m.setRetouch(ps, null, 0, true);
			if (m.iPen != 20)
			    m.draw();
			dEnd(false);
		    }
		}
		psCount = -1;
		break;
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void dScroll(MouseEvent mouseevent, int i, int i_68_) {
	if (i != 0 || i_68_ != 0)
	    scroll(i, i_68_);
	else {
	    int i_69_ = mouseevent.getID();
	    Point point = mouseevent.getPoint();
	    if (i_69_ == 502 || i_69_ == 506) {
		scroll(isSpace ? poS.x - point.x : point.x - poS.x,
		       isSpace ? poS.y - point.y : point.y - poS.y);
		poS = point;
	    } else if (i_69_ == 501)
		poS = point;
	}
    }
    
    public void dText(int i, int i_70_, int i_71_) {
	switch (i) {
	case 501:
	    break;
	case 502:
	    setM();
	    if (text == null) {
		text = new TextField(16);
		text.addActionListener(this);
		isText = true;
		this.getParent().add(text, 0);
	    }
	    if (!isText) {
		primary.setColor(new Color(mgInfo.iColor));
		primary.fillRect(i_70_ - 1, i_71_ - 1, mgInfo.iSize + 1, 1);
		primary.fillRect(i_70_ - 1, i_71_, 1, mgInfo.iSize);
	    } else {
		text.setFont(m.getFont(m.iSize * info.scale / info.Q));
		text.setSize(text.getPreferredSize());
		Point point = this.getLocation();
		text.setLocation(i_70_ + point.x, i_71_ + point.y + 2);
		text.setVisible(true);
	    }
	    p(0, i_70_, i_71_);
	    break;
	}
    }
    
    private void ePre() {
	dPre(oldX, oldY, false);
	oldX = -1000;
	oldY = -1000;
    }
    
    private final int getS() {
	try {
	    if (tab == null || tab == this)
		return 255;
	    return ((Integer) mGet.invoke(tab, null)).intValue();
	} catch (Throwable throwable) {
	    tab = this;
	    return 255;
	}
    }
    
    private final boolean in(int i, int i_72_) {
	Dimension dimension = this.getSize();
	return (i >= 0 && i_72_ >= 0 && i < dimension.width
		&& i_72_ < dimension.height);
    }
    
    public void init(Applet applet, Res res, int i, int i_73_, int i_74_,
		     int i_75_, Cursor[] cursors) throws IOException {
	String string = "color_";
	this.cursors = cursors;
	cls = new Color[6];
	cls[0] = new Color(res.getP(string + "frame", 5263480));
	cls[1] = new Color(res.getP(string + "icon", 13421823));
	cls[2] = new Color(res.getP(string + "bar_hl", 16777215));
	cls[3] = new Color(res.getP(string + "bar", 7303086));
	cls[4] = new Color(res.getP(string + "bar_hl", 15658751));
	cls[5] = new Color(res.getP(string + "bar_shadow", 11184810));
	this.setBackground(Color.white);
	m = new M();
	user = m.newUser(this);
	M.Info info = m.newInfo(applet, this, res);
	info.setSize(i, i_73_, i_74_);
	info.setL(i_75_);
	this.info = info;
	mgInfo = info.m;
    }
    
    private final boolean isOKPo(int i, int i_76_) {
	int i_77_ = ps[psCount - 1];
	return (Math.max(Math.abs(i - (i_77_ >> 16)),
			 Math.abs(i_76_ - (short) i_77_))
		>= info.scale);
    }
    
    public void m_paint(int i, int i_78_, int i_79_, int i_80_) {
	Dimension dimension = info.getSize();
	if (m == null) {
	    primary.setColor(Color.white);
	    primary.fillRect(0, 0, dimension.width, dimension.height);
	} else {
	    if (i == 0 && i_78_ == 0 && i_79_ == 0 && i_80_ == 0) {
		i = i_78_ = 0;
		i_79_ = dimension.width;
		i_80_ = dimension.height;
	    } else {
		i = Math.max(i, 0);
		i_78_ = Math.max(i_78_, 0);
		i_79_ = Math.min(i_79_, dimension.width);
		i_80_ = Math.min(i_80_, dimension.height);
	    }
	    m.m_paint(i, i_78_, i_79_, i_80_);
	}
    }
    
    public void m_paint(Rectangle rectangle) {
	if (rectangle == null)
	    m_paint(0, 0, 0, 0);
	else
	    m_paint(rectangle.x, rectangle.y, rectangle.width,
		    rectangle.height);
    }
    
    private void mPaint(Graphics graphics) {
	if (graphics == null)
	    graphics = primary;
	drawScroll(graphics);
	if (isPaint)
	    m_paint(graphics == primary ? null : graphics.getClipBounds());
    }
    
    private final void p(int i, int i_81_, int i_82_) {
	ps[i] = i_81_ << 16 | i_82_ & 0xffff;
    }
    
    public void paint2(Graphics graphics) {
	try {
	    Rectangle rectangle = graphics.getClipBounds();
	    int i = (info == null ? 0 : info.scale) * 2;
	    graphics.setClip(rectangle.x - i, rectangle.y - i,
			     rectangle.width + i * 2,
			     rectangle.height + i * 2);
	    mPaint(graphics);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void pMouse(MouseEvent mouseevent) {
	try {
	    int i = mouseevent.getID();
	    int i_83_ = info.scale;
	    int i_84_ = mouseevent.getX() / i_83_ * i_83_;
	    int i_85_ = mouseevent.getY() / i_83_ * i_83_;
	    boolean bool = i == 501;
	    boolean bool_86_ = i == 506;
	    boolean bool_87_ = isRight;
	    if (mouseevent.isAltDown() && mouseevent.isControlDown()) {
		if (psCount >= 0)
		    reset();
		if (bool) {
		    poS.y = i_85_;
		    poS.x = mgInfo.iSize;
		    m_paint(null);
		}
		if (bool_86_) {
		    Dimension dimension = this.getSize();
		    int i_88_ = dimension.width / 2;
		    int i_89_ = dimension.height / 2;
		    int i_90_ = info.getPenSize(mgInfo) * info.scale;
		    m_paint(i_88_ - i_90_, i_89_ - i_90_, i_90_ * 2,
			    i_90_ * 2);
		    imi.setLineSize((i_85_ - poS.y) / 4 + poS.x);
		    dPre(i_88_, i_89_, false);
		}
	    } else {
		if (bool) {
		    if (text == null || !text.isVisible())
			this.requestFocus();
		    bool_87_ = isRight = Awt.isR(mouseevent);
		    if (!isDrag)
			dPre(oldX, oldY, false);
		    isDrag = true;
		    if (b(i_84_, i_85_))
			return;
		    if (isText && text != null && text.isVisible())
			text.setVisible(false);
		}
		if (i == 502) {
		    if (!isDrag)
			return;
		    isRight = false;
		    isDrag = false;
		    isPaint = true;
		    if (isScroll) {
			isScroll = false;
			imi.scroll(false, 0, 0);
			if (info.scale < 1)
			    m_paint(null);
			return;
		    }
		}
		if (isRight && isDrag) {
		    if (psCount >= 0) {
			reset();
			isRight = false;
			isDrag = false;
			isPaint = true;
		    } else
			imi.setARGB(user.getPixel((i_84_ / info.scale
						   + info.scaleX),
						  (i_85_ / info.scale
						   + info.scaleY)) & 0xffffff
				    | info.m.iAlpha << 24);
		} else {
		    if (!isDrag) {
			cursor(i, i_84_, i_85_);
			switch (i) {
			case 504:
			    getS();
			    break;
			case 503:
			    dPre(i_84_, i_85_, isIn);
			    isIn = true;
			    break;
			case 505:
			    if (isIn) {
				isIn = false;
				dPre(oldX, oldY, false);
			    }
			    break;
			}
		    }
		    if (isScroll)
			dScroll(mouseevent, 0, 0);
		    else {
			if (isEnable
			    && (((long) (mgInfo.iLayer + 1) & info.permission)
				!= 0L)
			    && !bool_87_
			    && (mgInfo.iHint == 10
				|| !(info.layers[mgInfo.iLayer].iAlpha
				     <= 0.0F))) {
			    switch (mgInfo.iHint) {
			    case 0:
			    case 11:
				dFLine(i, i_84_, i_85_);
				break;
			    case 1:
				dLine(i, i_84_, i_85_);
				break;
			    case 2:
				dBz(i, i_84_, i_85_);
				break;
			    case 8:
			    case 12:
				dText(i, i_84_, i_85_);
				break;
			    case 9:
				dCopy(i, i_84_, i_85_);
				break;
			    case 10:
				if (info.isClean && bool)
				    dClear();
				break;
			    case 7:
				if (bool && info.isFill) {
				    m.set(info.m);
				    p(0, i_84_, i_85_);
				    p(1, i_84_ + 1024, i_85_ + 1024);
				    transRect();
				    m.setRetouch(user.points, null, 0, true);
				    m.draw();
				    send();
				}
				break;
			    default:
				dRect(i, i_84_, i_85_);
			    }
			}
			if (i == 502 && isIn) {
			    dPre(i_84_, i_85_, false);
			    isDrag = false;
			}
		    }
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private final void poll() {
	if (tab != null && tab != this) {
	    try {
		if (((Boolean) mPoll.invoke(tab, null)).booleanValue())
		    return;
	    } catch (Throwable throwable) {
		throwable.printStackTrace();
	    }
	    mgInfo.iSOB = 0;
	}
    }
    
    protected void processKeyEvent(KeyEvent keyevent) {
	try {
	    boolean bool = keyevent.isControlDown() || keyevent.isShiftDown();
	    boolean bool_91_ = keyevent.isAltDown();
	    boolean bool_92_ = true;
	    switch (keyevent.getID()) {
	    case 401:
		switch (keyevent.getKeyCode()) {
		case 32:
		    isSpace = true;
		    break;
		case 39:
		    scroll(5, 0);
		    break;
		case 38:
		    scroll(0, -5);
		    break;
		case 40:
		    scroll(0, 5);
		    break;
		case 37:
		    scroll(-5, 0);
		    break;
		case 107:
		    scaleChange(1, false);
		    break;
		case 109:
		    scaleChange(-1, false);
		    break;
		case 82:
		case 89:
		    bool_92_ = false;
		    /* fall through */
		case 90:
		    if (bool_91_)
			bool_92_ = false;
		    if (bool)
			imi.undo(bool_92_);
		    break;
		case 66:
		    imi.setARGB(mgInfo.iAlpha << 24 | mgInfo.iColor);
		    break;
		case 69:
		    imi.setARGB(16777215);
		    break;
		}
		break;
	    case 402:
		switch (keyevent.getKeyCode()) {
		case 32:
		    isSpace = false;
		    break;
		default:
		    break;
		}
		break;
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void reset() {
	if (psCount >= 0) {
	    psCount = -1;
	    switch (mgInfo.iHint) {
	    case 0:
	    case 11:
		m.reset(true);
		break;
	    default:
		m.reset(false);
		m_paint(null);
	    }
	}
    }
    
    public synchronized void resetGraphics() {
	if (primary != null)
	    primary.dispose();
	if (primary2 != null)
	    primary2.dispose();
	Dimension dimension = this.getSize();
	int i = dimension.width - sizeBar;
	int i_93_ = dimension.height - sizeBar;
	primary = this.getGraphics();
	if (primary != null) {
	    primary.translate(this.getGapX(), this.getGapY());
	    primary2 = primary.create(0, 0, i, i_93_);
	    primary2.setXORMode(Color.white);
	    info.setComponent(this, primary, i, i_93_);
	}
	int[] is = rS;
	int i_94_ = sizeBar;
	dimension = info.getSize();
	for (int i_95_ = 0; i_95_ < 20; i_95_++)
	    is[i_95_] = i_94_;
	is[1] = dimension.height;
	is[2] = dimension.width - i_94_;
	is[4] = dimension.width;
	is[7] = dimension.height - i_94_;
	is[8] = 0;
	is[9] = dimension.height;
	is[12] = dimension.width;
	is[13] = dimension.height;
	is[16] = dimension.width;
	is[17] = 0;
    }
    
    public void scaleChange(int i, boolean bool) {
	if (isIn)
	    ePre();
	if (info.addScale(i, bool) && !isGUI) {
	    float f = (float) info.scale;
	    int i_96_ = (int) ((float) info.imW * f) + sizeBar;
	    int i_97_ = (int) ((float) info.imH * f) + sizeBar;
	    Dimension dimension = this.getSize();
	    int i_98_ = dimension.width;
	    int i_99_ = dimension.height;
	    if (i_96_ != dimension.width || i_97_ != dimension.height)
		setSize(i_96_, i_97_);
	    dimension = this.getSize();
	    if (dimension.width == i_98_ && dimension.height == i_99_)
		mPaint(null);
	    else
		imi.changeSize();
	}
    }
    
    public void scroll(int i, int i_100_) {
	if (info != null) {
	    Dimension dimension = info.getSize();
	    int i_101_ = info.imW;
	    int i_102_ = info.imH;
	    float f = (float) info.scale;
	    int i_103_ = info.scaleX;
	    int i_104_ = info.scaleY;
	    float f_105_
		= (float) i * ((float) i_101_ / (float) dimension.width);
	    if (f < 1.0F)
		f_105_ /= f;
	    if (f_105_ != 0.0F)
		f_105_
		    = (f_105_ >= 0.0F && f_105_ <= 1.0F ? 1.0F
		       : f_105_ <= 0.0F && f_105_ >= -1.0F ? -1.0F : f_105_);
	    int i_106_ = (int) f_105_;
	    f_105_
		= (float) i_100_ * ((float) i_102_ / (float) dimension.height);
	    if (f_105_ != 0.0F)
		f_105_
		    = (f_105_ >= 0.0F && f_105_ <= 1.0F ? 1.0F
		       : f_105_ <= 0.0F && f_105_ >= -1.0F ? -1.0F : f_105_);
	    if (f < 1.0F)
		f_105_ /= f;
	    int i_107_ = (int) f_105_;
	    Graphics graphics = primary;
	    info.scaleX = Math.max(i_103_ + i_106_, 0);
	    info.scaleY = Math.max(i_104_ + i_107_, 0);
	    drawScroll(graphics);
	    poS.translate(i, i_100_);
	    int i_108_ = (int) ((float) (info.scaleX - i_103_) * f);
	    int i_109_ = (int) ((float) (info.scaleY - i_104_) * f);
	    i_106_ = dimension.width - Math.abs(i_108_);
	    i_107_ = dimension.height - Math.abs(i_109_);
	    try {
		graphics.copyArea(Math.max(i_108_, 0), Math.max(i_109_, 0),
				  i_106_, i_107_, -i_108_, -i_109_);
		if (f >= 1.0F) {
		    if (i_108_ != 0) {
			if (i_108_ > 0)
			    m_paint(dimension.width - i_108_, 0, i_108_,
				    dimension.height);
			else
			    m_paint(0, 0, -i_108_, dimension.height);
		    }
		    if (i_109_ != 0) {
			if (i_109_ > 0)
			    m_paint(0, dimension.height - i_109_,
				    dimension.width, i_109_);
			else
			    m_paint(0, 0, dimension.width, -i_109_);
		    }
		} else {
		    if (i_108_ != 0) {
			if (i_108_ > 0)
			    graphics.clearRect(dimension.width - i_108_, 0,
					       i_108_, dimension.height);
			else
			    graphics.clearRect(0, 0, -i_108_,
					       dimension.height);
		    }
		    if (i_109_ != 0) {
			if (i_108_ > 0)
			    graphics.clearRect(0, dimension.height - i_109_,
					       dimension.width, i_109_);
			else
			    graphics.clearRect(0, 0, dimension.width, -i_109_);
		    }
		}
		imi.scroll(true, i_108_, i_109_);
	    } catch (Throwable throwable) {
		throwable.printStackTrace();
	    }
	}
    }
    
    private void send() {
	imi.send(m);
    }
    
    public void send(String string) {
	try {
	    M m = new M(info, user);
	    m.set(string);
	    m.draw();
	    send(m);
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    public void send(M m) throws InterruptedException {
	imi.send(m);
    }
    
    public void setA() {
	m.iAlpha2 = ((int) (info.layers[m.iLayer].iAlpha * 255.0F) << 8
		     | (int) (info.layers[m.iLayerSrc].iAlpha * 255.0F));
    }
    
    private final void setM() {
	m.set(mgInfo);
	if (m.iPen == 20)
	    m.iLayerSrc = m.iLayer;
	setA();
    }
    
    public void setSize(Dimension dimension) {
	setSize(dimension.width, dimension.height);
    }
    
    public void setSize(int i, int i_110_) {
	if (info == null)
	    super.setSize(i, i_110_);
	else {
	    int i_111_ = info.imW * info.scale + sizeBar;
	    int i_112_ = info.imH * info.scale + sizeBar;
	    super.setSize(Math.min(i - this.getGapW(), i_111_),
			  Math.min(i_110_ - this.getGapW(), i_112_));
	}
	this.repaint();
    }
    
    public final boolean transRect() {
	int i = ps[0];
	int i_113_ = i >> 16;
	short i_114_ = (short) i;
	i = ps[1];
	int i_115_ = i >> 16;
	short i_116_ = (short) i;
	int i_117_ = Math.max(Math.min(i_113_, i_115_), 0);
	int i_118_ = Math.min(Math.max(i_113_, i_115_), info.imW * info.scale);
	int i_119_ = Math.max(Math.min(i_114_, i_116_), 0);
	int i_120_ = Math.min(Math.max(i_114_, i_116_), info.imH * info.scale);
	if (i_118_ - i_117_ < info.scale || i_120_ - i_119_ < info.scale)
	    return false;
	user.points[0] = i_117_ << 16 | i_119_ & 0xffff;
	user.points[1] = i_118_ << 16 | i_120_ & 0xffff;
	return true;
    }
    
    public void up() {
	if (tab != null)
	    tab.repaint();
    }
}
