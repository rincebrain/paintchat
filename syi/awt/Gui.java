package syi.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import syi.util.Io;
import syi.util.PProperties;

public class Gui extends Awt
{
  private static PProperties resource = null;

  public static File fileDialog(Window paramWindow, String paramString, boolean paramBoolean)
  {
    String str1;
    String str2;
    try
    {
      String str3;
      if (resource == null)
        str3 = (paramBoolean ? "書き込む" : "読みこむ") + "ファイルを選択してください";
      else
        str3 = resource.getString("Dialog." + (paramBoolean ? "Save" : "Load"));
      Frame localFrame = (paramWindow instanceof Frame) ? (Frame)paramWindow : Awt.getPFrame();
      FileDialog localFileDialog = new FileDialog(localFrame, str3, paramBoolean ? 1 : 0);
      if (paramString != null)
        localFileDialog.setFile(paramString);
      localFileDialog.setModal(true);
      localFileDialog.setVisible(true);
      str1 = localFileDialog.getDirectory();
      str2 = localFileDialog.getFile();
      if ((str2.equals("null")) || (str2.equals("null")))
        return null;
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
      return null;
    }
    return new File(str1, str2);
  }

  public static String getClipboard()
  {
    String str = null;
    try
    {
      StringSelection localStringSelection = new StringSelection("");
      Transferable localTransferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(localStringSelection);
      if (localTransferable != null)
        str = (String)localTransferable.getTransferData(DataFlavor.stringFlavor);
    }
    catch (Exception localException)
    {
      str = null;
    }
    return str == null ? "" : str;
  }

  public static void getDefSize(Component paramComponent)
  {
    Dimension localDimension = Toolkit.getDefaultToolkit().getScreenSize();
    paramComponent.setSize(localDimension.width / 2, localDimension.height / 2);
  }

  public static Point getScreenPos(Component paramComponent, Point paramPoint)
  {
    Point localPoint = paramComponent.getLocationOnScreen();
    localPoint.translate(paramPoint.x, paramPoint.y);
    return localPoint;
  }

  public static Point getScreenPos(MouseEvent paramMouseEvent)
  {
    Point localPoint = paramMouseEvent.getComponent().getLocationOnScreen();
    localPoint.translate(paramMouseEvent.getX(), paramMouseEvent.getY());
    return localPoint;
  }

  public static void giveDef(Component paramComponent)
  {
    if ((paramComponent instanceof Container))
    {
      Component[] arrayOfComponent = ((Container)paramComponent).getComponents();
      for (int i = 0; i < arrayOfComponent.length; i++)
      {
        if (arrayOfComponent[i] == null)
          continue;
        giveDef(arrayOfComponent[i]);
      }
    }
    Awt.getDef(paramComponent);
  }

  public static void pack(Container paramContainer)
  {
    Component[] arrayOfComponent = paramContainer.getComponents();
    if (arrayOfComponent != null)
      for (int i = 0; i < arrayOfComponent.length; i++)
        if ((arrayOfComponent[i] instanceof Container))
        {
          pack((Container)arrayOfComponent[i]);
        }
        else
        {
          if (arrayOfComponent[i].isValid())
            continue;
          arrayOfComponent[i].validate();
        }
    if (!paramContainer.isValid())
      paramContainer.validate();
  }

  public static boolean showDocument(String paramString, PProperties paramPProperties, Hashtable paramHashtable)
  {
    Runtime localRuntime = Runtime.getRuntime();
    try
    {
      File localFile;
      if (paramString.startsWith("http://"))
      {
        localFile = new File(Io.getCurrent(), "cnf/dummy.html");
        localObject = new FileOutputStream(localFile);
        ((FileOutputStream)localObject).write(("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0;URL=" + paramString + "\"></head></html>").getBytes());
        ((OutputStream)localObject).flush();
        ((FileOutputStream)localObject).close();
      }
      else
      {
        localFile = new File(paramString);
      }
      Object localObject = localFile.getCanonicalPath();
      String str1 = "App.BrowserPath";
      if (Awt.isWin())
        try
        {
          localRuntime.exec(new String[] { paramPProperties.getString(str1, "explorer"), localObject });
          return true;
        }
        catch (IOException localIOException)
        {
        }
      String str2 = paramPProperties.getString(str1);
      if (str2.length() <= 0)
      {
        if (MessageBox.confirm("NeedBrowser", "Option"))
          str2 = fileDialog(Awt.getPFrame(), "", false).getCanonicalPath();
        else
          str2 = "false";
        paramPProperties.put(str1, str2);
      }
      if ((str2 == null) || (str2.length() <= 0) || (str2.equalsIgnoreCase("false")))
        return false;
      localRuntime.exec(new String[] { str2, localObject });
      return true;
    }
    catch (Throwable localThrowable)
    {
      System.out.println(localThrowable);
    }
    return false;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.Gui
 * JD-Core Version:    0.6.0
 */