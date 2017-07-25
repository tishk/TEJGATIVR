package utils;

import java.util.LinkedList;

/**
 * Created by root on 7/17/17.
 */
public class DataBaseLoggerQueue<E> {

   private LinkedList<E> list=new LinkedList<E>();

    public void enqueue(E item){
        list.addLast(item);
    }
    public E dequeue(){
        return list.poll();
    }
    public boolean hasItems(){
        return !list.isEmpty();
    }

    public int size(){
        return list.size();
    }

    public void addItems(DataBaseLoggerQueue<? extends E> q){
        while (q.hasItems()) list.add(q.dequeue());
    }


}
