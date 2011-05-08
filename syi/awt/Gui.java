package syi.awt;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Hashtable;
import syi.util.Io;
import syi.util.PProperties;

// Referenced classes of package syi.awt:
//            Awt, MessageBox

public class Gui extends Awt
{

    private static PProperties resource = null;

    public Gui()
    {
    }

    public static File fileDialog(Window window, String s, boolean flag)
    {
        String s1;
        String s2;
        try
        {
            String s3;
            if(resource == null)
            {
                s3 = (flag ? "\u66F8\u304D\u8FBC\u3080" : "\u8AAD\u307F\u3053\u3080") + "\u30D5\u30A1\u30A4\u30EB\u3092\u9078\u629E\u3057\u3066\u304F\u3060\u3055\u3044";
            } else
            {
                s3 = resource.getString("Dialog." + (flag ? "Save" : "Load"));
            }
            Frame frame = (window instanceof Frame) ? (Frame)window : Awt.getPFrame();
            FileDialog filedialog = new FileDialog(frame, s3, flag ? 1 : 0);
            if(s != null)
            {
                filedialog.setFile(s);
            }
            filedialog.setModal(true);
            filedialog.setVisible(true);
            s1 = filedialog.getDirectory();
            s2 = filedialog.getFile();
            if(s2.equals("null") || s2.equals("null"))
            {
                return null;
            }
        }
        catch(RuntimeException runtimeexception)
        {
            runtimeexception.printStackTrace();
            return null;
        }
        return new File(s1, s2);
    }

    public static String getClipboard()
    {
        String s = null;
        try
        {
            StringSelection stringselection = new StringSelection("");
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(stringselection);
            if(transferable != null)
            {
                s = (String)transferable.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch(Exception _ex)
        {
            s = null;
        }
        return s != null ? s : "";
    }

    public static void getDefSize(Component component)
    {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        component.setSize(dimension.width / 2, dimension.height / 2);
    }

    public static Point getScreenPos(Component component, Point point)
    {
        Point point1 = component.getLocationOnScreen();
        point1.translate(point.x, point.y);
        return point1;
    }

    public static Point getScreenPos(MouseEvent mouseevent)
    {
        Point point = mouseevent.getComponent().getLocationOnScreen();
        point.translate(mouseevent.getX(), mouseevent.getY());
        return point;
    }

    public static void giveDef(Component component)
    {
        if(component instanceof Container)
        {
            Component acomponent[] = ((Container)component).getComponents();
            for(int i = 0; i < acomponent.length; i++)
            {
                if(acomponent[i] != null)
                {
                    giveDef(acomponent[i]);
                }
            }

        }
        Awt.getDef(component);
    }

    public static void pack(Container container)
    {
        Component acomponent[] = container.getComponents();
        if(acomponent != null)
        {
            for(int i = 0; i < acomponent.length; i++)
            {
                if(acomponent[i] instanceof Container)
                {
                    pack((Container)acomponent[i]);
                } else
                if(!acomponent[i].isValid())
                {
                    acomponent[i].validate();
                }
            }

        }
        if(!container.isValid())
        {
            container.validate();
        }
    }

    public static boolean showDocument(String s, PProperties pproperties, Hashtable hashtable)
    {
        Runtime runtime = Runtime.getRuntime();
        try
        {
            File file;
            if(s.startsWith("http://"))
            {
                file = new File(Io.getCurrent(), "cnf/dummy.html");
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                fileoutputstream.write(("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0;URL=" + s + "\"></head></html>").getBytes());
                fileoutputstream.flush();
                fileoutputstream.close();
            } else
            {
                file = new File(s);
            }
            String s1 = file.getCanonicalPath();
            String s2 = "App.BrowserPath";
            if(Awt.isWin())
            {
                try
                {
                    runtime.exec(new String[] {
                        pproperties.getString(s2, "explorer"), s1
                    });
                    return true;
                }
                catch(IOException _ex) { }
            }
            String s3 = pproperties.getString(s2);
            if(s3.length() <= 0)
            {
                if(MessageBox.confirm("NeedBrowser", "Option"))
                {
                    s3 = fileDialog(Awt.getPFrame(), "", false).getCanonicalPath();
                } else
                {
                    s3 = "false";
                }
                pproperties.put(s2, s3);
            }
            if(s3 == null || s3.length() <= 0 || s3.equalsIgnoreCase("false"))
            {
                return false;
            } else
            {
                runtime.exec(new String[] {
                    s3, s1
                });
                return true;
            }
        }
        catch(Throwable throwable)
        {
            System.out.println(throwable);
        }
        return false;
    }

}
