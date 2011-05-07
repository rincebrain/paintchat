package syi.awt;

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
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

  public LComponent()
  {
    if (Q == 0.0F)
      setup();
    this.iGap = Math.max((int)(4.0F * Q), 1);
    this.iBSize = Math.max((int)(12.0F * Q), 7);
    this.isRepaint = (!isWin);
    Awt.getDef(this);
    this.clFrame = Awt.cF;
    enableEvents(57L);
  }

  public void escape(boolean paramBoolean)
  {
    if (this.isEscape == paramBoolean)
      return;
    this.isEscape = paramBoolean;
    if (paramBoolean)
    {
      this.rEscape = new Rectangle(getBounds());
      setBounds(0, 0, 1, 1);
    }
    else if (this.rEscape != null)
    {
      setBounds(this.rEscape);
      this.rEscape = null;
    }
  }

  private Cursor getCur(int paramInt1, int paramInt2)
  {
    int i;
    switch (inCorner(paramInt1, paramInt2))
    {
    case 1:
      i = 6;
      break;
    case 2:
      i = 7;
      break;
    case 3:
      i = 4;
      break;
    case 4:
      i = 5;
      break;
    default:
      i = 0;
    }
    return Cursor.getPredefinedCursor(i);
  }

  public Graphics getG()
  {
    Graphics localGraphics = getGraphics();
    localGraphics.translate(getGapX(), getGapY());
    Dimension localDimension = getSize();
    localGraphics.clipRect(0, 0, localDimension.width, localDimension.height);
    return localGraphics;
  }

  public int getGapH()
  {
    return getGapY() + this.iGap;
  }

  public int getGapW()
  {
    return this.iGap * 2;
  }

  public int getGapX()
  {
    return this.iGap;
  }

  public int getGapY()
  {
    return this.iGap + (this.isGUI ? this.iBSize : 0);
  }

  public Point getLocation()
  {
    if (this.pLocation == null)
      this.pLocation = super.getLocation();
    return this.pLocation;
  }

  public Dimension getMaximumSize()
  {
    return this.dL == null ? getSize() : this.dL;
  }

  public Dimension getMinimumSize()
  {
    return this.dS == null ? getSize() : this.dS;
  }

  public Dimension getPreferredSize()
  {
    return this.dM == null ? getSize() : this.dM;
  }

  public Dimension getSize()
  {
    if (this.dVisit == null)
      this.dVisit = new Dimension();
    this.dVisit.setSize(getSizeW());
    this.dVisit.width -= getGapW();
    this.dVisit.height -= getGapH();
    return this.dVisit;
  }

  public Dimension getSizeW()
  {
    if (this.dSize == null)
      this.dSize = super.getSize();
    return this.dSize;
  }

  private int inCorner(int paramInt1, int paramInt2)
  {
    Dimension localDimension = getSizeW();
    int i = 0;
    int j = this.iGap;
    if (paramInt2 <= j)
    {
      if (paramInt1 <= j)
        i = 1;
      if (paramInt1 >= localDimension.width - j)
        i = 2;
    }
    else if (paramInt2 >= localDimension.height - j)
    {
      if (paramInt1 <= j)
        i = 3;
      if (paramInt1 >= localDimension.width - j)
        i = 4;
    }
    return i;
  }

  public void inParent()
  {
    Container localContainer = getParent();
    if (localContainer == null)
      return;
    Point localPoint = getLocation();
    Dimension localDimension1 = localContainer.getSize();
    Dimension localDimension2 = getSizeW();
    int i = localPoint.x;
    int j = localPoint.y;
    int k = localDimension2.width;
    int m = localDimension2.height;
    i = i + k >= localDimension1.width ? localDimension1.width - k : i <= 0 ? 0 : i;
    i = i <= 0 ? 0 : i;
    j = j + m >= localDimension1.height ? localDimension1.height - m : j <= 0 ? 0 : j;
    j = j <= 0 ? 0 : j;
    if ((i != localPoint.x) || (j != localPoint.y))
      setLocation(i, j);
    k = Math.min(k, localDimension1.width);
    m = Math.min(m, localDimension1.height);
    if ((k != localDimension2.width) || (m != localDimension2.height))
      setSize(k - getGapW(), m - getGapH());
  }

  public void paint(Graphics paramGraphics)
  {
    if (!isVisible())
      return;
    Dimension localDimension = getSizeW();
    if ((!this.isPaint) && (this.isMove))
    {
      paramGraphics.drawRect(0, 0, localDimension.width - 1, localDimension.height - 1);
      return;
    }
    int i = this.iBSize;
    int j = this.iGap;
    (j * 2);
    int k = localDimension.width;
    int m = localDimension.height;
    if (this.isFrame)
    {
      paramGraphics.setColor(this.clFrame);
      paramGraphics.drawRect(0, 0, k - 1, m - 1);
    }
    if (this.isGUI)
    {
      paramGraphics.fillRect(1, i, k - 2, 1);
      paramGraphics.fillRect(k - i - 1, 1, 1, i - 1);
      paramGraphics.setColor(this.clLBar);
      paramGraphics.fillRect(1, 1, k - 2, 1);
      paramGraphics.setColor(this.clBar);
      paramGraphics.fillRect(1, 2, k - 2 - this.iBSize, this.iBSize - 2);
      paramGraphics.drawLine(k - i + 1, 2, k - 2, i - 1);
      paramGraphics.drawLine(k - i + 1, i - 1, k - 2, 1);
      if ((this.title != null) && (this.title.length() > 0))
      {
        paramGraphics.setClip(1, 1, k - 1 - this.iBSize, this.iBSize - 1);
        paramGraphics.setFont(fontBar);
        paramGraphics.setColor(this.clBarT);
        paramGraphics.drawString(this.title, j, i - 1);
        paramGraphics.setClip(0, 0, localDimension.width, localDimension.height);
      }
    }
    int n = getGapX();
    int i1 = getGapY();
    paramGraphics.translate(n, i1);
    try
    {
      paint2(paramGraphics);
    }
    catch (Throwable localThrowable)
    {
    }
    paramGraphics.translate(-n, -i1);
  }

  public abstract void paint2(Graphics paramGraphics);

  public abstract void pMouse(MouseEvent paramMouseEvent);

  protected void processEvent(AWTEvent paramAWTEvent)
  {
    try
    {
      int i = paramAWTEvent.getID();
      Dimension localDimension1 = getSizeW();
      Point localPoint1 = getLocation();
      int k;
      int m;
      switch (i)
      {
      case 101:
        localDimension1.setSize(super.getSize());
        int j = localDimension1.width;
        k = localDimension1.height;
        inParent();
        if (!this.isRepaint)
          break;
        getParent().repaint(0L, localPoint1.x, localPoint1.y, j, k);
        break;
      case 100:
        localPoint1.setLocation(super.getLocation());
        m = localPoint1.x;
        int n = localPoint1.y;
        inParent();
        if (!this.isRepaint)
          break;
        getParent().repaint(0L, m, n, localDimension1.width, localDimension1.height);
      }
      if ((paramAWTEvent instanceof MouseEvent))
      {
        MouseEvent localMouseEvent = (MouseEvent)paramAWTEvent;
        localMouseEvent.consume();
        k = localMouseEvent.getX();
        m = localMouseEvent.getY();
        if (this.isGUI)
        {
          localDimension1 = getSizeW();
          Dimension localDimension2 = getSize();
          (this.iGap * 2);
          int i1 = 0;
          Object localObject;
          switch (localMouseEvent.getID())
          {
          case 503:
            if (!getCursor().equals(getCur(k, m)))
              setCursor(getCur(k, m));
            if (!this.isUpDown)
              break;
            Container localContainer = getParent();
            if (localContainer.getComponent(0) == this)
              break;
            localContainer.remove(this);
            localContainer.add(this, 0);
            break;
          case 501:
            this.oldX = k;
            this.oldY = m;
            int i2 = inCorner(k, m);
            if (i2 != 0)
            {
              this.isMove = true;
              this.isResize = true;
              this.isPaint = false;
              this.countResize = 0;
              return;
            }
            if (m > this.iBSize)
              break;
            if (k >= localDimension1.width - this.iBSize)
            {
              if (this.isHide)
                setVisible(false);
              return;
            }
            if (localMouseEvent.getClickCount() % 2 == 0)
            {
              localObject = getMaximumSize();
              if (localDimension2.equals(localObject))
                setSize(getPreferredSize());
              else
                setSize((Dimension)localObject);
              return;
            }
            this.isMove = true;
            this.isResize = false;
            this.isPaint = false;
            return;
            break;
          case 506:
            if ((!this.isMove) || (++this.countResize < 4))
              break;
            i1 = 1;
            this.countResize = 0;
            break;
          case 502:
            if (!this.isMove)
              break;
            this.isMove = false;
            i1 = 1;
            this.isPaint = true;
          case 504:
          case 505:
          }
          if (i1 != 0)
          {
            Point localPoint2 = getLocation();
            Dimension localDimension3;
            if (this.isResize)
            {
              localObject = this.dL;
              localDimension3 = this.dS;
              int i3 = localDimension2.width + (k - this.oldX);
              int i4 = localDimension2.height + (m - this.oldY);
              setSize(i3 > ((Dimension)localObject).width ? ((Dimension)localObject).width : i3 < localDimension3.width ? localDimension3.width : i3, i4 > ((Dimension)localObject).height ? ((Dimension)localObject).height : i4 < localDimension3.height ? localDimension3.height : i4);
              this.oldX = k;
              this.oldY = m;
            }
            else
            {
              localObject = getLocation();
              localDimension3 = getParent().getSize();
              k = ((Point)localObject).x + k - this.oldX;
              k = k + localDimension1.width >= localDimension3.width ? localDimension3.width - localDimension1.width : k <= 0 ? 0 : k;
              m = ((Point)localObject).y + m - this.oldY;
              m = m + localDimension1.height >= localDimension3.height ? localDimension3.height - localDimension1.height : m <= 0 ? 0 : m;
              setLocation(k, m);
            }
            if (this.isPaint)
              repaint();
          }
        }
        k = getGapX();
        m = getGapY();
        if (!this.isMove)
        {
          localMouseEvent.translatePoint(-k, -m);
          pMouse(localMouseEvent);
          localMouseEvent.translatePoint(k, m);
        }
      }
      super.processEvent(paramAWTEvent);
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void setDimension(Dimension paramDimension1, Dimension paramDimension2, Dimension paramDimension3)
  {
    paramDimension2 = paramDimension2 == null ? getSize() : paramDimension2;
    this.dS = (paramDimension1 == null ? this.dS : this.dS == null ? new Dimension() : paramDimension1);
    this.dL = (paramDimension3 == null ? this.dL : this.dL == null ? new Dimension(9999, 9999) : paramDimension3);
    this.dM = new Dimension(paramDimension2);
    setSize(paramDimension2.width, paramDimension2.height);
  }

  public void setLocation(int paramInt1, int paramInt2)
  {
    getLocation().setLocation(paramInt1, paramInt2);
    super.setLocation(paramInt1, paramInt2);
  }

  public void setSize(int paramInt1, int paramInt2)
  {
    Dimension localDimension1 = getMaximumSize();
    Dimension localDimension2 = getMinimumSize();
    paramInt1 = paramInt1 < localDimension2.width ? localDimension2.width : paramInt1 > localDimension1.width ? localDimension1.width : paramInt1;
    paramInt2 = paramInt2 < localDimension2.height ? localDimension2.height : paramInt2 > localDimension1.height ? localDimension1.height : paramInt2;
    Dimension localDimension3 = getSize();
    if ((localDimension3.width == paramInt1) && (localDimension3.height == paramInt2))
      return;
    paramInt1 += getGapW();
    paramInt2 += getGapH();
    getSizeW().setSize(paramInt1, paramInt2);
    super.setSize(paramInt1, paramInt2);
  }

  public void setSize(Dimension paramDimension)
  {
    setSize(paramDimension.width, paramDimension.height);
  }

  public void setTitle(String paramString)
  {
    this.title = paramString;
  }

  private void setup()
  {
    isWin = Awt.isWin();
    this.clFrame = Color.black;
    this.clBar = new Color(5592575);
    this.clLBar = this.clBar.brighter();
    this.clBarT = Color.white;
    Q = Awt.q();
    fontBar = new Font("sansserif", 0, (int)(10.0F * Q));
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.LComponent
 * JD-Core Version:    0.6.0
 */