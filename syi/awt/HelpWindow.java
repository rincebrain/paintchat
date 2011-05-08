package syi.awt;

import java.awt.*;
import syi.util.ThreadPool;

// Referenced classes of package syi.awt:
//            TextCanvas, ImageCanvas, HelpWindowContent

public class HelpWindow extends Window
    implements Runnable
{

    private Thread rAdd;
    private HelpWindowContent object;
    private ImageCanvas imageCanvas;
    private TextCanvas textCanvas;
    private boolean isShow;
    private boolean isFront;

    public HelpWindow(Frame frame)
    {
        super(frame);
        rAdd = null;
        object = null;
        imageCanvas = null;
        textCanvas = null;
        isShow = false;
        isFront = true;
    }

    public boolean getIsShow()
    {
        return isShow;
    }

    public synchronized void reset()
    {
        setVisible(false);
        object = null;
        if(getComponentCount() > 0)
        {
            Component component = getComponent(0);
            if(component == textCanvas)
            {
                ((TextCanvas)component).reset();
            } else
            {
                ((ImageCanvas)component).reset();
            }
        }
        if(rAdd != null && rAdd != Thread.currentThread())
        {
            synchronized(rAdd)
            {
                rAdd.interrupt();
                rAdd.notify();
                rAdd = null;
            }
        }
    }

    public void run()
    {
        try
        {
            Thread thread = Thread.currentThread();
            if(object.timeStart > 0)
            {
                synchronized(thread)
                {
                    thread.wait(object.timeStart);
                }
            }
            if(!thread.isInterrupted())
            {
                showHelp(object);
                if(object.timeEnd > 0)
                {
                    synchronized(thread)
                    {
                        thread.wait(object.timeEnd);
                    }
                }
            }
            reset();
        }
        catch(Throwable _ex) { }
    }

    public void setIsFront(boolean flag)
    {
        isFront = flag;
    }

    public void setIsShow(boolean flag)
    {
        isShow = flag;
    }

    private synchronized void showHelp(HelpWindowContent helpwindowcontent)
    {
        if(helpwindowcontent == null)
        {
            return;
        }
        if(helpwindowcontent.image != null)
        {
            if(imageCanvas == null)
            {
                imageCanvas = new ImageCanvas(getBackground(), getForeground());
            }
            if(getComponentCount() > 0)
            {
                Component component = getComponent(0);
                if(component != imageCanvas)
                {
                    remove(component);
                    add(imageCanvas);
                }
            } else
            {
                add(imageCanvas);
            }
            imageCanvas.setImage(helpwindowcontent.image);
            if(helpwindowcontent.string != null)
            {
                imageCanvas.setText(helpwindowcontent.getText());
            }
        } else
        {
            if(textCanvas == null)
            {
                textCanvas = new TextCanvas();
                textCanvas.setBackground(new Color(0xccccff));
                textCanvas.setForeground(Color.black);
            }
            if(getComponentCount() > 0)
            {
                Component component1 = getComponent(0);
                if(component1 != textCanvas)
                {
                    remove(component1);
                    add(textCanvas);
                }
            } else
            {
                add(textCanvas);
            }
            textCanvas.setText(helpwindowcontent.getText());
        }
        pack();
        getSize();
        setLocation(helpwindowcontent.point.x, helpwindowcontent.point.y);
        setVisible(true);
        if(isFront)
        {
            toFront();
        }
    }

    public synchronized void startHelp(HelpWindowContent helpwindowcontent)
    {
        if(!helpwindowcontent.isVisible(isShow))
        {
            return;
        } else
        {
            reset();
            object = helpwindowcontent;
            helpwindowcontent.point.y += 15;
            rAdd = ThreadPool.poolStartThread(this, "s");
            return;
        }
    }
}
