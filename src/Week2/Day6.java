package Week2;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day6 {
}
/**
 *  code
 *  gc
 *  jvm tranlater
 *  machine
 */
/**
 *
 * volatile  + cas compare and set/swap (atomic instruction)
 *      i=0
 *      t1          t2
 *      0            0
 *    exp=0       exp=0
 *  1 if cur==exp(succ)  1 if cur!=exp(fail)
 *                  exp=1
 *                  2 if cur==exp
 *  compareAndSet(Object, offset, expectValue(Prev Read Value), NewValue);
 *                  target attribute
 *         optimistic lock
 *              pros:
 *                  more read than write, improve performance
 *              cons:
 *                  a lot write op, waste cpu
 */

/**
 * ThreadSafe Library
 *      HashTable
 *          synchronized the method(on the whole table)
 *      ConcurrentHashMap
 *          synchronized on the Node
 *          Node[]
 *      Stack
 */

//Main thread(worker) execute your public static void main(String[] args)
//t1
//t2
class ThreadSafeLibrary{
    static AtomicInteger ai = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for(int i = 0; i < 10000; i++){
                ai.incrementAndGet();
            }
        });
        Thread t2 = new Thread(() -> {
            for(int i = 0; i < 10000; i++){
                ai.incrementAndGet();
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(ai.get());
    }
}
/**
 * Reentrant Lock
 *
 *      fair lock
 *      multip wl (condition)
 *      tryLock
 *
 *      condition (waitlist) provide await()、signal()、signalAll()
 *      原理和synchronized锁对象的wait()、notify()、notifyAll()是一致的，并且其行为也是一样的：
 *      creating critical code block
 *      lock()
 *      lock()
 *      unlock()
 *      unlock()
 * compare to synchronized
 *       same: create critical code block: pessimistic lock
 *       diff: synchr doesn't have fair lock, provide multip wl
 */
class TestReentrantLock{
    //t1(A) t2(B) t3(C), A B C ...
    static ReentrantLock lock = new ReentrantLock(true);
    static Condition t1wl = lock.newCondition();
    static Condition t2wl = lock.newCondition();
    static Condition t3wl = lock.newCondition();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                lock.lock();
                for (int i = 0; i < 10; i++) {
                    System.out.println("A");
                    t2wl.signal();
                    t1wl.await();
                }
                lock.unlock();
            }catch(Exception ex) {
                System.out.println(ex);
            }
        });
        Thread t2 = new Thread(() -> {
           try{
               Thread.currentThread().sleep(10);
               lock.lock();
               for(int i = 10; i < 10; i++){
                   System.out.println("B");
                   t3wl.signal();
                   t2wl.await();
               }
               lock.lock();
           }catch(Exception ex){
               System.out.println(ex);
           }
        });
        Thread t3 = new Thread(() -> {
           try{
               Thread.currentThread().sleep(20);
               lock.lock();
               for(int i = 0; i < 10; i++){
                   System.out.println("C");
                   t1wl.signal();
                   t3wl.await();
               }
               lock.unlock();
           }catch(Exception ex){
               System.out.println(ex);
           }
        });
        t1.start();
        t2.start();
        t3.start();
    }
}
class Test11{
    public static void main(String[] args) {
        Boolean fair = false;
        System.out.println(fair ? "this is true" : "this is false");
    }
}

/**
 * DeadLock
 *      2 resource
 *      lock1 lock2
 *      t1                t2
 *      lock1             lock2           -> lock1
 *      try get lock2     try to get lock1 ->lock2
 *
 *      Sol1: release the lock you current holding after a timeout
 *      Sol2: lock in same order
 *      *Sol3: create a look up table to check which thread hold which lock, and prevent deadlock happened
 *
 *
 */
class Test12{
    public static void main(String[] args) {
        Object l1 = new Object();
        Object l2 = new Object();
        Thread t1 = new Thread(() -> {
            synchronized(l1){
                try{
                    Thread.currentThread().sleep(1000);
                }catch(Exception ex){
                }
                synchronized (l2){
                    System.out.println("real work in t1");
                }
            }
        });
        Thread t2 = new Thread(() -> {
           synchronized (l1){
               try{
                   Thread.currentThread().sleep(1000);
               }catch(Exception ex){
               }
               synchronized (l2){
                   System.out.println("real work in t2");
               }
           }
        });
        t1.start();
        t2.start();
    }
}

/**
 * Blocking Queue
 *      like a buffer
 *    producer(t1)->add ele into queue  [][][][][]  -> consumer(t2)
 *    while the queue if full, we will block the producer
 *    while the queue if empty, we will block the consumer
 *
 */
/**
 * ThreadPool
 *      creation new thread is expensive
 *      use a pool to cache
 *      reuse the worker thread
 *
 *       Executor                       Executors
 *
 *                 create threadPool:
 *                  ThreadPoolExecutor
 *                  ScheduledThreadPoolExecutor
 *                  ForkJoinPool
 *                      forkJoinTask(Big) -> t0 -> steal multip thread -> join into one result
 *                                           t1
 *                                           t2
 *                                           ...
 *            List<Integer>  {0,123,123,12,3}  ->{0, 123}           -> {0, 123^2, 123^2, 12^2, 3^2}
 *                                               {123, 12}
 *                                               {3}
 *
 *
 */

class Main11 {
    public static void main(String[] args) {
        // 创建一个固定大小的线程池:
        ExecutorService es = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 6; i++) {
            es.submit(new Task("" + i));
        }
        // 关闭线程池:
        es.shutdown();
    }
}

class Task implements Runnable {
    private final String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("start task " + name);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        System.out.println("end task " + name);
    }
}
//class TestThreadPool{
//    Executor threadPool = Executors.newFixedThreadPool(1);
//    Executors.newSingleThreadExecutor();
//    Executors.newCachedThreadPool();
//}

/**
 * Future (wrapper)
 *  get() blocking
 */
class TestFuture{
    public static void main(String[] args) throws Exception{
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        //        Future future = threadPool.submit(()->{
//            try{
//                Thread.currentThread().sleep(3000);
//            }catch (Exception ex){
//                System.out.println(ex);
//            }
//
//            System.out.println("some work");
////            throw new RuntimeException("customized runtime");
//        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    throw new FileNotFoundException();
                }catch (Exception ex){}

            }
        };
        Callable<Integer> callableTask = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                throw new FileNotFoundException("xxxx");

            }
        };
        Future<Integer> future = threadPool.submit(callableTask);
        Integer result = future.get(); // blocking method block the current thread(Main)
        System.out.println(result);
        //...operation

        System.out.println(result *2);
//        while(!future.isDone()){
//            System.out.println("work didn't finished");
//            Thread.currentThread().sleep(1000);
//        }
//        System.out.println("future task is done!");
    }
}

class CallableDemo implements Callable<Integer>{

    private int sum;
    @Override
    public Integer call() throws Exception {
        System.out.println("Callable子线程开始计算! ");
        Thread.sleep(2000);
        for(int i = 0; i < 100; i++){
            sum = sum + i;
        }
        System.out.println("Callable子线程计算结束！ ");
        return sum;
    }
}
class CallableTest{
    public static void main(String[] args) {
        ExecutorService es = Executors.newSingleThreadExecutor();//创建线程池
        CallableDemo calTask = new CallableDemo();//creat Callable Object task
        Future<Integer> future = es.submit(calTask);//提交任务并获取执行结果
        es.shutdown();//关闭线程
        try{
            Thread.sleep(2000);
            System.out.println("主线程在执行其他任务");
            if(future.get() != null){//输出获取结果
                System.out.println("furture.get() --> " + future.get());
            }else{
                System.out.println("future.get()未获取到结果 ");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("主线程在执行完成");
    }
}
/**
 *  Java 8
 *      interface
 *          default static method
 *      time
 *      Functional Interface
 *      Lambda Expression
 *      Stream API
 *      Optional
 *      Completable Future
 */
/**
 * Functional Interface
 *      interface only contains one abstract method
 */
interface NormalInterface{
    //void method0();
    //void method1();
    //void method2();
    static void method3(){
        System.out.println("xxx");
    }
    default void method4(){
        System.out.println("method4");
    }
    default void method5(){
        method4();
    }
}
interface NormalInterface2{
    //void method0;
    //void method1;
    //void method2;
    //static void method3(){
    //  Syetem.out.println("xxx);
    // }
    //private void method4(){
    //  System.out.println("method4");
    // }
    default void method5(){
        System.out.println("i am in nor2");
    }
}
class Test1111{
    static class Cld implements NormalInterface, NormalInterface2{
        @Override
        public void method5(){
            System.out.println("something");
        }
    }

    public static void main(String[] args) {
        Cld cld = new Cld();
        cld.method5();
        NormalInterface.method3();
    }
}
@FunctionalInterface
interface MyFunctionInterface{
    void method1();
    static void method2(){};
}
class MyClzz implements Runnable{
    @Override
    public void run(){}
}
/**
 * Runnable vs Callable
 *  void         return<>
 *  /            throws handle checked exception
 */
class Test71{
    public static void main(String[] args) {
//        Runnable runnable1 = new MyClzz();
//        //anonymous class implement the runnable interface, then new an object from that anonymous class
//        Runnable runnable = new Runnable(){
//            @Override
//            public void run(){
//
//            }
//        };
//        Callable callable = new Callable(){
//            @Override
//            public Object call() throws Exception{
//                return null;
//            }
//        };
        Predicate<Integer> predicate = a -> a > 10;
        System.out.println(predicate.test(5));
        Comparator<Integer> comparator = (a, b) -> a - b;
        System.out.println("comparator is used to : " + comparator.compare(10, 1));

        BiFunction<Integer, Boolean, Object> biFunction = (x1, x2) -> {
            return new Object();
        };
        System.out.println(biFunction.apply(1, true));
        Supplier<HashMap> supplier = () -> new HashMap<Integer, Integer>();
        //Consumer
//        MyFuncitonInterface myFuncitonInterface = ()->{
//            System.out.println("overring my customized fi");
//        };
//        myFuncitonInterface.method1();
    }
}
/**
 * Lambda Expression (only work with functional interface)
 *  *  (input rv)-> { method body}
 *  *  if there one input
 *  *   ...-> ...
 */
/**
 * Stream API
 *      for loop
 *          Stream obj(contains ele in your collections) stream api apply operation on each of the ele
 *      chain operation
 *      intermediate step (map(), mapToInt(), filter()...)
 *      terminate step(collect(), reduce(), forEach())
 * Parallel Stream API
 */
class Test72{
    public static void main(String[] args) {
        //Function function
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        int i = (int)list.parallelStream().
         //       map(x -> x + 10).
                map(x -> x * 10).
                reduce(0,
                        (res, x) -> {
                            System.out.println(Thread.currentThread().getName() + " working on " + res + " , " + x);
                            return res + x;
                        });
        System.out.println("final answer i is: " + i);
    }
}

class StreamDemo{
    public static void main(String[] args) {
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .parallel()
                .reduce((a, b) -> {
                    System.out.println(String.format("%s 线程: %d + %d = %d",
                            Thread.currentThread().getName(), a, b, a + b));
                    return a + b;
                }).ifPresent(System.out::println);
    }
}
/**
 * filter the employee age>=25
 */
class Employee{
    int age;

    public Employee(int age) {
        this.age = age;
    }
    @Override
    public String toString(){
        return "Employee{" + "age =" + age + '}';
    }
}
class Test73{
    public static void main(String[] args) {
        List<Employee> employeeList = new LinkedList<>();
        new Employee(18);
        employeeList.add(new Employee(18));
        employeeList.add(new Employee(20));
        employeeList.add(new Employee(25));
        employeeList.add(new Employee(30));
        employeeList.add(new Employee(35));
        //List<Employee> result = new LinkedList<>();
        // ele -> new ele
        List<Employee> res = employeeList.stream().filter(emp -> emp.age >= 25).map(emp -> {
            System.out.println(Thread.currentThread().getName() + " working on " + emp);
            emp.age += 1;
            return emp;
        }).collect(Collectors.toList());
        System.out.println(res);
    }
}
class Test111{
    public static void main(String[] args) {
        IntStream.range(0, 10).forEach(System.out::println);
    }
}

/**
 * Optional
 *      wrapper class
 *      wrap object
 *      help with null check
 *      eliminate all the "null" in code
 */
//class Test74{
//    public static void main(String[] args) throws Exception{
//        Employee emp = null;
//        Optional<Employee> optionalEmployee = Optional.of(emp);
//        if(!optionalEmployee.isPresent()){
//            System.out.println("Emp is null");
//        }
//    }
//}

class Test75{
    public static void main(String[] args) throws Exception{
//        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() ->{
//            try{
//                Thread.currentThread().sleep(3000);
//            }catch(Exception ex){
//                System.out.println(ex);
//            }
//            return 10;
//        });
//        int i = completableFuture.get();
//        System.out.println("i is :" + i);
//        completableFuture.thenAccept((x) -> {
//            System.out.println("x0 is :" + x);
//            x += 10;
//            System.out.println("x1 is :" + x);
//        });
//        CompletableFuture cf = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.currentThread().sleep(3000);
//            } catch (Exception ex) {
//                System.out.println(ex);
//            }
//            return 10;
//        }).thenApply((x) -> x + 10).thenAccept(x -> {
//            System.out.println("x2 is :" + x);
//        });
//        System.out.println("this is main");
//        System.out.println("finished all work in main");
//        cf.join();
        List<CompletableFuture> completableFutures = new LinkedList<>();
        for(int i = 0; i < 5; i++){
            final int fi = i;
            CompletableFuture<Integer> tempCf = CompletableFuture.supplyAsync(() -> {
                System.out.println("STEP1：send apis calla and gather res");
                try{
                    Thread.currentThread().sleep(fi * 1000);
                }catch(Exception ex){
                    System.out.println(ex);
                }
                return fi;
            });
            completableFutures.add(tempCf);
        }
        CompletableFuture signalCf = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
        signalCf.thenAccept(Void -> {
            for(int i = 0; i < 5; i++){
                CompletableFuture<Integer> interEleCf = completableFutures.get(i);
                interEleCf.thenAccept(x -> {
                    x += 10;
                    System.out.println("STEP2: " + x);
                }).join();
            }
        }).join();
    }
}
/**
 *  basic query
 *      select
 *      from
 *      where
 *      subQuery
 *      minus
 *      union
 *      intersect
 *      join
 *      group by
 *      aggregation funct min max avg
 *      execution
 *      select 2nd salary emp in table
 *  what transaciton ACID
 *  Table Design
 *      stu class
 *      1 -  1
 *      m -  1
 *      m -  m
 *  Normalization
 *  Index
 *      b/b+ tree
 *
 */
class Main12{
    public static void main(String[] args) throws Exception {
        //创建异步执行任务
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(Main12::fetchPrice);
        //如果执行成功
        cf.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        //如果执行异常
        cf.exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });
        Thread.sleep(200);//主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭
    }

    static Double fetchPrice(){
        try{
            Thread.sleep(100);
        }catch(InterruptedException e){
        }
        if(Math.random() < 0.3){
            throw new RuntimeException("fetch price failed");
        }
        return 5 + Math.random() * 20;
    }
}

class Main13{
    public static void main(String[] args) throws Exception {
        //第一个任务
        CompletableFuture<String> cfQuery = CompletableFuture.supplyAsync(() -> {
            return queryCode("china petroleum");
        });
        //cfQuery成功后继续执行下一个任务
        CompletableFuture<Double> cfFetch = cfQuery.thenApplyAsync((code) -> {
            return fetchPrice(code);
        });
        cfQuery.thenAccept((result) -> {
            System.out.println("name: " + result);
        });
        //cfFetch成功后打印
        cfFetch.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        Thread.sleep(2000);//主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭
    }

    static String queryCode(String name){
        try{
            Thread.sleep(100);
        }catch(InterruptedException e){
        }
        return "601857";
    }
    static Double fetchPrice(String code){
        try{
            Thread.sleep(100);
        }catch(InterruptedException e){
        }
        return 5 + Math.random() * 20;
    }
}





















