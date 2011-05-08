package syi.util;


public class Vector2
{

    private Object elements[];
    private int seek;
    private float sizeAdd;
    private int sizeDefault;

    public Vector2()
    {
        this(10);
    }

    public Vector2(int i)
    {
        elements = null;
        seek = 0;
        sizeAdd = 1.5F;
        sizeDefault = 25;
        i = i > 0 ? i : 50;
        sizeDefault = i;
        elements = new Object[i];
    }

    public final synchronized void add(Object aobj[])
    {
        if(aobj.length + seek >= elements.length)
        {
            moreData(aobj.length + seek);
        }
        System.arraycopy(((Object) (aobj)), 0, ((Object) (elements)), seek, aobj.length);
        seek += aobj.length;
    }

    public final synchronized void add(Object obj)
    {
        if(seek >= elements.length)
        {
            moreData(seek + 1);
        }
        elements[seek++] = obj;
    }

    public final synchronized void copy(Object aobj[], int i, int j, int k)
    {
        k = i + k <= seek ? k : seek - i;
        if(k <= 0)
        {
            return;
        } else
        {
            System.arraycopy(((Object) (elements)), i, ((Object) (aobj)), j, k);
            return;
        }
    }

    public final synchronized void copy(Vector2 vector2)
    {
        int i = seek;
        for(int j = 0; j < i; j++)
        {
            vector2.add(elements[j]);
        }

    }

    public final synchronized void gc()
    {
        int i = Math.max(seek, sizeDefault) + 3;
        if(elements.length > i)
        {
            Object aobj[] = new Object[i];
            System.arraycopy(((Object) (elements)), 0, ((Object) (aobj)), 0, seek);
            elements = aobj;
        }
    }

    public final synchronized Object get(int i)
    {
        return i < seek ? elements[i] : null;
    }

    private final void moreData(int i)
    {
        Object aobj[] = new Object[i + Math.max((int)((float)i * sizeAdd), 1)];
        System.arraycopy(((Object) (elements)), 0, ((Object) (aobj)), 0, elements.length);
        elements = aobj;
    }

    public final synchronized void remove(int i)
    {
        i = Math.min(i, seek);
        if(i <= 0)
        {
            return;
        }
        if(seek != i)
        {
            System.arraycopy(((Object) (elements)), i, ((Object) (elements)), 0, seek - i);
        }
        for(int j = seek - i; j < seek; j++)
        {
            elements[j] = null;
        }

        seek -= i;
    }

    public final synchronized void removeAll()
    {
        remove(seek);
    }

    public final int size()
    {
        return seek;
    }
}
