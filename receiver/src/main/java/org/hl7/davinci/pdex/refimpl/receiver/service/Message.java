package org.hl7.davinci.pdex.refimpl.receiver.service;

public class Message {
    private final String title;
    private final String content;

    Message(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
