package interview.task.queue;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class MostRecentlyInserterQueue<E> extends AbstractQueue<E> {

    protected int queueSize;
    protected transient Object[] queueElements;
    protected int DEFAULT_SIZE = 11;
    protected int capacity = 0;

    public MostRecentlyInserterQueue(int size){
        if(size < 1) throw new IllegalArgumentException();
        else if(size < DEFAULT_SIZE) this.DEFAULT_SIZE = size;
        this.queueSize = size;
        queueElements = new Object[DEFAULT_SIZE];
    }

    @Override
    public Iterator<E> iterator() {
        return new MostRecentlyInserterQueueIterator(queueElements);
    }

    @Override
    public int size() {
        return this.capacity;
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
            System.arraycopy(queueElements,1,queueElements,0,queueElements.length - 1);
            queueElements[capacity - 1] = e;
        }

        return true;
    }


    @Override
    public E poll() {
        if(capacity != 0){
            E element = (E)queueElements[0];
            System.arraycopy(queueElements,1,queueElements,0,queueElements.length - 1);
            --capacity;
            return element;
        }
        return null;
    }

    @Override
    public E peek() {
        return (E)queueElements[0];
    }

    final class MostRecentlyInserterQueueIterator<E> implements Iterator<E>{

        final Object[] elements;
        int cursor;
        int lastElement;

        MostRecentlyInserterQueueIterator(Object[] elements){
            lastElement = -1;
            this.elements = elements;
        }

        @Override
        public boolean hasNext() {
            return cursor < elements.length;
        }

        @Override
        public E next() {
            if (cursor >= elements.length)
                throw new NoSuchElementException();
            lastElement = cursor;
            return (E)elements[cursor++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
