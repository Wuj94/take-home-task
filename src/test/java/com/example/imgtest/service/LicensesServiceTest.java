package com.example.imgtest.service;

import static reactor.core.publisher.Mono.when;


import com.example.imgtest.repository.config.DynamoDbLicensesRepository;
import com.example.imgtest.repository.config.LicenseRecord;
import com.example.imgtest.repository.config.LicenseType;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.enhanced.dynamodb.internal.client.DefaultDynamoDbAsyncIndex;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {LicensesService.class},
    initializers = {ConfigDataApplicationContextInitializer.class})
@TestPropertySource("classpath:application.properties")
public class LicensesServiceTest {

    @Autowired
    private LicensesService licensesService;

    @MockBean
    private DynamoDbLicensesRepository dynamoDbLicensesRepository;

    @MockBean
    private DefaultDynamoDbAsyncIndex<LicenseRecord> customerLicenseGsi;

    @Test
    public void get_licenses_by_customer_and_type_returns_records_accordingly() {
        UUID customerId = UUID.fromString("6b32508e-6ba5-44d4-82dc-4742caad7cf8");
        List<LicenseRecord> result = licensesService.getLicenses(customerId, LicenseType.TOURNAMENT).block();

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(customerId, result.get(0).getCustomerId());
        Assert.assertEquals(LicenseType.TOURNAMENT, result.get(0).getType());
    }

    @Test
    public void get_licenses_by_invalid_customer_id_throws_bad_parameter_exception() {
        UUID customerId = UUID.randomUUID();
        //mock gsi here

        Assert.assertThrows(BadParameterException.class,
            () -> licensesService.getLicenses(customerId, LicenseType.MATCH).block());
    }
}
