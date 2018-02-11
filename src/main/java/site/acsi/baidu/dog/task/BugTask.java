package site.acsi.baidu.dog.task;

import com.google.common.primitives.Doubles;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.acsi.baidu.dog.config.GlobalConfig;
import site.acsi.baidu.dog.enums.PetsSortingEnum;
import site.acsi.baidu.dog.global.DoneOrderSet;
import site.acsi.baidu.dog.pojo.Acount;
import site.acsi.baidu.dog.pojo.Amount;
import site.acsi.baidu.dog.pojo.CreateOrderStatus;
import site.acsi.baidu.dog.pojo.SaleData;
import site.acsi.baidu.dog.service.PetOperationService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@Component
@Slf4j
public class BugTask {
    @Resource
    private PetOperationService service;

    @Resource
    private DoneOrderSet doneOrderSet;

    @Resource
    private GlobalConfig config;

    private Map<Integer, Amount> rareDegreeMap = new ConcurrentHashMap<>(6);

    private static final int FIREST_PAGE = 1;
    private static final int PAGE_SIZE = 20;
    private static final String BLANK = " ";
    private static final String COMMA_SEPARATEOR = "，";
    private static final int FREE_PRICE = 0;

    @PostConstruct
    private void init() {
        config.getConfig().getAmounts().forEach((amount -> rareDegreeMap.put(amount.getRareDegree(), amount)));
    }

    @Async
    @SneakyThrows
    public void doTask(Acount acount) {

        while (true) {
            Thread.sleep(config.getConfig().getTime());
            try {
                // 查询宠物市场
                List<SaleData.Pet> pets = service.queryPetsOnSale(PetsSortingEnum.CREATETIME_DESC, FIREST_PAGE, PAGE_SIZE, acount.getDes());
                // 日志
                if (config.getConfig().getLogSwitch()) {
                    pageLog(FIREST_PAGE, pets, acount.getDes());
                }
                tryCreateOrder(acount, pets);
            } catch (Throwable e) {
                if (config.getConfig().getLogSwitch()) {
                    log.error("请求宠物市场列表失败, user:{}，error:{}", acount.getDes(), e);
                }
            }
        }
    }

    private void tryCreateOrder(Acount acount, List<SaleData.Pet> pets) {
        for (SaleData.Pet item : pets) {
            try {
                Amount rareDegree = rareDegreeMap.get(item.getRareDegree());
                Double amount = Doubles.tryParse(item.getAmount());
                if (canCreateOrder(rareDegree, amount)) {
                    createOrder(acount, item);
                }
            } catch (Throwable e) {
                if (config.getConfig().getLogSwitch()) {
                    log.error("生单时发生异常, user:{}, error:{}", acount.getDes(), e);
                }
                log.info("生单时发生异常, user:{} petId:{}，amount:{}", acount.getDes(), item.getPetId(), item.getAmount());

            }
        }
    }

    private void pageLog(int currPage, List<SaleData.Pet> pets ,String userName) {
        if (!pets.isEmpty()) {
            StringBuilder info = new StringBuilder();
            for (SaleData.Pet pet : pets) {
                info.append(rareDegreeMap.get(pet.getRareDegree()).getDes());
                info.append(BLANK);
                info.append(pet.getAmount());
                info.append(COMMA_SEPARATEOR);
            }
//            log.info("===  page:{} userName:{}，{}", currPage, userName, info);
        }
    }

    private boolean canCreateOrder(Amount rareDegree, Double amount) {
        return null != rareDegree
                && null != amount
                && amount <= rareDegree.getBuyAmount()
                && amount > FREE_PRICE;
    }

    @SneakyThrows
    private void createOrder(Acount acount, SaleData.Pet item) {
        if (doneOrderSet.isCompleted(item.getPetId())) {
            return;
        }
        log.info("=========================  开始生单 user:{} petid:{} amount:{}",acount.getDes(), item.getPetId(), item.getAmount());
        CreateOrderStatus status = service.createOrder(acount, item.getPetId(), item.getAmount(), item.getValidCode());
        log.info("===  user:{} success:{} message:{} petid:{} amount:{}", acount.getDes(), status.getSuccess(), status.getMessage(), item.getPetId(), item.getAmount());
        if (status.getSuccess()) {
            log.info("******************  success user:{} 稀有度：{} 价格：{} ******************", acount.getDes(), rareDegreeMap.get(item.getRareDegree()), item.getAmount());
        }
    }

}