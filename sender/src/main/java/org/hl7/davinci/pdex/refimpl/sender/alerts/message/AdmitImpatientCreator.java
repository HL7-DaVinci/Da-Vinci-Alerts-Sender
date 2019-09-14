package org.hl7.davinci.pdex.refimpl.sender.alerts.message;

import org.hl7.davinci.pdex.refimpl.sender.alerts.AlertType;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Component(AlertType.ADMIT_INPATIENT)
public class AdmitImpatientCreator extends BaseMessageCreator {

    public Parameters createNotifyOperation(Patient patient) {
        return createDemoNotifyParams(
                patient,
                "IMP", "impatient encounter",
                getTopic(AlertType.ADMIT_INPATIENT, "Alert Admit Inpatient")
        );
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
