package com.yun.springbootinit.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: cheny
 * @Description: TODO
 * @Date: 2023/7/22 22:08
 */
// @Configuration 是 Spring 框架中的一个注解，它用于类级别，表明这个类是一个配置类，其目的是允许在应用程序上下文中定义额外的 bean。
@Configuration
public class ThreadPoolExecutorConfig {
    // 此方法的作用：项目启动时创建一个threadPoolExecutor对象并作为Bean保存在Spring容器中等待使用。
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {

        // ThreadFactory是一个接口，这里自定义一个实现类，重写newThread方法创建一个Thread并返回
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count = 1;

            @Override
            // @NotNull Runnable r表示方法参数r 应该永远不为null，如果该方法调用的时候传递了一个null，就会报错
            public Thread newThread(@NotNull Runnable r) {
                // 这个参数r 需要传到Thread的构造方法中，不然CompletableFuture.runAsync不运行，不打印日志
                Thread thread = new Thread(r);
                thread.setName("线程" + count);
                // count递增
                count++;
                return thread;
            }
        };
        // 创建一个新的线程池，线程池核心大小为2，最大线程数为4，非核心线程空闲时间为100秒，任务队列为阻塞队列，长度为4，使用自定义的线程工厂创建线程
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,
                4,
                100,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4),
                threadFactory
        );
        return threadPoolExecutor;
    }
}
