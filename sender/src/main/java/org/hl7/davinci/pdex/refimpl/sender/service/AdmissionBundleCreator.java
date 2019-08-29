package org.hl7.davinci.pdex.refimpl.sender.service;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.*;

import java.util.Arrays;
import java.util.Collections;

public class AdmissionBundleCreator {

    public static Bundle createNotifyBundle(Patient patient){
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

        return bundle;
    }

    public static Bundle createMessageBundle(Patient patient) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        Encounter encounter = new Encounter()
            .setType(Collections.singletonList(
                new CodeableConcept(new Coding("http://www.ama-assn.org/go/cpt","12345","Admit to zyx")))
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
