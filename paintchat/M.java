/* M - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

import syi.awt.Awt;
import syi.util.ByteStream;

public class M
{
    private Info info;
    private User user;
    public int iHint = 0;
    public int iPen = 0;
    public int iPenM = 0;
    public int iTT = 0;
    public int iColor = 0;
    public int iColorMask = 0;
    public int iAlpha = 255;
    public int iAlpha2;
    public int iSA = 65280;
    public int iLayer = 0;
    public int iLayerSrc = 1;
    public int iMask = 0;
    public int iSize = 0;
    public int iSS = 65280;
    public int iCount = -8;
    public int iSOB;
    public boolean isAFix;
    public boolean isOver;
    public boolean isCount = true;
    public boolean isAnti;
    public boolean isAllL;
    public byte[] strHint;
    private int iSeek;
    private int iOffset;
    private byte[] offset;
    public static final int H_FLINE = 0;
    public static final int H_LINE = 1;
    public static final int H_BEZI = 2;
    public static final int H_RECT = 3;
    public static final int H_FRECT = 4;
    public static final int H_OVAL = 5;
    public static final int H_FOVAL = 6;
    public static final int H_FILL = 7;
    public static final int H_TEXT = 8;
    public static final int H_COPY = 9;
    public static final int H_CLEAR = 10;
    public static final int H_SP = 11;
    public static final int H_VTEXT = 12;
    public static final int H_L = 14;
    public static final int P_SOLID = 0;
    public static final int P_PEN = 1;
    public static final int P_SUISAI = 2;
    public static final int P_SUISAI2 = 3;
    public static final int P_WHITE = 4;
    public static final int P_SWHITE = 5;
    public static final int P_LIGHT = 6;
    public static final int P_DARK = 7;
    public static final int P_BOKASHI = 8;
    public static final int P_MOSAIC = 9;
    public static final int P_FILL = 10;
    public static final int P_LPEN = 11;
    public static final int P_NULL = 14;
    public static final int P_LR = 17;
    public static final int P_UD = 18;
    public static final int P_R = 19;
    public static final int P_FUSION = 20;
    public static final int PM_PEN = 0;
    public static final int PM_SUISAI = 1;
    public static final int PM_MANY = 2;
    public static final int M_N = 0;
    public static final int M_M = 1;
    public static final int M_R = 2;
    public static final int M_ADD = 3;
    public static final int M_SUB = 4;
    private static final int F1O = 4;
    private static final int F1C = 8;
    private static final int F1A = 16;
    private static final int F1S = 32;
    private static final int F2H = 1;
    private static final int F2PM = 2;
    private static final int F2M = 4;
    private static final int F2P = 8;
    private static final int F2T = 16;
    private static final int F2L = 32;
    private static final int F2LS = 64;
    private static final int F3A = 1;
    private static final int F3C = 2;
    private static final int F3CM = 4;
    private static final int F3S = 8;
    private static final int F3E = 16;
    private static final int F3SA = 32;
    private static final int F3SS = 64;
    private static final int DEF_COUNT = -8;
    private static final String ENCODE = "UTF8";
    private static float[] b255 = new float[256];
    static float[] b255d = new float[256];
    private static ColorModel color_model = null;
    private static final M mgDef = new M();
    
    public class User
    {
	private Image image = null;
	private SRaster raster = null;
	private int[] buffer = new int[65536];
	private int[] argb = new int[4];
	public int[] points = new int[6];
	private int[] ps2 = null;
	private int[] p = null;
	private int pW;
	private int pM = -1;
	private int pA = -1;
	private int pS = -1;
	private float pV = 1.0F;
	private float[] pTT = null;
	private int pTTW;
	private boolean isDirect;
	public int wait = 0;
	public boolean isPre = false;
	private int[] pX = new int[4];
	private int[] pY = new int[4];
	private int oX;
	private int oY;
	private float fX;
	private float fY;
	private int iDCount;
	private int X;
	private int Y;
	private int X2;
	private int Y2;
	private int count = 0;
	private int countMax;
	
	private void setup(M m) {
	    pV = M.b255[m.info.bPen[m.iPenM].length - 1];
	    m.getPM();
	    count = 0;
	    iDCount = 0;
	    oX = -1000;
	    oY = -1000;
	    isDirect = m.iPen == 3 || m.iHint == 9 || m.isOver;
	    if (info.L <= m.iLayer)
		info.setL(m.iLayer + 1);
	    info.layers[m.iLayer].isDraw = true;
	    if (m.iTT >= 12) {
		pTT = info.getTT(m.iTT);
		pTTW = (int) Math.sqrt((double) pTT.length);
	    }
	}
	
	public void setIm(M m) {
	    if (!m.isText()) {
		if (pM != m.iPenM || pA != m.iAlpha || pS != m.iSize) {
		    int[] is = m.info.bPen[m.iPenM][m.iSize];
		    int i = is.length;
		    if (p == null || p.length < i)
			p = new int[i];
		    float f = M.b255[m.iAlpha];
		    for (int i_0_ = 0; i_0_ < i; i_0_++) {
			float f_1_ = (float) is[i_0_] * f;
			p[i_0_] = f_1_ <= 1.0F && f_1_ > 0.0F ? 1 : (int) f_1_;
		    }
		    pW = m.iPen = (int) Math.sqrt((double) i);
		    pM = m.iPenM;
		    pA = m.iAlpha;
		    pS = m.iSize;
		}
	    }
	}
	
	public int getPixel(int i, int i_2_) {
	    int i_3_ = info.imW;
	    if (i < 0 || i_2_ < 0 || i >= i_3_ || i_2_ >= info.imH)
		return 0;
	    int i_4_ = info.Q;
	    mkLPic(buffer, i, i_2_, 1, 1, i_4_);
	    if (info.L != 0) {
		/* empty */
	    }
	    if (info.m.iLayer != 0) {
		/* empty */
	    }
	    LO[] los = info.layers;
	    i *= i_4_;
	    i_2_ *= i_4_;
	    float f = 0.0F;
	    for (int i_5_ = info.m.iLayer; i_5_ >= 0; i_5_--) {
		f += ((1.0F - f) * M.b255[los[i_5_].getPixel(i, i_2_) >>> 24]
		      * los[i_5_].iAlpha);
		if (f >= 1.0F)
		    break;
	    }
	    return ((int) (f * 255.0F) << 24) + (buffer[0] & 0xffffff);
	}
	
	public int[] getBuffer() {
	    return buffer;
	}
	
	public long getRect() {
	    return ((long) (X <= 0 ? 0 : X) << 48
		    | (long) (Y <= 0 ? 0 : Y) << 32 | (long) (X2 << 16)
		    | (long) Y2);
	}
	
	public void setRect(int i, int i_6_, int i_7_, int i_8_) {
	    X = i;
	    Y = i_6_;
	    X2 = i_7_;
	    Y2 = i_8_;
	}
	
	public final void addRect(int i, int i_9_, int i_10_, int i_11_) {
	    setRect(Math.min(i, X), Math.min(i_9_, Y), Math.max(i_10_, X2),
		    Math.max(i_11_, Y2));
	}
	
	public Image mkImage(int i, int i_12_) {
	    raster.newPixels(image, buffer, i, i_12_);
	    return image;
	}
    }
    
    public class Info
    {
	private ByteStream workOut = new ByteStream();
	public boolean isLEdit = false;
	public boolean isFill = false;
	public boolean isClean = false;
	public long permission = -1L;
	public long unpermission = 0L;
	private Res cnf;
	private String dirTT = null;
	public Graphics g = null;
	private int vWidth;
	private int vHeight;
	private Dimension vD = new Dimension();
	private Component component = null;
	public int Q = 1;
	public int L;
	public LO[] layers = null;
	public int scale = 1;
	public int scaleX = 0;
	public int scaleY = 0;
	private byte[] iMOffs;
	public int imH;
	public int imW;
	public int W;
	public int H;
	private int[][][] bPen = new int[16][][];
	private float[][] bTT = new float[14][];
	public M m = new M();
	
	public void setSize(int i, int i_13_, int i_14_) {
	    int i_15_ = i * i_14_;
	    int i_16_ = i_13_ * i_14_;
	    if (i_15_ != W || i_16_ != H) {
		for (int i_17_ = 0; i_17_ < L; i_17_++)
		    layers[i_17_].setSize(i_15_, i_16_);
	    }
	    imW = i;
	    imH = i_13_;
	    W = i_15_;
	    H = i_16_;
	    Q = i_14_;
	    int i_18_ = W * H;
	    if (iMOffs == null || iMOffs.length < i_18_)
		iMOffs = new byte[i_18_];
	}
	
	public void setLayers(LO[] los) {
	    L = los.length;
	    layers = los;
	}
	
	public void setComponent(Component component, Graphics graphics, int i,
				 int i_19_) {
	    this.component = component;
	    vWidth = i;
	    vHeight = i_19_;
	    g = graphics;
	}
	
	public void setL(int i) {
	    int i_20_ = layers == null ? 0 : layers.length;
	    int i_21_ = Math.min(i_20_, i);
	    if (i_20_ != i) {
		LO[] los = new LO[i];
		if (layers != null)
		    System.arraycopy(layers, 0, los, 0, i_21_);
		for (int i_22_ = 0; i_22_ < i; i_22_++) {
		    if (los[i_22_] == null)
			los[i_22_] = LO.getLO(W, H);
		}
		layers = los;
	    }
	    L = i;
	}
	
	public void delL(int i) {
	    int i_23_ = layers.length;
	    if (i < i_23_) {
		LO[] los = new LO[i_23_ - 1];
		int i_24_ = 0;
		for (int i_25_ = 0; i_25_ < i_23_; i_25_++) {
		    if (i_25_ != i)
			los[i_24_++] = layers[i_25_];
		}
		layers = los;
		L = i_23_ - 1;
	    }
	}
	
	public void swapL(int i, int i_26_) {
	    int i_27_ = Math.max(i, i_26_);
	    if (i_27_ >= L)
		setL(i_27_);
	    layers[i].isDraw = true;
	    layers[i_26_].isDraw = true;
	    layers[i].swap(layers[i_26_]);
	}
	
	public boolean addScale(int i, boolean bool) {
	    if (bool) {
		if (i <= 0) {
		    scale = 1;
		    setQuality(1 - i);
		} else {
		    setQuality(1);
		    scale = i;
		}
		return true;
	    }
	    int i_28_ = scale + i;
	    if (i_28_ > 32)
		return false;
	    if (i_28_ <= 0) {
		scale = 1;
		setQuality(Q + 1 - i_28_);
	    } else if (Q >= 2)
		setQuality(Q - 1);
	    else {
		setQuality(1);
		scale = i_28_;
	    }
	    return true;
	}
	
	public void setQuality(int i) {
	    Q = i;
	    imW = W / Q;
	    imH = H / Q;
	}
	
	public Dimension getSize() {
	    vD.setSize(vWidth, vHeight);
	    return vD;
	}
	
	private void center(Point point) {
	    point.x = point.x / scale + scaleX;
	    point.y = point.y / scale + scaleY;
	}
	
	public int[][][] getPenMask() {
	    return bPen;
	}
	
	public int getPenSize(M m) {
	    return (int) Math.sqrt((double) bPen[m.iPenM][m.iSize].length);
	}
	
	public int getPMMax() {
	    return (m.isText() || m.iHint >= 3 && m.iHint <= 6 ? 255
		    : bPen[m.iPenM].length);
	}
	
	public float[] getTT(int i) {
	    i -= 12;
	    if (bTT[i] == null) {
		if (dirTT != null) {
		    String string = dirTT;
		    dirTT = null;
		    try {
			cnf.loadZip(string);
		    } catch (IOException ioexception) {
			ioexception.printStackTrace();
		    }
		}
		int[] is = M.this.loadIm("tt/" + i + ".gif", false);
		if (is == null)
		    return null;
		int i_29_ = is.length;
		float[] fs = new float[i_29_];
		for (int i_30_ = 0; i_30_ < i_29_; i_30_++)
		    fs[i_30_] = M.b255[is[i_30_]];
		bTT[i] = fs;
	    }
	    return bTT[i];
	}
    }
    
    public M() {
	/* empty */
    }
    
    public M(Info info, User user) {
	this.info = info;
	this.user = user;
    }
    
    private final void copy(int[][] is, int[][] is_31_) {
	for (int i = 0; i < is_31_.length; i++)
	    System.arraycopy(is[i], 0, is_31_[i], 0, is_31_[i].length);
    }
    
    public final void dBuffer() {
	dBuffer(!user.isDirect, user.X, user.Y, user.X2, user.Y2);
    }
    
    private final void dBuffer(boolean bool, int i, int i_32_, int i_33_,
			       int i_34_) {
	try {
	    int i_35_ = info.scale;
	    int i_36_ = info.Q;
	    int i_37_ = info.W;
	    int i_38_ = info.H;
	    int i_39_ = info.scaleX;
	    int i_40_ = info.scaleY;
	    boolean bool_41_ = i_35_ == 1;
	    int[] is = user.buffer;
	    Color color = Color.white;
	    Graphics graphics = info.g;
	    if (graphics != null) {
		i /= i_36_;
		i_32_ /= i_36_;
		i_33_ /= i_36_;
		i_34_ /= i_36_;
		i = i <= i_39_ ? i_39_ : i;
		i_32_ = i_32_ <= i_40_ ? i_40_ : i_32_;
		int i_42_ = info.vWidth / i_35_ + i_39_;
		i_33_ = i_33_ > i_42_ ? i_42_ : i_33_;
		i_33_ = i_33_ > i_37_ ? i_37_ : i_33_;
		i_42_ = info.vHeight / i_35_ + i_40_;
		i_34_ = i_34_ > i_42_ ? i_42_ : i_34_;
		i_34_ = i_34_ > i_38_ ? i_38_ : i_34_;
		if (i_33_ > i && i_34_ > i_32_) {
		    i_37_ = i_33_ - i;
		    int i_43_ = i_37_ * i_35_;
		    int i_44_ = (i - i_39_) * i_35_;
		    if (i != 0) {
			/* empty */
		    }
		    int i_45_ = i_32_;
		    i_42_ = is.length / (i_37_ * i_36_ * i_36_);
		    for (;;) {
			i_38_ = Math.min(i_42_, i_34_ - i_45_);
			if (i_38_ <= 0)
			    break;
			Image image
			    = (bool ? mkMPic(i, i_45_, i_37_, i_38_, i_36_)
			       : mkLPic(null, i, i_45_, i_37_, i_38_, i_36_));
			if (bool_41_)
			    graphics.drawImage(image, i_44_, i_45_ - i_40_,
					       color, null);
			else
			    graphics.drawImage(image, i_44_,
					       (i_45_ - i_40_) * i_35_, i_43_,
					       i_38_ * i_35_, color, null);
			i_45_ += i_38_;
		    }
		}
	    }
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	}
    }
    
    private final void dBz(int[] is) throws InterruptedException {
	try {
	    int i = is[0];
	    int i_46_ = 0;
	    for (int i_47_ = 1; i_47_ < 4; i_47_++) {
		float f = (float) (is[i_47_] >> 16);
		float f_48_ = (float) (short) is[i_47_];
		if ((float) (i >> 16) != 0) {
		    /* empty */
		}
		if ((float) (short) i != 0) {
		    /* empty */
		}
		i_46_ += Math.sqrt((double) (f * f + f_48_ * f_48_));
		i = is[i_47_];
	    }
	    if (i_46_ > 0) {
		int i_49_ = -100;
		int i_50_ = -100;
		int i_51_ = -1000;
		int i_52_ = -1000;
		int i_53_ = 0;
		boolean bool = isAnti;
		int i_54_ = user.pW / 2;
		for (i = i_46_; i > 0; i--) {
		    float f = (float) i / (float) i_46_;
		    float f_55_ = (float) Math.pow((double) (1.0F - f), 3.0);
		    float f_56_ = f_55_ * (float) (is[3] >> 16);
		    float f_57_ = f_55_ * (float) (short) is[3];
		    f_55_ = 3.0F * (1.0F - f) * (1.0F - f) * f;
		    f_56_ += f_55_ * (float) (is[2] >> 16);
		    f_57_ += f_55_ * (float) (short) is[2];
		    f_55_ = 3.0F * f * f * (1.0F - f);
		    f_56_ += f_55_ * (float) (is[1] >> 16);
		    f_57_ += f_55_ * (float) (short) is[1];
		    f_55_ = f * f * f;
		    f_56_ += f_55_ * (float) (is[0] >> 16);
		    f_57_ += f_55_ * (float) (short) is[0];
		    i_49_ = (int) f_56_ + i_54_;
		    i_50_ = (int) f_57_ + i_54_;
		    if (i_49_ != i_51_ || i_50_ != i_52_) {
			if (bool) {
			    shift(i_49_, i_50_);
			    if (++i_53_ >= 4)
				dFLine2(iSize);
			} else
			    dFLine(i_49_, i_50_, iSize);
			i_51_ = i_49_;
			i_52_ = i_50_;
		    }
		}
		user.X--;
		user.Y--;
		user.X2 += 2;
		user.Y2 += 2;
	    }
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	}
    }
    
    public void dClear() {
	if (iPen != 14) {
	    for (int i = 0; i < info.L; i++) {
		if (i >= 64 || (info.unpermission & (long) (1 << i)) == 0L)
		    info.layers[i].clear();
	    }
	    user.isDirect = true;
	    setD(0, 0, info.W, info.H);
	    if (user.wait >= 0)
		dBuffer();
	}
    }
    
    private void dFusion(byte[] is) {
	LO[] los = info.layers;
	LO lo = new LO();
	LO lo_58_ = new LO();
	int i = info.W;
	int i_59_ = is.length / 4;
	int[] is_60_ = user.buffer;
	int i_61_ = is_60_.length / i;
	for (int i_62_ = 0; i_62_ < info.H; i_62_ += i_61_) {
	    int i_63_ = Math.min(info.H - i_62_, i_61_);
	    if (i * i_63_ != 0) {
		/* empty */
	    }
	    int i_64_ = 0;
	    LO lo_65_ = null;
	    for (int i_66_ = 0; i_66_ < i_59_; i_66_++) {
		LO lo_67_ = los[is[i_64_++]];
		lo.setField(lo_67_);
		lo_67_.iAlpha = b255[is[i_64_++] & 0xff];
		lo_67_.iCopy = is[i_64_++];
		i_64_++;
		lo_67_.normalize(lo_67_.iAlpha, 0, i_62_, i, i_62_ + i_63_);
		if (lo_65_ == null) {
		    lo_65_ = lo_67_;
		    lo_58_.setField(lo);
		    lo_67_.reserve();
		} else {
		    if (lo_67_.iCopy == 1) {
			memset(is_60_, 16777215);
			for (int i_68_ = 0; i_68_ < i_66_ - 2; i_68_++)
			    los[i_68_].draw(is_60_, 0, i_62_, i, i_62_ + i_63_,
					    i);
		    }
		    lo_67_.dAdd(lo_65_.offset, 0, i_62_, i, i_62_ + i_63_,
				is_60_);
		    lo_67_.clear(0, i_62_, i, i_62_ + i_63_);
		    lo_67_.setField(lo);
		}
	    }
	    if (lo_65_ != los[iLayer]) {
		lo_65_.copyTo(0, i_62_, i, i_62_ + i_63_, los[iLayer], 0,
			      i_62_, null);
		lo_65_.clear(0, i_62_, i, i_62_ + i_63_);
	    }
	}
	lo.iAlpha = 1.0F;
	lo.iCopy = 0;
	lo.isDraw = true;
	for (int i_69_ = 0; i_69_ < i_59_; i_69_++) {
	    LO lo_70_ = los[is[i_69_ * 4]];
	    lo.name = lo_70_.name;
	    lo_70_.setField(lo);
	}
    }
    
    private void dCopy(int[] is) {
	if (info.W != 0) {
	    /* empty */
	}
	if (info.H != 0) {
	    /* empty */
	}
	int i = is[0];
	int i_71_ = i >> 16;
	short i_72_ = (short) i;
	i = is[1];
	int i_73_ = i >> 16;
	short i_74_ = (short) i;
	i = is[2];
	int i_75_ = i >> 16;
	short i_76_ = (short) i;
	info.layers[iLayerSrc].copyTo(i_71_, i_72_, i_73_, i_74_,
				      info.layers[iLayer], i_75_, i_76_,
				      user.buffer);
	setD(i_75_, i_76_, i_75_ + (i_73_ - i_71_), i_76_ + (i_74_ - i_72_));
    }
    
    public final void dEnd() throws InterruptedException {
	if (!user.isDirect)
	    dFlush();
	ByteStream bytestream = info.workOut;
	if (bytestream.size() > 0) {
	    offset = bytestream.writeTo(offset, 0);
	    iOffset = bytestream.size();
	}
	if (user.wait == -1)
	    dBuffer();
    }
    
    private void dFill(byte[] is, int i, int i_77_, int i_78_, int i_79_) {
	byte i_80_ = (byte) iAlpha;
	int i_81_ = info.W;
	try {
	    int i_82_ = i_78_ - i;
	    for (/**/; i_77_ < i_79_; i_77_++) {
		int i_84_;
		int i_83_ = i_84_ = i_77_ * i_81_ + i;
		int i_85_;
		for (i_85_ = 0; i_85_ < i_82_; i_85_++) {
		    if (is[i_84_] == i_80_)
			break;
		    i_84_++;
		}
		for (/**/; i_85_ < i_82_; i_85_++) {
		    if (is[i_84_] != i_80_)
			break;
		    i_84_++;
		}
		i_83_ = i_84_;
		if (i_85_ < i_82_) {
		    for (/**/; i_85_ < i_82_; i_85_++) {
			if (is[i_84_] == i_80_)
			    break;
			i_84_++;
		    }
		    int i_86_ = i_84_;
		    if (i_85_ < i_82_) {
			for (/**/; i_83_ < i_86_; i_83_++)
			    is[i_83_] = i_80_;
		    }
		}
	    }
	} catch (RuntimeException runtimeexception) {
	    System.out.println(runtimeexception);
	}
    }
    
    private void dFill(int[] is, int i, int i_87_, int i_88_, int i_89_) {
	int i_90_ = iAlpha;
	int i_91_ = info.W;
	try {
	    int i_92_ = i_88_ - i;
	    for (/**/; i_87_ < i_89_; i_87_++) {
		int i_93_ = i_87_ * i_91_ + i;
		int i_94_;
		for (i_94_ = i_93_ + i_92_; i_93_ < i_94_; i_93_++) {
		    if (is[i_93_] == i_90_)
			break;
		}
		if (i_93_ < i_94_ - 1) {
		    for (i_93_++; i_93_ < i_94_; i_93_++) {
			if (is[i_93_] != i_90_)
			    break;
		    }
		    if (i_93_ < i_94_ - 1) {
			int i_95_ = i_93_;
			for (i_93_++; i_93_ < i_94_; i_93_++) {
			    if (is[i_93_] == i_90_)
				break;
			}
			if (i_93_ < i_94_) {
			    for (int i_96_ = i_93_; i_95_ < i_96_; i_95_++)
				is[i_95_] = i_90_;
			}
		    }
		}
	    }
	} catch (RuntimeException runtimeexception) {
	    System.out.println(runtimeexception);
	}
    }
    
    private void dFill(int i, int i_97_) {
	int i_98_ = info.W;
	int i_99_ = info.H;
	byte i_100_ = (byte) iAlpha;
	byte[] is = info.iMOffs;
	try {
	    int[] is_101_ = user.buffer;
	    int i_102_ = 0;
	    if (i < 0 || i >= i_98_ || i_97_ < 0 || i_97_ >= i_99_)
		return;
	    int i_103_ = pix(i, i_97_);
	    int i_104_ = iAlpha << 24 | iColor;
	    if (i_103_ == i_104_)
		return;
	    is_101_[i_102_++] = s(i_103_, i, i_97_) << 16 | i_97_;
	    while (i_102_ > 0) {
		int i_105_ = is_101_[--i_102_];
		i = i_105_ >>> 16;
		i_97_ = i_105_ & 0xffff;
		int i_106_ = i_98_ * i_97_;
		boolean bool = false;
		boolean bool_107_ = false;
		for (;;) {
		    is[i_106_ + i] = i_100_;
		    if (i_97_ > 0 && pix(i, i_97_ - 1) == i_103_
			&& is[i_106_ - i_98_ + i] == 0) {
			if (!bool) {
			    bool = true;
			    is_101_[i_102_++]
				= s(i_103_, i, i_97_ - 1) << 16 | i_97_ - 1;
			}
		    } else
			bool = false;
		    if (i_97_ < i_99_ - 1 && pix(i, i_97_ + 1) == i_103_
			&& is[i_106_ + i_98_ + i] == 0) {
			if (!bool_107_) {
			    bool_107_ = true;
			    is_101_[i_102_++]
				= s(i_103_, i, i_97_ + 1) << 16 | i_97_ + 1;
			}
		    } else
			bool_107_ = false;
		    if (i <= 0 || pix(i - 1, i_97_) != i_103_
			|| is[i_106_ + i - 1] != 0)
			break;
		    i--;
		}
	    }
	} catch (RuntimeException runtimeexception) {
	    System.out.println(runtimeexception);
	}
	setD(0, 0, i_98_, i_99_);
	t();
    }
    
    private final void dFLine(float f, float f_108_, int i)
	throws InterruptedException {
	int i_109_ = user.wait;
	float f_110_ = user.fX;
	float f_111_ = user.fY;
	float f_112_ = f - f_110_;
	float f_113_ = f_108_ - f_111_;
	float f_114_ = Math.max(Math.abs(f_112_), Math.abs(f_113_));
	int i_115_ = (int) f_110_;
	int i_116_ = (int) f_111_;
	int i_117_ = user.oX;
	int i_118_ = user.oY;
	float f_119_ = 0.25F;
	if (!isCount)
	    user.count = 0;
	int i_120_ = ss(i);
	int i_121_ = sa(i);
	int i_122_ = Math.max(i_120_, iSize);
	float f_123_ = (float) iSize;
	float f_124_ = (float) iAlpha;
	float f_125_
	    = f_114_ == 0.0F ? 0.0F : ((float) i_120_ - f_123_) / f_114_;
	f_125_ = f_125_ >= 1.0F ? 1.0F : f_125_ <= -1.0F ? -1.0F : f_125_;
	float f_126_
	    = f_114_ == 0.0F ? 0.0F : ((float) i_121_ - f_124_) / f_114_;
	f_126_ = f_126_ >= 1.0F ? 1.0F : f_126_ <= -1.0F ? -1.0F : f_126_;
	float f_127_ = f_112_ == 0.0F ? 0.0F : f_112_ / f_114_;
	float f_128_ = f_113_ == 0.0F ? 0.0F : f_113_ / f_114_;
	float f_129_ = f_110_;
	float f_130_ = f_111_;
	if (f_114_ <= 0.0F)
	    f_114_++;
	f_127_ *= f_119_;
	f_128_ *= f_119_;
	f_125_ *= f_119_;
	f_126_ *= f_119_;
	int i_131_ = (int) (f_114_ / f_119_);
	for (int i_132_ = 0; i_132_ < i_131_; i_132_++) {
	    if (i_117_ != i_115_ || i_118_ != i_116_) {
		user.count--;
		i_117_ = i_115_;
		i_118_ = i_116_;
	    }
	    if (user.count <= 0) {
		user.count = user.countMax;
		iSize = (int) f_123_;
		iAlpha = (int) f_124_;
		getPM();
		int i_133_ = i_115_ - (user.pW >>> 1);
		int i_134_ = i_116_ - (user.pW >>> 1);
		float f_135_ = f_129_ - (float) (int) f_129_;
		float f_136_ = f_130_ - (float) (int) f_130_;
		if (f_135_ < 0.0F) {
		    i_133_--;
		    f_135_++;
		}
		if (f_136_ < 0.0F) {
		    i_134_--;
		    f_136_++;
		}
		if (f_135_ != 1.0F && f_136_ != 1.0F)
		    dPen(i_133_, i_134_, (1.0F - f_135_) * (1.0F - f_136_));
		if (f_135_ != 0.0F)
		    dPen(i_133_ + 1, i_134_, f_135_ * (1.0F - f_136_));
		if (f_136_ != 0.0F)
		    dPen(i_133_, i_134_ + 1, (1.0F - f_135_) * f_136_);
		if (f_135_ != 0.0F && f_136_ != 0.0F)
		    dPen(i_133_ + 1, i_134_ + 1, f_135_ * f_136_);
		if (i_109_ > 0) {
		    dBuffer(!user.isDirect, i_133_, i_134_, i_133_ + user.pW,
			    i_134_ + user.pW);
		    if (i_109_ > 1) {
			Thread.currentThread();
			Thread.sleep((long) i_109_);
		    }
		}
	    }
	    i_115_ = (int) (f_129_ += f_127_);
	    i_116_ = (int) (f_130_ += f_128_);
	    f_123_ += f_125_;
	    f_124_ += f_126_;
	}
	user.fX = f_129_;
	user.fY = f_130_;
	user.oX = i_117_;
	user.oY = i_118_;
	int i_137_
	    = (int) Math.sqrt((double) info.bPen[iPenM][i_122_].length) / 2;
	int i_138_ = (int) Math.min(f_110_, f) - i_137_;
	int i_139_ = (int) Math.min(f_111_, f_108_) - i_137_;
	int i_140_ = (int) Math.max(f_110_, f) + i_137_ + info.Q + 1;
	int i_141_ = (int) Math.max(f_111_, f_108_) + i_137_ + info.Q + 1;
	if (i_109_ == 0)
	    dBuffer(!user.isDirect, i_138_, i_139_, i_140_, i_141_);
	addD(i_138_, i_139_, i_140_, i_141_);
    }
    
    private final void dFLine(int i, int i_142_, int i_143_)
	throws InterruptedException {
	int i_144_ = user.wait;
	int i_145_ = (int) user.fX;
	int i_146_ = (int) user.fY;
	int i_147_ = i - i_145_;
	int i_148_ = i_142_ - i_146_;
	int i_149_ = Math.max(Math.abs(i_147_), Math.abs(i_148_));
	int i_150_ = i_145_;
	int i_151_ = i_146_;
	int i_152_ = user.oX;
	int i_153_ = user.oY;
	if (!isCount)
	    user.count = 0;
	int i_154_ = ss(i_143_);
	int i_155_ = sa(i_143_);
	int i_156_ = Math.max(i_154_, iSize);
	float f = (float) iSize;
	float f_157_ = (float) iAlpha;
	float f_158_
	    = i_149_ == 0 ? 0.0F : ((float) i_154_ - f) / (float) i_149_;
	f_158_ = f_158_ >= 1.0F ? 1.0F : f_158_ <= -1.0F ? -1.0F : f_158_;
	float f_159_
	    = i_149_ == 0 ? 0.0F : ((float) i_155_ - f_157_) / (float) i_149_;
	f_159_ = f_159_ >= 5.0F ? 5.0F : f_159_ <= -10.0F ? -10.0F : f_159_;
	float f_160_ = i_147_ == 0 ? 0.0F : (float) i_147_ / (float) i_149_;
	float f_161_ = i_148_ == 0 ? 0.0F : (float) i_148_ / (float) i_149_;
	float f_162_ = (float) i_145_;
	float f_163_ = (float) i_146_;
	if (i_149_ <= 0)
	    i_149_++;
	for (int i_164_ = 0; i_164_ < i_149_; i_164_++) {
	    if (i_152_ != i_150_ || i_153_ != i_151_) {
		user.count--;
		i_152_ = i_150_;
		i_153_ = i_151_;
		if (user.count <= 0) {
		    user.count = user.countMax;
		    iSize = (int) f;
		    iAlpha = (int) f_157_;
		    getPM();
		    int i_165_ = i_150_ - (user.pW >>> 1);
		    int i_166_ = i_151_ - (user.pW >>> 1);
		    dPen(i_165_, i_166_, 1.0F);
		    if (i_144_ > 0) {
			dBuffer(!user.isDirect, i_165_, i_166_,
				i_165_ + user.pW, i_166_ + user.pW);
			if (i_144_ > 1) {
			    Thread.currentThread();
			    Thread.sleep((long) i_144_);
			}
		    }
		}
	    }
	    i_150_ = (int) (f_162_ += f_160_);
	    i_151_ = (int) (f_163_ += f_161_);
	    f += f_158_;
	    f_157_ += f_159_;
	}
	user.fX = f_162_ - f_160_;
	user.fY = f_163_ - f_161_;
	user.oX = i_152_;
	user.oY = i_153_;
	int i_167_
	    = (int) Math.sqrt((double) info.bPen[iPenM][i_156_].length) / 2;
	int i_168_ = Math.min(i_145_, i_150_) - i_167_;
	int i_169_ = Math.min(i_146_, i_151_) - i_167_;
	int i_170_ = Math.max(i_145_, i_150_) + i_167_ + info.Q;
	int i_171_ = Math.max(i_146_, i_151_) + i_167_ + info.Q;
	if (i_144_ == 0)
	    dBuffer(!user.isDirect, i_168_, i_169_, i_170_, i_171_);
	addD(i_168_, i_169_, i_170_, i_171_);
    }
    
    private final void dFLine2(int i) throws InterruptedException {
	try {
	    int i_172_ = user.pX[0];
	    int i_173_ = user.pY[0];
	    int i_174_ = user.pX[1];
	    int i_175_ = user.pY[1];
	    int i_176_ = user.pX[2];
	    int i_177_ = user.pY[2];
	    int i_178_ = user.pX[3];
	    int i_179_ = user.pY[3];
	    boolean bool = isAnti;
	    float f = user.fX;
	    float f_180_ = user.fY;
	    int i_181_ = (int) f;
	    int i_182_ = (int) f_180_;
	    int i_183_ = i_181_;
	    int i_184_ = i_182_;
	    int i_185_ = user.oX;
	    int i_186_ = user.oY;
	    int i_187_ = user.wait;
	    if (!isCount)
		user.count = 0;
	    int i_188_ = 2 * i_174_;
	    int i_189_ = 2 * i_175_;
	    int i_190_ = 2 * i_172_ - 5 * i_174_ + 4 * i_176_ - i_178_;
	    int i_191_ = 2 * i_173_ - 5 * i_175_ + 4 * i_177_ - i_179_;
	    int i_192_ = -i_172_ + 3 * i_174_ - 3 * i_176_ + i_178_;
	    int i_193_ = -i_173_ + 3 * i_175_ - 3 * i_177_ + i_179_;
	    float f_194_ = (float) iSize;
	    float f_195_ = (float) iAlpha;
	    int i_196_ = ss(i);
	    int i_197_ = sa(i);
	    float f_198_ = (float) (i_196_ - iSize) * 0.25F;
	    f_198_ = f_198_ <= -1.5F ? -1.5F : f_198_ >= 1.5F ? 1.5F : f_198_;
	    float f_199_ = (float) (i_197_ - iAlpha) * 0.25F;
	    int i_200_
		= (int) Math.sqrt((double) Math.max((info.getPenMask()[iPenM]
						     [iSize]).length,
						    (info.getPenMask()[iPenM]
						     [i_196_]).length));
	    int i_201_ = info.Q;
	    for (float f_202_ = 0.0F; f_202_ < 1.0F; f_202_ += 0.25F) {
		float f_203_ = f_202_ * f_202_;
		float f_204_ = f_203_ * f_202_;
		float f_205_ = 0.5F * ((float) i_188_
				       + (float) (-i_172_ + i_176_) * f_202_
				       + (float) i_190_ * f_203_
				       + (float) i_192_ * f_204_);
		float f_206_ = 0.5F * ((float) i_189_
				       + (float) (-i_173_ + i_177_) * f_202_
				       + (float) i_191_ * f_203_
				       + (float) i_193_ * f_204_);
		float f_207_ = Math.max(Math.abs(f_205_ - f),
					Math.abs(f_206_ - f_180_));
		if (!(f_207_ < 1.0F)) {
		    float f_208_ = (f_205_ - f) / f_207_ * 0.25F;
		    f_208_ = (f_208_ <= -1.0F ? -1.0F : f_208_ >= 1.0F ? 1.0F
			      : f_208_);
		    float f_209_ = (f_206_ - f_180_) / f_207_ * 0.25F;
		    f_209_ = (f_209_ <= -1.0F ? -1.0F : f_209_ >= 1.0F ? 1.0F
			      : f_209_);
		    int i_210_ = (int) (f_207_ / 0.25F);
		    if (i_210_ < 16)
			i_210_ = 1;
		    float f_211_ = f_198_ / (float) i_210_;
		    float f_212_ = f_199_ / (float) i_210_;
		    i_181_ = Math.min(Math.min((int) f, (int) f_205_), i_181_);
		    i_182_ = Math.min(Math.min((int) f_180_, (int) f_206_),
				      i_182_);
		    i_183_ = Math.max(Math.max((int) f, (int) f_205_), i_183_);
		    i_184_ = Math.max(Math.max((int) f_180_, (int) f_206_),
				      i_184_);
		    for (int i_213_ = 0; i_213_ < i_210_; i_213_++) {
			int i_214_ = (int) f;
			int i_215_ = (int) f_180_;
			if (i_185_ != i_214_ || i_186_ != i_215_) {
			    i_185_ = i_214_;
			    i_186_ = i_215_;
			    user.count--;
			} else {
			    f_194_ += f_211_;
			    f_195_ += f_212_;
			}
			if (user.count > 0) {
			    f += f_208_;
			    f_180_ += f_209_;
			} else {
			    iSize = (int) f_194_;
			    iAlpha = (int) f_195_;
			    getPM();
			    int i_216_ = user.pW / 2;
			    i_214_ -= i_216_;
			    i_215_ -= i_216_;
			    user.count = user.countMax;
			    if (bool) {
				float f_217_ = f - (float) (int) f;
				float f_218_ = f_180_ - (float) (int) f_180_;
				if (f_217_ < 0.0F) {
				    i_214_--;
				    f_217_++;
				}
				if (f_218_ < 0.0F) {
				    i_215_--;
				    f_218_++;
				}
				if (f_217_ != 1.0F && f_218_ != 1.0F)
				    dPen(i_214_, i_215_,
					 (1.0F - f_217_) * (1.0F - f_218_));
				if (f_217_ != 0.0F)
				    dPen(i_214_ + 1, i_215_,
					 f_217_ * (1.0F - f_218_));
				if (f_218_ != 0.0F)
				    dPen(i_214_, i_215_ + 1,
					 (1.0F - f_217_) * f_218_);
				if (f_217_ != 0.0F && f_218_ != 0.0F)
				    dPen(i_214_ + 1, i_215_ + 1,
					 f_217_ * f_218_);
			    } else
				dPen(i_214_, i_215_, 1.0F);
			    if (i_187_ > 0) {
				dBuffer(!user.isDirect, i_214_, i_215_,
					i_214_ + i_216_ * 2,
					i_215_ + i_216_ * 2);
				if (i_187_ > 1) {
				    Thread.currentThread();
				    Thread.sleep((long) i_187_);
				}
			    }
			    f += f_208_;
			    f_180_ += f_209_;
			}
		    }
		}
	    }
	    user.oX = i_185_;
	    user.oY = i_186_;
	    user.fX = f;
	    user.fY = f_180_;
	    int i_219_ = i_200_ / 2;
	    i_181_ -= i_219_;
	    i_182_ -= i_219_;
	    i_183_ += i_219_ + 1;
	    i_184_ += i_219_ + 1;
	    addD(i_181_, i_182_, i_183_, i_184_);
	    if (user.wait == 0)
		dBuffer(!user.isDirect, i_181_, i_182_, i_183_ + i_201_,
			i_184_ + i_201_);
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	}
    }
    
    private final void dFlush() {
	if (!user.isPre) {
	    if (info.Q != 0) {
		/* empty */
	    }
	    int i = info.W;
	    int i_220_ = info.H;
	    int i_221_ = user.X <= 0 ? 0 : user.X;
	    int i_222_ = user.Y <= 0 ? 0 : user.Y;
	    int i_223_ = user.X2 >= i ? i : user.X2;
	    int i_224_ = user.Y2 >= i_220_ ? i_220_ : user.Y2;
	    if (i_223_ - i_221_ > 0 && i_224_ - i_222_ > 0
		&& iLayer < info.L) {
		byte[] is = info.iMOffs;
		LO lo = info.layers[iLayer];
		switch (iPen) {
		case 17:
		    lo.dLR(i_221_, i_222_, i_223_, i_224_);
		    dCMask(i_221_, i_222_, i_223_, i_224_);
		    break;
		case 18:
		    lo.dUD(i_221_, i_222_, i_223_, i_224_);
		    dCMask(i_221_, i_222_, i_223_, i_224_);
		    break;
		case 19:
		    lo.dR(i_221_, i_222_, i_223_, i_224_, null);
		    dCMask(i_221_, i_222_, i_223_, i_224_);
		    addD(i_221_, i_222_,
			 i_221_ + Math.max(i_223_ - i_221_, i_224_ - i_222_),
			 i_222_ + Math.max(i_223_ - i_221_, i_224_ - i_222_));
		    break;
		case 20: {
		    byte i_225_ = iOffset > 8 ? offset[8] : (byte) 0;
		    LO lo_226_ = info.layers[iLayerSrc];
		    LO lo_227_ = lo;
		    lo_226_.normalize(b255[iAlpha2 & 0xff], i_221_, i_222_,
				      i_223_, i_224_);
		    lo_227_.normalize(b255[iAlpha2 >>> 8], i_221_, i_222_,
				      i_223_, i_224_);
		    if (lo_226_.offset == null)
			dCMask(i_221_, i_222_, i_223_, i_224_);
		    else {
			lo_227_.reserve();
			LO lo_228_ = lo_227_;
			LO lo_229_ = lo_226_;
			if (iLayer < iLayerSrc) {
			    lo_228_ = lo_226_;
			    lo_229_ = lo_227_;
			}
			if (lo_228_.W != 0) {
			    /* empty */
			}
			LO lo_230_ = new LO();
			LO lo_231_ = new LO();
			lo_230_.setField(lo_228_);
			lo_231_.setField(lo_229_);
			lo_228_.iCopy = i_225_;
			lo_229_.reserve();
			lo_228_.dAdd(lo_229_.offset, i_221_, i_222_, i_223_,
				     i_224_, null);
			if (lo_227_ != lo_229_)
			    lo_229_.copyTo(i_221_, i_222_, i_223_, i_224_,
					   lo_228_, i_221_, i_222_, null);
			lo_226_.clear(i_221_, i_222_, i_223_, i_224_);
			lo_226_.isDraw = true;
			dCMask(i_221_, i_222_, i_223_, i_224_);
			lo_228_.setField(lo_230_);
			lo_229_.setField(lo_231_);
		    }
		    break;
		}
		case 9: {
		    lo.reserve();
		    int[] is_232_ = lo.offset;
		    int i_233_ = iAlpha / 10 + 1;
		    i_221_ = i_221_ / i_233_ * i_233_;
		    i_222_ = i_222_ / i_233_ * i_233_;
		    int[] is_234_ = user.argb;
		    for (int i_235_ = i_222_; i_235_ < i_224_;
			 i_235_ += i_233_) {
			for (int i_236_ = i_221_; i_236_ < i_223_;
			     i_236_ += i_233_) {
			    int i_237_ = Math.min(i_233_, i - i_236_);
			    int i_238_ = Math.min(i_233_, i_220_ - i_235_);
			    for (int i_239_ = 0; i_239_ < 4; i_239_++)
				is_234_[i_239_] = 0;
			    int i_240_ = 0;
			    for (int i_241_ = 0; i_241_ < i_238_; i_241_++) {
				for (int i_242_ = 0; i_242_ < i_237_;
				     i_242_++) {
				    int i_243_ = pix(i_236_ + i_242_,
						     i_235_ + i_241_);
				    int i_244_ = ((i_235_ + i_241_) * i
						  + i_236_ + i_242_);
				    for (int i_245_ = 0; i_245_ < 4; i_245_++)
					is_234_[i_245_]
					    += i_243_ >>> i_245_ * 8 & 0xff;
				    i_240_++;
				}
			    }
			    int i_246_
				= (is_234_[3] << 24 | is_234_[2] / i_240_ << 16
				   | is_234_[1] / i_240_ << 8
				   | is_234_[0] / i_240_);
			    for (int i_247_ = i_235_; i_247_ < i_235_ + i_238_;
				 i_247_++) {
				int i_248_ = i * i_247_ + i_236_;
				for (int i_249_ = 0; i_249_ < i_237_;
				     i_249_++) {
				    if (is[i_248_] != 0) {
					is[i_248_] = (byte) 0;
					is_232_[i_248_] = i_246_;
				    }
				    i_248_++;
				}
			    }
			}
		    }
		    break;
		}
		case 3:
		    dCMask(i_221_, i_222_, i_223_, i_224_);
		    break;
		default:
		    if (iHint == 14 || iHint == 9)
			dCMask(i_221_, i_222_, i_223_, i_224_);
		    else {
			lo.reserve();
			int[] is_250_ = lo.offset;
			for (/**/; i_222_ < i_224_; i_222_++) {
			    int i_251_ = i_222_ * i + i_221_;
			    for (int i_252_ = i_221_; i_252_ < i_223_;
				 i_252_++) {
				is_250_[i_251_]
				    = getM(is_250_[i_251_], is[i_251_] & 0xff,
					   i_251_);
				is[i_251_] = (byte) 0;
				i_251_++;
			    }
			}
		    }
		}
		if (user.wait >= 0)
		    dBuffer();
	    }
	}
    }
    
    private final void dCMask(int i, int i_253_, int i_254_, int i_255_) {
	int i_256_ = i_254_ - i;
	int i_257_ = info.W;
	int i_258_ = i_253_ * i_257_ + i;
	byte[] is = info.iMOffs;
	for (int i_259_ = 0; i_259_ < i_256_; i_259_++)
	    is[i_258_ + i_259_] = (byte) 0;
	i_253_++;
	int i_260_ = i_258_;
	i_258_ += i_257_;
	for (/**/; i_253_ < i_255_; i_253_++) {
	    System.arraycopy(is, i_260_, is, i_258_, i_256_);
	    i_258_ += i_257_;
	}
    }
    
    private final boolean dNext() throws InterruptedException {
	if (iSeek >= iOffset)
	    return false;
	int i = user.pX[3] + rPo();
	int i_261_ = user.pY[3] + rPo();
	int i_262_ = iSOB != 0 ? ru() : 0;
	shift(i, i_261_);
	user.iDCount++;
	if (iHint != 11) {
	    if (isAnti)
		dFLine((float) i, (float) i_261_, i_262_);
	    else
		dFLine(i, i_261_, i_262_);
	} else if (user.iDCount >= 2)
	    dFLine2(i_262_);
	return true;
    }
    
    public final void dNext(int i, int i_263_, int i_264_, int i_265_)
	throws InterruptedException, IOException {
	int i_266_ = info.scale;
	if (user.pW != 0) {
	    /* empty */
	}
	i = (i / i_266_ + info.scaleX) * info.Q;
	i_263_ = (i_263_ / i_266_ + info.scaleY) * info.Q;
	if (Math.abs(i - user.pX[3]) + Math.abs(i_263_ - user.pY[3])
	    >= i_265_) {
	    wPo(i - user.pX[3]);
	    wPo(i_263_ - user.pY[3]);
	    shift(i, i_263_);
	    user.iDCount++;
	    if (iSOB != 0)
		info.workOut.write(i_264_);
	    if (iHint == 11) {
		if (user.iDCount >= 2)
		    dFLine2(i_264_);
	    } else if (isAnti)
		dFLine((float) i, (float) i_263_, i_264_);
	    else
		dFLine(i, i_263_, i_264_);
	}
    }
    
    private final void dPen(int i, int i_267_, float f) {
	if (iPen == 3) {
	    if (!user.isPre)
		dPY(i, i_267_);
	} else {
	    dPenM(i, i_267_, f);
	    if (isOver)
		dFlush();
	}
    }
    
    private final void dPenM(int i, int i_268_, float f) {
	boolean bool = false;
	if (info.Q != 0) {
	    /* empty */
	}
	int[] is = getPM();
	int i_269_ = info.W;
	int i_270_ = user.pW;
	int i_271_ = i_270_ * Math.max(-i_268_, 0) + Math.max(-i, 0);
	int i_272_ = Math.min(i + i_270_, i_269_);
	int i_273_ = Math.min(i_268_ + i_270_, info.H);
	if (i_272_ > 0 && i_273_ > 0) {
	    i = i <= 0 ? 0 : i;
	    i_268_ = i_268_ <= 0 ? 0 : i_268_;
	    int[] is_274_ = info.layers[iLayer].offset;
	    byte[] is_275_ = info.iMOffs;
	    for (int i_276_ = i_268_; i_276_ < i_273_; i_276_++) {
		int i_277_ = i_269_ * i_276_ + i;
		int i_278_ = i_271_;
		i_271_ += i_270_;
		for (int i_279_ = i; i_279_ < i_272_; i_279_++) {
		    if (isM(is_274_[i_277_])) {
			i_277_++;
			i_278_++;
		    } else {
			int i_280_ = is_275_[i_277_] & 0xff;
			int i_281_ = is[i_278_++];
			if (i_281_ == 0)
			    i_277_++;
			else {
			    switch (iPen) {
			    case 1:
			    case 20:
				i_281_ = Math.max((int) ((float) i_281_
							 * b255[(255 - i_280_
								 >>> 1)]
							 * f),
						  1);
				is_275_[i_277_++]
				    = (byte) Math.min(i_280_ + i_281_, 255);
				break;
			    case 2:
			    case 5:
			    case 6:
			    case 7:
				if ((i_281_
				     *= /*TYPE_ERROR*/ getTT(i_279_, i_276_))
				    != 0)
				    is_275_[i_277_]
					= (byte) (Math.min
						  ((i_280_
						    + (Math.max
						       ((int) ((float) i_281_
							       * (b255
								  [((255
								     - i_280_)
								    >>> 2)])),
							1))),
						   255));
				i_277_++;
				break;
			    default:
				is_275_[i_277_++]
				    = (byte) Math.max((int) ((float) i_281_
							     * getTT(i_279_,
								     i_276_)),
						      i_280_);
			    }
			}
		    }
		}
	    }
	}
    }
    
    private final void dPY(int i, int i_282_) {
	info.layers[iLayer].reserve();
	boolean bool = false;
	int[] is = getPM();
	int i_283_ = info.W;
	int i_284_ = user.pW;
	int i_285_ = i_284_ * Math.max(-i_282_, 0) + Math.max(-i, 0);
	int i_286_ = i_285_;
	int i_287_ = Math.min(i + i_284_, i_283_);
	int i_288_ = Math.min(i_282_ + i_284_, info.H);
	i = i <= 0 ? 0 : i;
	i_282_ = i_282_ <= 0 ? 0 : i_282_;
	if (i_287_ - i > 0 && i_288_ - i_282_ > 0) {
	    int[] is_289_ = info.layers[iLayer].offset;
	    int i_290_ = 0;
	    int i_291_ = 0;
	    int i_292_ = 0;
	    int i_293_ = 0;
	    int i_294_ = 0;
	    for (int i_295_ = i_282_; i_295_ < i_288_; i_295_++) {
		int i_296_ = i_283_ * i_295_ + i;
		int i_297_ = i_286_;
		i_286_ += i_284_;
		for (int i_298_ = i; i_298_ < i_287_; i_298_++) {
		    int i_299_;
		    int i_300_;
		    if ((i_299_ = is[i_297_++]) == 0
			|| isM(i_300_ = is_289_[i_296_++]))
			i_296_++;
		    else {
			i_291_ += i_300_ >>> 24;
			i_292_ += i_300_ >>> 16 & 0xff;
			i_293_ += i_300_ >>> 8 & 0xff;
			i_294_ += i_300_ & 0xff;
			i_290_++;
		    }
		}
	    }
	    if (i_290_ != 0) {
		i_291_ /= i_290_;
		i_292_ /= i_290_;
		i_293_ /= i_290_;
		i_294_ /= i_290_;
		if (iAlpha > 0) {
		    float f = b255[iAlpha] / 3.0F;
		    int i_301_ = iColor >>> 16 & 0xff;
		    int i_302_ = iColor >>> 8 & 0xff;
		    int i_303_ = iColor & 0xff;
		    i_291_ = Math.max((int) ((float) i_291_
					     + (float) (255 - i_291_) * f),
				      1);
		    int i_304_ = (int) ((float) (i_301_ - i_292_) * f);
		    i_292_ = i_292_ + (i_304_ != 0 ? i_304_ : i_301_ > i_292_
				       ? 1 : i_301_ < i_292_ ? -1 : 0);
		    i_304_ = (int) ((float) (i_302_ - i_293_) * f);
		    i_293_ = i_293_ + (i_304_ != 0 ? i_304_ : i_302_ > i_293_
				       ? 1 : i_302_ < i_293_ ? -1 : 0);
		    i_304_ = (int) ((float) (i_303_ - i_294_) * f);
		    i_294_ = i_294_ + (i_304_ != 0 ? i_304_ : i_303_ > i_294_
				       ? 1 : i_303_ < i_294_ ? -1 : 0);
		}
		i_286_ = i_285_;
		for (int i_305_ = i_282_; i_305_ < i_288_; i_305_++) {
		    int i_306_ = i_283_ * i_305_ + i;
		    int i_307_ = i_286_;
		    i_286_ += i_284_;
		    for (int i_308_ = i; i_308_ < i_287_; i_308_++) {
			int i_309_ = is[i_307_++];
			int i_310_ = is_289_[i_306_];
			float f;
			if (i_309_ == 0 || isM(i_310_)
			    || ((f = getTT(i_308_, i_305_) * b255[i_309_])
				== 0.0F))
			    i_306_++;
			else {
			    int i_311_ = i_310_ >>> 24;
			    int i_312_ = i_310_ >>> 16 & 0xff;
			    int i_313_ = i_310_ >>> 8 & 0xff;
			    int i_314_ = i_310_ & 0xff;
			    i_290_ = (int) ((float) (i_291_ - i_311_) * f);
			    i_311_ = i_311_ + (i_290_ != 0 ? i_290_
					       : i_291_ > i_311_ ? 1
					       : i_291_ < i_311_ ? -1 : 0);
			    i_290_ = (int) ((float) (i_292_ - i_312_) * f);
			    i_312_ = i_312_ + (i_290_ != 0 ? i_290_
					       : i_292_ > i_312_ ? 1
					       : i_292_ < i_312_ ? -1 : 0);
			    i_290_ = (int) ((float) (i_293_ - i_313_) * f);
			    i_313_ = i_313_ + (i_290_ != 0 ? i_290_
					       : i_293_ > i_313_ ? 1
					       : i_293_ < i_313_ ? -1 : 0);
			    i_290_ = (int) ((float) (i_294_ - i_314_) * f);
			    i_314_ = i_314_ + (i_290_ != 0 ? i_290_
					       : i_294_ > i_314_ ? 1
					       : i_294_ < i_314_ ? -1 : 0);
			    is_289_[i_306_++]
				= ((i_311_ << 24) + (i_312_ << 16)
				   + (i_313_ << 8) + i_314_);
			}
		    }
		}
	    }
	}
    }
    
    public final void draw() throws InterruptedException {
	try {
	    if (info == null)
		return;
	    iSeek = 0;
	    switch (iHint) {
	    case 0:
	    case 1:
	    case 11:
		dStart();
		while (dNext()) {
		    /* empty */
		}
		break;
	    case 10:
		dClear();
		break;
	    default:
		dRetouch();
	    }
	} catch (InterruptedException interruptedexception) {
	    /* empty */
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
	dEnd();
    }
    
    private void dRect(int i, int i_315_, int i_316_, int i_317_) {
	int i_318_ = info.W;
	int i_319_ = info.H;
	byte[] is = info.iMOffs;
	byte i_320_ = (byte) iAlpha;
	if (i < 0)
	    i = 0;
	if (i_315_ < 0)
	    i_315_ = 0;
	if (i_316_ > i_318_)
	    i_316_ = i_318_;
	if (i_317_ > i_319_)
	    i_317_ = i_319_;
	if (i < i_316_ && i_315_ < i_317_ && i_320_ != 0) {
	    setD(i, i_315_, i_316_, i_317_);
	    info.layers[iLayer].reserve();
	    int[] is_321_ = info.layers[iLayer].offset;
	    switch (iHint) {
	    case 3:
		for (int i_322_ = i_315_; i_322_ < i_317_; i_322_++) {
		    int i_323_ = i_322_ * i_318_ + i;
		    for (int i_324_ = i; i_324_ < i_316_; i_324_++) {
			if (!isM(is_321_[i_323_]))
			    is[i_323_] = i_320_;
			i_323_++;
		    }
		}
		break;
	    case 4: {
		int i_325_ = i;
		int i_326_ = i_315_;
		int i_327_ = i_316_;
		int i_328_ = i_317_;
		for (int i_329_ = 0; i_329_ < iSize + 1; i_329_++) {
		    int i_330_ = i_318_ * i_326_ + i_325_;
		    int i_331_ = i_318_ * (i_328_ - 1) + i_325_;
		    for (int i_332_ = i_325_; i_332_ < i_327_; i_332_++) {
			if (!isM(is_321_[i_330_]))
			    is[i_330_] = i_320_;
			if (!isM(is_321_[i_331_]))
			    is[i_331_] = i_320_;
			i_330_++;
			i_331_++;
		    }
		    i_330_ = i_318_ * i_326_ + i_325_;
		    i_331_ = i_318_ * i_326_ + i_327_ - 1;
		    for (int i_333_ = i_326_; i_333_ < i_328_; i_333_++) {
			if (!isM(is_321_[i_330_]))
			    is[i_330_] = i_320_;
			if (!isM(is_321_[i_331_]))
			    is[i_331_] = i_320_;
			i_330_ += i_318_;
			i_331_ += i_318_;
		    }
		    i_325_++;
		    i_327_--;
		    i_326_++;
		    i_328_--;
		    if (i_327_ <= i_325_ || i_328_ <= i_326_)
			break;
		}
		break;
	    }
	    case 5:
	    case 6: {
		int i_334_ = i_316_ - i - 1;
		int i_335_ = i_317_ - i_315_ - 1;
		int i_336_ = i_334_ / 2;
		int i_337_ = i_335_ / 2;
		int i_338_ = Math.min(Math.min(iSize + 1, i_336_), i_337_);
		for (int i_339_ = 0; i_339_ < i_338_; i_339_++) {
		    for (float f = 0.0F; f < 7.0F; f += 0.001) {
			int i_340_
			    = (i + i_336_
			       + (int) Math.round(Math.cos((double) f)
						  * (double) (i_336_
							      - i_339_)));
			int i_341_
			    = (i_315_ + i_337_
			       + (int) Math.round(Math.sin((double) f)
						  * (double) (i_337_
							      - i_339_)));
			is[i_318_ * i_341_ + i_340_] = i_320_;
		    }
		}
		if (iHint == 5 && i_336_ > 0 && i_337_ > 0) {
		    int i_342_ = iColor;
		    iColor = i_320_;
		    dFill(is, i, i_315_, i_316_, i_317_);
		    iColor = i_342_;
		}
		for (int i_343_ = i_315_; i_343_ < i_317_; i_343_++) {
		    int i_344_ = i_343_ * i_318_ + i;
		    for (int i_345_ = i; i_345_ < i_316_; i_345_++) {
			if (isM(is_321_[i_344_]))
			    is[i_344_] = (byte) 0;
			i_344_++;
		    }
		}
		break;
	    }
	    }
	    t();
	}
    }
    
    public void dRetouch() throws InterruptedException {
	try {
	    getPM();
	    user.setup(this);
	    int i = user.pW / 2;
	    int i_346_ = info.W;
	    int i_347_ = info.H;
	    LO[] los = info.layers;
	    setD(0, 0, 0, 0);
	    int[] is = user.points;
	    int i_348_ = isText() ? 1 : 4;
	    for (int i_349_ = 0; i_349_ < i_348_ && iSeek < iOffset; i_349_++)
		is[i_349_] = (r2() & 0xffff) << 16 | r2() & 0xffff;
	    int i_350_ = is[0] >> 16;
	    int i_351_ = (short) is[0];
	    switch (iHint) {
	    case 2: {
		int i_352_ = user.wait;
		user.wait = -2;
		dStart(i_350_ + i, i_351_ + i, 0, false, false);
		dBz(is);
		user.wait = i_352_;
		break;
	    }
	    case 8:
	    case 12: {
		String string
		    = new String(offset, iSeek, iOffset - iSeek, "UTF8");
		int i_353_ = string.indexOf('\0');
		dText(string.substring(i_353_ + 1), i_350_, i_351_);
		break;
	    }
	    case 9:
		dCopy(is);
		break;
	    case 7:
		dFill(i_350_, i_351_);
		break;
	    case 14: {
		LO lo = los[iLayer];
		switch (i_351_) {
		case 0:
		    info.swapL(iLayerSrc, iLayer);
		    break;
		case 1:
		    info.setL(is[1]);
		    break;
		case 2:
		    info.delL(iLayerSrc);
		    break;
		case 3:
		    if (iLayer > iLayerSrc) {
			for (int i_354_ = iLayerSrc; i_354_ < iLayer; i_354_++)
			    info.swapL(i_354_, i_354_ + 1);
		    }
		    if (iLayer < iLayerSrc) {
			for (int i_355_ = iLayerSrc; i_355_ > iLayer; i_355_--)
			    info.swapL(i_355_, i_355_ - 1);
		    }
		    break;
		case 6:
		    try {
			Toolkit toolkit = info.component.getToolkit();
			i_350_ = is[1] >> 16;
			short i_356_ = (short) is[1];
			Image image;
			if ((is[2] & 0xff) == 1)
			    image = toolkit.createImage(offset, iSeek,
							iOffset - iSeek);
			else
			    image
				= (toolkit.createImage
				   ((byte[])
				    info.cnf.getRes(new String(offset, iSeek,
							       iOffset - iSeek,
							       "UTF8"))));
			if (image != null) {
			    Awt.wait(image);
			    int i_357_ = image.getWidth(null);
			    int i_358_ = image.getHeight(null);
			    int[] is_359_ = Awt.getPix(image);
			    image.flush();
			    Object object = null;
			    if (i_357_ > 0 && i_358_ > 0)
				los[iLayer].toCopy(i_357_, i_358_, is_359_,
						   i_350_, i_356_);
			}
			break;
		    } catch (Throwable throwable) {
			throwable.printStackTrace();
			break;
		    }
		case 7: {
		    byte i_360_ = offset[4];
		    byte[] is_361_ = new byte[i_360_ * 4];
		    System.arraycopy(offset, 6, is_361_, 0, i_360_ * 4);
		    dFusion(is_361_);
		    break;
		}
		case 5:
		case 8:
		    lo.iAlpha = b255[offset[4] & 0xff];
		    break;
		case 9:
		    lo.iCopy = offset[4];
		    break;
		case 10:
		    lo.name = new String(offset, 4, iOffset - 4, "UTF8");
		    break;
		}
		setD(0, 0, i_346_, i_347_);
		break;
	    }
	    default:
		dRect(i_350_, i_351_, is[1] >> 16, (short) is[1]);
	    }
	    if (isOver)
		dFlush();
	    if (user.wait >= 0)
		dBuffer();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void dStart() {
	try {
	    int i = r2();
	    int i_362_ = r2();
	    user.setup(this);
	    info.layers[iLayer].reserve();
	    int i_363_ = iSOB != 0 ? ru() : 0;
	    if (iSOB != 0) {
		iSize = ss(i_363_);
		iAlpha = sa(i_363_);
	    }
	    memset(user.pX, i);
	    memset(user.pY, i_362_);
	    int i_364_ = user.pW / 2;
	    setD(i - i_364_ - 1, i_362_ - i_364_ - 1, i + i_364_,
		 i_362_ + i_364_);
	    user.fX = (float) i;
	    user.fY = (float) i_362_;
	    if (iHint != 11 && !isAnti)
		dFLine(i, i_362_, i_363_);
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	} catch (InterruptedException interruptedexception) {
	    /* empty */
	}
    }
    
    public void dStart(int i, int i_365_, int i_366_, boolean bool,
		       boolean bool_367_) {
	try {
	    user.setup(this);
	    info.layers[iLayer].reserve();
	    iSize = ss(i_366_);
	    iAlpha = sa(i_366_);
	    user.setup(this);
	    if (bool_367_) {
		int i_368_ = info.scale;
		i = (i / i_368_ + info.scaleX) * info.Q;
		i_365_ = (i_365_ / i_368_ + info.scaleY) * info.Q;
	    }
	    if (bool) {
		ByteStream bytestream = getWork();
		bytestream.w((long) i, 2);
		bytestream.w((long) i_365_, 2);
		if (iSOB != 0)
		    bytestream.write(i_366_);
	    }
	    memset(user.pX, i);
	    memset(user.pY, i_365_);
	    int i_369_ = user.pW / 2;
	    setD(i - i_369_ - 1, i_365_ - i_369_ - 1, i + i_369_,
		 i_365_ + i_369_);
	    user.fX = (float) i;
	    user.fY = (float) i_365_;
	    if (iHint != 11 && !isAnti)
		dFLine(i, i_365_, i_366_);
	} catch (IOException ioexception) {
	    ioexception.printStackTrace();
	} catch (InterruptedException interruptedexception) {
	    interruptedexception.printStackTrace();
	}
    }
    
    private void dText(String string, int i, int i_370_) {
	try {
	    int i_371_ = info.W;
	    int i_372_ = info.H;
	    int[] is = info.layers[iLayer].offset;
	    byte[] is_373_ = info.iMOffs;
	    float f = b255[iAlpha];
	    if (f != 0.0F) {
		Font font = getFont(iSize);
		FontMetrics fontmetrics = info.component.getFontMetrics(font);
		if (string != null && string.length() > 0) {
		    info.layers[iLayer].reserve();
		    boolean bool = iHint == 8;
		    int i_374_ = fontmetrics.getMaxAdvance();
		    int i_375_ = (fontmetrics.getMaxAscent()
				  + fontmetrics.getMaxDescent()
				  + fontmetrics.getLeading() + 2);
		    int i_376_ = (fontmetrics.getMaxAscent()
				  + fontmetrics.getLeading() / 2 + 1);
		    int i_377_ = string.length();
		    int i_378_;
		    int i_379_;
		    if (bool) {
			i_378_ = i_374_ * (i_377_ + 1) + 2;
			i_379_ = i_375_;
		    } else {
			i_374_ = fontmetrics.getMaxAdvance();
			i_378_ = i_374_ + 2;
			i_379_ = (i_375_ + iCount) * (i_377_ + 1);
		    }
		    i_378_ = Math.min(i_378_, i_371_);
		    i_379_ = Math.min(i_379_, i_372_);
		    setD(i, i_370_, i + i_378_, i_370_ + i_379_);
		    Image image = info.component.createImage(i_378_, i_379_);
		    Graphics graphics = image.getGraphics();
		    graphics.setFont(font);
		    graphics.setColor(Color.black);
		    graphics.fillRect(0, 0, i_378_, i_379_);
		    graphics.setColor(Color.blue);
		    if (bool)
			graphics.drawString(string, 1, i_376_);
		    else {
			int i_380_ = i_376_;
			for (int i_381_ = 0; i_381_ < i_377_; i_381_++) {
			    graphics.drawString
				(String.valueOf(string.charAt(i_381_)), 1,
				 i_380_);
			    i_380_ += i_375_ + iCount;
			}
		    }
		    graphics.dispose();
		    Object object = null;
		    Object object_382_ = null;
		    Object object_383_ = null;
		    int[] is_384_ = Awt.getPix(image);
		    image.flush();
		    Object object_385_ = null;
		    boolean bool_386_ = false;
		    int i_387_ = Math.min(i_371_ - i, i_378_);
		    int i_388_ = Math.min(i_372_ - i_370_, i_379_);
		    for (int i_389_ = 0; i_389_ < i_388_; i_389_++) {
			int i_390_ = i_389_ * i_378_;
			int i_391_ = (i_389_ + i_370_) * i_371_ + i;
			for (int i_392_ = 0; i_392_ < i_387_; i_392_++) {
			    if (!isM(is[i_391_]))
				is_373_[i_391_]
				    = (byte) (int) ((float) (is_384_[i_390_]
							     & 0xff)
						    * f);
			    i_390_++;
			    i_391_++;
			}
		    }
		    setD(i, i_370_, i + i_378_, i_370_ + i_379_);
		    t();
		}
	    }
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }
    
    private final int fu(int i, int i_393_, int i_394_) {
	if (i_394_ == 0)
	    return i;
	int i_395_ = i >>> 24;
	int i_396_ = i_395_ + (int) ((float) i_394_ * b255[255 - i_395_]);
	float f = b255[Math.min((int) ((float) i_394_ * b255d[i_396_]), 255)];
	int i_397_ = i >>> 16 & 0xff;
	int i_398_ = i >>> 8 & 0xff;
	int i_399_ = i & 0xff;
	return (i_396_ << 24
		| i_397_ + (int) ((float) ((i_393_ >>> 16 & 0xff) - i_397_)
				  * f) << 16
		| i_398_ + (int) ((float) ((i_393_ >>> 8 & 0xff) - i_398_)
				  * f) << 8
		| i_399_ + (int) ((float) ((i_393_ & 0xff) - i_399_) * f));
    }
    
    public final void get(OutputStream outputstream, ByteStream bytestream,
			  M m_400_) {
	try {
	    bytestream.reset();
	    int i = 0;
	    boolean bool = false;
	    int i_401_ = getFlag(m_400_);
	    int i_402_ = i_401_ >>> 8 & 0xff;
	    int i_403_ = i_401_ & 0xff;
	    bytestream.write(i_401_ >>> 16);
	    bytestream.write(i_402_);
	    bytestream.write(i_403_);
	    if ((i_402_ & 0x1) != 0) {
		i = iHint;
		bool = true;
	    }
	    if ((i_402_ & 0x2) != 0) {
		if (bool)
		    bytestream.write(i << 4 | iPenM);
		else
		    i = iPenM;
		bool = !bool;
	    }
	    if ((i_402_ & 0x4) != 0) {
		if (bool)
		    bytestream.write(i << 4 | iMask);
		else
		    i = iMask;
		bool = !bool;
	    }
	    if (bool)
		bytestream.write(i << 4);
	    if ((i_402_ & 0x8) != 0)
		bytestream.write(iPen);
	    if ((i_402_ & 0x10) != 0)
		bytestream.write(iTT);
	    if ((i_402_ & 0x20) != 0)
		bytestream.write(iLayer);
	    if ((i_402_ & 0x40) != 0)
		bytestream.write(iLayerSrc);
	    if ((i_403_ & 0x1) != 0)
		bytestream.write(iAlpha);
	    if ((i_403_ & 0x2) != 0)
		bytestream.w((long) iColor, 3);
	    if ((i_403_ & 0x4) != 0)
		bytestream.w((long) iColorMask, 3);
	    if ((i_403_ & 0x8) != 0)
		bytestream.write(iSize);
	    if ((i_403_ & 0x10) != 0)
		bytestream.write(iCount);
	    if ((i_403_ & 0x20) != 0)
		bytestream.w((long) iSA, 2);
	    if ((i_403_ & 0x40) != 0)
		bytestream.w((long) iSS, 2);
	    if (iPen == 20)
		bytestream.w2(iAlpha2);
	    if (isText()) {
		if (strHint == null)
		    bytestream.w2(0);
		else {
		    bytestream.w2(strHint.length);
		    bytestream.write(strHint);
		}
	    }
	    if (offset != null && iOffset > 0)
		bytestream.write(offset, 0, iOffset);
	    outputstream.write(bytestream.size() >>> 8);
	    outputstream.write(bytestream.size() & 0xff);
	    bytestream.writeTo(outputstream);
	} catch (IOException ioexception) {
	    ioexception.printStackTrace();
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	}
    }
    
    private final int getFlag(M m_404_) {
	int i = 0;
	if (isAllL)
	    i |= 0x1;
	if (isAFix)
	    i |= 0x2;
	if (isAnti)
	    i |= 0x10;
	if (isCount)
	    i |= 0x8;
	if (isOver)
	    i |= 0x4;
	i |= iSOB << 6;
	int i_405_ = i << 16;
	if (m_404_ == null)
	    return i_405_ | 0xffff;
	i = 0;
	if (iHint != m_404_.iHint)
	    i |= 0x1;
	if (iPenM != m_404_.iPenM)
	    i |= 0x2;
	if (iMask != m_404_.iMask)
	    i |= 0x4;
	if (iPen != m_404_.iPen)
	    i |= 0x8;
	if (iTT != m_404_.iTT)
	    i |= 0x10;
	if (iLayer != m_404_.iLayer)
	    i |= 0x20;
	if (iLayerSrc != m_404_.iLayerSrc)
	    i |= 0x40;
	i_405_ |= i << 8;
	i = 0;
	if (iAlpha != m_404_.iAlpha)
	    i |= 0x1;
	if (iColor != m_404_.iColor)
	    i |= 0x2;
	if (iColorMask != m_404_.iColorMask)
	    i |= 0x4;
	if (iSize != m_404_.iSize)
	    i |= 0x8;
	if (iCount != m_404_.iCount)
	    i |= 0x10;
	if (iSA != m_404_.iSA)
	    i |= 0x20;
	if (iSS != m_404_.iSS)
	    i |= 0x40;
	return i_405_ | i;
    }
    
    public Image getImage(int i, int i_406_, int i_407_, int i_408_,
			  int i_409_) {
	i_406_ = Math.round((float) (i_406_ / info.scale)) + info.scaleX;
	i_407_ = Math.round((float) (i_407_ / info.scale)) + info.scaleY;
	i_408_ /= info.scale;
	i_409_ /= info.scale;
	int i_410_ = info.Q;
	if (i_410_ <= 1)
	    return (info.component.createImage
		    (new MemoryImageSource(i_408_, i_409_,
					   info.layers[i].offset,
					   i_407_ * info.W + i_406_, info.W)));
	Image image = (info.component.createImage
		       (new MemoryImageSource(i_408_ * i_410_, i_409_ * i_410_,
					      info.layers[i].offset,
					      (i_407_ * i_410_ * info.W
					       + i_406_ * i_410_),
					      info.W)));
	Image image_411_ = image.getScaledInstance(i_408_, i_409_, 2);
	image.flush();
	return image_411_;
    }
    
    private final int getM(int i, int i_412_, int i_413_) {
	if (i_412_ == 0)
	    return i;
	switch (iPen) {
	default:
	    return fu(i, iColor, i_412_);
	case 4:
	case 5: {
	    int i_414_ = i >>> 24;
	    int i_415_ = i_414_ - (int) ((float) i_414_ * b255[i_412_]);
	    return i_415_ == 0 ? 16777215 : i_415_ << 24 | i & 0xffffff;
	}
	case 6:
	case 11: {
	    int i_416_ = i >>> 24;
	    int i_417_ = i >>> 16 & 0xff;
	    int i_418_ = i >>> 8 & 0xff;
	    int i_419_ = i & 0xff;
	    if (iColor != 0) {
		/* empty */
	    }
	    float f = b255[i_412_];
	    return ((i_416_ << 24)
		    + (Math.min(i_417_ + (int) ((float) i_417_ * f), 255)
		       << 16)
		    + (Math.min(i_418_ + (int) ((float) i_418_ * f), 255) << 8)
		    + Math.min(i_419_ + (int) ((float) i_419_ * f), 255));
	}
	case 7: {
	    int i_420_ = i >>> 24;
	    int i_421_ = i >>> 16 & 0xff;
	    int i_422_ = i >>> 8 & 0xff;
	    int i_423_ = i & 0xff;
	    if (iColor != 0) {
		/* empty */
	    }
	    float f = b255[i_412_];
	    return ((i_420_ << 24)
		    + (Math.max(i_421_ - (int) ((float) (255 - i_421_) * f), 0)
		       << 16)
		    + (Math.max(i_422_ - (int) ((float) (255 - i_422_) * f), 0)
		       << 8)
		    + Math.max(i_423_ - (int) ((float) (255 - i_423_) * f),
			       0));
	}
	case 8: {
	    if (i >>> 24 != 0) {
		/* empty */
	    }
	    if ((i >>> 16 & 0xff) != 0) {
		/* empty */
	    }
	    if ((i >>> 8 & 0xff) != 0) {
		/* empty */
	    }
	    if ((i & 0xff) != 0) {
		/* empty */
	    }
	    if (iColor != 0) {
		/* empty */
	    }
	    float f = b255[i_412_];
	    int[] is = user.argb;
	    int[] is_424_ = info.layers[iLayer].offset;
	    int i_425_ = info.W;
	    for (int i_426_ = 0; i_426_ < 4; i_426_++)
		is[i_426_] = 0;
	    int i_427_ = i_413_ % i_425_;
	    i_413_
		= i_413_ + (i_427_ == 0 ? 1 : i_427_ == i_425_ - 1 ? -1 : 0);
	    i_413_ = i_413_ + (i_413_ < i_425_ ? i_425_
			       : i_413_ > i_425_ * (info.H - 1) ? -i_425_ : 0);
	    for (int i_428_ = -1; i_428_ < 2; i_428_++) {
		for (int i_429_ = -1; i_429_ < 2; i_429_++) {
		    i_427_ = is_424_[i_413_ + i_429_ + i_428_ * i_425_];
		    if (i_427_ >>> 24 != 0) {
			/* empty */
		    }
		    for (int i_430_ = 0; i_430_ < 4; i_430_++)
			is[i_430_] += i_427_ >>> (i_430_ << 3) & 0xff;
		}
	    }
	    for (int i_431_ = 0; i_431_ < 4; i_431_++) {
		i_427_ = i >>> (i_431_ << 3) & 0xff;
		is[i_431_]
		    = i_427_ + (int) ((float) (is[i_431_] / 9 - i_427_) * f);
	    }
	    return is[3] << 24 | is[2] << 16 | is[1] << 8 | is[0];
	}
	case 9:
	case 20:
	    if (i_412_ == 0)
		return i;
	    return i_412_ << 24 | 0xff0000;
	case 10:
	    return i_412_ << 24 | iColor;
	}
    }
    
    public final byte[] getOffset() {
	return offset;
    }
    
    private final int[] getPM() {
	if (isText() || iHint >= 3 && iHint <= 6)
	    return null;
	int[] is = user.p;
	if (user.pM != iPenM || user.pA != iAlpha || user.pS != iSize) {
	    int[][] is_432_ = info.bPen[iPenM];
	    int[] is_433_ = is_432_[iSize];
	    int i = is_433_.length;
	    if (is == null || is.length < i)
		is = new int[i];
	    float f = b255[iAlpha];
	    for (int i_434_ = 0; i_434_ < i; i_434_++)
		is[i_434_] = (int) ((float) is_433_[i_434_] * f);
	    user.pW = (int) Math.sqrt((double) i);
	    user.pM = iPenM;
	    user.pA = iAlpha;
	    user.pS = iSize;
	    user.p = is;
	    user.countMax
		= (iCount >= 0 ? iCount
		   : (int) ((float) user.pW
			    / (float) Math.sqrt((double) is_432_
							 [(is_432_.length
							   - 1)].length)
			    * (float) -iCount));
	    user.count = Math.min(user.countMax, user.count);
	}
	return is;
    }
    
    private final float getTT(int i, int i_435_) {
	if (iTT == 0)
	    return 1.0F;
	if (iTT < 12)
	    return (float) (isTone(iTT - 1, i, i_435_) ? 0 : 1);
	int i_436_ = user.pTTW;
	return user.pTT[i_435_ % i_436_ * i_436_ + i % i_436_];
    }
    
    private final ByteStream getWork() {
	info.workOut.reset();
	return info.workOut;
    }
    
    private final boolean isM(int i) {
	if (iMask == 0)
	    return false;
	i &= 0xffffff;
	return (iMask == 1 ? iColorMask == i : iMask == 2 ? iColorMask != i
		: false);
    }
    
    public static final boolean isTone(int i, int i_437_, int i_438_) {
	switch (i) {
	default:
	    break;
	case 10:
	    if ((i_437_ + 3) % 4 == 0 && (i_438_ + 2) % 4 == 0)
		return true;
	    break;
	case 9:
	    if ((i_437_ + 1) % 4 == 0 && (i_438_ + 2) % 4 == 0)
		break;
	    /* fall through */
	case 8:
	    if (i_437_ % 2 != 0 && (i_438_ + 1) % 2 != 0)
		return true;
	    break;
	case 7:
	    if ((i_437_ + 2) % 4 == 0 && (i_438_ + 3) % 4 == 0)
		break;
	    /* fall through */
	case 6:
	    if (i_437_ % 4 == 0 && (i_438_ + 1) % 4 == 0)
		break;
	    /* fall through */
	case 5:
	    if ((i_437_ + 1) % 2 != (i_438_ + 1) % 2)
		return true;
	    break;
	case 4:
	    if ((i_437_ + 1) % 4 == 0 && (i_438_ + 3) % 4 == 0)
		break;
	    /* fall through */
	case 3:
	    if (i_437_ % 2 != 0 || i_438_ % 2 != 0)
		return true;
	    break;
	case 2:
	    if ((i_437_ + 2) % 4 == 0 && (i_438_ + 4) % 4 == 0)
		break;
	    /* fall through */
	case 1:
	    if ((i_437_ + 2) % 4 == 0 && (i_438_ + 2) % 4 == 0)
		break;
	    /* fall through */
	case 0:
	    if (i_437_ % 4 != 0 || i_438_ % 4 != 0)
		return true;
	}
	return false;
    }
    
    private int[] loadIm(Object object, boolean bool) {
	try {
	    Component component = info.component;
	    Image image = component.getToolkit()
			      .createImage((byte[]) info.cnf.getRes(object));
	    info.cnf.remove(object);
	    Awt.wait(image);
	    int[] is = Awt.getPix(image);
	    int i = is.length;
	    image.flush();
	    Object object_439_ = null;
	    if (bool) {
		for (int i_440_ = 0; i_440_ < i; i_440_++)
		    is[i_440_] = is[i_440_] & 0xff ^ 0xff;
	    } else {
		for (int i_441_ = 0; i_441_ < i; i_441_++)
		    is[i_441_] &= 0xff;
	    }
	    return is;
	} catch (RuntimeException runtimeexception) {
	    return null;
	}
    }
    
    public final void m_paint(int i, int i_442_, int i_443_, int i_444_) {
	int i_445_ = info.scale;
	int i_446_ = info.Q;
	i = (i / i_445_ + info.scaleX) * i_446_;
	i_442_ = (i_442_ / i_445_ + info.scaleY) * i_446_;
	i_443_ = i_443_ / i_445_ * i_446_;
	i_444_ = i_444_ / i_445_ * i_446_;
	dBuffer(false, i, i_442_, i + i_443_, i_442_ + i_444_);
    }
    
    public final void memset(float[] fs, float f) {
	int i = fs.length >>> 1;
	for (int i_447_ = 0; i_447_ < i; i_447_++)
	    fs[i_447_] = f;
	System.arraycopy(fs, 0, fs, i - 1, i);
	fs[i + i - 1] = f;
    }
    
    public final void memset(int[] is, int i) {
	int i_448_ = is.length >>> 1;
	for (int i_449_ = 0; i_449_ < i_448_; i_449_++)
	    is[i_449_] = i;
	System.arraycopy(is, 0, is, i_448_ - 1, i_448_);
	is[i_448_ + i_448_ - 1] = i;
    }
    
    public final void memset(byte[] is, byte i) {
	int i_450_ = is.length >>> 1;
	for (int i_451_ = 0; i_451_ < i_450_; i_451_++)
	    is[i_451_] = i;
	System.arraycopy(is, 0, is, i_450_ - 1, i_450_);
	is[i_450_ + i_450_ - 1] = i;
    }
    
    public final Image mkLPic(int[] is, int i, int i_452_, int i_453_,
			      int i_454_, int i_455_) {
	i *= i_455_;
	i_452_ *= i_455_;
	i_453_ *= i_455_;
	i_454_ *= i_455_;
	boolean bool = is == null;
	int i_456_ = info.L;
	LO[] los = info.layers;
	if (bool)
	    is = user.buffer;
	memset(is, 16777215);
	for (int i_457_ = 0; i_457_ < i_456_; i_457_++)
	    los[i_457_].draw(is, i, i_452_, i + i_453_, i_452_ + i_454_,
			     i_453_);
	if (bool)
	    user.raster.newPixels(user.image, i_453_, i_454_, i_455_);
	else
	    user.raster.scale(is, i_453_, i_454_, i_455_);
	return user.image;
    }
    
    private final Image mkMPic(int i, int i_458_, int i_459_, int i_460_,
			       int i_461_) {
	i *= i_461_;
	i_458_ *= i_461_;
	i_459_ *= i_461_;
	i_460_ *= i_461_;
	int[] is = user.buffer;
	int i_462_ = info.L;
	LO[] los = info.layers;
	memset(is, 16777215);
	for (int i_463_ = 0; i_463_ < i_462_; i_463_++) {
	    if (i_463_ == iLayer) {
		byte[] is_464_ = info.iMOffs;
		int[] is_465_ = los[i_463_].offset;
		int i_466_ = info.W;
		float f = los[i_463_].iAlpha;
		if (is_465_ != null) {
		    switch (los[i_463_].iCopy) {
		    case 1:
			for (int i_467_ = 0; i_467_ < i_460_; i_467_++) {
			    int i_468_ = i_466_ * (i_467_ + i_458_) + i;
			    int i_469_ = i_459_ * i_467_;
			    for (int i_470_ = 0; i_470_ < i_459_; i_470_++) {
				int i_471_ = is[i_469_];
				int i_472_
				    = getM(is_465_[i_468_],
					   is_464_[i_468_] & 0xff, i_468_);
				float f_473_ = b255[i_472_ >>> 24] * f;
				if (f_473_ > 0.0F)
				    is[i_469_]
					= ((((i_471_ >>> 16 & 0xff)
					     - (int) ((b255
						       [i_471_ >>> 16 & 0xff])
						      * ((float) (((i_472_
								    >>> 16)
								   & 0xff)
								  ^ 0xff)
							 * f_473_)))
					    << 16)
					   + (((i_471_ >>> 8 & 0xff)
					       - (int) ((b255
							 [i_471_ >>> 8 & 0xff])
							* ((float) (((i_472_
								      >>> 8)
								     & 0xff)
								    ^ 0xff)
							   * f_473_)))
					      << 8)
					   + ((i_471_ & 0xff)
					      - (int) (b255[i_471_ & 0xff]
						       * ((float) ((i_472_
								    & 0xff)
								   ^ 0xff)
							  * f_473_))));
				i_469_++;
				i_468_++;
			    }
			}
			break;
		    case 2:
			for (int i_474_ = 0; i_474_ < i_460_; i_474_++) {
			    int i_475_ = i_466_ * (i_474_ + i_458_) + i;
			    int i_476_ = i_459_ * i_474_;
			    for (int i_477_ = 0; i_477_ < i_459_; i_477_++) {
				int i_478_ = is[i_476_];
				int i_479_
				    = getM(is_465_[i_475_],
					   is_464_[i_475_] & 0xff, i_475_);
				float f_480_ = b255[i_479_ >>> 24] * f;
				i_479_ ^= 0xffffff;
				int i_481_ = i_478_ >>> 16 & 0xff;
				int i_482_ = i_478_ >>> 8 & 0xff;
				int i_483_ = i_478_ & 0xff;
				is[i_476_++]
				    = (f_480_ == 1.0F ? i_479_
				       : ((i_481_
					   + (int) ((float) ((i_479_ >>> 16
							      & 0xff)
							     - i_481_)
						    * f_480_)) << 16
					  | (i_482_
					     + (int) ((float) ((i_479_ >>> 8
								& 0xff)
							       - i_482_)
						      * f_480_)) << 8
					  | i_483_ + (int) ((float) ((i_479_
								      & 0xff)
								     - i_483_)
							    * f_480_)));
				i_475_++;
			    }
			}
			break;
		    default:
			for (int i_484_ = 0; i_484_ < i_460_; i_484_++) {
			    int i_485_ = i_466_ * (i_484_ + i_458_) + i;
			    int i_486_ = i_459_ * i_484_;
			    for (int i_487_ = 0; i_487_ < i_459_; i_487_++) {
				int i_488_ = is[i_486_];
				int i_489_
				    = getM(is_465_[i_485_],
					   is_464_[i_485_] & 0xff, i_485_);
				float f_490_ = b255[i_489_ >>> 24] * f;
				if (f_490_ == 1.0F)
				    is[i_486_++] = i_489_;
				else {
				    int i_491_ = i_488_ >>> 16 & 0xff;
				    int i_492_ = i_488_ >>> 8 & 0xff;
				    int i_493_ = i_488_ & 0xff;
				    is[i_486_++]
					= ((i_491_
					    + (int) ((float) ((i_489_ >>> 16
							       & 0xff)
							      - i_491_)
						     * f_490_)) << 16
					   | (i_492_
					      + (int) ((float) ((i_489_ >>> 8
								 & 0xff)
								- i_492_)
						       * f_490_)) << 8
					   | i_493_ + (int) ((float) ((i_489_
								       & 0xff)
								      - i_493_)
							     * f_490_));
				}
				i_485_++;
			    }
			}
		    }
		}
	    } else
		los[i_463_].draw(is, i, i_458_, i + i_459_, i_458_ + i_460_,
				 i_459_);
	}
	user.raster.newPixels(user.image, i_459_, i_460_, i_461_);
	return user.image;
    }
    
    public Info newInfo(Applet applet, Component component, Res res) {
	if (this.info != null)
	    return this.info;
	this.info = new Info();
	this.info.cnf = res;
	this.info.component = component;
	Info info = this.info;
	M m_494_ = this.info.m;
	float f = 3.1415927F;
	for (int i = 1; i < 256; i++) {
	    b255[i] = (float) i / 255.0F;
	    b255d[i] = 255.0F / (float) i;
	}
	b255[0] = 0.0F;
	b255d[0] = 0.0F;
	int[][][] is = this.info.bPen;
	boolean bool = false;
	int i = 1;
	int i_495_ = 255;
	m_494_.iAlpha = 255;
	set(m_494_);
	int[][] is_496_ = new int[23][];
	for (int i_497_ = 0; i_497_ < 23; i_497_++) {
	    int i_498_ = i * i;
	    if (i <= 6) {
		int[] is_499_;
		is_496_[i_497_] = is_499_ = new int[i_498_];
		int i_500_ = i;
		for (int i_501_ = 0; i_501_ < i_498_; i_501_++)
		    is_499_[i_501_]
			= (i_501_ < i || i_498_ - i_501_ < i || i_501_ % i == 0
			   || i_501_ % i == i - 1) ? i_495_ : m_494_.iAlpha;
		if (i >= 3)
		    is_499_[0] = is_499_[i - 1] = is_499_[i * (i - 1)]
			= is_499_[i_498_ - 1] = 0;
	    } else {
		int i_502_ = i + 1;
		int[] is_503_;
		is_496_[i_497_] = is_503_ = new int[i_502_ * i_502_];
		int i_504_ = (i - 1) / 2;
		int i_505_
		    = (int) ((float) Math.round(2.0F * f * (float) i_504_)
			     * 3.0F);
		for (int i_506_ = 0; i_506_ < i_505_; i_506_++) {
		    int i_507_
			= (Math.min
			   ((i_504_
			     + (int) Math.round((double) i_504_
						* Math.cos((double) i_506_))),
			    i));
		    int i_508_
			= (Math.min
			   ((i_504_
			     + (int) Math.round((double) i_504_
						* Math.sin((double) i_506_))),
			    i));
		    is_503_[i_508_ * i_502_ + i_507_] = i_495_;
		}
		info.W = info.H = i_502_;
		dFill(is_503_, 0, 0, i_502_, i_502_);
	    }
	    i = i + (i_497_ <= 7 ? 1 : i_497_ < 18 ? 2 : 4);
	}
	is[0] = is_496_;
	m_494_.iAlpha = 255;
	is_496_ = new int[32][];
	is_496_[0] = new int[] { 128 };
	is_496_[1] = new int[] { 255 };
	is_496_[2] = new int[] { 0, 128, 0, 128, 255, 128, 0, 128, 0 };
	is_496_[3] = new int[] { 128, 174, 128, 174, 255, 174, 128, 174, 128 };
	is_496_[4] = new int[] { 174, 255, 174, 255, 255, 255, 174, 255, 174 };
	is_496_[5] = new int[9];
	memset(is_496_[5], 255);
	is_496_[6] = new int[] { 0, 128, 128, 0, 128, 255, 255, 128, 128, 255,
				 255, 128, 0, 128, 128, 0 };
	int[] is_509_ = is_496_[7] = new int[16];
	memset(is_509_, 255);
	is_509_[0] = is_509_[3] = is_509_[15] = is_509_[12] = 128;
	memset(is_496_[8] = new int[16], 255);
	i = 3;
	for (int i_510_ = 9; i_510_ < 32; i_510_++) {
	    int i_511_ = i + 3;
	    float f_512_ = (float) i / 2.0F;
	    is_496_[i_510_] = is_509_ = new int[i_511_ * i_511_];
	    int i_513_ = ((int) ((float) Math.round(2.0F * f * f_512_)
				 * (float) (2 + i_510_ / 16))
			  + i_510_ * 2);
	    for (int i_514_ = 0; i_514_ < i_513_; i_514_++) {
		float f_516_;
		int i_515_
		    = (int) (f_516_
			     = (f_512_ + 1.5F
				+ f_512_ * (float) Math.cos((double) i_514_)));
		float f_518_;
		int i_517_
		    = (int) (f_518_
			     = (f_512_ + 1.5F
				+ f_512_ * (float) Math.sin((double) i_514_)));
		float f_519_ = f_516_ - (float) i_515_;
		float f_520_ = f_518_ - (float) i_517_;
		int i_521_ = i_517_ * i_511_ + i_515_;
		is_509_[i_521_] += (int) ((1.0F - f_519_) * 255.0F);
		is_509_[i_521_ + 1] += (int) (f_519_ * 255.0F);
		is_509_[i_521_ + i_511_] += (int) ((1.0F - f_520_) * 255.0F);
		is_509_[i_521_ + i_511_ + 1] += (int) (f_520_ * 255.0F);
	    }
	    int i_522_ = i_511_ * i_511_;
	    for (int i_523_ = 0; i_523_ < i_522_; i_523_++)
		is_509_[i_523_] = Math.min(is_509_[i_523_], 255);
	    i += 2;
	    info.W = info.H = i_511_;
	    dFill(is_509_, 0, 0, i_511_, i_511_);
	}
	is[1] = is_496_;
	set((M) null);
	m_494_.set((M) null);
	if (res != null) {
	    for (int i_524_ = 0; i_524_ < 16; i_524_++) {
		int i_525_;
		for (i_525_ = 0;
		     (res.get((Object) ("pm" + i_524_ + '/' + i_525_ + ".gif"))
		      != null);
		     i_525_++) {
		    /* empty */
		}
		if (i_525_ > 0) {
		    is[i_524_] = new int[i_525_][];
		    for (int i_526_ = 0; i_526_ < i_525_; i_526_++)
			is[i_524_][i_526_]
			    = loadIm("pm" + i_524_ + '/' + i_526_ + ".gif",
				     true);
		}
	    }
	    this.info.bTT = new float[res.getP("tt_size", 31)][];
	}
	String string = applet.getParameter("tt.zip");
	if (string != null && string.length() > 0)
	    this.info.dirTT = string;
	return this.info;
    }
    
    public User newUser(Component component) {
	if (user == null) {
	    user = new User();
	    if (color_model == null)
		color_model = new DirectColorModel(24, 16711680, 65280, 255);
	    user.raster = new SRaster(color_model, user.buffer, 128, 128);
	    user.image = component.createImage(user.raster);
	}
	return user;
    }
    
    public final int pix(int i, int i_527_) {
	if (!isAllL)
	    return info.layers[iLayer].getPixel(i, i_527_);
	int i_528_ = info.L;
	int i_529_ = 0;
	int i_530_ = 16777215;
	if (info.W * i_527_ + i != 0) {
	    /* empty */
	}
	for (int i_531_ = 0; i_531_ < i_528_; i_531_++) {
	    int i_532_ = info.layers[i_531_].getPixel(i, i_527_);
	    float f = b255[i_532_ >>> 24];
	    if (f != 0.0F) {
		if (f == 1.0F) {
		    i_530_ = i_532_;
		    i_529_ = 255;
		}
		i_529_ += (float) (255 - i_529_) * f;
		int i_533_ = 0;
		for (int i_534_ = 16; i_534_ >= 0; i_534_ -= 8) {
		    int i_535_ = i_530_ >>> i_534_ & 0xff;
		    i_533_
			|= i_535_ + (int) ((float) ((i_532_ >>> i_534_ & 0xff)
						    - i_535_)
					   * f) << i_534_;
		}
		i_530_ = i_533_;
	    }
	}
	return i_529_ << 24 | i_530_;
    }
    
    private final byte r() {
	if (iSeek >= iOffset)
	    return (byte) 0;
	return offset[iSeek++];
    }
    
    private final int r(byte[] is, int i, int i_536_) {
	int i_537_ = 0;
	for (int i_538_ = i_536_ - 1; i_538_ >= 0; i_538_--)
	    i_537_ |= (is[i++] & 0xff) << i_538_ * 8;
	return i_537_;
    }
    
    private final short r2() {
	return (short) ((ru() << 8) + ru());
    }
    
    public void reset(boolean bool) {
	byte[] is = info.iMOffs;
	int i = info.W;
	int i_539_ = Math.max(user.X, 0);
	int i_540_ = Math.max(user.Y, 0);
	int i_541_ = Math.min(user.X2, i);
	int i_542_ = Math.min(user.Y2, info.H);
	for (int i_543_ = i_540_; i_543_ < i_542_; i_543_++) {
	    int i_544_ = i_539_ + i_543_ * i;
	    for (int i_545_ = i_539_; i_545_ < i_541_; i_545_++)
		is[i_544_++] = (byte) 0;
	}
	if (bool)
	    dBuffer(false, i_539_, i_540_, i_541_, i_542_);
	setD(0, 0, 0, 0);
    }
    
    private final int rPo() {
	int i = r();
	return i != -128 ? (int) i : r2();
    }
    
    private final int ru() {
	return r() & 0xff;
    }
    
    private final int s(int i, int i_546_, int i_547_) {
	byte[] is = info.iMOffs;
	int i_548_ = info.W - 1;
	for (int i_549_ = (i_548_ + 1) * i_547_ + i_546_;
	     (i_546_ < i_548_ && pix(i_546_ + 1, i_547_) == i
	      && is[i_549_ + 1] == 0);
	     i_546_++)
	    i_549_++;
	return i_546_;
    }
    
    private final int sa(int i) {
	if ((iSOB & 0x1) == 0)
	    return iAlpha;
	int i_550_ = iSA & 0xff;
	return i_550_ + (int) (b255[(iSA >>> 8) - i_550_] * (float) i);
    }
    
    public final int set(byte[] is, int i) {
	int i_551_ = (is[i++] & 0xff) << 8 | is[i++] & 0xff;
	int i_552_ = i;
	if (i_551_ <= 2)
	    return i_551_ + 2;
	try {
	    int i_553_ = 0;
	    boolean bool = false;
	    int i_554_ = is[i++] & 0xff;
	    int i_555_ = is[i++] & 0xff;
	    int i_556_ = is[i++] & 0xff;
	    isAllL = (i_554_ & 0x1) != 0;
	    isAFix = (i_554_ & 0x2) != 0;
	    isOver = (i_554_ & 0x4) != 0;
	    isCount = (i_554_ & 0x8) != 0;
	    isAnti = (i_554_ & 0x10) != 0;
	    iSOB = i_554_ >>> 6;
	    if ((i_555_ & 0x1) != 0) {
		i_553_ = is[i++] & 0xff;
		bool = true;
		iHint = i_553_ >>> 4;
	    }
	    if ((i_555_ & 0x2) != 0) {
		if (!bool) {
		    i_553_ = is[i++] & 0xff;
		    iPenM = i_553_ >>> 4;
		} else
		    iPenM = i_553_ & 0xf;
		bool = !bool;
	    }
	    if ((i_555_ & 0x4) != 0) {
		if (!bool) {
		    i_553_ = is[i++] & 0xff;
		    iMask = i_553_ >>> 4;
		} else
		    iMask = i_553_ & 0xf;
		bool = !bool;
	    }
	    if ((i_555_ & 0x8) != 0)
		iPen = is[i++] & 0xff;
	    if ((i_555_ & 0x10) != 0)
		iTT = is[i++] & 0xff;
	    if ((i_555_ & 0x20) != 0)
		iLayer = is[i++] & 0xff;
	    if ((i_555_ & 0x40) != 0)
		iLayerSrc = is[i++] & 0xff;
	    if ((i_556_ & 0x1) != 0)
		iAlpha = is[i++] & 0xff;
	    if ((i_556_ & 0x2) != 0) {
		iColor = r(is, i, 3);
		i += 3;
	    }
	    if ((i_556_ & 0x4) != 0) {
		iColorMask = r(is, i, 3);
		i += 3;
	    }
	    if ((i_556_ & 0x8) != 0)
		iSize = is[i++] & 0xff;
	    if ((i_556_ & 0x10) != 0)
		iCount = is[i++];
	    if ((i_556_ & 0x20) != 0) {
		iSA = r(is, i, 2);
		i += 2;
	    }
	    if ((i_556_ & 0x40) != 0) {
		iSS = r(is, i, 2);
		i += 2;
	    }
	    if (iPen == 20) {
		iAlpha2 = r(is, i, 2);
		i += 2;
	    }
	    if (isText()) {
		int i_557_ = r(is, i, 2);
		i += 2;
		if (i_557_ == 0)
		    strHint = null;
		else {
		    strHint = new byte[i_557_];
		    System.arraycopy(is, i, strHint, 0, i_557_);
		    i += i_557_;
		}
	    }
	    i_552_ = i_551_ - (i - i_552_);
	    if (i_552_ > 0) {
		if (offset == null || offset.length < i_552_)
		    offset = new byte[i_552_];
		iOffset = i_552_;
		System.arraycopy(is, i, offset, 0, i_552_);
	    } else
		iOffset = 0;
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	    iOffset = 0;
	}
	return i_551_ + 2;
    }
    
    public final void set(String string) {
	try {
	    if (string != null && string.length() != 0) {
		Field[] fields = this.getClass().getDeclaredFields();
		int i = string.indexOf('@');
		if (i < 0)
		    i = string.length();
		int i_558_;
		for (int i_559_ = 0; i_559_ < i; i_559_ = i_558_ + 1) {
		    i_558_ = string.indexOf('=', i_559_);
		    if (i_558_ == -1)
			break;
		    String string_560_ = string.substring(i_559_, i_558_);
		    i_559_ = i_558_ + 1;
		    i_558_ = string.indexOf(';', i_559_);
		    if (i_558_ < 0)
			i_558_ = i;
		    try {
			for (int i_561_ = 0; i_561_ < fields.length;
			     i_561_++) {
			    Field field = fields[i_561_];
			    if (field.getName().equals(string_560_)) {
				String string_562_
				    = string.substring(i_559_, i_558_);
				Class var_class = field.getType();
				if (var_class.equals(Integer.TYPE))
				    field.setInt(this,
						 Integer
						     .parseInt(string_562_));
				else if (var_class.equals(Boolean.TYPE))
				    field.setBoolean
					(this,
					 Integer.parseInt(string_562_) != 0);
				else
				    field.set(this, string_562_);
				break;
			    }
			}
		    } catch (NumberFormatException numberformatexception) {
			/* empty */
		    } catch (IllegalAccessException illegalaccessexception) {
			/* empty */
		    }
		}
		if (i != string.length()) {
		    ByteStream bytestream = getWork();
		    for (int i_563_ = i + 1; i_563_ < string.length();
			 i_563_ += 2)
			bytestream.write(Character.digit(string.charAt(i_563_),
							 16) << 4
					 | Character.digit(string.charAt(i_563_
									 + 1),
							   16));
		    offset = bytestream.toByteArray();
		    iOffset = offset.length;
		}
	    }
	} catch (Throwable throwable) {
	    /* empty */
	}
    }
    
    public final void set(M m_564_) {
	if (m_564_ == null)
	    m_564_ = mgDef;
	iHint = m_564_.iHint;
	iPen = m_564_.iPen;
	iPenM = m_564_.iPenM;
	iTT = m_564_.iTT;
	iMask = m_564_.iMask;
	iSize = m_564_.iSize;
	iSS = m_564_.iSS;
	iCount = m_564_.iCount;
	isOver = m_564_.isOver;
	isCount = m_564_.isCount;
	isAFix = m_564_.isAFix;
	isAnti = m_564_.isAnti;
	isAllL = m_564_.isAllL;
	iAlpha = m_564_.iAlpha;
	iAlpha2 = m_564_.iAlpha2;
	iSA = m_564_.iSA;
	iColor = m_564_.iColor;
	iColorMask = m_564_.iColorMask;
	iLayer = m_564_.iLayer;
	iLayerSrc = m_564_.iLayerSrc;
	iSOB = m_564_.iSOB;
	strHint = m_564_.strHint;
	iOffset = 0;
    }
    
    public final int set(ByteStream bytestream) {
	return set(bytestream.getBuffer(), 0);
    }
    
    public void setRetouch(int[] is, byte[] is_565_, int i, boolean bool) {
	try {
	    int i_566_ = 4;
	    int i_567_ = info.scale;
	    int i_568_ = info.Q;
	    int i_569_ = info.scaleX;
	    int i_570_ = info.scaleY;
	    getPM();
	    int i_571_ = user.pW / 2;
	    int i_572_ = iHint == 2 ? i_571_ : 0;
	    int[] is_573_ = user.points;
	    switch (iHint) {
	    case 8:
	    case 12:
		i_566_ = 1;
		break;
	    case 2:
	    case 7:
		break;
	    case 9:
		i_566_ = 3;
		break;
	    case 14:
		break;
	    case 10:
		i_566_ = 0;
		break;
	    default:
		i_566_ = 2;
	    }
	    if (is != null)
		i_566_ = Math.min(i_566_, is.length);
	    for (int i_574_ = 0; i_574_ < i_566_; i_574_++) {
		int i_575_ = is[i_574_] >> 16;
		int i_576_ = (short) is[i_574_];
		if (bool) {
		    i_575_ = (i_575_ / i_567_ + i_569_) * i_568_ - i_572_;
		    i_576_ = (i_576_ / i_567_ + i_570_) * i_568_ - i_572_;
		}
		is_573_[i_574_] = i_575_ << 16 | i_576_ & 0xffff;
	    }
	    ByteStream bytestream = getWork();
	    for (int i_577_ = 0; i_577_ < i_566_; i_577_++)
		bytestream.w((long) is_573_[i_577_], 4);
	    if (is_565_ != null && i > 0)
		bytestream.write(is_565_, 0, i);
	    offset = bytestream.writeTo(offset, 0);
	    iOffset = bytestream.size();
	    bytestream.reset();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private final void addD(int i, int i_578_, int i_579_, int i_580_) {
	user.addRect(i, i_578_, i_579_, i_580_);
    }
    
    private final void setD(int i, int i_581_, int i_582_, int i_583_) {
	user.setRect(i, i_581_, i_582_, i_583_);
    }
    
    public void setInfo(Info info) {
	this.info = info;
    }
    
    public void setUser(User user) {
	this.user = user;
    }
    
    private final void shift(int i, int i_584_) {
	System.arraycopy(user.pX, 1, user.pX, 0, 3);
	System.arraycopy(user.pY, 1, user.pY, 0, 3);
	user.pX[3] = i;
	user.pY[3] = i_584_;
    }
    
    private final int ss(int i) {
	if ((iSOB & 0x2) == 0)
	    return iSize;
	int i_585_ = iSS & 0xff;
	return (int) (((float) i_585_ + b255[(iSS >>> 8) - i_585_] * (float) i)
		      * user.pV);
    }
    
    private final void t() {
	if (iTT != 0) {
	    byte[] is = info.iMOffs;
	    int i = info.W;
	    int i_586_ = user.X;
	    int i_587_ = user.Y;
	    int i_588_ = user.X2;
	    int i_589_ = user.Y2;
	    for (int i_590_ = i_587_; i_590_ < i_589_; i_590_++) {
		int i_591_ = i * i_590_ + i_586_;
		for (int i_592_ = i_586_; i_592_ < i_588_; i_592_++)
		    is[i_591_] = (byte) (int) ((float) (is[i_591_++] & 0xff)
					       * getTT(i_592_, i_590_));
	    }
	}
    }
    
    private final void wPo(int i) throws IOException {
	ByteStream bytestream = info.workOut;
	if (i > 127 || i < -127) {
	    bytestream.write(-128);
	    bytestream.w((long) i, 2);
	} else
	    bytestream.write(i);
    }
    
    public boolean isText() {
	if (iHint != 8 && iHint != 12)
	    return false;
	return true;
    }
    
    public Font getFont(int i) {
	try {
	    if (strHint != null)
		return Font.decode(new String(strHint, "UTF8") + i);
	} catch (IOException ioexception) {
	    /* empty */
	}
	return new Font("sansserif", 0, iSize);
    }
    
    public static float[] getb255() {
	return b255;
    }
}
