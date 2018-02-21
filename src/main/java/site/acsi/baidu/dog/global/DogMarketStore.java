package site.acsi.baidu.dog.global;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.acsi.baidu.dog.config.GlobalConfig;
import site.acsi.baidu.dog.pojo.Pet;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Acsi
 * @date 2018/2/20
 */
@Component
@Slf4j
public class DogMarketStore {

    @Resource
    private GlobalConfig config;

    private static final int STORE_MAX_SIZE = 10000;

    private LinkedHashMap<String, Pet> map = Maps.newLinkedHashMapWithExpectedSize(STORE_MAX_SIZE);

    public List<Pet> list() {
        int skipNum = map.size() - 20 >= 0 ? map.size() - 20 : 0;
        List<Pet> pets = map.entrySet().stream().skip(skipNum).map(Map.Entry::getValue).collect(Collectors.toList());
        if (config.getConfig().getLogSwitch()) {
            log.info("中心仓库返回数据, list:{}", pets);
        }
        return pets;
    }

    public void put(List<Pet> pets) {
        if (config.getConfig().getLogSwitch()) {
            log.info("中心仓库merge数据， pets:{}", pets);
        }
        pets.stream()
                .filter(pet -> !map.containsKey(pet.getPetId()) || oneMinuteBefore(map.get(pet.getPetId()).getCreateTime()))
                .forEach(pet -> map.put(pet.getPetId(), pet));
    }

    private boolean oneMinuteBefore(long time) {
        return System.currentTimeMillis() - time > 60000;
    }

    public Pet queryPetById(String petId) {
        return map.get(petId);
    }

    @Scheduled(fixedRate = 60000)
    private void clearTask() {
        if (map.size() <= STORE_MAX_SIZE) {
            return;
        }
        int removeNum = map.size() - STORE_MAX_SIZE;
        // TODO 这里可能有问题
        List<Pet> pets = map.entrySet().stream().limit(removeNum).map(Map.Entry::getValue).collect(Collectors.toList());
        pets.forEach(pet -> map.remove(pet.getPetId()));
    }

}
