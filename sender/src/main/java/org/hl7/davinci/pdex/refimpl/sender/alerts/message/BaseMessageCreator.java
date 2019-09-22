package org.hl7.davinci.pdex.refimpl.sender.alerts.message;

import org.hl7.fhir.r4.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

abstract class BaseMessageCreator implements MessageCreator {

    Parameters createDemoNotifyParams(Patient patient, String encounterCode, String encounterDisplay, CodeableConcept topic) {
        Bundle bundle = new Bundle()
                .setType(Bundle.BundleType.COLLECTION);

        bundle.addEntry().setResource(patient)
                .getRequest()
                .setMethod(Bundle.HTTPVerb.POST);


        Encounter encounter = createEncounter(patient, encounterCode, encounterDisplay);
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
                .setTopic(topic)
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
        parameters.setId("admit-1");
        Parameters.ParametersParameterComponent param1 = new Parameters.ParametersParameterComponent();
        param1.setName("alert");
        param1.setPart(Arrays.asList(
                new Parameters.ParametersParameterComponent().setName("alert-endpoint").setResource(endpoint),
                new Parameters.ParametersParameterComponent().setName("alert-bundle").setResource(bundle)
        ));
        parameters.addParameter(param1);
        return parameters;
    }

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

    Encounter createEncounter(Patient patient, String code, String display) {
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
