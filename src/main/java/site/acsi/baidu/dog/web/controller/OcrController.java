package site.acsi.baidu.dog.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import site.acsi.baidu.dog.config.GlobalConfig;
import site.acsi.baidu.dog.service.IVerCodeParseService;
import site.acsi.baidu.dog.vo.VerCodeParseRequest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@Slf4j
@RestController
public class OcrController {

    @Resource
    private Map<String, IVerCodeParseService> verCodeParseServiceMap;

    @Resource
    private GlobalConfig config;

    /**
     * 验证码识别接口
     * @param request request body
     * @return 识别结果
     */
    @RequestMapping(value="/ocr", method=RequestMethod.POST)
    public String ocr(@RequestBody VerCodeParseRequest request) {
        try {
            return verCodeParseServiceMap.get(config.getConfig().getVerCodeStrategy()).predict(request.getImg());
        } catch (IOException e) {
            log.error("验证码识别失败", e);
            return "";
        }
    }
}
