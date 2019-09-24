package org.hl7.davinci.alerts.refimpl.sender.alerts.message;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;

public interface MessageCreator {

    //String getType();

    Parameters createNotifyOperation(Patient patient);

    Bundle createMessageBundle(Patient patient);

}
