package com.medical.doctorservice.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorSummaryDTO {
    private Long id;
    private String name;
    private String specialization;
}