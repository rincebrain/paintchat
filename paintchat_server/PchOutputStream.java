package paintchat_server;

import java.io.*;
import syi.util.Io;

public class PchOutputStream extends OutputStream
{

    private OutputStream out;
    private boolean isWriteHeader;
    public static String OPTION_MGCOUNT = "mg_count";
    public static String OPTION_VERSION = "version";
    private int write_size;

    public PchOutputStream(OutputStream outputstream, boolean flag)
    {
        isWriteHeader = false;
        write_size = 0;
        out = outputstream;
        isWriteHeader = flag;
    }

    public void close()
        throws IOException
    {
        if(!isWriteHeader)
        {
            writeHeader();
        }
        out.close();
    }

    public void flush()
        throws IOException
    {
        if(!isWriteHeader)
        {
            writeHeader();
        }
        out.flush();
    }

    public void write(byte abyte0[])
        throws IOException
    {
        write(abyte0, 0, abyte0.length);
    }

    public void write(byte abyte0[], int i, int j)
        throws IOException
    {
        if(!isWriteHeader)
        {
            writeHeader();
        }
        Io.wShort(out, j);
        out.write(abyte0, i, j);
    }

    public void write(int i)
        throws IOException
    {
        if(!isWriteHeader)
        {
            writeHeader();
        }
        out.write(i);
    }

    public void write(File file)
        throws IOException
    {
        if(!isWriteHeader)
        {
            writeHeader();
        }
        int i = 0;
        int j = (int)file.length();
        byte abyte0[] = (byte[])null;
        if(j <= 2)
        {
            return;
        }
        FileInputStream fileinputstream = null;
        try
        {
            fileinputstream = new FileInputStream(file);
            while(i < j) 
            {
                int k = Io.readUShort(fileinputstream);
                if(abyte0 == null || abyte0.length < k)
                {
                    abyte0 = new byte[k];
                }
                Io.rFull(fileinputstream, abyte0, 0, k);
                write(abyte0, 0, k);
            }
        }
        catch(IOException _ex) { }
        if(fileinputstream != null)
        {
            try
            {
                fileinputstream.close();
            }
            catch(IOException _ex) { }
        }
    }

    private void writeHeader()
        throws IOException
    {
        if(isWriteHeader)
        {
            return;
        } else
        {
            out.write(13);
            out.write(10);
            isWriteHeader = true;
            return;
        }
    }

    public void writeHeader(String s, String s1)
        throws IOException
    {
        if(isWriteHeader)
        {
            return;
        } else
        {
            out.write((s + "=" + s1 + "\r\n").getBytes("UTF8"));
            return;
        }
    }

    public int size()
    {
        return write_size;
    }

}
