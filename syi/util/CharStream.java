package syi.util;

import java.io.IOException;
import java.io.Writer;

public class CharStream extends Writer
{

    private char buffer[];
    private int count;
    private static char separator[];

    public CharStream()
    {
        this(512);
    }

    public CharStream(int i)
    {
        count = 0;
        buffer = new char[i > 0 ? i : 512];
        if(separator == null)
        {
            String s = System.getProperty("line.separator", "\n");
            separator = s.toCharArray();
        }
    }

    public final void addSize(int i)
    {
        int j = count + i;
        if(buffer.length < j)
        {
            char ac[] = new char[Math.max((int)((float)buffer.length * 1.25F), j) + 1];
            System.arraycopy(buffer, 0, ac, 0, buffer.length);
            buffer = ac;
        }
    }

    public void close()
    {
    }

    public void flush()
    {
    }

    public void gc()
    {
        if(buffer.length == count)
        {
            return;
        }
        char ac[] = new char[count];
        if(count != 0)
        {
            System.arraycopy(buffer, 0, ac, 0, count);
        }
        buffer = ac;
    }

    public char[] getBuffer()
    {
        return buffer;
    }

    public final void insert(int i, int j)
    {
        buffer[i] = (char)j;
    }

    public void reset()
    {
        count = 0;
    }

    public void seek(int i)
    {
        count = i;
    }

    public final int size()
    {
        return count;
    }

    public char[] toCharArray()
    {
        char ac[] = new char[count];
        if(count > 0)
        {
            System.arraycopy(buffer, 0, ac, 0, count);
        }
        return ac;
    }

    public final void write(char ac[])
    {
        write(ac, 0, ac.length);
    }

    public final void write(char ac[], int i, int j)
    {
        addSize(j);
        System.arraycopy(ac, i, buffer, count, j);
        count += j;
    }

    public final void write(int i)
        throws IOException
    {
        addSize(1);
        buffer[count++] = (char)i;
    }

    public final void write(String s)
    {
        write(s, 0, s.length());
    }

    public final void write(String s, int i, int j)
    {
        addSize(j);
        s.getChars(i, i + j, buffer, count);
        count += j;
    }

    public final void writeln(String s)
    {
        write(s);
        write(separator);
    }

    public void writeTo(char ac[], int i)
    {
        int j = i + count;
        if(i >= j)
        {
            return;
        }
        if(ac.length < j)
        {
            char ac1[] = new char[j];
            System.arraycopy(ac, 0, ac1, 0, ac.length);
            ac = ac1;
        }
        System.arraycopy(buffer, 0, ac, i, count);
    }

    public void writeTo(Writer writer)
        throws IOException
    {
        if(count == 0)
        {
            return;
        } else
        {
            writer.write(buffer, 0, count);
            return;
        }
    }
}
