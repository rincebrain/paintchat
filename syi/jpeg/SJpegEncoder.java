/* SJpegEncoder - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.jpeg;
import java.io.IOException;
import java.io.OutputStream;

public class SJpegEncoder
{
    OutputStream OUT;
    private int[] i_off;
    private int width;
    private int height;
    private int HV;
    private double[][] mCosT;
    private int[] mOldDC;
    private double kSqrt2;
    private double kDisSqrt2;
    private double kPaiDiv16;
    private int bitSeek = 7;
    private int bitValue = 0;
    private int[] kYQuantumT = new int[64];
    private int[] kCQuantumT = new int[64];
    private final byte[] kYDcSizeT = { 2, 3, 3, 3, 3, 3, 4, 5, 6, 7, 8, 9 };
    private final short[] kYDcCodeT
	= { 0, 2, 3, 4, 5, 6, 14, 30, 62, 126, 254, 510 };
    private final byte[] kCDcSizeT = { 2, 2, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
    private final short[] kCDcCodeT
	= { 0, 1, 2, 6, 14, 30, 62, 126, 254, 510, 1022, 2046 };
    private final byte[] kYAcSizeT
	= { 4, 2, 2, 3, 4, 5, 7, 8, 10, 16, 16, 4, 5, 7, 9, 11, 16, 16, 16, 16,
	    16, 5, 8, 10, 12, 16, 16, 16, 16, 16, 16, 6, 9, 12, 16, 16, 16, 16,
	    16, 16, 16, 6, 10, 16, 16, 16, 16, 16, 16, 16, 16, 7, 11, 16, 16,
	    16, 16, 16, 16, 16, 16, 7, 12, 16, 16, 16, 16, 16, 16, 16, 16, 8,
	    12, 16, 16, 16, 16, 16, 16, 16, 16, 9, 15, 16, 16, 16, 16, 16, 16,
	    16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16,
	    16, 16, 16, 16, 16, 10, 16, 16, 16, 16, 16, 16, 16, 16, 16, 10, 16,
	    16, 16, 16, 16, 16, 16, 16, 16, 11, 16, 16, 16, 16, 16, 16, 16, 16,
	    16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 11, 16, 16, 16, 16, 16,
	    16, 16, 16, 16, 16 };
    private final short[] kYAcCodeT
	= { 10, 0, 1, 4, 11, 26, 120, 248, 1014, -126, -125, 12, 27, 121, 502,
	    2038, -124, -123, -122, -121, -120, 28, 249, 1015, 4084, -119,
	    -118, -117, -116, -115, -114, 58, 503, 4085, -113, -112, -111,
	    -110, -109, -108, -107, 59, 1016, -106, -105, -104, -103, -102,
	    -101, -100, -99, 122, 2039, -98, -97, -96, -95, -94, -93, -92, -91,
	    123, 4086, -90, -89, -88, -87, -86, -85, -84, -83, 250, 4087, -82,
	    -81, -80, -79, -78, -77, -76, -75, 504, 32704, -74, -73, -72, -71,
	    -70, -69, -68, -67, 505, -66, -65, -64, -63, -62, -61, -60, -59,
	    -58, 506, -57, -56, -55, -54, -53, -52, -51, -50, -49, 1017, -48,
	    -47, -46, -45, -44, -43, -42, -41, -40, 1018, -39, -38, -37, -36,
	    -35, -34, -33, -32, -31, 2040, -30, -29, -28, -27, -26, -25, -24,
	    -23, -22, -21, -20, -19, -18, -17, -16, -15, -14, -13, -12, 2041,
	    -11, -10, -9, -8, -7, -6, -5, -4, -3, -2 };
    private int kYEOBidx = 0;
    private int kYZRLidx = 151;
    private final byte[] kCAcSizeT
	= { 2, 2, 3, 4, 5, 5, 6, 7, 9, 10, 12, 4, 6, 8, 9, 11, 12, 16, 16, 16,
	    16, 5, 8, 10, 12, 15, 16, 16, 16, 16, 16, 5, 8, 10, 12, 16, 16, 16,
	    16, 16, 16, 6, 9, 16, 16, 16, 16, 16, 16, 16, 16, 6, 10, 16, 16,
	    16, 16, 16, 16, 16, 16, 7, 11, 16, 16, 16, 16, 16, 16, 16, 16, 7,
	    11, 16, 16, 16, 16, 16, 16, 16, 16, 8, 16, 16, 16, 16, 16, 16, 16,
	    16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16, 16, 16, 16,
	    16, 16, 16, 16, 16, 9, 16, 16, 16, 16, 16, 16, 16, 16, 16, 9, 16,
	    16, 16, 16, 16, 16, 16, 16, 16, 11, 16, 16, 16, 16, 16, 16, 16, 16,
	    16, 14, 16, 16, 16, 16, 16, 16, 16, 16, 16, 10, 15, 16, 16, 16, 16,
	    16, 16, 16, 16, 16 };
    private final short[] kCAcCodeT
	= { 0, 1, 4, 10, 24, 25, 56, 120, 500, 1014, 4084, 11, 57, 246, 501,
	    2038, 4085, -120, -119, -118, -117, 26, 247, 1015, 4086, 32706,
	    -116, -115, -114, -113, -112, 27, 248, 1016, 4087, -111, -110,
	    -109, -108, -107, -106, 58, 502, -105, -104, -103, -102, -101,
	    -100, -99, -98, 59, 1017, -97, -96, -95, -94, -93, -92, -91, -90,
	    121, 2039, -89, -88, -87, -86, -85, -84, -83, -82, 122, 2040, -81,
	    -80, -79, -78, -77, -76, -75, -74, 249, -73, -72, -71, -70, -69,
	    -68, -67, -66, -65, 503, -64, -63, -62, -61, -60, -59, -58, -57,
	    -56, 504, -55, -54, -53, -52, -51, -50, -49, -48, -47, 505, -46,
	    -45, -44, -43, -42, -41, -40, -39, -38, 506, -37, -36, -35, -34,
	    -33, -32, -31, -30, -29, 2041, -28, -27, -26, -25, -24, -23, -22,
	    -21, -20, 16352, -19, -18, -17, -16, -15, -14, -13, -12, -11, 1018,
	    32707, -10, -9, -8, -7, -6, -5, -4, -3, -2 };
    private int kCEOBidx = 0;
    private int kCZRLidx = 151;
    private final byte[] kZigzag
	= { 0, 1, 8, 16, 9, 2, 3, 10, 17, 24, 32, 25, 18, 11, 4, 5, 12, 19, 26,
	    33, 40, 48, 41, 34, 27, 20, 13, 6, 7, 14, 21, 28, 35, 42, 49, 56,
	    57, 50, 43, 36, 29, 22, 15, 23, 30, 37, 44, 51, 58, 59, 52, 45, 38,
	    31, 39, 46, 53, 60, 61, 54, 47, 55, 62, 63 };
    
    public SJpegEncoder() {
	kSqrt2 = 1.41421356;
	kDisSqrt2 = 1.0 / kSqrt2;
	kPaiDiv16 = 0.19634954084936207;
	mCosT = new double[8][8];
	for (int i = 0; i < 8; i++) {
	    for (int i_0_ = 0; i_0_ < 8; i_0_++)
		mCosT[i][i_0_]
		    = Math.cos((double) ((2 * i_0_ + 1) * i) * kPaiDiv16);
	}
    }
    
    public void encode(OutputStream outputstream, int[] is, int i, int i_1_,
		       int i_2_) {
	try {
	    int i_3_ = 65496;
	    int i_4_ = 65497;
	    OUT = outputstream;
	    mOldDC = new int[3];
	    q(i_2_);
	    i_off = is;
	    width = i;
	    height = i_1_;
	    wShort(i_3_);
	    mAPP0();
	    mComment();
	    mDQT();
	    mDHT();
	    mSOF();
	    mSOS();
	    mMCU();
	    wShort(i_4_);
	    OUT.flush();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void getYCC(int[] is, int[] is_5_, int i, int i_6_, int i_7_,
			int i_8_, int i_9_) {
	int i_10_ = 0;
	int i_11_ = 0;
	int i_12_ = i_7_ * 8;
	int i_13_ = i_8_ * 8;
	int i_14_ = i + i_12_;
	int i_15_ = i_6_ + i_13_;
	int[] is_16_ = new int[3];
	for (int i_17_ = i_6_; i_17_ < i_15_; i_17_++) {
	    if (i_17_ >= height) {
		try {
		    for (int i_18_ = i_10_; i_18_ < is_5_.length; i_18_++)
			is_5_[i_18_] = is_5_[i_18_ - i_12_];
		    break;
		} catch (RuntimeException runtimeexception) {
		    break;
		}
	    }
	    int i_19_ = i_17_ * width + i;
	    for (int i_20_ = i; i_20_ < i_14_; i_20_++) {
		int i_21_ = i_20_ >= width ? i_11_ : i_off[i_19_];
		i_11_ = i_21_;
		is_5_[i_10_++] = i_21_;
		i_19_++;
	    }
	}
	int[] is_22_ = new int[3];
	i_10_ = 0;
	for (int i_23_ = 0; i_23_ < i_13_; i_23_ += i_8_) {
	    for (int i_24_ = 0; i_24_ < i_12_; i_24_ += i_7_) {
		int i_25_ = 0;
		is_22_[0] = 0;
		is_22_[1] = 0;
		is_22_[2] = 0;
		for (int i_26_ = 0; i_26_ < i_8_; i_26_++) {
		    for (int i_27_ = 0; i_27_ < i_7_; i_27_++) {
			int i_28_
			    = is_5_[(i_23_ + i_26_) * i_12_ + (i_24_ + i_27_)];
			for (int i_29_ = 0; i_29_ < 3; i_29_++)
			    is_22_[i_29_] += i_28_ >> i_29_ * 8 & 0xff;
			i_25_++;
		    }
		}
		for (int i_30_ = 0; i_30_ < 3; i_30_++)
		    is_22_[i_30_]
			= Math.min(Math.max(is_22_[i_30_] / i_25_, 0), 255);
		is[i_10_++] = is_22_[2] << 16 | is_22_[1] << 8 | is_22_[0];
	    }
	}
	for (int i_31_ = 0; i_31_ < 64; i_31_++) {
	    ycc(is_16_, is[i_31_]);
	    is[i_31_] = is_16_[i_9_];
	}
    }
    
    private void mAPP0() throws IOException {
	int i = 65504;
	byte[] is = "JFIF\0".getBytes();
	wShort(i);
	wShort(is.length + 11);
	wArray(is);
	w(1);
	w(2);
	w(1);
	wShort(72);
	wShort(72);
	w(0);
	w(0);
    }
    
    private final void mComment() throws IOException {
	int i = 65534;
	String string = "(C)shi-chan 2001";
	byte[] is = (string + '\0').getBytes();
	wShort(i);
	wShort(is.length + 2);
	wArray(is);
	System.out.println(string);
    }
    
    private void mDHT() throws IOException {
	int i = 65476;
	byte[] is = { 0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
		      2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
	byte[] is_32_ = { 1, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0,
			  1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
	byte[] is_33_
	    = { 16, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125, 1, 2, 3,
		0, 4, 17, 5, 18, 33, 49, 65, 6, 19, 81, 97, 7, 34, 113, 20, 50,
		-127, -111, -95, 8, 35, 66, -79, -63, 21, 82, -47, -16, 36, 51,
		98, 114, -126, 9, 10, 22, 23, 24, 25, 26, 37, 38, 39, 40, 41,
		42, 52, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72, 73, 74,
		83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103, 104,
		105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -125, -124,
		-123, -122, -121, -120, -119, -118, -110, -109, -108, -107,
		-106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89,
		-88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70,
		-62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44,
		-43, -42, -41, -40, -39, -38, -31, -30, -29, -28, -27, -26,
		-25, -24, -23, -22, -15, -14, -13, -12, -11, -10, -9, -8, -7,
		-6 };
	byte[] is_34_
	    = { 17, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119, 0, 1, 2,
		3, 17, 4, 5, 33, 49, 6, 18, 65, 81, 7, 97, 113, 19, 34, 50,
		-127, 8, 20, 66, -111, -95, -79, -63, 9, 35, 51, 82, -16, 21,
		98, 114, -47, 10, 22, 36, 52, -31, 37, -15, 23, 24, 25, 26, 38,
		39, 40, 41, 42, 53, 54, 55, 56, 57, 58, 67, 68, 69, 70, 71, 72,
		73, 74, 83, 84, 85, 86, 87, 88, 89, 90, 99, 100, 101, 102, 103,
		104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -126,
		-125, -124, -123, -122, -121, -120, -119, -118, -110, -109,
		-108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91,
		-90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72,
		-71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46,
		-45, -44, -43, -42, -41, -40, -39, -38, -30, -29, -28, -27,
		-26, -25, -24, -23, -22, -14, -13, -12, -11, -10, -9, -8, -7,
		-6 };
	wShort(i);
	wShort(is.length + is_33_.length + is_32_.length + is_34_.length + 2);
	wArray(is);
	wArray(is_33_);
	wArray(is_32_);
	wArray(is_34_);
    }
    
    private void mDQT() throws IOException {
	int i = 65499;
	wShort(i);
	wShort(132);
	w(0);
	for (int i_35_ = 0; i_35_ < 64; i_35_++)
	    w(kYQuantumT[kZigzag[i_35_]] & 0xff);
	w(1);
	for (int i_36_ = 0; i_36_ < 64; i_36_++)
	    w(kCQuantumT[kZigzag[i_36_]] & 0xff);
    }
    
    private void mMCU() throws IOException {
	try {
	    int i = HV;
	    int i_37_ = 8 * i;
	    int[] is = new int[64];
	    int[] is_38_ = new int[64];
	    int[] is_39_ = new int[i_37_ * i_37_];
	    for (int i_40_ = 0; i_40_ < height; i_40_ += i_37_) {
		for (int i_41_ = 0; i_41_ < width; i_41_ += i_37_) {
		    for (int i_42_ = 0; i_42_ < i; i_42_++) {
			for (int i_43_ = 0; i_43_ < i; i_43_++) {
			    getYCC(is, is_39_, i_41_ + 8 * i_43_,
				   i_40_ + 8 * i_42_, 1, 1, 0);
			    tDCT(is, is_38_);
			    tQuantization(is_38_, 0);
			    tHuffman(is_38_, 0);
			}
		    }
		    getYCC(is, is_39_, i_41_, i_40_, HV, HV, 1);
		    tDCT(is, is_38_);
		    tQuantization(is_38_, 1);
		    tHuffman(is_38_, 1);
		    getYCC(is, is_39_, i_41_, i_40_, HV, HV, 2);
		    tDCT(is, is_38_);
		    tQuantization(is_38_, 2);
		    tHuffman(is_38_, 2);
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void mSOF() throws IOException {
	int i = 65472;
	wShort(i);
	wShort(17);
	w(8);
	wShort(height);
	wShort(width);
	w(3);
	for (int i_44_ = 0; i_44_ < 3; i_44_++) {
	    w(i_44_);
	    int i_45_ = i_44_ == 0 ? HV : 1;
	    w(i_45_ << 4 | i_45_);
	    w(i_44_ == 0 ? 0 : 1);
	}
    }
    
    private void mSOS() throws IOException {
	int i = 65498;
	wShort(i);
	wShort(12);
	w(3);
	for (int i_46_ = 0; i_46_ < 3; i_46_++) {
	    w(i_46_);
	    w(i_46_ == 0 ? 0 : 17);
	}
	w(0);
	w(63);
	w(0);
    }
    
    private void q(int i) {
	byte[] is = { 16, 11, 10, 16, 24, 40, 51, 61, 12, 12, 14, 19, 26, 58,
		      60, 55, 14, 13, 16, 24, 40, 57, 69, 56, 14, 17, 22, 29,
		      51, 87, 80, 62, 18, 22, 37, 56, 68, 109, 103, 77, 24, 35,
		      55, 64, 81, 104, 113, 92, 49, 64, 78, 87, 103, 121, 120,
		      101, 72, 92, 95, 98, 112, 100, 103, 99 };
	byte[] is_47_ = { 17, 18, 24, 47, 99, 99, 99, 99, 18, 21, 26, 66, 99,
			  99, 99, 99, 24, 26, 56, 99, 99, 99, 99, 99, 47, 66,
			  99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
			  99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
			  99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99 };
	i = i < 1 ? 1 : i > 100 ? 100 : i;
	HV = 1;
	if (i >= 25)
	    HV = 2;
	float f = (float) i / 50.0F;
	float f_48_ = (float) i / 50.0F;
	for (int i_49_ = 0; i_49_ < 64; i_49_++) {
	    kYQuantumT[i_49_]
		= Math.min(Math.max((int) ((float) is[i_49_] * f), 1), 127);
	    kCQuantumT[i_49_]
		= Math.min(Math.max((int) ((float) is_47_[i_49_] * f_48_), 1),
			   127);
	}
    }
    
    private void tDCT(int[] is, int[] is_50_) {
	try {
	    int i = 0;
	    boolean bool = false;
	    for (int i_51_ = 0; i_51_ < 8; i_51_++) {
		double d = i_51_ > 0 ? 1.0 : kDisSqrt2;
		for (int i_52_ = 0; i_52_ < 8; i_52_++) {
		    double d_53_ = i_52_ > 0 ? 1.0 : kDisSqrt2;
		    double d_54_ = 0.0;
		    int i_55_ = 0;
		    for (int i_56_ = 0; i_56_ < 8; i_56_++) {
			for (int i_57_ = 0; i_57_ < 8; i_57_++)
			    d_54_
				+= ((double) is[i_55_++] * mCosT[i_52_][i_57_]
				    * mCosT[i_51_][i_56_]);
		    }
		    is_50_[i++] = (int) (d_54_ * d_53_ * d / 4.0);
		}
	    }
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	}
    }
    
    private void tHuffman(int[] is, int i) throws IOException {
	try {
	    byte[] is_58_;
	    short[] is_59_;
	    byte[] is_60_;
	    short[] is_61_;
	    int i_62_;
	    int i_63_;
	    if (i == 0) {
		is_58_ = kYAcSizeT;
		is_59_ = kYAcCodeT;
		is_60_ = kYDcSizeT;
		is_61_ = kYDcCodeT;
		i_62_ = kYEOBidx;
		i_63_ = kYZRLidx;
	    } else {
		is_58_ = kCAcSizeT;
		is_59_ = kCAcCodeT;
		is_60_ = kCDcSizeT;
		is_61_ = kCDcCodeT;
		i_62_ = kCEOBidx;
		i_63_ = kCZRLidx;
	    }
	    int i_64_ = is[0] - mOldDC[i];
	    mOldDC[i] = is[0];
	    int i_65_ = Math.abs(i_64_);
	    byte i_66_ = 0;
	    while (i_65_ > 0) {
		i_65_ >>= 1;
		i_66_++;
	    }
	    wBit(is_61_[i_66_], is_60_[i_66_]);
	    if (i_66_ != 0) {
		if (i_64_ < 0)
		    i_64_--;
		wBit(i_64_, i_66_);
	    }
	    int i_67_ = 0;
	    for (int i_68_ = 1; i_68_ < 64; i_68_++) {
		i_65_ = Math.abs(is[kZigzag[i_68_]]);
		if (i_65_ == 0)
		    i_67_++;
		else {
		    for (/**/; i_67_ > 15; i_67_ -= 16)
			wBit(is_59_[i_63_], is_58_[i_63_]);
		    i_66_ = (byte) 0;
		    while (i_65_ > 0) {
			i_65_ >>= 1;
			i_66_++;
		    }
		    int i_69_ = i_67_ * 10 + i_66_ + (i_67_ == 15 ? 1 : 0);
		    wBit(is_59_[i_69_], is_58_[i_69_]);
		    i_64_ = is[kZigzag[i_68_]];
		    if (i_64_ < 0)
			i_64_--;
		    wBit(i_64_, i_66_);
		    i_67_ = 0;
		}
	    }
	    if (i_67_ > 0)
		wBit(is_59_[i_62_], is_58_[i_62_]);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void tQuantization(int[] is, int i) {
	int[] is_70_ = i == 0 ? kYQuantumT : kCQuantumT;
	for (int i_71_ = 0; i_71_ < is.length; i_71_++)
	    is[i_71_] /= is_70_[i_71_];
    }
    
    private void w(int i) throws IOException {
	wFullBit();
	OUT.write(i);
    }
    
    private void wArray(byte[] is) throws IOException {
	wFullBit();
	for (int i = 0; i < is.length; i++)
	    w(is[i] & 0xff);
    }
    
    private void wArray(int[] is) throws IOException {
	wFullBit();
	for (int i = 0; i < is.length; i++)
	    w(is[i] & 0xff);
    }
    
    private void wBit(int i, byte i_72_) throws IOException {
	i_72_--;
	for (byte i_73_ = i_72_; i_73_ >= 0; i_73_--) {
	    int i_74_ = i >>> i_73_ & 0x1;
	    bitValue |= i_74_ << bitSeek;
	    if (--bitSeek <= -1) {
		OUT.write(bitValue);
		if (bitValue == 255)
		    OUT.write(0);
		bitValue = 0;
		bitSeek = 7;
	    }
	}
    }
    
    private void wFullBit() throws IOException {
	if (bitSeek != 7) {
	    wBit(255, (byte) (bitSeek + 1));
	    bitValue = 0;
	    bitSeek = 7;
	}
    }
    
    private void wShort(int i) throws IOException {
	wFullBit();
	OUT.write(i >>> 8 & 0xff);
	OUT.write(i & 0xff);
    }
    
    private void ycc(int[] is, int i) {
	int i_75_ = i >>> 16 & 0xff;
	int i_76_ = i >>> 8 & 0xff;
	int i_77_ = i & 0xff;
	is[0] = (int) (0.299F * (float) i_75_ + 0.587F * (float) i_76_
		       + 0.114F * (float) i_77_ - 128.0F);
	is[1] = (int) (-(0.1687F * (float) i_75_) - 0.3313F * (float) i_76_
		       + 0.5F * (float) i_77_);
	is[2] = (int) (0.5F * (float) i_75_ - 0.4187F * (float) i_76_
		       - 0.0813F * (float) i_77_);
    }
}
