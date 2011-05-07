/* ConfigServer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat.config;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
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

import paintchat.Config;
import paintchat.Resource;

import syi.applet.ServerStub;
import syi.awt.Gui;
import syi.awt.LButton;
import syi.awt.LTextField;

public class ConfigServer extends ConfigApplet implements ActionListener
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
    
    public void actionPerformed(ActionEvent actionevent) {
	if (actionevent.getSource() == getButton1())
	    connEtoM1(actionevent);
	if (actionevent.getSource() == getOk()) {
	    mSave();
	    mDestroy();
	}
	if (actionevent.getSource() == getCancel())
	    mDestroy();
    }
    
    public void button1_ActionPerformed(ActionEvent actionevent) {
	/* empty */
    }
    
    private void connEtoC1() {
	try {
	    initValue();
	} catch (Throwable throwable) {
	    handleException(throwable);
	}
    }
    
    private void connEtoM1(ActionEvent actionevent) {
	try {
	    this.stop();
	} catch (Throwable throwable) {
	    handleException(throwable);
	}
    }
    
    public String getAppletInfo() {
	return "paintchat.config.ConfigServer \u306f VisualAge for Java \u3092\u4f7f\u7528\u3057\u3066\u4f5c\u6210\u3055\u308c\u307e\u3057\u305f\u3002";
    }
    
    private Button getButton1() {
	if (ivjButton1 == null) {
	    try {
		ivjButton1 = new Button();
		ivjButton1.setName("Button1");
		ivjButton1.setBounds(134, 283, 56, 20);
		ivjButton1.setLabel("Button1");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjButton1;
    }
    
    private LButton getCancel() {
	if (ivjCancel == null) {
	    try {
		ivjCancel = new LButton();
		ivjCancel.setName("Cancel");
		ivjCancel.setText("Cancel");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjCancel;
    }
    
    private TextField getClient_Image_Height() {
	if (ivjClient_Image_Height == null) {
	    try {
		ivjClient_Image_Height = new TextField();
		ivjClient_Image_Height.setName("Client_Image_Height");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjClient_Image_Height;
    }
    
    private TextField getClient_Image_Width() {
	if (ivjClient_Image_Width == null) {
	    try {
		ivjClient_Image_Width = new TextField();
		ivjClient_Image_Width.setName("Client_Image_Width");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjClient_Image_Width;
    }
    
    private Checkbox getClient_Sound() {
	if (ivjClient_Sound == null) {
	    try {
		ivjClient_Sound = new Checkbox();
		ivjClient_Sound.setName("Client_Sound");
		ivjClient_Sound.setLabel("Client_Sound");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjClient_Sound;
    }
    
    private Label getLabel1() {
	if (ivjLabel1 == null) {
	    try {
		ivjLabel1 = new Label();
		ivjLabel1.setName("Label1");
		ivjLabel1.setAlignment(2);
		ivjLabel1.setText("Client_Image_Width");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjLabel1;
    }
    
    private Label getLabel2() {
	if (ivjLabel2 == null) {
	    try {
		ivjLabel2 = new Label();
		ivjLabel2.setName("Label2");
		ivjLabel2.setAlignment(2);
		ivjLabel2.setText("Client_Image_Height");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjLabel2;
    }
    
    private LButton getOk() {
	if (ivjOk == null) {
	    try {
		ivjOk = new LButton();
		ivjOk.setName("Ok");
		ivjOk.setText("Ok");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjOk;
    }
    
    private Panel getPanel1() {
	if (ivjPanel1 == null) {
	    try {
		ivjPanel1 = new Panel();
		ivjPanel1.setName("Panel1");
		ivjPanel1.setLayout(getPanel1GridLayout());
		getPanel1().add(getServer_Cash_Text(),
				getServer_Cash_Text().getName());
		getPanel1().add(getServer_Cash_Line(),
				getServer_Cash_Line().getName());
		getPanel1().add(getServer_Log_Text(),
				getServer_Log_Text().getName());
		getPanel1().add(getServer_Log_Line(),
				getServer_Log_Line().getName());
		getPanel1().add(getServer_Load_Text(),
				getServer_Load_Text().getName());
		getPanel1().add(getServer_Load_Line(),
				getServer_Load_Line().getName());
		getPanel1().add(getClient_Sound(),
				getClient_Sound().getName());
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjPanel1;
    }
    
    private GridLayout getPanel1GridLayout() {
	GridLayout gridlayout = null;
	try {
	    gridlayout = new GridLayout(0, 2);
	} catch (Throwable throwable) {
	    handleException(throwable);
	}
	return gridlayout;
    }
    
    private Panel getPanel2() {
	if (ivjPanel2 == null) {
	    try {
		ivjPanel2 = new Panel();
		ivjPanel2.setName("Panel2");
		ivjPanel2.setLayout(getPanel2GridLayout());
		ivjPanel2.add(getPanel4());
		getPanel2().add(getServer_Cash_Text_Size(),
				getServer_Cash_Text_Size().getName());
		getPanel2().add(getServer_Cash_Line_Size(),
				getServer_Cash_Line_Size().getName());
		LTextField ltextfield = new LTextField();
		textPermission = ltextfield;
		ltextfield.setText("Client_Permission");
		ltextfield.setName(ltextfield.getText());
		getPanel2().add(ltextfield, ltextfield.getName());
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjPanel2;
    }
    
    private GridLayout getPanel2GridLayout() {
	GridLayout gridlayout = null;
	try {
	    gridlayout = new GridLayout(0, 1);
	} catch (Throwable throwable) {
	    handleException(throwable);
	}
	return gridlayout;
    }
    
    private Panel getPanel3() {
	if (ivjPanel3 == null) {
	    try {
		ivjPanel3 = new Panel();
		ivjPanel3.setName("Panel3");
		ivjPanel3.setLayout(new FlowLayout());
		getPanel3().add(getOk(), getOk().getName());
		getPanel3().add(getCancel(), getCancel().getName());
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjPanel3;
    }
    
    private Panel getPanel4() {
	if (ivjPanel4 == null) {
	    try {
		ivjPanel4 = new Panel();
		ivjPanel4.setName("Panel4");
		ivjPanel4.setLayout(new GridLayout());
		getPanel4().add(getLabel1(), getLabel1().getName());
		getPanel4().add(getClient_Image_Width(),
				getClient_Image_Width().getName());
		getPanel4().add(getLabel2(), getLabel2().getName());
		getPanel4().add(getClient_Image_Height(),
				getClient_Image_Height().getName());
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjPanel4;
    }
    
    private Checkbox getServer_Cash_Line() {
	if (ivjServer_Cash_Line == null) {
	    try {
		ivjServer_Cash_Line = new Checkbox();
		ivjServer_Cash_Line.setName("Server_Cash_Line");
		ivjServer_Cash_Line.setLabel("Server_Cash_Line");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjServer_Cash_Line;
    }
    
    private LTextField getServer_Cash_Line_Size() {
	if (ivjServer_Cash_Line_Size == null) {
	    try {
		ivjServer_Cash_Line_Size = new LTextField();
		ivjServer_Cash_Line_Size.setName("Server_Cash_Line_Size");
		ivjServer_Cash_Line_Size.setText("Server_Cash_Text_Size");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjServer_Cash_Line_Size;
    }
    
    private Checkbox getServer_Cash_Text() {
	if (ivjServer_Cash_Text == null) {
	    try {
		ivjServer_Cash_Text = new Checkbox();
		ivjServer_Cash_Text.setName("Server_Cash_Text");
		ivjServer_Cash_Text.setLabel("Server_Cash_Text");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjServer_Cash_Text;
    }
    
    private LTextField getServer_Cash_Text_Size() {
	if (ivjServer_Cash_Text_Size == null) {
	    try {
		ivjServer_Cash_Text_Size = new LTextField();
		ivjServer_Cash_Text_Size.setName("Server_Cash_Text_Size");
		ivjServer_Cash_Text_Size.setText("Server_Cash_Text_Size");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjServer_Cash_Text_Size;
    }
    
    private Checkbox getServer_Load_Line() {
	if (ivjServer_Load_Line == null) {
	    try {
		ivjServer_Load_Line = new Checkbox();
		ivjServer_Load_Line.setName("Server_Load_Line");
		ivjServer_Load_Line.setLabel("Server_Load_Line");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjServer_Load_Line;
    }
    
    private Checkbox getServer_Load_Text() {
	if (ivjServer_Load_Text == null) {
	    try {
		ivjServer_Load_Text = new Checkbox();
		ivjServer_Load_Text.setName("Server_Load_Text");
		ivjServer_Load_Text.setLabel("Server_Load_Text");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjServer_Load_Text;
    }
    
    private Checkbox getServer_Log_Line() {
	if (ivjServer_Log_Line == null) {
	    try {
		ivjServer_Log_Line = new Checkbox();
		ivjServer_Log_Line.setName("Server_Log_Line");
		ivjServer_Log_Line.setLabel("Server_Log_Line");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjServer_Log_Line;
    }
    
    private Checkbox getServer_Log_Text() {
	if (ivjServer_Log_Text == null) {
	    try {
		ivjServer_Log_Text = new Checkbox();
		ivjServer_Log_Text.setName("Server_Log_Text");
		ivjServer_Log_Text.setLabel("Server_Log_Text");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjServer_Log_Text;
    }
    
    private void handleException(Throwable throwable) {
	/* empty */
    }
    
    public void init() {
	try {
	    this.setName("ConfigServer");
	    this.setLayout(new BorderLayout());
	    this.setSize(359, 226);
	    this.add(getPanel1(), "North");
	    this.add(getPanel2(), "Center");
	    this.add(getPanel3(), "South");
	    initConnections();
	    connEtoC1();
	    Gui.giveDef(this);
	} catch (Throwable throwable) {
	    handleException(throwable);
	}
    }
    
    private void initConnections() throws Exception {
	getOk().addActionListener(this);
	getCancel().addActionListener(this);
	this.setMouseListener(this, this);
	getButton1().addActionListener(this);
    }
    
    public void initValue() {
	this.getResource(res, this);
	this.getParameter(this);
	getClient_Image_Width()
	    .setText(this.getParameter(getClient_Image_Width().getName()));
	getClient_Image_Height()
	    .setText(this.getParameter(getClient_Image_Height().getName()));
	textPermission.setText(this.getParameter(textPermission.getName()));
    }
    
    public static void main(String[] strings) {
	try {
	    Frame frame = new Frame();
	    Class var_class = Class.forName("paintchat.config.ConfigServer");
	    ClassLoader classloader = var_class.getClassLoader();
	    ConfigServer configserver
		= ((ConfigServer)
		   Beans.instantiate(classloader,
				     "paintchat.config.ConfigServer"));
	    frame.add("Center", configserver);
	    frame.setSize(configserver.getSize());
	    frame.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent windowevent) {
		    System.exit(0);
		}
	    });
	    frame.setVisible(true);
	} catch (Throwable throwable) {
	    System.err.println
		("paintchat.config.ConfigApplet \u306e main() \u3067\u4f8b\u5916\u304c\u767a\u751f\u3057\u307e\u3057\u305f");
	    throwable.printStackTrace(System.out);
	}
    }
    
    public void mDestroy() {
	try {
	    this.getHelp().reset();
	    ((Window) this.getParent()).dispose();
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	}
    }
    
    public void mSave() {
	try {
	    this.setParameter(this);
	    Config config
		= ((Config)
		   ((ServerStub) this.getAppletContext()).getHashTable());
	    config.save
		(new FileOutputStream(config.getString("File_Config",
						       "cnf/paintchat.cf")),
		 Resource.loadResource("Config"));
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
}
