package com.ricardopassarella.nbrown.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricardopassarella.nbrown.client.ClientFacade;
import com.ricardopassarella.nbrown.client.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateClientIdInterceptor implements HandlerInterceptor {

    private final ClientFacade clientFacade;
    private final ObjectMapper mapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientId = request.getHeader("Client-Id");

        Optional<ClientDto> clientDto = Optional.ofNullable(clientId)
                .map(String::toLowerCase)
                .flatMap(clientFacade::findById);

        if (!clientDto.isPresent()) {
            ErrorResponse msg
                    = new ErrorResponse("Invalid or unknown Client-id");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(mapper.writeValueAsString(msg));
            return false;
        }

        return true;
    }

}
