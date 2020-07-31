package com.practise.concurrency.ExecutorService;

import java.util.concurrent.ThreadFactory;

public class CustomThreadFactory implements ThreadFactory {


    @Override
    public Thread newThread(Runnable r) {
        Thread thread=new Thread(r);
        return thread;
    }
}
