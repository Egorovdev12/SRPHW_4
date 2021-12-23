package hwnetology.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

// Отвечает за правильный парсинг JSON
@Configuration
public class WebConfig {
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        final var bean = new RequestMappingHandlerAdapter();
        bean.getMessageConverters().add(new GsonHttpMessageConverter());
        return bean;
    }
}