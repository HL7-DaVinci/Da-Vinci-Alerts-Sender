package org.hl7.davinci.alerts.refimpl.sender.alerts.message;

import org.hl7.davinci.alerts.refimpl.sender.alerts.AlertType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Component;

@Component(value = AlertType.ADMIT_ER)
public class AdmitERCreator extends BaseMessageCreator {
    @Override
    public Parameters createNotifyOperation(Patient patient) {
        return createDemoNotifyParams(
                patient,
                "EMER", "emergency",
                getTopic(AlertType.ADMIT_ER, "Alert Admit ER")
        );
    }

    @Override
    public Bundle createMessageBundle(Patient patient) {
        return createDemoProcessMessage(
                patient,
                "EMER",
                "emergency",
                getTopic(AlertType.ADMIT_ER, "Alert Admit ER").getCodingFirstRep()
        );
    }
}
