package paintchat;

import java.io.*;
import java.util.Hashtable;
import java.util.Locale;

// Referenced classes of package paintchat:
//            Res

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

    public Resource()
    {
    }

    private static void addResource(String s, String s1)
    {
        Res res = new Res();
        res.load(getContent(s, s1));
        table.put(s1, res);
    }

    private static String getContent(String s, String s1)
    {
        int i = s.indexOf('<' + s1 + '>');
        if(i == -1)
        {
            return null;
        }
        int j = s.indexOf("</" + s1 + '>', i);
        if(j == -1)
        {
            j = s.length();
        }
        return s.substring(i, j);
    }

    private static File getResourceFile()
    {
        String s = "resource";
        String s1 = ".properties";
        Locale locale = Locale.getDefault();
        File file = new File(System.getProperty("user.dir"), "cnf");
        File file1 = null;
        file1 = new File(file, s + locale.getLanguage());
        if(!file1.exists())
        {
            file1 = new File(file, s + '_' + locale.getLanguage() + s1);
            if(!file1.exists())
            {
                file1 = new File(file, s + s1);
            }
        }
        return file1;
    }

    public static synchronized void loadResource()
    {
        try
        {
            if(table.size() >= 4)
            {
                return;
            }
            File file = getResourceFile();
            StringBuffer stringbuffer = new StringBuffer();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
            int i;
            while((i = bufferedreader.read()) != -1) 
            {
                stringbuffer.append((char)i);
            }
            bufferedreader.close();
            String s = getContent(stringbuffer.toString(), "Resource");
            addResource(s, "Config");
            addResource(s, "Application");
            addResource(s, "Server");
            addResource(s, "Http");
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public static synchronized Res loadResource(String s)
    {
        try
        {
            Res res = (Res)table.get(s);
            if(res != null)
            {
                return res;
            }
            File file = getResourceFile();
            StringBuffer stringbuffer = new StringBuffer();
            BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
            int i;
            while((i = bufferedreader.read()) != -1) 
            {
                stringbuffer.append((char)i);
            }
            bufferedreader.close();
            String s1 = getContent(stringbuffer.toString(), "Resource");
            addResource(s1, s);
            return (Res)table.get(s);
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        return null;
    }

}
