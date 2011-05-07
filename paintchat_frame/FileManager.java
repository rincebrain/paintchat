/* FileManager - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_frame;
import java.io.File;
import java.io.IOException;

import paintchat.Config;

import syi.util.Io;

public class FileManager
{
    Config config;
    
    public FileManager(Config config) {
	this.config = config;
    }
    
    public static void copyFile(File file, File file_0_) throws IOException {
	Io.copyFile(file, file_0_);
    }
    
    public static void copyFile(String string, String string_1_) {
	copyFile(string, string_1_);
    }
    
    public static void copyFiles(String[] strings, String string) {
	Io.copyFiles(strings, string);
    }
    
    public void templateToWWW() {
	try {
	    String string = Io.getCurrent();
	    String string_2_
		= "cnf" + File.separatorChar + "template" + File.separatorChar;
	    String string_3_ = "index.html";
	    new File(string_2_);
	    File file = new File(config.getString("File_PaintChatClient_Dir",
						  "www"));
	    if (!file.isDirectory())
		file.mkdirs();
	    String[] strings
		= { "pchat.jar", "pchat_user_list.swf", "entrance_normal.html",
		    "entrance_pro.html", string_3_ };
	    for (int i = 0; i < strings.length; i++) {
		if (strings[i] != string_3_
		    || config.getBool("App_Get_Index", true)) {
		    String string_4_ = strings[i];
		    Io.copyFile(Io.makeFile(string, string_2_ + string_4_),
				new File(file, string_4_));
		}
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
}
