package com.company.project.core;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThread {
    private MyThread() {

    }

    // 等待队列


    public static ThreadPoolExecutor getInstance() {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1100);
        // 创建一个大小为5的线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, workQueue, new ThreadPoolExecutor.DiscardPolicy());
        return executor;
    }
}
