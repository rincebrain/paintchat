package paintchat.pro;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import paintchat.Res;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.awt.LComponent;

public class TBar extends LComponent
{

    private boolean isOption;
    private Res res;
    private Res config;
    private LComponent cs[];
    private String strs[];
    private boolean flags[];
    private Mi mi;
    private URL codebase;
    private String strAuthor;
    private int W;
    private int H;
    private Image image;
    private Color cls[][];
    private Color clT;

    public TBar(Res res1, Res res2, LComponent alcomponent[])
    {
        image = null;
        cls = new Color[2][3];
        res = res2;
        config = res1;
        cs = alcomponent;
        H = (int)(19F * LComponent.Q);
    }

    public final void drawFrame(Graphics g, boolean flag, int i, int j, int k, int l)
    {
        Color acolor[] = cls[flag ? 1 : 0];
        Awt.drawFrame(g, flag, i, j, k, l, acolor[2], acolor[1]);
    }

    public final void fillFrame(Graphics g, boolean flag, int i, int j, int k, int l)
    {
        drawFrame(g, flag, i, j, k, l);
        Color acolor[] = cls[flag ? 1 : 0];
        Awt.setup();
        Awt.fillFrame(g, flag, i, j, k, l, cls[0][0], cls[1][0], acolor[2], acolor[1]);
    }

    public void init()
    {
        super.isHide = false;
        int i = H;
        FontMetrics fontmetrics = getFontMetrics(getFont());
        int j = cs.length - 1;
        int k = 0;
        strs = new String[j];
        for(int l = 0; l < j; l++)
        {
            String s1 = res.res("window_" + l);
            k = Math.max(fontmetrics.stringWidth(s1) + s1.length(), k);
            strs[l] = s1;
            cs[l].setTitle(s1);
        }

        s();
        Color acolor[] = cls[0];
        cls[0] = cls[1];
        cls[1] = acolor;
        W = k;
        setDimension(new Dimension(W, i), new Dimension(W, i * j), new Dimension(W * j, i * j));
    }

    public void initOption(URL url, Mi mi1)
    {
        isOption = true;
        mi = mi1;
        codebase = url;
        byte byte0 = 2;
        strs = new String[byte0];
        flags = new boolean[byte0];
        strAuthor = res.res("option_author");
        s();
        setDimension(null, new Dimension(10, 10), null);
    }

    public void mouseO(MouseEvent mouseevent)
    {
        if(mouseevent.getID() != 501)
        {
            return;
        }
        int i = mouseevent.getY();
        if(i <= H)
        {
            boolean flag = !flags[0];
            mi.isGUI = flag;
            flags[0] = flag;
            repaint();
            mi.setVisible(false);
            mi.setDimension(null, mi.getSize(), getParent().getSize());
            mi.setVisible(true);
        } else
        if(i <= H * 2)
        {
            z();
            repaint();
        } else
        if(i > H * 3)
        {
            try
            {
                String s1 = config.getP("app_url", "http://shichan.jp/");
                if((s1.length() != 1 || s1.charAt(0) != '_') && mi.alert("kakunin_0", true))
                {
                    Applet applet = (Applet)getParent().getParent();
                    applet.getAppletContext().showDocument(new URL(applet.getCodeBase(), s1), "_blank");
                }
            }
            catch(Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
    }

    public void paint2(Graphics g)
    {
        getSize();
        if(isOption)
        {
            paintO(g);
        } else
        {
            paintBar(g);
        }
    }

    private void paintBar(Graphics g)
    {
        int i = 0;
        int j = 0;
        int k = 0;
        Dimension dimension = getSize();
        int l = W;
        int i1 = H;
        for(int j1 = 0; j1 < cs.length; j1++)
        {
            if(cs[j1] != null && cs[j1] != this)
            {
                fillFrame(g, !cs[j1].isVisible(), i, j, l, i1);
                g.setColor(clT != null ? clT : Awt.cFore);
                g.drawString(strs[k++], i + 2, (j + i1) - 3);
                if((i += l) + l > dimension.width)
                {
                    i = 0;
                    j += i1;
                }
            }
        }

    }

    private void paintO(Graphics g)
    {
        if(image == null)
        {
            try
            {
                FontMetrics fontmetrics = getFontMetrics(getFont());
                int k = strs.length;
                int l = 0;
                for(int j1 = 0; j1 < k; j1++)
                {
                    String s1 = res.res("option_" + j1);
                    l = Math.max(fontmetrics.stringWidth(s1 + " OFF") + s1.length() + 3, l);
                    strs[j1] = s1;
                }

                l = Math.max(fontmetrics.stringWidth(strAuthor) + strAuthor.length(), l);
                image = getToolkit().createImage((byte[])config.getRes("bn.gif"));
                Awt.wait(image);
                int k1 = image.getWidth(null);
                int i2 = image.getHeight(null);
                if(LComponent.Q > 1.0F)
                {
                    k1 = (int)((float)k1 * LComponent.Q);
                    i2 = (int)((float)i2 * LComponent.Q);
                }
                W = Math.max(l, k1);
                H = fontmetrics.getHeight() + 2;
                Dimension dimension1 = new Dimension(W, H * (k + 2) + i2 + 2);
                setDimension(new Dimension(W, H), dimension1, dimension1);
            }
            catch(RuntimeException _ex) { }
        }
        int i = image.getWidth(null);
        int j = image.getHeight(null);
        if(LComponent.Q > 1.0F)
        {
            i = (int)((float)i * LComponent.Q);
            j = (int)((float)j * LComponent.Q);
        }
        Dimension dimension = getSize();
        g.setFont(getFont());
        int i1 = 0;
        int l1 = 0;
        int j2 = W;
        int k2 = H;
        int l2 = strs.length;
        for(int i3 = 0; i3 < l2; i3++)
        {
            fillFrame(g, flags[i3], i1, l1, j2, k2);
            g.setColor(clT != null ? clT : getForeground());
            g.drawString(strs[i3] + (flags[i3] ? " ON" : " OFF"), i1 + 2, (l1 + k2) - 3);
            l1 += k2;
        }

        l1 = dimension.height - H - j - 2;
        g.drawRect(i1, l1, j2 - 1, dimension.height - l1 - 1);
        g.drawImage(image, (dimension.width - i) / 2, l1 + 2, i, j, getBackground(), null);
        g.drawString(strAuthor, (dimension.width - g.getFontMetrics().stringWidth(strAuthor)) / 2, dimension.height - 2);
    }

    public void pMouse(MouseEvent mouseevent)
    {
        if(isOption)
        {
            mouseO(mouseevent);
            return;
        }
        int i = mouseevent.getID();
        int j = W;
        int k = H;
        Dimension dimension = getSize();
        if(i == 504)
        {
            repaint();
        }
        if(i == 501)
        {
            int l = (dimension.width / j) * (mouseevent.getY() / k) + mouseevent.getX() / j;
            if(l >= cs.length || cs[l] == this)
            {
                return;
            }
            cs[l].setVisible(!cs[l].isVisible());
            invalidate();
            repaint();
        }
    }

    private void s()
    {
        String s1 = "pro_menu_color_off";
        for(int i = 0; i < 2; i++)
        {
            Color acolor[] = cls[i];
            if(config.getP(s1) != null)
            {
                acolor[0] = new Color(config.getP(s1, 0));
            }
            if(config.getP(s1 + "_hl") != null)
            {
                acolor[1] = new Color(config.getP(s1 + "_hl", 0));
            }
            if(config.getP(s1 + "_dk") != null)
            {
                acolor[2] = new Color(config.getP(s1 + "_dk", 0));
            }
            s1 = "pro_menu_color_on";
        }

        if(config.getP("pro_menu_color_text") != null)
        {
            clT = new Color(config.getP("pro_menu_color_text", 0));
        }
    }

    public void z()
    {
        boolean flag = !flags[1];
        flags[1] = flag;
        for(int i = 0; i < cs.length; i++)
        {
            if(cs[i] != null)
            {
                cs[i].isUpDown = flag;
            }
        }

    }
}
