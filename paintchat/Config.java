/* Config - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import syi.util.PProperties;

public class Config extends PProperties
{
    private static String strSep = null;
    public static final String CF_CHATMASTER = "Admin_ChatMaster";
    private final String CF_CHATMASTER2 = "ChatMaster";
    public static final String CF_PASSWORD = "Admin_Password";
    private final String CF_PASSWORD2 = "password";
    public static final String CF_MAX_USER = "Server_User_Max";
    public static final String CF_GUEST = "Server_User_Guest";
    private final String CF_GUEST2 = "guest";
    public static final String CF_CONNECT = "Connection_GrobalAddress";
    public final String CF_CONNECT2 = "co";
    public static final String CF_PORT = "Connection_Port_PaintChat";
    public final String CF_PORT2 = "k";
    public static final String CF_PORT_HTTP = "Connection_Port_Http";
    public static final String CF_CONNECTION_MAX = "Connection_Max";
    public static final String CF_CONNECTION_TIMEOUT = "Connection_Timeout";
    public static final String CF_HOST = "Connection_Host";
    private final String CF_HOST2 = "h";
    public static final String CF_INFOMATION = "Server_Infomation";
    private final String CF_INFOMATION2 = "i";
    public static final String CF_LOG_LINE = "Server_Log_Line";
    public final String CF_LOG_LINE2 = "ss";
    public static final String CF_LOG_SERVER = "Server_Log_Server";
    private final String CF_LOG_SERVER2 = "Log";
    public static final String CF_LOG_HTTP = "Http_Log";
    public static final String CF_SERVER_DEBUG = "Server_Debug";
    public static final String CF_LOG_TEXT = "Server_Log_Text";
    private final String CF_LOG_TEXT2 = "Log_Text";
    public static final String CF_LOAD_LINE = "Server_Load_Line";
    private final String CF_LOAD_LINE2 = "Load";
    public static final String CF_LOAD_TEXT = "Server_Load_Text";
    public static final String CF_CASH_TEXT = "Server_Cash_Text";
    private final String CF_CASH_TEXT2 = "Caches_Text";
    public final String CF_MAX_TEXT2 = "ht";
    public final String CF_MAX_TEXT3 = "Cash_Text_Size";
    public static final String CF_CASH_LINE = "Server_Cash_Line";
    private final String CF_CASH_LINE2 = "Caches_Line";
    public static final String CF_CASH_LINE_SIZE = "Server_Cash_Line_Size";
    public static final String CF_CASH_TEXT_SIZE = "Server_Cash_Text_Size";
    public static final String CF_LOG_LINE_DIR = "Server_Log_Line_Dir";
    public static final String CF_LOG_TEXT_DIR = "Server_Log_Text_Dir";
    public final String CF_MAX_LINE2 = "hl";
    public final String CF_MAX_LINE3 = "Max_Line";
    public static final String CF_WIDTH = "Client_Image_Width";
    public final String CF_CANVAS_SIZE_X2 = "cx";
    public static final String CF_HEIGHT = "Client_Image_Height";
    public final String CF_CANVAS_SIZE_Y2 = "cy";
    public static final String CF_SOUND = "Client_Sound";
    public static final String CF_SOUND2 = "so";
    public static final String CF_FILE_CONFIG = "File_Config";
    public static final String CF_FILE_PAINTCHAT_CLIENT_DIR
	= "File_PaintChatClient_Dir";
    public static final String CF_FILE_PAINTCHAT_SERVER_DIR
	= "File_PaintChatServer_Dir";
    public static final String CF_FILE_HTTP_DIR = "File_Http_Dir";
    public static final String CF_FILE_PAINTCHAT_INFOMATION
	= "File_PaintChat_Infomation";
    public static int DEF_PORT = 41411;
    public static final String CF_APP_VERSION = "App_Version";
    public static final String CF_APP_IS_CONSOLE = "App_IsConsole";
    public static final String CF_APP_SHOW_STARTHELP = "App_ShowStartHelp";
    public static final String CF_APP_SHOW_HELP = "App_ShowHelp";
    public static final String CF_APP_AUTO_HTTP = "App_Auto_Http";
    public static final String CF_APP_AUTO_CHAT = "App_Auto_Paintchat";
    public static final String CF_APP_AUTO_LOBBY = "App_Auto_Lobby";
    public static final String CF_APP_CGI = "App_Cgi";
    public static final String CF_APP_JVM_PATH = "App_JvmPath";
    public static final String CF_APP_GET_INDEX = "App_Get_Index";
    public static final String CF_HTTP_DIR = "Http_Dir";
    public static final String CF_CLIENT_IMAGE_WIDTH = "Client_Image_Width";
    public static final String CF_CLIENT_IMAGE_HEIGHT = "Client_Image_Height";
    public static final String CF_CLIENT_PERMISSION = "Client_Permission";
    
    public Config(Object object) throws IOException {
	loadConfig(object);
    }
    
    public void appendParam(StringBuffer stringbuffer, String string) {
	stringbuffer.append("<param name=\"");
	stringbuffer.append(string);
	stringbuffer.append("\" value=\"");
	stringbuffer.append(this.getString("Connection_Port_PaintChat"));
	stringbuffer.append("\">");
	stringbuffer.append(getSeparator());
    }
    
    public String getSeparator() {
	if (strSep == null)
	    strSep = System.getProperty("line.separator");
	return strSep;
    }
    
    public void loadConfig(Object object) {
	try {
	    if (object instanceof Hashtable) {
		Hashtable hashtable = (Hashtable) object;
		Enumeration enumeration = hashtable.keys();
		while (enumeration.hasMoreElements()) {
		    Object object_0_ = enumeration.nextElement();
		    this.put(object_0_, hashtable.get(object_0_));
		}
	    } else if (object instanceof String) {
		File file = new File((String) object);
		this.loadPut(new FileInputStream(file));
		this.put("File_Config", file.getCanonicalPath());
	    } else if (object instanceof File) {
		this.loadPut(new FileInputStream((File) object));
		this.put("File_Config", ((File) object).getCanonicalPath());
	    }
	} catch (IOException ioexception) {
	    this.put("File_Config", object.toString());
	}
	replaceOldKeys();
	setDefault();
    }
    
    private void putDef(String string, String string_1_) {
	this.put(string, this.getString(string, string_1_));
    }
    
    private void replaceOldKeys() {
	String[][] strings
	    = { { "Admin_ChatMaster", "ChatMaster" },
		{ "Admin_Password", "password" },
		{ "Server_User_Guest", "guest" },
		{ "Connection_GrobalAddress", "co" },
		{ "Connection_Port_PaintChat", "k" },
		{ "Connection_Host", "h" }, { "Server_Infomation", "i" },
		{ "Server_Log_Line", "ss" }, { "Server_Log_Server", "Log" },
		{ "Server_Log_Text", "Log_Text" },
		{ "Server_Load_Line", "Load" },
		{ "Server_Cash_Text", "Caches_Text" },
		{ "Server_Cash_Line", "Caches_Line" },
		{ "Server_Cash_Line_Size", "hl", "Max_Line" },
		{ "Client_Image_Width", "cx" },
		{ "Client_Image_Height", "cy" }, { "Client_Sound", "so" } };
	for (int i = 0; i < strings.length; i++) {
	    for (int i_2_ = 1; i_2_ < strings[i].length; i_2_++) {
		Object object = this.get(strings[i][i_2_]);
		if (object != null) {
		    this.put(strings[i][0], object);
		    this.remove(strings[i][i_2_]);
		}
	    }
	}
    }
    
    public void saveConfig(File file, String string) {
	try {
	    this.save((new FileOutputStream
		       (file == null
			? new File(this.getString("File_Config",
						  "./cnf/paintchat.cf"))
			: file)),
		      Resource.loadResource((string == null
					     || string.length() <= 0)
					    ? "Config" : string));
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void setDefault() {
	String string = "true";
	String string_3_ = "false";
	putDef("App_IsConsole", string);
	putDef("App_Auto_Paintchat", string);
	putDef("App_Auto_Http", string);
	putDef("App_Auto_Lobby", string);
	putDef("App_ShowStartHelp", string);
	putDef("App_Get_Index", string);
	putDef("Connection_GrobalAddress", string);
	putDef("File_PaintChat_Infomation", "www/.paintchat");
	putDef("Server_Cash_Line", string);
	putDef("Server_Cash_Text", string);
	putDef("Server_Log_Line", string_3_);
	putDef("Server_Log_Text", string_3_);
	putDef("Server_Load_Line", string);
	putDef("Server_Load_Text", string);
	putDef("Server_Cash_Line_Size", "512000");
	putDef("Server_Cash_Text_Size", "200");
	putDef("Client_Image_Width", "1200");
	putDef("Client_Image_Height", "900");
	putDef
	    ("Client_Permission",
	     "layer:all;layer_edit:true;canvas:true;talk:true;fill:false;clean:true;");
    }
    
    public static String setParams(String string, String string_4_,
				   String string_5_) {
	int i = string.indexOf("<!--" + string_4_ + "-->");
	int i_6_ = string.indexOf("<!--/" + string_4_ + "-->");
	if (i < 0 || i_6_ < 0)
	    return string;
	i += string_4_.length() + 7;
	StringBuffer stringbuffer = new StringBuffer();
	stringbuffer.append(string.substring(0, i));
	stringbuffer.append(string_5_);
	stringbuffer.append(string.substring(i_6_, string.length()));
	return stringbuffer.toString();
    }
}
