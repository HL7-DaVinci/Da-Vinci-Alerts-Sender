package org.hl7.davinci.alerts.refimpl.sender.service;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import lombok.extern.slf4j.Slf4j;
import org.hl7.davinci.alerts.refimpl.sender.dto.CurrentContextDto;
import org.hl7.davinci.alerts.refimpl.sender.dto.CurrentContextResponseDto;
import org.hl7.davinci.alerts.refimpl.sender.fhir.IGenericClientProvider;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    List<Encounter> encountersForPatient = getEncountersForPatient(patient);
    return new CurrentContextResponseDto(patient, encountersForPatient);
  }

  Patient getPatient(String patientId) {
    IGenericClient client = clientProvider.client();
    return client.read().resource(Patient.class).withId(patientId).execute();
  }

  List<Encounter> getEncountersForPatient(Patient patient) {
    Bundle execute = clientProvider.client()
        .search()
        .forResource(Encounter.class)
        .where(Encounter.SUBJECT.hasId(patient.getIdElement().toUnqualifiedVersionless().getValue()))
        .returnBundle(Bundle.class)
        .execute();
    List<Encounter> resultList = new ArrayList<>();
    for (Bundle.BundleEntryComponent bundleEntryComponent : execute.getEntry()) {
      Encounter encounter = (Encounter) bundleEntryComponent.getResource();
      resultList.add(encounter);
    }
    return resultList;
  }

  public Encounter getEncounter(String existingEventId) {
    IGenericClient client = clientProvider.client();
    return client.read().resource(Encounter.class).withId(existingEventId).execute();
  }
}
