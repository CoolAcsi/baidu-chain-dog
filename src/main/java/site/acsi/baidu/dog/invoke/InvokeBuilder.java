package site.acsi.baidu.dog.invoke;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.Setter;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author Acsi
 * @date 2018/2/5
 */
@Configuration
@ConfigurationProperties("url")
public class InvokeBuilder {

    @Setter
    private String petUrl;

    @Setter
    private String codeParseUrl;

    @Bean
    PetOperationInvoke petOperationInvoke() {
        OkHttpClient client = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(petUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(PetOperationInvoke.class);
    }

    @Bean
    CodeParseInvoke codeParseInvoke() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(codeParseUrl)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();
        return retrofit.create(CodeParseInvoke.class);
    }
}
