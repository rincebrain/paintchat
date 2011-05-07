/* LComponent - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public abstract class LComponent extends Canvas
{
    protected static boolean isWin;
    private String title = null;
    public boolean isUpDown = false;
    public boolean isGUI = true;
    public boolean isHide = true;
    public boolean isEscape = false;
    protected boolean isFrame = true;
    protected boolean isPaint = true;
    protected boolean isRepaint = true;
    private Rectangle rEscape = null;
    private Dimension dSize = null;
    private Dimension dVisit = null;
    private Dimension dS = null;
    private Dimension dM = null;
    private Dimension dL = null;
    private Point pLocation = null;
    private boolean isMove = false;
    private boolean isResize = false;
    private int oldX = 0;
    private int oldY = 0;
    private int countResize = 0;
    public Color clFrame;
    public Color clLBar;
    public Color clBar;
    public Color clBarT;
    protected int iBSize;
    protected int iGap;
    private static Font fontBar = null;
    public static float Q;
    
    public LComponent() {
	if (Q == 0.0F)
	    setup();
	iGap = Math.max((int) (4.0F * Q), 1);
	iBSize = Math.max((int) (12.0F * Q), 7);
	isRepaint = !isWin;
	Awt.getDef(this);
	clFrame = Awt.cF;
	this.enableEvents(57L);
    }
    
    public void escape(boolean bool) {
	if (isEscape != bool) {
	    isEscape = bool;
	    if (bool) {
		rEscape = new Rectangle(this.getBounds());
		this.setBounds(0, 0, 1, 1);
	    } else if (rEscape != null) {
		this.setBounds(rEscape);
		rEscape = null;
	    }
	}
    }
    
    private Cursor getCur(int i, int i_0_) {
	int i_1_;
	switch (inCorner(i, i_0_)) {
	case 1:
	    i_1_ = 6;
	    break;
	case 2:
	    i_1_ = 7;
	    break;
	case 3:
	    i_1_ = 4;
	    break;
	case 4:
	    i_1_ = 5;
	    break;
	default:
	    i_1_ = 0;
	}
	return Cursor.getPredefinedCursor(i_1_);
    }
    
    public Graphics getG() {
	Graphics graphics = this.getGraphics();
	graphics.translate(getGapX(), getGapY());
	Dimension dimension = getSize();
	graphics.clipRect(0, 0, dimension.width, dimension.height);
	return graphics;
    }
    
    public int getGapH() {
	return getGapY() + iGap;
    }
    
    public int getGapW() {
	return iGap * 2;
    }
    
    public int getGapX() {
	return iGap;
    }
    
    public int getGapY() {
	return iGap + (isGUI ? iBSize : 0);
    }
    
    public Point getLocation() {
	if (pLocation == null)
	    pLocation = super.getLocation();
	return pLocation;
    }
    
    public Dimension getMaximumSize() {
	return dL == null ? getSize() : dL;
    }
    
    public Dimension getMinimumSize() {
	return dS == null ? getSize() : dS;
    }
    
    public Dimension getPreferredSize() {
	return dM == null ? getSize() : dM;
    }
    
    public Dimension getSize() {
	if (dVisit == null)
	    dVisit = new Dimension();
	dVisit.setSize(getSizeW());
	dVisit.width -= getGapW();
	dVisit.height -= getGapH();
	return dVisit;
    }
    
    public Dimension getSizeW() {
	if (dSize == null)
	    dSize = super.getSize();
	return dSize;
    }
    
    private int inCorner(int i, int i_2_) {
	Dimension dimension = getSizeW();
	int i_3_ = 0;
	int i_4_ = iGap;
	if (i_2_ <= i_4_) {
	    if (i <= i_4_)
		i_3_ = 1;
	    if (i >= dimension.width - i_4_)
		i_3_ = 2;
	} else if (i_2_ >= dimension.height - i_4_) {
	    if (i <= i_4_)
		i_3_ = 3;
	    if (i >= dimension.width - i_4_)
		i_3_ = 4;
	}
	return i_3_;
    }
    
    public void inParent() {
	Container container = this.getParent();
	if (container != null) {
	    Point point = getLocation();
	    Dimension dimension = container.getSize();
	    Dimension dimension_5_ = getSizeW();
	    int i = point.x;
	    int i_6_ = point.y;
	    int i_7_ = dimension_5_.width;
	    int i_8_ = dimension_5_.height;
	    i = (i <= 0 ? 0 : i + i_7_ >= dimension.width
		 ? dimension.width - i_7_ : i);
	    i = i <= 0 ? 0 : i;
	    i_6_ = (i_6_ <= 0 ? 0 : i_6_ + i_8_ >= dimension.height
		    ? dimension.height - i_8_ : i_6_);
	    i_6_ = i_6_ <= 0 ? 0 : i_6_;
	    if (i != point.x || i_6_ != point.y)
		setLocation(i, i_6_);
	    i_7_ = Math.min(i_7_, dimension.width);
	    i_8_ = Math.min(i_8_, dimension.height);
	    if (i_7_ != dimension_5_.width || i_8_ != dimension_5_.height)
		setSize(i_7_ - getGapW(), i_8_ - getGapH());
	}
    }
    
    public void paint(Graphics graphics) {
	if (this.isVisible()) {
	    Dimension dimension = getSizeW();
	    if (!isPaint && isMove)
		graphics.drawRect(0, 0, dimension.width - 1,
				  dimension.height - 1);
	    else {
		int i = iBSize;
		int i_9_ = iGap;
		if (i_9_ * 2 != 0) {
		    /* empty */
		}
		int i_10_ = dimension.width;
		int i_11_ = dimension.height;
		if (isFrame) {
		    graphics.setColor(clFrame);
		    graphics.drawRect(0, 0, i_10_ - 1, i_11_ - 1);
		}
		if (isGUI) {
		    graphics.fillRect(1, i, i_10_ - 2, 1);
		    graphics.fillRect(i_10_ - i - 1, 1, 1, i - 1);
		    graphics.setColor(clLBar);
		    graphics.fillRect(1, 1, i_10_ - 2, 1);
		    graphics.setColor(clBar);
		    graphics.fillRect(1, 2, i_10_ - 2 - iBSize, iBSize - 2);
		    graphics.drawLine(i_10_ - i + 1, 2, i_10_ - 2, i - 1);
		    graphics.drawLine(i_10_ - i + 1, i - 1, i_10_ - 2, 1);
		    if (title != null && title.length() > 0) {
			graphics.setClip(1, 1, i_10_ - 1 - iBSize, iBSize - 1);
			graphics.setFont(fontBar);
			graphics.setColor(clBarT);
			graphics.drawString(title, i_9_, i - 1);
			graphics.setClip(0, 0, dimension.width,
					 dimension.height);
		    }
		}
		int i_12_ = getGapX();
		int i_13_ = getGapY();
		graphics.translate(i_12_, i_13_);
		try {
		    paint2(graphics);
		} catch (Throwable throwable) {
		    /* empty */
		}
		graphics.translate(-i_12_, -i_13_);
	    }
	}
    }
    
    public abstract void paint2(Graphics graphics);
    
    public abstract void pMouse(MouseEvent mouseevent);
    
    protected void processEvent(AWTEvent awtevent) {
	try {
	    int i = awtevent.getID();
	    Dimension dimension = getSizeW();
	    Point point = getLocation();
	    switch (i) {
	    case 101: {
		dimension.setSize(super.getSize());
		int i_14_ = dimension.width;
		int i_15_ = dimension.height;
		inParent();
		if (isRepaint)
		    this.getParent().repaint(0L, point.x, point.y, i_14_,
					     i_15_);
		break;
	    }
	    case 100: {
		point.setLocation(super.getLocation());
		int i_16_ = point.x;
		int i_17_ = point.y;
		inParent();
		if (isRepaint)
		    this.getParent().repaint(0L, i_16_, i_17_, dimension.width,
					     dimension.height);
		break;
	    }
	    }
	    if (awtevent instanceof MouseEvent) {
		MouseEvent mouseevent = (MouseEvent) awtevent;
		mouseevent.consume();
		int i_18_ = mouseevent.getX();
		int i_19_ = mouseevent.getY();
		if (isGUI) {
		    dimension = getSizeW();
		    Dimension dimension_20_ = getSize();
		    if (iGap * 2 != 0) {
			/* empty */
		    }
		    boolean bool = false;
		    switch (mouseevent.getID()) {
		    case 503:
			if (!this.getCursor().equals(getCur(i_18_, i_19_)))
			    this.setCursor(getCur(i_18_, i_19_));
			if (isUpDown) {
			    Container container = this.getParent();
			    if (container.getComponent(0) != this) {
				container.remove(this);
				container.add(this, 0);
			    }
			}
			break;
		    case 501: {
			oldX = i_18_;
			oldY = i_19_;
			int i_21_ = inCorner(i_18_, i_19_);
			if (i_21_ != 0) {
			    isMove = true;
			    isResize = true;
			    isPaint = false;
			    countResize = 0;
			} else {
			    if (i_19_ <= iBSize) {
				if (i_18_ >= dimension.width - iBSize) {
				    if (isHide)
					this.setVisible(false);
				} else if (mouseevent.getClickCount() % 2
					   == 0) {
				    Dimension dimension_22_ = getMaximumSize();
				    if (dimension_20_.equals(dimension_22_))
					setSize(getPreferredSize());
				    else
					setSize(dimension_22_);
				} else {
				    isMove = true;
				    isResize = false;
				    isPaint = false;
				}
			    } else
				break;
			    return;
			}
			return;
		    }
		    case 506:
			if (isMove && ++countResize >= 4) {
			    bool = true;
			    countResize = 0;
			}
			break;
		    case 502:
			if (isMove) {
			    isMove = false;
			    bool = true;
			    isPaint = true;
			}
			break;
		    }
		    if (bool) {
			Point point_23_ = getLocation();
			if (point_23_.x != 0) {
			    /* empty */
			}
			if (point_23_.y != 0) {
			    /* empty */
			}
			if (dimension_20_.width != 0) {
			    /* empty */
			}
			if (dimension_20_.height != 0) {
			    /* empty */
			}
			if (isResize) {
			    Dimension dimension_24_ = dL;
			    Dimension dimension_25_ = dS;
			    int i_26_ = dimension_20_.width + (i_18_ - oldX);
			    int i_27_ = dimension_20_.height + (i_19_ - oldY);
			    setSize((i_26_ < dimension_25_.width
				     ? dimension_25_.width
				     : i_26_ > dimension_24_.width
				     ? dimension_24_.width : i_26_),
				    (i_27_ < dimension_25_.height
				     ? dimension_25_.height
				     : i_27_ > dimension_24_.height
				     ? dimension_24_.height : i_27_));
			    oldX = i_18_;
			    oldY = i_19_;
			} else {
			    Point point_28_ = getLocation();
			    Dimension dimension_29_
				= this.getParent().getSize();
			    i_18_ = point_28_.x + i_18_ - oldX;
			    i_18_ = (i_18_ <= 0 ? 0
				     : (i_18_ + dimension.width
					>= dimension_29_.width)
				     ? dimension_29_.width - dimension.width
				     : i_18_);
			    i_19_ = point_28_.y + i_19_ - oldY;
			    i_19_ = (i_19_ <= 0 ? 0
				     : (i_19_ + dimension.height
					>= dimension_29_.height)
				     ? dimension_29_.height - dimension.height
				     : i_19_);
			    setLocation(i_18_, i_19_);
			}
			if (isPaint)
			    this.repaint();
		    }
		}
		i_18_ = getGapX();
		i_19_ = getGapY();
		if (!isMove) {
		    mouseevent.translatePoint(-i_18_, -i_19_);
		    pMouse(mouseevent);
		    mouseevent.translatePoint(i_18_, i_19_);
		}
	    }
	    super.processEvent(awtevent);
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    public void setDimension(Dimension dimension, Dimension dimension_30_,
			     Dimension dimension_31_) {
	dimension_30_ = dimension_30_ == null ? getSize() : dimension_30_;
	dS = dimension == null ? dS == null ? new Dimension() : dS : dimension;
	dL = (dimension_31_ == null
	      ? dL == null ? new Dimension(9999, 9999) : dL : dimension_31_);
	dM = new Dimension(dimension_30_);
	setSize(dimension_30_.width, dimension_30_.height);
    }
    
    public void setLocation(int i, int i_32_) {
	getLocation().setLocation(i, i_32_);
	super.setLocation(i, i_32_);
    }
    
    public void setSize(int i, int i_33_) {
	Dimension dimension = getMaximumSize();
	Dimension dimension_34_ = getMinimumSize();
	i = (i > dimension.width ? dimension.width : i < dimension_34_.width
	     ? dimension_34_.width : i);
	i_33_
	    = (i_33_ > dimension.height ? dimension.height
	       : i_33_ < dimension_34_.height ? dimension_34_.height : i_33_);
	Dimension dimension_35_ = getSize();
	if (dimension_35_.width != i || dimension_35_.height != i_33_) {
	    i += getGapW();
	    i_33_ += getGapH();
	    getSizeW().setSize(i, i_33_);
	    super.setSize(i, i_33_);
	}
    }
    
    public void setSize(Dimension dimension) {
	setSize(dimension.width, dimension.height);
    }
    
    public void setTitle(String string) {
	title = string;
    }
    
    private void setup() {
	isWin = Awt.isWin();
	clFrame = Color.black;
	clBar = new Color(5592575);
	clLBar = clBar.brighter();
	clBarT = Color.white;
	Q = Awt.q();
	fontBar = new Font("sansserif", 0, (int) (10.0F * Q));
    }
    
    public void update(Graphics graphics) {
	paint(graphics);
    }
}
