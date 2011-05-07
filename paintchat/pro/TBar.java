/* TBar - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat.pro;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.net.URL;

import paintchat.Res;

import paintchat_client.Mi;

import syi.awt.Awt;
import syi.awt.LComponent;

public class TBar extends LComponent
{
    private boolean isOption;
    private Res res;
    private Res config;
    private LComponent[] cs;
    private String[] strs;
    private boolean[] flags;
    private Mi mi;
    private URL codebase;
    private String strAuthor;
    private int W;
    private int H;
    private Image image = null;
    private Color[][] cls = new Color[2][3];
    private Color clT;
    
    public TBar(Res res, Res res_0_, LComponent[] lcomponents) {
	this.res = res_0_;
	config = res;
	cs = lcomponents;
	H = (int) (19.0F * LComponent.Q);
    }
    
    public final void drawFrame(Graphics graphics, boolean bool, int i,
				int i_1_, int i_2_, int i_3_) {
	Color[] colors = cls[bool ? 1 : 0];
	Awt.drawFrame(graphics, bool, i, i_1_, i_2_, i_3_, colors[2],
		      colors[1]);
    }
    
    public final void fillFrame(Graphics graphics, boolean bool, int i,
				int i_4_, int i_5_, int i_6_) {
	drawFrame(graphics, bool, i, i_4_, i_5_, i_6_);
	Color[] colors = cls[bool ? 1 : 0];
	Awt.setup();
	Awt.fillFrame(graphics, bool, i, i_4_, i_5_, i_6_, cls[0][0],
		      cls[1][0], colors[2], colors[1]);
    }
    
    public void init() {
	isHide = false;
	int i = H;
	FontMetrics fontmetrics = this.getFontMetrics(this.getFont());
	int i_7_ = cs.length - 1;
	int i_8_ = 0;
	strs = new String[i_7_];
	for (int i_9_ = 0; i_9_ < i_7_; i_9_++) {
	    String string = res.res("window_" + i_9_);
	    i_8_ = Math.max(fontmetrics.stringWidth(string) + string.length(),
			    i_8_);
	    strs[i_9_] = string;
	    cs[i_9_].setTitle(string);
	}
	s();
	Color[] colors = cls[0];
	cls[0] = cls[1];
	cls[1] = colors;
	W = i_8_;
	this.setDimension(new Dimension(W, i), new Dimension(W, i * i_7_),
			  new Dimension(W * i_7_, i * i_7_));
    }
    
    public void initOption(URL url, Mi mi) {
	isOption = true;
	this.mi = mi;
	codebase = url;
	int i = 2;
	strs = new String[i];
	flags = new boolean[i];
	strAuthor = res.res("option_author");
	s();
	this.setDimension(null, new Dimension(10, 10), null);
    }
    
    public void mouseO(MouseEvent mouseevent) {
	if (mouseevent.getID() == 501) {
	    int i = mouseevent.getY();
	    if (i <= H) {
		boolean bool = !flags[0];
		mi.isGUI = bool;
		flags[0] = bool;
		this.repaint();
		mi.setVisible(false);
		mi.setDimension(null, mi.getSize(),
				this.getParent().getSize());
		mi.setVisible(true);
	    } else if (i <= H * 2) {
		z();
		this.repaint();
	    } else if (i > H * 3) {
		try {
		    String string
			= config.getP("app_url", "http://shichan.jp/");
		    if ((string.length() != 1 || string.charAt(0) != '_')
			&& mi.alert("kakunin_0", true)) {
			Applet applet = (Applet) this.getParent().getParent();
			applet.getAppletContext().showDocument
			    (new URL(applet.getCodeBase(), string), "_blank");
		    }
		} catch (Throwable throwable) {
		    throwable.printStackTrace();
		}
	    }
	}
    }
    
    public void paint2(Graphics graphics) {
	this.getSize();
	if (isOption)
	    paintO(graphics);
	else
	    paintBar(graphics);
    }
    
    private void paintBar(Graphics graphics) {
	int i = 0;
	int i_10_ = 0;
	int i_11_ = 0;
	Dimension dimension = this.getSize();
	int i_12_ = W;
	int i_13_ = H;
	for (int i_14_ = 0; i_14_ < cs.length; i_14_++) {
	    if (cs[i_14_] != null && cs[i_14_] != this) {
		fillFrame(graphics, !cs[i_14_].isVisible(), i, i_10_, i_12_,
			  i_13_);
		graphics.setColor(clT == null ? Awt.cFore : clT);
		graphics.drawString(strs[i_11_++], i + 2, i_10_ + i_13_ - 3);
		if ((i += i_12_) + i_12_ > dimension.width) {
		    i = 0;
		    i_10_ += i_13_;
		}
	    }
	}
    }
    
    private void paintO(Graphics graphics) {
	if (image == null) {
	    try {
		FontMetrics fontmetrics = this.getFontMetrics(this.getFont());
		int i = strs.length;
		int i_15_ = 0;
		for (int i_16_ = 0; i_16_ < i; i_16_++) {
		    String string = res.res("option_" + i_16_);
		    i_15_ = Math.max((fontmetrics.stringWidth(string + " OFF")
				      + string.length() + 3),
				     i_15_);
		    strs[i_16_] = string;
		}
		i_15_ = Math.max((fontmetrics.stringWidth(strAuthor)
				  + strAuthor.length()),
				 i_15_);
		image = this.getToolkit()
			    .createImage((byte[]) config.getRes("bn.gif"));
		Awt.wait(image);
		int i_17_ = image.getWidth(null);
		int i_18_ = image.getHeight(null);
		if (LComponent.Q > 1.0F) {
		    i_17_ *= LComponent.Q;
		    i_18_ *= LComponent.Q;
		}
		W = Math.max(i_15_, i_17_);
		H = fontmetrics.getHeight() + 2;
		Dimension dimension
		    = new Dimension(W, H * (i + 2) + i_18_ + 2);
		this.setDimension(new Dimension(W, H), dimension, dimension);
	    } catch (RuntimeException runtimeexception) {
		/* empty */
	    }
	}
	int i = image.getWidth(null);
	int i_19_ = image.getHeight(null);
	if (LComponent.Q > 1.0F) {
	    i *= LComponent.Q;
	    i_19_ *= LComponent.Q;
	}
	Dimension dimension = this.getSize();
	graphics.setFont(this.getFont());
	int i_20_ = 0;
	int i_21_ = 0;
	int i_22_ = W;
	int i_23_ = H;
	int i_24_ = strs.length;
	for (int i_25_ = 0; i_25_ < i_24_; i_25_++) {
	    fillFrame(graphics, flags[i_25_], i_20_, i_21_, i_22_, i_23_);
	    graphics.setColor(clT == null ? this.getForeground() : clT);
	    graphics.drawString(strs[i_25_] + (flags[i_25_] ? " ON" : " OFF"),
				i_20_ + 2, i_21_ + i_23_ - 3);
	    i_21_ += i_23_;
	}
	i_21_ = dimension.height - H - i_19_ - 2;
	graphics.drawRect(i_20_, i_21_, i_22_ - 1,
			  dimension.height - i_21_ - 1);
	graphics.drawImage(image, (dimension.width - i) / 2, i_21_ + 2, i,
			   i_19_, this.getBackground(), null);
	graphics.drawString(strAuthor,
			    ((dimension.width - graphics.getFontMetrics()
						    .stringWidth(strAuthor))
			     / 2),
			    dimension.height - 2);
    }
    
    public void pMouse(MouseEvent mouseevent) {
	if (isOption)
	    mouseO(mouseevent);
	else {
	    int i = mouseevent.getID();
	    int i_26_ = W;
	    int i_27_ = H;
	    Dimension dimension = this.getSize();
	    if (i == 504)
		this.repaint();
	    if (i == 501) {
		int i_28_
		    = (dimension.width / i_26_ * (mouseevent.getY() / i_27_)
		       + mouseevent.getX() / i_26_);
		if (i_28_ < cs.length && cs[i_28_] != this) {
		    cs[i_28_].setVisible(!cs[i_28_].isVisible());
		    this.invalidate();
		    this.repaint();
		}
	    }
	}
    }
    
    private void s() {
	String string = "pro_menu_color_off";
	for (int i = 0; i < 2; i++) {
	    Color[] colors = cls[i];
	    if (config.getP(string) != null)
		colors[0] = new Color(config.getP(string, 0));
	    if (config.getP(string + "_hl") != null)
		colors[1] = new Color(config.getP(string + "_hl", 0));
	    if (config.getP(string + "_dk") != null)
		colors[2] = new Color(config.getP(string + "_dk", 0));
	    string = "pro_menu_color_on";
	}
	if (config.getP("pro_menu_color_text") != null)
	    clT = new Color(config.getP("pro_menu_color_text", 0));
    }
    
    public void z() {
	boolean bool = !flags[1];
	flags[1] = bool;
	for (int i = 0; i < cs.length; i++) {
	    if (cs[i] != null)
		cs[i].isUpDown = bool;
	}
    }
}
