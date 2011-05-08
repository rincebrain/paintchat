package syi.util;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Io
{

    public Io()
    {
    }

    public static void close(Object obj)
    {
        if(obj == null)
        {
            return;
        }
        try
        {
            if(obj instanceof InputStream)
            {
                ((InputStream)obj).close();
                return;
            }
            if(obj instanceof OutputStream)
            {
                ((OutputStream)obj).close();
                return;
            }
            if(obj instanceof Reader)
            {
                ((Reader)obj).close();
                return;
            }
            if(obj instanceof Writer)
            {
                ((Writer)obj).close();
                return;
            }
            if(obj instanceof RandomAccessFile)
            {
                ((RandomAccessFile)obj).close();
                return;
            }
        }
        catch(IOException _ex) { }
        try
        {
            obj.getClass().getMethod("close", new Class[0]).invoke(obj, new Object[0]);
        }
        catch(NoSuchMethodException _ex) { }
        catch(IllegalAccessException _ex) { }
        catch(InvocationTargetException _ex) { }
    }

    public static void copyDirectory(File file, File file1)
    {
        copyDirectory(file, file1, null);
    }

    public static void copyDirectory(File file, File file1, Vector vector)
    {
        if(!file1.isDirectory())
        {
            file1.mkdirs();
        }
        String as[] = file.list();
        for(int i = 0; i < as.length; i++)
        {
            File file2 = new File(file, as[i]);
            if(file2.isFile())
            {
                copyFile(new File(file, as[i]), file1, vector);
            } else
            {
                copyDirectory(new File(file, as[i]), new File(file1, as[i]), vector);
            }
        }

    }

    public static void copy(File file, File file1, Vector vector)
    {
        if(file.isFile())
        {
            copyFile(file, file1, vector);
        } else
        if(file.isDirectory())
        {
            copyDirectory(file, file1, vector);
        }
    }

    public static boolean copyFile(File file, File file1)
    {
        try
        {
            File file2 = getDirectory(file1);
            if(!file2.isDirectory())
            {
                file2.mkdirs();
            }
            if(file1.isDirectory())
            {
                file1 = new File(file1, getFileName(file.toString()));
            }
            byte abyte0[] = new byte[512];
            FileInputStream fileinputstream = new FileInputStream(file);
            FileOutputStream fileoutputstream = new FileOutputStream(file1);
            int i;
            while((i = fileinputstream.read(abyte0)) != -1) 
            {
                fileoutputstream.write(abyte0, 0, i);
            }
            fileoutputstream.flush();
            fileoutputstream.close();
            fileinputstream.close();
            return true;
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        return false;
    }

    public static boolean copyFile(File file, File file1, Vector vector)
    {
        try
        {
            if(file.isDirectory())
            {
                throw new IOException("src is directory");
            }
            if(vector != null)
            {
                boolean flag = false;
                String s = getFileName(file.getCanonicalPath()).toLowerCase();
                for(Enumeration enumeration = vector.elements(); enumeration.hasMoreElements();)
                {
                    String s1 = enumeration.nextElement().toString();
                    if(s1.endsWith("*") || s.endsWith(s1))
                    {
                        flag = true;
                        break;
                    }
                }

                if(!flag)
                {
                    return false;
                }
            }
            copyFile(file, file1);
            return true;
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        return false;
    }

    public static void copyFiles(File afile[], File file)
    {
        for(int i = 0; i < afile.length; i++)
        {
            copyFile(afile[i], file);
        }

    }

    public static void copyFiles(String as[], String s)
    {
        for(int i = 0; i < as.length; i++)
        {
            copyFile(new File(as[i]), new File(s));
        }

    }

    public static String getCurrent()
    {
        return System.getProperty("user.dir", "/");
    }

    public static final String getDateString(String s, String s1, String s2)
    {
        String s3 = '.' + s1;
        String s4 = null;
        File file = new File(s2);
        if(!file.isDirectory())
        {
            file.mkdirs();
        }
        try
        {
            GregorianCalendar gregoriancalendar = new GregorianCalendar();
            String s5 = s + gregoriancalendar.get(2) + '-' + gregoriancalendar.get(5) + '_';
            int i;
            for(i = 0; i < 256; i++)
            {
                File file1 = new File(s2, s5 + i + s3);
                if(file1.isFile())
                {
                    continue;
                }
                s4 = s2 + "/" + s5 + i + s3;
                break;
            }

            if(i >= 32767)
            {
                s4 = s2 + "/" + s + "over_file255" + s3;
            }
        }
        catch(RuntimeException _ex)
        {
            s4 = s + "." + s1;
        }
        return s4;
    }

    public static File getDirectory(File file)
    {
        try
        {
            return new File(getDirectory(file.getCanonicalPath()));
        }
        catch(IOException _ex)
        {
            return null;
        }
    }

    public static String getDirectory(String s)
    {
        if(s == null || s.length() <= 0)
        {
            return "./";
        }
        if(s.indexOf('\\') >= 0)
        {
            s = s.replace('\\', '/');
        }
        int i = s.lastIndexOf('/');
        if(i < 0)
        {
            return "./";
        }
        int j = s.indexOf('.', i);
        if(j < i)
        {
            if(s.charAt(s.length() - 1) != '/')
            {
                s = s + '/';
            }
        } else
        {
            s = s.substring(0, i + 1);
        }
        return s;
    }

    public static String getFileName(String s)
    {
        if(s.lastIndexOf('.') < 0)
        {
            return "";
        }
        int i = s.lastIndexOf('/');
        if(i < 0)
        {
            i = s.lastIndexOf('\\');
        }
        if(i < 0)
        {
            return s;
        } else
        {
            return s.substring(i + 1);
        }
    }

    public static void initFile(File file)
    {
        try
        {
            File file1 = getDirectory(file);
            if(!file1.isDirectory())
            {
                file1.mkdirs();
            }
        }
        catch(RuntimeException _ex) { }
    }

    public static Image loadImageNow(Component component, String s)
    {
        Image image = null;
        try
        {
            image = component.getToolkit().getImage(s);
            MediaTracker mediatracker = new MediaTracker(component);
            mediatracker.addImage(image, 0);
            mediatracker.waitForID(0, 10000L);
            mediatracker.removeImage(image);
            mediatracker = null;
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return image;
    }

    public static String loadString(File file)
        throws IOException
    {
        StringBuffer stringbuffer = new StringBuffer();
        BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
        int i;
        while((i = bufferedreader.read()) != -1) 
        {
            stringbuffer.append((char)i);
        }
        bufferedreader.close();
        return stringbuffer.toString();
    }

    public static File makeFile(String s, String s1)
    {
        File file = toDir(s);
        if(!file.exists())
        {
            file.mkdirs();
        }
        return new File(file, s1);
    }

    public static final void moveFile(File file, File file1)
        throws Throwable
    {
        if(!file.renameTo(file1))
        {
            copyFile(file, file1);
            file.delete();
        }
    }

    public static final int r(InputStream inputstream)
        throws IOException
    {
        int i = inputstream.read();
        if(i == -1)
        {
            throw new EOFException();
        } else
        {
            return i;
        }
    }

    public static final int readInt(InputStream inputstream)
        throws IOException
    {
        int i = inputstream.read();
        int j = inputstream.read();
        int k = inputstream.read();
        int l = inputstream.read();
        if((i | j | k | l) < 0)
        {
            throw new EOFException();
        } else
        {
            return (i << 24) + (j << 16) + (k << 8) + l;
        }
    }

    public static final short readShort(InputStream inputstream)
        throws IOException
    {
        int i = inputstream.read();
        int j = inputstream.read();
        if((i | j) < 0)
        {
            throw new EOFException();
        } else
        {
            return (short)((i << 8) + j);
        }
    }

    public static final int readUShort(InputStream inputstream)
        throws IOException
    {
        int i = inputstream.read();
        int j = inputstream.read();
        if((i | j) < 0)
        {
            throw new EOFException();
        } else
        {
            return (i << 8) + j;
        }
    }

    public static final void rFull(InputStream inputstream, byte abyte0[], int i, int j)
        throws EOFException, IOException
    {
        int k;
        for(j += i; i < j; i += k)
        {
            k = inputstream.read(abyte0, i, j - i);
            if(k == -1)
            {
                throw new EOFException();
            }
        }

    }

    public static File toDir(String s)
    {
        if(s == null || s.length() <= 0)
        {
            return new File("./");
        }
        if(s.indexOf('\\') >= 0)
        {
            s = s.replace('\\', '/');
        }
        if(s.charAt(s.length() - 1) != '/')
        {
            s = s + '/';
        }
        return new File(s);
    }

    public static final void wInt(OutputStream outputstream, int i)
        throws IOException
    {
        outputstream.write(i >>> 24);
        outputstream.write(i >>> 16 & 0xff);
        outputstream.write(i >>> 8 & 0xff);
        outputstream.write(i & 0xff);
    }

    public static final void wShort(OutputStream outputstream, int i)
        throws IOException
    {
        outputstream.write(i >>> 8 & 0xff);
        outputstream.write(i & 0xff);
    }
}
