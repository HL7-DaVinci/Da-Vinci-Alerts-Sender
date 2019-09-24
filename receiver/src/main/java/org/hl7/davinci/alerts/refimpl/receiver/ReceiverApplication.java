package org.hl7.davinci.alerts.refimpl.receiver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ReceiverApplication {

  public static void main(String[] args) {
    SpringApplication.run(ReceiverApplication.class, args);
  }

}
