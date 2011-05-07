/* L - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_client;
import java.awt.CheckboxMenuItem;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;

import paintchat.LO;
import paintchat.M;
import paintchat.Res;
import paintchat.ToolBox;

import syi.awt.Awt;
import syi.awt.LComponent;

public class L extends LComponent implements ActionListener, ItemListener
{
    private Mi mi;
    private ToolBox tool;
    private Res res;
    private M m;
    private int B = -1;
    private Font bFont;
    private int bH;
    private int bW;
    private int base;
    private int layer_size = -1;
    private int mouse = -1;
    private boolean isASlide = false;
    private int Y;
    private int YOFF;
    private PopupMenu popup = null;
    private String strMenu;
    private boolean is_pre = true;
    private boolean is_DIm = false;
    private Color cM;
    private Color cT;
    private String sL;
    
    public L(Mi mi, ToolBox toolbox, Res res, Res res_0_) {
	tool = toolbox;
	bFont = Awt.getDefFont();
	bFont = new Font(bFont.getName(), 0,
			 (int) ((float) bFont.getSize() * 0.8F));
	FontMetrics fontmetrics = this.getFontMetrics(bFont);
	bH = fontmetrics.getHeight() + 6;
	base = bH - 2 - fontmetrics.getMaxDescent();
	int i = (int) (60.0F * LComponent.Q);
	String string = res.res("Layer");
	sL = string;
	strMenu = res.res("MenuLayer");
	cM = new Color(res_0_.getP("l_m_color", 0));
	cT = new Color(res_0_.getP("l_m_color_text", 16777215));
	fontmetrics = this.getFontMetrics(bFont);
	i = Math.max(fontmetrics.stringWidth(string + "00") + 4, i);
	i = Math.max(fontmetrics.stringWidth(strMenu) + 4, i);
	bW = i;
	i += bH + 100;
	this.mi = mi;
	this.res = res;
	this.setTitle(string);
	isGUI = true;
	m = mi.info.m;
	Dimension dimension = new Dimension(bW, bH);
	this.setDimension(new Dimension(dimension), dimension,
			  new Dimension());
	this.setSize(getMaximumSize());
    }
    
    public void actionPerformed(ActionEvent actionevent) {
	try {
	    String string = actionevent.getActionCommand();
	    int i = popup.getItemCount();
	    int i_1_;
	    for (i_1_ = 0; i_1_ < i; i_1_++) {
		if (popup.getItem(i_1_).getLabel().equals(string))
		    break;
	    }
	    M.Info info = mi.info;
	    M m = mg();
	    setA(m);
	    LO[] los = info.layers;
	    int i_2_ = info.L;
	    byte[] is = new byte[4];
	    boolean bool = false;
	    boolean bool_3_ = false;
	    int i_4_ = mi.user.wait;
	    mi.user.wait = -2;
	    if (popup.getName().charAt(0) == 'm') {
		switch (i_1_) {
		case 0:
		    m.setRetouch(new int[] { 1, i_2_ + 1 }, null, 0, false);
		    bool = true;
		    bool_3_ = true;
		    break;
		case 1:
		    if (info.L > 1 && confirm(los[m.iLayer].name
					      + res.res("DelLayerQ"))) {
			m.iLayerSrc = m.iLayer;
			m.setRetouch(new int[] { 2 }, null, 0, false);
			bool = true;
			break;
		    }
		    return;
		case 2:
		    dFusion();
		    break;
		case 3:
		    config(this.m.iLayer);
		}
	    } else if (i_1_ == 0) {
		m.iHint = 14;
		m.setRetouch(new int[] { 3 }, null, 0, false);
		bool = true;
	    } else {
		byte i_5_ = (byte) los[m.iLayerSrc].iCopy;
		if (i_5_ == 1)
		    dFusion();
		else {
		    m.iHint = 3;
		    m.iPen = 20;
		    is[0] = i_5_;
		    m.setRetouch(new int[] { 0, info.W << 16 | info.H }, is, 4,
				 false);
		    bool = true;
		}
	    }
	    if (bool) {
		m.draw();
		if (bool_3_)
		    info.layers[info.L - 1].makeName(sL);
		mi.send(m);
	    }
	    this.m.iLayerSrc = this.m.iLayer
		= Math.min(this.m.iLayer, info.L - 1);
	    this.repaint();
	    mi.user.wait = i_4_;
	    mi.repaint();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private int b(int i) {
	if (i < bH)
	    return 0;
	return Math.max(mi.info.L - (i / bH - 1), 1);
    }
    
    private void send(int[] is, byte[] is_6_) {
	M m = mg();
	setA(m);
	m.setRetouch(is, is_6_, is_6_ != null ? is_6_.length : 0, false);
	int i = mi.user.wait;
	try {
	    m.draw();
	    mi.send(m);
	} catch (Throwable throwable) {
	    /* empty */
	}
	this.repaint();
	mi.user.wait = i;
	mi.repaint();
    }
    
    private void dFusion() {
	if (confirm(res.res("CombineVQ"))) {
	    try {
		int i = mi.info.L;
		LO[] los = mi.info.layers;
		int i_7_ = 0;
		for (int i_8_ = 0; i_8_ < i; i_8_++) {
		    if (los[i_8_].iAlpha > 0.0F)
			i_7_++;
		}
		if (i_7_ > 0) {
		    int i_9_ = mi.user.wait;
		    M m = mg();
		    setA(m);
		    byte[] is = new byte[i_7_ * 4 + 2];
		    int i_10_ = 2;
		    is[0] = (byte) i_7_;
		    for (int i_11_ = 0; i_11_ < i; i_11_++) {
			LO lo = los[i_11_];
			if (!(lo.iAlpha <= 0.0F)) {
			    is[i_10_++] = (byte) i_11_;
			    is[i_10_++] = (byte) (int) (lo.iAlpha * 255.0F);
			    is[i_10_++] = (byte) lo.iCopy;
			    is[i_10_++] = (byte) 41;
			}
		    }
		    mi.user.wait = -2;
		    m.setRetouch(new int[] { 7 }, is, is.length, false);
		    m.draw();
		    mi.send(m);
		    mi.user.wait = i_9_;
		}
	    } catch (Throwable throwable) {
		throwable.printStackTrace();
	    }
	}
    }
    
    private boolean confirm(String string) {
	return Me.confirm(string, true);
    }
    
    private void dL(Graphics graphics, int i, int i_12_) {
	if (mi.info.L > i_12_) {
	    this.getSize();
	    int i_13_ = bW - 1;
	    int i_14_ = bH - 2;
	    Color color = m.iLayer == i_12_ ? Awt.cFSel : clFrame;
	    LO lo = mi.info.layers[i_12_];
	    graphics.setColor(color);
	    graphics.drawRect(0, i, i_13_, i_14_);
	    graphics.setColor(Awt.cFore);
	    graphics.setFont(bFont);
	    graphics.drawString(lo.name, 2, i + base);
	    graphics.setColor(color);
	    graphics.drawRect(bW, i, 100, i_14_);
	    graphics.setColor(cM);
	    int i_15_ = (int) (100.0F * lo.iAlpha);
	    graphics.fillRect(bW + 1, i + 1, i_15_ - 1, i_14_ - 1);
	    graphics.setColor(cT);
	    graphics.drawString(String.valueOf(i_15_) + "%", bW + 3, i + base);
	    int i_16_ = bW + 100;
	    graphics.setColor(color);
	    graphics.drawRect(i_16_ + 1, i, i_14_ - 2, i_14_);
	    graphics.setColor(Awt.cFore);
	    if (i_15_ == 0) {
		graphics.drawLine(i_16_ + 2, i + 1, i_16_ + i_14_ - 2,
				  i + i_14_ - 1);
		graphics.drawLine(1, i + 1, i_13_ - 1, i + bH - 3);
	    } else
		graphics.drawOval(i_16_ + 2, i + 2, i_14_ - 3, i_14_ - 3);
	}
    }
    
    public Dimension getMaximumSize() {
	Dimension dimension = super.getMaximumSize();
	if (mi != null)
	    dimension.setSize(bW + 100 + bH, bH * (mi.info.L + 1));
	return dimension;
    }
    
    public void itemStateChanged(ItemEvent itemevent) {
	is_pre = !is_pre;
    }
    
    private M mg() {
	M m = new M(mi.info, mi.user);
	m.iAlpha = 255;
	m.iHint = 14;
	m.iLayer = this.m.iLayer;
	m.iLayerSrc = this.m.iLayerSrc;
	return m;
    }
    
    private void p() {
	this.repaint();
	tool.up();
    }
    
    public void paint2(Graphics graphics) {
	try {
	    int i = mi.info.L;
	    for (int i_17_ = 0; i_17_ < i; i_17_++) {
		LO lo = mi.info.layers[i_17_];
		if (lo.name == null)
		    lo.makeName(sL);
	    }
	    if (layer_size != i) {
		layer_size = i;
		this.setSize(getMaximumSize());
	    } else {
		Dimension dimension = this.getSize();
		int i_18_ = i - 1;
		int i_19_ = bH;
		graphics.setFont(bFont);
		graphics.setColor(Awt.cBk);
		graphics.fillRect(0, 0, dimension.width, dimension.height);
		for (/**/; i_19_ < dimension.height; i_19_ += bH) {
		    if (isASlide || i_18_ != mouse - 1)
			dL(graphics, i_19_, i_18_);
		    if (--i_18_ < 0)
			break;
		}
		if (!isASlide && mouse > 0)
		    dL(graphics, Y - YOFF, mouse - 1);
		Awt.drawFrame(graphics, mouse == 0, 0, 0, bW, bH - 2);
		graphics.setColor(Awt.cFore);
		graphics.drawString(strMenu, 3, bH - 6);
	    }
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    public void pMouse(MouseEvent mouseevent) {
	try {
	    int i = Y = mouseevent.getY();
	    int i_20_ = mouseevent.getX();
	    M.Info info = mi.info;
	    boolean bool = Awt.isR(mouseevent);
	    switch (mouseevent.getID()) {
	    case 501:
		if (mouse < 0) {
		    int i_21_ = b(i);
		    int i_22_ = i_21_ - 1;
		    if (i_22_ >= 0) {
			if (i_20_ > bW + 100 + 1) {
			    int i_23_ = mi.user.wait;
			    mi.user.wait = -2;
			    if (bool) {
				for (int i_24_ = 0; i_24_ < info.L; i_24_++)
				    setAlpha(i_24_, i_24_ == i_22_ ? 255 : 0,
					     true);
			    } else
				setAlpha(i_22_,
					 (info.layers[i_22_].iAlpha == 0.0F
					  ? 255 : 0),
					 true);
			    mi.user.wait = i_23_;
			    mi.repaint();
			    p();
			} else if (mouseevent.getClickCount() >= 2 || bool) {
			    config(i_22_);
			    mi.repaint();
			} else {
			    isASlide = i_20_ >= bW;
			    mouse = i_21_;
			    m.iLayer = m.iLayerSrc = i_22_;
			    YOFF = i % bH;
			    if (isASlide)
				setAlpha(i_22_,
					 (int) ((float) (i_20_ - bW) / 100.0F
						* 255.0F),
					 false);
			    else
				p();
			}
		    } else {
			m.iLayerSrc = m.iLayer;
			if (i_20_ < bW && i > 2)
			    popup(new String[] { "AddLayer", "DelLayer",
						 "CombineV", "PropertyLayer" },
				  i_20_, i, true);
		    }
		}
		break;
	    case 506:
		if (mouse > 0) {
		    if (isASlide)
			setAlpha(m.iLayer, (int) ((float) (i_20_ - bW) / 100.0F
						  * 255.0F), false);
		    else {
			m.iLayer = b(Y) - 1;
			this.repaint();
		    }
		}
		break;
	    case 503: {
		int i_25_ = b(i) - 1;
		if (!is_pre || i_25_ < 0 || i_20_ >= bW) {
		    if (is_DIm) {
			is_DIm = false;
			this.repaint();
		    }
		} else {
		    is_DIm = true;
		    Dimension dimension = this.getSize();
		    int i_26_ = mi.info.W;
		    int i_27_ = mi.info.H;
		    int[] is = mi.info.layers[i_25_].offset;
		    Graphics graphics = this.getG();
		    int i_28_ = Math.min(dimension.width - bW - 1,
					 dimension.height - 1);
		    if (is == null) {
			graphics.setColor(Color.white);
			graphics.fillRect(bW + 1, 1, i_28_ - 1, i_28_ - 1);
		    } else {
			Image image = (this.getToolkit().createImage
				       (new MemoryImageSource
					(i_26_, i_27_,
					 new DirectColorModel(24, 16711680,
							      65280, 255),
					 is, 0, i_26_)));
			graphics.drawImage(image, bW + 1, 1, i_28_ - 1,
					   i_28_ - 1, null);
			image.flush();
		    }
		    graphics.setColor(Color.black);
		    graphics.drawRect(bW, 0, i_28_, i_28_);
		    graphics.dispose();
		    break;
		}
		break;
	    }
	    case 502:
		if (!bool) {
		    if (isASlide) {
			setAlpha(m.iLayer, (int) ((float) (i_20_ - bW) / 100.0F
						  * 255.0F), true);
			mouse = -1;
			isASlide = false;
		    } else {
			int i_29_ = mouse - 1;
			int i_30_ = b(Y) - 1;
			if (i_29_ >= 0 && i_30_ >= 0 && i_29_ != i_30_) {
			    m.iLayer = i_30_;
			    m.iLayerSrc = i_29_;
			    popup(new String[] { res.res("Shift"),
						 res.res("Combine") },
				  i_20_, i, false);
			}
			mouse = -1;
			this.repaint();
		    }
		}
		break;
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void popup(String[] strings, int i, int i_31_, boolean bool) {
	if (mi.info.isLEdit) {
	    if (popup == null) {
		popup = new PopupMenu();
		popup.addActionListener(this);
		this.add(popup);
	    } else
		popup.removeAll();
	    for (int i_32_ = 0; i_32_ < strings.length; i_32_++)
		popup.add(res.res(strings[i_32_]));
	    if (bool) {
		popup.addSeparator();
		CheckboxMenuItem checkboxmenuitem
		    = new CheckboxMenuItem(res.res("IsPreview"), is_pre);
		checkboxmenuitem.addItemListener(this);
		popup.add(checkboxmenuitem);
		popup.setName("m");
	    } else
		popup.setName("l");
	    popup.show(this, i, i_31_);
	}
    }
    
    private void setA(M m) {
	m.iAlpha2 = ((int) (mi.info.layers[m.iLayer].iAlpha * 255.0F) << 8
		     | (int) (mi.info.layers[m.iLayerSrc].iAlpha * 255.0F));
    }
    
    public void setAlpha(int i, int i_33_, boolean bool) throws Throwable {
	i_33_ = i_33_ <= 0 ? 0 : i_33_ >= 255 ? 255 : i_33_;
	if ((float) i_33_ != mi.info.layers[i].iAlpha) {
	    if (bool) {
		int i_34_ = m.iLayer;
		m.iLayer = i;
		send(new int[] { 8 }, new byte[] { (byte) i_33_ });
		m.iLayer = i_34_;
	    } else {
		mi.info.layers[i].iAlpha = (float) i_33_ / 255.0F;
		mi.repaint();
		this.repaint();
	    }
	}
    }
    
    public void config(int i) {
	LO lo = mi.info.layers[i];
	Choice choice = new Choice();
	choice.add(res.res("Normal"));
	choice.add(res.res("Multiply"));
	choice.add(res.res("Reverse"));
	choice.select(lo.iCopy);
	TextField textfield = new TextField(lo.name);
	Me me = Me.getMe();
	Panel panel = new Panel(new GridLayout(0, 1));
	panel.add(textfield);
	panel.add(choice);
	textfield.addActionListener(me);
	me.add(panel, "Center");
	me.pack();
	Awt.moveCenter(me);
	me.setVisible(true);
	if (me.isOk) {
	    String string = textfield.getText();
	    if (!string.equals(lo.name)) {
		try {
		    send(new int[] { 10 }, string.getBytes("UTF8"));
		} catch (Throwable throwable) {
		    /* empty */
		}
	    }
	    int i_35_ = choice.getSelectedIndex();
	    if (lo.iCopy != i_35_)
		send(new int[] { 9 }, new byte[] { (byte) i_35_ });
	    this.repaint();
	}
    }
}
