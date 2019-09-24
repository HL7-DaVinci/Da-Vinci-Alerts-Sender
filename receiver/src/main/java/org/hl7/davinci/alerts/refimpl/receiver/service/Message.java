package org.hl7.davinci.alerts.refimpl.receiver.service;

public class Message {
    private final String channelType;
    private final String patientString;
    private final String code;
    private final String content;

    Message(String channelType, String patientString, String code, String content) {
        this.channelType = channelType;
        this.patientString = patientString;
        this.code = code;
        this.content = content;
    }
}
