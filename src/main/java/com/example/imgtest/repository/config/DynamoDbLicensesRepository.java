package com.example.imgtest.repository.config;


import com.example.imgtest.repository.LicensesRepository;
import java.util.List;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.internal.client.DefaultDynamoDbAsyncIndex;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

@Log4j2
@Repository
public class DynamoDbLicensesRepository implements LicensesRepository {

    private final DynamoDbAsyncTable<LicenseRecord> playerDynamoDbAsyncTable;
    private final DynamoDbEnhancedAsyncClient enhancedClient;
    private DefaultDynamoDbAsyncIndex<LicenseRecord> customerLicenseGsi;


    public DynamoDbLicensesRepository(DynamoDbEnhancedAsyncClient enhancedClient,
                                      @Qualifier("customerLicenseGsi") DefaultDynamoDbAsyncIndex<LicenseRecord> customerLicensesGsi,
                                      @Value("${database.table}") String tableName) {
        log.info("Starting Repository with DynamoDb table name: {}", tableName);
        this.enhancedClient = enhancedClient;
        this.customerLicenseGsi = customerLicensesGsi;
        this.playerDynamoDbAsyncTable = enhancedClient.table(tableName, TableSchema.fromClass(LicenseRecord.class));
    }

    public boolean ciaoDynamo() {
        return true;
    }


    @Override
    public Mono<List<LicenseRecord>> getLicenses(UUID customerId, LicenseType type) {

        return null;
    }
}
