/* Vector2 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;

public class Vector2
{
    private Object[] elements = null;
    private int seek = 0;
    private float sizeAdd = 1.5F;
    private int sizeDefault = 25;
    
    public Vector2() {
	this(10);
    }
    
    public Vector2(int i) {
	i = i <= 0 ? 50 : i;
	sizeDefault = i;
	elements = new Object[i];
    }
    
    public final synchronized void add(Object[] objects) {
	if (objects.length + seek >= elements.length)
	    moreData(objects.length + seek);
	System.arraycopy(objects, 0, elements, seek, objects.length);
	seek += objects.length;
    }
    
    public final synchronized void add(Object object) {
	if (seek >= elements.length)
	    moreData(seek + 1);
	elements[seek++] = object;
    }
    
    public final synchronized void copy(Object[] objects, int i, int i_0_,
					int i_1_) {
	i_1_ = i + i_1_ > seek ? seek - i : i_1_;
	if (i_1_ > 0)
	    System.arraycopy(elements, i, objects, i_0_, i_1_);
    }
    
    public final synchronized void copy(Vector2 vector2_2_) {
	int i = seek;
	for (int i_3_ = 0; i_3_ < i; i_3_++)
	    vector2_2_.add(elements[i_3_]);
    }
    
    public final synchronized void gc() {
	int i = Math.max(seek, sizeDefault) + 3;
	if (elements.length > i) {
	    Object[] objects = new Object[i];
	    System.arraycopy(elements, 0, objects, 0, seek);
	    elements = objects;
	}
    }
    
    public final synchronized Object get(int i) {
	return i >= seek ? null : elements[i];
    }
    
    private final void moreData(int i) {
	Object[] objects
	    = new Object[i + Math.max((int) ((float) i * sizeAdd), 1)];
	System.arraycopy(elements, 0, objects, 0, elements.length);
	elements = objects;
    }
    
    public final synchronized void remove(int i) {
	i = Math.min(i, seek);
	if (i > 0) {
	    if (seek != i)
		System.arraycopy(elements, i, elements, 0, seek - i);
	    for (int i_4_ = seek - i; i_4_ < seek; i_4_++)
		elements[i_4_] = null;
	    seek -= i;
	}
    }
    
    public final synchronized void removeAll() {
	remove(seek);
    }
    
    public final int size() {
	return seek;
    }
}
