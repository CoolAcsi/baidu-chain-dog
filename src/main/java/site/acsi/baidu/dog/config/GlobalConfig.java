package site.acsi.baidu.dog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import site.acsi.baidu.dog.pojo.GlobalConfigBean;

import javax.annotation.PostConstruct;
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

    private ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    @SneakyThrows
    public void init() {
        config = mapper.readValue(new InputStreamReader(resource.getInputStream()), GlobalConfigBean.class);
        config.setStartTime(System.currentTimeMillis());
    }

    public GlobalConfigBean getConfig() {
        return config;
    }

    public void setConfig(GlobalConfigBean config) {
        this.config = config;
        this.config.setStartTime(System.currentTimeMillis());
    }
}
