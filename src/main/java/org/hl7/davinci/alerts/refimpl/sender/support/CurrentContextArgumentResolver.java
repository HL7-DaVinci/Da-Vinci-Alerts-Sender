package org.hl7.davinci.alerts.refimpl.sender.support;

import org.hl7.davinci.alerts.refimpl.sender.dto.CurrentContextDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class CurrentContextArgumentResolver implements HandlerMethodArgumentResolver {

    private HttpSession session;

    @Autowired
    public CurrentContextArgumentResolver(HttpSession session) {
        this.session = session;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(CurrentContextDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assert.notNull(authentication, "Authentication object is missing from security context.");
        String profile = ((OAuth2User) authentication.getPrincipal()).getAttribute("profile");

        String patientId = (String)((Map) session.getAttribute("smart-context")).get("patient");
        //String encounterId = (String) context.getAccessToken().getAdditionalInformation().get("encounter");

        CurrentContextDto currentContextDto = new CurrentContextDto(patientId, null, profile);
        if (!"Practitioner".equals(currentContextDto.getUserType())) {
            throw new IllegalStateException("Current user is not a Practitioner");
        }


        return currentContextDto;
    }

}
