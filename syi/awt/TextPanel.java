/* TextPanel - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.PopupMenu;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.StringReader;
import java.net.URL;

import paintchat.Res;

import syi.javascript.JSController;

public class TextPanel extends Canvas implements ActionListener, ItemListener
{
    public boolean isView = true;
    private boolean isPress = false;
    private Font font;
    private Object lock;
    private Applet applet;
    private boolean isSScroll;
    private boolean isGetFSize;
    private boolean isGetSize;
    private boolean isVisitScroll;
    private int H;
    private int WS;
    private int As;
    private int Ds;
    private int Gap;
    private int scrollPos;
    private int scrollMax;
    private int iSeek;
    private TextField textField;
    private Res config;
    private Color nowColor;
    private Color beColor;
    private String[] strings;
    private Color[] colors;
    private Graphics primary;
    private static PopupMenu popup = null;
    private int Y;
    private Dimension size;
    private static final String strEmpty = "";
    
    public TextPanel() {
	lock = new Object();
	isSScroll = false;
	isGetFSize = false;
	isGetSize = false;
	isVisitScroll = true;
	H = 15;
	WS = 12;
	As = 0;
	Ds = 0;
	Gap = 1;
	iSeek = 0;
	nowColor = null;
	beColor = null;
	strings = null;
	colors = null;
	primary = null;
	size = new Dimension();
	nowColor = Color.black;
    }
    
    public TextPanel(Applet applet, int i, Color color, Color color_0_,
		     TextField textfield) {
	lock = new Object();
	isSScroll = false;
	isGetFSize = false;
	isGetSize = false;
	isVisitScroll = true;
	H = 15;
	WS = 12;
	As = 0;
	Ds = 0;
	Gap = 1;
	iSeek = 0;
	nowColor = null;
	beColor = null;
	strings = null;
	colors = null;
	primary = null;
	size = new Dimension();
	init(applet, i, color, color_0_, textfield);
    }
    
    public void actionPerformed(ActionEvent actionevent) {
	try {
	    String string = actionevent.getActionCommand();
	    if (string != null && string.length() > 0) {
		PopupMenu popupmenu = popup;
		if (popupmenu.getItem(0).getLabel().equals(string)) {
		    if ((string = getLine(Y)) != null)
			textField.setText(string);
		} else if (popupmenu.getItem(2).getLabel().equals(string)) {
		    String string_1_ = getLine(Y);
		    int i = string_1_.indexOf("http://");
		    int i_2_ = string_1_.indexOf(' ', i);
		    i_2_ = i_2_ < 0 ? string_1_.length() : i_2_;
		    applet.getAppletContext().showDocument
			(new URL(string_1_.substring(i, i_2_)), "jump_url");
		} else if (popupmenu.getItem(3).getLabel().equals(string)) {
		    String string_3_ = System.getProperty("line.separator");
		    StringBuffer stringbuffer = new StringBuffer();
		    stringbuffer.append("<html><body>");
		    stringbuffer.append(string_3_);
		    for (int i = 0; i < iSeek; i++) {
			string = strings[i];
			if (string != null) {
			    string = Awt.replaceText(string, "&lt;", "<");
			    string = Awt.replaceText(string, "&gt;", ">");
			    stringbuffer.append(string);
			    stringbuffer.append("<br>");
			    stringbuffer.append(string_3_);
			}
		    }
		    stringbuffer.append
			("<div align=\"right\"> <a href=\"http://www.gt.sakura.ne.jp/~ocosama/\">shi-chan site</a></div>");
		    stringbuffer.append("</body></html>");
		    String string_4_ = "text_html";
		    JSController jscontroller = new JSController(applet);
		    jscontroller.openWindow(null, string_4_, null, true, false,
					    true, true, false);
		    jscontroller.showDocument(string_4_, "text/html",
					      stringbuffer.toString());
		} else if (popupmenu.getItem(7).getLabel().equals(string)) {
		    clear();
		    this.repaint();
		} else {
		    if (string.charAt(0) == '+')
			string = string.substring(1);
		    int i = Math.min(Math.max((font.getSize()
					       + Integer.parseInt(string)),
					      4),
				     256);
		    setFont(new Font(font.getName(), font.getStyle(), i));
		    this.repaint();
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void addText(String string) {
	addText(string, true);
    }
    
    public void addText(String string, boolean bool) {
	Object object;
/*	MONITORENTER (object = lock);
	MISSING MONITORENTER*/
	synchronized (object) {
	    if (iSeek > 0) {
		System.arraycopy(strings, 0, strings, 1, iSeek);
		System.arraycopy(colors, 0, colors, 1, iSeek);
	    }
	}
	strings[0] = string;
	colors[0] = nowColor;
	if (iSeek < strings.length - 2)
	    iSeek++;
	if (bool && isGetFSize) {
	    if (primary == null)
		primary = this.getGraphics();
	    paint(primary);
	}
    }
    
    public void call(String string) {
	try {
	    JSController jscontroller = new JSController(applet);
	    jscontroller.runScript(string);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public synchronized void clear() {
	for (int i = 0; i < strings.length; i++) {
	    strings[i] = null;
	    colors[i] = null;
	}
	iSeek = 0;
    }
    
    public void decode(String string) {
	if (string.length() > 0) {
	    if (string.charAt(0) == '$') {
		int i = 0;
		boolean bool = false;
		if (string.startsWith("$js:"))
		    call(string.substring(4, string.length()));
		else {
		    int i_5_;
		    for (/**/; i < string.length(); i = i_5_ + 1) {
			i_5_ = string.indexOf(';', i);
			if (i_5_ < 0)
			    i_5_ = string.length();
			decode(string, i, i_5_);
		    }
		}
	    } else
		decode(string, 0, string.length());
	}
    }
    
    private void decode(String string, int i, int i_6_) {
	if (i_6_ - i > 0) {
	    do {
		try {
		    if (string.charAt(i) != '$') {
			addText(string.substring(i, i_6_));
			break;
		    }
		    int i_7_ = string.indexOf(':', i);
		    String string_8_ = "";
		    String string_9_ = "";
		    if (i_7_ <= i || i_7_ > i_6_)
			string_8_ = string;
		    else
			string_8_ = string.substring(i, i_7_);
		    if (i_7_ >= 0 && i_7_ < i_6_ - 1)
			string_9_ = string.substring(i_7_ + 1, i_6_);
		    if (string_8_.indexOf("$clear") >= 0) {
			Object object;
/*			MONITORENTER (object = lock);
			MISSING MONITORENTER*/
			synchronized (object) {
			    for (int i_10_ = 0; i_10_ < strings.length;
				 i_10_++)
				strings[i_10_] = null;
			    break;
			}
		    }
		    if (string_8_.indexOf("$bkcolor") >= 0)
			this.setBackground(new Color(Res.parseInt(string_9_)));
		    else if (string_8_.indexOf("$color") >= 0)
			setForeground(string_9_.charAt(0) == '/' ? beColor
				      : new Color(Res.parseInt(string_9_)));
		    else if (string_8_.indexOf("$font_size") >= 0) {
			Font font = this.getFont();
			i_7_ = Res.parseInt(string_9_);
			setFont(new Font(font.getName(), 0,
					 font.getSize() + i_7_));
			this.repaint();
		    } else if (string_8_.indexOf("$font") >= 0) {
			Font font = this.getFont();
			int i_11_ = string_9_.equals("bold") ? 1 : 0;
			setFont(new Font(font.getName(), i_11_,
					 font.getSize()));
			this.repaint();
		    } else if (string_8_.indexOf("$js") >= 0)
			call(string_9_);
		    else
			addText(string.substring(i, i_6_));
		} catch (RuntimeException runtimeexception) {
		    /* empty */
		}
	    } while (false);
	}
    }
    
    public String getLine(int i) {
	int i_12_ = (scrollPos + i) / (H + Gap * 2);
	return strings[i_12_ <= 0 ? 0 : i_12_ >= iSeek ? iSeek - 1 : i_12_];
    }
    
    public Dimension getPreferredSize() {
	Dimension dimension = this.getToolkit().getScreenSize();
	int i = Gap * 2;
	Font font = this.getFont();
	FontMetrics fontmetrics = null;
	if (font != null)
	    fontmetrics = this.getFontMetrics(font);
	if (fontmetrics == null) {
	    dimension.setSize(300, 120);
	    return dimension;
	}
	H = fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent() + 1;
	int i_13_ = 0;
	if (strings != null) {
	    for (int i_14_ = 0; i_14_ < iSeek; i_14_++) {
		if (strings[i_14_] != null)
		    i_13_
			= Math.max(fontmetrics.stringWidth(strings[i_14_]) + i,
				   i_13_);
	    }
	}
	i_13_ += i + WS;
	dimension.setSize((i_13_ <= 100 ? 100 : i_13_ >= dimension.width / 2
			   ? dimension.width / 2 : i_13_),
			  (H + i) * (iSeek <= 3 ? 3 : iSeek >= 12 ? 12
				     : iSeek));
	return dimension;
    }
    
    public Dimension getSize() {
	if (size.width + size.height == 0 || !isGetSize)
	    size.setSize(super.getSize());
	return size;
    }
    
    public void init(Applet applet, int i, Color color, Color color_15_,
		     TextField textfield) {
	this.enableEvents(49L);
	textField = textfield;
	this.applet = applet;
	this.setBackground(color);
	setForeground(color_15_);
	nowColor = beColor = color_15_;
	setMaxLabel(i);
    }
    
    public void itemStateChanged(ItemEvent itemevent) {
	try {
	    isSScroll = itemevent.getStateChange() == 1;
	    scrollPos = 0;
	    paint(primary);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void paint(Graphics graphics) {
	try {
	    Dimension dimension = getSize();
	    if (!isView) {
		graphics.setColor(this.getBackground());
		graphics.fillRect(0, 0, dimension.width, dimension.height);
	    } else if (strings != null && graphics != null) {
		int i = Gap * 2;
		if (strings.length != 0) {
		    /* empty */
		}
		if (!isGetFSize) {
		    isGetFSize = true;
		    if (font == null)
			font = super.getFont();
		    FontMetrics fontmetrics = graphics.getFontMetrics();
		    As = fontmetrics.getMaxAscent();
		    Ds = fontmetrics.getMaxDescent();
		    H = As + Ds;
		    scrollPos = 0;
		}
		graphics.setFont(font);
		int i_16_ = H + i;
		scrollMax = i_16_ * iSeek;
		if (dimension.height > 0 && dimension.width > 0) {
		    int i_17_ = WS;
		    int i_18_ = dimension.width - 2 - i_17_;
		    int i_19_ = Math.max(scrollPos / i_16_, 0);
		    int i_20_ = i_19_ + dimension.height / i_16_ + 2;
		    int i_21_ = i_19_ * i_16_ - scrollPos;
		    graphics.setClip(1, 1, i_18_, dimension.height - 2);
		    Color color = this.getBackground();
		    graphics.setColor(color);
		    for (int i_22_ = i_19_; i_22_ < i_20_; i_22_++) {
			if (i_22_ < strings.length && strings[i_22_] != null) {
			    graphics.fillRect(1, i_21_, i_18_, H + i);
			    graphics.setColor(colors[i_22_]);
			    graphics.drawString(strings[i_22_], 1,
						i_21_ + Gap + H - Ds);
			    graphics.setColor(color);
			} else
			    graphics.fillRect(1, i_21_, i_18_, H + i);
			i_21_ += i_16_;
		    }
		    if (i_21_ > 0) {
			graphics.setColor(Color.black);
			graphics.fillRect(1, i_21_, i_18_ - 1,
					  dimension.height - i_21_ - 1);
		    }
		    graphics.setClip(0, 0, dimension.width, dimension.height);
		    if (isVisitScroll) {
			int i_23_ = dimension.height / (H + i);
			int i_24_ = (int) ((float) dimension.height
					   * ((float) i_23_
					      / (float) (iSeek + i_23_)));
			i_21_
			    = (int) ((float) scrollPos / (float) scrollMax
				     * (float) (dimension.height - i_24_ - 1));
			i_18_++;
			i_17_--;
			graphics.setColor(this.getForeground());
			graphics.fillRect(i_18_, 1, 1, dimension.height - 2);
			graphics.fillRect(i_18_ + 1, i_21_, i_17_, i_24_);
			graphics.setColor(color);
			graphics.setColor(this.getBackground());
			graphics.fillRect(i_18_ + 1, 1, i_17_, i_21_);
			graphics.fillRect(i_18_ + 1, i_21_ + i_24_ + 1, i_17_,
					  (dimension.height - i_24_ - i_21_
					   - 1));
			graphics.setColor(this.getForeground());
			graphics.drawRect(0, 0, dimension.width - 1,
					  dimension.height - 1);
		    }
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void popup(int i, int i_25_) {
	if (popup == null) {
	    Menu menu = new Menu("Font size");
	    menu.addActionListener(this);
	    for (int i_26_ = 6; i_26_ > -6; i_26_--) {
		if (i_26_ != 0) {
		    String string = String.valueOf(i_26_);
		    if (i_26_ > 0)
			string = String.valueOf('+') + string;
		    menu.add(string);
		}
	    }
	    popup = new PopupMenu();
	    popup.add("CopyString");
	    popup.addSeparator();
	    popup.add("GotoURL");
	    popup.add("ToHTML");
	    popup.addSeparator();
	    popup.add(menu);
	    popup.addSeparator();
	    popup.add("Erase");
	    CheckboxMenuItem checkboxmenuitem
		= new CheckboxMenuItem("Smooth scroll", isSScroll);
	    checkboxmenuitem.addItemListener(this);
	    popup.add(checkboxmenuitem);
	}
	this.add(popup);
	popup.addActionListener(this);
	popup.getItem(2).setEnabled(getLine(i_25_).indexOf("http://") >= 0);
	popup.show(this, i, i_25_);
    }
    
    protected void processEvent(AWTEvent awtevent) {
	try {
	    if (awtevent instanceof MouseEvent) {
		MouseEvent mouseevent = (MouseEvent) awtevent;
		int i = mouseevent.getX();
		int i_27_ = mouseevent.getY();
		switch (awtevent.getID()) {
		case 501:
		    Y = i_27_;
		    isPress = !Awt.isR(mouseevent);
		    if (!isPress)
			popup(i, i_27_);
		    break;
		case 502:
		    isPress = false;
		    break;
		case 506:
		    if (isPress && Y != i_27_) {
			int i_28_ = -(Y - i_27_);
			if (!isSScroll)
			    i_28_ *= H + Gap * 2;
			scrollPos
			    = Math.max(Math.min(scrollPos + i_28_, scrollMax),
				       0);
			Y = i_27_;
			if (primary == null)
			    primary = this.getGraphics();
			paint(primary);
		    }
		    break;
		}
	    } else if (awtevent instanceof ComponentEvent) {
		switch (awtevent.getID()) {
		case 101:
		case 102:
		    isGetSize = false;
		    if (primary != null) {
			primary.dispose();
			primary = null;
		    }
		    if (isGetFSize)
			this.repaint();
		    /* fall through */
		default:
		    break;
		}
	    } else
		super.processEvent(awtevent);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void remove(String string) {
	try {
	    Object object;
/*	    MONITORENTER (object = lock);
	    MISSING MONITORENTER*/
	    synchronized (object) {
		int i = iSeek;
		for (int i_29_ = 0; i_29_ < i; i_29_++) {
		    String string_30_;
		    if ((string_30_ = strings[i_29_]) != null
			&& string_30_.equals(string)) {
			if (i_29_ != i - 1)
			    System.arraycopy(strings, i_29_ + 1, strings,
					     i_29_, i - i_29_ - 1);
			strings[i - 1] = null;
			iSeek--;
			break;
		    }
		}
	    }
	    this.repaint();
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	}
    }
    
    public void setFont(Font font) {
	this.font = font;
	isGetFSize = false;
    }
    
    public void setForeground(Color color) {
	beColor = nowColor;
	nowColor = color;
	super.setForeground(color);
    }
    
    public void setMaxLabel(int i) {
	if (i > 0) {
	    String[] strings = new String[i];
	    Color[] colors = new Color[i];
	    scrollPos = 0;
	    if (this.strings != null)
		System.arraycopy(this.strings, 0, strings, 0,
				 this.strings.length);
	    if (this.colors != null)
		System.arraycopy(this.colors, 0, colors, 0,
				 this.colors.length);
	    this.strings = strings;
	    this.colors = colors;
	}
    }
    
    public void setRText(String string) {
	StringBuffer stringbuffer = new StringBuffer();
	try {
	    BufferedReader bufferedreader
		= new BufferedReader(new StringReader(string));
	    String string_31_;
	    while ((string_31_ = bufferedreader.readLine()) != null) {
		stringbuffer.insert(0, string_31_);
		stringbuffer.insert(0, '\n');
	    }
	    bufferedreader.close();
	} catch (java.io.IOException ioexception) {
	    /* empty */
	}
	setText(stringbuffer.toString());
    }
    
    public void setText(String string) {
	int i = string.length();
	CharArrayWriter chararraywriter = new CharArrayWriter();
	for (int i_32_ = 0; i_32_ < i; i_32_++) {
	    char c = string.charAt(i_32_);
	    if (c == '\r' || c == '\n') {
		if (chararraywriter.size() > 0) {
		    decode(chararraywriter.toString());
		    chararraywriter.reset();
		}
	    } else
		chararraywriter.write(c);
	}
	if (chararraywriter.size() > 0)
	    decode(chararraywriter.toString());
    }
    
    public void setVisitScroll(boolean bool) {
	isVisitScroll = bool;
	WS = bool ? 12 : 0;
    }
    
    public void update(Graphics graphics) {
	paint(graphics);
    }
}
