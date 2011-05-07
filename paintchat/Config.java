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
  public static final String CF_FILE_PAINTCHAT_CLIENT_DIR = "File_PaintChatClient_Dir";
  public static final String CF_FILE_PAINTCHAT_SERVER_DIR = "File_PaintChatServer_Dir";
  public static final String CF_FILE_HTTP_DIR = "File_Http_Dir";
  public static final String CF_FILE_PAINTCHAT_INFOMATION = "File_PaintChat_Infomation";
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

  public Config(Object paramObject)
    throws IOException
  {
    loadConfig(paramObject);
  }

  public void appendParam(StringBuffer paramStringBuffer, String paramString)
  {
    paramStringBuffer.append("<param name=\"");
    paramStringBuffer.append(paramString);
    paramStringBuffer.append("\" value=\"");
    paramStringBuffer.append(getString("Connection_Port_PaintChat"));
    paramStringBuffer.append("\">");
    paramStringBuffer.append(getSeparator());
  }

  public String getSeparator()
  {
    if (strSep == null)
      strSep = System.getProperty("line.separator");
    return strSep;
  }

  public void loadConfig(Object paramObject)
  {
    try
    {
      Object localObject1;
      if ((paramObject instanceof Hashtable))
      {
        localObject1 = (Hashtable)paramObject;
        Enumeration localEnumeration = ((Hashtable)localObject1).keys();
        while (localEnumeration.hasMoreElements())
        {
          Object localObject2 = localEnumeration.nextElement();
          put(localObject2, ((Hashtable)localObject1).get(localObject2));
        }
      }
      else if ((paramObject instanceof String))
      {
        localObject1 = new File((String)paramObject);
        loadPut(new FileInputStream((File)localObject1));
        put("File_Config", ((File)localObject1).getCanonicalPath());
      }
      else if ((paramObject instanceof File))
      {
        loadPut(new FileInputStream((File)paramObject));
        put("File_Config", ((File)paramObject).getCanonicalPath());
      }
    }
    catch (IOException localIOException)
    {
      put("File_Config", paramObject.toString());
    }
    replaceOldKeys();
    setDefault();
  }

  private void putDef(String paramString1, String paramString2)
  {
    put(paramString1, getString(paramString1, paramString2));
  }

  private void replaceOldKeys()
  {
    String[][] arrayOfString; = { { "Admin_ChatMaster", "ChatMaster" }, { "Admin_Password", "password" }, { "Server_User_Guest", "guest" }, { "Connection_GrobalAddress", "co" }, { "Connection_Port_PaintChat", "k" }, { "Connection_Host", "h" }, { "Server_Infomation", "i" }, { "Server_Log_Line", "ss" }, { "Server_Log_Server", "Log" }, { "Server_Log_Text", "Log_Text" }, { "Server_Load_Line", "Load" }, { "Server_Cash_Text", "Caches_Text" }, { "Server_Cash_Line", "Caches_Line" }, { "Server_Cash_Line_Size", "hl", "Max_Line" }, { "Client_Image_Width", "cx" }, { "Client_Image_Height", "cy" }, { "Client_Sound", "so" } };
    for (int i = 0; i < arrayOfString;.length; i++)
      for (int j = 1; j < arrayOfString;[i].length; j++)
      {
        Object localObject = get(arrayOfString;[i][j]);
        if (localObject == null)
          continue;
        put(arrayOfString;[i][0], localObject);
        remove(arrayOfString;[i][j]);
      }
  }

  public void saveConfig(File paramFile, String paramString)
  {
    try
    {
      save(new FileOutputStream(paramFile == null ? new File(getString("File_Config", "./cnf/paintchat.cf")) : paramFile), Resource.loadResource((paramString == null) || (paramString.length() <= 0) ? "Config" : paramString));
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void setDefault()
  {
    String str1 = "true";
    String str2 = "false";
    putDef("App_IsConsole", str1);
    putDef("App_Auto_Paintchat", str1);
    putDef("App_Auto_Http", str1);
    putDef("App_Auto_Lobby", str1);
    putDef("App_ShowStartHelp", str1);
    putDef("App_Get_Index", str1);
    putDef("Connection_GrobalAddress", str1);
    putDef("File_PaintChat_Infomation", "www/.paintchat");
    putDef("Server_Cash_Line", str1);
    putDef("Server_Cash_Text", str1);
    putDef("Server_Log_Line", str2);
    putDef("Server_Log_Text", str2);
    putDef("Server_Load_Line", str1);
    putDef("Server_Load_Text", str1);
    putDef("Server_Cash_Line_Size", "512000");
    putDef("Server_Cash_Text_Size", "200");
    putDef("Client_Image_Width", "1200");
    putDef("Client_Image_Height", "900");
    putDef("Client_Permission", "layer:all;layer_edit:true;canvas:true;talk:true;fill:false;clean:true;");
  }

  public static String setParams(String paramString1, String paramString2, String paramString3)
  {
    int i = paramString1.indexOf("<!--" + paramString2 + "-->");
    int j = paramString1.indexOf("<!--/" + paramString2 + "-->");
    if ((i < 0) || (j < 0))
      return paramString1;
    i += paramString2.length() + 7;
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(paramString1.substring(0, i));
    localStringBuffer.append(paramString3);
    localStringBuffer.append(paramString1.substring(j, paramString1.length()));
    return localStringBuffer.toString();
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.Config
 * JD-Core Version:    0.6.0
 */