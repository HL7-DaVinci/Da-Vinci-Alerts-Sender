package org.hl7.davinci.alerts.refimpl.sender.alerts.message.config;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Encounter;

import java.util.HashMap;
import java.util.Map;

/**
 * Simplified way to get MessageHEader.eventCoding based on Encounter.class or Encounter.hospitalization.dischargeDisposition
 */
public class EncounterToNotificationEventConfig {

  private static String codeSystem = "http://hl7.org/fhir/us/davinci-alerts/CodeSystem/communication-topic";

  private static Map<String, Coding> encounterClassCodes = new HashMap<String,Coding>(){{
    put("AMB",new Coding(codeSystem,"notification-admit-ambulatory","Notification Admit Ambulatory"));
    put("EMER",new Coding(codeSystem,"notification-admit-er","Notification Admit ER"));
    put("OBSENC",new Coding(codeSystem,"notification-admit-forobservation","Notification Admit for Observation"));
    put("IMP",new Coding(codeSystem,"notification-admit-inpatient","Notification Admit Inpatient"));
  }};

  private static Map<String, Coding> encounterDischargeCodes = new HashMap<String,Coding>(){{
    put("home",new Coding(codeSystem,"notification-discharge-home","Notification Discharge Home"));
    put("rehab",new Coding(codeSystem,"notification-discharge-rehab","Notification Discharge Rehab"));
    put("snf",new Coding(codeSystem,"notification-discharge-skilled-nursing","Notification Discharge Skilled Nursing"));
    put("long",new Coding(codeSystem,"notification-discharge-longterm-care","Notification Discharge Longterm Care"));
    put("exp",new Coding(codeSystem,"notification-discharge-desceased","Notification Discharge Desceased"));
  }};

  public static Coding eventCodingForEncounter(Encounter encounter){
    if(encounter.hasClass_()){
      return encounterClassCodes.get(encounter.getClass_().getCode());
    }
    if(encounter.getHospitalization().hasDischargeDisposition()){
      return encounterDischargeCodes.get(encounter.getHospitalization().getDischargeDisposition().getCodingFirstRep().getCode());
    }
    return new Coding(codeSystem, "none","No event coding was found for encounter");
  }

}
