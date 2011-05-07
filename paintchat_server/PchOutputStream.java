/* PchOutputStream - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import syi.util.Io;

public class PchOutputStream extends OutputStream
{
    private OutputStream out;
    private boolean isWriteHeader = false;
    public static String OPTION_MGCOUNT = "mg_count";
    public static String OPTION_VERSION = "version";
    private int write_size = 0;
    
    public PchOutputStream(OutputStream outputstream, boolean bool) {
	out = outputstream;
	isWriteHeader = bool;
    }
    
    public void close() throws IOException {
	if (!isWriteHeader)
	    writeHeader();
	out.close();
    }
    
    public void flush() throws IOException {
	if (!isWriteHeader)
	    writeHeader();
	out.flush();
    }
    
    public void write(byte[] is) throws IOException {
	write(is, 0, is.length);
    }
    
    public void write(byte[] is, int i, int i_0_) throws IOException {
	if (!isWriteHeader)
	    writeHeader();
	Io.wShort(out, i_0_);
	out.write(is, i, i_0_);
    }
    
    public void write(int i) throws IOException {
	if (!isWriteHeader)
	    writeHeader();
	out.write(i);
    }
    
    public void write(File file) throws IOException {
	if (!isWriteHeader)
	    writeHeader();
	int i = 0;
	int i_1_ = (int) file.length();
	byte[] is = null;
	if (i_1_ > 2) {
	    FileInputStream fileinputstream = null;
	    try {
		fileinputstream = new FileInputStream(file);
		while (i < i_1_) {
		    int i_2_ = Io.readUShort(fileinputstream);
		    if (is == null || is.length < i_2_)
			is = new byte[i_2_];
		    Io.rFull(fileinputstream, is, 0, i_2_);
		    write(is, 0, i_2_);
		}
	    } catch (IOException ioexception) {
		/* empty */
	    }
	    if (fileinputstream != null) {
		try {
		    fileinputstream.close();
		} catch (IOException ioexception) {
		    /* empty */
		}
	    }
	}
    }
    
    private void writeHeader() throws IOException {
	if (!isWriteHeader) {
	    out.write(13);
	    out.write(10);
	    isWriteHeader = true;
	}
    }
    
    public void writeHeader(String string, String string_3_)
	throws IOException {
	if (!isWriteHeader)
	    out.write((string + "=" + string_3_ + "\r\n").getBytes("UTF8"));
    }
    
    public int size() {
	return write_size;
    }
}
