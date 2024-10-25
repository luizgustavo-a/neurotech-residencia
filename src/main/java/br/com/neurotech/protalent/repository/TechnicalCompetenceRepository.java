package br.com.neurotech.protalent.repository;

import br.com.neurotech.protalent.model.TechnicalCompetence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicalCompetenceRepository extends JpaRepository<TechnicalCompetence, Long> {
}
