package interview.task.queue;


import org.junit.Test;

import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertTrue;

public class ConcurrentMostRecentlyInsertedQueueTest {


    @Test
    public void testConcurrentMostRecentlyInsertedQueueTest() throws InterruptedException {
        final Queue<Integer> concurrentMostRecentlyInsertedQueue = new ConcurrentMostRecentlyInsertedQueue<>(500);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(500);
        final CountDownLatch latch = new CountDownLatch(1);
        for (int i=0; i<500; ++i) {
            final int finalI = i;
            Runnable runner = new Runnable() {
                public void run() {
                    try {
                        latch.await();
                        concurrentMostRecentlyInsertedQueue.offer(finalI);
                    } catch (InterruptedException ie) { }
                }
            };
            executor.execute(new Thread(runner, "TestThread"+i));
//          new Thread(runner, "TestThread"+i).start();
        }
        // all threads are waiting on the latch.
        latch.countDown(); // release the latch
        // all threads are now running concurrently.
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        int currentQueueSize = concurrentMostRecentlyInsertedQueue.size();
        assertTrue("concurrentMostRecentlyInsertedQueue size should be 50, but actual size is : " + currentQueueSize,
                currentQueueSize == 500);

    }

}
