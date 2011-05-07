/* BitOutputStream - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;
import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream extends OutputStream
{
    private int bit_count = 0;
    private int bit;
    private OutputStream out;
    
    public BitOutputStream(OutputStream outputstream) {
	out = outputstream;
    }
    
    public void close() throws IOException {
	if (bit_count > 0)
	    wBit(0, 8 - bit_count);
	flush();
	out.close();
    }
    
    public void flush() throws IOException {
	out.flush();
    }
    
    public void wBit(int i, int i_0_) throws IOException {
	int i_1_;
	for (/**/; i_0_ > 0; i_0_ -= i_1_) {
	    if (bit_count == 0 && i_0_ >= 8) {
		i_1_ = 8;
		out.write(i & 0xff);
	    } else {
		i_1_ = Math.min(8 - bit_count, i_0_);
		bit |= i << bit_count;
		bit_count += i_1_;
		if (bit_count >= 8) {
		    out.write(bit);
		    bit = 0;
		    bit_count = 0;
		}
	    }
	    i >>>= i_1_;
	}
    }
    
    public void write(int i) throws IOException {
	wBit(i, 8);
    }
}
