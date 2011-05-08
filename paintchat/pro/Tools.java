package paintchat.pro;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ColorModel;
import java.lang.reflect.Field;
import java.util.EventObject;
import java.util.Hashtable;
import paintchat.*;
import paintchat_client.L;
import paintchat_client.Mi;
import syi.awt.Awt;
import syi.awt.LComponent;

// Referenced classes of package paintchat.pro:
//            TPalette, TPen, TPic, TBar

public class Tools
    implements ToolBox, ActionListener
{

    private Applet applet;
    private Component parent;
    private Res res;
    protected Mi mi;
    paintchat.M.Info info;
    M mg;
    private LComponent components[];
    private TPic tPic;
    private TPalette tPalette;
    protected int iBuffer[];
    private Image image;
    private SRaster raster;

    public Tools()
    {
        image = null;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        try
        {
            PopupMenu popupmenu = (PopupMenu)actionevent.getSource();
            int i = popupmenu.getItemCount();
            String s = actionevent.getActionCommand();
            for(int j = 0; j < i; j++)
            {
                if(!popupmenu.getItem(j).getLabel().equals(s))
                {
                    continue;
                }
                mg.set(popupmenu.getName() + '=' + String.valueOf(j));
                repaint();
                break;
            }

        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public String getC()
    {
        return tPalette.getC();
    }

    public LComponent[] getCs()
    {
        return components;
    }

    public Dimension getCSize()
    {
        return null;
    }

    public void init(Container container, Applet applet1, Res res1, Res res2, Mi mi1)
    {
        applet = applet1;
        info = mi1.info;
        mg = info.m;
        res = res2;
        iBuffer = mi1.user.getBuffer();
        mi = mi1;
        parent = container;
        Dimension dimension = container.getSize();
        LComponent alcomponent[] = new LComponent[9];
        TPen tpen = new TPen(this, info, res1, null, alcomponent);
        tpen.init(0);
        alcomponent[0] = tpen;
        TPen tpen1 = new TPen(this, info, res1, tpen, alcomponent);
        tpen1.init(1);
        alcomponent[1] = tpen1;
        TPalette tpalette = new TPalette();
        tpalette.setLocation((int)((float)tpen.getSizeW().width * Awt.q()) + 10, 0);
        tpalette.init(this, info, res1, res2);
        alcomponent[2] = tpalette;
        tPalette = tpalette;
        TPen tpen2 = new TPen(this, info, res1, null, alcomponent);
        tpen2.initTT();
        alcomponent[3] = tpen2;
        TPic tpic = new TPic(this);
        alcomponent[4] = tpic;
        tPic = tpic;
        TPen tpen3 = new TPen(this, info, res1, null, alcomponent);
        tpen3.setLocation(tpalette.getLocation().x + tpalette.getSizeW().width, 0);
        tpen3.initHint();
        alcomponent[5] = tpen3;
        L l = new L(mi1, this, res2, res1);
        alcomponent[6] = l;
        TBar tbar = new TBar(res1, res2, alcomponent);
        alcomponent[7] = tbar;
        TBar tbar1 = new TBar(res1, res2, alcomponent);
        alcomponent[8] = tbar1;
        tbar.initOption(applet1.getCodeBase(), mi1);
        tbar1.init();
        tpen2.setLocation(tpen3.getLocation().x + tpen3.getSizeW().width, 0);
        tpen1.setLocation(0, tpen.getSizeW().height);
        tpic.setLocation(0, tpen1.getLocation().y + tpen1.getSizeW().height);
        tbar1.setLocation(dimension.width - tbar1.getSizeW().width, 0);
        for(int i = 0; i < alcomponent.length; i++)
        {
            alcomponent[i].setVisible(false);
            container.add(alcomponent[i], 0);
        }

        components = alcomponent;
        tbar1.setVisible(true);
        tpen.setItem(0, null);
    }

    public void lift()
    {
        ((TPen)components[0]).setItem(-1, null);
    }

    protected Image mkImage(int i, int j)
    {
        if(raster == null)
        {
            raster = new SRaster(ColorModel.getRGBdefault(), iBuffer, i, j);
            image = applet.createImage(raster);
        } else
        {
            raster.newPixels(image, iBuffer, i, j);
        }
        return image;
    }

    public void pack()
    {
        if(components != null)
        {
            for(int i = 0; i < components.length; i++)
            {
                if(components[i] != null)
                {
                    components[i].inParent();
                }
            }

            mi.setVisible(false);
            Dimension dimension = parent.getSize();
            mi.setDimension(null, new Dimension(dimension), new Dimension(dimension));
            Dimension dimension1 = mi.getSize();
            mi.setLocation((dimension.width - dimension1.width) / 2, (dimension.height - dimension1.height) / 2);
            mi.setVisible(true);
        }
    }

    void repaint()
    {
        for(int i = 0; i < components.length; i++)
        {
            components[i].repaint();
        }

    }

    public void selPix(boolean flag)
    {
        ((TPen)components[0]).undo(flag);
    }

    public void setARGB(int i)
    {
        int j = mg.iAlpha << 24 | mg.iColor;
        mg.iAlpha = i >>> 24;
        mg.iColor = i & 0xffffff;
        if(j != i)
        {
            tPic.setColor(i);
            tPalette.setColor(i);
        }
    }

    public void setC(String s)
    {
        tPalette.setC(s);
    }

    void setField(Component component, String s, String s1, int i, int j)
    {
        try
        {
            PopupMenu popupmenu = new PopupMenu();
            popupmenu.setName(s);
            popupmenu.addActionListener(this);
            int k = paintchat.M.class.getField(s).getInt(mg);
            for(int l = 0; l < 16; l++)
            {
                Object obj = res.get(s1 + l);
                if(obj != null)
                {
                    if(k == l)
                    {
                        popupmenu.add(new CheckboxMenuItem(obj.toString(), true));
                    } else
                    {
                        popupmenu.add(obj.toString());
                    }
                }
            }

            component.add(popupmenu);
            popupmenu.show(component, i, j);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void setLineSize(int i)
    {
        tPalette.setLineSize(i);
    }

    public void setMask(Component component, int i, int j, int k, boolean flag)
    {
        if(flag)
        {
            setField(component, "iMask", "mask_", j, k);
        } else
        {
            mg.iColorMask = i & 0xffffff;
            components[4].repaint();
        }
    }

    public void setRGB(int i)
    {
        setARGB(mg.iAlpha << 24 | i & 0xffffff);
    }

    public void up()
    {
        tPic.repaint();
        tPalette.repaint();
        if(components != null && components[6] != null)
        {
            components[6].repaint();
        }
    }
}
