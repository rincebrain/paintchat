package syi.util;

import java.io.*;

public class ByteStream extends OutputStream
{

    private byte buffer[];
    private int last;

    public ByteStream()
    {
        this(512);
    }

    public ByteStream(byte abyte0[])
    {
        last = 0;
        buffer = abyte0;
    }

    public ByteStream(int i)
    {
        last = 0;
        buffer = new byte[i > 0 ? i : 512];
    }

    public final void addSize(int i)
    {
        int j = last + i;
        if(buffer.length < j)
        {
            byte abyte0[] = new byte[Math.max((int)((float)buffer.length * 1.5F), j) + 1];
            System.arraycopy(buffer, 0, abyte0, 0, buffer.length);
            buffer = abyte0;
        }
    }

    public void gc()
    {
        if(buffer.length == last)
        {
            return;
        }
        byte abyte0[] = new byte[last];
        if(last != 0)
        {
            System.arraycopy(buffer, 0, abyte0, 0, last);
        }
        buffer = abyte0;
    }

    public byte[] getBuffer()
    {
        return buffer;
    }

    public final void insert(int i, int j)
    {
        buffer[i] = (byte)j;
    }

    public void reset()
    {
        last = 0;
    }

    public void reset(int i)
    {
        int j = last;
        reset();
        if(i < j)
        {
            write(buffer, i, j - i);
        }
    }

    public void seek(int i)
    {
        last = i;
    }

    public final int size()
    {
        return last;
    }

    public byte[] toByteArray()
    {
        byte abyte0[] = new byte[last];
        if(last > 0)
        {
            System.arraycopy(buffer, 0, abyte0, 0, last);
        }
        return abyte0;
    }

    public final void w(long l, int i)
        throws IOException
    {
        for(int j = i - 1; j >= 0; j--)
        {
            write((int)(l >>> (j << 3)) & 0xff);
        }

    }

    public final void w2(int i)
        throws IOException
    {
        write(i >>> 8 & 0xff);
        write(i & 0xff);
    }

    public final void write(byte abyte0[])
    {
        write(abyte0, 0, abyte0.length);
    }

    public final void write(byte abyte0[], int i, int j)
    {
        addSize(j);
        System.arraycopy(abyte0, i, buffer, last, j);
        last += j;
    }

    public final void write(int i)
        throws IOException
    {
        addSize(1);
        buffer[last++] = (byte)i;
    }

    public void write(InputStream inputstream)
        throws IOException
    {
        addSize(128);
        int i;
        try
        {
            while((i = inputstream.read(buffer, last, buffer.length - last)) != -1) 
            {
                last += i;
                if(last >= buffer.length)
                {
                    addSize(256);
                }
            }
        }
        catch(IOException _ex) { }
    }

    public void write(InputStream inputstream, int i)
        throws IOException
    {
        if(i == 0)
        {
            return;
        }
        addSize(i);
        int k = 0;
        int j;
        while((j = inputstream.read(buffer, last, i - k)) != -1) 
        {
            last += j;
            k += j;
            if(k >= i)
            {
                break;
            }
        }
        if(k < i)
        {
            throw new EOFException();
        } else
        {
            return;
        }
    }

    public byte[] writeTo(byte abyte0[], int i)
    {
        int j = i + last;
        if(abyte0 == null)
        {
            abyte0 = new byte[j];
        }
        if(abyte0.length < j)
        {
            byte abyte1[] = new byte[j];
            System.arraycopy(abyte0, 0, abyte1, 0, abyte0.length);
            abyte0 = abyte1;
        }
        System.arraycopy(buffer, 0, abyte0, i, last);
        return abyte0;
    }

    public void writeTo(OutputStream outputstream)
        throws IOException
    {
        if(last == 0)
        {
            return;
        } else
        {
            outputstream.write(buffer, 0, last);
            return;
        }
    }
}
