package fr.lovotech.galaxy.qa.fileService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final GridFsTemplateInterceptor gridFsTemplateInterceptor;

    public WebMvcConfiguration(GridFsTemplateInterceptor gridFsTemplateInterceptor) {
        this.gridFsTemplateInterceptor = gridFsTemplateInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(gridFsTemplateInterceptor);
    }

}
