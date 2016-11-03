package interview.task.queue;

import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentMostRecentlyInsertedQueue<E> extends MostRecentlyInserterQueue<E>{

    protected final ReentrantLock lock;

    public ConcurrentMostRecentlyInsertedQueue(int size){
        super(size);
        this.lock = new ReentrantLock();
    }

    @Override
    public int size() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        int result = super.size();
        lock.unlock();
        return result;

    }
    @Override
    public boolean offer(E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        boolean result = super.offer(e);
        lock.unlock();
        return result;
    }
    @Override
    public E poll() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        E result = super.poll();
        lock.unlock();
        return result;
    }
    @Override
    public E peek() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        E result = super.peek();
        lock.unlock();
        return result;
    }

}
