/* CommentCutter - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

class CommentCutter
{
    private Reader In = null;
    private Writer Out = null;
    private int[][] flagLine = null;
    private int[][] flagWhole = null;
    private int[][] flagWholeEnd = null;
    private int[] countLine = null;
    private int[] countWhole = null;
    private int[] cash;
    private int countCash;
    private int oldCh;
    
    public CommentCutter() {
	cash = new int[3];
	countCash = 0;
	oldCh = 32;
    }
    
    public CommentCutter(Reader reader, Writer writer) {
	cash = new int[3];
	countCash = 0;
	oldCh = 32;
	In = reader;
	Out = writer;
    }
    
    private void addDefault() {
	addLineFlag(new int[] { 47, 47 });
	addWholeFlag(new int[] { 47, 37 }, new int[] { 37, 47 });
    }
    
    public void addLineFlag(int[] is) {
	CommentCutter commentcutter_0_;
	MONITORENTER (commentcutter_0_ = this);
	MISSING MONITORENTER
	synchronized (commentcutter_0_) {
	    if (flagLine == null) {
		flagLine = new int[0][];
		countLine = new int[0];
	    }
	    int[][] is_1_ = new int[flagLine.length + 1][];
	    int[] is_2_ = new int[flagLine.length + 1];
	    int i;
	    for (i = 0; i < flagLine.length - 1; i++)
		is_1_[i] = flagLine[i];
	    is_1_[i] = is;
	    flagLine = is_1_;
	    countLine = is_2_;
	}
    }
    
    public void addWholeFlag(int[] is, int[] is_3_) {
	CommentCutter commentcutter_4_;
	MONITORENTER (commentcutter_4_ = this);
	MISSING MONITORENTER
	synchronized (commentcutter_4_) {
	    if (flagWhole == null) {
		flagWhole = new int[0][];
		flagWholeEnd = new int[0][];
		countWhole = new int[0];
	    }
	    int[][] is_5_ = new int[flagWhole.length + 1][];
	    int[][] is_6_ = new int[flagWholeEnd.length + 1][];
	    int[] is_7_ = new int[countWhole.length + 1];
	    int i;
	    for (i = 0; i < flagLine.length - 1; i++) {
		is_5_[i] = flagWhole[i];
		is_6_[i] = flagWholeEnd[i];
	    }
	    is_5_[i] = is;
	    is_6_[i] = is_3_;
	    flagWhole = is_5_;
	    flagWholeEnd = is_6_;
	    countWhole = is_7_;
	}
    }
    
    private void cleanCounter() {
	for (int i = 0; i < countLine.length; i++)
	    countLine[i] = 0;
	for (int i = 0; i < countWhole.length; i++)
	    countWhole[i] = 0;
	countCash = 0;
    }
    
    public void cut() throws IOException {
	CommentCutter commentcutter_8_;
	MONITORENTER (commentcutter_8_ = this);
	MISSING MONITORENTER
	synchronized (commentcutter_8_) {
	    try {
		if (flagLine == null && flagWhole == null)
		    addDefault();
		cleanCounter();
		findFlags();
	    } catch (java.io.EOFException eofexception) {
		/* empty */
	    }
	    In.close();
	    Out.flush();
	    Out.close();
	}
    }
    
    private void cutArray(int[] is) throws IOException {
	int i = 0;
	int i_9_ = is.length;
	int i_10_;
	while ((i_10_ = In.read()) != -1) {
	    i = is[i] == i_10_ ? i + 1 : 0;
	    if (i == i_9_)
		break;
	}
    }
    
    private void cutCRLF() throws IOException {
	int i;
	while ((i = In.read()) != -1 && (i != 13 && i != 10)) {
	    /* empty */
	}
    }
    
    private void findFlags() throws IOException {
	int i;
	while ((i = In.read()) != -1) {
	    if (Character.isWhitespace((char) i))
		i = 32;
	    if (switchFlag(i))
		out(i);
	}
    }
    
    private void inCash(int i) {
	if (countCash >= cash.length) {
	    int[] is = new int[cash.length];
	    System.arraycopy(cash, 0, is, 0, cash.length);
	    cash = is;
	}
	cash[countCash++] = i;
    }
    
    public static void main(String[] strings) {
	try {
	    String string
		= "c:\\Windows\\\uff83\uff9e\uff7d\uff78\uff84\uff6f\uff8c\uff9f\\";
	    String string_11_ = "";
	    FileDialog filedialog
		= (new FileDialog
		   (new Frame(),
		    "\u30b3\u30e1\u30f3\u30c8\u3092\u30ab\u30c3\u30c8\u3059\u308b\u30d5\u30a1\u30a4\u30eb\u3092\u6307\u5b9a",
		    0));
	    filedialog.setDirectory(string);
	    filedialog.setFile(string_11_);
	    filedialog.show();
	    string = filedialog.getDirectory();
	    string_11_ = filedialog.getFile();
	    if (string == null || string.equals("null") || string_11_ == null
		|| string_11_.equals(null))
		System.exit(0);
	    File file = new File(string, string_11_);
	    filedialog.setMode(1);
	    filedialog.setTitle
		("\u30ab\u30c3\u30c8\u3057\u305f\u30d5\u30a1\u30a4\u30eb\u3092\u4fdd\u5b58\u3059\u308b\u5834\u6240\u3092\u6307\u5b9a");
	    filedialog.show();
	    string = filedialog.getDirectory();
	    string_11_ = filedialog.getFile();
	    if (string == null || string.equals("null") || string_11_ == null
		|| string_11_.equals(null))
		System.exit(0);
	    File file_12_ = new File(string, string_11_);
	    CommentCutter commentcutter
		= (new CommentCutter
		   ((new BufferedReader
		     (new InputStreamReader(new FileInputStream(file),
					    "JISAutoDetect"))),
		    new BufferedWriter(new OutputStreamWriter
				       (new FileOutputStream(file_12_)))));
	    commentcutter.cut();
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
	System.exit(0);
    }
    
    private void out(int i) throws IOException {
	if (i != 32 || oldCh != 32) {
	    Out.write(i);
	    oldCh = i;
	}
    }
    
    private void outCash() throws IOException {
	for (int i = 0; i < countCash; i++)
	    out(cash[i]);
	countCash = 0;
    }
    
    public void setInOut(Reader reader, Writer writer) {
	CommentCutter commentcutter_13_;
	MONITORENTER (commentcutter_13_ = this);
	MISSING MONITORENTER
	synchronized (commentcutter_13_) {
	    Out = writer;
	    In = reader;
	}
    }
    
    private boolean switchFlag(int i) throws IOException {
	boolean bool = true;
	for (int i_14_ = 0; i_14_ < flagLine.length; i_14_++) {
	    if (flagLine[i_14_][countLine[i_14_]] == i) {
		bool = false;
		countLine[i_14_]++;
		if (countLine[i_14_] >= flagLine[i_14_].length) {
		    cleanCounter();
		    cutCRLF();
		    return false;
		}
	    } else
		countLine[i_14_] = 0;
	}
	if (!bool)
	    inCash(i);
	else
	    outCash();
	return bool;
    }
}
