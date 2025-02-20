package com.yun.springbootinit.controller;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池接口
 */
@RestController
@RequestMapping("/queue")
@Slf4j
// 指定只有开发环境和本地环境可用
@Profile(value = {"dev", "local"})
public class QueueController {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 给线程池添加任务
     *
     * @param name
     */
    @GetMapping("/add")
    public void add(String name) {
        CompletableFuture.runAsync(() -> {
            log.info("任务执行中：" + name + "执行线程：" + Thread.currentThread().getName());
            try {
                // 模拟工作，休眠10分钟
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 异步任务在threadPoolExecutor中执行，如果不指定，则默认放在ForkJoinPool，这个ForkJoinPool是整个项目共用的，可能会有资源紧张的情况。
        }, threadPoolExecutor);
    }

    /**
     * 返回线程池的状态信息
     *
     * @return
     */
    @GetMapping("/get")
    public String get() {
        Map<String, Object> map = new HashMap<>();
        // 获取线程池的队列长度
        int size = threadPoolExecutor.getQueue().size();
        map.put("队列长度", size);
        // 获取线程池已接收的任务总数
        long taskCount = threadPoolExecutor.getTaskCount();
        map.put("任务总数", taskCount);
        // 获取线程池已完成的任务数
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        map.put("已完成任务数", completedTaskCount);
        // 获取线程池中正在执行的线程数
        int activeCount = threadPoolExecutor.getActiveCount();
        map.put("正在执行的线程数", activeCount);
        return JSONUtil.toJsonStr(map);
    }


}
