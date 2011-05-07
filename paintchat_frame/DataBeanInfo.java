/* DataBeanInfo - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package paintchat_frame;
import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;

public class DataBeanInfo extends SimpleBeanInfo
{
    /*synthetic*/ static Class class$0;
    /*synthetic*/ static Class class$1;
    
    public static Method findMethod(Class var_class, String string, int i) {
	try {
	    Method[] methods = var_class.getMethods();
	    for (int i_0_ = 0; i_0_ < methods.length; i_0_++) {
		Method method = methods[i_0_];
		if (method.getParameterTypes().length == i
		    && method.getName().equals(string))
		    return method;
	    }
	} catch (Throwable throwable) {
	    return null;
	}
	return null;
    }
    
    public BeanInfo[] getAdditionalBeanInfo() {
	BeanInfo beaninfo = null;
	Class var_class;
	try {
	    var_class = getBeanDescriptor().getBeanClass().getSuperclass();
	} catch (Throwable throwable) {
	    return null;
	}
	try {
	    beaninfo = Introspector.getBeanInfo(var_class);
	} catch (java.beans.IntrospectionException introspectionexception) {
	    /* empty */
	}
	if (beaninfo != null) {
	    BeanInfo[] beaninfos = new BeanInfo[1];
	    beaninfos[0] = beaninfo;
	    return beaninfos;
	}
	return null;
    }
    
    public static Class getBeanClass() {
	Class var_class = class$0;
	if (var_class == null) {
	    Class var_class_1_;
	    try {
		var_class_1_ = Class.forName("paintchat_frame.Data");
	    } catch (ClassNotFoundException classnotfoundexception) {
		NoClassDefFoundError noclassdeffounderror
		    = new NoClassDefFoundError();
		((UNCONSTRUCTED)noclassdeffounderror)
		    .NoClassDefFoundError(classnotfoundexception.getMessage());
		throw noclassdeffounderror;
	    }
	    var_class = class$0 = var_class_1_;
	}
	return var_class;
    }
    
    public static String getBeanClassName() {
	return "paintchat_frame.Data";
    }
    
    public BeanDescriptor getBeanDescriptor() {
	BeanDescriptor beandescriptor = null;
	try {
	    BeanDescriptor beandescriptor_2_ = new BeanDescriptor();
	    Class var_class = class$0;
	    if (var_class == null) {
		Class var_class_3_;
		try {
		    var_class_3_ = Class.forName("paintchat_frame.Data");
		} catch (ClassNotFoundException classnotfoundexception) {
		    NoClassDefFoundError noclassdeffounderror
			= new NoClassDefFoundError();
		    ((UNCONSTRUCTED)noclassdeffounderror).NoClassDefFoundError
			(classnotfoundexception.getMessage());
		    throw noclassdeffounderror;
		}
		var_class = class$0 = var_class_3_;
	    }
	    ((UNCONSTRUCTED)beandescriptor_2_).BeanDescriptor(var_class);
	    beandescriptor = beandescriptor_2_;
	    beandescriptor.setDisplayName("paintchat_frame.Data");
	    beandescriptor.setShortDescription("paintchat_frame.Data");
	} catch (Throwable throwable) {
	    /* empty */
	}
	return beandescriptor;
    }
    
    public EventSetDescriptor[] getEventSetDescriptors() {
	try {
	    EventSetDescriptor[] eventsetdescriptors
		= new EventSetDescriptor[0];
	    return eventsetdescriptors;
	} catch (Throwable throwable) {
	    handleException(throwable);
	    return null;
	}
    }
    
    public Image getIcon(int i) {
	Image image = null;
	if (i == 1)
	    image = this.loadImage("/cnf/icon.gif");
	if (i == 3)
	    image = this.loadImage("/cnf/icon.gif");
	return image;
    }
    
    public MethodDescriptor[] getMethodDescriptors() {
	try {
	    MethodDescriptor[] methoddescriptors = new MethodDescriptor[0];
	    return methoddescriptors;
	} catch (Throwable throwable) {
	    handleException(throwable);
	    return null;
	}
    }
    
    public PropertyDescriptor[] getPropertyDescriptors() {
	try {
	    PropertyDescriptor[] propertydescriptors
		= { isNativeWindowsPropertyDescriptor() };
	    return propertydescriptors;
	} catch (Throwable throwable) {
	    handleException(throwable);
	    return null;
	}
    }
    
    private void handleException(Throwable throwable) {
	/* empty */
    }
    
    public PropertyDescriptor isNativeWindowsPropertyDescriptor() {
	PropertyDescriptor propertydescriptor = null;
	try {
	    try {
		Object object = null;
		Method method;
		try {
		    Class[] var_classes = new Class[0];
		    method = getBeanClass().getMethod("getIsNativeWindows",
						      var_classes);
		} catch (Throwable throwable) {
		    handleException(throwable);
		    method
			= findMethod(getBeanClass(), "getIsNativeWindows", 0);
		}
		Object object_4_ = null;
		Method method_5_;
		try {
		    Class[] var_classes = { Boolean.TYPE };
		    method_5_ = getBeanClass().getMethod("setIsNativeWindows",
							 var_classes);
		} catch (Throwable throwable) {
		    handleException(throwable);
		    method_5_
			= findMethod(getBeanClass(), "setIsNativeWindows", 1);
		}
		propertydescriptor = new PropertyDescriptor("isNativeWindows",
							    method, method_5_);
	    } catch (Throwable throwable) {
		handleException(throwable);
		propertydescriptor = new PropertyDescriptor("isNativeWindows",
							    getBeanClass());
	    }
	} catch (Throwable throwable) {
	    handleException(throwable);
	}
	return propertydescriptor;
    }
}
