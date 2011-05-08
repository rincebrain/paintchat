package paintchat.config;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.beans.Beans;
import java.io.File;
import java.io.PrintStream;
import java.util.EventObject;
import paintchat.Config;
import syi.applet.ServerStub;
import syi.awt.*;

// Referenced classes of package paintchat.config:
//            ConfigApplet

public class PConfig extends ConfigApplet
    implements ActionListener
{

    private Panel ivjPanel1;
    private GridLayout ivjPanel1GridLayout;
    private LTextField ivjAdmin_Password;
    private Checkbox ivjApp_Auto_Http;
    private Checkbox ivjApp_Auto_Lobby;
    private Checkbox ivjApp_Auto_Paintchat;
    private LTextField ivjApp_BrowserPath;
    private LTextField ivjApp_Cgi;
    private LButton ivjCancel;
    private Checkbox ivjConnection_GrobalAddress;
    private LButton ivjOk;
    private Panel ivjPanel2;
    private Panel ivjPanel3;
    private GridLayout ivjPanel2GridLayout;
    private Checkbox ivjApp_ShowStartHelp;
    private Button ivjButton1;
    private Checkbox ivjAdmin_ChatMaster;
    private Checkbox ivjApp_Get_Index;

    public PConfig()
    {
        ivjPanel1 = null;
        ivjPanel1GridLayout = null;
        ivjAdmin_Password = null;
        ivjApp_Auto_Http = null;
        ivjApp_Auto_Lobby = null;
        ivjApp_Auto_Paintchat = null;
        ivjApp_BrowserPath = null;
        ivjApp_Cgi = null;
        ivjCancel = null;
        ivjConnection_GrobalAddress = null;
        ivjOk = null;
        ivjPanel2 = null;
        ivjPanel3 = null;
        ivjPanel2GridLayout = null;
        ivjApp_ShowStartHelp = null;
        ivjButton1 = null;
        ivjAdmin_ChatMaster = null;
        ivjApp_Get_Index = null;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getSource() == getButton1())
        {
            connEtoM1(actionevent);
        }
        if(actionevent.getSource() == getOk())
        {
            m_save();
            m_dispose();
        }
        if(actionevent.getSource() == getCancel())
        {
            m_dispose();
        }
    }

    public void addsMouseListener(Container container)
    {
        Component acomponent[] = container.getComponents();
        if(acomponent != null)
        {
            for(int i = 0; i < acomponent.length; i++)
            {
                if(acomponent[i] instanceof Container)
                {
                    addsMouseListener((Container)acomponent[i]);
                } else
                {
                    acomponent[i].addMouseListener(this);
                }
            }

        }
        container.addMouseListener(this);
    }

    public void browse(TextField textfield, boolean flag)
    {
        String s = textfield.getText();
        File file = Gui.fileDialog(Awt.getPFrame(), s, flag);
        textfield.setText(file.getAbsolutePath());
    }

    private void connEtoM1(ActionEvent actionevent)
    {
        try
        {
            stop();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
    }

    private Checkbox getAdmin_ChatMaster()
    {
        if(ivjAdmin_ChatMaster == null)
        {
            try
            {
                ivjAdmin_ChatMaster = new Checkbox();
                ivjAdmin_ChatMaster.setName("Admin_ChatMaster");
                ivjAdmin_ChatMaster.setLabel("Admin_ChatMaster");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjAdmin_ChatMaster;
    }

    private LTextField getAdmin_Password()
    {
        if(ivjAdmin_Password == null)
        {
            try
            {
                ivjAdmin_Password = new LTextField();
                ivjAdmin_Password.setName("Admin_Password");
                ivjAdmin_Password.setTitle("Admin_Password");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjAdmin_Password;
    }

    private Checkbox getApp_Auto_Http()
    {
        if(ivjApp_Auto_Http == null)
        {
            try
            {
                ivjApp_Auto_Http = new Checkbox();
                ivjApp_Auto_Http.setName("App_Auto_Http");
                ivjApp_Auto_Http.setLabel("App_Auto_Http");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjApp_Auto_Http;
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

    private Checkbox getApp_Auto_Paintchat()
    {
        if(ivjApp_Auto_Paintchat == null)
        {
            try
            {
                ivjApp_Auto_Paintchat = new Checkbox();
                ivjApp_Auto_Paintchat.setName("App_Auto_Paintchat");
                ivjApp_Auto_Paintchat.setLabel("App_Auto_Paintchat");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjApp_Auto_Paintchat;
    }

    private LTextField getApp_BrowserPath()
    {
        if(ivjApp_BrowserPath == null)
        {
            try
            {
                ivjApp_BrowserPath = new LTextField();
                ivjApp_BrowserPath.setName("App_BrowserPath");
                ivjApp_BrowserPath.setTitle("App_BrowserPath");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjApp_BrowserPath;
    }

    private LTextField getApp_Cgi()
    {
        if(ivjApp_Cgi == null)
        {
            try
            {
                ivjApp_Cgi = new LTextField();
                ivjApp_Cgi.setName("App_Cgi");
                ivjApp_Cgi.setTitle("App_Cgi");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjApp_Cgi;
    }

    private Checkbox getApp_Get_Index()
    {
        if(ivjApp_Get_Index == null)
        {
            try
            {
                ivjApp_Get_Index = new Checkbox();
                ivjApp_Get_Index.setName("App_Get_Index");
                ivjApp_Get_Index.setLabel("App_Get_Index");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjApp_Get_Index;
    }

    private Checkbox getApp_ShowStartHelp()
    {
        if(ivjApp_ShowStartHelp == null)
        {
            try
            {
                ivjApp_ShowStartHelp = new Checkbox();
                ivjApp_ShowStartHelp.setName("App_ShowStartHelp");
                ivjApp_ShowStartHelp.setLabel("App_ShowStartHelp");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjApp_ShowStartHelp;
    }

    public String getAppletInfo()
    {
        return "paintchat.config.Ao (The) VisualAge for Java (Was created using.)";
    }

    private Button getButton1()
    {
        if(ivjButton1 == null)
        {
            try
            {
                ivjButton1 = new Button();
                ivjButton1.setName("Button1");
                ivjButton1.setBounds(135, 339, 56, 20);
                ivjButton1.setLabel("Button1");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjButton1;
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

    private Checkbox getConnection_GrobalAddress()
    {
        if(ivjConnection_GrobalAddress == null)
        {
            try
            {
                ivjConnection_GrobalAddress = new Checkbox();
                ivjConnection_GrobalAddress.setName("Connection_GrobalAddress");
                ivjConnection_GrobalAddress.setLabel("Connection_GrobalAddress");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjConnection_GrobalAddress;
    }

    private LButton getOk()
    {
        if(ivjOk == null)
        {
            try
            {
                ivjOk = new LButton();
                ivjOk.setName("Ok");
                ivjOk.setText("Ok");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjOk;
    }

    private Panel getPanel1()
    {
        if(ivjPanel1 == null)
        {
            try
            {
                ivjPanel1 = new Panel();
                ivjPanel1.setName("Panel1");
                ivjPanel1.setLayout(getPanel1GridLayout());
                getPanel1().add(getApp_Auto_Lobby(), getApp_Auto_Lobby().getName());
                getPanel1().add(getApp_Auto_Paintchat(), getApp_Auto_Paintchat().getName());
                getPanel1().add(getApp_Auto_Http(), getApp_Auto_Http().getName());
                getPanel1().add(getConnection_GrobalAddress(), getConnection_GrobalAddress().getName());
                getPanel1().add(getApp_ShowStartHelp(), getApp_ShowStartHelp().getName());
                getPanel1().add(getAdmin_ChatMaster(), getAdmin_ChatMaster().getName());
                getPanel1().add(getApp_Get_Index(), getApp_Get_Index().getName());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjPanel1;
    }

    private GridLayout getPanel1GridLayout()
    {
        GridLayout gridlayout = null;
        try
        {
            gridlayout = new GridLayout(0, 2);
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
        return gridlayout;
    }

    private Panel getPanel2()
    {
        if(ivjPanel2 == null)
        {
            try
            {
                ivjPanel2 = new Panel();
                ivjPanel2.setName("Panel2");
                ivjPanel2.setLayout(getPanel2GridLayout());
                getPanel2().add(getAdmin_Password(), getAdmin_Password().getName());
                getPanel2().add(getApp_Cgi(), getApp_Cgi().getName());
                getPanel2().add(getApp_BrowserPath(), getApp_BrowserPath().getName());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjPanel2;
    }

    private GridLayout getPanel2GridLayout()
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
                getPanel3().add(getCancel(), getCancel().getName());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjPanel3;
    }

    private void handleException(Throwable throwable)
    {
    }

    public void init()
    {
        try
        {
            setName("Ao");
            setLayout(new BorderLayout());
            setBackground(new Color(204, 204, 204));
            setSize(446, 266);
            add(getPanel1(), "North");
            add(getPanel2(), "Center");
            add(getPanel3(), "South");
            initConnections();
            Gui.giveDef(this);
            initValue();
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
        getButton1().addActionListener(this);
    }

    public void initValue()
    {
        getResource(super.res, this);
        getParameter(this);
        Gui.giveDef(this);
    }

    public void m_dispose()
    {
        try
        {
            getHelp().reset();
            ((Window)getParent()).dispose();
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
        }
    }

    public void m_save()
    {
        try
        {
            setParameter(this);
            Config config = (Config)((ServerStub)getAppletContext()).getHashTable();
            config.saveConfig(null, null);
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
            Class class1 = Class.forName("paintchat.config.PConfig");
            ClassLoader classloader = class1.getClassLoader();
            PConfig pconfig = (PConfig)Beans.instantiate(classloader, "paintchat.config.PConfig");
            frame.add("Center", pconfig);
            frame.setSize(pconfig.getSize());
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
            System.err.println("java.applet.Applet (The) main() (An exception occurred in)"
);
            throwable.printStackTrace(System.out);
        }
    }

    public void reset()
    {
        getHelp().reset();
    }
}
