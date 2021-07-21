package com.example.imgtest.repository;

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
    public void get_match_licenses_for_a_customer_returns_match_licenses_only() {
        // We have 3 licenses for the same customer (1 of type tournament, 2 of type match)
        UUID customerId = UUID.fromString("6b32508e-6ba5-44d4-82dc-4742caad7cf8");
        List<LicenseRecord> records = List.of(new LicenseRecord(UUID.randomUUID(), customerId, LicenseType.MATCH, LocalDateTime.now(), "Joe", "Marc"),
            new LicenseRecord(UUID.randomUUID(), customerId, LicenseType.MATCH, LocalDateTime.now(), "Marc", "Joe"),
            new LicenseRecord(UUID.randomUUID(), customerId, LicenseType.TOURNAMENT, LocalDateTime.now(), "Joe", "Marc"));

        Mono<List<LicenseRecord>> licenses = dynamoDbPlayerRepository.getLicenses(customerId, LicenseType.MATCH);

        StepVerifier.create(licenses).expectNextMatches(list -> list.containsAll(records)).verifyComplete();
    }

    @Test
    public void test_db_connection() {
        Assert.assertEquals(dynamoDbPlayerRepository.ciaoDynamo(), true);
    }

}
