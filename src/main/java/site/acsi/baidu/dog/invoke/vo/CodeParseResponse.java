package site.acsi.baidu.dog.invoke.vo;

import lombok.Data;

/**
 * @author Acsi
 * @date 2018/2/6
 */
@Data
public class CodeParseResponse {
    private Integer errNo;
    private String errStr;
    private String picId;
    private String picStr;
    private String md5;
}
