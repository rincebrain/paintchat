package syi.awt;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.EventObject;
import java.util.Hashtable;

// Referenced classes of package syi.awt:
//            TextCanvas, Awt, LButton

public class MessageBox extends Dialog
    implements ActionListener
{

    private static MessageBox message = null;
    public boolean bool;
    private static Hashtable res;
    private Panel panelUnder;
    private Panel panelUpper;
    private Panel panelCenter;
    private LButton b_ok;
    private LButton b_cancel;
    private TextField textField;
    private TextCanvas textCanvas;

    public MessageBox(Frame frame)
    {
        super(frame);
        bool = false;
        textField = new TextField();
        textCanvas = new TextCanvas();
        setForeground(frame.getForeground());
        setBackground(frame.getBackground());
        super.enableEvents(64L);
        setLayout(new BorderLayout());
        setModal(true);
        setResizable(false);
        Awt.getDef(this);
        textCanvas.isBorder = false;
        textField.setColumns(64);
        panelUnder = new Panel();
        panelUnder.setLayout(new FlowLayout());
        String s = "Ok";
        String s1 = "Cancel";
        b_ok = new LButton(s);
        b_cancel = new LButton(s1);
        if(res != null)
        {
            String s2 = (String)res.get(s);
            if(s2 != null)
            {
                b_ok.setText(s2);
            }
            s2 = (String)res.get(s1);
            if(s2 != null)
            {
                b_cancel.setText(s2);
            }
        }
        panelUnder.add(b_ok);
        b_ok.addActionListener(this);
        b_cancel.addActionListener(this);
        textField.addActionListener(this);
        add(textCanvas, "North");
        add(panelUnder, "South");
        Awt.setDef(this, false);
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        Object obj = actionevent.getSource();
        bool = obj == b_ok || obj == textField;
        try
        {
            Object obj1 = (Component)actionevent.getSource();
            for(int i = 0; i < 10; i++)
            {
                if(obj1 == null || obj1 == this)
                {
                    break;
                }
                obj1 = ((Component) (obj1)).getParent();
            }

            ((Window)obj1).dispose();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public static void alert(String s, String s1)
    {
        messageBox(s, s1, 1);
    }

    public static boolean confirm(String s, String s1)
    {
        return messageBox(s, s1, 2);
    }

    public void error(String s)
    {
        String s1 = "TitleOfError";
        String s2 = (String)res.get(s1);
        if(s2 == null)
        {
            s2 = s1;
        }
        alert(s, s2);
    }

    private static String getRes(String s)
    {
        if(res == null)
        {
            return s;
        }
        if(s == null)
        {
            return "";
        } else
        {
            String s1 = (String)res.get(s);
            return s1 != null ? s1 : s;
        }
    }

    public static String getString(String s, String s1)
    {
        if(messageBox(s, s1, 3))
        {
            return message.getText();
        } else
        {
            return s;
        }
    }

    public static String getString(String s, String s1, Point point)
    {
        if(messageBox(s, s1, point, 3))
        {
            return message.getText();
        } else
        {
            return s;
        }
    }

    public String getText()
    {
        return textField.getText();
    }

    public static synchronized boolean messageBox(String s, String s1, int i)
    {
        return messageBox(s, s1, null, i);
    }

    public static synchronized boolean messageBox(String s, String s1, Point point, int i)
    {
        boolean flag = false;
        try
        {
            MessageBox messagebox = message;
            if(messagebox == null)
            {
                message = new MessageBox(Awt.getPFrame());
                messagebox = message;
            } else
            if(messagebox.isShowing())
            {
                messagebox.dispose();
            }
            Awt.getDef(messagebox);
            messagebox.resetMessage();
            messagebox.setOkCancel(i >= 2);
            if(i == 3)
            {
                messagebox.textField.setText(s);
                messagebox.textCanvas.setText(getRes("EorBInput"));
            } else
            {
                messagebox.setText(getRes(s));
            }
            messagebox.setTextField(i == 3);
            messagebox.setTitle(getRes(s1));
            messagebox.pack();
            if(point != null)
            {
                messagebox.setLocation(point);
            } else
            {
                Awt.moveCenter(messagebox);
            }
            messagebox.setVisible(true);
            flag = messagebox.bool;
        }
        catch(Throwable throwable)
        {
            System.out.println("message" + throwable);
        }
        return flag;
    }

    protected void processWindowEvent(WindowEvent windowevent)
    {
        try
        {
            int i = windowevent.getID();
            Dialog _tmp = (Dialog)windowevent.getWindow();
            if(i == 201)
            {
                dispose();
            }
        }
        catch(Throwable _ex) { }
    }

    private void resetMessage()
    {
        if(textField != null)
        {
            textField.setText("");
        }
        bool = false;
    }

    private void setOkCancel(boolean flag)
    {
        if(flag)
        {
            panelUnder.add(b_cancel);
        } else
        {
            panelUnder.remove(b_cancel);
        }
    }

    public static void setResource(Hashtable hashtable)
    {
        res = hashtable;
    }

    public void setText(String s)
        throws IOException
    {
        textCanvas.setText(s);
    }

    private void setTextField(boolean flag)
    {
        if(flag)
        {
            add(textField, "Center");
            textField.requestFocus();
            textField.selectAll();
        } else
        {
            remove(textField);
        }
    }

}
