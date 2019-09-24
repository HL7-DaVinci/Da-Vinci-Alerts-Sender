package org.hl7.davinci.alerts.refimpl.receiver.fhir;

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import org.hl7.davinci.alerts.refimpl.receiver.service.MessageService;
import org.hl7.fhir.r4.model.Bundle;

public class SystemProvider {

    private MessageService messageService;

    SystemProvider(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(name="$process-message")
    public Bundle closureOperation(@OperationParam(name = "someName") Bundle bundle){
        messageService.notificationReceived(bundle);
        return bundle;
    }

}
