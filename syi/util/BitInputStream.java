package syi.util;

import java.io.InputStream;

public class BitInputStream
{

    private byte data[];
    private int seekMax;
    private int seekByte;
    private int seekBit;
    private byte nowByte;
    private InputStream in;

    public BitInputStream()
    {
        seekByte = 0;
        seekBit = 7;
    }

    public BitInputStream(byte abyte0[])
    {
        seekByte = 0;
        seekBit = 7;
        setArray(abyte0);
    }

    public BitInputStream(InputStream inputstream)
    {
        seekByte = 0;
        seekBit = 7;
    }

    public final int r()
    {
        if(seekByte >= seekMax)
        {
            return -1;
        }
        int i = nowByte >> seekBit & 1;
        if(--seekBit < 0)
        {
            seekBit = 7;
            seekByte++;
            if(seekByte < seekMax)
            {
                nowByte = data[seekByte];
            }
        }
        return i;
    }

    public final int rByte()
    {
        int i = 0;
        for(int j = 7; j >= 0; j--)
        {
            if(r() == -1)
            {
                return j != 7 ? i : -1;
            }
            i |= r() << j;
        }

        return i;
    }

    public void setArray(byte abyte0[])
    {
        setArray(abyte0, abyte0.length);
    }

    public void setArray(byte abyte0[], int i)
    {
        setArray(abyte0, 0, i);
    }

    public void setArray(byte abyte0[], int i, int j)
    {
        data = abyte0;
        seekByte = i;
        seekBit = 7;
        seekMax = j;
        nowByte = data[i];
    }
}
