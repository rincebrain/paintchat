package paintchat_http;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import paintchat.debug.Debug;
import syi.util.*;

// Referenced classes of package paintchat_http:
//            HttpServer, HttpFiles

public class TalkerHttp
    implements Runnable
{

    private HttpServer server;
    private HttpFiles files;
    private Socket socket;
    private InputStream In;
    private OutputStream Out;
    private Date date;
    private boolean isLineOut;
    private char bC[];
    private byte bB[];
    private ByteStream bWork;
    private String strMethod;
    private String strRequest;
    private static final byte strHttp[] = "HTTP/1.0".getBytes();
    private long sizeRequest;
    private String strIfMod;
    private int sizeIfMod;
    private boolean isCash;
    private boolean isBody;
    private File fileRequest;
    private static final String STR_PRAGMA = "pragma";
    private static final String STR_LENGTH = "length=";
    private static final String STR_IF_MODIFIED_SINCE = "if-modified-since";
    private static final String STR_CONTENT_LENGTH = "content-length";
    private static final String STR_DEF_MIME = "application/octet-stream";
    private static final String strDef[] = {
        "GET", "HEAD", "/", "HTTP/1.0", "HTTP/1.1"
    };
    private static final String strDef2[] = {
        "pragma", "if-modified-since", "content-length"
    };
    private static final EOFException EOF = new EOFException();
    private static final int I_OK = 0;
    private static final int I_NOT_MODIFIED = 1;
    private static final int I_MOVED_PERMANENTLY = 2;
    private static final int I_NOT_FOUND = 3;
    private static final int I_SERVER_ERROR = 4;
    private static final int I_SERVICE_UNAVALIABLE = 5;
    private static final byte BYTE_RESPONCE[][] = {
        " 200 OK".getBytes(), " 304 Not Modified".getBytes(), " 301 Moved Permanently".getBytes(), " 404 Not Found".getBytes(), " 500 Internal Server Error".getBytes(), " 503 Service Unavailable".getBytes()
    };
    private static final byte BYTE_DEF_HEADER[] = "Server: PaintChatHTTP/3.1".getBytes();
    private static final byte BYTE_CRLF[] = {
        13, 10
    };
    private static final byte BYTE_LOCATION[] = "Location: ".getBytes();
    private static final byte BYTE_LAST_MODIFIED[] = "Last-Modified: ".getBytes();
    private static final byte BYTE_DATE[] = "Date: ".getBytes();
    private static final byte BYTE_CONTENT_LENGTH[] = "Content-Length: ".getBytes();
    private static final byte BYTE_CONTENT_TYPE[] = "Content-Type: ".getBytes();
    private static final byte BYTE_CONNECTION_CLOSE[] = "Connection: close".getBytes();

    public TalkerHttp(Socket socket1, HttpServer httpserver, HttpFiles httpfiles)
    {
        date = new Date();
        isLineOut = false;
        bC = new char[350];
        bB = new byte[350];
        bWork = new ByteStream();
        sizeRequest = 0L;
        strIfMod = null;
        sizeIfMod = -1;
        isCash = true;
        isBody = true;
        server = httpserver;
        socket = socket1;
        files = httpfiles;
        ThreadPool.poolStartThread(this, null);
    }

    private void close()
    {
        if(socket == null)
        {
            return;
        }
        try
        {
            Out.close();
            Out = null;
        }
        catch(IOException _ex) { }
        try
        {
            In.close();
            In = null;
        }
        catch(IOException _ex) { }
        try
        {
            socket.close();
            socket = null;
        }
        catch(IOException _ex) { }
    }

    private int getResponce()
    {
        try
        {
            boolean flag = sizeIfMod != -1 && (long)sizeIfMod != fileRequest.length() || strIfMod != null && HttpServer.fmt.parse(strIfMod).getTime() == fileRequest.lastModified();
            return !flag || !isCash ? 0 : 1;
        }
        catch(RuntimeException _ex) { }
        catch(ParseException _ex) { }
        return 0;
    }

    public byte[] getSince(long l)
    {
        date.setTime(l);
        return HttpServer.fmt.format(date).getBytes();
    }

    private String getString(char ac[], int i)
    {
        if(i == 0)
        {
            return null;
        }
        for(int l = 0; l < strDef.length; l++)
        {
            String s = strDef[l];
            int j = s.length();
            if(i == j)
            {
                int k;
                for(k = 0; k < j; k++)
                {
                    if(ac[k] != s.charAt(k))
                    {
                        break;
                    }
                }

                if(k == j)
                {
                    return s;
                }
            }
        }

        return new String(ac, 0, i);
    }

    private char r()
        throws IOException
    {
        int i = In.read();
        if(i == -1)
        {
            throw EOF;
        } else
        {
            return (char)i;
        }
    }

    private void readMethod()
        throws IOException
    {
        try
        {
            while(true) 
            {
                String s;
                if((s = readSpace()) != null)
                {
                    strMethod = s;
                    break;
                }
                if(isLineOut)
                {
                    throw EOF;
                }
            }
            do
            {
                String s1;
                if((s1 = readSpace()) != null)
                {
                    strRequest = s1;
                    break;
                }
                if(isLineOut)
                {
                    throw EOF;
                }
            } while(true);
            String s2;
            while((s2 = readSpace()) == null) 
            {
                if(isLineOut)
                {
                    throw EOF;
                }
            }
            if(!isLineOut)
            {
                for(int i = 0; i < 4096; i++)
                {
                    if(r() == '\n')
                    {
                        return;
                    }
                }

            }
        }
        catch(IOException ioexception)
        {
            close();
            throw ioexception;
        }
    }

    private boolean readOption()
        throws IOException
    {
        boolean flag = true;
        char ac[] = bC;
        int i = 0;
        int j = ac.length;
        for(int k = 0; k < j; k++)
        {
            char c = r();
            if(c == '\n')
            {
                flag = false;
                break;
            }
            if(Character.isWhitespace(c))
            {
                continue;
            }
            if(c == ':')
            {
                flag = false;
                break;
            }
            if(c == '+')
            {
                c = ' ';
            }
            if(c == '%')
            {
                c = (char)(Character.digit(r(), 16) << 4 | Character.digit(r(), 16));
            }
            ac[i++] = Character.toLowerCase(c);
        }

        if(flag)
        {
            int l;
            for(l = 0; l < 4096; l++)
            {
                if(r() == '\n')
                {
                    break;
                }
            }

            if(l >= 4096)
            {
                throw EOF;
            } else
            {
                return true;
            }
        }
        if(i == 0)
        {
            return false;
        }
        String s = getString(ac, i);
        i = 0;
        flag = true;
        for(int i1 = 0; i1 < j; i1++)
        {
            char c1 = r();
            if(c1 == '\n')
            {
                flag = false;
                break;
            }
            if((i != 0 || !Character.isWhitespace(c1)) && c1 != '\r')
            {
                if(c1 == '+')
                {
                    c1 = ' ';
                }
                if(c1 == '%')
                {
                    c1 = (char)(Character.digit(r(), 16) << 4 | Character.digit(r(), 16));
                }
                ac[i++] = c1;
            }
        }

        if(flag)
        {
            int j1;
            for(j1 = 0; j1 < 4096; j1++)
            {
                if(r() == '\n')
                {
                    break;
                }
            }

            if(j1 >= 4096)
            {
                throw EOF;
            }
        }
        switchOption(s, i > 0 ? new String(ac, 0, i) : null);
        return true;
    }

    private String readSpace()
        throws IOException
    {
        boolean flag = false;
        int i = -1;
        int j = 0;
        char c = '\u0400';
        bWork.reset();
        for(; j < c; j++)
        {
            i = In.read();
            if(i == 13)
            {
                continue;
            }
            if(i == -1 || Character.isWhitespace((char)i))
            {
                break;
            }
            switch(i)
            {
            case 37: // '%'
                int k = In.read();
                int l = In.read();
                if(k == -1 || l == -1)
                {
                    throw EOF;
                }
                bWork.write(Character.digit((char)k, 16) << 4 | Character.digit((char)l, 16));
                break;

            default:
                bWork.write(i);
                break;
            }
        }

        isLineOut = i == -1 || i == 10;
        if(!flag && i == -1 || j >= 1024)
        {
            throw EOF;
        } else
        {
            return new String(bWork.getBuffer(), 0, bWork.size(), "UTF8");
        }
    }

    private void rRun()
        throws Throwable
    {
        In = socket.getInputStream();
        Out = socket.getOutputStream();
        try
        {
            readMethod();
            while(readOption()) ;
            server.debug.log(socket.getLocalAddress().getHostName() + ' ' + strRequest);
            String s = files.uriToPath(strRequest);
            if(files.needMove(s))
            {
                sendMessage(2, files.addIndex(s));
            } else
            {
                fileRequest = files.getFile(s);
                sendMessage(getResponce(), s);
            }
        }
        catch(EOFException _ex) { }
        catch(FileNotFoundException _ex)
        {
            sendMessage(3, strRequest);
        }
        catch(Throwable throwable)
        {
            server.debug.log(throwable.getMessage());
        }
        close();
    }

    public void run()
    {
        try
        {
            rRun();
        }
        catch(Throwable _ex) { }
    }

    private void sendMessage(int i, String s)
        throws IOException
    {
        if(server.debug.bool_debug)
        {
            server.debug.log(s + ' ' + i);
        }
        Out.write(strHttp);
        Out.write(BYTE_RESPONCE[i]);
        Out.write(BYTE_CRLF);
        Out.write(BYTE_DEF_HEADER);
        Out.write(BYTE_CRLF);
        byte abyte0[] = getSince(System.currentTimeMillis());
        Out.write(BYTE_DATE);
        Out.write(abyte0);
        Out.write(BYTE_CRLF);
        switch(i)
        {
        case 2: // '\002'
            w(BYTE_LOCATION);
            w(s);
            w(BYTE_CONNECTION_CLOSE);
            w(BYTE_CRLF);
            w(BYTE_CRLF);
            break;

        case 1: // '\001'
            w(BYTE_CONNECTION_CLOSE);
            w(BYTE_CRLF);
            w(BYTE_CRLF);
            break;

        case 0: // '\0'
            byte abyte1[] = getSince(fileRequest.lastModified());
            w(BYTE_LAST_MODIFIED);
            w(abyte1);
            w(BYTE_CRLF);
            w(BYTE_CONTENT_TYPE);
            writeMime(s);
            w(BYTE_CRLF);
            w(BYTE_CONTENT_LENGTH);
            w(String.valueOf(fileRequest.length()));
            w(BYTE_CRLF);
            w(BYTE_CONNECTION_CLOSE);
            w(BYTE_CRLF);
            w(BYTE_CRLF);
            if(isBody)
            {
                wFile(fileRequest);
            }
            break;

        default:
            byte abyte2[] = strRequest.getBytes();
            w(BYTE_CONTENT_TYPE);
            w("text/html");
            w(BYTE_CRLF);
            w(BYTE_CONTENT_LENGTH);
            w(String.valueOf(files.getErrorMessageSize(i) + abyte2.length));
            w(BYTE_CRLF);
            w(BYTE_CONNECTION_CLOSE);
            w(BYTE_CRLF);
            w(BYTE_CRLF);
            if(isBody)
            {
                files.getErrorMessage(Out, i, abyte2, 0, abyte2.length);
            }
            break;
        }
        Out.flush();
    }

    private void switchOption(String s, String s1)
        throws RuntimeException
    {
        if(server.debug.bool_debug)
        {
            server.debug.log(s + ": " + s1);
        }
        if(s == null || s1 == null || s.length() <= 0 || s1.length() <= 0)
        {
            return;
        }
        if(s.equals("pragma"))
        {
            isCash = s1.indexOf("no-cache") < 0;
            return;
        }
        if(s.equals("content-length"))
        {
            try
            {
                int i = s1.length();
                int k;
                for(k = 0; k < i; k++)
                {
                    if(!Character.isDigit(s1.charAt(k)))
                    {
                        break;
                    }
                }

                if(i != k)
                {
                    s1 = s1.substring(0, k);
                }
                sizeRequest = Integer.parseInt(s1);
            }
            catch(NumberFormatException _ex)
            {
                sizeRequest = 0L;
            }
            return;
        }
        if(s.equals("if-modified-since"))
        {
            try
            {
                int j = s1.indexOf(';');
                strIfMod = j == -1 ? s1 : s1.substring(0, j);
                if(j >= 0)
                {
                    j = s1.indexOf("length=", j + 1);
                    if(j != -1)
                    {
                        j += "length=".length();
                        if(j < s1.length())
                        {
                            sizeIfMod = Integer.parseInt(s1.substring(j), 10);
                        }
                    }
                }
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
                strIfMod = null;
                sizeIfMod = -1;
            }
            return;
        } else
        {
            return;
        }
    }

    private void w(byte abyte0[])
        throws IOException
    {
        Out.write(abyte0);
    }

    private void w(String s)
        throws IOException
    {
        int i = s.length();
        s.getChars(0, s.length(), bC, 0);
        for(int j = 0; j < i; j++)
        {
            bB[j] = (byte)bC[j];
        }

        Out.write(bB, 0, i);
    }

    private void wFile(File file)
        throws IOException
    {
        if(file.length() <= 0L)
        {
            return;
        }
        FileInputStream fileinputstream = new FileInputStream(file);
        int i;
        while((i = fileinputstream.read(bB)) != -1) 
        {
            Out.write(bB, 0, i);
        }
        fileinputstream.close();
    }

    private void wln(byte abyte0[])
        throws IOException
    {
        w(abyte0);
        w(BYTE_CRLF);
    }

    private void writeMime(String s)
        throws IOException
    {
        int i = s.lastIndexOf('.');
        String s1;
        if(i < 0)
        {
            s1 = "application/octet-stream";
        } else
        {
            s1 = HttpServer.Mime.getString(s.substring(i + 1), "application/octet-stream");
        }
        w(s1);
    }

}
