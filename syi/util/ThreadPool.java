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

    public static int getCountOfSleeping() {
        int i = 0;
        ThreadPool threadpool;
        int j;
        synchronized (lock) {
            if (pool == null) {
                return 0;
            }
            for (j = 0; j < pool.length; j++) {
                threadpool = pool[j];
                i += threadpool != null && threadpool.isEmpty ? 1 : 0;
            }
        }
        return i;
    }

    public static int getCountOfWorking() {
        int i = 0;
        ThreadPool threadpool;
        int j;
        synchronized (lock) {
            if (pool == null) {
                return 0;
            }
            for (j = 0; j < pool.length; j++) {
                threadpool = pool[j];
                i += threadpool != null && !threadpool.isEmpty ? 1 : 0;
            }
        }
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

    public static ThreadPool poolStartThread(Runnable runnable1, String s) {
        if (s == null || s.length() <= 0) {
            s = "pool";
        }
        try {
            synchronized (lock) {
                int i;
                int j;
                if (pool == null) {
                    poolSetLimit(threadLimit);
                }
                i = pool.length;
                for (j = 0; j < i; j++) {
                    ThreadPool threadpool;
                    threadpool = pool[j];
                    if (threadpool == null || !threadpool.isEmpty) {
                        continue;
                    }
                    if (threadpool.restart(runnable1, s)) {
                        return threadpool;
                    }
                    threadpool.kill();
                }
                for (j = 0; j < i; j++) {
                    if (pool[j] != null) {
                        continue;
                    }
                    pool[j] = new ThreadPool(runnable1, s, j);
                    return pool[j];
                }
                if (threadLimit <= 0) {
                    ThreadPool athreadpool[] = new ThreadPool[i + Math.min(Math.max((int) ((double) i * 0.40000000000000002D), 1), 100)];
                    for (int k = 0; k < i; k++) {
                        athreadpool[k] = pool[k];
                    }
                    pool = athreadpool;
                    pool[i] = new ThreadPool(runnable1, s, i);
                    return pool[i];
                }
            }
        } catch (RuntimeException runtimeexception) {
            runtimeexception.printStackTrace();
        }
        return null;
    }

    private boolean restart(Runnable runnable1, String s) {
        try {
            if (!isLive || !isEmpty) {
                return false;
            }
            synchronized (this) {
                if (!isEmpty || !isLive) {
                    return false;
                }
                runnable = runnable1;
                isEmpty = false;
                setName(s);
                notify();
            }
            return true;
        } catch (RuntimeException localRuntimeException) {
        }
        return false;
    }

    public void run() {
        while (this.isLive) {
            run2();
            try {
                if (!isLive) {
                    break;
                }
                synchronized (this) {
                    isEmpty = true;
                    wait(30000L);
                    if (isEmpty) {
                        kill();
                        return;
                    }
                }
            } catch (Throwable _ex) {
                kill();
                return;
            }
        }
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
