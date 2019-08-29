package org.hl7.davinci.pdex.refimpl.sender.service;

import org.hl7.davinci.pdex.refimpl.sender.fhir.IGenericClientProvider;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.Parameters;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    private final IGenericClientProvider clientProvider;

    public AlertService(IGenericClientProvider clientProvider) {
        this.clientProvider = clientProvider;
    }

    public String notify(Bundle bundle, String receiverUrl) {

        Parameters parameters = new Parameters();
        parameters.addParameter().setName("someName").setResource(bundle);

        Parameters result = clientProvider.client(receiverUrl, null)
                .operation()
                .onType(Communication.class)
                .named("$notify")
                .withParameters(parameters)
                .execute();

        return result.toString();
    }

    public String sendMessage(Bundle bundle, String receiverUrl) {
        Bundle result2 = clientProvider.client(receiverUrl,null)
                .operation()
                .processMessage()
                .setMessageBundle(bundle)
                .asynchronous(Bundle.class)
                .execute();

        return result2.toString();
    }
}
