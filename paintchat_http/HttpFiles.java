/* HttpFiles - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_http;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import paintchat.Config;
import paintchat.Res;

import syi.util.Io;

public class HttpFiles
{
    private String STR_SLASH = "//";
    private String STR_DOT = "..";
    private File dirWWW;
    private File dirTmp;
    private Config config;
    private Res res;
    static final FileNotFoundException NOT_FOUND = new FileNotFoundException();
    private byte[] bErrorUpper = null;
    private byte[] bErrorBottom = null;
    private byte[][] res_bytes = new byte[6][];
    private static final int I_OK = 0;
    private static final int I_NOT_MODIFIED = 1;
    private static final int I_MOVED_PERMANENTLY = 2;
    private static final int I_NOT_FOUND = 3;
    private static final int I_SERVER_ERROR = 4;
    private static final int I_SERVICE_UNAVALIABLE = 5;
    
    public HttpFiles(Config config, Res res) {
	this.config = config;
	this.res = res;
	dirWWW = new File(config.getString("Http_Dir", "www"));
	dirTmp = new File("cnf/template/");
    }
    
    public String addIndex(String string) {
	int i = string.length();
	if (i == 0 || string.charAt(i - 1) != '/')
	    return string + "/index.html";
	return string + "index.html";
    }
    
    public void getErrorMessage(OutputStream outputstream, int i, byte[] is,
				int i_0_, int i_1_) throws IOException {
	setup();
	outputstream.write(bErrorUpper);
	outputstream.write(is, i_0_, i_1_);
	byte[] is_2_ = res_bytes[i];
	if (is_2_ != null)
	    outputstream.write(is_2_);
	outputstream.write(bErrorBottom);
    }
    
    public int getErrorMessageSize(int i) throws IOException {
	setup();
	int i_3_ = 0;
	byte[] is = res_bytes[i];
	if (is != null)
	    i_3_ += is.length;
	return bErrorUpper.length + i_3_ + bErrorBottom.length;
    }
    
    public File getFile(String string) throws FileNotFoundException {
	File file = new File(dirWWW, string);
	if (!file.isFile()) {
	    file = new File(dirTmp, string);
	    if (!file.isFile())
		file = new File(dirTmp, Io.getFileName(string));
	}
	if (!file.isFile())
	    throw NOT_FOUND;
	return file;
    }
    
    private int indexOf(byte[] is, byte[] is_4_) {
	int i = is.length;
	int i_5_ = 0;
	for (int i_6_ = 0; i_6_ < i; i_6_++) {
	    if (is[i_6_] == is_4_[i_5_]) {
		if (i_5_ >= is_4_.length - 1)
		    return i_6_ - i_5_;
		i_5_++;
	    } else
		i_5_ = 0;
	}
	return -1;
    }
    
    public boolean needMove(String string) {
	int i = Math.max(string.lastIndexOf('/'), 0);
	return string.lastIndexOf('.') <= i;
    }
    
    private String replaceText(String string, String string_7_,
			       String string_8_) {
	if (string.indexOf(string_7_) < 0)
	    return string;
	try {
	    int i_9_;
	    for (int i = 0; (i_9_ = string.indexOf(string_7_, i)) < 0;
		 i += i_9_)
		string = (string.substring(0, i) + string_8_
			  + string.substring(i + string_7_.length()));
	} catch (RuntimeException runtimeexception) {
	    System.out.println("replace" + runtimeexception);
	}
	return string;
    }
    
    private void setup() {
	object = object_11_;
	break while_5_;
    }
    
    public String uriToPath(String string) {
	string = string.replace('\\', '/');
	string = string.replace('\0', '_');
	string = string.replace('\n', '_');
	string = string.replace('\r', '_');
	StringBuffer stringbuffer = null;
	if (string.indexOf(STR_SLASH) >= 0 || string.indexOf(STR_DOT) >= 0) {
	    stringbuffer = new StringBuffer();
	    int i = string.length();
	    int i_13_ = 0;
	    boolean bool = false;
	    boolean bool_14_ = true;
	    stringbuffer.append('.');
	    while (i_13_ < i) {
		char c = string.charAt(i_13_++);
		if ((c != '/' || !bool) && (c != '.' || !bool_14_)) {
		    bool = c == '/';
		    bool_14_ = c == '.';
		    stringbuffer.append(c);
		}
	    }
	    string = stringbuffer.toString();
	}
	if (string.length() == 0)
	    return "./index.html";
	if (string.charAt(0) == '/') {
	    if (stringbuffer == null) {
		stringbuffer = new StringBuffer(string.length() + 1);
		stringbuffer.append('.');
		stringbuffer.append(string);
	    } else
		stringbuffer = stringbuffer.insert(0, '.');
	    string = stringbuffer.toString();
	}
	if (string.charAt(string.length() - 1) == '/')
	    return addIndex(string);
	return string;
    }
}
