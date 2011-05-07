/* ThreadPool - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package syi.util;

public class ThreadPool extends Thread
{
    private static Object lock = new Object();
    private static int threadLimit = 0;
    private static int priority = 1;
    private static ThreadPool[] pool = null;
    private int poolNumber;
    private boolean isLive = true;
    private boolean isEmpty = false;
    private Runnable runnable = null;
    
    private ThreadPool(Runnable runnable, String string, int i) {
	super(string);
	poolNumber = i;
	this.runnable = runnable;
	this.setPriority(priority);
	this.setDaemon(true);
	this.start();
    }
    
    protected void finalize() throws Throwable {
	kill();
    }
    
    public static int getCountOfSleeping() {
	object_0_ = object_1_;
	break while_7_;
    }
    
    public static int getCountOfWorking() {
	object_4_ = object_5_;
	break while_9_;
    }
    
    public void interrupt() {
	try {
	    ThreadPool threadpool_8_;
	    MONITORENTER (threadpool_8_ = this);
	    MISSING MONITORENTER
	    synchronized (threadpool_8_) {
		if (!isEmpty)
		    super.interrupt();
	    }
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	}
    }
    
    private void kill() {
	try {
	    if (pool != null) {
		Object object;
		MONITORENTER (object = lock);
		MISSING MONITORENTER
		synchronized (object) {
		    if (isLive) {
			pool[poolNumber] = null;
			runnable = null;
		    }
		    isLive = false;
		    if (isEmpty)
			this.notify();
		    else
			interrupt();
		}
	    }
	} catch (RuntimeException runtimeexception) {
	    /* empty */
	} catch (ThreadDeath threaddeath) {
	    /* empty */
	}
    }
    
    public static void poolGcAll() {
	if (pool != null) {
	    Object object;
	    MONITORENTER (object = lock);
	    MISSING MONITORENTER
	    synchronized (object) {
		for (int i = 0; i < pool.length; i++) {
		    ThreadPool threadpool = pool[i];
		    if (threadpool != null && threadpool.isEmpty)
			threadpool.kill();
		}
	    }
	}
    }
    
    public static void poolKillAll() {
	if (pool != null) {
	    Object object;
	    MONITORENTER (object = lock);
	    MISSING MONITORENTER
	    synchronized (object) {
		for (int i = 0; i < pool.length; i++)
		    pool[i].kill();
	    }
	}
    }
    
    public static void poolSetLimit(int i) {
	Object object;
	MONITORENTER (object = lock);
	MISSING MONITORENTER
	synchronized (object) {
	    poolKillAll();
	    threadLimit = i;
	    pool = new ThreadPool[i <= 0 ? 25 : threadLimit];
	}
    }
    
    public static void poolStartThread(Runnable runnable, char c) {
	poolStartThread(runnable, String.valueOf(c));
    }
    
    public static ThreadPool poolStartThread(Runnable runnable,
					     String string) {
	object_9_ = object_14_;
	break while_11_;
    }
    
    private boolean restart(Runnable runnable, String string) {
	object = object_18_;
	break while_13_;
    }
    
    public void run() {
	object = object_21_;
	break while_15_;
    }
    
    private void run2() {
	try {
	    if (runnable != null)
		runnable.run();
	} catch (Throwable throwable) {
	    /* empty */
	}
	runnable = null;
    }
}
