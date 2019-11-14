package org.hl7.davinci.alerts.refimpl.sender.alerts.message;

import org.hl7.fhir.r4.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

abstract class BaseMessageCreator implements MessageCreator {

    Bundle createDemoProcessMessage(Patient patient, String code, String display, Coding topic) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        Encounter encounter = createEncounter(patient, code, display);

        MessageHeader header = new MessageHeader()
                .setEvent(topic)
                .setDestination(Collections.singletonList(
                        new MessageHeader.MessageDestinationComponent()
                                .setName("Acme Message Gateway")
                                .setTarget(new Reference().setDisplay("Device/example"))
                                .setEndpoint("llp:10.11.12.14:5432")
                                .setReceiver(new Reference("http://acme.com/ehr/fhir/PractitionerRole/example"))
                ))
                .setSender(new Reference("7221aa91-d9e1-44fa-8b90-c5a8c7caeade"))
                .setEnterer(new Reference("http://acme.com/ehr/fhir/Practitioner/example-1"))
                .setAuthor(new Reference("http://acme.com/ehr/fhir/Practitioner/example-1"))
                .setSource(new MessageHeader.MessageSourceComponent()
                        .setName("Acme Central Patient Registry")
                        .setSoftware("FooBar Patient Manager")
                        .setVersion("3.1.45.AABB")
                        .setContact(new ContactPoint()
                                .setSystem(ContactPoint.ContactPointSystem.fromCode("phone"))
                                .setValue("+1 (555) 123 4567")
                        )
                        .setEndpoint("llp:10.11.12.13:5432")
                )
                //todo add message definition and structure elements better.
                .setFocus(Collections.singletonList(new Reference(encounter)));

        bundle.addEntry().setResource(header)
                .getRequest()
                .setMethod(Bundle.HTTPVerb.POST);

        bundle.addEntry().setResource(patient);
        bundle.addEntry().setResource(encounter);

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

    CodeableConcept getTopic(String code, String display) {
        return new CodeableConcept(new Coding("http://hl7.org/fhir/us/davinci-alerts/CodeSystem/communication-topic", code, display)).setText(display);
    }

}
