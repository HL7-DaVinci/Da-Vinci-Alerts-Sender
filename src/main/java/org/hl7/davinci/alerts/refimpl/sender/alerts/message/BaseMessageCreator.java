package org.hl7.davinci.alerts.refimpl.sender.alerts.message;

import org.hl7.davinci.alerts.refimpl.sender.alerts.message.config.SampleMessageCreatorUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Reference;

import java.util.Collections;
import java.util.Date;

abstract class BaseMessageCreator implements MessageCreator {

    Bundle createDemoProcessMessage(Patient patient, String code, String display) {
        Encounter encounter = createEncounter(patient, code, display);
        return SampleMessageCreatorUtil.createMessageBundle(patient, encounter);
    }

    private Encounter createEncounter(Patient patient, String code, String display) {
        return new Encounter()
                .setStatus(Encounter.EncounterStatus.FINISHED)
                .setClass_(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", code, display))
                .setSubject(new Reference(patient))
                .setPeriod(new Period().setStart(new Date()).setEnd(new Date()))
                .setLocation(Collections.singletonList(new Encounter.EncounterLocationComponent(new Reference("Location/hl7east"))))
                .setType(Collections.singletonList(new CodeableConcept(new Coding("http://www.ama-assn.org/go/cpt", "99234", "99234")).setText("inpatient hospital care")));
                //In real app we will have id, but here we will just not set it and let SampleMessageCreator assign a random UUID.
    }

}
