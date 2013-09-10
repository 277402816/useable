package com.roiland.crm.sm.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Chunji Li
 *
 */
public class ThreadPool {
    private static final String tag = Log.getTag(ThreadPool.class);

    private static final int SHUTDOWN_WAITING_TIME = 15;

    public static final int FIXED_POOL_SIZE = 4;

    public static final int TYPE_LONG_LIFE = 0;     //run time < 1s
    public static final int TYPE_SHORT_LIFE = 1;    //run time >= 1s
    public static final int TYPE_SCHEDULED = 2;

    private static final Map<Integer, ExecutorService> pools = new HashMap<Integer, ExecutorService>();

    public static Future<?> excute(int type, Runnable task) {
        start();

        synchronized (pools) {
            ExecutorService pool = pools.get(type);
            if (pool != null) {
                return pool.submit(task);
            }
        }

        return null;
    }

    public static Future<?> excuteShortTask(Runnable task) {
        return excute(TYPE_SHORT_LIFE, task);
    }

    public static Future<?> excuteLongTask(Runnable task) {
        return excute(TYPE_LONG_LIFE, task);
    }

    public static ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
    	if (pools == null || pools.size() == 0) {
    		start();
    	}
        ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService) pools.get(TYPE_SCHEDULED);
        return scheduledExecutorService.schedule(command, delay, unit);
    }

    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService) pools.get(TYPE_SCHEDULED);
        return scheduledExecutorService.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService) pools.get(TYPE_SCHEDULED);
        return scheduledExecutorService.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    public static void start() {
        synchronized (pools) {
            if (pools.isEmpty()) {
                pools.put(TYPE_LONG_LIFE, Executors.newCachedThreadPool());
                pools.put(TYPE_SHORT_LIFE, Executors.newFixedThreadPool(FIXED_POOL_SIZE));
                pools.put(TYPE_SCHEDULED, Executors.newSingleThreadScheduledExecutor());
            }
        }
    }

    public static void shutdown() {
        synchronized (pools) {
            for (Iterator<Map.Entry<Integer, ExecutorService>> iterator = pools.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<Integer, ExecutorService> entry = iterator.next();
                iterator.remove();

                ExecutorService pool = entry.getValue();
                shutdownAndAwaitTermination(pool);
            }
        }
    }

    private static void shutdownAndAwaitTermination(ExecutorService pool) {
        if (pool == null) {
            return;
        }

        synchronized (pool) {
            if (pool.isTerminated()) {
                return;
            }

            try {
                pool.shutdownNow(); // Disable new tasks from being submitted
                // Wait a while for existing tasks to terminate
                if (!pool.awaitTermination(SHUTDOWN_WAITING_TIME, TimeUnit.SECONDS)) {
                    pool.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                pool.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            if (Log.DEBUG.get()) {
                Log.d(tag, e.getMessage());
            }
        }
    }

}
