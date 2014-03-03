package com.smarthost.data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 6:39 PM
 */
public class DataProcessor {

    static ExecutorService exe = Executors.newSingleThreadExecutor();

    public static void runProcess(Runnable r)
    {
        exe.execute(r);
    }
}