package com.practise.concurrency.Latch;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasicCountDownLatch implements Runnable{



    private CountDownLatch countDownLatch;


    public BasicCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3*1000);
            System.out.println("Current Time in milliseconds " + System.currentTimeMillis()+
                    " execute by Thread "+Thread.currentThread().getName());
        } catch (Exception e) {
            System.out.println("Exception " + e);
        } finally {
            countDownLatch.countDown();
        }
    }

    public static void main(String args[]){

        try{
            CountDownLatch countDownLatch=new CountDownLatch(5);

            List<Thread> threadList= Stream.
                    generate(()->new Thread(new BasicCountDownLatch(countDownLatch))
                    ).limit(5)
                    .collect(Collectors.toList());

            //countDownLatch.await(3, TimeUnit.SECONDS);
            threadList.stream().forEach( t -> t.start());
            countDownLatch.await();
            System.out.println("Exiting "+Thread.currentThread().getName()+" Thread");
        }catch (InterruptedException e){
            System.out.println("Exception "+e);
        }
    }
}
