package paintchat.normal;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.Hashtable;
import paintchat.M;
import paintchat.Res;
import paintchat_client.Mi;

// Referenced classes of package paintchat.normal:
//            Tools

public class ToolList
{

    private Tools tools;
    private Res res;
    private Res cnf;
    boolean isField;
    boolean isClass;
    boolean isDirect;
    boolean isMask;
    boolean isEraser;
    boolean isSelect;
    boolean isDrawList;
    boolean isIm;
    String strField;
    private int quality;
    private boolean isDrag;
    public int iSelect;
    public int iSelectList;
    public boolean isList;
    private M info;
    private M mgs[];
    private int items[];
    private String strs[];
    private String strings[];
    private int length;
    private ToolList lists[];
    private Font font;
    private int base;
    private Image image;
    private int imW;
    private int imH;
    private int imIndex;
    public Rectangle r;

    public ToolList()
    {
        quality = 1;
        isDrag = false;
        info = null;
        mgs = null;
        items = null;
        strs = null;
        base = 0;
        r = new Rectangle();
    }

    private void dImage(Graphics g, Color color, int i, int j)
    {
        int k = r.height;
        int l = r.width;
        g.setColor(color);
        g.fillRect(2, i + 2, r.width - 4, k - 4);
        if(isMask)
        {
            g.setColor(new Color(info.iColorMask));
            g.fillRect(l - imW - 3, i + 3, imW, (k - 4) / 2);
        }
        if(!isIm || image == null || j >= image.getHeight(null) / imH)
        {
            return;
        } else
        {
            int i1 = imIndex * imW;
            int j1 = j * imH;
            int k1 = r.x + 2;
            int l1 = i + 2;
            g.drawImage(image, k1, l1, (k1 + l) - 4, (l1 + k) - 4, i1, j1, i1 + imW, j1 + imH, color, null);
            return;
        }
    }

    private void drag(int i, int j)
    {
        if(!isDrag)
        {
            return;
        }
        int k = len();
        int l = r.width;
        int i1 = r.height;
        int j1 = j / (i1 - 2) - 1;
        isList = true;
        int k1 = iSelectList;
        if(i < 0 || i >= l || j1 < 0 || j1 >= k)
        {
            iSelectList = -1;
            j1 = -1;
        } else
        {
            iSelectList = j1;
        }
        if(isList && !isDrawList)
        {
            isDrawList = true;
            repaint();
        }
        if(k1 == j1 || !isList)
        {
            return;
        }
        Graphics g = tools.primary();
        if(k1 >= 0)
        {
            g.setColor(tools.clFrame);
            g.drawRect(r.x + 1, r.y + (i1 - 3) * (k1 + 1) + 2, l - 3, i1 - 3);
        }
        if(j1 >= 0)
        {
            g.setColor(tools.clSel);
            g.drawRect(r.x + 1, r.y + (i1 - 3) * (j1 + 1) + 2, l - 3, i1 - 3);
        }
    }

    private int getValue()
    {
        try
        {
            return isField ? paintchat.M.class.getField(strField).getInt(info) : iSelect;
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return 0;
    }

    public void init(Tools tools1, Res res1, Res res2, M m, ToolList atoollist[], int i)
    {
        try
        {
            tools = tools1;
            res = res1;
            cnf = res2;
            lists = atoollist;
            info = m;
            String s = "t0" + i + "_";
            isDirect = res2.getP(s + "direct", false);
            isClass = res2.getP(s + "class", false);
            isEraser = res2.getP(s + "iseraser", false);
            isIm = res2.getP(s + "image", true);
            strField = res2.getP(s + "field", null);
            isField = strField != null;
            if(isField && strField.equals("iMask"))
            {
                isMask = true;
            }
            s = "t0" + i;
            int j;
            for(j = 0; res2.getP(s + j) != null; j++) { }
            strings = new String[j];
            for(int k = 0; k < j; k++)
            {
                String s1 = s + k;
                if(isField)
                {
                    if(items == null)
                    {
                        items = new int[j];
                    }
                    items[k] = res2.getP(s1, 0);
                } else
                if(isClass)
                {
                    if(strs == null)
                    {
                        strs = new String[j];
                    }
                    strs[k] = res2.getP(s1);
                } else
                {
                    if(mgs == null)
                    {
                        mgs = new M[j];
                    }
                    (mgs[k] = new M()).set(res2.getP(s1));
                }
                strings[k] = res1.res(s1);
                res2.remove(s1);
                res1.remove(s1);
            }

        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private int len()
    {
        return mgs != null ? mgs.length : items != null ? items.length : strs != null ? strs.length : 0;
    }

    public void paint(Graphics g, Graphics g1)
    {
        try
        {
            if(g == null || g1 == null)
            {
                return;
            }
            int j = r.width;
            int k = r.height;
            int l = r.x;
            int i1 = r.y;
            int j1 = len();
            int k1 = k - 2;
            if(isList)
            {
                int l1 = (i1 + k) - 2;
                Color color = isDirect ? tools.clB2 : tools.clB;
                for(int k2 = 0; k2 < j1; k2++)
                {
                    dImage(g, color, l1, k2);
                    g.setColor(tools.clText);
                    if(k2 < strings.length)
                    {
                        g.drawString(strings[k2], l + 4, l1 + base);
                    }
                    l1 += k1 - 1;
                }

                l1 = i1 + k1;
                g.setColor(tools.clFrame);
                g.drawRect(l, l1, j - 1, (k1 - 1) * j1 + 2);
                for(int l2 = 0; l2 < j1; l2++)
                {
                    g.drawRect(l + 1, l1 + 1, j - 3, k - 3);
                    l1 += k1 - 1;
                }

            }
            int i = getValue();
            if(isField)
            {
                int i2 = items.length;
                for(int j2 = 0; j2 < i2; j2++)
                {
                    if(items[j2] != i)
                    {
                        continue;
                    }
                    i = j2;
                    break;
                }

            }
            dImage(g1, isDirect ? tools.clB2 : tools.clB, 0, i);
            g1.setColor(tools.clFrame);
            g1.drawRect(0, 0, j - 1, k - 1);
            if(isSelect)
            {
                g1.setColor(tools.clSel);
                g1.drawRect(1, 1, j - 3, k - 3);
            } else
            {
                g1.setColor(tools.clBL);
                g1.fillRect(1, 1, j - 2, 1);
                g1.fillRect(1, 1, 1, k - 2);
                g1.setColor(tools.clBD);
                g1.fillRect(j - 2, 2, 1, k - 4);
                g1.fillRect(2, k - 2, j - 3, 1);
            }
            if(i >= 0 && i < strings.length)
            {
                g1.setColor(tools.clText);
                g1.drawString(strings[i], 3, base);
            }
            g.drawImage(tools.imBack, r.x, r.y, r.x + j, r.y + k, 0, 0, j, k, tools.clB, null);
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
        int k = i - r.x;
        int l = j - r.y;
        switch(mouseevent.getID())
        {
        case 503: 
        case 504: 
        case 505: 
        default:
            break;

        case 501: 
            if(r.contains(i, j))
            {
                press();
            }
            break;

        case 506: 
            drag(k, l);
            break;

        case 502: 
            release(k, l, r.contains(i, j));
            break;
        }
    }

    private void press()
    {
        if(isDrag)
        {
            return;
        }
        isDrag = true;
        iSelectList = -1;
        if(isDirect)
        {
            isList = true;
            isDrawList = true;
            repaint();
        }
    }

    private void release(int i, int j, boolean flag)
    {
        if(!isDrag)
        {
            return;
        }
        int k = r.height;
        int l = r.width;
        int i1 = j / (k - 2) - 1;
        boolean flag1 = isDrawList;
        boolean flag2 = false;
        isDrag = false;
        isList = false;
        if(i1 < 0 || i1 >= len() || i < 0 || i >= l)
        {
            i1 = -1;
        }
        if(i1 == -1)
        {
            if(flag)
            {
                if(isSelect)
                {
                    int j1 = len();
                    unSelect();
                    if(++iSelect >= j1)
                    {
                        iSelect = 0;
                    }
                    select();
                } else
                {
                    select();
                }
                flag2 = true;
            }
        } else
        {
            if(isSelect)
            {
                unSelect();
            }
            iSelect = i1;
            select();
        }
        iSelectList = -1;
        isDrawList = false;
        if(flag1)
        {
            Graphics g = tools.primary();
            g.setColor(tools.getBackground());
            g.fillRect(r.x - 1, r.y - 1, l + 2, (k - 2) * (len() + 1) + 2);
        }
        if(flag2 || flag1)
        {
            tools.mPaint(-1);
        }
    }

    public void repaint()
    {
        paint(tools.primary(), tools.getBack());
    }

    public void select()
    {
        try
        {
            if(isField)
            {
                paintchat.M.class.getField(strField).setInt(info, items[iSelect]);
                tools.upCS();
                return;
            }
            if(isClass)
            {
                tools.showW(strs[iSelect]);
                return;
            }
            if(!isDirect)
            {
                tools.unSelect();
            }
            int i = info.iColor;
            int j = info.iMask;
            int k = info.iColorMask;
            int l = info.iLayer;
            int i1 = info.iLayerSrc;
            int j1 = info.iTT;
            int k1 = info.iSA;
            int l1 = info.iSS;
            info.set(mgs[iSelect]);
            info.iColor = i;
            info.iMask = j;
            info.iColorMask = k;
            info.iLayer = l;
            info.iLayerSrc = i1;
            info.iTT = j1;
            if(k1 != info.iSA || l1 != info.iSS)
            {
                tools.mi.up();
            }
            if(!isDirect)
            {
                isSelect = true;
            }
        }
        catch(Throwable _ex) { }
    }

    public void setImage(Image image1, int i, int j, int k)
    {
        image = image1;
        imW = i;
        imH = j;
        imIndex = k;
    }

    public void setSize(int i, int j, int k)
    {
        r.setSize(i, j);
        base = k;
    }

    public void unSelect()
    {
        if(isSelect && !isDirect)
        {
            mgs[iSelect].set(info);
        }
        isSelect = false;
    }
}
