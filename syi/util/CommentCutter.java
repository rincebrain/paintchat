package syi.util;

import java.awt.*;
import java.io.*;

class CommentCutter
{

    private Reader In;
    private Writer Out;
    private int flagLine[][];
    private int flagWhole[][];
    private int flagWholeEnd[][];
    private int countLine[];
    private int countWhole[];
    private int cash[];
    private int countCash;
    private int oldCh;

    public CommentCutter()
    {
        In = null;
        Out = null;
        flagLine = null;
        flagWhole = null;
        flagWholeEnd = null;
        countLine = null;
        countWhole = null;
        cash = new int[3];
        countCash = 0;
        oldCh = 32;
    }

    public CommentCutter(Reader reader, Writer writer)
    {
        In = null;
        Out = null;
        flagLine = null;
        flagWhole = null;
        flagWholeEnd = null;
        countLine = null;
        countWhole = null;
        cash = new int[3];
        countCash = 0;
        oldCh = 32;
        In = reader;
        Out = writer;
    }

    private void addDefault()
    {
        addLineFlag(new int[] {
            47, 47
        });
        addWholeFlag(new int[] {
            47, 37
        }, new int[] {
            37, 47
        });
    }

    public void addLineFlag(int ai[])
    {
        synchronized(this)
        {
            if(flagLine == null)
            {
                flagLine = new int[0][];
                countLine = new int[0];
            }
            int ai1[][] = new int[flagLine.length + 1][];
            int ai2[] = new int[flagLine.length + 1];
            int i;
            for(i = 0; i < flagLine.length - 1; i++)
            {
                ai1[i] = flagLine[i];
            }

            ai1[i] = ai;
            flagLine = ai1;
            countLine = ai2;
        }
    }

    public void addWholeFlag(int ai[], int ai1[])
    {
        synchronized(this)
        {
            if(flagWhole == null)
            {
                flagWhole = new int[0][];
                flagWholeEnd = new int[0][];
                countWhole = new int[0];
            }
            int ai2[][] = new int[flagWhole.length + 1][];
            int ai3[][] = new int[flagWholeEnd.length + 1][];
            int ai4[] = new int[countWhole.length + 1];
            int i;
            for(i = 0; i < flagLine.length - 1; i++)
            {
                ai2[i] = flagWhole[i];
                ai3[i] = flagWholeEnd[i];
            }

            ai2[i] = ai;
            ai3[i] = ai1;
            flagWhole = ai2;
            flagWholeEnd = ai3;
            countWhole = ai4;
        }
    }

    private void cleanCounter()
    {
        for(int i = 0; i < countLine.length; i++)
        {
            countLine[i] = 0;
        }

        for(int j = 0; j < countWhole.length; j++)
        {
            countWhole[j] = 0;
        }

        countCash = 0;
    }

    public void cut()
        throws IOException
    {
        synchronized(this)
        {
            try
            {
                if(flagLine == null && flagWhole == null)
                {
                    addDefault();
                }
                cleanCounter();
                findFlags();
            }
            catch(EOFException _ex) { }
            In.close();
            Out.flush();
            Out.close();
        }
    }

    private void cutArray(int ai[])
        throws IOException
    {
        int j = 0;
        int k = ai.length;
        int i;
        while((i = In.read()) != -1) 
        {
            j = ai[j] != i ? 0 : j + 1;
            if(j == k)
            {
                break;
            }
        }
    }

    private void cutCRLF()
        throws IOException
    {
        int i;
        while((i = In.read()) != -1) 
        {
            if(i == 13 || i == 10)
            {
                break;
            }
        }
    }

    private void findFlags()
        throws IOException
    {
        int i;
        while((i = In.read()) != -1) 
        {
            if(Character.isWhitespace((char)i))
            {
                i = 32;
            }
            if(switchFlag(i))
            {
                out(i);
            }
        }
    }

    private void inCash(int i)
    {
        if(countCash >= cash.length)
        {
            int ai[] = new int[cash.length];
            System.arraycopy(cash, 0, ai, 0, cash.length);
            cash = ai;
        }
        cash[countCash++] = i;
    }

    public static void main(String args[])
    {
        try
        {
            String s = "c:\\Windows\\\uFF83\uFF9E\uFF7D\uFF78\uFF84\uFF6F\uFF8C\uFF9F\\";
            String s1 = "";
            FileDialog filedialog = new FileDialog(new Frame(), "\u30B3\u30E1\u30F3\u30C8\u3092\u30AB\u30C3\u30C8\u3059\u308B\u30D5\u30A1\u30A4\u30EB" +
"\u3092\u6307\u5B9A"
, 0);
            filedialog.setDirectory(s);
            filedialog.setFile(s1);
            filedialog.show();
            s = filedialog.getDirectory();
            s1 = filedialog.getFile();
            if(s == null || s.equals("null") || s1 == null || s1.equals(null))
            {
                System.exit(0);
            }
            File file = new File(s, s1);
            filedialog.setMode(1);
            filedialog.setTitle("\u30AB\u30C3\u30C8\u3057\u305F\u30D5\u30A1\u30A4\u30EB\u3092\u4FDD\u5B58\u3059\u308B" +
"\u5834\u6240\u3092\u6307\u5B9A"
);
            filedialog.show();
            s = filedialog.getDirectory();
            s1 = filedialog.getFile();
            if(s == null || s.equals("null") || s1 == null || s1.equals(null))
            {
                System.exit(0);
            }
            File file1 = new File(s, s1);
            CommentCutter commentcutter = new CommentCutter(new BufferedReader(new InputStreamReader(new FileInputStream(file), "JISAutoDetect")), new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1))));
            commentcutter.cut();
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        System.exit(0);
    }

    private void out(int i)
        throws IOException
    {
        if(i == 32 && oldCh == 32)
        {
            return;
        } else
        {
            Out.write(i);
            oldCh = i;
            return;
        }
    }

    private void outCash()
        throws IOException
    {
        for(int i = 0; i < countCash; i++)
        {
            out(cash[i]);
        }

        countCash = 0;
    }

    public void setInOut(Reader reader, Writer writer)
    {
        synchronized(this)
        {
            Out = writer;
            In = reader;
        }
    }

    private boolean switchFlag(int i)
        throws IOException
    {
        boolean flag = true;
        for(int j = 0; j < flagLine.length; j++)
        {
            if(flagLine[j][countLine[j]] == i)
            {
                flag = false;
                countLine[j]++;
                if(countLine[j] >= flagLine[j].length)
                {
                    cleanCounter();
                    cutCRLF();
                    return false;
                }
            } else
            {
                countLine[j] = 0;
            }
        }

        if(!flag)
        {
            inCash(i);
        } else
        {
            outCash();
        }
        return flag;
    }
}
