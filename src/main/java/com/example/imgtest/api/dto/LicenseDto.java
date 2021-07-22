package com.example.imgtest.api.dto;

import com.example.imgtest.repository.config.LicenseRecord;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class LicenseDto {
    @NonNull
    private UUID matchId;

    @NonNull
    private LocalDateTime startDate;

    @NonNull
    private String playerA;

    @NonNull
    private String playerB;

//    private String summary;

    public static LicenseDto from(LicenseRecord licenseRecord) {
        return new LicenseDto(licenseRecord.getLicenseId(),
            licenseRecord.getStartDate(),
            licenseRecord.getPlayerA(),
            licenseRecord.getPlayerB());
    }
}
