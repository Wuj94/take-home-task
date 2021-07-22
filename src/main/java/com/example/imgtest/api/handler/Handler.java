package com.example.imgtest.api.handler;

import static com.example.imgtest.api.ApiRouterConfiguration.CUSTOMER_PATH_VAR;


import com.example.imgtest.api.dto.GetLicenseByCustomerResponse;
import com.example.imgtest.repository.config.LicenseType;
import com.example.imgtest.service.LicensesService;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class Handler {
    static final String TYPE_QUERY_PARAM = "type";

    private static final ServerWebInputException TYPE_INVALID_EXCEPTION =
        new ServerWebInputException("Invalid query params. Reason: type must be one of " + LicenseType.values());

    private final LicensesService service;

    public Handler(@Autowired LicensesService service) {
        this.service = service;
    }

    public Mono<ServerResponse> getLicenses(ServerRequest request) {
        log.debug("Get Licenses: " + request.pathVariables() + "\n" + request.queryParams());
        final UUID customerId = UUID.fromString(request.pathVariables().get(CUSTOMER_PATH_VAR));
        final LicenseType licenseType = request.queryParam(TYPE_QUERY_PARAM).map(LicenseType::fromValue).orElseThrow(() -> TYPE_INVALID_EXCEPTION);

        return service.getLicenses(customerId, licenseType)
            .flatMap(resultSet -> ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Mono.just(GetLicenseByCustomerResponse.fromListOfLicenseRecord(resultSet)), GetLicenseByCustomerResponse.class));
    }

}
