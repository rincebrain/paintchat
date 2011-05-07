/* ChatLogOutputStream - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import paintchat.MgText;
import paintchat.debug.DebugListener;

import syi.util.Io;
import syi.util.Vector2;

public class ChatLogOutputStream extends Writer
{
    private boolean isValidate = true;
    BufferedWriter outLog = null;
    File fDir;
    DebugListener debug;
    long timeStart = 0L;
    
    public ChatLogOutputStream(File file, DebugListener debuglistener,
			       boolean bool) {
	fDir = file;
	debug = debuglistener;
	isValidate = bool;
    }
    
    public synchronized void write(char c) throws IOException {
	try {
	    if (isValidate) {
		initFile();
		if (outLog != null)
		    outLog.write(c);
	    }
	} catch (IOException ioexception) {
	    debug.log(ioexception);
	}
    }
    
    public synchronized void write(char[] cs, int i, int i_0_) {
	try {
	    if (isValidate) {
		initFile();
		if (outLog != null) {
		    outLog.write(cs, i, i_0_);
		    outLog.newLine();
		}
	    }
	} catch (IOException ioexception) {
	    debug.log(ioexception);
	}
    }
    
    public void write(String string) {
	write(string, 0, string.length());
    }
    
    public synchronized void write(String string, int i, int i_1_) {
	try {
	    if (isValidate) {
		initFile();
		if (outLog != null) {
		    outLog.write(string, i, i_1_);
		    outLog.newLine();
		}
	    }
	} catch (IOException ioexception) {
	    debug.log(ioexception);
	}
    }
    
    public synchronized void write(MgText mgtext) {
	try {
	    if (isValidate) {
		initFile();
		if (outLog != null) {
		    if (mgtext.bName != null) {
			outLog.write(mgtext.getUserName());
			outLog.write('>');
		    }
		    switch (mgtext.head) {
		    case 0:
		    case 6:
		    case 8:
			outLog.write(mgtext.toString());
			outLog.newLine();
		    }
		}
	    }
	} catch (IOException ioexception) {
	    /* empty */
	}
    }
    
    private void initFile() {
	try {
	    if (isValidate) {
		long l = System.currentTimeMillis();
		if (outLog == null
		    || timeStart != 0L && l - timeStart >= 86400000L) {
		    timeStart = l;
		    if (outLog == null) {
			/* empty */
		    }
		    if (!fDir.isDirectory())
			fDir.mkdirs();
		    if (outLog != null) {
			try {
			    outLog.flush();
			    outLog.close();
			} catch (IOException ioexception) {
			    debug.log(ioexception);
			}
			outLog = null;
		    }
		    File file = getLogFile();
		    outLog = new BufferedWriter((new OutputStreamWriter
						 (new FileOutputStream(file,
								       true),
						  "UTF8")),
						1024);
		}
	    }
	} catch (IOException ioexception) {
	    debug.log(ioexception);
	}
    }
    
    public synchronized void close() {
	try {
	    if (outLog != null) {
		outLog.flush();
		outLog.close();
		outLog = null;
		File file = getLogFile();
		if (file.isFile() && file.length() <= 0L)
		    file.delete();
	    }
	} catch (IOException ioexception) {
	    ioexception.printStackTrace();
	}
    }
    
    private File getLogFile() throws IOException {
	return new File(Io.getDateString("text_log", "txt",
					 fDir.getCanonicalPath()));
    }
    
    public File getTmpFile() {
	return new File(fDir, "text_cash.tmp");
    }
    
    public void loadLog(Vector2 vector2, int i, boolean bool) {
	File file = getTmpFile();
	if (file.isFile() && file.length() > 0L) {
	    BufferedReader bufferedreader = null;
	    try {
		bufferedreader
		    = new BufferedReader(new InputStreamReader
					 (new FileInputStream(file), "UTF8"));
		String string;
		while ((string = bufferedreader.readLine()) != null) {
		    vector2.add(new MgText(0, (byte) 6, string));
		    if (vector2.size() >= i)
			vector2.remove(5);
		}
	    } catch (IOException ioexception) {
		/* empty */
	    }
	    try {
		if (bufferedreader != null)
		    bufferedreader.close();
	    } catch (IOException ioexception) {
		/* empty */
	    }
	    if (bool)
		file.delete();
	}
    }
    
    public void flush() throws IOException {
	if (outLog != null)
	    outLog.flush();
    }
}
