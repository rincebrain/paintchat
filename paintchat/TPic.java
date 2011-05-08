package paintchat;

import java.awt.*;
import java.awt.event.MouseEvent;
import syi.awt.Awt;
import syi.awt.LComponent;

// Referenced classes of package paintchat:
//            SW, M, Res, ToolBox

public class TPic extends LComponent
    implements SW
{

    private ToolBox ts;
    private M.Info info;
    private M.User user;
    private M mg;
    private Res r_conf;
    private int iDrag;
    private int lastMask;
    private Color cls[];
    private static float fhsb[] = new float[3];

    public TPic()
    {
        lastMask = -1;
    }

    private Image cMk()
    {
        int ai[] = user.getBuffer();
        int i = 64;
        int l = 0;
        float f1 = 0.0F;
        float f3 = 0.0F;
        float f4 = 1.0F / (float)i;
        float f = fhsb[0];
        for(int k = 0; k < i; k++)
        {
            float f2 = 1.0F;
            for(int j = 0; j < i; j++)
            {
                ai[l++] = Color.HSBtoRGB(f, f2 -= f4, f3);
            }

            f3 += f4;
        }

        return user.mkImage(i, i);
    }

    private Image cMkB()
    {
        int ai[] = user.getBuffer();
        int i = (int)(64F * LComponent.Q);
        int j = (int)(22F * LComponent.Q);
        int ai1[] = ai;
        int k = 0;
        float f = 0.0F;
        float f1 = 1.0F / (float)i;
        for(int j1 = 0; j1 < i; j1++)
        {
            int l = Color.HSBtoRGB(f, 1.0F, 1.0F);
            for(int i1 = 0; i1 < j; i1++)
            {
                ai1[k++] = l;
            }

            f += f1;
        }

        return user.mkImage(j, i);
    }

    private int getRGB()
    {
        return Color.HSBtoRGB(fhsb[0], fhsb[1], fhsb[2]) & 0xffffff;
    }

    public void lift()
    {
    }

    public void mPack()
    {
        inParent();
    }

    public void mPaint()
    {
        try
        {
            Graphics g = getG();
            mPaint(g);
            g.dispose();
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
        }
    }

    public void mPaint(Graphics g)
    {
        Dimension dimension = getSize();
        int i = (int)(22F * LComponent.Q);
        int j = i;
        int k = 0;
        int l = 0;
        int i1 = dimension.width - j - 1;
        int j1 = dimension.height - i - 1;
        int ai[] = user.getBuffer();
        synchronized(ai)
        {
            Image image = cMk();
            g.drawImage(image, k, l, i1, j1, Color.white, null);
            image = cMkB();
            g.drawImage(image, i1 + 1, l, j, j1, Color.white, null);
            l += j1;
        }
        Awt.drawFrame(g, false, k, l + 1, i1, i);
        int k1 = (int)((float)(i1 - 8) * 0.7F);
        lastMask = mg.iColorMask;
        g.setColor(new Color(lastMask));
        g.fillRect(k + k1 + 6, l + 4, (int)((float)(i1 - 8) * 0.3F), i - 6);
        g.setColor(Color.getHSBColor(fhsb[0], fhsb[1], fhsb[2]));
        g.fillRect(k + 3, l + 4, k1, i - 6);
        g.setColor(Color.blue);
        g.setXORMode(Color.white);
        int l1 = Math.max((int)(10F * LComponent.Q), 2);
        int i2 = l1 >>> 1;
        g.setClip(i1 + 1, 0, j, j1);
        g.drawOval((i1 + 1 + j / 2) - i2, (int)((float)j1 * fhsb[0]) - i2, l1, l1);
        g.setClip(0, 0, i1, j1);
        g.drawOval((int)((float)i1 * (1.0F - fhsb[1])) - i2, (int)((float)j1 * fhsb[2]) - i2, l1, l1);
        g.setPaintMode();
        g.setClip(0, 0, dimension.width, dimension.height);
    }

    public void mSetup(ToolBox toolbox, M.Info info1, M.User user1, M m, Res res, Res res1)
    {
        ts = toolbox;
        info = info1;
        user = user1;
        mg = m;
        r_conf = res1;
        setTitle(res1.getP("window_4"));
        setDimension(new Dimension((int)(66F * LComponent.Q), (int)(66F * LComponent.Q)), new Dimension((int)(128F * LComponent.Q), (int)(128F * LComponent.Q)), new Dimension((int)(284F * LComponent.Q), (int)(284F * LComponent.Q)));
    }

    public void paint2(Graphics g)
    {
        mPaint(g);
    }

    public void pMouse(MouseEvent mouseevent)
    {
        int i = mouseevent.getX();
        int j = mouseevent.getY();
        int k = (int)(22F * LComponent.Q);
        int l = (int)(25F * LComponent.Q);
        boolean flag = false;
        Dimension dimension = getSize();
        int i1 = dimension.width - l - 1;
        int j1 = dimension.height - k - 1;
        if(mouseevent.getID() == 501 && j > j1)
        {
            int k1 = (int)((float)(i1 - 8) * 0.7F);
            if(i > k1)
            {
                mg.iColorMask = mg.iColor;
                ts.up();
            }
            return;
        }
        i = i > 0 ? i < i1 ? i : i1 : 0;
        j = j > 0 ? j < j1 ? j : j1 : 0;
        switch(mouseevent.getID())
        {
        case 501: 
            iDrag = i >= i1 ? 1 : 0;
            flag = true;
            break;

        case 506: 
            flag = iDrag >= 0;
            break;

        case 502: 
            if(iDrag >= 0)
            {
                flag = true;
                iDrag = -1;
            }
            break;
        }
        if(flag && iDrag >= 0)
        {
            if(iDrag == 0)
            {
                fhsb[1] = 1.0F - (float)i / (float)i1;
                fhsb[2] = (float)j / (float)j1;
            } else
            {
                fhsb[0] = (float)j / (float)j1;
            }
            ts.setARGB(mg.iAlpha << 24 | getRGB());
            mPaint();
        }
    }

    public void setColor(int i)
    {
        Color.RGBtoHSB(i >>> 16 & 0xff, i >>> 8 & 0xff, i & 0xff, fhsb);
        mPaint();
    }

    public void up()
    {
        int i = getRGB();
        int j = mg.iColor;
        if(i != j || lastMask != mg.iColorMask)
        {
            setColor(j);
        }
    }

}
