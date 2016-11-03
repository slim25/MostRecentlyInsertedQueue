package interview.task.queue;


import org.junit.Test;

import java.util.Queue;
import java.util.concurrent.*;

import static org.junit.Assert.assertTrue;

public class MostRecentlyInsertedBlockingQueueTest {

    @Test
    public void testMostRecentlyInsertedBlockingQueueTest() throws InterruptedException {
        final BlockingQueue<Integer> mostRecentlyInsertedBlockingQueue = new MostRecentlyInsertedBlockingQueue<>(2);
        assertTrue("myQueue size should be 0, but actual size is : " + mostRecentlyInsertedBlockingQueue.size(),
                mostRecentlyInsertedBlockingQueue.size() == 0);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        final CountDownLatch latch = new CountDownLatch(1);

        Runnable runnerPutter = new Runnable() {
            public void run() {
                try {
                    mostRecentlyInsertedBlockingQueue.put(1);
                    mostRecentlyInsertedBlockingQueue.put(2);
                    assertTrue("mostRecentlyInsertedBlockingQueue size should be 2, but actual size is : " + mostRecentlyInsertedBlockingQueue.size(),
                            mostRecentlyInsertedBlockingQueue.size() == 2);
                    mostRecentlyInsertedBlockingQueue.put(3);
                    assertTrue("mostRecentlyInsertedBlockingQueue size should be 2, but actual size is : " + mostRecentlyInsertedBlockingQueue.size(),
                            mostRecentlyInsertedBlockingQueue.size() == 2);

                } catch (InterruptedException ie) { }
            }
        };

        Runnable runnerPuller = new Runnable() {
            public void run() {
                try {
                    latch.await(10,TimeUnit.SECONDS);
                    mostRecentlyInsertedBlockingQueue.take();

                } catch (InterruptedException ie) { }
            }
        };
        executor.execute(new Thread(runnerPutter, "TestThread1"));
        executor.execute(new Thread(runnerPuller, "TestThread2"));

        executor.shutdown();
        executor.awaitTermination(15, TimeUnit.SECONDS);

        assertTrue("mostRecentlyInsertedBlockingQueue size should be 2, but actual size is : " + mostRecentlyInsertedBlockingQueue.size(),
                mostRecentlyInsertedBlockingQueue.size() == 2);



    }
}
