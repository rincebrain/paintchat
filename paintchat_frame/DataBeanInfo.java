package paintchat_frame;

import java.awt.Image;
import java.beans.*;
import java.lang.reflect.Method;

public class DataBeanInfo extends SimpleBeanInfo
{

    static Class class$1; /* synthetic field */

    public DataBeanInfo()
    {
    }

    public static Method findMethod(Class class1, String s, int i)
    {
        try
        {
            Method amethod[] = class1.getMethods();
            for(int j = 0; j < amethod.length; j++)
            {
                Method method = amethod[j];
                if(method.getParameterTypes().length == i && method.getName().equals(s))
                {
                    return method;
                }
            }

        }
        catch(Throwable _ex)
        {
            return null;
        }
        return null;
    }

    public BeanInfo[] getAdditionalBeanInfo()
    {
        BeanInfo beaninfo = null;
        Class class1;
        try
        {
            class1 = getBeanDescriptor().getBeanClass().getSuperclass();
        }
        catch(Throwable _ex)
        {
            return null;
        }
        try
        {
            beaninfo = Introspector.getBeanInfo(class1);
        }
        catch(IntrospectionException _ex) { }
        if(beaninfo != null)
        {
            BeanInfo abeaninfo[] = new BeanInfo[1];
            abeaninfo[0] = beaninfo;
            return abeaninfo;
        } else
        {
            return null;
        }
    }

    public static Class getBeanClass()
    {
        return paintchat_frame.Data.class;
    }

    public static String getBeanClassName()
    {
        return "paintchat_frame.Data";
    }

    public BeanDescriptor getBeanDescriptor()
    {
        BeanDescriptor beandescriptor = null;
        try
        {
            beandescriptor = new BeanDescriptor(paintchat_frame.Data.class);
            beandescriptor.setDisplayName("paintchat_frame.Data");
            beandescriptor.setShortDescription("paintchat_frame.Data");
        }
        catch(Throwable _ex) { }
        return beandescriptor;
    }

    public EventSetDescriptor[] getEventSetDescriptors()
    {
        try
        {
            EventSetDescriptor aeventsetdescriptor[] = new EventSetDescriptor[0];
            return aeventsetdescriptor;
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
        return null;
    }

    public Image getIcon(int i)
    {
        Image image = null;
        if(i == 1)
        {
            image = loadImage("/cnf/icon.gif");
        }
        if(i == 3)
        {
            image = loadImage("/cnf/icon.gif");
        }
        return image;
    }

    public MethodDescriptor[] getMethodDescriptors()
    {
        try
        {
            MethodDescriptor amethoddescriptor[] = new MethodDescriptor[0];
            return amethoddescriptor;
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
        return null;
    }

    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor apropertydescriptor[] = {
                isNativeWindowsPropertyDescriptor()
            };
            return apropertydescriptor;
        }
        catch(Throwable throwable)
        {
            handleException(throwable);
        }
        return null;
    }

    private void handleException(Throwable throwable)
    {
    }

    public PropertyDescriptor isNativeWindowsPropertyDescriptor()
    {
        PropertyDescriptor propertydescriptor = null;
        try
        {
            try
            {
                Method method = null;
                try
                {
                    Class aclass[] = new Class[0];
                    method = getBeanClass().getMethod("getIsNativeWindows", aclass);
                }
                catch(Throwable throwable2)
                {
                    handleException(throwable2);
                    method = findMethod(getBeanClass(), "getIsNativeWindows", 0);
                }
                Method method1 = null;
                try
                {
                    Class aclass1[] = {
                        Boolean.TYPE
                    };
                    method1 = getBeanClass().getMethod("setIsNativeWindows", aclass1);
                }
                catch(Throwable throwable3)
                {
                    handleException(throwable3);
                    method1 = findMethod(getBeanClass(), "setIsNativeWindows", 1);
                }
                propertydescriptor = new PropertyDescriptor("isNativeWindows", method, method1);
            }
            catch(Throwable throwable)
            {
                handleException(throwable);
                propertydescriptor = new PropertyDescriptor("isNativeWindows", getBeanClass());
            }
        }
        catch(Throwable throwable1)
        {
            handleException(throwable1);
        }
        return propertydescriptor;
    }
}
