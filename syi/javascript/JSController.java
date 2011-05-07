package syi.javascript;

import [Ljava.lang.Object;;
import java.applet.Applet;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

public class JSController
{
  private boolean isCash = false;
  private Class cJava = null;
  private Object oJava = null;
  private Method mGet;
  private Method mCall;
  private Method mGetMember;
  private Method mSetMember;
  private Method mSetSlot;
  private Method mGetSlot;
  private Method mEval;
  private Applet applet;
  private Hashtable tables = null;
  private SimpleDateFormat dateFormat = null;
  private static final String STR_NO_SUPPORT = "No support JavaScript";
  private static final String STR_VERSION = "JavaScript Controller (C)shi-chan 2001";

  public JSController(Applet paramApplet)
    throws IOException
  {
    System.out.println("JavaScript Controller (C)shi-chan 2001");
    this.applet = paramApplet;
    setup();
  }

  public void alert(String paramString)
    throws IOException
  {
    callFunction("alert", new Object[] { paramString });
  }

  public String callFunction(String paramString, Object[] paramArrayOfObject)
    throws IOException
  {
    try
    {
      if (this.mCall == null)
        this.mCall = this.cJava.getMethod("call", new Class[] { String.class, [Ljava.lang.Object.class });
      return this.mCall.invoke(this.oJava, new Object[] { paramString, paramArrayOfObject }).toString();
    }
    catch (Throwable localThrowable)
    {
    }
    throw new IOException("Can't call " + paramString);
  }

  public boolean confirm(String paramString)
    throws IOException
  {
    return new Boolean(callFunction("confirm", new Object[] { paramString })).booleanValue();
  }

  public String[][] dataLoad()
    throws IOException
  {
    Hashtable localHashtable = new Hashtable();
    dataLoad(localHashtable);
    int i = localHashtable.size();
    String[][] arrayOfString = new String[i][2];
    int j = 0;
    Enumeration localEnumeration = localHashtable.keys();
    while (localEnumeration.hasMoreElements())
    {
      Object localObject = localEnumeration.nextElement();
      arrayOfString[j][0] = ((String)localObject);
      arrayOfString[j][1] = localHashtable.get(localObject).toString();
      j++;
    }
    return arrayOfString;
  }

  public void dataLoad(Hashtable paramHashtable)
    throws IOException
  {
    String str1 = getProperty("document.cookie");
    int i = str1.length();
    for (int j = 0; j < i; j++)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      char c;
      while (j < i)
      {
        c = str1.charAt(j);
        if (c == '=')
        {
          j++;
          break;
        }
        localStringBuffer.append(c);
        j++;
      }
      String str2 = unEscape(localStringBuffer.toString());
      localStringBuffer = new StringBuffer();
      while (j < i)
      {
        c = str1.charAt(j);
        if (c == ';')
        {
          j++;
          break;
        }
        localStringBuffer.append(c);
        j++;
      }
      String str3 = unEscape(localStringBuffer.toString());
      if (str2.equals("expires"))
        continue;
      paramHashtable.put(str2, str3);
    }
  }

  public void dataSave(String[][] paramArrayOfString, int paramInt)
    throws IOException
  {
    try
    {
      Hashtable localHashtable = new Hashtable();
      for (int i = 0; i < paramArrayOfString.length; i++)
        localHashtable.put(paramArrayOfString[i][0], paramArrayOfString[i][1]);
      dataSave(localHashtable, paramInt);
    }
    catch (RuntimeException localRuntimeException)
    {
      throw new RuntimeException("Format error String[*][2]");
    }
  }

  public void dataSave(Hashtable paramHashtable, int paramInt)
    throws IOException
  {
    if (paramHashtable.size() <= 0)
      return;
    StringBuffer localStringBuffer = new StringBuffer();
    Enumeration localEnumeration = paramHashtable.keys();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      localStringBuffer.append(escape(str));
      localStringBuffer.append('=');
      localStringBuffer.append(escape((String)paramHashtable.get(str)));
      localStringBuffer.append("; ");
    }
    if (this.dateFormat == null)
      this.dateFormat = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' GMT'", Locale.ENGLISH);
    localStringBuffer.append("expires=" + this.dateFormat.format(new Date(System.currentTimeMillis() + 86400000 * paramInt)) + "; ");
    setProperty("document.cookie", localStringBuffer.toString());
  }

  public String escape(String paramString)
  {
    try
    {
      return callFunction("escape", new Object[] { paramString });
    }
    catch (IOException localIOException)
    {
    }
    return "";
  }

  private final Object getArray(Object paramObject, Object[] paramArrayOfObject)
    throws IOException
  {
    if (this.mGetSlot == null)
      try
      {
        this.mGetSlot = this.cJava.getMethod("getSlot", new Class[] { Integer.TYPE });
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        throw new IOException("No support JavaScript");
      }
    Object[] arrayOfObject = new Object[1];
    String str1 = (String)paramArrayOfObject[0];
    int i = str1.indexOf('[');
    String str2 = str1.substring(0, i);
    arrayOfObject[0] = str2;
    paramObject = getMember(paramObject, arrayOfObject);
    try
    {
      arrayOfObject[0] = Integer.decode(str1.substring(i + 1, str1.indexOf(93)));
      return this.mGetSlot.invoke(paramObject, arrayOfObject);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
    }
    throw new IOException("No support JavaScript");
  }

  private final Object getEndObject(String paramString, StringBuffer paramStringBuffer)
    throws IOException
  {
    if (this.isCash)
    {
      j = paramString.lastIndexOf('.');
      String str1 = j < 0 ? paramString : paramString.substring(0, j);
      localObject = this.tables.get(str1);
      if (localObject != null)
        return localObject;
    }
    Object localObject = this.oJava;
    int i = 0;
    int j = 0;
    paramString.length();
    Object[] arrayOfObject = new Object[1];
    while ((j = paramString.indexOf('.', i)) >= 0)
    {
      String str2 = paramString.substring(i, j);
      arrayOfObject[0] = str2;
      localObject = getMember(localObject, arrayOfObject);
      i = j + 1;
    }
    paramStringBuffer.append(paramString.substring(i));
    return localObject;
  }

  private final Object getMember(Object paramObject, Object[] paramArrayOfObject)
    throws IOException
  {
    String str = (String)paramArrayOfObject[0];
    if (this.mGetMember == null)
      try
      {
        this.mGetMember = this.cJava.getMethod("getMember", new Class[] { String.class });
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        throw new IOException("No support JavaScript");
      }
    try
    {
      if (str.indexOf('[') >= 0)
        return getArray(paramObject, paramArrayOfObject);
      return this.mGetMember.invoke(paramObject, paramArrayOfObject);
    }
    catch (Throwable localThrowable)
    {
    }
    throw new IOException("can't get " + paramArrayOfObject[0]);
  }

  public String getProperty(String paramString)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Object localObject = getEndObject(paramString, localStringBuffer);
    return getMember(localObject, new Object[] { localStringBuffer.toString() }).toString();
  }

  public void openWindow(String paramString1, String paramString2, Rectangle paramRectangle, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (paramString1 == null)
      paramString1 = "";
    if ((paramString2 == null) || (paramString2.length() == 0))
      paramString2 = "sub";
    if ((!paramBoolean5) && (paramRectangle != null))
      localStringBuffer.append("left=" + paramRectangle.x + ",top=" + paramRectangle.y + ",width=" + paramRectangle.width + ",height=" + paramRectangle.height + ",");
    int i = paramBoolean1 ? 1 : 0;
    localStringBuffer.append("toolbar=" + i + ",location" + i + ",directories=" + i + ",status=" + i + ",menubar=" + i + ',');
    i = paramBoolean2 ? 1 : 0;
    localStringBuffer.append("titlebar=" + i + ',');
    i = paramBoolean3 ? 1 : 0;
    localStringBuffer.append("scrollbars=" + i + ',');
    i = paramBoolean4 ? 1 : 0;
    localStringBuffer.append("resizable=" + i + ',');
    i = paramBoolean5 ? 1 : 0;
    localStringBuffer.append("fullscreen=" + i);
    runScript(paramString2 + "=window.open('" + paramString1 + "','" + paramString2 + "','" + localStringBuffer.toString() + "');");
  }

  public final String runScript(String paramString)
    throws IOException
  {
    if (this.mEval == null)
      try
      {
        this.mEval = this.cJava.getMethod("eval", new Class[] { String.class });
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        throw new IOException("No support JavaScript");
      }
    try
    {
      Object localObject = this.mEval.invoke(this.oJava, new Object[] { paramString });
      return localObject == null ? "" : localObject.toString();
    }
    catch (Throwable localThrowable)
    {
    }
    throw new IOException("ScriptError");
  }

  protected String scriptString(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      switch (c)
      {
      case '\'':
        localStringBuffer.append('\\');
        break;
      case '"':
        localStringBuffer.append('\\');
        break;
      case '\\':
        localStringBuffer.append('\\');
        break;
      case '\r':
        localStringBuffer.append('\\');
        c = 'r';
        break;
      case '\n':
        localStringBuffer.append('\\');
        c = 'n';
      }
      localStringBuffer.append(c);
    }
    return localStringBuffer.toString();
  }

  public synchronized void setCash(boolean paramBoolean)
  {
    this.isCash = paramBoolean;
    if (paramBoolean)
      this.tables = new Hashtable();
    else
      this.tables = null;
  }

  public void setProperty(String paramString, Object paramObject)
    throws IOException
  {
    if (this.mSetMember == null)
      try
      {
        this.mSetMember = this.cJava.getMethod("setMember", new Class[] { String.class, Object.class });
        this.mSetSlot = this.cJava.getMethod("setSlot", new Class[] { Integer.TYPE, Object.class });
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        throw new IOException("Not found Java script");
      }
    StringBuffer localStringBuffer = new StringBuffer();
    Object localObject = getEndObject(paramString, localStringBuffer);
    String str = localStringBuffer.toString();
    localStringBuffer = null;
    try
    {
      if (str.indexOf('[') >= 0)
        this.mSetSlot.invoke(localObject, new Object[] { str, paramObject });
      else
        this.mSetMember.invoke(localObject, new Object[] { str, paramObject });
      return;
    }
    catch (Throwable localThrowable)
    {
    }
    throw new IOException("can't set " + str);
  }

  public final synchronized void setup()
    throws IOException
  {
    try
    {
      if (this.cJava == null)
      {
        this.cJava = Class.forName("netscape.javascript.JSObject");
        this.mGet = this.cJava.getMethod("getWindow", new Class[] { Applet.class });
      }
      this.oJava = this.mGet.invoke(null, new Object[] { this.applet });
    }
    catch (Throwable localThrowable)
    {
      this.oJava = null;
      throw new IOException("No support JavaScript");
    }
  }

  public void showDocument(String paramString1, String paramString2, String paramString3)
    throws IOException
  {
    if ((paramString1 == null) || (paramString1.equals("_self")) || (paramString1.equals("_parent")))
      paramString1 = "";
    if (!paramString1.endsWith("document"))
    {
      if (paramString1.length() > 0)
        paramString1 = paramString1.concat(".");
      paramString1 = paramString1.concat("document");
    }
    if ((paramString2 == null) || (paramString2.length() == 0))
      paramString2 = "text/plain";
    runScript("var doc=" + paramString1 + ".open('" + paramString2 + "');\ndoc.write('" + scriptString(paramString3) + "');\ndoc.close();");
  }

  public String unEscape(String paramString)
  {
    try
    {
      return callFunction("unescape", new Object[] { paramString });
    }
    catch (IOException localIOException)
    {
    }
    return "";
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.javascript.JSController
 * JD-Core Version:    0.6.0
 */