package syi.util;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

public class PProperties extends Hashtable
{

    private static final String str_empty = "";

    public PProperties()
    {
    }

    public final boolean getBool(String s)
    {
        return getBool(s, false);
    }

    public final boolean getBool(String s, boolean flag)
    {
        try
        {
            s = (String)get(s);
            if(s == null || s.length() <= 0)
            {
                return flag;
            }
            char c = Character.toLowerCase(s.charAt(0));
            switch(c)
            {
            case 49: // '1'
            case 111: // 'o'
            case 116: // 't'
            case 121: // 'y'
                return true;

            case 48: // '0'
            case 102: // 'f'
            case 110: // 'n'
            case 120: // 'x'
                return false;
            }
        }
        catch(Exception _ex) { }
        return flag;
    }

    public final int getInt(String s)
    {
        try
        {
            return getInt(s, 0);
        }
        catch(Exception _ex)
        {
            return 0;
        }
    }

    public final int getInt(String s, int i)
    {
        try
        {
            String s1 = (String)get(s);
            if(s1 != null && s1.length() > 0)
            {
                return Integer.decode(s1).intValue();
            }
        }
        catch(Throwable _ex) { }
        return i;
    }

    public final String getString(String s)
    {
        return getString(s, "");
    }

    public final String getString(String s, String s1)
    {
        if(s == null)
        {
            return s1;
        }
        Object obj = get(s);
        if(obj == null)
        {
            return s1;
        } else
        {
            return obj.toString();
        }
    }

    public synchronized boolean load(InputStream inputstream)
    {
        clear();
        return loadPut(inputstream);
    }

    public synchronized boolean load(Reader reader)
    {
        clear();
        return loadPut(reader);
    }

    public synchronized void load(String s)
    {
        if(s == null || s.length() <= 0)
        {
            return;
        } else
        {
            load(((Reader) (new StringReader(s))));
            return;
        }
    }

    public synchronized boolean loadPut(InputStream inputstream)
    {
        return loadPut(((Reader) (new InputStreamReader(inputstream))));
    }

    public synchronized boolean loadPut(Reader reader)
    {
        try
        {
            Object obj = (reader instanceof StringReader) ? ((Object) (reader)) : ((Object) (new BufferedReader(reader, 1024)));
            CharArrayWriter chararraywriter = new CharArrayWriter();
            String s1 = null;
            try
            {
                do
                {
                    String s;
                    do
                    {
                        s = readLine(((Reader) (obj)));
                    } while(s == null);
                    int i = s.indexOf('=');
                    if(i > 0)
                    {
                        if(s1 != null)
                        {
                            put(s1, chararraywriter.toString());
                            s1 = null;
                        }
                        s1 = s.substring(0, i);
                        chararraywriter.reset();
                        if(i + 1 < s.length())
                        {
                            chararraywriter.write(s.substring(i + 1));
                        }
                    } else
                    if(s1 != null)
                    {
                        chararraywriter.write(10);
                        chararraywriter.write(s);
                    }
                } while(true);
            }
            catch(EOFException _ex) { }
            if(s1 != null && chararraywriter.size() > 0)
            {
                put(s1, chararraywriter.toString());
            }
            ((Reader) (obj)).close();
        }
        catch(IOException ioexception)
        {
            System.out.println("load" + ioexception.getMessage());
            return false;
        }
        return true;
    }

    private final String readLine(Reader reader)
        throws EOFException, IOException
    {
        int i = reader.read();
        if(i == -1)
        {
            throw new EOFException();
        }
        if(i == 13 || i == 10)
        {
            return null;
        }
        if(i == 35)
        {
            do
            {
                i = reader.read();
            } while(i != 13 && i != 10 && i != -1);
            return null;
        }
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append((char)i);
        while((i = reader.read()) != -1) 
        {
            if(i == 13 || i == 10)
            {
                break;
            }
            stringbuffer.append((char)i);
        }
        return stringbuffer.toString();
    }

    public void save(OutputStream outputstream)
        throws Exception
    {
        save(outputstream, null);
    }

    public synchronized void save(OutputStream outputstream, Hashtable hashtable)
        throws IOException
    {
        PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(outputstream), false);
        boolean flag = hashtable != null;
        for(Enumeration enumeration = keys(); enumeration.hasMoreElements();)
        {
            String s1 = (String)enumeration.nextElement();
            if(s1 != null && s1.length() > 0 && s1.charAt(0) != '#')
            {
                if(flag)
                {
                    String s2 = (String)hashtable.get(s1);
                    if(s2 != null && s2.length() > 0)
                    {
                        StringReader stringreader = new StringReader(s2);
                        try
                        {
                            do
                            {
                                String s3 = readLine(stringreader);
                                printwriter.print('#');
                                printwriter.println(s3);
                            } while(true);
                        }
                        catch(EOFException _ex) { }
                    }
                }
                String s = getString(s1);
                printwriter.print(s1);
                printwriter.print('=');
                if(s != null && s.length() > 0)
                {
                    printwriter.print(s);
                }
                printwriter.println();
                printwriter.println();
            }
        }

        printwriter.flush();
        printwriter.close();
        outputstream.close();
    }
}
