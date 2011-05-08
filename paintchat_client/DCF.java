package paintchat_client;

import java.awt.*;
import java.awt.event.*;
import paintchat.Res;
import syi.awt.Awt;

public class DCF extends Dialog
    implements ItemListener, ActionListener
{

    private Res res;
    private Checkbox cbAdmin;
    private TextField tPas;
    private Label lPas;
    private TextField tName;
    private Panel pText;
    private String strName;
    private String strPas;
    boolean isAdmin;

    public DCF(Res res1)
    {
        super(Awt.getPFrame(), res1.res("handle"), true);
        cbAdmin = new Checkbox();
        tPas = new TextField(10);
        lPas = new Label();
        tName = new TextField(10);
        pText = new Panel(new GridLayout(0, 1));
        strName = "";
        strPas = "";
        setLayout(new BorderLayout());
        res = res1;
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
        if(itemevent.getStateChange() == 1)
        {
            pText.add(lPas);
            pText.add(tPas);
        } else
        {
            pText.remove(lPas);
            pText.remove(tPas);
        }
        pack();
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        String s = tName.getText().trim();
        if(s.length() > 0)
        {
            up();
            dispose();
        } else
        {
            tName.setText("");
        }
    }

    public void mShow()
    {
        tName.addActionListener(this);
        tPas.addActionListener(this);
        Panel panel = new Panel();
        Button button = new Button(res.res("enter"));
        button.addActionListener(this);
        panel.add(button);
        add("South", panel);
        pText.add(new Label(getTitle()));
        pText.add(tName);
        cbAdmin.setLabel(res.res("admin"));
        cbAdmin.addItemListener(this);
        pText.add(cbAdmin);
        add("Center", pText);
        lPas.setText(res.res("password"));
        enableEvents(64L);
        Awt.getDef(this);
        Awt.setDef(this, false);
        pack();
        Awt.moveCenter(this);
        tName.requestFocus();
        up();
        setVisible(true);
    }

    public void mReset()
    {
        tPas.setText("");
        tName.setText("");
        cbAdmin.setState(false);
        up();
    }

    protected void processWindowEvent(WindowEvent windowevent)
    {
        switch(windowevent.getID())
        {
        case 201: 
            mReset();
            dispose();
            break;
        }
    }

    public String mGetHandle()
    {
        return strName;
    }

    public String mGetPass()
    {
        return strPas;
    }

    private void up()
    {
        strName = tName.getText();
        if(strName.length() > 10)
        {
            strName = strName.substring(0, 10);
        }
        isAdmin = cbAdmin.getState();
        strPas = isAdmin ? tPas.getText() : "";
    }
}
