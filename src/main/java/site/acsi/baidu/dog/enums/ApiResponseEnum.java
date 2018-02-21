package site.acsi.baidu.dog.enums;


import site.acsi.baidu.dog.vo.base.BaseResponse;

/**
 * @author Acsi
 * @date 2018/2/20
 */
public enum ApiResponseEnum {
    SUCCESS(new BaseResponse(0, "success"))
    ;
    private BaseResponse response;

    ApiResponseEnum(BaseResponse response) {
        this.response = response;
    }

    public BaseResponse getResponse() {
        return response;
    }

    public void setResponse(BaseResponse response) {
        this.response = response;
    }
}
