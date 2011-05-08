package paintchat;

import java.io.*;

public class MgText
{

    public byte head;
    public int ID;
    public byte bName[];
    private byte data[];
    private int seekMax;
    private String strData;
    public static final String ENCODE = "UTF8";
    private static final String EMPTY = "";
    public static final byte M_TEXT = 0;
    public static final byte M_IN = 1;
    public static final byte M_OUT = 2;
    public static final byte M_UPDATE = 3;
    public static final byte M_VERSION = 4;
    public static final byte M_EXIT = 5;
    public static final byte M_INFOMATION = 6;
    public static final byte M_SCRIPT = 8;
    public static final byte M_EMPTY = 100;
    public static final byte M_SERVER = 102;

    public MgText()
    {
        head = 100;
        ID = 0;
        bName = null;
        data = null;
        seekMax = 0;
        strData = null;
    }

    public MgText(int i, byte byte0, byte abyte0[])
    {
        head = 100;
        ID = 0;
        bName = null;
        data = null;
        seekMax = 0;
        strData = null;
        setData(i, byte0, abyte0);
    }

    public MgText(int i, byte byte0, String s)
    {
        head = 100;
        ID = 0;
        bName = null;
        data = null;
        seekMax = 0;
        strData = null;
        setData(i, byte0, s);
    }

    public MgText(MgText mgtext)
    {
        head = 100;
        ID = 0;
        bName = null;
        data = null;
        seekMax = 0;
        strData = null;
        setData(mgtext);
    }

    public void getData(OutputStream outputstream, boolean flag)
        throws IOException
    {
        if(head == -1)
        {
            return;
        }
        toBin(false);
        int i = bName != null && flag ? bName.length : 0;
        int j = seekMax + 4 + i;
        if(i >= 255)
        {
            throw new IOException("longer name");
        }
        w2(outputstream, j);
        outputstream.write(head);
        w2(outputstream, ID);
        if(flag)
        {
            outputstream.write(i);
            if(i > 0)
            {
                outputstream.write(bName);
            }
        } else
        {
            outputstream.write(0);
        }
        if(seekMax > 0)
        {
            outputstream.write(data, 0, seekMax);
        }
    }

    public String getUserName()
    {
        try
        {
            if(bName != null)
            {
                return new String(bName, "UTF8");
            }
        }
        catch(UnsupportedEncodingException _ex) { }
        return "";
    }

    public void setUserName(Object obj)
    {
        try
        {
            if(obj == null)
            {
                bName = null;
            } else
            if(obj instanceof String)
            {
                String s = (String)obj;
                bName = s.length() > 0 ? s.getBytes("UTF8") : null;
            } else
            {
                bName = (byte[])obj;
            }
        }
        catch(UnsupportedEncodingException _ex)
        {
            bName = null;
        }
    }

    public int getValueSize()
    {
        toBin(false);
        return seekMax;
    }

    private final int r(InputStream inputstream)
        throws IOException
    {
        int i = inputstream.read();
        if(i == -1)
        {
            throw new EOFException();
        } else
        {
            return i;
        }
    }

    private final int r2(InputStream inputstream)
        throws IOException
    {
        return r(inputstream) << 8 | r(inputstream);
    }

    private final void r(InputStream inputstream, byte abyte0[], int i)
        throws IOException
    {
        int k;
        for(int j = 0; j < i; j += k)
        {
            k = inputstream.read(abyte0, j, i - j);
            if(k == -1)
            {
                throw new EOFException();
            }
        }

    }

    private final void w2(OutputStream outputstream, int i)
        throws IOException
    {
        outputstream.write(i >>> 8 & 0xff);
        outputstream.write(i & 0xff);
    }

    public void setData(int i, byte byte0, byte abyte0[])
    {
        int j = abyte0.length;
        head = byte0;
        ID = i;
        bName = null;
        if(data == null || data.length < j)
        {
            data = abyte0;
        } else
        {
            System.arraycopy(abyte0, 0, data, 0, j);
        }
        seekMax = j;
        strData = null;
    }

    public void setData(int i, byte byte0, String s)
    {
        head = byte0;
        ID = i;
        bName = null;
        strData = s;
        seekMax = 0;
    }

    public int setData(InputStream inputstream)
        throws IOException
    {
        try
        {
            strData = null;
            int i = r2(inputstream);
            head = (byte)r(inputstream);
            ID = r2(inputstream);
            int j = r(inputstream);
            if(j <= 0)
            {
                bName = null;
            } else
            {
                if(j >= 255)
                {
                    throw new IOException("abnormal");
                }
                bName = new byte[j];
                r(inputstream, bName, j);
            }
            int k = i - 1 - 2 - 1 - j;
            if(k > 0)
            {
                if(k >= 1024)
                {
                    throw new IOException("abnormal");
                }
                if(data == null || data.length < k)
                {
                    data = new byte[k];
                }
                r(inputstream, data, k);
                seekMax = k;
            } else
            {
                seekMax = 0;
            }
            return i + 2;
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
        }
        head = 100;
        ID = 0;
        bName = null;
        seekMax = 0;
        return 2;
    }

    public void setData(MgText mgtext)
    {
        head = mgtext.head;
        ID = mgtext.ID;
        bName = mgtext.bName;
        seekMax = mgtext.seekMax;
        if(seekMax > 0)
        {
            if(data == null || data.length < seekMax)
            {
                data = new byte[seekMax];
            }
            System.arraycopy(mgtext.data, 0, data, 0, seekMax);
        }
        strData = mgtext.strData;
    }

    public final void toBin(boolean flag)
    {
        if(seekMax == 0 && strData != null && strData.length() > 0)
        {
            try
            {
                byte abyte0[] = strData.getBytes("UTF8");
                int i = abyte0.length;
                if(data == null || data.length < i)
                {
                    data = abyte0;
                } else
                {
                    System.arraycopy(abyte0, 0, data, 0, i);
                }
                seekMax = i;
            }
            catch(UnsupportedEncodingException _ex)
            {
                strData = null;
                seekMax = 0;
            }
        }
        if(flag)
        {
            strData = null;
        }
    }

    public String toString()
    {
        try
        {
            if(seekMax > 0 && strData == null)
            {
                strData = new String(data, 0, seekMax, "UTF8");
            }
            if(strData != null)
            {
                return strData;
            }
        }
        catch(RuntimeException _ex) { }
        catch(UnsupportedEncodingException _ex) { }
        strData = null;
        seekMax = 0;
        return "";
    }
}
