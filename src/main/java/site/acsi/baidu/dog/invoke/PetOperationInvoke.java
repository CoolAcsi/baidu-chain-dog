package site.acsi.baidu.dog.invoke;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import site.acsi.baidu.dog.invoke.vo.*;
import site.acsi.baidu.dog.pojo.SaleData;

/**
 * @author Acsi
 * @date 2018/2/5
 */
public interface PetOperationInvoke {
    @POST("data/market/queryPetsOnSale")
    Call<SaleData> queryList(@Body PetOnSaleRequest body);

    /**
     * header可以copy到这里
     * @param body
     * @param cookie
     * @param referer
     * @return
     */
    @Headers({
            "Accept: application/json",
            "Accept-Encoding: gzip, deflate, br"
    })
    @POST("data/txn/create")
    Call<BaseResponse> createOrder(@Body CreateOrderRequest body, @Header("Cookie") String cookie, @Header("Referer") String referer);

    @POST("data/captcha/gen")
    Call<VerificationCodeResponse> genVerificationCode(@Body BaseRequest body, @Header("Cookie") String cookie);

}
