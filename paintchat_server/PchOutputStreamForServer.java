package paintchat_server;

import java.io.*;
import java.util.zip.Deflater;
import paintchat.Config;
import syi.util.*;

// Referenced classes of package paintchat_server:
//            PchInputStream, PchOutputStream

public class PchOutputStreamForServer extends OutputStream
{

    private Config config;
    private PchOutputStream out;
    private long lTimer;
    private boolean isLogEnable;
    private boolean isLogLoad;
    private int iCashMax;
    private int iLogMax;
    private Deflater deflater;
    private byte bDeflate[];
    private VectorBin vAnime;

    public PchOutputStreamForServer(Config config1)
    {
        out = null;
        lTimer = 0L;
        isLogEnable = false;
        isLogLoad = false;
        bDeflate = new byte[0x11170];
        config = config1;
        mInit();
    }

    private void mInit()
    {
        iCashMax = config.getInt("Server_Cash_Line_Size", 0x7a120);
        iLogMax = 0x5f5e100;
        isLogEnable = config.getBool("Server_Log_Line", false);
        isLogLoad = config.getBool("Server_Load_Line", false);
        boolean flag = config.getBool("Server_Cash_Line", true);
        if((isLogLoad || isLogEnable || flag) && deflater == null)
        {
            deflater = new Deflater(9, false);
        }
        if(isLogLoad)
        {
            File file = getTmpFile();
            if(file.isFile() && file.length() > 0L)
            {
                try
                {
                    PchInputStream pchinputstream = new PchInputStream(new BufferedInputStream(new FileInputStream(file)), iCashMax);
                    vAnime = pchinputstream.getLines();
                }
                catch(IOException ioexception)
                {
                    ioexception.printStackTrace();
                }
            }
        }
        if(vAnime == null)
        {
            vAnime = new VectorBin();
        }
    }

    public void mFinalize()
    {
        close();
        deflater.end();
    }

    public void write(int i)
    {
        write(new byte[] {
            (byte)i
        });
    }

    public void write(byte abyte0[])
    {
        write(abyte0, 0, abyte0.length);
    }

    public void write(byte abyte0[], int i, int j)
    {
        try
        {
            if(checkTimeout())
            {
                newLog();
            }
            setupStream();
            deflater.reset();
            deflater.setInput(abyte0, i, j);
            deflater.finish();
            int k;
            for(k = 0; !deflater.finished(); k += deflater.deflate(bDeflate, k, j - k)) { }
            byte abyte1[] = new byte[k];
            System.arraycopy(bDeflate, 0, abyte1, 0, k);
            vAnime.add(abyte1);
            if(vAnime.getSizeBytes() >= iCashMax)
            {
                vAnime.remove(1);
            }
            if(out != null)
            {
                out.write(bDeflate, 0, k);
                if(out.size() >= iLogMax)
                {
                    close();
                    setupStream();
                }
            }
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
            close();
            isLogEnable = false;
        }
    }

    public void flush()
    {
        try
        {
            if(out != null)
            {
                out.flush();
            }
        }
        catch(IOException _ex)
        {
            close();
        }
    }

    public void close()
    {
        try
        {
            if(out != null)
            {
                out.flush();
                out.close();
            }
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        out = null;
        if(isLogEnable)
        {
            try
            {
                File file = getTmpFile();
                if(file.isFile() && file.length() > 0L)
                {
                    File file1 = new File(Io.getDateString("pch", "spch", Io.getDirectory(file.getCanonicalPath())));
                    Io.copyFile(file, file1);
                }
            }
            catch(IOException ioexception1)
            {
                ioexception1.printStackTrace();
            }
        }
    }

    public boolean checkTimeout()
    {
        return lTimer != 0L && System.currentTimeMillis() - lTimer >= 0x5265c00L;
    }

    public void setupStream()
        throws IOException
    {
        if(out != null || !isLogEnable && !isLogLoad)
        {
            return;
        }
        File file = getTmpFile();
        boolean flag = file.isFile() && file.length() > 0L;
        out = new PchOutputStream(new BufferedOutputStream(new FileOutputStream(file, flag)), flag);
        if(!flag)
        {
            out.writeHeader("Client_Image_Width", config.getString("Client_Image_Width"));
            out.writeHeader("Client_Image_Height", config.getString("Client_Image_Height"));
            out.writeHeader("version", "2");
        }
        lTimer = System.currentTimeMillis();
    }

    public void getLog(VectorBin vectorbin)
    {
        vAnime.copy(vectorbin);
    }

    public void newLog()
    {
        vAnime.removeAll();
        flush();
        close();
        File file = getTmpFile();
        if(file.isFile())
        {
            file.delete();
        }
    }

    private File getTmpFile()
    {
        File file = new File(config.getString("Server_Log_Line_Dir", "./save_server/"));
        if(!file.isDirectory())
        {
            file.mkdirs();
        }
        return new File(file, "line_cash.tmp");
    }
}
