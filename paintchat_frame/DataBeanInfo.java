package paintchat_frame;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;

public class DataBeanInfo extends SimpleBeanInfo
{
  public static Method findMethod(Class paramClass, String paramString, int paramInt)
  {
    try
    {
      Method[] arrayOfMethod = paramClass.getMethods();
      for (int i = 0; i < arrayOfMethod.length; i++)
      {
        Method localMethod = arrayOfMethod[i];
        if ((localMethod.getParameterTypes().length == paramInt) && (localMethod.getName().equals(paramString)))
          return localMethod;
      }
    }
    catch (Throwable localThrowable)
    {
      return null;
    }
    return null;
  }

  public BeanInfo[] getAdditionalBeanInfo()
  {
    BeanInfo localBeanInfo = null;
    Class localClass;
    try
    {
      localClass = getBeanDescriptor().getBeanClass().getSuperclass();
    }
    catch (Throwable localThrowable)
    {
      return null;
    }
    try
    {
      localBeanInfo = Introspector.getBeanInfo(localClass);
    }
    catch (IntrospectionException localIntrospectionException)
    {
    }
    if (localBeanInfo != null)
    {
      BeanInfo[] arrayOfBeanInfo = new BeanInfo[1];
      arrayOfBeanInfo[0] = localBeanInfo;
      return arrayOfBeanInfo;
    }
    return null;
  }

  public static Class getBeanClass()
  {
    return Data.class;
  }

  public static String getBeanClassName()
  {
    return "paintchat_frame.Data";
  }

  public BeanDescriptor getBeanDescriptor()
  {
    BeanDescriptor localBeanDescriptor = null;
    try
    {
      localBeanDescriptor = new BeanDescriptor(Data.class);
      localBeanDescriptor.setDisplayName("paintchat_frame.Data");
      localBeanDescriptor.setShortDescription("paintchat_frame.Data");
    }
    catch (Throwable localThrowable)
    {
    }
    return localBeanDescriptor;
  }

  public EventSetDescriptor[] getEventSetDescriptors()
  {
    try
    {
      EventSetDescriptor[] arrayOfEventSetDescriptor = new EventSetDescriptor[0];
      return arrayOfEventSetDescriptor;
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
    return null;
  }

  public Image getIcon(int paramInt)
  {
    Image localImage = null;
    if (paramInt == 1)
      localImage = loadImage("/cnf/icon.gif");
    if (paramInt == 3)
      localImage = loadImage("/cnf/icon.gif");
    return localImage;
  }

  public MethodDescriptor[] getMethodDescriptors()
  {
    try
    {
      MethodDescriptor[] arrayOfMethodDescriptor = new MethodDescriptor[0];
      return arrayOfMethodDescriptor;
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
    return null;
  }

  public PropertyDescriptor[] getPropertyDescriptors()
  {
    try
    {
      PropertyDescriptor[] arrayOfPropertyDescriptor = { isNativeWindowsPropertyDescriptor() };
      return arrayOfPropertyDescriptor;
    }
    catch (Throwable localThrowable)
    {
      handleException(localThrowable);
    }
    return null;
  }

  private void handleException(Throwable paramThrowable)
  {
  }

  public PropertyDescriptor isNativeWindowsPropertyDescriptor()
  {
    PropertyDescriptor localPropertyDescriptor = null;
    try
    {
      try
      {
        Method localMethod1 = null;
        try
        {
          Class[] arrayOfClass1 = new Class[0];
          localMethod1 = getBeanClass().getMethod("getIsNativeWindows", arrayOfClass1);
        }
        catch (Throwable localThrowable3)
        {
          handleException(localThrowable3);
          localMethod1 = findMethod(getBeanClass(), "getIsNativeWindows", 0);
        }
        Method localMethod2 = null;
        try
        {
          Class[] arrayOfClass2 = { Boolean.TYPE };
          localMethod2 = getBeanClass().getMethod("setIsNativeWindows", arrayOfClass2);
        }
        catch (Throwable localThrowable4)
        {
          handleException(localThrowable4);
          localMethod2 = findMethod(getBeanClass(), "setIsNativeWindows", 1);
        }
        localPropertyDescriptor = new PropertyDescriptor("isNativeWindows", localMethod1, localMethod2);
      }
      catch (Throwable localThrowable1)
      {
        handleException(localThrowable1);
        localPropertyDescriptor = new PropertyDescriptor("isNativeWindows", getBeanClass());
      }
    }
    catch (Throwable localThrowable2)
    {
      handleException(localThrowable2);
    }
    return localPropertyDescriptor;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_frame.DataBeanInfo
 * JD-Core Version:    0.6.0
 */