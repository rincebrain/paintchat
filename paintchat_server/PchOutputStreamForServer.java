/* PchOutputStreamForServer - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

import paintchat.Config;

import syi.util.Io;
import syi.util.VectorBin;

public class PchOutputStreamForServer extends OutputStream
{
    private Config config;
    private PchOutputStream out = null;
    private long lTimer = 0L;
    private boolean isLogEnable = false;
    private boolean isLogLoad = false;
    private int iCashMax;
    private int iLogMax;
    private Deflater deflater;
    private byte[] bDeflate = new byte[70000];
    private VectorBin vAnime;
    
    public PchOutputStreamForServer(Config config) {
	this.config = config;
	mInit();
    }
    
    private void mInit() {
	iCashMax = config.getInt("Server_Cash_Line_Size", 500000);
	iLogMax = 100000000;
	isLogEnable = config.getBool("Server_Log_Line", false);
	isLogLoad = config.getBool("Server_Load_Line", false);
	boolean bool = config.getBool("Server_Cash_Line", true);
	if ((isLogLoad || isLogEnable || bool) && deflater == null)
	    deflater = new Deflater(9, false);
	if (isLogLoad) {
	    File file = getTmpFile();
	    if (file.isFile() && file.length() > 0L) {
		try {
		    PchInputStream pchinputstream
			= (new PchInputStream
			   (new BufferedInputStream(new FileInputStream(file)),
			    iCashMax));
		    vAnime = pchinputstream.getLines();
		} catch (IOException ioexception) {
		    ioexception.printStackTrace();
		}
	    }
	}
	if (vAnime == null)
	    vAnime = new VectorBin();
    }
    
    public void mFinalize() {
	close();
	deflater.end();
    }
    
    public void write(int i) {
	write(new byte[] { (byte) i });
    }
    
    public void write(byte[] is) {
	write(is, 0, is.length);
    }
    
    public void write(byte[] is, int i, int i_0_) {
	try {
	    if (checkTimeout())
		newLog();
	    setupStream();
	    deflater.reset();
	    deflater.setInput(is, i, i_0_);
	    deflater.finish();
	    int i_1_ = 0;
	    while (!deflater.finished())
		i_1_ += deflater.deflate(bDeflate, i_1_, i_0_ - i_1_);
	    byte[] is_2_ = new byte[i_1_];
	    System.arraycopy(bDeflate, 0, is_2_, 0, i_1_);
	    vAnime.add(is_2_);
	    if (vAnime.getSizeBytes() >= iCashMax)
		vAnime.remove(1);
	    if (out != null) {
		out.write(bDeflate, 0, i_1_);
		if (out.size() >= iLogMax) {
		    close();
		    setupStream();
		}
	    }
	} catch (IOException ioexception) {
	    ioexception.printStackTrace();
	    close();
	    isLogEnable = false;
	}
    }
    
    public void flush() {
	try {
	    if (out != null)
		out.flush();
	} catch (IOException ioexception) {
	    close();
	}
    }
    
    public void close() {
	try {
	    if (out != null) {
		out.flush();
		out.close();
	    }
	} catch (IOException ioexception) {
	    ioexception.printStackTrace();
	}
	out = null;
	if (isLogEnable) {
	    try {
		File file = getTmpFile();
		if (file.isFile() && file.length() > 0L) {
		    File file_3_
			= new File(Io.getDateString
				   ("pch", "spch",
				    Io.getDirectory(file.getCanonicalPath())));
		    Io.copyFile(file, file_3_);
		}
	    } catch (IOException ioexception) {
		ioexception.printStackTrace();
	    }
	}
    }
    
    public boolean checkTimeout() {
	if (lTimer != 0L && System.currentTimeMillis() - lTimer >= 86400000L)
	    return true;
	return false;
    }
    
    public void setupStream() throws IOException {
	if (out == null && (isLogEnable || isLogLoad)) {
	    File file = getTmpFile();
	    boolean bool = file.isFile() && file.length() > 0L;
	    out = (new PchOutputStream
		   (new BufferedOutputStream(new FileOutputStream(file, bool)),
		    bool));
	    if (!bool) {
		out.writeHeader("Client_Image_Width",
				config.getString("Client_Image_Width"));
		out.writeHeader("Client_Image_Height",
				config.getString("Client_Image_Height"));
		out.writeHeader("version", "2");
	    }
	    lTimer = System.currentTimeMillis();
	}
    }
    
    public void getLog(VectorBin vectorbin) {
	vAnime.copy(vectorbin);
    }
    
    public void newLog() {
	vAnime.removeAll();
	flush();
	close();
	File file = getTmpFile();
	if (file.isFile())
	    file.delete();
    }
    
    private File getTmpFile() {
	File file = new File(config.getString("Server_Log_Line_Dir",
					      "./save_server/"));
	if (!file.isDirectory())
	    file.mkdirs();
	return new File(file, "line_cash.tmp");
    }
}
