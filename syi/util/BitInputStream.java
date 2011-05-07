/* BitInputStream - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;
import java.io.InputStream;

public class BitInputStream
{
    private byte[] data;
    private int seekMax;
    private int seekByte = 0;
    private int seekBit = 7;
    private byte nowByte;
    private InputStream in;
    
    public BitInputStream() {
	/* empty */
    }
    
    public BitInputStream(byte[] is) {
	setArray(is);
    }
    
    public BitInputStream(InputStream inputstream) {
	/* empty */
    }
    
    public final int r() {
	if (seekByte >= seekMax)
	    return -1;
	int i = nowByte >> seekBit & 0x1;
	if (--seekBit < 0) {
	    seekBit = 7;
	    seekByte++;
	    if (seekByte < seekMax)
		nowByte = data[seekByte];
	}
	return i;
    }
    
    public final int rByte() {
	int i = 0;
	for (int i_0_ = 7; i_0_ >= 0; i_0_--) {
	    if (r() == -1)
		return i_0_ == 7 ? -1 : i;
	    i |= r() << i_0_;
	}
	return i;
    }
    
    public void setArray(byte[] is) {
	setArray(is, is.length);
    }
    
    public void setArray(byte[] is, int i) {
	setArray(is, 0, i);
    }
    
    public void setArray(byte[] is, int i, int i_1_) {
	data = is;
	seekByte = i;
	seekBit = 7;
	seekMax = i_1_;
	nowByte = data[i];
    }
}
