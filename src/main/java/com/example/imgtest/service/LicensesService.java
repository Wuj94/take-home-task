package com.example.imgtest.service;

import com.example.imgtest.repository.LicensesRepository;
import com.example.imgtest.repository.config.LicenseRecord;
import com.example.imgtest.repository.config.LicenseType;
import java.util.List;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class LicensesService {

    private final LicensesRepository licensesRepository;

    public LicensesService(@Autowired  LicensesRepository licensesRepository) {
        this.licensesRepository = licensesRepository;
    }

    Mono<List<LicenseRecord>> getLicenses(UUID customerId, LicenseType type) {
        return licensesRepository.getLicenses(customerId, type);
    }

}
