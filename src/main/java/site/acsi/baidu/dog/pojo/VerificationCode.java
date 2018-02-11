package site.acsi.baidu.dog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Acsi
 * @date 2018/2/10
 */
@Data
@AllArgsConstructor
public class VerificationCode {
    private String seed;
    private String code;
    private Long createTime;
}
