/* Res - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat;
import java.applet.Applet;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import syi.awt.Awt;
import syi.util.ByteStream;

public class Res extends Hashtable
{
    private Object resBase;
    private Applet applet;
    private ByteStream work;
    private static final String EMPTY = "";
    
    public Res() {
	this(null, null, null);
    }
    
    public Res(Applet applet, Object object, ByteStream bytestream) {
	resBase = object;
	this.applet = applet;
	work = bytestream;
    }
    
    public final String get(String string) {
	return get(string, "");
    }
    
    public final String get(String string, String string_0_) {
	if (string == null)
	    return string_0_;
	String string_1_ = (String) super.get(string);
	if (string_1_ == null)
	    return string_0_;
	return string_1_;
    }
    
    public final boolean getBool(String string) {
	return getBool(string, false);
    }
    
    public final boolean getBool(String string, boolean bool) {
	try {
	    string = get(string);
	    if (string == null || string.length() <= 0)
		return bool;
	    char c = string.charAt(0);
	    switch (c) {
	    case '1':
	    case 'o':
	    case 't':
	    case 'y':
		return true;
	    case '0':
	    case 'f':
	    case 'n':
	    case 'x':
		return false;
	    }
	} catch (RuntimeException runtimeexception) {
	    /* empty */
	}
	return bool;
    }
    
    public ByteStream getBuffer() {
	if (work == null)
	    work = new ByteStream();
	else
	    work.reset();
	return work;
    }
    
    public final int getInt(String string) {
	try {
	    return getInt(string, 0);
	} catch (Exception exception) {
	    return 0;
	}
    }
    
    public final int getInt(String string, int i) {
	try {
	    String string_2_ = get(string);
	    if (string_2_ != null && string_2_.length() > 0)
		return parseInt(string_2_);
	} catch (Throwable throwable) {
	    /* empty */
	}
	return i;
    }
    
    public String getP(String string) {
	String string_3_ = p(string);
	if (string_3_ != null)
	    return string_3_;
	return get(string, null);
    }
    
    public final int getP(String string, int i) {
	String string_4_ = p(string);
	if (string_4_ != null)
	    this.put(string, string_4_);
	return getInt(string, i);
    }
    
    public String getP(String string, String string_5_) {
	String string_6_ = p(string);
	return (string_6_ == null || string_6_.length() <= 0
		? get(string, string_5_) : string_6_);
    }
    
    public boolean getP(String string, boolean bool) {
	String string_7_ = p(string);
	if (string_7_ != null)
	    this.put(string, string_7_);
	return getBool(string, bool);
    }
    
    public final Object getRes(Object object) {
	try {
	    Object object_8_ = this.get(object);
	    if (object_8_ == null) {
		ByteStream bytestream = getBuffer();
		bytestream.write(Awt.openStream(resBase instanceof String
						? new URL(applet.getCodeBase(),
							  ((String) resBase
							   + (String) object))
						: new URL((URL) resBase,
							  (String) object)));
		return bytestream.toByteArray();
	    }
	    return object_8_;
	} catch (IOException ioexception) {
	    return null;
	}
    }
    
    public boolean load(InputStream inputstream) {
	try {
	    return load(new InputStreamReader(inputstream, "UTF8"));
	} catch (java.io.UnsupportedEncodingException unsupportedencodingexception) {
	    return false;
	}
    }
    
    public boolean load(Reader reader) {
	try {
	    Reader reader_9_
		= (reader instanceof StringReader ? (Reader) reader
		   : new BufferedReader(reader, 512));
	    CharArrayWriter chararraywriter = new CharArrayWriter();
	    String string = null;
	    try {
		for (;;) {
		    String string_10_ = readLine(reader_9_);
		    if (string_10_ != null) {
			int i = string_10_.indexOf('=');
			if (i > 0) {
			    if (string != null) {
				this.put(string, chararraywriter.toString());
				string = null;
			    }
			    string = string_10_.substring(0, i).trim();
			    chararraywriter.reset();
			    if (i + 1 < string_10_.length())
				chararraywriter
				    .write(string_10_.substring(i + 1));
			} else if (string != null) {
			    chararraywriter.write('\n');
			    chararraywriter.write(string_10_);
			}
		    }
		}
	    } catch (EOFException eofexception) {
		if (string != null && chararraywriter.size() > 0)
		    this.put(string, chararraywriter.toString());
		reader_9_.close();
	    }
	} catch (IOException ioexception) {
	    ioexception.printStackTrace();
	    return false;
	}
	return true;
    }
    
    public void load(String string) {
	if (string != null && string.length() > 0)
	    load(new StringReader(string));
    }
    
    public void loadResource(Res res_11_, String string, String string_12_) {
	if (string_12_ != null && string_12_.equals("ja")) {
	    /* empty */
	}
	String string_13_
	    = string + (string_12_ != null && string_12_.length() != 0
			? String.valueOf('_') + string_12_ : "") + ".txt";
	for (int i = 0; i < 2; i++) {
	    try {
		byte[] is = (byte[]) res_11_.getRes(string_13_);
		if (is != null) {
		    ByteArrayInputStream bytearrayinputstream
			= new ByteArrayInputStream(is);
		    load(new InputStreamReader(bytearrayinputstream, "UTF8"));
		    break;
		}
	    } catch (RuntimeException runtimeexception) {
		/* empty */
	    } catch (java.io.UnsupportedEncodingException unsupportedencodingexception) {
		/* empty */
	    }
	    string_13_ = string + ".txt";
	}
    }
    
    public void loadZip(InputStream inputstream) throws IOException {
	ByteStream bytestream = getBuffer();
	ZipInputStream zipinputstream = new ZipInputStream(inputstream);
	ZipEntry zipentry;
	while ((zipentry = zipinputstream.getNextEntry()) != null) {
	    bytestream.reset();
	    bytestream.write(zipinputstream);
	    r(bytestream, zipentry.getName());
	}
	zipinputstream.close();
    }
    
    public void loadZip(String string) throws IOException {
	try {
	    InputStream inputstream
		= this.getClass()
		      .getResourceAsStream(String.valueOf('/') + string);
	    if (inputstream != null) {
		loadZip(inputstream);
		return;
	    }
	} catch (Throwable throwable) {
	    /* empty */
	}
	loadZip(Awt.openStream(new URL(applet.getCodeBase(), string)));
    }
    
    private String p(String string) {
	return applet.getParameter(string);
    }
    
    public static final int parseInt(String string) {
	int i = string.length();
	if (i <= 0)
	    return 0;
	int i_14_ = 0;
	if (string.charAt(0) == '0')
	    i_14_ = 2;
	else if (string.charAt(0) == '#')
	    i_14_ = 1;
	if (i_14_ != 0) {
	    int i_15_ = 0;
	    i -= i_14_;
	    for (int i_16_ = 0; i_16_ < i; i_16_++)
		i_15_ |= (Character.digit(string.charAt(i_16_ + i_14_), 16)
			  << (i - 1 - i_16_) * 4);
	    return i_15_;
	}
	return Integer.parseInt(string);
    }
    
    private void r(ByteStream bytestream, String string) throws IOException {
	String string_17_ = string.toLowerCase();
	if (string_17_.endsWith("zip"))
	    loadZip(new ByteArrayInputStream(bytestream.toByteArray()));
	else
	    this.put(string, bytestream.toByteArray());
    }
    
    private final String readLine(Reader reader)
	throws EOFException, IOException {
	int i = reader.read();
	if (i == -1)
	    throw new EOFException();
	if (i == 13 || i == 10)
	    return null;
	if (i == 35) {
	    do
		i = reader.read();
	    while (i != 13 && i != 10 && i != -1);
	    return null;
	}
	StringBuffer stringbuffer = new StringBuffer();
	stringbuffer.append((char) i);
	while ((i = reader.read()) != -1) {
	    if (i == 13 || i == 10)
		break;
	    stringbuffer.append((char) i);
	}
	return stringbuffer.toString();
    }
    
    public final String res(String string) {
	return getP(string, string);
    }
    
    public void put(Res res_18_) {
	Enumeration enumeration = res_18_.keys();
	while (enumeration.hasMoreElements()) {
	    Object object = enumeration.nextElement();
	    this.put(object, res_18_.get(object));
	}
    }
}
