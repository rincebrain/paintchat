package paintchat_frame;

import java.io.File;
import java.io.IOException;
import paintchat.Config;
import syi.util.Io;
import syi.util.PProperties;

public class FileManager
{
  Config config;

  public FileManager(Config paramConfig)
  {
    this.config = paramConfig;
  }

  public static void copyFile(File paramFile1, File paramFile2)
    throws IOException
  {
    Io.copyFile(paramFile1, paramFile2);
  }

  public static void copyFile(String paramString1, String paramString2)
  {
    copyFile(paramString1, paramString2);
  }

  public static void copyFiles(String[] paramArrayOfString, String paramString)
  {
    Io.copyFiles(paramArrayOfString, paramString);
  }

  public void templateToWWW()
  {
    try
    {
      String str2 = Io.getCurrent();
      String str3 = "cnf" + File.separatorChar + "template" + File.separatorChar;
      String str4 = "index.html";
      new File(str3);
      File localFile = new File(this.config.getString("File_PaintChatClient_Dir", "www"));
      if (!localFile.isDirectory())
        localFile.mkdirs();
      String[] arrayOfString = { "pchat.jar", "pchat_user_list.swf", "entrance_normal.html", "entrance_pro.html", str4 };
      for (int i = 0; i < arrayOfString.length; i++)
      {
        if ((arrayOfString[i] == str4) && (!this.config.getBool("App_Get_Index", true)))
          continue;
        String str1 = arrayOfString[i];
        Io.copyFile(Io.makeFile(str2, str3 + str1), new File(localFile, str1));
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_frame.FileManager
 * JD-Core Version:    0.6.0
 */