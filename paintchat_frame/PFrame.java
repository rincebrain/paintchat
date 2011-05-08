package paintchat_frame;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.EventObject;
import java.util.Hashtable;
import paintchat.*;
import paintchat.debug.Debug;
import syi.applet.AppletWatcher;
import syi.applet.ServerStub;
import syi.awt.*;
import syi.util.*;

// Referenced classes of package paintchat_frame:
//            ConfigDialog, Console, Data, PopupMenuPaintChat, 
//            FileManager

public class PFrame extends Frame
    implements ActionListener, ItemListener, MouseListener, WindowListener, Runnable
{

    public static final String STR_VERSION = "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66";
    private Config config;
    private Res res;
    private Debug debug;
    private Panel ivjPanel3;
    private Panel ivjPanelLeft;
    private GridLayout ivjPanelLeftGridLayout;
    private Menu ivjMenu1;
    private FlowLayout ivjPanel3FlowLayout;
    private Menu ivjMenu2;
    private Menu ivjMenu3;
    private MenuItem ivjMenuItem1;
    private MenuItem ivjMenuItem2;
    private MenuItem ivjMenuItem3;
    private MenuItem ivjMenuItem4;
    private BorderLayout ivjPFrameBorderLayout;
    private MenuBar ivjPFrameMenuBar;
    private HelpWindow ivjHelp;
    private Console ivjConsole;
    private MenuItem ivjMenuItem6;
    private MenuItem ivjMenuHelpDocument;
    private CheckboxMenuItem ivjMenuShowConsole;
    private CheckboxMenuItem ivjMenuShowHelp;
    private Data ivjData;
    private LTextField ivjHttp_Port;
    private LTextField ivjIp;
    private LButton ivjLobby_Button;
    private LTextField ivjPaintchat_Port;
    private LButton ivjHttp_Button;
    private LButton ivjPaintchat_Button;
    private MenuItem ivjMenu_Help_Update;
    private MenuItem ivjMenuItem9;
    private MenuItem ivjMenuItem10;
    private MenuItem ivjMenu_FilesCopy;

    public PFrame()
    {
        debug = null;
        ivjPanel3 = null;
        ivjPanelLeft = null;
        ivjPanelLeftGridLayout = null;
        ivjMenu1 = null;
        ivjPanel3FlowLayout = null;
        ivjMenu2 = null;
        ivjMenu3 = null;
        ivjMenuItem1 = null;
        ivjMenuItem2 = null;
        ivjMenuItem3 = null;
        ivjMenuItem4 = null;
        ivjPFrameBorderLayout = null;
        ivjPFrameMenuBar = null;
        ivjHelp = null;
        ivjConsole = null;
        ivjMenuItem6 = null;
        ivjMenuHelpDocument = null;
        ivjMenuShowConsole = null;
        ivjMenuShowHelp = null;
        ivjData = null;
        ivjHttp_Port = null;
        ivjIp = null;
        ivjLobby_Button = null;
        ivjPaintchat_Port = null;
        ivjHttp_Button = null;
        ivjPaintchat_Button = null;
        ivjMenu_Help_Update = null;
        ivjMenuItem9 = null;
        ivjMenuItem10 = null;
        ivjMenu_FilesCopy = null;
        initialize();
    }

    public PFrame(String s)
    {
        super(s);
        debug = null;
        ivjPanel3 = null;
        ivjPanelLeft = null;
        ivjPanelLeftGridLayout = null;
        ivjMenu1 = null;
        ivjPanel3FlowLayout = null;
        ivjMenu2 = null;
        ivjMenu3 = null;
        ivjMenuItem1 = null;
        ivjMenuItem2 = null;
        ivjMenuItem3 = null;
        ivjMenuItem4 = null;
        ivjPFrameBorderLayout = null;
        ivjPFrameMenuBar = null;
        ivjHelp = null;
        ivjConsole = null;
        ivjMenuItem6 = null;
        ivjMenuHelpDocument = null;
        ivjMenuShowConsole = null;
        ivjMenuShowHelp = null;
        ivjData = null;
        ivjHttp_Port = null;
        ivjIp = null;
        ivjLobby_Button = null;
        ivjPaintchat_Port = null;
        ivjHttp_Button = null;
        ivjPaintchat_Button = null;
        ivjMenu_Help_Update = null;
        ivjMenuItem9 = null;
        ivjMenuItem10 = null;
        ivjMenu_FilesCopy = null;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getSource() == getMenuItem6())
        {
            connEtoC2(actionevent);
        }
        if(actionevent.getSource() == getMenuItem4())
        {
            connEtoC6(actionevent);
        }
        if(actionevent.getSource() == getMenuHelpDocument())
        {
            connEtoC7(actionevent);
        }
        if(actionevent.getSource() == getMenuItem1())
        {
            connEtoC11(actionevent);
        }
        if(actionevent.getSource() == getMenuItem2())
        {
            connEtoC12(actionevent);
        }
        if(actionevent.getSource() == getMenuItem9())
        {
            connEtoC16(actionevent);
        }
        if(actionevent.getSource() == getMenuItem10())
        {
            connEtoC18(actionevent);
        }
        if(actionevent.getSource() == getMenuItem3())
        {
            connEtoC9(actionevent);
        }
        if(actionevent.getSource() == getMenu_FilesCopy())
        {
            connEtoC13(actionevent);
        }
        if(actionevent.getSource() == getPaintchat_Port())
        {
            String s;
            try
            {
                s = actionevent.getActionCommand();
                Integer.parseInt(s);
            }
            catch(NumberFormatException _ex)
            {
                s = "41411";
            }
            getPaintchat_Port().setText(s);
            config.put("Connection_Port_PaintChat", s);
            config.saveConfig(null, null);
        }
        if(actionevent.getSource() == getHttp_Port())
        {
            String s1;
            try
            {
                s1 = actionevent.getActionCommand();
                Integer.parseInt(s1);
            }
            catch(NumberFormatException _ex)
            {
                s1 = "80";
            }
            getHttp_Port().setText(s1);
            config.put("Connection_Port_Http", s1);
            config.saveConfig(null, null);
        }
    }

    private void connEtoC1(WindowEvent windowevent)
    {
        try
        {
            destroy();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC11(ActionEvent actionevent)
    {
        try
        {
            startHttp();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC12(ActionEvent actionevent)
    {
        try
        {
            startServer();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC13(ActionEvent actionevent)
    {
        try
        {
            setupWWWFolder();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC16(ActionEvent actionevent)
    {
        try
        {
            startClient();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC17(WindowEvent windowevent)
    {
        try
        {
            pFrame_WindowClosed(windowevent);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC18(ActionEvent actionevent)
    {
        try
        {
            menuItem10_ActionPerformed1();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    public void connEtoC18_NormalResult(boolean flag)
    {
        try
        {
            if(flag)
            {
                new ConfigDialog("paintchat.config.PConfig", "cnf/dialogs.jar", config, res, "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66");
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void connEtoC2(ActionEvent actionevent)
    {
        try
        {
            menuItem6_ActionPerformed();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC3(MouseEvent mouseevent)
    {
        try
        {
            iP_MouseClicked();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC4(ItemEvent itemevent)
    {
        try
        {
            showConsole();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC5(ItemEvent itemevent)
    {
        try
        {
            showHelp();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC6(ActionEvent actionevent)
    {
        try
        {
            menuItem4_ActionPerformed();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC7(ActionEvent actionevent)
    {
        try
        {
            menuHelpDocument_ActionPerformed();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC8(WindowEvent windowevent)
    {
        try
        {
            pFrame_WindowIconified(windowevent);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC9(ActionEvent actionevent)
    {
        try
        {
            menuItem3_ActionPerformed(actionevent);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoM1(MouseEvent mouseevent)
    {
        try
        {
            getHelp().startHelp(new HelpWindowContent(mouseevent.getComponent().getName(), true, Gui.getScreenPos(mouseevent), res));
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoM2(MouseEvent mouseevent)
    {
        try
        {
            getHelp().startHelp(new HelpWindowContent(mouseevent.getComponent().getName(), true, Gui.getScreenPos(mouseevent), res));
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoM3(MouseEvent mouseevent)
    {
        try
        {
            getHelp().startHelp(new HelpWindowContent(mouseevent.getComponent().getName(), true, Gui.getScreenPos(mouseevent), res));
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoM4(MouseEvent mouseevent)
    {
        try
        {
            getHelp().startHelp(new HelpWindowContent(mouseevent.getComponent().getName(), true, Gui.getScreenPos(mouseevent), res));
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoM5(MouseEvent mouseevent)
    {
        try
        {
            getHelp().startHelp(new HelpWindowContent(mouseevent.getComponent().getName(), true, Gui.getScreenPos(mouseevent), res));
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoM6(MouseEvent mouseevent)
    {
        try
        {
            getHelp().startHelp(new HelpWindowContent(mouseevent.getComponent().getName(), true, Gui.getScreenPos(mouseevent), res));
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void destroy()
    {
        Thread thread = new Thread(this, "destroy");
        thread.start();
    }

    public Config getConfig()
        throws IOException
    {
        if(config == null)
        {
            config = new Config("cnf/paintchat.cf");
        }
        return config;
    }

    private Console getConsole()
    {
        if(ivjConsole == null)
        {
            try
            {
                ivjConsole = new Console();
                ivjConsole.setName("Console");
                ivjConsole.setBackground(Color.white);
                ivjConsole.setBounds(861, 328, 159, 60);
                ivjConsole.setForeground(Color.black);
                Applet applet = new Applet();
                applet.setStub(ServerStub.getDefaultStub(getConfig(), getResource()));
                ivjConsole.init(applet, 400, ivjConsole.getBackground(), ivjConsole.getForeground(), null);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjConsole;
    }

    private Data getData()
    {
        if(ivjData == null)
        {
            try
            {
                ivjData = new Data();
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjData;
    }

    private HelpWindow getHelp()
    {
        if(ivjHelp == null)
        {
            try
            {
                ivjHelp = new HelpWindow(this);
                ivjHelp.setName("Help");
                ivjHelp.setBounds(275, 283, 76, 75);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjHelp;
    }

    private LButton getHttp_Button()
    {
        if(ivjHttp_Button == null)
        {
            try
            {
                ivjHttp_Button = new LButton();
                ivjHttp_Button.setName("Http_Button");
                ivjHttp_Button.setForeground(new Color(80, 80, 120));
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjHttp_Button;
    }

    private LTextField getHttp_Port()
    {
        if(ivjHttp_Port == null)
        {
            try
            {
                ivjHttp_Port = new LTextField();
                ivjHttp_Port.setName("Http_Port");
                ivjHttp_Port.setText("80");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjHttp_Port;
    }

    private LTextField getIp()
    {
        if(ivjIp == null)
        {
            try
            {
                ivjIp = new LTextField();
                ivjIp.setName("Ip");
                ivjIp.setText("127.0.0.1");
                ivjIp.setEdit(false);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjIp;
    }

    private LButton getLobby_Button()
    {
        if(ivjLobby_Button == null)
        {
            try
            {
                ivjLobby_Button = new LButton();
                ivjLobby_Button.setName("Lobby_Button");
                ivjLobby_Button.setForeground(new Color(80, 80, 120));
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjLobby_Button;
    }

    private MenuItem getMenu_FilesCopy()
    {
        if(ivjMenu_FilesCopy == null)
        {
            try
            {
                ivjMenu_FilesCopy = new MenuItem();
                ivjMenu_FilesCopy.setLabel("Menu_FilesCopy");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenu_FilesCopy;
    }

    private MenuItem getMenu_Help_Update()
    {
        if(ivjMenu_Help_Update == null)
        {
            try
            {
                ivjMenu_Help_Update = new MenuItem();
                ivjMenu_Help_Update.setLabel("Menu_Help_Update");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenu_Help_Update;
    }

    private Menu getMenu1()
    {
        if(ivjMenu1 == null)
        {
            try
            {
                ivjMenu1 = new Menu();
                ivjMenu1.setFont(new Font("dialog", 0, 14));
                ivjMenu1.setActionCommand("Menu.Server");
                ivjMenu1.setLabel("Menu_Action");
                ivjMenu1.add(getMenuItem1());
                ivjMenu1.add(getMenuItem2());
                ivjMenu1.add(getMenuItem9());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenu1;
    }

    private Menu getMenu2()
    {
        if(ivjMenu2 == null)
        {
            try
            {
                ivjMenu2 = new Menu();
                ivjMenu2.setFont(new Font("dialog", 0, 14));
                ivjMenu2.setActionCommand("Menu.Option");
                ivjMenu2.setLabel("Menu_Option");
                ivjMenu2.add(getMenuItem3());
                ivjMenu2.add(getMenuItem10());
                ivjMenu2.add(getMenuItem4());
                ivjMenu2.add(getMenu_FilesCopy());
                ivjMenu2.add(getMenuShowConsole());
                ivjMenu2.add(getMenuShowHelp());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenu2;
    }

    private Menu getMenu3()
    {
        if(ivjMenu3 == null)
        {
            try
            {
                ivjMenu3 = new Menu();
                ivjMenu3.setFont(new Font("dialog", 0, 14));
                ivjMenu3.setActionCommand("Menu3");
                ivjMenu3.setLabel("Menu_Help");
                ivjMenu3.add(getMenu_Help_Update());
                ivjMenu3.add(getMenuHelpDocument());
                ivjMenu3.add(getMenuItem6());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenu3;
    }

    private MenuItem getMenuHelpDocument()
    {
        if(ivjMenuHelpDocument == null)
        {
            try
            {
                ivjMenuHelpDocument = new MenuItem();
                ivjMenuHelpDocument.setLabel("Menu_Help_Document");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenuHelpDocument;
    }

    private MenuItem getMenuItem1()
    {
        if(ivjMenuItem1 == null)
        {
            try
            {
                ivjMenuItem1 = new MenuItem();
                ivjMenuItem1.setActionCommand("Menu.Server.HTTP");
                ivjMenuItem1.setLabel("Menu_Action_HTTP");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenuItem1;
    }

    private MenuItem getMenuItem10()
    {
        if(ivjMenuItem10 == null)
        {
            try
            {
                ivjMenuItem10 = new MenuItem();
                ivjMenuItem10.setLabel("Menu_Option_Server");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenuItem10;
    }

    private MenuItem getMenuItem2()
    {
        if(ivjMenuItem2 == null)
        {
            try
            {
                ivjMenuItem2 = new MenuItem();
                ivjMenuItem2.setActionCommand("Menu.Server.PaintChat");
                ivjMenuItem2.setLabel("Menu_Action_PaintChat");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenuItem2;
    }

    private MenuItem getMenuItem3()
    {
        if(ivjMenuItem3 == null)
        {
            try
            {
                ivjMenuItem3 = new MenuItem();
                ivjMenuItem3.setActionCommand("Menu.Option.Config");
                ivjMenuItem3.setLabel("Menu_Option_Config");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenuItem3;
    }

    private MenuItem getMenuItem4()
    {
        if(ivjMenuItem4 == null)
        {
            try
            {
                ivjMenuItem4 = new MenuItem();
                ivjMenuItem4.setActionCommand("Menu.Option.Lobby");
                ivjMenuItem4.setLabel("Menu_Option_Lobby");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenuItem4;
    }

    private MenuItem getMenuItem6()
    {
        if(ivjMenuItem6 == null)
        {
            try
            {
                ivjMenuItem6 = new MenuItem();
                ivjMenuItem6.setLabel("Menu_Help_About");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenuItem6;
    }

    private MenuItem getMenuItem9()
    {
        if(ivjMenuItem9 == null)
        {
            try
            {
                ivjMenuItem9 = new MenuItem();
                ivjMenuItem9.setLabel("Menu_Action_Client");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenuItem9;
    }

    private CheckboxMenuItem getMenuShowConsole()
    {
        if(ivjMenuShowConsole == null)
        {
            try
            {
                ivjMenuShowConsole = new CheckboxMenuItem();
                ivjMenuShowConsole.setLabel("Menu_Option_ShowConsole");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenuShowConsole;
    }

    private CheckboxMenuItem getMenuShowHelp()
    {
        if(ivjMenuShowHelp == null)
        {
            try
            {
                ivjMenuShowHelp = new CheckboxMenuItem();
                ivjMenuShowHelp.setLabel("Menu_Option_ShowHelp");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjMenuShowHelp;
    }

    private LButton getPaintchat_Button()
    {
        if(ivjPaintchat_Button == null)
        {
            try
            {
                ivjPaintchat_Button = new LButton();
                ivjPaintchat_Button.setName("Paintchat_Button");
                ivjPaintchat_Button.setForeground(new Color(80, 80, 120));
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjPaintchat_Button;
    }

    private LTextField getPaintchat_Port()
    {
        if(ivjPaintchat_Port == null)
        {
            try
            {
                ivjPaintchat_Port = new LTextField();
                ivjPaintchat_Port.setName("Paintchat_Port");
                ivjPaintchat_Port.setText("0");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjPaintchat_Port;
    }

    private Panel getPanel3()
    {
        if(ivjPanel3 == null)
        {
            try
            {
                ivjPanel3 = new Panel();
                ivjPanel3.setName("Panel3");
                ivjPanel3.setLayout(getPanel3FlowLayout());
                ivjPanel3.setBackground(new Color(204, 204, 204));
                ivjPanel3.setForeground(new Color(80, 80, 120));
                getPanel3().add(getPanelLeft(), getPanelLeft().getName());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjPanel3;
    }

    private FlowLayout getPanel3FlowLayout()
    {
        FlowLayout flowlayout = null;
        try
        {
            flowlayout = new FlowLayout();
            flowlayout.setAlignment(1);
            flowlayout.setVgap(5);
            flowlayout.setHgap(5);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
        return flowlayout;
    }

    private Panel getPanelLeft()
    {
        if(ivjPanelLeft == null)
        {
            try
            {
                ivjPanelLeft = new Panel();
                ivjPanelLeft.setName("PanelLeft");
                ivjPanelLeft.setLayout(getPanelLeftGridLayout());
                ivjPanelLeft.setBackground(new Color(204, 204, 204));
                ivjPanelLeft.setForeground(new Color(80, 80, 120));
                getPanelLeft().add(getIp(), getIp().getName());
                getPanelLeft().add(getHttp_Port(), getHttp_Port().getName());
                getPanelLeft().add(getPaintchat_Port(), getPaintchat_Port().getName());
                getPanelLeft().add(getPaintchat_Button(), getPaintchat_Button().getName());
                getPanelLeft().add(getHttp_Button(), getHttp_Button().getName());
                getPanelLeft().add(getLobby_Button(), getLobby_Button().getName());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjPanelLeft;
    }

    private GridLayout getPanelLeftGridLayout()
    {
        GridLayout gridlayout = null;
        try
        {
            gridlayout = new GridLayout(0, 1);
            gridlayout.setVgap(3);
            gridlayout.setHgap(0);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
        return gridlayout;
    }

    private BorderLayout getPFrameBorderLayout()
    {
        BorderLayout borderlayout = null;
        try
        {
            borderlayout = new BorderLayout();
            borderlayout.setVgap(0);
            borderlayout.setHgap(0);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
        return borderlayout;
    }

    private MenuBar getPFrameMenuBar()
    {
        if(ivjPFrameMenuBar == null)
        {
            try
            {
                ivjPFrameMenuBar = new MenuBar();
                ivjPFrameMenuBar.setHelpMenu(getMenu3());
                ivjPFrameMenuBar.add(getMenu2());
                ivjPFrameMenuBar.add(getMenu1());
                ivjPFrameMenuBar.add(getMenu3());
                MenuBar menubar = ivjPFrameMenuBar;
                int i = menubar.getMenuCount();
                for(int j = 0; j < i; j++)
                {
                    setResource(menubar.getMenu(j));
                }

            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjPFrameMenuBar;
    }

    private Res getResource()
    {
        if(res == null)
        {
            Resource.loadResource();
            res = Resource.loadResource("Application");
        }
        return res;
    }

    private void handleException(Throwable throwable)
    {
    }

    public void init()
    {
        Thread thread = new Thread(this, "init");
        thread.setPriority(1);
        thread.setDaemon(false);
        thread.start();
    }

    private void initConnections()
        throws Exception
    {
        getPaintchat_Port().addActionListener(this);
        getHttp_Port().addActionListener(this);
        getIp().addMouseListener(this);
        getHttp_Port().addMouseListener(this);
        getPaintchat_Port().addMouseListener(this);
        getPaintchat_Button().addMouseListener(this);
        getHttp_Button().addMouseListener(this);
        getLobby_Button().addMouseListener(this);
        getMenuItem6().addActionListener(this);
        addWindowListener(this);
        getMenuShowConsole().addItemListener(this);
        getMenuShowHelp().addItemListener(this);
        getMenuItem4().addActionListener(this);
        getMenuHelpDocument().addActionListener(this);
        getMenu_Help_Update().addActionListener(this);
        getMenuItem1().addActionListener(this);
        getMenuItem2().addActionListener(this);
        getMenuItem9().addActionListener(this);
        getMenuItem10().addActionListener(this);
        getMenuItem3().addActionListener(this);
        getMenu_FilesCopy().addActionListener(this);
    }

    private void initialize()
    {
        try
        {
            setName("PFrame");
            setLayout(getPFrameBorderLayout());
            setBackground(new Color(204, 204, 204));
            setForeground(new Color(80, 80, 120));
            setMenuBar(getPFrameMenuBar());
            setBounds(new Rectangle(0, 0, 391, 172));
            setSize(391, 172);
            setTitle("PaintChat");
            add(getPanel3(), "West");
            initConnections();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    public void iP_MouseClicked()
    {
        PopupMenuPaintChat popupmenupaintchat = new PopupMenuPaintChat(debug, config, res);
        popupmenupaintchat.show(this, ivjIp, 0, 0);
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
        if(itemevent.getSource() == getMenuShowConsole())
        {
            connEtoC4(itemevent);
        }
        if(itemevent.getSource() == getMenuShowHelp())
        {
            connEtoC5(itemevent);
        }
    }

    public static void main(String args[])
    {
        try
        {
            PFrame pframe = new PFrame();
            pframe.init();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace(System.out);
            System.exit(0);
        }
    }

    public void menuHelpDocument_ActionPerformed()
    {
        Gui.showDocument("Help.html", config, res);
    }

    public void menuItem1_ActionPerformed1()
    {
        ThreadPool.poolStartThread(getData(), 'h');
    }

    public void menuItem10_ActionPerformed(ActionEvent actionevent)
    {
        try
        {
            new ConfigDialog("paintchat.config.ConfigServer", "cnf/dialogs.jar", config, res, "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66");
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void menuItem10_ActionPerformed1()
    {
        if(getData().isRunPaintChatServer() && MessageBox.confirm("ConfirmMayServerStopNow", "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66"))
        {
            getData().startPaintChat(false);
        }
        try
        {
            new ConfigDialog("paintchat.config.ConfigServer", "cnf/dialogs.jar", config, res, "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66");
        }
        catch(Throwable _ex) { }
    }

    public void menuItem3_ActionPerformed(ActionEvent actionevent)
    {
        try
        {
            new ConfigDialog("paintchat.config.PConfig", "cnf/dialogs.jar", config, res, "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66");
        }
        catch(Throwable throwable)
        {
            debug.log(throwable.getMessage());
        }
    }

    public void menuItem4_ActionPerformed()
    {
        try
        {
            new ConfigDialog("paintchat.config.Ao", "cnf/dialogs.jar", config, res, "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66");
        }
        catch(Throwable throwable)
        {
            debug.log(throwable.getMessage());
        }
    }

    public void menuItem6_ActionPerformed()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66");
        stringbuffer.append('\n');
        stringbuffer.append('\n');
        stringbuffer.append("JavaVirtualMachine(JVM):");
        stringbuffer.append(System.getProperty("java.vendor"));
        stringbuffer.append("\nJVM Version:");
        stringbuffer.append(System.getProperty("java.version"));
        stringbuffer.append('\n');
        stringbuffer.append("\n\u7ACB\u3061\u4E0A\u3052\u753B\u9762\u306ECG\u306Fuzuki\u3055\u3093\u304C\u4F5C" +
"\u6210\u3057\u307E\u3057\u305F\u3002\nuzuki\u3055\u3093\u306EHP http://www19.fre" +
"eweb.ne.jp/play/m_uzuki/top.htm\n\u52B9\u679C\u97F3\u306F\u3042\u3084\u3084\u3055" +
"\u3093\u304C\u4F5C\u6210\u3057\u307E\u3057\u305F\u3002\n\u30ED\u30D3\u30FC\u30D7" +
"\u30ED\u30B0\u30E9\u30E0\u306F\u85CD\u73E0\u3055\u3093\u304C\u4F5C\u6210\u3001\u7BA1" +
"\u7406\u3057\u3066\u3044\u307E\u3059\u3002"
);
        MessageBox.alert(stringbuffer.toString(), "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66");
    }

    public void mouseClicked(MouseEvent mouseevent)
    {
        if(mouseevent.getSource() == getIp())
        {
            connEtoC3(mouseevent);
        }
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
        if(mouseevent.getSource() == getIp())
        {
            connEtoM1(mouseevent);
        }
        if(mouseevent.getSource() == getHttp_Port())
        {
            connEtoM2(mouseevent);
        }
        if(mouseevent.getSource() == getPaintchat_Port())
        {
            connEtoM3(mouseevent);
        }
        if(mouseevent.getSource() == getPaintchat_Button())
        {
            connEtoM4(mouseevent);
        }
        if(mouseevent.getSource() == getHttp_Button())
        {
            connEtoM5(mouseevent);
        }
        if(mouseevent.getSource() == getLobby_Button())
        {
            connEtoM6(mouseevent);
        }
    }

    public void mouseExited(MouseEvent mouseevent)
    {
        getHelp().reset();
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void panel3_MouseReleased(MouseEvent mouseevent)
    {
        MenuBar menubar = getMenuBar();
        if(ivjMenuShowConsole.getState() || menubar != null)
        {
            return;
        } else
        {
            MenuBar menubar1 = getPFrameMenuBar();
            menubar1.remove(0);
            menubar1.remove(0);
            menubar1.remove(0);
            PopupMenu popupmenu = new PopupMenu();
            popupmenu.addActionListener(this);
            popupmenu.add(getMenu1());
            popupmenu.add(getMenu2());
            popupmenu.add(getMenu3());
            add(popupmenu);
            popupmenu.show(mouseevent.getComponent(), mouseevent.getX(), mouseevent.getY());
            popupmenu.removeAll();
            menubar1.add(getMenu1());
            menubar1.add(getMenu2());
            menubar1.add(getMenu3());
            return;
        }
    }

    public void pFrame_WindowClosed(WindowEvent windowevent)
    {
        System.exit(0);
    }

    public void pFrame_WindowIconified(WindowEvent windowevent)
    {
        if(getData().getIsNativeWindows())
        {
            setVisible(false);
        }
    }

    private void rDestroy()
    {
        try
        {
            config.put("Connection_Port_Http", getHttp_Port().getText().trim());
            config.put("Connection_Port_PaintChat", getPaintchat_Port().getText().trim());
            config.saveConfig(null, null);
            if(getData().getIsNativeWindows())
            {
                getData().exitWin();
            }
            dispose();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void rInit()
    {
        try
        {
            System.currentTimeMillis();
            getResource();
            setResource(res, this);
            Awt.setPFrame(this);
            Awt.cBk = getBackground();
            Awt.cFore = getForeground();
            getConfig();
            getHttp_Port().setText(config.getString("Connection_Port_Http", "80"));
            getPaintchat_Port().setText(config.getString("Connection_Port_PaintChat", "0"));
            Image image = Io.loadImageNow(this, "cnf/template/top.jpg");
            Dimension dimension = getToolkit().getScreenSize();
            Point point = new Point((dimension.width - image.getWidth(null)) / 2, (dimension.height - image.getHeight(null)) / 2);
            getHelp().setForeground(Color.black);
            HelpWindowContent helpwindowcontent = new HelpWindowContent(image, "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66", false, point, null);
            helpwindowcontent.timeStart = 0;
            helpwindowcontent.timeEnd = 3500;
            helpwindowcontent.setVisit(true);
            getHelp().startHelp(helpwindowcontent);
            MessageBox.setResource(res);
            debug = new Debug(res);
            setIconImage(Io.loadImageNow(this, "cnf/icon.gif"));
            setTitle("PaintChat_MainWindow");
            if(config.getBool("App_IsConsole"))
            {
                showConsole();
                ivjConsole.addText("(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66");
                ivjConsole.addText("http://shichan.jp/");
                ivjConsole.addText(" ");
                if(config.getBool("App_ShowStartHelp", true))
                {
                    ivjConsole.setRText(res.get("StartHelp"));
                    ivjConsole.addText(" ");
                }
            }
            res.remove("StartHelp");
            if(config.getBool("App_ShowHelp", true))
            {
                showHelp();
            }
            if(!config.getString("App_Version").equals("(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66"))
            {
                (new FileManager(config)).templateToWWW();
                config.put("App_Version", "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66");
                debug.log("Client update.");
            }
            getIp().setText(PopupMenuPaintChat.getAddress(config, debug));
            getData().init(config, res, debug, getPaintchat_Button(), getHttp_Button(), getLobby_Button());
            pack();
            Awt.moveCenter(this);
            setVisible(true);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void run()
    {
        try
        {
            switch(Thread.currentThread().getName().charAt(0))
            {
            case 100: // 'd'
                rDestroy();
                break;

            case 105: // 'i'
                rInit();
                break;
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void setResource(MenuItem menuitem)
    {
        menuitem.setLabel(getResource().get(menuitem.getLabel(), menuitem.getLabel()));
        if(menuitem instanceof Menu)
        {
            Menu menu = (Menu)menuitem;
            int i = menu.getItemCount();
            for(int j = 0; j < i; j++)
            {
                setResource(menu.getItem(j));
            }

        }
    }

    public void setResource(Res res1, Component component)
    {
        if(component instanceof Container)
        {
            Container container = (Container)component;
            int i = container.getComponentCount();
            for(int j = 0; j < i; j++)
            {
                setResource(res1, container.getComponent(j));
            }

        } else
        {
            String s = res1.get(component.getName(), component.getName());
            if(component instanceof LTextField)
            {
                LTextField ltextfield = (LTextField)component;
                ltextfield.setTitle(s);
                return;
            }
            if(component instanceof LButton)
            {
                LButton lbutton = (LButton)component;
                lbutton.setText(s);
                return;
            }
            if(component instanceof Checkbox)
            {
                Checkbox checkbox = (Checkbox)component;
                checkbox.setLabel(s);
                return;
            }
        }
    }

    public void setupWWWFolder()
    {
        (new FileManager(config)).templateToWWW();
    }

    public void showConsole()
    {
        Console console = getConsole();
        boolean flag = console.getParent() == this;
        if(flag)
        {
            remove(console);
            console.stop();
            MenuBar menubar = getMenuBar();
            menubar.remove(getMenu1());
            menubar.remove(getMenu3());
        } else
        {
            console.start(debug);
            add(console, "Center");
            MenuBar menubar1 = getPFrameMenuBar();
            menubar1.add(getMenu1());
            menubar1.add(getMenu3());
        }
        config.put("App_IsConsole", String.valueOf(!flag));
        ivjMenuShowConsole.setState(!flag);
        pack();
    }

    public void showHelp()
    {
        boolean flag = getHelp().getIsShow();
        getHelp().setIsShow(!flag);
        getMenuShowHelp().setState(!flag);
        config.put("App_ShowHelp", String.valueOf(!flag));
    }

    public void startClient()
    {
        try
        {
            AppletWatcher appletwatcher = new AppletWatcher("paintchat_client.Client", "(C)\u3057\u3043\u3061\u3083\u3093 PaintChatApp v3.66", config, res, false);
            appletwatcher.setIconImage(getIconImage());
            appletwatcher.show();
        }
        catch(Throwable throwable)
        {
            debug.log(throwable.getMessage());
        }
    }

    public void startHttp()
    {
        getData().startHttp(true);
    }

    public void startServer()
    {
        getData().startPaintChat(true);
    }

    public void startViewer()
    {
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
        if(windowevent.getSource() == this)
        {
            connEtoC17(windowevent);
        }
    }

    public void windowClosing(WindowEvent windowevent)
    {
        if(windowevent.getSource() == this)
        {
            connEtoC1(windowevent);
        }
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
        setVisible(true);
    }

    public void windowIconified(WindowEvent windowevent)
    {
        if(windowevent.getSource() == this)
        {
            connEtoC8(windowevent);
        }
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }
}
