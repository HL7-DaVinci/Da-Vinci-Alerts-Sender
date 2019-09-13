package org.hl7.davinci.pdex.refimpl.receiver.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.google.gson.Gson;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MessageService {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe(){
        SseEmitter emitter = new SseEmitter();
        this.emitters.add(emitter);
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        return emitter;
    }

    public void messageReceived(Bundle message,String channelType){
        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(formatBundleIntoMessage(message, channelType));
            }
            catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });

        this.emitters.removeAll(deadEmitters);
    }

    private String formatBundleIntoMessage(Bundle bundle, String channelType) {
        IParser iParser = FhirContext.forR4().newJsonParser().setPrettyPrint(true);
        String bundleString = iParser.encodeResourceToString(bundle);
        Patient patient = ((Patient) bundle.getEntryFirstRep().getResource());
        String patientString = patient.getNameFirstRep().getNameAsSingleString();
        String code = "some code";
        return new Gson().toJson(new Message(channelType+patientString+code,bundleString));
    }

}
