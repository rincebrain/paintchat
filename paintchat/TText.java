package paintchat;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import syi.awt.Awt;

// Referenced classes of package paintchat:
//            SW, ToolBox, M, Res

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

    public void actionPerformed(ActionEvent actionevent)
    {
        try
        {
            ts.lift();
            mg.iPen = Integer.parseInt(cFill.getText());
            mg.iSize = Integer.parseInt(cSize.getText());
            mg.iHint = cV.getState() ? 12 : 8;
            mg.strHint = (cName.getSelectedItem() + '-' + (cBL.getState() ? "BOLD" : "") + (cIT.getState() ? "ITALIC" : "") + '-').getBytes("UTF8");
            mg.iCount = Integer.parseInt(cSpace.getText());
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public void lift()
    {
    }

    public void mPack()
    {
    }

    public void mSetup(ToolBox toolbox, M.Info info, M.User user, M m, Res res, Res res1)
    {
        ts = toolbox;
        mg = m;
        setTitle(res.res("Font"));
        String as[] = (String[])null;
        try
        {
            Class class1 = Class.forName("java.awt.GraphicsEnvironment");
            Object obj = class1.getMethod("getLocalGraphicsEnvironment", null).invoke(null, null);
            as = (String[])class1.getMethod("getAvailableFontFamilyNames", null).invoke(obj, null);
        }
        catch(Throwable _ex) { }
        Choice choice = new Choice();
        cName = choice;
        if(as != null)
        {
            for(int i = 0; i < as.length; i++)
            {
                choice.addItem(as[i]);
            }

        }
        as = (String[])null;
        int j = 0;
        setLayout(new GridLayout(0, 1));
        TextField textfield = new TextField("20");
        cSize = textfield;
        cSpace = new TextField("-5");
        add(new Label(res.res("Font"), j));
        add(choice);
        add(new Label(res.res("Size"), j));
        add(textfield);
        add(new Label(res.res("WSpace"), j));
        add(cSpace);
        Panel panel = new Panel();
        panel.add(cBL = new Checkbox(res.res("Bold")));
        panel.add(cIT = new Checkbox(res.res("Italic")));
        add(panel);
        panel = new Panel();
        panel.add(cV = new Checkbox(res.res("VText")));
        panel.add(cFill = new TextField("1"));
        add(panel);
        panel = new Panel();
        Button button = new Button(res.res("Apply"));
        button.addActionListener(this);
        panel.add(button);
        add(panel, "Center");
        cFill.addActionListener(this);
        textfield.addActionListener(this);
        cSpace.addActionListener(this);
        choice.addItemListener(this);
        cBL.addItemListener(this);
        cIT.addItemListener(this);
        cV.addItemListener(this);
        pack();
        enableEvents(64L);
        setVisible(true);
    }

    protected void processWindowEvent(WindowEvent windowevent)
    {
        if(windowevent.getID() == 201)
        {
            setVisible(false);
        }
    }

    public void up()
    {
    }

    public void itemStateChanged(ItemEvent itemevent)
    {
        actionPerformed(null);
    }
}
