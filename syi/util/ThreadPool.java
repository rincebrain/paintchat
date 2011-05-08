package syi.util;


public class ThreadPool extends Thread
{

    private static Object lock = new Object();
    private static int threadLimit = 0;
    private static int priority = 1;
    private static ThreadPool pool[] = null;
    private int poolNumber;
    private boolean isLive;
    private boolean isEmpty;
    private Runnable runnable;

    private ThreadPool(Runnable runnable1, String s, int i)
    {
        super(s);
        isLive = true;
        isEmpty = false;
        runnable = null;
        poolNumber = i;
        runnable = runnable1;
        setPriority(priority);
        setDaemon(true);
        start();
    }

    protected void finalize()
        throws Throwable
    {
        kill();
    }

    public static int getCountOfSleeping()
    {
        int i;
label0:
        {
            i = 0;
            ThreadPool threadpool;
            int j;
            synchronized(lock)
            {
                if(pool != null)
                {
                    break label0;
                }
            }
            return 0;
        }
        for(j = 0; j < pool.length; j++)
        {
            threadpool = pool[j];
            i += threadpool != null && threadpool.isEmpty ? 1 : 0;
        }

        obj;
        JVM INSTR monitorexit ;
        return i;
    }

    public static int getCountOfWorking()
    {
        int i;
label0:
        {
            i = 0;
            ThreadPool threadpool;
            int j;
            synchronized(lock)
            {
                if(pool != null)
                {
                    break label0;
                }
            }
            return 0;
        }
        for(j = 0; j < pool.length; j++)
        {
            threadpool = pool[j];
            i += threadpool != null && !threadpool.isEmpty ? 1 : 0;
        }

        obj;
        JVM INSTR monitorexit ;
        return i;
    }

    public void interrupt()
    {
        try
        {
            synchronized(this)
            {
                if(!isEmpty)
                {
                    super.interrupt();
                }
            }
        }
        catch(Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

    private void kill()
    {
        try
        {
            if(pool == null)
            {
                return;
            }
            synchronized(lock)
            {
                if(isLive)
                {
                    pool[poolNumber] = null;
                    runnable = null;
                }
                isLive = false;
                if(isEmpty)
                {
                    notify();
                } else
                {
                    interrupt();
                }
            }
        }
        catch(RuntimeException _ex) { }
        catch(ThreadDeath _ex) { }
    }

    public static void poolGcAll()
    {
        if(pool == null)
        {
            return;
        }
        synchronized(lock)
        {
            for(int i = 0; i < pool.length; i++)
            {
                ThreadPool threadpool = pool[i];
                if(threadpool != null && threadpool.isEmpty)
                {
                    threadpool.kill();
                }
            }

        }
    }

    public static void poolKillAll()
    {
        if(pool == null)
        {
            return;
        }
        synchronized(lock)
        {
            for(int i = 0; i < pool.length; i++)
            {
                pool[i].kill();
            }

        }
    }

    public static void poolSetLimit(int i)
    {
        synchronized(lock)
        {
            poolKillAll();
            threadLimit = i;
            pool = new ThreadPool[i > 0 ? threadLimit : 25];
        }
    }

    public static void poolStartThread(Runnable runnable1, char c)
    {
        poolStartThread(runnable1, String.valueOf(c));
    }

    public static ThreadPool poolStartThread(Runnable runnable1, String s)
    {
        if(s == null || s.length() <= 0)
        {
            s = "pool";
        }
        Object obj = lock;
        JVM INSTR monitorenter ;
        int i;
        int j;
        if(pool == null)
        {
            poolSetLimit(threadLimit);
        }
        i = pool.length;
        j = 0;
          goto _L1
_L3:
        ThreadPool threadpool;
        threadpool = pool[j];
        if(threadpool == null || !threadpool.isEmpty)
        {
            continue; /* Loop/switch isn't completed */
        }
        if(threadpool.restart(runnable1, s))
        {
            return threadpool;
        }
        threadpool.kill();
        j++;
_L1:
        if(j < i) goto _L3; else goto _L2
_L2:
        j = 0;
          goto _L4
_L6:
        if(pool[j] != null)
        {
            continue; /* Loop/switch isn't completed */
        }
        pool[j] = new ThreadPool(runnable1, s, j);
        pool[j];
        obj;
        JVM INSTR monitorexit ;
        return;
        j++;
_L4:
        if(j < i) goto _L6; else goto _L5
_L5:
        if(threadLimit > 0) goto _L8; else goto _L7
_L7:
        ThreadPool athreadpool[] = new ThreadPool[i + Math.min(Math.max((int)((double)i * 0.40000000000000002D), 1), 100)];
        for(int k = 0; k < i; k++)
        {
            athreadpool[k] = pool[k];
        }

        pool = athreadpool;
        pool[i] = new ThreadPool(runnable1, s, i);
        pool[i];
        obj;
        JVM INSTR monitorexit ;
        return;
_L8:
        obj;
        JVM INSTR monitorexit ;
        break MISSING_BLOCK_LABEL_245;
        obj;
        JVM INSTR monitorexit ;
        throw ;
        RuntimeException runtimeexception;
        runtimeexception;
        runtimeexception.printStackTrace();
        return null;
    }

    private boolean restart(Runnable runnable1, String s)
    {
label0:
        {
            if(!isLive || !isEmpty)
            {
                return false;
            }
            synchronized(this)
            {
                if(isEmpty && isLive)
                {
                    break label0;
                }
            }
            return false;
        }
        runnable = runnable1;
        isEmpty = false;
        setName(s);
        notify();
        threadpool;
        JVM INSTR monitorexit ;
        return true;
        JVM INSTR pop ;
        return false;
    }

    public void run()
    {
_L2:
        run2();
        try
        {
label0:
            {
                if(!isLive)
                {
                    break; /* Loop/switch isn't completed */
                }
                synchronized(this)
                {
                    isEmpty = true;
                    wait(30000L);
                    if(!isEmpty)
                    {
                        break label0;
                    }
                    kill();
                }
                return;
            }
        }
        catch(Throwable _ex)
        {
            kill();
            return;
        }
        threadpool;
        JVM INSTR monitorexit ;
        continue; /* Loop/switch isn't completed */
        if(isLive) goto _L2; else goto _L1
_L1:
    }

    private void run2()
    {
        try
        {
            if(runnable != null)
            {
                runnable.run();
            }
        }
        catch(Throwable _ex) { }
        runnable = null;
    }

}
