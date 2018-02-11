package site.acsi.baidu.dog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import site.acsi.baidu.dog.pojo.GlobalConfigBean;

import java.io.InputStreamReader;

/**
 * @author Acsi
 * @date 2018/2/10
 */
@Component
public class GlobalConfig {

    @Value(value="classpath:config.json")
    private Resource resource;

    private GlobalConfigBean config;

    @SneakyThrows
    public GlobalConfigBean getConfig() {
        if (config == null) {
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(new InputStreamReader(resource.getInputStream()), GlobalConfigBean.class);
        }
        return config;
    }
}
