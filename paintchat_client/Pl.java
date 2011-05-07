/* Pl - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_client;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.lang.reflect.Method;
import java.net.URL;

import paintchat.M;
import paintchat.MgText;
import paintchat.Res;
import paintchat.ToolBox;

import syi.awt.Awt;
import syi.awt.LButton;
import syi.awt.LComponent;
import syi.awt.TextPanel;
import syi.util.ThreadPool;

public class Pl extends Panel
    implements Runnable, ActionListener, IMi, KeyListener
{
    private static final String STR_VERSION = "PaintChatClient v3.66";
    private static final String STR_INFO = "PaintChat";
    protected Applet applet;
    private boolean isStart = false;
    private int iScrollType = 0;
    private Data dd;
    public Res res;
    public Mi mi;
    private ToolBox tool = null;
    private Panel tPanel;
    private Panel tPanelB;
    private TextPanel tText;
    private TextField tField;
    private TextPanel tList;
    private Panel miPanel;
    private Label tLabel;
    private MgText mgText;
    private Dimension dPack = new Dimension();
    private Dimension dSize = null;
    private Dimension dMax = new Dimension();
    private int iGap = 5;
    private int iCenter = 80;
    private int iCenterOld = -1;
    private Color clInfo;
    private AudioClip[] sounds = null;
    private int iPG = 10;
    /*synthetic*/ static Class class$0;
    /*synthetic*/ static Class class$1;
    /*synthetic*/ static Class class$2;
    /*synthetic*/ static Class class$3;
    
    public Pl(Applet applet) {
	super((java.awt.LayoutManager) null);
	this.applet = applet;
    }
    
    public void actionPerformed(ActionEvent actionevent) {
	try {
	    Object object = actionevent.getSource();
	    if (object instanceof LButton) {
		switch (Integer.parseInt(((Component) object).getName())) {
		case 0:
		    f(tPanel, true);
		    break;
		case 1:
		    f(this, false);
		    break;
		case 2:
		    mExit();
		}
	    } else
		typed();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void mExit() {
	try {
	    applet.getAppletContext().showDocument
		(new URL(applet.getDocumentBase(),
			 dd.config.getP("exit", "../index.html")));
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    protected void addInOut(String string, boolean bool) {
	if (bool) {
	    tList.addText(string);
	    string += res.res("entered");
	    dSound(2);
	} else {
	    tList.remove(string);
	    string += res.res("leaved");
	    dSound(3);
	}
	addTextInfo(string, false);
    }
    
    protected void addSText(String string) {
	tText.decode(string);
    }
    
    protected void addText(String string, String string_0_, boolean bool) {
	if (string_0_ == null)
	    tText.repaint();
	else
	    tText.addText((string == null ? string_0_
			   : string + '>' + string_0_),
			  bool);
    }
    
    protected void addTextInfo(String string, boolean bool) {
	Color color = tText.getForeground();
	tText.setForeground(Color.red);
	addText(null, "PaintChat>" + string, bool);
	tText.setForeground(color);
    }
    
    public void changeSize() {
	mi.resetGraphics();
	pack();
    }
    
    public void destroy() {
	try {
	    if (dd != null)
		dd.destroy();
	    dd = null;
	    tool = null;
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    protected void dSound(int i) {
	try {
	    if (sounds != null && sounds[i] != null)
		sounds[i].play();
	} catch (RuntimeException runtimeexception) {
	    sounds = null;
	}
    }
    
    private void f(Component component, boolean bool) {
	try {
	    boolean bool_1_ = false;
	    Panel panel = bool ? (Panel) this : applet;
	    Component[] components = panel.getComponents();
	    for (int i = 0; i < components.length; i++) {
		if (components[i] == component) {
		    bool_1_ = true;
		    break;
		}
	    }
	    Container container = component.getParent();
	    container.remove(component);
	    if (bool_1_) {
		if (bool)
		    iCenter = 100;
		pack();
		Frame frame = new Frame("PaintChatClient v3.66");
		frame.setLayout(new BorderLayout());
		frame.add(component, "Center");
		frame.pack();
		frame.setVisible(true);
	    } else {
		((Window) container).dispose();
		if (bool) {
		    iCenter = 70;
		    this.add(component);
		} else {
		    applet.add(component, "Center");
		    applet.validate();
		}
		pack();
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public Dimension getSize() {
	if (dSize == null)
	    dSize = super.getSize();
	return dSize;
    }
    
    public void iPG(boolean bool) {
	if (bool)
	    iPG = Math.min(100, iPG + 10);
	if (!isStart) {
	    try {
		Graphics graphics = this.getGraphics();
		if (graphics != null) {
		    String string = String.valueOf(iPG) + '%';
		    FontMetrics fontmetrics = graphics.getFontMetrics();
		    int i = fontmetrics.getHeight() + 2;
		    graphics.setColor(this.getBackground());
		    graphics.fillRect(5, 5 + i,
				      fontmetrics.stringWidth(string) + 15,
				      i + 10);
		    graphics.setColor(this.getForeground());
		    graphics.drawString("PaintChatClient v3.66", 10, 10 + i);
		    graphics.drawString(string, 10, 10 + i * 2);
		    graphics.dispose();
		}
	    } catch (Throwable throwable) {
		/* empty */
	    }
	}
    }
    
    public void keyPressed(KeyEvent keyevent) {
	try {
	    boolean bool = keyevent.isAltDown() || keyevent.isControlDown();
	    int i = keyevent.getKeyCode();
	    if (bool) {
		if (i == 38) {
		    keyevent.consume();
		    iCenter = Math.max(iCenter - 4, 0);
		    pack();
		}
		if (i == 40) {
		    keyevent.consume();
		    iCenter = Math.min(iCenter + 4, 100);
		    pack();
		}
		if (i == 83) {
		    keyevent.consume();
		    typed();
		}
	    } else {
		switch (i) {
		case 117:
		    f(this, false);
		    break;
		case 10:
		    break;
		default:
		    dSound(0);
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void keyReleased(KeyEvent keyevent) {
	/* empty */
    }
    
    public void keyTyped(KeyEvent keyevent) {
	/* empty */
    }
    
    private Cursor loadCursor(String string, int i) {
	try {
	    if (string != null && string.length() > 0) {
		boolean bool = string.equals("none");
		int i_2_;
		int i_3_;
		int i_4_;
		int i_5_;
		Image image;
		if (!bool) {
		    image
			= this.getToolkit()
			      .createImage((byte[]) dd.config.getRes(string));
		    if (image == null)
			return Cursor.getPredefinedCursor(i);
		    Awt.wait(image);
		    i_4_ = image.getWidth(null);
		    i_5_ = image.getHeight(null);
		    i_2_ = string.indexOf('x');
		    if (i_2_ == -1)
			i_2_
			    = (i_2_ == -1 ? i_4_ / 2 - 1
			       : Integer.parseInt(string.substring
						  (i_2_ + 1,
						   string.indexOf('x',
								  i_2_ + 1))));
		    i_3_ = string.indexOf('y');
		    if (i_3_ == -1)
			i_3_
			    = (i_3_ == -1 ? i_5_ / 2 - 1
			       : Integer.parseInt(string.substring
						  (i_3_ + 1,
						   string.indexOf('y',
								  i_3_ + 1))));
		} else {
		    i_2_ = i_3_ = 7;
		    i_4_ = i_5_ = 16;
		    image = null;
		}
		try {
		    if (image == null) {
			IndexColorModel indexcolormodel
			    = new IndexColorModel(8, 2, new byte[2],
						  new byte[2], new byte[2], 0);
			image = this.createImage(new MemoryImageSource
						 (i_4_, i_5_, indexcolormodel,
						  new byte[i_4_ * i_5_], 0,
						  i_4_));
		    }
		    java.awt.Toolkit toolkit = this.getToolkit();
		    toolkit.getClass();
		    Class var_class = class$0;
		    if (var_class == null) {
			Class var_class_6_;
			try {
			    var_class_6_ = Class.forName("java.awt.Toolkit");
			} catch (ClassNotFoundException classnotfoundexception) {
			    NoClassDefFoundError noclassdeffounderror
				= new NoClassDefFoundError(classnotfoundexception.getMessage());
			    throw noclassdeffounderror;
			}
			var_class = class$0 = var_class_6_;
		    }
		    String string_7_ = "createCustomCursor";
		    Class[] var_classes = new Class[3];
		    int i_8_ = 0;
		    Class var_class_9_ = class$1;
		    if (var_class_9_ == null) {
			Class var_class_10_;
			try {
			    var_class_10_ = Class.forName("java.awt.Image");
			} catch (ClassNotFoundException classnotfoundexception) {
			    NoClassDefFoundError noclassdeffounderror
				= new NoClassDefFoundError(classnotfoundexception.getMessage());
			    throw noclassdeffounderror;
			}
			var_class_9_ = class$1 = var_class_10_;
		    }
		    var_classes[i_8_] = var_class_9_;
		    int i_11_ = 1;
		    Class var_class_12_ = class$2;
		    if (var_class_12_ == null) {
			Class var_class_13_;
			try {
			    var_class_13_ = Class.forName("java.awt.Point");
			} catch (ClassNotFoundException classnotfoundexception) {
			    NoClassDefFoundError noclassdeffounderror
				= new NoClassDefFoundError(classnotfoundexception.getMessage());
			    throw noclassdeffounderror;
			}
			var_class_12_ = class$2 = var_class_13_;
		    }
		    var_classes[i_11_] = var_class_12_;
		    int i_14_ = 2;
		    Class var_class_15_ = class$3;
		    if (var_class_15_ == null) {
			Class var_class_16_;
			try {
			    var_class_16_ = Class.forName("java.lang.String");
			} catch (ClassNotFoundException classnotfoundexception) {
			    NoClassDefFoundError noclassdeffounderror
				= new NoClassDefFoundError(classnotfoundexception.getMessage());
			    throw noclassdeffounderror;
			}
			var_class_15_ = class$3 = var_class_16_;
		    }
		    var_classes[i_14_] = var_class_15_;
		    Method method
			= var_class.getMethod(string_7_, var_classes);
		    Class var_class_17_ = class$0;
		    if (var_class_17_ == null) {
			Class var_class_18_;
			try {
			    var_class_18_ = Class.forName("java.awt.Toolkit");
			} catch (ClassNotFoundException classnotfoundexception) {
			    NoClassDefFoundError noclassdeffounderror
				= new NoClassDefFoundError((classnotfoundexception.getMessage()));
			    throw noclassdeffounderror;
			}
			var_class_17_ = class$0 = var_class_18_;
		    }
		    Method method_19_
			= var_class_17_.getMethod("getBestCursorSize",
						  (new Class[]
						   { Integer.TYPE,
						     Integer.TYPE }));
		    Dimension dimension
			= ((Dimension)
			   method_19_.invoke(toolkit,
					     (new Object[]
					      { new Integer(i_4_),
						new Integer(i_5_) })));
		    if (dimension.width != 0 && dimension.height != 0)
			return ((Cursor)
				(method.invoke
				 (toolkit,
				  (new Object[]
				   { image,
				     new Point((int) ((float) i_4_
						      / (float) dimension.width
						      * (float) i_2_),
					       (int) ((float) i_5_
						      / (float) (dimension
								 .height)
						      * (float) i_3_)),
				     "custum" }))));
		} catch (NoSuchMethodException nosuchmethodexception) {
		    if (image == null)
			image = (this.createImage
				 (new MemoryImageSource(i_4_, i_5_,
							new int[i_4_ * i_5_],
							0, i_4_)));
		    return (Cursor) (Class.forName("com.ms.awt.CursorX")
					 .getConstructors
					 ()[0].newInstance
				     (new Object[] { image, new Integer(i_2_),
						     new Integer(i_3_) }));
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
	return Cursor.getPredefinedCursor(i);
    }
    
    private void loadSound() {
	if (sounds != null)
	    sounds = null;
	else {
	    sounds = new AudioClip[4];
	    String[] strings = { "tp.au", "talk.au", "in.au", "out.au" };
	    for (int i = 0; i < 4; i++) {
		try {
		    String string = dd.config.res(strings[i]);
		    if (string != null && string.length() > 0
			&& string.charAt(0) != '_')
			sounds[i]
			    = applet.getAudioClip(new URL(applet.getCodeBase(),
							  string));
		} catch (java.io.IOException ioexception) {
		    sounds[i] = null;
		}
	    }
	}
    }
    
    private synchronized void mkTextPanel() {
	if (tField == null) {
	    String string = "Center";
	    String string_20_ = "East";
	    String string_21_ = "West";
	    Panel panel = new Panel(new BorderLayout());
	    tField = new TextField();
	    tField.addActionListener(this);
	    panel.add(tField, string);
	    tLabel = new Label(dd.res.res("input"));
	    panel.add(tLabel, string_21_);
	    String[] strings = { "F", "FAll", "leave" };
	    Panel panel_22_ = new Panel(new FlowLayout(0, 2, 1));
	    tPanelB = panel_22_;
	    for (int i = 0; i < 3; i++) {
		LButton lbutton = new LButton(res.res(strings[i]));
		lbutton.addActionListener(this);
		lbutton.setName(String.valueOf(i));
		panel_22_.add(lbutton);
	    }
	    panel.add(panel_22_, string_20_);
	    Color color = this.getBackground();
	    Color color_23_ = this.getForeground();
	    tText = new TextPanel(applet, 100, color, color_23_, tField);
	    tList = new TextPanel(applet, 20, color, color_23_, tField);
	    tPanel = new Panel(new BorderLayout());
	    tPanel.add(tText, string);
	    tPanel.add(tList, string_20_);
	    tPanel.add(panel, "South");
	    Awt.getDef(tPanel);
	    Awt.setDef(tPanel, false);
	}
    }
    
    private void pack() {
	dSize = super.getSize();
	if (tool != null && mi != null && dPack != null) {
	    getSize();
	    ThreadPool.poolStartThread(this, 'p');
	}
    }
    
    public void paint(Graphics graphics) {
	if (!isStart)
	    iPG(false);
	Dimension dimension = getSize();
	graphics.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
    }
    
    protected void processEvent(AWTEvent awtevent) {
	int i = awtevent.getID();
	if (i == 101) {
	    dSize = super.getSize();
	    Dimension dimension = getSize();
	    this.setSize(dimension.getSize());
	    if (dPack != null && !dPack.equals(dimension))
		pack();
	} else if (mi != null && awtevent instanceof MouseEvent) {
	    Point point = mi.getLocation();
	    ((MouseEvent) awtevent).translatePoint(-point.x, -point.y);
	    mi.dispatchEvent(awtevent);
	}
	super.processEvent(awtevent);
    }
    
    public void repaint(long l, int i, int i_24_, int i_25_, int i_26_) {
	repaint(this, i, i_24_, i_25_, i_26_);
    }
    
    private void repaint(Component component, int i, int i_27_, int i_28_,
			 int i_29_) {
	if (component instanceof Container) {
	    Component[] components = ((Container) component).getComponents();
	    for (int i_30_ = 0; i_30_ < components.length; i_30_++) {
		Point point = components[i_30_].getLocation();
		repaint(components[i_30_], i - point.x, i_27_ - point.y, i_28_,
			i_29_);
	    }
	} else {
	    Point point = component.getLocation();
	    int i_31_ = i - point.x;
	    int i_32_ = i_27_ - point.y;
	    if (i_31_ + i_28_ > 0 && i_32_ + i_29_ > 0)
		component.repaint(i_31_, i_32_, i_28_, i_29_);
	}
    }
    
    private void rInit() {
	String string = "cursor_";
	String string_33_ = "window_color_";
	try {
	    getSize();
	    dd = new Data(this);
	    mgText = new MgText();
	    mi = new Mi(this, this.res);
	    iPG(true);
	    dd.mi = mi;
	    dd.init();
	    this.res = dd.res;
	    Res res = dd.config;
	    int i = res.getP("layer_count", 2);
	    int i_34_ = res.getP("quality", 1);
	    try {
		Color color = new Color(res.getP("color_bk", 13619199));
		applet.setBackground(color);
		this.setBackground(color);
		color
		    = new Color(res.getP(string_33_ + "_bk", color.getRGB()));
		Awt.cBk = Awt.cC = color;
		color = new Color(res.getP("color_text", 5263480));
		applet.setForeground(color);
		this.setForeground(color);
		Awt.cFore = new Color(res.getP(string_33_ + "_text",
					       color.getRGB()));
		Awt.cFSel = new Color(res.getP("color_iconselect", 15610675));
		Awt.cF = new Color(res.getP(string_33_ + "_frame", 0));
		Awt.clBar = new Color(res.getP(string_33_ + "_bar", 6711039));
		Awt.clLBar
		    = new Color(res.getP(string_33_ + "_bar_hl", 8947967));
		Awt.clBarT
		    = new Color(res.getP(string_33_ + "_bar_text", 16777215));
		Awt.getDef(this);
		Awt.setPFrame((Frame) Awt.getParent(this));
	    } catch (Throwable throwable) {
		/* empty */
	    }
	    iPG(true);
	    Cursor[] cursors = new Cursor[4];
	    int i_35_ = 0;
	    int[] is = { i_35_, 13, i_35_, i_35_ };
	    for (i_35_ = 0; i_35_ < 4; i_35_++)
		cursors[i_35_]
		    = loadCursor(applet.getParameter(string + (i_35_ + 1)),
				 is[i_35_]);
	    iPG(true);
	    miPanel = new Panel(null);
	    mi.init(applet, dd.config, dd.imW, dd.imH, i_34_, i, cursors);
	    miPanel.add(mi);
	    iPG(true);
	    String string_36_ = res.getP("tools", "normal");
	    try {
		tool = (ToolBox) Class.forName
				     ("paintchat." + string_36_ + ".Tools")
				     .newInstance();
		tool.init(miPanel, applet, dd.config, this.res, mi);
	    } catch (Throwable throwable) {
		throwable.printStackTrace();
	    }
	    mkTextPanel();
	    tField.addKeyListener(this);
	    this.enableEvents(9L);
	    isStart = true;
	    this.add(tPanel);
	    this.add(miPanel);
	    tField.requestFocus();
	    iPG(true);
	    pack();
	    if (dd.config.getP("Client_Sound", false))
		loadSound();
	    DCF dcf = new DCF(this.res);
	    dcf.mShow();
	    String string_37_ = dcf.mGetHandle();
	    if (string_37_.length() <= 0)
		mExit();
	    else {
		dd.strName = string_37_;
		dd.config.put("chat_password", dcf.mGetPass());
		dd.start();
		addInOut(string_37_, true);
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private synchronized void rPack() {
	Dimension dimension = getSize();
	dPack.setSize(dimension);
	this.setVisible(false);
	int i = iGap;
	int i_38_
	    = (int) ((float) dimension.height * ((float) iCenter / 100.0F));
	if (miPanel != null)
	    miPanel.setBounds(0, 0, dimension.width, i_38_);
	if (tool != null) {
	    tool.pack();
	    if (tPanel != null && tPanel.getParent() == this) {
		int i_39_ = 0;
		tPanel.setBounds(i_39_, i_38_ + i, dimension.width - i_39_,
				 dimension.height - (i_38_ + i));
		this.validate();
	    }
	}
	mi.resetGraphics();
	this.setVisible(true);
    }
    
    public void run() {
	try {
	    switch (Thread.currentThread().getName().charAt(0)) {
	    case 'i':
		rInit();
		break;
	    case 'p':
		rPack();
		break;
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void scroll(boolean bool, int i, int i_40_) {
	LComponent[] lcomponents = tool.getCs();
	int i_41_ = lcomponents.length;
	Point point = mi.getLocation();
	int i_42_ = point.x + mi.getGapX();
	int i_43_ = point.y + mi.getGapY();
	Dimension dimension = mi.getSizeW();
	for (int i_44_ = 0; i_44_ < i_41_; i_44_++) {
	    LComponent lcomponent = lcomponents[i_44_];
	    Point point_45_ = lcomponent.getLocation();
	    Dimension dimension_46_ = lcomponent.getSizeW();
	    if (((point_45_.x + dimension_46_.width > point.x
		  && point_45_.y + dimension_46_.height > point.y
		  && point_45_.x < point.x + dimension.width
		  && point_45_.y < point.y + dimension.height)
		 || lcomponent.isEscape)
		&& lcomponent.isVisible()) {
		if (iScrollType == 0) {
		    int i_47_ = point_45_.x - i_42_;
		    int i_48_ = point_45_.y - i_43_;
		    int i_49_ = dimension_46_.width;
		    if (dimension_46_.height != 0) {
			/* empty */
		    }
		    if (i > 0)
			mi.m_paint(i_47_ - i, i_48_, i, dimension_46_.height);
		    if (i < 0)
			mi.m_paint(i_47_ + i_49_, i_48_, -i,
				   dimension_46_.height);
		    i_49_ += Math.abs(i);
		    if (i_40_ < 0)
			mi.m_paint(i_47_ - i, i_48_ + dimension_46_.height,
				   i_49_, -i_40_);
		    if (i_40_ > 0)
			mi.m_paint(i_47_ - i, i_48_ - i_40_, i_49_, i_40_);
		} else {
		    boolean bool_50_ = lcomponent.isEscape;
		    lcomponent.escape(bool);
		    if (!bool_50_)
			mi.m_paint(point_45_.x - point.x,
				   point_45_.y - point.y, dimension_46_.width,
				   dimension_46_.height);
		}
	    }
	}
    }
    
    public void send(M m) {
	dd.send(m);
    }
    
    public void setARGB(int i) {
	i &= 0xffffff;
	tool.selPix(mi.info.m.iLayer != 0 && i == 16777215);
	if (mi.info.m.iPen != 4 && mi.info.m.iPen != 5)
	    tool.setARGB(mi.info.m.iAlpha << 24 | i);
    }
    
    private void setDefComponent(Container container) {
	try {
	    if (container != null) {
		Color color = container.getForeground();
		Color color_51_ = container.getBackground();
		Component[] components = container.getComponents();
		if (components != null) {
		    for (int i = 0; i < components.length; i++) {
			Component component = components[i];
			component.setBackground(color_51_);
			component.setForeground(color);
			if (component instanceof Container)
			    setDefComponent((Container) component);
		    }
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void setLineSize(int i) {
	tool.setLineSize(i);
    }
    
    private void typed() {
	try {
	    String string = tField.getText();
	    if (string != null && string.length() > 0) {
		tField.setText("");
		if (string.length() > 256)
		    mi.alert("longer it", false);
		else if (mi.info.m.isText())
		    mi.addText(string);
		else {
		    mgText.setData(0, (byte) 0, string);
		    dd.send(mgText);
		    string = dd.strName + '>' + string;
		    tText.addText(string, true);
		    dSound(1);
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void undo(boolean bool) {
	/* empty */
    }
    
    public void update(Graphics graphics) {
	paint(graphics);
    }
}
