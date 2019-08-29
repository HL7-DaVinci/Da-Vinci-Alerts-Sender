package org.hl7.davinci.pdex.refimpl.sender.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private String receiverUrl;
    private String receiverApp;

    public IndexController(@Value("${receiverUrl}") String receiverUrl, @Value("${receiverApp}") String receiverApp) {
        this.receiverUrl = receiverUrl;
        this.receiverApp = receiverApp;
    }

    @GetMapping("/launch")
    public String main(Model model) {
        model.addAttribute("receiverUrl", receiverUrl);
        model.addAttribute("receiverApp", receiverApp);
        return "index";
    }

}
