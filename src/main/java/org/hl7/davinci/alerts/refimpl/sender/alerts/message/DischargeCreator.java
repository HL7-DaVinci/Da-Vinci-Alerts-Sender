package org.hl7.davinci.alerts.refimpl.sender.alerts.message;

import org.hl7.davinci.alerts.refimpl.sender.alerts.AlertType;
import org.hl7.davinci.alerts.refimpl.sender.alerts.message.config.EncounterToNotificationEventConfig;
import org.hl7.davinci.alerts.refimpl.sender.alerts.message.config.SampleMessageCreatorUtil;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Component(value = AlertType.DISCHARGE)
public class DischargeCreator extends BaseMessageCreator {

    @Override
    public Bundle createMessageBundle(Patient patient) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        Encounter encounter = getDischargeEncounter(patient);
        MessageHeader header = new MessageHeader()
                .setEvent(EncounterToNotificationEventConfig.eventCodingForEncounter(encounter))
                .setFocus(Collections.singletonList(new Reference(encounter)));

        bundle.addEntry().setResource(header)
                .getRequest()
                .setMethod(Bundle.HTTPVerb.POST);

        bundle.addEntry().setResource(encounter);
        bundle.addEntry().setResource(patient);

        Condition condition = new Condition()
                .setSubject(new Reference(patient).setDisplay(patient.getNameFirstRep().primitiveValue()))
                .setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "active", "Active")).setText("Active"))
                .setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "442311008", "Liveborn born in hospital")).setText("Single liveborn, born in hospital"))
                .setCategory(Collections.singletonList(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-category", "problem-list-item", "Problem List Item")).setText("Problem")))
                .setVerificationStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status", "confirmed", "Confirmed")).setText("Confirmed"));

        bundle.addEntry().setResource(condition);
        bundle.addEntry().setResource(new Coverage());
        bundle.addEntry().setResource(new Organization());
        Endpoint endpoint = new Endpoint()
                .setConnectionType(new Coding("http://terminology.hl7.org/CodeSystem/endpoint-connection-type", "hl7-fhir-rest", "hl7-fhir-rest"))
                .setStatus(Endpoint.EndpointStatus.ACTIVE)
                .setName("Acme Hospital EHR FHIR R4 Server")
                .setAddress("https://fhir-ehr.acme/r4/1234/")
                .setHeader(Collections.singletonList(new StringType("Authorization: Bearer this_is_demo")));
        bundle.addEntry().setResource(endpoint);
        bundle.addEntry().setResource(new MessageDefinition());

        return bundle;
    }

    private Encounter getDischargeEncounter(Patient patient) {
        return (Encounter) new Encounter()
                .setStatus(Encounter.EncounterStatus.FINISHED)
                .setSubject(new Reference(patient))
                .setPeriod(new Period().setStart(new Date()).setEnd(new Date()))
                .setHospitalization(
                        new Encounter.EncounterHospitalizationComponent().setDischargeDisposition(
                                new CodeableConcept(
                                        new Coding(
                                                "http://terminology.hl7.org/CodeSystem/discharge-disposition",
                                                "home",
                                                "Home"
                                        )
                                )
                        )
                )
                .setLocation(Collections.singletonList(new Encounter.EncounterLocationComponent(new Reference("Location/hl7east"))))
                .setType(Collections.singletonList(new CodeableConcept(new Coding("http://www.ama-assn.org/go/cpt", "99234", "99234")).setText("inpatient hospital care")))
                .setId(UUID.randomUUID().toString());
    }
}
