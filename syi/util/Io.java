/* Io - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;
import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;

public class Io
{
    public static void close(Object object) {
	if (object != null) {
	    try {
		if (object instanceof InputStream) {
		    ((InputStream) object).close();
		    return;
		}
		if (object instanceof OutputStream) {
		    ((OutputStream) object).close();
		    return;
		}
		if (object instanceof Reader) {
		    ((Reader) object).close();
		    return;
		}
		if (object instanceof Writer) {
		    ((Writer) object).close();
		    return;
		}
		if (object instanceof RandomAccessFile) {
		    ((RandomAccessFile) object).close();
		    return;
		}
	    } catch (IOException ioexception) {
		/* empty */
	    }
	    try {
		object.getClass().getMethod("close", new Class[0])
		    .invoke(object, new Object[0]);
	    } catch (NoSuchMethodException nosuchmethodexception) {
		/* empty */
	    } catch (IllegalAccessException illegalaccessexception) {
		/* empty */
	    } catch (java.lang.reflect.InvocationTargetException invocationtargetexception) {
		/* empty */
	    }
	}
    }
    
    public static void copyDirectory(File file, File file_0_) {
	copyDirectory(file, file_0_, null);
    }
    
    public static void copyDirectory(File file, File file_1_, Vector vector) {
	if (!file_1_.isDirectory())
	    file_1_.mkdirs();
	String[] strings = file.list();
	for (int i = 0; i < strings.length; i++) {
	    File file_2_ = new File(file, strings[i]);
	    if (file_2_.isFile())
		copyFile(new File(file, strings[i]), file_1_, vector);
	    else
		copyDirectory(new File(file, strings[i]),
			      new File(file_1_, strings[i]), vector);
	}
    }
    
    public static void copy(File file, File file_3_, Vector vector) {
	if (file.isFile())
	    copyFile(file, file_3_, vector);
	else if (file.isDirectory())
	    copyDirectory(file, file_3_, vector);
    }
    
    public static boolean copyFile(File file, File file_4_) {
	try {
	    File file_5_ = getDirectory(file_4_);
	    if (!file_5_.isDirectory())
		file_5_.mkdirs();
	    if (file_4_.isDirectory())
		file_4_ = new File(file_4_, getFileName(file.toString()));
	    byte[] is = new byte[512];
	    FileInputStream fileinputstream = new FileInputStream(file);
	    FileOutputStream fileoutputstream = new FileOutputStream(file_4_);
	    int i;
	    while ((i = fileinputstream.read(is)) != -1)
		fileoutputstream.write(is, 0, i);
	    fileoutputstream.flush();
	    fileoutputstream.close();
	    fileinputstream.close();
	    return true;
	} catch (IOException ioexception) {
	    ioexception.printStackTrace();
	    return false;
	}
    }
    
    public static boolean copyFile(File file, File file_6_, Vector vector) {
	try {
	    if (file.isDirectory())
		throw new IOException("src is directory");
	    if (vector != null) {
		boolean bool = false;
		String string
		    = getFileName(file.getCanonicalPath()).toLowerCase();
		Enumeration enumeration = vector.elements();
		while (enumeration.hasMoreElements()) {
		    String string_7_ = enumeration.nextElement().toString();
		    if (string_7_.endsWith("*")
			|| string.endsWith(string_7_)) {
			bool = true;
			break;
		    }
		}
		if (!bool)
		    return false;
	    }
	    copyFile(file, file_6_);
	    return true;
	} catch (IOException ioexception) {
	    ioexception.printStackTrace();
	    return false;
	}
    }
    
    public static void copyFiles(File[] files, File file) {
	for (int i = 0; i < files.length; i++)
	    copyFile(files[i], file);
    }
    
    public static void copyFiles(String[] strings, String string) {
	for (int i = 0; i < strings.length; i++)
	    copyFile(new File(strings[i]), new File(string));
    }
    
    public static String getCurrent() {
	return System.getProperty("user.dir", "/");
    }
    
    public static final String getDateString(String string, String string_8_,
					     String string_9_) {
	String string_10_ = String.valueOf('.') + string_8_;
	String string_11_ = null;
	File file = new File(string_9_);
	if (!file.isDirectory())
	    file.mkdirs();
	try {
	    GregorianCalendar gregoriancalendar = new GregorianCalendar();
	    String string_12_ = (string + gregoriancalendar.get(2) + '-'
				 + gregoriancalendar.get(5) + '_');
	    int i;
	    for (i = 0; i < 256; i++) {
		File file_13_
		    = new File(string_9_, string_12_ + i + string_10_);
		if (!file_13_.isFile()) {
		    string_11_ = string_9_ + "/" + string_12_ + i + string_10_;
		    break;
		}
	    }
	    if (i >= 32767)
		string_11_
		    = string_9_ + "/" + string + "over_file255" + string_10_;
	} catch (RuntimeException runtimeexception) {
	    string_11_ = string + "." + string_8_;
	}
	return string_11_;
    }
    
    public static File getDirectory(File file) {
	try {
	    return new File(getDirectory(file.getCanonicalPath()));
	} catch (IOException ioexception) {
	    return null;
	}
    }
    
    public static String getDirectory(String string) {
	if (string == null || string.length() <= 0)
	    return "./";
	if (string.indexOf('\\') >= 0)
	    string = string.replace('\\', '/');
	int i = string.lastIndexOf('/');
	if (i < 0)
	    return "./";
	int i_14_ = string.indexOf('.', i);
	if (i_14_ < i) {
	    if (string.charAt(string.length() - 1) != '/')
		string += '/';
	} else
	    string = string.substring(0, i + 1);
	return string;
    }
    
    public static String getFileName(String string) {
	if (string.lastIndexOf('.') < 0)
	    return "";
	int i = string.lastIndexOf('/');
	if (i < 0)
	    i = string.lastIndexOf('\\');
	if (i < 0)
	    return string;
	return string.substring(i + 1);
    }
    
    public static void initFile(File file) {
	try {
	    File file_15_ = getDirectory(file);
	    if (!file_15_.isDirectory())
		file_15_.mkdirs();
	} catch (RuntimeException runtimeexception) {
	    /* empty */
	}
    }
    
    public static Image loadImageNow(Component component, String string) {
	Image image = null;
	try {
	    image = component.getToolkit().getImage(string);
	    MediaTracker mediatracker = new MediaTracker(component);
	    mediatracker.addImage(image, 0);
	    mediatracker.waitForID(0, 10000L);
	    mediatracker.removeImage(image);
	    Object object = null;
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
	return image;
    }
    
    public static String loadString(File file) throws IOException {
	StringBuffer stringbuffer = new StringBuffer();
	BufferedReader bufferedreader
	    = new BufferedReader(new FileReader(file));
	int i;
	while ((i = bufferedreader.read()) != -1)
	    stringbuffer.append((char) i);
	bufferedreader.close();
	return stringbuffer.toString();
    }
    
    public static File makeFile(String string, String string_16_) {
	File file = toDir(string);
	if (!file.exists())
	    file.mkdirs();
	return new File(file, string_16_);
    }
    
    public static final void moveFile(File file, File file_17_)
	throws Throwable {
	if (!file.renameTo(file_17_)) {
	    copyFile(file, file_17_);
	    file.delete();
	}
    }
    
    public static final int r(InputStream inputstream) throws IOException {
	int i = inputstream.read();
	if (i == -1)
	    throw new EOFException();
	return i;
    }
    
    public static final int readInt(InputStream inputstream)
	throws IOException {
	int i = inputstream.read();
	int i_18_ = inputstream.read();
	int i_19_ = inputstream.read();
	int i_20_ = inputstream.read();
	if ((i | i_18_ | i_19_ | i_20_) < 0)
	    throw new EOFException();
	return (i << 24) + (i_18_ << 16) + (i_19_ << 8) + i_20_;
    }
    
    public static final short readShort(InputStream inputstream)
	throws IOException {
	int i = inputstream.read();
	int i_21_ = inputstream.read();
	if ((i | i_21_) < 0)
	    throw new EOFException();
	return (short) ((i << 8) + i_21_);
    }
    
    public static final int readUShort(InputStream inputstream)
	throws IOException {
	int i = inputstream.read();
	int i_22_ = inputstream.read();
	if ((i | i_22_) < 0)
	    throw new EOFException();
	return (i << 8) + i_22_;
    }
    
    public static final void rFull
	(InputStream inputstream, byte[] is, int i, int i_23_)
	throws EOFException, IOException {
	int i_24_;
	for (i_23_ += i; i < i_23_; i += i_24_) {
	    i_24_ = inputstream.read(is, i, i_23_ - i);
	    if (i_24_ == -1)
		throw new EOFException();
	}
    }
    
    public static File toDir(String string) {
	if (string == null || string.length() <= 0)
	    return new File("./");
	if (string.indexOf('\\') >= 0)
	    string = string.replace('\\', '/');
	if (string.charAt(string.length() - 1) != '/')
	    string += '/';
	return new File(string);
    }
    
    public static final void wInt(OutputStream outputstream, int i)
	throws IOException {
	outputstream.write(i >>> 24);
	outputstream.write(i >>> 16 & 0xff);
	outputstream.write(i >>> 8 & 0xff);
	outputstream.write(i & 0xff);
    }
    
    public static final void wShort(OutputStream outputstream, int i)
	throws IOException {
	outputstream.write(i >>> 8 & 0xff);
	outputstream.write(i & 0xff);
    }
}
