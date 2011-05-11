package syi.awt;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import paintchat.Res;
import syi.javascript.JSController;

// Referenced classes of package syi.awt:
//            Awt

public class TextPanel extends Canvas
    implements ActionListener, ItemListener
{

    public boolean isView;
    private boolean isPress;
    private Font font;
    private Object lock;
    private Applet applet;
    private boolean isSScroll;
    private boolean isGetFSize;
    private boolean isGetSize;
    private boolean isVisitScroll;
    private int H;
    private int WS;
    private int As;
    private int Ds;
    private int Gap;
    private int scrollPos;
    private int scrollMax;
    private int iSeek;
    private TextField textField;
    private Res config;
    private Color nowColor;
    private Color beColor;
    private String strings[];
    private Color colors[];
    private Graphics primary;
    private static PopupMenu popup = null;
    private int Y;
    private Dimension size;
    private static final String strEmpty = "";

    public TextPanel()
    {
        isView = true;
        isPress = false;
        lock = new Object();
        isSScroll = false;
        isGetFSize = false;
        isGetSize = false;
        isVisitScroll = true;
        H = 15;
        WS = 12;
        As = 0;
        Ds = 0;
        Gap = 1;
        iSeek = 0;
        nowColor = null;
        beColor = null;
        strings = null;
        colors = null;
        primary = null;
        size = new Dimension();
        nowColor = Color.black;
    }

    public TextPanel(Applet applet1, int i, Color color, Color color1, TextField textfield)
    {
        isView = true;
        isPress = false;
        lock = new Object();
        isSScroll = false;
        isGetFSize = false;
        isGetSize = false;
        isVisitScroll = true;
        H = 15;
        WS = 12;
        As = 0;
        Ds = 0;
        Gap = 1;
        iSeek = 0;
        nowColor = null;
        beColor = null;
        strings = null;
        colors = null;
        primary = null;
        size = new Dimension();
        init(applet1, i, color, color1, textfield);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        try
        {
            String s = actionevent.getActionCommand();
            if(s == null || s.length() <= 0)
            {
                return;
            }
            PopupMenu popupmenu = popup;
            if(popupmenu.getItem(0).getLabel().equals(s))
            {
                if((s = getLine(Y)) != null)
                {
                    textField.setText(s);
                }
                return;
            }
            if(popupmenu.getItem(2).getLabel().equals(s))
            {
                String s1 = getLine(Y);
                int j = s1.indexOf("http://");
                int k = s1.indexOf(' ', j);
                k = k >= 0 ? k : s1.length();
                applet.getAppletContext().showDocument(new URL(s1.substring(j, k)), "jump_url");
                return;
            }
            if(popupmenu.getItem(3).getLabel().equals(s))
            {
                String s2 = System.getProperty("line.separator");
                StringBuffer stringbuffer = new StringBuffer();
                stringbuffer.append("<html><body>");
                stringbuffer.append(s2);
                for(int l = 0; l < iSeek; l++)
                {
                    s = strings[l];
                    if(s != null)
                    {
                        s = Awt.replaceText(s, "&lt;", "<");
                        s = Awt.replaceText(s, "&gt;", ">");
                        stringbuffer.append(s);
                        stringbuffer.append("<br>");
                        stringbuffer.append(s2);
                    }
                }

                stringbuffer.append("<div align=\"right\"> <a href=\"http://www.gt.sakura.ne.jp/~ocosama/\">shi-chan " +
"site</a></div>"
);
                stringbuffer.append("</body></html>");
                String s3 = "text_html";
                JSController jscontroller = new JSController(applet);
                jscontroller.openWindow(null, s3, null, true, false, true, true, false);
                jscontroller.showDocument(s3, "text/html", stringbuffer.toString());
                return;
            }
            if(popupmenu.getItem(7).getLabel().equals(s))
            {
                clear();
                repaint();
                return;
            }
            if(s.charAt(0) == '+')
            {
                s = s.substring(1);
            }
            int i = Math.min(Math.max(font.getSize() + Integer.parseInt(s), 4), 256);
            setFont(new Font(font.getName(), font.getStyle(), i));
            repaint();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void addText(String s)
    {
        addText(s, true);
    }

    public void addText(String s, boolean flag)
    {
        synchronized(lock)
        {
            if(iSeek > 0)
            {
                System.arraycopy(strings, 0, strings, 1, iSeek);
                System.arraycopy(colors, 0, colors, 1, iSeek);
            }
        }
        strings[0] = s;
        colors[0] = nowColor;
        if(iSeek < strings.length - 2)
        {
            iSeek++;
        }
        if(flag && isGetFSize)
        {
            if(primary == null)
            {
                primary = getGraphics();
            }
            paint(primary);
        }
    }

    public void call(String s)
    {
        try
        {
            JSController jscontroller = new JSController(applet);
            jscontroller.runScript(s);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public synchronized void clear()
    {
        for(int i = 0; i < strings.length; i++)
        {
            strings[i] = null;
            colors[i] = null;
        }

        iSeek = 0;
    }

    public void decode(String s)
    {
        if(s.length() <= 0)
        {
            return;
        }
        if(s.charAt(0) == '$')
        {
            int i = 0;
            boolean flag = false;
            if(s.startsWith("$js:"))
            {
                call(s.substring(4, s.length()));
                return;
            }
            int j;
            for(; i < s.length(); i = j + 1)
            {
                j = s.indexOf(';', i);
                if(j < 0)
                {
                    j = s.length();
                }
                decode(s, i, j);
            }

        } else
        {
            decode(s, 0, s.length());
        }
    }

    private void decode(String s, int i, int j)
    {
        if(j - i <= 0)
        {
            return;
        }
        try
        {
            if(s.charAt(i) != '$')
            {
                addText(s.substring(i, j));
                return;
            }
            int k = s.indexOf(':', i);
            String s1 = "";
            String s2 = "";
            if(k <= i || k > j)
            {
                s1 = s;
            } else
            {
                s1 = s.substring(i, k);
            }
            if(k >= 0 && k < j - 1)
            {
                s2 = s.substring(k + 1, j);
            }
            if(s1.indexOf("$clear") >= 0)
            {
                synchronized(lock)
                {
                    for(int i1 = 0; i1 < strings.length; i1++)
                    {
                        strings[i1] = null;
                    }

                }
            } else
            if(s1.indexOf("$bkcolor") >= 0)
            {
                setBackground(new Color(Res.parseInt(s2)));
            } else
            if(s1.indexOf("$color") >= 0)
            {
                setForeground(s2.charAt(0) != '/' ? new Color(Res.parseInt(s2)) : beColor);
            } else
            if(s1.indexOf("$font_size") >= 0)
            {
                Font font1 = getFont();
                int l = Res.parseInt(s2);
                setFont(new Font(font1.getName(), 0, font1.getSize() + l));
                repaint();
            } else
            if(s1.indexOf("$font") >= 0)
            {
                Font font2 = getFont();
                int j1 = s2.equals("bold") ? 1 : 0;
                setFont(new Font(font2.getName(), j1, font2.getSize()));
                repaint();
            } else
            if(s1.indexOf("$js") >= 0)
            {
                call(s2);
            } else
            {
                addText(s.substring(i, j));
            }
        }
        catch(RuntimeException _ex) { }
    }

    public String getLine(int i)
    {
        int j = (scrollPos + i) / (H + Gap * 2);
        return strings[j > 0 ? j < iSeek ? j : iSeek - 1 : 0];
    }

    public Dimension getPreferredSize()
    {
        Dimension dimension = getToolkit().getScreenSize();
        int i = Gap * 2;
        Font font1 = getFont();
        FontMetrics fontmetrics = null;
        if(font1 != null)
        {
            fontmetrics = getFontMetrics(font1);
        }
        if(fontmetrics == null)
        {
            dimension.setSize(300, 120);
            return dimension;
        }
        H = fontmetrics.getMaxAscent() + fontmetrics.getMaxDescent() + 1;
        int j = 0;
        if(strings != null)
        {
            for(int k = 0; k < iSeek; k++)
            {
                if(strings[k] != null)
                {
                    j = Math.max(fontmetrics.stringWidth(strings[k]) + i, j);
                }
            }

        }
        j += i + WS;
        dimension.setSize(j > 100 ? j < dimension.width / 2 ? j : dimension.width / 2 : 100, (H + i) * (iSeek > 3 ? iSeek < 12 ? iSeek : 12 : 3));
        return dimension;
    }

    public Dimension getSize()
    {
        if(size.width + size.height == 0 || !isGetSize)
        {
            size.setSize(super.getSize());
        }
        return size;
    }

    public void init(Applet applet1, int i, Color color, Color color1, TextField textfield)
    {
        enableEvents(49L);
        textField = textfield;
        applet = applet1;
        setBackground(color);
        setForeground(color1);
        nowColor = beColor = color1;
        setMaxLabel(i);
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
        try
        {
            isSScroll = itemevent.getStateChange() == 1;
            scrollPos = 0;
            paint(primary);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void paint(Graphics g)
    {
        try
        {
            Dimension dimension = getSize();
            if(!isView)
            {
                g.setColor(getBackground());
                g.fillRect(0, 0, dimension.width, dimension.height);
                return;
            }
            if(strings == null)
            {
                return;
            }
            if(g == null)
            {
                return;
            }
            int i = Gap * 2;
            int _tmp = strings.length;
            if(!isGetFSize)
            {
                isGetFSize = true;
                if(font == null)
                {
                    font = super.getFont();
                }
                FontMetrics fontmetrics = g.getFontMetrics();
                As = fontmetrics.getMaxAscent();
                Ds = fontmetrics.getMaxDescent();
                H = As + Ds;
                scrollPos = 0;
            }
            g.setFont(font);
            int j = H + i;
            scrollMax = j * iSeek;
            if(dimension.height <= 0 || dimension.width <= 0)
            {
                return;
            }
            int k = WS;
            int l = dimension.width - 2 - k;
            int i1 = Math.max(scrollPos / j, 0);
            int j1 = i1 + dimension.height / j + 2;
            int k1 = i1 * j - scrollPos;
            g.setClip(1, 1, l, dimension.height - 2);
            Color color = getBackground();
            g.setColor(color);
            for(int i2 = i1; i2 < j1; i2++)
            {
                if(i2 < strings.length && strings[i2] != null)
                {
                    g.fillRect(1, k1, l, H + i);
                    g.setColor(colors[i2]);
                    // java.lang.ClassCastException: sun.java2d.NullSurfaceData cannot be cast to sun.java2d.d3d.D3DSurfaceData
                    g.drawString(strings[i2], 1, (k1 + Gap + H) - Ds);
                    g.setColor(color);
                } else
                {
                    g.fillRect(1, k1, l, H + i);
                }
                k1 += j;
            }

            if(k1 > 0)
            {
                g.setColor(Color.black);
                g.fillRect(1, k1, l - 1, dimension.height - k1 - 1);
            }
            g.setClip(0, 0, dimension.width, dimension.height);
            if(isVisitScroll)
            {
                int j2 = dimension.height / (H + i);
                int k2 = (int)((float)dimension.height * ((float)j2 / (float)(iSeek + j2)));
                int l1 = (int)(((float)scrollPos / (float)scrollMax) * (float)(dimension.height - k2 - 1));
                l++;
                k--;
                g.setColor(getForeground());
                g.fillRect(l, 1, 1, dimension.height - 2);
                g.fillRect(l + 1, l1, k, k2);
                g.setColor(color);
                g.setColor(getBackground());
                g.fillRect(l + 1, 1, k, l1);
                g.fillRect(l + 1, l1 + k2 + 1, k, dimension.height - k2 - l1 - 1);
                g.setColor(getForeground());
                g.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void popup(int i, int j)
    {
        if(popup == null)
        {
            Menu menu = new Menu("Font size");
            menu.addActionListener(this);
            for(int k = 6; k > -6; k--)
            {
                if(k != 0)
                {
                    String s = String.valueOf(k);
                    if(k > 0)
                    {
                        s = '+' + s;
                    }
                    menu.add(s);
                }
            }

            popup = new PopupMenu();
            popup.add("CopyString");
            popup.addSeparator();
            popup.add("GotoURL");
            popup.add("ToHTML");
            popup.addSeparator();
            popup.add(menu);
            popup.addSeparator();
            popup.add("Erase");
            CheckboxMenuItem checkboxmenuitem = new CheckboxMenuItem("Smooth scroll", isSScroll);
            checkboxmenuitem.addItemListener(this);
            popup.add(checkboxmenuitem);
        }
        add(popup);
        popup.addActionListener(this);
        popup.getItem(2).setEnabled(getLine(j).indexOf("http://") >= 0);
        popup.show(this, i, j);
    }

    protected void processEvent(AWTEvent awtevent)
    {
        try
        {
            if(awtevent instanceof MouseEvent)
            {
                MouseEvent mouseevent = (MouseEvent)awtevent;
                int i = mouseevent.getX();
                int j = mouseevent.getY();
                switch(awtevent.getID())
                {
                case 503: 
                case 504: 
                case 505: 
                default:
                    break;

                case 501: 
                    Y = j;
                    isPress = !Awt.isR(mouseevent);
                    if(!isPress)
                    {
                        popup(i, j);
                    }
                    break;

                case 502: 
                    isPress = false;
                    break;

                case 506: 
                    if(!isPress || Y == j)
                    {
                        break;
                    }
                    int k = -(Y - j);
                    if(!isSScroll)
                    {
                        k *= H + Gap * 2;
                    }
                    scrollPos = Math.max(Math.min(scrollPos + k, scrollMax), 0);
                    Y = j;
                    if(primary == null)
                    {
                        primary = getGraphics();
                    }
                    paint(primary);
                    break;
                }
                return;
            }
            if(awtevent instanceof ComponentEvent)
            {
                switch(awtevent.getID())
                {
                case 101: // 'e'
                case 102: // 'f'
                    isGetSize = false;
                    if(primary != null)
                    {
                        primary.dispose();
                        primary = null;
                    }
                    if(isGetFSize)
                    {
                        repaint();
                    }
                    break;
                }
                return;
            }
            super.processEvent(awtevent);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void remove(String s)
    {
        try
        {
            synchronized(lock)
            {
                int i = iSeek;
                for(int j = 0; j < i; j++)
                {
                    String s1;
                    if((s1 = strings[j]) == null || !s1.equals(s))
                    {
                        continue;
                    }
                    if(j != i - 1)
                    {
                        System.arraycopy(strings, j + 1, strings, j, i - j - 1);
                    }
                    strings[i - 1] = null;
                    iSeek--;
                    break;
                }

            }
            repaint();
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
        }
    }

    public void setFont(Font font1)
    {
        font = font1;
        isGetFSize = false;
    }

    public void setForeground(Color color)
    {
        beColor = nowColor;
        nowColor = color;
        super.setForeground(color);
    }

    public void setMaxLabel(int i)
    {
        if(i <= 0)
        {
            return;
        }
        String as[] = new String[i];
        Color acolor[] = new Color[i];
        scrollPos = 0;
        if(strings != null)
        {
            System.arraycopy(strings, 0, as, 0, strings.length);
        }
        if(colors != null)
        {
            System.arraycopy(colors, 0, acolor, 0, colors.length);
        }
        strings = as;
        colors = acolor;
    }

    public void setRText(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        try
        {
            BufferedReader bufferedreader = new BufferedReader(new StringReader(s));
            String s1;
            while((s1 = bufferedreader.readLine()) != null) 
            {
                stringbuffer.insert(0, s1);
                stringbuffer.insert(0, '\n');
            }
            bufferedreader.close();
        }
        catch(IOException _ex) { }
        setText(stringbuffer.toString());
    }

    public void setText(String s)
    {
        int i = s.length();
        CharArrayWriter chararraywriter = new CharArrayWriter();
        for(int j = 0; j < i; j++)
        {
            char c = s.charAt(j);
            if(c == '\r' || c == '\n')
            {
                if(chararraywriter.size() > 0)
                {
                    decode(chararraywriter.toString());
                    chararraywriter.reset();
                }
            } else
            {
                chararraywriter.write(c);
            }
        }

        if(chararraywriter.size() > 0)
        {
            decode(chararraywriter.toString());
        }
    }

    public void setVisitScroll(boolean flag)
    {
        isVisitScroll = flag;
        WS = flag ? 12 : 0;
    }

    public void update(Graphics g)
    {
        paint(g);
    }

}
