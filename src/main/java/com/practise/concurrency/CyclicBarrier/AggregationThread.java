package com.practise.concurrency.CyclicBarrier;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

public class AggregationThread implements Runnable {

    private List<Integer> numberList;

    public AggregationThread(List numberList) {
        this.numberList=numberList;
    }

    @Override
    public void run() {
        int sum=0;
        for(Integer num:numberList){
            sum+=num;
        }
        numberList.clear();
        System.out.println("Total sum "+sum);
    }
}
