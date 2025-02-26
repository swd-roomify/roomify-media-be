package com.roomify.detection_be.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  private final List<Locale> locales = List.of(new Locale("en"));

  @Bean("localeResolver")
  LocaleResolver acceptHeaderLocaleResolver() {
    return new AcceptHeaderLocaleResolver() {
      @Override
      public Locale resolveLocale(@NonNull HttpServletRequest request) {
        String headerLang = request.getHeader("Accept-Language");
        return headerLang == null || headerLang.isEmpty()
            ? locales.getFirst()
            : Locale.lookup(Locale.LanguageRange.parse(headerLang), locales);
      }
    };
  }

  @Bean(name = "globalMessageSource")
  ResourceBundleMessageSource resourceBundleMessageSource() {
    ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
    rs.setBasename("messages");
    rs.setDefaultEncoding("UTF-8");
    rs.setUseCodeAsDefaultMessage(true);
    rs.setAlwaysUseMessageFormat(true);

    return rs;
  }
}
