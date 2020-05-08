package org.hl7.davinci.alerts.refimpl.sender.alerts.message;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import org.hl7.davinci.alerts.refimpl.sender.alerts.AlertType;
import org.hl7.davinci.alerts.refimpl.sender.alerts.message.config.EncounterToNotificationEventConfig;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterHospitalizationComponent;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.MessageDefinition;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.stereotype.Component;

@Component(value = AlertType.TRANSFER)
public class TransferCreator extends DischargeCreator {

    @Override
    protected EncounterHospitalizationComponent createEncounterHospitalizationComponent(){
        return new Encounter.EncounterHospitalizationComponent().setAdmitSource(
            new CodeableConcept(
                new Coding(
                    "http://terminology.hl7.org/CodeSystem/admit-source",
                    "transfer",
                    "Transfer"
                )
            )
        );
    }
}
