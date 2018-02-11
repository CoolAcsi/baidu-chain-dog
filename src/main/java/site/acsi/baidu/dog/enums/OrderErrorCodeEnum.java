package site.acsi.baidu.dog.enums;

/**
 * @author Acsi
 * @date 2018/2/10
 */
public enum OrderErrorCodeEnum {
    SUCCESS("00");
    private String code;

    OrderErrorCodeEnum(String code) {
        this.code = code;
    }
}
