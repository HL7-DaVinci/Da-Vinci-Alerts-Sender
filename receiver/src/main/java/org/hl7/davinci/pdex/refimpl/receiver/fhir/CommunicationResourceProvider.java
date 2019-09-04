package org.hl7.davinci.pdex.refimpl.receiver.fhir;

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.davinci.pdex.refimpl.receiver.service.MessageService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.Parameters;

public class CommunicationResourceProvider implements IResourceProvider {
    private MessageService messageService;

    CommunicationResourceProvider(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Communication.class;
    }


    @Operation(name = "$notify")
    public Bundle notify(@OperationParam(name = "alert-bundle") Bundle bundle) {
        messageService.messageReceived(bundle);
        return bundle;
    }

}
