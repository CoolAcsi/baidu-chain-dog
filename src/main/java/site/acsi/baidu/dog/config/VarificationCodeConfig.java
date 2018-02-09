package site.acsi.baidu.dog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Acsi
 * @date 2018/2/6
 */
@Configuration
@ConfigurationProperties("varificationCodeConfig")
@Component
@Data
public class VarificationCodeConfig {
    private String user;
    private String pass;
    private String softId;
    private String codeType;
    private String lenMin;
    private String verCodeStorePath;
}
