package com.practise.concurrency.Locks;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomRentrantLocks implements Runnable {

    private ReentrantLock reentrantLock;
    private Integer[] integerList;


    public CustomRentrantLocks(ReentrantLock reentrantLock,Integer[] integerList) {
        this.reentrantLock = reentrantLock;
        this.integerList=integerList;
    }

    @Override
    public void run() {
        try {
            reentrantLock.lock();
            System.out.println("Execution started for thread "+Thread.currentThread().getName());
            Thread.sleep(3*1000);
            int index=new Random().nextInt(integerList.length-1);
            int additionValue=new Random().nextInt(100);
            int initialValue=integerList[index];
            integerList[index]=initialValue+additionValue;
            System.out.println("Adding "+additionValue+ " at index "+index);
            System.out.println("Initial value - "+initialValue+ " Final value - "+integerList[index]);
            System.out.println("Execution ended for thread "+Thread.currentThread().getName()+"\n\n");
            reentrantLock.unlock();
        }catch (InterruptedException e){
            System.out.println("Exception occurred - "+e);
        }

    }


    public  static void main(String args[]) throws InterruptedException{
       Integer[] integerList = {1,2,3,4,5,6,7,8,9};
       ReentrantLock reentrantLock=new ReentrantLock(true);
       List<Thread> threadList= Stream.generate(() -> new Thread(new CustomRentrantLocks(reentrantLock,integerList)))
                .limit(5)
                .collect(Collectors.toList());


        threadList.stream().forEach(Thread::start);
    }


}
