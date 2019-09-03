package org.hl7.davinci.pdex.refimpl.sender.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirConfig {

  @Bean
  public FhirContext fhirContext() {
    return FhirContext.forR4();
  }

  @Bean
  public IParser parser(FhirContext context) {
    return context.newJsonParser().setPrettyPrint(true);
  }

  @Bean
  public IParser iParser() {
    return fhirContext().newJsonParser().setPrettyPrint(true);
  }

}
