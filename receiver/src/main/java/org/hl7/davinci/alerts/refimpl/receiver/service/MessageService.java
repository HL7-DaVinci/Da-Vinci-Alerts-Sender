package org.hl7.davinci.alerts.refimpl.receiver.service;

import ca.uhn.fhir.parser.IParser;
import com.google.gson.Gson;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class MessageService {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private IParser iParser;

    @Autowired
    public MessageService(IParser iParser) {
        this.iParser = iParser;
    }

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter();
        this.emitters.add(emitter);
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        return emitter;
    }

    public void notificationReceived(Bundle bundle) {
        emitReceivedMessage(formatBundleIntoMessage(bundle));
    }

    public void notificationReceived(Parameters parameters) {
        emitReceivedMessage(formatParametersIntoMessage(parameters));
    }

    private void emitReceivedMessage(String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(message);
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });

        this.emitters.removeAll(deadEmitters);
    }

    private String formatParametersIntoMessage(Parameters parameters) {
        Parameters.ParametersParameterComponent parameterComponent = null;
        for (Parameters.ParametersParameterComponent next : parameters.getParameter()) {
            if (next.getName().equals("alert")) {
                parameterComponent = next;
            }
        }

        Bundle bundle = null;
        Endpoint endpoint = null;
        for (Parameters.ParametersParameterComponent parametersParameterComponent : parameterComponent.getPart()) {
            if (parametersParameterComponent.getName().equals("alert-bundle")) {
                bundle = (Bundle) parametersParameterComponent.getResource();
            }
            if (parametersParameterComponent.getName().equals("alert-endpoint")) {
                endpoint = (Endpoint) parametersParameterComponent.getResource();
            }
        }

        String parametersString = iParser.encodeResourceToString(parameters);
        Patient patient = getPatient(bundle);
        String patientString = patient.getNameFirstRep().getNameAsSingleString();
        String code = getEncounterCode(bundle);
        return new Gson().toJson(new Message("$notify", patientString, code, parametersString));
    }

    private String formatBundleIntoMessage(Bundle bundle) {
        String bundleString = iParser.encodeResourceToString(bundle);
        Patient patient = getPatient(bundle);
        String patientString = patient.getNameFirstRep().getNameAsSingleString();
        String code = getEncounterCode(bundle);
        return new Gson().toJson(new Message("$process-message", patientString, code, bundleString));
    }

    private String getEncounterCode(Bundle bundle) {
        Encounter encounter = (Encounter) getFirstResourceOfType(bundle, ResourceType.Encounter);
        if (encounter.getClass_().isEmpty()) {//we are at discharge
            Coding coding = encounter.getHospitalization().getDischargeDisposition().getCodingFirstRep();
            return "Discharge disposition code: " + coding.getCode() + " " + coding.getDisplay();
        }
        return "Encounter code: " + encounter.getClass_().getCode() + "  " + encounter.getClass_().getDisplay();
    }

    private Patient getPatient(Bundle bundle) {
        return (Patient) getFirstResourceOfType(bundle, ResourceType.Patient);
    }

    private Resource getFirstResourceOfType(Bundle bundle, ResourceType type) {
        Resource res = null;
        Iterator<Bundle.BundleEntryComponent> iterator = bundle.getEntry().iterator();
        while (iterator.hasNext() && res == null) {
            Resource resource = iterator.next().getResource();
            if (type.equals(resource.getResourceType())) {
                res = resource;
            }
        }
        return res;
    }

}
