/* CharStream - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;
import java.io.IOException;
import java.io.Writer;

public class CharStream extends Writer
{
    private char[] buffer;
    private int count = 0;
    private static char[] separator;
    
    public CharStream() {
	this(512);
    }
    
    public CharStream(int i) {
	buffer = new char[i <= 0 ? 512 : i];
	if (separator == null) {
	    String string = System.getProperty("line.separator", "\n");
	    separator = string.toCharArray();
	}
    }
    
    public final void addSize(int i) {
	int i_0_ = count + i;
	if (buffer.length < i_0_) {
	    char[] cs
		= new char[Math.max((int) ((float) buffer.length * 1.25F),
				    i_0_) + 1];
	    System.arraycopy(buffer, 0, cs, 0, buffer.length);
	    buffer = cs;
	}
    }
    
    public void close() {
	/* empty */
    }
    
    public void flush() {
	/* empty */
    }
    
    public void gc() {
	if (buffer.length != count) {
	    char[] cs = new char[count];
	    if (count != 0)
		System.arraycopy(buffer, 0, cs, 0, count);
	    buffer = cs;
	}
    }
    
    public char[] getBuffer() {
	return buffer;
    }
    
    public final void insert(int i, int i_1_) {
	buffer[i] = (char) i_1_;
    }
    
    public void reset() {
	count = 0;
    }
    
    public void seek(int i) {
	count = i;
    }
    
    public final int size() {
	return count;
    }
    
    public char[] toCharArray() {
	char[] cs = new char[count];
	if (count > 0)
	    System.arraycopy(buffer, 0, cs, 0, count);
	return cs;
    }
    
    public final void write(char[] cs) {
	write(cs, 0, cs.length);
    }
    
    public final void write(char[] cs, int i, int i_2_) {
	addSize(i_2_);
	System.arraycopy(cs, i, buffer, count, i_2_);
	count += i_2_;
    }
    
    public final void write(int i) throws IOException {
	addSize(1);
	buffer[count++] = (char) i;
    }
    
    public final void write(String string) {
	write(string, 0, string.length());
    }
    
    public final void write(String string, int i, int i_3_) {
	addSize(i_3_);
	string.getChars(i, i + i_3_, buffer, count);
	count += i_3_;
    }
    
    public final void writeln(String string) {
	write(string);
	write(separator);
    }
    
    public void writeTo(char[] cs, int i) {
	int i_4_ = i + count;
	if (i < i_4_) {
	    if (cs.length < i_4_) {
		char[] cs_5_ = new char[i_4_];
		System.arraycopy(cs, 0, cs_5_, 0, cs.length);
		cs = cs_5_;
	    }
	    System.arraycopy(buffer, 0, cs, i, count);
	}
    }
    
    public void writeTo(Writer writer) throws IOException {
	if (count != 0)
	    writer.write(buffer, 0, count);
    }
}
