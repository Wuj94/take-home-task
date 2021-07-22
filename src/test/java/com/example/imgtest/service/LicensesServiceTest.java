package com.example.imgtest.service;


import static com.example.imgtest.repository.config.DynamoDbTableSetup.CUSTOMER_ID;
import static org.mockito.Mockito.when;


import com.example.imgtest.repository.config.DynamoDbLicensesRepository;
import com.example.imgtest.repository.config.LicenseRecord;
import com.example.imgtest.repository.config.LicenseType;
import java.time.LocalDateTime;
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
import reactor.core.publisher.Mono;

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

    @Test
    public void get_licenses_by_customer_and_type_returns_records_accordingly() {
        final UUID customerId = CUSTOMER_ID;
        final LicenseType licenseType = LicenseType.TOURNAMENT;

        when(dynamoDbLicensesRepository.getLicenses(customerId, licenseType))
            .thenReturn(Mono.just(List.of(new LicenseRecord(UUID.randomUUID(), customerId, LicenseType.TOURNAMENT,
                LocalDateTime.now(), "Joe", "Marc"))));

        List<LicenseRecord> result = licensesService.getLicenses(customerId, licenseType).block();

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(customerId, result.get(0).getCustomerId());
        Assert.assertEquals(LicenseType.TOURNAMENT, result.get(0).getType());
    }

    @Test
    public void get_licenses_by_nonexistent_customer_id_returns_empty_result() {
        final UUID customerId = UUID.randomUUID();

        when(dynamoDbLicensesRepository.getLicenses(customerId, LicenseType.MATCH))
            .thenReturn(Mono.just(List.of()));
        List<LicenseRecord> licenses = licensesService.getLicenses(customerId, LicenseType.MATCH).block();

        Assert.assertTrue(licenses.isEmpty());
    }
}
