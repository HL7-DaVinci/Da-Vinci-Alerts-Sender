package org.hl7.davinci.alerts.refimpl.sender.alerts.message;

import org.hl7.davinci.alerts.refimpl.sender.alerts.AlertType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterHospitalizationComponent;
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
