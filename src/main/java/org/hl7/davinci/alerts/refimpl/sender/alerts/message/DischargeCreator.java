package org.hl7.davinci.alerts.refimpl.sender.alerts.message;

import org.hl7.davinci.alerts.refimpl.sender.alerts.AlertType;
import org.hl7.davinci.alerts.refimpl.sender.alerts.message.config.SampleMessageCreatorUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterHospitalizationComponent;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Component(value = AlertType.DISCHARGE)
public class DischargeCreator extends BaseMessageCreator {

    @Override
    public Bundle createMessageBundle(Patient patient) {
        Bundle bundle = SampleMessageCreatorUtil.createSampleBundle();

        Encounter encounter = (Encounter) new Encounter()
            .setStatus(Encounter.EncounterStatus.FINISHED)
            .setClass_(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", null))
            .setSubject(new Reference(patient))
            .setPeriod(new Period().setStart(new Date()).setEnd(new Date()))
            .setHospitalization( createEncounterHospitalizationComponent())
            .setLocation(Collections.singletonList(new Encounter.EncounterLocationComponent(new Reference("Location/hl7east"))))
            .setType(Collections.singletonList(new CodeableConcept(new Coding("http://www.ama-assn.org/go/cpt", "99234", "99234")).setText("inpatient hospital care")))
            .setId(UUID.randomUUID().toString());

        MessageHeader header = SampleMessageCreatorUtil.createSampleMessageHeader(encounter);

        bundle.addEntry().setFullUrl("urn:uuid:" + header.getId()).setResource(header);
        bundle.addEntry().setFullUrl("urn:uuid:" + encounter.getId()).setResource(encounter);
        bundle.addEntry().setFullUrl(SampleMessageCreatorUtil.composeFullUrl(patient.getIdElement())).setResource(patient);

        Condition condition = SampleMessageCreatorUtil.createSampleCondition(patient);

        bundle.addEntry().setFullUrl("urn:uuid:" + condition.getId()).setResource(condition);
        //bundle.addEntry().setResource(new Coverage());
        //bundle.addEntry().setResource(new Organization());
        Endpoint endpoint = SampleMessageCreatorUtil.createSampleEndpoint();
        bundle.addEntry().setFullUrl("urn:uuid:" + endpoint.getId()).setResource(endpoint);
        //bundle.addEntry().setResource(new MessageDefinition());

        return bundle;
    }

    protected EncounterHospitalizationComponent createEncounterHospitalizationComponent(){
        return new Encounter.EncounterHospitalizationComponent().setDischargeDisposition(
            new CodeableConcept(
                new Coding(
                    "http://terminology.hl7.org/CodeSystem/discharge-disposition",
                    "home",
                    "Home"
                )
            )
        );
    }
}
