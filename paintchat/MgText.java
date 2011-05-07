/* MgText - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MgText
{
    public byte head = 100;
    public int ID = 0;
    public byte[] bName = null;
    private byte[] data = null;
    private int seekMax = 0;
    private String strData = null;
    public static final String ENCODE = "UTF8";
    private static final String EMPTY = "";
    public static final byte M_TEXT = 0;
    public static final byte M_IN = 1;
    public static final byte M_OUT = 2;
    public static final byte M_UPDATE = 3;
    public static final byte M_VERSION = 4;
    public static final byte M_EXIT = 5;
    public static final byte M_INFOMATION = 6;
    public static final byte M_SCRIPT = 8;
    public static final byte M_EMPTY = 100;
    public static final byte M_SERVER = 102;
    
    public MgText() {
	/* empty */
    }
    
    public MgText(int i, byte i_0_, byte[] is) {
	setData(i, i_0_, is);
    }
    
    public MgText(int i, byte i_1_, String string) {
	setData(i, i_1_, string);
    }
    
    public MgText(MgText mgtext_2_) {
	setData(mgtext_2_);
    }
    
    public void getData(OutputStream outputstream, boolean bool)
	throws IOException {
	if (head != -1) {
	    toBin(false);
	    int i = bName == null || !bool ? 0 : bName.length;
	    int i_3_ = seekMax + 4 + i;
	    if (i >= 255)
		throw new IOException("longer name");
	    w2(outputstream, i_3_);
	    outputstream.write(head);
	    w2(outputstream, ID);
	    if (bool) {
		outputstream.write(i);
		if (i > 0)
		    outputstream.write(bName);
	    } else
		outputstream.write(0);
	    if (seekMax > 0)
		outputstream.write(data, 0, seekMax);
	}
    }
    
    public String getUserName() {
	try {
	    if (bName != null)
		return new String(bName, "UTF8");
	} catch (java.io.UnsupportedEncodingException unsupportedencodingexception) {
	    /* empty */
	}
	return "";
    }
    
    public void setUserName(Object object) {
	try {
	    if (object == null)
		bName = null;
	    else if (object instanceof String) {
		String string = (String) object;
		bName = string.length() <= 0 ? null : string.getBytes("UTF8");
	    } else
		bName = (byte[]) object;
	} catch (java.io.UnsupportedEncodingException unsupportedencodingexception) {
	    bName = null;
	}
    }
    
    public int getValueSize() {
	toBin(false);
	return seekMax;
    }
    
    private final int r(InputStream inputstream) throws IOException {
	int i = inputstream.read();
	if (i == -1)
	    throw new EOFException();
	return i;
    }
    
    private final int r2(InputStream inputstream) throws IOException {
	return r(inputstream) << 8 | r(inputstream);
    }
    
    private final void r(InputStream inputstream, byte[] is, int i)
	throws IOException {
	int i_5_;
	for (int i_4_ = 0; i_4_ < i; i_4_ += i_5_) {
	    i_5_ = inputstream.read(is, i_4_, i - i_4_);
	    if (i_5_ == -1)
		throw new EOFException();
	}
    }
    
    private final void w2(OutputStream outputstream, int i)
	throws IOException {
	outputstream.write(i >>> 8 & 0xff);
	outputstream.write(i & 0xff);
    }
    
    public void setData(int i, byte i_6_, byte[] is) {
	int i_7_ = is.length;
	head = i_6_;
	ID = i;
	bName = null;
	if (data == null || data.length < i_7_)
	    data = is;
	else
	    System.arraycopy(is, 0, data, 0, i_7_);
	seekMax = i_7_;
	strData = null;
    }
    
    public void setData(int i, byte i_8_, String string) {
	head = i_8_;
	ID = i;
	bName = null;
	strData = string;
	seekMax = 0;
    }
    
    public int setData(InputStream inputstream) throws IOException {
	try {
	    strData = null;
	    int i = r2(inputstream);
	    head = (byte) r(inputstream);
	    ID = r2(inputstream);
	    int i_9_ = r(inputstream);
	    if (i_9_ <= 0)
		bName = null;
	    else {
		if (i_9_ >= 255)
		    throw new IOException("abnormal");
		bName = new byte[i_9_];
		r(inputstream, bName, i_9_);
	    }
	    int i_10_ = i - 1 - 2 - 1 - i_9_;
	    if (i_10_ > 0) {
		if (i_10_ >= 1024)
		    throw new IOException("abnormal");
		if (data == null || data.length < i_10_)
		    data = new byte[i_10_];
		r(inputstream, data, i_10_);
		seekMax = i_10_;
	    } else
		seekMax = 0;
	    return i + 2;
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	    head = (byte) 100;
	    ID = 0;
	    bName = null;
	    seekMax = 0;
	    return 2;
	}
    }
    
    public void setData(MgText mgtext_11_) {
	head = mgtext_11_.head;
	ID = mgtext_11_.ID;
	bName = mgtext_11_.bName;
	seekMax = mgtext_11_.seekMax;
	if (seekMax > 0) {
	    if (data == null || data.length < seekMax)
		data = new byte[seekMax];
	    System.arraycopy(mgtext_11_.data, 0, data, 0, seekMax);
	}
	strData = mgtext_11_.strData;
    }
    
    public final void toBin(boolean bool) {
	if (seekMax == 0 && strData != null && strData.length() > 0) {
	    try {
		byte[] is = strData.getBytes("UTF8");
		int i = is.length;
		if (data == null || data.length < i)
		    data = is;
		else
		    System.arraycopy(is, 0, data, 0, i);
		seekMax = i;
	    } catch (java.io.UnsupportedEncodingException unsupportedencodingexception) {
		strData = null;
		seekMax = 0;
	    }
	}
	if (bool)
	    strData = null;
    }
    
    public String toString() {
	try {
	    if (seekMax > 0 && strData == null)
		strData = new String(data, 0, seekMax, "UTF8");
	    if (strData != null)
		return strData;
	} catch (RuntimeException runtimeexception) {
	    /* empty */
	} catch (java.io.UnsupportedEncodingException unsupportedencodingexception) {
	    /* empty */
	}
	strData = null;
	seekMax = 0;
	return "";
    }
}
