/* Gui - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.awt;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

import syi.util.Io;
import syi.util.PProperties;

public class Gui extends Awt
{
    private static PProperties resource = null;
    
    public static File fileDialog(Window window, String string, boolean bool) {
	String string_0_;
	String string_1_;
	try {
	    String string_2_;
	    if (resource == null)
		string_2_
		    = ((bool ? "\u66f8\u304d\u8fbc\u3080"
			: "\u8aad\u307f\u3053\u3080")
		       + "\u30d5\u30a1\u30a4\u30eb\u3092\u9078\u629e\u3057\u3066\u304f\u3060\u3055\u3044");
	    else
		string_2_
		    = resource.getString("Dialog." + (bool ? "Save" : "Load"));
	    Frame frame
		= window instanceof Frame ? (Frame) window : Awt.getPFrame();
	    FileDialog filedialog
		= new FileDialog(frame, string_2_, bool ? 1 : 0);
	    if (string != null)
		filedialog.setFile(string);
	    filedialog.setModal(true);
	    filedialog.setVisible(true);
	    string_0_ = filedialog.getDirectory();
	    string_1_ = filedialog.getFile();
	    if (string_1_.equals("null") || string_1_.equals("null"))
		return null;
	} catch (RuntimeException runtimeexception) {
	    runtimeexception.printStackTrace();
	    return null;
	}
	return new File(string_0_, string_1_);
    }
    
    public static String getClipboard() {
	String string = null;
	try {
	    StringSelection stringselection = new StringSelection("");
	    Transferable transferable
		= Toolkit.getDefaultToolkit().getSystemClipboard()
		      .getContents(stringselection);
	    if (transferable != null)
		string = (String) transferable.getTransferData(DataFlavor
							       .stringFlavor);
	} catch (Exception exception) {
	    string = null;
	}
	return string == null ? "" : string;
    }
    
    public static void getDefSize(Component component) {
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	component.setSize(dimension.width / 2, dimension.height / 2);
    }
    
    public static Point getScreenPos(Component component, Point point) {
	Point point_3_ = component.getLocationOnScreen();
	point_3_.translate(point.x, point.y);
	return point_3_;
    }
    
    public static Point getScreenPos(MouseEvent mouseevent) {
	Point point = mouseevent.getComponent().getLocationOnScreen();
	point.translate(mouseevent.getX(), mouseevent.getY());
	return point;
    }
    
    public static void giveDef(Component component) {
	if (component instanceof Container) {
	    Component[] components = ((Container) component).getComponents();
	    for (int i = 0; i < components.length; i++) {
		if (components[i] != null)
		    giveDef(components[i]);
	    }
	}
	Awt.getDef(component);
    }
    
    public static void pack(Container container) {
	Component[] components = container.getComponents();
	if (components != null) {
	    for (int i = 0; i < components.length; i++) {
		if (components[i] instanceof Container)
		    pack((Container) components[i]);
		else if (!components[i].isValid())
		    components[i].validate();
	    }
	}
	if (!container.isValid())
	    container.validate();
    }
    
    public static boolean showDocument(String string, PProperties pproperties,
				       Hashtable hashtable) {
	Runtime runtime = Runtime.getRuntime();
	try {
	    File file;
	    if (string.startsWith("http://")) {
		file = new File(Io.getCurrent(), "cnf/dummy.html");
		FileOutputStream fileoutputstream = new FileOutputStream(file);
		fileoutputstream.write
		    (("<html><head><META HTTP-EQUIV=\"Refresh\" CONTENT=\"0;URL="
		      + string + "\"></head></html>")
			 .getBytes());
		fileoutputstream.flush();
		fileoutputstream.close();
	    } else
		file = new File(string);
	    String string_4_ = file.getCanonicalPath();
	    String string_5_ = "App.BrowserPath";
	    if (Awt.isWin()) {
		try {
		    runtime.exec(new String[]
				 { pproperties.getString(string_5_,
							 "explorer"),
				   string_4_ });
		    return true;
		} catch (java.io.IOException ioexception) {
		    /* empty */
		}
	    }
	    String string_6_ = pproperties.getString(string_5_);
	    if (string_6_.length() <= 0) {
		if (MessageBox.confirm("NeedBrowser", "Option"))
		    string_6_ = fileDialog(Awt.getPFrame(), "", false)
				    .getCanonicalPath();
		else
		    string_6_ = "false";
		pproperties.put(string_5_, string_6_);
	    }
	    if (string_6_ == null || string_6_.length() <= 0
		|| string_6_.equalsIgnoreCase("false"))
		return false;
	    runtime.exec(new String[] { string_6_, string_4_ });
	    return true;
	} catch (Throwable throwable) {
	    System.out.println(throwable);
	    return false;
	}
    }
}
