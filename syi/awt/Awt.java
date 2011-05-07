/* Awt - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Awt
{
    public static Frame main_frame = null;
    public static Color cC;
    public static Color cDk;
    public static Color cLt;
    public static Color cBk;
    public static Color cFore;
    public static Color cF;
    public static Color cFSel;
    public static Color clBar;
    public static Color clLBar;
    public static Color clBarT;
    private static Font fontDef = null;
    private static float Q = 0.0F;
    private static MediaTracker mt = null;
    
    public static final void drawFrame(Graphics graphics, boolean bool, int i,
				       int i_0_, int i_1_, int i_2_) {
	setup();
	drawFrame(graphics, bool, i, i_0_, i_1_, i_2_, cDk, cLt);
    }
    
    public static final void drawFrame(Graphics graphics, boolean bool, int i,
				       int i_3_, int i_4_, int i_5_,
				       Color color, Color color_6_) {
	setup();
	int i_7_ = i + i_4_;
	int i_8_ = i_3_ + i_5_;
	graphics.setColor(color == null ? cDk : color);
	graphics.fillRect(i, i_3_, i_4_, 1);
	graphics.fillRect(i, i_3_ + 1, 1, i_5_ - 2);
	graphics.fillRect(i + 2, i_8_ - 2, i_4_ - 2, 1);
	graphics.fillRect(i_7_ - 1, i_3_ + 2, 1, i_5_ - 4);
	graphics.setColor(color_6_ == null ? cLt : color_6_);
	if (!bool) {
	    graphics.fillRect(i + 1, i_3_ + 1, i_4_ - 2, 1);
	    graphics.fillRect(i + 1, i_3_ + 2, 1, i_5_ - 4);
	}
	graphics.fillRect(i + 1, i_8_ - 1, i_4_ - 1, 1);
	graphics.fillRect(i_7_, i_3_ + 1, 1, i_5_ - 2);
    }
    
    public static final void fillFrame(Graphics graphics, boolean bool, int i,
				       int i_9_, int i_10_, int i_11_) {
	fillFrame(graphics, bool, i, i_9_, i_10_, i_11_, cC, cDk, cDk, cLt);
    }
    
    public static final void fillFrame(Graphics graphics, boolean bool, int i,
				       int i_12_, int i_13_, int i_14_,
				       Color color, Color color_15_,
				       Color color_16_, Color color_17_) {
	drawFrame(graphics, bool, i, i_12_, i_13_, i_14_, color_16_,
		  color_17_);
	graphics.setColor(bool ? color_15_ == null ? cDk : color_15_
			  : color == null ? cC : color);
	graphics.fillRect(i + 2, i_12_ + 2, i_13_ - 3, i_14_ - 4);
    }
    
    public static void getDef(Component component) {
	setup();
	component.setBackground(cBk);
	component.setForeground(cFore);
	component.setFont(getDefFont());
	if (component instanceof LComponent) {
	    LComponent lcomponent = (LComponent) component;
	    lcomponent.clBar = clBar;
	    lcomponent.clLBar = clLBar;
	    lcomponent.clBarT = clBarT;
	    lcomponent.clFrame = cF;
	}
    }
    
    public static Font getDefFont() {
	if (fontDef == null)
	    fontDef = new Font("sansserif", 0, (int) (16.0F * q()));
	return fontDef;
    }
    
    public static Component getParent(Component component) {
	Container container = component.getParent();
	return (container == null ? component : container instanceof Window
		? (Component) container : getParent(container));
    }
    
    public static Frame getPFrame() {
	if (main_frame == null)
	    main_frame = new Frame();
	return main_frame;
    }
    
    public static boolean isR(MouseEvent mouseevent) {
	if (!mouseevent.isAltDown() && !mouseevent.isControlDown()
	    && (mouseevent.getModifiers() & 0x4) == 0)
	    return false;
	return true;
    }
    
    public static boolean isWin() {
	String string = "Win";
	return System.getProperty("os.name", string).startsWith(string);
    }
    
    public static void moveCenter(Window window) {
	Dimension dimension = window.getToolkit().getScreenSize();
	Dimension dimension_18_ = window.getSize();
	window.setLocation(dimension.width / 2 - dimension_18_.width / 2,
			   dimension.height / 2 - dimension_18_.height / 2);
    }
    
    public static InputStream openStream(URL url) throws IOException {
	URLConnection urlconnection = url.openConnection();
	urlconnection.setUseCaches(true);
	return urlconnection.getInputStream();
    }
    
    public static float q() {
	if (Q == 0.0F) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int i = 2264;
	    int i_19_ = dimension.width + dimension.height;
	    Q = Math.min(1.0F + (float) (i_19_ - i) / (float) i / 2.0F, 2.0F);
	}
	return Q;
    }
    
    public static String replaceText(String string, String string_20_,
				     String string_21_) {
	if (string.indexOf(string_21_) < 0)
	    return string;
	StringBuffer stringbuffer = new StringBuffer();
	try {
	    char[] cs = string_21_.toCharArray();
	    if (cs.length <= 0)
		return string;
	    int i = 0;
	    int i_22_ = 0;
	    int i_23_ = string.length();
	    for (int i_24_ = 0; i_24_ < i_23_; i_24_++) {
		char c;
		if ((c = string.charAt(i_24_)) == cs[i_22_]) {
		    if (i_22_ == 0)
			i = i_24_;
		    if (++i_22_ >= cs.length) {
			i_22_ = 0;
			stringbuffer.append(string_20_);
		    }
		} else {
		    if (i_22_ > 0) {
			for (int i_25_ = 0; i_25_ < i_22_; i_25_++)
			    stringbuffer.append(string.charAt(i + i_25_));
			i_22_ = 0;
		    }
		    stringbuffer.append(c);
		}
	    }
	} catch (RuntimeException runtimeexception) {
	    System.out.println("replace" + runtimeexception);
	}
	return stringbuffer.toString();
    }
    
    public static void setDef(Component component, boolean bool) {
	try {
	    if (!bool)
		bool = true;
	    else {
		Container container = component.getParent();
		container.setFont(container.getFont());
		container.setForeground(container.getForeground());
		container.setBackground(container.getBackground());
	    }
	    if (component instanceof Container) {
		Component[] components
		    = ((Container) component).getComponents();
		if (components != null) {
		    for (int i = 0; i < components.length; i++) {
			component = components[i];
			setDef(component, true);
		    }
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public static void setPFrame(Frame frame) {
	main_frame = frame;
    }
    
    public static final void setup() {
	if (cC == null) {
	    cC = new Color(13487565);
	    cDk = cLt = null;
	}
	if (cDk == null)
	    cDk = cC.darker();
	if (cLt == null)
	    cLt = cC.brighter();
	if (cBk == null)
	    cBk = new Color(13619199);
	if (cFore == null)
	    cFore = new Color(5263480);
	if (cF == null)
	    cF = cFore;
	if (cFSel == null)
	    cFSel = new Color(15610675);
	if (clBar == null)
	    clBar = new Color(6711039);
	if (clLBar == null)
	    clLBar = new Color(8947967);
	if (clBarT == null)
	    clBarT = Color.white;
    }
    
    public static Image toMin(Image image, int i, int i_26_) {
	Image image_27_ = image.getScaledInstance(i, i_26_, 16);
	image.flush();
	wait(image_27_);
	return image_27_;
    }
    
    public static String trimString(String string, String string_28_,
				    String string_29_) {
	if (string == null || string.length() <= 0 || string_28_ == null
	    || string_29_ == null)
	    return "";
	try {
	    int i;
	    if ((i = string.indexOf(string_28_)) == -1)
		return "";
	    int i_30_;
	    if ((i_30_ = string.indexOf(string_29_, i + string_28_.length()))
		== -1)
		i_30_ = string.length() - 1;
	    return string.substring(i + string_28_.length(), i_30_);
	} catch (RuntimeException runtimeexception) {
	    System.out.println("t_trimString:" + runtimeexception.toString());
	    return "";
	}
    }
    
    public static int[] getPix(Image image) {
	try {
	    PixelGrabber pixelgrabber
		= new PixelGrabber(image, 0, 0, image.getWidth(null),
				   image.getHeight(null), true);
	    pixelgrabber.grabPixels();
	    return (int[]) pixelgrabber.getPixels();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	    return null;
	}
    }
    
    public static void wait(Image image) {
	if (mt == null)
	    mt = new MediaTracker(getPFrame());
	try {
	    mt.addImage(image, 0);
	    mt.waitForID(0);
	} catch (InterruptedException interruptedexception) {
	    /* empty */
	}
	mt.removeImage(image, 0);
    }
}
