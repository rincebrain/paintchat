package paintchat;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.util.Hashtable;
import syi.awt.Awt;
import syi.awt.LComponent;

// Referenced classes of package paintchat:
//            SW, Res, M, ToolBox

public class TT extends LComponent
    implements SW, Runnable
{

    private ToolBox ts;
    private M.Info info;
    private M.User user;
    private M mg;
    private boolean isRun;
    private Image images[];
    private boolean isDrag;
    private int selButton;
    private int selWhite;
    private int selPen;
    private int imW;
    private int imH;
    private int imCount;
    private int selItem;
    private M mgs[];
    private int sizeTT;
    private int iLast;

    public TT()
    {
        isRun = false;
        images = null;
        isDrag = false;
        selButton = 0;
        selPen = 0;
        imW = 0;
        imH = 0;
        selItem = -1;
        mgs = null;
        sizeTT = 0;
        iLast = -1;
    }

    private int getIndex(int i, int j, int k)
    {
        Dimension dimension = getSize();
        int l = imW;
        int i1 = imH;
        i -= k;
        int j1 = (dimension.width - k) / l;
        return (j / i1) * j1 + Math.min(i / l, j1);
    }

    public void lift()
    {
    }

    public void mPack()
    {
        inParent();
        java.awt.Container container = getParent();
        Dimension dimension = getMaximumSize();
        dimension.height = container.getSize().height - getGapH();
    }

    public void mSetup(ToolBox toolbox, M.Info info1, M.User user1, M m, Res res, Res res1)
    {
        ts = toolbox;
        info = info1;
        user = user1;
        mg = m;
        setTitle(res1.getP("window_3"));
        getToolkit();
        imW = imH = (int)(34F * LComponent.Q);
        try
        {
            String s = "tt_size";
            images = new Image[Integer.parseInt(res1.get(s))];
            res1.remove(s);
            int i = imW * 5 + 1;
            int j = ((images.length + 12) / 5 + 1) * imW + 1;
            setDimension(new Dimension(imW + 1, imW + 1), new Dimension(i, j), new Dimension(i, j));
        }
        catch(RuntimeException _ex) { }
    }

    public void paint2(Graphics g)
    {
        if(images == null)
        {
            return;
        }
        if(!isRun)
        {
            Thread thread = new Thread(this);
            thread.setPriority(1);
            thread.setDaemon(true);
            thread.start();
            isRun = true;
        }
        int i = images.length + 11;
        int j = 0;
        int k = 0;
        int l = imW;
        int i1 = imH;
        int j1 = l - 3;
        M m = mg;
        int ai[] = user.getBuffer();
        Color color = getBackground();
        char c = '\377';
        color.getRGB();
        Dimension dimension = getSize();
        getToolkit();
        iLast = m.iTT;
        for(int i2 = -1; i2 < i; i2++)
        {
            g.setColor(i2 + 1 != m.iTT ? Awt.cF : Awt.cFSel);
            g.drawRect(j + 1, k + 1, l - 2, i1 - 2);
            if(i2 == -1)
            {
                g.setColor(Color.blue);
                g.fillRect(j + 2, k + 2, l - 3, i1 - 3);
            } else
            if(i2 < 11)
            {
                synchronized(ai)
                {
                    int j2 = 0;
                    int k2 = i2;
                    for(int l1 = 0; l1 < j1; l1++)
                    {
                        for(int k1 = 0; k1 < j1; k1++)
                        {
                            ai[j2++] = M.isTone(k2, k1, l1) ? -1 : ((int) (c));
                        }

                    }

                    g.drawImage(user.mkImage(j1, j1), j + 2, k + 2, color, null);
                }
            } else
            {
                Image image = images[i2 - 11];
                if(image == null)
                {
                    g.setColor(Color.blue);
                    g.fillRect(j + 2, k + 2, l - 3, i1 - 3);
                } else
                {
                    g.drawImage(image, j + 2, k + 2, color, null);
                }
            }
            j += l;
            if(j + l < dimension.width)
            {
                continue;
            }
            j = 0;
            k += i1;
            if(k + i1 >= dimension.height)
            {
                break;
            }
        }

    }

    public void pMouse(MouseEvent mouseevent)
    {
        if(mouseevent.getID() == 501)
        {
            getSize();
            int i = getIndex(mouseevent.getX(), mouseevent.getY(), 0);
            if(i >= images.length + 12)
            {
                return;
            }
            mg.iTT = i;
            repaint();
        }
    }

    public void run()
    {
        try
        {
            DirectColorModel directcolormodel = new DirectColorModel(24, 65280, 65280, 255);
            int i = imW;
            int j = imH;
            for(int k = 0; k < images.length; k++)
            {
                if(images[k] == null)
                {
                    float af[] = info.getTT(k + 12);
                    int ai[] = new int[af.length];
                    for(int l = 0; l < ai.length; l++)
                    {
                        ai[l] = (int)((1.0F - af[l]) * 255F) << 8 | 0xff;
                    }

                    int i1 = (int)Math.sqrt(ai.length);
                    images[k] = Awt.toMin(createImage(new MemoryImageSource(i1, i1, directcolormodel, ai, 0, i1)), i - 3, j - 3);
                    if(k % 5 == 2)
                    {
                        repaint();
                    }
                }
            }

            repaint();
        }
        catch(Throwable _ex) { }
    }

    public void up()
    {
        if(iLast != mg.iTT)
        {
            repaint();
        }
    }
}
