package paintchat.config;

import java.applet.Applet;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Point;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import paintchat.Res;
import paintchat.Resource;
import syi.applet.ServerStub;
import syi.awt.HelpWindow;
import syi.awt.HelpWindowContent;
import syi.awt.LButton;
import syi.awt.LTextField;
import syi.util.PProperties;

public class ConfigApplet extends Applet
  implements MouseListener, FocusListener
{
  Res res = Resource.loadResource("Config");
  private HelpWindow helpWindow = null;

  public void focusGained(FocusEvent paramFocusEvent)
  {
  }

  public void focusLost(FocusEvent paramFocusEvent)
  {
    getHelp().reset();
  }

  protected boolean getBool(String paramString)
  {
    if ((paramString == null) || (paramString.length() <= 0))
      return false;
    int i = Character.toLowerCase(paramString.charAt(0));
    switch (i)
    {
    case 49:
    case 111:
    case 116:
    case 121:
      return true;
    }
    return false;
  }

  protected boolean getBool(String paramString, boolean paramBoolean)
  {
    if ((paramString == null) || (paramString.length() <= 0))
      return paramBoolean;
    int i = Character.toLowerCase(paramString.charAt(0));
    switch (i)
    {
    case 49:
    case 111:
    case 116:
    case 121:
      return true;
    case 48:
    case 99:
    case 102:
    case 110:
      return false;
    }
    return paramBoolean;
  }

  protected HelpWindow getHelp()
  {
    if (this.helpWindow == null)
    {
      this.helpWindow = new HelpWindow((Frame)getParent().getParent());
      this.helpWindow.setIsShow(getBool(getParameter("App_IsConsole"), true));
    }
    return this.helpWindow;
  }

  public void getParameter(Component paramComponent)
  {
    Object localObject;
    if ((paramComponent instanceof Container))
    {
      localObject = ((Container)paramComponent).getComponents();
      for (int i = 0; i < localObject.length; i++)
        getParameter(localObject[i]);
    }
    else
    {
      if ((!(paramComponent instanceof LTextField)) && (!(paramComponent instanceof Checkbox)))
        return;
      localObject = getParameter(paramComponent.getName());
      if (localObject == null)
        return;
      if ((paramComponent instanceof LTextField))
        ((LTextField)paramComponent).setText((String)localObject);
      if ((paramComponent instanceof Checkbox))
        ((Checkbox)paramComponent).setState(getBool((String)localObject));
    }
  }

  public void getResource(Res paramRes, Component paramComponent)
  {
    Object localObject;
    if ((paramComponent instanceof Container))
    {
      localObject = (Container)paramComponent;
      int i = ((Container)localObject).getComponentCount();
      for (int j = 0; j < i; j++)
        getResource(paramRes, ((Container)localObject).getComponent(j));
    }
    else
    {
      localObject = paramComponent.getName();
      localObject = paramRes.get((String)localObject, (String)localObject);
      if ((paramComponent instanceof LTextField))
      {
        ((LTextField)paramComponent).setTitle((String)localObject);
        return;
      }
      if ((paramComponent instanceof LButton))
      {
        ((LButton)paramComponent).setText((String)localObject);
        return;
      }
      if ((paramComponent instanceof Checkbox))
      {
        ((Checkbox)paramComponent).setLabel((String)localObject);
        return;
      }
      if ((paramComponent instanceof Label))
      {
        Label localLabel = (Label)paramComponent;
        localLabel.setText(paramRes.get(localLabel.getText(), localLabel.getText()));
        return;
      }
    }
  }

  public void mouseClicked(MouseEvent paramMouseEvent)
  {
  }

  public void mouseEntered(MouseEvent paramMouseEvent)
  {
    Component localComponent = paramMouseEvent.getComponent();
    if ((localComponent instanceof Container))
      return;
    String str = localComponent.getName();
    if ((str != null) && (str.length() > 0))
    {
      Point localPoint1 = localComponent.getLocationOnScreen();
      Point localPoint2 = paramMouseEvent.getPoint();
      localPoint1.translate(localPoint2.x + 10, localPoint2.y);
      getHelp().startHelp(new HelpWindowContent(str, true, localPoint1, this.res));
    }
  }

  public void mouseExited(MouseEvent paramMouseEvent)
  {
    getHelp().reset();
  }

  public void mousePressed(MouseEvent paramMouseEvent)
  {
  }

  public void mouseReleased(MouseEvent paramMouseEvent)
  {
  }

  public void setMouseListener(Component paramComponent, MouseListener paramMouseListener)
  {
    if ((paramComponent instanceof Container))
    {
      Component[] arrayOfComponent = ((Container)paramComponent).getComponents();
      int i = arrayOfComponent.length;
      for (int j = 0; j < i; j++)
        setMouseListener(arrayOfComponent[j], paramMouseListener);
    }
    else
    {
      paramComponent.addMouseListener(paramMouseListener);
    }
  }

  protected void setParam(Component paramComponent, Hashtable paramHashtable)
  {
    if ((paramComponent instanceof Container))
    {
      Component[] arrayOfComponent = ((Container)paramComponent).getComponents();
      int i = arrayOfComponent.length;
      for (int j = 0; j < i; j++)
        setParam(arrayOfComponent[j], paramHashtable);
    }
    else
    {
      if ((paramComponent instanceof LTextField))
      {
        paramHashtable.put(paramComponent.getName(), ((LTextField)paramComponent).getText());
        return;
      }
      if ((paramComponent instanceof Checkbox))
      {
        paramHashtable.put(paramComponent.getName(), String.valueOf(((Checkbox)paramComponent).getState()));
        return;
      }
      if ((paramComponent instanceof TextField))
      {
        paramHashtable.put(paramComponent.getName(), ((TextField)paramComponent).getText());
        return;
      }
    }
  }

  public void setParameter(Component paramComponent)
  {
    PProperties localPProperties = ((ServerStub)getAppletContext()).getHashTable();
    setParam(this, localPProperties);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.config.ConfigApplet
 * JD-Core Version:    0.6.0
 */