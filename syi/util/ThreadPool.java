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

  private ThreadPool(Runnable paramRunnable, String paramString, int paramInt)
  {
    super(paramString);
    this.poolNumber = paramInt;
    this.runnable = paramRunnable;
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
    int i = 0;
    synchronized (lock)
    {
      if (pool == null)
        return 0;
      for (int j = 0; j < pool.length; j++)
      {
        ThreadPool localThreadPool = pool[j];
        i += ((localThreadPool == null) || (!localThreadPool.isEmpty) ? 0 : 1);
      }
    }
    return i;
  }

  public static int getCountOfWorking()
  {
    int i = 0;
    synchronized (lock)
    {
      if (pool == null)
        return 0;
      for (int j = 0; j < pool.length; j++)
      {
        ThreadPool localThreadPool = pool[j];
        i += ((localThreadPool == null) || (localThreadPool.isEmpty) ? 0 : 1);
      }
    }
    return i;
  }

  public void interrupt()
  {
    try
    {
      synchronized (this)
      {
        if (!this.isEmpty)
          super.interrupt();
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

  private void kill()
  {
    try
    {
      if (pool == null)
        return;
      synchronized (lock)
      {
        if (this.isLive)
        {
          pool[this.poolNumber] = null;
          this.runnable = null;
        }
        this.isLive = false;
        if (this.isEmpty)
          notify();
        else
          interrupt();
      }
    }
    catch (RuntimeException localRuntimeException)
    {
    }
    catch (ThreadDeath localThreadDeath)
    {
    }
  }

  public static void poolGcAll()
  {
    if (pool == null)
      return;
    synchronized (lock)
    {
      for (int i = 0; i < pool.length; i++)
      {
        ThreadPool localThreadPool = pool[i];
        if ((localThreadPool == null) || (!localThreadPool.isEmpty))
          continue;
        localThreadPool.kill();
      }
    }
  }

  public static void poolKillAll()
  {
    if (pool == null)
      return;
    synchronized (lock)
    {
      for (int i = 0; i < pool.length; i++)
        pool[i].kill();
    }
  }

  public static void poolSetLimit(int paramInt)
  {
    synchronized (lock)
    {
      poolKillAll();
      threadLimit = paramInt;
      pool = new ThreadPool[paramInt <= 0 ? 25 : threadLimit];
    }
  }

  public static void poolStartThread(Runnable paramRunnable, char paramChar)
  {
    poolStartThread(paramRunnable, String.valueOf(paramChar));
  }

  public static ThreadPool poolStartThread(Runnable paramRunnable, String paramString)
  {
    if ((paramString == null) || (paramString.length() <= 0))
      paramString = "pool";
    try
    {
      synchronized (lock)
      {
        if (pool == null)
          poolSetLimit(threadLimit);
        int i = pool.length;
        for (int j = 0; j < i; j++)
        {
          ThreadPool localThreadPool = pool[j];
          if ((localThreadPool == null) || (!localThreadPool.isEmpty))
            continue;
          if (localThreadPool.restart(paramRunnable, paramString))
            return localThreadPool;
          localThreadPool.kill();
        }
        for (j = 0; j < i; j++)
        {
          if (pool[j] != null)
            continue;
          pool[j] = new ThreadPool(paramRunnable, paramString, j);
          return pool[j];
        }
        if (threadLimit <= 0)
        {
          ThreadPool[] arrayOfThreadPool = new ThreadPool[i + Math.min(Math.max((int)(i * 0.4D), 1), 100)];
          for (int k = 0; k < i; k++)
            arrayOfThreadPool[k] = pool[k];
          pool = arrayOfThreadPool;
          pool[i] = new ThreadPool(paramRunnable, paramString, i);
          return pool[i];
        }
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      localRuntimeException.printStackTrace();
    }
    return null;
  }

  private boolean restart(Runnable paramRunnable, String paramString)
  {
    try
    {
      if ((!this.isLive) || (!this.isEmpty))
        return false;
      synchronized (this)
      {
        if ((!this.isEmpty) || (!this.isLive))
          return false;
        this.runnable = paramRunnable;
        this.isEmpty = false;
        setName(paramString);
        notify();
      }
      return true;
    }
    catch (RuntimeException localRuntimeException)
    {
    }
    return false;
  }

  public void run()
  {
    while (this.isLive)
    {
      run2();
      try
      {
        if (!this.isLive)
          break;
        synchronized (this)
        {
          this.isEmpty = true;
          wait(30000L);
          if (this.isEmpty)
          {
            kill();
            return;
          }
        }
      }
      catch (Throwable localThrowable)
      {
        kill();
        return;
      }
    }
  }

  private void run2()
  {
    try
    {
      if (this.runnable != null)
        this.runnable.run();
    }
    catch (Throwable localThrowable)
    {
    }
    this.runnable = null;
  }
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     syi.util.ThreadPool
 * JD-Core Version:    0.6.0
 */