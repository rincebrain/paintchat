/* DCF - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_client;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;

import paintchat.Res;

import syi.awt.Awt;

public class DCF extends Dialog implements ItemListener, ActionListener
{
    private Res res;
    private Checkbox cbAdmin = new Checkbox();
    private TextField tPas = new TextField(10);
    private Label lPas = new Label();
    private TextField tName = new TextField(10);
    private Panel pText = new Panel(new GridLayout(0, 1));
    private String strName = "";
    private String strPas = "";
    boolean isAdmin;
    
    public DCF(Res res) {
	super(Awt.getPFrame(), res.res("handle"), true);
	this.setLayout(new BorderLayout());
	this.res = res;
    }
    
    public void itemStateChanged(ItemEvent itemevent) {
	if (itemevent.getStateChange() == 1) {
	    pText.add(lPas);
	    pText.add(tPas);
	} else {
	    pText.remove(lPas);
	    pText.remove(tPas);
	}
	this.pack();
    }
    
    public void actionPerformed(ActionEvent actionevent) {
	String string = tName.getText().trim();
	if (string.length() > 0) {
	    up();
	    this.dispose();
	} else
	    tName.setText("");
    }
    
    public void mShow() {
	tName.addActionListener(this);
	tPas.addActionListener(this);
	Panel panel = new Panel();
	Button button = new Button(res.res("enter"));
	button.addActionListener(this);
	panel.add(button);
	this.add("South", panel);
	pText.add(new Label(this.getTitle()));
	pText.add(tName);
	cbAdmin.setLabel(res.res("admin"));
	cbAdmin.addItemListener(this);
	pText.add(cbAdmin);
	this.add("Center", pText);
	lPas.setText(res.res("password"));
	this.enableEvents(64L);
	Awt.getDef(this);
	Awt.setDef(this, false);
	this.pack();
	Awt.moveCenter(this);
	tName.requestFocus();
	up();
	this.setVisible(true);
    }
    
    public void mReset() {
	tPas.setText("");
	tName.setText("");
	cbAdmin.setState(false);
	up();
    }
    
    protected void processWindowEvent(WindowEvent windowevent) {
	switch (windowevent.getID()) {
	case 201:
	    mReset();
	    this.dispose();
	}
    }
    
    public String mGetHandle() {
	return strName;
    }
    
    public String mGetPass() {
	return strPas;
    }
    
    private void up() {
	strName = tName.getText();
	if (strName.length() > 10)
	    strName = strName.substring(0, 10);
	isAdmin = cbAdmin.getState();
	strPas = !isAdmin ? "" : tPas.getText();
    }
}
