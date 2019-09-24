package org.hl7.davinci.alerts.refimpl.receiver.fhir;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.apache.commons.io.IOUtils;
import org.hl7.davinci.alerts.refimpl.receiver.service.MessageService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.Parameters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommunicationResourceProvider implements IResourceProvider {

    private MessageService messageService;
    private IParser parser;

    CommunicationResourceProvider(MessageService messageService, IParser parser) {
        this.messageService = messageService;
        this.parser = parser;
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Communication.class;
    }

    @Operation(name = "$notify", manualRequest = true, manualResponse = true)
    public void manualNotify(
            HttpServletRequest theServletRequest,
            HttpServletResponse theServletResponse
    ) throws IOException {
        IBaseResource iBaseResource = parser.parseResource(new String(IOUtils.toByteArray(theServletRequest.getInputStream())));
        Parameters parameters = (Parameters) iBaseResource;
        messageService.notificationReceived(parameters);

        theServletResponse.setStatus(200);

    }

}
