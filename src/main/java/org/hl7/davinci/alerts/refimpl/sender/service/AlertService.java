package org.hl7.davinci.alerts.refimpl.sender.service;

import ca.uhn.fhir.parser.IParser;
import org.hl7.davinci.alerts.refimpl.sender.alerts.message.MessageCreator;
import org.hl7.davinci.alerts.refimpl.sender.alerts.service.FhirMessageService;
import org.hl7.davinci.alerts.refimpl.sender.alerts.service.NotifyService;
import org.hl7.davinci.alerts.refimpl.sender.dto.AlertRequestDto;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AlertService {

    private final NotifyService notifyService;
    private final FhirMessageService fhirMessageService;

    private final EHRService ehrService;
    private final IParser parser;

    private Map<String, MessageCreator> messageCreatorMap;

    @Autowired
    public AlertService(
            NotifyService notifyService,
            FhirMessageService fhirMessageService,
            EHRService ehrService,
            IParser parser,
            Map<String, MessageCreator> messageCreatorMap
    ) {
        this.notifyService = notifyService;
        this.fhirMessageService = fhirMessageService;
        this.ehrService = ehrService;
        this.parser = parser;
        this.messageCreatorMap = messageCreatorMap;
    }

    public String sendAlert(AlertRequestDto alertRequestDto){
        Patient patient = ehrService.getPatient(alertRequestDto.getPatientId());
        if ("notify".equals(alertRequestDto.getChannelType())) {
            MessageCreator messageCreator = messageCreatorMap.get(alertRequestDto.getEventId());
            Parameters parameters = messageCreator.createNotifyOperation(patient);
            return notifyService.notify(parameters, alertRequestDto.getReceiverUrl());
        } else {
            MessageCreator messageCreator = messageCreatorMap.get(alertRequestDto.getEventId());
            Bundle bundle = messageCreator.createMessageBundle(patient);
            return fhirMessageService.sendMessage(bundle, alertRequestDto.getReceiverUrl());
        }
    }

    public String previewAlert(AlertRequestDto alertRequestDto){
        Patient patient = ehrService.getPatient(alertRequestDto.getPatientId());
        if ("notify".equals(alertRequestDto.getChannelType())) {
            MessageCreator messageCreator = messageCreatorMap.get(alertRequestDto.getEventId());
            Parameters parameters = messageCreator.createNotifyOperation(patient);
            return parser.encodeResourceToString(parameters);
        } else {
            MessageCreator messageCreator = messageCreatorMap.get(alertRequestDto.getEventId());
            return parser.encodeResourceToString(messageCreator.createMessageBundle(patient));
        }
    }

}
