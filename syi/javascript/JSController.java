/* JSController - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.javascript;
import java.applet.Applet;
import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Method;
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
    private static final String STR_VERSION
	= "JavaScript Controller (C)shi-chan 2001";
    /*synthetic*/ static Class class$0;
    /*synthetic*/ static Class class$1;
    /*synthetic*/ static Class class$2;
    /*synthetic*/ static Class class$3;
    
    public JSController(Applet applet) throws IOException {
	System.out.println("JavaScript Controller (C)shi-chan 2001");
	this.applet = applet;
	setup();
    }
    
    public void alert(String string) throws IOException {
	callFunction("alert", new Object[] { string });
    }
    
    public String callFunction(String string, Object[] objects)
	throws IOException {
	try {
	    if (mCall == null) {
		JSController jscontroller_0_ = this;
		Class var_class = cJava;
		String string_1_ = "call";
		Class[] var_classes = new Class[2];
		int i = 0;
		Class var_class_2_ = class$0;
		if (var_class_2_ == null) {
		    Class var_class_3_;
		    try {
			var_class_3_ = Class.forName("java.lang.String");
		    } catch (ClassNotFoundException classnotfoundexception) {
			NoClassDefFoundError noclassdeffounderror
			    = new NoClassDefFoundError;
			((UNCONSTRUCTED)noclassdeffounderror)
			    .NoClassDefFoundError
			    (classnotfoundexception.getMessage());
			throw noclassdeffounderror;
		    }
		    var_class_2_ = class$0 = var_class_3_;
		}
		var_classes[i] = var_class_2_;
		int i_4_ = 1;
		Class var_class_5_ = class$1;
		if (var_class_5_ == null) {
		    Class var_class_6_;
		    try {
			var_class_6_ = Class.forName("[Ljava.lang.Object;");
		    } catch (ClassNotFoundException classnotfoundexception) {
			NoClassDefFoundError noclassdeffounderror
			    = new NoClassDefFoundError;
			((UNCONSTRUCTED)noclassdeffounderror)
			    .NoClassDefFoundError
			    (classnotfoundexception.getMessage());
			throw noclassdeffounderror;
		    }
		    var_class_5_ = class$1 = var_class_6_;
		}
		var_classes[i_4_] = var_class_5_;
		jscontroller_0_.mCall
		    = var_class.getMethod(string_1_, var_classes);
	    }
	    return mCall.invoke(oJava, new Object[] { string, objects })
		       .toString();
	} catch (Throwable throwable) {
	    throw new IOException("Can't call " + string);
	}
    }
    
    public boolean confirm(String string) throws IOException {
	return new Boolean
		   (callFunction("confirm", new Object[] { string }))
		   .booleanValue();
    }
    
    public String[][] dataLoad() throws IOException {
	Hashtable hashtable = new Hashtable();
	dataLoad(hashtable);
	int i = hashtable.size();
	String[][] strings = new String[i][2];
	int i_7_ = 0;
	Enumeration enumeration = hashtable.keys();
	while (enumeration.hasMoreElements()) {
	    Object object = enumeration.nextElement();
	    strings[i_7_][0] = (String) object;
	    strings[i_7_][1] = hashtable.get(object).toString();
	    i_7_++;
	}
	return strings;
    }
    
    public void dataLoad(Hashtable hashtable) throws IOException {
	String string = getProperty("document.cookie");
	int i = string.length();
	for (int i_8_ = 0; i_8_ < i; i_8_++) {
	    StringBuffer stringbuffer = new StringBuffer();
	    for (/**/; i_8_ < i; i_8_++) {
		char c = string.charAt(i_8_);
		if (c == '=') {
		    i_8_++;
		    break;
		}
		stringbuffer.append(c);
	    }
	    String string_9_ = unEscape(stringbuffer.toString());
	    stringbuffer = new StringBuffer();
	    for (/**/; i_8_ < i; i_8_++) {
		char c = string.charAt(i_8_);
		if (c == ';') {
		    i_8_++;
		    break;
		}
		stringbuffer.append(c);
	    }
	    String string_10_ = unEscape(stringbuffer.toString());
	    if (!string_9_.equals("expires"))
		hashtable.put(string_9_, string_10_);
	}
    }
    
    public void dataSave(String[][] strings, int i) throws IOException {
	try {
	    Hashtable hashtable = new Hashtable();
	    for (int i_11_ = 0; i_11_ < strings.length; i_11_++)
		hashtable.put(strings[i_11_][0], strings[i_11_][1]);
	    dataSave(hashtable, i);
	} catch (RuntimeException runtimeexception) {
	    throw new RuntimeException("Format error String[*][2]");
	}
    }
    
    public void dataSave(Hashtable hashtable, int i) throws IOException {
	if (hashtable.size() > 0) {
	    StringBuffer stringbuffer = new StringBuffer();
	    Enumeration enumeration = hashtable.keys();
	    while (enumeration.hasMoreElements()) {
		String string = (String) enumeration.nextElement();
		stringbuffer.append(escape(string));
		stringbuffer.append('=');
		stringbuffer.append(escape((String) hashtable.get(string)));
		stringbuffer.append("; ");
	    }
	    if (dateFormat == null)
		dateFormat = (new SimpleDateFormat
			      ("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' GMT'",
			       Locale.ENGLISH));
	    stringbuffer.append
		("expires="
		 + dateFormat.format(new Date(System.currentTimeMillis()
					      + (long) (86400000 * i)))
		 + "; ");
	    setProperty("document.cookie", stringbuffer.toString());
	}
    }
    
    public String escape(String string) {
	try {
	    return callFunction("escape", new Object[] { string });
	} catch (IOException ioexception) {
	    return "";
	}
    }
    
    private final Object getArray(Object object, Object[] objects)
	throws IOException {
	if (mGetSlot == null) {
	    try {
		mGetSlot
		    = cJava.getMethod("getSlot", new Class[] { Integer.TYPE });
	    } catch (NoSuchMethodException nosuchmethodexception) {
		throw new IOException("No support JavaScript");
	    }
	}
	Object[] objects_12_ = new Object[1];
	String string = (String) objects[0];
	int i = string.indexOf('[');
	String string_13_ = string.substring(0, i);
	objects_12_[0] = string_13_;
	object = getMember(object, objects_12_);
	try {
	    objects_12_[0]
		= Integer.decode(string.substring(i + 1, string.indexOf(']')));
	    return mGetSlot.invoke(object, objects_12_);
	} catch (IllegalAccessException illegalaccessexception) {
	    /* empty */
	} catch (java.lang.reflect.InvocationTargetException invocationtargetexception) {
	    /* empty */
	}
	throw new IOException("No support JavaScript");
    }
    
    private final Object getEndObject
	(String string, StringBuffer stringbuffer) throws IOException {
	if (isCash) {
	    int i = string.lastIndexOf('.');
	    String string_14_ = i < 0 ? string : string.substring(0, i);
	    Object object = tables.get(string_14_);
	    if (object != null)
		return object;
	}
	Object object = oJava;
	int i = 0;
	boolean bool = false;
	string.length();
	Object[] objects = new Object[1];
	int i_15_;
	for (/**/; (i_15_ = string.indexOf('.', i)) >= 0; i = i_15_ + 1) {
	    String string_16_ = string.substring(i, i_15_);
	    objects[0] = string_16_;
	    object = getMember(object, objects);
	}
	stringbuffer.append(string.substring(i));
	return object;
    }
    
    private final Object getMember(Object object, Object[] objects)
	throws IOException {
	String string = (String) objects[0];
	if (mGetMember == null) {
	    try {
		JSController jscontroller_17_ = this;
		Class var_class = cJava;
		String string_18_ = "getMember";
		Class[] var_classes = new Class[1];
		int i = 0;
		Class var_class_19_ = class$0;
		if (var_class_19_ == null) {
		    Class var_class_20_;
		    try {
			var_class_20_ = Class.forName("java.lang.String");
		    } catch (ClassNotFoundException classnotfoundexception) {
			NoClassDefFoundError noclassdeffounderror
			    = new NoClassDefFoundError;
			((UNCONSTRUCTED)noclassdeffounderror)
			    .NoClassDefFoundError
			    (classnotfoundexception.getMessage());
			throw noclassdeffounderror;
		    }
		    var_class_19_ = class$0 = var_class_20_;
		}
		var_classes[i] = var_class_19_;
		jscontroller_17_.mGetMember
		    = var_class.getMethod(string_18_, var_classes);
	    } catch (NoSuchMethodException nosuchmethodexception) {
		throw new IOException("No support JavaScript");
	    }
	}
	try {
	    if (string.indexOf('[') >= 0)
		return getArray(object, objects);
	    return mGetMember.invoke(object, objects);
	} catch (Throwable throwable) {
	    throw new IOException("can't get " + objects[0]);
	}
    }
    
    public String getProperty(String string) throws IOException {
	StringBuffer stringbuffer = new StringBuffer();
	Object object = getEndObject(string, stringbuffer);
	return getMember(object, new Object[] { stringbuffer.toString() })
		   .toString();
    }
    
    public void openWindow(String string, String string_21_,
			   Rectangle rectangle, boolean bool, boolean bool_22_,
			   boolean bool_23_, boolean bool_24_,
			   boolean bool_25_) throws IOException {
	StringBuffer stringbuffer = new StringBuffer();
	if (string == null)
	    string = "";
	if (string_21_ == null || string_21_.length() == 0)
	    string_21_ = "sub";
	if (!bool_25_ && rectangle != null)
	    stringbuffer.append("left=" + rectangle.x + ",top=" + rectangle.y
				+ ",width=" + rectangle.width + ",height="
				+ rectangle.height + ",");
	int i = bool ? 1 : 0;
	stringbuffer.append("toolbar=" + i + ",location" + i + ",directories="
			    + i + ",status=" + i + ",menubar=" + i + ',');
	i = bool_22_ ? 1 : 0;
	stringbuffer.append("titlebar=" + i + ',');
	i = bool_23_ ? 1 : 0;
	stringbuffer.append("scrollbars=" + i + ',');
	i = bool_24_ ? 1 : 0;
	stringbuffer.append("resizable=" + i + ',');
	i = bool_25_ ? 1 : 0;
	stringbuffer.append("fullscreen=" + i);
	runScript(string_21_ + "=window.open('" + string + "','" + string_21_
		  + "','" + stringbuffer.toString() + "');");
    }
    
    public final String runScript(String string) throws IOException {
	if (mEval == null) {
	    try {
		JSController jscontroller_26_ = this;
		Class var_class = cJava;
		String string_27_ = "eval";
		Class[] var_classes = new Class[1];
		int i = 0;
		Class var_class_28_ = class$0;
		if (var_class_28_ == null) {
		    Class var_class_29_;
		    try {
			var_class_29_ = Class.forName("java.lang.String");
		    } catch (ClassNotFoundException classnotfoundexception) {
			NoClassDefFoundError noclassdeffounderror
			    = new NoClassDefFoundError;
			((UNCONSTRUCTED)noclassdeffounderror)
			    .NoClassDefFoundError
			    (classnotfoundexception.getMessage());
			throw noclassdeffounderror;
		    }
		    var_class_28_ = class$0 = var_class_29_;
		}
		var_classes[i] = var_class_28_;
		jscontroller_26_.mEval
		    = var_class.getMethod(string_27_, var_classes);
	    } catch (NoSuchMethodException nosuchmethodexception) {
		throw new IOException("No support JavaScript");
	    }
	}
	try {
	    Object object = mEval.invoke(oJava, new Object[] { string });
	    return object == null ? "" : object.toString();
	} catch (Throwable throwable) {
	    throw new IOException("ScriptError");
	}
    }
    
    protected String scriptString(String string) {
	StringBuffer stringbuffer = new StringBuffer();
	int i = string.length();
	for (int i_30_ = 0; i_30_ < i; i_30_++) {
	    char c = string.charAt(i_30_);
	    switch (c) {
	    case '\'':
		stringbuffer.append('\\');
		break;
	    case '\"':
		stringbuffer.append('\\');
		break;
	    case '\\':
		stringbuffer.append('\\');
		break;
	    case '\r':
		stringbuffer.append('\\');
		c = 'r';
		break;
	    case '\n':
		stringbuffer.append('\\');
		c = 'n';
		break;
	    }
	    stringbuffer.append(c);
	}
	return stringbuffer.toString();
    }
    
    public synchronized void setCash(boolean bool) {
	isCash = bool;
	if (bool)
	    tables = new Hashtable();
	else
	    tables = null;
    }
    
    public void setProperty(String string, Object object) throws IOException {
	if (mSetMember == null) {
	    try {
		JSController jscontroller_31_ = this;
		Class var_class = cJava;
		String string_32_ = "setMember";
		Class[] var_classes = new Class[2];
		int i = 0;
		Class var_class_33_ = class$0;
		if (var_class_33_ == null) {
		    Class var_class_34_;
		    try {
			var_class_34_ = Class.forName("java.lang.String");
		    } catch (ClassNotFoundException classnotfoundexception) {
			NoClassDefFoundError noclassdeffounderror
			    = new NoClassDefFoundError;
			((UNCONSTRUCTED)noclassdeffounderror)
			    .NoClassDefFoundError
			    (classnotfoundexception.getMessage());
			throw noclassdeffounderror;
		    }
		    var_class_33_ = class$0 = var_class_34_;
		}
		var_classes[i] = var_class_33_;
		int i_35_ = 1;
		Class var_class_36_ = class$2;
		if (var_class_36_ == null) {
		    Class var_class_37_;
		    try {
			var_class_37_ = Class.forName("java.lang.Object");
		    } catch (ClassNotFoundException classnotfoundexception) {
			NoClassDefFoundError noclassdeffounderror
			    = new NoClassDefFoundError;
			((UNCONSTRUCTED)noclassdeffounderror)
			    .NoClassDefFoundError
			    (classnotfoundexception.getMessage());
			throw noclassdeffounderror;
		    }
		    var_class_36_ = class$2 = var_class_37_;
		}
		var_classes[i_35_] = var_class_36_;
		jscontroller_31_.mSetMember
		    = var_class.getMethod(string_32_, var_classes);
		JSController jscontroller_38_ = this;
		Class var_class_39_ = cJava;
		String string_40_ = "setSlot";
		Class[] var_classes_41_ = { Integer.TYPE, null };
		int i_42_ = 1;
		Class var_class_43_ = class$2;
		if (var_class_43_ == null) {
		    Class var_class_44_;
		    try {
			var_class_44_ = Class.forName("java.lang.Object");
		    } catch (ClassNotFoundException classnotfoundexception) {
			NoClassDefFoundError noclassdeffounderror
			    = new NoClassDefFoundError;
			((UNCONSTRUCTED)noclassdeffounderror)
			    .NoClassDefFoundError
			    (classnotfoundexception.getMessage());
			throw noclassdeffounderror;
		    }
		    var_class_43_ = class$2 = var_class_44_;
		}
		var_classes_41_[i_42_] = var_class_43_;
		jscontroller_38_.mSetSlot
		    = var_class_39_.getMethod(string_40_, var_classes_41_);
	    } catch (NoSuchMethodException nosuchmethodexception) {
		throw new IOException("Not found Java script");
	    }
	}
	StringBuffer stringbuffer = new StringBuffer();
	Object object_45_ = getEndObject(string, stringbuffer);
	String string_46_ = stringbuffer.toString();
	Object object_47_ = null;
	try {
	    if (string_46_.indexOf('[') >= 0)
		mSetSlot.invoke(object_45_,
				new Object[] { string_46_, object });
	    else
		mSetMember.invoke(object_45_,
				  new Object[] { string_46_, object });
	} catch (Throwable throwable) {
	    throw new IOException("can't set " + string_46_);
	}
    }
    
    public final synchronized void setup() throws IOException {
	try {
	    if (cJava == null) {
		cJava = Class.forName("netscape.javascript.JSObject");
		JSController jscontroller_48_ = this;
		Class var_class = cJava;
		String string = "getWindow";
		Class[] var_classes = new Class[1];
		int i = 0;
		Class var_class_49_ = class$3;
		if (var_class_49_ == null) {
		    Class var_class_50_;
		    try {
			var_class_50_ = Class.forName("java.applet.Applet");
		    } catch (ClassNotFoundException classnotfoundexception) {
			NoClassDefFoundError noclassdeffounderror
			    = new NoClassDefFoundError;
			((UNCONSTRUCTED)noclassdeffounderror)
			    .NoClassDefFoundError
			    (classnotfoundexception.getMessage());
			throw noclassdeffounderror;
		    }
		    var_class_49_ = class$3 = var_class_50_;
		}
		var_classes[i] = var_class_49_;
		jscontroller_48_.mGet
		    = var_class.getMethod(string, var_classes);
	    }
	    oJava = mGet.invoke(null, new Object[] { applet });
	} catch (Throwable throwable) {
	    oJava = null;
	    throw new IOException("No support JavaScript");
	}
    }
    
    public void showDocument(String string, String string_51_,
			     String string_52_) throws IOException {
	if (string == null || string.equals("_self")
	    || string.equals("_parent"))
	    string = "";
	if (!string.endsWith("document")) {
	    if (string.length() > 0)
		string = string.concat(".");
	    string = string.concat("document");
	}
	if (string_51_ == null || string_51_.length() == 0)
	    string_51_ = "text/plain";
	runScript("var doc=" + string + ".open('" + string_51_
		  + "');\ndoc.write('" + scriptString(string_52_)
		  + "');\ndoc.close();");
    }
    
    public String unEscape(String string) {
	try {
	    return callFunction("unescape", new Object[] { string });
	} catch (IOException ioexception) {
	    return "";
	}
    }
}
