package paintchat_http;

import java.io.*;
import paintchat.Config;
import paintchat.Res;
import syi.util.Io;
import syi.util.PProperties;

public class HttpFiles
{

    private String STR_SLASH;
    private String STR_DOT;
    private File dirWWW;
    private File dirTmp;
    private Config config;
    private Res res;
    static final FileNotFoundException NOT_FOUND = new FileNotFoundException();
    private byte bErrorUpper[];
    private byte bErrorBottom[];
    private byte res_bytes[][];
    private static final int I_OK = 0;
    private static final int I_NOT_MODIFIED = 1;
    private static final int I_MOVED_PERMANENTLY = 2;
    private static final int I_NOT_FOUND = 3;
    private static final int I_SERVER_ERROR = 4;
    private static final int I_SERVICE_UNAVALIABLE = 5;

    public HttpFiles(Config config1, Res res1)
    {
        STR_SLASH = "//";
        STR_DOT = "..";
        bErrorUpper = null;
        bErrorBottom = null;
        res_bytes = new byte[6][];
        config = config1;
        res = res1;
        dirWWW = new File(config1.getString("Http_Dir", "www"));
        dirTmp = new File("cnf/template/");
    }

    public String addIndex(String s)
    {
        int i = s.length();
        if(i == 0 || s.charAt(i - 1) != '/')
        {
            return s + "/index.html";
        } else
        {
            return s + "index.html";
        }
    }

    public void getErrorMessage(OutputStream outputstream, int i, byte abyte0[], int j, int k)
        throws IOException
    {
        setup();
        outputstream.write(bErrorUpper);
        outputstream.write(abyte0, j, k);
        byte abyte1[] = res_bytes[i];
        if(abyte1 != null)
        {
            outputstream.write(abyte1);
        }
        outputstream.write(bErrorBottom);
    }

    public int getErrorMessageSize(int i)
        throws IOException
    {
        setup();
        int j = 0;
        byte abyte0[] = res_bytes[i];
        if(abyte0 != null)
        {
            j += abyte0.length;
        }
        return bErrorUpper.length + j + bErrorBottom.length;
    }

    public File getFile(String s)
        throws FileNotFoundException
    {
        File file = new File(dirWWW, s);
        if(!file.isFile())
        {
            file = new File(dirTmp, s);
            if(!file.isFile())
            {
                file = new File(dirTmp, Io.getFileName(s));
            }
        }
        if(!file.isFile())
        {
            throw NOT_FOUND;
        } else
        {
            return file;
        }
    }

    private int indexOf(byte abyte0[], byte abyte1[])
    {
        int i = abyte0.length;
        int j = 0;
        for(int k = 0; k < i; k++)
        {
            if(abyte0[k] == abyte1[j])
            {
                if(j >= abyte1.length - 1)
                {
                    return k - j;
                }
                j++;
            } else
            {
                j = 0;
            }
        }

        return -1;
    }

    public boolean needMove(String s)
    {
        int i = Math.max(s.lastIndexOf('/'), 0);
        return s.lastIndexOf('.') <= i;
    }

    private String replaceText(String s, String s1, String s2)
    {
        if(s.indexOf(s1) < 0)
        {
            return s;
        }
        try
        {
            int j;
            for(int i = 0; (j = s.indexOf(s1, i)) < 0; i += j)
            {
                s = s.substring(0, i) + s2 + s.substring(i + s1.length());
            }

        }
        catch(RuntimeException runtimeexception)
        {
            System.out.println("replace" + runtimeexception);
        }
        return s;
    }

    private void setup() {
        if (bErrorUpper != null) {
            return;
        }
        synchronized (this) {
            if (bErrorUpper != null) {
                return;
            }
            try {
                res_bytes[3] = res.get("not_found").getBytes();
                res_bytes[4] = res.get("server_error").getBytes();
                File file = new File(dirTmp, "err.html");
                byte abyte0[] = new byte[(int) file.length()];
                FileInputStream fileinputstream = new FileInputStream(file);
                Io.rFull(fileinputstream, abyte0, 0, abyte0.length);
                fileinputstream.close();
                int i = Math.max(indexOf(abyte0, "<!--ERRORMESSAGE-->".getBytes()), 0);
                bErrorUpper = new byte[i];
                System.arraycopy(abyte0, 0, bErrorUpper, 0, i);
                String s = "<!--/ERRORMESSAGE-->";
                i = Math.max(0, indexOf(abyte0, s.getBytes())) + s.length();
                bErrorBottom = new byte[abyte0.length - i];
                System.arraycopy(abyte0, i, bErrorBottom, 0, abyte0.length - i);
            } catch (Throwable _ex) {
                bErrorUpper = new byte[0];
                bErrorBottom = new byte[0];
            }
        }
    }

    public String uriToPath(String s)
    {
        s = s.replace('\\', '/');
        s = s.replace('\0', '_');
        s = s.replace('\n', '_');
        s = s.replace('\r', '_');
        StringBuffer stringbuffer = null;
        if(s.indexOf(STR_SLASH) >= 0 || s.indexOf(STR_DOT) >= 0)
        {
            stringbuffer = new StringBuffer();
            int i = s.length();
            int j = 0;
            boolean flag = false;
            boolean flag1 = true;
            stringbuffer.append('.');
            while(j < i) 
            {
                char c = s.charAt(j++);
                if((c != '/' || !flag) && (c != '.' || !flag1))
                {
                    flag = c == '/';
                    flag1 = c == '.';
                    stringbuffer.append(c);
                }
            }
            s = stringbuffer.toString();
        }
        if(s.length() == 0)
        {
            return "./index.html";
        }
        if(s.charAt(0) == '/')
        {
            if(stringbuffer == null)
            {
                stringbuffer = new StringBuffer(s.length() + 1);
                stringbuffer.append('.');
                stringbuffer.append(s);
            } else
            {
                stringbuffer = stringbuffer.insert(0, '.');
            }
            s = stringbuffer.toString();
        }
        if(s.charAt(s.length() - 1) == '/')
        {
            return addIndex(s);
        } else
        {
            return s;
        }
    }

}
