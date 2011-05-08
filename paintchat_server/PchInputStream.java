package paintchat_server;

import java.io.*;
import paintchat.Res;
import syi.util.*;

public class PchInputStream
{

    private VectorBin lines;
    private Res res;
    private InputStream in;
    boolean isRead;
    private int iMax;

    public PchInputStream(InputStream inputstream, int i)
    {
        lines = new VectorBin();
        res = new Res();
        isRead = false;
        iMax = 0x989680;
        in = inputstream;
        iMax = Math.max(i, 0);
    }

    public VectorBin getLines()
    {
        getPch();
        return lines;
    }

    public Res getStatus()
    {
        getPch();
        return res;
    }

    private void getPch()
    {
        if(isRead)
        {
            return;
        }
        isRead = true;
        ByteStream bytestream = new ByteStream();
        byte abyte0[] = {
            13, 10, 13, 10
        };
        int j = 0;
        try
        {
            do
            {
                int i = Io.r(in);
                if(i < 0)
                {
                    throw new EOFException();
                }
                if(abyte0[j] == i)
                {
                    if(++j >= abyte0.length)
                    {
                        break;
                    }
                } else
                {
                    j = 0;
                }
                bytestream.write(i);
            } while(true);
            if(bytestream.size() > 0)
            {
                ByteInputStream byteinputstream = new ByteInputStream();
                byteinputstream.setByteStream(bytestream);
                res.load(byteinputstream);
            }
            do
            {
                int k = Io.readUShort(in);
                if(k < 0)
                {
                    break;
                }
                if(k >= 2)
                {
                    byte abyte1[] = new byte[k];
                    Io.rFull(in, abyte1, 0, k);
                    lines.add(abyte1);
                    for(; lines.size() > 0 && lines.getSizeBytes() > iMax; lines.remove(1)) { }
                }
            } while(true);
        }
        catch(EOFException _ex) { }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
        try
        {
            in.close();
            in = null;
        }
        catch(IOException _ex) { }
    }
}
