/* LO - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat;

public class LO
{
    public int W;
    public int H;
    int offX = 0;
    int offY = 0;
    public int[] offset = null;
    public String name;
    public int iCopy = 0;
    public float iAlpha = 1.0F;
    public boolean isDraw = false;
    public static int iL = 0;
    
    public static LO getLO(int i, int i_0_) {
	LO lo = new LO(i, i_0_);
	return lo;
    }
    
    public LO() {
	this(0, 0);
    }
    
    public LO(int i, int i_1_) {
	W = i;
	H = i_1_;
    }
    
    public void setSize(int i, int i_2_) {
	if ((i != W || i_2_ != H) && offset != null) {
	    int i_3_ = Math.min(i, W);
	    int i_4_ = Math.min(i_2_, H);
	    int[] is = new int[i * i_2_];
	    for (int i_5_ = 0; i_5_ < is.length; i_5_++)
		is[i_5_] = 16777215;
	    for (int i_6_ = 0; i_6_ < i_4_; i_6_++)
		System.arraycopy(offset, i_6_ * W, is, i_6_ * i, i_3_);
	    offset = is;
	}
	W = i;
	H = i_2_;
    }
    
    public void reserve() {
	if (offset == null) {
	    int i = W * H;
	    offset = new int[i];
	    clear();
	}
    }
    
    public final void draw(int[] is, int i, int i_7_, int i_8_, int i_9_,
			   int i_10_) {
	if (offset != null && !(iAlpha <= 0.0F)) {
	    float[] fs = M.getb255();
	    float f = iAlpha;
	    int i_11_ = W;
	    i = Math.max(i, 0);
	    i_7_ = Math.max(i_7_, 0);
	    i_8_ = Math.min(W, i_8_);
	    i_9_ = Math.min(H, i_9_);
	    int i_12_ = i_8_ - i;
	    int i_13_ = i_9_ - i_7_;
	    int i_14_ = 0;
	    int i_15_ = i_7_ * i_11_ + i;
	    switch (iCopy) {
	    case 1:
		for (int i_16_ = 0; i_16_ < i_13_; i_16_++) {
		    for (int i_17_ = 0; i_17_ < i_12_; i_17_++) {
			int i_18_ = offset[i_15_ + i_17_];
			int i_19_ = is[i_14_ + i_17_];
			float f_20_ = fs[i_18_ >>> 24] * f;
			int i_21_ = i_19_ >>> 16 & 0xff;
			int i_22_ = i_19_ >>> 8 & 0xff;
			int i_23_ = i_19_ & 0xff;
			if (f_20_ > 0.0F)
			    is[i_14_ + i_17_]
				= ((((i_19_ >>> 16 & 0xff)
				     - (int) (fs[i_19_ >>> 16 & 0xff]
					      * ((float) (i_18_ >>> 16 & 0xff
							  ^ 0xff)
						 * f_20_)))
				    << 16)
				   + (((i_19_ >>> 8 & 0xff)
				       - (int) (fs[i_19_ >>> 8 & 0xff]
						* ((float) (i_18_ >>> 8 & 0xff
							    ^ 0xff)
						   * f_20_)))
				      << 8)
				   + ((i_19_ & 0xff)
				      - (int) (fs[i_19_ & 0xff]
					       * ((float) (i_18_ & 0xff ^ 0xff)
						  * f_20_))));
		    }
		    i_14_ += i_10_;
		    i_15_ += i_11_;
		}
		break;
	    case 2:
		for (int i_24_ = 0; i_24_ < i_13_; i_24_++) {
		    for (int i_25_ = 0; i_25_ < i_12_; i_25_++) {
			int i_26_ = offset[i_15_ + i_25_] ^ 0xffffff;
			int i_27_ = is[i_14_ + i_25_];
			float f_28_ = fs[i_26_ >>> 24] * f;
			if (!(f_28_ <= 0.0F)) {
			    int i_29_ = i_27_ >>> 16 & 0xff;
			    int i_30_ = i_27_ >>> 8 & 0xff;
			    int i_31_ = i_27_ & 0xff;
			    is[i_14_ + i_25_]
				= (f_28_ == 1.0F ? i_26_
				   : (i_29_ + (int) ((float) ((i_26_ >>> 16
							       & 0xff)
							      - i_29_)
						     * f_28_) << 16
				      | i_30_ + (int) ((float) ((i_26_ >>> 8
								 & 0xff)
								- i_30_)
						       * f_28_) << 8
				      | i_31_ + (int) ((float) ((i_26_ & 0xff)
								- i_31_)
						       * f_28_)));
			}
		    }
		    i_14_ += i_10_;
		    i_15_ += i_11_;
		}
		break;
	    default:
		for (int i_32_ = 0; i_32_ < i_13_; i_32_++) {
		    for (int i_33_ = 0; i_33_ < i_12_; i_33_++) {
			int i_34_ = offset[i_15_ + i_33_];
			int i_35_ = is[i_14_ + i_33_];
			float f_36_ = fs[i_34_ >>> 24] * f;
			if (!(f_36_ <= 0.0F)) {
			    int i_37_ = i_35_ >>> 16 & 0xff;
			    int i_38_ = i_35_ >>> 8 & 0xff;
			    int i_39_ = i_35_ & 0xff;
			    is[i_14_ + i_33_]
				= (f_36_ == 1.0F ? i_34_
				   : (i_37_ + (int) ((float) ((i_34_ >>> 16
							       & 0xff)
							      - i_37_)
						     * f_36_) << 16
				      | i_38_ + (int) ((float) ((i_34_ >>> 8
								 & 0xff)
								- i_38_)
						       * f_36_) << 8
				      | i_39_ + (int) ((float) ((i_34_ & 0xff)
								- i_39_)
						       * f_36_)));
			}
		    }
		    i_14_ += i_10_;
		    i_15_ += i_11_;
		}
	    }
	}
    }
    
    public void drawAlpha(int[] is, int i, int i_40_, int i_41_, int i_42_,
			  int i_43_) {
	if (offset != null && !(iAlpha <= 0.0F)) {
	    float[] fs = M.getb255();
	    float f = iAlpha;
	    int i_44_ = W;
	    i = Math.max(i, 0);
	    i_40_ = Math.max(i_40_, 0);
	    i_41_ = Math.min(W, i_41_);
	    i_42_ = Math.min(H, i_42_);
	    int i_45_ = i_41_ - i;
	    int i_46_ = i_42_ - i_40_;
	    int i_47_ = 0;
	    int i_48_ = i_40_ * i_44_ + i;
	    int[] is_49_ = offset;
	    for (int i_50_ = 0; i_50_ < i_46_; i_50_++) {
		for (int i_51_ = 0; i_51_ < i_45_; i_51_++) {
		    int i_52_
			= (int) ((float) (is_49_[i_48_ + i_51_] >>> 24) * f);
		    int i_53_ = (int) ((float) (is[i_47_ + i_51_] >>> 24)
				       * fs[255 - i_52_]);
		    is[i_47_ + i_51_]
			= i_52_ + i_53_ << 24 | is[i_47_ + i_51_] & 0xffffff;
		}
		i_47_ += i_43_;
		i_48_ += i_44_;
	    }
	}
    }
    
    public void dAdd(int[] is, int i, int i_54_, int i_55_, int i_56_,
		     int[] is_57_) {
	if (offset != null) {
	    float[] fs = M.getb255();
	    int[] is_58_ = offset;
	    int i_59_ = W;
	    int i_60_ = i_59_;
	    i = Math.max(i, 0);
	    i_54_ = Math.max(i_54_, 0);
	    i_55_ = Math.min(W, i_55_);
	    i_56_ = Math.min(H, i_56_);
	    int i_61_ = i_55_ - i;
	    int i_62_ = i_56_ - i_54_;
	    int i_63_ = i_54_ * i_60_ + i;
	    int i_64_ = i_54_ * i_59_ + i;
	    int i_65_ = 0;
	    switch (iCopy) {
	    case 1:
		for (int i_66_ = 0; i_66_ < i_62_; i_66_++) {
		    for (int i_67_ = 0; i_67_ < i_61_; i_67_++) {
			int i_68_ = is_58_[i_64_ + i_67_];
			int i_69_ = is[i_63_ + i_67_];
			int i_70_ = i_68_ >>> 24;
			int i_71_ = i_69_ >>> 24;
			int i_72_
			    = i_70_ + (int) ((float) i_71_ * fs[255 - i_70_]);
			int i_73_ = is_57_[i_65_ + i_67_] & 0xffffff;
			int i_74_ = i_69_ >>> 16 & 0xff;
			int i_75_ = i_69_ >>> 8 & 0xff;
			int i_76_ = i_69_ & 0xff;
			float f = 1.0F - (float) i_71_ / (float) i_72_;
			i_74_ += (int) ((float) ((i_73_ >>> 16 & 0xff) - i_74_)
					* f);
			i_75_ += (int) ((float) ((i_73_ >>> 8 & 0xff) - i_75_)
					* f);
			i_76_ += (int) ((float) ((i_73_ & 0xff) - i_76_) * f);
			i_69_ = i_74_ << 16 | i_75_ << 8 | i_76_;
			if (i_71_ <= 0)
			    is[i_63_ + i_67_] = i_68_;
			else {
			    f = fs[i_70_] + fs[255 - i_70_] * fs[255 - i_71_];
			    is[i_63_ + i_67_]
				= (i_72_ << 24
				   | ((i_69_ >>> 16 & 0xff)
				      - (int) (fs[i_69_ >>> 16 & 0xff]
					       * ((float) (i_68_ >>> 16 & 0xff
							   ^ 0xff)
						  * f))) << 16
				   | ((i_69_ >>> 8 & 0xff)
				      - (int) (fs[i_69_ >>> 8 & 0xff]
					       * ((float) (i_68_ >>> 8 & 0xff
							   ^ 0xff)
						  * f))) << 8
				   | ((i_69_ & 0xff)
				      - (int) (fs[i_69_ & 0xff]
					       * ((float) (i_68_ & 0xff ^ 0xff)
						  * f))));
			}
		    }
		    i_63_ += i_60_;
		    i_64_ += i_59_;
		    i_65_ += i_60_;
		}
		break;
	    case 2:
		for (int i_77_ = 0; i_77_ < i_62_; i_77_++) {
		    for (int i_78_ = 0; i_78_ < i_61_; i_78_++) {
			int i_79_ = is_58_[i_64_ + i_78_];
			int i_80_ = is[i_63_ + i_78_];
			int i_81_ = i_79_ >>> 24;
			int i_82_
			    = (int) ((float) (i_80_ >>> 24) * fs[255 - i_81_]);
			int i_83_ = i_81_ + i_82_;
			i_79_ ^= 0xffffff;
			if (i_83_ == 0)
			    is[i_63_ + i_78_] = 16777215;
			else {
			    float f = (float) i_81_ / (float) i_83_;
			    int i_84_ = i_80_ >>> 16 & 0xff;
			    int i_85_ = i_80_ >>> 8 & 0xff;
			    int i_86_ = i_80_ & 0xff;
			    is[i_63_ + i_78_]
				= (f == 1.0F ? i_79_
				   : (i_83_ << 24
				      | i_84_ + (int) ((float) ((i_79_ >>> 16
								 & 0xff)
								- i_84_)
						       * f) << 16
				      | i_85_ + (int) ((float) ((i_79_ >>> 8
								 & 0xff)
								- i_85_)
						       * f) << 8
				      | i_86_ + (int) ((float) ((i_79_ & 0xff)
								- i_86_)
						       * f)));
			}
		    }
		    i_63_ += i_60_;
		    i_64_ += i_59_;
		}
		break;
	    default:
		for (int i_87_ = 0; i_87_ < i_62_; i_87_++) {
		    for (int i_88_ = 0; i_88_ < i_61_; i_88_++) {
			int i_89_ = is_58_[i_64_ + i_88_];
			int i_90_ = is[i_63_ + i_88_];
			int i_91_ = i_89_ >>> 24;
			int i_92_
			    = (int) ((float) (i_90_ >>> 24) * fs[255 - i_91_]);
			int i_93_ = i_91_ + i_92_;
			if (i_93_ == 0)
			    is[i_63_ + i_88_] = 16777215;
			else {
			    float f = (float) i_91_ / (float) i_93_;
			    int i_94_ = i_90_ >>> 16 & 0xff;
			    int i_95_ = i_90_ >>> 8 & 0xff;
			    int i_96_ = i_90_ & 0xff;
			    is[i_63_ + i_88_]
				= (f == 1.0F ? i_89_
				   : (i_93_ << 24
				      | i_94_ + (int) ((float) ((i_89_ >>> 16
								 & 0xff)
								- i_94_)
						       * f) << 16
				      | i_95_ + (int) ((float) ((i_89_ >>> 8
								 & 0xff)
								- i_95_)
						       * f) << 8
				      | i_96_ + (int) ((float) ((i_89_ & 0xff)
								- i_96_)
						       * f)));
			}
		    }
		    i_63_ += i_60_;
		    i_64_ += i_59_;
		}
	    }
	}
    }
    
    public void normalize(float f) {
	normalize(f, 0, 0, W, H);
    }
    
    public void normalize(float f, int i, int i_97_, int i_98_, int i_99_) {
	if (offset != null) {
	    int i_100_ = i_98_ - i;
	    for (/**/; i_97_ < i_99_; i_97_++) {
		int i_101_ = i_97_ * W + i;
		for (int i_102_ = 0; i_102_ < i_100_; i_102_++) {
		    int i_103_ = offset[i_101_];
		    offset[i_101_] = ((int) ((float) (i_103_ >>> 24) * f) << 24
				      | i_103_ & 0xffffff);
		    i_101_++;
		}
	    }
	}
    }
    
    public final int getPixel(int i, int i_104_) {
	if (offset == null || i < 0 || i_104_ < 0 || i >= W || i_104_ >= H)
	    return 16777215;
	return offset[i_104_ * W + i];
    }
    
    public final void setPixel(int i, int i_105_, int i_106_) {
	if (i_105_ >= 0 && i_106_ >= 0 && i_105_ < W && i_106_ < H
	    && offset != null)
	    offset[i_106_ * W + i_105_] = i;
    }
    
    public final void clear() {
	clear(0, 0, W, H);
    }
    
    public void clear(int i, int i_107_, int i_108_, int i_109_) {
	if (offset != null) {
	    int i_110_ = i_107_ * W + i;
	    int i_111_ = i_108_ - i;
	    for (int i_112_ = 0; i_112_ < i_111_; i_112_++)
		offset[i_110_ + i_112_] = 16777215;
	    i_110_ += W;
	    for (i_107_++; i_107_ < i_109_; i_107_++) {
		System.arraycopy(offset, i_110_ - W, offset, i_110_, i_111_);
		i_110_ += W;
	    }
	}
    }
    
    public void copyTo(int i, int i_113_, int i_114_, int i_115_, LO lo_116_,
		       int i_117_, int i_118_, int[] is) {
	int[] is_119_ = lo_116_.offset;
	if (offset != null || is_119_ != null) {
	    if (is_119_ == null) {
		lo_116_.reserve();
		is_119_ = lo_116_.offset;
	    }
	    copyTo(i, i_113_, i_114_, i_115_, is_119_, i_117_, i_118_,
		   lo_116_.W, lo_116_.H, is);
	}
    }
    
    public final void copyTo(int i, int i_120_, int i_121_, int i_122_,
			     int[] is, int i_123_, int i_124_, int i_125_,
			     int i_126_, int[] is_127_) {
	i = Math.max(i, 0);
	i_120_ = Math.max(i_120_, 0);
	i_121_ = Math.min(i_121_, W);
	i_122_ = Math.min(i_122_, H);
	int i_128_ = Math.min(i_121_ - i, i_125_);
	int i_129_ = Math.min(i_122_ - i_120_, i_126_);
	if (i_123_ < 0) {
	    i_128_ += i_123_;
	    i -= i_123_;
	    i_123_ = 0;
	}
	if (i_124_ < 0) {
	    i_129_ += i_124_;
	    i_120_ -= i_124_;
	    i_124_ = 0;
	}
	if (i_123_ + i_128_ >= i_125_)
	    i_128_ = i_125_ - i_123_;
	if (i_124_ + i_129_ >= i_126_)
	    i_129_ = i_126_ - i_124_;
	if (i_128_ > 0 && i_129_ > 0) {
	    if (offset == null) {
		for (int i_130_ = i_124_; i_130_ < i_124_ + i_129_; i_130_++) {
		    int i_131_ = i_130_ * i_125_ + i_123_;
		    for (int i_132_ = 0; i_132_ < i_128_; i_132_++)
			is[i_131_++] = 16777215;
		}
	    } else {
		int i_133_ = i_120_ * W + i;
		if (offset != is) {
		    int i_134_ = i_124_ * i_125_ + i_123_;
		    for (int i_135_ = 0; i_135_ < i_129_; i_135_++) {
			System.arraycopy(offset, i_133_, is, i_134_, i_128_);
			i_133_ += W;
			i_134_ += i_125_;
		    }
		} else {
		    int i_136_ = i_128_ * i_129_;
		    if (is_127_ == null || is_127_.length < i_136_)
			is_127_ = new int[i_136_];
		    for (int i_137_ = 0; i_137_ < i_129_; i_137_++) {
			System.arraycopy(offset, i_133_, is_127_,
					 i_137_ * i_128_, i_128_);
			i_133_ += W;
		    }
		    i_133_ = i_124_ * i_125_ + i_123_;
		    for (int i_138_ = 0; i_138_ < i_129_; i_138_++) {
			System.arraycopy(is_127_, i_138_ * i_128_, is, i_133_,
					 i_128_);
			i_133_ += i_125_;
		    }
		}
	    }
	}
    }
    
    public void setLayer(LO lo_139_) {
	setField(lo_139_);
	setImage(lo_139_);
    }
    
    public void setImage(LO lo_140_) {
	int i = lo_140_.W;
	int i_141_ = lo_140_.H;
	int i_142_ = i * i_141_;
	if (offset != null || lo_140_.offset != null) {
	    reserve();
	    if (lo_140_.offset == null) {
		for (int i_143_ = 0; i_143_ < i_142_; i_143_++)
		    offset[i_143_] = 16777215;
	    } else
		System.arraycopy(lo_140_.offset, 0, offset, 0, i_142_);
	    W = i;
	    H = i_141_;
	}
    }
    
    public void setField(LO lo_144_) {
	name = lo_144_.name;
	iAlpha = lo_144_.iAlpha;
	iCopy = lo_144_.iCopy;
	offX = lo_144_.offX;
	offY = lo_144_.offY;
	isDraw = lo_144_.isDraw;
    }
    
    public void makeName(String string) {
	name = string + iL++;
    }
    
    public void toCopy(int i, int i_145_, int[] is, int i_146_, int i_147_) {
	if (offset == null)
	    reserve();
	int i_148_ = 0;
	int i_149_ = 0;
	int i_150_ = i;
	if (i_145_ != 0) {
	    /* empty */
	}
	if (i_146_ < 0) {
	    i_148_ = -i_146_;
	    i += i_146_;
	    i_146_ = 0;
	}
	if (i_146_ + i > W)
	    i = W - i_146_;
	if (i_147_ < 0) {
	    i_149_ = -i_147_;
	    i_145_ += i_147_;
	    i_147_ = 0;
	}
	if (i_147_ + i_145_ > H)
	    i_145_ = H - i_147_;
	if (i > 0 && i_145_ > 0) {
	    int i_151_ = i_149_ * i_150_ + i_148_;
	    int i_152_ = i_147_ * W + i_146_;
	    for (int i_153_ = 0; i_153_ < i_145_; i_153_++) {
		System.arraycopy(is, i_151_, offset, i_152_, i);
		i_151_ += i;
		i_152_ += W;
	    }
	}
    }
    
    public void dLR(int i, int i_154_, int i_155_, int i_156_) {
	if (offset != null) {
	    for (/**/; i_154_ < i_156_; i_154_++) {
		int i_157_ = i_155_ - 1;
		for (int i_158_ = i; i_158_ < i_157_; i_158_++) {
		    int i_159_ = getPixel(i_158_, i_154_);
		    setPixel(getPixel(i_157_, i_154_), i_158_, i_154_);
		    setPixel(i_159_, i_157_, i_154_);
		    i_157_--;
		    if (i_158_ + 1 >= i_157_)
			break;
		}
	    }
	}
    }
    
    public void dUD(int i, int i_160_, int i_161_, int i_162_) {
	if (offset != null) {
	    for (/**/; i < i_161_; i++) {
		int i_163_ = i_162_ - 1;
		for (int i_164_ = i_160_; i_164_ < i_163_; i_164_++) {
		    int i_165_ = getPixel(i, i_164_);
		    setPixel(getPixel(i, i_163_), i, i_164_);
		    setPixel(i_165_, i, i_163_);
		    i_163_--;
		    if (i_164_ + 1 >= i_163_)
			break;
		}
	    }
	}
    }
    
    public void dR(int i, int i_166_, int i_167_, int i_168_, int[] is) {
	if (offset != null) {
	    int i_169_ = W;
	    int i_170_ = H;
	    int i_171_ = i_167_ - i;
	    int i_172_ = i_168_ - i_166_;
	    Math.max(i_171_, i_172_);
	    int i_173_ = i_166_ * i_169_ + i;
	    int i_174_ = i_171_ * i_172_;
	    if (is == null || is.length < i_174_)
		is = new int[i_174_];
	    for (int i_175_ = 0; i_175_ < i_172_; i_175_++)
		System.arraycopy(offset, i_173_ + i_169_ * i_175_, is,
				 i_171_ * i_175_, i_171_);
	    for (int i_176_ = 0; i_176_ < i_171_; i_176_++)
		offset[i_173_ + i_176_] = 16777215;
	    for (int i_177_ = 1; i_177_ < i_172_; i_177_++)
		System.arraycopy(offset, i_173_, offset,
				 i_173_ + i_177_ * i_169_, i_171_);
	    i_174_ = i_169_ * i_170_;
	    for (int i_178_ = 0; i_178_ < i_172_; i_178_++) {
		int i_179_ = i_171_ * i_178_;
		int i_180_ = i_173_ + i_172_ - i_178_;
		for (int i_181_ = 0; i_181_ < i_171_; i_181_++) {
		    int i_182_ = i_181_ + i;
		    if (i_182_ <= i_169_ && i_182_ >= 0 && i_180_ < i_174_)
			offset[i_180_] = is[i_179_];
		    i_180_ += i_169_;
		    i_179_++;
		}
	    }
	}
    }
    
    public void swap(LO lo_183_) {
	LO lo_184_ = new LO(W, H);
	lo_184_.setField(this);
	setField(lo_183_);
	lo_183_.setField(lo_184_);
	int[] is = offset;
	int[] is_185_ = lo_183_.offset;
	if (is != null && is_185_ != null) {
	    int i = W * H;
	    for (int i_186_ = 0; i_186_ < i; i_186_++) {
		int i_187_ = is[i_186_];
		is[i_186_] = is_185_[i_186_];
		is_185_[i_186_] = i_187_;
	    }
	} else {
	    offset = is_185_;
	    lo_183_.offset = is;
	}
    }
}
