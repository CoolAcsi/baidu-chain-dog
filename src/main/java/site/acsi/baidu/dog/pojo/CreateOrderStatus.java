package site.acsi.baidu.dog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@Data
@AllArgsConstructor
public class CreateOrderStatus {
    private Boolean success;
    private String message;
}
