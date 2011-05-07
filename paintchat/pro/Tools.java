/* Tools - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat.pro;
import java.applet.Applet;
import java.awt.CheckboxMenuItem;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ColorModel;

import paintchat.M;
import paintchat.Res;
import paintchat.SRaster;
import paintchat.ToolBox;

import paintchat_client.L;
import paintchat_client.Mi;

import syi.awt.Awt;
import syi.awt.LComponent;

public class Tools implements ToolBox, ActionListener
{
    private Applet applet;
    private Component parent;
    private Res res;
    protected Mi mi;
    M.Info info;
    M mg;
    private LComponent[] components;
    private TPic tPic;
    private TPalette tPalette;
    protected int[] iBuffer;
    private Image image = null;
    private SRaster raster;
    /*synthetic*/ static Class class$0;
    
    public void actionPerformed(ActionEvent actionevent) {
	try {
	    PopupMenu popupmenu = (PopupMenu) actionevent.getSource();
	    int i = popupmenu.getItemCount();
	    String string = actionevent.getActionCommand();
	    for (int i_0_ = 0; i_0_ < i; i_0_++) {
		if (popupmenu.getItem(i_0_).getLabel().equals(string)) {
		    mg.set(popupmenu.getName() + '=' + String.valueOf(i_0_));
		    repaint();
		    break;
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public String getC() {
	return tPalette.getC();
    }
    
    public LComponent[] getCs() {
	return components;
    }
    
    public Dimension getCSize() {
	return null;
    }
    
    public void init(Container container, Applet applet, Res res, Res res_1_,
		     Mi mi) {
	this.applet = applet;
	info = mi.info;
	mg = info.m;
	this.res = res_1_;
	iBuffer = mi.user.getBuffer();
	this.mi = mi;
	parent = container;
	Dimension dimension = container.getSize();
	LComponent[] lcomponents = new LComponent[9];
	TPen tpen = new TPen(this, info, res, null, lcomponents);
	tpen.init(0);
	lcomponents[0] = tpen;
	TPen tpen_2_ = new TPen(this, info, res, tpen, lcomponents);
	tpen_2_.init(1);
	lcomponents[1] = tpen_2_;
	TPalette tpalette = new TPalette();
	tpalette.setLocation(((int) ((float) tpen.getSizeW().width * Awt.q())
			      + 10),
			     0);
	tpalette.init(this, info, res, res_1_);
	lcomponents[2] = tpalette;
	tPalette = tpalette;
	TPen tpen_3_ = new TPen(this, info, res, null, lcomponents);
	tpen_3_.initTT();
	lcomponents[3] = tpen_3_;
	TPic tpic = new TPic(this);
	lcomponents[4] = tpic;
	tPic = tpic;
	TPen tpen_4_ = new TPen(this, info, res, null, lcomponents);
	tpen_4_.setLocation((tpalette.getLocation().x
			     + tpalette.getSizeW().width),
			    0);
	tpen_4_.initHint();
	lcomponents[5] = tpen_4_;
	L l = new L(mi, this, res_1_, res);
	lcomponents[6] = l;
	TBar tbar = new TBar(res, res_1_, lcomponents);
	lcomponents[7] = tbar;
	TBar tbar_5_ = new TBar(res, res_1_, lcomponents);
	lcomponents[8] = tbar_5_;
	tbar.initOption(applet.getCodeBase(), mi);
	tbar_5_.init();
	tpen_3_.setLocation(tpen_4_.getLocation().x + tpen_4_.getSizeW().width,
			    0);
	tpen_2_.setLocation(0, tpen.getSizeW().height);
	tpic.setLocation(0,
			 tpen_2_.getLocation().y + tpen_2_.getSizeW().height);
	tbar_5_.setLocation(dimension.width - tbar_5_.getSizeW().width, 0);
	for (int i = 0; i < lcomponents.length; i++) {
	    lcomponents[i].setVisible(false);
	    container.add(lcomponents[i], 0);
	}
	components = lcomponents;
	tbar_5_.setVisible(true);
	tpen.setItem(0, null);
    }
    
    public void lift() {
	((TPen) components[0]).setItem(-1, null);
    }
    
    protected Image mkImage(int i, int i_6_) {
	if (raster == null) {
	    raster = new SRaster(ColorModel.getRGBdefault(), iBuffer, i, i_6_);
	    image = applet.createImage(raster);
	} else
	    raster.newPixels(image, iBuffer, i, i_6_);
	return image;
    }
    
    public void pack() {
	if (components != null) {
	    for (int i = 0; i < components.length; i++) {
		if (components[i] != null)
		    components[i].inParent();
	    }
	    mi.setVisible(false);
	    Dimension dimension = parent.getSize();
	    mi.setDimension(null, new Dimension(dimension),
			    new Dimension(dimension));
	    Dimension dimension_7_ = mi.getSize();
	    mi.setLocation((dimension.width - dimension_7_.width) / 2,
			   (dimension.height - dimension_7_.height) / 2);
	    mi.setVisible(true);
	}
    }
    
    void repaint() {
	for (int i = 0; i < components.length; i++)
	    components[i].repaint();
    }
    
    public void selPix(boolean bool) {
	((TPen) components[0]).undo(bool);
    }
    
    public void setARGB(int i) {
	int i_8_ = mg.iAlpha << 24 | mg.iColor;
	mg.iAlpha = i >>> 24;
	mg.iColor = i & 0xffffff;
	if (i_8_ != i) {
	    tPic.setColor(i);
	    tPalette.setColor(i);
	}
    }
    
    public void setC(String string) {
	tPalette.setC(string);
    }
    
    void setField(Component component, String string, String string_9_, int i,
		  int i_10_) {
	try {
	    PopupMenu popupmenu = new PopupMenu();
	    popupmenu.setName(string);
	    popupmenu.addActionListener(this);
	    Class var_class = class$0;
	    if (var_class == null) {
		Class var_class_11_;
		try {
		    var_class_11_ = Class.forName("paintchat.M");
		} catch (ClassNotFoundException classnotfoundexception) {
		    NoClassDefFoundError noclassdeffounderror
			= new NoClassDefFoundError();
		    (noclassdeffounderror).NoClassDefFoundError
			(classnotfoundexception.getMessage());
		    throw noclassdeffounderror;
		}
		var_class = class$0 = var_class_11_;
	    }
	    int i_12_ = var_class.getField(string).getInt(mg);
	    for (int i_13_ = 0; i_13_ < 16; i_13_++) {
		Object object = res.get((Object) (string_9_ + i_13_));
		if (object != null) {
		    if (i_12_ == i_13_)
			popupmenu.add(new CheckboxMenuItem(object.toString(),
							   true));
		    else
			popupmenu.add(object.toString());
		}
	    }
	    component.add(popupmenu);
	    popupmenu.show(component, i, i_10_);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void setLineSize(int i) {
	tPalette.setLineSize(i);
    }
    
    public void setMask(Component component, int i, int i_14_, int i_15_,
			boolean bool) {
	if (bool)
	    setField(component, "iMask", "mask_", i_14_, i_15_);
	else {
	    mg.iColorMask = i & 0xffffff;
	    components[4].repaint();
	}
    }
    
    public void setRGB(int i) {
	setARGB(mg.iAlpha << 24 | i & 0xffffff);
    }
    
    public void up() {
	tPic.repaint();
	tPalette.repaint();
	if (components != null && components[6] != null)
	    components[6].repaint();
    }
}
