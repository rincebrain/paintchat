package paintchat_client;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.EventObject;
import java.util.Hashtable;
import paintchat.M;
import paintchat.M.Info;
import paintchat.MgText;
import paintchat.Res;
import paintchat.ToolBox;
import syi.awt.Awt;
import syi.awt.LButton;
import syi.awt.LComponent;
import syi.awt.TextPanel;
import syi.util.ThreadPool;

public class Pl extends Panel
  implements Runnable, ActionListener, IMi, KeyListener
{
  private static final String STR_VERSION = "PaintChatClient v3.66";
  private static final String STR_INFO = "PaintChat";
  protected Applet applet;
  private boolean isStart = false;
  private int iScrollType = 0;
  private Data dd;
  public Res res;
  public Mi mi;
  private ToolBox tool = null;
  private Panel tPanel;
  private Panel tPanelB;
  private TextPanel tText;
  private TextField tField;
  private TextPanel tList;
  private Panel miPanel;
  private Label tLabel;
  private MgText mgText;
  private Dimension dPack = new Dimension();
  private Dimension dSize = null;
  private Dimension dMax = new Dimension();
  private int iGap = 5;
  private int iCenter = 80;
  private int iCenterOld = -1;
  private Color clInfo;
  private AudioClip[] sounds = null;
  private int iPG = 10;

  public Pl(Applet paramApplet)
  {
    super(null);
    this.applet = paramApplet;
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    try
    {
      Object localObject = paramActionEvent.getSource();
      if ((localObject instanceof LButton))
        switch (Integer.parseInt(((Component)localObject).getName()))
        {
        case 0:
          f(this.tPanel, true);
          break;
        case 1:
          f(this, false);
          break;
        case 2:
          mExit();
        default:
          break;
        }
      else
        typed();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void mExit()
  {
    try
    {
      this.applet.getAppletContext().showDocument(new URL(this.applet.getDocumentBase(), this.dd.config.getP("exit", "../index.html")));
    }
    catch (Throwable localThrowable)
    {
    }
  }

  protected void addInOut(String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.tList.addText(paramString);
      paramString = paramString + this.res.res("entered");
      dSound(2);
    }
    else
    {
      this.tList.remove(paramString);
      paramString = paramString + this.res.res("leaved");
      dSound(3);
    }
    addTextInfo(paramString, false);
  }

  protected void addSText(String paramString)
  {
    this.tText.decode(paramString);
  }

  protected void addText(String paramString1, String paramString2, boolean paramBoolean)
  {
    if (paramString2 == null)
      this.tText.repaint();
    else
      this.tText.addText(paramString1 + '>' + paramString2, paramBoolean);
  }

  protected void addTextInfo(String paramString, boolean paramBoolean)
  {
    Color localColor = this.tText.getForeground();
    this.tText.setForeground(Color.red);
    addText(null, "PaintChat>" + paramString, paramBoolean);
    this.tText.setForeground(localColor);
  }

  public void changeSize()
  {
    this.mi.resetGraphics();
    pack();
  }

  public void destroy()
  {
    try
    {
      if (this.dd != null)
        this.dd.destroy();
      this.dd = null;
      this.tool = null;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  protected void dSound(int paramInt)
  {
    try
    {
      if ((this.sounds == null) || (this.sounds[paramInt] == null))
        return;
      this.sounds[paramInt].play();
    }
    catch (RuntimeException localRuntimeException)
    {
      this.sounds = null;
    }
  }

  private void f(Component paramComponent, boolean paramBoolean)
  {
    try
    {
      int i = 0;
      Object localObject = paramBoolean ? this : this.applet;
      Component[] arrayOfComponent = ((Container)localObject).getComponents();
      for (int j = 0; j < arrayOfComponent.length; j++)
      {
        if (arrayOfComponent[j] != paramComponent)
          continue;
        i = 1;
        break;
      }
      localObject = paramComponent.getParent();
      ((Container)localObject).remove(paramComponent);
      if (i != 0)
      {
        if (paramBoolean)
          this.iCenter = 100;
        pack();
        Frame localFrame = new Frame("PaintChatClient v3.66");
        localFrame.setLayout(new BorderLayout());
        localFrame.add(paramComponent, "Center");
        localFrame.pack();
        localFrame.setVisible(true);
      }
      else
      {
        ((Window)localObject).dispose();
        if (paramBoolean)
        {
          this.iCenter = 70;
          add(paramComponent);
        }
        else
        {
          this.applet.add(paramComponent, "Center");
          this.applet.validate();
        }
        pack();
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public Dimension getSize()
  {
    if (this.dSize == null)
      this.dSize = super.getSize();
    return this.dSize;
  }

  public void iPG(boolean paramBoolean)
  {
    if (paramBoolean)
      this.iPG = Math.min(100, this.iPG + 10);
    if (this.isStart)
      return;
    try
    {
      Graphics localGraphics = getGraphics();
      if (localGraphics == null)
        return;
      String str = String.valueOf(this.iPG) + '%';
      FontMetrics localFontMetrics = localGraphics.getFontMetrics();
      int i = localFontMetrics.getHeight() + 2;
      localGraphics.setColor(getBackground());
      localGraphics.fillRect(5, 5 + i, localFontMetrics.stringWidth(str) + 15, i + 10);
      localGraphics.setColor(getForeground());
      localGraphics.drawString("PaintChatClient v3.66", 10, 10 + i);
      localGraphics.drawString(str, 10, 10 + i * 2);
      localGraphics.dispose();
    }
    catch (Throwable localThrowable)
    {
    }
  }

  public void keyPressed(KeyEvent paramKeyEvent)
  {
    try
    {
      int i = (!paramKeyEvent.isAltDown()) && (!paramKeyEvent.isControlDown()) ? 0 : 1;
      int j = paramKeyEvent.getKeyCode();
      if (i != 0)
      {
        if (j == 38)
        {
          paramKeyEvent.consume();
          this.iCenter = Math.max(this.iCenter - 4, 0);
          pack();
        }
        if (j == 40)
        {
          paramKeyEvent.consume();
          this.iCenter = Math.min(this.iCenter + 4, 100);
          pack();
        }
        if (j == 83)
        {
          paramKeyEvent.consume();
          typed();
        }
      }
      else
      {
        switch (j)
        {
        case 117:
          f(this, false);
          break;
        case 10:
          break;
        default:
          dSound(0);
        }
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void keyReleased(KeyEvent paramKeyEvent)
  {
  }

  public void keyTyped(KeyEvent paramKeyEvent)
  {
  }

  private Cursor loadCursor(String paramString, int paramInt)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +695 -> 696
    //   4: aload_1
    //   5: invokevirtual 396	java/lang/String:length	()I
    //   8: ifle +688 -> 696
    //   11: aload_1
    //   12: ldc_w 398
    //   15: invokevirtual 402	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   18: istore_3
    //   19: iload_3
    //   20: ifne +164 -> 184
    //   23: aload_0
    //   24: invokevirtual 406	java/awt/Component:getToolkit	()Ljava/awt/Toolkit;
    //   27: aload_0
    //   28: getfield 153	paintchat_client/Pl:dd	Lpaintchat_client/Data;
    //   31: getfield 158	paintchat_client/Data:config	Lpaintchat/Res;
    //   34: aload_1
    //   35: invokevirtual 410	paintchat/Res:getRes	(Ljava/lang/Object;)Ljava/lang/Object;
    //   38: checkcast 412	[B
    //   41: invokevirtual 418	java/awt/Toolkit:createImage	([B)Ljava/awt/Image;
    //   44: astore 8
    //   46: aload 8
    //   48: ifnonnull +8 -> 56
    //   51: iload_2
    //   52: invokestatic 424	java/awt/Cursor:getPredefinedCursor	(I)Ljava/awt/Cursor;
    //   55: areturn
    //   56: aload 8
    //   58: invokestatic 430	syi/awt/Awt:wait	(Ljava/awt/Image;)V
    //   61: aload 8
    //   63: aconst_null
    //   64: invokevirtual 436	java/awt/Image:getWidth	(Ljava/awt/image/ImageObserver;)I
    //   67: istore 6
    //   69: aload 8
    //   71: aconst_null
    //   72: invokevirtual 438	java/awt/Image:getHeight	(Ljava/awt/image/ImageObserver;)I
    //   75: istore 7
    //   77: aload_1
    //   78: bipush 120
    //   80: invokevirtual 442	java/lang/String:indexOf	(I)I
    //   83: istore 4
    //   85: iload 4
    //   87: iconst_m1
    //   88: if_icmpne +41 -> 129
    //   91: iload 4
    //   93: iconst_m1
    //   94: if_icmpne +12 -> 106
    //   97: iload 6
    //   99: iconst_2
    //   100: idiv
    //   101: iconst_1
    //   102: isub
    //   103: goto +24 -> 127
    //   106: aload_1
    //   107: iload 4
    //   109: iconst_1
    //   110: iadd
    //   111: aload_1
    //   112: bipush 120
    //   114: iload 4
    //   116: iconst_1
    //   117: iadd
    //   118: invokevirtual 444	java/lang/String:indexOf	(II)I
    //   121: invokevirtual 448	java/lang/String:substring	(II)Ljava/lang/String;
    //   124: invokestatic 122	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   127: istore 4
    //   129: aload_1
    //   130: bipush 121
    //   132: invokevirtual 442	java/lang/String:indexOf	(I)I
    //   135: istore 5
    //   137: iload 5
    //   139: iconst_m1
    //   140: if_icmpne +61 -> 201
    //   143: iload 5
    //   145: iconst_m1
    //   146: if_icmpne +12 -> 158
    //   149: iload 7
    //   151: iconst_2
    //   152: idiv
    //   153: iconst_1
    //   154: isub
    //   155: goto +24 -> 179
    //   158: aload_1
    //   159: iload 5
    //   161: iconst_1
    //   162: iadd
    //   163: aload_1
    //   164: bipush 121
    //   166: iload 5
    //   168: iconst_1
    //   169: iadd
    //   170: invokevirtual 444	java/lang/String:indexOf	(II)I
    //   173: invokevirtual 448	java/lang/String:substring	(II)Ljava/lang/String;
    //   176: invokestatic 122	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   179: istore 5
    //   181: goto +20 -> 201
    //   184: bipush 7
    //   186: dup
    //   187: istore 5
    //   189: istore 4
    //   191: bipush 16
    //   193: dup
    //   194: istore 7
    //   196: istore 6
    //   198: aconst_null
    //   199: astore 8
    //   201: aload 8
    //   203: ifnonnull +54 -> 257
    //   206: new 450	java/awt/image/IndexColorModel
    //   209: dup
    //   210: bipush 8
    //   212: iconst_2
    //   213: iconst_2
    //   214: newarray byte
    //   216: iconst_2
    //   217: newarray byte
    //   219: iconst_2
    //   220: newarray byte
    //   222: iconst_0
    //   223: invokespecial 453	java/awt/image/IndexColorModel:<init>	(II[B[B[BI)V
    //   226: astore 9
    //   228: aload_0
    //   229: new 455	java/awt/image/MemoryImageSource
    //   232: dup
    //   233: iload 6
    //   235: iload 7
    //   237: aload 9
    //   239: iload 6
    //   241: iload 7
    //   243: imul
    //   244: newarray byte
    //   246: iconst_0
    //   247: iload 6
    //   249: invokespecial 458	java/awt/image/MemoryImageSource:<init>	(IILjava/awt/image/ColorModel;[BII)V
    //   252: invokevirtual 461	java/awt/Component:createImage	(Ljava/awt/image/ImageProducer;)Ljava/awt/Image;
    //   255: astore 8
    //   257: aload_0
    //   258: invokevirtual 406	java/awt/Component:getToolkit	()Ljava/awt/Toolkit;
    //   261: astore 9
    //   263: aload 9
    //   265: invokevirtual 467	java/lang/Object:getClass	()Ljava/lang/Class;
    //   268: pop
    //   269: getstatic 469	paintchat_client/Pl:class$0	Ljava/lang/Class;
    //   272: dup
    //   273: ifnonnull +29 -> 302
    //   276: pop
    //   277: ldc_w 471
    //   280: invokestatic 477	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   283: dup
    //   284: putstatic 469	paintchat_client/Pl:class$0	Ljava/lang/Class;
    //   287: goto +15 -> 302
    //   290: new 479	java/lang/NoClassDefFoundError
    //   293: dup_x1
    //   294: swap
    //   295: invokevirtual 482	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   298: invokespecial 483	java/lang/NoClassDefFoundError:<init>	(Ljava/lang/String;)V
    //   301: athrow
    //   302: ldc_w 485
    //   305: iconst_3
    //   306: anewarray 473	java/lang/Class
    //   309: dup
    //   310: iconst_0
    //   311: getstatic 487	paintchat_client/Pl:class$1	Ljava/lang/Class;
    //   314: dup
    //   315: ifnonnull +29 -> 344
    //   318: pop
    //   319: ldc_w 489
    //   322: invokestatic 477	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   325: dup
    //   326: putstatic 487	paintchat_client/Pl:class$1	Ljava/lang/Class;
    //   329: goto +15 -> 344
    //   332: new 479	java/lang/NoClassDefFoundError
    //   335: dup_x1
    //   336: swap
    //   337: invokevirtual 482	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   340: invokespecial 483	java/lang/NoClassDefFoundError:<init>	(Ljava/lang/String;)V
    //   343: athrow
    //   344: aastore
    //   345: dup
    //   346: iconst_1
    //   347: getstatic 491	paintchat_client/Pl:class$2	Ljava/lang/Class;
    //   350: dup
    //   351: ifnonnull +29 -> 380
    //   354: pop
    //   355: ldc_w 493
    //   358: invokestatic 477	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   361: dup
    //   362: putstatic 491	paintchat_client/Pl:class$2	Ljava/lang/Class;
    //   365: goto +15 -> 380
    //   368: new 479	java/lang/NoClassDefFoundError
    //   371: dup_x1
    //   372: swap
    //   373: invokevirtual 482	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   376: invokespecial 483	java/lang/NoClassDefFoundError:<init>	(Ljava/lang/String;)V
    //   379: athrow
    //   380: aastore
    //   381: dup
    //   382: iconst_2
    //   383: getstatic 495	paintchat_client/Pl:class$3	Ljava/lang/Class;
    //   386: dup
    //   387: ifnonnull +29 -> 416
    //   390: pop
    //   391: ldc_w 497
    //   394: invokestatic 477	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   397: dup
    //   398: putstatic 495	paintchat_client/Pl:class$3	Ljava/lang/Class;
    //   401: goto +15 -> 416
    //   404: new 479	java/lang/NoClassDefFoundError
    //   407: dup_x1
    //   408: swap
    //   409: invokevirtual 482	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   412: invokespecial 483	java/lang/NoClassDefFoundError:<init>	(Ljava/lang/String;)V
    //   415: athrow
    //   416: aastore
    //   417: invokevirtual 501	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   420: astore 10
    //   422: getstatic 469	paintchat_client/Pl:class$0	Ljava/lang/Class;
    //   425: dup
    //   426: ifnonnull +29 -> 455
    //   429: pop
    //   430: ldc_w 471
    //   433: invokestatic 477	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   436: dup
    //   437: putstatic 469	paintchat_client/Pl:class$0	Ljava/lang/Class;
    //   440: goto +15 -> 455
    //   443: new 479	java/lang/NoClassDefFoundError
    //   446: dup_x1
    //   447: swap
    //   448: invokevirtual 482	java/lang/Throwable:getMessage	()Ljava/lang/String;
    //   451: invokespecial 483	java/lang/NoClassDefFoundError:<init>	(Ljava/lang/String;)V
    //   454: athrow
    //   455: ldc_w 503
    //   458: iconst_2
    //   459: anewarray 473	java/lang/Class
    //   462: dup
    //   463: iconst_0
    //   464: getstatic 506	java/lang/Integer:TYPE	Ljava/lang/Class;
    //   467: aastore
    //   468: dup
    //   469: iconst_1
    //   470: getstatic 506	java/lang/Integer:TYPE	Ljava/lang/Class;
    //   473: aastore
    //   474: invokevirtual 501	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   477: astore 11
    //   479: aload 11
    //   481: aload 9
    //   483: iconst_2
    //   484: anewarray 463	java/lang/Object
    //   487: dup
    //   488: iconst_0
    //   489: new 118	java/lang/Integer
    //   492: dup
    //   493: iload 6
    //   495: invokespecial 508	java/lang/Integer:<init>	(I)V
    //   498: aastore
    //   499: dup
    //   500: iconst_1
    //   501: new 118	java/lang/Integer
    //   504: dup
    //   505: iload 7
    //   507: invokespecial 508	java/lang/Integer:<init>	(I)V
    //   510: aastore
    //   511: invokevirtual 514	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   514: checkcast 79	java/awt/Dimension
    //   517: astore 12
    //   519: aload 12
    //   521: getfield 517	java/awt/Dimension:width	I
    //   524: ifeq +172 -> 696
    //   527: aload 12
    //   529: getfield 520	java/awt/Dimension:height	I
    //   532: ifeq +164 -> 696
    //   535: aload 10
    //   537: aload 9
    //   539: iconst_3
    //   540: anewarray 463	java/lang/Object
    //   543: dup
    //   544: iconst_0
    //   545: aload 8
    //   547: aastore
    //   548: dup
    //   549: iconst_1
    //   550: new 522	java/awt/Point
    //   553: dup
    //   554: iload 6
    //   556: i2f
    //   557: aload 12
    //   559: getfield 517	java/awt/Dimension:width	I
    //   562: i2f
    //   563: fdiv
    //   564: iload 4
    //   566: i2f
    //   567: fmul
    //   568: f2i
    //   569: iload 7
    //   571: i2f
    //   572: aload 12
    //   574: getfield 520	java/awt/Dimension:height	I
    //   577: i2f
    //   578: fdiv
    //   579: iload 5
    //   581: i2f
    //   582: fmul
    //   583: f2i
    //   584: invokespecial 525	java/awt/Point:<init>	(II)V
    //   587: aastore
    //   588: dup
    //   589: iconst_2
    //   590: ldc_w 527
    //   593: aastore
    //   594: invokevirtual 514	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   597: checkcast 420	java/awt/Cursor
    //   600: areturn
    //   601: goto +95 -> 696
    //   604: pop
    //   605: aload 8
    //   607: ifnonnull +30 -> 637
    //   610: aload_0
    //   611: new 455	java/awt/image/MemoryImageSource
    //   614: dup
    //   615: iload 6
    //   617: iload 7
    //   619: iload 6
    //   621: iload 7
    //   623: imul
    //   624: newarray int
    //   626: iconst_0
    //   627: iload 6
    //   629: invokespecial 530	java/awt/image/MemoryImageSource:<init>	(II[III)V
    //   632: invokevirtual 461	java/awt/Component:createImage	(Ljava/awt/image/ImageProducer;)Ljava/awt/Image;
    //   635: astore 8
    //   637: ldc_w 532
    //   640: invokestatic 477	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   643: invokevirtual 536	java/lang/Class:getConstructors	()[Ljava/lang/reflect/Constructor;
    //   646: iconst_0
    //   647: aaload
    //   648: iconst_3
    //   649: anewarray 463	java/lang/Object
    //   652: dup
    //   653: iconst_0
    //   654: aload 8
    //   656: aastore
    //   657: dup
    //   658: iconst_1
    //   659: new 118	java/lang/Integer
    //   662: dup
    //   663: iload 4
    //   665: invokespecial 508	java/lang/Integer:<init>	(I)V
    //   668: aastore
    //   669: dup
    //   670: iconst_2
    //   671: new 118	java/lang/Integer
    //   674: dup
    //   675: iload 5
    //   677: invokespecial 508	java/lang/Integer:<init>	(I)V
    //   680: aastore
    //   681: invokevirtual 542	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   684: checkcast 420	java/awt/Cursor
    //   687: areturn
    //   688: goto +8 -> 696
    //   691: astore_3
    //   692: aload_3
    //   693: invokevirtual 139	java/lang/Throwable:printStackTrace	()V
    //   696: iload_2
    //   697: invokestatic 424	java/awt/Cursor:getPredefinedCursor	(I)Ljava/awt/Cursor;
    //   700: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   277	283	290	java/lang/ClassNotFoundException
    //   319	325	332	java/lang/ClassNotFoundException
    //   355	361	368	java/lang/ClassNotFoundException
    //   391	397	404	java/lang/ClassNotFoundException
    //   430	436	443	java/lang/ClassNotFoundException
    //   201	604	604	java/lang/NoSuchMethodException
    //   0	691	691	java/lang/Throwable
  }

  private void loadSound()
  {
    if (this.sounds != null)
    {
      this.sounds = null;
      return;
    }
    this.sounds = new AudioClip[4];
    String[] arrayOfString = { "tp.au", "talk.au", "in.au", "out.au" };
    for (int i = 0; i < 4; i++)
      try
      {
        String str = this.dd.config.res(arrayOfString[i]);
        if ((str == null) || (str.length() <= 0) || (str.charAt(0) == '_'))
          continue;
        this.sounds[i] = this.applet.getAudioClip(new URL(this.applet.getCodeBase(), str));
      }
      catch (IOException localIOException)
      {
        this.sounds[i] = null;
      }
  }

  private synchronized void mkTextPanel()
  {
    if (this.tField != null)
      return;
    String str1 = "Center";
    String str2 = "East";
    String str3 = "West";
    Panel localPanel1 = new Panel(new BorderLayout());
    this.tField = new TextField();
    this.tField.addActionListener(this);
    localPanel1.add(this.tField, str1);
    this.tLabel = new Label(this.dd.res.res("input"));
    localPanel1.add(this.tLabel, str3);
    String[] arrayOfString = { "F", "FAll", "leave" };
    Panel localPanel2 = new Panel(new FlowLayout(0, 2, 1));
    this.tPanelB = localPanel2;
    for (int i = 0; i < 3; i++)
    {
      LButton localLButton = new LButton(this.res.res(arrayOfString[i]));
      localLButton.addActionListener(this);
      localLButton.setName(String.valueOf(i));
      localPanel2.add(localLButton);
    }
    localPanel1.add(localPanel2, str2);
    Color localColor1 = getBackground();
    Color localColor2 = getForeground();
    this.tText = new TextPanel(this.applet, 100, localColor1, localColor2, this.tField);
    this.tList = new TextPanel(this.applet, 20, localColor1, localColor2, this.tField);
    this.tPanel = new Panel(new BorderLayout());
    this.tPanel.add(this.tText, str1);
    this.tPanel.add(this.tList, str2);
    this.tPanel.add(localPanel1, "South");
    Awt.getDef(this.tPanel);
    Awt.setDef(this.tPanel, false);
  }

  private void pack()
  {
    this.dSize = super.getSize();
    if ((this.tool == null) || (this.mi == null) || (this.dPack == null))
      return;
    getSize();
    ThreadPool.poolStartThread(this, 'p');
  }

  public void paint(Graphics paramGraphics)
  {
    if (!this.isStart)
      iPG(false);
    Dimension localDimension = getSize();
    paramGraphics.drawRect(0, 0, localDimension.width - 1, localDimension.height - 1);
  }

  protected void processEvent(AWTEvent paramAWTEvent)
  {
    int i = paramAWTEvent.getID();
    Object localObject;
    if (i == 101)
    {
      this.dSize = super.getSize();
      localObject = getSize();
      setSize(((Dimension)localObject).getSize());
      if ((this.dPack != null) && (!this.dPack.equals(localObject)))
        pack();
    }
    else if ((this.mi != null) && ((paramAWTEvent instanceof MouseEvent)))
    {
      localObject = this.mi.getLocation();
      ((MouseEvent)paramAWTEvent).translatePoint(-((Point)localObject).x, -((Point)localObject).y);
      this.mi.dispatchEvent(paramAWTEvent);
    }
    super.processEvent(paramAWTEvent);
  }

  public void repaint(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    repaint(this, paramInt1, paramInt2, paramInt3, paramInt4);
  }

  private void repaint(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Object localObject;
    int i;
    if ((paramComponent instanceof Container))
    {
      localObject = ((Container)paramComponent).getComponents();
      for (i = 0; i < localObject.length; i++)
      {
        Point localPoint = localObject[i].getLocation();
        repaint(localObject[i], paramInt1 - localPoint.x, paramInt2 - localPoint.y, paramInt3, paramInt4);
      }
    }
    else
    {
      localObject = paramComponent.getLocation();
      i = paramInt1 - ((Point)localObject).x;
      int j = paramInt2 - ((Point)localObject).y;
      if ((i + paramInt3 <= 0) || (j + paramInt4 <= 0))
        return;
      paramComponent.repaint(i, j, paramInt3, paramInt4);
    }
  }

  private void rInit()
  {
    String str1 = "cursor_";
    String str2 = "window_color_";
    try
    {
      getSize();
      this.dd = new Data(this);
      this.mgText = new MgText();
      this.mi = new Mi(this, this.res);
      iPG(true);
      this.dd.mi = this.mi;
      this.dd.init();
      this.res = this.dd.res;
      Res localRes = this.dd.config;
      int j = localRes.getP("layer_count", 2);
      int k = localRes.getP("quality", 1);
      try
      {
        localObject = new Color(localRes.getP("color_bk", 13619199));
        this.applet.setBackground((Color)localObject);
        setBackground((Color)localObject);
        localObject = new Color(localRes.getP(str2 + "_bk", ((Color)localObject).getRGB()));
        Awt.cBk = Awt.cC = localObject;
        localObject = new Color(localRes.getP("color_text", 5263480));
        this.applet.setForeground((Color)localObject);
        setForeground((Color)localObject);
        Awt.cFore = new Color(localRes.getP(str2 + "_text", ((Color)localObject).getRGB()));
        Awt.cFSel = new Color(localRes.getP("color_iconselect", 15610675));
        Awt.cF = new Color(localRes.getP(str2 + "_frame", 0));
        Awt.clBar = new Color(localRes.getP(str2 + "_bar", 6711039));
        Awt.clLBar = new Color(localRes.getP(str2 + "_bar_hl", 8947967));
        Awt.clBarT = new Color(localRes.getP(str2 + "_bar_text", 16777215));
        Awt.getDef(this);
        Awt.setPFrame((Frame)Awt.getParent(this));
      }
      catch (Throwable localThrowable3)
      {
      }
      iPG(true);
      Object localObject = new Cursor[4];
      int i = 0;
      int[] arrayOfInt = { i, 13, i, i };
      for (i = 0; i < 4; i++)
        localObject[i] = loadCursor(this.applet.getParameter(str1 + (i + 1)), arrayOfInt[i]);
      iPG(true);
      this.miPanel = new Panel(null);
      this.mi.init(this.applet, this.dd.config, this.dd.imW, this.dd.imH, k, j, localObject);
      this.miPanel.add(this.mi);
      iPG(true);
      String str3 = localRes.getP("tools", "normal");
      try
      {
        this.tool = ((ToolBox)Class.forName("paintchat." + str3 + ".Tools").newInstance());
        this.tool.init(this.miPanel, this.applet, this.dd.config, this.res, this.mi);
      }
      catch (Throwable localThrowable2)
      {
        localThrowable2.printStackTrace();
      }
      mkTextPanel();
      this.tField.addKeyListener(this);
      enableEvents(9L);
      this.isStart = true;
      add(this.tPanel);
      add(this.miPanel);
      this.tField.requestFocus();
      iPG(true);
      pack();
      if (this.dd.config.getP("Client_Sound", false))
        loadSound();
      DCF localDCF = new DCF(this.res);
      localDCF.mShow();
      String str4 = localDCF.mGetHandle();
      if (str4.length() <= 0)
      {
        mExit();
        return;
      }
      this.dd.strName = str4;
      this.dd.config.put("chat_password", localDCF.mGetPass());
      this.dd.start();
      addInOut(str4, true);
    }
    catch (Throwable localThrowable1)
    {
      localThrowable1.printStackTrace();
    }
  }

  private synchronized void rPack()
  {
    Dimension localDimension = getSize();
    this.dPack.setSize(localDimension);
    setVisible(false);
    int i = this.iGap;
    int j = (int)(localDimension.height * (this.iCenter / 100.0F));
    if (this.miPanel != null)
      this.miPanel.setBounds(0, 0, localDimension.width, j);
    if (this.tool != null)
    {
      this.tool.pack();
      if ((this.tPanel != null) && (this.tPanel.getParent() == this))
      {
        int k = 0;
        this.tPanel.setBounds(k, j + i, localDimension.width - k, localDimension.height - (j + i));
        validate();
      }
    }
    this.mi.resetGraphics();
    setVisible(true);
  }

  public void run()
  {
    try
    {
      switch (Thread.currentThread().getName().charAt(0))
      {
      case 'i':
        rInit();
        break;
      case 'p':
        rPack();
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void scroll(boolean paramBoolean, int paramInt1, int paramInt2)
  {
    LComponent[] arrayOfLComponent = this.tool.getCs();
    int i = arrayOfLComponent.length;
    Point localPoint2 = this.mi.getLocation();
    int n = localPoint2.x + this.mi.getGapX();
    int i1 = localPoint2.y + this.mi.getGapY();
    Dimension localDimension2 = this.mi.getSizeW();
    for (int i2 = 0; i2 < i; i2++)
    {
      LComponent localLComponent = arrayOfLComponent[i2];
      Point localPoint1 = localLComponent.getLocation();
      Dimension localDimension1 = localLComponent.getSizeW();
      if (((localPoint1.x + localDimension1.width <= localPoint2.x) || (localPoint1.y + localDimension1.height <= localPoint2.y) || (localPoint1.x >= localPoint2.x + localDimension2.width) || (localPoint1.y >= localPoint2.y + localDimension2.height)) && ((!localLComponent.isEscape) || (!localLComponent.isVisible())))
        continue;
      if (this.iScrollType == 0)
      {
        int j = localPoint1.x - n;
        int k = localPoint1.y - i1;
        int m = localDimension1.width;
        if (paramInt1 > 0)
          this.mi.m_paint(j - paramInt1, k, paramInt1, localDimension1.height);
        if (paramInt1 < 0)
          this.mi.m_paint(j + m, k, -paramInt1, localDimension1.height);
        m += Math.abs(paramInt1);
        if (paramInt2 < 0)
          this.mi.m_paint(j - paramInt1, k + localDimension1.height, m, -paramInt2);
        if (paramInt2 <= 0)
          continue;
        this.mi.m_paint(j - paramInt1, k - paramInt2, m, paramInt2);
      }
      else
      {
        boolean bool = localLComponent.isEscape;
        localLComponent.escape(paramBoolean);
        if (bool)
          continue;
        this.mi.m_paint(localPoint1.x - localPoint2.x, localPoint1.y - localPoint2.y, localDimension1.width, localDimension1.height);
      }
    }
  }

  public void send(M paramM)
  {
    this.dd.send(paramM);
  }

  public void setARGB(int paramInt)
  {
    paramInt &= 16777215;
    this.tool.selPix((this.mi.info.m.iLayer != 0) && (paramInt == 16777215));
    if ((this.mi.info.m.iPen != 4) && (this.mi.info.m.iPen != 5))
      this.tool.setARGB(this.mi.info.m.iAlpha << 24 | paramInt);
  }

  private void setDefComponent(Container paramContainer)
  {
    try
    {
      if (paramContainer == null)
        return;
      Color localColor1 = paramContainer.getForeground();
      Color localColor2 = paramContainer.getBackground();
      Component[] arrayOfComponent = paramContainer.getComponents();
      if (arrayOfComponent != null)
        for (int i = 0; i < arrayOfComponent.length; i++)
        {
          Component localComponent = arrayOfComponent[i];
          localComponent.setBackground(localColor2);
          localComponent.setForeground(localColor1);
          if (!(localComponent instanceof Container))
            continue;
          setDefComponent((Container)localComponent);
        }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void setLineSize(int paramInt)
  {
    this.tool.setLineSize(paramInt);
  }

  private void typed()
  {
    try
    {
      String str = this.tField.getText();
      if ((str == null) || (str.length() <= 0))
        return;
      this.tField.setText("");
      if (str.length() > 256)
      {
        this.mi.alert("longer it", false);
        return;
      }
      if (this.mi.info.m.isText())
      {
        this.mi.addText(str);
      }
      else
      {
        this.mgText.setData(0, 0, str);
        this.dd.send(this.mgText);
        str = this.dd.strName + '>' + str;
        this.tText.addText(str, true);
        dSound(1);
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  public void undo(boolean paramBoolean)
  {
  }

  public void update(Graphics paramGraphics)
  {
    paint(paramGraphics);
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_client.Pl
 * JD-Core Version:    0.6.0
 */