package juc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author HongTaiwei
 * @date 2021/8/3
 */
public class ForkJoinDemo {

    private static int number = 1000000000;

    private static int[] numberArray = new int[number];

    static {
        for (int i = 0; i < numberArray.length ; i++) {
            numberArray[i] = i;
        }
    }


    public static void main(String[] args) {
        foriSum();
        streamSum();
        parallelStreamSum();
        forechSum();
        forkJoinSum();
    }

    private static void foriSum() {
        long sum = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < numberArray.length; i++) {
            sum += numberArray[i];
        }
        System.out.println("foriTime:"+(System.currentTimeMillis()-startTime)+" sum:"+sum);
    }

    private static void streamSum() {
        long sum = 0;
        long startTime = System.currentTimeMillis();
        sum = Arrays.stream(numberArray).asLongStream().sum();
        System.out.println("streamTime:"+(System.currentTimeMillis()-startTime)+" sum:"+sum);
    }

    private static void parallelStreamSum() {
        long sum = 0;
        long startTime = System.currentTimeMillis();
        sum = Arrays.stream(numberArray).parallel().asLongStream().sum();
        System.out.println("parallelStreamTime:"+(System.currentTimeMillis()-startTime)+" sum:"+sum);
    }

    private static void forechSum() {
        long sum = 0;
        long startTime = System.currentTimeMillis();
        for (int i : numberArray) {
            sum+=i;
        }
        System.out.println("forechTime:"+(System.currentTimeMillis()-startTime)+" sum:"+sum);
    }

    private static void forkJoinSum() {
        ForkJoinPool pool = new ForkJoinPool();
        MyTask myTask = new MyTask(0, numberArray.length);
        long startTime = System.currentTimeMillis();
        pool.execute(myTask);
        Long sum = myTask.join();
        System.out.println("forkJoinSum:"+(System.currentTimeMillis()-startTime)+" sum:"+sum);
    }




    static class MyTask extends RecursiveTask<Long>{

        private static final int THRESHOLD = 50000000;

        private int start;

        private int end;

        public MyTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            long sum = 0;
            if(end - start < THRESHOLD){
                for (int i = start; i < end; i++) {
                    sum += numberArray[i];
                }
                return sum;
            }
            int mid = ((end - start) >> 1) + start;
            MyTask myTask = new MyTask(start, mid);
            MyTask myTask1 = new MyTask(mid, end);
            myTask.fork();
            myTask1.fork();
            return myTask.join()+myTask1.join();
        }
    }
}
