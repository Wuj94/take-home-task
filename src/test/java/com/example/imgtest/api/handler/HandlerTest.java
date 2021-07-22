package com.example.imgtest.api.handler;

import static com.example.imgtest.repository.config.DynamoDbTableSetup.CUSTOMER_ID;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


import com.example.imgtest.repository.config.DynamoDbTableSetup;
import com.example.imgtest.repository.config.LicenseRecord;
import com.example.imgtest.repository.config.LicenseType;
import com.example.imgtest.repository.config.Summary;
import com.example.imgtest.service.LicensesService;
import com.example.imgtest.api.ApiRouterConfiguration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest
@Import( {ApiRouterConfiguration.class, Handler.class, LicensesService.class})
public class HandlerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private LicensesService service;

    @Test
    public void get_licenses_by_customer_id_and_type_returns_list_of_players() {
        final UUID customerId = CUSTOMER_ID;
        final LicenseType type = LicenseType.MATCH;
        final List<LicenseRecord> list = List.of(
            new LicenseRecord(UUID.randomUUID(), customerId, LicenseType.MATCH, LocalDateTime.now(), "Joe", "Marc", Summary.AvB),
            new LicenseRecord(UUID.randomUUID(), customerId, LicenseType.MATCH, LocalDateTime.now(), "Marc", "Joe", Summary.AvBTime));

        when(service.getLicenses(customerId, type)).thenReturn(Mono.just(list));

        client.get()
            .uri(ApiRouterConfiguration.LICENSES_BASE_RESOURCE + String.format("?type=%s", type.toString()), customerId)
            .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
            .exchange().expectStatus().isOk()
//            .expectBody(String.class)
//            .consumeWith(str -> System.out.println(str));
            .expectBody()
            .jsonPath("$.licenses.[0].matchId").exists()
            .jsonPath("$.licenses.[0].playerA").value(equalTo("Joe"))
            .jsonPath("$.licenses.[0].playerB").value(equalTo("Marc"))
            .jsonPath("$.licenses.[0].startDate").exists()
            .jsonPath("$.licenses.[0].summary").exists()
            .jsonPath("$.licenses.[1].matchId").exists()
            .jsonPath("$.licenses.[1].playerA").value(equalTo("Marc"))
            .jsonPath("$.licenses.[1].playerB").value(equalTo("Joe"))
            .jsonPath("$.licenses.[1].startDate").exists()
            .jsonPath("$.licenses.[1].summary").exists()
            .jsonPath("$.licenses.[0].id").doesNotExist();
    }

    @Test
    public void search_players_is_bad_request_for_wrong_type_parameters() {
        final UUID customerId = CUSTOMER_ID;

        client.get()
            .uri(ApiRouterConfiguration.LICENSES_BASE_RESOURCE + String.format("?%s=%s", Handler.TYPE_QUERY_PARAM, "matchctam"), customerId)
            .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
            .exchange().expectStatus().isBadRequest();

    }

    @Test
    public void search_players_is_bad_request_for_missing_type_parameters() {
        final UUID customerId = CUSTOMER_ID;

        client.get()
            .uri(ApiRouterConfiguration.LICENSES_BASE_RESOURCE + String.format("?%s=%s", Handler.TYPE_QUERY_PARAM, "matchctam"), customerId)
            .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
            .exchange().expectStatus().isBadRequest();

    }
}
