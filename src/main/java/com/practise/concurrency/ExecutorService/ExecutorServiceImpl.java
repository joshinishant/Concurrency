package com.practise.concurrency.ExecutorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class ExecutorServiceImpl {


    public static void main(String args[]) throws  InterruptedException, ExecutionException {

        List numberList= Collections.synchronizedList(new ArrayList<>());
        CustomThreadFactory customThreadFactory=new CustomThreadFactory();

        ExecutorService executorService= Executors.newCachedThreadPool(customThreadFactory);

        Callable generateNumber = () ->{
            Thread.sleep(3*1000);
            Integer val=new Random().nextInt(10);
            numberList.add(val);
            return val;
        };

        Future<Integer> futureObj= executorService.submit(generateNumber);
        while(!futureObj.isDone()){
            System.out.println("Waiting for value from future object....");
            Thread.sleep(5*100);
        }

        if(futureObj.isDone()){
            int val=futureObj.get();
            System.out.println("Returned value from future object"+val);
        }
    }
}
