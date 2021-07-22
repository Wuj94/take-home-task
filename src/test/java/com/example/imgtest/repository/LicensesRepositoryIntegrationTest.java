package com.example.imgtest.repository;

import static com.example.imgtest.repository.config.DynamoDbTableSetup.CUSTOMER_ID;


import com.example.imgtest.TestingSpringContext;
import com.example.imgtest.repository.config.DynamoDbConfig;
import com.example.imgtest.repository.config.DynamoDbLicensesRepository;
import com.example.imgtest.repository.config.DynamoDbTableSetup;
import com.example.imgtest.repository.config.LicenseRecord;
import com.example.imgtest.repository.config.LicenseType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Resource;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestingSpringContext.class, DynamoDbConfig.class, DynamoDbTableSetup.class,
    DynamoDbLicensesRepository.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestPropertySource("classpath:application.properties")
public class LicensesRepositoryIntegrationTest {

    @Container
    public static GenericContainer<?> dynamoDbLocal = new GenericContainer<>(
        DockerImageName.parse("amazon/dynamodb-local"))
        .withExposedPorts(8000);

    @Resource(name = "dynamoDbLicensesRepository")
    private DynamoDbLicensesRepository dynamoDbPlayerRepository;

    @DynamicPropertySource
    static void dynamoDBProperties(DynamicPropertyRegistry registry) {
        System.out.println("Started Dynamo on http://" + dynamoDbLocal.getHost() + ":" + dynamoDbLocal.getFirstMappedPort());
        registry.add("aws.dynamodb.endpoint", () -> "http://" + dynamoDbLocal.getHost() + ":" + dynamoDbLocal.getFirstMappedPort());
    }


    @Test
    public void get_licenses_for_customer_and_type_returns_licenses_of_that_type_only() {
        // We have 3 licenses for the same customer (1 of type tournament, 2 of type match)
        final UUID customerId = CUSTOMER_ID;

        Mono<List<LicenseRecord>> matchLicenses = dynamoDbPlayerRepository.getLicenses(customerId, LicenseType.MATCH);
        Mono<List<LicenseRecord>> tournametLicenses = dynamoDbPlayerRepository.getLicenses(customerId, LicenseType.TOURNAMENT);

        StepVerifier.create(matchLicenses).expectNextMatches(list -> {
            return list.size() == 2
                && list.get(0).getType().equals(LicenseType.MATCH)
                && list.get(1).getType().equals(LicenseType.MATCH)
                && list.get(0).getCustomerId().equals(customerId)
                && list.get(1).getCustomerId().equals(customerId);
        }).verifyComplete();

        StepVerifier.create(tournametLicenses).expectNextMatches(list -> {
            return list.size() == 1
                && list.get(0).getType().equals(LicenseType.TOURNAMENT)
                && list.get(0).getCustomerId().equals(customerId);
        }).verifyComplete();
    }

    @Test
    public void get_licenses_for_nonexistent_customer_returns_empty_result() {
        // We have 3 licenses for the same customer (1 of type tournament, 2 of type match)
        final UUID customerId = UUID.randomUUID();

        Mono<List<LicenseRecord>> tournametLicenses = dynamoDbPlayerRepository.getLicenses(customerId, LicenseType.TOURNAMENT);

        StepVerifier.create(tournametLicenses).expectNextMatches(list -> list.isEmpty()).verifyComplete();
    }

    @Test
    public void test_db_connection() {
        Assert.assertEquals(dynamoDbPlayerRepository.ciaoDynamo(), true);
    }

}
