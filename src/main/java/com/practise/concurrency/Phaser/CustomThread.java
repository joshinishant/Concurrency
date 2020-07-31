package com.practise.concurrency.Phaser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomThread implements Runnable{

    private Phaser initalPhaser;
    private Phaser finalPhaser;
    private List numberList;

    public CustomThread(Phaser initalPhaser,Phaser finalPhaser,List numberList) {
        this.initalPhaser=initalPhaser;
        this.finalPhaser=finalPhaser;
        this.numberList=numberList;
        initalPhaser.register();
        finalPhaser.register();
    }


    @Override
    public void run() {
        initalPhaser.arriveAndAwaitAdvance();
        try{
            Thread.sleep(2*1000);
            int value=new Random().nextInt(10000);
            System.out.println("Generated number "+value);
            numberList.add(value);
            Thread.sleep(1*1000);
        }catch (InterruptedException e){
            System.out.println("Exception occurred - "+e);
        }finally {
            initalPhaser.arriveAndDeregister();
            finalPhaser.arriveAndAwaitAdvance();
            finalPhaser.arriveAndDeregister();
        }
    }

    public static void main(String args[]){
        System.out.println("Total available cores "+Runtime.getRuntime().availableProcessors());

        Phaser initalPhaser=new Phaser(0);
        Phaser finalPhaser=new Phaser(1);

        List numberList= Collections.synchronizedList(new ArrayList<Integer>());

        List<Thread> threadList=Stream
                .generate(() ->  new Thread(new CustomThread(initalPhaser,finalPhaser,numberList)))
                .limit(5)
                .collect(Collectors.toList());

        threadList.stream().forEach(Thread::start);
        finalPhaser.arriveAndAwaitAdvance();
        System.out.println("Phase 1 complete");

        threadList=Stream
                .generate(() ->  new Thread(new AggregationThread(initalPhaser,finalPhaser,numberList)))
                .limit(1)
                .collect(Collectors.toList());

        threadList.stream().forEach(Thread::start);

        finalPhaser.arriveAndAwaitAdvance();



        System.out.println("Processing completed");
        finalPhaser.arriveAndDeregister();

    }
}
