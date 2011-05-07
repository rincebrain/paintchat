package paintchat_http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import paintchat.Config;
import paintchat.Res;
import syi.util.Io;
import syi.util.PProperties;

public class HttpFiles
{
  private String STR_SLASH = "//";
  private String STR_DOT = "..";
  private File dirWWW;
  private File dirTmp;
  private Config config;
  private Res res;
  static final FileNotFoundException NOT_FOUND = new FileNotFoundException();
  private byte[] bErrorUpper = null;
  private byte[] bErrorBottom = null;
  private byte[][] res_bytes = new byte[6];
  private static final int I_OK = 0;
  private static final int I_NOT_MODIFIED = 1;
  private static final int I_MOVED_PERMANENTLY = 2;
  private static final int I_NOT_FOUND = 3;
  private static final int I_SERVER_ERROR = 4;
  private static final int I_SERVICE_UNAVALIABLE = 5;

  public HttpFiles(Config paramConfig, Res paramRes)
  {
    this.config = paramConfig;
    this.res = paramRes;
    this.dirWWW = new File(paramConfig.getString("Http_Dir", "www"));
    this.dirTmp = new File("cnf/template/");
  }

  public String addIndex(String paramString)
  {
    int i = paramString.length();
    if ((i == 0) || (paramString.charAt(i - 1) != '/'))
      return paramString + "/index.html";
    return paramString + "index.html";
  }

  public void getErrorMessage(OutputStream paramOutputStream, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
    throws IOException
  {
    setup();
    paramOutputStream.write(this.bErrorUpper);
    paramOutputStream.write(paramArrayOfByte, paramInt2, paramInt3);
    byte[] arrayOfByte = this.res_bytes[paramInt1];
    if (arrayOfByte != null)
      paramOutputStream.write(arrayOfByte);
    paramOutputStream.write(this.bErrorBottom);
  }

  public int getErrorMessageSize(int paramInt)
    throws IOException
  {
    setup();
    int i = 0;
    byte[] arrayOfByte = this.res_bytes[paramInt];
    if (arrayOfByte != null)
      i += arrayOfByte.length;
    return this.bErrorUpper.length + i + this.bErrorBottom.length;
  }

  public File getFile(String paramString)
    throws FileNotFoundException
  {
    File localFile = new File(this.dirWWW, paramString);
    if (!localFile.isFile())
    {
      localFile = new File(this.dirTmp, paramString);
      if (!localFile.isFile())
        localFile = new File(this.dirTmp, Io.getFileName(paramString));
    }
    if (!localFile.isFile())
      throw NOT_FOUND;
    return localFile;
  }

  private int indexOf(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte1.length;
    int j = 0;
    for (int k = 0; k < i; k++)
      if (paramArrayOfByte1[k] == paramArrayOfByte2[j])
      {
        if (j >= paramArrayOfByte2.length - 1)
          return k - j;
        j++;
      }
      else
      {
        j = 0;
      }
    return -1;
  }

  public boolean needMove(String paramString)
  {
    int i = Math.max(paramString.lastIndexOf('/'), 0);
    return paramString.lastIndexOf('.') <= i;
  }

  private String replaceText(String paramString1, String paramString2, String paramString3)
  {
    if (paramString1.indexOf(paramString2) < 0)
      return paramString1;
    try
    {
      int i = 0;
      int j;
      while ((j = paramString1.indexOf(paramString2, i)) < 0)
      {
        paramString1 = paramString1.substring(0, i) + paramString3 + paramString1.substring(i + paramString2.length());
        i += j;
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      System.out.println("replace" + localRuntimeException);
    }
    return paramString1;
  }

  private void setup()
  {
    if (this.bErrorUpper != null)
      return;
    synchronized (this)
    {
      if (this.bErrorUpper != null)
        return;
      try
      {
        this.res_bytes[3] = this.res.get("not_found").getBytes();
        this.res_bytes[4] = this.res.get("server_error").getBytes();
        File localFile = new File(this.dirTmp, "err.html");
        byte[] arrayOfByte = new byte[(int)localFile.length()];
        FileInputStream localFileInputStream = new FileInputStream(localFile);
        Io.rFull(localFileInputStream, arrayOfByte, 0, arrayOfByte.length);
        localFileInputStream.close();
        int i = Math.max(indexOf(arrayOfByte, "<!--ERRORMESSAGE-->".getBytes()), 0);
        this.bErrorUpper = new byte[i];
        System.arraycopy(arrayOfByte, 0, this.bErrorUpper, 0, i);
        String str = "<!--/ERRORMESSAGE-->";
        i = Math.max(0, indexOf(arrayOfByte, str.getBytes())) + str.length();
        this.bErrorBottom = new byte[arrayOfByte.length - i];
        System.arraycopy(arrayOfByte, i, this.bErrorBottom, 0, arrayOfByte.length - i);
      }
      catch (Throwable localThrowable)
      {
        this.bErrorUpper = new byte[0];
        this.bErrorBottom = new byte[0];
      }
    }
  }

  public String uriToPath(String paramString)
  {
    paramString = paramString.replace('\\', '/');
    paramString = paramString.replace('\000', '_');
    paramString = paramString.replace('\n', '_');
    paramString = paramString.replace('\r', '_');
    StringBuffer localStringBuffer = null;
    if ((paramString.indexOf(this.STR_SLASH) >= 0) || (paramString.indexOf(this.STR_DOT) >= 0))
    {
      localStringBuffer = new StringBuffer();
      int i = paramString.length();
      int j = 0;
      int k = 0;
      int m = 1;
      localStringBuffer.append('.');
      while (j < i)
      {
        char c = paramString.charAt(j++);
        if (((c == '/') && (k != 0)) || ((c == '.') && (m != 0)))
          continue;
        k = c == '/' ? 1 : 0;
        m = c == '.' ? 1 : 0;
        localStringBuffer.append(c);
      }
      paramString = localStringBuffer.toString();
    }
    if (paramString.length() == 0)
      return "./index.html";
    if (paramString.charAt(0) == '/')
    {
      if (localStringBuffer == null)
      {
        localStringBuffer = new StringBuffer(paramString.length() + 1);
        localStringBuffer.append('.');
        localStringBuffer.append(paramString);
      }
      else
      {
        localStringBuffer = localStringBuffer.insert(0, '.');
      }
      paramString = localStringBuffer.toString();
    }
    if (paramString.charAt(paramString.length() - 1) == '/')
      return addIndex(paramString);
    return paramString;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_http.HttpFiles
 * JD-Core Version:    0.6.0
 */