package site.acsi.baidu.dog.web.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.POST;
import site.acsi.baidu.dog.task.TestTask;
import site.acsi.baidu.dog.util.VerificationCodeUtils;
import site.acsi.baidu.dog.vo.VerCodeParseRequest;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@Slf4j
@RestController
public class TestController {

    @Resource
    private TestTask task;

    @Resource
    private VerificationCodeUtils verificationCodeUtils;


    @RequestMapping("/start")
    @SneakyThrows
    public String hello() {

        task.doTask("你的cookie");
        task.doTask("你的另一个账号cookie");
        // 添加更多账号只需要copy以上代码即可
        return "ok";
    }

    @RequestMapping("/health")
    public String health() {
        return "ok";
    }

    @RequestMapping(value="/ocr", method=RequestMethod.POST)
    public String ocr(@RequestBody VerCodeParseRequest request) {
        try {
            return verificationCodeUtils.predict(request.getImg());
        } catch (IOException e) {
            log.error("验证码识别失败", e);
            return "";
        }
    }
}
