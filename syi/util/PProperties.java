/* PProperties - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;

public class PProperties extends Hashtable
{
    private static final String str_empty = "";
    
    public final boolean getBool(String string) {
	return getBool(string, false);
    }
    
    public final boolean getBool(String string, boolean bool) {
	try {
	    string = (String) this.get(string);
	    if (string == null || string.length() <= 0)
		return bool;
	    char c = Character.toLowerCase(string.charAt(0));
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
	} catch (Exception exception) {
	    /* empty */
	}
	return bool;
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
	    String string_0_ = (String) this.get(string);
	    if (string_0_ != null && string_0_.length() > 0)
		return Integer.decode(string_0_).intValue();
	} catch (Throwable throwable) {
	    /* empty */
	}
	return i;
    }
    
    public final String getString(String string) {
	return getString(string, "");
    }
    
    public final String getString(String string, String string_1_) {
	if (string == null)
	    return string_1_;
	Object object = this.get(string);
	if (object == null)
	    return string_1_;
	return object.toString();
    }
    
    public synchronized boolean load(InputStream inputstream) {
	this.clear();
	return loadPut(inputstream);
    }
    
    public synchronized boolean load(Reader reader) {
	this.clear();
	return loadPut(reader);
    }
    
    public synchronized void load(String string) {
	if (string != null && string.length() > 0)
	    load(new StringReader(string));
    }
    
    public synchronized boolean loadPut(InputStream inputstream) {
	return loadPut(new InputStreamReader(inputstream));
    }
    
    public synchronized boolean loadPut(Reader reader) {
	try {
	    Reader reader_2_
		= (reader instanceof StringReader ? (Reader) reader
		   : new BufferedReader(reader, 1024));
	    CharArrayWriter chararraywriter = new CharArrayWriter();
	    String string = null;
	    try {
		for (;;) {
		    String string_3_ = readLine(reader_2_);
		    if (string_3_ != null) {
			int i = string_3_.indexOf('=');
			if (i > 0) {
			    if (string != null) {
				this.put(string, chararraywriter.toString());
				string = null;
			    }
			    string = string_3_.substring(0, i);
			    chararraywriter.reset();
			    if (i + 1 < string_3_.length())
				chararraywriter
				    .write(string_3_.substring(i + 1));
			} else if (string != null) {
			    chararraywriter.write('\n');
			    chararraywriter.write(string_3_);
			}
		    }
		}
	    } catch (EOFException eofexception) {
		if (string != null && chararraywriter.size() > 0)
		    this.put(string, chararraywriter.toString());
		reader_2_.close();
	    }
	} catch (IOException ioexception) {
	    System.out.println("load" + ioexception.getMessage());
	    return false;
	}
	return true;
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
    
    public void save(OutputStream outputstream) throws Exception {
	save(outputstream, null);
    }
    
    public synchronized void save
	(OutputStream outputstream, Hashtable hashtable) throws IOException {
	PrintWriter printwriter
	    = new PrintWriter(new OutputStreamWriter(outputstream), false);
	boolean bool = hashtable != null;
	Enumeration enumeration = this.keys();
	while (enumeration.hasMoreElements()) {
	    String string = (String) enumeration.nextElement();
	    if (string != null && string.length() > 0
		&& string.charAt(0) != '#') {
		if (bool) {
		    String string_4_ = (String) hashtable.get(string);
		    if (string_4_ != null && string_4_.length() > 0) {
			StringReader stringreader
			    = new StringReader(string_4_);
			try {
			    for (;;) {
				String string_5_ = readLine(stringreader);
				printwriter.print('#');
				printwriter.println(string_5_);
			    }
			} catch (EOFException eofexception) {
			    /* empty */
			}
		    }
		}
		String string_6_ = getString(string);
		printwriter.print(string);
		printwriter.print('=');
		if (string_6_ != null && string_6_.length() > 0)
		    printwriter.print(string_6_);
		printwriter.println();
		printwriter.println();
	    }
	}
	printwriter.flush();
	printwriter.close();
	outputstream.close();
    }
}
