package br.com.neurotech.employees_management.controller;

import br.com.neurotech.employees_management.dto.CertificationDto;
import br.com.neurotech.employees_management.dto.CertificationUpdateDto;
import br.com.neurotech.employees_management.model.exception.CertificationNotFoundException;
import br.com.neurotech.employees_management.service.CertificationService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/certification")
public class CertificationController {
    private final CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @PostMapping
    public ResponseEntity<CertificationDto> createCertification(@RequestBody @Valid CertificationDto creationDto,
                                                            UriComponentsBuilder builder) {
        var certification = certificationService.createCertification(creationDto);
        var uri = builder.path("/certification/{id}").buildAndExpand(certification.getId()).toUri();

        return ResponseEntity.created(uri).body(new CertificationDto(certification));
    }

    @GetMapping
    public ResponseEntity<Page<CertificationDto>> listAllCertifications(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        var paginatedCertifications = certificationService.listAllCertifications(pageable)
                .map(CertificationDto::new);

        return ResponseEntity.ok(paginatedCertifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificationDto> findCertification(@PathVariable Long id) throws CertificationNotFoundException {
        var certification = certificationService.findCertificationById(id);
        return ResponseEntity.ok(new CertificationDto(certification));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<CertificationDto> updateCertification(@RequestBody @Valid CertificationUpdateDto updateDto) throws CertificationNotFoundException {
        var certification = certificationService.updateCertification(updateDto);

        return ResponseEntity.ok(new CertificationDto(certification));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<CertificationDto> deleteCertification(@PathVariable Long id) throws CertificationNotFoundException {
        var certification = certificationService.delete(id);

        return ResponseEntity.ok(new CertificationDto(certification));
    }
}
