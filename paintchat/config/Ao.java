package paintchat.config;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Random;
import paintchat.Config;
import paintchat.Resource;
import syi.applet.ServerStub;
import syi.awt.Awt;
import syi.awt.Gui;
import syi.awt.HelpWindow;
import syi.awt.LButton;
import syi.util.PProperties;

public class Ao extends ConfigApplet
  implements ActionListener
{
  private String CF_AO_CHATINDEX = "chatIndex";
  private String CF_AO_SHOW_HTML = "ao_show_html";
  private Checkbox ivjao_show_html = null;
  private Button ivjbu_ok = null;
  private Button ivjButton2 = null;
  private GridLayout ivjAo2GridLayout = null;
  private Panel ivjPanel3 = null;
  private LButton ivjCancel = null;
  private LButton ivjOk = null;
  private Panel ivjPanelConfig = null;
  private Panel ivjPanelContent = null;
  private TextField ivjadministratorName = null;
  private TextField ivjchatName = null;
  private TextField ivjchatUrl = null;
  private TextField ivjcommentString = null;
  private TextField ivjhomepageName = null;
  private TextField ivjhpUrl = null;
  private TextField ivjinformtionServerAddress = null;
  private Label ivjLabel1 = null;
  private Label ivjLabel2 = null;
  private Label ivjLabel3 = null;
  private Label ivjLabel4 = null;
  private Label ivjLabel5 = null;
  private Label ivjLabel6 = null;
  private Label ivjLabel7 = null;
  private Panel ivjleftPanel = null;
  private GridLayout ivjleftPanelGridLayout = null;
  private Panel ivjpanelRight = null;
  private GridLayout ivjpanelRightGridLayout = null;
  private Checkbox ivjApp_Auto_Lobby = null;
  private Panel ivjpanelBottom = null;
  private Label ivjlobby_setup = null;
  private FlowLayout ivjpanelBottomFlowLayout = null;

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    if (paramActionEvent.getSource() == getButton2())
      connEtoC1(paramActionEvent);
    if (paramActionEvent.getSource() == getbu_ok())
      connEtoC2(paramActionEvent);
    if (paramActionEvent.getSource() == getOk())
    {
      save();
      m_destroy();
    }
    if (paramActionEvent.getSource() == getCancel())
      m_destroy();
  }

  public void ao2_Init()
  {
  }

  public void ao2_Start()
  {
    Awt.setPFrame(Awt.getPFrame());
    getCancel().addActionListener(this);
    getOk().addActionListener(this);
  }

  private void connEtoC1(ActionEvent paramActionEvent)
  {
    try
    {
      m_destroy();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC2(ActionEvent paramActionEvent)
  {
    try
    {
      save();
      connEtoC3();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC3()
  {
    try
    {
      m_destroy();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void connEtoC4()
  {
    try
    {
      ao2_Start();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private TextField getadministratorName()
  {
    if (this.ivjadministratorName == null)
      try
      {
        this.ivjadministratorName = new TextField();
        this.ivjadministratorName.setName("administratorName");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjadministratorName;
  }

  private Checkbox getao_show_html()
  {
    if (this.ivjao_show_html == null)
      try
      {
        this.ivjao_show_html = new Checkbox();
        this.ivjao_show_html.setName("ao_show_html");
        this.ivjao_show_html.setLabel("ao_show_html");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjao_show_html;
  }

  private GridLayout getAo2GridLayout()
  {
    GridLayout localGridLayout = null;
    try
    {
      localGridLayout = new GridLayout(0, 1);
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
    return localGridLayout;
  }

  private Checkbox getApp_Auto_Lobby()
  {
    if (this.ivjApp_Auto_Lobby == null)
      try
      {
        this.ivjApp_Auto_Lobby = new Checkbox();
        this.ivjApp_Auto_Lobby.setName("App_Auto_Lobby");
        this.ivjApp_Auto_Lobby.setLabel("App_Auto_Lobby");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjApp_Auto_Lobby;
  }

  public String getAppletInfo()
  {
    return "paintchat.config.Ao2 は VisualAge for Java を使用して作成されました。";
  }

  private Button getbu_ok()
  {
    if (this.ivjbu_ok == null)
      try
      {
        this.ivjbu_ok = new Button();
        this.ivjbu_ok.setName("bu_ok");
        this.ivjbu_ok.setBounds(190, 333, 38, 20);
        this.ivjbu_ok.setLabel(" OK ");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjbu_ok;
  }

  private Button getButton2()
  {
    if (this.ivjButton2 == null)
      try
      {
        this.ivjButton2 = new Button();
        this.ivjButton2.setName("Button2");
        this.ivjButton2.setBounds(235, 359, 50, 20);
        this.ivjButton2.setLabel("CANCEL");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjButton2;
  }

  private LButton getCancel()
  {
    if (this.ivjCancel == null)
      try
      {
        this.ivjCancel = new LButton();
        this.ivjCancel.setName("Cancel");
        this.ivjCancel.setText("Cancel");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjCancel;
  }

  private TextField getchatName()
  {
    if (this.ivjchatName == null)
      try
      {
        this.ivjchatName = new TextField();
        this.ivjchatName.setName("chatName");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjchatName;
  }

  private TextField getchatUrl()
  {
    if (this.ivjchatUrl == null)
      try
      {
        this.ivjchatUrl = new TextField();
        this.ivjchatUrl.setName("chatUrl");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjchatUrl;
  }

  private TextField getcommentString()
  {
    if (this.ivjcommentString == null)
      try
      {
        this.ivjcommentString = new TextField();
        this.ivjcommentString.setName("commentString");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjcommentString;
  }

  private TextField gethomepageName()
  {
    if (this.ivjhomepageName == null)
      try
      {
        this.ivjhomepageName = new TextField();
        this.ivjhomepageName.setName("homepageName");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjhomepageName;
  }

  private TextField gethpUrl()
  {
    if (this.ivjhpUrl == null)
      try
      {
        this.ivjhpUrl = new TextField();
        this.ivjhpUrl.setName("hpUrl");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjhpUrl;
  }

  private TextField getinformtionServerAddress()
  {
    if (this.ivjinformtionServerAddress == null)
      try
      {
        this.ivjinformtionServerAddress = new TextField();
        this.ivjinformtionServerAddress.setName("informtionServerAddress");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjinformtionServerAddress;
  }

  private Label getLabel1()
  {
    if (this.ivjLabel1 == null)
      try
      {
        this.ivjLabel1 = new Label();
        this.ivjLabel1.setName("Label1");
        this.ivjLabel1.setText("chatUrl");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjLabel1;
  }

  private Label getLabel2()
  {
    if (this.ivjLabel2 == null)
      try
      {
        this.ivjLabel2 = new Label();
        this.ivjLabel2.setName("Label2");
        this.ivjLabel2.setText("administratorName");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjLabel2;
  }

  private Label getLabel3()
  {
    if (this.ivjLabel3 == null)
      try
      {
        this.ivjLabel3 = new Label();
        this.ivjLabel3.setName("Label3");
        this.ivjLabel3.setText("chatName");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjLabel3;
  }

  private Label getLabel4()
  {
    if (this.ivjLabel4 == null)
      try
      {
        this.ivjLabel4 = new Label();
        this.ivjLabel4.setName("Label4");
        this.ivjLabel4.setText("commentString");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjLabel4;
  }

  private Label getLabel5()
  {
    if (this.ivjLabel5 == null)
      try
      {
        this.ivjLabel5 = new Label();
        this.ivjLabel5.setName("Label5");
        this.ivjLabel5.setText("hpUrl");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjLabel5;
  }

  private Label getLabel6()
  {
    if (this.ivjLabel6 == null)
      try
      {
        this.ivjLabel6 = new Label();
        this.ivjLabel6.setName("Label6");
        this.ivjLabel6.setText("homepageName");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjLabel6;
  }

  private Label getLabel7()
  {
    if (this.ivjLabel7 == null)
      try
      {
        this.ivjLabel7 = new Label();
        this.ivjLabel7.setName("Label7");
        this.ivjLabel7.setText("informtionServerAddress");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjLabel7;
  }

  private Panel getleftPanel()
  {
    if (this.ivjleftPanel == null)
      try
      {
        this.ivjleftPanel = new Panel();
        this.ivjleftPanel.setName("leftPanel");
        this.ivjleftPanel.setLayout(getleftPanelGridLayout());
        getleftPanel().add(getLabel2(), getLabel2().getName());
        this.ivjleftPanel.add(getLabel1());
        this.ivjleftPanel.add(getLabel3());
        this.ivjleftPanel.add(getLabel4());
        this.ivjleftPanel.add(getLabel5());
        this.ivjleftPanel.add(getLabel6());
        this.ivjleftPanel.add(getLabel7());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjleftPanel;
  }

  private GridLayout getleftPanelGridLayout()
  {
    GridLayout localGridLayout = null;
    try
    {
      localGridLayout = new GridLayout(0, 1);
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
    return localGridLayout;
  }

  private Label getlobby_setup()
  {
    if (this.ivjlobby_setup == null)
      try
      {
        this.ivjlobby_setup = new Label();
        this.ivjlobby_setup.setName("lobby_setup");
        this.ivjlobby_setup.setAlignment(1);
        this.ivjlobby_setup.setText("lobby_setup");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjlobby_setup;
  }

  private LButton getOk()
  {
    if (this.ivjOk == null)
      try
      {
        this.ivjOk = new LButton();
        this.ivjOk.setName("Ok");
        this.ivjOk.setText("   OK   ");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjOk;
  }

  private Panel getPanel3()
  {
    if (this.ivjPanel3 == null)
      try
      {
        this.ivjPanel3 = new Panel();
        this.ivjPanel3.setName("Panel3");
        this.ivjPanel3.setLayout(new FlowLayout());
        getPanel3().add(getOk(), getOk().getName());
        this.ivjPanel3.add(getCancel());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjPanel3;
  }

  private Panel getpanelBottom()
  {
    if (this.ivjpanelBottom == null)
      try
      {
        this.ivjpanelBottom = new Panel();
        this.ivjpanelBottom.setName("panelBottom");
        this.ivjpanelBottom.setLayout(getpanelBottomFlowLayout());
        getpanelBottom().add(getao_show_html(), getao_show_html().getName());
        getpanelBottom().add(getApp_Auto_Lobby(), getApp_Auto_Lobby().getName());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjpanelBottom;
  }

  private FlowLayout getpanelBottomFlowLayout()
  {
    FlowLayout localFlowLayout = null;
    try
    {
      localFlowLayout = new FlowLayout();
      localFlowLayout.setAlignment(0);
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
    return localFlowLayout;
  }

  private Panel getPanelConfig()
  {
    if (this.ivjPanelConfig == null)
      try
      {
        this.ivjPanelConfig = new Panel();
        this.ivjPanelConfig.setName("PanelConfig");
        this.ivjPanelConfig.setLayout(new BorderLayout());
        getPanelConfig().add(getPanelContent(), "Center");
        getPanelConfig().add(getPanel3(), "South");
        getPanelConfig().add(getlobby_setup(), "North");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjPanelConfig;
  }

  private Panel getPanelContent()
  {
    if (this.ivjPanelContent == null)
      try
      {
        this.ivjPanelContent = new Panel();
        this.ivjPanelContent.setName("PanelContent");
        this.ivjPanelContent.setLayout(new BorderLayout());
        getPanelContent().add(getleftPanel(), "West");
        getPanelContent().add(getpanelRight(), "Center");
        getPanelContent().add(getpanelBottom(), "South");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjPanelContent;
  }

  private Panel getpanelRight()
  {
    if (this.ivjpanelRight == null)
      try
      {
        this.ivjpanelRight = new Panel();
        this.ivjpanelRight.setName("panelRight");
        this.ivjpanelRight.setLayout(getpanelRightGridLayout());
        getpanelRight().add(getadministratorName(), getadministratorName().getName());
        getpanelRight().add(getchatUrl(), getchatUrl().getName());
        getpanelRight().add(getchatName(), getchatName().getName());
        getpanelRight().add(getcommentString(), getcommentString().getName());
        getpanelRight().add(gethpUrl(), gethpUrl().getName());
        getpanelRight().add(gethomepageName(), gethomepageName().getName());
        getpanelRight().add(getinformtionServerAddress(), getinformtionServerAddress().getName());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjpanelRight;
  }

  private GridLayout getpanelRightGridLayout()
  {
    GridLayout localGridLayout = null;
    try
    {
      localGridLayout = new GridLayout(0, 1);
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
    return localGridLayout;
  }

  private void handleException(Throwable paramThrowable)
  {
  }

  public void init()
  {
    try
    {
      setName("Ao2");
      setLayout(getAo2GridLayout());
      setBackground(new Color(204, 204, 204));
      setSize(426, 240);
      setForeground(Color.black);
      add(getPanelConfig());
      initConnections();
      Gui.giveDef(this);
      load();
      initValue();
      ((Dialog)getParent()).pack();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private void initConnections()
    throws Exception
  {
    getOk().addActionListener(this);
    getCancel().addActionListener(this);
    setMouseListener(this, this);
    getButton2().addActionListener(this);
    getbu_ok().addActionListener(this);
  }

  private void initValue()
  {
    try
    {
      this.res = Resource.loadResource("Config");
      getResource(this.res, this);
      String str = getParameter("App_ShowHelp");
      boolean bool = true;
      if ((str != null) || (str.length() > 0))
        switch (Character.toLowerCase(str.charAt(0)))
        {
        case '0':
        case 'f':
        case 'n':
          bool = false;
          break;
        default:
          bool = true;
        }
      getHelp().setIsShow(bool);
    }
    catch (Throwable localThrowable)
    {
    }
  }

  private void load()
  {
    try
    {
      PProperties localPProperties = ((ServerStub)getAppletContext()).getHashTable();
      this.ivjao_show_html.setState(localPProperties.getBool(this.CF_AO_SHOW_HTML));
      String str = System.getProperty("user.name", "");
      getadministratorName().setText(localPProperties.getString(getadministratorName().getName(), str));
      getchatName().setText(localPProperties.getString(getchatName().getName(), str + "'s chat room"));
      getchatUrl().setText(localPProperties.getString(getchatUrl().getName()));
      getcommentString().setText(localPProperties.getString(getcommentString().getName(), "test room"));
      gethpUrl().setText(localPProperties.getString(gethpUrl().getName()));
      gethomepageName().setText(localPProperties.getString(gethomepageName().getName()));
      getinformtionServerAddress().setText(localPProperties.getString(getinformtionServerAddress().getName(), "http://www.ax.sakura.ne.jp/~aotama/paintchat/paintchatexcheange.conf"));
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void m_destroy()
  {
    try
    {
      getHelp().reset();
      ((Window)Awt.getParent(this)).dispose();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      Frame localFrame = new Frame();
      Class localClass = Class.forName("paintchat.config.Ao");
      ClassLoader localClassLoader = localClass.getClassLoader();
      Ao localAo = (Ao)Beans.instantiate(localClassLoader, "paintchat.config.Ao");
      localFrame.add("Center", localAo);
      localFrame.setSize(localAo.getSize());
      localFrame.addWindowListener(new WindowAdapter()
      {
        public void windowClosing(WindowEvent paramWindowEvent)
        {
          System.exit(0);
        }
      });
      localFrame.setVisible(true);
    }
    catch (Throwable localThrowable)
    {
      System.err.println("java.applet.Applet の main() で例外が発生しました");
      localThrowable.printStackTrace(System.out);
    }
  }

  private void save()
  {
    try
    {
      Config localConfig = (Config)((ServerStub)getAppletContext()).getHashTable();
      if (localConfig.getInt(this.CF_AO_CHATINDEX) == 0)
      {
        Random localRandom = new Random();
        long l = 0L;
        while (l == 0L)
        {
          l = localRandom.nextInt();
          for (int i = 0; i < 100; i++)
            l += localRandom.nextInt() % 6;
        }
        localConfig.put(this.CF_AO_CHATINDEX, String.valueOf((int)l));
      }
      setParameter(this);
      localConfig.save(new FileOutputStream(localConfig.getString("File_Config", "cnf/paintchat.cf")), Resource.loadResource("Config"));
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void start()
  {
    connEtoC4();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.config.Ao
 * JD-Core Version:    0.6.0
 */