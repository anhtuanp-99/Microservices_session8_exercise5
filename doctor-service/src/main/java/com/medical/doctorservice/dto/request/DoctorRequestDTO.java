package com.medical.doctorservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDTO {

    @NotBlank(message = "Tên bác sĩ không được để trống")
    @Size(max = 100, message = "Tên bác sĩ không vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "Chuyên khoa không được để trống")
    private String specialization;

    @NotNull(message = "Số năm kinh nghiệm không được để trống")
    @Min(value = 0, message = "Số năm kinh nghiệm không được âm")
    private Integer experienceYears;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotNull(message = "Trạng thái không được để trống")
    private Boolean status;
}