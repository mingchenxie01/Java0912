package Week1;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class Day4 {
    public static void main(String[] args) {
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        map.put(1, 1);
        map.put(2, 2);
        Iterator<Integer> it = map.keySet().iterator(); //????
        while(it.hasNext()){
            Integer curr = it.next();
            if(curr == 1) map.put(3, 3);
            System.out.println(curr);
        }
    }
}
/**
 * JVM Memory Model
 *      Stack(LIFO) vs Heap
 *          Multiple thread every thread will have own stack (local reference variable)
 *          share one heap (object)
 *      Stack:
 *      Once thread invoke method, create stack frame []
 *      fib(10) fib(9) fib(8) ....fib(0)
 *      new Object() -> eden -> after several garbage collect/ eden is full -> s0/s1 -> old
 *      Heap:
 *                 s0 s1
 *          [eden][survivor0][s1  ] young generation: minor gc(serial GC/parallel GC/ Parallel New GC) STW stop the world, freeze your program
 *          [                     ] old generation: major gc(serial GC/ parallel Old GC/ CMS concurrent mark and sweep)
 *          [                     ] permanent / meta space(method area)
 *                                  full gc: happen both in young and old
 *          CMS:
 *          1. initial mark (stw)
 *          2. concurrent mark
 *          3. final mark (stw)
 *          4. concurrent sweep
 *          heap dump
 *          GC root(class, monitor, classLoader):
 *
 *              root
 *               |
 *             object
 *          G1 Algorithm(full gc):
 *          [][unused][][e][][][][s1][]
 *          [][][][][][s0][][o][]
 *          ...
 *          [][][][][o][][][][]
 *
 */

/**
 *              fib(10)
 *              fib(9)        fib(8)
 *           fib(8) fib(7)
 */

class TestStack{
    static int fib(int i){
        if(i <= 1) return i;
        return fib(i - 1) + fib(i - 2);
    }

    public static void main(String[] args) {
        System.out.println(fib(10));
    }
}
/**
 * finalize() method will invoke while the object is removed
 *  final vs finally vs finalize
 */
class CO{
    @Override
    protected void finalize(){
        System.out.println("In the finalize");
    }

    public static void main(String[] args) {
        new CO();
        //suggestion to jvm to do gc
        System.gc();
    }
}
/**
 * Thread
 *      How to create new thread?
 *          1.extend thread class
 *          2.implement runnable
 */
class MyThread extends Thread{
    @Override
    public void run(){
        System.out.println("I am(Thread) :" + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
        System.out.println("I am :" + Thread.currentThread().getName());
        //I am :main      I am(Thread) :Thread-0  ????why main at first
    }
}
class NameClass implements Runnable{

    @Override
    public void run() {
        System.out.println("xxxx3");
    }
}
class TestRunnable{
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println("I am(in main TestRunnable) :" + Thread.currentThread().getName());
        });
//        Runnable runnable = () -> {
//            System.out.println("xxxx");
//        };
        Runnable runnable = new Runnable(){
            @Override
            public void run(){
                System.out.println("xxxx2");
            }
        };
        NameClass nameClass = new NameClass();
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        Thread t3 = new Thread(nameClass);
        t3.start();
    }
}

class RunnableDemo implements Runnable{
    private Thread t;
    private String threadName;

    RunnableDemo(String name){
        threadName = name;
        System.out.println("Creating " + threadName);
    }
    @Override
    public void run() {
        System.out.println("Running ThreadName is:" + threadName);
        try{
            for(int i = 4; i > 0; i--){
                System.out.println("Thread in for loop: " + threadName + "," + i);
                Thread.sleep(50);
            }
        }catch(InterruptedException e){
            System.out.println("Thread(in catch) is:" + threadName + "interrupted.");
        }
        System.out.println("which Thread ? " + threadName + "exiting.");
    }
    public void start(){
        System.out.println("Starting is " + threadName);
        if(t == null){
            t = new Thread(this, threadName);
            t.start();
        }
    }
}
class TestThread{
    public static void main(String[] args) {
        RunnableDemo R1 = new RunnableDemo("Thread - 1");
        R1.start();
        RunnableDemo R2 = new RunnableDemo("Thread - 2");
        R2.start();
    }
}

class ThreadDemo extends Thread{
    private Thread t;
    private String threadName;

    ThreadDemo(String name){
        threadName = name;
        System.out.println("Creating is: " + threadName);
    }
    public void run() {
        System.out.println("Running is: " + threadName);
        try {
            for (int i = 4; i > 0; i--) {
                System.out.println("Thread:" + threadName + "," + i);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread: " + threadName + " interrupted.");
        }
        System.out.println("Thread: " + threadName + " exiting");
    }
        public void start(){
            System.out.println("Starting " + threadName);
            if(t == null){
                t = new Thread(this, threadName);
                t.start();
            }
        }
}

class TestThread2{
    public static void main(String[] args) {
        ThreadDemo T1 = new ThreadDemo("Thread - 1");
        T1.start();
        ThreadDemo T2 = new ThreadDemo("Thread - 2");
        T2.start();
    }
}
