package org.hl7.davinci.alerts.refimpl.sender.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hl7.davinci.alerts.refimpl.sender.alerts.AlertType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class CurrentContextResponseDto {

  private PatientResponseDto patient;
  private List<EventDto> generateEventTypes;
  private List<EventDto> existingEvents;

  private String channelType;

  public CurrentContextResponseDto(Patient patient) {
    Assert.notNull(patient, "Patient cannot be null");

    //SET PATIENT
    this.patient = new PatientResponseDto();
    this.patient.setId(patient.getId());
    this.patient.setName(Optional.ofNullable(patient.getNameFirstRep()).map(HumanName::getNameAsSingleString)
        .orElse(null));
    this.patient.setGender(Optional.ofNullable(patient.getGender()).map(AdministrativeGender::getDisplay).orElse(null));
    Optional.ofNullable(patient.getBirthDateElement()).map(DateType::getYear).ifPresent(
        yob -> this.patient.setAge(LocalDate.now().getYear() - yob)
    );

    this.generateEventTypes = Arrays.asList(new EventDto(AlertType.ADMIT_ER, "Alert Admit ER"),
        new EventDto(AlertType.ADMIT_INPATIENT, "Alert Admit Inpatient"),
        new EventDto(AlertType.ADMIT_FOROBSERVATION, "Alert Admit for Observation"),
        new EventDto(AlertType.ADMIT_AMBULATORY, "Alert Admit Ambulatory"),
        new EventDto(AlertType.DISCHARGE, "Alert Discharge"));

    this.existingEvents = Arrays.asList(
        new EventDto("1","Test1"),
        new EventDto("2","Test2"),
        new EventDto("3","Test3"),
        new EventDto("4","Test4"),
        new EventDto("5","Test5"),
        new EventDto("6","Test6")
    );

    this.channelType = "FHIR Messaging ($process-message)";
  }

  @Data
  private class PatientResponseDto {
    private String id;
    private String name;
    private String gender;
    private Integer age;
  }

}
