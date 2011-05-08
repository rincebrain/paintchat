package syi.util.zip;

import java.io.*;
import java.util.zip.*;
import syi.util.ByteStream;
import syi.util.Io;

public class CustomDeflaterOutputStream
{

    private ByteStream stm;
    private ByteStream work;
    private byte buffer[];
    private OutputStream Out;
    private boolean isOriginal;
    private ZipOutputStream OutZip;
    private CRC32 crc;

    public CustomDeflaterOutputStream(OutputStream outputstream)
    {
        this(outputstream, false);
    }

    public CustomDeflaterOutputStream(OutputStream outputstream, boolean flag)
    {
        stm = new ByteStream();
        work = new ByteStream();
        buffer = new byte[32767];
        isOriginal = false;
        crc = new CRC32();
        Out = outputstream;
        isOriginal = flag;
        if(!flag)
        {
            OutZip = new ZipOutputStream(stm);
            OutZip.setMethod(0);
            OutZip.setLevel(0);
        }
    }

    public void close()
        throws IOException
    {
        close(null);
    }

    public void close(String s)
        throws IOException
    {
        if(!isOriginal)
        {
            OutZip.close();
            ZipOutputStream zipoutputstream = new ZipOutputStream(Out);
            zipoutputstream.setLevel(9);
            zipoutputstream.putNextEntry(new ZipEntry(s != null && s.length() != 0 ? s : "0.zip"));
            stm.writeTo(zipoutputstream);
            zipoutputstream.flush();
            zipoutputstream.closeEntry();
            zipoutputstream.close();
        } else
        {
            Deflater deflater = new Deflater(9, true);
            deflater.setInput(stm.getBuffer(), 0, stm.size());
            deflater.finish();
            while(!deflater.finished()) 
            {
                int i = deflater.deflate(buffer, 0, 1);
                if(i > 0)
                {
                    Out.write(buffer, 0, i);
                }
            }
        }
        try
        {
            Out.flush();
            Out.close();
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public static void compress(File file, File file1)
    {
        try
        {
            CustomDeflaterOutputStream customdeflateroutputstream = new CustomDeflaterOutputStream(new FileOutputStream(file1), false);
            customdeflateroutputstream.write(file);
            customdeflateroutputstream.close(file.getName());
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        try
        {
            File file = new File(args[0] + ".tmp");
            File file1 = new File(args[0]);
            compress(file1, file);
            file1.delete();
            file.renameTo(file1);
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        System.exit(0);
    }

    public void write(File file)
        throws IOException
    {
        ZipInputStream zipinputstream = new ZipInputStream(new FileInputStream(file));
        ZipEntry zipentry;
        if(isOriginal)
        {
            while((zipentry = zipinputstream.getNextEntry()) != null) 
            {
                work.reset();
                work.write(zipentry.getName().getBytes());
                work.write(0);
                int i;
                while((i = zipinputstream.read(buffer)) != -1) 
                {
                    work.write(buffer, 0, i);
                }
                if(work.size() > 0)
                {
                    Io.wShort(stm, work.size() & 0xffff);
                    work.writeTo(stm);
                }
            }
        } else
        {
            while((zipentry = zipinputstream.getNextEntry()) != null) 
            {
                work.reset();
                int j;
                while((j = zipinputstream.read(buffer)) != -1) 
                {
                    work.write(buffer, 0, j);
                }
                if(work.size() > 0)
                {
                    crc.reset();
                    crc.update(work.getBuffer(), 0, work.size());
                    ZipEntry zipentry1 = new ZipEntry(zipentry.getName());
                    zipentry1.setSize(work.size());
                    zipentry1.setCrc(crc.getValue());
                    OutZip.putNextEntry(zipentry1);
                    work.writeTo(OutZip);
                    OutZip.flush();
                    OutZip.closeEntry();
                }
            }
        }
        zipinputstream.close();
    }

    public void write(ByteStream bytestream)
        throws IOException
    {
        ZipInputStream zipinputstream = new ZipInputStream(new ByteArrayInputStream(bytestream.getBuffer(), 0, bytestream.size()));
        ZipEntry zipentry;
        if(isOriginal)
        {
            while((zipentry = zipinputstream.getNextEntry()) != null) 
            {
                work.reset();
                work.write(zipentry.getName().getBytes());
                work.write(0);
                int i;
                while((i = zipinputstream.read(buffer)) != -1) 
                {
                    work.write(buffer, 0, i);
                }
                if(work.size() > 0)
                {
                    Io.wShort(bytestream, work.size() & 0xffff);
                    work.writeTo(bytestream);
                }
            }
        } else
        {
            while((zipentry = zipinputstream.getNextEntry()) != null) 
            {
                work.reset();
                int j;
                while((j = zipinputstream.read(buffer)) != -1) 
                {
                    work.write(buffer, 0, j);
                }
                if(work.size() > 0)
                {
                    crc.reset();
                    crc.update(work.getBuffer(), 0, work.size());
                    ZipEntry zipentry1 = new ZipEntry(zipentry.getName());
                    zipentry1.setSize(work.size());
                    zipentry1.setCrc(crc.getValue());
                    OutZip.putNextEntry(zipentry1);
                    work.writeTo(OutZip);
                    OutZip.flush();
                    OutZip.closeEntry();
                }
            }
        }
        zipinputstream.close();
    }
}
