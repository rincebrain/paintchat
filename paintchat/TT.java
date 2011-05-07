/* TT - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;

import syi.awt.Awt;
import syi.awt.LComponent;

public class TT extends LComponent implements SW, Runnable
{
    private ToolBox ts;
    private M.Info info;
    private M.User user;
    private M mg;
    private boolean isRun = false;
    private Image[] images = null;
    private boolean isDrag = false;
    private int selButton = 0;
    private int selWhite;
    private int selPen = 0;
    private int imW = 0;
    private int imH = 0;
    private int imCount;
    private int selItem = -1;
    private M[] mgs = null;
    private int sizeTT = 0;
    private int iLast = -1;
    
    private int getIndex(int i, int i_0_, int i_1_) {
	Dimension dimension = this.getSize();
	int i_2_ = imW;
	int i_3_ = imH;
	i -= i_1_;
	int i_4_ = (dimension.width - i_1_) / i_2_;
	return i_0_ / i_3_ * i_4_ + Math.min(i / i_2_, i_4_);
    }
    
    public void lift() {
	/* empty */
    }
    
    public void mPack() {
	this.inParent();
	java.awt.Container container = this.getParent();
	Dimension dimension = this.getMaximumSize();
	dimension.height = container.getSize().height - this.getGapH();
    }
    
    public void mSetup(ToolBox toolbox, M.Info info, M.User user, M m, Res res,
		       Res res_5_) {
	ts = toolbox;
	this.info = info;
	this.user = user;
	mg = m;
	this.setTitle(res_5_.getP("window_3"));
	this.getToolkit();
	imW = imH = (int) (34.0F * LComponent.Q);
	try {
	    String string = "tt_size";
	    images = new Image[Integer.parseInt(res_5_.get(string))];
	    res_5_.remove(string);
	    int i = imW * 5 + 1;
	    int i_6_ = ((images.length + 12) / 5 + 1) * imW + 1;
	    this.setDimension(new Dimension(imW + 1, imW + 1),
			      new Dimension(i, i_6_), new Dimension(i, i_6_));
	} catch (RuntimeException runtimeexception) {
	    /* empty */
	}
    }
    
    public void paint2(Graphics graphics) {
	if (images != null) {
	    if (!isRun) {
		Thread thread = new Thread(this);
		thread.setPriority(1);
		thread.setDaemon(true);
		thread.start();
		isRun = true;
	    }
	    int i = images.length + 11;
	    int i_7_ = 0;
	    int i_8_ = 0;
	    int i_9_ = imW;
	    int i_10_ = imH;
	    int i_11_ = i_9_ - 3;
	    M m = mg;
	    int[] is = user.getBuffer();
	    Color color = this.getBackground();
	    int i_12_ = 255;
	    color.getRGB();
	    Dimension dimension = this.getSize();
	    this.getToolkit();
	    iLast = m.iTT;
	    for (int i_13_ = -1; i_13_ < i; i_13_++) {
		graphics.setColor(i_13_ + 1 == m.iTT ? Awt.cFSel : Awt.cF);
		graphics.drawRect(i_7_ + 1, i_8_ + 1, i_9_ - 2, i_10_ - 2);
		do {
		    if (i_13_ == -1) {
			graphics.setColor(Color.blue);
			graphics.fillRect(i_7_ + 2, i_8_ + 2, i_9_ - 3,
					  i_10_ - 3);
		    } else {
			if (i_13_ < 11) {
			    int[] is_14_;
			    //MONITORENTER (is_14_ = is);
//			    MISSING MONITORENTER
			    synchronized (is_14_) {
				int i_15_ = 0;
				int i_16_ = i_13_;
				for (int i_17_ = 0; i_17_ < i_11_; i_17_++) {
				    for (int i_18_ = 0; i_18_ < i_11_; i_18_++)
					is[i_15_++]
					    = (M.isTone(i_16_, i_18_, i_17_)
					       ? -1 : i_12_);
				}
				graphics.drawImage(user.mkImage(i_11_, i_11_),
						   i_7_ + 2, i_8_ + 2, color,
						   null);
				break;
			    }
			}
			Image image = images[i_13_ - 11];
			if (image == null) {
			    graphics.setColor(Color.blue);
			    graphics.fillRect(i_7_ + 2, i_8_ + 2, i_9_ - 3,
					      i_10_ - 3);
			} else
			    graphics.drawImage(image, i_7_ + 2, i_8_ + 2,
					       color, null);
		    }
		} while (false);
		i_7_ += i_9_;
		if (i_7_ + i_9_ >= dimension.width) {
		    i_7_ = 0;
		    i_8_ += i_10_;
		    if (i_8_ + i_10_ >= dimension.height)
			break;
		}
	    }
	}
    }
    
    public void pMouse(MouseEvent mouseevent) {
	if (mouseevent.getID() == 501) {
	    this.getSize();
	    int i = getIndex(mouseevent.getX(), mouseevent.getY(), 0);
	    if (i < images.length + 12) {
		mg.iTT = i;
		this.repaint();
	    }
	}
    }
    
    public void run() {
	try {
	    DirectColorModel directcolormodel
		= new DirectColorModel(24, 65280, 65280, 255);
	    int i = imW;
	    int i_19_ = imH;
	    for (int i_20_ = 0; i_20_ < images.length; i_20_++) {
		if (images[i_20_] == null) {
		    float[] fs = info.getTT(i_20_ + 12);
		    int[] is = new int[fs.length];
		    for (int i_21_ = 0; i_21_ < is.length; i_21_++)
			is[i_21_]
			    = (int) ((1.0F - fs[i_21_]) * 255.0F) << 8 | 0xff;
		    int i_22_ = (int) Math.sqrt((double) is.length);
		    images[i_20_]
			= Awt.toMin((this.createImage
				     (new MemoryImageSource(i_22_, i_22_,
							    directcolormodel,
							    is, 0, i_22_))),
				    i - 3, i_19_ - 3);
		    if (i_20_ % 5 == 2)
			this.repaint();
		}
	    }
	    this.repaint();
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    public void up() {
	if (iLast != mg.iTT)
	    this.repaint();
    }
}
