package site.acsi.baidu.dog.invoke;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import site.acsi.baidu.dog.vo.MarketListMergeRequest;
import site.acsi.baidu.dog.vo.MarketListResponse;
import site.acsi.baidu.dog.vo.base.BaseResponse;


/**
 * @author Acsi
 * @date 2018/2/20
 */
public interface DogMarketInvoke {

    /**
     * 将刷新的宠物市场列表传给中心服务器
     * @param request 宠物市场列表
     * @return response
     */
    @POST("/dogMarket/merge")
    Call<BaseResponse> merge(@Body MarketListMergeRequest request);

    /**
     * 获取最新的宠物市场列表
     * @return 列表
     */
    @GET("/dogMarket/marketList")
    Call<MarketListResponse> list();
}
