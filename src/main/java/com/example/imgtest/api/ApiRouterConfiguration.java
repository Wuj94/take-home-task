package com.example.imgtest.api;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


import com.example.imgtest.api.handler.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ApiRouterConfiguration {
    public static final String CUSTOMER_PATH_VAR = "CustomerId";
    public static final String LICENSES_BASE_RESOURCE = String.format("/customers/{%s}/licenses", CUSTOMER_PATH_VAR);

    @Bean
    RouterFunction<ServerResponse> routes(Handler handler) {
        return route(GET(LICENSES_BASE_RESOURCE), handler::getLicenses);
    }
}
