package syi.util;


public class VectorBin
{

    private byte elements[][];
    private int seek;
    private float sizeAdd;
    private int sizeDefault;

    public VectorBin()
    {
        this(10);
    }

    public VectorBin(int i)
    {
        elements = null;
        seek = 0;
        sizeAdd = 1.5F;
        sizeDefault = 25;
        i = i > 0 ? i : 50;
        sizeDefault = i;
        elements = new byte[i][];
    }

    public final synchronized void add(byte abyte0[])
    {
        if(seek >= elements.length)
        {
            moreData(seek + 1);
        }
        elements[seek++] = abyte0;
    }

    public final synchronized void copy(byte abyte0[][], int i, int j, int k)
    {
        k = i + k <= seek ? k : seek - i;
        if(k <= 0)
        {
            return;
        } else
        {
            System.arraycopy(elements, i, abyte0, j, k);
            return;
        }
    }

    public final synchronized void copy(VectorBin vectorbin)
    {
        for(int i = 0; i < seek; i++)
        {
            vectorbin.add(elements[i]);
        }

    }

    public final synchronized void gc()
    {
        int i = Math.max(seek, sizeDefault) + 3;
        if(elements.length > i)
        {
            byte abyte0[][] = new byte[i][];
            System.arraycopy(elements, 0, abyte0, 0, seek);
            elements = abyte0;
        }
    }

    public final synchronized byte[] get(int i)
    {
        return i < seek ? elements[i] : null;
    }

    public synchronized int getSizeBytes()
    {
        int i = 0;
        byte abyte0[][] = elements;
        int j = seek;
        for(int k = 0; k < j; k++)
        {
            i += abyte0[k].length;
        }

        return i;
    }

    private final void moreData(int i)
    {
        byte abyte0[][] = new byte[i + Math.max((int)((float)i * sizeAdd), 1)][];
        System.arraycopy(elements, 0, abyte0, 0, elements.length);
        elements = abyte0;
    }

    public final synchronized void remove(int i)
    {
        i = Math.min(i, seek);
        if(i <= 0)
        {
            return;
        }
        if(seek != i)
        {
            System.arraycopy(elements, i, elements, 0, seek - i);
        }
        for(int j = seek - i; j < seek; j++)
        {
            elements[j] = null;
        }

        seek -= i;
    }

    public final synchronized void removeAll()
    {
        remove(seek);
    }

    public final int size()
    {
        return seek;
    }
}
