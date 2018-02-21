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

    @Headers({
            "Accept: application/json",
            "Accept-Encoding: gzip, deflate, br",
            "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8",
            "Connection: keep-alive",
            "Host: pet-chain.baidu.com",
            "Origin: https://pet-chain.baidu.com",
            "Referer: https://pet-chain.baidu.com/chain/dogMarket"
    })
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
            "Accept-Encoding: gzip, deflate, br",
            "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8",
            "Host: pet-chain.baidu.com",
            "Origin: https://pet-chain.baidu.com",
            "User-Agent: Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Mobile Safari/537.36"
    })
    @POST("data/txn/create")
    Call<BaseResponse> createOrder(@Body CreateOrderRequest body, @Header("Cookie") String cookie, @Header("Referer") String referer);

    @POST("data/captcha/gen")
    Call<VerificationCodeResponse> genVerificationCode(@Body BaseRequest body, @Header("Cookie") String cookie);

    @POST("data/pet/queryPetById")
    Call<PetQueriedByIdResponse> queryPetById(@Body PetQueriedByIdRequest body);

}
