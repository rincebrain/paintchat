package paintchat_frame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuComponent;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.EventObject;
import java.util.Hashtable;
import paintchat.Config;
import paintchat.Res;
import paintchat.Resource;
import paintchat.debug.Debug;
import syi.applet.AppletWatcher;
import syi.applet.ServerStub;
import syi.awt.Awt;
import syi.awt.Gui;
import syi.awt.HelpWindow;
import syi.awt.HelpWindowContent;
import syi.awt.LButton;
import syi.awt.LTextField;
import syi.awt.MessageBox;
import syi.awt.TextPanel;
import syi.util.Io;
import syi.util.PProperties;
import syi.util.ThreadPool;

public class PFrame extends Frame
  implements ActionListener, ItemListener, MouseListener, WindowListener, Runnable
{
  public static final String STR_VERSION = "(C)しぃちゃん PaintChatApp v3.66";
  private Config config;
  private Res res;
  private Debug debug = null;
  private Panel ivjPanel3 = null;
  private Panel ivjPanelLeft = null;
  private GridLayout ivjPanelLeftGridLayout = null;
  private Menu ivjMenu1 = null;
  private FlowLayout ivjPanel3FlowLayout = null;
  private Menu ivjMenu2 = null;
  private Menu ivjMenu3 = null;
  private MenuItem ivjMenuItem1 = null;
  private MenuItem ivjMenuItem2 = null;
  private MenuItem ivjMenuItem3 = null;
  private MenuItem ivjMenuItem4 = null;
  private BorderLayout ivjPFrameBorderLayout = null;
  private MenuBar ivjPFrameMenuBar = null;
  private HelpWindow ivjHelp = null;
  private Console ivjConsole = null;
  private MenuItem ivjMenuItem6 = null;
  private MenuItem ivjMenuHelpDocument = null;
  private CheckboxMenuItem ivjMenuShowConsole = null;
  private CheckboxMenuItem ivjMenuShowHelp = null;
  private Data ivjData = null;
  private LTextField ivjHttp_Port = null;
  private LTextField ivjIp = null;
  private LButton ivjLobby_Button = null;
  private LTextField ivjPaintchat_Port = null;
  private LButton ivjHttp_Button = null;
  private LButton ivjPaintchat_Button = null;
  private MenuItem ivjMenu_Help_Update = null;
  private MenuItem ivjMenuItem9 = null;
  private MenuItem ivjMenuItem10 = null;
  private MenuItem ivjMenu_FilesCopy = null;

  public PFrame()
  {
    initialize();
  }

  public PFrame(String paramString)
  {
    super(paramString);
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    if (paramActionEvent.getSource() == getMenuItem6())
      connEtoC2(paramActionEvent);
    if (paramActionEvent.getSource() == getMenuItem4())
      connEtoC6(paramActionEvent);
    if (paramActionEvent.getSource() == getMenuHelpDocument())
      connEtoC7(paramActionEvent);
    if (paramActionEvent.getSource() == getMenuItem1())
      connEtoC11(paramActionEvent);
    if (paramActionEvent.getSource() == getMenuItem2())
      connEtoC12(paramActionEvent);
    if (paramActionEvent.getSource() == getMenuItem9())
      connEtoC16(paramActionEvent);
    if (paramActionEvent.getSource() == getMenuItem10())
      connEtoC18(paramActionEvent);
    if (paramActionEvent.getSource() == getMenuItem3())
      connEtoC9(paramActionEvent);
    if (paramActionEvent.getSource() == getMenu_FilesCopy())
      connEtoC13(paramActionEvent);
    String str;
    if (paramActionEvent.getSource() == getPaintchat_Port())
    {
      try
      {
        str = paramActionEvent.getActionCommand();
        Integer.parseInt(str);
      }
      catch (NumberFormatException localNumberFormatException1)
      {
        str = "41411";
      }
      getPaintchat_Port().setText(str);
      this.config.put("Connection_Port_PaintChat", str);
      this.config.saveConfig(null, null);
    }
    if (paramActionEvent.getSource() == getHttp_Port())
    {
      try
      {
        str = paramActionEvent.getActionCommand();
        Integer.parseInt(str);
      }
      catch (NumberFormatException localNumberFormatException2)
      {
        str = "80";
      }
      getHttp_Port().setText(str);
      this.config.put("Connection_Port_Http", str);
      this.config.saveConfig(null, null);
    }
  }

  private void connEtoC1(WindowEvent paramWindowEvent)
  {
    try
    {
      destroy();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC11(ActionEvent paramActionEvent)
  {
    try
    {
      startHttp();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC12(ActionEvent paramActionEvent)
  {
    try
    {
      startServer();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC13(ActionEvent paramActionEvent)
  {
    try
    {
      setupWWWFolder();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC16(ActionEvent paramActionEvent)
  {
    try
    {
      startClient();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC17(WindowEvent paramWindowEvent)
  {
    try
    {
      pFrame_WindowClosed(paramWindowEvent);
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC18(ActionEvent paramActionEvent)
  {
    try
    {
      menuItem10_ActionPerformed1();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  public void connEtoC18_NormalResult(boolean paramBoolean)
  {
    try
    {
      if (paramBoolean)
        new ConfigDialog("paintchat.config.PConfig", "cnf/dialogs.jar", this.config, this.res, "(C)しぃちゃん PaintChatApp v3.66");
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  private void connEtoC2(ActionEvent paramActionEvent)
  {
    try
    {
      menuItem6_ActionPerformed();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC3(MouseEvent paramMouseEvent)
  {
    try
    {
      iP_MouseClicked();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC4(ItemEvent paramItemEvent)
  {
    try
    {
      showConsole();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC5(ItemEvent paramItemEvent)
  {
    try
    {
      showHelp();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC6(ActionEvent paramActionEvent)
  {
    try
    {
      menuItem4_ActionPerformed();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC7(ActionEvent paramActionEvent)
  {
    try
    {
      menuHelpDocument_ActionPerformed();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC8(WindowEvent paramWindowEvent)
  {
    try
    {
      pFrame_WindowIconified(paramWindowEvent);
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC9(ActionEvent paramActionEvent)
  {
    try
    {
      menuItem3_ActionPerformed(paramActionEvent);
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoM1(MouseEvent paramMouseEvent)
  {
    try
    {
      getHelp().startHelp(new HelpWindowContent(paramMouseEvent.getComponent().getName(), true, Gui.getScreenPos(paramMouseEvent), this.res));
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoM2(MouseEvent paramMouseEvent)
  {
    try
    {
      getHelp().startHelp(new HelpWindowContent(paramMouseEvent.getComponent().getName(), true, Gui.getScreenPos(paramMouseEvent), this.res));
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoM3(MouseEvent paramMouseEvent)
  {
    try
    {
      getHelp().startHelp(new HelpWindowContent(paramMouseEvent.getComponent().getName(), true, Gui.getScreenPos(paramMouseEvent), this.res));
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoM4(MouseEvent paramMouseEvent)
  {
    try
    {
      getHelp().startHelp(new HelpWindowContent(paramMouseEvent.getComponent().getName(), true, Gui.getScreenPos(paramMouseEvent), this.res));
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoM5(MouseEvent paramMouseEvent)
  {
    try
    {
      getHelp().startHelp(new HelpWindowContent(paramMouseEvent.getComponent().getName(), true, Gui.getScreenPos(paramMouseEvent), this.res));
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoM6(MouseEvent paramMouseEvent)
  {
    try
    {
      getHelp().startHelp(new HelpWindowContent(paramMouseEvent.getComponent().getName(), true, Gui.getScreenPos(paramMouseEvent), this.res));
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void destroy()
  {
    Thread localThread = new Thread(this, "destroy");
    localThread.start();
  }

  public Config getConfig()
    throws IOException
  {
    if (this.config == null)
      this.config = new Config("cnf/paintchat.cf");
    return this.config;
  }

  private Console getConsole()
  {
    if (this.ivjConsole == null)
      try
      {
        this.ivjConsole = new Console();
        this.ivjConsole.setName("Console");
        this.ivjConsole.setBackground(Color.white);
        this.ivjConsole.setBounds(861, 328, 159, 60);
        this.ivjConsole.setForeground(Color.black);
        Applet localApplet = new Applet();
        localApplet.setStub(ServerStub.getDefaultStub(getConfig(), getResource()));
        this.ivjConsole.init(localApplet, 400, this.ivjConsole.getBackground(), this.ivjConsole.getForeground(), null);
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjConsole;
  }

  private Data getData()
  {
    if (this.ivjData == null)
      try
      {
        this.ivjData = new Data();
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjData;
  }

  private HelpWindow getHelp()
  {
    if (this.ivjHelp == null)
      try
      {
        this.ivjHelp = new HelpWindow(this);
        this.ivjHelp.setName("Help");
        this.ivjHelp.setBounds(275, 283, 76, 75);
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjHelp;
  }

  private LButton getHttp_Button()
  {
    if (this.ivjHttp_Button == null)
      try
      {
        this.ivjHttp_Button = new LButton();
        this.ivjHttp_Button.setName("Http_Button");
        this.ivjHttp_Button.setForeground(new Color(80, 80, 120));
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjHttp_Button;
  }

  private LTextField getHttp_Port()
  {
    if (this.ivjHttp_Port == null)
      try
      {
        this.ivjHttp_Port = new LTextField();
        this.ivjHttp_Port.setName("Http_Port");
        this.ivjHttp_Port.setText("80");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjHttp_Port;
  }

  private LTextField getIp()
  {
    if (this.ivjIp == null)
      try
      {
        this.ivjIp = new LTextField();
        this.ivjIp.setName("Ip");
        this.ivjIp.setText("127.0.0.1");
        this.ivjIp.setEdit(false);
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjIp;
  }

  private LButton getLobby_Button()
  {
    if (this.ivjLobby_Button == null)
      try
      {
        this.ivjLobby_Button = new LButton();
        this.ivjLobby_Button.setName("Lobby_Button");
        this.ivjLobby_Button.setForeground(new Color(80, 80, 120));
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjLobby_Button;
  }

  private MenuItem getMenu_FilesCopy()
  {
    if (this.ivjMenu_FilesCopy == null)
      try
      {
        this.ivjMenu_FilesCopy = new MenuItem();
        this.ivjMenu_FilesCopy.setLabel("Menu_FilesCopy");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenu_FilesCopy;
  }

  private MenuItem getMenu_Help_Update()
  {
    if (this.ivjMenu_Help_Update == null)
      try
      {
        this.ivjMenu_Help_Update = new MenuItem();
        this.ivjMenu_Help_Update.setLabel("Menu_Help_Update");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenu_Help_Update;
  }

  private Menu getMenu1()
  {
    if (this.ivjMenu1 == null)
      try
      {
        this.ivjMenu1 = new Menu();
        this.ivjMenu1.setFont(new Font("dialog", 0, 14));
        this.ivjMenu1.setActionCommand("Menu.Server");
        this.ivjMenu1.setLabel("Menu_Action");
        this.ivjMenu1.add(getMenuItem1());
        this.ivjMenu1.add(getMenuItem2());
        this.ivjMenu1.add(getMenuItem9());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenu1;
  }

  private Menu getMenu2()
  {
    if (this.ivjMenu2 == null)
      try
      {
        this.ivjMenu2 = new Menu();
        this.ivjMenu2.setFont(new Font("dialog", 0, 14));
        this.ivjMenu2.setActionCommand("Menu.Option");
        this.ivjMenu2.setLabel("Menu_Option");
        this.ivjMenu2.add(getMenuItem3());
        this.ivjMenu2.add(getMenuItem10());
        this.ivjMenu2.add(getMenuItem4());
        this.ivjMenu2.add(getMenu_FilesCopy());
        this.ivjMenu2.add(getMenuShowConsole());
        this.ivjMenu2.add(getMenuShowHelp());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenu2;
  }

  private Menu getMenu3()
  {
    if (this.ivjMenu3 == null)
      try
      {
        this.ivjMenu3 = new Menu();
        this.ivjMenu3.setFont(new Font("dialog", 0, 14));
        this.ivjMenu3.setActionCommand("Menu3");
        this.ivjMenu3.setLabel("Menu_Help");
        this.ivjMenu3.add(getMenu_Help_Update());
        this.ivjMenu3.add(getMenuHelpDocument());
        this.ivjMenu3.add(getMenuItem6());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenu3;
  }

  private MenuItem getMenuHelpDocument()
  {
    if (this.ivjMenuHelpDocument == null)
      try
      {
        this.ivjMenuHelpDocument = new MenuItem();
        this.ivjMenuHelpDocument.setLabel("Menu_Help_Document");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenuHelpDocument;
  }

  private MenuItem getMenuItem1()
  {
    if (this.ivjMenuItem1 == null)
      try
      {
        this.ivjMenuItem1 = new MenuItem();
        this.ivjMenuItem1.setActionCommand("Menu.Server.HTTP");
        this.ivjMenuItem1.setLabel("Menu_Action_HTTP");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenuItem1;
  }

  private MenuItem getMenuItem10()
  {
    if (this.ivjMenuItem10 == null)
      try
      {
        this.ivjMenuItem10 = new MenuItem();
        this.ivjMenuItem10.setLabel("Menu_Option_Server");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenuItem10;
  }

  private MenuItem getMenuItem2()
  {
    if (this.ivjMenuItem2 == null)
      try
      {
        this.ivjMenuItem2 = new MenuItem();
        this.ivjMenuItem2.setActionCommand("Menu.Server.PaintChat");
        this.ivjMenuItem2.setLabel("Menu_Action_PaintChat");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenuItem2;
  }

  private MenuItem getMenuItem3()
  {
    if (this.ivjMenuItem3 == null)
      try
      {
        this.ivjMenuItem3 = new MenuItem();
        this.ivjMenuItem3.setActionCommand("Menu.Option.Config");
        this.ivjMenuItem3.setLabel("Menu_Option_Config");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenuItem3;
  }

  private MenuItem getMenuItem4()
  {
    if (this.ivjMenuItem4 == null)
      try
      {
        this.ivjMenuItem4 = new MenuItem();
        this.ivjMenuItem4.setActionCommand("Menu.Option.Lobby");
        this.ivjMenuItem4.setLabel("Menu_Option_Lobby");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenuItem4;
  }

  private MenuItem getMenuItem6()
  {
    if (this.ivjMenuItem6 == null)
      try
      {
        this.ivjMenuItem6 = new MenuItem();
        this.ivjMenuItem6.setLabel("Menu_Help_About");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenuItem6;
  }

  private MenuItem getMenuItem9()
  {
    if (this.ivjMenuItem9 == null)
      try
      {
        this.ivjMenuItem9 = new MenuItem();
        this.ivjMenuItem9.setLabel("Menu_Action_Client");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenuItem9;
  }

  private CheckboxMenuItem getMenuShowConsole()
  {
    if (this.ivjMenuShowConsole == null)
      try
      {
        this.ivjMenuShowConsole = new CheckboxMenuItem();
        this.ivjMenuShowConsole.setLabel("Menu_Option_ShowConsole");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenuShowConsole;
  }

  private CheckboxMenuItem getMenuShowHelp()
  {
    if (this.ivjMenuShowHelp == null)
      try
      {
        this.ivjMenuShowHelp = new CheckboxMenuItem();
        this.ivjMenuShowHelp.setLabel("Menu_Option_ShowHelp");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjMenuShowHelp;
  }

  private LButton getPaintchat_Button()
  {
    if (this.ivjPaintchat_Button == null)
      try
      {
        this.ivjPaintchat_Button = new LButton();
        this.ivjPaintchat_Button.setName("Paintchat_Button");
        this.ivjPaintchat_Button.setForeground(new Color(80, 80, 120));
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjPaintchat_Button;
  }

  private LTextField getPaintchat_Port()
  {
    if (this.ivjPaintchat_Port == null)
      try
      {
        this.ivjPaintchat_Port = new LTextField();
        this.ivjPaintchat_Port.setName("Paintchat_Port");
        this.ivjPaintchat_Port.setText("0");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjPaintchat_Port;
  }

  private Panel getPanel3()
  {
    if (this.ivjPanel3 == null)
      try
      {
        this.ivjPanel3 = new Panel();
        this.ivjPanel3.setName("Panel3");
        this.ivjPanel3.setLayout(getPanel3FlowLayout());
        this.ivjPanel3.setBackground(new Color(204, 204, 204));
        this.ivjPanel3.setForeground(new Color(80, 80, 120));
        getPanel3().add(getPanelLeft(), getPanelLeft().getName());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjPanel3;
  }

  private FlowLayout getPanel3FlowLayout()
  {
    FlowLayout localFlowLayout = null;
    try
    {
      localFlowLayout = new FlowLayout();
      localFlowLayout.setAlignment(1);
      localFlowLayout.setVgap(5);
      localFlowLayout.setHgap(5);
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
    return localFlowLayout;
  }

  private Panel getPanelLeft()
  {
    if (this.ivjPanelLeft == null)
      try
      {
        this.ivjPanelLeft = new Panel();
        this.ivjPanelLeft.setName("PanelLeft");
        this.ivjPanelLeft.setLayout(getPanelLeftGridLayout());
        this.ivjPanelLeft.setBackground(new Color(204, 204, 204));
        this.ivjPanelLeft.setForeground(new Color(80, 80, 120));
        getPanelLeft().add(getIp(), getIp().getName());
        getPanelLeft().add(getHttp_Port(), getHttp_Port().getName());
        getPanelLeft().add(getPaintchat_Port(), getPaintchat_Port().getName());
        getPanelLeft().add(getPaintchat_Button(), getPaintchat_Button().getName());
        getPanelLeft().add(getHttp_Button(), getHttp_Button().getName());
        getPanelLeft().add(getLobby_Button(), getLobby_Button().getName());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjPanelLeft;
  }

  private GridLayout getPanelLeftGridLayout()
  {
    GridLayout localGridLayout = null;
    try
    {
      localGridLayout = new GridLayout(0, 1);
      localGridLayout.setVgap(3);
      localGridLayout.setHgap(0);
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
    return localGridLayout;
  }

  private BorderLayout getPFrameBorderLayout()
  {
    BorderLayout localBorderLayout = null;
    try
    {
      localBorderLayout = new BorderLayout();
      localBorderLayout.setVgap(0);
      localBorderLayout.setHgap(0);
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
    return localBorderLayout;
  }

  private MenuBar getPFrameMenuBar()
  {
    if (this.ivjPFrameMenuBar == null)
      try
      {
        this.ivjPFrameMenuBar = new MenuBar();
        this.ivjPFrameMenuBar.setHelpMenu(getMenu3());
        this.ivjPFrameMenuBar.add(getMenu2());
        this.ivjPFrameMenuBar.add(getMenu1());
        this.ivjPFrameMenuBar.add(getMenu3());
        MenuBar localMenuBar = this.ivjPFrameMenuBar;
        int i = localMenuBar.getMenuCount();
        for (int j = 0; j < i; j++)
          setResource(localMenuBar.getMenu(j));
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjPFrameMenuBar;
  }

  private Res getResource()
  {
    if (this.res == null)
    {
      Resource.loadResource();
      this.res = Resource.loadResource("Application");
    }
    return this.res;
  }

  private void handleException(Throwable paramThrowable)
  {
  }

  public void init()
  {
    Thread localThread = new Thread(this, "init");
    localThread.setPriority(1);
    localThread.setDaemon(false);
    localThread.start();
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
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  public void iP_MouseClicked()
  {
    PopupMenuPaintChat localPopupMenuPaintChat = new PopupMenuPaintChat(this.debug, this.config, this.res);
    localPopupMenuPaintChat.show(this, this.ivjIp, 0, 0);
  }

  public void itemStateChanged(ItemEvent paramItemEvent)
  {
    if (paramItemEvent.getSource() == getMenuShowConsole())
      connEtoC4(paramItemEvent);
    if (paramItemEvent.getSource() == getMenuShowHelp())
      connEtoC5(paramItemEvent);
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      PFrame localPFrame = new PFrame();
      localPFrame.init();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace(System.out);
      System.exit(0);
    }
  }

  public void menuHelpDocument_ActionPerformed()
  {
    Gui.showDocument("Help.html", this.config, this.res);
  }

  public void menuItem1_ActionPerformed1()
  {
    ThreadPool.poolStartThread(getData(), 'h');
  }

  public void menuItem10_ActionPerformed(ActionEvent paramActionEvent)
  {
    try
    {
      new ConfigDialog("paintchat.config.ConfigServer", "cnf/dialogs.jar", this.config, this.res, "(C)しぃちゃん PaintChatApp v3.66");
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }

  public void menuItem10_ActionPerformed1()
  {
    if ((getData().isRunPaintChatServer()) && (MessageBox.confirm("ConfirmMayServerStopNow", "(C)しぃちゃん PaintChatApp v3.66")))
      getData().startPaintChat(false);
    try
    {
      new ConfigDialog("paintchat.config.ConfigServer", "cnf/dialogs.jar", this.config, this.res, "(C)しぃちゃん PaintChatApp v3.66");
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void menuItem3_ActionPerformed(ActionEvent paramActionEvent)
  {
    try
    {
      new ConfigDialog("paintchat.config.PConfig", "cnf/dialogs.jar", this.config, this.res, "(C)しぃちゃん PaintChatApp v3.66");
    }
    catch (Throwable localThrowable)
    {
      this.debug.log(localThrowable.getMessage());
    }
  }

  public void menuItem4_ActionPerformed()
  {
    try
    {
      new ConfigDialog("paintchat.config.Ao", "cnf/dialogs.jar", this.config, this.res, "(C)しぃちゃん PaintChatApp v3.66");
    }
    catch (Throwable localThrowable)
    {
      this.debug.log(localThrowable.getMessage());
    }
  }

  public void menuItem6_ActionPerformed()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("(C)しぃちゃん PaintChatApp v3.66");
    localStringBuffer.append('\n');
    localStringBuffer.append('\n');
    localStringBuffer.append("JavaVirtualMachine(JVM):");
    localStringBuffer.append(System.getProperty("java.vendor"));
    localStringBuffer.append("\nJVM Version:");
    localStringBuffer.append(System.getProperty("java.version"));
    localStringBuffer.append('\n');
    localStringBuffer.append("\n立ち上げ画面のCGはuzukiさんが作成しました。\nuzukiさんのHP http://www19.freeweb.ne.jp/play/m_uzuki/top.htm\n効果音はあややさんが作成しました。\nロビープログラムは藍珠さんが作成、管理しています。");
    MessageBox.alert(localStringBuffer.toString(), "(C)しぃちゃん PaintChatApp v3.66");
  }

  public void mouseClicked(MouseEvent paramMouseEvent)
  {
    if (paramMouseEvent.getSource() == getIp())
      connEtoC3(paramMouseEvent);
  }

  public void mouseEntered(MouseEvent paramMouseEvent)
  {
    if (paramMouseEvent.getSource() == getIp())
      connEtoM1(paramMouseEvent);
    if (paramMouseEvent.getSource() == getHttp_Port())
      connEtoM2(paramMouseEvent);
    if (paramMouseEvent.getSource() == getPaintchat_Port())
      connEtoM3(paramMouseEvent);
    if (paramMouseEvent.getSource() == getPaintchat_Button())
      connEtoM4(paramMouseEvent);
    if (paramMouseEvent.getSource() == getHttp_Button())
      connEtoM5(paramMouseEvent);
    if (paramMouseEvent.getSource() == getLobby_Button())
      connEtoM6(paramMouseEvent);
  }

  public void mouseExited(MouseEvent paramMouseEvent)
  {
    getHelp().reset();
  }

  public void mousePressed(MouseEvent paramMouseEvent)
  {
  }

  public void mouseReleased(MouseEvent paramMouseEvent)
  {
  }

  public void panel3_MouseReleased(MouseEvent paramMouseEvent)
  {
    MenuBar localMenuBar = getMenuBar();
    if ((this.ivjMenuShowConsole.getState()) || (localMenuBar != null))
      return;
    localMenuBar = getPFrameMenuBar();
    localMenuBar.remove(0);
    localMenuBar.remove(0);
    localMenuBar.remove(0);
    PopupMenu localPopupMenu = new PopupMenu();
    localPopupMenu.addActionListener(this);
    localPopupMenu.add(getMenu1());
    localPopupMenu.add(getMenu2());
    localPopupMenu.add(getMenu3());
    add(localPopupMenu);
    localPopupMenu.show(paramMouseEvent.getComponent(), paramMouseEvent.getX(), paramMouseEvent.getY());
    localPopupMenu.removeAll();
    localMenuBar.add(getMenu1());
    localMenuBar.add(getMenu2());
    localMenuBar.add(getMenu3());
  }

  public void pFrame_WindowClosed(WindowEvent paramWindowEvent)
  {
    System.exit(0);
  }

  public void pFrame_WindowIconified(WindowEvent paramWindowEvent)
  {
    if (getData().getIsNativeWindows())
      setVisible(false);
  }

  private void rDestroy()
  {
    try
    {
      this.config.put("Connection_Port_Http", getHttp_Port().getText().trim());
      this.config.put("Connection_Port_PaintChat", getPaintchat_Port().getText().trim());
      this.config.saveConfig(null, null);
      if (getData().getIsNativeWindows())
        getData().exitWin();
      dispose();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void rInit()
  {
    try
    {
      System.currentTimeMillis();
      getResource();
      setResource(this.res, this);
      Awt.setPFrame(this);
      Awt.cBk = getBackground();
      Awt.cFore = getForeground();
      getConfig();
      getHttp_Port().setText(this.config.getString("Connection_Port_Http", "80"));
      getPaintchat_Port().setText(this.config.getString("Connection_Port_PaintChat", "0"));
      Image localImage = Io.loadImageNow(this, "cnf/template/top.jpg");
      Dimension localDimension = getToolkit().getScreenSize();
      Point localPoint = new Point((localDimension.width - localImage.getWidth(null)) / 2, (localDimension.height - localImage.getHeight(null)) / 2);
      getHelp().setForeground(Color.black);
      HelpWindowContent localHelpWindowContent = new HelpWindowContent(localImage, "(C)しぃちゃん PaintChatApp v3.66", false, localPoint, null);
      localHelpWindowContent.timeStart = 0;
      localHelpWindowContent.timeEnd = 3500;
      localHelpWindowContent.setVisit(true);
      getHelp().startHelp(localHelpWindowContent);
      MessageBox.setResource(this.res);
      this.debug = new Debug(this.res);
      setIconImage(Io.loadImageNow(this, "cnf/icon.gif"));
      setTitle("PaintChat_MainWindow");
      if (this.config.getBool("App_IsConsole"))
      {
        showConsole();
        this.ivjConsole.addText("(C)しぃちゃん PaintChatApp v3.66");
        this.ivjConsole.addText("http://shichan.jp/");
        this.ivjConsole.addText(" ");
        if (this.config.getBool("App_ShowStartHelp", true))
        {
          this.ivjConsole.setRText(this.res.get("StartHelp"));
          this.ivjConsole.addText(" ");
        }
      }
      this.res.remove("StartHelp");
      if (this.config.getBool("App_ShowHelp", true))
        showHelp();
      if (!this.config.getString("App_Version").equals("(C)しぃちゃん PaintChatApp v3.66"))
      {
        new FileManager(this.config).templateToWWW();
        this.config.put("App_Version", "(C)しぃちゃん PaintChatApp v3.66");
        this.debug.log("Client update.");
      }
      getIp().setText(PopupMenuPaintChat.getAddress(this.config, this.debug));
      getData().init(this.config, this.res, this.debug, getPaintchat_Button(), getHttp_Button(), getLobby_Button());
      pack();
      Awt.moveCenter(this);
      setVisible(true);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void run()
  {
    try
    {
      switch (Thread.currentThread().getName().charAt(0))
      {
      case 'd':
        rDestroy();
        break;
      case 'i':
        rInit();
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void setResource(MenuItem paramMenuItem)
  {
    paramMenuItem.setLabel(getResource().get(paramMenuItem.getLabel(), paramMenuItem.getLabel()));
    if ((paramMenuItem instanceof Menu))
    {
      Menu localMenu = (Menu)paramMenuItem;
      int i = localMenu.getItemCount();
      for (int j = 0; j < i; j++)
        setResource(localMenu.getItem(j));
    }
  }

  public void setResource(Res paramRes, Component paramComponent)
  {
    Object localObject1;
    if ((paramComponent instanceof Container))
    {
      localObject1 = (Container)paramComponent;
      int i = ((Container)localObject1).getComponentCount();
      for (int j = 0; j < i; j++)
        setResource(paramRes, ((Container)localObject1).getComponent(j));
    }
    else
    {
      localObject1 = paramRes.get(paramComponent.getName(), paramComponent.getName());
      Object localObject2;
      if ((paramComponent instanceof LTextField))
      {
        localObject2 = (LTextField)paramComponent;
        ((LTextField)localObject2).setTitle((String)localObject1);
        return;
      }
      if ((paramComponent instanceof LButton))
      {
        localObject2 = (LButton)paramComponent;
        ((LButton)localObject2).setText((String)localObject1);
        return;
      }
      if ((paramComponent instanceof Checkbox))
      {
        localObject2 = (Checkbox)paramComponent;
        ((Checkbox)localObject2).setLabel((String)localObject1);
        return;
      }
    }
  }

  public void setupWWWFolder()
  {
    new FileManager(this.config).templateToWWW();
  }

  public void showConsole()
  {
    Console localConsole = getConsole();
    int i = localConsole.getParent() == this ? 1 : 0;
    MenuBar localMenuBar;
    if (i != 0)
    {
      remove(localConsole);
      localConsole.stop();
      localMenuBar = getMenuBar();
      localMenuBar.remove(getMenu1());
      localMenuBar.remove(getMenu3());
    }
    else
    {
      localConsole.start(this.debug);
      add(localConsole, "Center");
      localMenuBar = getPFrameMenuBar();
      localMenuBar.add(getMenu1());
      localMenuBar.add(getMenu3());
    }
    this.config.put("App_IsConsole", String.valueOf(i == 0));
    this.ivjMenuShowConsole.setState(i == 0);
    pack();
  }

  public void showHelp()
  {
    boolean bool = getHelp().getIsShow();
    getHelp().setIsShow(!bool);
    getMenuShowHelp().setState(!bool);
    this.config.put("App_ShowHelp", String.valueOf(!bool));
  }

  public void startClient()
  {
    try
    {
      AppletWatcher localAppletWatcher = new AppletWatcher("paintchat_client.Client", "(C)しぃちゃん PaintChatApp v3.66", this.config, this.res, false);
      localAppletWatcher.setIconImage(getIconImage());
      localAppletWatcher.show();
    }
    catch (Throwable localThrowable)
    {
      this.debug.log(localThrowable.getMessage());
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

  public void windowActivated(WindowEvent paramWindowEvent)
  {
  }

  public void windowClosed(WindowEvent paramWindowEvent)
  {
    if (paramWindowEvent.getSource() == this)
      connEtoC17(paramWindowEvent);
  }

  public void windowClosing(WindowEvent paramWindowEvent)
  {
    if (paramWindowEvent.getSource() == this)
      connEtoC1(paramWindowEvent);
  }

  public void windowDeactivated(WindowEvent paramWindowEvent)
  {
  }

  public void windowDeiconified(WindowEvent paramWindowEvent)
  {
    setVisible(true);
  }

  public void windowIconified(WindowEvent paramWindowEvent)
  {
    if (paramWindowEvent.getSource() == this)
      connEtoC8(paramWindowEvent);
  }

  public void windowOpened(WindowEvent paramWindowEvent)
  {
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_frame.PFrame
 * JD-Core Version:    0.6.0
 */