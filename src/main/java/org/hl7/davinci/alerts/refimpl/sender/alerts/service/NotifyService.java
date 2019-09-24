package org.hl7.davinci.alerts.refimpl.sender.alerts.service;

import org.hl7.davinci.alerts.refimpl.sender.fhir.IGenericClientProvider;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.Parameters;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {

    private final IGenericClientProvider clientProvider;

    public NotifyService(IGenericClientProvider clientProvider) {
        this.clientProvider = clientProvider;
    }

    public String notify(Parameters parameters, String receiverUrl) {
        Parameters result = clientProvider.client(receiverUrl, null)
                .operation()
                .onType(Communication.class)
                .named("$notify")
                .withParameters(parameters)
                .execute();
        return result.toString();
    }
}
