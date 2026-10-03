package com.medical.patientservice.mapper;

import com.medical.patientservice.dto.response.PatientResponseDTO;
import com.medical.patientservice.entity.Patient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PatientMapper {

    public PatientResponseDTO toResponseDTO(Patient patient) {
        if (patient == null) return null;
        return PatientResponseDTO.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .medicalHistory(patient.getMedicalHistory())
                .build();
    }

    public List<PatientResponseDTO> toResponseDTOList(List<Patient> patients) {
        if (patients == null) return List.of();
        return patients.stream().map(this::toResponseDTO).toList();
    }
}