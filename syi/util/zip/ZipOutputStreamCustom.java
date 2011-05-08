package syi.util.zip;

import java.io.*;
import java.util.zip.*;
import syi.util.ByteStream;
import syi.util.Io;

public class ZipOutputStreamCustom extends ZipOutputStream
{

    private boolean is_deflate;
    private CRC32 crc32;

    public ZipOutputStreamCustom(OutputStream outputstream)
    {
        super(outputstream);
        is_deflate = true;
        crc32 = null;
    }

    public static void main(String args[])
    {
        try
        {
            File file = new File("C:\\IBMVJava\\Ide\\project_resources\\PaintChat\\sub\\bbs\\pbbs\\");
            ZipOutputStreamCustom zipoutputstreamcustom = new ZipOutputStreamCustom(new FileOutputStream(new File(file, "_PaintBBS.jar")));
            zipoutputstreamcustom.setMethod(8);
            zipoutputstreamcustom.setLevel(9);
            zipoutputstreamcustom.putZip(new ZipInputStream(new FileInputStream(new File(file, "PaintBBS.jar"))));
            zipoutputstreamcustom.finish();
            zipoutputstreamcustom.close();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        System.exit(0);
    }

    public void putBytes(byte abyte0[], int i, int j, String s)
        throws IOException
    {
        ZipEntry zipentry = new ZipEntry(s);
        if(!is_deflate)
        {
            if(crc32 == null)
            {
                crc32 = new CRC32();
            } else
            {
                crc32.reset();
            }
            crc32.update(abyte0);
            zipentry.setSize(abyte0.length);
            zipentry.setCrc(crc32.getValue());
        }
        putNextEntry(zipentry);
        write(abyte0, i, j);
        closeEntry();
    }

    public void putDirectory(File file, String s)
        throws IOException
    {
        File file1 = new File(file, s);
        if(file1.isFile())
        {
            putFile(file, s);
            return;
        }
        if(!file1.exists())
        {
            return;
        }
        String as[] = file1.list();
        for(int i = 0; i < as.length; i++)
        {
            new File(file1, as[i]);
            putDirectory(file, s + '/' + as[i]);
        }

    }

    public void putFile(File file, String s)
        throws IOException
    {
        byte abyte0[] = (byte[])null;
        File file1 = new File(file, s);
        if(file1.isDirectory())
        {
            putDirectory(file, s);
            return;
        }
        if(!file1.exists())
        {
            return;
        }
        try
        {
            abyte0 = new byte[(int)file1.length()];
            FileInputStream fileinputstream = new FileInputStream(file1);
            Io.rFull(fileinputstream, abyte0, 0, abyte0.length);
            fileinputstream.close();
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
            abyte0 = (byte[])null;
        }
        if(abyte0 == null)
        {
            return;
        } else
        {
            putBytes(abyte0, 0, abyte0.length, s);
            return;
        }
    }

    public void putFile(String s, String s1)
        throws IOException
    {
        putFile(new File(s), s1);
    }

    public void putZip(ZipInputStream zipinputstream)
        throws IOException
    {
        ByteStream bytestream = new ByteStream();
        byte abyte0[] = new byte[512];
        ZipEntry zipentry;
        while((zipentry = zipinputstream.getNextEntry()) != null) 
        {
            int i;
            while((i = zipinputstream.read(abyte0)) != -1) 
            {
                bytestream.write(abyte0, 0, i);
            }
            zipinputstream.closeEntry();
            putBytes(bytestream.getBuffer(), 0, bytestream.size(), zipentry.getName());
            bytestream.reset();
        }
        zipinputstream.close();
    }

    public void setMethod(int i)
    {
        is_deflate = i == 8;
        super.setMethod(i);
    }
}
