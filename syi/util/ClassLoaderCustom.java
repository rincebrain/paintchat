/* ClassLoaderCustom - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ClassLoaderCustom extends ClassLoader
{
    private Hashtable cash = new Hashtable();
    private ClassLoader cl = this.getClass().getClassLoader();
    /*synthetic*/ static Class class$0;
    
    public boolean loadArchive(String string) {
	try {
	    InputStream inputstream;
	    if (string.indexOf("://") != -1)
		inputstream = new URL(string).openStream();
	    else
		inputstream
		    = new FileInputStream(new File(Io.getCurrent(), string));
	    ZipInputStream zipinputstream = new ZipInputStream(inputstream);
	    byte[] is = new byte[4000];
	    ByteArrayOutputStream bytearrayoutputstream
		= new ByteArrayOutputStream(4000);
	    ZipEntry zipentry;
	    while ((zipentry = zipinputstream.getNextEntry()) != null) {
		String string_0_
		    = replace(zipentry.getName(), '/', '.', false);
		if (string_0_.endsWith(".class")) {
		    bytearrayoutputstream.reset();
		    int i;
		    while ((i = zipinputstream.read(is)) != -1)
			bytearrayoutputstream.write(is, 0, i);
		    bytearrayoutputstream.flush();
		    zipinputstream.closeEntry();
		    byte[] is_1_ = bytearrayoutputstream.toByteArray();
		    String string_2_
			= string_0_.substring(0, string_0_.length() - 6);
		    Class var_class
			= this.defineClass(string_2_, is_1_, 0, is_1_.length);
		    is_1_ = null;
		    if (var_class != null)
			cash.put(string_2_, var_class);
		    Object object = null;
		}
	    }
	    zipinputstream.close();
	    return true;
	} catch (Exception exception) {
	    System.out.println(exception.getMessage());
	    return false;
	}
    }
    
    public Class loadClass(String string, String string_3_, boolean bool) {
	Class var_class = (Class) cash.get(string);
	if (var_class == null) {
	    if (string_3_ == null || !string_3_.startsWith("http://"))
		var_class = loadLocal(string, string_3_);
	    else
		var_class = loadURL(string, string_3_);
	    if (var_class == null) {
		Class var_class_4_ = class$0;
		if (var_class_4_ == null) {
		    Class var_class_5_;
		    try {
			var_class_5_ = Class.forName("java.lang.Object");
		    } catch (ClassNotFoundException classnotfoundexception) {
			NoClassDefFoundError noclassdeffounderror
			    = new NoClassDefFoundError();
			((UNCONSTRUCTED)noclassdeffounderror)
			    .NoClassDefFoundError
			    (classnotfoundexception.getMessage());
			throw noclassdeffounderror;
		    }
		    var_class_4_ = class$0 = var_class_5_;
		}
		var_class = var_class_4_;
	    }
	    cash.put(string, var_class);
	}
	if (bool)
	    this.resolveClass(var_class);
	return var_class;
    }
    
    protected Class loadClass(String string, boolean bool)
	throws ClassNotFoundException {
	Class var_class = (Class) cash.get(string);
	if (var_class == null)
	    return this.findSystemClass(string);
	if (bool)
	    this.resolveClass(var_class);
	return var_class;
    }
    
    private Class loadLocal(String string, String string_6_) {
	Class var_class;
	try {
	    if (string_6_ == null || string_6_.length() <= 0) {
		var_class = this.findSystemClass(string);
		if (var_class != null)
		    return var_class;
	    }
	    File file = new File(string_6_, replace(string, '.',
						    File.separatorChar, true));
	    if (!file.isFile())
		return null;
	    byte[] is = new byte[(int) file.length()];
	    DataInputStream datainputstream
		= new DataInputStream(new FileInputStream(file));
	    datainputstream.readFully(is);
	    datainputstream.close();
	    var_class = this.defineClass(string, is, 0, is.length);
	} catch (java.io.IOException ioexception) {
	    var_class = null;
	} catch (ClassNotFoundException classnotfoundexception) {
	    var_class = null;
	}
	return var_class;
    }
    
    private Class loadURL(String string, String string_7_) {
	Class var_class;
	try {
	    String string_8_ = replace(string, '\\', '/', true);
	    URL url;
	    if (string_7_.charAt(string_7_.length() - 1) != '/')
		url = new URL(string_7_ + '/' + string_8_);
	    else
		url = new URL(string_7_ + string_8_);
	    Object object = null;
	    URLConnection urlconnection = url.openConnection();
	    urlconnection.connect();
	    int i = urlconnection.getContentLength();
	    byte[] is = new byte[i];
	    int i_9_ = 0;
	    InputStream inputstream = urlconnection.getInputStream();
	    while ((i_9_ += inputstream.read(is, i_9_, i - i_9_)) < i) {
		/* empty */
	    }
	    inputstream.close();
	    var_class = this.defineClass(string, is, 0, i);
	} catch (Exception exception) {
	    var_class = null;
	}
	return var_class;
    }
    
    public void putClass(Class var_class) {
	cash.put(var_class.getName(), var_class);
    }
    
    private String replace(String string, char c, char c_10_, boolean bool) {
	StringBuffer stringbuffer = new StringBuffer(string);
	int i = stringbuffer.length();
	for (int i_11_ = 0; i_11_ < i; i_11_++) {
	    if (stringbuffer.charAt(i_11_) == c)
		stringbuffer.setCharAt(i_11_, c_10_);
	}
	if (bool)
	    stringbuffer.append(".class");
	return stringbuffer.toString();
    }
}
