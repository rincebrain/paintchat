package paintchat_client;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import paintchat.*;
import syi.awt.Awt;
import syi.awt.LComponent;

// Referenced classes of package paintchat_client:
//            Mi, Me

public class L extends LComponent
    implements ActionListener, ItemListener
{

    private Mi mi;
    private ToolBox tool;
    private Res res;
    private M m;
    private int B;
    private Font bFont;
    private int bH;
    private int bW;
    private int base;
    private int layer_size;
    private int mouse;
    private boolean isASlide;
    private int Y;
    private int YOFF;
    private PopupMenu popup;
    private String strMenu;
    private boolean is_pre;
    private boolean is_DIm;
    private Color cM;
    private Color cT;
    private String sL;

    public L(Mi mi1, ToolBox toolbox, Res res1, Res res2)
    {
        B = -1;
        layer_size = -1;
        mouse = -1;
        isASlide = false;
        popup = null;
        is_pre = true;
        is_DIm = false;
        tool = toolbox;
        bFont = Awt.getDefFont();
        bFont = new Font(bFont.getName(), 0, (int)((float)bFont.getSize() * 0.8F));
        FontMetrics fontmetrics = getFontMetrics(bFont);
        bH = fontmetrics.getHeight() + 6;
        base = bH - 2 - fontmetrics.getMaxDescent();
        int i = (int)(60F * LComponent.Q);
        String s = res1.res("Layer");
        sL = s;
        strMenu = res1.res("MenuLayer");
        cM = new Color(res2.getP("l_m_color", 0));
        cT = new Color(res2.getP("l_m_color_text", 0xffffff));
        fontmetrics = getFontMetrics(bFont);
        i = Math.max(fontmetrics.stringWidth(s + "00") + 4, i);
        i = Math.max(fontmetrics.stringWidth(strMenu) + 4, i);
        bW = i;
        i += bH + 100;
        mi = mi1;
        res = res1;
        setTitle(s);
        super.isGUI = true;
        m = mi1.info.m;
        Dimension dimension = new Dimension(bW, bH);
        setDimension(new Dimension(dimension), dimension, new Dimension());
        setSize(getMaximumSize());
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        try
        {
            String s = actionevent.getActionCommand();
            int i = popup.getItemCount();
            int j;
            for(j = 0; j < i; j++)
            {
                if(popup.getItem(j).getLabel().equals(s))
                {
                    break;
                }
            }

            paintchat.M.Info info = mi.info;
            M m1 = mg();
            setA(m1);
            LO alo[] = info.layers;
            int k = info.L;
            byte abyte0[] = new byte[4];
            boolean flag = false;
            boolean flag1 = false;
            int l = mi.user.wait;
            mi.user.wait = -2;
            if(popup.getName().charAt(0) == 'm')
            {
                switch(j)
                {
                case 0: // '\0'
                    m1.setRetouch(new int[] {
                        1, k + 1
                    }, null, 0, false);
                    flag = true;
                    flag1 = true;
                    break;

                case 1: // '\001'
                    if(info.L <= 1 || !confirm(alo[m1.iLayer].name + res.res("DelLayerQ")))
                    {
                        return;
                    }
                    m1.iLayerSrc = m1.iLayer;
                    m1.setRetouch(new int[] {
                        2
                    }, null, 0, false);
                    flag = true;
                    break;

                case 2: // '\002'
                    dFusion();
                    break;

                case 3: // '\003'
                    config(m.iLayer);
                    break;
                }
            } else
            if(j == 0)
            {
                m1.iHint = 14;
                m1.setRetouch(new int[] {
                    3
                }, null, 0, false);
                flag = true;
            } else
            {
                byte byte0 = (byte)alo[m1.iLayerSrc].iCopy;
                if(byte0 == 1)
                {
                    dFusion();
                } else
                {
                    m1.iHint = 3;
                    m1.iPen = 20;
                    abyte0[0] = byte0;
                    m1.setRetouch(new int[] {
                        0, info.W << 16 | info.H
                    }, abyte0, 4, false);
                    flag = true;
                }
            }
            if(flag)
            {
                m1.draw();
                if(flag1)
                {
                    info.layers[info.L - 1].makeName(sL);
                }
                mi.send(m1);
            }
            m.iLayerSrc = m.iLayer = Math.min(m.iLayer, info.L - 1);
            repaint();
            mi.user.wait = l;
            mi.repaint();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private int b(int i)
    {
        if(i < bH)
        {
            return 0;
        } else
        {
            return Math.max(mi.info.L - (i / bH - 1), 1);
        }
    }

    private void send(int ai[], byte abyte0[])
    {
        M m1 = mg();
        setA(m1);
        m1.setRetouch(ai, abyte0, abyte0 == null ? 0 : abyte0.length, false);
        int i = mi.user.wait;
        try
        {
            m1.draw();
            mi.send(m1);
        }
        catch(Throwable _ex) { }
        repaint();
        mi.user.wait = i;
        mi.repaint();
    }

    private void dFusion()
    {
        if(!confirm(res.res("CombineVQ")))
        {
            return;
        }
        try
        {
            int i = mi.info.L;
            LO alo[] = mi.info.layers;
            int j = 0;
            for(int k = 0; k < i; k++)
            {
                if(alo[k].iAlpha > 0.0F)
                {
                    j++;
                }
            }

            if(j <= 0)
            {
                return;
            }
            int l = mi.user.wait;
            M m1 = mg();
            setA(m1);
            byte abyte0[] = new byte[j * 4 + 2];
            int i1 = 2;
            abyte0[0] = (byte)j;
            for(int j1 = 0; j1 < i; j1++)
            {
                LO lo = alo[j1];
                if(lo.iAlpha > 0.0F)
                {
                    abyte0[i1++] = (byte)j1;
                    abyte0[i1++] = (byte)(int)(lo.iAlpha * 255F);
                    abyte0[i1++] = (byte)lo.iCopy;
                    abyte0[i1++] = 41;
                }
            }

            mi.user.wait = -2;
            m1.setRetouch(new int[] {
                7
            }, abyte0, abyte0.length, false);
            m1.draw();
            mi.send(m1);
            mi.user.wait = l;
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private boolean confirm(String s)
    {
        return Me.confirm(s, true);
    }

    private void dL(Graphics g, int i, int j)
    {
        if(mi.info.L <= j)
        {
            return;
        }
        getSize();
        int k = bW - 1;
        int l = bH - 2;
        Color color = m.iLayer != j ? super.clFrame : Awt.cFSel;
        LO lo = mi.info.layers[j];
        g.setColor(color);
        g.drawRect(0, i, k, l);
        g.setColor(Awt.cFore);
        g.setFont(bFont);
        g.drawString(lo.name, 2, i + base);
        g.setColor(color);
        g.drawRect(bW, i, 100, l);
        g.setColor(cM);
        int i1 = (int)(100F * lo.iAlpha);
        g.fillRect(bW + 1, i + 1, i1 - 1, l - 1);
        g.setColor(cT);
        g.drawString(i1 + "%", bW + 3, i + base);
        int j1 = bW + 100;
        g.setColor(color);
        g.drawRect(j1 + 1, i, l - 2, l);
        g.setColor(Awt.cFore);
        if(i1 == 0)
        {
            g.drawLine(j1 + 2, i + 1, (j1 + l) - 2, (i + l) - 1);
            g.drawLine(1, i + 1, k - 1, (i + bH) - 3);
        } else
        {
            g.drawOval(j1 + 2, i + 2, l - 3, l - 3);
        }
    }

    public Dimension getMaximumSize()
    {
        Dimension dimension = super.getMaximumSize();
        if(mi != null)
        {
            dimension.setSize(bW + 100 + bH, bH * (mi.info.L + 1));
        }
        return dimension;
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
        is_pre = !is_pre;
    }

    private M mg()
    {
        M m1 = new M(mi.info, mi.user);
        m1.iAlpha = 255;
        m1.iHint = 14;
        m1.iLayer = m.iLayer;
        m1.iLayerSrc = m.iLayerSrc;
        return m1;
    }

    private void p()
    {
        repaint();
        tool.up();
    }

    public void paint2(Graphics g)
    {
        try
        {
            int i = mi.info.L;
            for(int j = 0; j < i; j++)
            {
                LO lo = mi.info.layers[j];
                if(lo.name == null)
                {
                    lo.makeName(sL);
                }
            }

            if(layer_size != i)
            {
                layer_size = i;
                setSize(getMaximumSize());
                return;
            }
            Dimension dimension = getSize();
            int k = i - 1;
            int l = bH;
            g.setFont(bFont);
            g.setColor(Awt.cBk);
            g.fillRect(0, 0, dimension.width, dimension.height);
            for(; l < dimension.height; l += bH)
            {
                if(isASlide || k != mouse - 1)
                {
                    dL(g, l, k);
                }
                if(--k < 0)
                {
                    break;
                }
            }

            if(!isASlide && mouse > 0)
            {
                dL(g, Y - YOFF, mouse - 1);
            }
            Awt.drawFrame(g, mouse == 0, 0, 0, bW, bH - 2);
            g.setColor(Awt.cFore);
            g.drawString(strMenu, 3, bH - 6);
        }
        catch(Throwable _ex) { }
    }

    public void pMouse(MouseEvent mouseevent)
    {
        try
        {
            int i = Y = mouseevent.getY();
            int j = mouseevent.getX();
            paintchat.M.Info info = mi.info;
            boolean flag = Awt.isR(mouseevent);
            switch(mouseevent.getID())
            {
            case 504: 
            case 505: 
            default:
                break;

            case 501: 
                if(mouse >= 0)
                {
                    break;
                }
                int k = b(i);
                int l = k - 1;
                if(l >= 0)
                {
                    if(j > bW + 100 + 1)
                    {
                        int i1 = mi.user.wait;
                        mi.user.wait = -2;
                        if(flag)
                        {
                            for(int l1 = 0; l1 < info.L; l1++)
                            {
                                setAlpha(l1, l1 != l ? 0 : 255, true);
                            }

                        } else
                        {
                            setAlpha(l, info.layers[l].iAlpha != 0.0F ? 0 : 255, true);
                        }
                        mi.user.wait = i1;
                        mi.repaint();
                        p();
                        break;
                    }
                    if(mouseevent.getClickCount() >= 2 || flag)
                    {
                        config(l);
                        mi.repaint();
                        break;
                    }
                    isASlide = j >= bW;
                    mouse = k;
                    m.iLayer = m.iLayerSrc = l;
                    YOFF = i % bH;
                    if(isASlide)
                    {
                        setAlpha(l, (int)(((float)(j - bW) / 100F) * 255F), false);
                    } else
                    {
                        p();
                    }
                    break;
                }
                m.iLayerSrc = m.iLayer;
                if(j < bW && i > 2)
                {
                    popup(new String[] {
                        "AddLayer", "DelLayer", "CombineV", "PropertyLayer"
                    }, j, i, true);
                }
                break;

            case 506: 
                if(mouse <= 0)
                {
                    break;
                }
                if(isASlide)
                {
                    setAlpha(m.iLayer, (int)(((float)(j - bW) / 100F) * 255F), false);
                } else
                {
                    m.iLayer = b(Y) - 1;
                    repaint();
                }
                break;

            case 503: 
                int j1 = b(i) - 1;
                if(!is_pre || j1 < 0 || j >= bW)
                {
                    if(is_DIm)
                    {
                        is_DIm = false;
                        repaint();
                    }
                    return;
                }
                is_DIm = true;
                Dimension dimension = getSize();
                int j2 = mi.info.W;
                int k2 = mi.info.H;
                int ai[] = mi.info.layers[j1].offset;
                Graphics g = getG();
                int l2 = Math.min(dimension.width - bW - 1, dimension.height - 1);
                if(ai == null)
                {
                    g.setColor(Color.white);
                    g.fillRect(bW + 1, 1, l2 - 1, l2 - 1);
                } else
                {
                    Image image = getToolkit().createImage(new MemoryImageSource(j2, k2, new DirectColorModel(24, 0xff0000, 65280, 255), ai, 0, j2));
                    g.drawImage(image, bW + 1, 1, l2 - 1, l2 - 1, null);
                    image.flush();
                }
                g.setColor(Color.black);
                g.drawRect(bW, 0, l2, l2);
                g.dispose();
                break;

            case 502: 
                if(flag)
                {
                    break;
                }
                if(isASlide)
                {
                    setAlpha(m.iLayer, (int)(((float)(j - bW) / 100F) * 255F), true);
                    mouse = -1;
                    isASlide = false;
                    break;
                }
                int k1 = mouse - 1;
                int i2 = b(Y) - 1;
                if(k1 >= 0 && i2 >= 0 && k1 != i2)
                {
                    m.iLayer = i2;
                    m.iLayerSrc = k1;
                    popup(new String[] {
                        res.res("Shift"), res.res("Combine")
                    }, j, i, false);
                }
                mouse = -1;
                repaint();
                break;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void popup(String as[], int i, int j, boolean flag)
    {
        if(!mi.info.isLEdit)
        {
            return;
        }
        if(popup == null)
        {
            popup = new PopupMenu();
            popup.addActionListener(this);
            add(popup);
        } else
        {
            popup.removeAll();
        }
        for(int k = 0; k < as.length; k++)
        {
            popup.add(res.res(as[k]));
        }

        if(flag)
        {
            popup.addSeparator();
            CheckboxMenuItem checkboxmenuitem = new CheckboxMenuItem(res.res("IsPreview"), is_pre);
            checkboxmenuitem.addItemListener(this);
            popup.add(checkboxmenuitem);
            popup.setName("m");
        } else
        {
            popup.setName("l");
        }
        popup.show(this, i, j);
    }

    private void setA(M m1)
    {
        m1.iAlpha2 = (int)(mi.info.layers[m1.iLayer].iAlpha * 255F) << 8 | (int)(mi.info.layers[m1.iLayerSrc].iAlpha * 255F);
    }

    public void setAlpha(int i, int j, boolean flag)
        throws Throwable
    {
        j = j > 0 ? j < 255 ? j : 255 : 0;
        if((float)j == mi.info.layers[i].iAlpha)
        {
            return;
        }
        if(flag)
        {
            int k = m.iLayer;
            m.iLayer = i;
            send(new int[] {
                8
            }, new byte[] {
                (byte)j
            });
            m.iLayer = k;
        } else
        {
            mi.info.layers[i].iAlpha = (float)j / 255F;
            mi.repaint();
            repaint();
        }
    }

    public void config(int i)
    {
        LO lo = mi.info.layers[i];
        Choice choice = new Choice();
        choice.add(res.res("Normal"));
        choice.add(res.res("Multiply"));
        choice.add(res.res("Reverse"));
        choice.select(lo.iCopy);
        TextField textfield = new TextField(lo.name);
        Me me = Me.getMe();
        Panel panel = new Panel(new GridLayout(0, 1));
        panel.add(textfield);
        panel.add(choice);
        textfield.addActionListener(me);
        me.add(panel, "Center");
        me.pack();
        Awt.moveCenter(me);
        me.setVisible(true);
        if(me.isOk)
        {
            String s = textfield.getText();
            if(!s.equals(lo.name))
            {
                try
                {
                    send(new int[] {
                        10
                    }, s.getBytes("UTF8"));
                }
                catch(Throwable _ex) { }
            }
            int j = choice.getSelectedIndex();
            if(lo.iCopy != j)
            {
                send(new int[] {
                    9
                }, new byte[] {
                    (byte)j
                });
            }
            repaint();
        }
    }
}
