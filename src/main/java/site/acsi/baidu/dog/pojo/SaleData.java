package site.acsi.baidu.dog.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.acsi.baidu.dog.invoke.vo.BaseResponse;

import java.util.List;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SaleData extends BaseResponse {

    private Data data;

    @lombok.Data
    public static class Data {
        private List<Pet> petsOnSale;
        private int totalCount;
        private boolean hasData;
    }
}
