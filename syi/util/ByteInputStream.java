/* ByteInputStream - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;
import java.io.IOException;
import java.io.InputStream;

public class ByteInputStream extends InputStream
{
    private byte[] buffer;
    private int iSeekStart = 0;
    private int iSeek = 0;
    private int iLen = 0;
    
    public int available() {
	return iLen - iSeek;
    }
    
    public int read() {
	return iSeek >= iLen ? -1 : buffer[iSeek++] & 0xff;
    }
    
    public int read(byte[] is, int i, int i_0_) {
	if (iSeek >= iLen)
	    return -1;
	i_0_ = Math.min(iLen - iSeek, i_0_);
	System.arraycopy(buffer, iSeek, is, i, i_0_);
	iSeek += i_0_;
	return i_0_;
    }
    
    public void reset() {
	iSeek = iSeekStart;
    }
    
    public void setBuffer(byte[] is, int i, int i_1_) {
	buffer = is;
	iLen = i + i_1_;
	iSeekStart = iSeek = i;
    }
    
    public void setByteStream(ByteStream bytestream) {
	setBuffer(bytestream.getBuffer(), 0, bytestream.size());
    }
    
    public void close() throws IOException {
	/* empty */
    }
    
    public int read(byte[] is) throws IOException {
	return read(is, 0, is.length);
    }
    
    public long skip(long l) throws IOException {
	l = Math.min(l, (long) (iLen - iSeek));
	iSeek += l;
	return l;
    }
    
    public boolean markSupported() {
	return true;
    }
    
    public void mark(int i) {
	iSeekStart = iSeek;
    }
}
