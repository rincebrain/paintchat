package paintchat_http;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import paintchat.debug.Debug;
import syi.util.ByteStream;
import syi.util.PProperties;
import syi.util.ThreadPool;

public class TalkerHttp
  implements Runnable
{
  private HttpServer server;
  private HttpFiles files;
  private Socket socket;
  private InputStream In;
  private OutputStream Out;
  private Date date = new Date();
  private boolean isLineOut = false;
  private char[] bC = new char[350];
  private byte[] bB = new byte[350];
  private ByteStream bWork = new ByteStream();
  private String strMethod;
  private String strRequest;
  private static final byte[] strHttp = "HTTP/1.0".getBytes();
  private long sizeRequest = 0L;
  private String strIfMod = null;
  private int sizeIfMod = -1;
  private boolean isCash = true;
  private boolean isBody = true;
  private File fileRequest;
  private static final String STR_PRAGMA = "pragma";
  private static final String STR_LENGTH = "length=";
  private static final String STR_IF_MODIFIED_SINCE = "if-modified-since";
  private static final String STR_CONTENT_LENGTH = "content-length";
  private static final String STR_DEF_MIME = "application/octet-stream";
  private static final String[] strDef = { "GET", "HEAD", "/", "HTTP/1.0", "HTTP/1.1" };
  private static final String[] strDef2 = { "pragma", "if-modified-since", "content-length" };
  private static final EOFException EOF = new EOFException();
  private static final int I_OK = 0;
  private static final int I_NOT_MODIFIED = 1;
  private static final int I_MOVED_PERMANENTLY = 2;
  private static final int I_NOT_FOUND = 3;
  private static final int I_SERVER_ERROR = 4;
  private static final int I_SERVICE_UNAVALIABLE = 5;
  private static final byte[][] BYTE_RESPONCE = { " 200 OK".getBytes(), " 304 Not Modified".getBytes(), " 301 Moved Permanently".getBytes(), " 404 Not Found".getBytes(), " 500 Internal Server Error".getBytes(), " 503 Service Unavailable".getBytes() };
  private static final byte[] BYTE_DEF_HEADER = "Server: PaintChatHTTP/3.1".getBytes();
  private static final byte[] BYTE_CRLF = { 13, 10 };
  private static final byte[] BYTE_LOCATION = "Location: ".getBytes();
  private static final byte[] BYTE_LAST_MODIFIED = "Last-Modified: ".getBytes();
  private static final byte[] BYTE_DATE = "Date: ".getBytes();
  private static final byte[] BYTE_CONTENT_LENGTH = "Content-Length: ".getBytes();
  private static final byte[] BYTE_CONTENT_TYPE = "Content-Type: ".getBytes();
  private static final byte[] BYTE_CONNECTION_CLOSE = "Connection: close".getBytes();

  public TalkerHttp(Socket paramSocket, HttpServer paramHttpServer, HttpFiles paramHttpFiles)
  {
    this.server = paramHttpServer;
    this.socket = paramSocket;
    this.files = paramHttpFiles;
    ThreadPool.poolStartThread(this, null);
  }

  private void close()
  {
    if (this.socket == null)
      return;
    try
    {
      this.Out.close();
      this.Out = null;
    }
    catch (IOException localIOException1)
    {
    }
    try
    {
      this.In.close();
      this.In = null;
    }
    catch (IOException localIOException2)
    {
    }
    try
    {
      this.socket.close();
      this.socket = null;
    }
    catch (IOException localIOException3)
    {
    }
  }

  private int getResponce()
  {
    try
    {
      int i = ((this.sizeIfMod == -1) || (this.sizeIfMod == this.fileRequest.length())) && ((this.strIfMod == null) || (HttpServer.fmt.parse(this.strIfMod).getTime() != this.fileRequest.lastModified())) ? 0 : 1;
      return (i != 0) && (this.isCash) ? 1 : 0;
    }
    catch (RuntimeException localRuntimeException)
    {
    }
    catch (ParseException localParseException)
    {
    }
    return 0;
  }

  public byte[] getSince(long paramLong)
  {
    this.date.setTime(paramLong);
    return HttpServer.fmt.format(this.date).getBytes();
  }

  private String getString(char[] paramArrayOfChar, int paramInt)
  {
    if (paramInt == 0)
      return null;
    for (int k = 0; k < strDef.length; k++)
    {
      String str = strDef[k];
      int i = str.length();
      if (paramInt != i)
        continue;
      for (int j = 0; j < i; j++)
        if (paramArrayOfChar[j] != str.charAt(j))
          break;
      if (j == i)
        return str;
    }
    return new String(paramArrayOfChar, 0, paramInt);
  }

  private char r()
    throws IOException
  {
    int i = this.In.read();
    if (i == -1)
      throw EOF;
    return (char)i;
  }

  private void readMethod()
    throws IOException
  {
    try
    {
      String str;
      while (true)
      {
        if ((str = readSpace()) != null)
        {
          this.strMethod = str;
          break;
        }
        if (this.isLineOut)
          throw EOF;
      }
      while (true)
      {
        if ((str = readSpace()) != null)
        {
          this.strRequest = str;
          break;
        }
        if (this.isLineOut)
          throw EOF;
      }
      while ((str = readSpace()) == null)
        if (this.isLineOut)
          throw EOF;
      if (!this.isLineOut)
        for (int i = 0; i < 4096; i++)
          if (r() == '\n')
            return;
    }
    catch (IOException localIOException)
    {
      close();
      throw localIOException;
    }
  }

  private boolean readOption()
    throws IOException
  {
    int i = 1;
    char[] arrayOfChar = this.bC;
    int j = 0;
    int k = arrayOfChar.length;
    char c;
    for (int m = 0; m < k; m++)
    {
      c = r();
      if (c == '\n')
      {
        i = 0;
        break;
      }
      if (Character.isWhitespace(c))
        continue;
      if (c == ':')
      {
        i = 0;
        break;
      }
      if (c == '+')
        c = ' ';
      if (c == '%')
        c = (char)(Character.digit(r(), 16) << 4 | Character.digit(r(), 16));
      arrayOfChar[(j++)] = Character.toLowerCase(c);
    }
    if (i != 0)
    {
      for (m = 0; m < 4096; m++)
        if (r() == '\n')
          break;
      if (m >= 4096)
        throw EOF;
      return true;
    }
    if (j == 0)
      return false;
    String str = getString(arrayOfChar, j);
    j = 0;
    i = 1;
    for (int n = 0; n < k; n++)
    {
      c = r();
      if (c == '\n')
      {
        i = 0;
        break;
      }
      if (((j == 0) && (Character.isWhitespace(c))) || (c == '\r'))
        continue;
      if (c == '+')
        c = ' ';
      if (c == '%')
        c = (char)(Character.digit(r(), 16) << 4 | Character.digit(r(), 16));
      arrayOfChar[(j++)] = c;
    }
    if (i != 0)
    {
      for (n = 0; n < 4096; n++)
        if (r() == '\n')
          break;
      if (n >= 4096)
        throw EOF;
    }
    switchOption(str, j <= 0 ? null : new String(arrayOfChar, 0, j));
    return true;
  }

  private String readSpace()
    throws IOException
  {
    int i = 0;
    int j = -1;
    int k = 0;
    int m = 1024;
    this.bWork.reset();
    while (k < m)
    {
      j = this.In.read();
      if (j != 13)
      {
        if ((j == -1) || (Character.isWhitespace((char)j)))
          break;
        switch (j)
        {
        case 37:
          int n = this.In.read();
          int i1 = this.In.read();
          if ((n == -1) || (i1 == -1))
            throw EOF;
          this.bWork.write(Character.digit((char)n, 16) << 4 | Character.digit((char)i1, 16));
          break;
        default:
          this.bWork.write(j);
        }
      }
      k++;
    }
    this.isLineOut = ((j == -1) || (j == 10));
    if (((i == 0) && (j == -1)) || (k >= 1024))
      throw EOF;
    return new String(this.bWork.getBuffer(), 0, this.bWork.size(), "UTF8");
  }

  private void rRun()
    throws Throwable
  {
    this.In = this.socket.getInputStream();
    this.Out = this.socket.getOutputStream();
    try
    {
      readMethod();
      while (readOption());
      this.server.debug.log(this.socket.getLocalAddress().getHostName() + ' ' + this.strRequest);
      String str = this.files.uriToPath(this.strRequest);
      if (this.files.needMove(str))
      {
        sendMessage(2, this.files.addIndex(str));
      }
      else
      {
        this.fileRequest = this.files.getFile(str);
        sendMessage(getResponce(), str);
      }
    }
    catch (EOFException localEOFException)
    {
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      sendMessage(3, this.strRequest);
    }
    catch (Throwable localThrowable)
    {
      this.server.debug.log(localThrowable.getMessage());
    }
    close();
  }

  public void run()
  {
    try
    {
      rRun();
    }
    catch (Throwable localThrowable)
    {
    }
  }

  private void sendMessage(int paramInt, String paramString)
    throws IOException
  {
    if (this.server.debug.bool_debug)
      this.server.debug.log(paramString + ' ' + paramInt);
    this.Out.write(strHttp);
    this.Out.write(BYTE_RESPONCE[paramInt]);
    this.Out.write(BYTE_CRLF);
    this.Out.write(BYTE_DEF_HEADER);
    this.Out.write(BYTE_CRLF);
    byte[] arrayOfByte1 = getSince(System.currentTimeMillis());
    this.Out.write(BYTE_DATE);
    this.Out.write(arrayOfByte1);
    this.Out.write(BYTE_CRLF);
    switch (paramInt)
    {
    case 2:
      w(BYTE_LOCATION);
      w(paramString);
      w(BYTE_CONNECTION_CLOSE);
      w(BYTE_CRLF);
      w(BYTE_CRLF);
      break;
    case 1:
      w(BYTE_CONNECTION_CLOSE);
      w(BYTE_CRLF);
      w(BYTE_CRLF);
      break;
    case 0:
      arrayOfByte1 = getSince(this.fileRequest.lastModified());
      w(BYTE_LAST_MODIFIED);
      w(arrayOfByte1);
      w(BYTE_CRLF);
      w(BYTE_CONTENT_TYPE);
      writeMime(paramString);
      w(BYTE_CRLF);
      w(BYTE_CONTENT_LENGTH);
      w(String.valueOf(this.fileRequest.length()));
      w(BYTE_CRLF);
      w(BYTE_CONNECTION_CLOSE);
      w(BYTE_CRLF);
      w(BYTE_CRLF);
      if (!this.isBody)
        break;
      wFile(this.fileRequest);
      break;
    default:
      byte[] arrayOfByte2 = this.strRequest.getBytes();
      w(BYTE_CONTENT_TYPE);
      w("text/html");
      w(BYTE_CRLF);
      w(BYTE_CONTENT_LENGTH);
      w(String.valueOf(this.files.getErrorMessageSize(paramInt) + arrayOfByte2.length));
      w(BYTE_CRLF);
      w(BYTE_CONNECTION_CLOSE);
      w(BYTE_CRLF);
      w(BYTE_CRLF);
      if (!this.isBody)
        break;
      this.files.getErrorMessage(this.Out, paramInt, arrayOfByte2, 0, arrayOfByte2.length);
    }
    this.Out.flush();
  }

  private void switchOption(String paramString1, String paramString2)
    throws RuntimeException
  {
    if (this.server.debug.bool_debug)
      this.server.debug.log(paramString1 + ": " + paramString2);
    if ((paramString1 == null) || (paramString2 == null) || (paramString1.length() <= 0) || (paramString2.length() <= 0))
      return;
    if (paramString1.equals("pragma"))
    {
      this.isCash = (paramString2.indexOf("no-cache") < 0);
      return;
    }
    int i;
    if (paramString1.equals("content-length"))
    {
      try
      {
        i = paramString2.length();
        for (int j = 0; j < i; j++)
          if (!Character.isDigit(paramString2.charAt(j)))
            break;
        if (i != j)
          paramString2 = paramString2.substring(0, j);
        this.sizeRequest = Integer.parseInt(paramString2);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        this.sizeRequest = 0L;
      }
      return;
    }
    if (paramString1.equals("if-modified-since"))
    {
      try
      {
        i = paramString2.indexOf(';');
        this.strIfMod = (i != -1 ? paramString2.substring(0, i) : paramString2);
        if (i >= 0)
        {
          i = paramString2.indexOf("length=", i + 1);
          if (i != -1)
          {
            i += "length=".length();
            if (i < paramString2.length())
              this.sizeIfMod = Integer.parseInt(paramString2.substring(i), 10);
          }
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        this.strIfMod = null;
        this.sizeIfMod = -1;
      }
      return;
    }
  }

  private void w(byte[] paramArrayOfByte)
    throws IOException
  {
    this.Out.write(paramArrayOfByte);
  }

  private void w(String paramString)
    throws IOException
  {
    int i = paramString.length();
    paramString.getChars(0, paramString.length(), this.bC, 0);
    for (int j = 0; j < i; j++)
      this.bB[j] = (byte)this.bC[j];
    this.Out.write(this.bB, 0, i);
  }

  private void wFile(File paramFile)
    throws IOException
  {
    if (paramFile.length() <= 0L)
      return;
    FileInputStream localFileInputStream = new FileInputStream(paramFile);
    int i;
    while ((i = localFileInputStream.read(this.bB)) != -1)
      this.Out.write(this.bB, 0, i);
    localFileInputStream.close();
  }

  private void wln(byte[] paramArrayOfByte)
    throws IOException
  {
    w(paramArrayOfByte);
    w(BYTE_CRLF);
  }

  private void writeMime(String paramString)
    throws IOException
  {
    int i = paramString.lastIndexOf('.');
    String str;
    if (i < 0)
      str = "application/octet-stream";
    else
      str = HttpServer.Mime.getString(paramString.substring(i + 1), "application/octet-stream");
    w(str);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_http.TalkerHttp
 * JD-Core Version:    0.6.0
 */