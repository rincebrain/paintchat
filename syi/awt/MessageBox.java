/* MessageBox - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Hashtable;

public class MessageBox extends Dialog implements ActionListener
{
    private static MessageBox message = null;
    public boolean bool = false;
    private static Hashtable res;
    private Panel panelUnder;
    private Panel panelUpper;
    private Panel panelCenter;
    private LButton b_ok;
    private LButton b_cancel;
    private TextField textField = new TextField();
    private TextCanvas textCanvas = new TextCanvas();
    
    public MessageBox(Frame frame) {
	super(frame);
	this.setForeground(frame.getForeground());
	this.setBackground(frame.getBackground());
	super.enableEvents(64L);
	this.setLayout(new BorderLayout());
	this.setModal(true);
	this.setResizable(false);
	Awt.getDef(this);
	textCanvas.isBorder = false;
	textField.setColumns(64);
	panelUnder = new Panel();
	panelUnder.setLayout(new FlowLayout());
	String string = "Ok";
	String string_0_ = "Cancel";
	b_ok = new LButton(string);
	b_cancel = new LButton(string_0_);
	if (res != null) {
	    String string_1_ = (String) res.get(string);
	    if (string_1_ != null)
		b_ok.setText(string_1_);
	    string_1_ = (String) res.get(string_0_);
	    if (string_1_ != null)
		b_cancel.setText(string_1_);
	}
	panelUnder.add(b_ok);
	b_ok.addActionListener(this);
	b_cancel.addActionListener(this);
	textField.addActionListener(this);
	this.add(textCanvas, "North");
	this.add(panelUnder, "South");
	Awt.setDef(this, false);
    }
    
    public void actionPerformed(ActionEvent actionevent) {
	Object object = actionevent.getSource();
	bool = object == b_ok || object == textField;
	try {
	    Component component = (Component) actionevent.getSource();
	    for (int i = 0; i < 10; i++) {
		if (component == null || component == this)
		    break;
		component = component.getParent();
	    }
	    ((Window) component).dispose();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public static void alert(String string, String string_2_) {
	messageBox(string, string_2_, 1);
    }
    
    public static boolean confirm(String string, String string_3_) {
	return messageBox(string, string_3_, 2);
    }
    
    public void error(String string) {
	String string_4_ = "TitleOfError";
	String string_5_ = (String) res.get(string_4_);
	if (string_5_ == null)
	    string_5_ = string_4_;
	alert(string, string_5_);
    }
    
    private static String getRes(String string) {
	if (res == null)
	    return string;
	if (string == null)
	    return "";
	String string_6_ = (String) res.get(string);
	return string_6_ == null ? string : string_6_;
    }
    
    public static String getString(String string, String string_7_) {
	if (messageBox(string, string_7_, 3))
	    return message.getText();
	return string;
    }
    
    public static String getString(String string, String string_8_,
				   Point point) {
	if (messageBox(string, string_8_, point, 3))
	    return message.getText();
	return string;
    }
    
    public String getText() {
	return textField.getText();
    }
    
    public static synchronized boolean messageBox(String string,
						  String string_9_, int i) {
	return messageBox(string, string_9_, null, i);
    }
    
    public static synchronized boolean messageBox
	(String string, String string_10_, Point point, int i) {
	boolean bool = false;
	try {
	    MessageBox messagebox = message;
	    if (messagebox == null) {
		message = new MessageBox(Awt.getPFrame());
		messagebox = message;
	    } else if (messagebox.isShowing())
		messagebox.dispose();
	    Awt.getDef(messagebox);
	    messagebox.resetMessage();
	    messagebox.setOkCancel(i >= 2);
	    if (i == 3) {
		messagebox.textField.setText(string);
		messagebox.textCanvas.setText(getRes("EorBInput"));
	    } else
		messagebox.setText(getRes(string));
	    messagebox.setTextField(i == 3);
	    messagebox.setTitle(getRes(string_10_));
	    messagebox.pack();
	    if (point != null)
		messagebox.setLocation(point);
	    else
		Awt.moveCenter(messagebox);
	    messagebox.setVisible(true);
	    bool = messagebox.bool;
	} catch (Throwable throwable) {
	    System.out.println("message" + throwable);
	}
	return bool;
    }
    
    protected void processWindowEvent(WindowEvent windowevent) {
	try {
	    int i = windowevent.getID();
	    if ((Dialog) windowevent.getWindow() != null) {
		/* empty */
	    }
	    if (i == 201)
		this.dispose();
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    private void resetMessage() {
	if (textField != null)
	    textField.setText("");
	bool = false;
    }
    
    private void setOkCancel(boolean bool) {
	if (bool)
	    panelUnder.add(b_cancel);
	else
	    panelUnder.remove(b_cancel);
    }
    
    public static void setResource(Hashtable hashtable) {
	res = hashtable;
    }
    
    public void setText(String string) throws IOException {
	textCanvas.setText(string);
    }
    
    private void setTextField(boolean bool) {
	if (bool) {
	    this.add(textField, "Center");
	    textField.requestFocus();
	    textField.selectAll();
	} else
	    this.remove(textField);
    }
}
