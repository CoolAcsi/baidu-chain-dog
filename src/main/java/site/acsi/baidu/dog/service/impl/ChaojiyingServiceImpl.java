package site.acsi.baidu.dog.service.impl;

import org.springframework.stereotype.Service;
import retrofit2.Call;
import site.acsi.baidu.dog.config.VarificationCodeConfig;
import site.acsi.baidu.dog.invoke.CodeParseInvoke;
import site.acsi.baidu.dog.invoke.vo.CodeParseResponse;
import site.acsi.baidu.dog.service.IVerCodeParseService;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Acsi
 * @date 2018/2/14
 */
@Service("chaojiying")
public class ChaojiyingServiceImpl implements IVerCodeParseService {

    @Resource
    private VarificationCodeConfig config;

    @Resource
    private CodeParseInvoke invoke;

    @Override
    public String predict(String imgData) throws IOException {
        Call<CodeParseResponse> call = invoke.parse(
                config.getUser(),
                config.getPass(),
                config.getSoftId(),
                config.getCodeType(),
                config.getLenMin(),
                imgData);
        CodeParseResponse response = call.execute().body();
        if (null != response && 0 == response.getErrNo()) {
            return response.getPicStr();
        }
        return "0000";
    }
}
