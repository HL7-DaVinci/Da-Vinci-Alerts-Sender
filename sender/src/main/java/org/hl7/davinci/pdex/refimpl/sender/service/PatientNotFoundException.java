package org.hl7.davinci.pdex.refimpl.sender.service;

public class PatientNotFoundException extends RuntimeException {

  public PatientNotFoundException(String message) {
    super(message);
  }
}
