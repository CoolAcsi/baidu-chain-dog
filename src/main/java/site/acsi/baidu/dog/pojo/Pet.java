package site.acsi.baidu.dog.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author Acsi
 * @date 2018/2/14
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pet {
    private String id;
    private String petId;
    private int birthType;
    private int mutation;
    private int generation;
    private int rareDegree;
    private String desc;
    private int petType;
    private String amount;
    private String bgColor;
    private String petUrl;
    private String validCode;
    private Long createTime = System.currentTimeMillis();
}
