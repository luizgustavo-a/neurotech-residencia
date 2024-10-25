package br.com.neurotech.employees_management.service;

import br.com.neurotech.employees_management.dto.CertificationDto;
import br.com.neurotech.employees_management.dto.CertificationUpdateDto;
import br.com.neurotech.employees_management.model.Certification;
import br.com.neurotech.employees_management.model.exception.CertificationNotFoundException;
import br.com.neurotech.employees_management.repository.CertificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;

    public CertificationService(CertificationRepository certificationRepository) {
        this.certificationRepository = certificationRepository;
    }

    public Certification createCertification(CertificationDto certificationDto) {
        Certification certification = CertificationDto.toModel(certificationDto);

        certificationRepository.save(certification);

        return certification;
    }

    public Page<Certification> listAllCertifications(Pageable pageable) {
        return certificationRepository.findAll(pageable);
    }

    public Certification findCertificationById(Long id) throws CertificationNotFoundException {
        return certificationRepository.findById(id)
                .orElseThrow(CertificationNotFoundException::new);
    }

    public Certification updateCertification(CertificationUpdateDto updateDto) throws CertificationNotFoundException {
        Certification certification = findCertificationById(updateDto.id());

        if(updateDto.description() != null && !updateDto.description().isEmpty()) {
            certification.setDescription(updateDto.description());
        }
        if(updateDto.institution() != null && !updateDto.institution().isEmpty()) {
            certification.setInstitution(updateDto.institution());
        }
        if(updateDto.date() != null && !updateDto.date().isEmpty()) {
            certification.setDate(LocalDate.parse(updateDto.date(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        if(updateDto.hours() != null) {
            certification.setHours(updateDto.hours());
        }

        certificationRepository.save(certification);

        return certification;
    }

    public Certification delete(Long id) throws CertificationNotFoundException {
        Certification certification = findCertificationById(id);

        certificationRepository.delete(certification);

        return certification;
    }
}
