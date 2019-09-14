package org.hl7.davinci.pdex.refimpl.sender.alerts.message;

import org.hl7.davinci.pdex.refimpl.sender.alerts.AlertType;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component(value = AlertType.ADMIT_FOROBSERVATION)
public class AdmitForObservationCreator extends BaseMessageCreator {

    public Parameters createNotifyOperation(Patient patient) {
        return createDemoNotifyParams(
                patient,
                "OBSENC", "observation encounter",
                getTopic(AlertType.ADMIT_FOROBSERVATION, "Alert Admit for Observation")
        );
    }



    public Bundle createMessageBundle(Patient patient) {
        return createDemoProcessMessage(
                patient,
                "OBSENC",
                "observation encounter",
                getTopic(AlertType.ADMIT_FOROBSERVATION, "Alert Admit for Observation").getCodingFirstRep()
        );
    }
}
