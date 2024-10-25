package br.com.neurotech.employees_management.controller;

import br.com.neurotech.employees_management.dto.TechnicalCompetenceDto;
import br.com.neurotech.employees_management.dto.TechnicalCompetenceUpdateDto;
import br.com.neurotech.employees_management.model.exception.TechnicalCompetenceNotFoundException;
import br.com.neurotech.employees_management.service.TechnicalCompetenceService;
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
@RequestMapping("/technical_competence")
public class TechnicalCompetenceController {

    private final TechnicalCompetenceService technicalCompetenceService;

    public TechnicalCompetenceController(TechnicalCompetenceService technicalCompetenceService) {
        this.technicalCompetenceService = technicalCompetenceService;
    }

    @PostMapping
    public ResponseEntity<TechnicalCompetenceDto> createTechnicalCompetence(@RequestBody @Valid TechnicalCompetenceDto creationDto,
                                                                            UriComponentsBuilder builder) {
        var technicalCompetence = technicalCompetenceService.createTechnicalCompetence(creationDto);
        var uri = builder.path("/technical_competence/{id}").buildAndExpand(technicalCompetence.getId()).toUri();

        return ResponseEntity.created(uri).body(new TechnicalCompetenceDto(technicalCompetence));
    }

    @GetMapping
    public ResponseEntity<Page<TechnicalCompetenceDto>> listAllTechnicalCompetences(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        var paginatedTechnicalCertifications = technicalCompetenceService.listAllTechnicalCompetences(pageable)
                .map(TechnicalCompetenceDto::new);

        return ResponseEntity.ok(paginatedTechnicalCertifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicalCompetenceDto> findTechnicalCompetence(@PathVariable Long id) throws TechnicalCompetenceNotFoundException {
        var technicalCompetence = technicalCompetenceService.findTechnicalCompetenceById(id);
        return ResponseEntity.ok(new TechnicalCompetenceDto(technicalCompetence));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<TechnicalCompetenceDto> updateTechnicalCompetence(@RequestBody @Valid TechnicalCompetenceUpdateDto updateDto) throws TechnicalCompetenceNotFoundException {
        var technicalCompetence = technicalCompetenceService.updateTechnicalCompetence(updateDto);

        return ResponseEntity.ok(new TechnicalCompetenceDto(technicalCompetence));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<TechnicalCompetenceDto> deleteTechnicalCompetence(@PathVariable Long id) throws TechnicalCompetenceNotFoundException {
        var technicalCompetence = technicalCompetenceService.delete(id);

        return ResponseEntity.ok(new TechnicalCompetenceDto(technicalCompetence));
    }
}
