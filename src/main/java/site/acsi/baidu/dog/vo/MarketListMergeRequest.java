package site.acsi.baidu.dog.vo;

import lombok.Data;
import site.acsi.baidu.dog.pojo.Pet;

import java.util.List;

/**
 * @author Acsi
 * @date 2018/2/17
 */
@Data
public class MarketListMergeRequest {
    private List<Pet> pets;
}
