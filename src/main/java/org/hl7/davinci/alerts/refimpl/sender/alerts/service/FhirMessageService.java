package org.hl7.davinci.alerts.refimpl.sender.alerts.service;

import org.hl7.davinci.alerts.refimpl.sender.fhir.IGenericClientProvider;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.springframework.stereotype.Service;

@Service
public class FhirMessageService {

    private final IGenericClientProvider clientProvider;

    public FhirMessageService(IGenericClientProvider clientProvider) {
        this.clientProvider = clientProvider;
    }

    public Bundle sendMessage(Bundle bundle, String receiverUrl) {
        return clientProvider.client(receiverUrl, null)
                .operation()
                .processMessage()
                .setMessageBundle(bundle)
                .asynchronous(Bundle.class)
                .execute();
    }

}
