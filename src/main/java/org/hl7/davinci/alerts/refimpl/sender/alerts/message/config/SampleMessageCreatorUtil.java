package org.hl7.davinci.alerts.refimpl.sender.alerts.message.config;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.MessageDefinition;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;

import java.util.Collections;

public class SampleMessageCreatorUtil {

  public static Bundle createMessageBundle(Patient patient, Encounter encounter) {
    Bundle bundle = new Bundle();
    bundle.setType(Bundle.BundleType.TRANSACTION);

    MessageHeader header = new MessageHeader()
        .setEvent(EncounterToNotificationEventConfig.eventCodingForEncounter(encounter))
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

}
