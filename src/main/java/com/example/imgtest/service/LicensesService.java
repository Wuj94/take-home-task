package com.example.imgtest.service;

import com.example.imgtest.repository.LicensesRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LicensesService {

    private final LicensesRepository licensesRepository;

    public LicensesService(@Autowired  LicensesRepository licensesRepository) {
        this.licensesRepository = licensesRepository;
    }

}
