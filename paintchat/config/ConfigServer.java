package paintchat.config;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Container;
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
import paintchat.Config;
import paintchat.Resource;
import syi.applet.ServerStub;
import syi.awt.Gui;
import syi.awt.HelpWindow;
import syi.awt.LButton;
import syi.awt.LTextField;
import syi.util.PProperties;

public class ConfigServer extends ConfigApplet
  implements ActionListener
{
  private Panel ivjPanel1 = null;
  private GridLayout ivjPanel1GridLayout = null;
  private Panel ivjPanel2 = null;
  private GridLayout ivjPanel2GridLayout = null;
  private Checkbox ivjServer_Load_Line = null;
  private Checkbox ivjServer_Log_Text = null;
  private Button ivjButton1 = null;
  private LButton ivjCancel = null;
  private LButton ivjOk = null;
  private Panel ivjPanel3 = null;
  private Checkbox ivjServer_Cash_Line = null;
  private LTextField ivjServer_Cash_Line_Size = null;
  private Checkbox ivjServer_Cash_Text = null;
  private LTextField ivjServer_Cash_Text_Size = null;
  private Checkbox ivjServer_Log_Line = null;
  private Checkbox ivjServer_Load_Text = null;
  private TextField ivjClient_Image_Height = null;
  private TextField ivjClient_Image_Width = null;
  private Label ivjLabel1 = null;
  private Label ivjLabel2 = null;
  private Panel ivjPanel4 = null;
  private Checkbox ivjClient_Sound = null;
  private LTextField textPermission;

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    if (paramActionEvent.getSource() == getButton1())
      connEtoM1(paramActionEvent);
    if (paramActionEvent.getSource() == getOk())
    {
      mSave();
      mDestroy();
    }
    if (paramActionEvent.getSource() == getCancel())
      mDestroy();
  }

  public void button1_ActionPerformed(ActionEvent paramActionEvent)
  {
  }

  private void connEtoC1()
  {
    try
    {
      initValue();
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
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

  public String getAppletInfo()
  {
    return "paintchat.config.ConfigServer は VisualAge for Java を使用して作成されました。";
  }

  private Button getButton1()
  {
    if (this.ivjButton1 == null)
      try
      {
        this.ivjButton1 = new Button();
        this.ivjButton1.setName("Button1");
        this.ivjButton1.setBounds(134, 283, 56, 20);
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

  private TextField getClient_Image_Height()
  {
    if (this.ivjClient_Image_Height == null)
      try
      {
        this.ivjClient_Image_Height = new TextField();
        this.ivjClient_Image_Height.setName("Client_Image_Height");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjClient_Image_Height;
  }

  private TextField getClient_Image_Width()
  {
    if (this.ivjClient_Image_Width == null)
      try
      {
        this.ivjClient_Image_Width = new TextField();
        this.ivjClient_Image_Width.setName("Client_Image_Width");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjClient_Image_Width;
  }

  private Checkbox getClient_Sound()
  {
    if (this.ivjClient_Sound == null)
      try
      {
        this.ivjClient_Sound = new Checkbox();
        this.ivjClient_Sound.setName("Client_Sound");
        this.ivjClient_Sound.setLabel("Client_Sound");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjClient_Sound;
  }

  private Label getLabel1()
  {
    if (this.ivjLabel1 == null)
      try
      {
        this.ivjLabel1 = new Label();
        this.ivjLabel1.setName("Label1");
        this.ivjLabel1.setAlignment(2);
        this.ivjLabel1.setText("Client_Image_Width");
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
        this.ivjLabel2.setAlignment(2);
        this.ivjLabel2.setText("Client_Image_Height");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjLabel2;
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
        getPanel1().add(getServer_Cash_Text(), getServer_Cash_Text().getName());
        getPanel1().add(getServer_Cash_Line(), getServer_Cash_Line().getName());
        getPanel1().add(getServer_Log_Text(), getServer_Log_Text().getName());
        getPanel1().add(getServer_Log_Line(), getServer_Log_Line().getName());
        getPanel1().add(getServer_Load_Text(), getServer_Load_Text().getName());
        getPanel1().add(getServer_Load_Line(), getServer_Load_Line().getName());
        getPanel1().add(getClient_Sound(), getClient_Sound().getName());
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
        this.ivjPanel2.add(getPanel4());
        getPanel2().add(getServer_Cash_Text_Size(), getServer_Cash_Text_Size().getName());
        getPanel2().add(getServer_Cash_Line_Size(), getServer_Cash_Line_Size().getName());
        LTextField localLTextField = new LTextField();
        this.textPermission = localLTextField;
        localLTextField.setText("Client_Permission");
        localLTextField.setName(localLTextField.getText());
        getPanel2().add(localLTextField, localLTextField.getName());
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

  private Panel getPanel4()
  {
    if (this.ivjPanel4 == null)
      try
      {
        this.ivjPanel4 = new Panel();
        this.ivjPanel4.setName("Panel4");
        this.ivjPanel4.setLayout(new GridLayout());
        getPanel4().add(getLabel1(), getLabel1().getName());
        getPanel4().add(getClient_Image_Width(), getClient_Image_Width().getName());
        getPanel4().add(getLabel2(), getLabel2().getName());
        getPanel4().add(getClient_Image_Height(), getClient_Image_Height().getName());
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjPanel4;
  }

  private Checkbox getServer_Cash_Line()
  {
    if (this.ivjServer_Cash_Line == null)
      try
      {
        this.ivjServer_Cash_Line = new Checkbox();
        this.ivjServer_Cash_Line.setName("Server_Cash_Line");
        this.ivjServer_Cash_Line.setLabel("Server_Cash_Line");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjServer_Cash_Line;
  }

  private LTextField getServer_Cash_Line_Size()
  {
    if (this.ivjServer_Cash_Line_Size == null)
      try
      {
        this.ivjServer_Cash_Line_Size = new LTextField();
        this.ivjServer_Cash_Line_Size.setName("Server_Cash_Line_Size");
        this.ivjServer_Cash_Line_Size.setText("Server_Cash_Text_Size");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjServer_Cash_Line_Size;
  }

  private Checkbox getServer_Cash_Text()
  {
    if (this.ivjServer_Cash_Text == null)
      try
      {
        this.ivjServer_Cash_Text = new Checkbox();
        this.ivjServer_Cash_Text.setName("Server_Cash_Text");
        this.ivjServer_Cash_Text.setLabel("Server_Cash_Text");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjServer_Cash_Text;
  }

  private LTextField getServer_Cash_Text_Size()
  {
    if (this.ivjServer_Cash_Text_Size == null)
      try
      {
        this.ivjServer_Cash_Text_Size = new LTextField();
        this.ivjServer_Cash_Text_Size.setName("Server_Cash_Text_Size");
        this.ivjServer_Cash_Text_Size.setText("Server_Cash_Text_Size");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjServer_Cash_Text_Size;
  }

  private Checkbox getServer_Load_Line()
  {
    if (this.ivjServer_Load_Line == null)
      try
      {
        this.ivjServer_Load_Line = new Checkbox();
        this.ivjServer_Load_Line.setName("Server_Load_Line");
        this.ivjServer_Load_Line.setLabel("Server_Load_Line");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjServer_Load_Line;
  }

  private Checkbox getServer_Load_Text()
  {
    if (this.ivjServer_Load_Text == null)
      try
      {
        this.ivjServer_Load_Text = new Checkbox();
        this.ivjServer_Load_Text.setName("Server_Load_Text");
        this.ivjServer_Load_Text.setLabel("Server_Load_Text");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjServer_Load_Text;
  }

  private Checkbox getServer_Log_Line()
  {
    if (this.ivjServer_Log_Line == null)
      try
      {
        this.ivjServer_Log_Line = new Checkbox();
        this.ivjServer_Log_Line.setName("Server_Log_Line");
        this.ivjServer_Log_Line.setLabel("Server_Log_Line");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjServer_Log_Line;
  }

  private Checkbox getServer_Log_Text()
  {
    if (this.ivjServer_Log_Text == null)
      try
      {
        this.ivjServer_Log_Text = new Checkbox();
        this.ivjServer_Log_Text.setName("Server_Log_Text");
        this.ivjServer_Log_Text.setLabel("Server_Log_Text");
      }
      catch (Throwable localThrowable)
      {
        handleException(localThrowable);
      }
    return this.ivjServer_Log_Text;
  }

  private void handleException(Throwable paramThrowable)
  {
  }

  public void init()
  {
    try
    {
      setName("ConfigServer");
      setLayout(new BorderLayout());
      setSize(359, 226);
      add(getPanel1(), "North");
      add(getPanel2(), "Center");
      add(getPanel3(), "South");
      initConnections();
      connEtoC1();
      Gui.giveDef(this);
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
    getClient_Image_Width().setText(getParameter(getClient_Image_Width().getName()));
    getClient_Image_Height().setText(getParameter(getClient_Image_Height().getName()));
    this.textPermission.setText(getParameter(this.textPermission.getName()));
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      Frame localFrame = new Frame();
      Class localClass = Class.forName("paintchat.config.ConfigServer");
      ClassLoader localClassLoader = localClass.getClassLoader();
      ConfigServer localConfigServer = (ConfigServer)Beans.instantiate(localClassLoader, "paintchat.config.ConfigServer");
      localFrame.add("Center", localConfigServer);
      localFrame.setSize(localConfigServer.getSize());
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
      System.err.println("paintchat.config.ConfigApplet の main() で例外が発生しました");
      localThrowable.printStackTrace(System.out);
    }
  }

  public void mDestroy()
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

  public void mSave()
  {
    try
    {
      setParameter(this);
      Config localConfig = (Config)((ServerStub)getAppletContext()).getHashTable();
      localConfig.save(new FileOutputStream(localConfig.getString("File_Config", "cnf/paintchat.cf")), Resource.loadResource("Config"));
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.config.ConfigServer
 * JD-Core Version:    0.6.0
 */