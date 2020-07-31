package com.practise.concurrency.Phaser;

import java.util.List;
import java.util.concurrent.Phaser;

public class AggregationThread implements Runnable {

    private Phaser initalPhaser;
    private Phaser finalPhaser;
    private List<Integer> numberList;

    public AggregationThread(Phaser initalPhaser,Phaser finalPhaser,List numberList) {
        this.initalPhaser = initalPhaser;
        this.finalPhaser=finalPhaser;
        this.numberList=numberList;
        initalPhaser.register();
        finalPhaser.register();
    }

    @Override
    public void run() {
        initalPhaser.arriveAndAwaitAdvance();
        int sum=0;
        for(Integer num:numberList){
            sum+=num;
        }
        numberList.clear();
        System.out.println("Total sum "+sum);
        initalPhaser.arriveAndDeregister();
        finalPhaser.arriveAndAwaitAdvance();
        finalPhaser.arriveAndDeregister();
    }
}
