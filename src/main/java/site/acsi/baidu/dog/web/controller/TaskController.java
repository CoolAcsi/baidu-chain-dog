package site.acsi.baidu.dog.web.controller;

import org.springframework.web.bind.annotation.*;
import site.acsi.baidu.dog.config.GlobalConfig;
import site.acsi.baidu.dog.pojo.GlobalConfigBean;
import site.acsi.baidu.dog.task.BuyTask;
import site.acsi.baidu.dog.task.VerCodeTask;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Acsi
 * @date 2018/2/14task
 */
@RequestMapping("/")
@RestController
public class TaskController {

    @Resource
    private GlobalConfig config;

    @Resource
    private BuyTask buyTask;

    @Resource
    private VerCodeTask verCodeTask;

    @PostConstruct
    private void init() {
        buyTask.initTask();
        config.getConfig().getAcounts().forEach(account -> buyTask.doTask(account));
    }

    @PostMapping("/buyTask")
    public String task(@RequestBody GlobalConfigBean config) {
        this.config.setConfig(config);
        verCodeTask.init();
        buyTask.initTask();
        config.getAcounts().forEach(account -> buyTask.doTask(account));
        return "ok";
    }

    @RequestMapping("/health")
    public String health() {
        return "ok";
    }
}
