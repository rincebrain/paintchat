/* VectorBin - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;

public class VectorBin
{
    private byte[][] elements = null;
    private int seek = 0;
    private float sizeAdd = 1.5F;
    private int sizeDefault = 25;
    
    public VectorBin() {
	this(10);
    }
    
    public VectorBin(int i) {
	i = i <= 0 ? 50 : i;
	sizeDefault = i;
	elements = new byte[i][];
    }
    
    public final synchronized void add(byte[] is) {
	if (seek >= elements.length)
	    moreData(seek + 1);
	elements[seek++] = is;
    }
    
    public final synchronized void copy(byte[][] is, int i, int i_0_,
					int i_1_) {
	i_1_ = i + i_1_ > seek ? seek - i : i_1_;
	if (i_1_ > 0)
	    System.arraycopy(elements, i, is, i_0_, i_1_);
    }
    
    public final synchronized void copy(VectorBin vectorbin_2_) {
	for (int i = 0; i < seek; i++)
	    vectorbin_2_.add(elements[i]);
    }
    
    public final synchronized void gc() {
	int i = Math.max(seek, sizeDefault) + 3;
	if (elements.length > i) {
	    byte[][] is = new byte[i][];
	    System.arraycopy(elements, 0, is, 0, seek);
	    elements = is;
	}
    }
    
    public final synchronized byte[] get(int i) {
	return i >= seek ? null : elements[i];
    }
    
    public synchronized int getSizeBytes() {
	int i = 0;
	byte[][] is = elements;
	int i_3_ = seek;
	for (int i_4_ = 0; i_4_ < i_3_; i_4_++)
	    i += is[i_4_].length;
	return i;
    }
    
    private final void moreData(int i) {
	byte[][] is = new byte[i + Math.max((int) ((float) i * sizeAdd), 1)][];
	System.arraycopy(elements, 0, is, 0, elements.length);
	elements = is;
    }
    
    public final synchronized void remove(int i) {
	i = Math.min(i, seek);
	if (i > 0) {
	    if (seek != i)
		System.arraycopy(elements, i, elements, 0, seek - i);
	    for (int i_5_ = seek - i; i_5_ < seek; i_5_++)
		elements[i_5_] = null;
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
