package site.acsi.baidu.dog.invoke.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateOrderRequest extends BaseRequest {

    private String petId;
    private String amount;
    private String seed;
    private String captcha;
    private String validCode;

}
