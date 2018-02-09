package site.acsi.baidu.dog.invoke.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse {
    private String errorNo;
    private String errorMsg;
    private String timestamp;
}
