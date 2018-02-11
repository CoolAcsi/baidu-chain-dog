package site.acsi.baidu.dog.service;

import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import site.acsi.baidu.dog.enums.PetsSortingEnum;
import site.acsi.baidu.dog.global.DoneOrderSet;
import site.acsi.baidu.dog.invoke.PetOperationInvoke;
import site.acsi.baidu.dog.invoke.vo.*;
import site.acsi.baidu.dog.pojo.*;
import site.acsi.baidu.dog.task.VerCodeTask;

import javax.annotation.Resource;
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
    private DoneOrderSet doneOrderSet;

    @Resource
    private VerCodeTask verCodeTask;


    private static final String REFERER_FORMAT = "https://pet-chain.baidu.com/chain/detail?channel=market&petId=%s&appId=1&validCode=%s";
    private static final int APP_ID = 4;
    private static final int ZERO = 0;
    private static final int FIRST_PAGE = 1;
    private static final int ONE_SECOND = 1000;

    @SneakyThrows
    public List<SaleData.Pet> queryPetsOnSale(PetsSortingEnum sortingEnum, int pageNum, int pageSize, String userName) {
        PetOnSaleRequest request = new PetOnSaleRequest();
        request.setAppId(APP_ID);
        request.setPageNo(pageNum > ZERO ? pageNum : FIRST_PAGE);
        request.setPageSize(pageSize);
        request.setQuerySortType(sortingEnum.name());
        request.setRequestId(System.currentTimeMillis());
        request.setTpl("wallet");
        request.setPetIds(Lists.newArrayList());
        Call<SaleData> saleDataCall = petOperationInvoke.queryList(request);
        SaleData saleData = saleDataCall.execute().body();

        if (saleData == null || saleData.getData() == null) {
            log.info("== 宠物商店返回数据异常 user:{} response:{}", userName, saleData);
            Thread.sleep(2 * ONE_SECOND);
            return queryPetsOnSale(sortingEnum, pageNum, pageSize, userName);
        }
        return saleData.getData().getPetsOnSale();
    }

    @SneakyThrows
    public CreateOrderStatus createOrder(Acount acount, String petId, String amount, String validCode) {

        // 获取验证码信息
        VerificationCode verCode = verCodeTask.getVerCodeInfo(acount);
        // 生单
        CreateOrderRequest request = initCreateOrderRequest(petId, amount, validCode, verCode.getSeed(), verCode.getCode());
        String referer = String.format(REFERER_FORMAT, petId, validCode);
        Call<BaseResponse> call = petOperationInvoke.createOrder(request, acount.getCookie(), referer);
        BaseResponse response = call.execute().body();
        Preconditions.checkNotNull(response, "提交订单网络异常 user:{}", acount.getDes());

        switch (response.getErrorNo()) {
            case "00":
                break;
            case "100":
                log.info("=== 验证码不正确，重新识别 user:{}", acount.getDes());
                return createOrder(acount, petId, amount, validCode);
            case "10002":
                doneOrderSet.add(petId);
            default:
                log.info("=== 生单返回状态码错误 user:{} response:{}", acount.getDes(), response);
                log.info("=== 生单返回状态码错误，暂停交易 user:{} response:{}", acount.getDes(), response);
                Thread.sleep(10 * ONE_SECOND);
                break;
        }

        return new CreateOrderStatus("00".equals(response.getErrorNo()),
                response.getErrorNo() + response.getErrorMsg());
    }

    private CreateOrderRequest initCreateOrderRequest(String petId,
                                                      String amount,
                                                      String validCode,
                                                      String seed,
                                                      String parseResult) {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setAppId(APP_ID);
        request.setPetId(petId);
        request.setAmount(amount);
        request.setRequestId(System.currentTimeMillis());
        request.setValidCode(validCode);
        request.setSeed(seed);
        request.setCaptcha(parseResult);
        request.setTpl("");
        return request;
    }
}
