package paintchat_server;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import paintchat.Res;
import syi.util.*;

public abstract class PaintChatTalker
    implements Runnable
{

    private boolean isLive;
    protected boolean canWrite;
    private boolean doWrite;
    protected int iSendInterval;
    private OutputStream Out;
    private InputStream In;
    private Socket socket;
    private ByteStream stm_buffer;
    private Res status;
    long lTime;
    private boolean isConnect;

    public PaintChatTalker()
    {
        isLive = true;
        canWrite = true;
        doWrite = false;
        iSendInterval = 2000;
        stm_buffer = new ByteStream();
        status = null;
        isConnect = false;
    }

    private void mInitInside()
        throws IOException
    {
        if(In == null)
        {
            In = socket.getInputStream();
        }
        if(Out == null)
        {
            Out = socket.getOutputStream();
        }
        updateTimer();
        if(isConnect)
        {
            w(98);
            write(getStatus());
            flush();
            ByteStream bytestream = getWriteBuffer();
            read(bytestream);
            if(bytestream.size() > 0)
            {
                ByteInputStream byteinputstream = new ByteInputStream();
                byteinputstream.setByteStream(bytestream);
                getStatus().load(byteinputstream);
            }
        } else
        {
            write(getStatus());
            flush();
        }
        mInit();
    }

    public void updateTimer()
    {
        lTime = System.currentTimeMillis();
    }

    public synchronized void mStart(Socket socket1, InputStream inputstream, OutputStream outputstream, Res res)
    {
        socket = socket1;
        In = inputstream;
        Out = outputstream;
        status = res != null ? res : new Res();
        socket1.getInetAddress().getHostName();
        ThreadPool.poolStartThread(this, 'l');
    }

    public synchronized void mConnect(Socket socket1, Res res)
        throws IOException
    {
        isConnect = true;
        mStart(socket1, null, null, res);
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

    private void mDestroyInside()
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

    protected abstract void mInit()
        throws IOException;

    protected abstract void mDestroy();

    protected abstract void mRead(ByteStream bytestream)
        throws IOException;

    protected abstract void mIdle(long l)
        throws IOException;

    protected abstract void mWrite()
        throws IOException;

    public void run()
    {
        long l = 0L;
        long l2 = 0L;
        try
        {
            mInitInside();
            canWrite = false;
            while(isLive) 
            {
                if(canRead(In))
                {
                    stm_buffer.reset();
                    read(stm_buffer);
                    if(stm_buffer.size() > 0)
                    {
                        mRead(stm_buffer);
                    }
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
                            write(stm_buffer);
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

    protected void read(ByteStream bytestream)
        throws IOException
    {
        int i = Io.readUShort(In);
        if(i <= 0)
        {
            return;
        } else
        {
            bytestream.write(In, i);
            updateTimer();
            return;
        }
    }

    protected void write(ByteStream bytestream)
        throws IOException
    {
        write(bytestream.getBuffer(), bytestream.size());
    }

    protected void write(Res res)
        throws IOException
    {
        if(res == null)
        {
            write(null, 0);
            return;
        }
        StringBuffer stringbuffer = new StringBuffer();
        String s;
        for(Enumeration enumeration = res.keys(); enumeration.hasMoreElements(); stringbuffer.append(res.get(s)))
        {
            s = (String)enumeration.nextElement();
            if(stringbuffer.length() > 0)
            {
                stringbuffer.append('\n');
            }
            stringbuffer.append(s);
            stringbuffer.append('=');
        }

        byte abyte0[] = stringbuffer.toString().getBytes("UTF8");
        write(abyte0, abyte0.length);
    }

    protected void write(byte abyte0[], int i)
        throws IOException
    {
        if(!isLive)
        {
            return;
        }
        if(!canWrite)
        {
            throw new IOException("write() bad timing");
        }
        Io.wShort(Out, i);
        if(i > 0)
        {
            Out.write(abyte0, 0, i);
        }
        doWrite = true;
        updateTimer();
    }

    protected void w(int i)
        throws IOException
    {
        if(!isLive)
        {
            return;
        } else
        {
            Out.write(i);
            doWrite = true;
            updateTimer();
            return;
        }
    }

    protected void flush()
        throws IOException
    {
        if(Out != null && doWrite)
        {
            Out.flush();
        }
        doWrite = false;
    }

    public boolean canRead(InputStream inputstream)
        throws IOException
    {
        return inputstream != null && inputstream.available() >= 2;
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
            stm_buffer.reset();
            return stm_buffer;
        }
    }

    public synchronized InetAddress getAddress()
    {
        return isLive && socket != null ? socket.getInetAddress() : null;
    }
}
