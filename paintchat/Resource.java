/* Resource - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;

public class Resource
{
    private static Hashtable table = new Hashtable();
    public static final String RESOURCE = "Resource";
    public static final String CONFIG = "Config";
    public static final String APP = "Application";
    public static final String SERVER = "Server";
    public static final String HTTP = "Http";
    public static final String CLIENT = "Client";
    public static final String R_ERROR = "error";
    
    private static void addResource(String string, String string_0_) {
	Res res = new Res();
	res.load(getContent(string, string_0_));
	table.put(string_0_, res);
    }
    
    private static String getContent(String string, String string_1_) {
	int i = string.indexOf(String.valueOf('<') + string_1_ + '>');
	if (i == -1)
	    return null;
	int i_2_ = string.indexOf("</" + string_1_ + '>', i);
	if (i_2_ == -1)
	    i_2_ = string.length();
	return string.substring(i, i_2_);
    }
    
    private static File getResourceFile() {
	String string = "resource";
	String string_3_ = ".properties";
	Locale locale = Locale.getDefault();
	File file = new File(System.getProperty("user.dir"), "cnf");
	Object object = null;
	File file_4_ = new File(file, string + locale.getLanguage());
	if (!file_4_.exists()) {
	    file_4_
		= new File(file,
			   string + '_' + locale.getLanguage() + string_3_);
	    if (!file_4_.exists())
		file_4_ = new File(file, string + string_3_);
	}
	return file_4_;
    }
    
    public static synchronized void loadResource() {
	try {
	    if (table.size() < 4) {
		File file = getResourceFile();
		StringBuffer stringbuffer = new StringBuffer();
		BufferedReader bufferedreader
		    = new BufferedReader(new FileReader(file));
		int i;
		while ((i = bufferedreader.read()) != -1)
		    stringbuffer.append((char) i);
		bufferedreader.close();
		String string
		    = getContent(stringbuffer.toString(), "Resource");
		addResource(string, "Config");
		addResource(string, "Application");
		addResource(string, "Server");
		addResource(string, "Http");
	    }
	} catch (IOException ioexception) {
	    ioexception.printStackTrace();
	}
    }
    
    public static synchronized Res loadResource(String string) {
	try {
	    Res res = (Res) table.get(string);
	    if (res != null)
		return res;
	    File file = getResourceFile();
	    StringBuffer stringbuffer = new StringBuffer();
	    BufferedReader bufferedreader
		= new BufferedReader(new FileReader(file));
	    int i;
	    while ((i = bufferedreader.read()) != -1)
		stringbuffer.append((char) i);
	    bufferedreader.close();
	    String string_5_ = getContent(stringbuffer.toString(), "Resource");
	    addResource(string_5_, string);
	    return (Res) table.get(string);
	} catch (IOException ioexception) {
	    ioexception.printStackTrace();
	    return null;
	}
    }
}
