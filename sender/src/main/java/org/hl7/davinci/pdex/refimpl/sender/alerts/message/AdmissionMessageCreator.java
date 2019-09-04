package org.hl7.davinci.pdex.refimpl.sender.alerts.message;

import org.hl7.davinci.pdex.refimpl.sender.alerts.EventType;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Component(value = EventType.ADMISSION_OBSERVATION)
public class AdmissionMessageCreator implements MessageCreator {

    public Parameters createNotifyOperation(Patient patient) {
        Bundle bundle = new Bundle()
                .setType(Bundle.BundleType.TRANSACTION);

        bundle.addEntry().setResource(patient)
                .getRequest()
                .setMethod(Bundle.HTTPVerb.POST);

        Encounter encounter = new Encounter()
                .setStatus(Encounter.EncounterStatus.FINISHED)
                .setClass_(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"))
                .setSubject(new Reference(patient))
                .setPeriod(new Period().setStart(new Date()).setEnd(new Date()))
                .setLocation(Collections.singletonList(new Encounter.EncounterLocationComponent(new Reference("Location/hl7east"))))
                .setType(Collections.singletonList(new CodeableConcept(new Coding("http://www.ama-assn.org/go/cpt", "99234", "99234")).setText("inpatient hospital care")));

        bundle.addEntry().setResource(encounter)
                .getRequest()
                .setMethod(Bundle.HTTPVerb.POST);

        Condition condition = new Condition()
                .setSubject(new Reference(patient).setDisplay(patient.getNameFirstRep().primitiveValue()))
                .setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "active", "Active")).setText("Active"))
                .setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "442311008", "Liveborn born in hospital")).setText("Single liveborn, born in hospital"))
                .setCategory(Collections.singletonList(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-category", "problem-list-item", "Problem List Item")).setText("Problem")))
                .setVerificationStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status", "confirmed", "Confirmed")).setText("Confirmed"));
        bundle.addEntry().setResource(condition)
                .getRequest()
                .setMethod(Bundle.HTTPVerb.POST);

        Communication communication = new Communication()
                .setStatus(Communication.CommunicationStatus.COMPLETED)
                .setPriority(Communication.CommunicationPriority.ROUTINE)
                .setSubject(new Reference(patient))
                .setEncounter(new Reference(encounter))
                .setSent(new Date())
                .setRecipient(Collections.singletonList(new Reference("PractitionerRole/example")))
                .setSender(new Reference("Organization/example-2"))
                .setPayload(Arrays.asList(
                        new Communication.CommunicationPayloadComponent().setContent(new StringType("Admit yo xyz")),
                        new Communication.CommunicationPayloadComponent().setContent(new Reference(encounter)),
                        new Communication.CommunicationPayloadComponent().setContent(new Reference(condition))
                ))
                .setAbout(Collections.singletonList(new Reference("Coverage/example-1")))
                .setTopic(new CodeableConcept(new Coding("http://hl7.org/fhir/us/davinci-alerts/CodeSystem/communication-topic", "alert-admit-inpatient", "Alert Admit Inpatient")).setText("Alert Admit Inpatient"))
                .setCategory(Collections.singletonList(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/communication-category", "alert", "Alert")).setText("Alert")))
                .setIdentifier(Collections.singletonList(new Identifier().setSystem("urn:oid:1.3.4.5.6.7").setValue("2345678901")));

        bundle.addEntry().setResource(communication)
                .getRequest()
                .setMethod(Bundle.HTTPVerb.POST);


        Endpoint endpoint = new Endpoint()
                .setConnectionType(new Coding("http://terminology.hl7.org/CodeSystem/endpoint-connection-type", "hl7-fhir-rest", "hl7-fhir-rest"))
                .setStatus(Endpoint.EndpointStatus.ACTIVE)
                .setName("Acme Hospital EHR FHIR R4 Server")
                .setAddress("https://fhir-ehr.acme/r4/1234/")
                .setHeader(Collections.singletonList(new StringType("Authorization: Bearer this_is_demo")));
        Parameters parameters = new Parameters();
        parameters.addParameter().setName("alert-bundle").setResource(bundle);
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