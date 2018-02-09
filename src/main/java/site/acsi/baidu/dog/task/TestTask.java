package site.acsi.baidu.dog.task;

import com.google.common.primitives.Doubles;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import site.acsi.baidu.dog.enums.PetsSortingEnum;
import site.acsi.baidu.dog.global.DoneOrderSet;
import site.acsi.baidu.dog.pojo.CreateOrderStatus;
import site.acsi.baidu.dog.pojo.RareDegree;
import site.acsi.baidu.dog.pojo.SaleData;
import site.acsi.baidu.dog.service.PetOperationService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@Service
@Slf4j
public class TestTask {
    @Resource
    private PetOperationService service;

    @Resource
    private DoneOrderSet doneOrderSet;

    private Map<Integer, RareDegree> rareDegreeMap = RareDegree.initRareDegrees();


    @Async
    @SneakyThrows
    public void doTask(String cookie) {
        int pageNum = 1;
        int pageSize = 20;
        for (int currPage = pageNum; ; currPage++) {
            log.info("==============================================  page: {} start==================", currPage);
            Thread.sleep(500);
            try {
                // 分页查询
//                List<SaleData.Pet> pets = service.queryPetsOnSale(PetsSortingEnum.RAREDEGREE_DESC, currPage, pageSize);
                List<SaleData.Pet> pets = service.queryPetsOnSale(PetsSortingEnum.CREATETIME_DESC, 1, pageSize);
                if (!pets.isEmpty()) {

                    StringBuilder info = new StringBuilder();
                    for (SaleData.Pet pet : pets) {
                        info.append(rareDegreeMap.get(pet.getRareDegree()).getDes());
                        info.append(" ");
                        info.append(pet.getAmount());
                        info.append("，");
                    }
                    log.info("===  page: {} ，{}", currPage, info);
                }

                for (SaleData.Pet item : pets) {
                    try {
                        RareDegree rareDegree = rareDegreeMap.get(item.getRareDegree());
                        Double amount = Doubles.tryParse(item.getAmount());
                        if (null != rareDegree
                                && null != amount
                                && amount <= rareDegree.getBugAmount()
                                && amount > 0) {
                            createOrder(cookie, item, rareDegree);
                        }
//                        if ("1861775104848915775".equals(item.getPetId())) {
//                            createOrder(cookie, item, rareDegree);
//                        }
                    } catch (Throwable e) {
                        log.error("生单时发生异常", e);
                    }
                }

//                if (pets.isEmpty() || pets.get(0).getRareDegree()<2) {
//                    currPage = 0;
//                } else {
//                    Double maxAmount = Doubles.tryParse(pets.get(pets.size()-1).getAmount());
//                    if (null != maxAmount && maxAmount <= 0) {
//                        continue;
//                    }
//                    if (null == maxAmount || maxAmount > 3000) {
//                        currPage = 0;
//                    }
//                }
            } catch (Throwable e) {
                log.error("请求宠物市场列表失败", e);
            }
        }
    }

    //    @Async
    @SneakyThrows
    protected void createOrder(String cookie, SaleData.Pet item, RareDegree rareDegree) {
        if (doneOrderSet.isCompleted(item.getPetId())) {
            return;
        }
        log.info("=========================  开始生单 petid:{} amount:{}", item.getPetId(), item.getAmount());
        CreateOrderStatus status = service.createOrder(cookie, item.getPetId(), item.getAmount(), item.getValidCode());
        Double amount = Doubles.tryParse(item.getAmount());
//        processBuyAmount(status.getSuccess(), rareDegree);
    }

    private void processBuyAmount(boolean success, RareDegree degree) {

        if (success) {
            degree.setSuccessCount(degree.getSuccessCount() + 1);
        } else {
            degree.setMissCount(degree.getMissCount() + 1);
        }
//        adjustAmount(degree);
        log.info("================  稀有度数据：{}", degree);
    }

    private void adjustAmount(RareDegree degree) {
        if (degree.getMissCount() > 500
                && degree.getSuccessCount().floatValue() / degree.getMissCount().floatValue() < 0.001
                && degree.getBugAmount() + degree.getChangeScope() <= degree.getMaxBuyAmount()) {
            degree.setBugAmount(degree.getBugAmount() + degree.getChangeScope());
            degree.setMissCount(0);
            degree.setSuccessCount(0);
        }
        if (degree.getMissCount() > 100
                && degree.getSuccessCount().floatValue() / degree.getMissCount().floatValue() > 0.05
                && degree.getBugAmount() - degree.getChangeScope() > 0) {
            degree.setBugAmount(degree.getBugAmount() - degree.getChangeScope());
            degree.setMissCount(0);
            degree.setSuccessCount(0);
        }
    }

}
