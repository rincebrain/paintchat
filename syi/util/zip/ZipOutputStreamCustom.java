/* ZipOutputStreamCustom - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util.zip;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import syi.util.ByteStream;
import syi.util.Io;

public class ZipOutputStreamCustom extends ZipOutputStream
{
    private boolean is_deflate = true;
    private CRC32 crc32 = null;
    
    public ZipOutputStreamCustom(OutputStream outputstream) {
	super(outputstream);
    }
    
    public static void main(String[] strings) {
	try {
	    File file
		= (new File
		   ("C:\\IBMVJava\\Ide\\project_resources\\PaintChat\\sub\\bbs\\pbbs\\"));
	    ZipOutputStreamCustom zipoutputstreamcustom
		= (new ZipOutputStreamCustom
		   (new FileOutputStream(new File(file, "_PaintBBS.jar"))));
	    zipoutputstreamcustom.setMethod(8);
	    zipoutputstreamcustom.setLevel(9);
	    zipoutputstreamcustom.putZip
		(new ZipInputStream
		 (new FileInputStream(new File(file, "PaintBBS.jar"))));
	    zipoutputstreamcustom.finish();
	    zipoutputstreamcustom.close();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
	System.exit(0);
    }
    
    public void putBytes(byte[] is, int i, int i_0_, String string)
	throws IOException {
	ZipEntry zipentry = new ZipEntry(string);
	if (!is_deflate) {
	    if (crc32 == null)
		crc32 = new CRC32();
	    else
		crc32.reset();
	    crc32.update(is);
	    zipentry.setSize((long) is.length);
	    zipentry.setCrc(crc32.getValue());
	}
	this.putNextEntry(zipentry);
	this.write(is, i, i_0_);
	this.closeEntry();
    }
    
    public void putDirectory(File file, String string) throws IOException {
	File file_1_ = new File(file, string);
	if (file_1_.isFile())
	    putFile(file, string);
	else if (file_1_.exists()) {
	    String[] strings = file_1_.list();
	    for (int i = 0; i < strings.length; i++) {
		new File(file_1_, strings[i]);
		putDirectory(file, string + '/' + strings[i]);
	    }
	}
    }
    
    public void putFile(File file, String string) throws IOException {
	byte[] is = null;
	File file_2_ = new File(file, string);
	if (file_2_.isDirectory())
	    putDirectory(file, string);
	else if (file_2_.exists()) {
	    try {
		is = new byte[(int) file_2_.length()];
		FileInputStream fileinputstream = new FileInputStream(file_2_);
		Io.rFull(fileinputstream, is, 0, is.length);
		fileinputstream.close();
	    } catch (IOException ioexception) {
		ioexception.printStackTrace();
		is = null;
	    }
	    if (is != null)
		putBytes(is, 0, is.length, string);
	}
    }
    
    public void putFile(String string, String string_3_) throws IOException {
	putFile(new File(string), string_3_);
    }
    
    public void putZip(ZipInputStream zipinputstream) throws IOException {
	ByteStream bytestream = new ByteStream();
	byte[] is = new byte[512];
	ZipEntry zipentry;
	while ((zipentry = zipinputstream.getNextEntry()) != null) {
	    int i;
	    while ((i = zipinputstream.read(is)) != -1)
		bytestream.write(is, 0, i);
	    zipinputstream.closeEntry();
	    putBytes(bytestream.getBuffer(), 0, bytestream.size(),
		     zipentry.getName());
	    bytestream.reset();
	}
	zipinputstream.close();
    }
    
    public void setMethod(int i) {
	is_deflate = i == 8;
	super.setMethod(i);
    }
}
