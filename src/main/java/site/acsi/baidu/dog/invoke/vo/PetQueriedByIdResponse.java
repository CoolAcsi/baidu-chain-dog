package site.acsi.baidu.dog.invoke.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.acsi.baidu.dog.pojo.PetDetail;

/**
 * @author Acsi
 * @date 2018/2/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PetQueriedByIdResponse extends BaseResponse {
    private PetDetail data;
}
