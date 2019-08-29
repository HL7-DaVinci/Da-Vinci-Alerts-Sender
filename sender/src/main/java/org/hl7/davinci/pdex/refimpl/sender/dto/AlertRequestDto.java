package org.hl7.davinci.pdex.refimpl.sender.dto;

import lombok.Data;

import java.util.List;

@Data
public class AlertRequestDto {
    private String patientId;
    private String eventId;
    private String channelType;
    private String receiverUrl;
}
