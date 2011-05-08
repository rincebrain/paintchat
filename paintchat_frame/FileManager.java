package paintchat_frame;

import java.io.File;
import java.io.IOException;
import paintchat.Config;
import syi.util.Io;
import syi.util.PProperties;

public class FileManager
{

    Config config;

    public FileManager(Config config1)
    {
        config = config1;
    }

    public static void copyFile(File file, File file1)
        throws IOException
    {
        Io.copyFile(file, file1);
    }

    public static void copyFile(String s, String s1)
    {
        copyFile(s, s1);
    }

    public static void copyFiles(String as[], String s)
    {
        Io.copyFiles(as, s);
    }

    public void templateToWWW()
    {
        try
        {
            String s1 = Io.getCurrent();
            String s2 = "cnf" + File.separatorChar + "template" + File.separatorChar;
            String s3 = "index.html";
            new File(s2);
            File file = new File(config.getString("File_PaintChatClient_Dir", "www"));
            if(!file.isDirectory())
            {
                file.mkdirs();
            }
            String as[] = {
                "pchat.jar", "pchat_user_list.swf", "entrance_normal.html", "entrance_pro.html", s3
            };
            for(int i = 0; i < as.length; i++)
            {
                if(as[i] != s3 || config.getBool("App_Get_Index", true))
                {
                    String s = as[i];
                    Io.copyFile(Io.makeFile(s1, s2 + s), new File(file, s));
                }
            }

        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }
}
