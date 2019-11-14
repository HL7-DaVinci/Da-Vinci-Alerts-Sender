package org.hl7.davinci.alerts.refimpl.sender.alerts.message;

import org.hl7.davinci.alerts.refimpl.sender.alerts.AlertType;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

@Component(AlertType.ADMIT_INPATIENT)
public class AdmitImpatientCreator extends BaseMessageCreator {

    public Bundle createMessageBundle(Patient patient) {
        return createDemoProcessMessage(
                patient,
                "IMP",
                "impatient encounter",
                getTopic(AlertType.ADMIT_INPATIENT, "Alert Admit Inpatient").getCodingFirstRep()
        );
    }
}
