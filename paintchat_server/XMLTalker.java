package paintchat_server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import paintchat.Res;
import syi.util.*;

public abstract class XMLTalker extends DefaultHandler
    implements Runnable
{

    private boolean isLive;
    private boolean isInit;
    private boolean canWrite;
    private boolean doWrite;
    protected int iSendInterval;
    private BufferedWriter Out;
    private BufferedInputStream In;
    private Socket socket;
    private ByteStream stm_buffer;
    private Res status;
    long lTime;
    private SAXParser saxParser;
    private String strTag;
    private Res res_att;
    private final String strTags[] = {
        "talk", "in", "leave", "infomation", "script", "admin"
    };
    private final String strStartTags[] = {
        "<talk", "<in", "<leave", "<infomation", "<script", "<admin"
    };
    private final int strHints[] = {
        0, 1, 2, 6, 8, 102
    };

    public XMLTalker()
    {
        isLive = true;
        isInit = false;
        canWrite = true;
        doWrite = false;
        iSendInterval = 2000;
        stm_buffer = new ByteStream();
        saxParser = null;
        res_att = new Res();
    }

    private synchronized void mInitInside()
        throws Throwable
    {
        if(isInit)
        {
            return;
        }
        isInit = true;
        if(In == null)
        {
            In = new BufferedInputStream(socket.getInputStream());
        }
        if(Out == null)
        {
            Out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
        }
        updateTimer();
    }

    public void updateTimer()
    {
        lTime = System.currentTimeMillis();
    }

    public synchronized void mStart(Socket socket1, InputStream inputstream, OutputStream outputstream, Res res)
    {
        try
        {
            socket = socket1;
            if(inputstream != null)
            {
                In = (inputstream instanceof BufferedInputStream) ? (BufferedInputStream)inputstream : new BufferedInputStream(inputstream);
            }
            if(outputstream != null)
            {
                Out = new BufferedWriter(new OutputStreamWriter(outputstream));
            }
            status = res != null ? res : new Res();
            ThreadPool.poolStartThread(this, 'l');
        }
        catch(Throwable _ex)
        {
            mStop();
        }
    }

    public synchronized void mStop()
    {
        if(!isLive)
        {
            return;
        } else
        {
            isLive = false;
            canWrite = true;
            mDestroy();
            canWrite = false;
            mDestroyInside();
            return;
        }
    }

    private synchronized void mDestroyInside()
    {
        try
        {
            if(Out != null)
            {
                Out.close();
            }
        }
        catch(IOException _ex) { }
        Out = null;
        try
        {
            if(In != null)
            {
                In.close();
            }
        }
        catch(IOException _ex) { }
        In = null;
        try
        {
            if(socket != null)
            {
                socket.close();
            }
        }
        catch(IOException _ex) { }
        socket = null;
    }

    private void mParseXML(InputStream inputstream)
    {
        try
        {
            if(saxParser == null)
            {
                saxParser = SAXParserFactory.newInstance().newSAXParser();
            }
            saxParser.parse(inputstream, this);
        }
        catch(Exception _ex)
        {
            mStop();
        }
    }

    protected abstract void mInit()
        throws IOException;

    protected abstract void mDestroy();

    protected abstract void mRead(String s, String s1, Res res)
        throws IOException;

    protected abstract void mWrite()
        throws IOException;

    public void run()
    {
        try
        {
            mInitInside();
            ByteInputStream byteinputstream = new ByteInputStream();
            long l = 0L;
            long l2 = 0L;
            canWrite = false;
            while(isLive) 
            {
                if(canRead(In))
                {
                    stm_buffer.reset();
                    read(stm_buffer);
                    byteinputstream.setByteStream(stm_buffer);
                    mParseXML(byteinputstream);
                    stm_buffer.reset();
                } else
                {
                    long l3 = System.currentTimeMillis();
                    long l1 = l3 - lTime;
                    if(l3 - l2 >= (long)iSendInterval)
                    {
                        l2 = l3;
                        canWrite = true;
                        mWrite();
                        if(l1 >= 60000L)
                        {
                            stm_buffer.reset();
                            write("ping", null);
                        }
                        canWrite = false;
                        flush();
                    }
                    Thread.currentThread();
                    Thread.sleep(l1 >= 1000L ? l1 >= 5000L ? l1 >= 10000L ? l1 >= 20000L ? '\u0960' : '\u04B0' : '\u0258' : '\u0190' : 200);
                }
            }
        }
        catch(InterruptedException _ex) { }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
        mStop();
    }

    private void setAtt(Attributes attributes)
    {
        int i = attributes.getLength();
        res_att.clear();
        try
        {
            for(int j = 0; j < i; j++)
            {
                res_att.put(attributes.getQName(j), attributes.getValue(j));
            }

            return;
        }
        catch(RuntimeException _ex)
        {
            res_att.clear();
        }
    }

    protected void write(Res res)
        throws IOException
    {
        StringBuffer stringbuffer = new StringBuffer();
        String s;
        for(Enumeration enumeration = res.keys(); enumeration.hasMoreElements(); write(s, res.get(s)))
        {
            s = (String)enumeration.nextElement();
            if(stringbuffer.length() > 0)
            {
                stringbuffer.append('\n');
            }
        }

    }

    protected void write(String s, String s1)
        throws IOException
    {
        write(s, s1, null);
    }

    protected void write(String s, String s1, String s2)
        throws IOException
    {
        if(!isLive || s == null || s.length() <= 0)
        {
            return;
        }
        if(!canWrite)
        {
            throw new IOException("write() bad timing");
        }
        if(s1 == null || s1.length() <= 0)
        {
            if(s2 != null && s2.length() > 0)
            {
                Out.write('<' + s + ' ' + s2 + "/>");
            } else
            {
                Out.write('<' + s + "/>");
            }
        } else
        {
            if(s2 != null && s2.length() > 0)
            {
                System.out.println('<' + s + ' ' + s2 + '>');
                Out.write('<' + s + ' ' + s2 + '>');
            } else
            {
                Out.write('<' + s + '>');
            }
            Out.write(s1);
            Out.write("</" + s + '>');
        }
        doWrite = true;
        updateTimer();
    }

    protected void read(ByteStream bytestream)
        throws IOException
    {
        while(isLive) 
        {
            int i = Io.r(In);
            if(i == 0)
            {
                break;
            }
            bytestream.write(i);
        }
    }

    protected void flush()
        throws IOException
    {
        if(Out != null && doWrite)
        {
            Out.write(0);
            Out.flush();
        }
        doWrite = false;
    }

    public boolean canRead(InputStream inputstream)
        throws IOException
    {
        return inputstream != null && inputstream.available() >= 1;
    }

    public Res getStatus()
    {
        return status;
    }

    public boolean isValidate()
    {
        return isLive;
    }

    public ByteStream getWriteBuffer()
        throws IOException
    {
        if(!canWrite)
        {
            throw new IOException("getWriteBuffer() bad timing");
        } else
        {
            return stm_buffer;
        }
    }

    public synchronized InetAddress getAddress()
    {
        return isLive && socket != null ? socket.getInetAddress() : null;
    }

    public void characters(char ac[], int i, int j)
        throws SAXException
    {
        if(strTag == null)
        {
            return;
        }
        try
        {
            mRead(strTag, new String(ac, i, j), res_att);
        }
        catch(IOException _ex)
        {
            mStop();
        }
    }

    public void endElement(String s, String s1, String s2)
        throws SAXException
    {
        strTag = null;
    }

    public void startElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        try
        {
            strTag = s2;
            if(s2 == null || s2.length() <= 0)
            {
                return;
            }
            if(s2.equals("ping"))
            {
                strTag = null;
                return;
            }
            if(s2.equals("talk"))
            {
                return;
            }
            setAtt(attributes);
            if(s2.equals("initialize"))
            {
                setStatus(attributes);
                mInit();
                return;
            }
            if(s2.equals("leave"))
            {
                mRead(s2, null, null);
                mStop();
                return;
            }
        }
        catch(Throwable _ex)
        {
            mStop();
        }
    }

    public void setStatus(Attributes attributes)
    {
        if(attributes == null)
        {
            return;
        }
        Res res = getStatus();
        int i = attributes.getLength();
        for(int j = 0; j < i; j++)
        {
            String s = attributes.getQName(j);
            String s1 = attributes.getValue(j);
            if(s != null && s.length() > 0 && s1 != null)
            {
                res.put(s, s1);
            }
        }

    }

    protected String hintToString(int i)
    {
        for(int j = 0; j < strHints.length; j++)
        {
            if(strHints[j] == i)
            {
                return strTags[j];
            }
        }

        return strTags[0];
    }

    protected int strToHint(String s)
    {
        if(s == null || s.length() <= 0 || s.equals("talk"))
        {
            return 0;
        }
        int i = strTags.length;
        for(int j = 1; j < i; j++)
        {
            if(strTags[j].equals(s))
            {
                return strHints[j];
            }
        }

        return 0;
    }
}
