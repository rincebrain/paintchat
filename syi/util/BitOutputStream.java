package syi.util;

import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream extends OutputStream
{

    private int bit_count;
    private int bit;
    private OutputStream out;

    public BitOutputStream(OutputStream outputstream)
    {
        bit_count = 0;
        out = outputstream;
    }

    public void close()
        throws IOException
    {
        if(bit_count > 0)
        {
            wBit(0, 8 - bit_count);
        }
        flush();
        out.close();
    }

    public void flush()
        throws IOException
    {
        out.flush();
    }

    public void wBit(int i, int j)
        throws IOException
    {
        int k;
        for(; j > 0; j -= k)
        {
            if(bit_count == 0 && j >= 8)
            {
                k = 8;
                out.write(i & 0xff);
            } else
            {
                k = Math.min(8 - bit_count, j);
                bit |= i << bit_count;
                bit_count += k;
                if(bit_count >= 8)
                {
                    out.write(bit);
                    bit = 0;
                    bit_count = 0;
                }
            }
            i >>>= k;
        }

    }

    public void write(int i)
        throws IOException
    {
        wBit(i, 8);
    }
}
