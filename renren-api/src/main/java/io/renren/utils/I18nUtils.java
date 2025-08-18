package io.renren.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * 国际化工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class I18nUtils {

    @Resource
    private MessageSource messageSource;

    /**
     * 获取国际化消息
     *
     * @param code 消息代码
     * @return 国际化消息
     */
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    /**
     * 获取国际化消息
     *
     * @param code 消息代码
     * @param args 参数
     * @return 国际化消息
     */
    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * 获取国际化消息
     *
     * @param code 消息代码
     * @param args 参数
     * @param locale 语言环境
     * @return 国际化消息
     */
    public String getMessage(String code, Object[] args, Locale locale) {
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            // 如果获取失败，返回代码本身
            return code;
        }
    }

    /**
     * 获取当前语言环境
     *
     * @return 当前语言环境
     */
    public Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    /**
     * 设置语言环境
     *
     * @param locale 语言环境
     */
    public void setLocale(Locale locale) {
        LocaleContextHolder.setLocale(locale);
    }

    /**
     * 获取语言代码
     *
     * @return 语言代码
     */
    public String getLanguage() {
        return LocaleContextHolder.getLocale().getLanguage();
    }

    /**
     * 获取国家代码
     *
     * @return 国家代码
     */
    public String getCountry() {
        return LocaleContextHolder.getLocale().getCountry();
    }

    /**
     * 检查是否为指定语言
     *
     * @param language 语言代码
     * @return 是否为指定语言
     */
    public boolean isLanguage(String language) {
        return LocaleContextHolder.getLocale().getLanguage().equals(language);
    }

    /**
     * 检查是否为中文
     *
     * @return 是否为中文
     */
    public boolean isChinese() {
        return isLanguage("zh");
    }

    /**
     * 检查是否为英文
     *
     * @return 是否为英文
     */
    public boolean isEnglish() {
        return isLanguage("en");
    }

    /**
     * 检查是否为印地语
     *
     * @return 是否为印地语
     */
    public boolean isHindi() {
        return isLanguage("hi");
    }
}
