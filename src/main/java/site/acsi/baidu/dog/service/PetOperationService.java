package site.acsi.baidu.dog.service;

import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import site.acsi.baidu.dog.config.VarificationCodeConfig;
import site.acsi.baidu.dog.enums.PetsSortingEnum;
import site.acsi.baidu.dog.invoke.CodeParseInvoke;
import site.acsi.baidu.dog.global.DoneOrderSet;
import site.acsi.baidu.dog.invoke.PetOperationInvoke;
import site.acsi.baidu.dog.invoke.vo.*;
import site.acsi.baidu.dog.pojo.CreateOrderStatus;
import site.acsi.baidu.dog.pojo.SaleData;
import site.acsi.baidu.dog.pojo.VerificationCodeData;
import site.acsi.baidu.dog.util.ImageUtils;
import site.acsi.baidu.dog.util.VerificationCodeUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@Service
@Slf4j
public class PetOperationService {

    @Resource
    private PetOperationInvoke petOperationInvoke;

    @Resource
    private CodeParseInvoke codeParseInvoke;

    @Resource
    private VarificationCodeConfig varificationCodeConfig;

    @Resource
    private DoneOrderSet doneOrderSet;

    @Resource
    private ImageUtils imageUtils;

    @Resource
    private VerificationCodeUtils verificationCodeUtils;

    private static final String REFERER_FORMAT = "https://pet-chain.baidu.com/chain/detail?channel=market&petId=%s&appId=1&validCode=%s";

    @SneakyThrows
    public List<SaleData.Pet> queryPetsOnSale(PetsSortingEnum sortingEnum, int pageNum, int pageSize) {
        PetOnSaleRequest request = new PetOnSaleRequest();
        request.setAppId(4);
        request.setPageNo(pageNum > 0 ? pageNum : 1);
        request.setPageSize(pageSize);
        request.setQuerySortType(sortingEnum.name());
        request.setRequestId(System.currentTimeMillis());
        request.setTpl("wallet");
        request.setPetIds(Lists.newArrayList());
        Call<SaleData> saleDataCall = petOperationInvoke.queryList(request);
        SaleData saleData = saleDataCall.execute().body();
        // 如果连接尚未恢复则停100s
        if (saleData == null) {
            Thread.sleep(20000);
            return queryPetsOnSale(sortingEnum, pageNum, pageSize);
        }
        Preconditions.checkNotNull(saleData, "查询宠物商店返回数据为空");
        if (saleData.getData() == null) {
            log.info("== 宠物商店data为空 response:{}", saleData);
            Thread.sleep(3000);
        }
        Preconditions.checkNotNull(saleData.getData(), "查询宠物商店返回数据为空");
        return saleData.getData().getPetsOnSale();
    }

    @SneakyThrows
//    @Async
    public CreateOrderStatus createOrder(String cookie, String petId, String amount, String validCode) {

        // 获取验证码
        VerificationCodeData codeData = genVerificationCode(cookie);
        log.info("=== 获取验证码成功");
        // 识别验证码
        String parseResult = verificationCodeUtils.predict(codeData.getImg());
        log.info("=== 验证码识别成功");
        // 生单
        CreateOrderRequest request = new CreateOrderRequest();
        request.setAppId(4);
        request.setPetId(petId);
        request.setAmount(amount);
        request.setRequestId(System.currentTimeMillis());
        request.setValidCode(validCode);
        request.setSeed(codeData.getSeed());
        request.setCaptcha(parseResult);
        request.setTpl("");

        String referer = String.format(REFERER_FORMAT, petId, validCode);
        Call<BaseResponse> call = petOperationInvoke.createOrder(request, cookie, referer);
        BaseResponse response = call.execute().body();
        Preconditions.checkNotNull(response, "提交订单网络异常");
        if ("03".equals(response.getErrorNo()) || "10003".equals(response.getErrorNo())) {
            log.info("============= 暂停交易，code:{}", response.getErrorNo());
            Thread.sleep(30000);
//            createOrder(cookie, petId, amount, validCode);
        } else if ("100".equals(response.getErrorNo())) {
            log.info("=== 验证码不正确，重新识别");
            return createOrder(cookie, petId, amount, validCode);
        } else if (!"00".equals(response.getErrorNo()) && !"10002".equals(response.getErrorNo())) {
            log.info("=== 生单返回状态码错误");
            Thread.sleep(10000);
        }

        CreateOrderStatus status = new CreateOrderStatus("00".equals(response.getErrorNo()), response.getErrorNo() + response.getErrorMsg());
        log.info("===  success:{} message:{} petid:{} amount:{}", status.getSuccess(), status.getMessage(), petId, amount);
        if (status.getSuccess()) {
            log.info("*****************************  success  *******************************");
            doneOrderSet.add(petId);
        }
        if (status.getMessage().startsWith("10002")) {
            doneOrderSet.add(petId);
        }
        return status;
    }

    @SneakyThrows
    private VerificationCodeData genVerificationCode(String cookie) {
        BaseRequest request = new BaseRequest();
        request.setAppId(4);
        request.setRequestId(System.currentTimeMillis());
        request.setTpl("");
        Call<VerificationCodeResponse> call = petOperationInvoke.genVerificationCode(request, cookie);
        VerificationCodeResponse response = call.execute().body();
        if (response == null) {
            log.info("=== 返回验证码数据为空");
            Thread.sleep(5000);
        }
        Preconditions.checkNotNull(response);
        if (response.getData() == null) {
            log.info("== 验证码data为空 response:{}", response);
            log.info("cookie: {}", cookie);
            Thread.sleep(500);
            return genVerificationCode(cookie);
        }
        Preconditions.checkNotNull(response.getData());
        return response.getData();
    }

    @SneakyThrows
    private String parseVarificationCode(String img) {
        for (int i = 0; i < 5; i++) {
            try {
                Call<CodeParseResponse> call = codeParseInvoke.parse(
                        varificationCodeConfig.getUser(),
                        varificationCodeConfig.getPass(),
                        varificationCodeConfig.getSoftId(),
                        varificationCodeConfig.getCodeType(),
                        varificationCodeConfig.getLenMin(),
                        img);
                CodeParseResponse response = call.execute().body();
                if (null != response) {
                    return response.getPicStr();
                }
            } catch (Exception e) {
                log.warn("---------------  超级鹰解析失败, msg:{}", e.getMessage());
            }
        }
        return "0000";
    }

    /**
     * 替换html中的base64图片数据为实际图片
     *
     * @param cookie cookie
     * @return
     */
    @Deprecated
    public String genVerificationCodeImg(String cookie) {
        String imgData = genVerificationCode(cookie).getImg();
        Preconditions.checkNotNull(imgData);
        return genVerCodeImgByDataUrl(imgData, parseVarificationCode(imgData));
    }

    private String genVerCodeImgByDataUrl(String imgData, String code) {
        File file = new File(varificationCodeConfig.getVerCodeStorePath());
        Preconditions.checkArgument(file.exists());
        String ext = "jpg";
        // 文件名
        String fileName = code + "." + ext;
        String filePath = varificationCodeConfig.getVerCodeStorePath() + File.separator + fileName;
        try {
            // 转成文件
            imageUtils.convertBase64DataToImage(imgData, filePath);
        } catch (IOException e) {
            log.error("生成验证码图片时发生异常", e);
        }
        return filePath;
    }

}
