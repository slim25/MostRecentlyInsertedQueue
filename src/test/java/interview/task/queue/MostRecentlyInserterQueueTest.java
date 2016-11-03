package interview.task.queue;

import static org.junit.Assert.*;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Queue;

public class MostRecentlyInserterQueueTest {
    private static final int QUEUE_DEFAULT_SIZE = 11;

    @Test
    public void testMostRecentlyInserterQueue(){
        Queue<Integer> myQueue = new MostRecentlyInserterQueue<>(3);
        assertTrue("myQueue size should be 0, but actual size is : " + myQueue.size(),
                myQueue.size() == 0);

        for (int i = 1; i <= 5; i++){
            myQueue.offer(i);
            int queueSize = i;
            if(i > 3 ) queueSize = 3;
            assertTrue("myQueue size should be : " + queueSize + ", but actual size is : " + myQueue.size(),
                    myQueue.size() == queueSize);
        }
        int poll1 = myQueue.poll();
        assertTrue("myQueue poll should return : 3, but returned value is : " + poll1, poll1 == 3);
        assertTrue("myQueue size should be : 2, but actual size is : " + myQueue.size(),
                myQueue.size() == 2);

        int poll2 = myQueue.poll();
        assertTrue("myQueue poll should return : 4, but returned value is : " + poll2, poll2 == 4);
        assertTrue("myQueue size should be : 1, but actual size is : " + myQueue.size(),
                myQueue.size() == 1);

        myQueue.clear();
        assertTrue("myQueue size should be 0, but actual size is : " + myQueue.size(),
                myQueue.size() == 0);

    }

    @Test
    public void testMostRecentlyInserterQueueWithSizeDynamicIncreasing() throws NoSuchFieldException, IllegalAccessException {
        Queue<Integer> myQueue = new MostRecentlyInserterQueue<>(15);
        assertTrue("myQueue size should be 0, but actual size is : " + myQueue.size(),
                myQueue.size() == 0);

        Field f = myQueue.getClass().getDeclaredField("queueElements");
        f.setAccessible(true);
        Object[] queue = (Object[])f.get(myQueue);
        assertTrue("myQueue default size should be : " + QUEUE_DEFAULT_SIZE + ", but actual size is : " + queue.length,
                queue.length == QUEUE_DEFAULT_SIZE);

        for (int i = 1; i <= 15; i++){
            myQueue.offer(i);
            assertTrue("myQueue size should be : " + i + ", but actual size is : " + myQueue.size(),
                    myQueue.size() == i);
        }

        Object[] queueAfterIncreasingSize = (Object[])f.get(myQueue);
        assertTrue("myQueue dynamically increased size should be : 15, but actual size is : " + queueAfterIncreasingSize.length,
                queueAfterIncreasingSize.length == 15);

        int peek1 = myQueue.peek();
        assertTrue("myQueue peek should return : 1, but returned value is : " + peek1, peek1 == 1);
        assertTrue("myQueue size should be : 15, but actual size is : " + myQueue.size(),
                myQueue.size() == 15);

        int poll1 = myQueue.poll();
        assertTrue("myQueue poll should return : 1, but returned value is : " + poll1, poll1 == 1);
        assertTrue("myQueue size should be : 14, but actual size is : " + myQueue.size(),
                myQueue.size() == 14);

        int poll2 = myQueue.poll();
        assertTrue("myQueue poll should return : 2, but returned value is : " + poll2, poll2 == 2);
        assertTrue("myQueue size should be : 13, but actual size is : " + myQueue.size(),
                myQueue.size() == 13);

        myQueue.clear();
        assertTrue("myQueue size should be 0, but actual size is : " + myQueue.size(),
                myQueue.size() == 0);

    }
}
