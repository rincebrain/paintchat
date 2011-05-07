package paintchat.config;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;
import java.io.File;
import java.io.PrintStream;
import java.util.EventObject;
import paintchat.Config;
import syi.applet.ServerStub;
import syi.awt.Awt;
import syi.awt.Gui;
import syi.awt.HelpWindow;
import syi.awt.LButton;
import syi.awt.LTextField;

public class PConfig extends ConfigApplet
  implements ActionListener
{
  private Panel ivjPanel1 = null;
  private GridLayout ivjPanel1GridLayout = null;
  private LTextField ivjAdmin_Password = null;
  private Checkbox ivjApp_Auto_Http = null;
  private Checkbox ivjApp_Auto_Lobby = null;
  private Checkbox ivjApp_Auto_Paintchat = null;
  private LTextField ivjApp_BrowserPath = null;
  private LTextField ivjApp_Cgi = null;
  private LButton ivjCancel = null;
  private Checkbox ivjConnection_GrobalAddress = null;
  private LButton ivjOk = null;
  private Panel ivjPanel2 = null;
  private Panel ivjPanel3 = null;
  private GridLayout ivjPanel2GridLayout = null;
  private Checkbox ivjApp_ShowStartHelp = null;
  private Button ivjButton1 = null;
  private Checkbox ivjAdmin_ChatMaster = null;
  private Checkbox ivjApp_Get_Index = null;

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    if (paramActionEvent.getSource() == getButton1())
      connEtoM1(paramActionEvent);
    if (paramActionEvent.getSource() == getOk())
    {
      m_save();
      m_dispose();
    }
    if (paramActionEvent.getSource() == getCancel())
      m_dispose();
  }

  public void addsMouseListener(Container paramContainer)
  {
    Component[] arrayOfComponent = paramContainer.getComponents();
    if (arrayOfComponent != null)
      for (int i = 0; i < arrayOfComponent.length; i++)
        if ((arrayOfComponent[i] instanceof Container))
          addsMouseListener((Container)arrayOfComponent[i]);
        else
          arrayOfComponent[i].addMouseListener(this);
    paramContainer.addMouseListener(this);
  }

  public void browse(TextField paramTextField, boolean paramBoolean)
  {
    String str = paramTextField.getText();
    File localFile = Gui.fileDialog(Awt.getPFrame(), str, paramBoolean);
    paramTextField.setText(localFile.getAbsolutePath());
  }

  private void connEtoM1(ActionEvent paramActionEvent)
  {
    try
    {
      stop();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
  }

  private Checkbox getAdmin_ChatMaster()
  {
    if (this.ivjAdmin_ChatMaster == null)
      try
      {
        this.ivjAdmin_ChatMaster = new Checkbox();
        this.ivjAdmin_ChatMaster.setName("Admin_ChatMaster");
        this.ivjAdmin_ChatMaster.setLabel("Admin_ChatMaster");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjAdmin_ChatMaster;
  }

  private LTextField getAdmin_Password()
  {
    if (this.ivjAdmin_Password == null)
      try
      {
        this.ivjAdmin_Password = new LTextField();
        this.ivjAdmin_Password.setName("Admin_Password");
        this.ivjAdmin_Password.setTitle("Admin_Password");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjAdmin_Password;
  }

  private Checkbox getApp_Auto_Http()
  {
    if (this.ivjApp_Auto_Http == null)
      try
      {
        this.ivjApp_Auto_Http = new Checkbox();
        this.ivjApp_Auto_Http.setName("App_Auto_Http");
        this.ivjApp_Auto_Http.setLabel("App_Auto_Http");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjApp_Auto_Http;
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

  private Checkbox getApp_Auto_Paintchat()
  {
    if (this.ivjApp_Auto_Paintchat == null)
      try
      {
        this.ivjApp_Auto_Paintchat = new Checkbox();
        this.ivjApp_Auto_Paintchat.setName("App_Auto_Paintchat");
        this.ivjApp_Auto_Paintchat.setLabel("App_Auto_Paintchat");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjApp_Auto_Paintchat;
  }

  private LTextField getApp_BrowserPath()
  {
    if (this.ivjApp_BrowserPath == null)
      try
      {
        this.ivjApp_BrowserPath = new LTextField();
        this.ivjApp_BrowserPath.setName("App_BrowserPath");
        this.ivjApp_BrowserPath.setTitle("App_BrowserPath");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjApp_BrowserPath;
  }

  private LTextField getApp_Cgi()
  {
    if (this.ivjApp_Cgi == null)
      try
      {
        this.ivjApp_Cgi = new LTextField();
        this.ivjApp_Cgi.setName("App_Cgi");
        this.ivjApp_Cgi.setTitle("App_Cgi");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjApp_Cgi;
  }

  private Checkbox getApp_Get_Index()
  {
    if (this.ivjApp_Get_Index == null)
      try
      {
        this.ivjApp_Get_Index = new Checkbox();
        this.ivjApp_Get_Index.setName("App_Get_Index");
        this.ivjApp_Get_Index.setLabel("App_Get_Index");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjApp_Get_Index;
  }

  private Checkbox getApp_ShowStartHelp()
  {
    if (this.ivjApp_ShowStartHelp == null)
      try
      {
        this.ivjApp_ShowStartHelp = new Checkbox();
        this.ivjApp_ShowStartHelp.setName("App_ShowStartHelp");
        this.ivjApp_ShowStartHelp.setLabel("App_ShowStartHelp");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjApp_ShowStartHelp;
  }

  public String getAppletInfo()
  {
    return "paintchat.config.Ao は VisualAge for Java を使用して作成されました。";
  }

  private Button getButton1()
  {
    if (this.ivjButton1 == null)
      try
      {
        this.ivjButton1 = new Button();
        this.ivjButton1.setName("Button1");
        this.ivjButton1.setBounds(135, 339, 56, 20);
        this.ivjButton1.setLabel("Button1");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjButton1;
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

  private Checkbox getConnection_GrobalAddress()
  {
    if (this.ivjConnection_GrobalAddress == null)
      try
      {
        this.ivjConnection_GrobalAddress = new Checkbox();
        this.ivjConnection_GrobalAddress.setName("Connection_GrobalAddress");
        this.ivjConnection_GrobalAddress.setLabel("Connection_GrobalAddress");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjConnection_GrobalAddress;
  }

  private LButton getOk()
  {
    if (this.ivjOk == null)
      try
      {
        this.ivjOk = new LButton();
        this.ivjOk.setName("Ok");
        this.ivjOk.setText("Ok");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjOk;
  }

  private Panel getPanel1()
  {
    if (this.ivjPanel1 == null)
      try
      {
        this.ivjPanel1 = new Panel();
        this.ivjPanel1.setName("Panel1");
        this.ivjPanel1.setLayout(getPanel1GridLayout());
        getPanel1().add(getApp_Auto_Lobby(), getApp_Auto_Lobby().getName());
        getPanel1().add(getApp_Auto_Paintchat(), getApp_Auto_Paintchat().getName());
        getPanel1().add(getApp_Auto_Http(), getApp_Auto_Http().getName());
        getPanel1().add(getConnection_GrobalAddress(), getConnection_GrobalAddress().getName());
        getPanel1().add(getApp_ShowStartHelp(), getApp_ShowStartHelp().getName());
        getPanel1().add(getAdmin_ChatMaster(), getAdmin_ChatMaster().getName());
        getPanel1().add(getApp_Get_Index(), getApp_Get_Index().getName());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjPanel1;
  }

  private GridLayout getPanel1GridLayout()
  {
    GridLayout localGridLayout = null;
    try
    {
      localGridLayout = new GridLayout(0, 2);
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
    return localGridLayout;
  }

  private Panel getPanel2()
  {
    if (this.ivjPanel2 == null)
      try
      {
        this.ivjPanel2 = new Panel();
        this.ivjPanel2.setName("Panel2");
        this.ivjPanel2.setLayout(getPanel2GridLayout());
        getPanel2().add(getAdmin_Password(), getAdmin_Password().getName());
        getPanel2().add(getApp_Cgi(), getApp_Cgi().getName());
        getPanel2().add(getApp_BrowserPath(), getApp_BrowserPath().getName());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjPanel2;
  }

  private GridLayout getPanel2GridLayout()
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

  private Panel getPanel3()
  {
    if (this.ivjPanel3 == null)
      try
      {
        this.ivjPanel3 = new Panel();
        this.ivjPanel3.setName("Panel3");
        this.ivjPanel3.setLayout(new FlowLayout());
        getPanel3().add(getOk(), getOk().getName());
        getPanel3().add(getCancel(), getCancel().getName());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjPanel3;
  }

  private void handleException(Throwable paramThrowable)
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
    getButton1().addActionListener(this);
  }

  public void initValue()
  {
    getResource(this.res, this);
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
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
  }

  public void m_save()
  {
    try
    {
      setParameter(this);
      Config localConfig = (Config)((ServerStub)getAppletContext()).getHashTable();
      localConfig.saveConfig(null, null);
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
      Class localClass = Class.forName("paintchat.config.PConfig");
      ClassLoader localClassLoader = localClass.getClassLoader();
      PConfig localPConfig = (PConfig)Beans.instantiate(localClassLoader, "paintchat.config.PConfig");
      localFrame.add("Center", localPConfig);
      localFrame.setSize(localPConfig.getSize());
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

  public void reset()
  {
    getHelp().reset();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.config.PConfig
 * JD-Core Version:    0.6.0
 */