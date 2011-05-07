package paintchat;

import java.awt.AWTEvent;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
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
import java.lang.reflect.Method;
import syi.awt.Awt;

public class TText extends Dialog
  implements SW, ActionListener, ItemListener
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

  public TText()
  {
    super(Awt.getPFrame());
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    try
    {
      this.ts.lift();
      this.mg.iPen = Integer.parseInt(this.cFill.getText());
      this.mg.iSize = Integer.parseInt(this.cSize.getText());
      this.mg.iHint = (this.cV.getState() ? 12 : 8);
      this.mg.strHint = (this.cName.getSelectedItem() + '-' + (this.cBL.getState() ? "BOLD" : "") + (this.cIT.getState() ? "ITALIC" : "") + '-').getBytes("UTF8");
      this.mg.iCount = Integer.parseInt(this.cSpace.getText());
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void lift()
  {
  }

  public void mPack()
  {
  }

  public void mSetup(ToolBox paramToolBox, M.Info paramInfo, M.User paramUser, M paramM, Res paramRes1, Res paramRes2)
  {
    this.ts = paramToolBox;
    this.mg = paramM;
    setTitle(paramRes1.res("Font"));
    String[] arrayOfString = (String[])null;
    try
    {
      localObject1 = Class.forName("java.awt.GraphicsEnvironment");
      Object localObject2 = ((Class)localObject1).getMethod("getLocalGraphicsEnvironment", null).invoke(null, null);
      arrayOfString = (String[])((Class)localObject1).getMethod("getAvailableFontFamilyNames", null).invoke(localObject2, null);
    }
    catch (Throwable localThrowable)
    {
    }
    Object localObject1 = new Choice();
    this.cName = ((Choice)localObject1);
    if (arrayOfString != null)
      for (i = 0; i < arrayOfString.length; i++)
        ((Choice)localObject1).addItem(arrayOfString[i]);
    arrayOfString = (String[])null;
    int i = 0;
    setLayout(new GridLayout(0, 1));
    TextField localTextField = new TextField("20");
    this.cSize = localTextField;
    this.cSpace = new TextField("-5");
    add(new Label(paramRes1.res("Font"), i));
    add((Component)localObject1);
    add(new Label(paramRes1.res("Size"), i));
    add(localTextField);
    add(new Label(paramRes1.res("WSpace"), i));
    add(this.cSpace);
    Panel localPanel = new Panel();
    localPanel.add(this.cBL = new Checkbox(paramRes1.res("Bold")));
    localPanel.add(this.cIT = new Checkbox(paramRes1.res("Italic")));
    add(localPanel);
    localPanel = new Panel();
    localPanel.add(this.cV = new Checkbox(paramRes1.res("VText")));
    localPanel.add(this.cFill = new TextField("1"));
    add(localPanel);
    localPanel = new Panel();
    Button localButton = new Button(paramRes1.res("Apply"));
    localButton.addActionListener(this);
    localPanel.add(localButton);
    add(localPanel, "Center");
    this.cFill.addActionListener(this);
    localTextField.addActionListener(this);
    this.cSpace.addActionListener(this);
    ((Choice)localObject1).addItemListener(this);
    this.cBL.addItemListener(this);
    this.cIT.addItemListener(this);
    this.cV.addItemListener(this);
    pack();
    enableEvents(64L);
    setVisible(true);
  }

  protected void processWindowEvent(WindowEvent paramWindowEvent)
  {
    if (paramWindowEvent.getID() == 201)
      setVisible(false);
  }

  public void up()
  {
  }

  public void itemStateChanged(ItemEvent paramItemEvent)
  {
    actionPerformed(null);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.TText
 * JD-Core Version:    0.6.0
 */