package syi.util;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.net.URL;

public class LabelLink extends Label
{
  private String strLink = null;
  private boolean isMouse = false;
  private Color clLink;
  private Color clBack;
  private Applet applet;

  public LabelLink()
  {
  }

  public LabelLink(String paramString)
  {
    super(paramString);
  }

  public LabelLink(String paramString, int paramInt)
  {
    super(paramString, paramInt);
  }

  private void init()
  {
    if (this.isMouse)
      return;
    this.isMouse = true;
    enableEvents(48L);
    this.clBack = getForeground();
  }

  protected void processMouseEvent(MouseEvent paramMouseEvent)
  {
    processMouseMotionEvent(paramMouseEvent);
  }

  protected void processMouseMotionEvent(MouseEvent paramMouseEvent)
  {
    if (!this.isMouse)
      return;
    try
    {
      switch (paramMouseEvent.getID())
      {
      case 504:
        setFont(true);
        break;
      case 505:
        setFont(false);
        break;
      case 501:
        this.applet.getAppletContext().showDocument(new URL(this.strLink), "top");
      case 502:
      case 503:
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void setFont(boolean paramBoolean)
  {
    Font localFont = getFont();
    localFont = new Font(localFont.getName(), paramBoolean ? 2 : 0, localFont.getSize());
    setFont(localFont);
    setForeground(paramBoolean ? this.clLink : this.clBack);
  }

  public void setLink(Applet paramApplet, String paramString, Color paramColor)
  {
    if ((paramString == null) || (paramString.length() <= 0))
      return;
    init();
    this.strLink = paramString;
    this.clLink = paramColor;
    this.applet = paramApplet;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.LabelLink
 * JD-Core Version:    0.6.0
 */