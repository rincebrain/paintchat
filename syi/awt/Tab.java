package syi.awt;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import paintchat.M;

// Referenced classes of package syi.awt:
//            LComponent, Awt

public class Tab extends LComponent
{

    private M mg;
    private int iDrag;
    private int sizeBar;
    private int max;
    private int strange;
    private Object tab;
    private Method mGet;
    private Method mPoll;
    private Method mEx;
    private byte iSOB;
    private final String STR[] = {
        "alpha", "size"
    };

    public Tab(Container container, paintchat.M.Info info)
        throws Throwable
    {
        iDrag = -1;
        max = 0;
        try
        {
            mg = info.m;
            int i = sizeBar = (int)(16F * LComponent.Q);
            Dimension dimension = getSize();
            dimension.setSize(i * 4, 8 + i * 6);
            setDimension(dimension, dimension, dimension);
            Class class1 = Class.forName("cello.tablet.JTablet");
            tab = class1.newInstance();
            mGet = class1.getMethod("getPressure", null);
            mPoll = class1.getMethod("poll", null);
            mEx = class1.getMethod("getPressureExtent", null);
            setTitle("tablet");
            container.add(this, 0);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private int at(int i)
    {
        if(i > getSize().height / 2)
        {
            i -= 5;
        }
        return i / sizeBar;
    }

    private void dBar(Graphics g, int i)
    {
        Dimension dimension = getSize();
        int j = sizeBar;
        int k = i * (dimension.height / 2) + j;
        float f = (float)(dimension.width - 6) / 255F;
        for(int i1 = 0; i1 < 2; i1++)
        {
            int j1 = i != 0 ? mg.iSS : mg.iSA;
            g.setColor(super.clFrame);
            g.drawRect(2, k, (int)(f * 255F), j);
            int l = (int)((float)(j1 >>> i1 * 8 & 0xff) * f);
            g.setColor(getForeground());
            g.fillRect(3, k + 1, l, j - 1);
            g.setColor(getBackground());
            g.fillRect(l + 3, k + 1, dimension.width - l - 7, j - 1);
            k += j;
        }

    }

    private void drag(int i)
    {
        int j = iDrag;
        if(j <= 0 || j == 3)
        {
            return;
        }
        boolean flag = j >= 3;
        if((iSOB & 1 << (flag ? 1 : 0)) == 0)
        {
            return;
        }
        i = (int)((255F / (float)getSize().width) * (float)i);
        i = i > 0 ? i < 255 ? i : 255 : 0;
        if(flag)
        {
            int k = (j - 4) * 8;
            mg.iSS = mg.iSS & 255 << 8 - k | i << k;
        } else
        {
            int l = (j - 1) * 8;
            mg.iSA = mg.iSA & 255 << 8 - l | i << l;
        }
        Graphics g = getG();
        dBar(g, j >= 3 ? 1 : 0);
        g.dispose();
    }

    public void paint2(Graphics g)
    {
        try
        {
            int i = sizeBar;
            Dimension dimension = getSize();
            int _tmp = mg.iSS;
            int k = dimension.width - 1;
            int l = k - 6;
            int i1 = i * 3 + 4;
            float _tmp1 = (float)l / 255F;
            for(int j = 0; j < 2; j++)
            {
                boolean flag = (iSOB & j + 1) != 0;
                int j1 = i1 * j;
                Awt.fillFrame(g, !flag, 0, j1, k, i1);
                Awt.fillFrame(g, flag, 0, j1, i, i);
                g.setColor(getForeground());
                g.drawString(STR[j] + '.' + (flag ? "On" : "Off"), i + 2, (j1 + i) - 2);
                dBar(g, j);
                int _tmp2 = mg.iSA;
            }

        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void pMouse(MouseEvent mouseevent)
    {
        int i = mouseevent.getX();
        int j = mouseevent.getY();
        int k = sizeBar;
        switch(mouseevent.getID())
        {
        case 503: 
        case 504: 
        case 505: 
        default:
            break;

        case 501: 
            if(iDrag >= 0)
            {
                break;
            }
            int l = at(j);
            iDrag = l;
            if((l == 0 || l == 3) && i <= k)
            {
                iSOB ^= 1 << (l != 0 ? 1 : 0);
                repaint();
            } else
            {
                drag(i);
            }
            break;

        case 502: 
            iDrag = -1;
            break;

        case 506: 
            drag(i);
            break;
        }
    }

    public final boolean poll()
    {
        if(iSOB == 0)
        {
            return false;
        }
        try
        {
            if(((Boolean)mPoll.invoke(tab, null)).booleanValue())
            {
                mg.iSOB = iSOB;
                if(max <= 0)
                {
                    max = ((Integer)mEx.invoke(tab, null)).intValue();
                    if(max != 0)
                    {
                        mEx = null;
                    }
                }
                return true;
            }
        }
        catch(Throwable _ex) { }
        return false;
    }

    public final int strange()
    {
        try
        {
            if(poll())
            {
                strange = (int)(((float)((Integer)mGet.invoke(tab, null)).intValue() / (float)max) * 255F);
            } else
            {
                strange = 0;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return strange;
    }
}
