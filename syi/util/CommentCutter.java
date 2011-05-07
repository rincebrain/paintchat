package syi.util;

import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

class CommentCutter
{
  private Reader In = null;
  private Writer Out = null;
  private int[][] flagLine = null;
  private int[][] flagWhole = null;
  private int[][] flagWholeEnd = null;
  private int[] countLine = null;
  private int[] countWhole = null;
  private int[] cash = new int[3];
  private int countCash = 0;
  private int oldCh = 32;

  public CommentCutter()
  {
  }

  public CommentCutter(Reader paramReader, Writer paramWriter)
  {
    this.In = paramReader;
    this.Out = paramWriter;
  }

  private void addDefault()
  {
    addLineFlag(new int[] { 47, 47 });
    addWholeFlag(new int[] { 47, 37 }, new int[] { 37, 47 });
  }

  public void addLineFlag(int[] paramArrayOfInt)
  {
    synchronized (this)
    {
      if (this.flagLine == null)
      {
        this.flagLine = new int[0];
        this.countLine = new int[0];
      }
      int[][] arrayOfInt = new int[this.flagLine.length + 1];
      int[] arrayOfInt1 = new int[this.flagLine.length + 1];
      for (int i = 0; i < this.flagLine.length - 1; i++)
        arrayOfInt[i] = this.flagLine[i];
      arrayOfInt[i] = paramArrayOfInt;
      this.flagLine = arrayOfInt;
      this.countLine = arrayOfInt1;
    }
  }

  public void addWholeFlag(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    synchronized (this)
    {
      if (this.flagWhole == null)
      {
        this.flagWhole = new int[0];
        this.flagWholeEnd = new int[0];
        this.countWhole = new int[0];
      }
      int[][] arrayOfInt1 = new int[this.flagWhole.length + 1];
      int[][] arrayOfInt2 = new int[this.flagWholeEnd.length + 1];
      int[] arrayOfInt = new int[this.countWhole.length + 1];
      for (int i = 0; i < this.flagLine.length - 1; i++)
      {
        arrayOfInt1[i] = this.flagWhole[i];
        arrayOfInt2[i] = this.flagWholeEnd[i];
      }
      arrayOfInt1[i] = paramArrayOfInt1;
      arrayOfInt2[i] = paramArrayOfInt2;
      this.flagWhole = arrayOfInt1;
      this.flagWholeEnd = arrayOfInt2;
      this.countWhole = arrayOfInt;
    }
  }

  private void cleanCounter()
  {
    for (int i = 0; i < this.countLine.length; i++)
      this.countLine[i] = 0;
    for (i = 0; i < this.countWhole.length; i++)
      this.countWhole[i] = 0;
    this.countCash = 0;
  }

  public void cut()
    throws IOException
  {
    synchronized (this)
    {
      try
      {
        if ((this.flagLine == null) && (this.flagWhole == null))
          addDefault();
        cleanCounter();
        findFlags();
      }
      catch (EOFException localEOFException)
      {
      }
      this.In.close();
      this.Out.flush();
      this.Out.close();
    }
  }

  private void cutArray(int[] paramArrayOfInt)
    throws IOException
  {
    int j = 0;
    int k = paramArrayOfInt.length;
    int i;
    while ((i = this.In.read()) != -1)
    {
      j = paramArrayOfInt[j] == i ? j + 1 : 0;
      if (j == k)
        break;
    }
  }

  private void cutCRLF()
    throws IOException
  {
    int i;
    while ((i = this.In.read()) != -1)
      if ((i == 13) || (i == 10))
        break;
  }

  private void findFlags()
    throws IOException
  {
    int i;
    while ((i = this.In.read()) != -1)
    {
      if (Character.isWhitespace((char)i))
        i = 32;
      if (!switchFlag(i))
        continue;
      out(i);
    }
  }

  private void inCash(int paramInt)
  {
    if (this.countCash >= this.cash.length)
    {
      int[] arrayOfInt = new int[this.cash.length];
      System.arraycopy(this.cash, 0, arrayOfInt, 0, this.cash.length);
      this.cash = arrayOfInt;
    }
    this.cash[(this.countCash++)] = paramInt;
  }

  public static void main(String[] paramArrayOfString)
  {
    try
    {
      String str1 = "c:\\Windows\\ﾃﾞｽｸﾄｯﾌﾟ\\";
      String str2 = "";
      FileDialog localFileDialog = new FileDialog(new Frame(), "コメントをカットするファイルを指定", 0);
      localFileDialog.setDirectory(str1);
      localFileDialog.setFile(str2);
      localFileDialog.show();
      str1 = localFileDialog.getDirectory();
      str2 = localFileDialog.getFile();
      if ((str1 == null) || (str1.equals("null")) || (str2 == null) || (str2.equals(null)))
        System.exit(0);
      File localFile1 = new File(str1, str2);
      localFileDialog.setMode(1);
      localFileDialog.setTitle("カットしたファイルを保存する場所を指定");
      localFileDialog.show();
      str1 = localFileDialog.getDirectory();
      str2 = localFileDialog.getFile();
      if ((str1 == null) || (str1.equals("null")) || (str2 == null) || (str2.equals(null)))
        System.exit(0);
      File localFile2 = new File(str1, str2);
      CommentCutter localCommentCutter = new CommentCutter(new BufferedReader(new InputStreamReader(new FileInputStream(localFile1), "JISAutoDetect")), new BufferedWriter(new OutputStreamWriter(new FileOutputStream(localFile2))));
      localCommentCutter.cut();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    System.exit(0);
  }

  private void out(int paramInt)
    throws IOException
  {
    if ((paramInt == 32) && (this.oldCh == 32))
      return;
    this.Out.write(paramInt);
    this.oldCh = paramInt;
  }

  private void outCash()
    throws IOException
  {
    for (int i = 0; i < this.countCash; i++)
      out(this.cash[i]);
    this.countCash = 0;
  }

  public void setInOut(Reader paramReader, Writer paramWriter)
  {
    synchronized (this)
    {
      this.Out = paramWriter;
      this.In = paramReader;
    }
  }

  private boolean switchFlag(int paramInt)
    throws IOException
  {
    int i = 1;
    for (int j = 0; j < this.flagLine.length; j++)
      if (this.flagLine[j][this.countLine[j]] == paramInt)
      {
        i = 0;
        this.countLine[j] += 1;
        if (this.countLine[j] < this.flagLine[j].length)
          continue;
        cleanCounter();
        cutCRLF();
        return false;
      }
      else
      {
        this.countLine[j] = 0;
      }
    if (i == 0)
      inCash(paramInt);
    else
      outCash();
    return i;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.CommentCutter
 * JD-Core Version:    0.6.0
 */