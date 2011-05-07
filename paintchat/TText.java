/* TText - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
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

import syi.awt.Awt;

public class TText extends Dialog implements SW, ActionListener, ItemListener
{
    ToolBox ts;
    M mg;
    private Choice cName;
    private Checkbox cIT;
    private Checkbox cBL;
    private Checkbox cV;
    private TextField cSize;
    private TextField cSpace;
    private TextField cFill;
    
    public TText() {
	super(Awt.getPFrame());
    }
    
    public void actionPerformed(ActionEvent actionevent) {
	try {
	    ts.lift();
	    mg.iPen = Integer.parseInt(cFill.getText());
	    mg.iSize = Integer.parseInt(cSize.getText());
	    mg.iHint = cV.getState() ? 12 : 8;
	    mg.strHint = (cName.getSelectedItem() + '-'
			  + (cBL.getState() ? "BOLD" : "")
			  + (cIT.getState() ? "ITALIC" : "") + '-')
			     .getBytes("UTF8");
	    mg.iCount = Integer.parseInt(cSpace.getText());
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void lift() {
	/* empty */
    }
    
    public void mPack() {
	/* empty */
    }
    
    public void mSetup(ToolBox toolbox, M.Info info, M.User user, M m, Res res,
		       Res res_0_) {
	ts = toolbox;
	mg = m;
	this.setTitle(res.res("Font"));
	String[] strings = null;
	try {
	    Class var_class = Class.forName("java.awt.GraphicsEnvironment");
	    Object object
		= var_class.getMethod("getLocalGraphicsEnvironment", null)
		      .invoke(null, null);
	    strings = (String[]) var_class.getMethod
				     ("getAvailableFontFamilyNames", null)
				     .invoke(object, null);
	} catch (Throwable throwable) {
	    /* empty */
	}
	Choice choice = new Choice();
	cName = choice;
	if (strings != null) {
	    for (int i = 0; i < strings.length; i++)
		choice.addItem(strings[i]);
	}
	strings = null;
	int i = 0;
	this.setLayout(new GridLayout(0, 1));
	TextField textfield = new TextField("20");
	cSize = textfield;
	cSpace = new TextField("-5");
	this.add(new Label(res.res("Font"), i));
	this.add(choice);
	this.add(new Label(res.res("Size"), i));
	this.add(textfield);
	this.add(new Label(res.res("WSpace"), i));
	this.add(cSpace);
	Panel panel = new Panel();
	panel.add(cBL = new Checkbox(res.res("Bold")));
	panel.add(cIT = new Checkbox(res.res("Italic")));
	this.add(panel);
	panel = new Panel();
	panel.add(cV = new Checkbox(res.res("VText")));
	panel.add(cFill = new TextField("1"));
	this.add(panel);
	panel = new Panel();
	Button button = new Button(res.res("Apply"));
	button.addActionListener(this);
	panel.add(button);
	this.add(panel, "Center");
	cFill.addActionListener(this);
	textfield.addActionListener(this);
	cSpace.addActionListener(this);
	choice.addItemListener(this);
	cBL.addItemListener(this);
	cIT.addItemListener(this);
	cV.addItemListener(this);
	this.pack();
	this.enableEvents(64L);
	this.setVisible(true);
    }
    
    protected void processWindowEvent(WindowEvent windowevent) {
	if (windowevent.getID() == 201)
	    this.setVisible(false);
    }
    
    public void up() {
	/* empty */
    }
    
    public void itemStateChanged(ItemEvent itemevent) {
	actionPerformed(null);
    }
}
