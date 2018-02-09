package site.acsi.baidu.dog.invoke;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import site.acsi.baidu.dog.invoke.vo.CodeParseResponse;

/**
 * @author Acsi
 * @date 2018/2/6
 */
public interface CodeParseInvoke {

    /**
     * 解析图形验证码
     * @param user 用户名
     * @param pass 密码
     * @param softId 软件id
     * @param codeType 验证码类型
     * @param lenMin 验证码最小长度，0表示不可变长
     * @param fileBase64 文件内容
     * @return 解析结果
     */
    @FormUrlEncoded
    @POST("Upload/Processing.php")
    Call<CodeParseResponse> parse(@Field("user") String user,
                                  @Field("pass") String pass,
                                  @Field("softid") String softId,
                                  @Field("codetype") String codeType,
                                  @Field("len_min") String lenMin,
                                  @Field("file_base64") String fileBase64);
}
