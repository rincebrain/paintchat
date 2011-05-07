package syi.awt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import syi.util.ThreadPool;

public class HelpWindow extends Window
  implements Runnable
{
  private Thread rAdd = null;
  private HelpWindowContent object = null;
  private ImageCanvas imageCanvas = null;
  private TextCanvas textCanvas = null;
  private boolean isShow = false;
  private boolean isFront = true;

  public HelpWindow(Frame paramFrame)
  {
    super(paramFrame);
  }

  public boolean getIsShow()
  {
    return this.isShow;
  }

  public synchronized void reset()
  {
    setVisible(false);
    this.object = null;
    if (getComponentCount() > 0)
    {
      Component localComponent = getComponent(0);
      if (localComponent == this.textCanvas)
        ((TextCanvas)localComponent).reset();
      else
        ((ImageCanvas)localComponent).reset();
    }
    if ((this.rAdd != null) && (this.rAdd != Thread.currentThread()))
      synchronized (this.rAdd)
      {
        this.rAdd.interrupt();
        this.rAdd.notify();
        this.rAdd = null;
      }
  }

  public void run()
  {
    try
    {
      Thread localThread = Thread.currentThread();
      if (this.object.timeStart > 0)
        synchronized (localThread)
        {
          localThread.wait(this.object.timeStart);
        }
      if (!localThread.isInterrupted())
      {
        showHelp(this.object);
        if (this.object.timeEnd > 0)
          synchronized (localThread)
          {
            localThread.wait(this.object.timeEnd);
          }
      }
      reset();
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void setIsFront(boolean paramBoolean)
  {
    this.isFront = paramBoolean;
  }

  public void setIsShow(boolean paramBoolean)
  {
    this.isShow = paramBoolean;
  }

  private synchronized void showHelp(HelpWindowContent paramHelpWindowContent)
  {
    if (paramHelpWindowContent == null)
      return;
    Component localComponent;
    if (paramHelpWindowContent.image != null)
    {
      if (this.imageCanvas == null)
        this.imageCanvas = new ImageCanvas(getBackground(), getForeground());
      if (getComponentCount() > 0)
      {
        localComponent = getComponent(0);
        if (localComponent != this.imageCanvas)
        {
          remove(localComponent);
          add(this.imageCanvas);
        }
      }
      else
      {
        add(this.imageCanvas);
      }
      this.imageCanvas.setImage(paramHelpWindowContent.image);
      if (paramHelpWindowContent.string != null)
        this.imageCanvas.setText(paramHelpWindowContent.getText());
    }
    else
    {
      if (this.textCanvas == null)
      {
        this.textCanvas = new TextCanvas();
        this.textCanvas.setBackground(new Color(13421823));
        this.textCanvas.setForeground(Color.black);
      }
      if (getComponentCount() > 0)
      {
        localComponent = getComponent(0);
        if (localComponent != this.textCanvas)
        {
          remove(localComponent);
          add(this.textCanvas);
        }
      }
      else
      {
        add(this.textCanvas);
      }
      this.textCanvas.setText(paramHelpWindowContent.getText());
    }
    pack();
    getSize();
    setLocation(paramHelpWindowContent.point.x, paramHelpWindowContent.point.y);
    setVisible(true);
    if (this.isFront)
      toFront();
  }

  public synchronized void startHelp(HelpWindowContent paramHelpWindowContent)
  {
    if (!paramHelpWindowContent.isVisible(this.isShow))
      return;
    reset();
    this.object = paramHelpWindowContent;
    paramHelpWindowContent.point.y += 15;
    this.rAdd = ThreadPool.poolStartThread(this, "s");
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.HelpWindow
 * JD-Core Version:    0.6.0
 */