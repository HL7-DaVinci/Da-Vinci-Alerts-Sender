package org.hl7.davinci.alerts.refimpl.sender.alerts.message.config;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;

/**
 * Simplified way to get MessageHeader.eventCoding based on Encounter.class or Encounter.hospitalization
 * .dischargeDisposition
 */
public class EncounterToNotificationEventConfig {

    private static String codeSystem = "http://hl7.org/fhir/us/davinci-alerts/CodeSystem/notification-event";

    public static Coding eventCodingForEncounter(Encounter encounter) {
        if (encounter.hasClass_()) {
            return new Coding(codeSystem, "notification-admit", "Notification Admit");
        }
        if (encounter.getHospitalization().hasDischargeDisposition()) {
            return new Coding(codeSystem, "notification-discharge", "Notification Discharge");
        }
        if (encounter.getHospitalization().hasAdmitSource()) {
            return new Coding(codeSystem, "notification-transfer", "Notification Transfer");
        }
        return new Coding(codeSystem, "none", "No event coding was found for encounter");
    }
}
