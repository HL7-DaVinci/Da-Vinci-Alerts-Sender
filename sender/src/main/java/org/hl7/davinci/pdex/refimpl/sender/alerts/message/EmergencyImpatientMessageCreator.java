package org.hl7.davinci.pdex.refimpl.sender.alerts.message;

import org.hl7.davinci.pdex.refimpl.sender.alerts.EventType;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component(EventType.EMERGENCY_IMPATIENT_ADMISSION)
public class EmergencyImpatientMessageCreator implements MessageCreator {

    public Parameters createNotifyOperation(Patient patient) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        bundle.addEntry().setResource(patient)
                .getRequest()
                .setMethod(Bundle.HTTPVerb.POST);

        bundle.addEntry().setResource(new Encounter());
        bundle.addEntry().setResource(new Condition());
        bundle.addEntry().setResource(new Endpoint());

        Communication communication = new Communication();
        communication.addPayload(new Communication.CommunicationPayloadComponent().setContent(new StringType().setValue("Admit to zyx")));
        bundle.addEntry().setResource(communication);

        Parameters parameters = new Parameters();
        parameters.addParameter().setName("admit").setResource(bundle);

        return parameters;
    }

    public Bundle createMessageBundle(Patient patient) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        Encounter encounter = new Encounter()
                .setType(Collections.singletonList(
                        new CodeableConcept(new Coding("http://www.ama-assn.org/go/cpt", "12345", "Admit to zyx")))
                )
                .setSubject(new Reference(patient));
        MessageHeader header = new MessageHeader().setFocus(Collections.singletonList(new Reference(encounter)));

        bundle.addEntry().setResource(header)
                .getRequest()
                .setMethod(Bundle.HTTPVerb.POST);

        bundle.addEntry().setResource(encounter);
        bundle.addEntry().setResource(new Condition());
        bundle.addEntry().setResource(new Condition());
        bundle.addEntry().setResource(new Coverage());
        bundle.addEntry().setResource(new Endpoint());

        return bundle;
    }
}
