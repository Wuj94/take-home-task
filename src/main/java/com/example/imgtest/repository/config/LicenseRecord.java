package com.example.imgtest.repository.config;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LicenseRecord {

    public static final String CUSTOMER_LICENSE_GSI = "customerLicenseGsi";

    @Getter(onMethod_= {@DynamoDbPartitionKey})
    private UUID licenseId;

    @Getter(onMethod_= {@DynamoDbSecondaryPartitionKey(indexNames = {CUSTOMER_LICENSE_GSI})})
    private UUID customerId;

    @Getter(onMethod_= {@DynamoDbSecondarySortKey(indexNames = {CUSTOMER_LICENSE_GSI})})
    private LicenseType type;

    private LocalDateTime startDate;

    private String playerA;

    private String playerB;

}
