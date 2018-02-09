package site.acsi.baidu.dog.invoke.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PetOnSaleRequest extends BaseRequest {
    private int pageNo;
    private int pageSize;
    private String querySortType;
    private List<String> petIds;
    private String lastAmount;
    private String lastRareDegree;
}
