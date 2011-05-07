package syi.awt;

import java.awt.Image;
import java.awt.Point;
import java.util.Hashtable;

public class HelpWindowContent
{
  public Point point;
  public String string = null;
  public Image image = null;
  boolean isResource = false;
  public Hashtable res;
  public int timeStart = 2000;
  public int timeEnd = 15000;
  private boolean isEnableVisited = false;
  private boolean isVisit = false;

  public HelpWindowContent(Image paramImage, String paramString, boolean paramBoolean, Point paramPoint, Hashtable paramHashtable)
  {
    this.point = paramPoint;
    this.string = paramString;
    this.res = paramHashtable;
    this.isResource = paramBoolean;
    this.image = paramImage;
  }

  public HelpWindowContent(String paramString, boolean paramBoolean, Point paramPoint, Hashtable paramHashtable)
  {
    this.point = paramPoint;
    this.string = paramString;
    this.res = paramHashtable;
    this.isResource = paramBoolean;
  }

  public String getText()
  {
    if ((this.string == null) || (this.string.length() == 0))
      return "";
    if (this.isResource)
    {
      String str = (String)this.res.get(this.string + "_Com");
      return str == null ? this.res.get(this.string).toString() : str;
    }
    return this.string;
  }

  public boolean isVisible(boolean paramBoolean)
  {
    return !this.isEnableVisited ? paramBoolean : this.isVisit;
  }

  public void setText(String paramString, boolean paramBoolean)
  {
    this.string = paramString;
    this.isResource = paramBoolean;
  }

  public void setVisit(boolean paramBoolean)
  {
    this.isEnableVisited = true;
    this.isVisit = paramBoolean;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.awt.HelpWindowContent
 * JD-Core Version:    0.6.0
 */