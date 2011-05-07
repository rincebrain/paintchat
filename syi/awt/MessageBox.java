package syi.awt;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.util.EventObject;
import java.util.Hashtable;

public class MessageBox extends Dialog
  implements ActionListener
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

  public MessageBox(Frame paramFrame)
  {
    super(paramFrame);
    setForeground(paramFrame.getForeground());
    setBackground(paramFrame.getBackground());
    super.enableEvents(64L);
    setLayout(new BorderLayout());
    setModal(true);
    setResizable(false);
    Awt.getDef(this);
    this.textCanvas.isBorder = false;
    this.textField.setColumns(64);
    this.panelUnder = new Panel();
    this.panelUnder.setLayout(new FlowLayout());
    String str1 = "Ok";
    String str2 = "Cancel";
    this.b_ok = new LButton(str1);
    this.b_cancel = new LButton(str2);
    if (res != null)
    {
      String str3 = (String)res.get(str1);
      if (str3 != null)
        this.b_ok.setText(str3);
      str3 = (String)res.get(str2);
      if (str3 != null)
        this.b_cancel.setText(str3);
    }
    this.panelUnder.add(this.b_ok);
    this.b_ok.addActionListener(this);
    this.b_cancel.addActionListener(this);
    this.textField.addActionListener(this);
    add(this.textCanvas, "North");
    add(this.panelUnder, "South");
    Awt.setDef(this, false);
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    Object localObject1 = paramActionEvent.getSource();
    this.bool = ((localObject1 == this.b_ok) || (localObject1 == this.textField));
    try
    {
      Object localObject2 = (Component)paramActionEvent.getSource();
      for (int i = 0; i < 10; i++)
      {
        if ((localObject2 == null) || (localObject2 == this))
          break;
        localObject2 = ((Component)localObject2).getParent();
      }
      ((Window)localObject2).dispose();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public static void alert(String paramString1, String paramString2)
  {
    messageBox(paramString1, paramString2, 1);
  }

  public static boolean confirm(String paramString1, String paramString2)
  {
    return messageBox(paramString1, paramString2, 2);
  }

  public void error(String paramString)
  {
    String str1 = "TitleOfError";
    String str2 = (String)res.get(str1);
    if (str2 == null)
      str2 = str1;
    alert(paramString, str2);
  }

  private static String getRes(String paramString)
  {
    if (res == null)
      return paramString;
    if (paramString == null)
      return "";
    String str = (String)res.get(paramString);
    return str == null ? paramString : str;
  }

  public static String getString(String paramString1, String paramString2)
  {
    if (messageBox(paramString1, paramString2, 3))
      return message.getText();
    return paramString1;
  }

  public static String getString(String paramString1, String paramString2, Point paramPoint)
  {
    if (messageBox(paramString1, paramString2, paramPoint, 3))
      return message.getText();
    return paramString1;
  }

  public String getText()
  {
    return this.textField.getText();
  }

  public static synchronized boolean messageBox(String paramString1, String paramString2, int paramInt)
  {
    return messageBox(paramString1, paramString2, null, paramInt);
  }

  public static synchronized boolean messageBox(String paramString1, String paramString2, Point paramPoint, int paramInt)
  {
    boolean bool1 = false;
    try
    {
      MessageBox localMessageBox = message;
      if (localMessageBox == null)
      {
        message = new MessageBox(Awt.getPFrame());
        localMessageBox = message;
      }
      else if (localMessageBox.isShowing())
      {
        localMessageBox.dispose();
      }
      Awt.getDef(localMessageBox);
      localMessageBox.resetMessage();
      localMessageBox.setOkCancel(paramInt >= 2);
      if (paramInt == 3)
      {
        localMessageBox.textField.setText(paramString1);
        localMessageBox.textCanvas.setText(getRes("EorBInput"));
      }
      else
      {
        localMessageBox.setText(getRes(paramString1));
      }
      localMessageBox.setTextField(paramInt == 3);
      localMessageBox.setTitle(getRes(paramString2));
      localMessageBox.pack();
      if (paramPoint != null)
        localMessageBox.setLocation(paramPoint);
      else
        Awt.moveCenter(localMessageBox);
      localMessageBox.setVisible(true);
      bool1 = localMessageBox.bool;
    }
    catch (Throwable localThrowable)
    {
      System.out.println("message" + localThrowable);
    }
    return bool1;
  }

  protected void processWindowEvent(WindowEvent paramWindowEvent)
  {
    try
    {
      int i = paramWindowEvent.getID();
      ((Dialog)paramWindowEvent.getWindow());
      if (i == 201)
        dispose();
    }
    catch (Throwable localThrowable)
    {
    }
  }

  private void resetMessage()
  {
    if (this.textField != null)
      this.textField.setText("");
    this.bool = false;
  }

  private void setOkCancel(boolean paramBoolean)
  {
    if (paramBoolean)
      this.panelUnder.add(this.b_cancel);
    else
      this.panelUnder.remove(this.b_cancel);
  }

  public static void setResource(Hashtable paramHashtable)
  {
    res = paramHashtable;
  }

  public void setText(String paramString)
    throws IOException
  {
    this.textCanvas.setText(paramString);
  }

  private void setTextField(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      add(this.textField, "Center");
      this.textField.requestFocus();
      this.textField.selectAll();
    }
    else
    {
      remove(this.textField);
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.MessageBox
 * JD-Core Version:    0.6.0
 */