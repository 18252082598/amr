package com.joymeter.controller;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * 语言切换
 *
 * @author Joymeter
 */
public class LanguageController {

    /**
     * 语言切换
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/lang")
    public String lang(HttpServletRequest request) {
        String langType = request.getParameter("langType");
        switch (langType) {
            case "zh": {
                Locale locale = new Locale("zh", "CN");
                request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
                break;
            }
            case "en": {
                Locale locale = new Locale("en", "US");
                request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
                break;
            }
            default:
                request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, LocaleContextHolder.getLocale());
                break;
        }
        return null;
    }
}
