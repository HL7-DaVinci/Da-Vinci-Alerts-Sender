package org.hl7.davinci.alerts.refimpl.sender.service;

public class PatientNotFoundException extends RuntimeException {

  public PatientNotFoundException(String message) {
    super(message);
  }
}
