package site.acsi.baidu.dog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Acsi
 * @date 2017/02/05
 */
@SpringBootApplication
@EnableAsync
public class Application implements EmbeddedServletContainerCustomizer {

    public static void main(String[] args)  {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(8081);
    }
}
