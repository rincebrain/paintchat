package paintchat_client;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import paintchat.Res;
import syi.awt.Awt;

public class DCF extends Dialog
  implements ItemListener, ActionListener
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

  public DCF(Res paramRes)
  {
    super(Awt.getPFrame(), paramRes.res("handle"), true);
    setLayout(new BorderLayout());
    this.res = paramRes;
  }

  public void itemStateChanged(ItemEvent paramItemEvent)
  {
    if (paramItemEvent.getStateChange() == 1)
    {
      this.pText.add(this.lPas);
      this.pText.add(this.tPas);
    }
    else
    {
      this.pText.remove(this.lPas);
      this.pText.remove(this.tPas);
    }
    pack();
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    String str = this.tName.getText().trim();
    if (str.length() > 0)
    {
      up();
      dispose();
    }
    else
    {
      this.tName.setText("");
    }
  }

  public void mShow()
  {
    this.tName.addActionListener(this);
    this.tPas.addActionListener(this);
    Panel localPanel = new Panel();
    Button localButton = new Button(this.res.res("enter"));
    localButton.addActionListener(this);
    localPanel.add(localButton);
    add("South", localPanel);
    this.pText.add(new Label(getTitle()));
    this.pText.add(this.tName);
    this.cbAdmin.setLabel(this.res.res("admin"));
    this.cbAdmin.addItemListener(this);
    this.pText.add(this.cbAdmin);
    add("Center", this.pText);
    this.lPas.setText(this.res.res("password"));
    enableEvents(64L);
    Awt.getDef(this);
    Awt.setDef(this, false);
    pack();
    Awt.moveCenter(this);
    this.tName.requestFocus();
    up();
    setVisible(true);
  }

  public void mReset()
  {
    this.tPas.setText("");
    this.tName.setText("");
    this.cbAdmin.setState(false);
    up();
  }

  protected void processWindowEvent(WindowEvent paramWindowEvent)
  {
    switch (paramWindowEvent.getID())
    {
    case 201:
      mReset();
      dispose();
    }
  }

  public String mGetHandle()
  {
    return this.strName;
  }

  public String mGetPass()
  {
    return this.strPas;
  }

  private void up()
  {
    this.strName = this.tName.getText();
    if (this.strName.length() > 10)
      this.strName = this.strName.substring(0, 10);
    this.isAdmin = this.cbAdmin.getState();
    this.strPas = (!this.isAdmin ? "" : this.tPas.getText());
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_client.DCF
 * JD-Core Version:    0.6.0
 */