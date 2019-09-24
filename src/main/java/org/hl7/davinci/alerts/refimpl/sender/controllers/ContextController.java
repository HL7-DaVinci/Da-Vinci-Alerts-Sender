package org.hl7.davinci.alerts.refimpl.sender.controllers;

import org.hl7.davinci.alerts.refimpl.sender.service.AlertService;
import org.hl7.davinci.alerts.refimpl.sender.service.EHRService;
import org.hl7.davinci.alerts.refimpl.sender.dto.AlertRequestDto;
import org.hl7.davinci.alerts.refimpl.sender.dto.CurrentContextDto;
import org.hl7.davinci.alerts.refimpl.sender.dto.CurrentContextResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
public class ContextController {

    private final EHRService ehrService;
    private final AlertService alertService;

    @Autowired
    public ContextController(
            EHRService ehrService,
            AlertService alertService
    ) {
        this.ehrService = ehrService;
        this.alertService = alertService;
    }

    @GetMapping("/current-context")
    public CurrentContextResponseDto getCurrentContext(@Valid CurrentContextDto currentContextDto) {
        return ehrService.getCurrentContextDetails(currentContextDto);
    }

    @PostMapping("/send-alert")
    public String sendEvent(@RequestBody AlertRequestDto alertRequestDto) {
        return alertService.sendAlert(alertRequestDto);
    }

    @PostMapping("/preview")
    public String preview(@RequestBody AlertRequestDto alertRequestDto) {
        return alertService.previewAlert(alertRequestDto);
    }

}