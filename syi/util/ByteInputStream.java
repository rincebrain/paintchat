package syi.util;

import java.io.IOException;
import java.io.InputStream;

// Referenced classes of package syi.util:
//            ByteStream

public class ByteInputStream extends InputStream
{

    private byte buffer[];
    private int iSeekStart;
    private int iSeek;
    private int iLen;

    public ByteInputStream()
    {
        iSeekStart = 0;
        iSeek = 0;
        iLen = 0;
    }

    public int available()
    {
        return iLen - iSeek;
    }

    public int read()
    {
        return iSeek < iLen ? buffer[iSeek++] & 0xff : -1;
    }

    public int read(byte abyte0[], int i, int j)
    {
        if(iSeek >= iLen)
        {
            return -1;
        } else
        {
            j = Math.min(iLen - iSeek, j);
            System.arraycopy(buffer, iSeek, abyte0, i, j);
            iSeek += j;
            return j;
        }
    }

    public void reset()
    {
        iSeek = iSeekStart;
    }

    public void setBuffer(byte abyte0[], int i, int j)
    {
        buffer = abyte0;
        iLen = i + j;
        iSeekStart = iSeek = i;
    }

    public void setByteStream(ByteStream bytestream)
    {
        setBuffer(bytestream.getBuffer(), 0, bytestream.size());
    }

    public void close()
        throws IOException
    {
    }

    public int read(byte abyte0[])
        throws IOException
    {
        return read(abyte0, 0, abyte0.length);
    }

    public long skip(long l)
        throws IOException
    {
        l = Math.min(l, iLen - iSeek);
        iSeek += l;
        return l;
    }

    public boolean markSupported()
    {
        return true;
    }

    public void mark(int i)
    {
        iSeekStart = iSeek;
    }
}
