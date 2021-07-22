package com.example.imgtest.api.dto;

import com.example.imgtest.repository.config.LicenseRecord;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Builder(builderMethodName = "internalBuilder", toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetLicenseByCustomerResponse {

    @NonNull
    @Builder.Default
    private ArrayList<LicenseDto> licenses = new ArrayList<>();

    public static GetLicenseByCustomerResponse fromListOfLicenseRecord(List<LicenseRecord> licenseRecords) {
        ArrayList<LicenseDto> result = new ArrayList<>();
        licenseRecords.forEach(el -> result.add(LicenseDto.from(el)));
        return new GetLicenseByCustomerResponse(result);
    }

}
