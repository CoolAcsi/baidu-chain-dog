package site.acsi.baidu.dog.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author Acsi
 * @date 2018/2/21
 */
@Data
public class PetDetail {
    private String id;

    private String name;

    private String petId;

    private Integer generation;

    private String rareDegree;

    private List<RareDegreeDetail> attributes ;

    private String desc;

    private String amount;

    private Integer selfStatus;

    private String faterId;

    private String motherId;

    private Boolean isOnChain;

    private String bgColor;

    private String headIcon;

    private String userName;

    private String petUrl;

    private Boolean onChain;
}
