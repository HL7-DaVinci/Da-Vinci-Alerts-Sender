package org.hl7.davinci.alerts.refimpl.sender.alerts.service;

import org.hl7.davinci.alerts.refimpl.sender.fhir.IGenericClientProvider;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.stereotype.Service;

@Service
public class FhirMessageService {

    private final IGenericClientProvider clientProvider;

    public FhirMessageService(IGenericClientProvider clientProvider) {
        this.clientProvider = clientProvider;
    }

    public String sendMessage(Bundle bundle, String receiverUrl) {
        Bundle result2 = clientProvider.client(receiverUrl, null)
                .operation()
                .processMessage()
                .setMessageBundle(bundle)
                .asynchronous(Bundle.class)
                .execute();

        return result2.toString();
    }

}
