package com.practise.concurrency.ThreadLocal;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomThread implements Runnable {

    private Phaser allTreads;
    private Phaser innerThreads;
    private ThreadLocal<Integer> threadLocal=ThreadLocal.withInitial(() -> 0);

    public CustomThread(Phaser allTreads,Phaser innerThreads ) {
        this.allTreads = allTreads;
        this.innerThreads=innerThreads;
        allTreads.register();
        innerThreads.register();
    }

    @Override
    public void run() {
        try{

            int waitTime=new Random().nextInt(9);
            Thread.sleep(waitTime*1000);
            allTreads.arriveAndAwaitAdvance();
            System.out.println("Starting execution for "+Thread.currentThread().getName());
            Thread.sleep(waitTime*1000);
            threadLocal.set(waitTime);

            innerThreads.arriveAndAwaitAdvance();
            System.out.println("Random value stored in ThreadLocal "+waitTime);
            innerThreads.arriveAndDeregister();
            allTreads.arriveAndAwaitAdvance();
            allTreads.arriveAndDeregister();
        }catch (Exception e){
            System.out.println("Exception occurred - "+e);
        }
    }


    public static void main(String args[]){

        Phaser allThreads=new Phaser(1);
        Phaser innerThreads=new Phaser(0);
        List<Thread> threadList= Stream
                .generate(() -> new Thread(new CustomThread(allThreads,innerThreads)))
                .limit(5)
                .collect(Collectors.toList());

        threadList.stream().forEach(Thread::start);
        try {

            System.out.println("Execution has started.....");
            allThreads.arriveAndAwaitAdvance();
            allThreads.arriveAndAwaitAdvance();
            System.out.println("Execution has completed.....");
            allThreads.arriveAndDeregister();
        }catch (Exception e){
            System.out.println("Exception occurred - "+e);
        }

    }

}
