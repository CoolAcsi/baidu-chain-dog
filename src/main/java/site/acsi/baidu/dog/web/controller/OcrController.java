package site.acsi.baidu.dog.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
public class OcrController {

    @Resource
    private VerificationCodeUtils verificationCodeUtils;

    /**
     * 验证码识别接口
     * @param request request body
     * @return 识别结果
     */
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
