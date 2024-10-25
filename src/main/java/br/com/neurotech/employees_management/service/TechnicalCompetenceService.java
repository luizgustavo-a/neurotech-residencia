package br.com.neurotech.employees_management.service;

import br.com.neurotech.employees_management.dto.TechnicalCompetenceDto;
import br.com.neurotech.employees_management.dto.TechnicalCompetenceUpdateDto;
import br.com.neurotech.employees_management.model.TechnicalCompetence;
import br.com.neurotech.employees_management.model.exception.TechnicalCompetenceNotFoundException;
import br.com.neurotech.employees_management.repository.TechnicalCompetenceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TechnicalCompetenceService {

    private final TechnicalCompetenceRepository technicalCompetenceRepository;

    public TechnicalCompetenceService(TechnicalCompetenceRepository technicalCompetenceRepository) {
        this.technicalCompetenceRepository = technicalCompetenceRepository;
    }

    public TechnicalCompetence createTechnicalCompetence(TechnicalCompetenceDto technicalCompetenceDto) {
        TechnicalCompetence technicalCompetence = TechnicalCompetenceDto.toModel(technicalCompetenceDto);

        technicalCompetenceRepository.save(technicalCompetence);

        return technicalCompetence;
    }

    public Page<TechnicalCompetence> listAllTechnicalCompetences(Pageable pageable) {
        return technicalCompetenceRepository.findAll(pageable);
    }

    public TechnicalCompetence findTechnicalCompetenceById(Long id) throws TechnicalCompetenceNotFoundException {
        return technicalCompetenceRepository.findById(id)
                .orElseThrow(TechnicalCompetenceNotFoundException::new);
    }

    public TechnicalCompetence updateTechnicalCompetence(TechnicalCompetenceUpdateDto updateDto) throws TechnicalCompetenceNotFoundException {
        TechnicalCompetence technicalCompetence = findTechnicalCompetenceById(updateDto.id());

        if(updateDto.description() != null && !updateDto.description().isEmpty()) {
            technicalCompetence.setDescription(updateDto.description());
        }
        if(updateDto.level() != null && !updateDto.level().isEmpty()) {
            technicalCompetence.setLevel(updateDto.level());
        }

        technicalCompetenceRepository.save(technicalCompetence);

        return technicalCompetence;
    }

    public TechnicalCompetence delete(Long id) throws TechnicalCompetenceNotFoundException {
        TechnicalCompetence technicalCompetence = findTechnicalCompetenceById(id);

        technicalCompetenceRepository.delete(technicalCompetence);

        return technicalCompetence;
    }
}
