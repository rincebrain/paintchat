package syi.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

// Referenced classes of package syi.util:
//            Io

public class ClassLoaderCustom extends ClassLoader
{

    private Hashtable cash;
    private ClassLoader cl;

    public ClassLoaderCustom()
    {
        cash = new Hashtable();
        cl = getClass().getClassLoader();
    }

    public boolean loadArchive(String s)
    {
        try
        {
            Object obj;
            if(s.indexOf("://") != -1)
            {
                obj = (new URL(s)).openStream();
            } else
            {
                obj = new FileInputStream(new File(Io.getCurrent(), s));
            }
            ZipInputStream zipinputstream = new ZipInputStream(((InputStream) (obj)));
            byte abyte0[] = new byte[4000];
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(4000);
            ZipEntry zipentry;
            while((zipentry = zipinputstream.getNextEntry()) != null) 
            {
                String s2 = replace(zipentry.getName(), '/', '.', false);
                if(s2.endsWith(".class"))
                {
                    bytearrayoutputstream.reset();
                    int i;
                    while((i = zipinputstream.read(abyte0)) != -1) 
                    {
                        bytearrayoutputstream.write(abyte0, 0, i);
                    }
                    bytearrayoutputstream.flush();
                    zipinputstream.closeEntry();
                    byte abyte1[] = bytearrayoutputstream.toByteArray();
                    String s1 = s2.substring(0, s2.length() - 6);
                    Class class1 = defineClass(s1, abyte1, 0, abyte1.length);
                    abyte1 = (byte[])null;
                    if(class1 != null)
                    {
                        cash.put(s1, class1);
                    }
                    class1 = null;
                }
            }
            zipinputstream.close();
            return true;
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
        }
        return false;
    }

    public Class loadClass(String s, String s1, boolean flag)
    {
        Class class1 = (Class)cash.get(s);
        if(class1 == null)
        {
            if(s1 == null || !s1.startsWith("http://"))
            {
                class1 = loadLocal(s, s1);
            } else
            {
                class1 = loadURL(s, s1);
            }
            if(class1 == null)
            {
                class1 = java.lang.Object.class;
            }
            cash.put(s, class1);
        }
        if(flag)
        {
            resolveClass(class1);
        }
        return class1;
    }

    protected Class loadClass(String s, boolean flag)
        throws ClassNotFoundException
    {
        Class class1 = (Class)cash.get(s);
        if(class1 == null)
        {
            return findSystemClass(s);
        }
        if(flag)
        {
            resolveClass(class1);
        }
        return class1;
    }

    private Class loadLocal(String s, String s1)
    {
        Class class2;
        try
        {
            if(s1 == null || s1.length() <= 0)
            {
                Class class1 = findSystemClass(s);
                if(class1 != null)
                {
                    return class1;
                }
            }
            File file = new File(s1, replace(s, '.', File.separatorChar, true));
            if(!file.isFile())
            {
                return null;
            }
            byte abyte0[] = new byte[(int)file.length()];
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));
            datainputstream.readFully(abyte0);
            datainputstream.close();
            class2 = defineClass(s, abyte0, 0, abyte0.length);
        }
        catch(IOException _ex)
        {
            class2 = null;
        }
        catch(ClassNotFoundException _ex)
        {
            class2 = null;
        }
        return class2;
    }

    private Class loadURL(String s, String s1)
    {
        Class class1;
        try
        {
            String s2 = replace(s, '\\', '/', true);
            URL url;
            if(s1.charAt(s1.length() - 1) != '/')
            {
                url = new URL(s1 + '/' + s2);
            } else
            {
                url = new URL(s1 + s2);
            }
            s2 = null;
            URLConnection urlconnection = url.openConnection();
            urlconnection.connect();
            int i = urlconnection.getContentLength();
            byte abyte0[] = new byte[i];
            int j = 0;
            InputStream inputstream = urlconnection.getInputStream();
            while((j += inputstream.read(abyte0, j, i - j)) < i) ;
            inputstream.close();
            class1 = defineClass(s, abyte0, 0, i);
        }
        catch(Exception _ex)
        {
            class1 = null;
        }
        return class1;
    }

    public void putClass(Class class1)
    {
        cash.put(class1.getName(), class1);
    }

    private String replace(String s, char c, char c1, boolean flag)
    {
        StringBuffer stringbuffer = new StringBuffer(s);
        int i = stringbuffer.length();
        for(int j = 0; j < i; j++)
        {
            if(stringbuffer.charAt(j) == c)
            {
                stringbuffer.setCharAt(j, c1);
            }
        }

        if(flag)
        {
            stringbuffer.append(".class");
        }
        return stringbuffer.toString();
    }
}
