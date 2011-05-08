package paintchat_frame;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.WindowEvent;
import paintchat.Config;
import paintchat.Res;
import syi.applet.ServerStub;
import syi.awt.Awt;

public class ConfigDialog extends Dialog
{

    private Applet applet;

    public ConfigDialog(String s, String s1, Config config, Res res, String s2)
        throws Exception
    {
        super(Awt.getPFrame());
        setModal(true);
        applet = (Applet)Class.forName(s).newInstance();
        applet.setStub(ServerStub.getDefaultStub(config, res));
        enableEvents(64L);
        setLayout(new BorderLayout());
        add(applet, "Center");
        applet.init();
        pack();
        Awt.moveCenter(this);
        setVisible(true);
        applet.start();
    }

    protected void processWindowEvent(WindowEvent windowevent)
    {
        switch(windowevent.getID())
        {
        case 201: 
            applet.stop();
            Window window = windowevent.getWindow();
            window.dispose();
            window.removeAll();
            break;

        case 202: 
            applet.destroy();
            break;
        }
        if(windowevent.getID() == 201)
        {
            Window window1 = windowevent.getWindow();
            window1.dispose();
            window1.removeAll();
        }
    }
}
