package com.jiang.springframework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author jiang
 */
@EnableWebFlux
@Configuration
public class WebfluxConfig implements WebFluxConfigurer {


    private ThymeleafReactiveViewResolver reactiveViewResolver;

    @Autowired
    public void setReactiveViewResolver(ThymeleafReactiveViewResolver reactiveViewResolver) {
        this.reactiveViewResolver = reactiveViewResolver;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //注册视图解析器
        registry.viewResolver(reactiveViewResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*");
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        List<HttpMessageReader<?>> readers = configurer.getReaders();
    }

    /**
     * 配置thymeleaf资源模板解析器
     */
    @Bean
    public SpringResourceTemplateResolver springResourceTemplateResolver() {
        SpringResourceTemplateResolver resourceTemplateResolver = new SpringResourceTemplateResolver();
        resourceTemplateResolver.setPrefix("classpath:/templates/");
        resourceTemplateResolver.setSuffix(".html");
        resourceTemplateResolver.setTemplateMode(TemplateMode.HTML);
        resourceTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());

        return resourceTemplateResolver;
    }

    /**
     * 配置thymeleaf Spring WebFlux模板引擎
     *
     * @param resourceTemplateResolver 模板资源
     */
    @Bean
    public SpringWebFluxTemplateEngine springWebFluxTemplateEngine(SpringResourceTemplateResolver resourceTemplateResolver) {
        SpringWebFluxTemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
        templateEngine.setTemplateResolver(resourceTemplateResolver);
        return templateEngine;
    }

    /**
     * 配置thymeleaf Reactive视图解析器
     *
     * @param templateEngine 模板引擎
     */
    @Bean
    public ThymeleafReactiveViewResolver thymeleafReactiveViewResolver(SpringWebFluxTemplateEngine templateEngine) {
        ThymeleafReactiveViewResolver reactiveViewResolver = new ThymeleafReactiveViewResolver();
        reactiveViewResolver.setTemplateEngine(templateEngine);
        return reactiveViewResolver;
    }
}
