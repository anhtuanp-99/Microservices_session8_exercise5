package com.medical.doctorservice.mapper;

import com.medical.doctorservice.dto.response.DoctorResponseDTO;
import com.medical.doctorservice.dto.response.DoctorSummaryDTO;
import com.medical.doctorservice.entity.Doctor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DoctorMapper {

    public DoctorResponseDTO toResponseDTO(Doctor doctor) {
        if (doctor == null) return null;
        return DoctorResponseDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialization(doctor.getSpecialization())
                .experienceYears(doctor.getExperienceYears())
                .email(doctor.getEmail())
                .status(doctor.getStatus())
                .build();
    }

    public DoctorSummaryDTO toSummaryDTO(Doctor doctor) {
        if (doctor == null) return null;
        return DoctorSummaryDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialization(doctor.getSpecialization())
                .build();
    }

    public List<DoctorSummaryDTO> toSummaryDTOList(List<Doctor> doctors) {
        if (doctors == null) return List.of();
        return doctors.stream().map(this::toSummaryDTO).toList();
    }
}