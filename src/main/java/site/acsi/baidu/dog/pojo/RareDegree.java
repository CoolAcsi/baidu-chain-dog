package site.acsi.baidu.dog.pojo;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@Data
@AllArgsConstructor
public class RareDegree {
    private String des;
    private Long bugAmount;
    private Long saleAmount;
    private Long maxBuyAmount;
    private Long changeScope;
    private Integer missCount;
    private Integer successCount;

    public static Map<Integer, RareDegree> initRareDegrees() {
        Map<Integer, RareDegree> rareDegrees = Maps.newHashMapWithExpectedSize(6);
        rareDegrees.put(0, new RareDegree("普通", 1000L, 3000L, 2250L,100L, 0, 0));
        rareDegrees.put(1, new RareDegree("稀有", 1000L, 4000L, 4000L, 100L, 0, 0));
        rareDegrees.put(2, new RareDegree("卓越", 2510L, 3000L, 4000L, 100L, 0, 0));
        rareDegrees.put(3, new RareDegree("史诗", 8050L, 6000L, 60000L, 200L, 0, 0));
        rareDegrees.put(4, new RareDegree("神话", 100000L, 10000L, 80000L, 200L, 0, 0));
        rareDegrees.put(6, new RareDegree("传说", 100000L, 100000L, 100000L, 300L, 0, 0));
        return rareDegrees;
    }
}
