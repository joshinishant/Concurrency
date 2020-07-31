package com.practise.concurrency.Latch;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancedCountDownLatch implements Runnable{

    private CountDownLatch readyCounter;
    private CountDownLatch startCounter;
    private CountDownLatch completionCounter;


    public AdvancedCountDownLatch(CountDownLatch readyCounter, CountDownLatch startCounter, CountDownLatch completionCounter) {
        this.readyCounter = readyCounter;
        this.startCounter = startCounter;
        this.completionCounter = completionCounter;
    }


    @Override
    public void run(){
        readyCounter.countDown();
        try {
            Thread.sleep(3 * 1000);
            System.out.println("Current Time in milliseconds " + System.currentTimeMillis()+
                    " execute by Thread "+Thread.currentThread().getName());
            completionCounter.countDown();
        }catch (InterruptedException e){
            System.out.println("Exception - "+e);
        }
    }

    public static void main(String args[]) {

        try {
            CountDownLatch readyCounter = new CountDownLatch(5);
            CountDownLatch startCounter = new CountDownLatch(1);
            CountDownLatch completionCounter = new CountDownLatch(5);

            List<Thread> threadList = Stream.
                    generate(() -> new Thread(new AdvancedCountDownLatch(readyCounter, startCounter, completionCounter)))
                    .limit(5)
                    .collect(Collectors.toList());

            threadList.stream().forEach(Thread::start);
            readyCounter.await();
            startCounter.countDown();
            completionCounter.await();
            System.out.println("Exiting " + Thread.currentThread().getName() + " Thread");
        } catch (InterruptedException e) {
            System.out.println("Exception - " + e);
        }
    }
}
