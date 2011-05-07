package paintchat_client;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.EventObject;
import paintchat.Res;
import syi.awt.Awt;

public class Me extends Dialog
  implements ActionListener
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

  public Me()
  {
    super(Awt.getPFrame());
    enableEvents(64L);
    setModal(true);
    setLayout(new BorderLayout(5, 5));
    String str1 = "yes";
    String str2 = "no";
    this.pText = new Panel(new GridLayout(0, 1));
    add(this.pText, "North");
    this.bOk = new Button(p(str1));
    this.bOk.addActionListener(this);
    this.bNo = new Button(p(str2));
    this.bNo.addActionListener(this);
    this.pBotton = new Panel(new FlowLayout(1, 10, 4));
    this.pBotton.add(this.bOk);
    add(this.pBotton, "South");
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    this.isOk = ((paramActionEvent.getSource() == this.bOk) || ((paramActionEvent.getSource() instanceof TextField)));
    dispose();
  }

  public void init(String paramString, boolean paramBoolean)
  {
    paramString = p(paramString);
    setConfirm(paramBoolean);
    int i = 0;
    while (i < paramString.length())
    {
      String str = r(paramString, i);
      i++;
      if (str == null)
        continue;
      ad(str);
      i += str.length();
    }
    Awt.getDef(this);
    setBackground(new Color(conf.getP("dlg_color_bk", Awt.cBk.getRGB())));
    setForeground(new Color(conf.getP("dlg_color_text", Awt.cFore.getRGB())));
    Awt.setDef(this, false);
    pack();
    Awt.moveCenter(this);
  }

  public Label ad(String paramString)
  {
    Label localLabel = new Label(paramString);
    this.pText.add(localLabel);
    return localLabel;
  }

  public static void alert(String paramString)
  {
    confirm(paramString, false);
  }

  public static boolean confirm(String paramString, boolean paramBoolean)
  {
    isD = true;
    Me localMe = getMe();
    try
    {
      localMe.init(paramString, paramBoolean);
      localMe.setVisible(true);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    isD = false;
    return localMe.isOk;
  }

  public static String getString(String paramString1, String paramString2)
  {
    isD = true;
    Me localMe = getMe();
    try
    {
      localMe.init(paramString1, true);
      if (paramString2 == null)
        paramString2 = "";
      if (localMe.tText == null)
        localMe.tText = new TextField(paramString2);
      else
        localMe.tText.setText(paramString2);
      localMe.add(localMe.tText, "Center");
      localMe.pack();
      localMe.setVisible(true);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    isD = false;
    return localMe.isOk ? localMe.tText.getText() : null;
  }

  public static Me getMe()
  {
    Me localMe = new Me();
    return localMe;
  }

  public static boolean isDialog()
  {
    return isD;
  }

  protected void processWindowEvent(WindowEvent paramWindowEvent)
  {
    if (paramWindowEvent.getID() == 201)
      paramWindowEvent.getWindow().dispose();
  }

  private static String r(String paramString, int paramInt)
  {
    for (int j = paramInt; j < paramString.length(); j++)
    {
      int i = paramString.charAt(j);
      if ((i == 13) || (i == 10))
        break;
    }
    return paramInt == j ? null : paramString.substring(paramInt, j);
  }

  private static String p(String paramString)
  {
    if (res == null)
      return paramString;
    return res.res(paramString);
  }

  private void setConfirm(boolean paramBoolean)
  {
    int i = this.pBotton.getComponentCount();
    if (paramBoolean)
    {
      if (i <= 1)
        this.pBotton.add(this.bNo);
    }
    else if (i >= 2)
      this.pBotton.remove(this.bNo);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_client.Me
 * JD-Core Version:    0.6.0
 */