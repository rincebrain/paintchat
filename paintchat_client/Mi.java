package paintchat_client;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import paintchat.*;
import syi.awt.Awt;
import syi.awt.LComponent;

// Referenced classes of package paintchat_client:
//            Me, IMi

public class Mi extends LComponent
    implements ActionListener
{

    private LComponent tab;
    private Method mGet;
    private Method mPoll;
    private IMi imi;
    private Res res;
    private boolean isRight;
    public TextField text;
    private boolean isText;
    private M m;
    public paintchat.M.Info info;
    public paintchat.M.User user;
    private M mgInfo;
    public boolean isEnable;
    private int ps[];
    private int psCount;
    private Graphics primary;
    private Graphics primary2;
    private int oldX;
    private int oldY;
    private boolean isIn;
    private int nowCur;
    private Cursor cursors[];
    private Image imCopy;
    private boolean isSpace;
    private boolean isScroll;
    private boolean isDrag;
    private Point poS;
    private int rS[];
    private int sizeBar;
    private Color cls[];
    private Color cPre;

    public Mi(IMi imi1, Res res1)
        throws Exception
    {
        isRight = false;
        isEnable = false;
        ps = new int[5];
        psCount = -1;
        oldX = 0;
        oldY = 0;
        isIn = false;
        nowCur = -1;
        imCopy = null;
        isSpace = false;
        isScroll = false;
        isDrag = false;
        poS = new Point();
        rS = new int[20];
        sizeBar = 20;
        cPre = Color.black;
        imi = imi1;
        res = res1;
        super.isRepaint = false;
        super.isHide = false;
        super.isGUI = false;
        super.iGap = 2;
        Me.res = res1;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(text != null)
        {
            addText(actionevent.getActionCommand());
            if(isText)
            {
                text.setVisible(false);
            }
        }
    }

    public void addText(String s)
    {
        try
        {
            if(!mgInfo.isText())
            {
                return;
            }
            setM();
            byte abyte0[] = ('\0' + s).getBytes("UTF8");
            m.setRetouch(ps, abyte0, abyte0.length, true);
            m.draw();
            send(m);
        }
        catch(Exception _ex) { }
    }

    public boolean alert(String s, boolean flag)
    {
        try
        {
            return Me.confirm(s, flag);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return true;
    }

    private boolean b(int i, int j)
        throws Throwable
    {
        if(!in(i, j))
        {
            return false;
        }
        Dimension dimension = info.getSize();
        int k = dimension.width;
        int l = dimension.height;
        if(isSpace)
        {
            isIn = false;
            imi.scroll(true, 0, 0);
            isScroll = true;
            poS.setLocation(i, j);
            return true;
        }
        if(i > k || j > l)
        {
            if(i < sizeBar)
            {
                scaleChange(isRight ? -1 : 1, false);
                isDrag = false;
            } else
            if(j < sizeBar)
            {
                if(tab == null)
                {
                    try
                    {
                        Class class1 = Class.forName("syi.awt.Tab");
                        tab = (LComponent)class1.getConstructors()[0].newInstance(new Object[] {
                            getParent(), info
                        });
                        mGet = class1.getMethod("strange", null);
                        mPoll = class1.getMethod("poll", null);
                    }
                    catch(Throwable _ex)
                    {
                        tab = this;
                    }
                } else
                {
                    tab.setVisible(true);
                }
                isDrag = false;
            } else
            if(i > k && j > l)
            {
                scaleChange(isRight ? 1 : -1, false);
                isDrag = false;
            } else
            {
                isIn = false;
                imi.scroll(true, 0, 0);
                isScroll = true;
                poS.setLocation(i, j);
            }
            return true;
        } else
        {
            return false;
        }
    }

    private void cursor(int i, int j, int k)
    {
        if(i != 503)
        {
            return;
        }
        Dimension dimension = info.getSize();
        int l = sizeBar;
        int i1 = dimension.width;
        int j1 = dimension.height;
        int k1;
        if(j > i1 || k >= j1)
        {
            k1 = j >= l ? ((int) (k >= l ? ((int) (j <= i1 || k <= j1 ? 1 : 3)) : 0)) : 2;
        } else
        {
            k1 = 0;
        }
        if(nowCur != k1)
        {
            nowCur = k1;
            setCursor(cursors[k1]);
        }
    }

    private void dBz(int i, int j, int k)
    {
        try
        {
            if(psCount <= 0)
            {
                if(i != 502)
                {
                    dLine(i, j, k);
                } else
                {
                    primary2.drawLine(ps[0] >> 16, (short)ps[0], ps[1] >> 16, (short)ps[1]);
                    user.setRect(0, 0, 0, 0);
                    p(3, j, k);
                    ps[1] = ps[0];
                    ps[2] = ps[0];
                    psCount = 1;
                    dPreB(false);
                }
                return;
            }
            ePre();
            dPreB(true);
            switch(i)
            {
            case 504: 
            case 505: 
            default:
                break;

            case 503: 
            case 506: 
                p(psCount, j, k);
                p(2, j, k);
                break;

            case 502: 
                p(psCount++, j, k);
                if(psCount >= 3)
                {
                    psCount--;
                    psCount = -1;
                    m.reset(true);
                    m.setRetouch(ps, null, 0, true);
                    m.draw();
                    dEnd(false);
                    return;
                }
                p(psCount, j, k);
                break;
            }
            dPreB(false);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void dClear()
        throws InterruptedException
    {
        if(!alert("kakunin_1", true))
        {
            return;
        } else
        {
            setM();
            m.setRetouch(null, null, 0, true);
            m.draw();
            dEnd(false);
            return;
        }
    }

    private void dCopy(int i, int j, int k)
        throws InterruptedException
    {
        if(psCount <= 1)
        {
            if(i == 502)
            {
                if(psCount <= 0)
                {
                    return;
                }
                psCount = 2;
                p(1, j, k);
                if(!transRect())
                {
                    psCount = -1;
                } else
                {
                    ps[0] = user.points[0];
                    ps[1] = user.points[1];
                    ps[2] = ps[0];
                }
                ps[4] = mgInfo.iLayer;
            } else
            {
                dRect(i, j, k);
            }
            return;
        }
        int l = ps[2] >> 16;
        int i1 = (short)ps[2];
        int j1 = (ps[1] >> 16) - (ps[0] >> 16);
        int k1 = (short)ps[1] - (short)ps[0];
        switch(i)
        {
        case 503: 
        case 504: 
        case 505: 
        default:
            break;

        case 501: 
            if(imCopy != null)
            {
                imCopy.flush();
            }
            imCopy = m.getImage(ps[4], l, i1, j1, k1);
            p(3, j, k);
            break;

        case 506: 
            m_paint(l, i1, j1, k1);
            l += j - (ps[3] >> 16);
            i1 += k - (short)ps[3];
            p(2, l, i1);
            p(3, j, k);
            primary2.setPaintMode();
            primary2.drawImage(imCopy, l, i1, j1, k1, Color.white, null);
            primary2.setXORMode(Color.white);
            break;

        case 502: 
            l += j - (ps[3] >> 16);
            i1 += k - (short)ps[3];
            p(2, l, i1);
            m.set(mgInfo);
            m.iLayerSrc = ps[4];
            m.setRetouch(ps, null, 0, true);
            m.draw();
            dEnd(false);
            psCount = -1;
            break;
        }
    }

    private void dEnd(boolean flag)
        throws InterruptedException
    {
        M m1 = m;
        if(m1.iHint != 10 && m1.iPen == 20 && m1.iHint != 14)
        {
            int i = user.wait;
            user.wait = -2;
            if(flag)
            {
                m1.dEnd();
            }
            int j = m1.iLayer;
            for(int k = j + 1; k < info.L; k++)
            {
                if(info.layers[k].iAlpha != 0.0F)
                {
                    m1.iLayerSrc = k;
                    setA();
                    m1.draw();
                    send(m1);
                }
            }

            for(int l = j - 1; l >= 0; l--)
            {
                if(info.layers[l].iAlpha != 0.0F)
                {
                    m1.iLayerSrc = l;
                    setA();
                    m1.draw();
                    send(m1);
                }
            }

            user.wait = i;
            mPaint(null);
        } else
        {
            if(flag)
            {
                m1.dEnd();
            }
            send(m1);
        }
    }

    private void dFLine(int i, int j, int k)
    {
        try
        {
            switch(i)
            {
            case 503: 
            case 504: 
            case 505: 
            default:
                break;

            case 501: 
                poll();
                setM();
                m.dStart(j, k, getS(), true, true);
                oldX = 0;
                oldY = 0;
                psCount = 1;
                p(0, j, k);
                break;

            case 506: 
                if(psCount >= 0 && isOKPo(j, k))
                {
                    psCount = 0;
                    m.dNext(j, k, getS(), 0);
                    p(psCount, j, k);
                    psCount++;
                }
                break;

            case 502: 
                if(psCount < 0)
                {
                    break;
                }
                if(m.iHint == 11)
                {
                    m.dNext(j, k, getS(), 0);
                }
                dEnd(true);
                psCount = -1;
                break;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void dLine(int i, int j, int k)
    {
        try
        {
            switch(i)
            {
            case 503: 
            case 504: 
            case 505: 
            default:
                break;

            case 501: 
                setM();
                for(int l = 0; l < 4; l++)
                {
                    p(l, j, k);
                }

                psCount = 0;
                primary2.setColor(new Color(m.iColor));
                primary2.drawLine(j, k, j, k);
                break;

            case 506: 
                if(psCount >= 0)
                {
                    primary2.drawLine(ps[0] >> 16, (short)ps[0], ps[1] >> 16, (short)ps[1]);
                    primary2.drawLine(ps[0] >> 16, (short)ps[0], j, k);
                    p(1, j, k);
                }
                break;

            case 502: 
                if(psCount < 0)
                {
                    break;
                }
                int i1 = ps[0] >> 16;
                short word0 = (short)ps[0];
                int j1 = m.iSize;
                int k1 = j - i1;
                int l1 = k - word0;
                int i2 = Math.max(Math.abs(k1), Math.abs(l1));
                if(i2 > 0)
                {
                    m.dStart(i1, word0, j1, true, true);
                    m.dNext(j, k, j1, 0);
                    dEnd(true);
                } else
                {
                    mPaint(null);
                }
                psCount = -1;
                break;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private final void dPre(int i, int j, boolean flag)
    {
        if(mgInfo == null || mgInfo.isText() || primary2 == null || psCount >= 0)
        {
            return;
        }
        int k = mgInfo.iHint;
        if(k >= 3 && k <= 6)
        {
            return;
        }
        try
        {
            int l = (info.getPenSize(mgInfo) * info.scale) / info.Q;
            if(l <= 5)
            {
                return;
            }
            int i1 = l >>> 1;
            Graphics g = primary2;
            Color color = cPre;
            color = (color.getRGB() & 0xffffff) == mgInfo.iColor >>> 1 ? color : new Color(mgInfo.iColor >>> 1);
            cPre = color;
            g.setColor(mgInfo.iPen != 4 && mgInfo.iPen != 5 ? color.getRGB() != 0xffffff ? color : Color.red : Color.cyan);
            if(l <= info.scale * 2)
            {
                if(flag)
                {
                    g.fillRect(oldX - i1, oldY - i1, l, l);
                }
                g.fillRect(i - i1, j - i1, l, l);
            } else
            {
                if(flag)
                {
                    g.drawOval(oldX - i1, oldY - i1, l, l);
                }
                g.drawOval(i - i1, j - i1, l, l);
            }
            oldX = i;
            oldY = j;
        }
        catch(RuntimeException _ex) { }
    }

    private void dPreB(boolean flag)
        throws InterruptedException
    {
        if(!flag)
        {
            int _tmp = user.wait;
            long l = user.getRect();
            m.reset(false);
            user.isPre = true;
            m.setRetouch(ps, null, 0, true);
            m.draw();
            user.addRect((int)(l >>> 48), (int)(l >>> 32) & 0xffff, (int)(l >>> 16) & 0xffff, (int)(l & 65535L));
            m.dBuffer();
            user.isPre = false;
        }
        Graphics g = primary2;
        int i = psCount + 1;
        for(int j = 0; j < ((i - 2) + 1) * 2; j += 2)
        {
            g.drawLine(ps[j] >> 16, (short)ps[j], ps[j + 1] >> 16, (short)ps[j + 1]);
        }

        byte byte0 = 7;
        int k = byte0 / 2;
        for(int i1 = 1; i1 < i; i1++)
        {
            g.drawOval((ps[i1] >> 16) - k, (short)ps[i1] - k, byte0, byte0);
        }

    }

    public void drawScroll(Graphics g)
    {
        try
        {
            if(g == null)
            {
                g = primary;
            }
            if(g == null)
            {
                return;
            }
            float f = info.scale;
            int k = sizeBar;
            int l = info.scaleX;
            int i1 = info.scaleY;
            int j1 = info.imW;
            int k1 = info.imH;
            Dimension dimension = info.getSize();
            int l1 = (int)((float)dimension.width / f);
            int i2 = (int)((float)dimension.height / f);
            if(l + l1 >= j1)
            {
                l = Math.max(0, j1 - l1);
                info.scaleX = l;
            }
            if((i1 + i2) - 1 >= k1)
            {
                i1 = Math.max(0, k1 - i2);
                info.scaleY = i1;
            }
            int j2 = dimension.width - k;
            int k2 = dimension.height - k;
            int l2 = Math.min((int)(((float)l1 / (float)j1) * (float)j2), j2);
            int i3 = Math.min((int)(((float)i2 / (float)k1) * (float)k2), k2);
            int j3 = (int)(((float)l / (float)j1) * (float)j2);
            int k3 = (int)(((float)i1 / (float)k1) * (float)k2);
            int ai[] = rS;
            g.setColor(cls[0]);
            for(int i = 0; i < 20; i += 4)
            {
                g.drawRect(ai[i], ai[i + 1], ai[i + 2], ai[i + 3]);
            }

            if(j3 > 0)
            {
                g.setColor(cls[2]);
                g.drawRect(ai[0] + 1, ai[1] + 1, j3 - 2, ai[3] - 2);
                g.setColor(cls[1]);
                g.fillRect(ai[0] + 2, ai[1] + 2, j3 - 2, ai[3] - 3);
            }
            g.setColor(cls[2]);
            g.drawRect(ai[0] + j3 + l2, ai[1] + 1, ai[2] - j3 - l2 - 1, ai[3] - 2);
            g.setColor(cls[1]);
            g.fillRect(ai[0] + 1 + j3 + l2, ai[1] + 2, ai[2] - j3 - l2 - 2, ai[3] - 3);
            g.setColor(cls[1]);
            if(k3 > 0)
            {
                g.setColor(cls[2]);
                g.drawRect(ai[4] + 1, ai[5] + 1, ai[6] - 2, k3 - 1);
                g.setColor(cls[1]);
                g.fillRect(ai[4] + 2, ai[5] + 2, ai[6] - 3, k3 - 1);
            }
            g.setColor(cls[2]);
            g.drawRect(ai[4] + 1, ai[5] + k3 + i3, ai[6] - 2, ai[7] - k3 - i3 - 1);
            g.setColor(cls[1]);
            g.fillRect(ai[4] + 2, ai[5] + k3 + i3, ai[6] - 3, ai[7] - k3 - i3 - 1);
            for(int j = 8; j < 20; j += 4)
            {
                for(int l3 = 0; l3 < 2; l3++)
                {
                    g.setColor(cls[2 - l3]);
                    if(l3 == 0)
                    {
                        g.drawRect(ai[j] + 1, ai[j + 1] + 1, ai[j + 2] - 2, ai[j + 3] - 2);
                    } else
                    {
                        g.fillRect(ai[j] + 2, ai[j + 1] + 2, ai[j + 2] - 3, ai[j + 3] - 3);
                    }
                }

                g.setColor(cls[3]);
                int i4 = ai[j + 2] / 2;
                int k4 = ai[j + 3] / 2;
                if(j == 16)
                {
                    int i5 = ai[j] + i4 / 2;
                    int k5 = ai[j + 1] + k4 / 2;
                    k4 /= 2;
                    g.drawRect(i5, k5, i4, k4);
                    g.fillRect(i5, k5 + k4, 1, k4);
                } else
                {
                    g.fillRect(ai[j] + i4 / 2, ai[j + 1] + k4, i4 + 1, 1);
                    if(j == 8)
                    {
                        g.fillRect(ai[j] + i4, ai[j + 1] + k4 / 2, 1, k4);
                    }
                }
            }

            int j4 = ai[0] + j3;
            int l4 = ai[1] + 1;
            int j5 = ai[4] + 1;
            int l5 = ai[5] + k3;
            g.setColor(cls[0]);
            g.drawRect(j4, l4, l2, ai[3] - 2);
            g.drawRect(j5, l5, ai[6] - 2, i3 + 1);
            g.setColor(cls[3]);
            g.fillRect(j4 + 2, l4 + 2, l2 - 3, ai[3] - 5);
            g.fillRect(j5 + 2, l5 + 2, ai[6] - 5, i3 - 2);
            g.setColor(cls[4]);
            g.fillRect(j4 + 1, l4 + 1, l2 - 2, 1);
            g.fillRect(j4 + 1, l4 + 2, 1, ai[3] - 5);
            g.fillRect(j5 + 1, l5 + 1, ai[6] - 4, 1);
            g.fillRect(j5 + 1, l5 + 2, 1, i3 - 2);
            g.setColor(cls[5]);
            g.fillRect((j4 + l2) - 1, l4 + 1, 1, ai[3] - 4);
            g.fillRect(j4 + 1, (l4 + ai[3]) - 3, l2 - 1, 1);
            g.fillRect((j5 + ai[6]) - 3, l5 + 1, 1, i3 - 1);
            g.fillRect(j5 + 1, l5 + i3, ai[6] - 3, 1);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void dRect(int i, int j, int k)
    {
        try
        {
            int ai[] = user.points;
            switch(i)
            {
            case 503: 
            case 504: 
            case 505: 
            default:
                break;

            case 501: 
                setM();
                p(0, j, k);
                m.memset(ai, 0);
                psCount = 1;
                break;

            case 506: 
                if(psCount == 1)
                {
                    int l = ai[0];
                    int i1 = ai[1];
                    int j1 = l >> 16;
                    short word0 = (short)l;
                    primary2.drawRect(j1, word0, (i1 >> 16) - j1 - 1, (short)i1 - word0 - 1);
                    p(1, j, k);
                    transRect();
                    l = ai[0];
                    i1 = ai[1];
                    j1 = l >> 16;
                    word0 = (short)l;
                    primary2.drawRect(j1, word0, (i1 >> 16) - j1 - 1, (short)i1 - word0 - 1);
                }
                break;

            case 502: 
                if(psCount > 0)
                {
                    p(1, j, k);
                    if(transRect())
                    {
                        ps[0] = ai[0];
                        ps[1] = ai[1];
                        m.setRetouch(ps, null, 0, true);
                        if(m.iPen != 20)
                        {
                            m.draw();
                        }
                        dEnd(false);
                    }
                }
                psCount = -1;
                break;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void dScroll(MouseEvent mouseevent, int i, int j)
    {
        if(i != 0 || j != 0)
        {
            scroll(i, j);
            return;
        }
        int k = mouseevent.getID();
        Point point = mouseevent.getPoint();
        if(k == 502 || k == 506)
        {
            scroll(isSpace ? poS.x - point.x : point.x - poS.x, isSpace ? poS.y - point.y : point.y - poS.y);
            poS = point;
        } else
        if(k == 501)
        {
            poS = point;
        }
    }

    public void dText(int i, int j, int k)
    {
        switch(i)
        {
        case 502: 
            setM();
            if(text == null)
            {
                text = new TextField(16);
                text.addActionListener(this);
                isText = true;
                getParent().add(text, 0);
            }
            if(!isText)
            {
                primary.setColor(new Color(mgInfo.iColor));
                primary.fillRect(j - 1, k - 1, mgInfo.iSize + 1, 1);
                primary.fillRect(j - 1, k, 1, mgInfo.iSize);
            } else
            {
                text.setFont(m.getFont((m.iSize * info.scale) / info.Q));
                text.setSize(text.getPreferredSize());
                Point point = getLocation();
                text.setLocation(j + point.x, k + point.y + 2);
                text.setVisible(true);
            }
            p(0, j, k);
            break;
        }
    }

    private void ePre()
    {
        dPre(oldX, oldY, false);
        oldX = -1000;
        oldY = -1000;
    }

    private final int getS()
    {
        try
        {
            if(tab == null || tab == this)
            {
                return 255;
            } else
            {
                return ((Integer)mGet.invoke(tab, null)).intValue();
            }
        }
        catch(Throwable _ex)
        {
            tab = this;
        }
        return 255;
    }

    private final boolean in(int i, int j)
    {
        Dimension dimension = getSize();
        return i >= 0 && j >= 0 && i < dimension.width && j < dimension.height;
    }

    public void init(Applet applet, Res res1, int i, int j, int k, int l, Cursor acursor[])
        throws IOException
    {
        String s = "color_";
        cursors = acursor;
        cls = new Color[6];
        cls[0] = new Color(res1.getP(s + "frame", 0x505078));
        cls[1] = new Color(res1.getP(s + "icon", 0xccccff));
        cls[2] = new Color(res1.getP(s + "bar_hl", 0xffffff));
        cls[3] = new Color(res1.getP(s + "bar", 0x6f6fae));
        cls[4] = new Color(res1.getP(s + "bar_hl", 0xeeeeff));
        cls[5] = new Color(res1.getP(s + "bar_shadow", 0xaaaaaa));
        setBackground(Color.white);
        m = new M();
        user = m.newUser(this);
        paintchat.M.Info info1 = m.newInfo(applet, this, res1);
        info1.setSize(i, j, k);
        info1.setL(l);
        info = info1;
        mgInfo = info1.m;
    }

    private final boolean isOKPo(int i, int j)
    {
        int k = ps[psCount - 1];
        return Math.max(Math.abs(i - (k >> 16)), Math.abs(j - (short)k)) >= info.scale;
    }

    public void m_paint(int i, int j, int k, int l)
    {
        Dimension dimension = info.getSize();
        if(m == null)
        {
            primary.setColor(Color.white);
            primary.fillRect(0, 0, dimension.width, dimension.height);
            return;
        }
        if(i == 0 && j == 0 && k == 0 && l == 0)
        {
            i = j = 0;
            k = dimension.width;
            l = dimension.height;
        } else
        {
            i = Math.max(i, 0);
            j = Math.max(j, 0);
            k = Math.min(k, dimension.width);
            l = Math.min(l, dimension.height);
        }
        m.m_paint(i, j, k, l);
    }

    public void m_paint(Rectangle rectangle)
    {
        if(rectangle == null)
        {
            m_paint(0, 0, 0, 0);
        } else
        {
            m_paint(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
    }

    private void mPaint(Graphics g)
    {
        if(g == null)
        {
            g = primary;
        }
        drawScroll(g);
        if(!super.isPaint)
        {
            return;
        } else
        {
            m_paint(g != primary ? g.getClipBounds() : null);
            return;
        }
    }

    private final void p(int i, int j, int k)
    {
        ps[i] = j << 16 | k & 0xffff;
    }

    public void paint2(Graphics g)
    {
        try
        {
            Rectangle rectangle = g.getClipBounds();
            int i = (info != null ? info.scale : 0) * 2;
            g.setClip(rectangle.x - i, rectangle.y - i, rectangle.width + i * 2, rectangle.height + i * 2);
            mPaint(g);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void pMouse(MouseEvent mouseevent)
    {
        try
        {
            int i = mouseevent.getID();
            int j = info.scale;
            int k = (mouseevent.getX() / j) * j;
            int l = (mouseevent.getY() / j) * j;
            boolean flag = i == 501;
            boolean flag1 = i == 506;
            boolean flag2 = isRight;
            if(mouseevent.isAltDown() && mouseevent.isControlDown())
            {
                if(psCount >= 0)
                {
                    reset();
                }
                if(flag)
                {
                    poS.y = l;
                    poS.x = mgInfo.iSize;
                    m_paint(null);
                }
                if(flag1)
                {
                    Dimension dimension = getSize();
                    int i1 = dimension.width / 2;
                    int j1 = dimension.height / 2;
                    int k1 = info.getPenSize(mgInfo) * info.scale;
                    m_paint(i1 - k1, j1 - k1, k1 * 2, k1 * 2);
                    imi.setLineSize((l - poS.y) / 4 + poS.x);
                    dPre(i1, j1, false);
                }
                return;
            }
            if(flag)
            {
                if(text == null || !text.isVisible())
                {
                    requestFocus();
                }
                flag2 = isRight = Awt.isR(mouseevent);
                if(!isDrag)
                {
                    dPre(oldX, oldY, false);
                }
                isDrag = true;
                if(b(k, l))
                {
                    return;
                }
                if(isText && text != null && text.isVisible())
                {
                    text.setVisible(false);
                }
            }
            if(i == 502)
            {
                if(!isDrag)
                {
                    return;
                }
                isRight = false;
                isDrag = false;
                super.isPaint = true;
                if(isScroll)
                {
                    isScroll = false;
                    imi.scroll(false, 0, 0);
                    if(info.scale < 1)
                    {
                        m_paint(null);
                    }
                    return;
                }
            }
            if(isRight && isDrag)
            {
                if(psCount >= 0)
                {
                    reset();
                    isRight = false;
                    isDrag = false;
                    super.isPaint = true;
                } else
                {
                    imi.setARGB(user.getPixel(k / info.scale + info.scaleX, l / info.scale + info.scaleY) & 0xffffff | info.m.iAlpha << 24);
                }
                return;
            }
            if(!isDrag)
            {
                cursor(i, k, l);
                switch(i)
                {
                default:
                    break;

                case 504: 
                    getS();
                    break;

                case 503: 
                    dPre(k, l, isIn);
                    isIn = true;
                    break;

                case 505: 
                    if(isIn)
                    {
                        isIn = false;
                        dPre(oldX, oldY, false);
                    }
                    break;
                }
            }
            if(isScroll)
            {
                dScroll(mouseevent, 0, 0);
                return;
            }
            if(isEnable && ((long)(mgInfo.iLayer + 1) & info.permission) != 0L && !flag2 && (mgInfo.iHint == 10 || info.layers[mgInfo.iLayer].iAlpha > 0.0F))
            {
                switch(mgInfo.iHint)
                {
                case 0: // '\0'
                case 11: // '\013'
                    dFLine(i, k, l);
                    break;

                case 1: // '\001'
                    dLine(i, k, l);
                    break;

                case 2: // '\002'
                    dBz(i, k, l);
                    break;

                case 8: // '\b'
                case 12: // '\f'
                    dText(i, k, l);
                    break;

                case 9: // '\t'
                    dCopy(i, k, l);
                    break;

                case 10: // '\n'
                    if(info.isClean && flag)
                    {
                        dClear();
                    }
                    break;

                case 7: // '\007'
                    if(flag && info.isFill)
                    {
                        m.set(info.m);
                        p(0, k, l);
                        p(1, k + 1024, l + 1024);
                        transRect();
                        m.setRetouch(user.points, null, 0, true);
                        m.draw();
                        send();
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                case 5: // '\005'
                case 6: // '\006'
                default:
                    dRect(i, k, l);
                    break;
                }
            }
            if(i == 502 && isIn)
            {
                dPre(k, l, false);
                isDrag = false;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private final void poll()
    {
        if(tab == null || tab == this)
        {
            return;
        }
        try
        {
            if(((Boolean)mPoll.invoke(tab, null)).booleanValue())
            {
                return;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        mgInfo.iSOB = 0;
    }

    protected void processKeyEvent(KeyEvent keyevent)
    {
        try
        {
            boolean flag = keyevent.isControlDown() || keyevent.isShiftDown();
            boolean flag1 = keyevent.isAltDown();
            boolean flag2 = true;
            switch(keyevent.getID())
            {
            default:
                break;

            case 401: 
                switch(keyevent.getKeyCode())
                {
                case 32: // ' '
                    isSpace = true;
                    break;

                case 39: // '\''
                    scroll(5, 0);
                    break;

                case 38: // '&'
                    scroll(0, -5);
                    break;

                case 40: // '('
                    scroll(0, 5);
                    break;

                case 37: // '%'
                    scroll(-5, 0);
                    break;

                case 107: // 'k'
                    scaleChange(1, false);
                    break;

                case 109: // 'm'
                    scaleChange(-1, false);
                    break;

                case 82: // 'R'
                case 89: // 'Y'
                    flag2 = false;
                    // fall through

                case 90: // 'Z'
                    if(flag1)
                    {
                        flag2 = false;
                    }
                    if(flag)
                    {
                        imi.undo(flag2);
                    }
                    break;

                case 66: // 'B'
                    imi.setARGB(mgInfo.iAlpha << 24 | mgInfo.iColor);
                    break;

                case 69: // 'E'
                    imi.setARGB(0xffffff);
                    break;
                }
                break;

            case 402: 
                switch(keyevent.getKeyCode())
                {
                case 32: // ' '
                    isSpace = false;
                    break;
                }
                break;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void reset()
    {
        if(psCount >= 0)
        {
            psCount = -1;
            switch(mgInfo.iHint)
            {
            case 0: // '\0'
            case 11: // '\013'
                m.reset(true);
                break;

            default:
                m.reset(false);
                m_paint(null);
                break;
            }
        }
    }

    public synchronized void resetGraphics()
    {
        if(primary != null)
        {
            primary.dispose();
        }
        if(primary2 != null)
        {
            primary2.dispose();
        }
        Dimension dimension = getSize();
        int i = dimension.width - sizeBar;
        int j = dimension.height - sizeBar;
        primary = getGraphics();
        if(primary != null)
        {
            primary.translate(getGapX(), getGapY());
            primary2 = primary.create(0, 0, i, j);
            primary2.setXORMode(Color.white);
            info.setComponent(this, primary, i, j);
        }
        int ai[] = rS;
        int k = sizeBar;
        dimension = info.getSize();
        for(int l = 0; l < 20; l++)
        {
            ai[l] = k;
        }

        ai[1] = dimension.height;
        ai[2] = dimension.width - k;
        ai[4] = dimension.width;
        ai[7] = dimension.height - k;
        ai[8] = 0;
        ai[9] = dimension.height;
        ai[12] = dimension.width;
        ai[13] = dimension.height;
        ai[16] = dimension.width;
        ai[17] = 0;
    }

    public void scaleChange(int i, boolean flag)
    {
        if(isIn)
        {
            ePre();
        }
        if(info.addScale(i, flag) && !super.isGUI)
        {
            float f = info.scale;
            int j = (int)((float)info.imW * f) + sizeBar;
            int k = (int)((float)info.imH * f) + sizeBar;
            Dimension dimension = getSize();
            int l = dimension.width;
            int i1 = dimension.height;
            if(j != dimension.width || k != dimension.height)
            {
                setSize(j, k);
            }
            dimension = getSize();
            if(dimension.width == l && dimension.height == i1)
            {
                mPaint(null);
            } else
            {
                imi.changeSize();
            }
        }
    }

    public void scroll(int i, int j)
    {
        if(info == null)
        {
            return;
        }
        Dimension dimension = info.getSize();
        int k = info.imW;
        int l = info.imH;
        float f = info.scale;
        int i1 = info.scaleX;
        int j1 = info.scaleY;
        float f1 = (float)i * ((float)k / (float)dimension.width);
        if(f < 1.0F)
        {
            f1 /= f;
        }
        if(f1 != 0.0F)
        {
            f1 = f1 < 0.0F || f1 > 1.0F ? f1 > 0.0F || f1 < -1F ? f1 : -1F : 1.0F;
        }
        int k1 = (int)f1;
        f1 = (float)j * ((float)l / (float)dimension.height);
        if(f1 != 0.0F)
        {
            f1 = f1 < 0.0F || f1 > 1.0F ? f1 > 0.0F || f1 < -1F ? f1 : -1F : 1.0F;
        }
        if(f < 1.0F)
        {
            f1 /= f;
        }
        int l1 = (int)f1;
        Graphics g = primary;
        info.scaleX = Math.max(i1 + k1, 0);
        info.scaleY = Math.max(j1 + l1, 0);
        drawScroll(g);
        poS.translate(i, j);
        int i2 = (int)((float)(info.scaleX - i1) * f);
        int j2 = (int)((float)(info.scaleY - j1) * f);
        k1 = dimension.width - Math.abs(i2);
        l1 = dimension.height - Math.abs(j2);
        try
        {
            g.copyArea(Math.max(i2, 0), Math.max(j2, 0), k1, l1, -i2, -j2);
            if(f >= 1.0F)
            {
                if(i2 != 0)
                {
                    if(i2 > 0)
                    {
                        m_paint(dimension.width - i2, 0, i2, dimension.height);
                    } else
                    {
                        m_paint(0, 0, -i2, dimension.height);
                    }
                }
                if(j2 != 0)
                {
                    if(j2 > 0)
                    {
                        m_paint(0, dimension.height - j2, dimension.width, j2);
                    } else
                    {
                        m_paint(0, 0, dimension.width, -j2);
                    }
                }
            } else
            {
                if(i2 != 0)
                {
                    if(i2 > 0)
                    {
                        g.clearRect(dimension.width - i2, 0, i2, dimension.height);
                    } else
                    {
                        g.clearRect(0, 0, -i2, dimension.height);
                    }
                }
                if(j2 != 0)
                {
                    if(i2 > 0)
                    {
                        g.clearRect(0, dimension.height - j2, dimension.width, j2);
                    } else
                    {
                        g.clearRect(0, 0, dimension.width, -j2);
                    }
                }
            }
            imi.scroll(true, i2, j2);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void send()
    {
        imi.send(m);
    }

    public void send(String s)
    {
        try
        {
            M m1 = new M(info, user);
            m1.set(s);
            m1.draw();
            send(m1);
        }
        catch(Throwable _ex) { }
    }

    public void send(M m1)
        throws InterruptedException
    {
        imi.send(m1);
    }

    public void setA()
    {
        m.iAlpha2 = (int)(info.layers[m.iLayer].iAlpha * 255F) << 8 | (int)(info.layers[m.iLayerSrc].iAlpha * 255F);
    }

    private final void setM()
    {
        m.set(mgInfo);
        if(m.iPen == 20)
        {
            m.iLayerSrc = m.iLayer;
        }
        setA();
    }

    public void setSize(Dimension dimension)
    {
        setSize(dimension.width, dimension.height);
    }

    public void setSize(int i, int j)
    {
        if(info == null)
        {
            super.setSize(i, j);
        } else
        {
            int k = info.imW * info.scale + sizeBar;
            int l = info.imH * info.scale + sizeBar;
            super.setSize(Math.min(i - getGapW(), k), Math.min(j - getGapW(), l));
        }
        repaint();
    }

    public final boolean transRect()
    {
        int i = ps[0];
        int j = i >> 16;
        short word0 = (short)i;
        i = ps[1];
        int k = i >> 16;
        short word1 = (short)i;
        int l = Math.max(Math.min(j, k), 0);
        int i1 = Math.min(Math.max(j, k), info.imW * info.scale);
        int j1 = Math.max(Math.min(word0, word1), 0);
        int k1 = Math.min(Math.max(word0, word1), info.imH * info.scale);
        if(i1 - l < info.scale || k1 - j1 < info.scale)
        {
            return false;
        } else
        {
            user.points[0] = l << 16 | j1 & 0xffff;
            user.points[1] = i1 << 16 | k1 & 0xffff;
            return true;
        }
    }

    public void up()
    {
        if(tab != null)
        {
            tab.repaint();
        }
    }
}
