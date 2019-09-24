package org.hl7.davinci.alerts.refimpl.receiver.controllers;

import org.hl7.davinci.alerts.refimpl.receiver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/launch")
    public String main() {
        return "index";
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        return messageService.subscribe();
    }


}
