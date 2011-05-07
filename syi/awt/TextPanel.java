package syi.awt;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import paintchat.Res;
import syi.javascript.JSController;

public class TextPanel extends Canvas
  implements ActionListener, ItemListener
{
  public boolean isView = true;
  private boolean isPress = false;
  private Font font;
  private Object lock = new Object();
  private Applet applet;
  private boolean isSScroll = false;
  private boolean isGetFSize = false;
  private boolean isGetSize = false;
  private boolean isVisitScroll = true;
  private int H = 15;
  private int WS = 12;
  private int As = 0;
  private int Ds = 0;
  private int Gap = 1;
  private int scrollPos;
  private int scrollMax;
  private int iSeek = 0;
  private TextField textField;
  private Res config;
  private Color nowColor = null;
  private Color beColor = null;
  private String[] strings = null;
  private Color[] colors = null;
  private Graphics primary = null;
  private static PopupMenu popup = null;
  private int Y;
  private Dimension size = new Dimension();
  private static final String strEmpty = "";

  public TextPanel()
  {
    this.nowColor = Color.black;
  }

  public TextPanel(Applet paramApplet, int paramInt, Color paramColor1, Color paramColor2, TextField paramTextField)
  {
    init(paramApplet, paramInt, paramColor1, paramColor2, paramTextField);
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    try
    {
      String str1 = paramActionEvent.getActionCommand();
      if ((str1 == null) || (str1.length() <= 0))
        return;
      PopupMenu localPopupMenu = popup;
      if (localPopupMenu.getItem(0).getLabel().equals(str1))
      {
        if ((str1 = getLine(this.Y)) != null)
          this.textField.setText(str1);
        return;
      }
      String str2;
      int k;
      if (localPopupMenu.getItem(2).getLabel().equals(str1))
      {
        str2 = getLine(this.Y);
        int j = str2.indexOf("http://");
        k = str2.indexOf(' ', j);
        k = k < 0 ? str2.length() : k;
        this.applet.getAppletContext().showDocument(new URL(str2.substring(j, k)), "jump_url");
        return;
      }
      if (localPopupMenu.getItem(3).getLabel().equals(str1))
      {
        str2 = System.getProperty("line.separator");
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("<html><body>");
        localStringBuffer.append(str2);
        for (k = 0; k < this.iSeek; k++)
        {
          str1 = this.strings[k];
          if (str1 == null)
            continue;
          str1 = Awt.replaceText(str1, "&lt;", "<");
          str1 = Awt.replaceText(str1, "&gt;", ">");
          localStringBuffer.append(str1);
          localStringBuffer.append("<br>");
          localStringBuffer.append(str2);
        }
        localStringBuffer.append("<div align=\"right\"> <a href=\"http://www.gt.sakura.ne.jp/~ocosama/\">shi-chan site</a></div>");
        localStringBuffer.append("</body></html>");
        String str3 = "text_html";
        JSController localJSController = new JSController(this.applet);
        localJSController.openWindow(null, str3, null, true, false, true, true, false);
        localJSController.showDocument(str3, "text/html", localStringBuffer.toString());
        return;
      }
      if (localPopupMenu.getItem(7).getLabel().equals(str1))
      {
        clear();
        repaint();
        return;
      }
      if (str1.charAt(0) == '+')
        str1 = str1.substring(1);
      int i = Math.min(Math.max(this.font.getSize() + Integer.parseInt(str1), 4), 256);
      setFont(new Font(this.font.getName(), this.font.getStyle(), i));
      repaint();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void addText(String paramString)
  {
    addText(paramString, true);
  }

  public void addText(String paramString, boolean paramBoolean)
  {
    synchronized (this.lock)
    {
      if (this.iSeek > 0)
      {
        System.arraycopy(this.strings, 0, this.strings, 1, this.iSeek);
        System.arraycopy(this.colors, 0, this.colors, 1, this.iSeek);
      }
    }
    this.strings[0] = paramString;
    this.colors[0] = this.nowColor;
    if (this.iSeek < this.strings.length - 2)
      this.iSeek += 1;
    if ((paramBoolean) && (this.isGetFSize))
    {
      if (this.primary == null)
        this.primary = getGraphics();
      paint(this.primary);
    }
  }

  public void call(String paramString)
  {
    try
    {
      JSController localJSController = new JSController(this.applet);
      localJSController.runScript(paramString);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public synchronized void clear()
  {
    for (int i = 0; i < this.strings.length; i++)
    {
      this.strings[i] = null;
      this.colors[i] = null;
    }
    this.iSeek = 0;
  }

  public void decode(String paramString)
  {
    if (paramString.length() <= 0)
      return;
    if (paramString.charAt(0) == '$')
    {
      int i = 0;
      int j = 0;
      if (paramString.startsWith("$js:"))
      {
        call(paramString.substring(4, paramString.length()));
        return;
      }
      while (i < paramString.length())
      {
        j = paramString.indexOf(';', i);
        if (j < 0)
          j = paramString.length();
        decode(paramString, i, j);
        i = j + 1;
      }
    }
    else
    {
      decode(paramString, 0, paramString.length());
    }
  }

  private void decode(String paramString, int paramInt1, int paramInt2)
  {
    if (paramInt2 - paramInt1 <= 0)
      return;
    try
    {
      if (paramString.charAt(paramInt1) != '$')
      {
        addText(paramString.substring(paramInt1, paramInt2));
        return;
      }
      int i = paramString.indexOf(':', paramInt1);
      String str1 = "";
      String str2 = "";
      if ((i <= paramInt1) || (i > paramInt2))
        str1 = paramString;
      else
        str1 = paramString.substring(paramInt1, i);
      if ((i >= 0) && (i < paramInt2 - 1))
        str2 = paramString.substring(i + 1, paramInt2);
      int j;
      if (str1.indexOf("$clear") >= 0)
      {
        synchronized (this.lock)
        {
          for (j = 0; j < this.strings.length; j++)
            this.strings[j] = null;
        }
      }
      else if (str1.indexOf("$bkcolor") >= 0)
      {
        setBackground(new Color(Res.parseInt(str2)));
      }
      else if (str1.indexOf("$color") >= 0)
      {
        setForeground(str2.charAt(0) == '/' ? this.beColor : new Color(Res.parseInt(str2)));
      }
      else if (str1.indexOf("$font_size") >= 0)
      {
        ??? = getFont();
        i = Res.parseInt(str2);
        setFont(new Font(((Font)???).getName(), 0, ((Font)???).getSize() + i));
        repaint();
      }
      else if (str1.indexOf("$font") >= 0)
      {
        ??? = getFont();
        j = str2.equals("bold") ? 1 : 0;
        setFont(new Font(((Font)???).getName(), j, ((Font)???).getSize()));
        repaint();
      }
      else if (str1.indexOf("$js") >= 0)
      {
        call(str2);
      }
      else
      {
        addText(paramString.substring(paramInt1, paramInt2));
      }
    }
    catch (RuntimeException localRuntimeException)
    {
    }
  }

  public String getLine(int paramInt)
  {
    int i = (this.scrollPos + paramInt) / (this.H + this.Gap * 2);
    return this.strings[i];
  }

  public Dimension getPreferredSize()
  {
    Dimension localDimension = getToolkit().getScreenSize();
    int i = this.Gap * 2;
    Font localFont = getFont();
    FontMetrics localFontMetrics = null;
    if (localFont != null)
      localFontMetrics = getFontMetrics(localFont);
    if (localFontMetrics == null)
    {
      localDimension.setSize(300, 120);
      return localDimension;
    }
    this.H = (localFontMetrics.getMaxAscent() + localFontMetrics.getMaxDescent() + 1);
    int j = 0;
    if (this.strings != null)
      for (int k = 0; k < this.iSeek; k++)
      {
        if (this.strings[k] == null)
          continue;
        j = Math.max(localFontMetrics.stringWidth(this.strings[k]) + i, j);
      }
    j += i + this.WS;
    localDimension.setSize(j >= localDimension.width / 2 ? localDimension.width / 2 : j <= 100 ? 100 : j, (this.H + i) * (this.iSeek >= 12 ? 12 : this.iSeek <= 3 ? 3 : this.iSeek));
    return localDimension;
  }

  public Dimension getSize()
  {
    if ((this.size.width + this.size.height == 0) || (!this.isGetSize))
      this.size.setSize(super.getSize());
    return this.size;
  }

  public void init(Applet paramApplet, int paramInt, Color paramColor1, Color paramColor2, TextField paramTextField)
  {
    enableEvents(49L);
    this.textField = paramTextField;
    this.applet = paramApplet;
    setBackground(paramColor1);
    setForeground(paramColor2);
    this.nowColor = (this.beColor = paramColor2);
    setMaxLabel(paramInt);
  }

  public void itemStateChanged(ItemEvent paramItemEvent)
  {
    try
    {
      this.isSScroll = (paramItemEvent.getStateChange() == 1);
      this.scrollPos = 0;
      paint(this.primary);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void paint(Graphics paramGraphics)
  {
    try
    {
      Dimension localDimension = getSize();
      if (!this.isView)
      {
        paramGraphics.setColor(getBackground());
        paramGraphics.fillRect(0, 0, localDimension.width, localDimension.height);
        return;
      }
      if (this.strings == null)
        return;
      if (paramGraphics == null)
        return;
      int i = this.Gap * 2;
      this.strings.length;
      if (!this.isGetFSize)
      {
        this.isGetFSize = true;
        if (this.font == null)
          this.font = super.getFont();
        FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
        this.As = localFontMetrics.getMaxAscent();
        this.Ds = localFontMetrics.getMaxDescent();
        this.H = (this.As + this.Ds);
        this.scrollPos = 0;
      }
      paramGraphics.setFont(this.font);
      int j = this.H + i;
      this.scrollMax = (j * this.iSeek);
      if ((localDimension.height <= 0) || (localDimension.width <= 0))
        return;
      int k = this.WS;
      int m = localDimension.width - 2 - k;
      int n = Math.max(this.scrollPos / j, 0);
      int i1 = n + localDimension.height / j + 2;
      int i2 = n * j - this.scrollPos;
      paramGraphics.setClip(1, 1, m, localDimension.height - 2);
      Color localColor = getBackground();
      paramGraphics.setColor(localColor);
      for (int i3 = n; i3 < i1; i3++)
      {
        if ((i3 < this.strings.length) && (this.strings[i3] != null))
        {
          paramGraphics.fillRect(1, i2, m, this.H + i);
          paramGraphics.setColor(this.colors[i3]);
          paramGraphics.drawString(this.strings[i3], 1, i2 + this.Gap + this.H - this.Ds);
          paramGraphics.setColor(localColor);
        }
        else
        {
          paramGraphics.fillRect(1, i2, m, this.H + i);
        }
        i2 += j;
      }
      if (i2 > 0)
      {
        paramGraphics.setColor(Color.black);
        paramGraphics.fillRect(1, i2, m - 1, localDimension.height - i2 - 1);
      }
      paramGraphics.setClip(0, 0, localDimension.width, localDimension.height);
      if (this.isVisitScroll)
      {
        i3 = localDimension.height / (this.H + i);
        int i4 = (int)(localDimension.height * (i3 / (this.iSeek + i3)));
        i2 = (int)(this.scrollPos / this.scrollMax * (localDimension.height - i4 - 1));
        m++;
        k--;
        paramGraphics.setColor(getForeground());
        paramGraphics.fillRect(m, 1, 1, localDimension.height - 2);
        paramGraphics.fillRect(m + 1, i2, k, i4);
        paramGraphics.setColor(localColor);
        paramGraphics.setColor(getBackground());
        paramGraphics.fillRect(m + 1, 1, k, i2);
        paramGraphics.fillRect(m + 1, i2 + i4 + 1, k, localDimension.height - i4 - i2 - 1);
        paramGraphics.setColor(getForeground());
        paramGraphics.drawRect(0, 0, localDimension.width - 1, localDimension.height - 1);
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void popup(int paramInt1, int paramInt2)
  {
    if (popup == null)
    {
      Menu localMenu = new Menu("Font size");
      localMenu.addActionListener(this);
      for (int i = 6; i > -6; i--)
      {
        if (i == 0)
          continue;
        String str = String.valueOf(i);
        if (i > 0)
          str = '+' + str;
        localMenu.add(str);
      }
      popup = new PopupMenu();
      popup.add("CopyString");
      popup.addSeparator();
      popup.add("GotoURL");
      popup.add("ToHTML");
      popup.addSeparator();
      popup.add(localMenu);
      popup.addSeparator();
      popup.add("Erase");
      CheckboxMenuItem localCheckboxMenuItem = new CheckboxMenuItem("Smooth scroll", this.isSScroll);
      localCheckboxMenuItem.addItemListener(this);
      popup.add(localCheckboxMenuItem);
    }
    add(popup);
    popup.addActionListener(this);
    popup.getItem(2).setEnabled(getLine(paramInt2).indexOf("http://") >= 0);
    popup.show(this, paramInt1, paramInt2);
  }

  protected void processEvent(AWTEvent paramAWTEvent)
  {
    try
    {
      if ((paramAWTEvent instanceof MouseEvent))
      {
        MouseEvent localMouseEvent = (MouseEvent)paramAWTEvent;
        int i = localMouseEvent.getX();
        int j = localMouseEvent.getY();
        switch (paramAWTEvent.getID())
        {
        case 501:
          this.Y = j;
          this.isPress = (!Awt.isR(localMouseEvent));
          if (this.isPress)
            break;
          popup(i, j);
          break;
        case 502:
          this.isPress = false;
          break;
        case 506:
          if ((!this.isPress) || (this.Y == j))
            break;
          int k = -(this.Y - j);
          if (!this.isSScroll)
            k *= (this.H + this.Gap * 2);
          this.scrollPos = Math.max(Math.min(this.scrollPos + k, this.scrollMax), 0);
          this.Y = j;
          if (this.primary == null)
            this.primary = getGraphics();
          paint(this.primary);
        case 503:
        case 504:
        case 505:
        }
        return;
      }
      if ((paramAWTEvent instanceof ComponentEvent))
      {
        switch (paramAWTEvent.getID())
        {
        case 101:
        case 102:
          this.isGetSize = false;
          if (this.primary != null)
          {
            this.primary.dispose();
            this.primary = null;
          }
          if (!this.isGetFSize)
            break;
          repaint();
        }
        return;
      }
      super.processEvent(paramAWTEvent);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void remove(String paramString)
  {
    try
    {
      synchronized (this.lock)
      {
        int i = this.iSeek;
        for (int j = 0; j < i; j++)
        {
          String str;
          if (((str = this.strings[j]) == null) || (!str.equals(paramString)))
            continue;
          if (j != i - 1)
            System.arraycopy(this.strings, j + 1, this.strings, j, i - j - 1);
          this.strings[(i - 1)] = null;
          this.iSeek -= 1;
          break;
        }
      }
      repaint();
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
  }

  public void setFont(Font paramFont)
  {
    this.font = paramFont;
    this.isGetFSize = false;
  }

  public void setForeground(Color paramColor)
  {
    this.beColor = this.nowColor;
    this.nowColor = paramColor;
    super.setForeground(paramColor);
  }

  public void setMaxLabel(int paramInt)
  {
    if (paramInt <= 0)
      return;
    String[] arrayOfString = new String[paramInt];
    Color[] arrayOfColor = new Color[paramInt];
    this.scrollPos = 0;
    if (this.strings != null)
      System.arraycopy(this.strings, 0, arrayOfString, 0, this.strings.length);
    if (this.colors != null)
      System.arraycopy(this.colors, 0, arrayOfColor, 0, this.colors.length);
    this.strings = arrayOfString;
    this.colors = arrayOfColor;
  }

  public void setRText(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new StringReader(paramString));
      String str;
      while ((str = localBufferedReader.readLine()) != null)
      {
        localStringBuffer.insert(0, str);
        localStringBuffer.insert(0, '\n');
      }
      localBufferedReader.close();
    }
    catch (IOException localIOException)
    {
    }
    setText(localStringBuffer.toString());
  }

  public void setText(String paramString)
  {
    int j = paramString.length();
    CharArrayWriter localCharArrayWriter = new CharArrayWriter();
    for (int k = 0; k < j; k++)
    {
      int i = paramString.charAt(k);
      if ((i == 13) || (i == 10))
      {
        if (localCharArrayWriter.size() <= 0)
          continue;
        decode(localCharArrayWriter.toString());
        localCharArrayWriter.reset();
      }
      else
      {
        localCharArrayWriter.write(i);
      }
    }
    if (localCharArrayWriter.size() > 0)
      decode(localCharArrayWriter.toString());
  }

  public void setVisitScroll(boolean paramBoolean)
  {
    this.isVisitScroll = paramBoolean;
    this.WS = (paramBoolean ? 12 : 0);
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.TextPanel
 * JD-Core Version:    0.6.0
 */