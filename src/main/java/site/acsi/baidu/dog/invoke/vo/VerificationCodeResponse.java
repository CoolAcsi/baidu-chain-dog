package site.acsi.baidu.dog.invoke.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.acsi.baidu.dog.pojo.VerificationCodeData;

/**
 * @author Acsi
 * @date 2018/2/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VerificationCodeResponse extends BaseResponse {
    private VerificationCodeData data;
}
