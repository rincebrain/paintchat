/* CustomDeflaterOutputStream - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util.zip;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import syi.util.ByteStream;
import syi.util.Io;

public class CustomDeflaterOutputStream
{
    private ByteStream stm = new ByteStream();
    private ByteStream work = new ByteStream();
    private byte[] buffer = new byte[32767];
    private OutputStream Out;
    private boolean isOriginal = false;
    private ZipOutputStream OutZip;
    private CRC32 crc = new CRC32();
    
    public CustomDeflaterOutputStream(OutputStream outputstream) {
	this(outputstream, false);
    }
    
    public CustomDeflaterOutputStream(OutputStream outputstream,
				      boolean bool) {
	Out = outputstream;
	isOriginal = bool;
	if (!bool) {
	    OutZip = new ZipOutputStream(stm);
	    OutZip.setMethod(0);
	    OutZip.setLevel(0);
	}
    }
    
    public void close() throws IOException {
	close(null);
    }
    
    public void close(String string) throws IOException {
	if (!isOriginal) {
	    OutZip.close();
	    ZipOutputStream zipoutputstream = new ZipOutputStream(Out);
	    zipoutputstream.setLevel(9);
	    zipoutputstream.putNextEntry(new ZipEntry((string == null
						       || string.length() == 0)
						      ? "0.zip" : string));
	    stm.writeTo(zipoutputstream);
	    zipoutputstream.flush();
	    zipoutputstream.closeEntry();
	    zipoutputstream.close();
	} else {
	    Deflater deflater = new Deflater(9, true);
	    deflater.setInput(stm.getBuffer(), 0, stm.size());
	    deflater.finish();
	    while (!deflater.finished()) {
		int i = deflater.deflate(buffer, 0, 1);
		if (i > 0)
		    Out.write(buffer, 0, i);
	    }
	}
	try {
	    Out.flush();
	    Out.close();
	} catch (IOException ioexception) {
	    ioexception.printStackTrace();
	}
    }
    
    public static void compress(File file, File file_0_) {
	try {
	    CustomDeflaterOutputStream customdeflateroutputstream
		= new CustomDeflaterOutputStream(new FileOutputStream(file_0_),
						 false);
	    customdeflateroutputstream.write(file);
	    customdeflateroutputstream.close(file.getName());
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    public static void main(String[] strings) {
	try {
	    File file = new File(strings[0] + ".tmp");
	    File file_1_ = new File(strings[0]);
	    compress(file_1_, file);
	    file_1_.delete();
	    file.renameTo(file_1_);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
	System.exit(0);
    }
    
    public void write(File file) throws IOException {
	ZipInputStream zipinputstream
	    = new ZipInputStream(new FileInputStream(file));
	if (isOriginal) {
	    ZipEntry zipentry;
	    while ((zipentry = zipinputstream.getNextEntry()) != null) {
		work.reset();
		work.write(zipentry.getName().getBytes());
		work.write(0);
		int i;
		while ((i = zipinputstream.read(buffer)) != -1)
		    work.write(buffer, 0, i);
		if (work.size() > 0) {
		    Io.wShort(stm, work.size() & 0xffff);
		    work.writeTo(stm);
		}
	    }
	} else {
	    ZipEntry zipentry;
	    while ((zipentry = zipinputstream.getNextEntry()) != null) {
		work.reset();
		int i;
		while ((i = zipinputstream.read(buffer)) != -1)
		    work.write(buffer, 0, i);
		if (work.size() > 0) {
		    crc.reset();
		    crc.update(work.getBuffer(), 0, work.size());
		    ZipEntry zipentry_2_ = new ZipEntry(zipentry.getName());
		    zipentry_2_.setSize((long) work.size());
		    zipentry_2_.setCrc(crc.getValue());
		    OutZip.putNextEntry(zipentry_2_);
		    work.writeTo(OutZip);
		    OutZip.flush();
		    OutZip.closeEntry();
		}
	    }
	}
	zipinputstream.close();
    }
    
    public void write(ByteStream bytestream) throws IOException {
	ZipInputStream zipinputstream
	    = new ZipInputStream(new ByteArrayInputStream(bytestream
							      .getBuffer(),
							  0,
							  bytestream.size()));
	if (isOriginal) {
	    ZipEntry zipentry;
	    while ((zipentry = zipinputstream.getNextEntry()) != null) {
		work.reset();
		work.write(zipentry.getName().getBytes());
		work.write(0);
		int i;
		while ((i = zipinputstream.read(buffer)) != -1)
		    work.write(buffer, 0, i);
		if (work.size() > 0) {
		    Io.wShort(bytestream, work.size() & 0xffff);
		    work.writeTo(bytestream);
		}
	    }
	} else {
	    ZipEntry zipentry;
	    while ((zipentry = zipinputstream.getNextEntry()) != null) {
		work.reset();
		int i;
		while ((i = zipinputstream.read(buffer)) != -1)
		    work.write(buffer, 0, i);
		if (work.size() > 0) {
		    crc.reset();
		    crc.update(work.getBuffer(), 0, work.size());
		    ZipEntry zipentry_3_ = new ZipEntry(zipentry.getName());
		    zipentry_3_.setSize((long) work.size());
		    zipentry_3_.setCrc(crc.getValue());
		    OutZip.putNextEntry(zipentry_3_);
		    work.writeTo(OutZip);
		    OutZip.flush();
		    OutZip.closeEntry();
		}
	    }
	}
	zipinputstream.close();
    }
}
