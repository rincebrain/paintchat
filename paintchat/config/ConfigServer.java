package paintchat.config;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.beans.Beans;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.EventObject;
import paintchat.Config;
import paintchat.Resource;
import syi.applet.ServerStub;
import syi.awt.*;
import syi.util.PProperties;

// Referenced classes of package paintchat.config:
//            ConfigApplet

public class ConfigServer extends ConfigApplet
    implements ActionListener
{

    private Panel ivjPanel1;
    private GridLayout ivjPanel1GridLayout;
    private Panel ivjPanel2;
    private GridLayout ivjPanel2GridLayout;
    private Checkbox ivjServer_Load_Line;
    private Checkbox ivjServer_Log_Text;
    private Button ivjButton1;
    private LButton ivjCancel;
    private LButton ivjOk;
    private Panel ivjPanel3;
    private Checkbox ivjServer_Cash_Line;
    private LTextField ivjServer_Cash_Line_Size;
    private Checkbox ivjServer_Cash_Text;
    private LTextField ivjServer_Cash_Text_Size;
    private Checkbox ivjServer_Log_Line;
    private Checkbox ivjServer_Load_Text;
    private TextField ivjClient_Image_Height;
    private TextField ivjClient_Image_Width;
    private Label ivjLabel1;
    private Label ivjLabel2;
    private Panel ivjPanel4;
    private Checkbox ivjClient_Sound;
    private LTextField textPermission;

    public ConfigServer()
    {
        ivjPanel1 = null;
        ivjPanel1GridLayout = null;
        ivjPanel2 = null;
        ivjPanel2GridLayout = null;
        ivjServer_Load_Line = null;
        ivjServer_Log_Text = null;
        ivjButton1 = null;
        ivjCancel = null;
        ivjOk = null;
        ivjPanel3 = null;
        ivjServer_Cash_Line = null;
        ivjServer_Cash_Line_Size = null;
        ivjServer_Cash_Text = null;
        ivjServer_Cash_Text_Size = null;
        ivjServer_Log_Line = null;
        ivjServer_Load_Text = null;
        ivjClient_Image_Height = null;
        ivjClient_Image_Width = null;
        ivjLabel1 = null;
        ivjLabel2 = null;
        ivjPanel4 = null;
        ivjClient_Sound = null;
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getSource() == getButton1())
        {
            connEtoM1(actionevent);
        }
        if(actionevent.getSource() == getOk())
        {
            mSave();
            mDestroy();
        }
        if(actionevent.getSource() == getCancel())
        {
            mDestroy();
        }
    }

    public void button1_ActionPerformed(ActionEvent actionevent)
    {
    }

    private void connEtoC1()
    {
        try
        {
            initValue();
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
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

    public String getAppletInfo()
    {
        return "paintchat.config.ConfigServer (The) VisualAge for Java (Was created using.)";
    }

    private Button getButton1()
    {
        if(ivjButton1 == null)
        {
            try
            {
                ivjButton1 = new Button();
                ivjButton1.setName("Button1");
                ivjButton1.setBounds(134, 283, 56, 20);
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

    private TextField getClient_Image_Height()
    {
        if(ivjClient_Image_Height == null)
        {
            try
            {
                ivjClient_Image_Height = new TextField();
                ivjClient_Image_Height.setName("Client_Image_Height");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjClient_Image_Height;
    }

    private TextField getClient_Image_Width()
    {
        if(ivjClient_Image_Width == null)
        {
            try
            {
                ivjClient_Image_Width = new TextField();
                ivjClient_Image_Width.setName("Client_Image_Width");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjClient_Image_Width;
    }

    private Checkbox getClient_Sound()
    {
        if(ivjClient_Sound == null)
        {
            try
            {
                ivjClient_Sound = new Checkbox();
                ivjClient_Sound.setName("Client_Sound");
                ivjClient_Sound.setLabel("Client_Sound");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjClient_Sound;
    }

    private Label getLabel1()
    {
        if(ivjLabel1 == null)
        {
            try
            {
                ivjLabel1 = new Label();
                ivjLabel1.setName("Label1");
                ivjLabel1.setAlignment(2);
                ivjLabel1.setText("Client_Image_Width");
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
                ivjLabel2.setAlignment(2);
                ivjLabel2.setText("Client_Image_Height");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjLabel2;
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
                getPanel1().add(getServer_Cash_Text(), getServer_Cash_Text().getName());
                getPanel1().add(getServer_Cash_Line(), getServer_Cash_Line().getName());
                getPanel1().add(getServer_Log_Text(), getServer_Log_Text().getName());
                getPanel1().add(getServer_Log_Line(), getServer_Log_Line().getName());
                getPanel1().add(getServer_Load_Text(), getServer_Load_Text().getName());
                getPanel1().add(getServer_Load_Line(), getServer_Load_Line().getName());
                getPanel1().add(getClient_Sound(), getClient_Sound().getName());
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
                ivjPanel2.add(getPanel4());
                getPanel2().add(getServer_Cash_Text_Size(), getServer_Cash_Text_Size().getName());
                getPanel2().add(getServer_Cash_Line_Size(), getServer_Cash_Line_Size().getName());
                LTextField ltextfield = new LTextField();
                textPermission = ltextfield;
                ltextfield.setText("Client_Permission");
                ltextfield.setName(ltextfield.getText());
                getPanel2().add(ltextfield, ltextfield.getName());
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

    private Panel getPanel4()
    {
        if(ivjPanel4 == null)
        {
            try
            {
                ivjPanel4 = new Panel();
                ivjPanel4.setName("Panel4");
                ivjPanel4.setLayout(new GridLayout());
                getPanel4().add(getLabel1(), getLabel1().getName());
                getPanel4().add(getClient_Image_Width(), getClient_Image_Width().getName());
                getPanel4().add(getLabel2(), getLabel2().getName());
                getPanel4().add(getClient_Image_Height(), getClient_Image_Height().getName());
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjPanel4;
    }

    private Checkbox getServer_Cash_Line()
    {
        if(ivjServer_Cash_Line == null)
        {
            try
            {
                ivjServer_Cash_Line = new Checkbox();
                ivjServer_Cash_Line.setName("Server_Cash_Line");
                ivjServer_Cash_Line.setLabel("Server_Cash_Line");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjServer_Cash_Line;
    }

    private LTextField getServer_Cash_Line_Size()
    {
        if(ivjServer_Cash_Line_Size == null)
        {
            try
            {
                ivjServer_Cash_Line_Size = new LTextField();
                ivjServer_Cash_Line_Size.setName("Server_Cash_Line_Size");
                ivjServer_Cash_Line_Size.setText("Server_Cash_Text_Size");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjServer_Cash_Line_Size;
    }

    private Checkbox getServer_Cash_Text()
    {
        if(ivjServer_Cash_Text == null)
        {
            try
            {
                ivjServer_Cash_Text = new Checkbox();
                ivjServer_Cash_Text.setName("Server_Cash_Text");
                ivjServer_Cash_Text.setLabel("Server_Cash_Text");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjServer_Cash_Text;
    }

    private LTextField getServer_Cash_Text_Size()
    {
        if(ivjServer_Cash_Text_Size == null)
        {
            try
            {
                ivjServer_Cash_Text_Size = new LTextField();
                ivjServer_Cash_Text_Size.setName("Server_Cash_Text_Size");
                ivjServer_Cash_Text_Size.setText("Server_Cash_Text_Size");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjServer_Cash_Text_Size;
    }

    private Checkbox getServer_Load_Line()
    {
        if(ivjServer_Load_Line == null)
        {
            try
            {
                ivjServer_Load_Line = new Checkbox();
                ivjServer_Load_Line.setName("Server_Load_Line");
                ivjServer_Load_Line.setLabel("Server_Load_Line");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjServer_Load_Line;
    }

    private Checkbox getServer_Load_Text()
    {
        if(ivjServer_Load_Text == null)
        {
            try
            {
                ivjServer_Load_Text = new Checkbox();
                ivjServer_Load_Text.setName("Server_Load_Text");
                ivjServer_Load_Text.setLabel("Server_Load_Text");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjServer_Load_Text;
    }

    private Checkbox getServer_Log_Line()
    {
        if(ivjServer_Log_Line == null)
        {
            try
            {
                ivjServer_Log_Line = new Checkbox();
                ivjServer_Log_Line.setName("Server_Log_Line");
                ivjServer_Log_Line.setLabel("Server_Log_Line");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjServer_Log_Line;
    }

    private Checkbox getServer_Log_Text()
    {
        if(ivjServer_Log_Text == null)
        {
            try
            {
                ivjServer_Log_Text = new Checkbox();
                ivjServer_Log_Text.setName("Server_Log_Text");
                ivjServer_Log_Text.setLabel("Server_Log_Text");
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
            }
        }
        return ivjServer_Log_Text;
    }

    private void handleException(Throwable throwable)
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
        getClient_Image_Width().setText(getParameter(getClient_Image_Width().getName()));
        getClient_Image_Height().setText(getParameter(getClient_Image_Height().getName()));
        textPermission.setText(getParameter(textPermission.getName()));
    }

    public static void main(String args[])
    {
        try
        {
            Frame frame = new Frame();
            Class class1 = Class.forName("paintchat.config.ConfigServer");
            ClassLoader classloader = class1.getClassLoader();
            ConfigServer configserver = (ConfigServer)Beans.instantiate(classloader, "paintchat.config.ConfigServer");
            frame.add("Center", configserver);
            frame.setSize(configserver.getSize());
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
            System.err.println("paintchat.config.ConfigApplet (The) main() (An exception occurred in)");
            throwable.printStackTrace(System.out);
        }
    }

    public void mDestroy()
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

    public void mSave()
    {
        try
        {
            setParameter(this);
            Config config = (Config)((ServerStub)getAppletContext()).getHashTable();
            config.save(new FileOutputStream(config.getString("File_Config", "cnf/paintchat.cf")), Resource.loadResource("Config"));
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }
}
