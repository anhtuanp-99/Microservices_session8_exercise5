package com.medical.appointmentservice.dto.external;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceDTO {
    private Long patientId;
    private Boolean approved;
    private String message;
    private String insuranceCode;
}