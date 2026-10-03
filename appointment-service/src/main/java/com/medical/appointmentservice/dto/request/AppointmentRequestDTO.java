package com.medical.appointmentservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequestDTO {

    @NotNull(message = "patientId không được để trống")
    @Min(value = 1, message = "patientId phải lớn hơn 0")
    private Long patientId;

    @NotNull(message = "doctorId không được để trống")
    @Min(value = 1, message = "doctorId phải lớn hơn 0")
    private Long doctorId;

    @NotNull(message = "Ngày giờ hẹn không được để trống")
    @Future(message = "Ngày giờ hẹn phải ở tương lai")
    private LocalDateTime appointmentDate;

    @NotBlank(message = "Lý do khám không được để trống")
    @Size(max = 500, message = "Lý do khám không vượt quá 500 ký tự")
    private String reason;
}