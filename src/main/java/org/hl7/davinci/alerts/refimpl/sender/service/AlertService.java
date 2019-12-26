package org.hl7.davinci.alerts.refimpl.sender.service;

import ca.uhn.fhir.parser.IParser;
import org.apache.commons.lang3.StringUtils;
import org.hl7.davinci.alerts.refimpl.sender.alerts.message.MessageCreator;
import org.hl7.davinci.alerts.refimpl.sender.alerts.message.config.SampleMessageCreatorUtil;
import org.hl7.davinci.alerts.refimpl.sender.alerts.service.FhirMessageService;
import org.hl7.davinci.alerts.refimpl.sender.dto.AlertRequestDto;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AlertService {

  private final FhirMessageService fhirMessageService;

  private final EHRService ehrService;
  private final IParser parser;

  private final Map<String, MessageCreator> messageCreatorMap;

  @Autowired
  public AlertService(FhirMessageService fhirMessageService, EHRService ehrService, IParser parser,
      Map<String, MessageCreator> messageCreatorMap) {
    this.fhirMessageService = fhirMessageService;
    this.ehrService = ehrService;
    this.parser = parser;
    this.messageCreatorMap = messageCreatorMap;
  }

  public String sendAlert(AlertRequestDto alertRequestDto) {
    Bundle bundle = prepareBundle(alertRequestDto);
    return parser.encodeResourceToString(fhirMessageService.sendMessage(bundle, alertRequestDto.getReceiverUrl()));
  }

  public String previewAlert(AlertRequestDto alertRequestDto) {
    return parser.encodeResourceToString(prepareBundle(alertRequestDto));
  }

  private Bundle prepareBundle(AlertRequestDto alertRequestDto) {
    Patient patient = ehrService.getPatient(alertRequestDto.getPatientId());
    if (StringUtils.isNotBlank(alertRequestDto.getExistingEventId())) {
      Encounter encounter = ehrService.getEncounter(alertRequestDto.getExistingEventId());
      return SampleMessageCreatorUtil.createMessageBundle(patient, encounter);
    } else {
      MessageCreator messageCreator = messageCreatorMap.get(alertRequestDto.getGenerateEventType());
      return messageCreator.createMessageBundle(patient);
    }
  }

}
