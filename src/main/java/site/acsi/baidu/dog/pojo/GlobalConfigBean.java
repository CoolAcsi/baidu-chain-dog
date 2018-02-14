package site.acsi.baidu.dog.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author Acsi
 * @date 2018/2/10
 */
@Data
public class GlobalConfigBean {
    private Integer time;
    private String verCodeStrategy;
    private Boolean logSwitch;
    private List<Amount> amounts;
    private List<Acount> acounts;
    private String exportVerCodeImgPath;
    private Boolean exportSwitch;
}
