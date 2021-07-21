package com.example.imgtest.repository;

import com.example.imgtest.repository.config.LicenseRecord;
import com.example.imgtest.repository.config.LicenseType;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface LicensesRepository {
    Mono<List<LicenseRecord>> getLicenses(UUID customerId, LicenseType type);
}
