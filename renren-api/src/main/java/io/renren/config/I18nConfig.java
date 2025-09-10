package io.renren.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * 国际化配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class I18nConfig implements WebMvcConfigurer {

    /**
     * 默认语言解析器
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        // 设置默认语言为英文
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }

    /**
     * 国际化消息源
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        // 设置国际化资源文件的基础名
        messageSource.setBasenames("i18n/messages");
        // 设置编码
        messageSource.setDefaultEncoding("UTF-8");
        // 设置缓存时间（秒）
        messageSource.setCacheSeconds(3600);
        return messageSource;
    }

    /**
     * 国际化拦截器
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        // 设置参数名，默认为locale
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加国际化拦截器
        registry.addInterceptor(localeChangeInterceptor());
    }
}
