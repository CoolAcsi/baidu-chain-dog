package site.acsi.baidu.dog.invoke.vo;

import lombok.Data;

/**
 * @author Acsi
 * @date 2018/2/6
 */
@Data
public class BaseRequest {
    private Long requestId;
    private Integer appId;
    private String tpl;
}
