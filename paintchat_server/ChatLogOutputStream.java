package paintchat_server;

import java.io.*;
import paintchat.MgText;
import paintchat.debug.DebugListener;
import syi.util.Io;
import syi.util.Vector2;

public class ChatLogOutputStream extends Writer
{

    private boolean isValidate;
    BufferedWriter outLog;
    File fDir;
    DebugListener debug;
    long timeStart;

    public ChatLogOutputStream(File file, DebugListener debuglistener, boolean flag)
    {
        isValidate = true;
        outLog = null;
        timeStart = 0L;
        fDir = file;
        debug = debuglistener;
        isValidate = flag;
    }

    public synchronized void write(char c)
        throws IOException
    {
        try
        {
            if(!isValidate)
            {
                return;
            }
            initFile();
            if(outLog != null)
            {
                outLog.write(c);
            }
        }
        catch(IOException ioexception)
        {
            debug.log(ioexception);
        }
    }

    public synchronized void write(char ac[], int i, int j)
    {
        try
        {
            if(!isValidate)
            {
                return;
            }
            initFile();
            if(outLog != null)
            {
                outLog.write(ac, i, j);
                outLog.newLine();
            }
        }
        catch(IOException ioexception)
        {
            debug.log(ioexception);
        }
    }

    public void write(String s)
    {
        write(s, 0, s.length());
    }

    public synchronized void write(String s, int i, int j)
    {
        try
        {
            if(!isValidate)
            {
                return;
            }
            initFile();
            if(outLog != null)
            {
                outLog.write(s, i, j);
                outLog.newLine();
            }
        }
        catch(IOException ioexception)
        {
            debug.log(ioexception);
        }
    }

    public synchronized void write(MgText mgtext)
    {
        try
        {
            if(!isValidate)
            {
                return;
            }
            initFile();
            if(outLog == null)
            {
                return;
            }
            if(mgtext.bName != null)
            {
                outLog.write(mgtext.getUserName());
                outLog.write(62);
            }
            switch(mgtext.head)
            {
            case 0: // '\0'
            case 6: // '\006'
            case 8: // '\b'
                outLog.write(mgtext.toString());
                outLog.newLine();
                break;
            }
        }
        catch(IOException _ex) { }
    }

    private void initFile()
    {
        try
        {
            if(!isValidate)
            {
                return;
            }
            long l = System.currentTimeMillis();
            if(outLog == null || timeStart != 0L && l - timeStart >= 0x5265c00L)
            {
                timeStart = l;
                boolean _tmp = outLog == null;
                if(!fDir.isDirectory())
                {
                    fDir.mkdirs();
                }
                if(outLog != null)
                {
                    try
                    {
                        outLog.flush();
                        outLog.close();
                    }
                    catch(IOException ioexception1)
                    {
                        debug.log(ioexception1);
                    }
                    outLog = null;
                }
                File file = getLogFile();
                outLog = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF8"), 1024);
            }
        }
        catch(IOException ioexception)
        {
            debug.log(ioexception);
        }
    }

    public synchronized void close()
    {
        try
        {
            if(outLog != null)
            {
                outLog.flush();
                outLog.close();
                outLog = null;
                File file = getLogFile();
                if(file.isFile() && file.length() <= 0L)
                {
                    file.delete();
                }
            }
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    private File getLogFile()
        throws IOException
    {
        return new File(Io.getDateString("text_log", "txt", fDir.getCanonicalPath()));
    }

    public File getTmpFile()
    {
        return new File(fDir, "text_cash.tmp");
    }

    public void loadLog(Vector2 vector2, int i, boolean flag)
    {
        File file = getTmpFile();
        if(!file.isFile() || file.length() <= 0L)
        {
            return;
        }
        BufferedReader bufferedreader = null;
        try
        {
            bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
            String s;
            while((s = bufferedreader.readLine()) != null) 
            {
                vector2.add(new MgText(0, (byte)6, s));
                if(vector2.size() >= i)
                {
                    vector2.remove(5);
                }
            }
        }
        catch(IOException _ex) { }
        try
        {
            if(bufferedreader != null)
            {
                bufferedreader.close();
            }
        }
        catch(IOException _ex) { }
        if(flag)
        {
            file.delete();
        }
    }

    public void flush()
        throws IOException
    {
        if(outLog != null)
        {
            outLog.flush();
        }
    }
}
