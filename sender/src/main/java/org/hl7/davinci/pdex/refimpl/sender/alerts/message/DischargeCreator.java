package org.hl7.davinci.pdex.refimpl.sender.alerts.message;

import org.hl7.davinci.pdex.refimpl.sender.alerts.AlertType;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@Component(value = AlertType.DISCHARGE)
public class DischargeCreator extends BaseMessageCreator {
    @Override
    public Parameters createNotifyOperation(Patient patient) {
        Bundle bundle = new Bundle()
                .setType(Bundle.BundleType.TRANSACTION);

        bundle.addEntry().setResource(patient)
                .getRequest()
                .setMethod(Bundle.HTTPVerb.POST);

        Encounter encounter = getDischargeEncounter(patient);

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
                .setTopic(getTopic(AlertType.DISCHARGE, "Alert Discharge"))
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

    @Override
    public Bundle createMessageBundle(Patient patient) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.TRANSACTION);

        Encounter encounter = getDischargeEncounter(patient);
        MessageHeader header = new MessageHeader()
                .setEvent(getTopic(AlertType.DISCHARGE, "Alert Discharge").getCodingFirstRep())
                .setFocus(Collections.singletonList(new Reference(encounter)));

        bundle.addEntry().setResource(header)
                .getRequest()
                .setMethod(Bundle.HTTPVerb.POST);


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
        return new Encounter()
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
                .setType(Collections.singletonList(new CodeableConcept(new Coding("http://www.ama-assn.org/go/cpt", "99234", "99234")).setText("inpatient hospital care")));
    }
}
