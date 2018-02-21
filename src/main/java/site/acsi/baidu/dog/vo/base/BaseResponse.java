package site.acsi.baidu.dog.vo.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Acsi
 * @date 2018/2/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private Integer status;
    private String message;
}
