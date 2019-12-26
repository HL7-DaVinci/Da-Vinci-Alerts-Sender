package org.hl7.davinci.alerts.refimpl.sender.alerts.message;

import org.hl7.davinci.alerts.refimpl.sender.alerts.message.config.SampleMessageCreatorUtil;
import org.hl7.fhir.r4.model.*;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

abstract class BaseMessageCreator implements MessageCreator {

    Bundle createDemoProcessMessage(Patient patient, String code, String display) {
        Encounter encounter = createEncounter(patient, code, display);
        return SampleMessageCreatorUtil.createMessageBundle(patient, encounter);
    }

    private Encounter createEncounter(Patient patient, String code, String display) {
        return (Encounter) new Encounter()
                .setStatus(Encounter.EncounterStatus.FINISHED)
                .setClass_(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", code, display))
                .setSubject(new Reference(patient))
                .setPeriod(new Period().setStart(new Date()).setEnd(new Date()))
                .setLocation(Collections.singletonList(new Encounter.EncounterLocationComponent(new Reference("Location/hl7east"))))
                .setType(Collections.singletonList(new CodeableConcept(new Coding("http://www.ama-assn.org/go/cpt", "99234", "99234")).setText("inpatient hospital care")))
                .setId(UUID.randomUUID().toString());//in real app we will have Encounter with real id so we emulate it here.
    }

}
