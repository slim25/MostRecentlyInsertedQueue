package interview.task.queue;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class MostRecentlyInsertedBlockingQueue<E> extends ConcurrentMostRecentlyInsertedQueue<E> implements BlockingQueue<E> {

    private final Condition notEmpty;

    public MostRecentlyInsertedBlockingQueue(int size) {
        super(size);
        this.notEmpty = lock.newCondition();
    }


    @Override
    public boolean offer(E e) {
        if (e == null)
            throw new NullPointerException();
        int newSize;
        Object [] queueWithNewSize;
        if(capacity < queueElements.length){
            queueElements[capacity++] = e;
        }else if(capacity == queueElements.length && capacity < this.queueSize){
            if((newSize = queueElements.length * 2) < this.queueSize){
                queueWithNewSize = new Object[newSize];
            }else{
                queueWithNewSize = new Object[this.queueSize];
            }
            System.arraycopy(queueElements,0,queueWithNewSize,0,queueElements.length);
            queueElements = queueWithNewSize;
            queueElements[capacity++] = e;

        }else if(queueElements.length == this.queueSize) {
           return false;
        }
        return true;
    }

    private boolean isFull(){
        return capacity == this.queueSize;
    }

    @Override
    public void put(E e) throws InterruptedException {
        lock.lockInterruptibly();
        if(isFull()) notEmpty.await();
        this.offer(e);
        lock.unlock();
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lockInterruptibly();
        if(isFull() && nanos > 0) notEmpty.awaitNanos(nanos);
        boolean result = offer(e);
        this.offer(e);
        lock.unlock();
        return result;

    }

    @Override
    public E take() throws InterruptedException {
        lock.lockInterruptibly();
        E result = poll();
        notEmpty.signal();
        lock.unlock();
        return result;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        lock.lockInterruptibly();
        E result = poll();
        notEmpty.signal();
        lock.unlock();
        return result;
    }

    @Override
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        throw new UnsupportedOperationException();
    }
}
