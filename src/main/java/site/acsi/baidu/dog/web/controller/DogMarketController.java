package site.acsi.baidu.dog.web.controller;

import com.google.common.base.Preconditions;
import com.sun.javafx.binding.StringFormatter;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import site.acsi.baidu.dog.enums.ApiResponseEnum;
import site.acsi.baidu.dog.global.DogMarketStore;
import site.acsi.baidu.dog.pojo.Pet;
import site.acsi.baidu.dog.vo.MarketListMergeRequest;
import site.acsi.baidu.dog.vo.MarketListResponse;
import site.acsi.baidu.dog.vo.base.BaseResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Acsi
 * @date 2018/2/18
 */
@RestController
@RequestMapping("/dogMarket")
public class DogMarketController {

    @Resource
    private DogMarketStore store;

    public static final String MARKET_LINK_FORMAT = "https://pet-chain.baidu.com/chain/detail?channel=market&petId=%s&appId=4&validCode=%s";

    @PostMapping("/merge")
    public BaseResponse merge(@RequestBody MarketListMergeRequest request) {
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(request.getPets());
        store.put(request.getPets());
        return ApiResponseEnum.SUCCESS.getResponse();
    }

    @RequestMapping("marketLink")
    @SneakyThrows
    public String marketLink(@RequestParam String petId, HttpServletResponse response) {
        Pet pet = store.queryPetById(petId);
        if (pet == null) {
            return "没有在市场上找到这只狗狗。";
        }
        response.sendRedirect(StringFormatter.format(MARKET_LINK_FORMAT, pet.getPetId(), pet.getValidCode()).getValue());
        return "";
    }

    @RequestMapping("/marketList")
    public MarketListResponse marketList() {
        MarketListResponse marketListResponse = new MarketListResponse();
        marketListResponse.setStatus(0);
        marketListResponse.setMessage("success");
        marketListResponse.setPets(store.list());
        return marketListResponse;
    }
}
