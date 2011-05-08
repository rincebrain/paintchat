package paintchat_client;

import java.awt.*;
import java.awt.event.*;
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
        String s = "yes";
        String s1 = "no";
        pText = new Panel(new GridLayout(0, 1));
        add(pText, "North");
        bOk = new Button(p(s));
        bOk.addActionListener(this);
        bNo = new Button(p(s1));
        bNo.addActionListener(this);
        pBotton = new Panel(new FlowLayout(1, 10, 4));
        pBotton.add(bOk);
        add(pBotton, "South");
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        isOk = actionevent.getSource() == bOk || (actionevent.getSource() instanceof TextField);
        dispose();
    }

    public void init(String s, boolean flag)
    {
        s = p(s);
        setConfirm(flag);
        for(int i = 0; i < s.length();)
        {
            String s1 = r(s, i);
            i++;
            if(s1 != null)
            {
                ad(s1);
                i += s1.length();
            }
        }

        Awt.getDef(this);
        setBackground(new Color(conf.getP("dlg_color_bk", Awt.cBk.getRGB())));
        setForeground(new Color(conf.getP("dlg_color_text", Awt.cFore.getRGB())));
        Awt.setDef(this, false);
        pack();
        Awt.moveCenter(this);
    }

    public Label ad(String s)
    {
        Label label = new Label(s);
        pText.add(label);
        return label;
    }

    public static void alert(String s)
    {
        confirm(s, false);
    }

    public static boolean confirm(String s, boolean flag)
    {
        isD = true;
        Me me = getMe();
        try
        {
            me.init(s, flag);
            me.setVisible(true);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        isD = false;
        return me.isOk;
    }

    public static String getString(String s, String s1)
    {
        isD = true;
        Me me = getMe();
        try
        {
            me.init(s, true);
            if(s1 == null)
            {
                s1 = "";
            }
            if(me.tText == null)
            {
                me.tText = new TextField(s1);
            } else
            {
                me.tText.setText(s1);
            }
            me.add(me.tText, "Center");
            me.pack();
            me.setVisible(true);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        isD = false;
        return me.isOk ? me.tText.getText() : null;
    }

    public static Me getMe()
    {
        Me me = new Me();
        return me;
    }

    public static boolean isDialog()
    {
        return isD;
    }

    protected void processWindowEvent(WindowEvent windowevent)
    {
        if(windowevent.getID() == 201)
        {
            windowevent.getWindow().dispose();
        }
    }

    private static String r(String s, int i)
    {
        int j;
        for(j = i; j < s.length(); j++)
        {
            char c = s.charAt(j);
            if(c == '\r' || c == '\n')
            {
                break;
            }
        }

        return i != j ? s.substring(i, j) : null;
    }

    private static String p(String s)
    {
        if(res == null)
        {
            return s;
        } else
        {
            return res.res(s);
        }
    }

    private void setConfirm(boolean flag)
    {
        int i = pBotton.getComponentCount();
        if(flag)
        {
            if(i <= 1)
            {
                pBotton.add(bNo);
            }
        } else
        if(i >= 2)
        {
            pBotton.remove(bNo);
        }
    }

}
