package org.hl7.davinci.alerts.refimpl.sender.service;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import lombok.extern.slf4j.Slf4j;
import org.hl7.davinci.alerts.refimpl.sender.dto.CurrentContextDto;
import org.hl7.davinci.alerts.refimpl.sender.dto.CurrentContextResponseDto;
import org.hl7.davinci.alerts.refimpl.sender.fhir.IGenericClientProvider;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EHRService {

  private final IGenericClientProvider clientProvider;

  public EHRService(
      @Autowired IGenericClientProvider clientProvider
  ) {
    this.clientProvider = clientProvider;
  }


  public CurrentContextResponseDto getCurrentContextDetails(CurrentContextDto currentContext) {
    String patientId = currentContext.getPatientId();
    Patient patient = getPatient(patientId);
    return new CurrentContextResponseDto(patient);
  }

  Patient getPatient(String patientId) {
    IGenericClient client = clientProvider.client();
    return client.read().resource(Patient.class).withId(patientId).execute();
  }

}
