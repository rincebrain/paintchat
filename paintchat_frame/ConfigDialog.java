package paintchat_frame;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.WindowEvent;
import paintchat.Config;
import paintchat.Res;
import syi.applet.ServerStub;
import syi.awt.Awt;

public class ConfigDialog extends Dialog
{
  private Applet applet;

  public ConfigDialog(String paramString1, String paramString2, Config paramConfig, Res paramRes, String paramString3)
    throws Exception
  {
    super(Awt.getPFrame());
    setModal(true);
    this.applet = ((Applet)Class.forName(paramString1).newInstance());
    this.applet.setStub(ServerStub.getDefaultStub(paramConfig, paramRes));
    enableEvents(64L);
    setLayout(new BorderLayout());
    add(this.applet, "Center");
    this.applet.init();
    pack();
    Awt.moveCenter(this);
    setVisible(true);
    this.applet.start();
  }

  protected void processWindowEvent(WindowEvent paramWindowEvent)
  {
    Window localWindow;
    switch (paramWindowEvent.getID())
    {
    case 201:
      this.applet.stop();
      localWindow = paramWindowEvent.getWindow();
      localWindow.dispose();
      localWindow.removeAll();
      break;
    case 202:
      this.applet.destroy();
    case 200:
    }
    if (paramWindowEvent.getID() == 201)
    {
      localWindow = paramWindowEvent.getWindow();
      localWindow.dispose();
      localWindow.removeAll();
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_frame.ConfigDialog
 * JD-Core Version:    0.6.0
 */