package site.acsi.baidu.dog.invoke.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Acsi
 * @date 2018/2/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PetQueriedByIdRequest extends BaseRequest {
    private String petId;
    private String nounce;
    private String token;
}
