/* ByteStream - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteStream extends OutputStream
{
    private byte[] buffer;
    private int last = 0;
    
    public ByteStream() {
	this(512);
    }
    
    public ByteStream(byte[] is) {
	buffer = is;
    }
    
    public ByteStream(int i) {
	buffer = new byte[i <= 0 ? 512 : i];
    }
    
    public final void addSize(int i) {
	int i_0_ = last + i;
	if (buffer.length < i_0_) {
	    byte[] is
		= (new byte
		   [Math.max((int) ((float) buffer.length * 1.5F), i_0_) + 1]);
	    System.arraycopy(buffer, 0, is, 0, buffer.length);
	    buffer = is;
	}
    }
    
    public void gc() {
	if (buffer.length != last) {
	    byte[] is = new byte[last];
	    if (last != 0)
		System.arraycopy(buffer, 0, is, 0, last);
	    buffer = is;
	}
    }
    
    public byte[] getBuffer() {
	return buffer;
    }
    
    public final void insert(int i, int i_1_) {
	buffer[i] = (byte) i_1_;
    }
    
    public void reset() {
	last = 0;
    }
    
    public void reset(int i) {
	int i_2_ = last;
	reset();
	if (i < i_2_)
	    write(buffer, i, i_2_ - i);
    }
    
    public void seek(int i) {
	last = i;
    }
    
    public final int size() {
	return last;
    }
    
    public byte[] toByteArray() {
	byte[] is = new byte[last];
	if (last > 0)
	    System.arraycopy(buffer, 0, is, 0, last);
	return is;
    }
    
    public final void w(long l, int i) throws IOException {
	for (int i_3_ = i - 1; i_3_ >= 0; i_3_--)
	    write((int) (l >>> (i_3_ << 3)) & 0xff);
    }
    
    public final void w2(int i) throws IOException {
	write(i >>> 8 & 0xff);
	write(i & 0xff);
    }
    
    public final void write(byte[] is) {
	write(is, 0, is.length);
    }
    
    public final void write(byte[] is, int i, int i_4_) {
	addSize(i_4_);
	System.arraycopy(is, i, buffer, last, i_4_);
	last += i_4_;
    }
    
    public final void write(int i) throws IOException {
	addSize(1);
	buffer[last++] = (byte) i;
    }
    
    public void write(InputStream inputstream) throws IOException {
	addSize(128);
	try {
	    int i;
	    while ((i = inputstream.read(buffer, last, buffer.length - last))
		   != -1) {
		last += i;
		if (last >= buffer.length)
		    addSize(256);
	    }
	} catch (IOException ioexception) {
	    /* empty */
	}
    }
    
    public void write(InputStream inputstream, int i) throws IOException {
	if (i != 0) {
	    addSize(i);
	    int i_5_ = 0;
	    int i_6_;
	    while ((i_6_ = inputstream.read(buffer, last, i - i_5_)) != -1) {
		last += i_6_;
		i_5_ += i_6_;
		if (i_5_ >= i)
		    break;
	    }
	    if (i_5_ < i)
		throw new EOFException();
	}
    }
    
    public byte[] writeTo(byte[] is, int i) {
	int i_7_ = i + last;
	if (is == null)
	    is = new byte[i_7_];
	if (is.length < i_7_) {
	    byte[] is_8_ = new byte[i_7_];
	    System.arraycopy(is, 0, is_8_, 0, is.length);
	    is = is_8_;
	}
	System.arraycopy(buffer, 0, is, i, last);
	return is;
    }
    
    public void writeTo(OutputStream outputstream) throws IOException {
	if (last != 0)
	    outputstream.write(buffer, 0, last);
    }
}
