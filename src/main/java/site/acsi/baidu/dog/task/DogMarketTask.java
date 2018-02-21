package site.acsi.baidu.dog.task;

import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.acsi.baidu.dog.config.GlobalConfig;
import site.acsi.baidu.dog.enums.PetsSortingEnum;
import site.acsi.baidu.dog.invoke.DogMarketInvoke;
import site.acsi.baidu.dog.pojo.Pet;
import site.acsi.baidu.dog.service.PetOperationService;
import site.acsi.baidu.dog.vo.MarketListMergeRequest;
import site.acsi.baidu.dog.vo.base.BaseResponse;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Acsi
 * @date 2018/2/17
 */
@Component
@Slf4j
public class DogMarketTask {

    @Resource
    private PetOperationService service;

    @Resource
    private GlobalConfig config;

    @Resource
    private DogMarketInvoke dogMarketInvoke;

    private int failedCount = 0;

    private static final int FIREST_PAGE = 1;
    private static final int PAGE_SIZE = 20;

    @Scheduled(fixedDelay = 4000)
    @SneakyThrows
    private void queryMarket() {
        try {
            List<Pet> pets = service.queryPetsOnSale(
                    PetsSortingEnum.CREATETIME_DESC, FIREST_PAGE, PAGE_SIZE, config.getConfig().getAcounts().get(0).getDes());
            MarketListMergeRequest request = new MarketListMergeRequest();
            request.setPets(pets);
            BaseResponse response = dogMarketInvoke.merge(request).execute().body();
            Preconditions.checkNotNull(response);
            if (config.getConfig().getLogSwitch()) {
                log.info("merge dogMarket success, response:{}", response);
            }
        } catch (Throwable e) {
            log.error("请求宠物商店失败，暂停查询", e);
            failedCount++;
            Thread.sleep(4000);
            if (failedCount > 1) {
                failedCount = 0;
                Thread.sleep(600000);
            }
        }
        Thread.sleep(System.currentTimeMillis() % 4000);
    }
}
