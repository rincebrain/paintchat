/* PConfig - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat.config;
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
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;
import java.io.File;

import paintchat.Config;

import syi.applet.ServerStub;
import syi.awt.Awt;
import syi.awt.Gui;
import syi.awt.LButton;
import syi.awt.LTextField;

public class PConfig extends ConfigApplet implements ActionListener
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
    
    public void actionPerformed(ActionEvent actionevent) {
	if (actionevent.getSource() == getButton1())
	    connEtoM1(actionevent);
	if (actionevent.getSource() == getOk()) {
	    m_save();
	    m_dispose();
	}
	if (actionevent.getSource() == getCancel())
	    m_dispose();
    }
    
    public void addsMouseListener(Container container) {
	Component[] components = container.getComponents();
	if (components != null) {
	    for (int i = 0; i < components.length; i++) {
		if (components[i] instanceof Container)
		    addsMouseListener((Container) components[i]);
		else
		    components[i].addMouseListener(this);
	    }
	}
	container.addMouseListener(this);
    }
    
    public void browse(TextField textfield, boolean bool) {
	String string = textfield.getText();
	File file = Gui.fileDialog(Awt.getPFrame(), string, bool);
	textfield.setText(file.getAbsolutePath());
    }
    
    private void connEtoM1(ActionEvent actionevent) {
	try {
	    this.stop();
	} catch (Throwable throwable) {
	    handleException(throwable);
	}
    }
    
    private Checkbox getAdmin_ChatMaster() {
	if (ivjAdmin_ChatMaster == null) {
	    try {
		ivjAdmin_ChatMaster = new Checkbox();
		ivjAdmin_ChatMaster.setName("Admin_ChatMaster");
		ivjAdmin_ChatMaster.setLabel("Admin_ChatMaster");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjAdmin_ChatMaster;
    }
    
    private LTextField getAdmin_Password() {
	if (ivjAdmin_Password == null) {
	    try {
		ivjAdmin_Password = new LTextField();
		ivjAdmin_Password.setName("Admin_Password");
		ivjAdmin_Password.setTitle("Admin_Password");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjAdmin_Password;
    }
    
    private Checkbox getApp_Auto_Http() {
	if (ivjApp_Auto_Http == null) {
	    try {
		ivjApp_Auto_Http = new Checkbox();
		ivjApp_Auto_Http.setName("App_Auto_Http");
		ivjApp_Auto_Http.setLabel("App_Auto_Http");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjApp_Auto_Http;
    }
    
    private Checkbox getApp_Auto_Lobby() {
	if (ivjApp_Auto_Lobby == null) {
	    try {
		ivjApp_Auto_Lobby = new Checkbox();
		ivjApp_Auto_Lobby.setName("App_Auto_Lobby");
		ivjApp_Auto_Lobby.setLabel("App_Auto_Lobby");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjApp_Auto_Lobby;
    }
    
    private Checkbox getApp_Auto_Paintchat() {
	if (ivjApp_Auto_Paintchat == null) {
	    try {
		ivjApp_Auto_Paintchat = new Checkbox();
		ivjApp_Auto_Paintchat.setName("App_Auto_Paintchat");
		ivjApp_Auto_Paintchat.setLabel("App_Auto_Paintchat");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjApp_Auto_Paintchat;
    }
    
    private LTextField getApp_BrowserPath() {
	if (ivjApp_BrowserPath == null) {
	    try {
		ivjApp_BrowserPath = new LTextField();
		ivjApp_BrowserPath.setName("App_BrowserPath");
		ivjApp_BrowserPath.setTitle("App_BrowserPath");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjApp_BrowserPath;
    }
    
    private LTextField getApp_Cgi() {
	if (ivjApp_Cgi == null) {
	    try {
		ivjApp_Cgi = new LTextField();
		ivjApp_Cgi.setName("App_Cgi");
		ivjApp_Cgi.setTitle("App_Cgi");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjApp_Cgi;
    }
    
    private Checkbox getApp_Get_Index() {
	if (ivjApp_Get_Index == null) {
	    try {
		ivjApp_Get_Index = new Checkbox();
		ivjApp_Get_Index.setName("App_Get_Index");
		ivjApp_Get_Index.setLabel("App_Get_Index");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjApp_Get_Index;
    }
    
    private Checkbox getApp_ShowStartHelp() {
	if (ivjApp_ShowStartHelp == null) {
	    try {
		ivjApp_ShowStartHelp = new Checkbox();
		ivjApp_ShowStartHelp.setName("App_ShowStartHelp");
		ivjApp_ShowStartHelp.setLabel("App_ShowStartHelp");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjApp_ShowStartHelp;
    }
    
    public String getAppletInfo() {
	return "paintchat.config.Ao \u306f VisualAge for Java \u3092\u4f7f\u7528\u3057\u3066\u4f5c\u6210\u3055\u308c\u307e\u3057\u305f\u3002";
    }
    
    private Button getButton1() {
	if (ivjButton1 == null) {
	    try {
		ivjButton1 = new Button();
		ivjButton1.setName("Button1");
		ivjButton1.setBounds(135, 339, 56, 20);
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
    
    private Checkbox getConnection_GrobalAddress() {
	if (ivjConnection_GrobalAddress == null) {
	    try {
		ivjConnection_GrobalAddress = new Checkbox();
		ivjConnection_GrobalAddress
		    .setName("Connection_GrobalAddress");
		ivjConnection_GrobalAddress
		    .setLabel("Connection_GrobalAddress");
	    } catch (Throwable throwable) {
		handleException(throwable);
	    }
	}
	return ivjConnection_GrobalAddress;
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
		getPanel1().add(getApp_Auto_Lobby(),
				getApp_Auto_Lobby().getName());
		getPanel1().add(getApp_Auto_Paintchat(),
				getApp_Auto_Paintchat().getName());
		getPanel1().add(getApp_Auto_Http(),
				getApp_Auto_Http().getName());
		getPanel1().add(getConnection_GrobalAddress(),
				getConnection_GrobalAddress().getName());
		getPanel1().add(getApp_ShowStartHelp(),
				getApp_ShowStartHelp().getName());
		getPanel1().add(getAdmin_ChatMaster(),
				getAdmin_ChatMaster().getName());
		getPanel1().add(getApp_Get_Index(),
				getApp_Get_Index().getName());
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
		getPanel2().add(getAdmin_Password(),
				getAdmin_Password().getName());
		getPanel2().add(getApp_Cgi(), getApp_Cgi().getName());
		getPanel2().add(getApp_BrowserPath(),
				getApp_BrowserPath().getName());
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
    
    private void handleException(Throwable throwable) {
	/* empty */
    }
    
    public void init() {
	try {
	    this.setName("Ao");
	    this.setLayout(new BorderLayout());
	    this.setBackground(new Color(204, 204, 204));
	    this.setSize(446, 266);
	    this.add(getPanel1(), "North");
	    this.add(getPanel2(), "Center");
	    this.add(getPanel3(), "South");
	    initConnections();
	    Gui.giveDef(this);
	    initValue();
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
	Gui.giveDef(this);
    }
    
    public void m_dispose() {
	try {
	    this.getHelp().reset();
	    ((Window) this.getParent()).dispose();
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	}
    }
    
    public void m_save() {
	try {
	    this.setParameter(this);
	    Config config
		= ((Config)
		   ((ServerStub) this.getAppletContext()).getHashTable());
	    config.saveConfig(null, null);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public static void main(String[] strings) {
	try {
	    Frame frame = new Frame();
	    Class var_class = Class.forName("paintchat.config.PConfig");
	    ClassLoader classloader = var_class.getClassLoader();
	    PConfig pconfig
		= ((PConfig)
		   Beans.instantiate(classloader, "paintchat.config.PConfig"));
	    frame.add("Center", pconfig);
	    frame.setSize(pconfig.getSize());
	    frame.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent windowevent) {
		    System.exit(0);
		}
	    });
	    frame.setVisible(true);
	} catch (Throwable throwable) {
	    System.err.println
		("java.applet.Applet \u306e main() \u3067\u4f8b\u5916\u304c\u767a\u751f\u3057\u307e\u3057\u305f");
	    throwable.printStackTrace(System.out);
	}
    }
    
    public void reset() {
	this.getHelp().reset();
    }
}
