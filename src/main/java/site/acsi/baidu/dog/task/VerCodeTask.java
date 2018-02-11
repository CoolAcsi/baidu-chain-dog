package site.acsi.baidu.dog.task;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import site.acsi.baidu.dog.config.GlobalConfig;
import site.acsi.baidu.dog.invoke.PetOperationInvoke;
import site.acsi.baidu.dog.invoke.vo.BaseRequest;
import site.acsi.baidu.dog.invoke.vo.VerificationCodeResponse;
import site.acsi.baidu.dog.pojo.Acount;
import site.acsi.baidu.dog.pojo.VerificationCode;
import site.acsi.baidu.dog.pojo.VerificationCodeData;
import site.acsi.baidu.dog.util.ImageUtils;
import site.acsi.baidu.dog.util.VerificationCodeUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author Acsi
 * @date 2018/2/10
 */

@Slf4j
@Component
@EnableScheduling
public class VerCodeTask {

    @Resource
    private PetOperationInvoke petOperationInvoke;

    @Resource
    private VerificationCodeUtils verificationCodeUtils;

    @Resource
    private BuyTask buyTask;

    @Resource
    private GlobalConfig config;

    @Resource
    private ImageUtils imageUtils;

    private Map<Acount, Queue<VerificationCode>> queueMap = Maps.newConcurrentMap();


    private static final int APP_ID = 4;
    private static final int ONE_SECOND = 1000;
    private static final int SAFE_QUEUE_SIZE = 10;
    private static final int VALID_TIME = 600000;

    @PostConstruct
    @SneakyThrows
    private void init() {
        config.getConfig().getAcounts().forEach((acount -> queueMap.put(acount, Lists.newLinkedList())));
        for (Acount acount : config.getConfig().getAcounts()) {
            buyTask.doTask(acount);
            Thread.sleep(config.getConfig().getTime()/config.getConfig().getAcounts().size());
        }
    }

    public VerificationCode getVerCodeInfo(Acount acount) {
        if (queueMap.get(acount).isEmpty()) {
            genVerCodeByAcount(acount);
        }
        return queueMap.get(acount).poll();
    }

    @Scheduled(fixedRate = 1000)
    public void doTask() {
        List<Acount> amounts = config.getConfig().getAcounts();
        Acount acount = amounts.get((int) (System.currentTimeMillis() % amounts.size()));
        genVerCodeByAcount(acount);
    }

    private void genVerCodeByAcount(Acount acount) {
        Queue<VerificationCode> queue = queueMap.get(acount);
        while (!queue.isEmpty()) {
            if (System.currentTimeMillis() - queue.peek().getCreateTime() > VALID_TIME) {
                queue.poll();
            } else {
                break;
            }
        }
        if (queue.size() < SAFE_QUEUE_SIZE) {
            VerificationCodeData data = genVerificationCode(acount);
            try {
                String code = verificationCodeUtils.predict(data.getImg());
                queue.offer(
                        new VerificationCode(
                                data.getSeed(),
                                code,
                                System.currentTimeMillis()));
                if (config.getConfig().getLogSwitch()) {
                    log.info("储备验证码成功，user:{} code:{}", acount.getDes(), code);
                }
                if (config.getConfig().getExportSwitch()) {
                    imageUtils.convertBase64DataToImage(data.getImg(), config.getConfig().getExportVerCodeImgPath() + "/" + code + ".jpg");
                }
            } catch (IOException e) {
                if (config.getConfig().getLogSwitch()) {
                    log.error("识别验证码失败", e);
                }
            }
        }
    }

    @SneakyThrows
    private VerificationCodeData genVerificationCode(Acount acount) {
        BaseRequest request = new BaseRequest();
        request.setAppId(APP_ID);
        request.setRequestId(System.currentTimeMillis());
        request.setTpl("");
        Call<VerificationCodeResponse> call = petOperationInvoke.genVerificationCode(request, acount.getCookie());
        VerificationCodeResponse response = call.execute().body();
        if (response == null) {
            log.info("=== 返回验证码数据为空 user:{}", acount.getDes());
            Thread.sleep(5 * ONE_SECOND);
        }
        Preconditions.checkNotNull(response);
        if (response.getData() == null) {
            log.info("== 验证码data为空，可能需要更新cookie user:{}, response:{}", acount.getDes(), response);
            Thread.sleep(ONE_SECOND);
            return genVerificationCode(acount);
        }
        Preconditions.checkNotNull(response.getData());
        return response.getData();
    }

}
