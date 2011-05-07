package paintchat_client;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import paintchat.LO;
import paintchat.M;
import paintchat.M.Info;
import paintchat.M.User;
import paintchat.Res;
import syi.awt.Awt;
import syi.awt.LComponent;

public class Mi extends LComponent
  implements ActionListener
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

  public Mi(IMi paramIMi, Res paramRes)
    throws Exception
  {
    this.imi = paramIMi;
    this.res = paramRes;
    this.isRepaint = false;
    this.isHide = false;
    this.isGUI = false;
    this.iGap = 2;
    Me.res = paramRes;
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    if (this.text != null)
    {
      addText(paramActionEvent.getActionCommand());
      if (this.isText)
        this.text.setVisible(false);
    }
  }

  public void addText(String paramString)
  {
    try
    {
      if (!this.mgInfo.isText())
        return;
      setM();
      byte[] arrayOfByte = ('\000' + paramString).getBytes("UTF8");
      this.m.setRetouch(this.ps, arrayOfByte, arrayOfByte.length, true);
      this.m.draw();
      send(this.m);
    }
    catch (Exception localException)
    {
    }
  }

  public boolean alert(String paramString, boolean paramBoolean)
  {
    try
    {
      return Me.confirm(paramString, paramBoolean);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return true;
  }

  private boolean b(int paramInt1, int paramInt2)
    throws Throwable
  {
    if (!in(paramInt1, paramInt2))
      return false;
    Dimension localDimension = this.info.getSize();
    int i = localDimension.width;
    int j = localDimension.height;
    if (this.isSpace)
    {
      this.isIn = false;
      this.imi.scroll(true, 0, 0);
      this.isScroll = true;
      this.poS.setLocation(paramInt1, paramInt2);
      return true;
    }
    if ((paramInt1 > i) || (paramInt2 > j))
    {
      if (paramInt1 < this.sizeBar)
      {
        scaleChange(this.isRight ? -1 : 1, false);
        this.isDrag = false;
      }
      else if (paramInt2 < this.sizeBar)
      {
        if (this.tab == null)
          try
          {
            Class localClass = Class.forName("syi.awt.Tab");
            this.tab = ((LComponent)localClass.getConstructors()[0].newInstance(new Object[] { getParent(), this.info }));
            this.mGet = localClass.getMethod("strange", null);
            this.mPoll = localClass.getMethod("poll", null);
          }
          catch (Throwable localThrowable)
          {
            this.tab = this;
          }
        else
          this.tab.setVisible(true);
        this.isDrag = false;
      }
      else if ((paramInt1 > i) && (paramInt2 > j))
      {
        scaleChange(this.isRight ? 1 : -1, false);
        this.isDrag = false;
      }
      else
      {
        this.isIn = false;
        this.imi.scroll(true, 0, 0);
        this.isScroll = true;
        this.poS.setLocation(paramInt1, paramInt2);
      }
      return true;
    }
    return false;
  }

  private void cursor(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt1 != 503)
      return;
    Dimension localDimension = this.info.getSize();
    int i = this.sizeBar;
    int j = localDimension.width;
    int k = localDimension.height;
    int n;
    if ((paramInt2 > j) || (paramInt3 >= k))
      n = (paramInt2 > j) && (paramInt3 > k) ? 3 : paramInt3 < i ? 0 : paramInt2 < i ? 2 : 1;
    else
      n = 0;
    if (this.nowCur != n)
    {
      this.nowCur = n;
      setCursor(this.cursors[n]);
    }
  }

  private void dBz(int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      if (this.psCount <= 0)
      {
        if (paramInt1 != 502)
        {
          dLine(paramInt1, paramInt2, paramInt3);
        }
        else
        {
          this.primary2.drawLine(this.ps[0] >> 16, (short)this.ps[0], this.ps[1] >> 16, (short)this.ps[1]);
          this.user.setRect(0, 0, 0, 0);
          p(3, paramInt2, paramInt3);
          this.ps[1] = this.ps[0];
          this.ps[2] = this.ps[0];
          this.psCount = 1;
          dPreB(false);
        }
        return;
      }
      ePre();
      dPreB(true);
      switch (paramInt1)
      {
      case 503:
      case 506:
        p(this.psCount, paramInt2, paramInt3);
        p(2, paramInt2, paramInt3);
        break;
      case 502:
        p(this.psCount++, paramInt2, paramInt3);
        if (this.psCount >= 3)
        {
          this.psCount -= 1;
          this.psCount = -1;
          this.m.reset(true);
          this.m.setRetouch(this.ps, null, 0, true);
          this.m.draw();
          dEnd(false);
          return;
        }
        p(this.psCount, paramInt2, paramInt3);
      case 504:
      case 505:
      }
      dPreB(false);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void dClear()
    throws InterruptedException
  {
    if (!alert("kakunin_1", true))
      return;
    setM();
    this.m.setRetouch(null, null, 0, true);
    this.m.draw();
    dEnd(false);
  }

  private void dCopy(int paramInt1, int paramInt2, int paramInt3)
    throws InterruptedException
  {
    if (this.psCount <= 1)
    {
      if (paramInt1 == 502)
      {
        if (this.psCount <= 0)
          return;
        this.psCount = 2;
        p(1, paramInt2, paramInt3);
        if (!transRect())
        {
          this.psCount = -1;
        }
        else
        {
          this.ps[0] = this.user.points[0];
          this.ps[1] = this.user.points[1];
          this.ps[2] = this.ps[0];
        }
        this.ps[4] = this.mgInfo.iLayer;
      }
      else
      {
        dRect(paramInt1, paramInt2, paramInt3);
      }
      return;
    }
    int i = this.ps[2] >> 16;
    int j = (short)this.ps[2];
    int k = (this.ps[1] >> 16) - (this.ps[0] >> 16);
    int n = (short)this.ps[1] - (short)this.ps[0];
    switch (paramInt1)
    {
    case 501:
      if (this.imCopy != null)
        this.imCopy.flush();
      this.imCopy = this.m.getImage(this.ps[4], i, j, k, n);
      p(3, paramInt2, paramInt3);
      break;
    case 506:
      m_paint(i, j, k, n);
      i += paramInt2 - (this.ps[3] >> 16);
      j += paramInt3 - (short)this.ps[3];
      p(2, i, j);
      p(3, paramInt2, paramInt3);
      this.primary2.setPaintMode();
      this.primary2.drawImage(this.imCopy, i, j, k, n, Color.white, null);
      this.primary2.setXORMode(Color.white);
      break;
    case 502:
      i += paramInt2 - (this.ps[3] >> 16);
      j += paramInt3 - (short)this.ps[3];
      p(2, i, j);
      this.m.set(this.mgInfo);
      this.m.iLayerSrc = this.ps[4];
      this.m.setRetouch(this.ps, null, 0, true);
      this.m.draw();
      dEnd(false);
      this.psCount = -1;
    case 503:
    case 504:
    case 505:
    }
  }

  private void dEnd(boolean paramBoolean)
    throws InterruptedException
  {
    M localM = this.m;
    if ((localM.iHint != 10) && (localM.iPen == 20) && (localM.iHint != 14))
    {
      int i = this.user.wait;
      this.user.wait = -2;
      if (paramBoolean)
        localM.dEnd();
      int j = localM.iLayer;
      for (int k = j + 1; k < this.info.L; k++)
      {
        if (this.info.layers[k].iAlpha == 0.0F)
          continue;
        localM.iLayerSrc = k;
        setA();
        localM.draw();
        send(localM);
      }
      for (k = j - 1; k >= 0; k--)
      {
        if (this.info.layers[k].iAlpha == 0.0F)
          continue;
        localM.iLayerSrc = k;
        setA();
        localM.draw();
        send(localM);
      }
      this.user.wait = i;
      mPaint(null);
    }
    else
    {
      if (paramBoolean)
        localM.dEnd();
      send(localM);
    }
  }

  private void dFLine(int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      switch (paramInt1)
      {
      case 501:
        poll();
        setM();
        this.m.dStart(paramInt2, paramInt3, getS(), true, true);
        this.oldX = 0;
        this.oldY = 0;
        this.psCount = 1;
        p(0, paramInt2, paramInt3);
        break;
      case 506:
        if ((this.psCount < 0) || (!isOKPo(paramInt2, paramInt3)))
          break;
        this.psCount = 0;
        this.m.dNext(paramInt2, paramInt3, getS(), 0);
        p(this.psCount, paramInt2, paramInt3);
        this.psCount += 1;
        break;
      case 502:
        if (this.psCount < 0)
          break;
        if (this.m.iHint == 11)
          this.m.dNext(paramInt2, paramInt3, getS(), 0);
        dEnd(true);
        this.psCount = -1;
      case 503:
      case 504:
      case 505:
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void dLine(int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      int i;
      switch (paramInt1)
      {
      case 501:
        setM();
        for (i = 0; i < 4; i++)
          p(i, paramInt2, paramInt3);
        this.psCount = 0;
        this.primary2.setColor(new Color(this.m.iColor));
        this.primary2.drawLine(paramInt2, paramInt3, paramInt2, paramInt3);
        break;
      case 506:
        if (this.psCount < 0)
          break;
        this.primary2.drawLine(this.ps[0] >> 16, (short)this.ps[0], this.ps[1] >> 16, (short)this.ps[1]);
        this.primary2.drawLine(this.ps[0] >> 16, (short)this.ps[0], paramInt2, paramInt3);
        p(1, paramInt2, paramInt3);
        break;
      case 502:
        if (this.psCount < 0)
          break;
        i = this.ps[0] >> 16;
        int j = (short)this.ps[0];
        int k = this.m.iSize;
        int n = paramInt2 - i;
        int i1 = paramInt3 - j;
        int i2 = Math.max(Math.abs(n), Math.abs(i1));
        if (i2 > 0)
        {
          this.m.dStart(i, j, k, true, true);
          this.m.dNext(paramInt2, paramInt3, k, 0);
          dEnd(true);
        }
        else
        {
          mPaint(null);
        }
        this.psCount = -1;
      case 503:
      case 504:
      case 505:
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private final void dPre(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if ((this.mgInfo == null) || (this.mgInfo.isText()) || (this.primary2 == null) || (this.psCount >= 0))
      return;
    int i = this.mgInfo.iHint;
    if ((i >= 3) && (i <= 6))
      return;
    try
    {
      int j = this.info.getPenSize(this.mgInfo) * this.info.scale / this.info.Q;
      if (j <= 5)
        return;
      int k = j >>> 1;
      Graphics localGraphics = this.primary2;
      Color localColor = this.cPre;
      localColor = (localColor.getRGB() & 0xFFFFFF) != this.mgInfo.iColor >>> 1 ? new Color(this.mgInfo.iColor >>> 1) : localColor;
      this.cPre = localColor;
      localGraphics.setColor(localColor.getRGB() == 16777215 ? Color.red : (this.mgInfo.iPen == 4) || (this.mgInfo.iPen == 5) ? Color.cyan : localColor);
      if (j <= this.info.scale * 2)
      {
        if (paramBoolean)
          localGraphics.fillRect(this.oldX - k, this.oldY - k, j, j);
        localGraphics.fillRect(paramInt1 - k, paramInt2 - k, j, j);
      }
      else
      {
        if (paramBoolean)
          localGraphics.drawOval(this.oldX - k, this.oldY - k, j, j);
        localGraphics.drawOval(paramInt1 - k, paramInt2 - k, j, j);
      }
      this.oldX = paramInt1;
      this.oldY = paramInt2;
    }
    catch (RuntimeException localRuntimeException)
    {
    }
  }

  private void dPreB(boolean paramBoolean)
    throws InterruptedException
  {
    if (!paramBoolean)
    {
      long l = this.user.getRect();
      this.m.reset(false);
      this.user.isPre = true;
      this.m.setRetouch(this.ps, null, 0, true);
      this.m.draw();
      this.user.addRect((int)(l >>> 48), (int)(l >>> 32) & 0xFFFF, (int)(l >>> 16) & 0xFFFF, (int)(l & 0xFFFF));
      this.m.dBuffer();
      this.user.isPre = false;
    }
    Graphics localGraphics = this.primary2;
    int i = this.psCount + 1;
    for (int j = 0; j < (i - 2 + 1) * 2; j += 2)
      localGraphics.drawLine(this.ps[j] >> 16, (short)this.ps[j], this.ps[(j + 1)] >> 16, (short)this.ps[(j + 1)]);
    j = 7;
    int k = j / 2;
    for (int n = 1; n < i; n++)
      localGraphics.drawOval((this.ps[n] >> 16) - k, (short)this.ps[n] - k, j, j);
  }

  public void drawScroll(Graphics paramGraphics)
  {
    try
    {
      if (paramGraphics == null)
        paramGraphics = this.primary;
      if (paramGraphics == null)
        return;
      float f = this.info.scale;
      int j = this.sizeBar;
      int k = this.info.scaleX;
      int n = this.info.scaleY;
      int i1 = this.info.imW;
      int i2 = this.info.imH;
      Dimension localDimension = this.info.getSize();
      int i3 = (int)(localDimension.width / f);
      int i4 = (int)(localDimension.height / f);
      if (k + i3 >= i1)
      {
        k = Math.max(0, i1 - i3);
        this.info.scaleX = k;
      }
      if (n + i4 - 1 >= i2)
      {
        n = Math.max(0, i2 - i4);
        this.info.scaleY = n;
      }
      int i5 = localDimension.width - j;
      int i6 = localDimension.height - j;
      int i7 = Math.min((int)(i3 / i1 * i5), i5);
      int i8 = Math.min((int)(i4 / i2 * i6), i6);
      int i9 = (int)(k / i1 * i5);
      int i10 = (int)(n / i2 * i6);
      int[] arrayOfInt = this.rS;
      paramGraphics.setColor(this.cls[0]);
      for (int i = 0; i < 20; i += 4)
        paramGraphics.drawRect(arrayOfInt[i], arrayOfInt[(i + 1)], arrayOfInt[(i + 2)], arrayOfInt[(i + 3)]);
      if (i9 > 0)
      {
        paramGraphics.setColor(this.cls[2]);
        paramGraphics.drawRect(arrayOfInt[0] + 1, arrayOfInt[1] + 1, i9 - 2, arrayOfInt[3] - 2);
        paramGraphics.setColor(this.cls[1]);
        paramGraphics.fillRect(arrayOfInt[0] + 2, arrayOfInt[1] + 2, i9 - 2, arrayOfInt[3] - 3);
      }
      paramGraphics.setColor(this.cls[2]);
      paramGraphics.drawRect(arrayOfInt[0] + i9 + i7, arrayOfInt[1] + 1, arrayOfInt[2] - i9 - i7 - 1, arrayOfInt[3] - 2);
      paramGraphics.setColor(this.cls[1]);
      paramGraphics.fillRect(arrayOfInt[0] + 1 + i9 + i7, arrayOfInt[1] + 2, arrayOfInt[2] - i9 - i7 - 2, arrayOfInt[3] - 3);
      paramGraphics.setColor(this.cls[1]);
      if (i10 > 0)
      {
        paramGraphics.setColor(this.cls[2]);
        paramGraphics.drawRect(arrayOfInt[4] + 1, arrayOfInt[5] + 1, arrayOfInt[6] - 2, i10 - 1);
        paramGraphics.setColor(this.cls[1]);
        paramGraphics.fillRect(arrayOfInt[4] + 2, arrayOfInt[5] + 2, arrayOfInt[6] - 3, i10 - 1);
      }
      paramGraphics.setColor(this.cls[2]);
      paramGraphics.drawRect(arrayOfInt[4] + 1, arrayOfInt[5] + i10 + i8, arrayOfInt[6] - 2, arrayOfInt[7] - i10 - i8 - 1);
      paramGraphics.setColor(this.cls[1]);
      paramGraphics.fillRect(arrayOfInt[4] + 2, arrayOfInt[5] + i10 + i8, arrayOfInt[6] - 3, arrayOfInt[7] - i10 - i8 - 1);
      for (i = 8; i < 20; i += 4)
      {
        for (i11 = 0; i11 < 2; i11++)
        {
          paramGraphics.setColor(this.cls[(2 - i11)]);
          if (i11 == 0)
            paramGraphics.drawRect(arrayOfInt[i] + 1, arrayOfInt[(i + 1)] + 1, arrayOfInt[(i + 2)] - 2, arrayOfInt[(i + 3)] - 2);
          else
            paramGraphics.fillRect(arrayOfInt[i] + 2, arrayOfInt[(i + 1)] + 2, arrayOfInt[(i + 2)] - 3, arrayOfInt[(i + 3)] - 3);
        }
        paramGraphics.setColor(this.cls[3]);
        i11 = arrayOfInt[(i + 2)] / 2;
        i12 = arrayOfInt[(i + 3)] / 2;
        if (i == 16)
        {
          i13 = arrayOfInt[i] + i11 / 2;
          i14 = arrayOfInt[(i + 1)] + i12 / 2;
          i12 /= 2;
          paramGraphics.drawRect(i13, i14, i11, i12);
          paramGraphics.fillRect(i13, i14 + i12, 1, i12);
        }
        else
        {
          paramGraphics.fillRect(arrayOfInt[i] + i11 / 2, arrayOfInt[(i + 1)] + i12, i11 + 1, 1);
          if (i != 8)
            continue;
          paramGraphics.fillRect(arrayOfInt[i] + i11, arrayOfInt[(i + 1)] + i12 / 2, 1, i12);
        }
      }
      int i11 = arrayOfInt[0] + i9;
      int i12 = arrayOfInt[1] + 1;
      int i13 = arrayOfInt[4] + 1;
      int i14 = arrayOfInt[5] + i10;
      paramGraphics.setColor(this.cls[0]);
      paramGraphics.drawRect(i11, i12, i7, arrayOfInt[3] - 2);
      paramGraphics.drawRect(i13, i14, arrayOfInt[6] - 2, i8 + 1);
      paramGraphics.setColor(this.cls[3]);
      paramGraphics.fillRect(i11 + 2, i12 + 2, i7 - 3, arrayOfInt[3] - 5);
      paramGraphics.fillRect(i13 + 2, i14 + 2, arrayOfInt[6] - 5, i8 - 2);
      paramGraphics.setColor(this.cls[4]);
      paramGraphics.fillRect(i11 + 1, i12 + 1, i7 - 2, 1);
      paramGraphics.fillRect(i11 + 1, i12 + 2, 1, arrayOfInt[3] - 5);
      paramGraphics.fillRect(i13 + 1, i14 + 1, arrayOfInt[6] - 4, 1);
      paramGraphics.fillRect(i13 + 1, i14 + 2, 1, i8 - 2);
      paramGraphics.setColor(this.cls[5]);
      paramGraphics.fillRect(i11 + i7 - 1, i12 + 1, 1, arrayOfInt[3] - 4);
      paramGraphics.fillRect(i11 + 1, i12 + arrayOfInt[3] - 3, i7 - 1, 1);
      paramGraphics.fillRect(i13 + arrayOfInt[6] - 3, i14 + 1, 1, i8 - 1);
      paramGraphics.fillRect(i13 + 1, i14 + i8, arrayOfInt[6] - 3, 1);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void dRect(int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      int[] arrayOfInt = this.user.points;
      switch (paramInt1)
      {
      case 501:
        setM();
        p(0, paramInt2, paramInt3);
        this.m.memset(arrayOfInt, 0);
        this.psCount = 1;
        break;
      case 506:
        if (this.psCount != 1)
          break;
        int i = arrayOfInt[0];
        int j = arrayOfInt[1];
        int k = i >> 16;
        int n = (short)i;
        this.primary2.drawRect(k, n, (j >> 16) - k - 1, (short)j - n - 1);
        p(1, paramInt2, paramInt3);
        transRect();
        i = arrayOfInt[0];
        j = arrayOfInt[1];
        k = i >> 16;
        n = (short)i;
        this.primary2.drawRect(k, n, (j >> 16) - k - 1, (short)j - n - 1);
        break;
      case 502:
        if (this.psCount > 0)
        {
          p(1, paramInt2, paramInt3);
          if (transRect())
          {
            this.ps[0] = arrayOfInt[0];
            this.ps[1] = arrayOfInt[1];
            this.m.setRetouch(this.ps, null, 0, true);
            if (this.m.iPen != 20)
              this.m.draw();
            dEnd(false);
          }
        }
        this.psCount = -1;
      case 503:
      case 504:
      case 505:
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void dScroll(MouseEvent paramMouseEvent, int paramInt1, int paramInt2)
  {
    if ((paramInt1 != 0) || (paramInt2 != 0))
    {
      scroll(paramInt1, paramInt2);
      return;
    }
    int i = paramMouseEvent.getID();
    Point localPoint = paramMouseEvent.getPoint();
    if ((i == 502) || (i == 506))
    {
      scroll(this.isSpace ? this.poS.x - localPoint.x : localPoint.x - this.poS.x, this.isSpace ? this.poS.y - localPoint.y : localPoint.y - this.poS.y);
      this.poS = localPoint;
    }
    else if (i == 501)
    {
      this.poS = localPoint;
    }
  }

  public void dText(int paramInt1, int paramInt2, int paramInt3)
  {
    switch (paramInt1)
    {
    case 501:
      break;
    case 502:
      setM();
      if (this.text == null)
      {
        this.text = new TextField(16);
        this.text.addActionListener(this);
        this.isText = true;
        getParent().add(this.text, 0);
      }
      if (!this.isText)
      {
        this.primary.setColor(new Color(this.mgInfo.iColor));
        this.primary.fillRect(paramInt2 - 1, paramInt3 - 1, this.mgInfo.iSize + 1, 1);
        this.primary.fillRect(paramInt2 - 1, paramInt3, 1, this.mgInfo.iSize);
      }
      else
      {
        this.text.setFont(this.m.getFont(this.m.iSize * this.info.scale / this.info.Q));
        this.text.setSize(this.text.getPreferredSize());
        Point localPoint = getLocation();
        this.text.setLocation(paramInt2 + localPoint.x, paramInt3 + localPoint.y + 2);
        this.text.setVisible(true);
      }
      p(0, paramInt2, paramInt3);
    }
  }

  private void ePre()
  {
    dPre(this.oldX, this.oldY, false);
    this.oldX = -1000;
    this.oldY = -1000;
  }

  private final int getS()
  {
    try
    {
      if ((this.tab == null) || (this.tab == this))
        return 255;
      return ((Integer)this.mGet.invoke(this.tab, null)).intValue();
    }
    catch (Throwable localThrowable)
    {
      this.tab = this;
    }
    return 255;
  }

  private final boolean in(int paramInt1, int paramInt2)
  {
    Dimension localDimension = getSize();
    return (paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 < localDimension.width) && (paramInt2 < localDimension.height);
  }

  public void init(Applet paramApplet, Res paramRes, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Cursor[] paramArrayOfCursor)
    throws IOException
  {
    String str = "color_";
    this.cursors = paramArrayOfCursor;
    this.cls = new Color[6];
    this.cls[0] = new Color(paramRes.getP(str + "frame", 5263480));
    this.cls[1] = new Color(paramRes.getP(str + "icon", 13421823));
    this.cls[2] = new Color(paramRes.getP(str + "bar_hl", 16777215));
    this.cls[3] = new Color(paramRes.getP(str + "bar", 7303086));
    this.cls[4] = new Color(paramRes.getP(str + "bar_hl", 15658751));
    this.cls[5] = new Color(paramRes.getP(str + "bar_shadow", 11184810));
    setBackground(Color.white);
    this.m = new M();
    this.user = this.m.newUser(this);
    M.Info localInfo = this.m.newInfo(paramApplet, this, paramRes);
    localInfo.setSize(paramInt1, paramInt2, paramInt3);
    localInfo.setL(paramInt4);
    this.info = localInfo;
    this.mgInfo = localInfo.m;
  }

  private final boolean isOKPo(int paramInt1, int paramInt2)
  {
    int i = this.ps[(this.psCount - 1)];
    return Math.max(Math.abs(paramInt1 - (i >> 16)), Math.abs(paramInt2 - (short)i)) >= this.info.scale;
  }

  public void m_paint(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Dimension localDimension = this.info.getSize();
    if (this.m == null)
    {
      this.primary.setColor(Color.white);
      this.primary.fillRect(0, 0, localDimension.width, localDimension.height);
      return;
    }
    if ((paramInt1 == 0) && (paramInt2 == 0) && (paramInt3 == 0) && (paramInt4 == 0))
    {
      paramInt1 = paramInt2 = 0;
      paramInt3 = localDimension.width;
      paramInt4 = localDimension.height;
    }
    else
    {
      paramInt1 = Math.max(paramInt1, 0);
      paramInt2 = Math.max(paramInt2, 0);
      paramInt3 = Math.min(paramInt3, localDimension.width);
      paramInt4 = Math.min(paramInt4, localDimension.height);
    }
    this.m.m_paint(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void m_paint(Rectangle paramRectangle)
  {
    if (paramRectangle == null)
      m_paint(0, 0, 0, 0);
    else
      m_paint(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
  }

  private void mPaint(Graphics paramGraphics)
  {
    if (paramGraphics == null)
      paramGraphics = this.primary;
    drawScroll(paramGraphics);
    if (!this.isPaint)
      return;
    m_paint(paramGraphics == this.primary ? null : paramGraphics.getClipBounds());
  }

  private final void p(int paramInt1, int paramInt2, int paramInt3)
  {
    this.ps[paramInt1] = (paramInt2 << 16 | paramInt3 & 0xFFFF);
  }

  public void paint2(Graphics paramGraphics)
  {
    try
    {
      Rectangle localRectangle = paramGraphics.getClipBounds();
      int i = (this.info == null ? 0 : this.info.scale) * 2;
      paramGraphics.setClip(localRectangle.x - i, localRectangle.y - i, localRectangle.width + i * 2, localRectangle.height + i * 2);
      mPaint(paramGraphics);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void pMouse(MouseEvent paramMouseEvent)
  {
    try
    {
      int i = paramMouseEvent.getID();
      int j = this.info.scale;
      int k = paramMouseEvent.getX() / j * j;
      int n = paramMouseEvent.getY() / j * j;
      int i1 = i == 501 ? 1 : 0;
      int i2 = i == 506 ? 1 : 0;
      boolean bool = this.isRight;
      if ((paramMouseEvent.isAltDown()) && (paramMouseEvent.isControlDown()))
      {
        if (this.psCount >= 0)
          reset();
        if (i1 != 0)
        {
          this.poS.y = n;
          this.poS.x = this.mgInfo.iSize;
          m_paint(null);
        }
        if (i2 != 0)
        {
          Dimension localDimension = getSize();
          int i3 = localDimension.width / 2;
          int i4 = localDimension.height / 2;
          int i5 = this.info.getPenSize(this.mgInfo) * this.info.scale;
          m_paint(i3 - i5, i4 - i5, i5 * 2, i5 * 2);
          this.imi.setLineSize((n - this.poS.y) / 4 + this.poS.x);
          dPre(i3, i4, false);
        }
        return;
      }
      if (i1 != 0)
      {
        if ((this.text == null) || (!this.text.isVisible()))
          requestFocus();
        bool = this.isRight = Awt.isR(paramMouseEvent);
        if (!this.isDrag)
          dPre(this.oldX, this.oldY, false);
        this.isDrag = true;
        if (b(k, n))
          return;
        if ((this.isText) && (this.text != null) && (this.text.isVisible()))
          this.text.setVisible(false);
      }
      if (i == 502)
      {
        if (!this.isDrag)
          return;
        this.isRight = false;
        this.isDrag = false;
        this.isPaint = true;
        if (this.isScroll)
        {
          this.isScroll = false;
          this.imi.scroll(false, 0, 0);
          if (this.info.scale < 1)
            m_paint(null);
          return;
        }
      }
      if ((this.isRight) && (this.isDrag))
      {
        if (this.psCount >= 0)
        {
          reset();
          this.isRight = false;
          this.isDrag = false;
          this.isPaint = true;
        }
        else
        {
          this.imi.setARGB(this.user.getPixel(k / this.info.scale + this.info.scaleX, n / this.info.scale + this.info.scaleY) & 0xFFFFFF | this.info.m.iAlpha << 24);
        }
        return;
      }
      if (!this.isDrag)
      {
        cursor(i, k, n);
        switch (i)
        {
        case 504:
          getS();
          break;
        case 503:
          dPre(k, n, this.isIn);
          this.isIn = true;
          break;
        case 505:
          if (!this.isIn)
            break;
          this.isIn = false;
          dPre(this.oldX, this.oldY, false);
        }
      }
      if (this.isScroll)
      {
        dScroll(paramMouseEvent, 0, 0);
        return;
      }
      if ((this.isEnable) && ((this.mgInfo.iLayer + 1 & this.info.permission) != 0L) && (!bool) && ((this.mgInfo.iHint == 10) || (this.info.layers[this.mgInfo.iLayer].iAlpha > 0.0F)))
        switch (this.mgInfo.iHint)
        {
        case 0:
        case 11:
          dFLine(i, k, n);
          break;
        case 1:
          dLine(i, k, n);
          break;
        case 2:
          dBz(i, k, n);
          break;
        case 8:
        case 12:
          dText(i, k, n);
          break;
        case 9:
          dCopy(i, k, n);
          break;
        case 10:
          if ((!this.info.isClean) || (i1 == 0))
            break;
          dClear();
          break;
        case 7:
          if ((i1 == 0) || (!this.info.isFill))
            break;
          this.m.set(this.info.m);
          p(0, k, n);
          p(1, k + 1024, n + 1024);
          transRect();
          this.m.setRetouch(this.user.points, null, 0, true);
          this.m.draw();
          send();
          break;
        case 3:
        case 4:
        case 5:
        case 6:
        default:
          dRect(i, k, n);
        }
      if ((i == 502) && (this.isIn))
      {
        dPre(k, n, false);
        this.isDrag = false;
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private final void poll()
  {
    if ((this.tab == null) || (this.tab == this))
      return;
    try
    {
      if (((Boolean)this.mPoll.invoke(this.tab, null)).booleanValue())
        return;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    this.mgInfo.iSOB = 0;
  }

  protected void processKeyEvent(KeyEvent paramKeyEvent)
  {
    try
    {
      int i = (!paramKeyEvent.isControlDown()) && (!paramKeyEvent.isShiftDown()) ? 0 : 1;
      boolean bool1 = paramKeyEvent.isAltDown();
      boolean bool2 = true;
      switch (paramKeyEvent.getID())
      {
      case 401:
        switch (paramKeyEvent.getKeyCode())
        {
        case 32:
          this.isSpace = true;
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
          bool2 = false;
        case 90:
          if (bool1)
            bool2 = false;
          if (i == 0)
            break;
          this.imi.undo(bool2);
          break;
        case 66:
          this.imi.setARGB(this.mgInfo.iAlpha << 24 | this.mgInfo.iColor);
          break;
        case 69:
          this.imi.setARGB(16777215);
        }
        break;
      case 402:
        switch (paramKeyEvent.getKeyCode())
        {
        case 32:
          this.isSpace = false;
        }
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void reset()
  {
    if (this.psCount >= 0)
    {
      this.psCount = -1;
      switch (this.mgInfo.iHint)
      {
      case 0:
      case 11:
        this.m.reset(true);
        break;
      default:
        this.m.reset(false);
        m_paint(null);
      }
    }
  }

  public synchronized void resetGraphics()
  {
    if (this.primary != null)
      this.primary.dispose();
    if (this.primary2 != null)
      this.primary2.dispose();
    Dimension localDimension = getSize();
    int i = localDimension.width - this.sizeBar;
    int j = localDimension.height - this.sizeBar;
    this.primary = getGraphics();
    if (this.primary != null)
    {
      this.primary.translate(getGapX(), getGapY());
      this.primary2 = this.primary.create(0, 0, i, j);
      this.primary2.setXORMode(Color.white);
      this.info.setComponent(this, this.primary, i, j);
    }
    int[] arrayOfInt = this.rS;
    int k = this.sizeBar;
    localDimension = this.info.getSize();
    for (int n = 0; n < 20; n++)
      arrayOfInt[n] = k;
    arrayOfInt[1] = localDimension.height;
    arrayOfInt[2] = (localDimension.width - k);
    arrayOfInt[4] = localDimension.width;
    arrayOfInt[7] = (localDimension.height - k);
    arrayOfInt[8] = 0;
    arrayOfInt[9] = localDimension.height;
    arrayOfInt[12] = localDimension.width;
    arrayOfInt[13] = localDimension.height;
    arrayOfInt[16] = localDimension.width;
    arrayOfInt[17] = 0;
  }

  public void scaleChange(int paramInt, boolean paramBoolean)
  {
    if (this.isIn)
      ePre();
    if ((this.info.addScale(paramInt, paramBoolean)) && (!this.isGUI))
    {
      float f = this.info.scale;
      int i = (int)(this.info.imW * f) + this.sizeBar;
      int j = (int)(this.info.imH * f) + this.sizeBar;
      Dimension localDimension = getSize();
      int k = localDimension.width;
      int n = localDimension.height;
      if ((i != localDimension.width) || (j != localDimension.height))
        setSize(i, j);
      localDimension = getSize();
      if ((localDimension.width == k) && (localDimension.height == n))
        mPaint(null);
      else
        this.imi.changeSize();
    }
  }

  public void scroll(int paramInt1, int paramInt2)
  {
    if (this.info == null)
      return;
    Dimension localDimension = this.info.getSize();
    int i = this.info.imW;
    int j = this.info.imH;
    float f1 = this.info.scale;
    int k = this.info.scaleX;
    int n = this.info.scaleY;
    float f2 = paramInt1 * (i / localDimension.width);
    if (f1 < 1.0F)
      f2 /= f1;
    if (f2 != 0.0F)
      f2 = (f2 <= 0.0F) && (f2 >= -1.0F) ? -1.0F : (f2 >= 0.0F) && (f2 <= 1.0F) ? 1.0F : f2;
    int i1 = (int)f2;
    f2 = paramInt2 * (j / localDimension.height);
    if (f2 != 0.0F)
      f2 = (f2 <= 0.0F) && (f2 >= -1.0F) ? -1.0F : (f2 >= 0.0F) && (f2 <= 1.0F) ? 1.0F : f2;
    if (f1 < 1.0F)
      f2 /= f1;
    int i2 = (int)f2;
    Graphics localGraphics = this.primary;
    this.info.scaleX = Math.max(k + i1, 0);
    this.info.scaleY = Math.max(n + i2, 0);
    drawScroll(localGraphics);
    this.poS.translate(paramInt1, paramInt2);
    int i3 = (int)((this.info.scaleX - k) * f1);
    int i4 = (int)((this.info.scaleY - n) * f1);
    i1 = localDimension.width - Math.abs(i3);
    i2 = localDimension.height - Math.abs(i4);
    try
    {
      localGraphics.copyArea(Math.max(i3, 0), Math.max(i4, 0), i1, i2, -i3, -i4);
      if (f1 >= 1.0F)
      {
        if (i3 != 0)
          if (i3 > 0)
            m_paint(localDimension.width - i3, 0, i3, localDimension.height);
          else
            m_paint(0, 0, -i3, localDimension.height);
        if (i4 != 0)
          if (i4 > 0)
            m_paint(0, localDimension.height - i4, localDimension.width, i4);
          else
            m_paint(0, 0, localDimension.width, -i4);
      }
      else
      {
        if (i3 != 0)
          if (i3 > 0)
            localGraphics.clearRect(localDimension.width - i3, 0, i3, localDimension.height);
          else
            localGraphics.clearRect(0, 0, -i3, localDimension.height);
        if (i4 != 0)
          if (i3 > 0)
            localGraphics.clearRect(0, localDimension.height - i4, localDimension.width, i4);
          else
            localGraphics.clearRect(0, 0, localDimension.width, -i4);
      }
      this.imi.scroll(true, i3, i4);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void send()
  {
    this.imi.send(this.m);
  }

  public void send(String paramString)
  {
    try
    {
      M localM = new M(this.info, this.user);
      localM.set(paramString);
      localM.draw();
      send(localM);
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void send(M paramM)
    throws InterruptedException
  {
    this.imi.send(paramM);
  }

  public void setA()
  {
    this.m.iAlpha2 = ((int)(this.info.layers[this.m.iLayer].iAlpha * 255.0F) << 8 | (int)(this.info.layers[this.m.iLayerSrc].iAlpha * 255.0F));
  }

  private final void setM()
  {
    this.m.set(this.mgInfo);
    if (this.m.iPen == 20)
      this.m.iLayerSrc = this.m.iLayer;
    setA();
  }

  public void setSize(Dimension paramDimension)
  {
    setSize(paramDimension.width, paramDimension.height);
  }

  public void setSize(int paramInt1, int paramInt2)
  {
    if (this.info == null)
    {
      super.setSize(paramInt1, paramInt2);
    }
    else
    {
      int i = this.info.imW * this.info.scale + this.sizeBar;
      int j = this.info.imH * this.info.scale + this.sizeBar;
      super.setSize(Math.min(paramInt1 - getGapW(), i), Math.min(paramInt2 - getGapW(), j));
    }
    repaint();
  }

  public final boolean transRect()
  {
    int i = this.ps[0];
    int j = i >> 16;
    int k = (short)i;
    i = this.ps[1];
    int n = i >> 16;
    int i1 = (short)i;
    int i2 = Math.max(Math.min(j, n), 0);
    int i3 = Math.min(Math.max(j, n), this.info.imW * this.info.scale);
    int i4 = Math.max(Math.min(k, i1), 0);
    int i5 = Math.min(Math.max(k, i1), this.info.imH * this.info.scale);
    if ((i3 - i2 < this.info.scale) || (i5 - i4 < this.info.scale))
      return false;
    this.user.points[0] = (i2 << 16 | i4 & 0xFFFF);
    this.user.points[1] = (i3 << 16 | i5 & 0xFFFF);
    return true;
  }

  public void up()
  {
    if (this.tab != null)
      this.tab.repaint();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_client.Mi
 * JD-Core Version:    0.6.0
 */