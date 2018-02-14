package site.acsi.baidu.dog.service;

import java.io.IOException;

/**
 * @author Acsi
 * @date 2018/2/14
 */
public interface IVerCodeParseService {
    /**
     * 验证码识别
     * @param imgData 图像数据
     * @return 识别结果
     */
    String predict(String imgData) throws IOException;
}
