package org.hl7.davinci.alerts.refimpl.receiver.fhir;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.hl7.davinci.alerts.refimpl.receiver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/fhir/*"}, displayName = "FHIR Receiver")
public class FhirReceiverServer extends RestfulServer {

    private static final long serialVersionUID = 1L;

    @Autowired
    private MessageService messageService;

    @Autowired
    private IParser parser;

    /**
     * The initialize method is automatically called when the servlet is starting up, so it can
     * be used to configure the servlet to define resource providers, or set up
     * configuration, interceptors, etc.
     */
    @Override
    protected void initialize() throws ServletException {
        String serverBaseUrl = "http://demo.com/fhir";
        setServerAddressStrategy(new HardcodedServerAddressStrategy(serverBaseUrl));

        registerProvider(new SystemProvider(messageService));

        List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
        resourceProviders.add(new CommunicationResourceProvider(messageService, parser));
        setResourceProviders(resourceProviders);
    }

}
