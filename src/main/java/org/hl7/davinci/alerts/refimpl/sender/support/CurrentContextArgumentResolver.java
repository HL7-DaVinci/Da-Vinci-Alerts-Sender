package org.hl7.davinci.alerts.refimpl.sender.support;

import com.healthlx.smartonfhir.core.SmartOnFhirContext;
import org.hl7.davinci.alerts.refimpl.sender.dto.CurrentContextDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentContextArgumentResolver implements HandlerMethodArgumentResolver {

    private SmartOnFhirContext smartOnFhirContext;

    @Autowired
    public CurrentContextArgumentResolver(SmartOnFhirContext smartOnFhirContext) {
        this.smartOnFhirContext = smartOnFhirContext;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(CurrentContextDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        CurrentContextDto currentContextDto = new CurrentContextDto(
                smartOnFhirContext.getPatient(),
                null,
                smartOnFhirContext.getProfile()
        );
        if (!"Practitioner".equals(currentContextDto.getUserType())) {
            throw new IllegalStateException("Current user is not a Practitioner");
        }


        return currentContextDto;
    }

}
