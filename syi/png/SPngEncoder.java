/* SPngEncoder - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.png;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

import syi.util.ByteStream;

public class SPngEncoder
{
    private final String STR_C = "(C)shi-chan 2001-2003";
    private OutputStream OUT;
    private int width;
    private int height;
    private int[] i_off;
    private CRC32 crc = new CRC32();
    private Deflater deflater;
    private byte[] b;
    private byte[] bGet = new byte[1025];
    private int seek = 0;
    private int[] iF = new int[3];
    private int[] iFNow = new int[3];
    private int[] iFLOld = new int[3];
    private int[] iFL;
    private ByteStream work;
    private int image_type = 2;
    private byte image_filter;
    private boolean isProgress = false;
    
    public SPngEncoder(ByteStream bytestream, ByteStream bytestream_0_,
		       Deflater deflater) {
	System.out.println("(C)shi-chan 2001-2003");
	work = bytestream;
	b = bytestream_0_.getBuffer();
	this.deflater = deflater;
    }
    
    private void bFilter() throws IOException {
	bW(image_filter);
	zero(iF);
    }
    
    private void bW(byte i) throws IOException {
	b[seek++] = i;
	if (seek >= b.length)
	    wCompress();
    }
    
    public void encode(OutputStream outputstream, int[] is, int i, int i_1_,
		       int i_2_) {
	try {
	    OUT = outputstream;
	    width = i;
	    height = i_1_;
	    i_off = is;
	    image_filter = (byte) i_2_;
	    if (image_filter > 1) {
		if (iFL == null || iFL.length < width)
		    iFL = new int[width];
		zero(iFL);
		zero(iFLOld);
	    }
	    outputstream.write(new byte[] { -119, 80, 78, 71, 13, 10, 26,
					    10 });
	    mIHDR();
	    mEXt("Title", "Shi-Tools Oekaki Data");
	    mEXt("Copyright", "(C)shi-chan 2001-2003");
	    mEXt("Software", "Shi-Tools");
	    mIDAT();
	    mIEND();
	    outputstream.flush();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public void fencode(ByteStream bytestream, int[] is, int i, int i_3_) {
	bytestream.reset();
	int[] is_4_ = new int[4];
	for (int i_5_ = 0; i_5_ < 4; i_5_++) {
	    bytestream.reset();
	    encode(bytestream, is, i, i_3_, i_5_);
	    is_4_[i_5_] = bytestream.size();
	}
	int i_6_;
	for (i_6_ = 0; i_6_ < 4; i_6_++) {
	    int i_7_;
	    for (i_7_ = i_6_ + 1; i_7_ < 4; i_7_++) {
		if (is_4_[i_6_] > is_4_[i_7_])
		    break;
	    }
	    if (i_7_ >= 4)
		break;
	}
	if (i_6_ != 3) {
	    bytestream.reset();
	    encode(bytestream, is, i, i_3_, i_6_);
	}
    }
    
    private void getFPic(int i, int i_8_) {
	for (int i_9_ = 0; i_9_ < 3; i_9_++)
	    iFNow[i_9_] = i >>> (i_9_ << 3) & 0xff;
	switch (image_filter) {
	case 0:
	    return;
	case 1:
	    for (int i_10_ = 0; i_10_ < 3; i_10_++)
		iF[i_10_] = iFNow[i_10_] - iF[i_10_];
	    break;
	case 2:
	    for (int i_11_ = 0; i_11_ < 3; i_11_++)
		iF[i_11_] = iFNow[i_11_] - (iFL[i_8_] >>> (i_11_ << 3) & 0xff);
	    iFL[i_8_] = i;
	    break;
	case 3:
	    for (int i_12_ = 0; i_12_ < 3; i_12_++)
		iF[i_12_]
		    = iFNow[i_12_] - (iF[i_12_] + (iFL[i_8_] >>> (i_12_ << 3)
						   & 0xff)
				      >>> 1);
	    iFL[i_8_] = i;
	    break;
	}
	int[] is = iF;
	iF = iFNow;
	iFNow = is;
    }
    
    private void mEXt(String string, String string_13_) throws IOException {
	int i = 1950701684;
	wCh(i);
	wArray(string.getBytes());
	w(0);
	wArray(string_13_.getBytes());
	wChA();
    }
    
    private void mIDAT() throws IOException {
	int i = 1229209940;
	wCh(i);
	wImage();
	wChA();
    }
    
    private void mIEND() throws IOException {
	int i = 1229278788;
	wCh(i);
	wChA();
    }
    
    private void mIHDR() throws IOException {
	int i = 1229472850;
	wCh(i);
	wInt(width);
	wInt(height);
	w(8);
	w(2);
	w(0);
	w(0);
	w(isProgress ? 1 : 0);
	wChA();
    }
    
    public void setInterlace(boolean bool) {
	isProgress = bool;
    }
    
    private void w(int i) throws IOException {
	work.write(i);
    }
    
    private void wArray(byte[] is) throws IOException {
	wArray(is, is.length);
    }
    
    private void wArray(byte[] is, int i) throws IOException {
	if (i > 0)
	    work.write(is, 0, i);
    }
    
    private void wCh(int i) throws IOException {
	work.reset();
	wInt(i);
    }
    
    private void wChA() throws IOException {
	int i = work.size();
	crc.reset();
	crc.update(work.getBuffer(), 0, i);
	wInt((int) crc.getValue());
	i -= 4;
	for (int i_14_ = 24; i_14_ >= 0; i_14_ -= 8)
	    OUT.write(i >>> i_14_ & 0xff);
	work.writeTo(OUT);
    }
    
    private void wCompress() throws IOException {
	if (seek != 0) {
	    deflater.setInput(b, 0, seek);
	    seek = 0;
	    while (!deflater.needsInput()) {
		int i = deflater.deflate(bGet, 0, bGet.length - 1);
		if (i != 0)
		    wArray(bGet, i);
	    }
	}
    }
    
    private void wImage() throws IOException {
	int i = 0;
	deflater.reset();
	seek = 0;
	if (!isProgress) {
	    for (int i_15_ = 0; i_15_ < height; i_15_++) {
		bFilter();
		for (int i_16_ = 0; i_16_ < width; i_16_++) {
		    getFPic(i_off[i++], i_16_);
		    for (int i_17_ = 2; i_17_ >= 0; i_17_--)
			bW((byte) iFNow[i_17_]);
		}
	    }
	} else {
	    boolean bool = false;
	    byte[][][] is = { { new byte[1] }, { { 4 } }, { { 32, 36 } },
			      { { 2, 6 }, { 34, 38 } },
			      { { 16, 18, 20, 22 }, { 48, 50, 52, 54 } },
			      { { 1, 3, 5, 7 }, { 17, 19, 21, 23 },
				{ 33, 35, 37, 39 }, { 49, 51, 53, 55 } },
			      { { 8, 9, 10, 11, 12, 13, 14, 15 },
				{ 24, 25, 26, 27, 28, 29, 30, 31 },
				{ 40, 41, 42, 43, 44, 45, 46, 47 },
				{ 56, 57, 58, 59, 60, 61, 62, 63 } } };
	    for (int i_18_ = 0; i_18_ < is.length; i_18_++) {
		zero(iFL);
		zero(iF);
		for (int i_19_ = 0; i_19_ < height; i_19_ += 8) {
		    for (int i_20_ = 0; i_20_ < is[i_18_].length; i_20_++) {
			boolean bool_21_ = false;
			for (int i_22_ = 0; i_22_ < width; i_22_ += 8) {
			    for (int i_23_ = 0;
				 i_23_ < is[i_18_][i_20_].length; i_23_++) {
				int i_24_ = is[i_18_][i_20_][i_23_];
				int i_25_ = i_22_ + i_24_ % 8;
				int i_26_ = i_19_ + i_24_ / 8;
				if (i_25_ < width && i_26_ < height) {
				    if (!bool_21_) {
					bFilter();
					bool_21_ = true;
				    }
				    getFPic(i_off[width * i_26_ + i_25_],
					    i_25_);
				    for (int i_27_ = 2; i_27_ >= 0; i_27_--)
					bW((byte) iFNow[i_27_]);
				}
			    }
			}
		    }
		}
	    }
	}
	wCompress();
	deflater.finish();
	while (!deflater.finished()) {
	    i = deflater.deflate(bGet, 0, bGet.length - 1);
	    wArray(bGet, i);
	}
    }
    
    private void wInt(int i) throws IOException {
	for (int i_28_ = 24; i_28_ >= 0; i_28_ -= 8)
	    w(i >>> i_28_ & 0xff);
    }
    
    private void zero(int[] is) {
	if (is != null) {
	    for (int i = 0; i < is.length; i++)
		is[i] = 0;
	}
    }
}
