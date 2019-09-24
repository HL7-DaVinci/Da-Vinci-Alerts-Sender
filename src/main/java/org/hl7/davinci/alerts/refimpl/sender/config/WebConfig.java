package org.hl7.davinci.alerts.refimpl.sender.config;

import java.util.List;

import org.hl7.davinci.alerts.refimpl.sender.support.CurrentContextArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new CurrentContextArgumentResolver());
  }

}
