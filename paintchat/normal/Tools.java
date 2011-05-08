package paintchat.normal;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.EventObject;
import java.util.Hashtable;
import paintchat.*;
import paintchat_client.L;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.awt.LComponent;

// Referenced classes of package paintchat.normal:
//            ToolList

public class Tools extends LComponent
    implements ToolBox, ActionListener
{

    Applet applet;
    Container parent;
    Mi mi;
    L L;
    paintchat.M.Info info;
    M mg;
    Res res;
    Res config;
    private Graphics primary;
    private Font fDef;
    private Font fIg;
    ToolList list[];
    private Rectangle rPaint;
    private Rectangle rects[];
    private int fit_w;
    private int fit_h;
    private int nowButton;
    private int nowColor;
    private int oldPen;
    Color clFrame;
    Color clB;
    Color clBD;
    Color clBL;
    Color clText;
    Color clBar;
    Color clB2;
    Color clSel;
    private boolean isWest;
    private boolean isLarge;
    private PopupMenu popup;
    private LComponent cs[];
    private Window ws[];
    private static int DEFC[] = {
        0, 0xffffff, 0xb47575, 0x888888, 0xfa9696, 0xc096c0, 0xffb6ff, 0x8080ff, 0x25c7c9, 0xe7e58d, 
        0xe7962d, 0x99cb7b, 0xfcece2, 0xf9ddcf
    };
    private static int COLORS[];
    private static Color clRGB[][];
    private static Color clERGB[][];
    private char clValue[][] = {
        {
            'H', 'S', 'B', 'A'
        }, {
            'R', 'G', 'B', 'A'
        }
    };
    private boolean isRGB;
    private float fhsb[];
    private int iColor;
    protected Image imBack;
    private Graphics back;
    protected int W;
    protected int H;
    protected int IMW;
    protected int IMH;

    public Tools()
    {
        primary = null;
        rPaint = new Rectangle();
        rects = null;
        fit_w = -1;
        fit_h = -1;
        nowButton = -1;
        nowColor = -1;
        isWest = false;
        isRGB = true;
        fhsb = new float[3];
        imBack = null;
        setTitle("tools");
        super.isHide = false;
        super.iGap = 2;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        String s = actionevent.getActionCommand();
        Menu menu1 = (Menu)actionevent.getSource();
        int j = 0;
        int k = menu1.getItemCount();
        for(int l = 0; l < k; l++)
        {
            if(!menu1.getItem(l).getLabel().equals(s))
            {
                continue;
            }
            j = l;
            break;
        }

        switch(Integer.parseInt(menu1.getLabel()))
        {
        case 2: // '\002'
            mg.iPenM = j;
            setLineSize(0, mg.iSize);
            repaint();
            break;
        }
    }

    public void addC(Object obj)
    {
        if(obj instanceof LComponent)
        {
            if(cs != null)
            {
                for(int j = 0; j < cs.length; j++)
                {
                    if(cs[j] == obj)
                    {
                        return;
                    }
                }

            } else
            {
                cs = (new LComponent[] {
                    (LComponent)obj
                });
                return;
            }
            int k = cs.length;
            LComponent alcomponent[] = new LComponent[k + 1];
            System.arraycopy(cs, 0, alcomponent, 0, k);
            alcomponent[k] = (LComponent)obj;
            cs = alcomponent;
        } else
        {
            if(ws != null)
            {
                for(int l = 0; l < ws.length; l++)
                {
                    if(ws[l] == obj)
                    {
                        return;
                    }
                }

            } else
            {
                ws = (new Window[] {
                    (Window)obj
                });
                return;
            }
            int i1 = ws.length;
            Window awindow[] = new Window[i1 + 1];
            System.arraycopy(cs, 0, awindow, 0, i1);
            awindow[i1] = (Window)obj;
            ws = awindow;
        }
    }

    public Graphics getBack()
    {
        if(imBack == null)
        {
            synchronized(this)
            {
                if(imBack == null)
                {
                    try
                    {
                        int j = 0;
                        for(int k = 0; k < list.length; k++)
                        {
                            j = Math.max(j, list[k].r.height);
                        }

                        j = Math.max(j, 32) * 2;
                        imBack = createImage(W + 1, j + 1);
                        back = imBack.getGraphics();
                    }
                    catch(RuntimeException _ex)
                    {
                        imBack = null;
                        back = null;
                    }
                }
            }
        }
        if(back != null)
        {
            back.setFont(fDef);
        }
        return back;
    }

    public String getC()
    {
        try
        {
            int ai[] = COLORS != null ? COLORS : DEFC;
            StringBuffer stringbuffer = new StringBuffer();
            for(int j = 0; j < ai.length; j++)
            {
                if(j != 0)
                {
                    stringbuffer.append('\n');
                }
                stringbuffer.append("#" + Integer.toHexString(0xff000000 | ai[j] & 0xffffff).substring(2).toUpperCase());
            }

            return stringbuffer.toString();
        }
        catch(Throwable _ex)
        {
            return null;
        }
    }

    public LComponent[] getCs()
    {
        return cs;
    }

    public Dimension getCSize()
    {
        Dimension dimension = parent.getSize();
        return new Dimension(dimension.width - getSizeW().width - mi.getGapW(), dimension.height);
    }

    private int getRGB()
    {
        if(!isRGB)
        {
            return Color.HSBtoRGB((float)(iColor >>> 16 & 0xff) / 255F, (float)(iColor >>> 8 & 0xff) / 255F, (float)(iColor & 0xff) / 255F) & 0xffffff;
        } else
        {
            return iColor & 0xffffff;
        }
    }

    private int i(String s, int j)
    {
        return config.getP(s, j);
    }

    public boolean i(String s, boolean flag)
    {
        return config.getP(s, flag);
    }

    public void init(Container container, Applet applet1, Res res1, Res res2, Mi mi1)
    {
        applet = applet1;
        parent = container;
        res = res2;
        config = res1;
        info = mi1.info;
        mi = mi1;
        mg = info.m;
        W = i("tool_width", 48) + 4;
        H = i("tool_height", 470);
        for(int j = 0; j < DEFC.length; j += 2)
        {
            DEFC[j] = res1.getP("color_" + (j + 2), DEFC[j]);
            DEFC[j + 1] = res1.getP("color_" + (j + 1), DEFC[j + 1]);
        }

        System.arraycopy(DEFC, 0, COLORS, 0, 14);
        sCMode(false);
        String s = "tool_color_";
        setBackground(new Color(i(s + "bk", i(s + "back", 0x9999bb))));
        clB = new Color(i(s + "button", 0xffe8dfae));
        clB2 = new Color(i(s + "button" + '2', 0xf8daaa));
        clFrame = new Color(i(s + "frame", 0));
        clText = new Color(i(s + "text", 0x773333));
        clBar = new Color(i(s + "bar", 0xddddff));
        clSel = new Color(i(s + "iconselect", i("color_iconselect", 0xee3333)));
        clBL = new Color(i(s + "button" + "_hl", clB.brighter().getRGB()));
        clBD = new Color(i(s + "button" + "_dk", clB.darker().getRGB()));
        isWest = "left".equals(res1.getP("tool_align"));
        isLarge = res1.getP("icon_enlarge", true);
        container.getSize();
        setDimension(new Dimension(W, 42), new Dimension(W, H), new Dimension(W, (int)((float)H * 1.25F)));
        list[0].select();
        container.add(this, 0);
        addC(this);
        if(res1.getP("tool_layer", true))
        {
            L = new L(mi1, this, res, res1);
            L.setVisible(false);
            addC(L);
        }
    }

    private int isClick(int j, int k)
    {
        if(rects == null)
        {
            return -1;
        }
        int l = rects.length;
        int i1 = list.length;
        for(int j1 = 0; j1 < i1; j1++)
        {
            if(list[j1].r.contains(j, k))
            {
                return j1;
            }
        }

        for(int k1 = 0; k1 < l; k1++)
        {
            Rectangle rectangle = rects[k1];
            if(rectangle != null && rectangle.contains(j, k))
            {
                return k1 + i1;
            }
        }

        return -1;
    }

    public void lift()
    {
        int _tmp = nowButton;
        unSelect();
        nowButton = -1;
        repaint();
    }

    private void makeList()
    {
        Image image = null;
        int j = 0;
        int k = 0;
        clB.brighter();
        clB.darker();
        try
        {
            String s = "res/s.gif";
            Image image1 = getToolkit().createImage((byte[])config.getRes(s));
            Awt.wait(image1);
            image = image1;
            config.remove(s);
            int i1 = i("tool_icon_count", 7);
            list = new ToolList[i1];
            j = image1.getWidth(null) / i1;
            k = i("tool_icon_height", image1.getHeight(null) / 9);
            IMW = j;
            IMH = k;
        }
        catch(RuntimeException _ex) { }
        for(int l = 0; l < list.length; l++)
        {
            ToolList toollist;
            list[l] = toollist = new ToolList();
            toollist.init(this, res, config, mg, list, l);
            toollist.setImage(image, j, k, l);
        }

    }

    private void menu(int j, int k, int l)
    {
        if(popup == null)
        {
            popup = new PopupMenu(String.valueOf(l));
            popup.addActionListener(this);
        } else
        {
            remove(popup);
            popup.removeAll();
            popup.setLabel(String.valueOf(l));
        }
label0:
        switch(l)
        {
        default:
            break;

        case 0: // '\0'
            for(int i1 = 0; i1 < 11; i1++)
            {
                String s = String.valueOf(i1 != 0 ? i1 * 10 : 5) + '%';
                if(mg.iTT - 1 == i1)
                {
                    popup.add(new CheckboxMenuItem(s, true));
                } else
                {
                    popup.add(s);
                }
            }

            break;

        case 1: // '\001'
            int j1 = config.getInt("tt_size");
            for(int k1 = 0; k1 < j1; k1++)
            {
                String s1 = res.res("t042" + k1);
                if(mg.iTT - 12 == k1)
                {
                    popup.add(new CheckboxMenuItem(s1, true));
                } else
                {
                    popup.add(s1);
                }
            }

            break;

        case 2: // '\002'
            for(int l1 = 0; l1 < 16; l1++)
            {
                String s2 = (String)res.get("penm_" + l1);
                if(s2 == null)
                {
                    break label0;
                }
                if(mg.iPenM == l1)
                {
                    popup.add(new CheckboxMenuItem(s2, true));
                } else
                {
                    popup.add(s2);
                }
            }

            break;
        }
        add(popup);
        popup.show(this, j, k);
    }

    public void mPaint(int j)
    {
        Rectangle rectangle;
        if(j == -1)
        {
            rectangle = rPaint;
            rectangle.setSize(getSize());
            rectangle.setLocation(0, 0);
        } else
        if(j < list.length)
        {
            rectangle = rPaint;
            rectangle.setBounds(list[j].r);
        } else
        {
            rectangle = rects[j - list.length];
        }
        mPaint(primary(), rectangle);
    }

    public void mPaint(int j, int k, int l, int i1)
    {
        Rectangle rectangle = rPaint;
        rectangle.setBounds(j, k, l, i1);
        mPaint(primary(), rectangle);
    }

    private void mPaint(Graphics g, Rectangle rectangle)
    {
        if(rects == null || g == null || list == null)
        {
            return;
        }
        Graphics g1 = getBack();
        if(rectangle == null)
        {
            rectangle = g.getClipBounds();
            if(rectangle == null || rectangle.isEmpty())
            {
                rectangle = new Rectangle(getSize());
            }
        }
        if(rectangle.isEmpty())
        {
            return;
        }
        int i1 = list.length;
        Dimension dimension = getSize();
        g1.setFont(fDef);
        for(int j = 0; j < i1; j++)
        {
            if(list[j].r.intersects(rectangle))
            {
                list[j].paint(g, g1);
            }
        }

        g1.setFont(fIg);
        int j1 = isRGB ? 1 : 0;
        for(int k = 0; k < rects.length; k++)
        {
            Rectangle rectangle1 = rects[k];
            int l = k + i1;
            if(rectangle1.intersects(rectangle))
            {
                if(k < 14)
                {
                    Color color = new Color(COLORS[k]);
                    g1.setColor(k != nowColor ? color.brighter() : color.darker());
                    g1.drawRect(1, 1, rectangle1.width - 2, rectangle1.height - 2);
                    g1.setColor(color);
                    g1.fillRect(2, 2, rectangle1.width - 3, rectangle1.height - 3);
                    g1.setColor(nowColor != k ? clFrame : clSel);
                } else
                {
                    switch(k)
                    {
                    default:
                        break;

                    case 14: // '\016'
                    case 15: // '\017'
                    case 16: // '\020'
                    case 17: // '\021'
                        int k1 = k - 14;
                        int l1 = rectangle1.height;
                        int i2 = k != 17 ? iColor >>> (2 - k1) * 8 & 0xff : mg.iAlpha;
                        int k2 = (int)(((float)(dimension.width - 10 - 2) / 255F) * (float)i2);
                        g1.setColor(clB2);
                        g1.fillRect(0, 0, 5, l1 - 1);
                        g1.fillRect(rectangle1.width - 5, 1, 5, l1 - 1);
                        g1.setColor(clFrame);
                        g1.fillRect(5, 1, 1, l1 - 1);
                        g1.fillRect(rectangle1.width - 5 - 1, 1, 1, l1 - 1);
                        if(k2 > 0)
                        {
                            g1.setColor(clRGB[j1][k1]);
                            g1.fillRect(6, 1, k2, rectangle1.height - 2);
                        }
                        int i3 = rectangle1.width - 10 - k2 - 2;
                        if(i3 > 0)
                        {
                            g1.setColor(clBar);
                            g1.fillRect(k2 + 5 + 1, 1, i3, rectangle1.height - 2);
                            g1.setColor(clERGB[j1][k1]);
                            g1.fillRect(k2 + 5 + 1, 1, 1, rectangle1.height - 2);
                        }
                        g1.setColor(clText);
                        g1.drawString(String.valueOf(clValue[j1][k1]) + i2, 8, rectangle1.height - 2);
                        break;

                    case 18: // '\022'
                        boolean flag = mg.isText();
                        Color color1 = new Color(getRGB());
                        int j2 = flag ? 255 : info.getPMMax();
                        int l2 = rectangle1.width - 10;
                        int j3 = rectangle1.height - 2;
                        g1.setColor(clB2);
                        g1.fillRect(1, 1, rectangle1.width - 2, j3);
                        if(mg.iSize >= j2)
                        {
                            mg.iSize = j2 - 1;
                        }
                        g1.setColor(color1);
                        g1.fillRect(1, 1, l2, (int)(((float)(mg.iSize + 1) / (float)j2) * (float)j3));
                        if(info.getPenMask() == null)
                        {
                            return;
                        }
                        g1.setColor(clText);
                        g1.drawString(String.valueOf(mg.iSize), 6, j3 - 1);
                        g1.setColor(clFrame);
                        g1.fillRect(l2, 1, 1, j3);
                        g1.fillRect(l2 + 1, j3 / 2, 8, 1);
                        g1.setColor(color1);
                        for(int k3 = 3; k3 >= 1; k3--)
                        {
                            g1.fillRect(rectangle1.width - 5 - k3, k3 + 2, k3 << 1, 1);
                            g1.fillRect(rectangle1.width - 5 - k3, j3 - 2 - k3, k3 << 1, 1);
                        }

                        g1.fillRect(rectangle1.width - 6, 5, 2, 8);
                        g1.fillRect(rectangle1.width - 6, j3 - 11, 2, 8);
                        break;

                    case 19: // '\023'
                        g1.setColor(clBar);
                        g1.fillRect(1, 1, rectangle1.width - 1, rectangle1.height - 2);
                        if(info.layers == null || info.layers.length <= mg.iLayer)
                        {
                            break;
                        }
                        LO lo = info.layers[mg.iLayer];
                        g1.setColor(clText);
                        if(lo.name != null)
                        {
                            g1.drawString(lo.name, 2, rectangle1.height - g1.getFontMetrics().getMaxDescent() - 1);
                        }
                        if(lo.iAlpha == 0.0F)
                        {
                            g1.setColor(Color.red);
                            g1.drawLine(1, 1, rectangle1.width - 3, rectangle1.height - 3);
                        }
                        break;
                    }
                    g1.setColor(nowButton != l ? clFrame : clSel);
                }
                g1.drawRect(0, 0, rectangle1.width - 1, rectangle1.height - 1);
                g.drawImage(imBack, rectangle1.x, rectangle1.y, rectangle1.x + rectangle1.width, rectangle1.y + rectangle1.height, 0, 0, rectangle1.width, rectangle1.height, Color.white, null);
            }
        }

    }

    private void mPress(MouseEvent mouseevent)
    {
        int j = mouseevent.getX();
        int k = mouseevent.getY();
        int l = isClick(j, k);
        int i1 = l;
        boolean flag = Awt.isR(mouseevent);
        nowButton = l;
        if(l < 0)
        {
            return;
        }
        if(l - list.length < 0)
        {
            if(flag)
            {
                if(list[l].isField && list[l].isMask)
                {
                    mg.iColorMask = mg.iColor;
                    mPaint(l);
                }
                nowButton = -1;
            }
            return;
        }
        l -= list.length;
        Rectangle rectangle = rects[l];
        if(l - 14 < 0)
        {
            if(flag)
            {
                COLORS[l] = mg.iColor;
            } else
            if(mouseevent.isShiftDown())
            {
                COLORS[l] = DEFC[l];
            } else
            {
                int _tmp = nowColor;
                nowColor = l;
                mg.iColor = COLORS[nowColor];
                selPix(false);
                toColor(mg.iColor);
            }
            up();
            return;
        }
        if((l -= 14) - 4 < 0)
        {
            int j1 = j > 5 ? ((int) (j < rectangle.width - 5 ? 0 : 1)) : -1;
            if(j1 != 0)
            {
                nowButton = -1;
                if(flag)
                {
                    j1 *= 5;
                }
            } else
            if(flag)
            {
                sCMode(isRGB);
                nowButton = -1;
                mPaint(-1);
                return;
            }
            setRGB(l, j1, j);
            return;
        }
        if((l -= 4) - 1 < 0)
        {
            if(flag)
            {
                nowButton = -1;
                menu(j, k, 2);
                return;
            }
            if(j >= (rectangle.x + rectangle.width) - 10)
            {
                setLineSize(0, Math.max(mg.iSize + (((rectangle.y + rectangle.height) - k) / 2 < 10 ? 1 : -1), 0));
                nowButton = -1;
            } else
            {
                setLineSize(k, -1);
            }
            mPaint(i1);
            return;
        }
        if(--l - 1 < 0)
        {
            int k1 = mg.iLayer;
            if(flag)
            {
                LO lo = info.layers[k1];
                lo.iAlpha = lo.iAlpha != 0.0F ? 0 : 1;
                mi.repaint();
            } else
            if(L != null && !L.isVisible())
            {
                if(L.getParent() == null)
                {
                    parent.add(L, 0);
                }
                L.setVisible(true);
            } else
            {
                if(++mg.iLayer >= info.L)
                {
                    mg.iLayer = 0;
                }
                mg.iLayerSrc = mg.iLayer;
            }
            if(L != null)
            {
                L.repaint();
            }
            mPaint(i1);
            return;
        } else
        {
            return;
        }
    }

    public void pack()
    {
        try
        {
            Container container = parent;
            Dimension dimension = container.getSize();
            setSize(Math.min(W, dimension.width), Math.min(H, dimension.height));
            if(L != null)
            {
                L.inParent();
            }
            Dimension dimension1 = getCSize();
            Dimension dimension2 = getSizeW();
            if(!((LComponent) (mi)).isGUI)
            {
                mi.setDimension(null, dimension1, dimension1);
                dimension1 = mi.getSizeW();
                mi.setLocation((dimension.width - dimension2.width - dimension1.width) / 2 + (isWest ? dimension2.width : 0), (dimension.height - dimension1.height) / 2);
            }
            dimension1 = mi.getSizeW();
            Point point = mi.getLocation();
            setLocation(isWest ? Math.max(0, point.x - dimension2.width - 10) : Math.min(point.x + dimension1.width + 10, dimension.width - dimension2.width), (dimension.height - dimension2.height) / 2);
            if(cs != null)
            {
                for(int j = 2; j < cs.length; j++)
                {
                    ((SW)cs[j]).mPack();
                }

            }
        }
        catch(Throwable _ex) { }
    }

    public void paint2(Graphics g)
    {
        try
        {
            g.setFont(fDef);
            mPaint(g, g.getClipBounds());
            if(primary != null)
            {
                primary.dispose();
                primary = null;
            }
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
            if(rects == null)
            {
                return;
            }
            int j = mouseevent.getX();
            int k = mouseevent.getY();
            boolean flag = Awt.isR(mouseevent);
            getSize();
            if(list != null)
            {
                for(int l = 0; l < list.length; l++)
                {
                    if(!list[l].isMask || !flag)
                    {
                        list[l].pMouse(mouseevent);
                    }
                }

            }
            int i1 = mouseevent.getID();
            switch(i1)
            {
            case 503: 
            case 504: 
            case 505: 
            default:
                break;

            case 501: 
                mPress(mouseevent);
                break;

            case 506: 
                int j1 = nowButton;
                if(j1 < list.length)
                {
                    return;
                }
                j1 -= list.length;
                if(j1 - 14 < 0)
                {
                    return;
                }
                if((j1 -= 14) - 4 < 0)
                {
                    setRGB(j1, 0, j);
                    mPaint(-1);
                    upCS();
                    return;
                }
                if((j1 -= 4) - 1 < 0)
                {
                    setLineSize(k, -1);
                    mPaint(nowButton);
                    return;
                }
                break;

            case 502: 
                nowButton = -1;
                break;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public Graphics primary()
    {
        if(primary == null)
        {
            try
            {
                primary = getGraphics();
                if(primary != null)
                {
                    primary.setFont(fDef);
                    primary.translate(getGapX(), getGapY());
                    Dimension dimension = getSize();
                    primary.setClip(0, 0, dimension.width, dimension.height);
                }
            }
            catch(RuntimeException _ex)
            {
                primary = null;
            }
        }
        return primary;
    }

    protected void processEvent(AWTEvent awtevent)
    {
        awtevent.getID();
        switch(awtevent.getID())
        {
        default:
            break;

        case 100: // 'd'
            int j = getLocation().x;
            int k = getParent().getSize().width / 2 - getSize().width / 2;
            if(j < k && !isWest)
            {
                isWest = true;
                pack();
            } else
            if(j >= k && isWest)
            {
                isWest = false;
                pack();
            }
            break;

        case 101: // 'e'
        case 102: // 'f'
            if(primary != null)
            {
                primary.dispose();
                primary = null;
            }
            break;
        }
        super.processEvent(awtevent);
    }

    private void sCMode(boolean flag)
    {
        isRGB = !flag;
        toColor(mg.iColor);
    }

    public void selPix(boolean flag)
    {
        if(list == null)
        {
            return;
        }
        int j = list.length;
        ToolList toollist1 = null;
        ToolList toollist2 = null;
        for(int k = 0; k < j; k++)
        {
            ToolList toollist = list[k];
            if(toollist.isEraser)
            {
                toollist2 = toollist;
            }
            if(toollist.isSelect)
            {
                toollist1 = toollist;
            }
        }

        if(flag)
        {
            if(toollist1 != toollist2)
            {
                unSelect();
                toollist2.select();
                mPaint(-1);
            }
        } else
        if(toollist1 == toollist2)
        {
            unSelect();
            list[oldPen].select();
            mPaint(-1);
        }
    }

    public void setARGB(int j)
    {
        mg.iAlpha = j >>> 24;
        j &= 0xffffff;
        mg.iColor = j;
        toColor(j);
        mPaint(-1);
        upCS();
    }

    public void setC(String s)
    {
        try
        {
            BufferedReader bufferedreader = new BufferedReader(new StringReader(s));
            int j = 0;
            while((s = bufferedreader.readLine()) != null && s.length() > 0) 
            {
                DEFC[j++] = Integer.decode(s).intValue();
            }
            System.arraycopy(DEFC, 0, COLORS, 0, COLORS.length);
            repaint();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void setLineSize(int j)
    {
        setLineSize(0, Math.max(j, 0));
        mPaint(list.length + 5);
    }

    public void setLineSize(int j, int k)
    {
        int l = info.getPMMax();
        Rectangle rectangle = rects[18];
        int _tmp = rectangle.height;
        if(k == -1)
        {
            k = (int)(((float)(j - rectangle.y) / (float)rectangle.height) * (float)l);
        }
        k = k > 0 ? k < l ? k : l - 1 : 0;
        mg.iSize = k;
    }

    private void setRGB(int j, int k, int l)
    {
        int j1 = j != 3 ? (2 - j) * 8 : 24;
        int k1 = mg.iAlpha << 24 | iColor;
        int i1;
        if(k != 0)
        {
            i1 = k1 >>> j1 & 0xff;
            i1 += k;
        } else
        {
            Rectangle rectangle = rects[14 + j];
            l = l - rectangle.x - 4;
            i1 = l <= 0 ? 0 : (int)(((float)l / (float)(W - 8)) * 255F);
        }
        i1 = i1 > 0 ? i1 < 255 ? i1 : 255 : 0;
        int l1 = 255 << j1;
        l1 = ~l1;
        k1 = k1 & l1 | i1 << j1;
        iColor = k1 & 0xffffff;
        mg.iColor = getRGB();
        mg.iAlpha = Math.max(k1 >>> 24, 1);
        mPaint(j);
        if(nowColor >= 0)
        {
            COLORS[nowColor] = mg.iColor;
        }
        mPaint(-1);
        upCS();
    }

    public void setSize(int j, int k)
    {
        if(applet == null)
        {
            super.setSize(j, k);
            return;
        }
        if(j == fit_w && k == fit_h)
        {
            return;
        }
        synchronized(this)
        {
            fit_w = j;
            fit_h = k;
            if(list == null)
            {
                makeList();
            }
            if(rects == null)
            {
                rects = new Rectangle[20];
                for(int l = 0; l < rects.length; l++)
                {
                    rects[l] = new Rectangle();
                }

            }
            Rectangle arectangle[] = rects;
            getSize();
            float _tmp = (float)j / (float)W;
            float f = (float)k / (float)H;
            int k1 = (int)((float)(IMH + 4) * f);
            if(!isLarge)
            {
                k1 = Math.min(IMH + 4, k1);
            }
            int l1 = Math.min((k - (k1 + 1) * list.length - (int)(16F * f * 4F) - (int)(33F * f) - 3) / 8, (j - 1) / 2);
            fIg = new Font("sansserif", 0, (int)((float)l1 * 0.475F));
            fDef = new Font("sansserif", 0, (int)((float)k1 * 0.43F));
            FontMetrics fontmetrics = getFontMetrics(fDef);
            int i2 = k1 - fontmetrics.getMaxDescent() - 2;
            int j2 = 0;
            int k2 = 0;
            for(int i1 = 0; i1 < list.length; i1++)
            {
                list[i1].r.setLocation(0, k2);
                list[i1].setSize(W, k1, i2);
                k2 += k1 + 1;
            }

            j2 = (j - 1) / 2;
            int j1;
            for(j1 = 0; j1 < 14; j1++)
            {
                Rectangle rectangle = arectangle[j1];
                rectangle.setBounds(j1 % 2 != 1 ? 0 : j2 + 1, k2, j1 % 2 != 1 ? j2 : j - j2 - 1, l1);
                if(j1 % 2 == 1)
                {
                    k2 += l1 + 1;
                }
            }

            j2 = (int)(15F * f);
            for(; j1 < 18; j1++)
            {
                Rectangle rectangle1 = arectangle[j1];
                rectangle1.setBounds(0, k2, j, j2);
                k2 += j2 + 1;
            }

            j2 = (int)(32F * f);
            Rectangle rectangle2 = arectangle[j1++];
            rectangle2.setBounds(0, k2, j, j2);
            k2 += j2 + 1;
            j2 = Math.min(k - k2, l1);
            rectangle2 = arectangle[j1++];
            rectangle2.setBounds(0, k - j2, j, j2);
            k2 += j2 + 1;
            super.setSize(j, k);
        }
    }

    public void showW(String s)
    {
        if(cs != null)
        {
            for(int j = 0; j < cs.length; j++)
            {
                if(cs[j].getClass().getName().equals(s))
                {
                    if(!cs[j].isVisible())
                    {
                        cs[j].setVisible(true);
                    }
                    return;
                }
            }

        }
        if(ws != null)
        {
            for(int k = 0; k < ws.length; k++)
            {
                if(ws[k].getClass().getName().equals(s))
                {
                    if(!ws[k].isVisible())
                    {
                        ws[k].setVisible(true);
                    }
                    return;
                }
            }

        }
        try
        {
            SW sw = (SW)Class.forName(s).newInstance();
            if(sw instanceof Window)
            {
                addC(sw);
                sw.mSetup(this, info, mi.user, mg, res, config);
            } else
            {
                LComponent lcomponent = (LComponent)sw;
                addC(lcomponent);
                lcomponent.setVisible(false);
                sw.mSetup(this, info, mi.user, mg, res, config);
                parent.add(lcomponent, 0);
                sw.mPack();
                lcomponent.setVisible(true);
            }
        }
        catch(Throwable throwable)
        {
            mi.alert(throwable.getLocalizedMessage(), false);
        }
    }

    private void toColor(int j)
    {
        if(!isRGB)
        {
            Color.RGBtoHSB(j >>> 16 & 0xff, j >>> 8 & 0xff, j & 0xff, fhsb);
            iColor = mg.iAlpha << 24 | (int)(fhsb[0] * 255F) << 16 | (int)(fhsb[1] * 255F) << 8 | (int)(fhsb[2] * 255F);
        } else
        {
            iColor = mg.iAlpha << 24 | j;
        }
    }

    public void unSelect()
    {
        for(int j = 0; j < list.length; j++)
        {
            ToolList toollist = list[j];
            if(toollist.isSelect)
            {
                if(!toollist.isEraser)
                {
                    oldPen = j;
                }
                toollist.unSelect();
            }
        }

    }

    public void up()
    {
        try
        {
            mPaint(-1);
            mi.up();
            upCS();
            if(L != null)
            {
                L.repaint();
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void upCS()
    {
        for(int j = 2; j < cs.length; j++)
        {
            ((SW)cs[j]).up();
        }

    }

    public void update(Graphics g)
    {
        paint(g);
    }

    static 
    {
        COLORS = new int[14];
        System.arraycopy(DEFC, 0, COLORS, 0, 14);
        clRGB = (new Color[][] {
            new Color[] {
                Color.magenta, Color.cyan, Color.white, Color.lightGray
            }, new Color[] {
                new Color(0xfa9696), new Color(0x82f238), new Color(0x8080ff), Color.lightGray
            }
        });
        clERGB = new Color[2][4];
        for(int j = 0; j < 2; j++)
        {
            for(int k = 0; k < 4; k++)
            {
                clERGB[j][k] = clRGB[j][k].darker();
            }

        }

    }
}
