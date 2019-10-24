package org.hl7.davinci.alerts.refimpl.sender.config;

import com.healthlx.smartonfhir.config.EnableSmartOnFhir;
import org.hl7.davinci.alerts.refimpl.sender.support.CurrentContextArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableSmartOnFhir
@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final CurrentContextArgumentResolver currentContextArgumentResolver;

  @Autowired
  public WebConfig(CurrentContextArgumentResolver currentContextArgumentResolver) {
    this.currentContextArgumentResolver = currentContextArgumentResolver;
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(currentContextArgumentResolver);
  }

}

