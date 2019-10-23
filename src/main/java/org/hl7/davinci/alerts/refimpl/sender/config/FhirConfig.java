package org.hl7.davinci.alerts.refimpl.sender.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.healthlx.smartonfhir.core.SmartOnFhirAccessTokenResponseClient;
import com.healthlx.smartonfhir.core.SmartOnFhirContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;

@Configuration
public class FhirConfig {

  @Bean
  public IParser parser(FhirContext context) {
    return context.newJsonParser().setPrettyPrint(true);
  }

  @Bean
  public FhirContext fhirContext() {
    return FhirContext.forR4();
  }

}
