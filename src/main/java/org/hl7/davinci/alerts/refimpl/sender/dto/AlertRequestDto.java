package org.hl7.davinci.alerts.refimpl.sender.dto;

import lombok.Data;

@Data
public class AlertRequestDto {
    private String patientId;

    private String generateEventType;
    private String existingEventId;

    private String channelType;
    private String receiverUrl;
}
