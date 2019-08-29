package org.hl7.davinci.pdex.refimpl.sender.controllers;


import org.hl7.davinci.pdex.refimpl.sender.dto.AlertRequestDto;
import org.hl7.davinci.pdex.refimpl.sender.dto.CurrentContextDto;
import org.hl7.davinci.pdex.refimpl.sender.dto.CurrentContextResponseDto;
import org.hl7.davinci.pdex.refimpl.sender.service.AlertService;
import org.hl7.davinci.pdex.refimpl.sender.service.EHRService;
import org.hl7.davinci.pdex.refimpl.sender.service.AdmissionBundleCreator;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Validated
@RestController
public class ContextController {

  private final EHRService EHRService;
  private final AlertService alertService;

  @Autowired
  public ContextController(
          EHRService EHRService,
          AlertService alertService
  ) {
    this.EHRService = EHRService;
    this.alertService = alertService;
  }

  @GetMapping("/current-context")
  public CurrentContextResponseDto getCurrentContext(@Valid CurrentContextDto currentContextDto, HttpSession session) {
    CurrentContextResponseDto currentContextDetails = EHRService.getCurrentContextDetails(currentContextDto);
    session.setAttribute("patient-id", currentContextDetails.getPatient().getId());
    return currentContextDetails;
  }

  @PostMapping("/send-alert")
  public String sendEvent(@RequestBody AlertRequestDto alertRequestDto) {
    Patient patient = EHRService.getPatient(alertRequestDto.getPatientId());
    if("notify".equals(alertRequestDto.getChannelType())){
      Bundle bundle = AdmissionBundleCreator.createNotifyBundle(patient);
      return alertService.notify(bundle, alertRequestDto.getReceiverUrl());
    }else{
      Bundle bundle = AdmissionBundleCreator.createMessageBundle(patient);
      return alertService.sendMessage(bundle, alertRequestDto.getReceiverUrl());
    }
  }

}