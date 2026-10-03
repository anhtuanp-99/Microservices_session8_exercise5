package com.medical.appointmentservice.dto.external;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {
    private Long id;
    private String name;
    private String specialization;
    private Integer experienceYears;
    private String email;
    private Boolean status;
}