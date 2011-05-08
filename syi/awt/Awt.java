package syi.awt;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

// Referenced classes of package syi.awt:
//            LComponent

public class Awt
{

    public static Frame main_frame = null;
    public static Color cC;
    public static Color cDk;
    public static Color cLt;
    public static Color cBk;
    public static Color cFore;
    public static Color cF;
    public static Color cFSel;
    public static Color clBar;
    public static Color clLBar;
    public static Color clBarT;
    private static Font fontDef = null;
    private static float Q = 0.0F;
    private static MediaTracker mt = null;

    public Awt()
    {
    }

    public static final void drawFrame(Graphics g, boolean flag, int i, int j, int k, int l)
    {
        setup();
        drawFrame(g, flag, i, j, k, l, cDk, cLt);
    }

    public static final void drawFrame(Graphics g, boolean flag, int i, int j, int k, int l, Color color, Color color1)
    {
        setup();
        int i1 = i + k;
        int j1 = j + l;
        g.setColor(color != null ? color : cDk);
        g.fillRect(i, j, k, 1);
        g.fillRect(i, j + 1, 1, l - 2);
        g.fillRect(i + 2, j1 - 2, k - 2, 1);
        g.fillRect(i1 - 1, j + 2, 1, l - 4);
        g.setColor(color1 != null ? color1 : cLt);
        if(!flag)
        {
            g.fillRect(i + 1, j + 1, k - 2, 1);
            g.fillRect(i + 1, j + 2, 1, l - 4);
        }
        g.fillRect(i + 1, j1 - 1, k - 1, 1);
        g.fillRect(i1, j + 1, 1, l - 2);
    }

    public static final void fillFrame(Graphics g, boolean flag, int i, int j, int k, int l)
    {
        fillFrame(g, flag, i, j, k, l, cC, cDk, cDk, cLt);
    }

    public static final void fillFrame(Graphics g, boolean flag, int i, int j, int k, int l, Color color, Color color1, 
            Color color2, Color color3)
    {
        drawFrame(g, flag, i, j, k, l, color2, color3);
        g.setColor(flag ? color1 != null ? color1 : cDk : color != null ? color : cC);
        g.fillRect(i + 2, j + 2, k - 3, l - 4);
    }

    public static void getDef(Component component)
    {
        setup();
        component.setBackground(cBk);
        component.setForeground(cFore);
        component.setFont(getDefFont());
        if(component instanceof LComponent)
        {
            LComponent lcomponent = (LComponent)component;
            lcomponent.clBar = clBar;
            lcomponent.clLBar = clLBar;
            lcomponent.clBarT = clBarT;
            lcomponent.clFrame = cF;
        }
    }

    public static Font getDefFont()
    {
        if(fontDef == null)
        {
            fontDef = new Font("sansserif", 0, (int)(16F * q()));
        }
        return fontDef;
    }

    public static Component getParent(Component component)
    {
        Container container = component.getParent();
        return ((Component) (container != null ? (container instanceof Window) ? container : getParent(((Component) (container))) : component));
    }

    public static Frame getPFrame()
    {
        if(main_frame == null)
        {
            main_frame = new Frame();
        }
        return main_frame;
    }

    public static boolean isR(MouseEvent mouseevent)
    {
        return mouseevent.isAltDown() || mouseevent.isControlDown() || (mouseevent.getModifiers() & 4) != 0;
    }

    public static boolean isWin()
    {
        String s = "Win";
        return System.getProperty("os.name", s).startsWith(s);
    }

    public static void moveCenter(Window window)
    {
        Dimension dimension = window.getToolkit().getScreenSize();
        Dimension dimension1 = window.getSize();
        window.setLocation(dimension.width / 2 - dimension1.width / 2, dimension.height / 2 - dimension1.height / 2);
    }

    public static InputStream openStream(URL url)
        throws IOException
    {
        URLConnection urlconnection = url.openConnection();
        urlconnection.setUseCaches(true);
        return urlconnection.getInputStream();
    }

    public static float q()
    {
        if(Q == 0.0F)
        {
            Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
            int i = 2264;
            int j = dimension.width + dimension.height;
            Q = Math.min(1.0F + (float)(j - i) / (float)i / 2.0F, 2.0F);
        }
        return Q;
    }

    public static String replaceText(String s, String s1, String s2)
    {
        if(s.indexOf(s2) < 0)
        {
            return s;
        }
        StringBuffer stringbuffer = new StringBuffer();
        try
        {
            char ac[] = s2.toCharArray();
            if(ac.length <= 0)
            {
                return s;
            }
            int i = 0;
            int j = 0;
            int k = s.length();
            for(int l = 0; l < k; l++)
            {
                char c;
                if((c = s.charAt(l)) == ac[j])
                {
                    if(j == 0)
                    {
                        i = l;
                    }
                    if(++j >= ac.length)
                    {
                        j = 0;
                        stringbuffer.append(s1);
                    }
                } else
                {
                    if(j > 0)
                    {
                        for(int i1 = 0; i1 < j; i1++)
                        {
                            stringbuffer.append(s.charAt(i + i1));
                        }

                        j = 0;
                    }
                    stringbuffer.append(c);
                }
            }

        }
        catch(RuntimeException runtimeexception)
        {
            System.out.println("replace" + runtimeexception);
        }
        return stringbuffer.toString();
    }

    public static void setDef(Component component, boolean flag)
    {
        try
        {
            if(!flag)
            {
                flag = true;
            } else
            {
                Container container = component.getParent();
                container.setFont(container.getFont());
                container.setForeground(container.getForeground());
                container.setBackground(container.getBackground());
            }
            if(component instanceof Container)
            {
                Component acomponent[] = ((Container)component).getComponents();
                if(acomponent != null)
                {
                    for(int i = 0; i < acomponent.length; i++)
                    {
                        component = acomponent[i];
                        setDef(component, true);
                    }

                }
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public static void setPFrame(Frame frame)
    {
        main_frame = frame;
    }

    public static final void setup()
    {
        if(cC == null)
        {
            cC = new Color(0xcdcdcd);
            cDk = cLt = null;
        }
        if(cDk == null)
        {
            cDk = cC.darker();
        }
        if(cLt == null)
        {
            cLt = cC.brighter();
        }
        if(cBk == null)
        {
            cBk = new Color(0xcfcfff);
        }
        if(cFore == null)
        {
            cFore = new Color(0x505078);
        }
        if(cF == null)
        {
            cF = cFore;
        }
        if(cFSel == null)
        {
            cFSel = new Color(0xee3333);
        }
        if(clBar == null)
        {
            clBar = new Color(0x6666ff);
        }
        if(clLBar == null)
        {
            clLBar = new Color(0x8888ff);
        }
        if(clBarT == null)
        {
            clBarT = Color.white;
        }
    }

    public static Image toMin(Image image, int i, int j)
    {
        Image image1 = image.getScaledInstance(i, j, 16);
        image.flush();
        wait(image1);
        return image1;
    }

    public static String trimString(String s, String s1, String s2)
    {
        if(s == null || s.length() <= 0 || s1 == null || s2 == null)
        {
            return "";
        }
        try
        {
            int i;
            if((i = s.indexOf(s1)) == -1)
            {
                return "";
            }
            int j;
            if((j = s.indexOf(s2, i + s1.length())) == -1)
            {
                j = s.length() - 1;
            }
            return s.substring(i + s1.length(), j);
        }
        catch(RuntimeException runtimeexception)
        {
            System.out.println("t_trimString:" + runtimeexception.toString());
        }
        return "";
    }

    public static int[] getPix(Image image)
    {
        try
        {
            PixelGrabber pixelgrabber = new PixelGrabber(image, 0, 0, image.getWidth(null), image.getHeight(null), true);
            pixelgrabber.grabPixels();
            return (int[])pixelgrabber.getPixels();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return null;
    }

    public static void wait(Image image)
    {
        if(mt == null)
        {
            mt = new MediaTracker(getPFrame());
        }
        try
        {
            mt.addImage(image, 0);
            mt.waitForID(0);
        }
        catch(InterruptedException _ex) { }
        mt.removeImage(image, 0);
    }

}
