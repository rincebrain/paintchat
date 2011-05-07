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

  private static void addResource(String paramString1, String paramString2)
  {
    Res localRes = new Res();
    localRes.load(getContent(paramString1, paramString2));
    table.put(paramString2, localRes);
  }

  private static String getContent(String paramString1, String paramString2)
  {
    int i = paramString1.indexOf('<' + paramString2 + '>');
    if (i == -1)
      return null;
    int j = paramString1.indexOf("</" + paramString2 + '>', i);
    if (j == -1)
      j = paramString1.length();
    return paramString1.substring(i, j);
  }

  private static File getResourceFile()
  {
    String str1 = "resource";
    String str2 = ".properties";
    Locale localLocale = Locale.getDefault();
    File localFile1 = new File(System.getProperty("user.dir"), "cnf");
    File localFile2 = null;
    localFile2 = new File(localFile1, str1 + localLocale.getLanguage());
    if (!localFile2.exists())
    {
      localFile2 = new File(localFile1, str1 + '_' + localLocale.getLanguage() + str2);
      if (!localFile2.exists())
        localFile2 = new File(localFile1, str1 + str2);
    }
    return localFile2;
  }

  public static synchronized void loadResource()
  {
    try
    {
      if (table.size() >= 4)
        return;
      File localFile = getResourceFile();
      StringBuffer localStringBuffer = new StringBuffer();
      BufferedReader localBufferedReader = new BufferedReader(new FileReader(localFile));
      int i;
      while ((i = localBufferedReader.read()) != -1)
        localStringBuffer.append((char)i);
      localBufferedReader.close();
      String str = getContent(localStringBuffer.toString(), "Resource");
      addResource(str, "Config");
      addResource(str, "Application");
      addResource(str, "Server");
      addResource(str, "Http");
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }

  public static synchronized Res loadResource(String paramString)
  {
    try
    {
      Res localRes = (Res)table.get(paramString);
      if (localRes != null)
        return localRes;
      File localFile = getResourceFile();
      StringBuffer localStringBuffer = new StringBuffer();
      BufferedReader localBufferedReader = new BufferedReader(new FileReader(localFile));
      int i;
      while ((i = localBufferedReader.read()) != -1)
        localStringBuffer.append((char)i);
      localBufferedReader.close();
      String str = getContent(localStringBuffer.toString(), "Resource");
      addResource(str, paramString);
      return (Res)table.get(paramString);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat.Resource
 * JD-Core Version:    0.6.0
 */