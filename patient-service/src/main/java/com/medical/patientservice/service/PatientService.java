package com.medical.patientservice.service;

import com.medical.patientservice.dto.request.PatientRequestDTO;
import com.medical.patientservice.dto.response.PatientResponseDTO;
import com.medical.patientservice.entity.Patient;
import com.medical.patientservice.exception.ResourceNotFoundException;
import com.medical.patientservice.mapper.PatientMapper;
import com.medical.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientResponseDTO createPatient(PatientRequestDTO request) {
        Patient patient = Patient.builder()
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .medicalHistory(request.getMedicalHistory())
                .build();

        Patient saved = patientRepository.save(patient);
        log.info("Đã tạo patient id={}, fullName={}", saved.getId(), saved.getFullName());
        return patientMapper.toResponseDTO(saved);
    }

    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "ID", id));
        return patientMapper.toResponseDTO(patient);
    }

    public List<PatientResponseDTO> getAllPatients() {
        return patientMapper.toResponseDTOList(patientRepository.findAll());
    }
}