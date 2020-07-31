package com.practise.concurrency.CyclicBarrier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomThread implements Runnable{

    private CyclicBarrier cyclicBarrier;
    private List numberList;

    public CustomThread(CyclicBarrier cyclicBarrier,List numberList) {
        this.cyclicBarrier = cyclicBarrier;
        this.numberList=numberList;
    }


    @Override
    public void run() {
        try{
            Thread.sleep(2*1000);
            int value=new Random().nextInt(10000);
            System.out.println("Generated number "+value);
            numberList.add(value);
            Thread.sleep(1*1000);
            cyclicBarrier.await();
        }catch (InterruptedException | BrokenBarrierException e){
            System.out.println("Exception occurred - "+e);
        }
    }

    public static void main(String args[]){
        List<Integer> numberList= Collections.synchronizedList(new ArrayList<Integer>());

        CyclicBarrier cyclicBarrier= new CyclicBarrier(6,() -> {
            int sum=0;
            for(Integer num:numberList){
                sum+=num;
            }
            numberList.clear();
            System.out.println("Total sum "+sum);
        });

        List<Thread> threadList=Stream
                .generate(() ->  new Thread(new CustomThread(cyclicBarrier,numberList)))
                .limit(5)
                .collect(Collectors.toList());

        threadList.stream().forEach(Thread::start);

        try {
            cyclicBarrier.await();
        }catch (InterruptedException | BrokenBarrierException e){
            System.out.println("Exception "+e);
        }
        System.out.println("Processing completed");

    }
}
