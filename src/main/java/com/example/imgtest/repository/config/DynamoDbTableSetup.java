package com.example.imgtest.repository.config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.Projection;

@Log4j2
@Component
public class DynamoDbTableSetup {
    private final DynamoDbAsyncClient client;
    private final DynamoDbEnhancedAsyncClient enhancedClient;
    private final String tableName;
    public static final UUID CUSTOMER_ID = UUID.fromString("6b32508e-6ba5-44d4-82dc-4742caad7cf8");

    public DynamoDbTableSetup(DynamoDbAsyncClient client,
                              DynamoDbEnhancedAsyncClient enhancedClient,
                              @Value("${database.table}") String tableName) {
        this.client = client;
        this.enhancedClient = enhancedClient;
        this.tableName = tableName;
        run();
    }

    public void run() {
        try {
            final List<String> tableNames = client.listTables().get().tableNames();
            DynamoDbAsyncTable<LicenseRecord> dynamoTable = enhancedClient.table(tableName, TableSchema.fromClass(
                LicenseRecord.class));
            if (!tableNames.contains(tableName)) {
                dynamoTable.createTable(getEnhancedRequestForGSIs()).get();
            }

            //Loading test dataset
            dynamoTable.putItem(new LicenseRecord(UUID.randomUUID(), CUSTOMER_ID, LicenseType.MATCH, LocalDateTime.now(), "Joe", "Marc")).get();
            dynamoTable.putItem(new LicenseRecord(UUID.randomUUID(), CUSTOMER_ID, LicenseType.MATCH, LocalDateTime.now(), "Marc", "Joe")).get();
            dynamoTable.putItem(new LicenseRecord(UUID.randomUUID(), CUSTOMER_ID, LicenseType.TOURNAMENT, LocalDateTime.now(), "Joe", "Marc")).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CreateTableEnhancedRequest getEnhancedRequestForGSIs() {
        return CreateTableEnhancedRequest.builder()
            .globalSecondaryIndices(
                EnhancedGlobalSecondaryIndex.builder()
                    .indexName(LicenseRecord.CUSTOMER_LICENSE_GSI)
                    .projection(Projection.builder().projectionType("ALL").build())
                    .build())
            .build();
    }

}
