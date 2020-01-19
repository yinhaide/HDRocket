package com.de.rocket.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 异步、延时、周期任务、倒计时等功能线程池工具类
 * Created by haide.yin(haide.yin@tcl.com) on 2020/1/8 10:20.
 *
 * 一、为什么要引入线程池？
 *  1）new Thread()的缺点
 *      1.每次new Thread()耗费性能
 *      2.调用new Thread()创建的线程缺乏管理，被称为野线程，而且可以无限制创建，之间相互竞争，会导致过多占用系统资源导致系统瘫痪。
 *      3.不利于扩展，比如如定时执行、定期执行、线程中断
 *  2）采用线程池的优点
 *      1.重用存在的线程，减少对象创建、消亡的开销，性能佳
 *      2.可有效控制最大并发线程数，提高系统资源的使用率，同时避免过多资源竞争，避免堵塞
 *      3.提供定时执行、定期执行、单线程、并发数控制等功能
 * 二、为什么不用Timer做定时和延时任务？
 *  1）Timer的特点
 *      1.Timer是单线程模式；
 *      2.如果在执行任务期间某个TimerTask耗时较久，那么就会影响其它任务的调度；
 *      3.Timer的任务调度是基于绝对时间的，对系统时间敏感；
 *      4.Timer不会捕获执行TimerTask时所抛出的异常，由于Timer是单线程，所以一旦出现异常，则线程就会终止，其他任务也得不到执行。
 *  2）ScheduledThreadPoolExecutor的特点
 *      1.ScheduledThreadPoolExecutor是多线程
 *      2.多线程，单个线程耗时操作不会影响影响其它任务的调度
 *      3.基于相对时间，对系统时间不敏感
 *      4.多线程，单个任务的执行异常不会影响其他线程
 * 三、四种线程池的区别与用途
 *  1）定长线程池（FixedThreadPool）
 *      1.特点：只有核心线程而且不会被回收、线程数量固定、任务队列无大小限制（超出的线程任务会在队列中等待,会阻塞）
 *      2.应用场景：控制线程最大并发数
 *  2）定时线程池（ScheduledThreadPool ）
 *      1.核心线程数量固定、非核心线程数量无限制（闲置时马上回收），不会阻塞，除非线程数超过CPU处理能力
 *      2.应用场景：执行定时以及周期性任务
 *  3）可缓存线程池（CachedThreadPool）
 *      1.特点：只有非核心线程、线程数量不固定（可无限大）、灵活回收空闲线程（具备超时机制，全部回收时几乎不占系统资源）、新建线程（无线程可用时），不会阻塞
 *      2.应用场景：执行大量、耗时少的线程任务
 *  4）单线程化线程池（SingleThreadExecutor）
 *      1.特点：只有一个核心线程（保证所有任务按照指定顺序在一个线程中执行，不需要处理线程同步的问题），会阻塞
 *      2.应用场景：不适合并发但可能引起IO阻塞性及影响UI线程响应的操作，如数据库操作，文件操作等
 * 四、本工具类的使用方法对应的业务场景(一句话即可)
 *  1）异步操作
 *      ExecutorUtil.get().execute(runnable);
 *  2）主线程中更新UI
 *      ExecutorUtil.get().postMainLooper(runnable);
 *  3）延时异步操作(多次调用不会阻塞)
 *      ScheduledFuture scheduledFuture = ExecutorUtil.get().sheduleDelay(delayMilSecond,runnable);
 *      scheduledFuture.cancel(true);//取消操作
 *  4）计数器操作(多次调用不会阻塞)
 *      ScheduledFuture scheduledFuture = ExecutorUtil.get().sheduleCount(periodMilSecond,coundMilSecond,runnable);
 *      scheduledFuture.cancel(true);//取消操作
 *  5）定时器操作(多次调用不会阻塞)
 *      ScheduledFuture scheduledFuture = ExecutorUtil.get().sheduleFixedRate(delayMilSecond,periodMilSecond,runnable);
 *      scheduledFuture.cancel(true);//取消操作
 *      ScheduledFuture scheduledFuture = ExecutorUtil.get().sheduleFixedDelay(delayMilSecond,periodMilSecond,runnable);
 *      scheduledFuture.cancel(true);//取消操作
 */
public class ExecutorUtil {

    //单例
    private volatile static ExecutorUtil instance;
    //核心线程数，目前不给外面设置，尽量保持较小的核心线程数
    private static final int CORE_POOL_SIZE = 3;
    //定时线程池，固定核心线程，无限线程数，任务超过核心线程数量不会阻塞
    private ScheduledExecutorService scheduledExecutorService;
    //定长线程池，固定核心线程与最大线程数，任务超过核心线程数量会阻塞
    private ExecutorService fixedThreadPool;
    //定长线程池，没有核心线程，无限线程数，任务超过核心线程数量不会阻塞
    private ExecutorService cachedThreadPool;
    //单线程化线程池，1个核心线程与最大线程数，任务超过核心线程数量会阻塞
    private ExecutorService singleThreadExecutor;
    //主线程的消息队列，用于执行异步操作之后更新UI
    private Handler handler;
    //计数器,临时存的变量
    private int count = 0;
    //计数定时器的引用,临时存的变量
    private ScheduledFuture scheduledFuture = null;

    /**
     * 单例
     */
    public static ExecutorUtil get() {
        if (instance == null) {
            synchronized (ExecutorUtil.class) {
                if (instance == null) {
                    instance = new ExecutorUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 构造函数
     */
    private ExecutorUtil() {
        handler = new Handler(Looper.getMainLooper());
    }

    /* ********************************** 获取主线程更新UI的操作 ***************************************** */

    /**
     * 主线程运行，用户异步结果之后更新UI
     * @param runnable 接口
     */
    public void postMainLooper(Runnable runnable) {
        handler.post(runnable);
    }

    /* ********************************** 异步线程的的操作 ***************************************** */

    /**
     * 执行无需返回值的异步任务,默认是用定长线程池（CashedThreadPool）来执行异步操作
     * 这里的异步需求是那些比较短小的耗时任务，不用于较长的耗时任务
     * @param runnable 接口
     */
    public void execute(Runnable runnable) {
        executeCashed(runnable);
    }
    /**
     * 用线程池（CashedThreadPool）来执行异步操作
     * @param runnable 接口
     */
    public void executeCashed(Runnable runnable) {
        getCachedThreadPool().execute(runnable);
    }

    /**
     * 用线程池（FixedThreadPool）来执行异步操作
     * @param runnable 接口
     */
    public void executeFixed(Runnable runnable) {
        getFixedThreadPool().execute(runnable);
    }

    /**
     * 用线程池（SingleThreadExecutor）来执行异步操作
     * @param runnable 接口
     */
    public void executeSingle(Runnable runnable) {
        getSingleThreadExecutor().execute(runnable);
    }

    /**
     * 用线程池（ScheduledThreadPool）来执行异步操作
     * @param runnable 接口
     */
    public void executeShedule(Runnable runnable) {
        getScheduledExecutor().execute(runnable);
    }

    /* ********************************** 值针对定时器线程池的操作 ***************************************** */

    /**
     * 延时操作
     * @param delayMilSecond 延时的时间
     * @param runnable 回调
     * @return 定时器对象
     */
    public ScheduledFuture sheduleDelay(long delayMilSecond,Runnable runnable){
        return getScheduledExecutor().schedule(runnable,delayMilSecond, TimeUnit.MILLISECONDS);
    }

    /**
     * 定时器，该方法设置了执行周期，下一次执行时间相当于是上一次的执行时间加上period，它是采用已固定的频率来执行任务
     * @param periodMilSecond 计数间隔
     * @param runnable 回调
     * @return 定时器对象
     */
    public ScheduledFuture sheduleFixedRate(long delayMilSecond,long periodMilSecond, Runnable runnable){
        return getScheduledExecutor().scheduleAtFixedRate(runnable,delayMilSecond,periodMilSecond,TimeUnit.MILLISECONDS);
    }

    /**
     * 定时器，该方法设置了执行周期，与scheduleAtFixedRate方法不同的是，下一次执行时间是上一次任务执行完的系统时间加上period，
     * 因而具体执行时间不是固定的，但周期是固定的，是采用相对固定的延迟来执行任务
     * @param periodMilSecond 计数间隔
     * @param runnable 回调
     * @return 定时器对象
     */
    public ScheduledFuture sheduleFixedDelay(long delayMilSecond,long periodMilSecond, Runnable runnable){
        return getScheduledExecutor().scheduleWithFixedDelay(runnable,delayMilSecond,periodMilSecond,TimeUnit.MILLISECONDS);
    }

    /**
     * 计数器的实现，可实现倒计时等相关操作
     * @param periodMilSecond 计数间隔
     * @param coundMilSecond 总共需要计数的时间
     * @param runnable 回调
     * @return 定时器对象
     */
    public ScheduledFuture sheduleCount(long periodMilSecond,long coundMilSecond,Runnable runnable){
        count = 0;
        scheduledFuture = sheduleFixedRate(0, periodMilSecond, () -> {
            count++;
            long currentTime = periodMilSecond * count;
            if (currentTime > coundMilSecond && scheduledFuture != null && !scheduledFuture.isCancelled()) {
                scheduledFuture.cancel(true);
                return;
            }
            postMainLooper(runnable);
        });
        return scheduledFuture;
    }

    /* ********************************* 按需创建指定类型的线程池，需要的时候才创建 ***************************************** */

    /**
     * 按需创建指定类型的线程池，需要的时候才创建
     * @return 线程池对象
     */
    private ScheduledExecutorService getScheduledExecutor(){
        //不存在就创建
        if(isExecutorNoExist(scheduledExecutorService)){
            scheduledExecutorService = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
        }
        return scheduledExecutorService;
    }

    /**
     * 按需创建指定类型的线程池，需要的时候才创建
     * @return 线程池对象
     */
    private ExecutorService getFixedThreadPool(){
        //不存在就创建
        if(isExecutorNoExist(fixedThreadPool)){
            fixedThreadPool = Executors.newFixedThreadPool(CORE_POOL_SIZE);
        }
        return fixedThreadPool;
    }

    /**
     * 按需创建指定类型的线程池，需要的时候才创建
     * @return 线程池对象
     */
    private ExecutorService getCachedThreadPool(){
        //不存在就创建
        if(isExecutorNoExist(cachedThreadPool)){
            cachedThreadPool = Executors.newCachedThreadPool();
        }
        return cachedThreadPool;
    }

    /**
     * 按需创建指定类型的线程池，需要的时候才创建
     * @return 线程池对象
     */
    private ExecutorService getSingleThreadExecutor(){
        //不存在就创建
        if(isExecutorNoExist(singleThreadExecutor)){
            singleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        return singleThreadExecutor;
    }

    /**
     * 判断线程池是否存在
     * @param executorService 线程池
     */
    private boolean isExecutorNoExist(ExecutorService executorService){
        //executorService.isTerminated()用于判断执行shutdown后，是否所有的线程都被关闭了，
        return executorService == null || executorService.isShutdown();
    }

    /* ********************************** 关闭线程池的操作 ***************************************** */

    // 默认情况下无需关闭线程池，特别是定时线程池，关闭的话会关闭整个线程池，影响其他地方的业务
    // 为什么可以不用shutdown呢？即使四个线程池都是用，最后核心线程也只有3+3+1=7个，并不会占用多少内存。反复的shutdown，然后创建线程池反而会加大内存开销
    // 对于延时、定时、计数的操作，如果需要关闭单独的线程，需要缓存返回的ScheduledFuture，进行对应的cancel操作。
    // 但是如果有特定的关闭需求，可以直接这样关闭指定的线程池
    /*private void shutdown(){
        //表示不再接受新的任务，并把任务队列中的任务直接移出掉，如果有正在执行的，立刻停止;
        getFixedThreadPool().shutdown();
        //并不是直接关闭线程池，而是不再接受新的任务.如果线程池内有任务，那么把这些任务执行完毕后，关闭线程池
        getFixedThreadPool().shutdownNow();
    }*/
}
