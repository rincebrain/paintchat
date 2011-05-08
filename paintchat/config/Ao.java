package paintchat.config;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.beans.Beans;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import paintchat.Config;
import paintchat.Resource;
import syi.applet.ServerStub;
import syi.awt.*;
import syi.util.PProperties;

// Referenced classes of package paintchat.config:
//            ConfigApplet

public class Ao extends ConfigApplet
    implements ActionListener
{

    private String CF_AO_CHATINDEX;
    private String CF_AO_SHOW_HTML;
    private Checkbox ivjao_show_html;
    private Button ivjbu_ok;
    private Button ivjButton2;
    private GridLayout ivjAo2GridLayout;
    private Panel ivjPanel3;
    private LButton ivjCancel;
    private LButton ivjOk;
    private Panel ivjPanelConfig;
    private Panel ivjPanelContent;
    private TextField ivjadministratorName;
    private TextField ivjchatName;
    private TextField ivjchatUrl;
    private TextField ivjcommentString;
    private TextField ivjhomepageName;
    private TextField ivjhpUrl;
    private TextField ivjinformtionServerAddress;
    private Label ivjLabel1;
    private Label ivjLabel2;
    private Label ivjLabel3;
    private Label ivjLabel4;
    private Label ivjLabel5;
    private Label ivjLabel6;
    private Label ivjLabel7;
    private Panel ivjleftPanel;
    private GridLayout ivjleftPanelGridLayout;
    private Panel ivjpanelRight;
    private GridLayout ivjpanelRightGridLayout;
    private Checkbox ivjApp_Auto_Lobby;
    private Panel ivjpanelBottom;
    private Label ivjlobby_setup;
    private FlowLayout ivjpanelBottomFlowLayout;

    public Ao()
    {
        CF_AO_CHATINDEX = "chatIndex";
        CF_AO_SHOW_HTML = "ao_show_html";
        ivjao_show_html = null;
        ivjbu_ok = null;
        ivjButton2 = null;
        ivjAo2GridLayout = null;
        ivjPanel3 = null;
        ivjCancel = null;
        ivjOk = null;
        ivjPanelConfig = null;
        ivjPanelContent = null;
        ivjadministratorName = null;
        ivjchatName = null;
        ivjchatUrl = null;
        ivjcommentString = null;
        ivjhomepageName = null;
        ivjhpUrl = null;
        ivjinformtionServerAddress = null;
        ivjLabel1 = null;
        ivjLabel2 = null;
        ivjLabel3 = null;
        ivjLabel4 = null;
        ivjLabel5 = null;
        ivjLabel6 = null;
        ivjLabel7 = null;
        ivjleftPanel = null;
        ivjleftPanelGridLayout = null;
        ivjpanelRight = null;
        ivjpanelRightGridLayout = null;
        ivjApp_Auto_Lobby = null;
        ivjpanelBottom = null;
        ivjlobby_setup = null;
        ivjpanelBottomFlowLayout = null;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getSource() == getButton2())
        {
            connEtoC1(actionevent);
        }
        if(actionevent.getSource() == getbu_ok())
        {
            connEtoC2(actionevent);
        }
        if(actionevent.getSource() == getOk())
        {
            save();
            m_destroy();
        }
        if(actionevent.getSource() == getCancel())
        {
            m_destroy();
        }
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

    private void connEtoC1(ActionEvent actionevent)
    {
        try
        {
            m_destroy();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC2(ActionEvent actionevent)
    {
        try
        {
            save();
            connEtoC3();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC3()
    {
        try
        {
            m_destroy();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private void connEtoC4()
    {
        try
        {
            ao2_Start();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private TextField getadministratorName()
    {
        if(ivjadministratorName == null)
        {
            try
            {
                ivjadministratorName = new TextField();
                ivjadministratorName.setName("administratorName");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjadministratorName;
    }

    private Checkbox getao_show_html()
    {
        if(ivjao_show_html == null)
        {
            try
            {
                ivjao_show_html = new Checkbox();
                ivjao_show_html.setName("ao_show_html");
                ivjao_show_html.setLabel("ao_show_html");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjao_show_html;
    }

    private GridLayout getAo2GridLayout()
    {
        GridLayout gridlayout = null;
        try
        {
            gridlayout = new GridLayout(0, 1);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
        return gridlayout;
    }

    private Checkbox getApp_Auto_Lobby()
    {
        if(ivjApp_Auto_Lobby == null)
        {
            try
            {
                ivjApp_Auto_Lobby = new Checkbox();
                ivjApp_Auto_Lobby.setName("App_Auto_Lobby");
                ivjApp_Auto_Lobby.setLabel("App_Auto_Lobby");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjApp_Auto_Lobby;
    }

    public String getAppletInfo()
    {
        return "paintchat.config.Ao2 \u306F VisualAge for Java \u3092\u4F7F\u7528\u3057\u3066\u4F5C" +
"\u6210\u3055\u308C\u307E\u3057\u305F\u3002"
;
    }

    private Button getbu_ok()
    {
        if(ivjbu_ok == null)
        {
            try
            {
                ivjbu_ok = new Button();
                ivjbu_ok.setName("bu_ok");
                ivjbu_ok.setBounds(190, 333, 38, 20);
                ivjbu_ok.setLabel(" OK ");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjbu_ok;
    }

    private Button getButton2()
    {
        if(ivjButton2 == null)
        {
            try
            {
                ivjButton2 = new Button();
                ivjButton2.setName("Button2");
                ivjButton2.setBounds(235, 359, 50, 20);
                ivjButton2.setLabel("CANCEL");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjButton2;
    }

    private LButton getCancel()
    {
        if(ivjCancel == null)
        {
            try
            {
                ivjCancel = new LButton();
                ivjCancel.setName("Cancel");
                ivjCancel.setText("Cancel");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjCancel;
    }

    private TextField getchatName()
    {
        if(ivjchatName == null)
        {
            try
            {
                ivjchatName = new TextField();
                ivjchatName.setName("chatName");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjchatName;
    }

    private TextField getchatUrl()
    {
        if(ivjchatUrl == null)
        {
            try
            {
                ivjchatUrl = new TextField();
                ivjchatUrl.setName("chatUrl");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjchatUrl;
    }

    private TextField getcommentString()
    {
        if(ivjcommentString == null)
        {
            try
            {
                ivjcommentString = new TextField();
                ivjcommentString.setName("commentString");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjcommentString;
    }

    private TextField gethomepageName()
    {
        if(ivjhomepageName == null)
        {
            try
            {
                ivjhomepageName = new TextField();
                ivjhomepageName.setName("homepageName");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjhomepageName;
    }

    private TextField gethpUrl()
    {
        if(ivjhpUrl == null)
        {
            try
            {
                ivjhpUrl = new TextField();
                ivjhpUrl.setName("hpUrl");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjhpUrl;
    }

    private TextField getinformtionServerAddress()
    {
        if(ivjinformtionServerAddress == null)
        {
            try
            {
                ivjinformtionServerAddress = new TextField();
                ivjinformtionServerAddress.setName("informtionServerAddress");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjinformtionServerAddress;
    }

    private Label getLabel1()
    {
        if(ivjLabel1 == null)
        {
            try
            {
                ivjLabel1 = new Label();
                ivjLabel1.setName("Label1");
                ivjLabel1.setText("chatUrl");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjLabel1;
    }

    private Label getLabel2()
    {
        if(ivjLabel2 == null)
        {
            try
            {
                ivjLabel2 = new Label();
                ivjLabel2.setName("Label2");
                ivjLabel2.setText("administratorName");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjLabel2;
    }

    private Label getLabel3()
    {
        if(ivjLabel3 == null)
        {
            try
            {
                ivjLabel3 = new Label();
                ivjLabel3.setName("Label3");
                ivjLabel3.setText("chatName");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjLabel3;
    }

    private Label getLabel4()
    {
        if(ivjLabel4 == null)
        {
            try
            {
                ivjLabel4 = new Label();
                ivjLabel4.setName("Label4");
                ivjLabel4.setText("commentString");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjLabel4;
    }

    private Label getLabel5()
    {
        if(ivjLabel5 == null)
        {
            try
            {
                ivjLabel5 = new Label();
                ivjLabel5.setName("Label5");
                ivjLabel5.setText("hpUrl");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjLabel5;
    }

    private Label getLabel6()
    {
        if(ivjLabel6 == null)
        {
            try
            {
                ivjLabel6 = new Label();
                ivjLabel6.setName("Label6");
                ivjLabel6.setText("homepageName");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjLabel6;
    }

    private Label getLabel7()
    {
        if(ivjLabel7 == null)
        {
            try
            {
                ivjLabel7 = new Label();
                ivjLabel7.setName("Label7");
                ivjLabel7.setText("informtionServerAddress");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjLabel7;
    }

    private Panel getleftPanel()
    {
        if(ivjleftPanel == null)
        {
            try
            {
                ivjleftPanel = new Panel();
                ivjleftPanel.setName("leftPanel");
                ivjleftPanel.setLayout(getleftPanelGridLayout());
                getleftPanel().add(getLabel2(), getLabel2().getName());
                ivjleftPanel.add(getLabel1());
                ivjleftPanel.add(getLabel3());
                ivjleftPanel.add(getLabel4());
                ivjleftPanel.add(getLabel5());
                ivjleftPanel.add(getLabel6());
                ivjleftPanel.add(getLabel7());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjleftPanel;
    }

    private GridLayout getleftPanelGridLayout()
    {
        GridLayout gridlayout = null;
        try
        {
            gridlayout = new GridLayout(0, 1);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
        return gridlayout;
    }

    private Label getlobby_setup()
    {
        if(ivjlobby_setup == null)
        {
            try
            {
                ivjlobby_setup = new Label();
                ivjlobby_setup.setName("lobby_setup");
                ivjlobby_setup.setAlignment(1);
                ivjlobby_setup.setText("lobby_setup");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjlobby_setup;
    }

    private LButton getOk()
    {
        if(ivjOk == null)
        {
            try
            {
                ivjOk = new LButton();
                ivjOk.setName("Ok");
                ivjOk.setText("   OK   ");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjOk;
    }

    private Panel getPanel3()
    {
        if(ivjPanel3 == null)
        {
            try
            {
                ivjPanel3 = new Panel();
                ivjPanel3.setName("Panel3");
                ivjPanel3.setLayout(new FlowLayout());
                getPanel3().add(getOk(), getOk().getName());
                ivjPanel3.add(getCancel());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjPanel3;
    }

    private Panel getpanelBottom()
    {
        if(ivjpanelBottom == null)
        {
            try
            {
                ivjpanelBottom = new Panel();
                ivjpanelBottom.setName("panelBottom");
                ivjpanelBottom.setLayout(getpanelBottomFlowLayout());
                getpanelBottom().add(getao_show_html(), getao_show_html().getName());
                getpanelBottom().add(getApp_Auto_Lobby(), getApp_Auto_Lobby().getName());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjpanelBottom;
    }

    private FlowLayout getpanelBottomFlowLayout()
    {
        FlowLayout flowlayout = null;
        try
        {
            flowlayout = new FlowLayout();
            flowlayout.setAlignment(0);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
        return flowlayout;
    }

    private Panel getPanelConfig()
    {
        if(ivjPanelConfig == null)
        {
            try
            {
                ivjPanelConfig = new Panel();
                ivjPanelConfig.setName("PanelConfig");
                ivjPanelConfig.setLayout(new BorderLayout());
                getPanelConfig().add(getPanelContent(), "Center");
                getPanelConfig().add(getPanel3(), "South");
                getPanelConfig().add(getlobby_setup(), "North");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjPanelConfig;
    }

    private Panel getPanelContent()
    {
        if(ivjPanelContent == null)
        {
            try
            {
                ivjPanelContent = new Panel();
                ivjPanelContent.setName("PanelContent");
                ivjPanelContent.setLayout(new BorderLayout());
                getPanelContent().add(getleftPanel(), "West");
                getPanelContent().add(getpanelRight(), "Center");
                getPanelContent().add(getpanelBottom(), "South");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjPanelContent;
    }

    private Panel getpanelRight()
    {
        if(ivjpanelRight == null)
        {
            try
            {
                ivjpanelRight = new Panel();
                ivjpanelRight.setName("panelRight");
                ivjpanelRight.setLayout(getpanelRightGridLayout());
                getpanelRight().add(getadministratorName(), getadministratorName().getName());
                getpanelRight().add(getchatUrl(), getchatUrl().getName());
                getpanelRight().add(getchatName(), getchatName().getName());
                getpanelRight().add(getcommentString(), getcommentString().getName());
                getpanelRight().add(gethpUrl(), gethpUrl().getName());
                getpanelRight().add(gethomepageName(), gethomepageName().getName());
                getpanelRight().add(getinformtionServerAddress(), getinformtionServerAddress().getName());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjpanelRight;
    }

    private GridLayout getpanelRightGridLayout()
    {
        GridLayout gridlayout = null;
        try
        {
            gridlayout = new GridLayout(0, 1);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
        return gridlayout;
    }

    private void handleException(Throwable throwable)
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
        catch(Throwable throwable)
        {
            handleException(throwable);
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
            super.res = Resource.loadResource("Config");
            getResource(super.res, this);
            String s = getParameter("App_ShowHelp");
            boolean flag = true;
            if(s != null || s.length() > 0)
            {
                switch(Character.toLowerCase(s.charAt(0)))
                {
                case 48: // '0'
                case 102: // 'f'
                case 110: // 'n'
                    flag = false;
                    break;

                default:
                    flag = true;
                    break;
                }
            }
            getHelp().setIsShow(flag);
        }
        catch(Throwable _ex) { }
    }

    private void load()
    {
        try
        {
            PProperties pproperties = ((ServerStub)getAppletContext()).getHashTable();
            ivjao_show_html.setState(pproperties.getBool(CF_AO_SHOW_HTML));
            String s = System.getProperty("user.name", "");
            getadministratorName().setText(pproperties.getString(getadministratorName().getName(), s));
            getchatName().setText(pproperties.getString(getchatName().getName(), s + "'s chat room"));
            getchatUrl().setText(pproperties.getString(getchatUrl().getName()));
            getcommentString().setText(pproperties.getString(getcommentString().getName(), "test room"));
            gethpUrl().setText(pproperties.getString(gethpUrl().getName()));
            gethomepageName().setText(pproperties.getString(gethomepageName().getName()));
            getinformtionServerAddress().setText(pproperties.getString(getinformtionServerAddress().getName(), "http://www.ax.sakura.ne.jp/~aotama/paintchat/paintchatexcheange.conf"));
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void m_destroy()
    {
        try
        {
            getHelp().reset();
            ((Window)Awt.getParent(this)).dispose();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        try
        {
            Frame frame = new Frame();
            Class class1 = Class.forName("paintchat.config.Ao");
            ClassLoader classloader = class1.getClassLoader();
            Ao ao = (Ao)Beans.instantiate(classloader, "paintchat.config.Ao");
            frame.add("Center", ao);
            frame.setSize(ao.getSize());
            frame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent windowevent)
                {
                    System.exit(0);
                }

            });
            frame.setVisible(true);
        }
        catch(Throwable throwable)
        {
            System.err.println("java.applet.Applet \u306E main() \u3067\u4F8B\u5916\u304C\u767A\u751F\u3057\u307E" +
"\u3057\u305F"
);
            throwable.printStackTrace(System.out);
        }
    }

    private void save()
    {
        try
        {
            Config config = (Config)((ServerStub)getAppletContext()).getHashTable();
            if(config.getInt(CF_AO_CHATINDEX) == 0)
            {
                Random random = new Random();
                long l;
                for(l = 0L; l == 0L;)
                {
                    l = random.nextInt();
                    for(int i = 0; i < 100; i++)
                    {
                        l += random.nextInt() % 6;
                    }

                }

                config.put(CF_AO_CHATINDEX, String.valueOf((int)l));
            }
            setParameter(this);
            config.save(new FileOutputStream(config.getString("File_Config", "cnf/paintchat.cf")), Resource.loadResource("Config"));
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void start()
    {
        connEtoC4();
    }
}
