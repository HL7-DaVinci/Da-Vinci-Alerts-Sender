package org.hl7.davinci.alerts.refimpl.sender.alerts.message.config;

import com.google.common.base.Strings;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;

import java.util.Collections;
import java.util.UUID;

public class SampleMessageCreatorUtil {

  public static Bundle createMessageBundle(Patient patient, Encounter encounter) {
    Bundle bundle = createSampleBundle();

    MessageHeader header = createSampleMessageHeader(encounter);

    bundle.addEntry()
        .setFullUrl("urn:uuid:" + header.getId())
        .setResource(header);

    bundle.addEntry()
        .setFullUrl(composeFullUrl(patient.getIdElement()))
        .setResource(patient);

    Bundle.BundleEntryComponent encounterEntry = bundle.addEntry()
        .setResource(encounter);
    //If id is not assigned - this is a stub Encounter and needs a random UUID.
    if (Strings.isNullOrEmpty(encounter.getId())) {
      encounter.setId(UUID.randomUUID()
          .toString());
      encounterEntry.setFullUrl(encounter.getId());
    } else {
      encounterEntry.setFullUrl(composeFullUrl(encounter.getIdElement()));
    }

    Condition condition = createSampleCondition(patient);

    bundle.addEntry()
        .setFullUrl("urn:uuid:" + condition.getId())
        .setResource(condition);
    //    bundle.addEntry()
    //        .setResource(new Coverage());
    //    bundle.addEntry()
    //        .setResource(new Organization());
    Endpoint endpoint = createSampleEndpoint();
    bundle.addEntry()
        .setFullUrl("urn:uuid:" + endpoint.getId())
        .setResource(endpoint);
    //    bundle.addEntry()
    //        .setResource(new MessageDefinition());

    return bundle;
  }

  public static MessageHeader createSampleMessageHeader(Encounter encounter) {
    MessageHeader header = new MessageHeader().setEvent(
        EncounterToNotificationEventConfig.eventCodingForEncounter(encounter))
        .setDestination(Collections.singletonList(new MessageHeader.MessageDestinationComponent().setName(
            "Acme Message Gateway")
            .setTarget(new Reference().setDisplay("Device/example"))
            .setEndpoint("llp:10.11.12.14:5432")
            .setReceiver(new Reference("http://acme.com/ehr/fhir/PractitionerRole/example"))))
        .setSender(new Reference("7221aa91-d9e1-44fa-8b90-c5a8c7caeade"))
        .setEnterer(new Reference("http://acme.com/ehr/fhir/Practitioner/example-1"))
        .setAuthor(new Reference("http://acme.com/ehr/fhir/Practitioner/example-1"))
        .setSource(new MessageHeader.MessageSourceComponent().setName("Acme Central Patient Registry")
            .setSoftware("FooBar Patient Manager")
            .setVersion("3.1.45.AABB")
            .setContact(new ContactPoint().setSystem(ContactPoint.ContactPointSystem.fromCode("phone"))
                .setValue("+1 (555) 123 4567"))
            .setEndpoint("llp:10.11.12.13:5432"))
        .setFocus(Collections.singletonList(new Reference(encounter)));
    header.setId(UUID.randomUUID()
        .toString());
    return header;
  }

  public static Condition createSampleCondition(Patient patient) {
    Condition condition = new Condition().setSubject(new Reference(patient).setDisplay(patient.getNameFirstRep()
        .primitiveValue()))
        .setClinicalStatus(new CodeableConcept(
            new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "active", "Active")).setText(
            "Active"))
        .setCode(
            new CodeableConcept(new Coding("http://snomed.info/sct", "442311008", "Liveborn born in hospital")).setText(
                "Single liveborn, born in hospital"))
        .setCategory(Collections.singletonList(new CodeableConcept(
            new Coding("http://terminology.hl7.org/CodeSystem/condition-category", "problem-list-item",
                "Problem List Item")).setText("Problem")))
        .setVerificationStatus(new CodeableConcept(
            new Coding("http://terminology.hl7.org/CodeSystem/condition-ver-status", "confirmed", "Confirmed")).setText(
            "Confirmed"));
    condition.setId(UUID.randomUUID()
        .toString());
    return condition;
  }

  public static Endpoint createSampleEndpoint() {
    Endpoint endpoint = new Endpoint().setConnectionType(
        new Coding("http://terminology.hl7.org/CodeSystem/endpoint-connection-type", "hl7-fhir-rest", "hl7-fhir-rest"))
        .addPayloadType(new CodeableConcept().addCoding(
            new Coding("http://terminology.hl7.org/CodeSystem/endpoint-payload-type", "hl7-fhir-rest",
                "hl7-fhir-rest")))
        .setStatus(Endpoint.EndpointStatus.ACTIVE)
        .setName("Acme Hospital EHR FHIR R4 Server")
        .setAddress("https://fhir-ehr.acme/r4/1234/")
        .setHeader(Collections.singletonList(new StringType("Authorization: Bearer this_is_demo")));
    endpoint.setId(UUID.randomUUID()
        .toString());
    return endpoint;
  }

  public static Bundle createSampleBundle() {
    Bundle bundle = new Bundle();
    bundle.setType(Bundle.BundleType.MESSAGE);
    bundle.setId(UUID.randomUUID()
        .toString());
    InstantType now = (InstantType) InstantType.now()
        .setTimeZoneZulu(true);
    bundle.setTimestampElement(now);
    bundle.setMeta(new Meta().setLastUpdatedElement(now)
        .addProfile("http://hl7.org/fhir/us/davinci-alerts/StructureDefinition/notifications-bundle"));
    return bundle;
  }

  public static String composeFullUrl(IdType idType) {
    return idType.getBaseUrl() + "/" + idType.getResourceType() + "/" + idType.getIdPart();
  }

}
