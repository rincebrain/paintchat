/* Me - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_client;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import paintchat.Res;

import syi.awt.Awt;

public class Me extends Dialog implements ActionListener
{
    private static boolean isD = false;
    public static Res res;
    public static Res conf;
    private Button bOk;
    private Button bNo;
    private TextField tText;
    private Panel pBotton;
    private Panel pText;
    public boolean isOk;
    
    public Me() {
	super(Awt.getPFrame());
	this.enableEvents(64L);
	this.setModal(true);
	this.setLayout(new BorderLayout(5, 5));
	String string = "yes";
	String string_0_ = "no";
	pText = new Panel(new GridLayout(0, 1));
	this.add(pText, "North");
	bOk = new Button(p(string));
	bOk.addActionListener(this);
	bNo = new Button(p(string_0_));
	bNo.addActionListener(this);
	pBotton = new Panel(new FlowLayout(1, 10, 4));
	pBotton.add(bOk);
	this.add(pBotton, "South");
    }
    
    public void actionPerformed(ActionEvent actionevent) {
	isOk = (actionevent.getSource() == bOk
		|| actionevent.getSource() instanceof TextField);
	this.dispose();
    }
    
    public void init(String string, boolean bool) {
	string = p(string);
	setConfirm(bool);
	int i = 0;
	while (i < string.length()) {
	    String string_1_ = r(string, i);
	    i++;
	    if (string_1_ != null) {
		ad(string_1_);
		i += string_1_.length();
	    }
	}
	Awt.getDef(this);
	this.setBackground(new Color(conf.getP("dlg_color_bk",
					       Awt.cBk.getRGB())));
	this.setForeground(new Color(conf.getP("dlg_color_text",
					       Awt.cFore.getRGB())));
	Awt.setDef(this, false);
	this.pack();
	Awt.moveCenter(this);
    }
    
    public Label ad(String string) {
	Label label = new Label(string);
	pText.add(label);
	return label;
    }
    
    public static void alert(String string) {
	confirm(string, false);
    }
    
    public static boolean confirm(String string, boolean bool) {
	isD = true;
	Me me = getMe();
	try {
	    me.init(string, bool);
	    me.setVisible(true);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
	isD = false;
	return me.isOk;
    }
    
    public static String getString(String string, String string_2_) {
	isD = true;
	Me me = getMe();
	try {
	    me.init(string, true);
	    if (string_2_ == null)
		string_2_ = "";
	    if (me.tText == null)
		me.tText = new TextField(string_2_);
	    else
		me.tText.setText(string_2_);
	    me.add(me.tText, "Center");
	    me.pack();
	    me.setVisible(true);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
	isD = false;
	return me.isOk ? me.tText.getText() : null;
    }
    
    public static Me getMe() {
	Me me = new Me();
	return me;
    }
    
    public static boolean isDialog() {
	return isD;
    }
    
    protected void processWindowEvent(WindowEvent windowevent) {
	if (windowevent.getID() == 201)
	    windowevent.getWindow().dispose();
    }
    
    private static String r(String string, int i) {
	int i_3_;
	for (i_3_ = i; i_3_ < string.length(); i_3_++) {
	    char c = string.charAt(i_3_);
	    if (c == '\r' || c == '\n')
		break;
	}
	return i == i_3_ ? null : string.substring(i, i_3_);
    }
    
    private static String p(String string) {
	if (res == null)
	    return string;
	return res.res(string);
    }
    
    private void setConfirm(boolean bool) {
	int i = pBotton.getComponentCount();
	if (bool) {
	    if (i <= 1)
		pBotton.add(bNo);
	} else if (i >= 2)
	    pBotton.remove(bNo);
    }
}
