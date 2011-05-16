package paintchat_client;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.EventObject;
import java.util.Hashtable;
import paintchat.*;
import syi.awt.*;
import syi.util.ThreadPool;

// Referenced classes of package paintchat_client:
//            IMi, Data, Mi, DCF

public class Pl extends Panel
    implements Runnable, ActionListener, IMi, KeyListener
{

    private static final String STR_VERSION = "PaintChatClient v3.66";
    private static final String STR_INFO = "PaintChat";
    protected Applet applet;
    private boolean isStart;
    private int iScrollType;
    private Data dd;
    public Res res;
    public Mi mi;
    private ToolBox tool;
    private Panel tPanel;
    private Panel tPanelB;
    private TextPanel tText;
    private TextField tField;
    private TextPanel tList;
    private Panel miPanel;
    private Label tLabel;
    private MgText mgText;
    private Dimension dPack;
    private Dimension dSize;
    private Dimension dMax;
    private int iGap;
    private int iCenter;
    private int iCenterOld;
    private Color clInfo;
    private AudioClip sounds[];
    private int iPG;

    public Pl(Applet applet1)
    {
        super(null);
        isStart = false;
        iScrollType = 0;
        tool = null;
        dPack = new Dimension();
        dSize = null;
        dMax = new Dimension();
        iGap = 5;
        iCenter = 80;
        iCenterOld = -1;
        sounds = null;
        iPG = 10;
        applet = applet1;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        try
        {
            Object obj = actionevent.getSource();
            if(obj instanceof LButton)
            {
                switch(Integer.parseInt(((Component)obj).getName()))
                {
                case 0: // '\0'
                    f(tPanel, true);
                    break;

                case 1: // '\001'
                    f(this, false);
                    break;

                case 2: // '\002'
                    mExit();
                    break;
                }
            } else
            {
                typed();
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void mExit()
    {
        try
        {
            applet.getAppletContext().showDocument(new URL(applet.getDocumentBase(), dd.config.getP("exit", "../index.html")));
        }
        catch(Throwable _ex) { }
    }

    protected void addInOut(String s, boolean flag)
    {
        if(flag)
        {
            tList.addText(s);
            s = s + res.res("entered");
            dSound(2);
        } else
        {
            tList.remove(s);
            s = s + res.res("leaved");
            dSound(3);
        }
        addTextInfo(s, false);
    }

    protected void addSText(String s)
    {
        tText.decode(s);
    }

    protected void addText(String s, String s1, boolean flag)
    {
        if(s1 == null)
        {
            tText.repaint();
        } else
        {
            tText.addText(s != null ? s + '>' + s1 : s1, flag);
        }
    }

    protected void addTextInfo(String s, boolean flag)
    {
        Color color = tText.getForeground();
        tText.setForeground(Color.red);
        addText(null, "PaintChat>" + s, flag);
        tText.setForeground(color);
    }

    public void changeSize()
    {
        mi.resetGraphics();
        pack();
    }

    public void destroy()
    {
        try
        {
            if(dd != null)
            {
                dd.destroy();
            }
            dd = null;
            tool = null;
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    protected void dSound(int i)
    {
        try
        {
            if(sounds == null || sounds[i] == null)
            {
                return;
            }
            sounds[i].play();
        }
        catch(RuntimeException _ex)
        {
            sounds = null;
        }
    }

    private void f(Component component, boolean flag)
    {
        try
        {
            boolean flag1 = false;
            Object obj = flag ? ((Object) (this)) : ((Object) (applet));
            Component acomponent[] = ((Container) (obj)).getComponents();
            for(int i = 0; i < acomponent.length; i++)
            {
                if(acomponent[i] != component)
                {
                    continue;
                }
                flag1 = true;
                break;
            }

            obj = component.getParent();
            ((Container) (obj)).remove(component);
            if(flag1)
            {
                if(flag)
                {
                    iCenter = 100;
                }
                pack();
                Frame frame = new Frame("PaintChatClient v3.66");
                frame.setLayout(new BorderLayout());
                frame.add(component, "Center");
                frame.pack();
                frame.setVisible(true);
            } else
            {
                ((Window)obj).dispose();
                if(flag)
                {
                    iCenter = 70;
                    add(component);
                } else
                {
                    applet.add(component, "Center");
                    applet.validate();
                }
                pack();
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public Dimension getSize()
    {
        if(dSize == null)
        {
            dSize = super.getSize();
        }
        return dSize;
    }

    public void iPG(boolean flag)
    {
        if(flag)
        {
            iPG = Math.min(100, iPG + 10);
        }
        if(isStart)
        {
            return;
        }
        try
        {
            Graphics g = getGraphics();
            if(g == null)
            {
                return;
            }
            String s = String.valueOf(iPG) + '%';
            FontMetrics fontmetrics = g.getFontMetrics();
            int i = fontmetrics.getHeight() + 2;
            g.setColor(getBackground());
            g.fillRect(5, 5 + i, fontmetrics.stringWidth(s) + 15, i + 10);
            g.setColor(getForeground());
            g.drawString("PaintChatClient v3.66", 10, 10 + i);
            g.drawString(s, 10, 10 + i * 2);
            g.dispose();
        }
        catch(Throwable _ex) { }
    }

    public void keyPressed(KeyEvent keyevent)
    {
        try
        {
            boolean flag = keyevent.isAltDown() || keyevent.isControlDown();
            int i = keyevent.getKeyCode();
            if(flag)
            {
                if(i == 38)
                {
                    keyevent.consume();
                    iCenter = Math.max(iCenter - 4, 0);
                    pack();
                }
                if(i == 40)
                {
                    keyevent.consume();
                    iCenter = Math.min(iCenter + 4, 100);
                    pack();
                }
                if(i == 83)
                {
                    keyevent.consume();
                    typed();
                }
            } else
            {
                switch(i)
                {
                case 117: // 'u'
                    f(this, false);
                    break;

                default:
                    dSound(0);
                    break;

                case 10: // '\n'
                    break;
                }
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void keyReleased(KeyEvent keyevent)
    {
    }

    public void keyTyped(KeyEvent keyevent)
    {
    }

    private Cursor loadCursor(String s, int i)
    {
        try
        {
            if(s != null && s.length() > 0)
            {
                boolean flag = s.equals("none");
                int j;
                int k;
                int l;
                int i1;
                Image image;
                if(!flag)
                {
                    image = getToolkit().createImage((byte[])dd.config.getRes(s));
                    if(image == null)
                    {
                        return Cursor.getPredefinedCursor(i);
                    }
                    Awt.wait(image);
                    l = image.getWidth(null);
                    i1 = image.getHeight(null);
                    j = s.indexOf('x');
                    if(j == -1)
                    {
                        j = j != -1 ? Integer.parseInt(s.substring(j + 1, s.indexOf('x', j + 1))) : l / 2 - 1;
                    }
                    k = s.indexOf('y');
                    if(k == -1)
                    {
                        k = k != -1 ? Integer.parseInt(s.substring(k + 1, s.indexOf('y', k + 1))) : i1 / 2 - 1;
                    }
                } else
                {
                    j = k = 7;
                    l = i1 = 16;
                    image = null;
                }
                try
                {
                    if(image == null)
                    {
                        IndexColorModel indexcolormodel = new IndexColorModel(8, 2, new byte[2], new byte[2], new byte[2], 0);
                        image = createImage(new MemoryImageSource(l, i1, indexcolormodel, new byte[l * i1], 0, l));
                    }
                    Toolkit toolkit = getToolkit();
                    toolkit.getClass();
                    Method method = java.awt.Toolkit.class.getMethod("createCustomCursor", new Class[] {
                        java.awt.Image.class, java.awt.Point.class, java.lang.String.class
                    });
                    Method method1 = java.awt.Toolkit.class.getMethod("getBestCursorSize", new Class[] {
                        Integer.TYPE, Integer.TYPE
                    });
                    Dimension dimension = (Dimension)method1.invoke(toolkit, new Object[] {
                        new Integer(l), new Integer(i1)
                    });
                    if(dimension.width != 0 && dimension.height != 0)
                    {
                        return (Cursor)method.invoke(toolkit, new Object[] {
                            image, new Point((int)(((float)l / (float)dimension.width) * (float)j), (int)(((float)i1 / (float)dimension.height) * (float)k)), "custum"
                        });
                    }
                }
                catch(NoSuchMethodException _ex)
                {
                    if(image == null)
                    {
                        image = createImage(new MemoryImageSource(l, i1, new int[l * i1], 0, l));
                    }
                    return (Cursor)Class.forName("com.ms.awt.CursorX").getConstructors()[0].newInstance(new Object[] {
                        image, new Integer(j), new Integer(k)
                    });
                }
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        return Cursor.getPredefinedCursor(i);
    }

    private void loadSound()
    {
        if(sounds != null)
        {
            sounds = null;
            return;
        }
        sounds = new AudioClip[4];
        String as[] = {
            "tp.au", "talk.au", "in.au", "out.au"
        };
        for(int i = 0; i < 4; i++)
        {
            try
            {
                String s = dd.config.res(as[i]);
                if(s != null && s.length() > 0 && s.charAt(0) != '_')
                {
                    sounds[i] = applet.getAudioClip(new URL(applet.getCodeBase(), s));
                }
            }
            catch(IOException _ex)
            {
                sounds[i] = null;
            }
        }

    }

    private synchronized void mkTextPanel()
    {
        if(tField != null)
        {
            return;
        }
        String s = "Center";
        String s1 = "East";
        String s2 = "West";
        Panel panel = new Panel(new BorderLayout());
        tField = new TextField();
        tField.addActionListener(this);
        panel.add(tField, s);
//        assert(dd.res != null);
        // FIXME: this line doesn't check for, and fails if, dd.res is null
        tLabel = new Label(dd.res.res("input"));
        panel.add(tLabel, s2);
        String as[] = {
            "F", "FAll", "leave"
        };
        Panel panel1 = new Panel(new FlowLayout(0, 2, 1));
        tPanelB = panel1;
        for(int i = 0; i < 3; i++)
        {
            LButton lbutton = new LButton(res.res(as[i]));
            lbutton.addActionListener(this);
            lbutton.setName(String.valueOf(i));
            panel1.add(lbutton);
        }

        panel.add(panel1, s1);
        Color color = getBackground();
        Color color1 = getForeground();
        tText = new TextPanel(applet, 100, color, color1, tField);
        tList = new TextPanel(applet, 20, color, color1, tField);
        tPanel = new Panel(new BorderLayout());
        tPanel.add(tText, s);
        tPanel.add(tList, s1);
        tPanel.add(panel, "South");
        Awt.getDef(tPanel);
        Awt.setDef(tPanel, false);
    }

    private void pack()
    {
        dSize = super.getSize();
        if(tool == null || mi == null || dPack == null)
        {
            return;
        } else
        {
            getSize();
            ThreadPool.poolStartThread(this, 'p');
            return;
        }
    }

    public void paint(Graphics g)
    {
        if(!isStart)
        {
            iPG(false);
        }
        Dimension dimension = getSize();
        g.drawRect(0, 0, dimension.width - 1, dimension.height - 1);
    }

    protected void processEvent(AWTEvent awtevent)
    {
        int i = awtevent.getID();
        if(i == 101)
        {
            dSize = super.getSize();
            Dimension dimension = getSize();
            setSize(dimension.getSize());
            if(dPack != null && !dPack.equals(dimension))
            {
                pack();
            }
        } else
        if(mi != null && (awtevent instanceof MouseEvent))
        {
            Point point = mi.getLocation();
            ((MouseEvent)awtevent).translatePoint(-point.x, -point.y);
            mi.dispatchEvent(awtevent);
        }
        super.processEvent(awtevent);
    }

    public void repaint(long l, int i, int j, int k, int i1)
    {
        repaint(((Component) (this)), i, j, k, i1);
    }

    private void repaint(Component component, int i, int j, int k, int l)
    {
        if(component instanceof Container)
        {
            Component acomponent[] = ((Container)component).getComponents();
            for(int i1 = 0; i1 < acomponent.length; i1++)
            {
                Point point1 = acomponent[i1].getLocation();
                repaint(acomponent[i1], i - point1.x, j - point1.y, k, l);
            }

        } else
        {
            Point point = component.getLocation();
            int j1 = i - point.x;
            int k1 = j - point.y;
            if(j1 + k <= 0 || k1 + l <= 0)
            {
                return;
            }
            component.repaint(j1, k1, k, l);
        }
    }

    private void rInit()
    {
        String s = "cursor_";
        String s1 = "window_color_";
        try
        {
            getSize();
            dd = new Data(this);
            mgText = new MgText();
            mi = new Mi(this, res);
            iPG(true);
            dd.mi = mi;
            dd.init();
            res = dd.res;
            Res res1 = dd.config;
            int k = res1.getP("layer_count", 2);
            int l = res1.getP("quality", 1);
            try
            {
                Color color = new Color(res1.getP("color_bk", 0xcfcfff));
                applet.setBackground(color);
                setBackground(color);
                color = new Color(res1.getP(s1 + "_bk", color.getRGB()));
                Awt.cBk = Awt.cC = color;
                color = new Color(res1.getP("color_text", 0x505078));
                applet.setForeground(color);
                setForeground(color);
                Awt.cFore = new Color(res1.getP(s1 + "_text", color.getRGB()));
                Awt.cFSel = new Color(res1.getP("color_iconselect", 0xee3333));
                Awt.cF = new Color(res1.getP(s1 + "_frame", 0));
                Awt.clBar = new Color(res1.getP(s1 + "_bar", 0x6666ff));
                Awt.clLBar = new Color(res1.getP(s1 + "_bar_hl", 0x8888ff));
                Awt.clBarT = new Color(res1.getP(s1 + "_bar_text", 0xffffff));
                Awt.getDef(this);
                Awt.setPFrame((Frame)Awt.getParent(this));
            }
            catch(Throwable _ex) { }
            iPG(true);
            Cursor acursor[] = new Cursor[4];
            int i = 0;
            int ai[] = {
                i, 13, i, i
            };
            for(int j = 0; j < 4; j++)
            {
                acursor[j] = loadCursor(applet.getParameter(s + (j + 1)), ai[j]);
            }

            iPG(true);
            miPanel = new Panel(null);
            mi.init(applet, dd.config, dd.imW, dd.imH, l, k, acursor);
            miPanel.add(mi);
            iPG(true);
            String s2 = res1.getP("tools", "normal");
            try
            {
                tool = (ToolBox)Class.forName("paintchat." + s2 + ".Tools").newInstance();
                tool.init(miPanel, applet, dd.config, res, mi);
            }
            catch(Throwable throwable1)
            {
                throwable1.printStackTrace();
            }
            mkTextPanel();
            tField.addKeyListener(this);
            enableEvents(9L);
            isStart = true;
            add(tPanel);
            add(miPanel);
            tField.requestFocus();
            iPG(true);
            pack();
            if(dd.config.getP("Client_Sound", false))
            {
                loadSound();
            }
            DCF dcf = new DCF(res);
            dcf.mShow();
            String s3 = dcf.mGetHandle();
            if(s3.length() <= 0)
            {
                mExit();
                return;
            }
            dd.strName = s3;
            dd.config.put("chat_password", dcf.mGetPass());
            dd.start();
            addInOut(s3, true);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private synchronized void rPack()
    {
        Dimension dimension = getSize();
        dPack.setSize(dimension);
        setVisible(false);
        int i = iGap;
        int j = (int)((float)dimension.height * ((float)iCenter / 100F));
        if(miPanel != null)
        {
            miPanel.setBounds(0, 0, dimension.width, j);
        }
        if(tool != null)
        {
            tool.pack();
            if(tPanel != null && tPanel.getParent() == this)
            {
                int k = 0;
                tPanel.setBounds(k, j + i, dimension.width - k, dimension.height - (j + i));
                validate();
            }
        }
        mi.resetGraphics();
        setVisible(true);
    }

    public void run()
    {
        try
        {
            switch(Thread.currentThread().getName().charAt(0))
            {
            case 105: // 'i'
                rInit();
                break;

            case 112: // 'p'
                rPack();
                break;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void scroll(boolean flag, int i, int j)
    {
        LComponent alcomponent[] = tool.getCs();
        int k = alcomponent.length;
        Point point1 = mi.getLocation();
        int k1 = point1.x + mi.getGapX();
        int l1 = point1.y + mi.getGapY();
        Dimension dimension1 = mi.getSizeW();
        for(int i2 = 0; i2 < k; i2++)
        {
            LComponent lcomponent = alcomponent[i2];
            Point point = lcomponent.getLocation();
            Dimension dimension = lcomponent.getSizeW();
            if((point.x + dimension.width > point1.x && point.y + dimension.height > point1.y && point.x < point1.x + dimension1.width && point.y < point1.y + dimension1.height || lcomponent.isEscape) && lcomponent.isVisible())
            {
                if(iScrollType == 0)
                {
                    int l = point.x - k1;
                    int i1 = point.y - l1;
                    int j1 = dimension.width;
                    int _tmp = dimension.height;
                    if(i > 0)
                    {
                        mi.m_paint(l - i, i1, i, dimension.height);
                    }
                    if(i < 0)
                    {
                        mi.m_paint(l + j1, i1, -i, dimension.height);
                    }
                    j1 += Math.abs(i);
                    if(j < 0)
                    {
                        mi.m_paint(l - i, i1 + dimension.height, j1, -j);
                    }
                    if(j > 0)
                    {
                        mi.m_paint(l - i, i1 - j, j1, j);
                    }
                } else
                {
                    boolean flag1 = lcomponent.isEscape;
                    lcomponent.escape(flag);
                    if(!flag1)
                    {
                        mi.m_paint(point.x - point1.x, point.y - point1.y, dimension.width, dimension.height);
                    }
                }
            }
        }

    }

    public void send(M m)
    {
        dd.send(m);
    }

    public void setARGB(int i)
    {
        i &= 0xffffff;
        tool.selPix(mi.info.m.iLayer != 0 && i == 0xffffff);
        if(mi.info.m.iPen != 4 && mi.info.m.iPen != 5)
        {
            tool.setARGB(mi.info.m.iAlpha << 24 | i);
        }
    }

    private void setDefComponent(Container container)
    {
        try
        {
            if(container == null)
            {
                return;
            }
            Color color = container.getForeground();
            Color color1 = container.getBackground();
            Component acomponent[] = container.getComponents();
            if(acomponent != null)
            {
                for(int i = 0; i < acomponent.length; i++)
                {
                    Component component = acomponent[i];
                    component.setBackground(color1);
                    component.setForeground(color);
                    if(component instanceof Container)
                    {
                        setDefComponent((Container)component);
                    }
                }

            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void setLineSize(int i)
    {
        tool.setLineSize(i);
    }

    private void typed()
    {
        try
        {
            String s = tField.getText();
            if(s == null || s.length() <= 0)
            {
                return;
            }
            tField.setText("");
            if(s.length() > 256)
            {
                mi.alert("longer it", false);
                return;
            }
            if(mi.info.m.isText())
            {
                mi.addText(s);
            } else
            {
                mgText.setData(0, (byte)0, s);
                dd.send(mgText);
                s = dd.strName + '>' + s;
                tText.addText(s, true);
                dSound(1);
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void undo(boolean flag)
    {
    }

    public void update(Graphics g)
    {
        paint(g);
    }
}
