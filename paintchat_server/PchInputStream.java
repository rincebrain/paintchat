/* PchInputStream - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import paintchat.Res;

import syi.util.ByteInputStream;
import syi.util.ByteStream;
import syi.util.Io;
import syi.util.VectorBin;

public class PchInputStream
{
    private VectorBin lines = new VectorBin();
    private Res res = new Res();
    private InputStream in;
    boolean isRead = false;
    private int iMax = 10000000;
    
    public PchInputStream(InputStream inputstream, int i) {
	in = inputstream;
	iMax = Math.max(i, 0);
    }
    
    public VectorBin getLines() {
	getPch();
	return lines;
    }
    
    public Res getStatus() {
	getPch();
	return res;
    }
    
    private void getPch() {
	if (!isRead) {
	    isRead = true;
	    ByteStream bytestream = new ByteStream();
	    byte[] is = { 13, 10, 13, 10 };
	    int i = 0;
	    try {
		for (;;) {
		    int i_0_ = Io.r(in);
		    if (i_0_ < 0)
			throw new EOFException();
		    if (is[i] == i_0_) {
			if (++i >= is.length)
			    break;
		    } else
			i = 0;
		    bytestream.write(i_0_);
		}
		if (bytestream.size() > 0) {
		    ByteInputStream byteinputstream = new ByteInputStream();
		    byteinputstream.setByteStream(bytestream);
		    res.load(byteinputstream);
		}
		for (;;) {
		    i = Io.readUShort(in);
		    if (i < 0)
			break;
		    if (i >= 2) {
			is = new byte[i];
			Io.rFull(in, is, 0, i);
			lines.add(is);
			while (lines.size() > 0 && lines.getSizeBytes() > iMax)
			    lines.remove(1);
		    }
		}
	    } catch (EOFException eofexception) {
		/* empty */
	    } catch (IOException ioexception) {
		ioexception.printStackTrace();
	    }
	    try {
		in.close();
		in = null;
	    } catch (IOException ioexception) {
		/* empty */
	    }
	}
    }
}
